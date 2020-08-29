package com.example.commonlibrary.android.base;

import java.util.List;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2019/2/21 20:42
 */
public enum TaskAddStrategy {
    /**
     * 覆盖
     */
    OVERRIDE {
        @Override
        void addTask(List<Task> taskList, Task task) {
            taskList.remove(task);
            taskList.add(task);
        }
    },

    /**
     * 不存在才添加
     */
    ADD_IF_NOT_EXIST {
        @Override
        void addTask(List<Task> taskList, Task task) {
            if (taskList.contains(task)) {
                return;
            }
            taskList.add(task);
        }
    },

    /**
     * 尾部插入
     */
    INSERT_TAIL {
        @Override
        void addTask(List<Task> taskList, Task task) {
            taskList.add(task);
        }
    },

    /**
     * 头部插入
     */
    INSERT_HEAD {
        @Override
        void addTask(List<Task> taskList, Task task) {
            taskList.add(0, task);
        }
    };

    /**
     * 添加任务到任务列表
     *
     * @param taskList 任务列表
     * @param task     待添加的任务
     */
    abstract void addTask(List<Task> taskList, Task task);
}
