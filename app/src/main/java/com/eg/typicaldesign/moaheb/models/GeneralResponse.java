package com.eg.typicaldesign.moaheb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MAGIC on 4/11/2018.
 */

public class GeneralResponse {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("sponsor_id")
    @Expose
    private int id;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("api_token")
    @Expose
    private String token;

    @SerializedName("messages")
    @Expose
    private List<String> messages = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

}
