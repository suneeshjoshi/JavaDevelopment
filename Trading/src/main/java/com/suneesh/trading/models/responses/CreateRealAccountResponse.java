package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.CreateRealAccountRequest;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * <h1>CreateRealAccountResponse</h1>
 *
 * <h2>Create real account Receive</h2>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 9/3/2017
 */
public class CreateRealAccountResponse extends ResponseBase<CreateRealAccountRequest> {

    @SerializedName("new_account_real")
    private Map<String, String> account;

    public Map<String, String> getAccount() {
        return account;
    }
}
