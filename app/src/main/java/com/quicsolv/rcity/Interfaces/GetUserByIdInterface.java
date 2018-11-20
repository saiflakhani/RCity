package com.quicsolv.rcity.Interfaces;

import com.quicsolv.rcity.responses.GetUserByIdResponse.GetUserByIdResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetUserByIdInterface {

    @GET("profile/{user_id}")
    Call<GetUserByIdResponse> getUserById(@Path("user_id") String userId);

}
