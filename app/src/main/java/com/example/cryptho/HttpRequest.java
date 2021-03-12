package com.example.cryptho;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.cryptho.utils.Functions;


public class HttpRequest {

    private final OkHttpClient client = new OkHttpClient();

    public Response call(String url, String api_key, String api_key_header_format){

        Response res = null;
        Request request = new Request.Builder().url(url)
                .addHeader(api_key_header_format, api_key)
                .build();
        try {
            res = client.newCall(request).execute();
            if (!res.isSuccessful()) throw new IOException("Unexpected code " + res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
    /*
    an enum for choosing the range of candles. #khash
     */
    public enum Range {
        weekly,
        oneMonth,
    }

    /*
    This is an overloaded version of call to get the candle information. since we needed a symbol and a range,
    I overloaded it. #khash
     */
    public Response call( String api_key, String api_key_header_format,String symbol, Range range){

        Response res = null;
        String miniUrl;
        final String description;
        switch (range) {

            case weekly:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(Functions.getCurrentDate()).concat("&limit=7"));
                description = "Daily candles from now";
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(Functions.getCurrentDate()).concat("&limit=30"));
                description = "Daily candles from now";
                break;

            default:
                miniUrl = "";
                description = "";

        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://rest.coinapi.io/v1/ohlcv/".concat(symbol).concat("/USD/history?".concat(miniUrl))).newBuilder();

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url)

                .addHeader(api_key_header_format, api_key)
                .build();
//        Log.d("mytest6",request.toString());
        try {
            res = client.newCall(request).execute();
            //if (!res.isSuccessful()) throw new IOException("Unexpected code " + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            JSONObject obj = (JSONObject) Functions.string_To_JsonArray(res.body().string()).get(0);
//            Log.d("mytest2",String.valueOf(obj.get("price_high")));
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
        return res;
    }
}
