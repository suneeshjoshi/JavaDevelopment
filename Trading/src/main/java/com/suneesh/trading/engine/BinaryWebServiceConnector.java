package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.database.PostgreSQLDatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;


public class BinaryWebServiceConnector {

    private static final Logger logger = (Logger) LogManager.getLogger();
    private ApiWrapper api;
    private String applicationId;
    private String applicationAuthorizeToken;
    private BlockingQueue<RequestBase> commandQueue = new LinkedBlockingQueue<>();
    private DatabaseConnection databaseConnection;
    private String databaseServer;
    private String databaseURL;
    protected CommandProcessor commandProcessor;
    protected CommandGenerator commandGenerator;


    public BinaryWebServiceConnector(BlockingQueue<RequestBase> inputCommandQueue, String appId, String appAuthToken, String dbServer, String dbURL) {
        commandQueue = inputCommandQueue;
        applicationId = appId;
        applicationAuthorizeToken = appAuthToken;
        databaseServer = dbServer;
        databaseURL = dbURL;
        databaseConnection = getDatabaseConnection(databaseServer);
        api = ApiWrapper.build(applicationId, databaseConnection);
    }

    private DatabaseConnection getDatabaseConnection(String databaseServer) {
        DatabaseConnection result = null;
        switch(databaseServer.toLowerCase()){
            case "postgres" : result =  new PostgreSQLDatabaseConnection(databaseURL);
                                break;
            default: logger.error("Database Server not supported.");
        }
        return result;
    }

    public void init() {
        logger.info("Creating WebConnection to Binary.com ....");
        logger.info("Application ID = {}", applicationId);
        logger.info("Application Authorize Token = {}", applicationAuthorizeToken);
        logger.info("Checking Database Schema, if not present creating schema...");
        databaseConnection.createDBSchema();
        commandProcessor = new CommandProcessor(commandQueue,api);
        commandGenerator = new CommandGenerator(commandQueue);

        threadCreation();
    }

    public void sendInitialSetupRequest(){
        AuthorizeRequest authorizeRequest = new AuthorizeRequest(applicationAuthorizeToken);
        api.sendRequest(authorizeRequest).subscribe(response -> {
            AuthorizeResponse auth = (AuthorizeResponse) response;
            // Authorised.
            if (auth.getAuthorize() != null) {
                api.sendRequest(new BalanceRequest(true));
                api.sendRequest(new TransactionsStreamRequest());
                api.sendRequest(new PortfolioRequest());
            }
        });
    }

    //-------------
    public void threadCreation(){
        logger.info("Timed threads...");

        ExecutorService publisherThread = Executors.newFixedThreadPool(1);
        publisherThread.submit(()->{
            Thread.currentThread().setName("BinaryWebServiceConnector");
            logger.info("{} started ... ", Thread.currentThread().getName());
            commandProcessor.threadWork();
        });

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(()->{
            Thread.currentThread().setName("CommandGenerator Thread 1");
            logger.info("{} started ... ", Thread.currentThread().getName());
            commandGenerator.sendRequest();
        },0,1,TimeUnit.SECONDS);

        ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(()->{
            Thread.currentThread().setName("CommandGenerator Thread 2 ");
            logger.info("{} started ... ", Thread.currentThread().getName());
            commandGenerator.sendRequest();
        },0,3,TimeUnit.SECONDS);
    }
}
