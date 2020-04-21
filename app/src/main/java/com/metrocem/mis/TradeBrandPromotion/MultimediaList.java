package com.metrocem.mis.TradeBrandPromotion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultimediaList {
    @SerializedName("data")
    @Expose
    private List<MultimediaInfo> multimediaList;

    public List<MultimediaInfo> getMultimediaList() {
        return multimediaList;
    }

    public void setMultimediaList(List<MultimediaInfo> multimediaList) {
        this.multimediaList = multimediaList;
    }
}
