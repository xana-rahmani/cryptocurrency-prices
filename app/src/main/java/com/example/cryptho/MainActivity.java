package com.example.cryptho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private MainModelView mmv;

    private HandlerThread handlerThread = new HandlerThread("HandlerThread");
    private MainHandler mainHandler;

    public TextView tv;


    // Api Info
    private String coinMarketCapUrlFormat = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=%d&limit=10&convert=USD";
    final private String coinMarketCapApiToken = "7fc06983-3d6c-437a-8bc5-09bd5b4d19bc";
    final private String coinMarketCapHeaderFormat = "X-CMC_PRO_API_KEY";
    // Thread Pool
    int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    int KEEP_ALIVE_TIME = 2;
    TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    ExecutorService networkExecutorService = new ThreadPoolExecutor(NUMBER_OF_CORES,
            NUMBER_OF_CORES*2,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            taskQueue);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tv = findViewById(R.id.main_text);

        handlerThread.start();
        mainHandler = new MainHandler(this, handlerThread.getLooper());

        this.mmv = MainModelView.get();

        // Find View Element
        Button getCoinBtn = findViewById(R.id.showMoreCoin);
        getCoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        mmv.showMoreCoin(mainHandler, 1);
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();

    }

    public void showMoreCoin(View view) {
        Log.v("Debug", "activity-showMoreCoin");
        mmv.showMoreCoin(mainHandler, 1);  // TODO: manage start
    }

}