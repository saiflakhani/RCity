
package com.quicsolv.rcity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Poi {

    @SerializedName("buid")
    @Expose
    private String buid;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("is_published")
    @Expose
    private String isPublished;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("floor_number")
    @Expose
    private String floorNumber;
    @SerializedName("coordinates_lon")
    @Expose
    private String coordinatesLon;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("floor_name")
    @Expose
    private String floorName;
    @SerializedName("puid")
    @Expose
    private String puid;
    @SerializedName("is_door")
    @Expose
    private String isDoor;
    @SerializedName("username_creator")
    @Expose
    private String usernameCreator;
    @SerializedName("coordinates_lat")
    @Expose
    private String coordinatesLat;
    @SerializedName("is_building_entrance")
    @Expose
    private String isBuildingEntrance;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pois_type")
    @Expose
    private String poisType;

    public String getBuid() {
        return buid;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(String isPublished) {
        this.isPublished = isPublished;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getCoordinatesLon() {
        return coordinatesLon;
    }

    public void setCoordinatesLon(String coordinatesLon) {
        this.coordinatesLon = coordinatesLon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getIsDoor() {
        return isDoor;
    }

    public void setIsDoor(String isDoor) {
        this.isDoor = isDoor;
    }

    public String getUsernameCreator() {
        return usernameCreator;
    }

    public void setUsernameCreator(String usernameCreator) {
        this.usernameCreator = usernameCreator;
    }

    public String getCoordinatesLat() {
        return coordinatesLat;
    }

    public void setCoordinatesLat(String coordinatesLat) {
        this.coordinatesLat = coordinatesLat;
    }

    public String getIsBuildingEntrance() {
        return isBuildingEntrance;
    }

    public void setIsBuildingEntrance(String isBuildingEntrance) {
        this.isBuildingEntrance = isBuildingEntrance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoisType() {
        return poisType;
    }

    public void setPoisType(String poisType) {
        this.poisType = poisType;
    }

}
