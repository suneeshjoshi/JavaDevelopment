package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.SetSelfExclusionSettingsRequest;
import com.google.gson.annotations.SerializedName;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 9/12/2017
 */
public class SetSelfExclusionSettingsResponse extends ResponseBase<SetSelfExclusionSettingsRequest> {

    @SerializedName("set_self_exclusion")
    private Integer result;

    public Integer getResult() {
        return result;
    }
}
