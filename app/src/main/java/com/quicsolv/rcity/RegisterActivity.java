package com.quicsolv.rcity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.quicsolv.rcity.Interfaces.RegisterInterface;
import com.quicsolv.rcity.requestbodies.RegisterBody.Contact;
import com.quicsolv.rcity.requestbodies.RegisterBody.RegisterBody;
import com.quicsolv.rcity.responses.RegisterResponse.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eTFirstName, eTLastName, eTEmail, eTPhone, eTPassword, eTConfirmPassword, eTAge;
    RadioGroup rGGender;
    Button btnRegister;
    int gender = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getIds();

        rGGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rBOther:
                        gender = 0;
                        break;
                    case R.id.rBFemale:
                        gender = 1;
                        break;
                    case R.id.rBMale:
                        gender = 2;
                        break;
                }
            }
        });

    }

    void getIds() {

        eTFirstName = findViewById(R.id.eTFirstName);
        eTLastName = findViewById(R.id.eTLastName);
        eTPhone = findViewById(R.id.eTPhoneNumber);
        eTEmail = findViewById(R.id.eTEmail);
        eTPassword = findViewById(R.id.eTPassword);
        eTConfirmPassword = findViewById(R.id.eTConfirmPassword);
        eTAge = findViewById(R.id.eTAge);
        rGGender = findViewById(R.id.rGGender);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String firstName = eTFirstName.getText().toString();
        String lastName = eTLastName.getText().toString();
        String email = eTEmail.getText().toString();
        String phone = eTPhone.getText().toString();
        String password = eTPassword.getText().toString();
        String confirmPassword = eTConfirmPassword.getText().toString();
        String age = eTAge.getText().toString();

        if(!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && (password.equals(confirmPassword)) && !age.isEmpty() && (gender!=9)) {

            RegisterInterface registerInterface = RetrofitClient.getClient(AppConstants.BASE_URL_SERVER).create(RegisterInterface.class);

            final RegisterBody registerBody = new RegisterBody();
            registerBody.setFirstName(firstName);
            registerBody.setLastName(lastName);
            final Contact contact = new Contact();
            contact.setEmailId(email);
            contact.setPhoneNo(phone);
            registerBody.setContact(contact);
            registerBody.setPassword(password);

            switch(rGGender.getCheckedRadioButtonId()) {
                case R.id.rBOther: gender = 0;
                break;
                case R.id.rBFemale: gender = 1;
                break;
                case R.id.rBMale: gender = 2;
                break;

            }
            registerInterface.postRegister(registerBody).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("RCityPrefs", Context.MODE_WORLD_WRITEABLE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("USER_ID", response.body().getId());

                    editor.apply();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "All details are required!", Toast.LENGTH_SHORT).show();
        }
    }
}
