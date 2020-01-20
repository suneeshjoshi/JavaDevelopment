package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.database.PostgreSQLDatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class BinaryWebServiceConnector {

    private static final Logger logger = (Logger) LogManager.getLogger();
    private ApiWrapper api;
    private String applicationId;
    private String applicationAuthorizeToken;
    private BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();
    private DatabaseConnection databaseConnection;
    private String databaseServer;

    public BinaryWebServiceConnector(BlockingQueue<RequestBase> messageQueue, String appId, String appAuthToken, String dbServer ) {
        inputMessageQueue = messageQueue;
        applicationId = appId;
        applicationAuthorizeToken = appAuthToken;
        databaseServer = dbServer;
        databaseConnection = getDatabaseConnection(databaseServer);
        api = ApiWrapper.build(applicationId);
    }

    private DatabaseConnection getDatabaseConnection(String databaseServer) {
        DatabaseConnection result = null;
        switch(databaseServer.toLowerCase()){
            case "postgres" : result =  new PostgreSQLDatabaseConnection("jdbc:postgresql://localhost/automated_trading");
                                break;
            default: logger.error("Database Server not supported.");
        }
        return result;
    }

    private void checkDatabaseTables(){

    }

    public void init() {
        logger.info("Creating WebConnection to Binary.com ....");
        logger.info("Application ID = {}", applicationId);
        logger.info("Application Authorize Token = {}", applicationAuthorizeToken);
        logger.info("Checking Database Schema, if not present creating schema...");
        databaseConnection.createDBSchema();

        AuthorizeRequest authorizeRequest = new AuthorizeRequest(applicationAuthorizeToken);
        api.sendRequest(authorizeRequest).subscribe(response -> {
            AuthorizeResponse auth = (AuthorizeResponse) response;
            // Authorised.
            if (auth.getAuthorize() != null) {
                api.sendRequest(new BalanceRequest(true));
                api.sendRequest(new TransactionsStreamRequest());
//                api.sendRequest(new AccountStatusRequest());
                api.sendRequest(new PortfolioRequest());
            }
        });
    }

    public void getTickDetail(String symbol){
        TickRequest request = new TickRequest(symbol);
        api.sendRequest(request);

        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setSubscribe(1);
        tickHistoryRequest.setCount(100);
        tickHistoryRequest.setGranularity(60);
        api.sendRequest(tickHistoryRequest);

    }

    public void threadWork(){
        logger.info("Thread work ... ");
        Long count = 0L;
        while(true){
            try {
                RequestBase request = inputMessageQueue.poll(100, TimeUnit.MILLISECONDS);
                if(request!=null){
                    logger.info("Sending message to Binary.com ... {}");
                    api.sendRequest(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
