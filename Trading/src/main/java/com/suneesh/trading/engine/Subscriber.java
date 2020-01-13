package com.suneesh.trading.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.GenericDeclaration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;
import org.apache.commons.collections4.MapUtils;

public class Subscriber {

    private static final org.apache.logging.log4j.Logger logger = (Logger) LogManager.getLogger();
    private Map<Long,TickPriceData> cache;
    private BlockingQueue<Float> inputMessageQueue;
    private float startValue;

    public Subscriber(float startValue, BlockingQueue<Float> inputMessageQueue){
        this.startValue = startValue;
        this.inputMessageQueue = inputMessageQueue;
    }

    public void init(){
        cache = new LinkedHashMap<Long, TickPriceData>();
    }

    public  void getTickData(){
        while(true){
            float subscribedPrice = 0.0f;
            try{
                subscribedPrice = inputMessageQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            processData(subscribedPrice);
        }
    }

    private void processData(float subscribedPrice) {
        TickPriceData pd = getPriceData(subscribedPrice);
        logger.info("cache = {}", pd);
        addToCache(pd);
    }

    private TickPriceData getPriceData(float newPrice) {
        float change = 0.0f;
        if(!MapUtils.isEmpty(cache)){
            final ArrayList<Map.Entry<Long, TickPriceData>> entries = new ArrayList<>(cache.entrySet());
            Map.Entry<Long, TickPriceData> lastEntry = entries.get(entries.size() - 1);
            change = (newPrice - lastEntry.getValue().getPrice());
        }
        else{
            change = newPrice = startValue;
        }
        return (new TickPriceData(newPrice,change));
    }

    private void addToCache(TickPriceData pd) {
        cache.put(pd.getId(), pd);
    }

    public void convertTicks(int tickCounts){
        Trade newTrade;
        int lastId = cache.size() -1 ;
        int startId = lastId - tickCounts;

        final TickPriceData startPriceData = cache.get(Long.valueOf(startId));
        final TickPriceData endPriceData = cache.get(Long.valueOf(lastId));

        logger.info(startPriceData);
        logger.info(endPriceData);

        float priceMovement = endPriceData.getPrice() - startPriceData.getPrice();
        if(priceMovement<0){
            newTrade = TradeProcessor.createNewTrade("PUT", endPriceData.getPrice(), LocalDateTime.now());
        }
        else{
            newTrade = TradeProcessor.createNewTrade("CALL", endPriceData.getPrice(), LocalDateTime.now());
        }
        logger.info(newTrade);
    }

}
