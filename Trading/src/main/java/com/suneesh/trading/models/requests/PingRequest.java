package com.suneesh.trading.models.requests;

import com.suneesh.trading.models.responses.PingResponse;
import com.google.gson.annotations.SerializedName;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/2/2017
 */
public class PingRequest extends RequestBase {

    @SerializedName("ping")
    private final int ping = 1;

    public PingRequest(){
        this.responseType = PingResponse.class;
    }
}
