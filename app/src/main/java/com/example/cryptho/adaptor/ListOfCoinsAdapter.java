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

import com.example.cryptho.R;
import com.example.cryptho.utils.CoinData;

import java.util.ArrayList;


public class ListOfCoinsAdapter extends RecyclerView.Adapter<ListOfCoinsAdapter.ViewHolder>{

    Context context;
    public ArrayList<CoinData> coinsData = new ArrayList();

    /**
     * Initialize the dataset of the Adapter.
     */
    public ListOfCoinsAdapter(Context ctx){
        context = ctx;
    }


    public void updateRecyclerViewData(ArrayList<CoinData> cd) {
        coinsData = cd;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView coinName, coinPercentChange1h;
        public ViewHolder(@NonNull View view){
            super(view);
            // Define click listener for the ViewHolder's View
            coinName = (TextView) view.findViewById(R.id.coinName);
            coinPercentChange1h = (TextView) view.findViewById(R.id.coinPercentChange1h);
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
        holder.coinName.setText(
                String.format("%s", coinsData.get(position).getName())
        );

        int color = 0;
        if (coinsData.get(position).getPercent_change_1h() > 0)
            color = Color.GREEN;
        else if (coinsData.get(position).getPercent_change_1h() < 0)
            color = Color.RED;

        holder.coinPercentChange1h.setTextColor(color);
        holder.coinPercentChange1h.setText(
                String.format("%.2f%%", coinsData.get(position).getPercent_change_1h())
        );
    }

    /** Return the size of your data set (invoked by the layout manager) **/
    @Override
    public int getItemCount() {
        return coinsData.size();
    }
}

// Log.v("TAG", "handling Massage: mHandler");