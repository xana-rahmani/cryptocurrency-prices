package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private HandlerThread handlerThread = new HandlerThread("HandlerThread");
    private MainHandler mainHandler;

    public TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.main_text);

        handlerThread.start();
        mainHandler = new MainHandler(this, handlerThread.getLooper());

        this.mmv = MainModelView.get();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();

    }

    public void clickMe(View view) {
        mmv.updateCities(mainHandler);
    }

}