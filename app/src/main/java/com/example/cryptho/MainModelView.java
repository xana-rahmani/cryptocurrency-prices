package com.example.cryptho;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

public class MainModelView {
    static private MainModelView me;

    public ArrayList<String> cities = new ArrayList<>();

    static public MainModelView get() {
        if (me == null) {
            me = new MainModelView();
        }
        return me;
    }

    public void updateCities(Handler handler) {
        cities.add("Tehran");
        cities.add("Shiraz");
        Message msg = Message.obtain();
        msg.what = 1;
        handler.dispatchMessage(msg);
    }
}


