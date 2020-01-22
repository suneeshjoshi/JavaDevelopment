package com.suneesh.trading.engine;

import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.models.requests.TickRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractCommandGenerator {
    private static final Logger LOGGER = LogManager.getLogger();
    protected BlockingQueue<RequestBase> commandQueue = null;

    public AbstractCommandGenerator(BlockingQueue<RequestBase> inputMessageQueue) {
        this.commandQueue = inputMessageQueue;
    }

    public void sendRequest(RequestBase requestObject){
        try {
            LOGGER.info("Sending message on Command Queue ... {}", String.valueOf(requestObject));
            commandQueue.put(requestObject);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    abstract public void process();

}
