package com.quicsolv.rcity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POIResponse {

@SerializedName("pois")
@Expose
private List<Poi> pois = null;

public List<Poi> getPois() {
return pois;
}

public void setPois(List<Poi> pois) {
this.pois = pois;
}

}