package com.metrocem.mis.SignInAndRegistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.metrocem.mis.Model.Token;

public class LoginResponse {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("token")
    @Expose
    private Token token;
    @SerializedName("status")
    @Expose
    private String status;
    //@SerializedName("message")
    //@Expose
    //private String message;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public String getMessage() {
//        return message;
//    }

//    public void setMessage(String message) {
//        this.message = message;
//    }

}
