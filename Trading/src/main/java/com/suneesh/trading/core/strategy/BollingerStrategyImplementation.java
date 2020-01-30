package com.suneesh.trading.core.strategy;

import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;

import java.util.Map;

public class BollingerStrategyImplementation extends AbstractStrategyClass {

    public BollingerStrategyImplementation(DatabaseConnection databaseConnection, Strategy strategy, Utility calculationUtility, Boolean isBackTestingMode) {
        super(databaseConnection, strategy, calculationUtility, isBackTestingMode);
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

    @Override
    public void getBidAmount(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle) {

    }
}
