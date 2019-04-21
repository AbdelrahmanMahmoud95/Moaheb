package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 4/18/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInformation {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}