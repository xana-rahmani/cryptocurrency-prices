package com.example.cryptho;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public class MainHandler extends Handler {
    private static final String TAG = "Main Handler";
    private WeakReference<MainActivity> mainActivityWeakReference;

    public MainHandler(MainActivity mainActivity, Looper looper) {
        super(looper);
        mainActivityWeakReference = new WeakReference<>(mainActivity);
    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 1:
                logCities();
                break;
        }
    }

    private void logCities() {
        MainActivity main = mainActivityWeakReference.get();
        MainModelView modelView = MainModelView.getInstance();
        // main.tv.setText(modelView.cities.toString());
    }
}
