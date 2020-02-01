package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.ProposalOpenContractRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;

import java.util.Arrays;
import java.util.List;

/**
 * <h1>ProposalOpenContractResponse</h1>
 *
 * <h2>Open contract proposal response</h2>
 * <p>
 *     Latest price and other details for an open contract in the user's portfolio
 * </p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/8/2017
 */
public class ProposalOpenContractResponse extends ResponseBase<ProposalOpenContractRequest> {

    /**
     * Latest price and other details for an open contract
     */
    @SerializedName("proposal_open_contract")
    private OpenContract openContract;

    public OpenContract getOpenContract() {
        return openContract;
    }

    public void setOpenContract(OpenContract openContract) {
        this.openContract = openContract;
    }

    @Override
    public List<String> databaseUpdateStringList() {
        return Arrays.asList("UPDATE public.open_contract SET " +
                "bidPrice = " + AutoTradingUtility.quotedString(openContract.getBidPrice()) + " , " +
                "currentSpot =  " + AutoTradingUtility.quotedString(openContract.getCurrentSpot()) + " , " +
                "currentSpotTime =  " + AutoTradingUtility.quotedString(openContract.getCurrentSpotTime()) + " , " +
                "expiryDate =  " + AutoTradingUtility.quotedString(openContract.getExpiryDate()) + " , " +
                "dateSetelment =  " + AutoTradingUtility.quotedString(openContract.getDateSetelment()) + " , " +
                "displayName =  " + AutoTradingUtility.quotedString(openContract.getDisplayName()) + " , " +
                "exitTick =  " + AutoTradingUtility.quotedString(openContract.getExitTick()) + " , " +
                "exitTickTime =  " + AutoTradingUtility.quotedString(openContract.getExitTickTime()) + " , " +
                "isExpired =  " + AutoTradingUtility.quotedString(openContract.getIsExpired()) + " , " +
                "isSettleable =  " + AutoTradingUtility.quotedString(openContract.getIsSettleable()) + " , " +
                "isValidToSell =  " + AutoTradingUtility.quotedString(openContract.getIsValidToSell()) + " , " +
                "sellPrice =  " + AutoTradingUtility.quotedString(openContract.getSellPrice()) + " , " +
                "sellSpot =  " + AutoTradingUtility.quotedString(openContract.getSellSpot()) + " , " +
                "sellSpotTime =  " + AutoTradingUtility.quotedString(openContract.getSellSpotTime()) + " , " +
                "sellTime =  " + AutoTradingUtility.quotedString(openContract.getSellTime()) + " , " +
                "status =  " + AutoTradingUtility.quotedString(openContract.getStatus()) + " , " +
                "validation_error =  " + AutoTradingUtility.quotedString(openContract.getValidationError()) + " , " +
                "WHERE contractId = " + AutoTradingUtility.quotedString(openContract.getContractId())
        );
    }


    @Override
    public List<String> databaseInsertStringList(){
        return Arrays.asList("INSERT INTO public.open_contract " +
                "(barrier, barrierCount, bidPrice, buyPrice, contractId, contractType, currency, currentSpot, " +
                "currentSpotTime, expiryDate, dateSetelment, dateStart, displayName, entryTick, entryTickTime, " +
                "exitTick, exitTickTime, isExpired, isIntraday, isSettleable, isValidToSell, longCode, payout, " +
                "purchaseTime, sellPrice, sellSpot, sellSpotTime, sellTime, shortCode, status, validation_error) " +
                " VALUES (" +
                AutoTradingUtility.quotedString(openContract.getBarrier()) + ", " +
                AutoTradingUtility.quotedString(openContract.getBarrierCount()) + ", " +
                AutoTradingUtility.quotedString(openContract.getBidPrice()) + ", " +
                AutoTradingUtility.quotedString(openContract.getBuyPrice()) + ", " +
                AutoTradingUtility.quotedString(openContract.getContractId()) + ", " +
                AutoTradingUtility.quotedString(openContract.getContractType()) + ", " +
                AutoTradingUtility.quotedString(openContract.getCurrency()) + ", " +
                AutoTradingUtility.quotedString(openContract.getCurrentSpot()) + ", " +
                AutoTradingUtility.quotedString(openContract.getCurrentSpotTime()) + ", " +
                AutoTradingUtility.quotedString(openContract.getExpiryDate()) + ", " +
                AutoTradingUtility.quotedString(openContract.getDateSetelment()) + ", " +
                AutoTradingUtility.quotedString(openContract.getDateStart()) + ", " +
                AutoTradingUtility.quotedString(openContract.getDisplayName()) + ", " +
                AutoTradingUtility.quotedString(openContract.getEntryTick()) + ", " +
                AutoTradingUtility.quotedString(openContract.getEntryTickTime()) + ", " +
                AutoTradingUtility.quotedString(openContract.getExitTick()) + ", " +
                AutoTradingUtility.quotedString(openContract.getExitTickTime()) + ", " +
                AutoTradingUtility.quotedString(openContract.getIsExpired()) + ", " +
                AutoTradingUtility.quotedString(openContract.getIsIntraday()) + ", " +
                AutoTradingUtility.quotedString(openContract.getIsSettleable()) + ", " +
                AutoTradingUtility.quotedString(openContract.getIsValidToSell()) + ", " +
                AutoTradingUtility.quotedString(openContract.getLongCode()) + ", " +
                AutoTradingUtility.quotedString(openContract.getPayout()) + ", " +
                AutoTradingUtility.quotedString(openContract.getPurchaseTime()) + ", " +
                AutoTradingUtility.quotedString(openContract.getSellPrice()) + ", " +
                AutoTradingUtility.quotedString(openContract.getSellSpot()) + ", " +
                AutoTradingUtility.quotedString(openContract.getSellSpotTime()) + ", " +
                AutoTradingUtility.quotedString(openContract.getSellTime()) + ", " +
                AutoTradingUtility.quotedString(openContract.getShortCode()) + ", " +
                AutoTradingUtility.quotedString(openContract.getStatus()) + ", " +
                AutoTradingUtility.quotedString(openContract.getValidationError()) + ");" );
    }

}
