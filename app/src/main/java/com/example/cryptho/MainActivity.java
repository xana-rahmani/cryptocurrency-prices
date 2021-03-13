package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.cryptho.adaptor.ListOfCoinsAdapter;
import com.example.cryptho.data.MyMessage;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private MainHandler mainHandler;

    public ListOfCoinsAdapter listOfCoinsAdapter;
    final private MyMessage myMessage = new MyMessage();

    public Button btnLoadMore;
    public Button btnReload;
    public ProgressBar pbLoadingMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoadMore = (Button) findViewById(R.id.showMoreCoin);
        btnReload = (Button) findViewById(R.id.btnReload);
        pbLoadingMore = (ProgressBar) findViewById(R.id.pbLoadingCoins);

        mainHandler = new MainHandler(this, Looper.getMainLooper());

        this.mmv = MainModelView.getInstance();

        // Recycler View
        RecyclerView recyclerListOfCoins = findViewById(R.id.recyclerListOfCoins);
        listOfCoinsAdapter = new ListOfCoinsAdapter(this, this);
        recyclerListOfCoins.setAdapter(listOfCoinsAdapter);
        recyclerListOfCoins.setLayoutManager(new LinearLayoutManager(this));

        if (isConnected()) {
            deactiveShowMoreButton();
            mmv.showMoreCoin(mainHandler);
        } else {
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

    public void reloadCoinList(View view) {
        deactiveShowMoreButton();
        if (!isConnected()) {
            Message msg = Message.obtain();
            msg.what = myMessage.SHOW_NOTIFICATION;
            msg.arg1 = 0;
            mainHandler.sendMessage(msg);
        } else mmv.reloadCoinList(mainHandler);
    }

    public void showMoreCoin(View view) {
        deactiveShowMoreButton();
        if (!isConnected()) {
            Message msg = Message.obtain();
            msg.what = myMessage.SHOW_NOTIFICATION;
            msg.arg1 = 0;
            mainHandler.sendMessage(msg);
        } else mmv.showMoreCoin(mainHandler);
    }

    /*
    this function switches to the ohlc activity.
     */
    public void showCoinChart(String coinSymbol) {
        Intent switchActivityIntent = new Intent(this, OHLCActivity.class);
        switchActivityIntent.putExtra("symbol", coinSymbol);
        startActivity(switchActivityIntent);
    }

    public void deactiveShowMoreButton() {
        btnLoadMore.setVisibility(View.INVISIBLE);
        btnReload.setVisibility(View.INVISIBLE);
        pbLoadingMore.setVisibility(View.VISIBLE);
    }

    public void activeShowMoreButton() {
        btnLoadMore.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.VISIBLE);
        pbLoadingMore.setVisibility(View.INVISIBLE);
    }
}