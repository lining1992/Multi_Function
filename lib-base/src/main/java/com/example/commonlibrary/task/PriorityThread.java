package com.example.commonlibrary.task;

import static android.os.Process.setThreadPriority;

public final class PriorityThread extends Thread {
    private final int priority;

    public PriorityThread(int priority, String name, Runnable runnable) {
        super(runnable, name);
        this.priority = priority;
    }

    @Override
    public void run() {
        if (priority != NORM_PRIORITY) setThreadPriority(priority);
        super.run();
    }
}
