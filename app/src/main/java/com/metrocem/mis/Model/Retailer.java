package com.metrocem.mis.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Retailer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("organization")
    @Expose
    private String organization;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("nid")
    @Expose
    private Object nid;
    @SerializedName("tin")
    @Expose
    private Object tin;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getNid() {
        return nid;
    }

    public void setNid(Object nid) {
        this.nid = nid;
    }

    public Object getTin() {
        return tin;
    }

    public void setTin(Object tin) {
        this.tin = tin;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
