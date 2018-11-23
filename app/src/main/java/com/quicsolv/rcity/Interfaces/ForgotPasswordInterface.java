package com.quicsolv.rcity.Interfaces;

import com.quicsolv.rcity.requestbodies.requestbodies.ForgotPasswordBody.ForgotPasswordBody;
import com.quicsolv.rcity.responses.ForgotPasswordResponse.ForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ForgotPasswordInterface {

    @Headers({
            "Content-Type: application/json"
    })

    @POST("forgotPassword")
    Call<ForgotPasswordResponse> postForgotPassword(@Body ForgotPasswordBody forgotPasswordBody);


}
