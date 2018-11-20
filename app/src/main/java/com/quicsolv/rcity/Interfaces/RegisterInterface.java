package com.quicsolv.rcity.Interfaces;

import com.quicsolv.rcity.requestbodies.RegisterBody.RegisterBody;
import com.quicsolv.rcity.responses.RegisterResponse.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegisterInterface {

    @Headers({
            "Content-Type: application/json"
    })

    @POST("profile")
    Call<RegisterResponse> postRegister(@Body RegisterBody registerBody);

}
