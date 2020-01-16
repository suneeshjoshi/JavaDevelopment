package com.suneesh.trading;

import com.google.gson.Gson;
import com.suneesh.trading.models.WebsocketEvent;

import com.suneesh.trading.models.requests.AccountStatusRequest;
import com.suneesh.trading.models.requests.TickRequest;
import com.suneesh.trading.models.responses.*;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by morteza on 7/18/2017.
 */

public class
WebsocketListener extends WebSocketListener {

    Logger logger = LoggerFactory.getLogger(WebsocketListener.class);

    private BehaviorSubject<WebsocketEvent> websocketEmitter;
    private PublishSubject<String> responseEmitter;
    private PublishSubject<String> requestEmitter;

    protected Map<Long, ResponseBase> cache;


    public WebsocketListener(BehaviorSubject<WebsocketEvent> wsEmitter,
                             PublishSubject<String> responseEmitter,
                             PublishSubject<String> requestEmitter,
                             Map<Long,ResponseBase> cache) {
        this(wsEmitter, responseEmitter, requestEmitter);
        this.cache = cache;
    }

    public WebsocketListener(BehaviorSubject<WebsocketEvent> wsEmitter,
                             PublishSubject<String> responseEmitter,
                             PublishSubject<String> requestEmitter){
        this.websocketEmitter = wsEmitter;
        this.responseEmitter = responseEmitter;
        this.requestEmitter = requestEmitter;

        this.responseEmitter.subscribe(
                o -> {
//                    logger.info("Received Message: {}", o);
                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(o);
//                    logger.info(jsonObject.toString(2));


                    if(!jsonObject.has("error") ){
                        String msg_type = jsonObject.getString("msg_type");

                        switch (msg_type) {
                            case "tick":
                                TickResponse tickResponse = new TickResponse();
                                JSONObject tickData = (JSONObject) jsonObject.get("tick");
                                if (tickData != null) {
                                    tickResponse.setTick(gson.fromJson(String.valueOf(tickData), Tick.class));
                                    logger.info(String.valueOf(tickResponse.getTick()));
                                }
                                break;
                            case "authorize":
                                AuthorizeResponse authorizeResponse = new AuthorizeResponse();
                                JSONObject authorizeData = (JSONObject) jsonObject.get("authorize");
                                if (authorizeData != null) {
                                    authorizeResponse.setAuthorize(gson.fromJson(String.valueOf(authorizeData), Authorize.class));
                                    logger.info(String.valueOf(authorizeResponse.getAuthorize()));
                                }
                                break;
                            case "balance":
                                BalanceResponse balanceResponse = new BalanceResponse();
                                JSONObject balanceData = (JSONObject) jsonObject.get("balance");
                                if (balanceData != null) {
                                    balanceResponse.setBalance(gson.fromJson(String.valueOf(balanceData), Balance.class));
                                    logger.info(String.valueOf(balanceResponse.getBalance()));
                                }
                                break;
                            case "candles":
                                JSONArray candleArray = (JSONArray) jsonObject.get("candles");
                                ArrayList<Candle> candleArrayList = new ArrayList<>();
                                TickHistoryResponse tickHistoryResponse = new TickHistoryResponse();
                                if (candleArray != null) {
                                    candleArray.forEach(f -> {
                                        candleArrayList.add(gson.fromJson(String.valueOf(f), Candle.class));
                                    });
                                }
                                tickHistoryResponse.setCandles(candleArrayList);
                                logger.info(String.valueOf(tickHistoryResponse.getCandles()));
                                break;
                            case "ohlc":
                                Candle OHLCObject = new Candle();
                                JSONObject OHLCData = (JSONObject) jsonObject.get("ohlc");
                                if (OHLCData != null) {
                                    OHLCObject = gson.fromJson(String.valueOf(OHLCData), Candle.class);
                                }
                                logger.info(String.valueOf(OHLCObject));
                                break;
                            case "transaction":
                                TransactionsStreamResponse transactionsStreamResponse = new TransactionsStreamResponse();
                                JSONObject transactionData = (JSONObject) jsonObject.get("transaction");
                                if (transactionData != null) {
                                    transactionsStreamResponse.setTransaction(gson.fromJson(String.valueOf(transactionData), Transaction.class));
                                    logger.info(String.valueOf(transactionsStreamResponse.getTransaction()));
                                }
                                break;
                            case "get_account_status":
                                AccountStatusResponse accountStatusResponse = new AccountStatusResponse();
                                JSONObject accountStatusData = (JSONObject) jsonObject.get("get_account_status");
                                if (accountStatusData != null) {
                                    accountStatusResponse.setAccountStatus(gson.fromJson(String.valueOf(accountStatusData), AccountStatus.class));
                                    logger.info(String.valueOf(accountStatusResponse.getAccountStatus()));
                                }
                                break;
                            case "portfolio":
                                JSONArray contractArray = (JSONArray) jsonObject.get("contracts");
                                ArrayList<Candle> contractArrayList = new ArrayList<>();
//                                TickHistoryResponse tickHistoryResponse = new TickHistoryResponse();
//                                if (candleArray != null) {
//                                    candleArray.forEach(f -> {
//                                        candleArrayList.add(gson.fromJson(String.valueOf(f), Candle.class));
//                                    });
//                                }
//                                tickHistoryResponse.setCandles(candleArrayList);
//                                logger.info(String.valueOf(tickHistoryResponse.getCandles()));
                                break;

                            default:
                                logger.info("Case not implemented.");
                                for (Map.Entry<String, Object> entry : jsonObject.toMap().entrySet()) {
                                    logger.info("{} - {}", entry.getKey(), String.valueOf(entry.getValue()));
                                }
                        }
                        //                    cache.put(1,o);


                    }
                    else{
                        Object jsonException = jsonObject.get("error");
                        logger.error(String.valueOf(jsonException));
                    }
                }
        );
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        logger.info("Connection is opened!");
        WebsocketEvent wse = new WebsocketEvent(true, null);
        this.requestEmitter.subscribe( request -> {
            webSocket.send(request);
        });
        this.websocketEmitter.onNext(wse);
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {

        this.responseEmitter.onNext(message);

    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        logger.info("Connection closed: {}", reason);
        this.websocketEmitter.onNext(new WebsocketEvent(false, reason));
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        logger.info("Connection failed: {}", response != null ? response.message() : "");
        this.websocketEmitter.onNext(new WebsocketEvent(false, response != null ? response.message() : ""));
    }

}
