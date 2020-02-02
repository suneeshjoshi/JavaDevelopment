package com.suneesh.trading.core;

import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.AuthorizeResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CommandProcessor {
    private static  final Logger logger = LogManager.getLogger();
    private BlockingQueue<RequestBase> commandQueue = new LinkedBlockingQueue<>();
    private float startValue;
    private ApiWrapper api;

    public CommandProcessor(BlockingQueue<RequestBase> inputMessageQueue, ApiWrapper apiWrapper) {
        this.commandQueue = inputMessageQueue;
        this.api = apiWrapper;
    }

    private void checkAndSendAuthorisationTokenIfRequired(){
        List<Map<String,String>> authorizationErrorList = api.getDatabaseConnection().executeQuery("SELECT * from error_table WHERE status = 'ACTIVE' and error_message = 'AuthorizationRequired' and fix_time is null");
        if(!CollectionUtils.isEmpty(authorizationErrorList)){

            AuthorizeRequest authorizeRequest = new AuthorizeRequest(api.getApplicationId());
            api.sendRequest(authorizeRequest).subscribe(response -> {
                AuthorizeResponse auth = (AuthorizeResponse) response;
                // Authorised.
                if (auth.getAuthorize() != null) {
                    api.sendRequest(new BalanceRequest(true));
                    api.sendRequest(new TransactionsStreamRequest());
                    api.sendRequest(new PortfolioRequest());
                }
            });

            api.getDatabaseConnection().executeNoResultSet("UPDATE error_table SET status ='FIXED' AND fix_time = now() WHERE status = 'ACTIVE' and error_message = 'AuthorizationRequired' and fix_time is null");
        }
    }

    public void threadWork(){
        logger.info("Thread work ... ");
        while(true){
            try {
                boolean allowedToSendRequest = false;
                RequestBase request = commandQueue.poll(100, TimeUnit.MILLISECONDS);
                if(request!=null){
                    checkAndSendAuthorisationTokenIfRequired();
                    logger.info("Sending message to Binary.com ... {}", String.valueOf(request));
                    api.sendRequest(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
