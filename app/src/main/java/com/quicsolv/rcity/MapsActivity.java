package com.quicsolv.rcity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.quicsolv.rcity.RetrofitClient.BASE_URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_stores);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




    }

    private void doRetroFit() {
        GetPOIInterface poiInterface = RetrofitClient.getClient().create(GetPOIInterface.class);



        poiInterface.postNearby(new PostBody("username","pass","building_a4adff86-0a9c-44f4-ad0c-f709a4f91451_1542360867089")).enqueue(new Callback<POIResponse>() {
            @Override
            public void onResponse(Call<POIResponse> call, Response<POIResponse> response) {
                Log.d("Response",response.message()+response.code()+response.toString());
                List<Poi> poiList = response.body().getPois();

                for(Poi curPoi:poiList){
                    MarkerOptions opt = new MarkerOptions().position(new LatLng(Double.parseDouble(curPoi.getCoordinatesLat()),Double.parseDouble(curPoi.getCoordinatesLon()))).title(curPoi.getName()).snippet(curPoi.getDescription());
                    if(!curPoi.getName().equals("Connector"))mMap.addMarker(opt);
                }
            }

            @Override
            public void onFailure(Call<POIResponse> call, Throwable t) {
                Log.d("POST FAILED",t.getMessage()+t.getCause());

            }
        });


        GetPOIMappingInterface mappingInterface = RetrofitClient.getClient().create(GetPOIMappingInterface.class);

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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng rCity = new LatLng(19.098805,72.915914);
        //mMap.addMarker(new MarkerOptions().position(rCity).title("Marker in RCity"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rCity));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        doRetroFit();
    }
}
