package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.GetSelfExclusionRequest;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>GetSelfExclusionResponse</h1>
 *
 * <h2>Get User Self-Exclusion Receive</h2>
 * <p>
 *     A message with User Self-Exclusion
 * </p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/4/2017
 */
public class GetSelfExclusionResponse extends ResponseBase<GetSelfExclusionRequest> {

    /**
     * Echo of the request made
     */
    @SerializedName("get_self_exclusion")
    private SelfExclusion selfExclusion;

    public SelfExclusion getSelfExclusion() {
        return selfExclusion;
    }
}
