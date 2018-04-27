/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.widget;

import com.fast.framework.util.RandomUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.media.audiofx.Visualizer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 音乐频谱视图
 * <p>
 * Created by lishicong on 2017/5/4.
 */

public class VisualizerView extends View implements Visualizer.OnDataCaptureListener {

    private static final int DN_W = 470; // view宽度与单个音频块占比 - 正常480 需微调
    private static final int DN_H = 360; // view高度与单个音频块占比
    private static final int DN_SL = 15; // 单个音频块宽度
    private static final int DN_SW = 5; // 单个音频块高度

    private int hgap = 0;
    private int vgap = 0;
    private int levelStep = 0;
    private float strokeWidth = 0;
    private float strokeLength = 0;

    protected static final int MAX_LEVEL = 20; // 音量柱·音频块 - 最大个数

    protected static final int CYLINDER_NUM = 26; // 音量柱 - 最大个数

    protected Visualizer mVisualizer = null; // 频谱器

    protected Paint mPaint = null; // 画笔

    protected byte[] mData = new byte[CYLINDER_NUM]; // 音量柱 数组

    // 构造函数初始化画笔
    public VisualizerView(Context context) {
        super(context);
        this.init();
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        mPaint = new Paint(); // 初始化画笔工具
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setColor(Color.WHITE); // 画笔颜色

        mPaint.setStrokeJoin(Join.ROUND);  // 频块圆角
        mPaint.setStrokeCap(Cap.ROUND);  // 频块圆角
    }

    // 执行 Layout 操作
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float w;
        float h;
        float xr;
        float yr;

        w = right - left;
        h = bottom - top;
        xr = w / (float) DN_W;
        yr = h / (float) DN_H;

        strokeWidth = DN_SW * yr;
        strokeLength = DN_SL * xr;
        hgap = (int) ((w - strokeLength * CYLINDER_NUM) / (CYLINDER_NUM + 1));
        vgap = (int) (h / (MAX_LEVEL + 2)); // 频谱块高度

        mPaint.setStrokeWidth(strokeWidth);  // 设置频谱块宽度
    }

    // 绘制频谱块和倒影
    protected void drawCylinder(Canvas canvas, float x, byte value) {
        if (value == 0) {
            value = 1;
        } // 最少有一个频谱块
        for (int i = 0; i < value; i++) {  // 每个能量柱绘制value个能量块
            float y = (getHeight() / 1.25f - i * vgap - vgap); // 计算y轴坐标
            float y1 = (getHeight() / 1.25f + i * vgap + vgap);
            // 绘制频谱块
            mPaint.setColor(Color.WHITE); // 画笔颜色
            mPaint.setAlpha(135);
            canvas.drawLine(x, y, (x + strokeLength), y, mPaint); // 绘制频谱块

            // 绘制音量柱倒影
            if (i <= 6 && value > 0) {
                mPaint.setColor(Color.WHITE); // 画笔颜色
                mPaint.setAlpha(100 - (100 / 6 * i)); // 倒影颜色
                canvas.drawLine(x, y1, (x + strokeLength), y1, mPaint); // 绘制频谱块
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        int j = -4;
        for (int i = 0; i < CYLINDER_NUM / 2 - 4; i++) {  // 绘制25个能量柱

            drawCylinder(canvas, strokeWidth / 2 + hgap + i * (hgap + strokeLength), mData[i]);
        }
        for (int i = CYLINDER_NUM; i >= CYLINDER_NUM / 2 - 4; i--) {
            j++;
            drawCylinder(canvas, strokeWidth / 2 + hgap + (CYLINDER_NUM / 2 + j - 1) * (hgap + strokeLength),
                         mData[i - 1]);
        }
    }

    /**
     * It sets the visualizer of the view. DO set the viaulizer to null when exit the program.
     *
     * @parma visualizer It is the visualizer to set.
     */
    public void setVisualizer(Visualizer visualizer) {
        if (visualizer != null) {
            if (!visualizer.getEnabled()) {
                visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
            }
            levelStep = 230 / MAX_LEVEL;

            //  先说后面三个参数：rate采样的频率，下边通过方法Visualizer.getMaxCaptureRate()
            //  返回最大的采样频率，单位为milliHertz毫赫兹，iswave是波形信号，isfft是频域信号。
            //  第一个参数OnDataCaptureListener接口，这里可以一个它的匿名内部类，然后它有两个回调方法：
            //  这两个回调对应着上边的两个参数iswave和isfft！如果iswave为true，isfft为false则会回调onWaveFormDataCapture方法，
            //  如果iswave为false，isfft 为true则会回调onFftDataCapture方法
            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, false, true);

        } else {

            if (mVisualizer != null) {
                mVisualizer.setEnabled(false);
                mVisualizer.release();
            }
        }
        mVisualizer = visualizer;
    }

    /**
     * 这个回调应该采集的是快速傅里叶变换有关的数据
     * <p>
     * 说到这不得不提一下数字信号处理相关的知识，FFT（Fast Fourier Transformation），即快速傅里叶转换，它用于把时域上连续的信号(波形)强度转换成离散的频域信号(频谱)
     *
     * @param visualizer
     * @param fft
     * @param samplingRate
     */
    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

        byte[] model = new byte[fft.length / 2 + 1];
        model[0] = (byte) Math.abs(fft[1]);
        int j = 1;
        for (int i = 2; i < fft.length; ) {
            model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
            i += 2;
            j++;
        }
        for (int i = 0; i < CYLINDER_NUM; i++) {
            final byte a = (byte) (Math.abs(model[CYLINDER_NUM - i]) / levelStep);

            final byte b = mData[i];
            if (a > b) {
                mData[i] = a;
            } else if (b > 0) {
                mData[i]--;
            }
        }

        //  feige系统获取fft数据有问题，为了美观人工干预数据显示
        int max = 0;
        for (int i = 0; i < mData.length; i++) {
            if (max < mData[i]) {
                max = mData[i];
            }
        }
        for (int i = 0; i < mData.length; i++) {
            if (mData[i] < max && mData[i] <= 1) {
                mData[i] = (byte) RandomUtil.getRandom(0, max);
            }
        }

        postInvalidate(); // 刷新界面
    }

    // 这个回调应该采集的是波形数据
    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        //  Do nothing...
    }
}
