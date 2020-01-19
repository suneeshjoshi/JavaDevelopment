package com.suneesh.trading.engine;

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
    private static ApiWrapper api;
    private static String applicationId;
    private static String applicationAuthorizeToken;
    private static BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();


    public BinaryWebServiceConnector(BlockingQueue<Float> inputMessageQueue, String applicationId, String applicationAuthorizeToken) {
        inputMessageQueue = inputMessageQueue;
        applicationId = applicationId;
        applicationAuthorizeToken = applicationAuthorizeToken;
        api = ApiWrapper.build(applicationId);
    }

    public static void init() {
        logger.info("Creating WebConnection to Binary.com ....");
        logger.info("Application ID = {}", applicationId);
        logger.info("Application Authorize Token = {}", applicationAuthorizeToken);

        AuthorizeRequest authorizeRequest = new AuthorizeRequest(applicationAuthorizeToken);
        api.sendRequest(authorizeRequest).subscribe(response -> {
            AuthorizeResponse auth = (AuthorizeResponse) response;
            // Authorised.
            if (auth.getAuthorize() != null) {
                api.sendRequest(new BalanceRequest(true));
                api.sendRequest(new TransactionsStreamRequest());
                api.sendRequest(new AccountStatusRequest());
                api.sendRequest(new PortfolioRequest());
            }
        });
    }

    public static void getTickDetail(String symbol){
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
