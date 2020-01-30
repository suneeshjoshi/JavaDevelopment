package com.suneesh.trading.core.strategy;

import com.suneesh.trading.core.calculations.Utility;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.Strategy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StrategyFactory {
    DatabaseConnection databaseConnection;
    Utility calculationUtility;
    Boolean isBackTestingMode;

    public StrategyFactory(DatabaseConnection databaseConnection, Utility calculationUtility, Boolean isBackTestingMode) {
        this.databaseConnection = databaseConnection;
        this.calculationUtility = calculationUtility;
        this.isBackTestingMode = isBackTestingMode;
    }

    public StrategyImplementationInterface getStrategyImplementation(Strategy strategyObj){
        AbstractStrategyClass strategyImplementation = null;
        long strategyId = strategyObj.getIdentifier();

        List<Strategy> strategyList = calculationUtility.getAllStrategies().stream().filter(f -> f.getIdentifier()==strategyId).collect(Collectors.toList());
        if(strategyList.size()>1){
            log.error("ERROR! more than one Strategy present for same identifier. Exiting");
            System.exit(-1);
        }

        switch(Math.toIntExact(strategyId)){
            case 1:
            case 3:
                strategyImplementation =  new CandleFollowWithIncreaseStrategyImplementation(databaseConnection, strategyObj, calculationUtility, isBackTestingMode); break;

            case 2: strategyImplementation =  new FixedAmountFixedCountNoResetStrategyImplementation(databaseConnection, strategyObj, calculationUtility, isBackTestingMode); break;
            case 4: strategyImplementation =  new BollingerStrategyImplementation(databaseConnection, strategyObj, calculationUtility, isBackTestingMode); break;
            case 5: strategyImplementation =  new ClosePriceHighLowStrategyImplementation(databaseConnection, strategyObj, calculationUtility, isBackTestingMode); break;
            default : log.error("ERROR ! Unknown Strategy implementation requested. Strategy Implementation not found. Exiting");
                        System.exit(-1);
        }
        return strategyImplementation;

    }
}
