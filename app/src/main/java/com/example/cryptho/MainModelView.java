package com.example.cryptho;

import android.os.Handler;
import android.util.Log;

import org.decimal4j.util.DoubleRounder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import okhttp3.HttpUrl;
import okhttp3.Response;

public class MainModelView {
    static private MainModelView me;

    public ArrayList<String> cities = new ArrayList<>();

    private int NumberOfCoins = 0;
    private String CoinMarketCapUrl = null;

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
        String ApiToken = "7fc06983-3d6c-437a-8bc5-09bd5b4d19bc";
        String ApiHeaderFormat = "X-CMC_PRO_API_KEY";

        if (CoinMarketCapUrl == null) CreateCoinMarketCapUrl();
        // start is offset (1-based index) of the paginated list of items to return.
        int start = NumberOfCoins + 1;
        String url = CoinMarketCapUrl + "&start=" + start;

        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {

                HttpRequest httpRequest = new HttpRequest();
                Response response = httpRequest.call(url, ApiToken, ApiHeaderFormat);

                if (response == null) return;

                try {
                    // convert string response to json object.
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    AddNewCoinsData(jsonObject);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void CreateCoinMarketCapUrl(){
        String UrlFormat = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(UrlFormat).newBuilder();
        urlBuilder.addQueryParameter("limit", "10");
        urlBuilder.addQueryParameter("convert", "USD");
        CoinMarketCapUrl = urlBuilder.build().toString();
    }

    private void AddNewCoinsData(JSONObject jsonCoinsData){
        try {
            JSONArray dataArray = jsonCoinsData.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject coinData = dataArray.getJSONObject(i);
                String coin_name = coinData.getString("name");
                String coin_symbol = coinData.getString("symbol");

                JSONObject coinUsdValue = coinData.getJSONObject("quote").getJSONObject("USD");
                double coin_price = coinUsdValue.getDouble("price");
                double change_1h = DoubleRound(coinUsdValue.getDouble("percent_change_1h"));
                double change_24h = DoubleRound(coinUsdValue.getDouble("percent_change_24h"));
                double change_7d = DoubleRound(coinUsdValue.getDouble("percent_change_7d"));

                Log.d("#Debug", coin_name + " " + coin_symbol);  //TODO: for test

            }

            //TODO: After add new coins >> update NumberOfCoins
            NumberOfCoins += 10;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double DoubleRound(double d) {
        return DoubleRounder.round(d, 2);
    }
}
