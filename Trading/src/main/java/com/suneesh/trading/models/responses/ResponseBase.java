package com.suneesh.trading.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by morteza on 7/19/2017.
 */

public abstract class ResponseBase<T> implements DatabaseOperation {
    @SerializedName("echo_req")
    @Expose
    T request;

    @SerializedName("msg_type")
    @Expose
    String type;

    @SerializedName("error")
    @Expose
    Error error;

    public String getType(){
        return this.type;
    }

    public T getRequest(){
        return this.request;
    }

    public Error getError() {
        return error;
    }

    public abstract List<String> databaseUpdateStringList();
}
