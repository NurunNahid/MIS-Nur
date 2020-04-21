package com.metrocem.mis.Subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Due {

    @SerializedName("data")
    @Expose
    private DueAmount data;

    public DueAmount getData() {
        return data;
    }

    public void setData(DueAmount data) {
        this.data = data;
    }
}
