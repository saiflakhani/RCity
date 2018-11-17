package com.quicsolv.rcity.mapping;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quicsolv.rcity.mapping.PoiMap;

public class POIMapping {

@SerializedName("num_of_pois")
@Expose
private Integer numOfPois;
@SerializedName("status_code")
@Expose
private Integer statusCode;
@SerializedName("message")
@Expose
private String message;
@SerializedName("pois")
@Expose
private List<PoiMap> pois = null;
@SerializedName("status")
@Expose
private String status;

public Integer getNumOfPois() {
return numOfPois;
}

public void setNumOfPois(Integer numOfPois) {
this.numOfPois = numOfPois;
}

public Integer getStatusCode() {
return statusCode;
}

public void setStatusCode(Integer statusCode) {
this.statusCode = statusCode;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<PoiMap> getPois() {
return pois;
}

public void setPois(List<PoiMap> pois) {
this.pois = pois;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}