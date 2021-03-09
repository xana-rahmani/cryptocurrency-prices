package com.example.cryptho;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.decimal4j.util.DoubleRounder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainModelView {

    final private HttpRequest httpRequest = new HttpRequest();
    static private MainModelView me;

    public ArrayList<String> cities = new ArrayList<>();

//    private int startToGetCoins = 1;  // (start is 1-based index in coin market cap API)



    static public MainModelView get() {
        if (me == null) {
            me = new MainModelView();
        }
        return me;
    }

    public void showMoreCoin(Handler handler, int start) {
        // Coin Market Cap Api Info
        final String coinMarketCapUrlFormat = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=%d&limit=10&convert=USD";
        final String coinMarketCapApiToken = "7fc06983-3d6c-437a-8bc5-09bd5b4d19bc";
        final String coinMarketCapHeaderFormat = "X-CMC_PRO_API_KEY";


        String url = String.format(coinMarketCapUrlFormat, start);
        Response response = httpRequest.call(url, coinMarketCapApiToken, coinMarketCapHeaderFormat);

        if (response == null) return;

        try {
            Log.v("ans: ", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


