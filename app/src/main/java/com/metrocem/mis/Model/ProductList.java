package com.metrocem.mis.Subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductList {
    @SerializedName("data")
    @Expose
    private List<ProductType> productList;

    public List<ProductType> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductType> productList) {
        this.productList = productList;
    }
}
