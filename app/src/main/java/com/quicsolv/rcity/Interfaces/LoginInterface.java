package com.quicsolv.rcity.Interfaces;

import com.quicsolv.rcity.requestbodies.requestbodies.LoginBody.LoginBody;
import com.quicsolv.rcity.responses.LoginResponse.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginInterface {

    @Headers({
            "Content-Type: application/json"
    })

    @POST("login")
    Call<LoginResponse> postLogin(@Body LoginBody loginBody);


}
