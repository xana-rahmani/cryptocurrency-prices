package com.example.cryptho.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/*
This class is designed for the useful static functions which might come to hand
 */
public class Functions {
    /*
    a function for getting the current date. for instance used when getting the candlesticks
    in the http request.
     */
    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        // Conversion
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String text = sdf.format(date);
        return text;
    }
    /*
    this function returns a json array from a string which depicts an array of jsons.
     */
    public static JSONArray string_To_JsonArray(String json) throws JSONException {

        return new JSONArray(json);
    }
    /*
    this function rounds the given double to with precison p
     */
    public static double Round(double d, Integer p) {
        if (p == null) return org.decimal4j.util.DoubleRounder.round(d, 2);
        return org.decimal4j.util.DoubleRounder.round(d, p);
    }
}
