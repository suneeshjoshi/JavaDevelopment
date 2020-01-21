package com.suneesh.trading.engine;

import com.google.gson.Gson;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.WebsocketEvent;

import com.suneesh.trading.models.responses.*;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import lombok.Data;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by morteza on 7/18/2017.
 */

@Data
public class
WebsocketListener extends WebSocketListener {

    Logger logger = LoggerFactory.getLogger(WebsocketListener.class);

    private BehaviorSubject<WebsocketEvent> websocketEmitter;
    private PublishSubject<String> responseEmitter;
    private PublishSubject<String> requestEmitter;
    protected DatabaseConnection databaseConnection;

    private void writeToDatabase(ResponseBase objectToWrite){
        objectToWrite.databaseInsertStringList().forEach(f->databaseConnection.executeNoResultSet(f));
    }


    public WebsocketListener(BehaviorSubject<WebsocketEvent> wsEmitter,
                             PublishSubject<String> responseEmitter,
                             PublishSubject<String> requestEmitter,
                             DatabaseConnection dbConnection){
        this.websocketEmitter = wsEmitter;
        this.responseEmitter = responseEmitter;
        this.requestEmitter = requestEmitter;
        this.databaseConnection = dbConnection;

        this.responseEmitter.subscribe(
                o -> {
                    logger.info("Received Message: {}", o);
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(o);
                    ResponseBase responseBase = null;
                    long epochTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

                    JSONObject echo_req = (JSONObject) jsonObject.get("echo_req");

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

                                writeToDatabase(tickResponse);

                                break;
                            case "authorize":
                                AuthorizeResponse authorizeResponse = new AuthorizeResponse();
                                JSONObject authorizeData = (JSONObject) jsonObject.get("authorize");
                                if (authorizeData != null) {
                                    authorizeResponse.setAuthorize(gson.fromJson(String.valueOf(authorizeData), Authorize.class));
                                    logger.info(String.valueOf(authorizeResponse.getAuthorize()));
                                }
                                writeToDatabase(authorizeResponse);

                                break;
                            case "balance":
                                BalanceResponse balanceResponse = new BalanceResponse();
                                JSONObject balanceData = (JSONObject) jsonObject.get("balance");
                                if (balanceData != null) {
                                    balanceResponse.setBalance(gson.fromJson(String.valueOf(balanceData), Balance.class));
                                    logger.info(String.valueOf(balanceResponse.getBalance()));
                                }

                                writeToDatabase(balanceResponse);

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

                                writeToDatabase(tickHistoryResponse);

                                break;
                            case "ohlc":
                                TickHistoryResponse ohlcTickHistoryResponse = new TickHistoryResponse();
                                Candle OHLCObject = new Candle();
                                JSONObject OHLCData = (JSONObject) jsonObject.get("ohlc");
                                if (OHLCData != null) {
                                    OHLCObject = gson.fromJson(String.valueOf(OHLCData), Candle.class);
                                }

                                ArrayList<Candle> candles = new ArrayList<>();
                                candles.add(OHLCObject);
                                ohlcTickHistoryResponse.setCandles(candles);
                                logger.info(String.valueOf(ohlcTickHistoryResponse.getCandles()));

                                writeToDatabase(ohlcTickHistoryResponse);

                                break;
                            case "transaction":
                                TransactionsStreamResponse transactionsStreamResponse = new TransactionsStreamResponse();
                                JSONObject transactionData = (JSONObject) jsonObject.get("transaction");
                                if( (transactionData != null) && ( transactionData.has("transaction_id")) ) {
                                    transactionsStreamResponse.setTransaction(gson.fromJson(String.valueOf(transactionData), Transaction.class));
                                    logger.info(String.valueOf(transactionsStreamResponse.getTransaction()));
                                    writeToDatabase(transactionsStreamResponse);
                                }



                                break;
//                            case "get_account_status":
//                                AccountStatusResponse accountStatusResponse = new AccountStatusResponse();
//                                JSONObject accountStatusData = (JSONObject) jsonObject.get("get_account_status");
//                                if (accountStatusData != null) {
//                                    accountStatusResponse.setAccountStatus(gson.fromJson(String.valueOf(accountStatusData), AccountStatus.class));
//                                    logger.info(String.valueOf(accountStatusResponse.getAccountStatus()));
//                                }
//
//                                cache.writeToCache(epochTime,accountStatusResponse);
//                                break;
                            case "portfolio":
                                PortfolioResponse portfolioResponse = new PortfolioResponse();
                                JSONObject portfolioObject = (JSONObject) jsonObject.get("portfolio");

                                JSONArray contractArray = (JSONArray) portfolioObject.get("contracts");
                                ArrayList<Contract> contractArrayList = new ArrayList<>();

                                ArrayList<PortfolioTransaction> portfolioTransactionList = new ArrayList<>();

                                if (contractArray != null) {
                                    contractArray.forEach(f -> {
                                        contractArrayList.add(gson.fromJson(String.valueOf(f), Contract.class));
                                        portfolioTransactionList.add(gson.fromJson(String.valueOf(f), PortfolioTransaction.class));
                                    });
                                }

                                Portfolio portfolio = new Portfolio();
                                portfolio.setContracts(portfolioTransactionList);
                                portfolioResponse.setPortfolio(portfolio);
//                                logger.info(String.valueOf(portfolioResponse.getPortfolio()));

                                writeToDatabase(portfolioResponse);

                                break;

                            default:
                                logger.info("Case not implemented.");
                                for (Map.Entry<String, Object> entry : jsonObject.toMap().entrySet()) {
                                    logger.info("{} - {}", entry.getKey(), String.valueOf(entry.getValue()));
                                }
                        }
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
