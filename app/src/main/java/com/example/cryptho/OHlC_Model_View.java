package com.example.cryptho;

import android.os.Message;
import android.util.Log;

import com.example.cryptho.data.CandleStick;
import com.example.cryptho.data.DataHolder;
import com.example.cryptho.data.MyMessage;
import com.example.cryptho.utils.Functions;
import com.example.cryptho.utils.Shared_Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Response;

public class OHlC_Model_View {
    static private com.example.cryptho.OHlC_Model_View me;
    final private DataHolder dataHolder = DataHolder.getInstance();
    final private MyMessage myMessage = new MyMessage();

    static public com.example.cryptho.OHlC_Model_View getInstance() {
        if (me == null) me = new com.example.cryptho.OHlC_Model_View();
        return me;
    }

    public void get_New_OHLC(OHLC_Handler handler, String symbol, HttpRequest.Range range) {
        Shared_Objects.executorService.execute(new Runnable() {
            @Override
            public void run() {
                String X_CoinAPI_Key = "45F7D1DF-B1F0-4C75-A7FC-00F8DD635DF1";
                String X_Coin_Header_Format = "X-CoinAPI-Key";
                HttpRequest httpRequest = new HttpRequest();
                Response response = httpRequest.call( X_CoinAPI_Key, X_Coin_Header_Format,symbol,range);
                try {
                    if (response == null){
                        Message msg = Message.obtain();
                        msg.what = myMessage.SHOW_NOTIFICATION;
                        msg.arg1 = 2;
                        return;
                    }
                    JSONArray json_of_candles = Functions.string_To_JsonArray(response.body().string());
                    ArrayList<CandleStick> OHLC = new ArrayList<>();
                    for (int i=0; i<json_of_candles.length(); i++){
                        JSONObject json_of_candle = (JSONObject) json_of_candles.get(i);
                        double open = Functions.Round(json_of_candle.getDouble("price_open"),null);
                        double high = Functions.Round(json_of_candle.getDouble("price_high"),null);
                        double low = Functions.Round(json_of_candle.getDouble("price_low"),null);
                        double close = Functions.Round(json_of_candle.getDouble("price_close"),null);
                        CandleStick received_candle = new CandleStick(open,high,low,close);
                        OHLC.add(received_candle);
                    }
                    dataHolder.update_OHLC(OHLC);
                    Message msg = Message.obtain();
                    msg.what = myMessage.UPDATE_OHLC;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    Log.d("JsonException", Objects.requireNonNull(response.body()).toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
