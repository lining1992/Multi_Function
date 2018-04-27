package com.baidu.lining.quicksidebar.listener;

/**
 * Created by Sai on 16/3/25.
 */
public interface OnQuickSideBarTouchListener {
    void onLetterChanged(String letter, int position, float y);
    void onLetterTouching(boolean touching);
}
