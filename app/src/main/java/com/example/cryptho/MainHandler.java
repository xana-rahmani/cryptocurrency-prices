package com.example.cryptho;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cryptho.utils.DataHolder;

import java.lang.ref.WeakReference;

public class MainHandler extends Handler {
    private static final String TAG = "Main Handler";
    private final WeakReference<MainActivity> mainActivityWeakReference;
    final private DataHolder dataHolder = DataHolder.getInstance();

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
                updateCoinsDataRecyclerView();
                break;
        }
    }


    private void updateCoinsDataRecyclerView() {
        MainActivity main = mainActivityWeakReference.get();
        main.listOfCoinsAdapter.updateRecyclerViewData(dataHolder.getCoinsData());
        main.listOfCoinsAdapter.notifyDataSetChanged();  // D'ont Work
    }
}
