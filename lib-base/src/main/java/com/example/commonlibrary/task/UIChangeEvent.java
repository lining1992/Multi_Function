package com.example.commonlibrary.task;
public final class UIChangeEvent {
    public static final int STATUS_START = 0;
    public static final int STATUS_STOP = 1;
    public static final int STATUS_DESTROY = 2;

    public final int status;

    public final String uniqueId;

    public UIChangeEvent(int status, String uniqueId) {
        this.status = status;
        this.uniqueId = uniqueId;
    }


}
