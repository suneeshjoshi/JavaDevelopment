package com.suneesh.trading.engineOLD;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Publisher {
    private static final org.apache.logging.log4j.Logger logger = (Logger) LogManager.getLogger();
    private float startValue;
    private BlockingQueue<Float> inputMessageQueue;
    private ExecutorService es;

    public Publisher(float startValue, BlockingQueue<Float> inputMessageQueue){
        this.startValue = startValue;
        this.inputMessageQueue = inputMessageQueue;
    }

    public void init(){
        es = Executors.newFixedThreadPool(1);
        es.submit(()->{
            threadWork();
        });
        es.shutdown();
    }

    private void threadWork() {
        logger.info("Thread Work ... ");
        float newValue = startValue;
        for(int i = 0 ; i < 10 ; ++i){
            float delta = (new Random()).nextFloat();
            int direction = (new Random()).nextBoolean()==true?1:-1;
            newValue += (delta * direction);
            try {
                inputMessageQueue.put(newValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
