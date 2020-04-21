package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealerList {

    @SerializedName("data")
    @Expose
    private List<Dealer> data = null;

    public List<Dealer> getData() {
        return data;
    }

    public void setData(List<Dealer> data) {
        this.data = data;
    }


}
