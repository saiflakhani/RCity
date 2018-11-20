package com.quicsolv.rcity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {


    LinearLayout lLDirectOffers, lLFindRoutes, lLFindFF, lLFindCar, lLPrivateShopping, lLPreorderDeals, lLFoodOffers, lLWallet, lLEvacuationGuide;
    EditText eTSearch;
    Button btnSearch;
    private static final int REQUEST_PERMISSION = 420;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getIds();
        checkAndAskPermissions();
    }




    private void getIds() {

        lLDirectOffers = findViewById(R.id.lLDirectOffers);
        lLDirectOffers.setOnClickListener(this);
        lLFindRoutes = findViewById(R.id.lLFindRoutes);
        lLFindRoutes.setOnClickListener(this);
        lLFindFF = findViewById(R.id.lLFindFF);
        lLFindFF.setOnClickListener(this);
        lLFindCar = findViewById(R.id.lLFindCar);
        lLFindCar.setOnClickListener(this);
        lLPrivateShopping = findViewById(R.id.lLPrivateShopping);
        lLPrivateShopping.setOnClickListener(this);
        lLPreorderDeals = findViewById(R.id.lLPreOrderDeals);
        lLPreorderDeals.setOnClickListener(this);
        lLFoodOffers = findViewById(R.id.lLFoodOffers);
        lLFoodOffers.setOnClickListener(this);
        lLWallet = findViewById(R.id.lLWallet);
        lLWallet.setOnClickListener(this);
        lLEvacuationGuide = findViewById(R.id.lLEvacuationGuide);
        lLEvacuationGuide.setOnClickListener(this);

        eTSearch = findViewById(R.id.eTSearch);
        eTSearch.setOnFocusChangeListener(this);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

    }


    private void checkAndAskPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
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
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {
                    Toast.makeText(MainActivity.this,"Need this permission",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }




    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.lLFindRoutes:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.lLDirectOffers:
                Intent intent1 = new Intent(this, DealsActivity.class);
                startActivity(intent1);
                break;
        }
    }



    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.getId() == R.id.eTSearch && b) {
            btnSearch.setVisibility(View.VISIBLE);
        } else btnSearch.setVisibility(View.GONE);
    }
}
