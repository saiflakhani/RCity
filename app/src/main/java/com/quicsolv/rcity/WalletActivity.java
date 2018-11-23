package com.quicsolv.rcity;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends Activity {

    private RecyclerView recyclerView;
    private WalletAdapter walletAdapter;
    private List<WalletData> walletDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initCollapsingToolbar();

        TextView tVCurrentBalance = findViewById(R.id.tVBalance);
        tVCurrentBalance.setText("\u20B91091.00");

        recyclerView = findViewById(R.id.rVMain);

        walletAdapter = new WalletAdapter(walletDataList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(walletAdapter);

        prepareWalletData();

    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(" ");
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("My  Wallet");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    private void prepareWalletData() {
        WalletData walletData = new WalletData("Zara Store", "September 09, 2018", "- \u20B9  50.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("Multi Store", "September 05, 2018", "+ \u20B9  520.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayPal Transfer", "August 21, 2018", "- \u20B9  50.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayTM", "June 09, 2018", "+ \u20B9  520.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayTM", "April 15, 2018", "+ \u20B9  520.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("Multi Store", "April 05, 2018", "- \u20B9  180.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayPal Transfer", "March 28, 2018", "+ \u20B9  50.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayTM", "March 09, 2018", "+ \u20B9  220.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayTM", "February 27, 2018", "- \u20B9  520.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("Multi Store", "February 22, 2018", "- \u20B9  170.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayPal Transfer", "February 21, 2018", "+ \u20B9  50.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayTM", "February 13, 2018", "+ \u20B9  100.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayTM", "January 26, 2018", "- \u20B9  340.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("Zara", "January 26, 2018", "- \u20B9  170.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("PayPal Transfer", "January 21, 2018", "+ \u20B9  50.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("Domino's Pizza", "January 13, 2018", "+ \u20B9  100.00", getDrawable(R.drawable.diagonal_credit));
        walletDataList.add(walletData);
        walletData = new WalletData("PVR Cinemas", "December 25, 2017", "- \u20B9  340.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);
        walletData = new WalletData("PVR Cinemas", "December 21, 2017", "- \u20B9  340.00", getDrawable(R.drawable.diagonal_debit));
        walletDataList.add(walletData);

        walletAdapter.notifyDataSetChanged();
    }

}
