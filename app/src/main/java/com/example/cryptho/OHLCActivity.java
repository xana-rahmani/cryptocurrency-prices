package com.example.cryptho;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptho.adaptor.OHLCAdapter;
import com.example.cryptho.data.MyMessage;


public class OHLCActivity extends AppCompatActivity {
    private OHLC_Handler OHLC_handler;
    private OHlC_Model_View omv;
    public OHLCAdapter OHLC_adapter;
    final private MyMessage myMessage = new MyMessage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ohlc);
        OHLC_handler = new OHLC_Handler(this, Looper.getMainLooper());
        this.omv = OHlC_Model_View.getInstance();

        // Recycler View
        RecyclerView recycler_of_OHLC = findViewById(R.id.recyclerofohlc);
        OHLC_adapter = new OHLCAdapter(this);
        recycler_of_OHLC.setAdapter(OHLC_adapter);
        recycler_of_OHLC.setLayoutManager(new LinearLayoutManager(this));
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
    public void onBackPressed()
    {
        finish();
    }
}