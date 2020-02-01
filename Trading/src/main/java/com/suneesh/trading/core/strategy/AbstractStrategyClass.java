package com.suneesh.trading.core.strategy;

import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;
import com.suneesh.trading.models.StrategySteps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Optional;

@Data
@Slf4j
public abstract class AbstractStrategyClass implements StrategyImplementationInterface {

    //    final static long CONTRACT_DURATION_IN_SECONDS = 60L;
//    final static long CONTRACT_DURATION_IN_SECONDS = 58L;
    // This should be the same as the granularity of the candle
    final static long CONTRACT_DURATION_IN_SECONDS = 300L;
    final static double INITIAL_BID_AMOUNT = 1.00D;
    DatabaseConnection databaseConnection;
    Strategy strategy;
    Utility calculationUtility;
    Boolean isBackTestingMode;

    public AbstractStrategyClass(DatabaseConnection databaseConnection, Strategy strategy, Utility calculationUtility, Boolean isBackTestingMode) {
        this.databaseConnection = databaseConnection;
        this.strategy = strategy;
        this.calculationUtility = calculationUtility;
        this.isBackTestingMode= isBackTestingMode;
    }


    public void getContractDuration(NextTradeDetails nextTradeDetails) {
        nextTradeDetails.setContractDuration(CONTRACT_DURATION_IN_SECONDS);
    }

    public void getCallOrPut(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle){
        String callOrPutResult = "CALL";
        if(!MapUtils.isEmpty(lastCandle)) {
            String previousCandleDirection = lastCandle.get("direction");
            callOrPutResult = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
        }
        nextTradeDetails.setCallOrPut(callOrPutResult);
    }

    public void getBidAmount(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle) {
        double amount = INITIAL_BID_AMOUNT;
        int nextStepCount = nextTradeDetails.getNextStepCount();
        StrategySteps strategyStep = strategy.getStepToStrategyStepsMap().get(nextStepCount);
        amount = strategyStep.getValue();

        nextTradeDetails.setAmount(amount);
    }

    public void getNextStepCount(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade) {
        int stepCount = 1;

        if(!MapUtils.isEmpty(lastTrade)){
            String previousTradeResult = String.valueOf(lastTrade.get("result"));
            long previousTradeStrategyID =  Long.valueOf(lastTrade.get("strategy_id"));
            int previousStepCount = Integer.parseInt(lastTrade.get("step_count"));

            Strategy previousTradeStrategy = calculationUtility.getStrategy(Optional.of(previousTradeStrategyID), Optional.of(false));
            boolean reset_step_count_on_success = previousTradeStrategy.isResetStepCountOnSuccess();

            if(previousTradeResult.equalsIgnoreCase("SUCCESS") ){
                if(reset_step_count_on_success) {
                    stepCount = 1;
                }
                else{
                    stepCount=previousStepCount+1;
                }
            }

            if(previousTradeResult.equalsIgnoreCase("FAIL")){
                stepCount=previousStepCount+1;
            }
        }
        nextTradeDetails.setNextStepCount(stepCount);
    }

    public void getNextTradeStrategyId(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade) {
        long nextTradeStrategyId = -1;

        if( !MapUtils.isEmpty(lastTrade)) {
            // Lowest amount possible.
            long previousTradeStrategyId = Long.valueOf(lastTrade.getOrDefault("strategy_id","1"));
            Strategy previousTradeStrategy = calculationUtility.getStrategy(Optional.of(previousTradeStrategyId), Optional.of(false));

            int maxSteps = strategy.getMaxSteps();
            if (nextTradeDetails.getNextStepCount() > maxSteps) {
                nextTradeStrategyId = strategy.getNextStrategyIdLink();

                // Resetting step_count to 1
                nextTradeDetails.setNextStepCount(1);
            }
            else{
                nextTradeStrategyId = previousTradeStrategyId;
            }
        }
        else{
            if(isBackTestingMode){
                Strategy strategy = calculationUtility.getBackTestingStrategy();
            }
            else{
                Strategy strategy = calculationUtility.getStrategy(Optional.empty(),Optional.of(true));
            }
            nextTradeStrategyId = strategy.getIdentifier();

        }
        nextTradeDetails.setStrategyId(nextTradeStrategyId);
    }

}
