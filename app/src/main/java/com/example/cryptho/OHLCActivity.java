package com.example.cryptho;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptho.adaptor.OHLCAdapter;
import com.example.cryptho.data.MyMessage;


public class OHLCActivity extends AppCompatActivity implements View.OnClickListener {
    private OHLC_Handler OHLC_handler;
    private OHlC_Model_View omv;
    public OHLCAdapter OHLC_adapter;
    final private MyMessage myMessage = new MyMessage();
    // variable to track event time
    private long mLastClickTime = 0;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohlc);
        progressBar = findViewById(R.id.ohlcprogressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        OHLC_handler = new OHLC_Handler(this, Looper.getMainLooper(),progressBar);
        this.omv = OHlC_Model_View.getInstance();

        // Recycler View
        RecyclerView recycler_of_OHLC = findViewById(R.id.recyclerofohlc);
        OHLC_adapter = new OHLCAdapter(this);
        recycler_of_OHLC.setAdapter(OHLC_adapter);
        recycler_of_OHLC.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btn7candles).setOnClickListener(OHLCActivity.this);
        findViewById(R.id.btn30candles).setOnClickListener(OHLCActivity.this);
    }

    public void get30daysChartData(View view){
        if (!isConnected()){
            Message msg = Message.obtain();
            msg.what = myMessage.SHOW_NOTIFICATION;
            msg.arg1 = 0;
            OHLC_handler.sendMessage(msg);
        }
        else {
            omv.get_New_OHLC(OHLC_handler, getIntent().getStringExtra("symbol"),
                    HttpRequest.Range.oneMonth);
        }
    }

    public void get7daysChartData(View view){
        if (!isConnected()){
            Message msg = Message.obtain();
            msg.what = myMessage.SHOW_NOTIFICATION;
            msg.arg1 = 0;
            OHLC_handler.sendMessage(msg);
        }
        else {
            omv.get_New_OHLC(OHLC_handler, getIntent().getStringExtra("symbol"),
                    HttpRequest.Range.weekly);
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onClick(View v) {
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if ( v == findViewById(R.id.btn7candles)){
            OHLC_handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });
            get7daysChartData(v);
        }
        else if (v == findViewById(R.id.btn30candles)){
            OHLC_handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });
            get30daysChartData(v);
        }

    }

        // Handle button clicks
    @Override
    public void onBackPressed()
    {
        finish();
    }
}