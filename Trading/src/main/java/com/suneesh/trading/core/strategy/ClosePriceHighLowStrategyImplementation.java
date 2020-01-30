package com.suneesh.trading.core.strategy;

import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Optional;

@Data
@Slf4j
public class ClosePriceHighLowStrategyImplementation extends AbstractStrategyClass{

    public ClosePriceHighLowStrategyImplementation(DatabaseConnection databaseConnection, Strategy strategy, Utility calculationUtility) {
        super(databaseConnection, strategy, calculationUtility);
    }


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

    @Override
    public void getCallOrPut(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle) {

    }

    @Override
    public void getContractDuration(NextTradeDetails nextTradeDetails) {

    }

    @Override
    public void getNextStepCount(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade) {

    }

    @Override
    public void getNextTradeStrategyId(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade) {

    }

    public void getBidAmount(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle) {
        double amount = INITIAL_BID_AMOUNT;
        final double INCREASED_MOMENTUM_FACTOR= 2.5;
        boolean increaseAmount = false;

        Map<String, String> lastTrade = calculationUtility.getLastTrade();
        if(MapUtils.isEmpty(lastTrade)){
            getInitialTradeAmount();
        }
        else{
            Strategy strategy = calculationUtility.getStrategy(Optional.of(nextTradeDetails.getStrategyId()) , Optional.of(false));
            int nextStepCount = nextTradeDetails.getNextStepCount();
            amount = strategy.getStepValuesMap().get(nextStepCount);
        }

        increaseAmount = closePriceAtDirectionExtreme(nextTradeDetails,lastCandle);
        if(increaseAmount){
            nextTradeDetails.setAmount(nextTradeDetails.getAmount() * INCREASED_MOMENTUM_FACTOR);
        }
        nextTradeDetails.setAmount(amount);
    }

}
