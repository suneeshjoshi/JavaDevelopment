package com.suneesh.trading.core.strategy;

import com.google.gson.Gson;
import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.core.technical.BollingerBand;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;
import com.suneesh.trading.models.StrategySteps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class ClosePriceHighLowStrategyImplementation extends AbstractStrategyClass{

    public ClosePriceHighLowStrategyImplementation(DatabaseConnection databaseConnection, Strategy strategy, Utility calculationUtility, Boolean isBackTestingMode) {
        super(databaseConnection, strategy, calculationUtility, isBackTestingMode);
    }


    @Override
    public boolean bookTrade(Map<String, String> lastTrade, Map<String, String> lastCandle){
        boolean result = false;
        // if the close value of the candle is high or low , then the chance of next candle going in similar direction is higher
        if(lastCandle!=null){
            double high = Double.parseDouble(lastCandle.get("high"));
            double low = Double.parseDouble(lastCandle.get("low"));
            double open = Double.parseDouble(lastCandle.get("open"));
            double close = Double.parseDouble(lastCandle.get("close"));
            String direction = lastCandle.get("direction");

            if(direction.equalsIgnoreCase("UP")){
                if(close==high ){
                    result= true;
                }
            }

            if(direction.equalsIgnoreCase("DOWN")){
                if(close==low ){
                    result= true;
                }
            }
        }
        return result;
    }

    public void getBidAmount(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle) {
        double amount = INITIAL_BID_AMOUNT;
        final double INCREASED_MOMENTUM_FACTOR= 1.0;
        boolean increaseAmount = false;

        int nextStepCount = nextTradeDetails.getNextStepCount();
        StrategySteps strategyStep = strategy.getStepToStrategyStepsMap().get(nextStepCount);
        amount = strategyStep.getValue();

        nextTradeDetails.setAmount(amount);

        increaseAmount = bookTrade(null,lastCandle);
        if(increaseAmount){
            nextTradeDetails.setAmount(nextTradeDetails.getAmount() * INCREASED_MOMENTUM_FACTOR);
        }
        nextTradeDetails.setAmount(amount);
    }

}
