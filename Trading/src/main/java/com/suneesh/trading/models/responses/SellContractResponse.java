package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.SellContractRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;

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
public class SellContractResponse extends ResponseBase<SellContractRequest> {

    /**
     * Receipt for the transaction
     */
    @SerializedName("sell")
    private SellReceipt sellReceipt;

    public SellReceipt getSellReceipt() {
        return sellReceipt;
    }

    @Override
    public List<String> databaseInsertStringList() {

        Long purchaseTime =transaction.getPurchaseTime()==null?transaction.getTransactionTime():transaction.getPurchaseTime();
        return
                Arrays.asList(
                        "INSERT INTO public.transaction " +
                                "( action ,amount ,balance ,barrier ,contract_id ,currency ,date_expiry ,display_name ," +
                                " long_code ,purchase_time ,symbol ,transaction_id ,transaction_time, date_expiry_string ," +
                                " purchase_time_string , transaction_time_string )" +
                                " VALUES ("
                                + AutoTradingUtility.quotedString(transaction.getAction()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getAmount()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getBalance()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getBarrier()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getContractId()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getCurrency()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getDateExpiry()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getDisplayName()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getLongCode()) + ", "
                                + AutoTradingUtility.quotedString(purchaseTime )  + ", "
                                + AutoTradingUtility.quotedString(transaction.getSymbol()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getTransactionId()) + ", "
                                + AutoTradingUtility.quotedString(transaction.getTransactionTime()) + ","
                                + AutoTradingUtility.getTimeStampString(transaction.getDateExpiry()) + ","
                                + AutoTradingUtility.getTimeStampString(purchaseTime) + ","
                                + AutoTradingUtility.getTimeStampString(transaction.getTransactionTime()) + ");"
                );

    }
}
