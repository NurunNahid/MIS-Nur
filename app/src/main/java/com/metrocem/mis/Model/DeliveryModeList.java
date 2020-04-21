package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveryModeList {

    @SerializedName("data")
    @Expose
    private List<DeliveryMode> data = null;

    public List<DeliveryMode> getData() {
        return data;
    }

    public void setData(List<DeliveryMode> data) {
        this.data = data;
    }
}
