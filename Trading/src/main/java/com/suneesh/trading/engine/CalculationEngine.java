package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.models.requests.TickRequest;
import com.suneesh.trading.utils.AutoTradingUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

import static com.suneesh.trading.utils.AutoTradingUtility.sleep;

public class CalculationEngine extends AbstractCommandGenerator {
    private static final Logger logger = LogManager.getLogger();
    private DatabaseConnection databaseConnection;
    private String symbol;

    public CalculationEngine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol) {
        super(inputMessageQueue);
        this.symbol = symbol;
        this.databaseConnection = dbConnection;
    }

    public void getTickDetail(String symbol) {
        sendRequest(new TickRequest(symbol));
    }

    public void getCandleDetails(String symbol) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setSubscribe(1);
        tickHistoryRequest.setCount(100);
        tickHistoryRequest.setGranularity(60);
        sendRequest(tickHistoryRequest);
    }

    private Map<String,String> getLastCandle(){
        return (Map<String, String>) (databaseConnection.executeQuery("select * from candle order by identifier desc limit 1")).get(0);
    }

    private void sleepTillStartOfNextMinute(){
        int secondsToNextMinute = 60- Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)%60);
        logger.info("************Going to sleep {} milliseconds for next minute to start...", secondsToNextMinute);
        AutoTradingUtility.sleep(secondsToNextMinute*1000);
        logger.info("************Woken Up from sleep.");
    }

    public void process(){
        getTickDetail(symbol);
        getCandleDetails(symbol);

        sleepTillStartOfNextMinute();

        Map<String,String> result = getLastCandle();
        String previousCandleDirection = result.get("direction");
        logger.debug("************ : {}", previousCandleDirection);

        for(Map.Entry<String,String> entry : result.entrySet()){
            logger.debug("{} - {} ", entry.getKey(),entry.getValue());
        }

    }


    public String convertEpochToSimpleDate(Long epochTime) {
        Date date = new Date(epochTime);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        System.out.println(formatted);
        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        formatted = format.format(date);
        System.out.println(formatted);
        return formatted;
    }
}
