package com.example.cryptho.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
this class is used for saving public static objects which need to accessible to all classes like threadpool,...
 */
public class Shared_Objects {
    // Thread Pool
    static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    static int KEEP_ALIVE_TIME = 2;
    static TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    static BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    // changed ExecutorService to public static. #khash
    public static ExecutorService executorService = new ThreadPoolExecutor(NUMBER_OF_CORES,
            NUMBER_OF_CORES*2,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            taskQueue);
}
