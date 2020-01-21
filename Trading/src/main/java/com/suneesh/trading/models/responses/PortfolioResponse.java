package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.PortfolioRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>PortfolioResponse</h1>
 *
 * <h2>Portfolio Receive</h2>
 * <p>
 *     Receive a list of outstanding options in the user's portfolio
 * </p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/7/2017
 */
public class PortfolioResponse extends ResponseBase<PortfolioRequest> {

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    /**
     * Client open positions
     */
    @SerializedName("portfolio")
    private Portfolio portfolio;

    public Portfolio getPortfolio() {
        return portfolio;
    }


    @Override
    public List<String> databaseInsertStringList() {
        return portfolio.getContracts().stream().map(portfolioTransaction -> {
            return "INSERT INTO public.portfolio_transaction " +
                    "( app_id ,buy_price ,contract_id ,contract_type ,currency ,date_start ," +
                    " expiry_time ,long_code ,short_code , payout ,symbol ,purchase_time ,transaction_id)" +
                    " VALUES ("
                    + AutoTradingUtility.quotedString(portfolioTransaction.getAppId()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getBuyPrice()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getContractId()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getContractType()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getCurrency()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getDateStart()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getExpiryTime()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getLongCode()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getShortCode()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getPayout()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getSymbol()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getPurchaseTime()) + ", "
                    + AutoTradingUtility.quotedString(portfolioTransaction.getTransactionId()) + "); ";
        }).collect(Collectors.toList());
    }
}
