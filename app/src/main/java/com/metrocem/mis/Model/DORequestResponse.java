package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DORequestResponse {
    @SerializedName("data")
    @Expose
    private DORequest doRequest;

    public DORequest getDoRequest() {
        return doRequest;
    }

    public void setDoRequest(DORequest doRequest) {
        this.doRequest = doRequest;
    }
}
