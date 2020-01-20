package com.suneesh.trading.engine;

import com.suneesh.trading.models.requests.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class BinaryWebServiceApplication {
    Logger logger = LogManager.getLogger();

    private static ApiWrapper api;
    private static String applicationId="21829";
    private static String applicationAuthorizeToken="9s5aGYbnsUQr3Fv";
    private static BlockingQueue<RequestBase> inputMessageQueue = new LinkedBlockingQueue<>();

//    public static void init() {
//        log.info("Creating WebConnection to Binary.com ....");
//        log.info("Application ID = {}", applicationId);
//        log.info("Application Authorize Token = {}", applicationAuthorizeToken);
//
//        AuthorizeRequest authorizeRequest = new AuthorizeRequest(applicationAuthorizeToken);
//        api.sendRequest(authorizeRequest).subscribe(response -> {
//            AuthorizeResponse auth = (AuthorizeResponse) response;
//            // Authorised.
//            if (auth.getAuthorize() != null) {
//                api.sendRequest(new BalanceRequest(true));
//                api.sendRequest(new TransactionsStreamRequest());
//                api.sendRequest(new AccountStatusRequest());
//                api.sendRequest(new PortfolioRequest());
//            }
//        });
//    }
//
//    public static void getTickDetail(String symbol){
//        TickRequest request = new TickRequest(symbol);
//        api.sendRequest(request);
//
//        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
//        tickHistoryRequest.setStyle(TickStyles.CANDLES);
//        tickHistoryRequest.setSubscribe(1);
//        tickHistoryRequest.setCount(100);
//        tickHistoryRequest.setGranularity(60);
//        api.sendRequest(tickHistoryRequest);
//
//    }

    public static void main(String[] args) {
        BinaryWebServiceConnector binaryWebServiceConnector = new BinaryWebServiceConnector(inputMessageQueue,applicationId, applicationAuthorizeToken);
        binaryWebServiceConnector.init();
        binaryWebServiceConnector.getTickDetail("R_10");
        binaryWebServiceConnector.threadWork();

    }
}
