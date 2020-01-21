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
        return null;
//        Arrays.asList(
//                "INSERT INTO public.authorize " +
//                        "( action ,amount ,balance ,barrier ,contract_id ,currency ,date_expiry ,display_name ,high_barrier ," +
//                        "id ,long_code ,low_barrier ,purchase_time ,symbol ,transaction_id ,transaction_time )" +
//                        " VALUES ("
//                        + AutoTradingUtility.quotedString(authorize.getAllowOmnibus()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getBalance()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getCountry()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getCurrency()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getEmail()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getFullName()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getIsVirtual()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getLandingCompanyFullName()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getLandingCompanyName()) + ", "
//                        + AutoTradingUtility.quotedString(authorize.getLoginId()) + ");"
//        );
    }
    }
