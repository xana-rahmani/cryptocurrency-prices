package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.example.cryptho.adaptor.ListOfCoinsAdapter;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private MainHandler mainHandler;

    public ListOfCoinsAdapter listOfCoinsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new MainHandler(this, Looper.getMainLooper());

        this.mmv = MainModelView.getInstance();

        // Recycler View
        RecyclerView recyclerListOfCoins = findViewById(R.id.recyclerListOfCoins);
        listOfCoinsAdapter = new ListOfCoinsAdapter(this);
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

    public void showMoreCoin(View view) {
        mmv.showMoreCoin(mainHandler);
    }
}