package com.suneesh.trading.core;

import com.suneesh.trading.models.requests.PingRequest;
import com.suneesh.trading.models.requests.RequestBase;

import java.util.concurrent.BlockingQueue;

public class ConnectionMonitor extends AbstractCommandGenerator {
    private String symbol;

    public ConnectionMonitor(BlockingQueue<RequestBase> inputMessageQueue) {
        super(inputMessageQueue);
    }

    public void process(){
        sendRequest( new PingRequest());
    }

}
