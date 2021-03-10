package com.example.cryptho;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.cryptho.adaptor.ListOfCoinsAdapter;
import com.example.cryptho.utils.DataHolder;
import com.example.cryptho.utils.DoubleRounder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    final private DataHolder dataHolder = DataHolder.getInstance();
    final private DoubleRounder DR = new DoubleRounder();

    //Main Handler Messages
    int UPDATE_COINS_DATA_LIST = 1;


    private int NumberOfCoins = 0;
    private String CoinMarketCapUrl = null;

    // Thread Pool
    int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    int KEEP_ALIVE_TIME = 2;
    TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    ExecutorService executorService = new ThreadPoolExecutor(NUMBER_OF_CORES,
            NUMBER_OF_CORES*2,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            taskQueue);


    static public MainModelView getInstance() {
        if (me == null) {
            me = new MainModelView();
        }
        return me;
    }

    public void showMoreCoin(Handler handler) {
        String ApiToken = "7fc06983-3d6c-437a-8bc5-09bd5b4d19bc";
        String ApiHeaderFormat = "X-CMC_PRO_API_KEY";

        if (CoinMarketCapUrl == null) CreateCoinMarketCapUrl();
        int start = NumberOfCoins + 1;  // start is offset (1-based index) of the paginated list.
        String url = CoinMarketCapUrl + "&start=" + start;

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // step1.   Get 10 Coins Data.
                    HttpRequest httpRequest = new HttpRequest();
                    Response response = httpRequest.call(url, ApiToken, ApiHeaderFormat);
                    if (response == null) return;

                    // step2.   Convert String response to Json object.
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());

                    // step3.   Parse json data and Save in dataHolder ArrayList.
                    SaveNewCoinsData(jsonObject);

                    // step4.   Send Message to handler for update view.
                    Message msg = Message.obtain();
                    msg.what = UPDATE_COINS_DATA_LIST;
                    handler.dispatchMessage(msg);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void CreateCoinMarketCapUrl(){
        String UrlFormat = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(UrlFormat)).newBuilder();
        urlBuilder.addQueryParameter("limit", "10");
        urlBuilder.addQueryParameter("convert", "USD");
        CoinMarketCapUrl = urlBuilder.build().toString();
    }

    // Parse coins data json and Save in dataHolder ArrayList.
    private void SaveNewCoinsData(JSONObject jsonCoinsData){
        try {
            JSONArray dataArray = jsonCoinsData.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject coinData = dataArray.getJSONObject(i);
                String coin_name = coinData.getString("name");
                String coin_symbol = coinData.getString("symbol");

                JSONObject coinUsdValue = coinData.getJSONObject("quote").getJSONObject("USD");
                double coin_price = DR.Round(coinUsdValue.getDouble("price"), 3);
                double change_1h = DR.Round(coinUsdValue.getDouble("percent_change_1h"), null);
                double change_24h = DR.Round(coinUsdValue.getDouble("percent_change_24h"), null);
                double change_7d = DR.Round(coinUsdValue.getDouble("percent_change_7d"), null);

                Log.d("COIN",coin_name + ": " + coin_price );
                dataHolder.addOrUpdateCoinData(coin_name, coin_symbol, coin_price,
                        change_1h, change_24h, change_7d);
            }

            //After save new coins >> update NumberOfCoins
            NumberOfCoins = dataHolder.CoinsDataSize();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
