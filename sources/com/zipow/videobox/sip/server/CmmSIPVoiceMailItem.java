package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmSIPVoiceMailItem {
    private long mNativeHandle;

    private native int getAudioFileCountImpl(long j);

    private native long getAudioFileItemByIDImpl(long j, String str);

    private native long getAudioFileItemByIndexImpl(long j, int i);

    private native long getCreateTimeImpl(long j);

    @Nullable
    private native String getForwardExtensionIDImpl(long j);

    private native int getForwardExtensionLevelImpl(long j);

    @Nullable
    private native String getForwardExtensionNameImpl(long j);

    @Nullable
    private native String getFromPhoneNumberImpl(long j);

    @Nullable
    private native String getFromUserNameImpl(long j);

    @Nullable
    private native String getIDImpl(long j);

    @Nullable
    private native String getTranscriptImpl(long j);

    private native boolean isRestrictedVoiceMailImpl(long j);

    private native boolean isUnreadImpl(long j);

    public CmmSIPVoiceMailItem(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getIDImpl(j);
    }

    public long getCreateTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCreateTimeImpl(j);
    }

    @Nullable
    public String getFromUserName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getFromUserNameImpl(j);
    }

    @Nullable
    public String getFromPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getFromPhoneNumberImpl(j);
    }

    @Nullable
    public String getTranscript() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getTranscriptImpl(j);
    }

    public boolean isUnread() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isUnreadImpl(j);
    }

    public int getAudioFileCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getAudioFileCountImpl(j);
    }

    @Nullable
    public CmmSIPAudioFileItem getAudioFileItemByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long audioFileItemByIndexImpl = getAudioFileItemByIndexImpl(j, i);
        if (audioFileItemByIndexImpl == 0) {
            return null;
        }
        return new CmmSIPAudioFileItem(audioFileItemByIndexImpl);
    }

    @Nullable
    public CmmSIPAudioFileItem getAudioFileItemByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long audioFileItemByIDImpl = getAudioFileItemByIDImpl(j, StringUtil.safeString(str));
        if (audioFileItemByIDImpl == 0) {
            return null;
        }
        return new CmmSIPAudioFileItem(audioFileItemByIDImpl);
    }

    @Nullable
    public String getForwardExtensionID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getForwardExtensionIDImpl(j);
    }

    @Nullable
    public String getForwardExtensionName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getForwardExtensionNameImpl(j);
    }

    public int getForwardExtensionLevel() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getForwardExtensionLevelImpl(j);
    }

    public Boolean isRestrictedVoiceMail() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(isRestrictedVoiceMailImpl(j));
    }
}
