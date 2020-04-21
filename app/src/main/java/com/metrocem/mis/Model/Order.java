package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("actual_bag_qty")
    @Expose
    private Double actualBagQty;
    @SerializedName("delivery_address")
    @Expose
    private OrderDeliveryAddress deliveryAddress;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("do_number")
    @Expose
    private String doNumber;
    @SerializedName("delivery_mode")
    @Expose
    private String deliveryMode;
    @SerializedName("approved_at")
    @Expose
    private String approvedAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("number_of_challan")
    @Expose
    private Integer numberOfChallan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getActualBagQty() {
        return actualBagQty;
    }

    public void setActualBagQty(Double actualBagQty) {
        this.actualBagQty = actualBagQty;
    }

    public OrderDeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(OrderDeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getNumberOfChallan() {
        return numberOfChallan;
    }

    public void setNumberOfChallan(Integer numberOfChallan) {
        this.numberOfChallan = numberOfChallan;
    }
}
