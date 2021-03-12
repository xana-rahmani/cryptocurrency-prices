package com.example.cryptho;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptho.adaptor.OHLCAdapter;


public class OHLCActivity extends AppCompatActivity {
    String X_CoinAPI_Key = "45F7D1DF-B1F0-4C75-A7FC-00F8DD635DF1";
    private OHLC_Handler OHLC_handler;
    private OHlC_Model_View omv;
    public OHLCAdapter OHLC_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohlc);
        OHLC_handler = new OHLC_Handler(this, Looper.getMainLooper());
        this.omv = OHlC_Model_View.getInstance();
        // Recycler View
        RecyclerView recycler_of_OHLC = findViewById(R.id.recyclerofohlc);
        OHLC_adapter = new OHLCAdapter(this, this);
        recycler_of_OHLC.setAdapter(OHLC_adapter);
        recycler_of_OHLC.setLayoutManager(new LinearLayoutManager(this));
        Button button7 = findViewById(R.id.btn7candles);
        Button button30 = findViewById(R.id.btn30candles);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                omv.get_New_OHLC(OHLC_handler,getIntent().getStringExtra("symbol"),HttpRequest.Range.weekly);

            }
        });
        button30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                omv.get_New_OHLC(OHLC_handler,getIntent().getStringExtra("symbol"),HttpRequest.Range.oneMonth);

            }
        });
    }
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }








}