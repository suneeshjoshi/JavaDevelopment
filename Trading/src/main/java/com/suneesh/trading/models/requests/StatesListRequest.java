package com.suneesh.trading.models.requests;

import com.suneesh.trading.models.responses.StatesListResponse;
import com.suneesh.trading.utils.Validator;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>StatesListRequest</h1>
 *
 * <h2>States List Send</h2>
 * <p>For a given country, returns a list of States of that country. This is useful to populate the account opening form.</p>
 *
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/2/2017
 */
public class StatesListRequest extends RequestBase {

    /**
     * Two letter country code.
     */
    @SerializedName("states_list")
    private String countyCode;

    public StatesListRequest(String countyCode) {
        this.responseType = StatesListResponse.class;
        this.setCountyCode(countyCode);
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        Validator.checkPattern("^\\w\\w$", countyCode,
                "Country Code does not match the regex pattern /^\\w\\w$/");
        this.countyCode = countyCode;
    }
}
