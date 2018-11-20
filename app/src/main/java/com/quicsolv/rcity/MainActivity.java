package com.quicsolv.rcity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {


    LinearLayout lLDirectOffers, lLFindRoutes, lLFindFF, lLFindCar, lLPrivateShopping, lLPreorderDeals, lLFoodOffers, lLWallet, lLEvacuationGuide;
    EditText eTSearch;
    Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        getIds();

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

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.lLFindRoutes:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
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
