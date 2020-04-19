package com.zipow.videobox.view;

import android.os.Handler;

public class ZMFeccEventTimeRunnable implements Runnable {
    private int mEvent = 0;
    private Handler mHandler;
    private int mLastEvent = 0;
    private IFeccListener mListener;

    public void initial(int i, Handler handler, IFeccListener iFeccListener) {
        this.mLastEvent = i;
        this.mEvent = i;
        this.mHandler = handler;
        this.mListener = iFeccListener;
    }

    public void updateEvent(int i) {
        this.mEvent = i;
        int i2 = this.mLastEvent;
        if (i != i2) {
            IFeccListener iFeccListener = this.mListener;
            if (iFeccListener != null) {
                iFeccListener.onFeccClick(3, i2);
            }
        }
    }

    public void run() {
        IFeccListener iFeccListener = this.mListener;
        if (iFeccListener != null) {
            int i = this.mLastEvent;
            int i2 = this.mEvent;
            if (i != i2 || i2 == 0) {
                this.mListener.onFeccClick(1, this.mEvent);
            } else {
                iFeccListener.onFeccClick(2, i2);
            }
            this.mLastEvent = this.mEvent;
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.postDelayed(this, 300);
            }
        }
    }
}
