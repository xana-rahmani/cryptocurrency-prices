package com.example.cryptho.data;

public class CandleStick {
    private double open;
    private double high;
    private double close;
    private double low;

    public CandleStick(double open, double high , double low, double close) {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }
}
