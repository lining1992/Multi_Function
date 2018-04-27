package com.fast.framework.support;

import android.os.Build;

/**
 * 生成android设备唯一标识ID.此方式不需要向系统申请任何的权限。
 * <p>
 * Created by lishicong on 2016/12/19.
 */

public class UUID {

    private static UUID instance;

    private String uuid;

    private UUID() {
        uuid = getUniquePsuedoID();
    }

    public static UUID getInstance() {
        if (instance == null) {
            synchronized (UUID.class) {
                if (instance == null) {
                    instance = new UUID();
                }
            }
        }
        return instance;
    }

    /**
     * 获取设备唯一标识
     *
     * @return 00000000-7208-ba4c-ffff-ffffef05ac4a
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * 获得独一无二的Psuedo ID
     *
     * @return
     */
    private String getUniquePsuedoID() {

        String serial = null;

        StringBuffer sbf = new StringBuffer();

        sbf.append("35");
        sbf.append(Build.BOARD.length() % 10);
        sbf.append(Build.BRAND.length() % 10);
        sbf.append(Build.CPU_ABI.length() % 10);
        sbf.append(Build.DEVICE.length() % 10);
        sbf.append(Build.DISPLAY.length() % 10);
        sbf.append(Build.HOST.length() % 10);
        sbf.append(Build.ID.length() % 10);
        sbf.append(Build.MANUFACTURER.length() % 10);
        sbf.append(Build.MODEL.length() % 10);
        sbf.append(Build.PRODUCT.length() % 10);
        sbf.append(Build.TAGS.length() % 10);
        sbf.append(Build.TYPE.length() % 10);
        sbf.append(Build.USER.length() % 10);

        // 使用硬件信息拼凑出来的15位号码，“35”加上后面的13位一共15位，demo：355715565309247 一串号码
        String szDevIDShort = sbf.toString();

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            // API>=9 使用serial号
            return new java.util.UUID(szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }

        return new java.util.UUID(szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

}
