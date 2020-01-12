package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.PriceProposalRequest;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>PriceProposalRequest</h1>
 *
 * <h2>Price Proposal Response</h2>
 * <p>Latest price and other details for a given contract</p>
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/2/2017
 */
public class PriceProposalResponse extends ResponseBase<PriceProposalRequest> {

    /**
     * Latest price and other details for a given contract
     */
    @SerializedName("proposal")
    private Proposal proposal;

    public Proposal getProposal() {
        return proposal;
    }
}
