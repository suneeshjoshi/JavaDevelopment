package com.suneesh.trading;

import com.suneesh.trading.core.BinaryWebServiceConnector;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class BinaryMainApplication {
    private static String applicationId;
    private static String applicationAuthorizeToken;
    private static String databaseServer;
    private static String dbURL;
    private static String dbName;
    private static BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();
    private static final String symbolToTrade ="R_50";
    private static boolean backTestingMode;

    private static void init(){
        String databaseNamePropertyToUse;
        applicationId = AutoTradingUtility.getPropertyFromPropertyFile("ApplicationId");
        applicationAuthorizeToken = AutoTradingUtility.getPropertyFromPropertyFile("ApplicationAuthorizeToken");
        databaseServer=AutoTradingUtility.getPropertyFromPropertyFile("DatabaseServerEngine");
        dbURL=AutoTradingUtility.getPropertyFromPropertyFile("DatabaseURL");
        backTestingMode=Boolean.parseBoolean(AutoTradingUtility.getPropertyFromPropertyFile("BackTesting"));
        if(backTestingMode){
            databaseNamePropertyToUse="BackTestingDatabaseName";
        }else{
            databaseNamePropertyToUse="DatabaseName";
        }

        dbName=AutoTradingUtility.getPropertyFromPropertyFile(databaseNamePropertyToUse);
    }

    public static void main(String[] args) {
        init();
        if(backTestingMode){
            log.info("\n\n");
            log.info("***************************************************");
            log.info("ALERT! RUNNING IN BACK TESTING MODE.");
            log.info("MANY NORMAL FEATURES DISABLED, IN BACKTESTING MODE.");
            log.info("***************************************************\n\n");
        }

        String completeDatabaseURL=dbURL+"/"+dbName;
        BinaryWebServiceConnector binaryWebServiceConnector = new BinaryWebServiceConnector(inputMessageQueue,
                                                                                            applicationId,
                                                                                            applicationAuthorizeToken,
                                                                                            databaseServer,
                                                                                            completeDatabaseURL,
                                                                                            symbolToTrade,
                                                                                            backTestingMode);
        binaryWebServiceConnector.init();
    }

}
