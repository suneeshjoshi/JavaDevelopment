package com.suneesh.trading.engine;

import lombok.Data;

@Data
public class NextTradeDetails {
        double amount;
        int strategyId;
        String callOrPut ;
        long contractDuration;
        int nextStepCount;
}
