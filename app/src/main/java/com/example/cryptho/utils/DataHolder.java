package com.example.cryptho.utils;

import java.util.ArrayList;

public class DataHolder {
    public ArrayList<CoinData> CoinsData = new ArrayList();

    private static final DataHolder dataHolder = new DataHolder();
    public static synchronized DataHolder getInstance() {
        return dataHolder;
    }

    public int CoinsDataSize(){
        synchronized (this.CoinsData){
            return this.CoinsData.size();
        }
    }

    public void addOrUpdateCoinData(String name, String symbol,
                                    double price,
                                    double percent_change_1h,
                                    double percent_change_24h,
                                    double percent_change_7d) {
        synchronized (this){
            for (CoinData cd : CoinsData){
                if (cd.getName().equals(name)){
                    cd.setPrice(price);
                    cd.setPercent_change_1h(percent_change_1h);
                    cd.setPercent_change_24h(percent_change_24h);
                    return;
                }
            }
            CoinsData.add(new CoinData(name, symbol, price,
                    percent_change_1h,
                    percent_change_24h,
                    percent_change_7d));
        }
    }
}
