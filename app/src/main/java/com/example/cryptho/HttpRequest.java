package com.example.cryptho;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
}
