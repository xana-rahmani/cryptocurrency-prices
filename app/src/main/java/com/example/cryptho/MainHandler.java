package com.example.cryptho;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public class MainHandler extends Handler {
    private static final String TAG = "Main Handler";
    private final WeakReference<MainActivity> mainActivityWeakReference;

    // Messages
    final private int UPDATE_COINS_DATA_LIST = 1;

    public MainHandler(MainActivity mainActivity, Looper looper) {
        super(looper);
        mainActivityWeakReference = new WeakReference<>(mainActivity);
    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case UPDATE_COINS_DATA_LIST:
                Log.d("Debug","Message Is Received in Main Handler");  //TODO: Remove this line
                updateCoinsDataRecyclerView();
                break;
        }
    }


    private void updateCoinsDataRecyclerView() {
        Log.d("Debug","Update RecyclerView");  //TODO: Remove this line
        MainActivity main = mainActivityWeakReference.get();
        main.listOfCoinsAdapter.notifyDataSetChanged();
    }
}
