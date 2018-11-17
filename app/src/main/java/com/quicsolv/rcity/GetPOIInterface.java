package com.quicsolv.rcity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetPOIInterface {
    /*@Headers({
            "Accept:application/json",
            "Accept-Encoding:gzip",
            "Content-Type:application/json"
    })*/

    @POST("anyplace/mapping/pois/all_building")

    Call<POIResponse> postNearby(@Body PostBody postBody);

}
