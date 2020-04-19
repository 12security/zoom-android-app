package com.zipow.videobox.confapp;

import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class InterpretationMgr {
    public static final int[] LAN_NAME_IDS = {C4558R.string.zm_language_english_88102, C4558R.string.zm_language_chinese_88102, C4558R.string.zm_language_japanese_88102, C4558R.string.zm_language_german_88102, C4558R.string.zm_language_french_88102, C4558R.string.zm_language_russian_88102, C4558R.string.zm_language_portuguese_88102, C4558R.string.zm_language_spanish_88102, C4558R.string.zm_language_korean_88102};
    public static final int[] LAN_RES_IDS = {C4558R.C4559drawable.zm_lan_unitedstates, C4558R.C4559drawable.zm_lan_china, C4558R.C4559drawable.zm_lan_japan, C4558R.C4559drawable.zm_lan_germany, C4558R.C4559drawable.zm_lan_france, C4558R.C4559drawable.zm_lan_russia, C4558R.C4559drawable.zm_lan_portugal, C4558R.C4559drawable.zm_lan_spain, C4558R.C4559drawable.zm_lan_southkorea};
    public static final String TAG = InterpretationMgr.class.getSimpleName();
    private long mNativeHandle = 0;
    private transient boolean needShowInterpreterTip = true;

    @Nullable
    private native int[] getAvailableInterpreteLansListImpl(long j);

    private native int getInterpreterActiveLanImpl(long j);

    @Nullable
    private native int[] getInterpreterLansImpl(long j);

    private native int getParticipantActiveLanImpl(long j);

    private native boolean isInterpretationEnabledImpl(long j);

    private native boolean isInterpretationStartedImpl(long j);

    private native boolean isInterpreterImpl(long j);

    private native boolean isOriginalAudioChannelEnabledImpl(long j);

    private native boolean setEventSinkImpl(long j, long j2);

    private native boolean setInterpreterActiveLanImpl(long j, int i);

    private native boolean setOriginalAudioChannelEnableImpl(long j, boolean z);

    private native boolean setParticipantActiveLanImpl(long j, int i);

    public InterpretationMgr(long j) {
        this.mNativeHandle = j;
    }

    public boolean isNeedShowInterpreterTip() {
        return this.needShowInterpreterTip;
    }

    public void setNeedShowInterpreterTip(boolean z) {
        this.needShowInterpreterTip = z;
    }

    public boolean isInterpretationEnabled() {
        return isInterpretationEnabledImpl(this.mNativeHandle);
    }

    public void setEventSink(InterpretationSinkUI interpretationSinkUI) {
        if (interpretationSinkUI != null) {
            setEventSinkImpl(this.mNativeHandle, interpretationSinkUI.getNativeHandle());
        }
    }

    public boolean isInterpretationStarted() {
        return isInterpretationStartedImpl(this.mNativeHandle);
    }

    @Nullable
    public int[] getAvailableInterpreteLansList() {
        return getAvailableInterpreteLansListImpl(this.mNativeHandle);
    }

    public boolean isInterpreter() {
        return isInterpreterImpl(this.mNativeHandle);
    }

    @Nullable
    public int[] getInterpreterLans() {
        return getInterpreterLansImpl(this.mNativeHandle);
    }

    public boolean setInterpreterActiveLan(int i) {
        return setInterpreterActiveLanImpl(this.mNativeHandle, i);
    }

    public int getInterpreterActiveLan() {
        return getInterpreterActiveLanImpl(this.mNativeHandle);
    }

    public boolean setParticipantActiveLan(int i) {
        return setParticipantActiveLanImpl(this.mNativeHandle, i);
    }

    public int getParticipantActiveLan() {
        return getParticipantActiveLanImpl(this.mNativeHandle);
    }

    public boolean setOriginalAudioChannelEnable(boolean z) {
        return setOriginalAudioChannelEnableImpl(this.mNativeHandle, z);
    }

    public boolean isOriginalAudioChannelEnabled() {
        return isOriginalAudioChannelEnabledImpl(this.mNativeHandle);
    }
}
