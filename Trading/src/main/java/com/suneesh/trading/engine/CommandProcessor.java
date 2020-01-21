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

    public void threadWork(){
        logger.info("Thread work ... ");
        while(true){
            try {
                RequestBase request = commandQueue.poll(100, TimeUnit.MILLISECONDS);
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
