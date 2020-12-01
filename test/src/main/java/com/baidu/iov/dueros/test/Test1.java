package com.baidu.iov.dueros.test;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author v_lining05
 * @date 2020/9/5
 */
class Test1 {

    volatile boolean b;

    @SuppressLint("NewApi")
    LongAdder longAdder = new LongAdder();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    Phaser phaser = new Phaser(5) {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("phase==" + phase + "==onAdvance==" + registeredParties);
                    return true;
                case 1:
                    System.out.println("phase==" + phase + "onAdvance==" + registeredParties);
                    return true;
            }
            return true;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void t(int i) {
        System.out.println("t==" + i);
        if (i > 3) {
            System.out.println("arriveAndDeregister==" + i);
            phaser.arriveAndDeregister();
        } else {
            System.out.println("arriveAndAwaitAdvance==" + i);
            phaser.arriveAndAwaitAdvance();
        }

    }


    public void n() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("我是方法n===");
    }

    public void m() {
        b = true;
        System.out.println("start===");
        while (b) {

        }
        System.out.println("end===");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void main(String[] args) {
        final Test1 test1 = new Test1();
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.n();
            }
        });
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test1.m();
            }
        });
        t1.start();
        t.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test1.b = false;

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            new Thread(() -> test1.t(finalI)).start();
        }
    }
}
