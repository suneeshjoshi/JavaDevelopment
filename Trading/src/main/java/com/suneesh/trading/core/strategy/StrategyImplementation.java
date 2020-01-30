package com.suneesh.trading.core.strategy;

import com.suneesh.trading.core.NextTradeDetails;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
public class StrategyImplementation {

    public boolean closePriceAtDirectionExtreme(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle){
        boolean result = false;
        final int maxStepCountToApplyFactor=3;
        // Here i am testing the logic to see if the close value of the
        // candle is high or low , then the chance of next candle going in similar direction is higher
        if(lastCandle!=null){
            double high = Double.parseDouble(lastCandle.get("high"));
            double low = Double.parseDouble(lastCandle.get("low"));
            double open = Double.parseDouble(lastCandle.get("open"));
            double close = Double.parseDouble(lastCandle.get("close"));
            String direction = lastCandle.get("direction");

            if(direction.equalsIgnoreCase("UP")){
                if(close==high && nextTradeDetails.getNextStepCount()<=maxStepCountToApplyFactor){
                    log.info(lastCandle.toString());
                    result= true;
                }
            }

            if(direction.equalsIgnoreCase("DOWN")){
                if(close==low && nextTradeDetails.getNextStepCount()<=maxStepCountToApplyFactor){
                    log.info(lastCandle.toString());
                    result= true;
                }
            }
        }
        return result;
    }

    public void calculateAmount(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle){
        final double INCREASED_MOMENTUM_FACTOR= 2.5;
        boolean increaseAmount = false;

        increaseAmount = closePriceAtDirectionExtreme(nextTradeDetails,lastCandle);
        if(increaseAmount){
            nextTradeDetails.setAmount(nextTradeDetails.getAmount() * INCREASED_MOMENTUM_FACTOR);
        }

    }



}
