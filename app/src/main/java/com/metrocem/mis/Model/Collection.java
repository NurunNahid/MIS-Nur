package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collection {

    @SerializedName("data")
    @Expose
    private CollectionAmount data;


    public CollectionAmount getData() {
        return data;
    }

    public void setData(CollectionAmount data) {
        this.data = data;
    }
}
