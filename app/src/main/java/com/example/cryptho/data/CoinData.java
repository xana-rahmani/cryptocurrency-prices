package com.example.cryptho.data;

public class CoinData {
    private final String name;
    private final String symbol;
    private double price;
    private int percent_change_1h;
    private int percent_change_24h;
    private int percent_change_7D;

    public CoinData(String name, String symbol, double price,
                    int percent_change_1h,
                    int percent_change_24h,
                    int percent_change_7d) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.percent_change_1h = percent_change_1h;
        this.percent_change_24h = percent_change_24h;
        this.percent_change_7D = percent_change_7d;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPercent_change_1h(int percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public void setPercent_change_24h(int percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public void setPercent_change_7D(int percent_change_7D) {
        this.percent_change_7D = percent_change_7D;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getPercent_change_1h() {
        return percent_change_1h;
    }

    public int getPercent_change_24h() {
        return percent_change_24h;
    }

    public int getPercent_change_7D() {
        return percent_change_7D;
    }
}