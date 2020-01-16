package com.suneesh.trading;

import com.google.gson.Gson;
import com.suneesh.trading.models.WebsocketEvent;

import com.suneesh.trading.models.responses.ResponseBase;
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
                    Gson gson  = new Gson();

                    JSONObject jsonObject = new JSONObject(o);
//                    logger.info(jsonObject.toString(2));

                    Map<String, Object> stringObjectMap = jsonObject.toMap();
                    for(Map.Entry<String, Object> entry : stringObjectMap.entrySet()){
                        Object value = entry.getValue();
                        String key = entry.getKey();

                        logger.info("{} - {}",entry.getKey(),String.valueOf(entry.getValue()));
                    }


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
