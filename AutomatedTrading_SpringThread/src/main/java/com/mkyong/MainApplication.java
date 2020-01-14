package com.mkyong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class MainApplication {
    private static final Logger LOGGER = LogManager.getLogger();
    protected BlockingQueue<Float> inputMessageQueue = new LinkedBlockingQueue<>();
    protected Publisher publisher;
    protected Calculator calculator;
    protected BookRepository repository;


    public void threadCreation(){
        LOGGER.info("Timed threads...");

        ExecutorService publisherThread = Executors.newFixedThreadPool(1);
        publisherThread.submit(()->{
            Thread.currentThread().setName("PublisherThread");
            LOGGER.info("{} started ... ", Thread.currentThread().getName());
            publisher.threadWork();
        });

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(()->{
            Thread.currentThread().setName("Calculator Thread 1");
            LOGGER.info("{} started ... ", Thread.currentThread().getName());
            calculator.sendRequest();
        },0,1,TimeUnit.SECONDS);

        ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(()->{
            Thread.currentThread().setName("Calculator Thread 2 ");
            LOGGER.info("{} started ... ", Thread.currentThread().getName());
            calculator.sendRequest();
        },0,3,TimeUnit.SECONDS);
    }

    public void init(BookRepository bookRepository) {
        this.repository = bookRepository;
        publisher = new Publisher(inputMessageQueue,this.repository);
        calculator = new Calculator(inputMessageQueue);
    }
}
