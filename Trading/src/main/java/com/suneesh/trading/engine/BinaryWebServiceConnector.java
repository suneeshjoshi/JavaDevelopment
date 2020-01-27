package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.database.PostgreSQLDatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

@Data
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
    protected CalculationEngine calculationEngine;
    protected String symbolToTrade;
    protected boolean backTestingMode;

    public BinaryWebServiceConnector(BlockingQueue<RequestBase> inputCommandQueue, String appId, String appAuthToken, String dbServer, String dbURL, String symbolToTrade, boolean backTestingMode) {
        this.commandQueue = inputCommandQueue;
        this.backTestingMode = backTestingMode;
        this.applicationId = appId;
        this.applicationAuthorizeToken = appAuthToken;
        this.symbolToTrade = symbolToTrade;
        this.databaseServer = dbServer;
        this.databaseURL = dbURL;
        this.databaseConnection = getDatabaseConnection(databaseServer);
        this.api = ApiWrapper.build(applicationId, databaseConnection);
    }

    DatabaseConnection getDatabaseConnection(String databaseServer) {
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
        logger.info("Application ID = {}", this::getApplicationId);
        logger.info("Application Authorize Token = {}", this::getApplicationAuthorizeToken);
        logger.info("Database URL = {}", this::getDatabaseURL);
        logger.info("SymbolToTrade = {}", this::getSymbolToTrade);

        logger.info("Checking Database Schema, if not present creating schema...");
        databaseConnection.init(isBackTestingMode());

        commandProcessor = new CommandProcessor(commandQueue,api);
        calculationEngine = new CalculationEngine(commandQueue, databaseConnection, symbolToTrade);
        threadCreation();
        sendInitialSetupRequest();
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

        ExecutorService commandProcessorThread = Executors.newFixedThreadPool(1);
        commandProcessorThread.submit(()->{
            Thread.currentThread().setName("CommandProcessorThread");
            logger.info("{} started ... ", Thread.currentThread().getName());
            commandProcessor.threadWork();
        });

        if(!isBackTestingMode()){
            ExecutorService commandGeneratorThread = Executors.newFixedThreadPool(1);
            commandGeneratorThread.submit(()->{
                Thread.currentThread().setName("CalculationEngine");
                logger.info("{} started ... ", Thread.currentThread().getName());
                calculationEngine.process();
            });

            ScheduledExecutorService pingServiceThread = Executors.newSingleThreadScheduledExecutor();
            pingServiceThread.scheduleAtFixedRate(()->{
                Thread.currentThread().setName("PingServiceThread");
                new ConnectionMonitor(commandQueue).process();
            },0, Integer.valueOf(AutoTradingUtility.getPropertyFromPropertyFile("PingIntervalInSeconds")),TimeUnit.SECONDS);

        }
        else{
            ExecutorService commandGeneratorThread = Executors.newFixedThreadPool(1);
            commandGeneratorThread.submit(()->{
                Thread.currentThread().setName("BackTestingCalculationEngine");
                logger.info("{} started ... ", Thread.currentThread().getName());
                new BackTestingCalculationEngine(commandQueue, databaseConnection, symbolToTrade).process();
            });
        }


    }
}
