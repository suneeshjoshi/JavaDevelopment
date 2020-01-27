package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class BackTestingCalculationEngine extends CalculationEngine{
    private static final Logger logger = LogManager.getLogger();
    private String symbol;

    public BackTestingCalculationEngine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol) {
        super(inputMessageQueue, dbConnection, symbol);
        this.symbol = symbol;
    }

    public void getCandleDetails(String symbol) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setCount(5000);
        tickHistoryRequest.setGranularity(60);
        sendRequest(tickHistoryRequest);
    }

    public void process(){
        log.info("in Backtesting calculation Engine");
        getCandleDetails(symbol);
        log.info("Request sent to get candle data...");
        System.exit(-1);
    }
}
