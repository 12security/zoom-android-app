package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration;
import p021us.zoom.androidlib.util.StringUtil;

public class ISIPCallConfigration {
    private long mNativeHandle;

    @Nullable
    private native String getCallFromNumberImpl(long j);

    private native int getCallerIDModeImpl(long j);

    @Nullable
    private native byte[] getCloudPBXInfoImpl(long j);

    private native long getLastRegistrationImpl(long j);

    @Nullable
    private native String getPreviousCalloutPhonenumberImpl(long j);

    @Nullable
    private native byte[] getRegisterInfoImpl(long j);

    private native int getSIPUserStatusImpl(long j);

    private native String getSelectedLineIdImpl(long j);

    private native boolean isAudioTransferToMeetingPromptReadedImpl(long j);

    private native boolean isBlockedCallerIDSelectedImpl(long j);

    private native boolean isCloudPBXEnabledImpl(long j);

    private native boolean isE911ServicePromptReadedImpl(long j);

    private native boolean isFirstTimeForSLAHoldImpl(long j);

    private native boolean isSIPCallEnabledImpl(long j);

    private native boolean isSharedLineEnabledImpl(long j);

    private native boolean isToggleAudioForUnHoldPromptReadedImpl(long j);

    private native boolean selectBlockedCallerIDImpl(long j, boolean z);

    private native boolean setAudioTransferToMeetingPromptAsReadedImpl(long j, boolean z);

    private native boolean setCallFromNumberImpl(long j, String str);

    private native boolean setE911ServicePromptAsReadedImpl(long j, boolean z);

    private native void setFirstTimeForSLAHoldImpl(long j, boolean z);

    private native boolean setPreviousCalloutPhonenumberImpl(long j, String str);

    private native boolean setRegisterInfoImpl(long j, byte[] bArr);

    private native boolean setToggleAudioForUnHoldPromptAsReadedImpl(long j, boolean z);

    public ISIPCallConfigration(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public SipPhoneIntegration getRegsiterInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            byte[] registerInfoImpl = getRegisterInfoImpl(j);
            if (registerInfoImpl != null && registerInfoImpl.length > 0) {
                return SipPhoneIntegration.parseFrom(registerInfoImpl);
            }
        } catch (InvalidProtocolBufferException unused) {
        }
        return null;
    }

    public boolean setRegisterInfo(@NonNull SipPhoneIntegration sipPhoneIntegration) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return setRegisterInfoImpl(this.mNativeHandle, sipPhoneIntegration.toByteArray());
    }

    @Nullable
    public CloudPBX getCloudPBXInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] cloudPBXInfoImpl = getCloudPBXInfoImpl(j);
        if (cloudPBXInfoImpl == null || cloudPBXInfoImpl.length == 0) {
            return null;
        }
        try {
            return CloudPBX.parseFrom(cloudPBXInfoImpl);
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean isSIPCallEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSIPCallEnabledImpl(j);
    }

    public boolean isCloudPBXEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isCloudPBXEnabledImpl(j);
    }

    public boolean isSharedLineEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSharedLineEnabledImpl(j);
    }

    public boolean setCallFromNumber(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setCallFromNumberImpl(j, StringUtil.safeString(str));
    }

    @Nullable
    public String getCallFromNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCallFromNumberImpl(j);
    }

    @Nullable
    public String getSelectedLineId() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSelectedLineIdImpl(j);
    }

    public long getLastRegistration() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLastRegistrationImpl(j);
    }

    public boolean isE911ServicePromptReaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isE911ServicePromptReadedImpl(j);
    }

    public boolean setE911ServicePromptAsReaded(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setE911ServicePromptAsReadedImpl(j, z);
    }

    @Nullable
    public String getPreviousCalloutPhonenumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPreviousCalloutPhonenumberImpl(j);
    }

    public boolean isBlockedCallerIDSelected() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isBlockedCallerIDSelectedImpl(j);
    }

    public boolean selectBlockedCallerID(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return selectBlockedCallerIDImpl(j, z);
    }

    public boolean setPreviousCalloutPhonenumber(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setPreviousCalloutPhonenumberImpl(j, StringUtil.safeString(str));
    }

    public int getSIPUserStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSIPUserStatusImpl(j);
    }

    public int getCallerIDMode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallerIDModeImpl(j);
    }

    public boolean isToggleAudioForUnHoldPromptReaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isToggleAudioForUnHoldPromptReadedImpl(j);
    }

    public void setToggleAudioForUnHoldPromptAsReaded(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setToggleAudioForUnHoldPromptAsReadedImpl(j, z);
        }
    }

    public boolean isAudioTransferToMeetingPromptReaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isAudioTransferToMeetingPromptReadedImpl(j);
    }

    public void setAudioTransferToMeetingPromptAsReaded(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setAudioTransferToMeetingPromptAsReadedImpl(j, z);
        }
    }

    public boolean isFirstTimeForSLAHold() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isFirstTimeForSLAHoldImpl(j);
    }

    public void setFirstTimeForSLAHold(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setFirstTimeForSLAHoldImpl(j, z);
        }
    }
}
