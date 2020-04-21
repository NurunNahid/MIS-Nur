package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DORequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("do_number")
    @Expose
    private String doNumber;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("dealer_id")
    @Expose
    private String dealerId;
    @SerializedName("retailer_id")
    @Expose
    private String retailerId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("delivery_mode")
    @Expose
    private String deliveryMode;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("delivery_address")
    @Expose
    private DODeliveryAddress deliveryAddress;
    @SerializedName("billed_bag_qty")
    @Expose
    private String billedBagQty;
    @SerializedName("actual_bag_qty")
    @Expose
    private String actualBagQty;
    @SerializedName("required_vehicle_capacity")
    @Expose
    private String requiredVehicleCapacity;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("requested_by")
    @Expose
    private String requestedBy;
    @SerializedName("commitment")
    @Expose
    private String commitment;
    @SerializedName("commitment_amount")
    @Expose
    private String commitmentAmount;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @SerializedName("created_by")
    @Expose
    private Object createdBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DODeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DODeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getBilledBagQty() {
        return billedBagQty;
    }

    public void setBilledBagQty(String billedBagQty) {
        this.billedBagQty = billedBagQty;
    }

    public String getActualBagQty() {
        return actualBagQty;
    }

    public void setActualBagQty(String actualBagQty) {
        this.actualBagQty = actualBagQty;
    }

    public String getRequiredVehicleCapacity() {
        return requiredVehicleCapacity;
    }

    public void setRequiredVehicleCapacity(String requiredVehicleCapacity) {
        this.requiredVehicleCapacity = requiredVehicleCapacity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getCommitment() {
        return commitment;
    }

    public void setCommitment(String commitment) {
        this.commitment = commitment;
    }

    public String getCommitmentAmount() {
        return commitmentAmount;
    }

    public void setCommitmentAmount(String commitmentAmount) {
        this.commitmentAmount = commitmentAmount;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
