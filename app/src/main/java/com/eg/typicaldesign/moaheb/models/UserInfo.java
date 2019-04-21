package com.eg.typicaldesign.moaheb.models;

/**
 * Created by MAGIC on 4/18/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class UserInfo {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
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

    public String getChampion() {
        return champion;
    }

    public void setChampion(String champion) {
        this.champion = champion;
    }

    @SerializedName("guardian")
    @Expose
    private String guardian;
    @SerializedName("champion")
    @Expose
    private String champion;
    @SerializedName("guardian_phone")
    @Expose
    private String guardianPhone;
    @SerializedName("identity_image")
    @Expose
    private String identityImage;
    @SerializedName("club_joined")
    @Expose
    private int clubJoined;
    @SerializedName("club_name")
    @Expose
    private String clubName;
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
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public String getIdentityImage() {
        return identityImage;
    }

    public void setIdentityImage(String identityImage) {
        this.identityImage = identityImage;
    }

    public int getClubJoined() {
        return clubJoined;
    }

    public void setClubJoined(int clubJoined) {
        this.clubJoined = clubJoined;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
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

}