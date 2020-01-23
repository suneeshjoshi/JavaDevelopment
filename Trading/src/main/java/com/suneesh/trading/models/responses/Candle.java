package com.suneesh.trading.models.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

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

    @SerializedName("open_time")
    private Integer open_time;
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

    @SerializedName("direction")
    private String direction;

    private BigDecimal openCloseDiff;

    public String getWriteTimeEpoch() {
        return writeTimeEpoch;
    }

    public void setWriteTimeEpoch() {
        this.writeTimeEpoch = "extract(epoch from now() )";
    }

    private String writeTimeEpoch;

    public void setDirection() {
        this.direction = (close.compareTo(open)>=0) ? "UP" : "DOWN";;
    }

    public String getDirection() {
        return direction;
    }

    public BigDecimal getOpenCloseDiff() {
        return openCloseDiff;
    }

    public void setOpenCloseDiff() {
        this.openCloseDiff = close.subtract(open);
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
