package com.quicsolv.rcity;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.quicsolv.rcity.Interfaces.GetPOIInterface;
import com.quicsolv.rcity.mapping.GetPOIMappingInterface;
import com.quicsolv.rcity.mapping.MappingPostBody;
import com.quicsolv.rcity.mapping.POIMapping;
import com.quicsolv.rcity.mapping.PoiMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnIndoorStateChangeListener {

    private GoogleMap mMap;

    private static final int REQUEST_RESOLVE_ERROR = 100;
    private static final int REQUEST_PERMISSION = 42;
    private GoogleApiClient mGoogleApiClient;
    private TextView tVDebug;
    private static final String TAG =
            MapsActivity.class.getSimpleName();
    MessagesClient mMessagesClient;
    MessageListener mMessageListener;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_stores);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tVDebug = findViewById(R.id.tVDebug);

        checkAndAskPermissions();
        userProfile = (UserProfile) getIntent().getSerializableExtra("UserProfile");

        mMessageListener = new MessageListener(){
            @Override
            public void onFound(Message message){
                tVDebug.append(new String(message.getContent()));
            }
            @Override
            public void onLost(Message message){

            }
        };
        

    }

    private void checkAndAskPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                    .setPermissions(NearbyPermissions.BLE)
                    .build());
        }else{
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }

    private void doRetroFit() {

        Toast.makeText(this, userProfile.getFullName() + "\n" + userProfile.getEmailId(), Toast.LENGTH_SHORT).show();

        GetPOIInterface poiInterface = RetrofitClient.getClient(AppConstants.BASE_URL_TOMTOM).create(GetPOIInterface.class);

        poiInterface.postNearby(new PostBody("username","pass","building_a4adff86-0a9c-44f4-ad0c-f709a4f91451_1542360867089")).enqueue(new Callback<POIResponse>() {
            @Override
            public void onResponse(Call<POIResponse> call, Response<POIResponse> response) {
                Log.d("Response",response.message()+response.code()+response.toString());
                List<Poi> poiList = response.body().getPois();

                for(Poi curPoi:poiList){
                    MarkerOptions opt = new MarkerOptions().position(new LatLng(Double.parseDouble(curPoi.getCoordinatesLat()),Double.parseDouble(curPoi.getCoordinatesLon()))).title(curPoi.getName()).snippet(curPoi.getDescription());
                    if(!curPoi.getName().equals("Connector"))mMap.addMarker(opt); //DON'T ADD TO MAP IF IT IS A CONNECTOR
                }
            }

            @Override
            public void onFailure(Call<POIResponse> call, Throwable t) {
                Log.d("POST FAILED",t.getMessage()+t.getCause());

            }
        });


        GetPOIMappingInterface mappingInterface = RetrofitClient.getClient(AppConstants.BASE_URL_TOMTOM).create(GetPOIMappingInterface.class);

        mappingInterface.postNearby(new MappingPostBody("username","pass","poi_2ea3e34f-9912-41df-8484-973004aa598f","poi_d8730ee4-3cbc-4e21-b654-d7b14e880e62")).enqueue(new Callback<POIMapping>() {
            @Override
            public void onResponse(Call<POIMapping> call, Response<POIMapping> response) {
                Log.d("Response",response.message()+response.code()+response.toString());
                List<PoiMap> routes = response.body().getPois();
                ArrayList<LatLng> coordinates = new ArrayList<>();

                for(PoiMap curPOI:routes)
                    coordinates.add(new LatLng(Double.parseDouble(curPOI.getLat()),Double.parseDouble(curPOI.getLon())));
                //Iterable<LatLng> latLngIterable = coordinates.iterator();
                mMap.addPolyline(new PolylineOptions().addAll(coordinates));
            }

            @Override
            public void onFailure(Call<POIMapping> call, Throwable t) {
                Log.d("POST FAILED",t.getMessage()+t.getCause());
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setIndoorEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng rCity = new LatLng(19.1001368,72.916189);
        //mMap.addMarker(new MarkerOptions().position(rCity).title("Marker in RCity"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rCity));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        doRetroFit();
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribe();
    }

    @Override
    public void onStop() {
        //Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }
    // Subscribe to receive messages.
    private void subscribe() {
        Log.i(TAG, "Subscribing.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(mMessageListener, options);
        backgroundSubscribe();
    }


    private void backgroundSubscribe() {
        Log.i(TAG, "Subscribing for background updates.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(getPendingIntent(), options);
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, BeaconService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
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
                } else {
                    Toast.makeText(MapsActivity.this,"Need this permission",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    @Override
    public void onIndoorBuildingFocused() {

    }

    @Override
    public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {

    }
}
