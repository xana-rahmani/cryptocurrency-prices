package com.example.cryptho.adaptor;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptho.R;
import com.example.cryptho.data.CandleStick;

import java.util.ArrayList;


public class OHLCAdapter extends RecyclerView.Adapter<OHLCAdapter.ViewHolder>{

    Context context;
    public ArrayList<CandleStick> OHLC = new ArrayList();

    /**
     * Initialize the dataset of the Adapter.
     */
    public OHLCAdapter(Context ctx){
        context = ctx;
    }


    public void updateRecyclerViewData(ArrayList<CandleStick> OHLC) {
        this.OHLC = OHLC;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView openPrice,highPrice ,closePrice, lowPrice,number;
        public ViewHolder(@NonNull View view){
            super(view);
            // Define click listener for the ViewHolder's View
            openPrice = view.findViewById(R.id.openprice);
            highPrice = view.findViewById(R.id.highprice);
            lowPrice = view.findViewById(R.id.lowprice);
            closePrice = view.findViewById(R.id.closeprice);
             number = view.findViewById(R.id.number);
        }
    }

    /** Create new views (invoked by the layout manager) **/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.candle_stick_row, parent, false);
        return new ViewHolder(view);
    }

    /** Replace the contents of a view (invoked by the layout manager) **/
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.openPrice.setText(
                String.format("%.2f$", OHLC.get(position).getOpen())
        );


        int GREEN = 0xFF3E8E14;
        int RED = 0xFFAE0000;
        holder.highPrice.setText(String.format("%.2f$", OHLC.get(position).getHigh()));
        holder.highPrice.setTextColor(GREEN);

        holder.lowPrice.setText(String.format("%.2f$", OHLC.get(position).getLow()));
        holder.lowPrice.setTextColor(RED);

        holder.closePrice.setText(String.format("%.2f$", OHLC.get(position).getClose()));
        holder.number.setText(String.format("%d )", position + 1));
    }

    /** Return the size of your data set (invoked by the layout manager) **/
    @Override
    public int getItemCount() {
        return OHLC.size();
    }
}
