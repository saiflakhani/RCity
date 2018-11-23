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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.quicsolv.rcity.Interfaces.GetPOIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {


    LinearLayout lLDirectOffers, lLFindRoutes, lLFindFF, lLFindCar, lLPrivateShopping, lLPreorderDeals, lLFoodOffers, lLWallet, lLEvacuationGuide;
    AutoCompleteTextView eTSearch;
    Button btnSearch;
    private static final int REQUEST_PERMISSION = 420;
    private List<Poi> poiList = new ArrayList<>();
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayAdapter<String> mSearchAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getIds();
        checkAndAskPermissions();
        fetchPlaces();
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
        if(eTSearch.getText().toString().isEmpty())
            btnSearch.setVisibility(View.GONE);
        eTSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(eTSearch.getText().toString().isEmpty()) {
                    btnSearch.setVisibility(View.GONE);
                } else
                    btnSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSearchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AppConstants.mAllPlacesList);
        eTSearch.setAdapter(mSearchAdapter);


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
        Intent intent;
        switch(view.getId()) {
            case R.id.lLFindRoutes:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.lLWallet:
                intent = new Intent(this, WalletActivity.class);
                startActivity(intent);
                break;
            case R.id.lLDirectOffers:
                intent = new Intent(this, DealsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSearch:
                intent = new Intent(this,MapsActivity.class);
                if(!eTSearch.getText().toString().equals("")){
                    intent.putExtra("searchStore",eTSearch.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"Search for something..",Toast.LENGTH_SHORT).show();
                }
        }
    }


    private void fetchPlaces() {
        GetPOIInterface poiInterface = RetrofitClient.getClient(AppConstants.BASE_URL_TOMTOM).create(GetPOIInterface.class);
        poiInterface.postNearby(new PostBody("username","pass","building_a4adff86-0a9c-44f4-ad0c-f709a4f91451_1542360867089")).enqueue(new Callback<POIResponse>() {
            @Override
            public void onResponse(Call<POIResponse> call, Response<POIResponse> response) {
                AppConstants.mAllPlacesList.clear();
                Log.d("Response",response.message()+response.code()+response.toString());
                poiList.clear();
                poiList = response.body().getPois();
                for(Poi curPoi:poiList){
                    AppConstants.mAllPlacesList.add(curPoi.getName());
                }
                mSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<POIResponse> call, Throwable t) {
                Log.d("POST FAILED",t.getMessage()+t.getCause());

            }
        });


        //GetPOIMappingInterface mappingInterface = RetrofitClient.getClient(AppConstants.BASE_URL_TOMTOM).create(GetPOIMappingInterface.class);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch(view.getId()) {
            case R.id.eTSearch:
                if(b){
                    if(!eTSearch.getText().toString().isEmpty())
                        btnSearch.setVisibility(View.VISIBLE);
                }
                else {
                    if(eTSearch.getText().toString().isEmpty())
                        btnSearch.setVisibility(View.GONE);
                }
        }
    }
}
