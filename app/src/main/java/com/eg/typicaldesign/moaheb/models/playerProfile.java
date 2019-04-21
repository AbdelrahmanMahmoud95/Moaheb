package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 4/24/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class playerProfile {


    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("user")
    @Expose
    private User user;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

