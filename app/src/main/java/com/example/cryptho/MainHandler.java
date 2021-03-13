package com.example.cryptho;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cryptho.data.DataHolder;
import com.example.cryptho.data.MyMessage;

import java.lang.ref.WeakReference;

public class MainHandler extends Handler {
    private static final String TAG = "Main Handler";
    private final WeakReference<MainActivity> mainActivityWeakReference;
    final private DataHolder dataHolder = DataHolder.getInstance();
    final private MyMessage myMessage = new MyMessage();


    public MainHandler(MainActivity mainActivity, Looper looper) {
        super(looper);
        mainActivityWeakReference = new WeakReference<>(mainActivity);
    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:  // SHOW_NOTIFICATION
                showNotification(myMessage.getNotifText(msg.arg1));
                break;
            case 1: // UPDATE_COINS_DATA_LIST
                updateCoinsDataRecyclerView();
                break;
        }
    }


    private void updateCoinsDataRecyclerView() {
        MainActivity main = mainActivityWeakReference.get();
        main.listOfCoinsAdapter.updateRecyclerViewData(dataHolder.getCoinsData());
        main.listOfCoinsAdapter.notifyDataSetChanged();
        main.activeShowMoreButton();
    }

    private void showNotification(String notif_text) {
        MainActivity main = mainActivityWeakReference.get();
        Toast.makeText(main.getApplicationContext(), notif_text, Toast.LENGTH_LONG).show();
        main.activeShowMoreButton();
    }

}
