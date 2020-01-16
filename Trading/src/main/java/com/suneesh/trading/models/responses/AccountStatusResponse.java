package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.AccountStatusRequest;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>AccountStatusResponse</h1>
 *
 * <h2>Get Account Status Receive</h2>
 * <p>A message with Account Status</p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/4/2017
 */
public class AccountStatusResponse extends ResponseBase<AccountStatusRequest> {

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    /**
     * Account status details
     */
    @SerializedName("get_account_status")
    private AccountStatus accountStatus;

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }
}
