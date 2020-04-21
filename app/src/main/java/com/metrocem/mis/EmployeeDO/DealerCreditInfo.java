package com.metrocem.mismetrocem.EmployeeDO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerCreditInfo {

    @SerializedName("creditLimit")
    @Expose
    private String creditLimit;
    @SerializedName("dueBalance")
    @Expose
    private String dueBalance;
    @SerializedName("blockAmount")
    @Expose
    private String blockAmount;

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getDueBalance() {
        return dueBalance;
    }

    public void setDueBalance(String dueBalance) {
        this.dueBalance = dueBalance;
    }

    public String getBlockAmount() {
        return blockAmount;
    }

    public void setBlockAmount(String blockAmount) {
        this.blockAmount = blockAmount;
    }
}
