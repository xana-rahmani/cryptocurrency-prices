package com.example.cryptho;

import android.annotation.SuppressLint;
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
    static private MainModelView me;

    public ArrayList<String> cities = new ArrayList<>();

    private int numberOfCoins = 0;

    // Thread Pool
    int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    int KEEP_ALIVE_TIME = 2;
    TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    ExecutorService networkExecutorService = new ThreadPoolExecutor(NUMBER_OF_CORES,
            NUMBER_OF_CORES*2,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            taskQueue);


    static public MainModelView get() {
        if (me == null) {
            me = new MainModelView();
        }
        return me;
    }

    public void showMoreCoin(Handler handler) {
        // Coin Market Cap Api Info
        final String UrlFormat = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=%d&limit=10&convert=USD";
        final String ApiToken = "7fc06983-3d6c-437a-8bc5-09bd5b4d19bc";
        final String HeaderFormat = "X-CMC_PRO_API_KEY";

        String url = String.format(UrlFormat, numberOfCoins + 1);

        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {

                HttpRequest httpRequest = new HttpRequest();
                Response response = httpRequest.call(url, ApiToken, HeaderFormat);

                if (response == null) return;

                try {
                    Log.v("ans: ", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
