package com.suneesh.trading.engine;

import com.google.gson.Gson;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.requests.BuyContractParameters;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.DoubleAccumulator;

@Data
@Slf4j
public class CalculationEngineUtility {
    private DatabaseConnection databaseConnection;
    final static long CONTRACT_DURATION_IN_SECONDS = 60L;
    final static double LOWEST_VALID_BID_AMOUNT = 0.35D;
    final static String ACCOUNT_CURRENCY="AccountCurrency";

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

    Map<String,String> getLastCandle(){
        return (Map<String, String>) (databaseConnection.executeQuery("select * from candle order by identifier desc limit 1")).get(0);
    }

    Map<String,String> getLastTrade(){
        Map<String,String> result = null;
        List<Map<String,String>> list = databaseConnection.executeQuery("select * from trade order by identifier desc limit 1");
        if(!CollectionUtils.isEmpty(list)){
            result = list.get(0);
        }
        return result;
    }

    int getLastStepCount() {
        int stepCount = 0;
        Map<String, String> lastTrade = getLastTrade();
        if(!MapUtils.isEmpty(lastTrade)){
            String previousTradeResult = String.valueOf(lastTrade.get("result"));
            if(previousTradeResult.equalsIgnoreCase("SUCCESS")){
                stepCount =1;
            }

            if(previousTradeResult.equalsIgnoreCase("FAIL")){
                stepCount = Integer.valueOf(lastTrade.get("step_count"));
            }
        }
        return stepCount;
    }

    String getTradeDatabaseInsertString(BuyContractParameters parameters, int stepCount) {
        return
                "INSERT INTO public.trade " +
                        "(bid_amount, call_or_put, symbol, step_count, strategy_id, trade_time, trade_time_string, amount_won) " +
                        " VALUES (" + AutoTradingUtility.quotedString(parameters.getAmount())+", "
                        + AutoTradingUtility.quotedString(parameters.getContractType())+", "
                        + AutoTradingUtility.quotedString(parameters.getSymbol())+", "
                        + AutoTradingUtility.quotedString(stepCount)+", "
                        + AutoTradingUtility.quotedString("1")+","
                        + "extract(epoch from now()) ,"
                        + "now()::timestamp ,"
                        +"0.00 );";
    }

    BuyContractParameters getParameters(String symbol, double bidAmount, String callOrPut, long contractDuration, String currency) {
        String json = "{\n" +
                "  \"amount\": \""+ bidAmount +"\",\n" +
                "  \"basis\": \"stake\",\n" +
                "  \"contract_type\": \""+callOrPut+"\",\n" +
                "  \"currency\": \""+currency+"\",\n" +
                "  \"duration\": \""+contractDuration+"\",\n" +
                "  \"duration_unit\": \"s\",\n" +
                "  \"symbol\": \""+symbol+"\"\n" +
                "}";

        Gson gson = new Gson();
        return gson.fromJson(json, BuyContractParameters.class);
    }

    public long getContractDuration() {
        return CONTRACT_DURATION_IN_SECONDS;
    }

    private double getInitialTradeAmount(){
        // Lowest amount possible.
        double amount = LOWEST_VALID_BID_AMOUNT;
        List<Map<String,String>> result = databaseConnection.executeQuery(
                "select ss.value from strategy_steps ss join strategy s on ( ss.strategy_id = s.identifier ) WHERE s.is_default_strategy=true and ss.step_count=1");
        if(!CollectionUtils.isEmpty(result)){
            Map<String,String> firstRow = result.get(0);
            amount = Double.valueOf(firstRow.get("value"));
        }
        return amount;
    }

    private Map<String,String> getStrategy(int strategy_id){
        return (Map<String, String>) (databaseConnection.executeQuery("select * from strategy WHERE identifier = "+strategy_id)).get(0);
    }

    private Map<String,String> getStrategySteps(int strategy_id){
        return (Map<String, String>) (databaseConnection.executeQuery("select * from strategy_steps WHERE strategy_id = "+strategy_id)).get(0);
    }

    private double getStrategyAmount( int strategyId, int stepCount){
        // Lowest amount possible.
        double amount = LOWEST_VALID_BID_AMOUNT;
        int strategyToUse = strategyId;

        Map<String, String> strategy = getStrategy(strategyId);
        int maxSteps = Integer.valueOf(strategy.get("max_steps"));
        if(stepCount > maxSteps){
            strategyToUse = Integer.valueOf(strategy.get("next_strategy_id_link"));
        }

        log.info("Strategy to Use = {}",strategyToUse);
        Map<String, String> nextStrategySteps = getStrategySteps(strategyToUse);

        nextStrategySteps.entrySet().forEach(e->log.info("STRATEGY_STEPS : {} - {}", e.getKey(),e.getValue()));

        amount = Double.valueOf(nextStrategySteps.get("value"));

        return amount;
    }

    double getBidAmount(int nextStepCount) {
        double amount = LOWEST_VALID_BID_AMOUNT;
        Map<String, String> lastTrade = getLastTrade();
        if(MapUtils.isEmpty(lastTrade)){
            getInitialTradeAmount();
        }
        else{
            int previousStrategy =  Integer.valueOf(lastTrade.get("strategy_id"));
            String previousTradeResult = String.valueOf(lastTrade.get("result"));
            if(previousTradeResult.equalsIgnoreCase("SUCCESS")){
                nextStepCount =1;
            }
            amount = getStrategyAmount(previousStrategy,nextStepCount);
        }
        return amount;
    }

    String getCallOrPut(){
        String callOrPutResult = "CALL";
        Map<String, String> lastCandle = getLastCandle();
        if(!MapUtils.isEmpty(lastCandle)) {
            String previousCandleDirection = lastCandle.get("direction");

            log.debug("************ : {}", previousCandleDirection);
            for (Map.Entry<String, String> entry : lastCandle.entrySet()) {
                log.debug("{} - {} ", entry.getKey(), entry.getValue());
            }

            callOrPutResult = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
        }
        return callOrPutResult;
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
}
