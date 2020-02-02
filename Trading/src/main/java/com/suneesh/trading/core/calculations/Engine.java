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
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public class Engine extends AbstractCommandGenerator {
    private static final Logger logger = LogManager.getLogger();
    private String symbol;
    DatabaseConnection databaseConnection;
    Utility calculationUtility;
    Signals calculateSignals;
    StrategyFactory strategyFactory;
    Boolean isBackTestingMode;

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

        logger.info("Sleeping for the start of next minute.");
        calculationUtility.sleepTillStartOfNextMinuteMinusSeconds(5);

        logger.info("\n\n");
        logger.info("*************************************************************************************************************************************");
        logger.info("*** Sleeping for another minute to stabalise last candle values, and to ensure its recorded completely and accurately for trading. ***");
        logger.info("*************************************************************************************************************************************\n\n");
//        calculationEngineUtility.sleepTillStartOfNextMinute();

        // Keep booking trades
        while(true) {
            logger.info("Going to send trade {}...",tradeCount);
            createAndSendTrade();
            logger.info("Trade {} sent, waiting for the trade to be completed...", tradeCount);

            /// waiting till last trade is completed.
            while(calculationUtility.waitToBookNextTrade()){

                sendProposalOpenContract();
//                calculationUtility.checkDeltaPercentageToCloseTrade();

                SellContractRequest sellContractRequest = calculationUtility.checkPotentialProfit();
                if(sellContractRequest!=null){
                    sendRequest(sellContractRequest);
                    calculationUtility.sleepTillStartOfNextMinuteMinusSeconds(5);
                    continue;
                }
                else {
                    AutoTradingUtility.sleep(2000);
                }
            }
            logger.info("Trade {} completed.",tradeCount);
            tradeCount++;
        }
    }

    private void sendProposalOpenContract() {
        List<HashMap<String,String>> openContractsList = databaseConnection.executeQuery("select contract_id From trade where result ='OPEN'");
        openContractsList.parallelStream().forEach(openContract->{
            String contract_id = openContract.get("contract_id");
            logger.info("Sending ProposalOpenContract request for {}", contract_id);
            sendRequest(new ProposalOpenContractRequest(Long.valueOf(contract_id), true) );
            databaseConnection.executeNoResultSet("UPDATE trade SET result ='PROPOSAL_OPEN_CONTRACT_SENT' WHERE contract_id = " +AutoTradingUtility.quotedString(contract_id));
        });
    }


    private void createAndSendTrade(){
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

            // Create Trade
            logger.info("Creating trade ... ");
            BuyContractRequest buyContractRequest = createTrade(strategyToUse, nextTradeDetails, currency, lastCandle, lastTrade, true);

            logger.info("Sending buy Contract Request ... ");
            // Send trade
            sendRequest(buyContractRequest);
        }
        catch(Exception e) {
            logger.info("Exception caught while creating new trade. {}",e.getMessage());
            e.printStackTrace();
        }
    }

    public BuyContractRequest createTrade(Strategy strategyToUse, NextTradeDetails nextTradeDetails, String currency, Map<String, String> lastCandle, Map<String, String> lastTrade, boolean debug) {
        BuyContractRequest buyContractRequest = null;
        try{

            // Get Present Strategy's details for booking trade
            StrategyImplementationInterface strategy = strategyFactory.getStrategyImplementation(strategyToUse);
            strategy.getCallOrPut(nextTradeDetails, lastCandle);
            strategy.getContractDuration(nextTradeDetails);
            strategy.getNextStepCount(nextTradeDetails, lastTrade);
            strategy.getNextTradeStrategyId(nextTradeDetails, lastTrade);
            strategy.getBidAmount(nextTradeDetails, lastCandle);
            BuyContractParameters parameters = calculationUtility.getParameters(symbol, nextTradeDetails, currency);
            nextTradeDetails.setStrikePrice(calculationUtility.getTickForEpochTime(Optional.empty(),symbol));
            String tradeInsertStatement = calculationUtility.getTradeDatabaseInsertString(parameters, nextTradeDetails);
            if(debug) {
                logger.debug(tradeInsertStatement);
            }
            databaseConnection.executeNoResultSet(tradeInsertStatement);
            buyContractRequest = new BuyContractRequest(new BigDecimal(nextTradeDetails.getAmount()), parameters, nextTradeDetails.getTradeId());

        }
        catch (Exception e ){
            logger.info("Exception caught while create new trade. {}",e.getMessage());
            e.printStackTrace();
        }
        return buyContractRequest;
    }


}
