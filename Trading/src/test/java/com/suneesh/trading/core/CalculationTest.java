package com.suneesh.trading.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

@Slf4j
//@Data
public class CalculationTest{

    @Data
    class Trade{
        public Trade(Boolean tradeResult, Double amount) {
            this.tradeResult = tradeResult;
            this.amount = amount;
        }

        Boolean tradeResult;
        Double amount;
    };

    List<Boolean> testResultList = new ArrayList<>();

    Map<Integer, Trade > stepBidResultAmountMap = new HashMap<>();

    double initialBidAmount = 1.0D;
    double nextBidMultiple = 2.5D;
    double maxBidAmount = 40.0D;

    boolean useRecoverStrategyFlag = false;
    double recoveryAmount = 10.0D;
    int maxRecoveryAttemptCount = 0 ;
    int recoveryCount = 0 ;

    Random random = new Random();
    int counter = 0;
    double value = initialBidAmount;

    public CalculationTest(){
        maxRecoveryAttemptCount = (int) Math.ceil( maxBidAmount*nextBidMultiple / recoveryAmount);
        testResultList = Arrays.asList(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
//        testResultList = Arrays.asList(false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false);
    }


    @Test
    public void calculationTest() {
        String type = random.nextBoolean()?"CALL":"PUT";
        Double prevBidAmount = initialBidAmount;

        for(Boolean result : testResultList){
            Double nextBidAmount = getNextBidAmount(result, stepBidResultAmountMap);
            log.info("Last Option Result / Last Amount / Next Amount = {} / {} /{}",result, prevBidAmount, nextBidAmount);

        }

        log.info("Test finished.");
    }

    public Double getNextBidAmount(Boolean islastBidSuccessful, Map<Integer, Trade> stepTradeMap) {
        Trade newTrade = new Trade(islastBidSuccessful,1.0D);

        Integer mapSize = Integer.valueOf(stepTradeMap.size());
        Trade prevTrade = new Trade(islastBidSuccessful,1.0D);
        if(  mapSize > 0 ) {
            prevTrade = stepTradeMap.get(counter);
        }

        counter = ++mapSize;
        stepTradeMap.put(counter, newTrade);

        if (useRecoverStrategyFlag){
            if (recoveryCount < maxRecoveryAttemptCount) {
                newTrade.setAmount(recoveryAmount);
//                log.info("Using Recovery strategy. NextBidAmount / Recovery Amount = {}. , recoveryCount / maxRecoveryAttemptCount = ", newTrade.getAmount(), recoveryCount, maxRecoveryAttemptCount);
                recoveryCount++;
            } else {
                useRecoverStrategyFlag=false;
                newTrade.setAmount(initialBidAmount);
                stepTradeMap.clear();
                stepTradeMap.put(counter, newTrade);


//                log.info("Stopping Recovery strategy. Resetting value to start Value of {}., recoveryCount/ maxRecoveryAttemptCount = {}/{} ",  newTrade.getAmount(), recoveryCount, maxRecoveryAttemptCount);
            }
        }
        else{
            if (islastBidSuccessful) {
                newTrade.setAmount(initialBidAmount);
//                    log.info("Last Binary Option succeeded. Resetting Bid amount to initial value of {}.", newTrade.getAmount());
            }
            else {
//                    log.info("Last Binary Option failed...");
                Double newTradeAmount;
                if(counter>1) {
                    newTradeAmount = prevTrade.getAmount() * nextBidMultiple;
                }
                else{
                    newTradeAmount = initialBidAmount;
                }

                if (newTradeAmount < maxBidAmount) {
                    value = Double.valueOf(String.format("%.2f", value).replace(",", "."));
                    newTrade.setAmount(newTradeAmount);
//                        log.info("Next bid amount = {}.", newTradeAmount);
                } else {
                    useRecoverStrategyFlag = true;
                    recoveryCount++;
                    newTrade.setAmount(recoveryAmount);
                }
            }
        }
        displayMap(stepTradeMap);

        Double result = newTrade.getAmount();
        return result;

    }

    private void displayMap( Map<Integer, Trade> stepTradeMap){
        for(Map.Entry<Integer, Trade> entry : stepTradeMap.entrySet()){
            log.info("{} : {} - {}", entry.getKey(),entry.getValue().getTradeResult(),entry.getValue().getAmount());
        }

    }
}
