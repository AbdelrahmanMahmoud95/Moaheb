package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 5/6/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("media")
    @Expose
    private Media_ media;

    public String getUserName() {
        return userName;
    }

    @SerializedName("user_name")
    @Expose

    private String userName;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Media_ getMedia() {
        return media;
    }

    public void setMedia(Media_ media) {
        this.media = media;
    }
}


