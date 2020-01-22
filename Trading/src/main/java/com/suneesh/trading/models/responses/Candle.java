package com.suneesh.trading.models.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

import static java.math.BigDecimal.*;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/3/2017
 */

@Data
public class Candle {
    /**
     * It is an epoch value
     */
    @SerializedName("epoch")
    private Integer epoch;

    /**
     * It is the open price value for the given time
     */
    @SerializedName("open")
    private BigDecimal open;

    /**
     * It is the high price value for the given time
     */
    @SerializedName("high")
    private BigDecimal high;

    /**
     * It is the low price value for the given time
     */
    @SerializedName("low")
    private BigDecimal low;

    /**
     * It is the close price value for the given time
     */
    @SerializedName("close")
    private BigDecimal close;

    @SerializedName("granularity")
    private Integer granularity;

    @SerializedName("symbol")
    private String symbol;

    public String getDirection() {
        return direction;
    }

    private String direction;

    public void setDirection() {
        this.direction = (close.compareTo(open)>0) ? "UP" : "DOWN";;
    }

    public Integer getEpoch() {
        return epoch;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }
}
