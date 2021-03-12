package com.example.cryptho.utils;

import java.util.Objects;

import okhttp3.HttpUrl;

public class Utils {
    public String CreateCoinMarketCapUrl(){
        String urlFormat = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(urlFormat)).newBuilder();
        urlBuilder.addQueryParameter("limit", "10");
        urlBuilder.addQueryParameter("convert", "USD");
        return urlBuilder.build().toString();
    }

    public String getCoinsInfoUrl(String[] symbols){
        StringBuilder str_symbols = new StringBuilder();
        for (int i = 0; i < symbols.length; ++i){
            if (i == 0) str_symbols.append(symbols[0]);
            else str_symbols.append(",").append(symbols[i]);
        }
        String urlFormat = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(urlFormat)).newBuilder();
        urlBuilder.addQueryParameter("aux", "logo");
        urlBuilder.addQueryParameter("symbol", str_symbols.toString());
        return urlBuilder.build().toString();
    }
}
