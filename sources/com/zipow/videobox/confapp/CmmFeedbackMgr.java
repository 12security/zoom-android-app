package com.zipow.videobox.confapp;

import p021us.zoom.videomeetings.C4558R;

public class CmmFeedbackMgr {
    private long mNativeHandle;

    private native boolean changeMyFeedbackImpl(long j, int i);

    private native void clearAllFeedbackImpl(long j);

    private native int getAllFeedbackCountImpl(long j);

    private native int getEmojiFeedbackCountImpl(long j);

    private native int getFeedbackCountImpl(long j, int i);

    public CmmFeedbackMgr(long j) {
        this.mNativeHandle = j;
    }

    public boolean changeMyFeedback(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return changeMyFeedbackImpl(j, i);
    }

    public void clearAllFeedback() {
        long j = this.mNativeHandle;
        if (j != 0) {
            clearAllFeedbackImpl(j);
        }
    }

    public int getFeedbackCount(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFeedbackCountImpl(j, i);
    }

    public int getAllFeedbackCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getAllFeedbackCountImpl(j);
    }

    public int getEmojiFeedbackCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getEmojiFeedbackCountImpl(j);
    }

    public static int getIconIdByFeedback(int i) {
        switch (i) {
            case 1:
                return C4558R.C4559drawable.zm_ic_raise_hand;
            case 2:
                return C4558R.C4559drawable.zm_ic_yes;
            case 3:
                return C4558R.C4559drawable.zm_ic_no;
            case 4:
                return C4558R.C4559drawable.zm_ic_faster;
            case 5:
                return C4558R.C4559drawable.zm_ic_slower;
            case 6:
                return C4558R.C4559drawable.zm_ic_dislike;
            case 7:
                return C4558R.C4559drawable.zm_ic_like;
            case 8:
                return C4558R.C4559drawable.zm_ic_clap;
            case 9:
                return C4558R.C4559drawable.zm_ic_coffee;
            case 10:
                return C4558R.C4559drawable.zm_ic_clock;
            case 11:
                return C4558R.C4559drawable.zm_ic_emojimore;
            default:
                return 0;
        }
    }
}
