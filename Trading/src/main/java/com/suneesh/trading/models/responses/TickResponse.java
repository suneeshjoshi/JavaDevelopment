package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.TickRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public String writeToDatabase(){
        return "INSERT INTO public.tick " +
                "(identifier, ask, bid, epoch, id, quote, symbol) " +
                " VALUES (" + tick.getIdentifier()+", "
                            + tick.getAsk()+", "
                            + tick.getBid()+", "
                            + tick.getEpoch()+", "
                            + tick.getId()+", "
                            + tick.getQuote()+", "
                            + tick.getSymbol() +");";
    }

}
