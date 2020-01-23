package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.requests.BuyContractParameters;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Data
public class CalculationEngineUtility {
    private DatabaseConnection databaseConnection;
    final static long CONTRACT_DURATION_IN_SECONDS = 60;
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

    int getStepCount() {
        int stepCount=1;
        List<Map<String,String>> result = (List<Map<String, String>>) databaseConnection.executeQuery(
                "select step_count from trade order by identifier desc limit 1");
        if(!CollectionUtils.isEmpty(result)){
            Map<String,String> firstRow = result.get(0);
            stepCount = Integer.valueOf(firstRow.get("step_count"));
            stepCount++;
        }
        return stepCount;
    }

    String getTradeDatabaseInsertString(BuyContractParameters parameters, int stepCount) {
        return
                "INSERT INTO public.trade " +
                        "(amount, call_or_put, symbol, step_count, strategy_id, trade_time) " +
                        " VALUES (" + AutoTradingUtility.quotedString(parameters.getAmount())+", "
                        + AutoTradingUtility.quotedString(parameters.getContractType())+", "
                        + AutoTradingUtility.quotedString(parameters.getSymbol())+", "
                        + AutoTradingUtility.quotedString(stepCount)+", "
                        + AutoTradingUtility.quotedString("1")+","
                        + "extract(epoch from now()) );";
    }


}
