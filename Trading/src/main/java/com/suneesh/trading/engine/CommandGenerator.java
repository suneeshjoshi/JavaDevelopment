package com.suneesh.trading.engine;

import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.models.requests.TickRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandGenerator {
    private static  final Logger LOGGER = LogManager.getLogger();
    private BlockingQueue<RequestBase> commandQueue = new LinkedBlockingQueue<>();
    private String symbol;

    public CommandGenerator(BlockingQueue<RequestBase> inputMessageQueue, String symbol) {
        this.commandQueue = inputMessageQueue;
        this.symbol = symbol;
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

    public void sendRequest(RequestBase requestObject){
        try {
            LOGGER.info("Sending message on Command Queue ... {}", String.valueOf(requestObject));
            commandQueue.put(requestObject);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void process(){
        getTickDetail(symbol);
        getCandleDetails(symbol);
    }

}
