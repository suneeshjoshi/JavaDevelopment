package com.suneesh.trading;

import com.suneesh.trading.engine.ApiWrapper;
import com.suneesh.trading.engine.BinaryWebServiceConnector;
import com.suneesh.trading.models.requests.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class BinaryMainApplication {
    Logger logger = LogManager.getLogger();

    private static ApiWrapper api;
    private static String applicationId="21829";
    private static String applicationAuthorizeToken="9s5aGYbnsUQr3Fv";
    private static String databaseServer="Postgres";
    private static final String symbolToTrade ="R_10";

    // Docker URL
        private static String dbURL = "jdbc:postgresql://192.168.99.100:5432/automated_trading";

    // Localhost
//    private static String dbURL = "jdbc:postgresql://localhost:5432/automated_trading";
    private static BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        BinaryWebServiceConnector binaryWebServiceConnector = new BinaryWebServiceConnector(inputMessageQueue,applicationId, applicationAuthorizeToken, databaseServer, dbURL, symbolToTrade);
        binaryWebServiceConnector.init();
        binaryWebServiceConnector.sendInitialSetupRequest();
    }
}
