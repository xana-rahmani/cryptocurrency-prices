package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.example.cryptho.adaptor.ListOfCoinsAdapter;
import com.example.cryptho.data.MyMessage;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private MainHandler mainHandler;

    public ListOfCoinsAdapter listOfCoinsAdapter;
    final private MyMessage myMessage = new MyMessage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new MainHandler(this, Looper.getMainLooper());

        this.mmv = MainModelView.getInstance();

        // Recycler View
        RecyclerView recyclerListOfCoins = findViewById(R.id.recyclerListOfCoins);
        listOfCoinsAdapter = new ListOfCoinsAdapter(this, this);
        recyclerListOfCoins.setAdapter(listOfCoinsAdapter);
        recyclerListOfCoins.setLayoutManager(new LinearLayoutManager(this));

        if (isConnected()){
            mmv.showMoreCoin(mainHandler);
        }
        else {
            // Disconnected
            Message msg = Message.obtain();
            msg.what = myMessage.SHOW_NOTIFICATION;
            msg.arg1 = 0;
            mainHandler.sendMessage(msg);
        }

        // TODO : go and download from cash or show error message if first time
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void reloadCoinList(View view){
        if (!isConnected()){
            Message msg = Message.obtain();
            msg.what = myMessage.SHOW_NOTIFICATION;
            msg.arg1 = 0;
            mainHandler.sendMessage(msg);
        }
        else mmv.reloadCoinList(mainHandler);
    }

    public void showMoreCoin(View view) {
        if (!isConnected()){
            Message msg = Message.obtain();
            msg.what = myMessage.SHOW_NOTIFICATION;
            msg.arg1 = 0;
            mainHandler.sendMessage(msg);
        }
        else mmv.showMoreCoin(mainHandler);
    }

    /*
    this function switches to the ohlc activity.
     */
    public void showCoinChart(String coinSymbol){
        Intent switchActivityIntent = new Intent(this, OHLCActivity.class);
        switchActivityIntent.putExtra("symbol",coinSymbol);
        startActivity(switchActivityIntent);
    }
}