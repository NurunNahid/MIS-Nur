package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecurityInfo {

    @SerializedName("dealer_id")
    @Expose
    private Integer dealerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bank_guarantee")
    @Expose
    private String bankGuarantee;
    @SerializedName("security_cheque")
    @Expose
    private String securityCheque;
    @SerializedName("blank_cheque")
    @Expose
    private String blankCheque;
    @SerializedName("total")
    @Expose
    private String total;

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankGuarantee() {
        return bankGuarantee;
    }

    public void setBankGuarantee(String bankGuarantee) {
        this.bankGuarantee = bankGuarantee;
    }

    public String getSecurityCheque() {
        return securityCheque;
    }

    public void setSecurityCheque(String securityCheque) {
        this.securityCheque = securityCheque;
    }

    public String getBlankCheque() {
        return blankCheque;
    }

    public void setBlankCheque(String blankCheque) {
        this.blankCheque = blankCheque;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
