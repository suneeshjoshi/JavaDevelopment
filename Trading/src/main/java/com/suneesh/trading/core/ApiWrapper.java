package com.suneesh.trading.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.WebsocketEvent;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.responses.AssetIndex;
import com.suneesh.trading.models.responses.ResponseBase;
import com.suneesh.trading.utils.AssetIndexDeserializer;
import com.suneesh.trading.utils.AutoTradingUtility;
import com.suneesh.trading.utils.ClassUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;


import java.io.IOException;

/**
 * Created by morteza on 7/19/2017.
 */
@Data
@Slf4j
public class ApiWrapper {

    private static ApiWrapper instance;

    private OkHttpClient client;
    private WebSocket webSocket;
    private WebsocketListener websocketListener;
    private String websocketUrl;
    public BehaviorSubject<WebsocketEvent> websocketEmitter = BehaviorSubject.create();
    private PublishSubject<String> responseEmitter = PublishSubject.create();
    private PublishSubject<String> requestEmitter = PublishSubject.create();
    protected String applicationId;
    final private String WEBSOCKET_ERROR = "Binary WebSocket connection failure";

    protected DatabaseConnection databaseConnection;


    private ApiWrapper(String applicationId, String language, String url, DatabaseConnection dbConnection){
        this.databaseConnection = dbConnection;
        this.websocketUrl = url + String.format("?app_id=%s&l=%s", applicationId, language);
        this.applicationId = applicationId;

        try {
            log.info("Attempting to connect to Binary WebSocket, url = {}", websocketUrl);
            this.connect();
        } catch (IOException e) {
            log.debug(e.getMessage());
        }

        this.websocketEmitter.subscribe(e -> {

            log.info("Connection status = {}", e.isOpened());
            if(!e.isOpened()) {
                if (!e.isOpened() ) {
                    log.info("Attempting to reconnect to Binary WebSocket, url = {}. Attempt : {}", websocketUrl);
                    this.connect();
                    Thread.sleep(1000);
                }

                if (!e.isOpened() ) {
                    log.info("Reconnect attempt failed. Exiting application.");
                    databaseConnection.recordInDBAndExit("insert into error_table (error_message , status, creation_time) VALUES ("+AutoTradingUtility.quotedString(WEBSOCKET_ERROR)+", 'ACTIVE', now())");
                }
            }
        });
    }

    public static ApiWrapper build(String applicationId, DatabaseConnection databaseConnection) {
        return build(applicationId, "en", "wss://ws.binaryws.com/websockets/v3", databaseConnection);
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
                this.responseEmitter, this.requestEmitter, this.databaseConnection);

        this.webSocket = this.client.newWebSocket(request, websocketListener);
    }

    public static ApiWrapper build(String applicationId){
        return build(applicationId, "en", "wss://ws.binaryws.com/websockets/v3",null);
    }

    /**
     * Create or retrieve an instance of ApiWrapper class
     * @param applicationId
     * @param language
     * @param url
     * @return An Instance of ApiWrapper class
     */
    public static ApiWrapper build(String applicationId, String language, String url, DatabaseConnection dbConnection){
        if (instance == null) {
            instance = new ApiWrapper(applicationId, language, url, dbConnection);
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
