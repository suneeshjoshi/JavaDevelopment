package com.suneesh.trading.engine;

import com.google.gson.Gson;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.utils.AutoTradingUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
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
    final long CONTRACT_DURATION_IN_SECONDS = 60;
    final String ACCOUNT_CURRENCY="AccountCurrency";

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

    private String getCurrency(){
        String applicationMode = AutoTradingUtility.getPropertyFromPropertyFile("ApplicationMode");
        String currencyProperty = applicationMode+ACCOUNT_CURRENCY;
        return (AutoTradingUtility.getPropertyFromPropertyFile(currencyProperty));
    }

    private Map<String,String> getLastCandle(){
        return (Map<String, String>) (databaseConnection.executeQuery("select * from candle order by identifier desc limit 1")).get(0);
    }

    private void sleepTillStartOfNextMinute(){
        int secondsToNextMinute = 60- Math.toIntExact(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)%60);
        AutoTradingUtility.sleep(secondsToNextMinute*1000);
    }

    public void process(){
        getTickDetail(symbol);
        getCandleDetails(symbol);
        String currency = getCurrency();


        if(currency.isEmpty()){
            logger.fatal("FATAL ERROR! No Currency detail found. Cannot book trade.\nExiting aoplication.");
            System.exit(-1);
        }
        else {
            sleepTillStartOfNextMinute();

            Map<String, String> result = getLastCandle();
            String previousCandleDirection = result.get("direction");
            logger.debug("************ : {}", previousCandleDirection);

            for (Map.Entry<String, String> entry : result.entrySet()) {
                logger.debug("{} - {} ", entry.getKey(), entry.getValue());
            }


            double bidAmount = getBidAmount();
            String callOrPut = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
            long contractDuration = CONTRACT_DURATION_IN_SECONDS;

//        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_TRADE"));
            BuyContractParameters parameters = getParameters(symbol, bidAmount, callOrPut, contractDuration, currency);
            BuyContractRequest buyContractRequest = new BuyContractRequest(new BigDecimal(bidAmount), parameters);

            logger.info("Sending buy Contract Request ... ");
            sendRequest(buyContractRequest);
        }
    }

    private double getBidAmount() {
        return 0;
    }

    private BuyContractParameters getParameters(String symbol, double bidAmount, String callOrPut, long contractDuration, String currency) {
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
}
