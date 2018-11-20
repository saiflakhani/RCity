package com.quicsolv.rcity.Interfaces;

import com.quicsolv.rcity.responses.ProfileResponse.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ProfileInterface {

    @GET("profile/")
    Call<ProfileResponse> profileResponse(@Url String url);

}
