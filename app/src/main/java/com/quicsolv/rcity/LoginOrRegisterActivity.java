package com.quicsolv.rcity;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.tasks.Task;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    SignInButton googleButton;
    GoogleSignInClient mGoogleSignInClient;
    final private int RC_SIGN_IN = 200;
    Button btnLogin, btnRegister;
    TextView tVForgotPassword;
    UserProfile userProfile;
    private MessageListener mMessageListener;

    private static final int REQUEST_PERMISSION = 201;
    private MessagesClient mMessagesClient;
    private static final String TAG =
            LoginOrRegisterActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getIds();
        checkAndAskPermissions();


    }

    private void backgroundSubscribe() {
        Log.i(TAG, "Subscribing for background updates.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        mMessagesClient.subscribe(getPendingIntent(), options);

    }
    private PendingIntent getPendingIntent() {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, BackgroundSubscribeIntentService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void checkAndAskPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                    .setPermissions(NearbyPermissions.BLE)
                    .build());
            backgroundSubscribe();
        }else{
            ActivityCompat.requestPermissions(LoginOrRegisterActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    REQUEST_PERMISSION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                            .setPermissions(NearbyPermissions.BLE)
                            .build());
                    backgroundSubscribe();

                } else {
                    Toast.makeText(LoginOrRegisterActivity.this,"Need this permission",Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
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
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("RCityPrefs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("USER_ID", 9999); //Shoulc be Integer.parseInt(googleSignInAccount.getId())
        editor.apply();
        userProfile.setFirstName(googleSignInAccount.getGivenName());
        userProfile.setLastName(googleSignInAccount.getFamilyName());
        userProfile.setEmailId(googleSignInAccount.getEmail());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserProfile", userProfile);
        startActivity(intent);
        finish();
    }
}
