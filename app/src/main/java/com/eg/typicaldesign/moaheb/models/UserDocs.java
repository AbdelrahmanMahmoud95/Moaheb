package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 5/14/2018.
 */import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDocs {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("identities")
    @Expose
    private List<Identity> identities = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Identity> getIdentities() {
        return identities;
    }

    public void setIdentities(List<Identity> identities) {
        this.identities = identities;
    }

}