package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class StrategyBackTesting {

    private static final Logger logger = (Logger) LogManager.getLogger();
    private ApiWrapper api;
    private String applicationId;
    private String applicationAuthorizeToken;
    private BlockingQueue<RequestBase> commandQueue = new LinkedBlockingQueue<>();
    private DatabaseConnection databaseConnection;
    private String databaseServer;
    private String databaseURL;
    protected CommandProcessor commandProcessor;
    protected CalculationEngine calculationEngine;
    protected String symbolToTrade;

    private void getBackTestingData() {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbolToTrade, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setCount(5000);
        tickHistoryRequest.setGranularity(60);
        api.sendRequest(tickHistoryRequest);
    }

    @Test
    public void startTest(){
        symbolToTrade = "R_10";
        boolean backTesting = Boolean.parseBoolean(AutoTradingUtility.getPropertyFromPropertyFile("BackTesting"));
        if(!backTesting){
            log.info("BAcktesting not enabled. Exiting.");
            System.exit(-1);
        }

        getBackTestingData();

        String json = "{\n" +
                "  \"backTesting\": \""+backTesting+"\"\n" +
                "}";

    }


}
