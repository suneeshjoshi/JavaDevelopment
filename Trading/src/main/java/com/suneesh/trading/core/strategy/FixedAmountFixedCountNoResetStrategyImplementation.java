package com.suneesh.trading.core.strategy;

import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;

public class FixedAmountFixedCountNoResetStrategyImplementation extends AbstractStrategyClass {
    public FixedAmountFixedCountNoResetStrategyImplementation(DatabaseConnection databaseConnection, Strategy strategy, Utility calculationUtility, Boolean isBackTestingMode) {
        super(databaseConnection, strategy, calculationUtility, isBackTestingMode);
    }
}

