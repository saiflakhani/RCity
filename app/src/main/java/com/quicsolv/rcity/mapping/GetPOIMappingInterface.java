package com.quicsolv.rcity.mapping;

import com.quicsolv.rcity.POIResponse;
import com.quicsolv.rcity.PostBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetPOIMappingInterface {

    @POST("anyplace/navigation/route")
    Call<POIMapping> postNearby(@Body MappingPostBody postBody);
}
