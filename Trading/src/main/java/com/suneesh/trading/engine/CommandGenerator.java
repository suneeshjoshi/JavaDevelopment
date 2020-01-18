package com.suneesh.trading.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandGenerator {
    private static  final Logger LOGGER = LogManager.getLogger();
    private BlockingQueue<Float> inputMessageQueue = new LinkedBlockingQueue<>();
    private float startValue;

    public CommandGenerator(BlockingQueue<Float> inputMessageQueue) {
        this.inputMessageQueue = inputMessageQueue;
        this.startValue = 0;
    }

    public void sendRequest(){
        float newValue = 0.0f;
        try{
            float delta = (new Random()).nextFloat();
            int direction = (new Random()).nextBoolean()==true?1:-1;
            newValue += (delta * direction);
            inputMessageQueue.put(newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
