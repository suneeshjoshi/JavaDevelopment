package com.suneesh.trading.engine;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.models.enums.TickStyles;
import com.suneesh.trading.models.requests.BuyContractParameters;
import com.suneesh.trading.models.requests.BuyContractRequest;
import com.suneesh.trading.models.requests.RequestBase;
import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Data
public class BackTestingCalculationEngine extends CalculationEngine{
    private static final Logger logger = LogManager.getLogger();
    private String symbol;
    private DatabaseConnection databaseConnection;
    final int CANDLE_DATA_POINTS=5000;

    public BackTestingCalculationEngine(BlockingQueue<RequestBase> inputMessageQueue, DatabaseConnection dbConnection, String symbol) {
        super(inputMessageQueue, dbConnection, symbol);
        this.databaseConnection = dbConnection;
        this.symbol = symbol;
    }

    public void getCandleDetailsFromBinaryWS(String symbol, int candleDataPoints) {
        TickHistoryRequest tickHistoryRequest = new TickHistoryRequest(symbol, "latest");
        tickHistoryRequest.setStyle(TickStyles.CANDLES);
        tickHistoryRequest.setCount(5000);
        tickHistoryRequest.setGranularity(60);
        sendRequest(tickHistoryRequest);
    }

    private List getCandleDataFromDB(){
        return getDatabaseConnection().executeQuery("Select * from candle order by identifier ASC");
    }

    void getCallOrPutFromCandleData(NextTradeDetails nextTradeDetails, Map<String,String> candleData){
        String callOrPutResult = "CALL";
        if(!MapUtils.isEmpty(candleData)) {
            String previousCandleDirection = candleData.get("direction");

            log.debug("************ : {}", previousCandleDirection);
            for (Map.Entry<String, String> entry : candleData.entrySet()) {
                log.debug("{} - {} ", entry.getKey(), entry.getValue());
            }

            callOrPutResult = previousCandleDirection.equalsIgnoreCase("UP") ? "CALL" : "PUT";
        }
        nextTradeDetails.setCallOrPut(callOrPutResult);
//        return callOrPutResult;
    }


    private void createDummyTrade(Map<String,String> candleData){
        try {
            String currency = calculationEngineUtility.getCurrency();
            if(currency.isEmpty()){
                logger.fatal("FATAL ERROR! No Currency detail found. Cannot book trade.\nExiting application.");
                System.exit(-1);
            }

            Map<String, String> lastTrade = calculationEngineUtility.getLastTrade();
            long lastTradeId = 0L;
            if(!MapUtils.isEmpty(lastTrade)){
                lastTradeId = Long.valueOf(lastTrade.get("identifier"));
            }

            NextTradeDetails nextTradeDetails = new NextTradeDetails(lastTradeId);
            getCallOrPutFromCandleData(nextTradeDetails,candleData);
            calculationEngineUtility.getContractDuration(nextTradeDetails);
            calculationEngineUtility.getNextStepCount(nextTradeDetails, lastTrade);
            calculationEngineUtility.getNextTradeStrategyId(nextTradeDetails, lastTrade);
            calculationEngineUtility.getBidAmount(nextTradeDetails);

            BuyContractParameters parameters = calculationEngineUtility.getParameters(symbol, nextTradeDetails, currency);
            BuyContractRequest buyContractRequest = new BuyContractRequest(new BigDecimal(nextTradeDetails.getAmount()), parameters, nextTradeDetails.getTradeId());

            String tradeInsertStatement = calculationEngineUtility.getTradeDatabaseInsertString(parameters, nextTradeDetails);
            logger.debug(tradeInsertStatement);
            calculationEngineUtility.getDatabaseConnection().executeNoResultSet(tradeInsertStatement);

            logger.info("SKIPPING Sending buy Contract Request as runing in BACKTESTING MODE... ");
        }
        catch (Exception e ){
            logger.info("Exception caught while create new trade. {}",e.getMessage());
            e.printStackTrace();
        }

    }


    public void process(){
        log.info("Request sent to Binary Websocket to get last {} candle data points ...");
        getCandleDetailsFromBinaryWS(symbol,CANDLE_DATA_POINTS);

        log.info("getting candle data from DB ...");

        log.info("Waiting to receive data from Binary WS ...");
        AutoTradingUtility.sleep(5000);
        List<Map<String,String>> candleDataFromDB = getCandleDataFromDB();

        log.info("Received {} candle data points", candleDataFromDB.size());

        for(Map<String,String> row : candleDataFromDB){
            for(Map.Entry<String,String> entry : row.entrySet()){
                log.info("{} - {} ", entry.getKey(), entry.getValue());
            }

            createDummyTrade(row);


        }


        System.exit(-1);
    }
}
