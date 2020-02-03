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

    @SerializedName("strategy_algorithm_id")
    @Expose
    long strategyAlgorithmId;

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

    HashMap<Integer, StrategySteps> stepToStrategyStepsMap = new HashMap<>();

    public void setStepValueMap(List<StrategySteps> strategyStepsList){
        try {
            if (CollectionUtils.isNotEmpty(strategyStepsList)) {
                strategyStepsList.stream().forEach(ele -> stepToStrategyStepsMap.putIfAbsent(Integer.valueOf(ele.getStepCount()), ele));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
