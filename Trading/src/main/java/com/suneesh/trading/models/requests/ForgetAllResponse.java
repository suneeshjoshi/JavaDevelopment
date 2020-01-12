package com.suneesh.trading.models.requests;

import com.suneesh.trading.models.responses.ResponseBase;
import com.google.gson.annotations.SerializedName;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/1/2017
 */
public class ForgetAllResponse extends ResponseBase<ForgetAllRequest> {

    @SerializedName("forget_all")
    private String[] streams;

    public String[] getStreams() {
        return streams;
    }

    public void setStreams(String[] streams) {
        this.streams = streams;
    }
}
