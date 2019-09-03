package com.sincerity.utilslibrary.http.volley;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sincerity on 2019/4/26.
 * 描述： 线程池管理类
 */
public class ThreadPoolManager {
    //用来存储请求任务
    private LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque();
    //把队列中的任务放到线程池
    private ThreadPoolExecutor executor;
    /*核心线程数 */
    private int COREPOOLSIZE = 4;
    /*线程池的大小*/
    private int MAXIMUMPOOLSIZE = 20;
    /*存活时间*/
    private int KEEPALIVETIME = 15;
    /*工作线程数量*/
    private int WORKQUEUESize = 4;
    private static ThreadPoolManager manager;


    private ThreadPoolManager() {
        executor = new ThreadPoolExecutor(COREPOOLSIZE, MAXIMUMPOOLSIZE, KEEPALIVETIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(WORKQUEUESize), handler);
        executor.execute(runnable);
    }

    public static ThreadPoolManager Instance() {
        if (manager == null) {
            synchronized (ThreadPoolManager.class) {
                if (manager == null) {
                    manager = new ThreadPoolManager();
                }
            }
        }
        return manager;
    }

    //添加请求任务
    public void execute(Runnable runnable) {
        if (runnable != null) {
            try {
                deque.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //当线程数超过maxPoolSize或者keep-alive时间超时时执行拒绝策略
    private RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                deque.put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Runnable runnable = null;
            try {
                runnable = deque.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (runnable != null) {
                executor.execute(runnable);
            }
        }
    };
}
