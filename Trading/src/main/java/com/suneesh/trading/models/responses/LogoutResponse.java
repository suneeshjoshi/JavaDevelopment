package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.LogoutRequest;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>LogouResponse</h1>
 *
 * <h2>Logout Response</h2>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/7/2017
 */
public class LogoutResponse extends ResponseBase<LogoutRequest> {

    @SerializedName("logout")
    private int logout;

    public boolean getLogout() {
        return logout == 1 ? true : false;
    }
}
