package com.quicsolv.rcity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.quicsolv.rcity.Interfaces.LoginInterface;
import com.quicsolv.rcity.requestbodies.RegisterBody.LoginBody.LoginBody;
import com.quicsolv.rcity.responses.LoginResponse.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eTUsername, eTPassword;
    Button btnLogin;
    CheckBox cBRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getIds();
    }

    private void getIds() {
        eTUsername = findViewById(R.id.eTEmail);
        eTPassword = findViewById(R.id.eTPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        cBRememberMe = findViewById(R.id.cBRememberMe);
    }


    @Override
    public void onClick(View view) {

        String user = eTUsername.getText().toString();
        String password = eTPassword.getText().toString();
        boolean toRemember = cBRememberMe.isChecked();

        if(!user.isEmpty() && !password.isEmpty()) {

            LoginBody loginBody = new LoginBody();
            loginBody.setEmailId(user);
            loginBody.setPassword(password);
            LoginInterface loginInterface = RetrofitClient.getClient(AppConstants.BASE_URL_SERVER).create(LoginInterface.class);
            loginInterface.postLogin(loginBody).enqueue(new Callback<Response<LoginResponse>>() {
                @Override
                public void onResponse(Call<Response<LoginResponse>> call, Response<Response<LoginResponse>> response) {
                    Log.e("RequestSuccess", response.raw().request().url().toString());
                }

                @Override
                public void onFailure(Call<Response<LoginResponse>> call, Throwable t) {
                    Log.e("RequestFailed: URL", t.getMessage());
                    Log.e("RequestFailed: URL", call.request().url().toString());
                    Log.e("RequestFailed: Body", call.request().body().toString());
                    Log.e("RequestFailed: Headers", call.request().headers().toString());
                    Log.e("RequestFailed: METHOD", call.request().method());
                }
            });


            //            loginInterface.postLogin(loginBody).enqueue(new Callback<Response<LoginResponse>>() {
//                @Override
//                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("RCityPrefs", 0);
//                    if(prefs.getInt("USER_ID", 99999) != 99999) {
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putInt("USER_ID", response.body().getId());
//                        editor.commit();
//                    }
//                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//                @Override
//                public void onFailure(Call<LoginResponse> call, Throwable t) {
//                    Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
//                }
//            });

        } else Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();

    }
}
