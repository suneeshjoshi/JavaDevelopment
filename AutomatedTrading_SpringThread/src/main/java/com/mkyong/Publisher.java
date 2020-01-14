package com.mkyong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Publisher {
    private static  final Logger LOGGER = LogManager.getLogger();
    private BlockingQueue<Float> inputMessageQueue = new LinkedBlockingQueue<>();
    private float startValue;
    private BookRepository repository;

    public Publisher(BlockingQueue<Float> inputMessageQueue, BookRepository repository) {
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
                    if(repository!=null) {
                        repository.save(new Book(String.valueOf(data.floatValue())));
//                    bookRepository.findAll().forEach(x->LOGGER.info("Repository value = {}",x));
                    }
                    else{
                        LOGGER.info("Repository is null.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
