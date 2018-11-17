package com.quicsolv.rcity.mapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PoiMap {

    @SerializedName("buid")
    @Expose
    private String buid;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("floor_number")
    @Expose
    private String floorNumber;
    @SerializedName("puid")
    @Expose
    private String puid;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("pois_type")
    @Expose
    private String poisType;

    public String getBuid() {
        return buid;
    }

    public void setBuid(String buid) {
        this.buid = buid;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPoisType() {
        return poisType;
    }

    public void setPoisType(String poisType) {
        this.poisType = poisType;
    }
}