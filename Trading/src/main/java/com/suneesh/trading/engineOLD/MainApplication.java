package com.suneesh.trading.engineOLD;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


public class MainApplication {

    private static final org.apache.logging.log4j.Logger logger = (Logger) LogManager.getLogger();
    protected  static BlockingQueue<Float> inputMessageQueue = new LinkedBlockingDeque<>();
    private static float startValue = 100.0f;

    public static void main(String[] args) {
        logger.info("Main Framework");

        Subscriber subscriber = new Subscriber(startValue, inputMessageQueue);
        Publisher publisher = new Publisher(startValue, inputMessageQueue);

        publisher.init();
        subscriber.init();

        subscriber.getTickData();
        logger.info("Going to make another call.");
    }
}
