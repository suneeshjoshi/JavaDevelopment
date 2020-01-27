package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.SellContractRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * <h1>SellContractResponse</h1>
 *
 * <h2>Sell a Contract Receive</h2>
 * <p>
 *     A message with transaction results is received
 * </p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/9/2017
 */
@Data
public class SellContractResponse extends ResponseBase<SellContractRequest> {

    /**
     * Receipt for the transaction
     */
    @SerializedName("sell")
    private SellReceipt sellReceipt;

    public SellReceipt getSellReceipt() {
        return sellReceipt;
    }
}
