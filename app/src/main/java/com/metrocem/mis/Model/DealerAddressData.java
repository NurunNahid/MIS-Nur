package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealerAddressData {

    @SerializedName("data")
    @Expose
    private DealerAddress address;

    public DealerAddress getAddress() {
        return address;
    }

    public void setAddress(DealerAddress address) {
        this.address = address;
    }
}
