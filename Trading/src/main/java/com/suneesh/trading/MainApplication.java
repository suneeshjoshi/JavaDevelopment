package com.suneesh.trading;

import com.suneesh.trading.models.WebsocketEvent;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.*;
import com.suneesh.trading.models.responses.*;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.LinkedHashMap;
import java.util.Map;


public class MainApplication {

    private static final Logger logger = (Logger) LogManager.getLogger();
//    protected static Map<Long, Tick> tickMap = new LinkedHashMap<>();
//    protected static Map<Long, Balance> balanceMap = new LinkedHashMap<>();
//    protected static Map<Long, Candle> candleMap = new LinkedHashMap<>();
//    protected static Map<Long, Contract> contractMap = new LinkedHashMap<>();



    public static void main(String[] args) {
        logger.info("Main Application");
        ApiWrapper api = ApiWrapper.build("21829");
//
        AuthorizeRequest authorizeRequest = new AuthorizeRequest("9s5aGYbnsUQr3Fv");
        api.sendRequest(authorizeRequest).subscribe( response -> {
            AuthorizeResponse auth = (AuthorizeResponse) response;
            // Authorised.
            if(auth.getAuthorize()!=null) {
                api.sendRequest(new BalanceRequest(true));
                api.sendRequest(new TransactionsStreamRequest());
                api.sendRequest(new AccountStatusRequest());
                api.sendRequest(new PortfolioRequest());
            }
        });


        TickRequest request = new TickRequest("R_10");
        api.sendRequest(request);

        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest("R_50", "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setSubscribe(1);
        tickHistoryRequest.setCount(100);
        tickHistoryRequest.setGranularity(60);
        api.sendRequest(tickHistoryRequest);

    }
}
