package com.example.cryptho;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.cryptho.data.DataHolder;
import com.example.cryptho.utils.DoubleRounder;
import com.example.cryptho.utils.Shared_Objects;
import com.example.cryptho.utils.Utils;

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
    final private DataHolder dataHolder = DataHolder.getInstance();
    final private DoubleRounder DR = new DoubleRounder();
    final private Utils utils = new Utils();

    //Main Handler Messages
    int UPDATE_COINS_DATA_LIST = 1;


    private int NumberOfCoins = 0;
    private String CoinMarketCapUrl = null;




    static public com.example.cryptho.MainModelView getInstance() {
        if (me == null) {
            me = new com.example.cryptho.MainModelView();
        }
        return me;
    }


    public void showMoreCoin(Handler handler) {
        String ApiToken = "7fc06983-3d6c-437a-8bc5-09bd5b4d19bc";
        String ApiHeaderFormat = "X-CMC_PRO_API_KEY";

        if (CoinMarketCapUrl == null) CoinMarketCapUrl = utils.CreateCoinMarketCapUrl();
        int start = NumberOfCoins + 1;  // start is offset (1-based index) of the paginated list.
        String url = CoinMarketCapUrl + "&start=" + start;

        Shared_Objects.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // step1.   Get 10 Coins Data.
                    HttpRequest httpRequest = new HttpRequest();
                    Response response = httpRequest.call(url, ApiToken, ApiHeaderFormat);
                    if (response == null) return; // TODO: Notification

                    // step2.   Convert String response to Json object.
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());

                    // step3.   Save in dataHolder ArrayList.
                    SaveNewCoinsData(jsonObject);

                    //step4.    Get New Coin icons
                    String[] symbols = getCoinsSymbols(jsonObject);
                    String coinsInfoUrl = utils.getCoinsInfoUrl(symbols);
                    response = httpRequest.call(coinsInfoUrl, ApiToken, ApiHeaderFormat);
                    jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    getCoinsIcon(jsonObject, symbols);

                    // step5.   Send Message to handler for update view.
                    Message msg = Message.obtain();
                    msg.what = UPDATE_COINS_DATA_LIST;
                    handler.sendMessage(msg);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                int change_1h = (int) coinUsdValue.getDouble("percent_change_1h");
                int change_24h = (int) coinUsdValue.getDouble("percent_change_24h");
                int change_7d = (int) coinUsdValue.getDouble("percent_change_7d");

                dataHolder.addOrUpdateCoinData(coin_name, coin_symbol, coin_price,
                        change_1h, change_24h, change_7d);
            }

            //After save new coins >> update NumberOfCoins
            NumberOfCoins = dataHolder.CoinsDataSize();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String[] getCoinsSymbols(JSONObject jsonCoinsData){
        String[] symbols = new String[0];
        try {
            JSONArray dataArray = jsonCoinsData.getJSONArray("data");
            symbols = new String[dataArray.length()];
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject coinData = dataArray.getJSONObject(i);
                symbols[i] = coinData.getString("symbol");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return symbols;
    }

    private void getCoinsIcon(JSONObject coinsInfo, String[] symbols) {
        ArrayList<String> url_coinsIcon = new ArrayList<>();

        try {
            JSONObject data = coinsInfo.getJSONObject("data");
            for (int i = 0; i < symbols.length; i++) {
                JSONObject coin_data = data.getJSONObject(symbols[i]);
                url_coinsIcon.add(coin_data.getString("logo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // send request to get image file
        HttpRequest httpRequest = new HttpRequest();
        Log.v("icon urls: ", "");
        for(String url : url_coinsIcon)
            Log.v("\t", url);
//        response = httpRequest.call(coinsInfoUrl, ApiToken, ApiHeaderFormat);

    }
}
