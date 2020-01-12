package com.suneesh.trading;

import com.suneesh.trading.models.requests.AccountStatusRequest;
import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.suneesh.trading.models.requests.BalanceRequest;
import com.suneesh.trading.models.requests.TickRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApplication {

    static Logger logger = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        logger.info("Main Application");
        ApiWrapper api = ApiWrapper.build("21829");

        TickRequest request = new TickRequest("R_10");
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
