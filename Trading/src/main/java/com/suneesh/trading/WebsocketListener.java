package com.suneesh.trading;

import com.google.gson.Gson;
import com.suneesh.trading.models.WebsocketEvent;

import com.suneesh.trading.models.requests.TickRequest;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.models.responses.Tick;
import com.suneesh.trading.models.responses.TickResponse;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

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
                    logger.info("Received Massage: {}", o);

//                    if(o.startsWith("{\"candles\"")){
//                        CandlesResponse candlesResponse = gson.fromJson(o, CandlesResponse.class);
//                    } else if( o.contains("\"msg_type\":\"ohlc\"")){
//                        OhlcStreamResponse ohlcStreamResponse = gson.fromJson(o,OhlcStreamResponse.class);
//                    } else if(o.startsWith("{\"balance\":{\"balance\":")){
//                        BalanceWrapper balanceWrapper = gson.fromJson(o,BalanceWrapper.class);
//                    } else if(o.contains("\"msg_type\":\"portfolio\"")){
//                        PortfolioWrapper portfolioWrapper= gson.fromJson(o,PortfolioWrapper.class);
//                    } else if(o.contains("\"msg_type\":\"transaction\"")){
//                        TransactionWrapper transactionWrapper= gson.fromJson(o, TransactionWrapper.class);
//                    } else if(o.contains("\"msg_type\":\"tick\"")){
//                        String s1 = gson.toJson(o);
//                        JsonElement jsonElement = gson.toJsonTree(o);
//                        StringTokenizer st = new StringTokenizer(o, ", ");
//                        while(st.nextElement()!=null){
//                            logger.info(st.nextToken());
//                        }
//                        Tick tickWrapper= gson.fromJson(o, Tick.class);
//                        logger.info("after making object");
//                    }

                    Gson gson = new Gson();

                    JSONObject jsonObject = new JSONObject(o);
//                    logger.info(jsonObject.toString(2));

                    String msg_type = jsonObject.getString("msg_type");
                    logger.info(msg_type);

                    switch(msg_type){
                        case "tick":
                            Tick tickObject = null;
                            JSONObject tick_data = (JSONObject) jsonObject.get("tick");
                            if(tick_data!=null) {
                                tickObject = gson.fromJson(String.valueOf(tick_data), Tick.class);
                                logger.info(String.valueOf(tickObject));
                            }
                        break;

                    case "candles": logger.info("This is candles class.");
                        break;
                    case "ohlc": logger.info("This is ohlc class.");
                        break;
                    case "balance": logger.info("This is balance class.");
                        break;
                    case "transaction": logger.info("This is transaction class.");
                        break;
                    default: logger.info("Case not implemented.");

                    }


                    Map<String, Object> stringObjectMap = jsonObject.toMap();
                    for(Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                        String key = entry.getKey();
                        String value = String.valueOf(entry.getValue());

                        if (key.equalsIgnoreCase("msg_type")) {
                            String objectType = value;
                        }
                        logger.info("{} - {}",key,String.valueOf(value));

                    }
//




                    //                    cache.put(1,o);
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
