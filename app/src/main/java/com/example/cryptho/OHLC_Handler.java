package com.example.cryptho;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cryptho.data.DataHolder;

import java.lang.ref.WeakReference;

public class OHLC_Handler extends Handler {
    private static final String TAG = "OHLC Handler";
    private final WeakReference<com.example.cryptho.OHLCActivity> ohlcActivityWeakReference;
//    final private DataHolder dataHolder = DataHolder.getInstance();

    // Messages
    final private int UPDATE_OHLC = 1;

    public OHLC_Handler(com.example.cryptho.OHLCActivity OHLCActivity, Looper looper) {
        super(looper);
        ohlcActivityWeakReference = new WeakReference<>(OHLCActivity);
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case UPDATE_OHLC:
                updateCoinsDataRecyclerView();
                break;
        }
    }
    private void updateCoinsDataRecyclerView() {
        com.example.cryptho.OHLCActivity main = ohlcActivityWeakReference.get();

        Log.v("Thread-is: ", String.valueOf(Looper.myLooper() == Looper.getMainLooper()));  //TODO: Remove this line

        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main.OHLC_adapter.updateRecyclerViewData(DataHolder.getInstance().get_OHLC());
                main.OHLC_adapter.notifyDataSetChanged();  // D'ont Work
            }
        });
    }
}
