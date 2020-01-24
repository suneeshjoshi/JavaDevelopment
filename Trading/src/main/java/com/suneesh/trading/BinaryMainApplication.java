package com.suneesh.trading;

import com.suneesh.trading.engine.ApiWrapper;
import com.suneesh.trading.engine.BinaryWebServiceConnector;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class BinaryMainApplication {
    private static Logger logger = LogManager.getLogger();

    private static ApiWrapper api;
    private static String applicationId;
    private static String applicationAuthorizeToken;
    private static String databaseServer;
    private static String dbURL;
    private static BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();
    private static final String symbolToTrade ="R_10";


    private static void init(){
        applicationId = AutoTradingUtility.getPropertyFromPropertyFile("ApplicationId");
        applicationAuthorizeToken = AutoTradingUtility.getPropertyFromPropertyFile("ApplicationAuthorizeToken");
        databaseServer=AutoTradingUtility.getPropertyFromPropertyFile("DatabaseServerEngine");
        dbURL=AutoTradingUtility.getPropertyFromPropertyFile("DatabaseURL");
    }

    public static void main(String[] args) {
        init();
        BinaryWebServiceConnector binaryWebServiceConnector = new BinaryWebServiceConnector(inputMessageQueue,
                                                                                            applicationId,
                                                                                            applicationAuthorizeToken,
                                                                                            databaseServer,
                                                                                            dbURL,
                                                                                            symbolToTrade);
        binaryWebServiceConnector.init();
        binaryWebServiceConnector.sendInitialSetupRequest();
    }

}
