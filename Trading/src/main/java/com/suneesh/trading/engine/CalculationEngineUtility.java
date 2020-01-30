package com.suneesh.trading.engine;

import com.google.gson.Gson;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.engine.technical.BollingerBand;
import com.suneesh.trading.models.requests.BuyContractParameters;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Data
@Slf4j
public class CalculationEngineUtility {


    private DatabaseConnection databaseConnection;
//    final static long CONTRACT_DURATION_IN_SECONDS = 60L;
    final static long CONTRACT_DURATION_IN_SECONDS = 58L;
    final static double INITIAL_BID_AMOUNT = 1.00D;
    final static String ACCOUNT_CURRENCY="AccountCurrency";
    final int BOLLINGER_BAND_DATA_COUNT=20;

    public CalculationEngineUtility(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    String getCurrency(){
        String applicationMode = AutoTradingUtility.getPropertyFromPropertyFile("ApplicationMode");
        String currencyProperty = applicationMode+ACCOUNT_CURRENCY;
        return (AutoTradingUtility.getPropertyFromPropertyFile(currencyProperty));
    }

    void sleepTillStartOfNextMinute(){
        int secondsToNextMinute = 60- Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)%60);
        AutoTradingUtility.sleep(secondsToNextMinute*1000);
    }

    List<Map<String,String>> getAllCandles(){
        return getCandles(Optional.empty(), Optional.empty());
    }

    Map<String,String> getLastCandle(){
        Map<String,String> result =null;
        List<Map<String, String>> allCandles = getCandles(Optional.of("DESC"), Optional.of(1));
        if(CollectionUtils.isNotEmpty(allCandles)){
            result = allCandles.get(0);
        }
        return result;
    }

    List<Map<String,String>> getCandles(Optional<String> sortOrder, Optional<Integer> numberOfRows){
        String querySortOrder = sortOrder.isPresent()?sortOrder.get():"ASC";
        int queryResultLimit = numberOfRows.isPresent()?numberOfRows.get():99999;

        return databaseConnection.executeQuery("select * from candle order by identifier "+querySortOrder+" limit "+queryResultLimit);
    }



    Map<String,String> getLastTrade(){
        Map<String,String> result = null;
        List<Map<String,String>> list = databaseConnection.executeQuery("select * from trade order by identifier desc limit 1");
        if(!CollectionUtils.isEmpty(list)){
            result = list.get(0);
        }
        return result;
    }

    void getNextStepCount(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade) {
        int stepCount = 1;
//        Map<String, String> lastTrade = getLastTrade();

        if(!MapUtils.isEmpty(lastTrade)){
            String previousTradeResult = String.valueOf(lastTrade.get("result"));
            int previousTradeStrategyID =  Integer.valueOf(lastTrade.get("strategy_id"));
            int previousStepCount = Integer.parseInt(lastTrade.get("step_count"));
            Map<String, String> previousTradeStrategy = getStrategy(previousTradeStrategyID,false);
            boolean reset_step_count_on_success = previousTradeStrategy.getOrDefault("reset_step_count_on_success", "True").equalsIgnoreCase("True");

            if(previousTradeResult.equalsIgnoreCase("SUCCESS") ){
                if(reset_step_count_on_success) {
                    stepCount = 1;
                }
                else{
                    stepCount=previousStepCount+1;
                }
            }

            if(previousTradeResult.equalsIgnoreCase("FAIL")){
//                stepCount = Integer.valueOf(lastTrade.get("step_count"));
                stepCount=previousStepCount+1;
            }
        }
        nextTradeDetails.setNextStepCount(stepCount);
    }

    String getTradeDatabaseInsertString(BuyContractParameters parameters, NextTradeDetails nextTradeDetails) {
        return
                "INSERT INTO public.trade " +
                        "(bid_amount, call_or_put, symbol, step_count, strategy_id, trade_time, trade_time_string, amount_won) " +
                        " VALUES (" + AutoTradingUtility.quotedString(parameters.getAmount())+", "
                        + AutoTradingUtility.quotedString(parameters.getContractType())+", "
                        + AutoTradingUtility.quotedString(parameters.getSymbol())+", "
                        + AutoTradingUtility.quotedString(nextTradeDetails.getNextStepCount())+", "
                        + AutoTradingUtility.quotedString(nextTradeDetails.getStrategyId())+","
                        + "extract(epoch from now()) ,"
                        + "now()::timestamp ,"
                        +"0.00 );";
    }

    public void getContractDuration(NextTradeDetails nextTradeDetails) {
        nextTradeDetails.setContractDuration(CONTRACT_DURATION_IN_SECONDS);
    }

    private double getInitialTradeAmount(){
        // Lowest amount possible.
        double amount = INITIAL_BID_AMOUNT;
        List<Map<String,String>> result = databaseConnection.executeQuery(
                "select ss.value from strategy_steps ss join strategy s on ( ss.strategy_id = s.identifier ) WHERE s.is_default_strategy=true and ss.step_count=1");
        if(!CollectionUtils.isEmpty(result)){
            Map<String,String> firstRow = result.get(0);
            amount = Double.valueOf(firstRow.get("value"));
        }
        return amount;
    }

    private Map<String,String> getStrategy(int strategyId, boolean defaultStrategy){
        Map<String, String>result = null;
        String queryToExecute="";
        if(defaultStrategy){
            queryToExecute="select * From strategy where strategy.is_default_strategy = true";
        }
        else{
            queryToExecute="select * from strategy WHERE identifier = "+strategyId;
        }

//        log.debug("getStrategy = {}",queryToExecute);
        List<Map<String, String>> list = (List<Map<String, String>>) databaseConnection.executeQuery(queryToExecute);
        if( CollectionUtils.isEmpty(list)){
            log.error("Unable to find Strategy for query = {}",queryToExecute);
        }
        else{
            result= list.get(0);
        }
        return result;
    }


    private Map<String,String> getStrategySteps(int strategyId, int stepCount){
        Map<String, String>result = null;
        String queryToExecute="select * from strategy_steps WHERE strategy_id = "+strategyId+" AND step_count = "+stepCount;

//        log.debug("getStrategySteps = {}",queryToExecute);
        List<Map<String, String>> list = (List<Map<String, String>>) databaseConnection.executeQuery(queryToExecute);
        if( CollectionUtils.isEmpty(list)){
            log.error("Unable to find StrategySteps for query = {}",queryToExecute);
        }
        else{
            result= list.get(0);
        }
        return result;

    }


    void getNextTradeStrategyId(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade) {
        int nextTradeStrategyId = -1;

        if( !MapUtils.isEmpty(lastTrade)) {
            // Lowest amount possible.
            int previousTradeStrategyId = Integer.valueOf(lastTrade.getOrDefault("strategy_id","1"));

            Map<String, String> strategy = getStrategy(previousTradeStrategyId, false);
            int maxSteps = Integer.valueOf(strategy.get("max_steps"));
            if (nextTradeDetails.getNextStepCount() > maxSteps) {
                nextTradeStrategyId = Integer.valueOf(strategy.get("next_strategy_id_link"));

                // Resetting step_count to 1
                nextTradeDetails.setNextStepCount(1);
            }
            else{
                nextTradeStrategyId = previousTradeStrategyId;
            }
        }
        else{
            Map<String, String> strategy = getStrategy(-1, true);
            nextTradeStrategyId = Integer.valueOf(strategy.get("identifier"));

        }
        nextTradeDetails.setStrategyId(nextTradeStrategyId);
    }

    void getBidAmount(NextTradeDetails nextTradeDetails) {
        double amount = INITIAL_BID_AMOUNT;
        Map<String, String> lastTrade = getLastTrade();
        if(MapUtils.isEmpty(lastTrade)){
            getInitialTradeAmount();
        }
        else{
            Map<String, String> nextStrategySteps = getStrategySteps(nextTradeDetails.getStrategyId(), nextTradeDetails.getNextStepCount());
//            nextStrategySteps.entrySet().forEach(e->log.info("STRATEGY_STEPS : {} - {}", e.getKey(),e.getValue()));
            amount = Double.valueOf(nextStrategySteps.get("value"));
        }
        nextTradeDetails.setAmount(amount);
    }

    void getCallOrPut(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle){
        String callOrPutResult = "CALL";
        if(!MapUtils.isEmpty(lastCandle)) {
            String previousCandleDirection = lastCandle.get("direction");

//            log.debug("************ : {}", previousCandleDirection);
//            for (Map.Entry<String, String> entry : lastCandle.entrySet()) {
//                log.debug("{} - {} ", entry.getKey(), entry.getValue());
//            }

            callOrPutResult = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
        }
        nextTradeDetails.setCallOrPut(callOrPutResult);
//        return callOrPutResult;
    }

    boolean closePriceAtDirectionExtreme(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle){
        boolean result = false;
        final int maxStepCountToApplyFactor=3;
        // Here i am testing the logic to see if the close value of the
        // candle is high or low , then the chance of next candle going in similar direction is higher
        if(lastCandle!=null){
            double INCREASED_MOMENTUM_FACTOR= 2.5;
            double high = Double.parseDouble(lastCandle.get("high"));
            double low = Double.parseDouble(lastCandle.get("low"));
            double open = Double.parseDouble(lastCandle.get("open"));
            double close = Double.parseDouble(lastCandle.get("close"));
            String direction = lastCandle.get("direction");

            if(direction.equalsIgnoreCase("UP")){
                if(close==high && nextTradeDetails.nextStepCount<=maxStepCountToApplyFactor){
                    log.info(lastCandle.toString());
                    result= true;
                }
            }

            if(direction.equalsIgnoreCase("DOWN")){
                if(close==low && nextTradeDetails.nextStepCount<=maxStepCountToApplyFactor){
                    log.info(lastCandle.toString());
                    result= true;
                }
            }
        }
        return result;
    }

    boolean waitToBookNextTrade(){
        boolean result = true;
        Map<String, String> lastTrade = getLastTrade();
        if(!MapUtils.isEmpty(lastTrade)){
            String tradeResult = lastTrade.get("result");

            if(!tradeResult.isEmpty() && tradeResult!=null )
                if( tradeResult.equalsIgnoreCase("SUCCESS") ||
                        tradeResult.equalsIgnoreCase("FAIL") ){
                result=false;
            }
        }
        return result;
    }


    BuyContractParameters getParameters(String symbol, NextTradeDetails nextTradeDetails, String currency) {
        String json = "{\n" +
                "  \"amount\": \""+ nextTradeDetails.getAmount() +"\",\n" +
                "  \"basis\": \"stake\",\n" +
                "  \"contract_type\": \""+nextTradeDetails.getCallOrPut()+"\",\n" +
                "  \"currency\": \""+currency+"\",\n" +
                "  \"duration\": \""+nextTradeDetails.getContractDuration()+"\",\n" +
                "  \"duration_unit\": \"s\",\n" +
                "  \"symbol\": \""+symbol+"\"\n" +
                "}";

        Gson gson = new Gson();
        return gson.fromJson(json, BuyContractParameters.class);
    }

    public void calculateBollingerBands(List<Map<String, String>> candleDataFromDB, Optional<String> candleSource) {
        String source = candleSource.isPresent()?candleSource.get():"";

        if(CollectionUtils.isNotEmpty(candleDataFromDB)) {
            if(candleDataFromDB.size()>=BOLLINGER_BAND_DATA_COUNT) {
                int lastElementCounter = candleDataFromDB.size();
                if(source.equalsIgnoreCase("CANDLES")) {
                    lastElementCounter = candleDataFromDB.size()-1;
                }

                for(int i=lastElementCounter;i>=BOLLINGER_BAND_DATA_COUNT; --i){
                    int start = i-BOLLINGER_BAND_DATA_COUNT ;
                    int end = i;
                    log.info("start : end = {} : {} ", start, end);

                    List<Map<String, String>> candleSubList = candleDataFromDB.subList(start, end);

                    BollingerBand bollingerBand = new BollingerBand(candleSubList);
                    bollingerBand.calculate();
                    log.info(bollingerBand.toString());

                    bollingerBand.writeToDB(databaseConnection);
                }
            }
        }
        log.info("Bollinger Band calculations done.");
    }

}
