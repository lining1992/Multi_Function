package com.example.commonlibrary.task;

public final class PriorityThreadFactory extends NamedThreadFactory {
    private final int priority;

    public PriorityThreadFactory(String namePrefix, int priority) {
        super(namePrefix);
        this.priority = priority;
    }

    @Override
    public Thread newThread(String name, Runnable r) {
        return new PriorityThread(priority, name, r);
    }
}
