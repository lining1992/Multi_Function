package com.example.commonlibrary.task;

/**
 * 用于线程间通信，可以表示处理成功或失败
 */
public final class ResultSignal<T> {
    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;
    private volatile int retCode = 0;
    private T result;

    public synchronized boolean trySuccess() {
        while (retCode == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        return retCode == SUCCESS;
    }

    public synchronized T tryResult() {
        while (retCode == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        return retCode == SUCCESS ? result : null;
    }

    public synchronized void signal(int retCode, T result) {
        this.retCode = retCode;
        this.result = result;
        this.notify();
    }

    public synchronized void signalSuccess() {
        this.retCode = SUCCESS;
        this.notify();
    }

    public synchronized void signalFailed() {
        this.retCode = FAILURE;
        this.notify();
    }

    public synchronized T getResult() {
        return result;
    }
}
