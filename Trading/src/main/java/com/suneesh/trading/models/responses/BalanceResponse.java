package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.BalanceRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>BalanceResponse</h1>
 *
 * <h2>Realtime Balance</h2>
 * <p>Return details of user account balance</p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/4/2017
 */
public class BalanceResponse extends ResponseBase<BalanceRequest> {

    /**
     * Realtime stream of user balance changes.
     */
    @SerializedName("balance")
    private Balance balance;

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    @Override
    public List<String> databaseInsertStringList(){
        return Arrays.asList(
                    "INSERT INTO public.balance " +
                            "(balance , currency , login_id , time, time_string) " +
                            " VALUES ("
                            + AutoTradingUtility.quotedString(balance.getBalance()) + ", "
                            + AutoTradingUtility.quotedString(balance.getCurrency()) + ", "
                            + AutoTradingUtility.quotedString(balance.getLoginId()) + ", "
                            + " extract(epoch from now()), "
                            + " now()::timestamp );"
                        );

    }
}
