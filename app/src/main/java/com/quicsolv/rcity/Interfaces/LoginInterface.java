package com.quicsolv.rcity.Interfaces;

import com.quicsolv.rcity.requestbodies.RegisterBody.LoginBody.LoginBody;
import com.quicsolv.rcity.responses.LoginResponse.LoginResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginInterface {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("login")
    Call<Response<LoginResponse>> postLogin(@Body LoginBody loginBody);


}
