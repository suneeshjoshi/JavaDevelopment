package com.suneesh.trading.engine;

import com.suneesh.trading.repository.BookRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class Framework {
    private static final Logger LOGGER = LogManager.getLogger();
    protected BlockingQueue<Float> inputMessageQueue = new LinkedBlockingQueue<>();
    protected CommandProcessor commandProcessor;
    protected CommandGenerator commandGenerator;
    protected BookRepository repository;


    public void threadCreation(){
        LOGGER.info("Timed threads...");

        ExecutorService publisherThread = Executors.newFixedThreadPool(1);
        publisherThread.submit(()->{
            Thread.currentThread().setName("BinaryWebServiceConnector");
            LOGGER.info("{} started ... ", Thread.currentThread().getName());
            commandProcessor.threadWork();
        });

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(()->{
            Thread.currentThread().setName("CommandGenerator Thread 1");
            LOGGER.info("{} started ... ", Thread.currentThread().getName());
            commandGenerator.sendRequest();
        },0,1,TimeUnit.SECONDS);

        ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(()->{
            Thread.currentThread().setName("CommandGenerator Thread 2 ");
            LOGGER.info("{} started ... ", Thread.currentThread().getName());
            commandGenerator.sendRequest();
        },0,3,TimeUnit.SECONDS);
    }

    public void init(BookRepository bookRepository) {
        this.repository = bookRepository;
        commandProcessor = new CommandProcessor(inputMessageQueue,this.repository);
        commandGenerator = new CommandGenerator(inputMessageQueue);
    }
}
