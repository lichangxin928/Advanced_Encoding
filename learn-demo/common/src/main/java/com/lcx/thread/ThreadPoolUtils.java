package com.lcx.thread;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : lichangxin
 * @create : 2024/5/6 13:20
 * @description 线程池工具类
 */
public class ThreadPoolUtils {

    private static final int corePoolSize = 10;
    private static final int maximumPoolSize = 20;
    private static final long keepAliveTime = 60;

    private static final HashMap<String,ThreadPoolExecutor> threadPoolMap = new HashMap<>();

    public static ThreadPoolExecutor getThreadPoolInstance(String threadPoolName) {

        if(threadPoolMap.containsKey(threadPoolName)) {
            return threadPoolMap.get(threadPoolName);
        }
        threadPoolMap.put(threadPoolName,new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, java.util.concurrent.TimeUnit.SECONDS, new java.util.concurrent.LinkedBlockingQueue<Runnable>()));

        return threadPoolMap.get(threadPoolName);

    }

    private ThreadPoolUtils(){}


    /**
     *     public ThreadPoolExecutor(int corePoolSize,
     *                               int maximumPoolSize,
     *                               long keepAliveTime,
     *                               TimeUnit unit,
     *                               BlockingQueue<Runnable> workQueue,
     *                               ThreadFactory threadFactory,
     *                               RejectedExecutionHandler handler);
     * @return
     */
    public static ExecutorService getFixedThreadPool(int poolSize) {
        return Executors.newFixedThreadPool(poolSize);
    }

    /**
     * 优雅地关闭线程池
     *
     * @param executorService 线程池
     */
    public static void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


}
