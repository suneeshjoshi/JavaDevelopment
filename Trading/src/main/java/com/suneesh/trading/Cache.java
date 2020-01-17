package com.suneesh.trading;

import com.suneesh.trading.models.responses.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {
    protected static Map<Long, TickResponse> tickCache;
    protected static Map<Long, TickHistoryResponse> tickHistoryCache;
    protected static Map<Long, AuthorizeResponse> authorizeResponseCache;
    protected static Map<Long, BalanceResponse> balanceResponseCache;
    protected static Map<Long, TransactionsStreamResponse> transactionsStreamResponseCache;
    protected static Map<Long, PortfolioResponse> portfolioResponseCache;
    protected static Map<Long, AccountStatusResponse> accountStatusResponseCache;

    public static void writeToCache(Long epochTime, Object response) {
        switch(response.getClass().getSimpleName()){
            case "TickResponse": tickCache.put(epochTime, (TickResponse) response); break;
            case "AuthorizeResponse": authorizeResponseCache.put(epochTime, (AuthorizeResponse) response); break;
            case "BalanceResponse": balanceResponseCache.put(epochTime, (BalanceResponse) response); break;
            case "TransactionsStreamResponse": transactionsStreamResponseCache.put(epochTime, (TransactionsStreamResponse) response); break;
            case "PortfolioResponse": portfolioResponseCache.put(epochTime, (PortfolioResponse) response); break;
            case "AccountStatusResponse": accountStatusResponseCache.put(epochTime, (AccountStatusResponse) response); break;
            case "TickHistoryResponse": tickHistoryCache.put(epochTime, (TickHistoryResponse) response); break;
        }
    }

    public static void initialize(){
        tickCache = new LinkedHashMap<>();
        tickHistoryCache = new LinkedHashMap<>();
        authorizeResponseCache = new LinkedHashMap<>();
        balanceResponseCache = new LinkedHashMap<>();
        transactionsStreamResponseCache = new LinkedHashMap<>();
        portfolioResponseCache = new LinkedHashMap<>();
        accountStatusResponseCache = new LinkedHashMap<>();
    }


}
