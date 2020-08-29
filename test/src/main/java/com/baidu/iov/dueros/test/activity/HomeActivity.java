package com.baidu.iov.dueros.test.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.iov.dueros.test.R;
import com.baidu.iov.dueros.test.view.CustomView;

/**
 * @author v_lining05
 * @date 2020/8/26
 */
public class HomeActivity extends AppCompatActivity {

    private CustomView customView;
    private boolean isFrist;
    private int currentStep;
    private int i = 1;
    private int preStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acvtivity_home);
        customView = findViewById(R.id.customView);
        customView.setMaxStep(4000);
        isFrist = true;
    }

    public void startAnimation(View view) {
        currentStep = currentStep + i * 100;
        if (currentStep > 4000) {
            Toast.makeText(getApplicationContext(), "别走了，很累的", Toast.LENGTH_SHORT).show();
            currentStep = 0;
            preStep = 0;
            i = 0;
            return;
        }
        i++;
        ValueAnimator animator = ObjectAnimator.ofFloat(preStep, currentStep);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                customView.setProgress((int) animatedValue);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        preStep = currentStep;
    }
}
