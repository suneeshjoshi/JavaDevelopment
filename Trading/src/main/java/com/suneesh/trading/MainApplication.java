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

        TickRequest request = new TickRequest("R_10");
        api.sendRequest(request);

        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest("R_50", "latest");
        TestObserver<ResponseBase> testObserver = new TestObserver<>();
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setSubscribe(1);
        tickHistoryRequest.setGranularity(60);

        api.sendRequest(tickHistoryRequest);

        //
//        api.sendRequest(request)
//                .subscribe(
//                        response -> {
//                            logger.info(String.valueOf(response));
//
////                            System.out.printf("epoch: %S, price: %s",
////                                    response.getTick().getEpoch(), responce.getTick().getQuote());
//                        }
//                );

        logger.info("Going to make another call.");

        AuthorizeRequest authorizeRequest = new AuthorizeRequest("9s5aGYbnsUQr3Fv");
        api.sendRequest(authorizeRequest).subscribe( response -> {
            api.sendRequest(new BalanceRequest(true));
        });
//
//
//        AccountStatusRequest accountStatusRequest = new AccountStatusRequest();
//        api.sendRequest(accountStatusRequest);
//
//        api.sendRequest(new BalanceRequest(true));



    }
}
