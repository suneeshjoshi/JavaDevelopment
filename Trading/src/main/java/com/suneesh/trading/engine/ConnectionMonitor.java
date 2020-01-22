package com.suneesh.trading.engine;

import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.PingRequest;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.models.requests.TickRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class ConnectionMonitor extends AbstractCommandGenerator {
    private static final Logger LOGGER = LogManager.getLogger();

    private String symbol;

    public ConnectionMonitor(BlockingQueue<RequestBase> inputMessageQueue) {
        super(inputMessageQueue);
    }

    public void process(){
        sendRequest( new PingRequest());
    }

}
