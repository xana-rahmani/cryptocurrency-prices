package com.example.cryptho.adaptor;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptho.MainActivity;
import com.example.cryptho.R;
import com.example.cryptho.utils.CoinData;

import java.util.ArrayList;


public class ListOfCoinsAdapter extends RecyclerView.Adapter<ListOfCoinsAdapter.ViewHolder>{

    Context context;
    public ArrayList<CoinData> coinsData = new ArrayList();
    private  MainActivity mainActivity;

    /**
     * Initialize the dataset of the Adapter.
     */
    public ListOfCoinsAdapter(Context ctx, MainActivity mainActivity){
        context = ctx;
        this.mainActivity = mainActivity;
    }


    public void updateRecyclerViewData(ArrayList<CoinData> cd) {
        coinsData = cd;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView coinNameAndSymbol, coinPrice, coinPercentChange1h, coinPercentChange24h, coinPercentChange7D;
        public ViewHolder(@NonNull View view){
            super(view);
            // Define click listener for the ViewHolder's View
            coinNameAndSymbol = (TextView) view.findViewById(R.id.coinNameAndSymbol);
            coinPrice = (TextView) view.findViewById(R.id.coinPrice);
            coinPercentChange1h = (TextView) view.findViewById(R.id.coinPercentChange1h);
            coinPercentChange24h = (TextView) view.findViewById(R.id.coinPercentChange24h);
            coinPercentChange7D = (TextView) view.findViewById(R.id.coinPercentChange7D);
        }
    }

    /** Create new views (invoked by the layout manager) **/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coin_data_row, parent, false);
        return new ViewHolder(view);
    }

    /** Replace the contents of a view (invoked by the layout manager) **/
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showCoinChart(coinsData.get(position).getSymbol());
            }
        });

        holder.coinNameAndSymbol.setText(
                String.format("%s | %s",
                        coinsData.get(position).getSymbol(),
                        coinsData.get(position).getName())
        );


        holder.coinPrice.setText(String.format("%.2f$", coinsData.get(position).getPrice()));

        int color = 0;
        int GREEN = 0xFF3E8E14;
        int RED = 0xFFAE0000;

        if (coinsData.get(position).getPercent_change_1h() > 0)
            color = GREEN;
        else if (coinsData.get(position).getPercent_change_1h() < 0)
            color = RED;

        if (color != 0) holder.coinPercentChange1h.setTextColor(color);
        holder.coinPercentChange1h.setText(
                String.format("%d%%", coinsData.get(position).getPercent_change_1h()));


        color = 0;
        if (coinsData.get(position).getPercent_change_24h() > 0)
            color = GREEN;
        else if (coinsData.get(position).getPercent_change_24h() < 0)
            color = RED;

        if (color != 0) holder.coinPercentChange24h.setTextColor(color);
        holder.coinPercentChange24h.setText(
                String.format("%d%%", coinsData.get(position).getPercent_change_24h()));


        color = 0;
        if (coinsData.get(position).getPercent_change_7D() > 0)
            color = GREEN;
        else if (coinsData.get(position).getPercent_change_7D() < 0)
            color = RED;

        if (color != 0) holder.coinPercentChange7D.setTextColor(color);
        holder.coinPercentChange7D.setText(
                String.format("%d%%", coinsData.get(position).getPercent_change_7D()));


    }

    /** Return the size of your data set (invoked by the layout manager) **/
    @Override
    public int getItemCount() {
        return coinsData.size();
    }
}

// Log.v("TAG", "handling Massage: mHandler");