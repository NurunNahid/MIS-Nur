package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChallanInfo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("bag_quantity")
    @Expose
    private Integer bagQuantity;
    @SerializedName("delivery_address")
    @Expose
    private DODeliveryAddress deliveryAddress;
    @SerializedName("dealer_phone")
    @Expose
    private String dealerPhone;
    @SerializedName("unit_price")
    @Expose
    private Integer unitPrice;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("do_number")
    @Expose
    private String doNumber;
    @SerializedName("delivery_mode")
    @Expose
    private String deliveryMode;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("driver_phone")
    @Expose
    private String driverPhone;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("do_processing")
    @Expose
    private String doProcessing;
    @SerializedName("do_allocating")
    @Expose
    private String doAllocating;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getBagQuantity() {
        return bagQuantity;
    }

    public void setBagQuantity(Integer bagQuantity) {
        this.bagQuantity = bagQuantity;
    }

    public DODeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DODeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDealerPhone() {
        return dealerPhone;
    }

    public void setDealerPhone(String dealerPhone) {
        this.dealerPhone = dealerPhone;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDoProcessing() {
        return doProcessing;
    }

    public void setDoProcessing(String doProcessing) {
        this.doProcessing = doProcessing;
    }

    public String getDoAllocating() {
        return doAllocating;
    }
    public void setDoAllocating(String doAllocating) {
        this.doAllocating = doAllocating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
