package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.utils.AutoTradingUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;

public class CalculationEngine extends AbstractCommandGenerator {
    private static final Logger logger = LogManager.getLogger();
    private String symbol;
    CalculationEngineUtility calculationEngineUtility;

    public CalculationEngine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol) {
        super(inputMessageQueue);
        this.symbol = symbol;
        this.calculationEngineUtility = new CalculationEngineUtility(dbConnection);
    }

    public void getTickDetail(String symbol) {
        sendRequest(new TickRequest(symbol));
    }

    public void getCandleDetails(String symbol) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setSubscribe(1);
        tickHistoryRequest.setCount(1);
        tickHistoryRequest.setGranularity(60);
        sendRequest(tickHistoryRequest);
    }

    public void process(){
        int tradeCount =1;
        getTickDetail(symbol);
        getCandleDetails(symbol);
        logger.info("Sleeping for the start of next minute.");
        calculationEngineUtility.sleepTillStartOfNextMinute();

        logger.info("\n\n");
        logger.info("*************************************************************************************************************************************");
        logger.info("*** Sleeping for another minute to stablise last candle values, and to ensure its recorded completely and accurately for trading. ***");
        logger.info("*************************************************************************************************************************************\n\n");
//        calculationEngineUtility.sleepTillStartOfNextMinute();

        // Keep booking trades
        while(true) {
            logger.info("Going to send trade {}...",tradeCount++);
            createAndSendTrade();
            logger.info("Trade sent. sleeping for 60 seconds");

            /// waiting till last trade is completed.
            while(calculationEngineUtility.waitToBookNextTrade()){
                AutoTradingUtility.sleep(1000);
            }
        }

    }

    private void createAndSendTrade(){
        try {
            String currency = calculationEngineUtility.getCurrency();
            if(currency.isEmpty()){
                logger.fatal("FATAL ERROR! No Currency detail found. Cannot book trade.\nExiting application.");
                System.exit(-1);
            }

            String callOrPut = calculationEngineUtility.getCallOrPut();
            long contractDuration = calculationEngineUtility.getContractDuration();
            int nextStepCount = calculationEngineUtility.getNextStepCount();
            double bidAmount = calculationEngineUtility.getBidAmount(nextStepCount);

            BuyContractParameters parameters = calculationEngineUtility.getParameters(symbol, bidAmount, callOrPut, contractDuration, currency);
            BuyContractRequest buyContractRequest = new BuyContractRequest(new BigDecimal(bidAmount), parameters);

            String tradeInsertStatement = calculationEngineUtility.getTradeDatabaseInsertString(parameters, nextStepCount);
            logger.debug(tradeInsertStatement);
            calculationEngineUtility.getDatabaseConnection().executeNoResultSet(tradeInsertStatement);

            logger.info("Sending buy Contract Request ... ");
            sendRequest(buyContractRequest);
        }
        catch (Exception e ){
            logger.info("Exception caught while create new trade. {}",e.getMessage());
            e.printStackTrace();
        }
    }

}
