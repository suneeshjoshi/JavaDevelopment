package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.CreateMaltaAccountRequest;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 9/3/2017
 */
public class CreateMaltaAccountResponse extends ResponseBase<CreateMaltaAccountRequest> {

    @SerializedName("new_account_maltainvest")
    private Map<String, String> account;

    public Map<String, String> getAccount() {
        return account;
    }
}
