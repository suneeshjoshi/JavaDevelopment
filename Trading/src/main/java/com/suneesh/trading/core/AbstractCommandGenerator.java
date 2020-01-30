package com.suneesh.trading.core;

import com.suneesh.trading.models.requests.RequestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;

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
