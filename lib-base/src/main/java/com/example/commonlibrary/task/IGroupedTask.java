package com.example.commonlibrary.task;


public interface IGroupedTask extends ITask {

    String DEFAULT_TASK_NAME = "at";//anonymous-task
    String DEFAULT_GROUP_NAME = "dg";//default-group

    /**
     * 丢弃新任务
     */
    int DISCARD_NEW = 0;

    /**
     * 取消老任务
     */
    int CANCEL_OLD = 1;

    /**
     * 强制提交，任务可重复
     */
    int FORCE_SUBMIT = 2;

    String taskName();

    String groupName();

    boolean serialExecute();

    /**
     * 任务去重策略
     *
     * @return
     */
    int dualPolicy();
}
