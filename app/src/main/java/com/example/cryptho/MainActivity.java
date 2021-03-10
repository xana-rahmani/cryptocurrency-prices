package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private HandlerThread handlerThread = new HandlerThread("HandlerThread");
    private MainHandler mainHandler;

    // RecyclerView: list of coins data
    public RecyclerView recyclerListOfCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread.start();
        mainHandler = new MainHandler(this, handlerThread.getLooper());

        this.mmv = MainModelView.getInstance();
        recyclerListOfCoins = findViewById(R.id.recyclerListOfCoins);

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