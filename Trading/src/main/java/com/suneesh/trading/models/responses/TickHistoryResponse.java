package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.TickHistoryRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;
import io.reactivex.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>TickHistoryResponse</h1>
 *
 * <h2>Tick History Response</h2>
 * <p>Historic tick data for a single symbol</p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/3/2017
 */
public class TickHistoryResponse extends ResponseBase<TickHistoryRequest> {

    /**
     * Historic tick data for a given symbol
     */
    @SerializedName("history")
    @Nullable
    private History history;

    public void setCandles(List<Candle> candles) {
        this.candles = candles;
    }

    /**
     * Array of OHLC (open/high/low/close) price values for the given time (only for style='candles')
     */
    @SerializedName("candles")
    @Nullable
    private List<Candle> candles;

    public History getHistory() {
        return history;
    }

    public List<Candle> getCandles() {
        return candles;
    }

    @Override
    public List<String> databaseInsertStringList(){
        return candles.stream().map(candle->{
                    candle.setDirection();
                    candle.setOpenCloseDiff();
                    candle.setWriteTimeEpoch();

                    return "INSERT INTO public.candle " +
                            "(close, epoch, high, low, open, granularity, symbol, direction, open_close_diff, open_time) " +
                            " VALUES ("
                            + AutoTradingUtility.quotedString(candle.getClose()) + ", "
                            + AutoTradingUtility.quotedString(candle.getEpoch()) + ", "
                            + AutoTradingUtility.quotedString(candle.getHigh()) + ", "
                            + AutoTradingUtility.quotedString(candle.getLow()) + ", "
                            + AutoTradingUtility.quotedString(candle.getOpen()) + ", "
                            + AutoTradingUtility.quotedString(candle.getGranularity()) + ", "
                            + AutoTradingUtility.quotedString(candle.getSymbol()) + ", "
                            + AutoTradingUtility.quotedString(candle.getDirection()) + ","
                            + AutoTradingUtility.quotedString(candle.getOpenCloseDiff()) + ","
                            + candle.getOpen_time()+ ");";
        }
        ).collect(Collectors.toList());

    }

}
