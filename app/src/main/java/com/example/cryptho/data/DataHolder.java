package com.example.cryptho.data;

import java.util.ArrayList;

public class DataHolder {
    private final ArrayList<CoinData> CoinsData = new ArrayList();

    private static final DataHolder dataHolder = new DataHolder();
    public static synchronized DataHolder getInstance() {
        return dataHolder;
    }

    public synchronized int CoinsDataSize(){
        return this.CoinsData.size();
    }

    public void addOrUpdateCoinData(String name, String symbol,
                                    double price,
                                    int percent_change_1h,
                                    int percent_change_24h,
                                    int percent_change_7d) {
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

    public synchronized ArrayList<CoinData> getCoinsData() {
        return (ArrayList<CoinData>) CoinsData.clone();
    }

    public synchronized void clearCoinsData(){
        synchronized (this.CoinsData){
            CoinsData.clear();
        }
    }

    /*
    the following functions are for working with the OHLC similar to coinsData. #khash
     */
    private ArrayList<CandleStick> OHLC = new ArrayList<>();
    public synchronized int OHLC_Size() {
        return this.OHLC.size();
    }
    public synchronized void update_OHLC(ArrayList<CandleStick> new_OHLC)
    {
        this.OHLC = new_OHLC;
    }
    public synchronized ArrayList<CandleStick> get_OHLC(){
        return (ArrayList<CandleStick>) this.OHLC.clone();
    }
}
