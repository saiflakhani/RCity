package com.quicsolv.rcity.Interfaces;

import com.quicsolv.rcity.requestbodies.RegisterBody.LoginBody.LoginBody;
import com.quicsolv.rcity.responses.LoginResponse.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginInterface {

    @Headers({
            "Content-Type: application/json"
    })

    @POST("login")
    Call<List<LoginResponse>> postLogin(@Body LoginBody loginBody);


}
