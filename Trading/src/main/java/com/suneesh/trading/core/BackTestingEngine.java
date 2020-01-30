package com.suneesh.trading.core;

import com.suneesh.trading.core.calculations.Engine;
import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.core.strategy.CandleFollowWithIncreaseStrategyImplementation;
import com.suneesh.trading.core.strategy.ClosePriceHighLowStrategyImplementation;
import com.suneesh.trading.core.strategy.StrategyImplementationInterface;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.BuyContractParameters;
import com.suneesh.trading.models.requests.BuyContractRequest;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.utils.AutoTradingUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;


public class BackTestingEngine extends Engine {
    private static final Logger logger = LogManager.getLogger();
    private String symbol;

    private DatabaseConnection databaseConnection;
    final int CANDLE_DATA_POINTS=21;
    final int CANDLE_DATA_DELAY_MILLISECONDS=10000;

    public BackTestingEngine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol) {
        super(inputMessageQueue, dbConnection, symbol);
        this.databaseConnection = dbConnection;
        this.symbol = symbol;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public void process(){
        getCalculationUtility().loadAllStrategies();
        logger.info("Loading all Strategy Objects.");

        logger.info("Request sent to Binary Websocket to get last {} candle data points ...");
        getCandleDetailsFromBinaryWS(symbol,CANDLE_DATA_POINTS);

        logger.info("Waiting to receive data from Binary WS ...");
        AutoTradingUtility.sleep(CANDLE_DATA_DELAY_MILLISECONDS);

        logger.info("getting candle data from DB ...");
        List<Map<String,String>> candleDataFromDB = getCalculationUtility().getCandles(Optional.empty(), Optional.empty());
        logger.info("Received {} candle data points", candleDataFromDB.size());

        getCalculateSignals().calculateBollingerBands(candleDataFromDB, Optional.empty());

        int test_run_id = getTestRunId();

        // Simulate different max_steps in the backtesting strategy.
//        for(int i = 1; i <= 10; ++i){

        // Defaulting to 5 steps.
        for(int i = 5; i <= 5; ++i){
            databaseConnection.executeNoResultSet("UPDATE strategy SET max_steps = "+i+" WHERE strategy_name = 'Backtesting strategy 1'");
            logger.info("Setting Backtesting strategy's MAX Step = {}",i);

            simulateAndReport(candleDataFromDB,test_run_id);
            test_run_id++;
        }


        System.exit(-1);
    }

    public void getCandleDetailsFromBinaryWS(String symbol, int candleDataPoints) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setCount(candleDataPoints);
        tickHistoryRequest.setGranularity(60);
        sendRequest(tickHistoryRequest);
    }

    void getCallOrPutFromCandleData(NextTradeDetails nextTradeDetails, Map<String,String> candleData){
        String callOrPutResult = "CALL";
        if(!MapUtils.isEmpty(candleData)) {
            String previousCandleDirection = candleData.get("direction");

            callOrPutResult = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
        }
        nextTradeDetails.setCallOrPut(callOrPutResult);
    }


    private void createDummyTrade(Map<String, String> candleData, Map<String, String> nextCandleData){
        try {
            Utility calculationUtility = getCalculationUtility();
            String currency = calculationUtility.getCurrency();
            if(currency.isEmpty()){
                logger.fatal("FATAL ERROR! No Currency detail found. Cannot book trade.\nExiting application.");
                System.exit(-1);
            }

            Map<String, String> lastTrade = calculationUtility.getLastTrade();
            long lastTradeId = 0L;
            if(!MapUtils.isEmpty(lastTrade)){
                lastTradeId = Long.valueOf(lastTrade.get("identifier"));
            }

            Map<String, String> lastCandle = candleData;
            NextTradeDetails nextTradeDetails = new NextTradeDetails(lastTradeId);

            Strategy strategyToUse = calculationUtility.getStrategyToUse();

            // Get Present Strategy's details for booking trade
            StrategyImplementationInterface strategy = new CandleFollowWithIncreaseStrategyImplementation(databaseConnection, strategyToUse, calculationUtility);
//            StrategyImplementationInterface strategy = new ClosePriceHighLowStrategyImplementation(databaseConnection,  strategyToUse);

            strategy.getCallOrPut(nextTradeDetails, lastCandle);
            strategy.getContractDuration(nextTradeDetails);
            strategy.getNextStepCount(nextTradeDetails, lastTrade);
            strategy.getNextTradeStrategyId(nextTradeDetails, lastTrade);
            strategy.getBidAmount(nextTradeDetails, lastCandle);





            BuyContractParameters parameters = calculationUtility.getParameters(symbol, nextTradeDetails, currency);
            BuyContractRequest buyContractRequest = new BuyContractRequest(new BigDecimal(nextTradeDetails.getAmount()), parameters, nextTradeDetails.getTradeId());

            String tradeInsertStatement = calculationUtility.getTradeDatabaseInsertString(parameters, nextTradeDetails);
//            logger.debug(tradeInsertStatement);

            calculationUtility.getDatabaseConnection().executeNoResultSet(tradeInsertStatement);

//            logger.debug("SKIPPING Sending buy Contract Request as running in BACKTESTING MODE... ");
//            logger.debug("Checking next candle status to set result of the latest trade created now...");
            String presentCandleDirection = candleData.get("direction");
            String nextCandleDirection = nextCandleData.get("direction");

            String tradeResult=null;
            if(!presentCandleDirection.equalsIgnoreCase(nextCandleDirection)){
                tradeResult="FAIL";
            }
            else{
                tradeResult="SUCCESS";
            }

//            if(strategyImplementation.closePriceAtDirectionExtreme(nextTradeDetails, lastCandle)){
//                tradeResult=tradeResult+"INCREASED";
//            }

            String tradeResultString = "UPDATE trade SET result ='"+tradeResult+"', contract_id=identifier*100 ";
            String whereString = " WHERE identifier = "+nextTradeDetails.getTradeId();
            if( (tradeResult.equalsIgnoreCase("SUCCESS") ) ||
                    (tradeResult.equalsIgnoreCase("SUCCESSINCREASED") ) ){
                 tradeResultString = tradeResultString+", amount_won = "+nextTradeDetails.getAmount()*1.95;
            }

            tradeResultString = tradeResultString+whereString;
//            logger.info("UPDATING trade result / amount / contract_id... {}",tradeResultString);
            databaseConnection.executeNoResultSet(tradeResultString);

        }
        catch (Exception e ){
            logger.info("Exception caught while create new trade. {}",e.getMessage());
            e.printStackTrace();
        }

    }


    private void simulateAndReport(List<Map<String, String>> candleDataFromDB, int test_run_id){
        // Checking till Size - 1, as we are going to use 2 rows simultaneously
        for(int i=0;i<candleDataFromDB.size()-1;i++){
            Map<String,String> row = candleDataFromDB.get(i);
            Map<String,String> nextRow = candleDataFromDB.get(i+1);

            createDummyTrade(row, nextRow);
        }
        generateReportFromTradeData(test_run_id);

    }

    private void generateReportFromTradeData(int test_run_id) {
        List<Map<String,String>> distinctStrategyIdList = (List<Map<String,String>>)databaseConnection.executeQuery("SELECT s.identifier, s.max_steps FROM strategy s where identifier in ( SELECT distinct(strategy_id) FROM trade)");
        if (CollectionUtils.isNotEmpty(distinctStrategyIdList)) {
            for(Map<String,String> row : distinctStrategyIdList) {
                writeTradeReportToDB(row, test_run_id);
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


    private void writeTradeReportToDB(Map<String, String> row, int test_run_id){
        String strategyId = row.get("identifier");
        String maxSteps = row.get("max_steps");

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
