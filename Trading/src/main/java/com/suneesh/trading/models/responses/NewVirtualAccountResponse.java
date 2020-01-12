package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.NewVirtualAccountRequest;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>NewVirtualAccountResponse</h1>
 *
 * <h2>Create virtual account Receive</h2>
 * <p>Create virtual-money account</p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/2/2017
 */
public class NewVirtualAccountResponse extends ResponseBase<NewVirtualAccountRequest> {

    /**
     * New virtual-money account details
     */
    @SerializedName("new_account_virtual")
    private NewAccountVirtual accountDetails;

    public NewAccountVirtual getAccountDetails() {
        return accountDetails;
    }
}
