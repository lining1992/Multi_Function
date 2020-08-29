package com.example.commonlibrary.task;

import android.os.Looper;

public final class Threads {
    public static boolean inMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
