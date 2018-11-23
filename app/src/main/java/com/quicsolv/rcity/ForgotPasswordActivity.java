package com.quicsolv.rcity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quicsolv.rcity.Interfaces.ForgotPasswordInterface;
import com.quicsolv.rcity.Interfaces.LoginInterface;
import com.quicsolv.rcity.requestbodies.requestbodies.ForgotPasswordBody.ForgotPasswordBody;
import com.quicsolv.rcity.responses.ForgotPasswordResponse.ForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eTEmail, eTPassword, eTConfirmPassword;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getIds();

    }

    void getIds() {

        eTEmail = findViewById(R.id.eTEmail);
        eTPassword = findViewById(R.id.eTNewPassword);
        eTConfirmPassword = findViewById(R.id.eTConfirmPassword);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

    }

    void sendData() {
        String email = eTEmail.getText().toString();
        String newPassword = eTPassword.getText().toString();
        String confirmPassword = eTConfirmPassword.getText().toString();
        if(email.isEmpty()) Toast.makeText(this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
        else if (newPassword.isEmpty()) Toast.makeText(this, "Enter your new password", Toast.LENGTH_SHORT).show();
        else if(confirmPassword.isEmpty()) Toast.makeText(this, "Re-enter your password", Toast.LENGTH_SHORT).show();
        else if(!newPassword.equals(confirmPassword)) Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
        else {
            ForgotPasswordBody forgotPasswordBody = new ForgotPasswordBody();
            forgotPasswordBody.setEmailId(email);
            forgotPasswordBody.setPassword(newPassword);
            ForgotPasswordInterface forgotPasswordInterface = RetrofitClient.getClient(AppConstants.BASE_URL_SERVER).create(ForgotPasswordInterface.class);
            forgotPasswordInterface.postForgotPassword(forgotPasswordBody).enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                    if(response.code() == 200) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password changed successfully. Please login now", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(ForgotPasswordActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnSend:
                sendData();
                break;
        }
    }
}
