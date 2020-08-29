package com.example.commonlibrary.task;

public interface IPriorityTask extends Comparable<IPriorityTask> {
    int PRIOR_HIGH = 256;
    int PRIOR_UI = 32;
    int PRIOR_NORMAL = 1;

    int getPriority();

    void setPriority(int priority);

}
