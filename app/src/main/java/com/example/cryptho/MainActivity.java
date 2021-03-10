package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.HandlerThread;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private HandlerThread handlerThread = new HandlerThread("HandlerThread");
    private MainHandler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread.start();
        mainHandler = new MainHandler(this, handlerThread.getLooper());

        this.mmv = MainModelView.getInstance();
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