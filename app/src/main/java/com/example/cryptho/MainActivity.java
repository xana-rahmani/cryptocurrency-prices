package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.View;

import com.example.cryptho.adaptor.ListOfCoinsAdapter;
import com.example.cryptho.utils.DataHolder;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private HandlerThread handlerThread = new HandlerThread("HandlerThread");
    private MainHandler mainHandler;
    private DataHolder dataHolder = DataHolder.getInstance();


    // RecyclerView: list of coins data
    private RecyclerView recyclerListOfCoins;
    public ListOfCoinsAdapter listOfCoinsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread.start();
        mainHandler = new MainHandler(this, handlerThread.getLooper());

        this.mmv = MainModelView.getInstance();
        recyclerListOfCoins = findViewById(R.id.recyclerListOfCoins);
        listOfCoinsAdapter = new ListOfCoinsAdapter(this,  dataHolder.getCoinsData());
        recyclerListOfCoins.setAdapter(listOfCoinsAdapter);
        recyclerListOfCoins.setLayoutManager(new LinearLayoutManager(this));

        if (isConnected()){
            mmv.showMoreCoin(mainHandler);
        }
        // TODO : go and download from cash or show error message if first time
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    public void showMoreCoin(View view) {
        mmv.showMoreCoin(mainHandler);
    }
}