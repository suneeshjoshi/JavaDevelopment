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

public class BinaryMainApplication {
    private static Logger logger = LogManager.getLogger();

    private static String applicationId;
    private static String applicationAuthorizeToken;
    private static String databaseServer;
    private static String dbURL;
    private static String dbName;
    private static BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();
    private static final String symbolToTrade ="R_10";
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
            logger.info("\n\n");
            logger.info("***************************************************");
            logger.info("ALERT! RUNNING IN BACK TESTING MODE.");
            logger.info("MANY NORMAL FEATURES DISABLED, IN BACKTESTING MODE.");
            logger.info("***************************************************\n\n");
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
