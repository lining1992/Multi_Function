package com.example.commonlibrary.task;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


final class TaskPriorityManager {
   final Map<String, List<AbstractTask<?>>> taskGroups;
   final BlockingQueue<Runnable> poolWorkQueue;

   TaskPriorityManager(BlockingQueue<Runnable> poolWorkQueue) {
       this.taskGroups = new ConcurrentHashMap<String, List<AbstractTask<?>>>();
       this.poolWorkQueue = poolWorkQueue;
   }

   void addTask(AbstractTask<?> task) {
       getByGroup(task.groupName).add(task);
   }

   List<AbstractTask<?>> getByGroup(String groupName) {
       List<AbstractTask<?>> group = taskGroups.get(groupName);
       if (group == null) {
           group = new CopyOnWriteArrayList<AbstractTask<?>>();
           taskGroups.put(groupName, group);
       }
       return group;
   }

   AbstractTask getOldTask(AbstractTask<?> task) {
       List<AbstractTask<?>> tasks = taskGroups.get(task.groupName);
       if (tasks == null || tasks.isEmpty()) return null;
       for (AbstractTask<?> t : tasks) {
           if (t.equals(task)) return t;
       }
       return null;
   }

   void removeTask(AbstractTask<?> task) {
       List<AbstractTask<?>> group = taskGroups.get(task.groupName);
       if (group == null || group.isEmpty()) return;
       group.remove(task);
       if (group.isEmpty()) {
           taskGroups.remove(task.groupName);
       }
   }

   /**
    * 选取策略：首先从所有分组中随机选取一个分组
    * 如果该组中的任务为空，那么找出第一个不为空的组返回
    *
    * @return 下一个可用的任务分组
    */
   List<AbstractTask<?>> getNextTaskGroup() {
       Collection<List<AbstractTask<?>>> allGroups = taskGroups.values();//所有的任务分组
       if (allGroups.isEmpty()) return null;
       List<AbstractTask<?>> next = null, firsNotEmptyList = null;
       int randomIndex = (int) (Math.random() * allGroups.size()), i = 0;
       for (List<AbstractTask<?>> group : allGroups) {
           if (firsNotEmptyList == null && group.size() > 0) {
               firsNotEmptyList = group;
               //第一个不为空的组和next都被找到则退出循环
               if (next != null) break;
           }
           //循环到对应的位置，设置当前组为next
           if (randomIndex == i++) {
               next = group;
               //第一个不为空的组和next都被找到则退出循环
               if (firsNotEmptyList != null) break;
           }
       }
       //如果next为空，则设置firsNotEmptyList为next
       if (next == null || next.isEmpty()) {
           next = firsNotEmptyList;
       }
       return next;
   }

   void changePriority(AbstractTask<?> task, int priority) {
       if (poolWorkQueue.isEmpty()) return;
       if (task.status != ITask.STATUS_READY) return;
       if (poolWorkQueue.remove(task)) {
           task.priority = priority;
           TaskScheduler.INSTANCE.submitReadyTask(task);
       }
   }


   void changeTaskPriority(int status, String groupName) {
       List<AbstractTask<?>> tasks = taskGroups.get(groupName);
       if (tasks == null || tasks.isEmpty()) return;
       switch (status) {
           case UIChangeEvent.STATUS_START://UI启动恢复被降级的任务
               for (AbstractTask<?> task : tasks) {
                   if (task.priority != IPriorityTask.PRIOR_NORMAL) {//只恢复被onStop事件降级的任务
                       changePriority(task, task.priority + 1);
                   }
               }
               break;
           case UIChangeEvent.STATUS_STOP://UI停止时降级其下的任务
               for (AbstractTask<?> task : tasks) {
                   changePriority(task, task.priority - 1);
               }
               break;
           case UIChangeEvent.STATUS_DESTROY://UI销毁时取消由其发起的所有任务
               for (AbstractTask<?> task : tasks) {
                   task.stop();
               }
               break;
       }
   }
}
