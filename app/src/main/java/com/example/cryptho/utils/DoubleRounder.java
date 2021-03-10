package com.example.cryptho.utils;

public class DoubleRounder {
    public double Round(double d, Integer p) {
        if (p == null) return org.decimal4j.util.DoubleRounder.round(d, 2);
        return org.decimal4j.util.DoubleRounder.round(d, p);
    }
}
