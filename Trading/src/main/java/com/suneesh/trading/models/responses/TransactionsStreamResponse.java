package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.TransactionsStreamRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;

import java.util.Arrays;
import java.util.List;

/**
 * <h1>TransactionStreamResponse</h1>
 *
 * <h2>Transaction Updates</h2>
 * <p>
 *     Return transaction updates
 * </p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/8/2017
 */
public class TransactionsStreamResponse extends ResponseBase<TransactionsStreamRequest> {

    /**
     * Realtime stream of user transaction updates.
     */
    @SerializedName("transaction")
    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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
