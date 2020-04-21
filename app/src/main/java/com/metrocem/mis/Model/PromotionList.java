package com.metrocem.mis.Subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromotionList {
    @SerializedName("data")
    @Expose
    private List<Promotion> data = null;

    public List<Promotion> getData() {
        return data;
    }

    public void setData(List<Promotion> data) {
        this.data = data;
    }
}
