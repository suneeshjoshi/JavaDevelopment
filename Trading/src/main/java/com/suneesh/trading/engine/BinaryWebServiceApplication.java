package com.suneesh.trading.engine;

import com.suneesh.trading.models.requests.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class BinaryWebServiceApplication {
    Logger logger = LogManager.getLogger();

    private static ApiWrapper api;
    private static String applicationId="21829";
    private static String applicationAuthorizeToken="9s5aGYbnsUQr3Fv";
    private static BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        BinaryWebServiceConnector binaryWebServiceConnector = new BinaryWebServiceConnector(inputMessageQueue,applicationId, applicationAuthorizeToken);
        binaryWebServiceConnector.init();
        binaryWebServiceConnector.getTickDetail("R_10");
        binaryWebServiceConnector.threadWork();

    }
}
