package com.suneesh.trading.core.calculations;

import com.google.gson.Gson;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.models.Strategy;
import com.suneesh.trading.models.StrategySteps;
import com.suneesh.trading.models.requests.BuyContractParameters;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Slf4j
public class Utility {
    private DatabaseConnection databaseConnection;
    final static String ACCOUNT_CURRENCY="AccountCurrency";
    List<Strategy> allStrategies;

    public Utility(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public String getCurrency(){
        String applicationMode = AutoTradingUtility.getPropertyFromPropertyFile("ApplicationMode");
        String currencyProperty = applicationMode+ACCOUNT_CURRENCY;
        String currency = AutoTradingUtility.getPropertyFromPropertyFile(currencyProperty);
        if(currency.isEmpty()){
            log.error("FATAL ERROR! No Currency detail found. Cannot book trade.\nExiting application.");
            System.exit(-1);
        }
        return currency;
    }

    void sleepTillStartOfNextMinute(){
        int secondsToNextMinute = 60- Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)%60);
        AutoTradingUtility.sleep(secondsToNextMinute*1000);
    }

    Map<String,String> getLastCandle(){
        Map<String,String> result =null;
        List<Map<String, String>> allCandles = getCandles(Optional.of("DESC"), Optional.of(1));
        if(CollectionUtils.isNotEmpty(allCandles)){
            result = allCandles.get(0);
        }
        return result;
    }

    public List<Map<String,String>> getCandles(Optional<String> sortOrder, Optional<Integer> numberOfRows){
        String querySortOrder = sortOrder.isPresent()?sortOrder.get():"ASC";
        int queryResultLimit = numberOfRows.isPresent()?numberOfRows.get():99999;

        return databaseConnection.executeQuery("select * from candle order by identifier "+querySortOrder+" limit "+queryResultLimit);
    }

    public String getTradeDatabaseInsertString(BuyContractParameters parameters, NextTradeDetails nextTradeDetails) {
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



    public Map<String,String> getLastTrade(){
        Map<String,String> result = null;
        List<Map<String,String>> list = databaseConnection.executeQuery("select * from trade order by identifier desc limit 1");
        if(!CollectionUtils.isEmpty(list)){
            result = list.get(0);
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


    public BuyContractParameters getParameters(String symbol, NextTradeDetails nextTradeDetails, String currency) {
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


    private BigDecimal getTickForEpochTime(long epochTime){
        BigDecimal quote= new BigDecimal(-1);
        List<Map<String,String>>  tickResult = databaseConnection.executeQuery(
                "select * from tick t where t.epoch = " + String.valueOf(epochTime));
        if(!CollectionUtils.isEmpty(tickResult)){
            Map<String, String> tickRow = tickResult.get(0);
            quote = new BigDecimal(tickRow.get("quote"));
        }
        return quote;
    }

    public void loadAllStrategies(){
        Gson gson = new Gson();
        List<String> jsonResultDBQuery = databaseConnection.getJsonResultDBQuery("select * from strategy");
        if(CollectionUtils.isNotEmpty(jsonResultDBQuery)) {
            allStrategies = jsonResultDBQuery.stream().map(ele -> gson.fromJson(ele, Strategy.class)).collect(Collectors.toList());

            List<String> jsonResultDBQuery2 = databaseConnection.getJsonResultDBQuery("select * from strategy_steps");
            List<StrategySteps> allStrategySteps = jsonResultDBQuery2.stream().map(ele -> gson.fromJson(ele, StrategySteps.class)).collect(Collectors.toList());

            allStrategies.stream().forEach(
                    strategy -> {
                        strategy.setStepValueMap(allStrategySteps.stream().filter(f -> f.getStrategyId() == strategy.getIdentifier()).collect(Collectors.toList()));
                    }
            );
        }
    }

    public Strategy getStrategyToUse() {
        List<Strategy> activeStrategies = allStrategies.stream().filter(s -> s.isActiveStrategy()).collect(Collectors.toList());
        if(activeStrategies.size()>1){
            log.error("ERROR! Multiple strategies marked as active. ONLY ONE STRATEGY can be active at a given time.");
            System.exit(-2);
        }

        Strategy strategy = activeStrategies.get(0);

        if (strategy==null){
            strategy=getStrategy(Optional.empty(), Optional.of(true));
        }
        return strategy;
    }


    public Strategy getStrategy(Optional<Long> strategyId, Optional<Boolean> defaultStrategy){
        Strategy strategy= null;
        final int INVALID_STRATEGY_ID = -1;

        boolean defaultStrategyToUse=defaultStrategy.isPresent()?defaultStrategy.get():false;
        long strategyIdToUse=strategyId.isPresent()?strategyId.get():INVALID_STRATEGY_ID;
        try {
            // incase a number lower than -1 is passed.
            if (strategyIdToUse < 0) {
                strategyIdToUse = INVALID_STRATEGY_ID;
            }
            if (defaultStrategyToUse) {
                List<Strategy> defaultStrategies = allStrategies.stream().filter(ele -> ele.isDefaultStrategy()).collect(Collectors.toList());
                if (defaultStrategies.size() > 1) {
                    log.error("ERROR! Multiple strategies marked as default. ONLY ONE STRATEGY can be default at a given time.");
                    System.exit(-3);
                }
                strategy = defaultStrategies.get(0);
            } else {
                if (strategyIdToUse != INVALID_STRATEGY_ID) {
                    long finalStrategyIdToUse = strategyIdToUse;
                    List<Strategy> filteredStrategies = allStrategies.stream().filter(ele -> ele.isDefaultStrategy()).collect(Collectors.toList());
                    if (filteredStrategies.size() > 1) {
                        log.error("ERROR! Multiple strategies have same identifier. ALL STRATEGIES MUST HAVE UNIQUE Identifier.");
                        System.exit(-4);
                    }
                    strategy = filteredStrategies.get(0);
                }
                if (strategy == null) {
                    List<Strategy> defaultStrategies = allStrategies.stream().filter(ele -> ele.isDefaultStrategy()).collect(Collectors.toList());
                    if (defaultStrategies.size() > 1) {
                        log.error("ERROR! Multiple strategies marked as default. ONLY ONE STRATEGY can be default at a given time.");
                        System.exit(-3);
                    }
                    strategy = defaultStrategies.get(0);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return strategy;
    }

}
