package com.quicsolv.rcity;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quicsolv.rcity.mapping.GetPOIMappingInterface;
import com.quicsolv.rcity.mapping.MappingPostBody;
import com.quicsolv.rcity.mapping.POIMapping;
import com.quicsolv.rcity.mapping.PoiMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnIndoorStateChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private GoogleMap mMap;

    private static final int REQUEST_RESOLVE_ERROR = 100;
    private static final int REQUEST_PERMISSION = 42;
    private GoogleApiClient mGoogleApiClient;
    private TextView tVDebug;
    private AutoCompleteTextView actvSearch;
    private Button btnSearch;
    private ImageButton btnLocateMe;
    private MessagesClient mMessagesClient;
    private ArrayAdapter<String> mSearchAdapter;
    private ArrayList<String> mAllPlacesList = new ArrayList<>();
    private static final String TAG =
            MapsActivity.class.getSimpleName();


    private static String curPoiId = "-1";
    private static Poi curPoi= null;
    public static List<Poi> poiList = new ArrayList<>();
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    UserProfile userProfile;
    private static Poi destPOI = null;


    LatLng latLng = null;

    //private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST_CODE = 1111;

    private static final String KEY_SUBSCRIBED = "subscribed";

    private static boolean isNavOn = false;

    private static String lastLocation = "-1";


    /**
     * The container {@link android.view.ViewGroup} for the minimal UI associated with this sample.
     */
    private RelativeLayout mContainer;


    private boolean mSubscribed = false;


    private MessageListener mMessageListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_stores);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tVDebug = findViewById(R.id.tVDebugView);
        actvSearch = findViewById(R.id.actSearch);


        mSearchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mAllPlacesList);
        actvSearch.setAdapter(mSearchAdapter);

        dealsButton();


        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                final Message msg1 = message;
                Log.d(TAG, "Found message: " + new String(message.getContent()));
                tVDebug.setText("You are near "+new String(msg1.getContent()));
                lastLocation = new String(msg1.getContent());
                handleUserLocation(new String(msg1.getContent()));
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            }
        };

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(searchClickListener);
        checkAndAskPermissions();
        userProfile = (UserProfile) getIntent().getSerializableExtra("UserProfile");

        btnLocateMe = findViewById(R.id.iBtnLocateMe);
        btnLocateMe.setOnClickListener(locateListener);

    }

    private void handleUserLocation(String msg1){
        if(mAllPlacesList.contains(msg1)){
            //int place = mAllPlacesList.indexOf(new String(msg1.getContent()));
            for(Poi poi : poiList){
                if(poi.getName().equalsIgnoreCase(msg1)){
                    curPoiId = poi.getPuid();
                    curPoi = poi;
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
                    mMap.clear();
                    LatLng currentLocation = new LatLng(Double.parseDouble(poi.getCoordinatesLat()),Double.parseDouble(poi.getCoordinatesLon()));
                    mMap.addMarker(new MarkerOptions().position(currentLocation).icon(icon));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 19));
                    if(isNavOn){
                        plotRoute(curPoi.getPuid(),destPOI.getPuid());
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(curPoi.getCoordinatesLat()),Double.parseDouble(curPoi.getCoordinatesLon()))).icon(icon));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(destPOI.getCoordinatesLat()),Double.parseDouble(destPOI.getCoordinatesLon()))));
                    }
                }
            }
        }
    }


    private void dealsButton() {
        ImageButton ibTn = findViewById(R.id.iBtnFloorUp);
        ibTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity.this,DealsActivity.class);
                startActivity(i);
            }
        });
    }

    private View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String searchShop = actvSearch.getText().toString();
            if(!searchShop.isEmpty()){
                if(mAllPlacesList.contains(searchShop)){
                    for(Poi poi : poiList){
                        if(poi.getName().equalsIgnoreCase(searchShop)){
                            if(curPoiId.equals("-1")) {
                                plotRoute("poi_2ea3e34f-9912-41df-8484-973004aa598f", poi.getPuid());
                                Log.d(TAG, "POI ID " + poi.getPuid());
                            }
                            else {
                                plotRoute(curPoiId, poi.getPuid());
                                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
                                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(curPoi.getCoordinatesLat()),Double.parseDouble(curPoi.getCoordinatesLon()))).icon(icon));
                                isNavOn = true;
                                destPOI = poi;
                                final TextView tVCancel = findViewById(R.id.tVCancelNav);
                                tVCancel.setVisibility(View.VISIBLE);
                                tVCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isNavOn = false;
                                        mMap.clear();
                                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.blue_dot);
                                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(curPoi.getCoordinatesLat()),Double.parseDouble(curPoi.getCoordinatesLon()))).icon(icon));
                                        tVCancel.setVisibility(View.GONE);
                                    }
                                });
                            }
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(poi.getCoordinatesLat()),Double.parseDouble(poi.getCoordinatesLon()))));

                        }
                    }
                }else{
                    Toast.makeText(MapsActivity.this,"Could not find the requested place",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(MapsActivity.this,"Please select a place",Toast.LENGTH_SHORT).show();
            }
        }
    };






    private void checkAndAskPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

                mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                        .setPermissions(NearbyPermissions.BLE)
                        .build());
                foregroundSubscribe();
                backgroundSubscribe();


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

    private void foregroundSubscribe() {
        Log.i(TAG, "Subscribing.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(mMessageListener, options);
    }

    private boolean havePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void doRetroFit() {
        try {
            Toast.makeText(this, userProfile.getFullName() + "\n" + userProfile.getEmailId(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d(TAG,"Coming  from nearby");
        }

        GetPOIInterface poiInterface = RetrofitClient.getClient(AppConstants.BASE_URL_TOMTOM).create(GetPOIInterface.class);

        poiInterface.postNearby(new PostBody("username","pass","building_a4adff86-0a9c-44f4-ad0c-f709a4f91451_1542360867089")).enqueue(new Callback<POIResponse>() {
            @Override
            public void onResponse(Call<POIResponse> call, Response<POIResponse> response) {
                mAllPlacesList.clear();
                Log.d("Response",response.message()+response.code()+response.toString());
                poiList.clear();
                poiList = response.body().getPois();
                BitmapDescriptor defaultIcon = BitmapDescriptorFactory.fromResource(R.drawable.circle_default);
                for(Poi curPoi:poiList){
                    MarkerOptions opt = new MarkerOptions().position(new LatLng(Double.parseDouble(curPoi.getCoordinatesLat()),Double.parseDouble(curPoi.getCoordinatesLon()))).title(curPoi.getName()).snippet(curPoi.getDescription()).icon(defaultIcon);
                    if(!curPoi.getName().equals("Connector")) {
                        mMap.addMarker(opt); //DON'T ADD TO MAP IF IT IS A CONNECTOR
                    }
                    mAllPlacesList.add(curPoi.getName());
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



    private void plotRoute(String startId,String endId){
        mMap.clear();
        GetPOIMappingInterface mappingInterface = RetrofitClient.getClient(AppConstants.BASE_URL_TOMTOM).create(GetPOIMappingInterface.class);
        mappingInterface.postNearby(new MappingPostBody("username","pass",startId,endId)).enqueue(new Callback<POIMapping>() {
            @Override
            public void onResponse(Call<POIMapping> call, Response<POIMapping> response) {

                Log.d("Response",response.message()+response.code()+response.toString());
                List<PoiMap> routes = response.body().getPois();
                ArrayList<LatLng> coordinates = new ArrayList<>();

                for(PoiMap curPOI:routes) {
                    coordinates.add(new LatLng(Double.parseDouble(curPOI.getLat()), Double.parseDouble(curPOI.getLon())));

                }
                //Iterable<LatLng> latLngIterable = coordinates.iterator();
                mMap.addPolyline(new PolylineOptions().addAll(coordinates));
            }

            @Override
            public void onFailure(Call<POIMapping> call, Throwable t) {
                Log.d("POST FAILED",t.getMessage()+t.getCause());
            }
        });
    }

    private LatLng nearestConnector(Poi poi){

        GetPOIMappingInterface mappingInterface = RetrofitClient.getClient(AppConstants.BASE_URL_TOMTOM).create(GetPOIMappingInterface.class);
        mappingInterface.postNearby(new MappingPostBody("username","pass",poi.getPuid(),"poi_2ea3e34f-9912-41df-8484-973004aa598f")).enqueue(new Callback<POIMapping>() {
            @Override
            public void onResponse(Call<POIMapping> call, Response<POIMapping> response) {

                Log.d("Response",response.message()+response.code()+response.toString());
                List<PoiMap> routes = response.body().getPois();
                ArrayList<LatLng> coordinates = new ArrayList<>();

                for(PoiMap curPOI:routes) {
                    coordinates.add(new LatLng(Double.parseDouble(curPOI.getLat()), Double.parseDouble(curPOI.getLon())));

                }
                latLng = coordinates.get(1);
                //Iterable<LatLng> latLngIterable = coordinates.iterator();
                //mMap.addPolyline(new PolylineOptions().addAll(coordinates));
            }

            @Override
            public void onFailure(Call<POIMapping> call, Throwable t) {
                Log.d("POST FAILED",t.getMessage()+t.getCause());

            }
        });

        return latLng;
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

    }

    @Override
    public void onStop() {
        //Nearby.getMessagesClient(this).unsubscribe(mMessageListener);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(havePermissions()){
            //buildGoogleApiClient();
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
                        mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                                .setPermissions(NearbyPermissions.BLE)
                                .build());
                        foregroundSubscribe();
                        backgroundSubscribe();
                    }

                } else {
                    Toast.makeText(MapsActivity.this,"Need this permission",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
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
    @Override
    public void onIndoorBuildingFocused() {

    }

    @Override
    public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    private View.OnClickListener locateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!lastLocation.equals("-1")){
                handleUserLocation(lastLocation);
            }else{
                Toast.makeText(MapsActivity.this,"Couldn't locate you",Toast.LENGTH_SHORT).show();
            }
        }
    };


}


