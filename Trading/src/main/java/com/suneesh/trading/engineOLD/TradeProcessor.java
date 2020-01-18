package com.suneesh.trading.engineOLD;

import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class TradeProcessor {
    private static final org.apache.logging.log4j.Logger logger = (Logger) LogManager.getLogger();
    private static Map<Trade, String> tradeCache = new LinkedHashMap<>();

    public static Trade createNewTrade(String PutOrCall, float tradePrice, LocalDateTime tradeTime) {
        Trade newTrade = new Trade();
        newTrade.setPutOrCall(PutOrCall);
        newTrade.setAmount(getAmountToTrade());
        newTrade.setStartPrice(tradePrice);
        newTrade.setTradeTime(tradeTime);
        return newTrade;
    }

    private static Trade getLastTrade(){
        Trade lastTrade = null;
        if(!MapUtils.isEmpty(tradeCache)){
            final ArrayList<Map.Entry<Trade, String>> entries = new ArrayList<>(tradeCache.entrySet());
            Map.Entry<Trade, String> lastEntry = entries.get(entries.size() - 1);
            lastTrade = lastEntry.getKey();
        }
        return lastTrade;
    }

    private static boolean lastTradeSucceeded(){
        boolean result = true;
        final Trade lastTrade = getLastTrade();
        if(lastTrade!=null) {
            if(lastTrade.getStatus().equalsIgnoreCase("SUCCESS")) {
                result = false;
            }
        }
        return result;
    }

    private static float getAmountToTrade(){
        float amount = 1.0f;

        if(!lastTradeSucceeded()){
            amount = getLastTrade().getAmount()*2.5f;
        }
        return amount;
    }

}
