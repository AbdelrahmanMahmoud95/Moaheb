package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 5/23/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SponsorProfile {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("sponsor_info")
    @Expose
    private SponsorInfo sponsorInfo;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SponsorInfo getSponsorInfo() {
        return sponsorInfo;
    }

    public void setSponsorInfo(SponsorInfo sponsorInfo) {
        this.sponsorInfo = sponsorInfo;
    }

}