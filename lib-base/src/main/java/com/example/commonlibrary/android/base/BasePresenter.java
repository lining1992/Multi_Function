package com.example.commonlibrary.android.base;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : leixing
 * @date : 2017-04-14
 * Email       : leixing@baidu.com
 * Version     : 0.0.1
 * <p>
 * Description : MVP架构的Presenter的基类，使用JDK的动态代理，代理视图对象的方法调用
 */

@SuppressWarnings("unused")
public abstract class BasePresenter<VIEW> {

    private VIEW mViewProxy;
    private boolean destroyed = false;
    private boolean isViewSet = false;
    private ProxyViewHandler mProxyViewHandler;
    private Activity activity;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    private Map<LifeCycle, Map<TaskAddStrategy, VIEW>> mProxies;
    private Class<?>[] mTargetViewInterfaces;
    private WeakReference<VIEW> mTargetView;
    private LifeCycle mLifeCycle = LifeCycle.INIT;
    private List<Task> mTasks;
    private VIEW mQueuedViewProxy;

    /**
     * 供子类继承是否在页面关闭时清除VIEW引用，
     * 如果这种情况下,getActivity()在页面关闭后会返回null
     * 如果页面关闭后，presenter继续执行长任务，会造成页面内存泄露
     *
     * @return 是否要一直持有activity的引用
     */
    @SuppressWarnings("WeakerAccess")
    protected boolean keepActivityAlways() {
        return false;
    }

    private Activity checkActivity(VIEW target) {
        Activity activity = null;
        if (target != null) {
            if (target instanceof Activity) {
                activity = (Activity) target;
            } else if (target instanceof Fragment) {
                activity = ((Fragment) target).getActivity();
            } else if (target instanceof android.app.Fragment) {
                activity = ((android.app.Fragment) target).getActivity();
            }
        }
        return activity;
    }

    final protected Activity getActivity() {
        if (activity == null && isViewSet) {
            VIEW target = mProxyViewHandler.getView();
            return checkActivity(target);
        }
        return activity;
    }

    /**
     * 仅当VIEW有效时在主线程运行命令
     *
     * @param command 命令
     */
    final protected void runOnUiThread(Runnable command) {
        runOnUiThread(command, true);
    }

    /**
     * 在主线程运行命令
     *
     * @param command              在主线程执行的命令
     * @param justRunWhenViewValid 是否仅在视图有效时运行
     *                             为{@code true}时，当视图销毁后（activity、fragment调用onDestroy），命令将被丢弃
     *                             为{@code false}时，不论视图是否销毁，都将在UI线程运行命令
     */
    @SuppressWarnings("WeakerAccess")
    final protected void runOnUiThread(Runnable command, @SuppressWarnings("SameParameterValue") boolean justRunWhenViewValid) {
        if (justRunWhenViewValid && !isViewValid()) {
            return;
        }
        mUIHandler.post(command);
    }

    /**
     * 判断VIEW是否有效
     *
     * @return 视图是否有效
     */
    private boolean isViewValid() {
        return isViewSet && !destroyed && (mProxyViewHandler != null) && mProxyViewHandler.isViewExists();
    }

    /**
     * 获取视图对象
     *
     * @return 代理的视图对象
     */
    protected final VIEW getView() {
        return mViewProxy;
    }

    protected final VIEW getQueuedViewRunLast() {
        return getQueuedView(TaskAddStrategy.OVERRIDE, LifeCycle.RESUME);
    }

    protected final VIEW getQueuedViewRunOnce() {
        return getQueuedView(TaskAddStrategy.ADD_IF_NOT_EXIST, LifeCycle.RESUME);
    }

    protected final VIEW getQueuedView() {
        return getQueuedView(TaskAddStrategy.INSERT_TAIL, LifeCycle.RESUME);
    }

    protected final VIEW getQueuedView(TaskAddStrategy strategy, LifeCycle... lifeCycles) {
        if (mQueuedViewProxy == null) {
            mQueuedViewProxy = createViewProxy(lifeCycles, strategy);
        }
        return mQueuedViewProxy;
    }

    private VIEW createViewProxy(final LifeCycle[] lifeCycles, final TaskAddStrategy strategy) {
        // noinspection unchecked
        return (VIEW) Proxy.newProxyInstance(this.getClass().getClassLoader(), mTargetViewInterfaces,
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
                        final VIEW targetView = mTargetView.get();
                        if (targetView == null) {
                            return null;
                        }
                        if (Util.contains(lifeCycles, mLifeCycle)) {
                            method.invoke(targetView, args);
                            return null;
                        }
                        String id = method.getDeclaringClass().getCanonicalName() + "#" + method.getName();
                        Task task = new Task(id, lifeCycles) {
                            @Override
                            public void run() {
                                final VIEW targetView = mTargetView.get();
                                if (targetView == null) {
                                    return;
                                }
                                try {
                                    method.invoke(targetView, args);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        if (mTasks == null) {
                            mTasks = new ArrayList<>();
                        }
                        strategy.addTask(mTasks, task);

                        return null;
                    }
                });
    }

    /**
     * 设置被代理的视图对象
     * 注意：当页面关闭时会自动清除该VIEW的引用,
     * 如果没有设置  {@link #keepActivityAlways()} 为true, 会造成getActivity()==null
     *
     * @param view 被代理的视图对象
     */
    @SuppressWarnings("unchecked")
    public void setView(@NonNull VIEW view) {
        isViewSet = true;
        mTargetViewInterfaces = view.getClass().getInterfaces();
        if (keepActivityAlways()) {
            activity = checkActivity(view);
        }
        if (view instanceof LifeCycleProvider) {
            addLifeCycleObserver((LifeCycleProvider) view);
        }
        mProxyViewHandler = new ProxyViewHandler(view);
        mTargetView = new WeakReference<>(view);
        mViewProxy = (VIEW) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                view.getClass().getInterfaces(), mProxyViewHandler);
    }

    private void addLifeCycleObserver(final LifeCycleProvider lifeCycleProvider) {
        lifeCycleProvider.addLifeCycleObserver(new LifeCycleObserver() {
            @Override
            public void onCreate() {
                mLifeCycle = LifeCycle.CREATE;
                executeTasks();
            }

            @Override
            public void onStart() {
                mLifeCycle = LifeCycle.START;
                executeTasks();
            }

            @Override
            public void onResume() {
                mLifeCycle = LifeCycle.RESUME;
                executeTasks();
            }

            @Override
            public void onPause() {
                mLifeCycle = LifeCycle.PAUSE;
                executeTasks();
            }

            @Override
            public void onStop() {
                mLifeCycle = LifeCycle.STOP;
                executeTasks();
            }

            @Override
            public void onDestroy() {
                mLifeCycle = LifeCycle.DESTROY;
                executeTasks();
                destroyed = true;
                if (!keepActivityAlways()) {
                    activity = null;
                }
            }
        });
    }

    private void executeTasks() {
        if (mTasks == null || mTasks.isEmpty()) {
            return;
        }
        Iterator<Task> iterator = mTasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (Util.contains(task.getLifeCycles(), mLifeCycle)) {
                task.run();
                iterator.remove();
            }
        }
    }

    /**
     * 代理视图对象的方法调用处理器
     */
    private class ProxyViewHandler implements InvocationHandler {
        final WeakReference<VIEW> target;

        ProxyViewHandler(VIEW target) {
            this.target = new WeakReference<>(target);
        }

        private boolean isViewExists() {
            return target.get() != null;
        }

        private VIEW getView() {
            return target.get();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isViewValid()) {
                VIEW targetView = target.get();
                if (targetView != null) {
                    return method.invoke(targetView, args);
                }
            }
            return null;
        }
    }
}
