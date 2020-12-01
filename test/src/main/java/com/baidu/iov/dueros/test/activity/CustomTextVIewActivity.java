package com.baidu.iov.dueros.test.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.iov.dueros.test.R;
import com.baidu.iov.dueros.test.view.CustomTextView;

/**
 * @author v_lining05
 * @date 2020/8/29
 */
public class CustomTextVIewActivity extends AppCompatActivity {

    private CustomTextView mCustomTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvtivity_custom_text_view);

        mCustomTextView = findViewById(R.id.customTextView);
    }

    public void RightToLeft(View view) {
        mCustomTextView.setDirection(CustomTextView.Direction.RIGHT_TO_LEFT);
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCustomTextView.setCurrentTextProgress(value);
            }
        });
        animator.start();
    }

    public void leftToRight(View view) {
        mCustomTextView.setDirection(CustomTextView.Direction.LEFT_TO_RIGHT);
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCustomTextView.setCurrentTextProgress(value);
            }
        });
        animator.start();
    }
}
