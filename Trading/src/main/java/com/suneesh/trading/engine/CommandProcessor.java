package com.suneesh.trading.engine;

import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.spring_stuff.repository.TickRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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

    private boolean checkIfAuthorisationNeeded(String requestName){
        // 1. check if in authorisation list of class
        // 2. is authorisation valid
        // 3. Return status. or if not then get authorisation.
        return false;
    }

    public void threadWork(){
        logger.info("Thread work ... ");
        while(true){
            try {
                boolean allowedToSendRequest = false;
                RequestBase request = commandQueue.poll(100, TimeUnit.MILLISECONDS);
                if(request!=null){
                    if(checkIfAuthorisationNeeded(request.getClass().getSimpleName())){

                    }
                    else{
                        allowedToSendRequest=true;
                    }
                    logger.info("Sending message to Binary.com ... {}",String.valueOf(request));
                    api.sendRequest(request);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
