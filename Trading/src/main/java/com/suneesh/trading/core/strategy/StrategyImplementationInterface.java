package com.suneesh.trading.core.strategy;

import com.suneesh.trading.core.NextTradeDetails;
import com.suneesh.trading.models.Strategy;
import lombok.Data;

import java.util.Map;

public interface StrategyImplementationInterface {
    void getCallOrPut(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle);
    void getContractDuration(NextTradeDetails nextTradeDetails);
    void getNextStepCount(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade);
    void getNextTradeStrategyId(NextTradeDetails nextTradeDetails, Map<String, String> lastTrade);
    void getBidAmount(NextTradeDetails nextTradeDetails, Map<String, String> lastCandle);

    default boolean bookTrade(Map<String, String> lastTrade, Map<String, String> lastCandle){return true;}
}
