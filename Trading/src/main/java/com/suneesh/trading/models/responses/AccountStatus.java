package com.suneesh.trading.models.responses;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/4/2017
 */
@Entity
@Data
public class AccountStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long identifier;

    /**
     * Array of Status
     */
    @SerializedName("status")
//    private List<String> status;
    private String status;

    /**
     * Indicates whether the client should be prompted to authenticate their account.
     */
    @SerializedName("prompt_client_to_authenticate")
    private int promptClientToAuthenticate;

    /**
     * Client risk classification: low, standard, high
     */
    @SerializedName("risk_classification")
    private String riskClassification;

//    public List<String> getStatus() {
    public String getStatus() {
        return String.join(",", status);
//        status;
    }

    public boolean getPromptClientToAuthenticate() {
        return promptClientToAuthenticate == 1 ? true : false;
    }

    public String getRiskClassification() {
        return riskClassification;
    }
}
