package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.LandingCompanyRequest;
import com.google.gson.annotations.SerializedName;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/1/2017
 */
public class LandingCompanyResponse extends ResponseBase<LandingCompanyRequest> {

    @SerializedName("landing_company")
    private LandingCompany landingCompany;

    public LandingCompany getLandingCompany() {
        return landingCompany;
    }

    public void setLandingCompany(LandingCompany landingCompany) {
        this.landingCompany = landingCompany;
    }
}
