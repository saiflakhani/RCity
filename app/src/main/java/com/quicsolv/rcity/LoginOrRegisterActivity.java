package com.quicsolv.rcity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    SignInButton googleButton;
    GoogleSignInClient mGoogleSignInClient;
    final private int RC_SIGN_IN = 200;
    Button btnLogin, btnRegister;
    TextView tVForgotPassword;
    UserProfile userProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getIds();

    }

    private void getIds() {

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        tVForgotPassword = findViewById(R.id.tVForgotPassword);
        tVForgotPassword.setOnClickListener(this);
        googleButton = findViewById(R.id.gBtnSignIn);
        googleButton.setOnClickListener(this);
        userProfile = new UserProfile();

    }

    @Override
    protected void onStart() {
        super.onStart();
         GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(googleSignInAccount != null) {
            handleSignIn(googleSignInAccount);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tVForgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
            case R.id.gBtnSignIn:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                handleSignIn(googleSignInAccount);
            } catch (ApiException e) {
                Log.w("LoginOrRegisterActivity","signInResult:failed code=" + e.getStatusCode());
            }
        }

    }

    private void handleSignIn(GoogleSignInAccount googleSignInAccount) {
        userProfile.setUserId(googleSignInAccount.getId());
        userProfile.setFirstName(googleSignInAccount.getGivenName());
        userProfile.setLastName(googleSignInAccount.getFamilyName());
        userProfile.setEmailId(googleSignInAccount.getEmail());
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("UserProfile", userProfile);
        startActivity(intent);
    }
}