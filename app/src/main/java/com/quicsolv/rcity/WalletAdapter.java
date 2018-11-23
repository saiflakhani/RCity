package com.quicsolv.rcity;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder> {

    private List<WalletData> walletDataList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tVStore, tVDate, tVAmount;
        public ImageView iVTransaction;

        public MyViewHolder(View view) {
            super(view);
            tVStore = view.findViewById(R.id.tVStore);
            tVDate = view.findViewById(R.id.tVDate);
            tVAmount = view.findViewById(R.id.tVAmount);
            iVTransaction = view.findViewById(R.id.iVTransaction);
        }

    }

    public WalletAdapter(List<WalletData> walletDataList, Context context) {
        this.walletDataList = walletDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public WalletAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wallet, viewGroup, false);

        return new WalletAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        WalletData walletData = walletDataList.get(i);
        myViewHolder.tVStore.setText(walletData.gettVStore());
        myViewHolder.tVDate.setText(walletData.gettVDate());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(walletData.gettVAmount().startsWith("-"))
                myViewHolder.tVAmount.setTextColor(context.getColor(R.color.colorDebit));
            else myViewHolder.tVAmount.setTextColor(context.getColor(R.color.colorCredit));

        }
        myViewHolder.tVAmount.setText(walletData.gettVAmount());
        myViewHolder.iVTransaction.setImageDrawable(walletData.getiVTransaction());
    }

    @Override
    public int getItemCount() {
        return walletDataList.size();
    }
}
