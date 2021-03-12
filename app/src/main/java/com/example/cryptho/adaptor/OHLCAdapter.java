package com.example.cryptho.adaptor;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptho.OHLCActivity;
import com.example.cryptho.R;
import com.example.cryptho.data.CandleStick;

import java.util.ArrayList;


public class OHLCAdapter extends RecyclerView.Adapter<OHLCAdapter.ViewHolder>{

    Context context;
    public ArrayList<CandleStick> OHLC = new ArrayList();
    private OHLCActivity OHLCActivity;

    /**
     * Initialize the dataset of the Adapter.
     */
    public OHLCAdapter(Context ctx, OHLCActivity OHLCActivity){
        context = ctx;
        this.OHLCActivity = OHLCActivity;
    }


    public void updateRecyclerViewData(ArrayList<CandleStick> OHLC) {
        this.OHLC = OHLC;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView openPrice,highPrice ,closePrice, lowPrice;
        public ViewHolder(@NonNull View view){
            super(view);
            // Define click listener for the ViewHolder's View
            openPrice = (TextView) view.findViewById(R.id.openprice);
            highPrice = (TextView) view.findViewById(R.id.highprice);
            lowPrice = (TextView) view.findViewById(R.id.lowprice);
            closePrice = (TextView) view.findViewById(R.id.closeprice);
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
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainActivity.showCoinChart(coinsData.get(position).getSymbol());
//            }
//        });

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
    }

    /** Return the size of your data set (invoked by the layout manager) **/
    @Override
    public int getItemCount() {
        return OHLC.size();
    }
}

// Log.v("TAG", "handling Massage: mHandler");