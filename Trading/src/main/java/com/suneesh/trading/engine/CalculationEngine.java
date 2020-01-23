package com.suneesh.trading.engine;

import com.google.gson.Gson;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.utils.AutoTradingUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.BlockingQueue;

import static com.suneesh.trading.utils.AutoTradingUtility.sleep;

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
        getTickDetail(symbol);
        getCandleDetails(symbol);
        String currency = calculationEngineUtility.getCurrency();

        if(currency.isEmpty()){
            logger.fatal("FATAL ERROR! No Currency detail found. Cannot book trade.\nExiting aoplication.");
            System.exit(-1);
        }
        else {
            logger.info("Sleeping for the start of next minute.");
            calculationEngineUtility.sleepTillStartOfNextMinute();

            logger.info("*** Sleeping for another minute to stablise last candle values, and to ensure its recorded completely and accurately for trading. ***");
            calculationEngineUtility.sleepTillStartOfNextMinute();

            Map<String, String> result = calculationEngineUtility.getLastCandle();
            String previousCandleDirection = result.get("direction");

            logger.debug("************ : {}", previousCandleDirection);
            for (Map.Entry<String, String> entry : result.entrySet()) {
                logger.debug("{} - {} ", entry.getKey(), entry.getValue());
            }


            double bidAmount = getBidAmount();
            int stepCount = calculationEngineUtility.getStepCount();
            String callOrPut = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
            long contractDuration = CalculationEngineUtility.CONTRACT_DURATION_IN_SECONDS;

//        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("VRTC_TRADE"));
            BuyContractParameters parameters = getParameters(symbol, bidAmount, callOrPut, contractDuration, currency);
            BuyContractRequest buyContractRequest = new BuyContractRequest(new BigDecimal(bidAmount), parameters);

            String tradeInsertStatement = calculationEngineUtility.getTradeDatabaseInsertString(parameters, stepCount);
            logger.debug(tradeInsertStatement);
            calculationEngineUtility.getDatabaseConnection().executeNoResultSet(tradeInsertStatement);

            logger.info("Sending buy Contract Request ... ");
            sendRequest(buyContractRequest);
        }
    }


    private double getBidAmount() {
        return 0.35D;
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
