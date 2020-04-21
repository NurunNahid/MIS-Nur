package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetailerList {

    @SerializedName("data")
    @Expose
    private List<Retailer> data = null;

    public List<Retailer> getData() {
        return data;
    }

    public void setData(List<Retailer> data) {
        this.data = data;
    }


}
