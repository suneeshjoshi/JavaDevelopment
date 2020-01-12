package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.google.gson.annotations.SerializedName;

/**
 * Created by morteza on 7/28/2017.
 */
public class AuthorizeResponse extends ResponseBase<AuthorizeRequest> {
    @SerializedName("authorize")
    private Authorize authorize;

    public Authorize getAuthorize() {
        return authorize;
    }

    public void setAuthorize(Authorize authorize) {
        this.authorize = authorize;
    }
}
