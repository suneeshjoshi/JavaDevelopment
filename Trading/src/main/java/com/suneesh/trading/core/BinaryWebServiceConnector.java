package com.suneesh.trading.core;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.database.PostgreSQLDatabaseConnection;
import com.suneesh.trading.core.calculations.Engine;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Data
@Slf4j
public class BinaryWebServiceConnector {
    private ApiWrapper api;
    private String applicationId;
    private String applicationAuthorizeToken;
    private BlockingQueue<RequestBase> commandQueue = new LinkedBlockingQueue<>();
    private DatabaseConnection databaseConnection;
    private String databaseServer;
    private String databaseURL;
    protected ErrorManager errorManager;
    protected CommandProcessor commandProcessor;
    protected Engine calculationEngine;
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
            default: log.error("Database Server not supported.");
        }
        return result;
    }

    public void init() {
        log.info("Creating WebConnection to Binary.com ....");
        log.info("Application ID = {}", getApplicationId());
        log.info("Application Authorize Token = {}", getApplicationAuthorizeToken());
        log.info("Database URL = {}", getDatabaseURL());
        log.info("SymbolToTrade = {}", getSymbolToTrade());

        log.info("Checking Database Schema, if not present creating schema...");
        databaseConnection.init(isBackTestingMode());

        commandProcessor = new CommandProcessor(commandQueue,api);
        errorManager = new ErrorManager(commandQueue,api);
        calculationEngine = new Engine(commandQueue, databaseConnection, symbolToTrade, false);
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
        log.info("Timed threads...");

        ExecutorService commandProcessorThread = Executors.newFixedThreadPool(1);
        commandProcessorThread.submit(()->{
            Thread.currentThread().setName("CommandProcessorThread");
            log.info("{} started ... ", Thread.currentThread().getName());
            commandProcessor.threadWork();
        });

        ExecutorService errorManagerThread = Executors.newFixedThreadPool(1);
        errorManagerThread.submit(()->{
            Thread.currentThread().setName("ErrorManagerThread");
            log.info("{} started ... ", Thread.currentThread().getName());
            errorManager.threadWork();
        });

        if(!isBackTestingMode()){
            ExecutorService commandGeneratorThread = Executors.newFixedThreadPool(1);
            commandGeneratorThread.submit(()->{
                Thread.currentThread().setName("Engine");
                log.info("{} started ... ", Thread.currentThread().getName());
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
                Thread.currentThread().setName("BackTestingEngine");
                log.info("{} started ... ", Thread.currentThread().getName());
                new BackTestingEngine(commandQueue, databaseConnection, symbolToTrade, true).process();
            });
        }


    }
}
