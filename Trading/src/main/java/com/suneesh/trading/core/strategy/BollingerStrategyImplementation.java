package com.suneesh.trading.core.strategy;

import com.google.gson.Gson;
import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.core.technical.BollingerBand;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class BollingerStrategyImplementation extends AbstractStrategyClass {

    public BollingerStrategyImplementation(DatabaseConnection databaseConnection, Strategy strategy, Utility calculationUtility, Boolean isBackTestingMode) {
        super(databaseConnection, strategy, calculationUtility, isBackTestingMode);
    }

    @Override
    public boolean bookTrade(Map<String, String> lastTrade, Map<String, String> lastCandle){
        boolean result = false;
        Double candleClose = Double.valueOf(lastCandle.get("close"));
        String candleDirection = lastCandle.get("direction");
        Gson gson = new Gson();

        ArrayList<String> jsonResultDBQuery = databaseConnection.getJsonResultDBQuery("select * from bollinger_band");
        ArrayList<BollingerBand> bollingerBands = (ArrayList<BollingerBand>) jsonResultDBQuery.stream().map(ele -> gson.fromJson(String.valueOf(ele), BollingerBand.class)).collect(Collectors.toList());

//        bollingerBands.stream().forEach(bollingerBand -> log.debug("{} : {} : {}", candleClose, candleDirection, bollingerBand.toString()));

        BollingerBand lastBollingerBand = bollingerBands.get(bollingerBands.size()-1);


        if( candleDirection.equalsIgnoreCase("DOWN")){
            if(candleClose < lastBollingerBand.getLowerBand() ) {
                log.info("LOWER BOLLINGER BAND BREACHED.");
                result = true;
            }
        }

        if( candleDirection.equalsIgnoreCase("HIGH")){
            if(candleClose > lastBollingerBand.getUpperBand() ) {
                log.info("UPPER BOLLINGER BAND BREACHED.");
                result = true;
            }
        }
        return result;
    }

    @Override
    public void getCallOrPut(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle) {
        String callOrPutResult = "NONE";
        

        if(!MapUtils.isEmpty(lastCandle)) {
            String previousCandleDirection = lastCandle.get("direction");
            callOrPutResult = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
        }
        nextTradeDetails.setCallOrPut(callOrPutResult);
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

    @Override
    public void getBidAmount(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle) {

    }
}
