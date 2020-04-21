package com.metrocem.mis.EmployeeDO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerCreditList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DealerCreditInfo dealerCreditInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DealerCreditInfo getDealerCreditInfo() {
        return dealerCreditInfo;
    }

    public void setDealerCreditInfo(DealerCreditInfo dealerCreditInfo) {
        this.dealerCreditInfo = dealerCreditInfo;
    }
}
