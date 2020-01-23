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
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private void writeToDatabase(ResponseBase objectToWrite, boolean debug){
        objectToWrite.databaseInsertStringList().forEach(f->{
            if(debug) {
                logger.debug(f);
            }
            databaseConnection.executeNoResultSet(f);
        });
    }


    public WebsocketListener(BehaviorSubject<WebsocketEvent> wsEmitter,
                             PublishSubject<String> responseEmitter,
                             PublishSubject<String> requestEmitter,
                             DatabaseConnection dbConnection){
        this.websocketEmitter = wsEmitter;
        this.responseEmitter = responseEmitter;
        this.requestEmitter = requestEmitter;
        this.databaseConnection = dbConnection;
        final AtomicInteger[] OHLC_count = {new AtomicInteger(1)};
        final Candle[] previousCandle = {new Candle()};

        this.responseEmitter.subscribe(
                o -> {
//                    logger.info("Received Message: {}", o);
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(o);
                    JSONObject echo_req = (JSONObject) jsonObject.get("echo_req");

                    if(!jsonObject.has("error") ){
                        String msg_type = jsonObject.getString("msg_type");

                        switch (msg_type) {
                            case "tick":
                                TickResponse tickResponse = new TickResponse();
                                JSONObject tickData = (JSONObject) jsonObject.get("tick");
                                if (tickData != null) {
                                    tickResponse.setTick(gson.fromJson(String.valueOf(tickData), Tick.class));
//                                    logger.info(String.valueOf(tickResponse.getTick()));
                                    writeToDatabase(tickResponse, true);
                                }

                                break;
                            case "authorize":
                                AuthorizeResponse authorizeResponse = new AuthorizeResponse();
                                JSONObject authorizeData = (JSONObject) jsonObject.get("authorize");
                                if (authorizeData != null) {
                                    authorizeResponse.setAuthorize(gson.fromJson(String.valueOf(authorizeData), Authorize.class));
//                                    logger.info(String.valueOf(authorizeResponse.getAuthorize()));
                                    writeToDatabase(authorizeResponse, true);
                                }

                                break;
                            case "balance":
                                BalanceResponse balanceResponse = new BalanceResponse();
                                JSONObject balanceData = (JSONObject) jsonObject.get("balance");
                                if (balanceData != null) {
                                    balanceResponse.setBalance(gson.fromJson(String.valueOf(balanceData), Balance.class));
//                                    logger.info(String.valueOf(balanceResponse.getBalance()));
                                    writeToDatabase(balanceResponse, true);
                                }


                                break;
                            case "candles":
                                JSONArray candleArray = (JSONArray) jsonObject.get("candles");
                                ArrayList<Candle> candleArrayList = new ArrayList<>();
                                TickHistoryResponse tickHistoryResponse = new TickHistoryResponse();
                                if (candleArray != null) {
                                    candleArray.forEach(f -> {
                                        Candle candle = gson.fromJson(String.valueOf(f), Candle.class);
                                        candle.setGranularity(echo_req.getInt("granularity"));
                                        candle.setSymbol(echo_req.getString("ticks_history"));
                                        candleArrayList.add(candle);
                                    });
                                    tickHistoryResponse.setCandles(candleArrayList);
//                                    logger.info(String.valueOf(tickHistoryResponse.getCandles()));

                                    writeToDatabase(tickHistoryResponse, true);
                                }

                                break;
                            case "ohlc":

                                TickHistoryResponse ohlcTickHistoryResponse = new TickHistoryResponse();
                                Candle newOHLCObject = new Candle();
                                JSONObject OHLCData = (JSONObject) jsonObject.get("ohlc");
                                Candle prevCandle = previousCandle[0];

                                if (OHLCData != null &&  prevCandle.getOpen()!=null ) {
                                    newOHLCObject = gson.fromJson(String.valueOf(OHLCData), Candle.class);
                                    // Write only at the start of second
//                                    Integer epoch = newOHLCObject.getEpoch();
//                                    Integer granularity = newOHLCObject.getGranularity();

                                    logger.info("{} == Received Message: {}", OHLC_count[0].get(), o);

                                    BigDecimal prevOpen = previousCandle[0].getOpen();
                                    BigDecimal newOpen = newOHLCObject.getOpen();
                                    if(newOpen.compareTo(prevOpen)!=0){
//                                    if(OHLC_count[0].get()%(granularity/2)==0) {
                                        logger.info("GOING TO WRITE OHLC CANDLE - {}", o);
//                                        Candle updatedPreviousCandle = calculateLastTickforCandle(prevCandle);
                                        Candle updatedPreviousCandle = prevCandle;

                                        logger.info("GOING TO WRITE OHLC CANDLE - {}", updatedPreviousCandle.toString());

                                        ArrayList<Candle> candles = new ArrayList<>();
                                        candles.add(updatedPreviousCandle);
                                        ohlcTickHistoryResponse.setCandles(candles);
//                                        logger.info(String.valueOf(ohlcTickHistoryResponse.getCandles()));

                                        writeToDatabase(ohlcTickHistoryResponse, true);
                                    }
                                }

                                previousCandle[0] = gson.fromJson(String.valueOf(OHLCData), Candle.class);
                                OHLC_count[0].incrementAndGet();

                                break;
                            case "transaction":
                                TransactionsStreamResponse transactionsStreamResponse = new TransactionsStreamResponse();
                                JSONObject transactionData = (JSONObject) jsonObject.get("transaction");
                                if( (transactionData != null) && ( transactionData.has("transaction_id")) ) {
                                    transactionsStreamResponse.setTransaction(gson.fromJson(String.valueOf(transactionData), Transaction.class));
                                    logger.info(String.valueOf(transactionsStreamResponse.getTransaction()));
                                    writeToDatabase(transactionsStreamResponse, true);
                                }

                                break;
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

                                    Portfolio portfolio = new Portfolio();
                                    portfolio.setContracts(portfolioTransactionList);
                                    portfolioResponse.setPortfolio(portfolio);
//                                    logger.info(String.valueOf(portfolioResponse.getPortfolio()));
                                    writeToDatabase(portfolioResponse, true);
                                }

                                break;

                            case "ping":
                                logger.info("PING Response received from Binary Websocket.");
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

    private BigDecimal getTickForEpochTime(long epochTime){
        BigDecimal quote= new BigDecimal(-1);
        List<Map<String,String>>  tickResult = databaseConnection.executeQuery(
                "select * from tick t where t.epoch = " + String.valueOf(epochTime));
        if(!CollectionUtils.isEmpty(tickResult)){
            Map<String, String> tickRow = tickResult.get(0);
            quote = new BigDecimal(tickRow.get("quote"));
        }
        return quote;
    }


    private Candle calculateLastTickforCandle(Candle prevCandle) {
        long previousCandleEpochTime;

        List<Map<String,String>> result = (List<Map<String, String>>) databaseConnection.executeQuery(
                "select epoch from candle order by identifier desc limit 1");
        if(!CollectionUtils.isEmpty(result)){
            Map<String,String> firstRow = result.get(0);
            previousCandleEpochTime = Long.valueOf(firstRow.get("epoch"));

            BigDecimal tickQuote = getTickForEpochTime(previousCandleEpochTime);

            if (tickQuote.doubleValue()!=-1) {
                if(tickQuote.doubleValue() > prevCandle.getHigh().doubleValue()) {
                    prevCandle.setHigh(tickQuote);
                }

                if(tickQuote.doubleValue() < prevCandle.getLow().doubleValue()){
                    prevCandle.setLow(tickQuote);
                }

                prevCandle.setClose(tickQuote);
            }
        }
        return prevCandle;
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
