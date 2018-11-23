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
import com.quicsolv.rcity.requestbodies.requestbodies.LoginBody.LoginBody;
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
        final boolean toRemember = cBRememberMe.isChecked();

        if(!user.isEmpty() && !password.isEmpty()) {

            LoginBody loginBody = new LoginBody();
            loginBody.setEmailId(user);
            loginBody.setPassword(password);
            LoginInterface loginInterface = RetrofitClient.getClient(AppConstants.BASE_URL_SERVER).create(LoginInterface.class);
            loginInterface.postLogin(loginBody).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.code() == 200) {
                        Log.e("RequestSuccess", response.raw().request().url().toString());
                        if (toRemember) {
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences("RCityPrefs", 0);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("USER_ID", response.body().getId());
                            editor.apply();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("RequestFailed: URL", t.getMessage());
                }
            });


        } else Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();

    }
}
