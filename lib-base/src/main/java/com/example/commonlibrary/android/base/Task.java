package com.example.commonlibrary.android.base;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2019/2/22 10:16
 */
public abstract class Task {
    private final LifeCycle[] lifeCycles;
    private final String id;

    Task(String id, LifeCycle[] lifeCycles) {
        this.id = id;
        this.lifeCycles = lifeCycles;
    }

    LifeCycle[] getLifeCycles() {
        return lifeCycles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Task task = (Task) o;

        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    abstract void run();
}
