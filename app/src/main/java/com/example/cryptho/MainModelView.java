package com.example.cryptho;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.cryptho.data.DataHolder;
import com.example.cryptho.data.MyMessage;
import com.example.cryptho.utils.DoubleRounder;
import com.example.cryptho.utils.Shared_Objects;
import com.example.cryptho.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;

public class MainModelView {
    static private MainModelView me;
    final private DataHolder dataHolder = DataHolder.getInstance();
    final private DoubleRounder DR = new DoubleRounder();
    final private Utils utils = new Utils();
    final private MyMessage myMessage = new MyMessage();


    private int NumberOfCoins = 0;
    private String CoinMarketCapUrl = null;

    // Coin Market Cap Api Info
    private final String CMC_ApiToken = "7fc06983-3d6c-437a-8bc5-09bd5b4d19bc";
    private final String CMC_ApiHeaderFormat = "X-CMC_PRO_API_KEY";


    static public com.example.cryptho.MainModelView getInstance() {
        if (me == null) me = new com.example.cryptho.MainModelView();
        return me;
    }

    public void reloadCoinList(Handler handler, Context ctx) {
        if (CoinMarketCapUrl == null) CoinMarketCapUrl = utils.CreateCoinMarketCapUrl();
        int start = 1;  // start is offset (1-based index) of the paginated list.
        String url = CoinMarketCapUrl + "&start=" + start;

        Shared_Objects.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // step1.   Get 10 First Coins Data.
                    HttpRequest httpRequest = new HttpRequest();
                    Response response = httpRequest.call(url, CMC_ApiToken, CMC_ApiHeaderFormat);
                    if (response == null) {
                        Message msg = Message.obtain();
                        msg.what = myMessage.SHOW_NOTIFICATION;
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                        return;
                    }

                    // step2.   Convert String response to Json object.
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());

                    // step3.   Save in dataHolder ArrayList and Cache
                    SaveNewCoinsData(jsonObject, true, true, ctx);

                    //step4.    Get New Coin icons
                    String[] symbols = getCoinsSymbols(jsonObject);
                    String coinsInfoUrl = utils.getCoinsInfoUrl(symbols);
                    response = httpRequest.call(coinsInfoUrl, CMC_ApiToken, CMC_ApiHeaderFormat);
                    jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    ArrayList<String> icons = getCoinsIcon(jsonObject, symbols);
                    Shared_Objects.executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            cacheCoinLogo(icons, ctx);
                        }
                    });

                    // step5.   Send Message to handler for update view.
                    Message msg = Message.obtain();
                    msg.what = myMessage.UPDATE_COINS_DATA_LIST;
                    handler.sendMessage(msg);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showMoreCoin(Handler handler, Context ctx) {
        if (CoinMarketCapUrl == null) CoinMarketCapUrl = utils.CreateCoinMarketCapUrl();
        int start = NumberOfCoins + 1;  // start is offset (1-based index) of the paginated list.
        String url = CoinMarketCapUrl + "&start=" + start;

        Shared_Objects.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // step1.   Get 10 Coins Data.
                    HttpRequest httpRequest = new HttpRequest();
                    Response response = httpRequest.call(url, CMC_ApiToken, CMC_ApiHeaderFormat);
                    if (response == null) {
                        Message msg = Message.obtain();
                        msg.what = myMessage.SHOW_NOTIFICATION;
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                        return;
                    }

                    // step2.   Convert String response to Json object.
                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());

                    // step3.   Save in dataHolder ArrayList.
                    SaveNewCoinsData(jsonObject, false, start == 1, ctx);

                    //step4.    Get New Coin icons
                    String[] symbols = getCoinsSymbols(jsonObject);
                    String coinsInfoUrl = utils.getCoinsInfoUrl(symbols);
                    response = httpRequest.call(coinsInfoUrl, CMC_ApiToken, CMC_ApiHeaderFormat);
                    jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                    ArrayList<String> icons = getCoinsIcon(jsonObject, symbols);
                    Shared_Objects.executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            cacheCoinLogo(icons, ctx);
                        }
                    });

                    // step5.   Send Message to handler for update view.
                    Message msg = Message.obtain();
                    msg.what = myMessage.UPDATE_COINS_DATA_LIST;
                    handler.sendMessage(msg);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Parse coins data json and Save in dataHolder ArrayList.
    private void SaveNewCoinsData(JSONObject jsonCoinsData, Boolean clearCoinsData, Boolean cache, Context ctx) {
        if (clearCoinsData) dataHolder.clearCoinsData();
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
            if (cache) {
                Shared_Objects.executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        cacheCoinInfo(jsonCoinsData, ctx);
                    }
                });
            }
            //After save new coins >> update NumberOfCoins
            NumberOfCoins = dataHolder.CoinsDataSize();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String[] getCoinsSymbols(JSONObject jsonCoinsData) {
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

    private ArrayList<String> getCoinsIcon(JSONObject coinsInfo, String[] symbols) {
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
        int i = 0;
        for (String url : url_coinsIcon) // TODO
        {
            dataHolder.updateCoinLogo(symbols[i], url);
            i++;
        }
        return url_coinsIcon;
//        response = httpRequest.call(coinsInfoUrl, ApiToken, ApiHeaderFormat);

    }

    public void cacheCoinInfo(JSONObject data, Context ctx) {
        try {
            File file = new File(ctx.getFilesDir(), "cached_coins_Info.json");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cacheCoinLogo(ArrayList<String> data, Context ctx) {
        try {
            File file = new File(ctx.getFilesDir(), "cached_coins_Logo.json");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String d = "";
            int i = 0;
            for (String s : data) {
                d += s;
                if (i < data.size() - 1)
                    d += ",";
                i++;
            }

            bufferedWriter.write(d);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readCachedCoinInfo_andSave(Handler handler, Context ctx) {
        try {
            File file = new File(ctx.getFilesDir(), "cached_coins_Info.json");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String responce = stringBuilder.toString();

            JSONObject jsonObject = new JSONObject(responce);
            SaveNewCoinsData(jsonObject, false, false, ctx);


            File fileLogo = new File(ctx.getFilesDir(), "cached_coins_Logo.json");
            FileReader logoReader = new FileReader(fileLogo);
            BufferedReader logoBuffer = new BufferedReader(logoReader);
            StringBuilder sbLogo = new StringBuilder();
            String l = logoBuffer.readLine();
            while (l != null) {
                sbLogo.append(l).append("\n");
                l = logoBuffer.readLine();
            }
            logoBuffer.close();
            String resp = sbLogo.toString();
            List<String> logosList = Arrays.asList(resp.split(","));
            ArrayList<String> logos = new ArrayList<String>();
            logos.addAll(logosList);


            int i = 0;
            for (String url : logos) // TODO
            {
                dataHolder.updateCoinLogo(dataHolder.getCoinsData().get(i).getSymbol(), url);
                i++;
            }


            // Send Message to handler for update view.
            Message msg = Message.obtain();
            msg.what = myMessage.UPDATE_COINS_DATA_LIST;
            handler.sendMessage(msg);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
