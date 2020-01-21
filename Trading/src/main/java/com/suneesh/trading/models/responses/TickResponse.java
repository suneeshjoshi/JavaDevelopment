package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.TickRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;

import java.util.Arrays;
import java.util.List;

/**
 * Created by morteza on 7/19/2017.
 */

public class TickResponse extends ResponseBase<TickRequest> {
    @SerializedName("tick")
    @Expose
    private
    Tick tick;

    public Tick getTick() {
        return tick;
    }

    public void setTick(Tick tick) {
        this.tick = tick;
    }

    @Override
    public List<String> databaseInsertStringList(){
        return Arrays.asList("INSERT INTO public.tick " +
                "(ask, bid, epoch, id, quote, symbol) " +
                " VALUES (" + AutoTradingUtility.quotedString(tick.getAsk())+", "
                            + AutoTradingUtility.quotedString(tick.getBid())+", "
                            + AutoTradingUtility.quotedString(tick.getEpoch())+", "
                            + AutoTradingUtility.quotedString(tick.getId())+", "
                            + AutoTradingUtility.quotedString(tick.getQuote())+", "
                            + AutoTradingUtility.quotedString(tick.getSymbol()) +");");

    }

}
