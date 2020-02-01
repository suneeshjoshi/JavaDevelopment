package com.suneesh.trading.models.requests;

import com.suneesh.trading.models.responses.ProposalOpenContractResponse;
import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;

/**
 * <h1>ProposalOpenContractRequest</h1>
 *
 * <h2>Latest price for an open contract</h2>
 * <p>
 *     Get latest price (and other information) for a contract in the user's portfolio
 * </p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/8/2017
 */
public class ProposalOpenContractRequest extends RequestBase {

    @SerializedName("proposal_open_contract")
    private final int proposalOpenContract = 1;

    /**
     * Contract id received from a Portfolio request. If not set, you will receive stream of all open contracts.
     */
    @SerializedName("contract_id")
    private Long contractId;

    /**
     * If set to 1, will send updates whenever the price changes
     */
    @SerializedName("subscribe")
    @Nullable
    private Integer subscribe = null;


    public ProposalOpenContractRequest(Long contractId) {
        this.responseType = ProposalOpenContractResponse.class;
        this.contractId = contractId;
    }

    public ProposalOpenContractRequest(Long contractId, Boolean subscribe) {
        this.responseType = ProposalOpenContractResponse.class;
        this.contractId = contractId;
        this.subscribe = subscribe? 1: null;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }
}
