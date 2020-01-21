package com.suneesh.trading.models.responses;

import com.suneesh.trading.models.requests.AuthorizeRequest;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.utils.AutoTradingUtility;

import java.util.Arrays;
import java.util.List;

/**
 * Created by morteza on 7/28/2017.
 */
public class AuthorizeResponse extends ResponseBase<AuthorizeRequest> {
    @SerializedName("authorize")
    private Authorize authorize;

    public Authorize getAuthorize() {
        return authorize;
    }

    public void setAuthorize(Authorize authorize) {
        this.authorize = authorize;
    }

    @Override
    public List<String> databaseInsertStringList(){
        return Arrays.asList(
                "INSERT INTO public.authorize " +
                        "(allow_omnibus ,balance ,country ,currency ,email ,full_name ,is_virtual ," +
                        " landing_company_full_name ,landing_company_name, login_id ) " +
                        " VALUES ("
                        + AutoTradingUtility.quotedString(authorize.getAllowOmnibus()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getBalance()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getCountry()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getCurrency()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getEmail()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getFullName()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getIsVirtual()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getLandingCompanyFullName()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getLandingCompanyName()) + ", "
                        + AutoTradingUtility.quotedString(authorize.getLoginId()) + ");"
        );

    }

}
