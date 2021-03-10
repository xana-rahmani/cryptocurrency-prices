package com.example.cryptho.utils;

public class CoinData {
    private final String name;
    private final String symbol;
    private double price;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;

    public CoinData(String name, String symbol, double price,
                    double percent_change_1h,
                    double percent_change_24h,
                    double percent_change_7d) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.percent_change_1h = percent_change_1h;
        this.percent_change_24h = percent_change_24h;
        this.percent_change_7d = percent_change_7d;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public void setPercent_change_24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
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

    public double getPercent_change_1h() {
        return percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }
}