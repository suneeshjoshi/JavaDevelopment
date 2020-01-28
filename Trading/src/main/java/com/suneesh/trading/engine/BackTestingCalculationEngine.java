package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.BuyContractParameters;
import com.suneesh.trading.models.requests.BuyContractRequest;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


public class BackTestingCalculationEngine extends CalculationEngine{
    private static final Logger logger = LogManager.getLogger();
    private String symbol;

    private DatabaseConnection databaseConnection;
    final int CANDLE_DATA_POINTS=2;
    final int CANDLE_DATA_DELAY_MILLISECONDS=10000;

    public BackTestingCalculationEngine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol) {
        super(inputMessageQueue, dbConnection, symbol);
        this.databaseConnection = dbConnection;
        this.symbol = symbol;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public void getCandleDetailsFromBinaryWS(String symbol, int candleDataPoints) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setCount(candleDataPoints);
        tickHistoryRequest.setGranularity(60);
        sendRequest(tickHistoryRequest);
    }

    private List getCandleDataFromDB(){
        return getDatabaseConnection().executeQuery("Select * from candle order by identifier ASC");
    }

    void getCallOrPutFromCandleData(NextTradeDetails nextTradeDetails, Map<String,String> candleData){
        String callOrPutResult = "CALL";
        if(!MapUtils.isEmpty(candleData)) {
            String previousCandleDirection = candleData.get("direction");

//            logger.debug("************ : {}", previousCandleDirection);
//            for (Map.Entry<String, String> entry : candleData.entrySet()) {
//                logger.debug("{} - {} ", entry.getKey(), entry.getValue());
//            }

            callOrPutResult = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
        }
        nextTradeDetails.setCallOrPut(callOrPutResult);
//        return callOrPutResult;
    }


    private void createDummyTrade(Map<String, String> candleData, Map<String, String> nextCandleData){
        try {
            String currency = calculationEngineUtility.getCurrency();
            if(currency.isEmpty()){
                logger.fatal("FATAL ERROR! No Currency detail found. Cannot book trade.\nExiting application.");
                System.exit(-1);
            }

            Map<String, String> lastTrade = calculationEngineUtility.getLastTrade();
            long lastTradeId = 0L;
            if(!MapUtils.isEmpty(lastTrade)){
                lastTradeId = Long.valueOf(lastTrade.get("identifier"));
            }

            NextTradeDetails nextTradeDetails = new NextTradeDetails(lastTradeId);
            getCallOrPutFromCandleData(nextTradeDetails,candleData);
            calculationEngineUtility.getContractDuration(nextTradeDetails);
            calculationEngineUtility.getNextStepCount(nextTradeDetails, lastTrade);
            calculationEngineUtility.getNextTradeStrategyId(nextTradeDetails, lastTrade);
            calculationEngineUtility.getBidAmount(nextTradeDetails);

            BuyContractParameters parameters = calculationEngineUtility.getParameters(symbol, nextTradeDetails, currency);
            BuyContractRequest buyContractRequest = new BuyContractRequest(new BigDecimal(nextTradeDetails.getAmount()), parameters, nextTradeDetails.getTradeId());

            String tradeInsertStatement = calculationEngineUtility.getTradeDatabaseInsertString(parameters, nextTradeDetails);
            logger.debug(tradeInsertStatement);
            calculationEngineUtility.getDatabaseConnection().executeNoResultSet(tradeInsertStatement);

            logger.info("SKIPPING Sending buy Contract Request as running in BACKTESTING MODE... ");
            logger.info("Checking next candle status to set result of the latest trade created now...");
            String presentCandleDirection = candleData.get("direction");
            String nextCandleDirection = nextCandleData.get("direction");

            String tradeResult=null;
            if(!presentCandleDirection.equalsIgnoreCase(nextCandleDirection)){
                tradeResult="FAIL";
            }
            else{
                tradeResult="SUCCESS";
            }

            String tradeResultString = "UPDATE trade SET result ='"+tradeResult+"', contract_id=identifier*100 ";
            String whereString = " WHERE identifier = "+nextTradeDetails.getTradeId();
            if(tradeResult.equalsIgnoreCase("SUCCESS")){
                 tradeResultString = tradeResultString+", amount_won = "+nextTradeDetails.getAmount()*1.95;
            }

            tradeResultString = tradeResultString+whereString;
            logger.info("UPDATING trade result / amount / contract_id... {}",tradeResultString);
            databaseConnection.executeNoResultSet(tradeResultString);

        }
        catch (Exception e ){
            logger.info("Exception caught while create new trade. {}",e.getMessage());
            e.printStackTrace();
        }

    }


    public void process(){
        logger.info("Request sent to Binary Websocket to get last {} candle data points ...");
        getCandleDetailsFromBinaryWS(symbol,CANDLE_DATA_POINTS);

        logger.info("getting candle data from DB ...");

        logger.info("Waiting to receive data from Binary WS ...");
        AutoTradingUtility.sleep(CANDLE_DATA_DELAY_MILLISECONDS);
        List<Map<String,String>> candleDataFromDB = getCandleDataFromDB();

        logger.info("Received {} candle data points", candleDataFromDB.size());

        // Checking till Size - 1, as we are going to use 2 rows simultaneously
        for(int i=0;i<candleDataFromDB.size()-1;i++){
            Map<String,String> row = candleDataFromDB.get(i);
            Map<String,String> nextRow = candleDataFromDB.get(i+1);
//
//            for(Map.Entry<String,String> entry : row.entrySet()){
//                logger.info("{} - {} ", entry.getKey(), entry.getValue());
//            }

            createDummyTrade(row, nextRow);

        }
        generateReportFromTradeData();

        System.exit(-1);
    }

    private String getFirstElementFromDBQuery(String query) {
        logger.debug("Query = {}", query);
        String result = null;
        List<Map<String,String>> dbResultList = (List<Map<String,String>>)databaseConnection.executeQuery(query);
        if (CollectionUtils.isNotEmpty(dbResultList)) {
            result = dbResultList.get(0).get("query_result");
        }
        return result;
    }

    private void generateReportFromTradeData() {
        List<Map<String,String>> distinctStrategyIdList = (List<Map<String,String>>)databaseConnection.executeQuery("SELECT s.identifier, s.max_steps FROM strategy s where identifier in ( SELECT distinct(strategy_id) FROM trade)");
        if (CollectionUtils.isNotEmpty(distinctStrategyIdList)) {
            distinctStrategyIdList.forEach(row -> {
                insertTradeReport(row);
                updateMaxFailed(row.get("identifier"));


            } );
        }
    }

    private void updateMaxFailed(String strategyId ){
        logger.info("going to update the maxFailedSteps.");

        String maxFailedStepsQuery = "select max(step_count) from trade where trade.strategy_id = "+strategyId;
        double maxFailedSteps = Double.parseDouble(getFirstElementFromDBQuery(maxFailedStepsQuery));

        String updateQuery= "UPDATE amount_result set maxFailedSteps = "+maxFailedSteps+" WHERE strategy_id ="+strategyId;
        logger.info("update Queru = {}", updateQuery);
        databaseConnection.executeNoResultSet(updateQuery);
        logger.info("going to update the maxFailedSteps.");

    }

    private void insertTradeReport(Map<String, String> row){
        String strategyId = row.get("identifier");
        String maxSteps = row.get("max_steps");

        String totalBidAmountQuery = "select sum(bid_amount) as query_result from trade where trade.strategy_id="+strategyId;
        String totalAmountWonQuery = "select sum(amount_won) as query_result from trade where trade.strategy_id="+strategyId;
        String totalSuccessfulTradesQuery="select count(*) as query_result from trade where result='SUCCESS' AND trade.strategy_id="+strategyId;
        String totalFailedTradesQuery="select count(*) as query_result from trade where result='FAIL' AND trade.strategy_id="+strategyId;

//        double maxFailedSteps = Double.parseDouble(getFirstElementFromDBQuery(maxFailedStepsQuery));
        double maxFailedSteps = 0D;
        double totalBidAmount = Double.parseDouble(getFirstElementFromDBQuery(totalBidAmountQuery));
        double totalAmountWon = Double.parseDouble(getFirstElementFromDBQuery(totalAmountWonQuery));
        double totalSuccessfulTrades = Double.parseDouble(getFirstElementFromDBQuery(totalSuccessfulTradesQuery));
        double totalFailedTrades = Double.parseDouble(getFirstElementFromDBQuery(totalFailedTradesQuery));
        double diff = totalAmountWon - totalBidAmount;
        double totalTrades = totalSuccessfulTrades + totalFailedTrades;

        String insertQuery = "INSERT INTO amount_result (strategy_id, total_bid_amount ,total_amount_won ,net_amount_diff,total_trades ,total_successful_trades, total_failed_trades, max_steps, max_failed_steps) VALUES( ";
        insertQuery = insertQuery+strategyId + ","+
                totalBidAmount + ","+
                totalAmountWon + ","+
                diff  + ","+
                totalTrades + ","+
                totalSuccessfulTrades + ","+
                totalFailedTrades + "," +
                maxSteps + "," +
                maxFailedSteps + ");";

        databaseConnection.executeNoResultSet(insertQuery);
    }
}
