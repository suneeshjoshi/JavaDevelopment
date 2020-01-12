package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.PortfolioRequest;
import com.google.gson.annotations.SerializedName;

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

    /**
     * Client open positions
     */
    @SerializedName("portfolio")
    private Portfolio portfolio;

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
