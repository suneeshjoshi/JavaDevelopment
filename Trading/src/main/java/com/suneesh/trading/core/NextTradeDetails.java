package com.suneesh.trading.core;

import lombok.Data;

@Data
public class NextTradeDetails {
        long tradeId;
        double amount;
        int strategyId;
        String callOrPut ;
        long contractDuration;
        int nextStepCount;

        public NextTradeDetails(long lastTradeId){
                setTradeId(lastTradeId+1);
        }
}
