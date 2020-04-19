package com.zipow.videobox.view;

public interface IFeccListener {
    public static final int FECC_ACTION_CONTINUE = 2;
    public static final int FECC_ACTION_START = 1;
    public static final int FECC_ACTION_STOP = 3;
    public static final int FECC_EVENT_DOWN = 1;
    public static final int FECC_EVENT_LEFT = 3;
    public static final int FECC_EVENT_NONE = 0;
    public static final int FECC_EVENT_OUTRANGE = -1;
    public static final int FECC_EVENT_RIGHT = 4;
    public static final int FECC_EVENT_UP = 2;
    public static final int FECC_EVENT_ZOOM_IN = 5;
    public static final int FECC_EVENT_ZOOM_OUT = 6;

    void onFeccClick(int i, int i2);
}
