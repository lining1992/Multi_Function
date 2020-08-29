package com.example.commonlibrary.android.handlercounter;

/**
 * description : RepeatMode specify how to repeat
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/16 11:17
 */
public interface RepeatMode {

    /**
     * 是否是周期末尾
     *
     * @param startValue 起始值
     * @param endValue   终止值
     * @param stepIndex  取值下标
     * @param stepSize   步长
     * @param cycleIndex 周期数
     * @return 是否结束此周期
     */
    boolean isInCycle(long startValue, long endValue, long stepIndex, long stepSize, long cycleIndex);

    /**
     * 生成数值
     *
     * @param start      区间的起始值
     * @param end        区间的终止值
     * @param stepIndex  取值的下标
     * @param stepSize   取值间隔步长
     * @param cycleIndex 周期数 从0开始
     * @return 生成的值
     */
    long generate(long start, long end, long stepIndex, long stepSize, long cycleIndex);


    /**
     * 重新开始计数
     */
    RepeatMode RESTART = new RepeatMode() {
        @Override
        public boolean isInCycle(long startValue, long endValue, long stepIndex, long stepSize, long cycleIndex) {
            long stepCountOfCycle = (endValue - startValue) / stepSize + 1;
            long startIndexOfCycle = cycleIndex * stepCountOfCycle;
            long endIndexOfCycle = startIndexOfCycle + stepCountOfCycle - 1;
            return stepIndex >= startIndexOfCycle && stepIndex <= endIndexOfCycle;
        }

        @Override
        public long generate(long start, long end, long stepIndex, long stepSize, long cycleIndex) {
            long stepCountOfCycle = (end - start) / stepSize + 1;
            long stepIndexInCycle = stepIndex % stepCountOfCycle;
            return Util.nextValue(start, end, stepIndexInCycle * stepSize);
        }
    };

    /**
     * 反向计数
     */
    RepeatMode REVERSE = new RepeatMode() {
        @Override
        public boolean isInCycle(long startValue, long endValue, long stepIndex, long stepSize, long cycleIndex) {
            long stepCountOfCycle = (endValue - startValue) / stepSize + 1;
            long startIndexOfCycle;
            long endIndexOfCycle;
            if (cycleIndex == 0) {
                startIndexOfCycle = 0;
                endIndexOfCycle = stepCountOfCycle - 1;
            } else {
                startIndexOfCycle = cycleIndex * (stepCountOfCycle - 1) + 1;
                endIndexOfCycle = startIndexOfCycle + (stepCountOfCycle - 1) - 1;
            }

            return stepIndex >= startIndexOfCycle && stepIndex <= endIndexOfCycle;
        }

        @Override
        public long generate(long start, long end, long stepIndex, long stepSize, long cycleIndex) {
            long stepCountOfCycle = (end - start) / stepSize + 1;
            long stepIndexInCycle;
            if (cycleIndex == 0) {
                stepIndexInCycle = stepIndex;
                return Util.nextValue(start, end, stepIndexInCycle * stepSize);
            } else {
                stepIndexInCycle = (stepIndex - 1) % (stepCountOfCycle - 1);
                if ((cycleIndex & 1) == 0) {
                    return Util.nextValue(start, end, (stepIndexInCycle + 1) * stepSize);
                } else {
                    return Util.nextValue(end, start, (stepIndexInCycle + 1) * stepSize);
                }
            }
        }
    };
}
