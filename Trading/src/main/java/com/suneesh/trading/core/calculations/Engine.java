package com.suneesh.trading.core.calculations;

import com.suneesh.trading.core.strategy.StrategyFactory;
import com.suneesh.trading.core.strategy.StrategyImplementationInterface;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.core.AbstractCommandGenerator;
import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.models.Strategy;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Slf4j
public class Engine extends AbstractCommandGenerator {
//    private static final log.log.= LogManager.getlog.);
    private String symbol;
    DatabaseConnection databaseConnection;
    Utility calculationUtility;
    Signals calculateSignals;
    StrategyFactory strategyFactory;
    Boolean isBackTestingMode;

    final int TRADE_SIGNAL_CHECKS_DELAY_IN_MILLISECONDS=500;
    final int NUMBER_OF_INITIAL_CANDLES_TO_READ=25;
    final int CANDLE_60_SECOND_GRANULARITY=60;
    final int CANDLE_300_SECOND_GRANULARITY=300;

    public Engine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol, Boolean isBackTestingMode) {
        super(inputMessageQueue);
        this.databaseConnection = dbConnection;
        this.symbol = symbol;
        this.calculationUtility = new Utility(dbConnection);
        this.calculateSignals = new Signals(dbConnection);
        this.isBackTestingMode = isBackTestingMode;
        this.strategyFactory = new StrategyFactory(databaseConnection, calculationUtility,isBackTestingMode);
    }

    public Signals getCalculateSignals() {
        return calculateSignals;
    }

    public Utility getCalculationUtility() {
        return calculationUtility;
    }

    public void init(){
        getTickDetail(symbol);
        getCandleDetailsFromBinaryWS(symbol);
    }

    public void getTickDetail(String symbol) {
        sendRequest(new TickRequest(symbol));
    }

    public void getCandleDetailsFromBinaryWS(String symbol) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setSubscribe(1);
        tickHistoryRequest.setCount(NUMBER_OF_INITIAL_CANDLES_TO_READ);
        tickHistoryRequest.setGranularity(CANDLE_60_SECOND_GRANULARITY);
        sendRequest(tickHistoryRequest);
    }

    public void process(){
        int tradeCount =1;

        init();

        log.info("Sleeping for the start of next minute.");
        calculationUtility.sleepTillStartOfNextMinuteMinusSeconds(5);

        log.info("\n\n");
        log.info("*************************************************************************************************************************************");
        log.info("*** Sleeping for another minute to stabilise last candle values, and to ensure its recorded completely and accurately for trading. ***");
        log.info("*************************************************************************************************************************************\n\n");
//        calculationEngineUtility.sleepTillStartOfNextMinute();

        // Keep booking trades
        while(true) {
            if (createAndSendTrade(tradeCount)) {

                // waiting till last trade is completed.
                while (calculationUtility.waitToBookNextTrade()) {

                    sendProposalOpenContract();

                    SellContractRequest sellContractRequest = calculationUtility.checkPotentialProfit();
                    if (sellContractRequest != null) {
                        sellContract(sellContractRequest, tradeCount);
                    } else {
                        AutoTradingUtility.sleep(2000);
                    }
                }
                log.info("Trade {} : COMPLETED.", tradeCount);
                tradeCount++;
            }
            else{
                log.info("Trade booking Signal not received. Sleeping for {} milliseconds",TRADE_SIGNAL_CHECKS_DELAY_IN_MILLISECONDS);
                AutoTradingUtility.sleep(TRADE_SIGNAL_CHECKS_DELAY_IN_MILLISECONDS);
            }
        }

    }

    private void sellContract(SellContractRequest sellContractRequest, int tradeCount){
        log.info("Going to sell Trade number {}, as Threshold value reached.", tradeCount);

        List<Map<String, String>> proposalOpenContractSent = calculationUtility.getTradesByResult("PROPOSAL_OPEN_CONTRACT_SENT");
        if(CollectionUtils.isNotEmpty(proposalOpenContractSent)){
            // Get list of all trades with result PROPOSAL_OPEN_CONTRACT_SENT
            List<String> contractIds = proposalOpenContractSent.stream().map(ele -> ele.get("contract_id")).collect(Collectors.toList());

            // If present contract is defined as PROPOSAL_OPEN_CONTRACT_SENT send the SELL_REQUEST and will then be changing its Result state too.
            if(contractIds.contains(String.valueOf(sellContractRequest.getContractId()))) {
                sendRequest(sellContractRequest);
                String resultStates =AutoTradingUtility.quotedString("PROPOSAL_OPEN_CONTRACT_SENT") +","+AutoTradingUtility.quotedString("OPEN");

                calculationUtility.updateTradeResult(Optional.empty(), Optional.ofNullable(sellContractRequest.getContractId()), Optional.ofNullable(resultStates), Optional.ofNullable("SELL_CONTRACT_SENT"), true);
                log.info("Going to sleep till the remaining time in the present candle");
                calculationUtility.sleepTillStartOfNextMinuteMinusSeconds(5);

            }
        }
    }

    private void sendSellContractRequest(SellContractRequest sellContractRequest) {
        databaseConnection.executeNoResultSet("UPDATE ");
    }

    private void sendProposalOpenContract() {
        List<HashMap<String,String>> openContractsList = databaseConnection.executeQuery("select contract_id From trade where result ='OPEN'");
        openContractsList.parallelStream().forEach(openContract->{
            String contract_id = openContract.get("contract_id");
            log.info("Sending ProposalOpenContract request for {}", contract_id);
            sendRequest(new ProposalOpenContractRequest(Long.valueOf(contract_id), true) );
            databaseConnection.executeNoResultSet("UPDATE trade SET result ='PROPOSAL_OPEN_CONTRACT_SENT' WHERE contract_id = " +AutoTradingUtility.quotedString(contract_id));
        });
    }


    private boolean createAndSendTrade(int tradeCount){
        boolean bookTrade = false;
        try {
            long lastTradeId = 0L;
            Map<String, String> lastTrade = calculationUtility.getLastTrade();
            if (!MapUtils.isEmpty(lastTrade)) {
                lastTradeId = Long.valueOf(lastTrade.get("identifier"));
            }

            String currency = calculationUtility.getCurrency();
            Map<String, String> lastCandle = calculationUtility.getLastCandle();
            NextTradeDetails nextTradeDetails = new NextTradeDetails(lastTradeId);
            Strategy strategyToUse = calculationUtility.getStrategyToUse();

            // Get Present Strategy's details for booking trade
            StrategyImplementationInterface strategyImplementation = strategyFactory.getStrategyImplementation(strategyToUse);

            log.info("Checking if we should be booking a trade using Strategy = {}", strategyImplementation.getClass().getSimpleName());
            // Check if signal received for generating trade as per the given strategy
            bookTrade = strategyImplementation.bookTrade(lastTrade, lastCandle);
            if(bookTrade){
                // Create Trade
                log.info("Trade {} : SIGNALLED",tradeCount);
                BuyContractRequest buyContractRequest = createTrade(strategyToUse, strategyImplementation, nextTradeDetails, currency, lastCandle, lastTrade, true);

                log.info("Trade {} : CREATED",tradeCount);
                // Send trade
                sendRequest(buyContractRequest);
                log.info("Trade {} : SENT",tradeCount);
            }

        }
        catch(Exception e) {
            log.info("Exception caught while creating new trade. {}",e.getMessage());
            e.printStackTrace();
        }
        return bookTrade;
    }

    public BuyContractRequest createTrade(Strategy strategyToUse, StrategyImplementationInterface strategyImplementation, NextTradeDetails nextTradeDetails, String currency, Map<String, String> lastCandle, Map<String, String> lastTrade, boolean debug) {
        BuyContractRequest buyContractRequest = null;
        try{

            strategyImplementation.getCallOrPut(nextTradeDetails, lastCandle);
            strategyImplementation.getContractDuration(nextTradeDetails);
            strategyImplementation.getNextStepCount(nextTradeDetails, lastTrade);
            strategyImplementation.getNextTradeStrategyId(nextTradeDetails, lastTrade);
            strategyImplementation.getBidAmount(nextTradeDetails, lastCandle);
            BuyContractParameters parameters = calculationUtility.getParameters(symbol, nextTradeDetails, currency);
            nextTradeDetails.setStrikePrice(calculationUtility.getTickForEpochTime(Optional.empty(),symbol));
            String tradeInsertStatement = calculationUtility.getTradeDatabaseInsertString(parameters, nextTradeDetails);
            if(debug) {
                log.debug(tradeInsertStatement);
            }
            databaseConnection.executeNoResultSet(tradeInsertStatement);
            buyContractRequest = new BuyContractRequest(new BigDecimal(nextTradeDetails.getAmount()), parameters, nextTradeDetails.getTradeId());

        }
        catch (Exception e ){
            log.info("Exception caught while create new trade. {}",e.getMessage());
            e.printStackTrace();
        }
        return buyContractRequest;
    }


}
