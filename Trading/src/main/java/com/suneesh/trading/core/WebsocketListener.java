package com.suneesh.trading.core;

import com.google.gson.Gson;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.core.calculations.Signals;
import com.suneesh.trading.core.calculations.Utility;
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

        // Calculate and write Bolling Bands for the received Candle data.
        Utility calculationEngineUtility = new Utility(databaseConnection);

        // Calculate and write Bolling Bands for the received Candle data.
        Signals signals = new Signals(databaseConnection);

        AtomicInteger ohlcCount = new AtomicInteger(0);

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
                                    writeToDatabase(tickResponse, false);
                                }

                                break;
                            case "authorize":
                                AuthorizeResponse authorizeResponse = new AuthorizeResponse();
                                JSONObject authorizeData = (JSONObject) jsonObject.get("authorize");
                                if (authorizeData != null) {
                                    authorizeResponse.setAuthorize(gson.fromJson(String.valueOf(authorizeData), Authorize.class));
//                                    logger.info(String.valueOf(authorizeResponse.getAuthorize()));
                                    writeToDatabase(authorizeResponse, false);
                                }

                                break;
                            case "balance":
                                BalanceResponse balanceResponse = new BalanceResponse();
                                JSONObject balanceData = (JSONObject) jsonObject.get("balance");
                                if (balanceData != null) {
                                    balanceResponse.setBalance(gson.fromJson(String.valueOf(balanceData), Balance.class));
//                                    logger.info(String.valueOf(balanceResponse.getBalance()));
                                    writeToDatabase(balanceResponse, false);
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

                                    writeToDatabase(tickHistoryResponse, false);
                                }

                                signals.calculateBollingerBands(calculationEngineUtility.getCandles(Optional.of("DESC"), Optional.empty()), Optional.of("CANDLES"));

                                break;
                            case "ohlc":

                                TickHistoryResponse ohlcTickHistoryResponse = new TickHistoryResponse();
                                Candle newOHLCObject = new Candle();
                                JSONObject OHLCData = (JSONObject) jsonObject.get("ohlc");
                                Candle prevCandle = previousCandle[0];

                                if (OHLCData != null &&  prevCandle.getOpen()!=null ) {
                                    newOHLCObject = gson.fromJson(String.valueOf(OHLCData), Candle.class);
                                    BigDecimal prevOpen = previousCandle[0].getOpen();
                                    BigDecimal newOpen = newOHLCObject.getOpen();
                                    if(newOpen.compareTo(prevOpen)!=0){
                                        Candle updatedPreviousCandle = prevCandle;
                                        ArrayList<Candle> candles = new ArrayList<>();
                                        candles.add(updatedPreviousCandle);
                                        ohlcTickHistoryResponse.setCandles(candles);
//                                        logger.info(String.valueOf(ohlcTickHistoryResponse.getCandles()));

                                        // If this is the first OHLC candle received, then we will have a duplicate candle record
                                        // all the candles received in "CANDLE" message contais an incomplete last candle , which this OHLC candle is completing
                                        if(ohlcCount.get()==0){
                                            databaseConnection.executeNoResultSet("DELETE FROM candle WHERE epoch = "+updatedPreviousCandle.getOpen_time());
                                        }
                                        writeToDatabase(ohlcTickHistoryResponse, false);
                                        signals.calculateBollingerBands(calculationEngineUtility.getCandles(Optional.of("DESC"),Optional.of(20)) , Optional.empty());
                                        ohlcCount.incrementAndGet();
                                    }
                                }

                                previousCandle[0] = gson.fromJson(String.valueOf(OHLCData), Candle.class);

                                break;
                            case "transaction":
                                logger.info("Received Message: {}", o);

                                TransactionsStreamResponse transactionsStreamResponse = new TransactionsStreamResponse();
                                JSONObject transactionData = (JSONObject) jsonObject.get("transaction");

                                if( (transactionData != null) && ( transactionData.has("transaction_id")) ) {

                                    processTransaction(msg_type, transactionData, null);

                                    transactionsStreamResponse.setTransaction(gson.fromJson(String.valueOf(transactionData), Transaction.class));
                                    logger.info(String.valueOf(transactionsStreamResponse.getTransaction()));
                                    writeToDatabase(transactionsStreamResponse, true);
                                }

                                break;
                            case "buy":
                                BuyContractResponse buyContractResponse = new BuyContractResponse();
                                JSONObject buyData = (JSONObject) jsonObject.get("buy");

                                if( (buyData != null) && ( buyData.has("contract_id")) ) {
                                    Integer req_id = (Integer) jsonObject.get("req_id");

                                    processTransaction(msg_type, buyData, req_id);

                                    BuyContractResponse buyContractResponse1 = gson.fromJson(String.valueOf(buyData), BuyContractResponse.class);
                                    logger.info("BuyContractResponse =  {}",buyContractResponse1.toString());
                                }

                                break;

                            case "sell":
                                SellContractResponse sellContractResponse = new SellContractResponse();
                                JSONObject sellData = (JSONObject) jsonObject.get("buy");

                                if( (sellData != null) && ( sellData.has("contract_id")) ) {
                                    Integer req_id = (Integer) jsonObject.get("req_id");

                                    processTransaction(msg_type, sellData, req_id);

                                    SellContractResponse sellContractResponse1 = gson.fromJson(String.valueOf(sellData), SellContractResponse.class);
                                    logger.info("SellContractResponse =  {}",sellContractResponse1.toString());
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
                                    writeToDatabase(portfolioResponse, false);
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

    private void processTransaction(String msg_type, JSONObject jsonData, Integer req_id) {
        String tradeIdentifier = null;
        String updateString = null;
        long contract_id = jsonData.getLong("contract_id");

        if(req_id!=null){
            tradeIdentifier = String.valueOf(req_id);
            if (tradeIdentifier == null) {
                logger.error("ERROR! req_id field not having data in transaction Response. Reverting to using 'contract is NULL & result is NULL clause'");
            }
        }

        switch(msg_type){
            case "buy":
                if(tradeIdentifier==null){
                    updateString= "UPDATE trade SET contract_id = '"+String.valueOf(contract_id)+"', result='OPEN' WHERE contract_id is null AND result is null";
                }
                else {
                    updateString = "UPDATE trade SET contract_id = '" + String.valueOf(contract_id) + "', result='OPEN' WHERE identifier = " + tradeIdentifier;
                }
                break;
            case "transaction":
                String action = jsonData.getString("action");
                if(action.equalsIgnoreCase("sell")) {
                    BigDecimal amount = jsonData.getBigDecimal("amount");
                    String tradeResult = amount.doubleValue() > 0 ? "SUCCESS" : "FAIL";
                    if (tradeIdentifier == null) {
                        updateString = "UPDATE trade SET result='" + tradeResult + "', amount_won = '" + amount.toPlainString() + "' WHERE contract_id ='" + String.valueOf(contract_id) + "' AND result ='OPEN'";
                    } else {
                        updateString = "UPDATE trade SET result='" + tradeResult + "', amount_won = '" + amount.toPlainString() + "' WHERE identifier = " + tradeIdentifier;
                    }
                }
                break;
            default : logger.info("Unhandled action . action = {}",msg_type);
        }

        if(updateString!=null && !updateString.isEmpty()){
            logger.info("action / Command = {} / {}", msg_type, updateString);
            databaseConnection.executeNoResultSet(updateString);
        }
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
