package com.suneesh.trading.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class StrategySteps {
    @SerializedName("identifier")
    @Expose
    long identifier ;

    @SerializedName("strategy_id")
    @Expose
    int strategyId ;

    @SerializedName("step_count")
    @Expose
    int stepCount;

    @SerializedName("value")
    @Expose
    double value;

    @SerializedName("profit_percentage_threshold")
    @Expose
    double profit_percentage_threshold;
}
