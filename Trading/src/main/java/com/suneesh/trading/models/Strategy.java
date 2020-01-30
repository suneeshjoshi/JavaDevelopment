package com.suneesh.trading.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;

@Data
@Slf4j
public class Strategy {
    @SerializedName("identifier")
    @Expose
    long identifier ;

    @SerializedName("strategy_name")
    @Expose
    String strategyName ;

    @SerializedName("classname")
    @Expose
    String className ;

    @SerializedName("creation_date")
    @Expose
    String creationDate;

    @SerializedName("max_steps")
    @Expose
    int maxSteps;

    @SerializedName("next_strategy_id_link")
    @Expose
    int nextStrategyIdLink;

    @SerializedName("is_backtesting_strategy")
    @Expose
    boolean isBacktestingStrategy;

    @SerializedName("is_default_strategy")
    @Expose
    boolean isDefaultStrategy;

    @SerializedName("reset_step_count_on_success")
    @Expose
    boolean resetStepCountOnSuccess;


    @SerializedName("active_strategy")
    @Expose
    boolean activeStrategy;

    HashMap<Integer, Double> stepValuesMap = new HashMap<>();

    public void setStepValueMap(List<StrategySteps> strategyStepsList){
        try {
            if (CollectionUtils.isNotEmpty(strategyStepsList)) {
                strategyStepsList.stream().forEach(ele -> stepValuesMap.putIfAbsent(Integer.valueOf(ele.getStepCount()), Double.valueOf(ele.getValue())));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
