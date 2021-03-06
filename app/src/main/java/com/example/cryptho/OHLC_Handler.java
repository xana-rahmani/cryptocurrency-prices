package com.example.cryptho;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cryptho.data.DataHolder;
import com.example.cryptho.data.MyMessage;
import java.lang.ref.WeakReference;

public class OHLC_Handler extends Handler {
    private static final String TAG = "OHLC Handler";
    private final WeakReference<OHLCActivity> ohlcActivityWeakReference;
    final private MyMessage myMessage = new MyMessage();
    final private ProgressBar progressBar;

    public OHLC_Handler(OHLCActivity OHLCActivity, Looper looper,ProgressBar progressBar) {
        super(looper);
        ohlcActivityWeakReference = new WeakReference<>(OHLCActivity);
        this.progressBar = progressBar;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:  // SHOW_NOTIFICATION
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                });
                showNotification(myMessage.getNotifText(msg.arg1));
                break;
            case 2: // UPDATE_OHLC
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                });
                updateOHCLRecyclerView();
                break;
        }
    }
    private void updateOHCLRecyclerView() {

        OHLCActivity main = ohlcActivityWeakReference.get();
        main.OHLC_adapter.updateRecyclerViewData(DataHolder.getInstance().get_OHLC());
        main.OHLC_adapter.notifyDataSetChanged();

    }

    private void showNotification(String notif_text) {
        OHLCActivity main = ohlcActivityWeakReference.get();
        Toast.makeText(main.getApplicationContext(), notif_text, Toast.LENGTH_LONG).show();
    }
}
