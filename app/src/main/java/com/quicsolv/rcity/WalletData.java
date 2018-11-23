package com.quicsolv.rcity;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class WalletData {

    private String tVStore, tVDate, tVAmount;
    private Drawable iVTransaction;

    public String gettVStore() {
        return tVStore;
    }

    public void settVStore(String tVStore) {
        this.tVStore = tVStore;
    }

    public String gettVDate() {
        return tVDate;
    }

    public void settVDate(String tVDate) {
        this.tVDate = tVDate;
    }

    public String gettVAmount() {
        return tVAmount;
    }

    public void settVAmount(String tVAmount) {
        this.tVAmount = tVAmount;
    }

    public Drawable getiVTransaction() {
        return iVTransaction;
    }

    public void setiVTransaction(Drawable iVTransaction) {
        this.iVTransaction = iVTransaction;
    }

    public WalletData(String tVStore, String tVDate, String tVAmount, Drawable iVTransaction) {
        this.iVTransaction = iVTransaction;
        this.tVAmount = tVAmount;
        this.tVDate = tVDate;
        this.tVStore = tVStore;
    }


}
