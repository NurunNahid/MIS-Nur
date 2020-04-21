package com.metrocem.mis.Subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecurityList {
    @SerializedName("data")
    @Expose
    private List<SecurityInfo> data = null;

    public List<SecurityInfo> getData() {
        return data;
    }

    public void setData(List<SecurityInfo> data) {
        this.data = data;
    }
}
