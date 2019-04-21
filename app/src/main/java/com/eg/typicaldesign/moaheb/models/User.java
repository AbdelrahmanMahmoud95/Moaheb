package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 4/24/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("gov_id")
    @Expose
    private String govId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("sport_type")
    @Expose
    private String sportType;
    @SerializedName("sport_id")
    @Expose
    private String sportId;
    @SerializedName("position_id")
    @Expose
    private String positionId;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("guardian")
    @Expose
    private Object guardian;

    @SerializedName("phone")
    @Expose
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @SerializedName("club_joined")
    @Expose
    private String clubJoined;
    @SerializedName("club_name")
    @Expose
    private Object clubName;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("sponsors_visits")
    @Expose
    private String sponsorsVisits;
    @SerializedName("users_visits")
    @Expose
    private String usersVisits;
    @SerializedName("approved")
    @Expose
    private String approved;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("champion")
    @Expose
    private String champion;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("approved_videos_count")
    @Expose
    private String approvedVideosCount;
    @SerializedName("approved_images_count")
    @Expose
    private String approvedImagesCount;
    @SerializedName("rank")
    @Expose
    private float rank;
    @SerializedName("years")
    @Expose
    private Years years;
    @SerializedName("gov")
    @Expose
    private Gov gov;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("sport")
    @Expose
    private Sport sport;
    @SerializedName("position")
    @Expose
    private Position position;
    @SerializedName("approved_videos")
    @Expose
    private List<Video> approvedVideos = null;
    @SerializedName("approved_images")
    @Expose
    private List<Image> approvedImages = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGovId() {
        return govId;
    }

    public void setGovId(String govId) {
        this.govId = govId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Object getGuardian() {
        return guardian;
    }

    public void setGuardian(Object guardian) {
        this.guardian = guardian;
    }

    public String getClubJoined() {
        return clubJoined;
    }

    public void setClubJoined(String clubJoined) {
        this.clubJoined = clubJoined;
    }

    public Object getClubName() {
        return clubName;
    }

    public void setClubName(Object clubName) {
        this.clubName = clubName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getChampion() {
        return champion;
    }

    public void setChampion(String champion) {
        this.champion = champion;
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

    public String getApprovedVideosCount() {
        return approvedVideosCount;
    }

    public void setApprovedVideosCount(String approvedVideosCount) {
        this.approvedVideosCount = approvedVideosCount;
    }

    public String getApprovedImagesCount() {
        return approvedImagesCount;
    }

    public void setApprovedImagesCount(String approvedImagesCount) {
        this.approvedImagesCount = approvedImagesCount;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public Years getYears() {
        return years;
    }

    public void setYears(Years years) {
        this.years = years;
    }

    public Gov getGov() {
        return gov;
    }

    public void setGov(Gov gov) {
        this.gov = gov;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Video> getApprovedVideos() {
        return approvedVideos;
    }

    public void setApprovedVideos(List<Video> approvedVideos) {
        this.approvedVideos = approvedVideos;
    }

    public List<Image> getApprovedImages() {
        return approvedImages;
    }

    public void setApprovedImages(List<Image> approvedImages) {
        this.approvedImages = approvedImages;
    }

}