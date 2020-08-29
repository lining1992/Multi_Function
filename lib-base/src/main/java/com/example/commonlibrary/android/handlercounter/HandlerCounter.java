package com.example.commonlibrary.android.handlercounter;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * description : 计数器
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/9 14:17
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class HandlerCounter {
    private static final String TAG = HandlerCounter.class.getSimpleName();

    private static final int MIN_INTERVAL_MILLS = 1;
    private static final int DEFAULT_INTERVAL_MILLS = 1000;
    private static final int REPEAT_INFINITE = -1;

    private static final RepeatMode REPEAT_MODE_DEFAULT = new RepeatMode() {
        @Override
        public boolean isInCycle(long startValue, long endValue, long stepIndex, long stepSize, long cycleIndex) {
            long stepCountOfCycle = (endValue - startValue) / stepSize + 1;
            long startIndexOfCycle = cycleIndex * stepCountOfCycle;
            long endIndexOfCycle = startIndexOfCycle + stepCountOfCycle - 1;
            return stepIndex >= startIndexOfCycle && stepIndex <= endIndexOfCycle;
        }

        @Override
        public long generate(long start, long end, long stepIndex, long stepSize, long cycleIndex) {
            return Util.nextValue(start, end, stepIndex * stepSize);
        }
    };

    private static final String KEY_PREFIX = HandlerCounter.class.getCanonicalName() + "#";

    private static final String KEY_STATUS = KEY_PREFIX + "mStatus";
    private static final String KEY_PAUSE_TIME_MILLS = KEY_PREFIX + "mPauseTimeMills";
    private static final String KEY_LAST_PAUSE_TIME_MILLS = KEY_PREFIX + "mLastPauseTimeMills";
    private static final String KEY_START_TIME_MILLS = KEY_PREFIX + "mStartTimeMills";
    private static final String KEY_STEP_INDEX = KEY_PREFIX + "mStepIndex";
    private static final String KEY_CURRENT_VALUE = KEY_PREFIX + "mCurrentValue";
    private static final String KEY_STRICT_MODE = KEY_PREFIX + "mStrictMode";
    private static final String KEY_STEP_SIZE = KEY_PREFIX + "mStepSize";
    private static final String KEY_COUNTER_INTERVAL = KEY_PREFIX + "mCountInterval";
    private static final String KEY_END_VALUE = KEY_PREFIX + "mEndValue";
    private static final String KEY_START_VALUE = KEY_PREFIX + "mStartValue";
    private static final String KEY_CYCLE_INDEX = KEY_PREFIX + "mCycleIndex";
    private static final String KEY_REPEAT_COUNT = KEY_PREFIX + "mRepeatCount";

    /**
     * attributes
     */
    private long mStartValue = 0;
    private long mEndValue = Long.MAX_VALUE;
    private long mCountInterval = DEFAULT_INTERVAL_MILLS;
    private long mStepSize = 1;
    private boolean mStrictMode = false;
    private long mRepeatCount = 1;

    /**
     * status
     */
    private long mCurrentValue;
    private long mStepIndex;
    private long mStartTimeMills;
    private long mLastPauseTimeMills;
    private long mPauseTimeMills;
    private long mCycleIndex;
    private CounterStatus mStatus;

    private final CounterHandler mHandler;
    private CountListener mCountListener;
    private CounterStatusListener mCounterStatusListener;
    private RepeatMode mRepeatMode = REPEAT_MODE_DEFAULT;

    public HandlerCounter() {
        mHandler = new CounterHandler(Looper.getMainLooper());
        mStatus = CounterStatus.IDLE;
    }

    public HandlerCounter startValue(long start) {
        mStartValue = start;
        return this;
    }

    public HandlerCounter endValue(long end) {
        mEndValue = end;
        return this;
    }

    public HandlerCounter countInterval(long mills) {
        if (mills < MIN_INTERVAL_MILLS) {
            throw new IllegalArgumentException();
        }
        mCountInterval = mills;
        return this;
    }

    public HandlerCounter stepSize(long stepSize) {
        mStepSize = stepSize;
        return this;
    }

    public HandlerCounter strictMode(boolean strictMode) {
        mStrictMode = strictMode;
        return this;
    }

    public HandlerCounter repeatCount(long repeat) {
        if (repeat < 1) {
            throw new IllegalArgumentException();
        }
        mRepeatCount = repeat;
        return this;
    }

    public HandlerCounter repeatInfinite() {
        mRepeatCount = REPEAT_INFINITE;
        return this;
    }

    public HandlerCounter repeatMode(RepeatMode mode) {
        if (mode == null) {
            throw new NullPointerException();
        }
        mRepeatMode = mode;
        return this;
    }

    public HandlerCounter clearRepeat() {
        mRepeatCount = 1;
        mRepeatMode = REPEAT_MODE_DEFAULT;
        return this;
    }

    public HandlerCounter countListener(CountListener listener) {
        mCountListener = listener;
        return this;
    }

    public HandlerCounter counterStatusListener(CounterStatusListener listener) {
        mCounterStatusListener = listener;
        return this;
    }

    public long getStartValue() {
        return mStartValue;
    }

    public long getEndValue() {
        return mEndValue;
    }

    public long getCountInterval() {
        return mCountInterval;
    }

    public long getCountStep() {
        return mStepSize;
    }

    public boolean isStrictMode() {
        return mStrictMode;
    }

    public HandlerCounter currentValue(long mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
        return this;
    }

    public long getCurrentValue() {
        return mCurrentValue;
    }

    public HandlerCounter start() {
        removeMessage();
        mCurrentValue = mStartValue;
        mStepIndex = 0;
        mStartTimeMills = System.currentTimeMillis();
        mPauseTimeMills = 0;
        mCycleIndex = 0;
        sendCountMessage();
        notifyNewStatus(CounterStatus.RUNNING);
        return this;
    }

    public HandlerCounter stop() {
        removeMessage();
        notifyNewStatus(CounterStatus.IDLE);
        return this;
    }

    public HandlerCounter pause() {
        if (mStatus != CounterStatus.RUNNING) {
            return this;
        }
        mLastPauseTimeMills = System.currentTimeMillis();
        removeMessage();
        notifyNewStatus(CounterStatus.PAUSE);
        return this;
    }

    public HandlerCounter restart() {
        if (mStatus != CounterStatus.PAUSE) {
            return this;
        }
        mPauseTimeMills += System.currentTimeMillis() - mLastPauseTimeMills;
        removeMessage();
        sendCountMessage();
        notifyNewStatus(CounterStatus.RUNNING);
        return this;
    }

    public void onSaveInstanceState(Bundle state) {
        state.putLong(KEY_START_VALUE, mStartValue);
        state.putLong(KEY_END_VALUE, mEndValue);
        state.putLong(KEY_COUNTER_INTERVAL, mCountInterval);
        state.putLong(KEY_STEP_SIZE, mStepSize);
        state.putBoolean(KEY_STRICT_MODE, mStrictMode);
        state.putLong(KEY_REPEAT_COUNT, mRepeatCount);

        state.putLong(KEY_CURRENT_VALUE, mCurrentValue);
        state.putLong(KEY_STEP_INDEX, mStepIndex);
        state.putLong(KEY_START_TIME_MILLS, mStartTimeMills);
        state.putLong(KEY_LAST_PAUSE_TIME_MILLS, mLastPauseTimeMills);
        state.putLong(KEY_PAUSE_TIME_MILLS, mPauseTimeMills);
        state.putInt(KEY_STATUS, mStatus.ordinal());
        state.putLong(KEY_CYCLE_INDEX, mCycleIndex);

        removeMessage();
    }

    public void onRestoreInstanceState(Bundle state) {
        mStartValue = state.getLong(KEY_START_VALUE);
        mEndValue = state.getLong(KEY_END_VALUE);
        mCountInterval = state.getLong(KEY_COUNTER_INTERVAL);
        mStepSize = state.getLong(KEY_STEP_SIZE);
        mStrictMode = state.getBoolean(KEY_STRICT_MODE);
        mRepeatCount = state.getLong(KEY_REPEAT_COUNT);

        mCurrentValue = state.getLong(KEY_CURRENT_VALUE);
        mStepIndex = state.getLong(KEY_STEP_INDEX);
        mStartTimeMills = state.getLong(KEY_START_TIME_MILLS);
        mLastPauseTimeMills = state.getLong(KEY_LAST_PAUSE_TIME_MILLS);
        mPauseTimeMills = state.getLong(KEY_PAUSE_TIME_MILLS);
        mStatus = CounterStatus.fromOrdinal(state.getInt(KEY_STATUS));
        mCycleIndex = state.getLong(KEY_CYCLE_INDEX);

        removeMessage();
        if (mStatus == CounterStatus.RUNNING) {
            sendCountMessage();
        }
    }

    void onCount() {
        long currentTimeMillis = System.currentTimeMillis();
        long countTime = currentTimeMillis - mStartTimeMills - mPauseTimeMills;
        long index = countTime / mCountInterval;
        boolean hasNext = true;

        if (mStrictMode) {
            for (long i = mStepIndex + 1; i <= index; i++) {
                if (adjustCycle(i)) {
                    doCount(i);
                } else {
                    hasNext = false;
                    break;
                }
            }
        } else {
            if (adjustCycle(index)) {
                doCount(index);
            } else {
                hasNext = false;
            }
        }

        long nextTimeMills = mStartTimeMills + mPauseTimeMills + (mStepIndex + 1) * mCountInterval;
        long delayMills = Math.max(0, nextTimeMills - currentTimeMillis);

        if (hasNext) {
            sendCountMessage(delayMills);
            return;
        }

        notifyNewStatus(CounterStatus.IDLE);
    }

    private boolean adjustCycle(long stepIndex) {
        long count = mRepeatCount;
        boolean isInfinite = mRepeatCount == REPEAT_INFINITE;

        for (long index = mCycleIndex; isInfinite || index < count; index++) {
            if (mRepeatMode.isInCycle(mStartValue, mEndValue, stepIndex, mStepSize, index)) {
                mCycleIndex = index;
                return true;
            }
        }
        return false;
    }

    private void doCount(long index) {
        mStepIndex = index;
        mCurrentValue = mRepeatMode.generate(mStartValue, mEndValue, mStepIndex, mStepSize, mCycleIndex);
        if (mCountListener != null) {
            mCountListener.onCount(mCurrentValue);
        }
    }

    private void sendCountMessage() {
        sendCountMessage(0);
    }

    private void sendCountMessage(long delayMills) {
        Message message = mHandler.obtainMessage(CounterHandler.MSG_COUNT);
        message.obj = new WeakReference<>(this);
        mHandler.sendMessageDelayed(message, delayMills);
    }

    private void removeMessage() {
        mHandler.removeMessages(CounterHandler.MSG_COUNT);
    }

    private void notifyNewStatus(CounterStatus status) {
        mStatus = status;
        if (mCounterStatusListener != null) {
            mCounterStatusListener.onNewStatus(mStatus);
        }
    }
}