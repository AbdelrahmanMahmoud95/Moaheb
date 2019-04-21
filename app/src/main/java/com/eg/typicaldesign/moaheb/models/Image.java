package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 4/24/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("sponsors_visits")
    @Expose
    private String sponsorsVisits;
    @SerializedName("users_visits")
    @Expose
    private String usersVisits;
    @SerializedName("approved")
    @Expose
    private String approved;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getSponsorsVisits() {
        return sponsorsVisits;
    }

    public void setSponsorsVisits(String sponsorsVisits) {
        this.sponsorsVisits = sponsorsVisits;
    }

    public String getUsersVisits() {
        return usersVisits;
    }

    public void setUsersVisits(String usersVisits) {
        this.usersVisits = usersVisits;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
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

