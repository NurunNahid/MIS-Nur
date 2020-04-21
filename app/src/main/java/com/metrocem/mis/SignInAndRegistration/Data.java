package com.metrocem.mis.SignIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metrocem.mis.Subclasses.UserInfo;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("allow_login_from")
    @Expose
    private String allowLoginFrom;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("lastlogin")
    @Expose
    private String lastlogin;
    @SerializedName("network_ip")
    @Expose
    private String networkIp;
    @SerializedName("profile")
    @Expose
    private UserInfo loggedInUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAllowLoginFrom() {
        return allowLoginFrom;
    }

    public void setAllowLoginFrom(String allowLoginFrom) {
        this.allowLoginFrom = allowLoginFrom;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getNetworkIp() {
        return networkIp;
    }

    public void setNetworkIp(String networkIp) {
        this.networkIp = networkIp;
    }

    public UserInfo getUser() {
        return loggedInUser;
    }

    public void setUser(UserInfo loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
