package com.example.commonlibrary.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author v_lining05
 * @date 2020-01-04
 */
public class TaskExecutor {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * IO读写线程池,最多二个同时执行
     */
    private static volatile ExecutorService sIOPool;

    /**
     * 全局cachePool,适用于AsyncHttpClient等不
     * 限制任务数的请求
     */
    private static volatile ExecutorService sCachePool;

    /**
     * 串行线程池
     */
    private static volatile ExecutorService sSerialPool;

    /**
     * 繁重 任务线程池，适用于像ImageLoader转换图像这种时间不长但又很占CPU的任务
     * 排队执行的ThreadPool,核心线程为CORE_POOL_SIZE+1个
     */
    private static volatile ExecutorService sCpuPool;

    /**
     * 主线程handler，用于将任务提交至主线程执行
     */
    private static volatile Handler uiHandler;

    private TaskExecutor() {
        throw new UnsupportedOperationException();
    }

    public static void ui(Runnable task) {
        getHandler().post(task);
    }

    public static void ui(Runnable task, long delayMills) {
        getHandler().postDelayed(task, delayMills);
    }

    public static void io(Runnable task) {
        getIOPool().execute(task);
    }

    public static void cpu(Runnable task) {
        getCpuPool().execute(task);
    }

    public static void enqueue(Runnable task) {
        getSerialPool().execute(task);
    }

    public static void infinite(Runnable task) {
        getCachePool().execute(task);
    }

    private static Handler getHandler() {
        if (uiHandler == null) {
            synchronized (TaskExecutor.class) {
                if (uiHandler == null) {
                    uiHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return uiHandler;
    }

    private static ExecutorService getIOPool() {
        if (sIOPool == null) {
            synchronized (TaskExecutor.class) {
                if (sIOPool == null) {
                    sIOPool = new ThreadPoolExecutor(
                            CORE_POOL_SIZE,
                            15,
                            5L, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            new NamedThreadFactory("io-pool"));
                }
            }
        }
        return sIOPool;
    }

    private static ExecutorService getCpuPool() {
        if (sCpuPool == null) {
            synchronized (TaskExecutor.class) {
                if (sCpuPool == null) {
                    sCpuPool = new ThreadPoolExecutor(
                            1,
                            1,
                            5L, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(128),
                            new NamedThreadFactory("cpu-pool"));
                }
            }
        }
        return sCpuPool;
    }

    private static ExecutorService getSerialPool() {
        if (sSerialPool == null) {
            synchronized (TaskExecutor.class) {
                if (sSerialPool == null) {
                    sSerialPool = new ThreadPoolExecutor(
                            1,
                            1,
                            0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            new NamedThreadFactory("serial-pool"));
                }
            }
        }
        return sSerialPool;
    }

    private static ExecutorService getCachePool() {
        if (sCachePool == null) {
            synchronized (TaskExecutor.class) {
                if (sCachePool == null) {
                    sCachePool = new ThreadPoolExecutor(
                            0,
                            MAXIMUM_POOL_SIZE,
                            60L, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>(),
                            new NamedThreadFactory("cache-pool"));
                }
            }
        }
        return sCachePool;
    }

    private static class NamedThreadFactory implements ThreadFactory {

        private final String mThreadName;
        private final AtomicInteger mCount;

        NamedThreadFactory(String threadName) {
            mThreadName = threadName;
            mCount = new AtomicInteger(1);
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, mThreadName + "#" + mCount.getAndIncrement());
        }
    }
}
