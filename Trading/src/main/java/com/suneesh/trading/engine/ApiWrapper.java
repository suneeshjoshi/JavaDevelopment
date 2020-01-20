package com.suneesh.trading.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suneesh.trading.models.WebsocketEvent;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.responses.AssetIndex;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.utils.AssetIndexDeserializer;
import com.suneesh.trading.utils.ClassUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by morteza on 7/19/2017.
 */

public class ApiWrapper {

    Logger logger = LoggerFactory.getLogger(ApiWrapper.class);

    private static ApiWrapper instance;

    private OkHttpClient client;
    private WebSocket webSocket;
    private WebsocketListener websocketListener;
    private String websocketUrl;
    public BehaviorSubject<WebsocketEvent> websocketEmitter = BehaviorSubject.create();
    private PublishSubject<String> responseEmitter = PublishSubject.create();
    private PublishSubject<String> requestEmitter = PublishSubject.create();
    protected static Map<Long, ResponseBase> cache = new LinkedHashMap<>();

    private ApiWrapper(String applicationId, String language, String url){
        this.websocketUrl = url + String.format("?app_id=%s&l=%s", applicationId, language);

        try {
            logger.info("Attempting to connect to Binary WebSocket, url = {}", websocketUrl);
            this.connect();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }

        this.websocketEmitter.subscribe(e -> {
            final int maxAttemptCount = 100;
            int attemptCount = 1;
            while(!e.isOpened() && attemptCount < maxAttemptCount) {
                logger.info("Attempting to connect to Binary WebSocket, url = {}. Attempt : {}", websocketUrl, attemptCount);
                this.connect();
                Thread.sleep(500);
                attemptCount++;
            }

            if(attemptCount >= maxAttemptCount){
                logger.info("ERROR ! Unable to connect to remote Binary WebSocket. url = {}", websocketUrl);

            }
        });

    }

    private void connect() throws IOException {
        if (this.client != null) {
            client.connectionPool().evictAll();
            client.dispatcher().executorService().shutdown();
        }
        this.client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        final Request request = new Request.Builder()
                .url(websocketUrl)
                .build();

        this.websocketListener = new WebsocketListener(this.websocketEmitter,
                this.responseEmitter, this.requestEmitter);

        this.webSocket = this.client.newWebSocket(request, websocketListener);
    }

    public static ApiWrapper build(String applicationId){
        return build(applicationId, "en", "wss://ws.binaryws.com/websockets/v3");
    }

    /**
     * Create or retrieve an instance of ApiWrapper class
     * @param applicationId
     * @param language
     * @param url
     * @return An Instance of ApiWrapper class
     */
    public static ApiWrapper build(String applicationId, String language, String url){
        if (instance == null) {
            instance = new ApiWrapper(applicationId, language, url);
        }
        return instance;
    }

    /**
     * Send a request to the server
     * @param request
     * @return An observale
     */
    public Observable<ResponseBase> sendRequest(RequestBase request){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AssetIndex.class, new AssetIndexDeserializer());
        Gson gson = gsonBuilder.create();
        this.websocketEmitter
                .filter(o -> {
                    return o.isOpened();
                })
                .take(1)
                .subscribe(
                o -> {
                    if(o.isOpened()) {
                        this.requestEmitter.onNext(gson.toJson(request));

                    }
                }
        );

        return instance.responseEmitter
                .filter((String o) -> {
                    ResponseBase response = gson.fromJson(o, request.getResponseType());

                    //FIXME Add req_id to the condition if it is exist
                    return request.getResponseType() == ClassUtils.getClassType(response.getType());
                })
                .map(o -> {
//                    ResponseBase response = gson.fromJson(o, request.getResponseType());
                    return gson.fromJson(o, request.getResponseType());
                });
    }


    public void closeConnection() {
        this.requestEmitter.onComplete();
        this.responseEmitter.onComplete();
        this.websocketEmitter.onComplete();
        this.webSocket.cancel();
        instance = null;
    }

}
