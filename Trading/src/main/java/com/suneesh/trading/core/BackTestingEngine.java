package com.suneesh.trading.core;

import com.suneesh.trading.core.calculations.Engine;
import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.core.strategy.StrategyFactory;
import com.suneesh.trading.core.strategy.StrategyImplementationInterface;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.BuyContractRequest;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.utils.AutoTradingUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;


public class BackTestingEngine extends Engine {
    private static final Logger logger = LogManager.getLogger();
    private String symbol;
    StrategyFactory strategyFactory;
    Utility calculationUtility;
    Boolean isBackTestingMode;

    private DatabaseConnection databaseConnection;
    final int CANDLE_DATA_POINTS=1440;
    final int CANDLE_DATA_DELAY_MILLISECONDS=10000;

    public BackTestingEngine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol, boolean isBackTestingMode) {
        super(inputMessageQueue, dbConnection, symbol, isBackTestingMode);
        this.databaseConnection = dbConnection;
        this.symbol = symbol;
        this.calculationUtility = new Utility(dbConnection);
        this.isBackTestingMode = isBackTestingMode;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public void init(){
        calculationUtility.loadAllStrategies();
        strategyFactory = new StrategyFactory(databaseConnection, calculationUtility, isBackTestingMode);
        getCandleDetailsFromBinaryWS(symbol,CANDLE_DATA_POINTS);
    }

    public void getCandleDetailsFromBinaryWS(String symbol, int candleDataPoints) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setCount(candleDataPoints);
        tickHistoryRequest.setGranularity(60);
        sendRequest(tickHistoryRequest);
    }

    public void process(){
        init();
        logger.info("Waiting to receive data from Binary WS ...");
        AutoTradingUtility.sleep(CANDLE_DATA_DELAY_MILLISECONDS);

        List<Map<String,String>> candleDataFromDB = calculationUtility.getCandles(Optional.empty(), Optional.empty());
        logger.info("Received {} candle data points", candleDataFromDB.size());

        int test_run_id = getTestRunId();

        // Simulate different max_steps in the backtesting strategy.
//        for(int simulate_max_steps_count = 1; simulate_max_steps_count <= 10; ++simulate_max_steps_count){

        // Defaulting to 5 steps.
         for(int simulate_max_steps_count = 5; simulate_max_steps_count <= 5; ++simulate_max_steps_count){
            logger.info("Setting Backtesting strategy's MAX Step = {}",simulate_max_steps_count);

            simulateAndReport(candleDataFromDB, test_run_id, simulate_max_steps_count);
            test_run_id++;
        }

        System.exit(-1);
    }

    private void simulateAndReport(List<Map<String, String>> candleDataFromDB, int test_run_id, int simulate_max_steps_count){
        Engine calculationEngine = new Engine(null, databaseConnection, symbol,isBackTestingMode);
        Strategy backTestingStrategy = calculationUtility.getBackTestingStrategy();
        backTestingStrategy.setMaxSteps(simulate_max_steps_count);

        // Checking till Size - 1, as we are going to use 2 rows simultaneously
        for(int i=0;i<candleDataFromDB.size()-1;i++){
            Map<String,String> row = candleDataFromDB.get(i);
            Map<String,String> nextRow = candleDataFromDB.get(i+1);

            createDummyTrade(calculationEngine, backTestingStrategy, row, nextRow);

        }

        generateReportFromTradeData(test_run_id, simulate_max_steps_count);
    }

    private void createDummyTrade(Engine calculationEngine, Strategy backTestingStrategy, Map<String, String> candleData, Map<String, String> nextCandleData){
        try {
            boolean debug = false;
            String currency = calculationUtility.getCurrency();

            Map<String, String> lastTrade = calculationUtility.getLastTrade();
            long lastTradeId = 0L;
            if(!MapUtils.isEmpty(lastTrade)){
                lastTradeId = Long.valueOf(lastTrade.get("identifier"));
            }

            Map<String, String> lastCandle = candleData;
            NextTradeDetails nextTradeDetails = new NextTradeDetails(lastTradeId);

            // Get Present Strategy's details for booking trade
            StrategyImplementationInterface strategyImplementation = strategyFactory.getStrategyImplementation(backTestingStrategy);

            BuyContractRequest buyContractRequest = calculationEngine.createTrade(backTestingStrategy, strategyImplementation, nextTradeDetails, currency, lastCandle, lastTrade, debug);
            if(buyContractRequest!=null && debug==true){
                logger.info("Dummy Trade Booked.");
            }

            //Get Result of the Dummy booked trade
            getSimulatedTradeResult(candleData, nextCandleData, nextTradeDetails);

        }
        catch (Exception e ){
            logger.info("Exception caught while create new trade. {}",e.getMessage());
            e.printStackTrace();
        }

    }

    private void getSimulatedTradeResult(Map<String, String> candleData, Map<String, String> nextCandleData, NextTradeDetails nextTradeDetails){
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
        if( tradeResult.equalsIgnoreCase("SUCCESS") ){
            tradeResultString = tradeResultString+", amount_won = "+nextTradeDetails.getAmount()*1.95;
        }

        tradeResultString = tradeResultString+whereString;
//            logger.info("UPDATING trade result / amount / contract_id... {}",tradeResultString);
        databaseConnection.executeNoResultSet(tradeResultString);

    }


    private void generateReportFromTradeData(int test_run_id, int simulate_max_steps_count) {
        List<Map<String,String>> distinctStrategyIdList = (List<Map<String,String>>)databaseConnection.executeQuery("SELECT distinct(strategy_id) FROM trade");
        if (CollectionUtils.isNotEmpty(distinctStrategyIdList)) {
            for(Map<String,String> row : distinctStrategyIdList) {
                writeTradeReportToDB(row, test_run_id, simulate_max_steps_count);
            }
        }
    }


    private int getTestRunId(){
        int test_run_id=1;
        List<Map<String,String>> lastTestRunIdResult = (List<Map<String,String>>)databaseConnection.executeQuery("select max(test_run_id) From amount_result");
        if (CollectionUtils.isNotEmpty(lastTestRunIdResult)) {
            Map<String, String> row = lastTestRunIdResult.get(0);
            if(MapUtils.isNotEmpty(row)){
                String query_result = row.get("query_result");
                if(query_result!=null && !query_result.equalsIgnoreCase("null")){
                    test_run_id = Integer.valueOf(query_result) + 1;
                }
            }
        }
        return test_run_id;
    }


    private void writeTradeReportToDB(Map<String, String> row, int test_run_id, int simulate_max_steps_count){
        String strategyId = row.get("strategy_id");
        String maxSteps = String.valueOf(simulate_max_steps_count);

        String totalBidAmountQuery = "select sum(bid_amount) from trade where trade.strategy_id="+strategyId;
        String totalAmountWonQuery = "select sum(amount_won) from trade where trade.strategy_id="+strategyId;
        String totalSuccessfulTradesQuery="select count(*) from trade where result='SUCCESS' AND trade.strategy_id="+strategyId;
        String totalFailedTradesQuery="select count(*) from trade where result='FAIL' AND trade.strategy_id="+strategyId;
        String maxFailedStepsQuery = "select max(step_count) from trade where trade.strategy_id = "+strategyId;

        double totalBidAmount = Double.parseDouble(databaseConnection.getFirstElementFromDBQuery(totalBidAmountQuery));
        double totalAmountWon = Double.parseDouble(databaseConnection.getFirstElementFromDBQuery(totalAmountWonQuery));
        double totalSuccessfulTrades = Double.parseDouble(databaseConnection.getFirstElementFromDBQuery(totalSuccessfulTradesQuery));
        double totalFailedTrades = Double.parseDouble(databaseConnection.getFirstElementFromDBQuery(totalFailedTradesQuery));
        double diff = totalAmountWon - totalBidAmount;
        double totalTrades = totalSuccessfulTrades + totalFailedTrades;
        double maxFailedSteps = Double.parseDouble(databaseConnection.getFirstElementFromDBQuery(maxFailedStepsQuery));

        String insertQuery = "INSERT INTO amount_result (test_run_id, strategy_id, total_bid_amount ,total_amount_won ,net_amount_diff,total_trades ,total_successful_trades, total_failed_trades, max_steps, max_failed_steps) VALUES( ";
        insertQuery = insertQuery +
                test_run_id + ","+
                strategyId + ","+
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
