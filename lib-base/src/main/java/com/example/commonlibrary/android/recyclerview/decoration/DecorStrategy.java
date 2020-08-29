package com.example.commonlibrary.android.recyclerview.decoration;

/**
 * @author : leixing
 * @date : 2018-01-22
 * <p>
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public interface DecorStrategy {
    boolean hasDecor(int position, int itemCount, int headerCount, int footerCount);
}
