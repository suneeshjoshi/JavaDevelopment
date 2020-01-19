package com.suneesh.trading.engine;

import com.suneesh.trading.repository.TickResponseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CommandProcessor {
    private static  final Logger LOGGER = LogManager.getLogger();
    private BlockingQueue<Float> inputMessageQueue = new LinkedBlockingQueue<>();
    private float startValue;
    private TickResponseRepository repository;

    public CommandProcessor(BlockingQueue<Float> inputMessageQueue, TickResponseRepository repository) {
        this.inputMessageQueue = inputMessageQueue;
        this.startValue = 0;
        this.repository = repository;
    }

    public void threadWork(){
        LOGGER.info("Thread work ... ");
        Long count = 0L;
        while(true){
            try {
                final Float data = inputMessageQueue.poll(100, TimeUnit.MILLISECONDS);
                if(data!=null){
                    count++;
                    LOGGER.info("data = {}", data);
//                    if(repository!=null) {
//                        repository.save(new Book(String.valueOf(data.floatValue())));
//                    }
//                    else{
//                        LOGGER.info("Repository is null.");
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
