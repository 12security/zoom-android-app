package com.zipow.videobox.dialog;

import android.os.Bundle;

public interface DialogActionCallBack {
    public static final int ACTION_BACK_PRESSED = 1;
    public static final int ACTION_CANCEL = -100;
    public static final int ACTION_NEGATIVE = -2;
    public static final int ACTION_NEUTRAL = -3;
    public static final int ACTION_POSITIVE = -1;

    void performDialogAction(int i, int i2, Bundle bundle);
}
