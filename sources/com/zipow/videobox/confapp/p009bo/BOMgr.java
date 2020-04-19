package com.zipow.videobox.confapp.p009bo;

import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUserList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.confapp.bo.BOMgr */
public class BOMgr {
    public static final int BO_GET_FLAG_ANY = 3;
    public static final int BO_GET_FLAG_ASSIGN = 1;
    public static final int BO_GET_FLAG_JOINED = 2;
    private long mNativeHandle;

    private native boolean clearImpl(long j, int i);

    private native long createBOImpl(long j, String str, List<String> list);

    private native int generateNewNameIndexImpl(long j);

    private native int getBOObjectCountImpl(long j);

    private native long getBOObjectHandleByBIdImpl(long j, String str);

    private native long getBOObjectHandleByIndexImpl(long j, int i);

    private native int getControlStatusImpl(long j);

    private native int getElapseTimeImpl(long j);

    private native int getLasterErrorImpl(long j);

    private native long getMasterConfUserListImpl(long j);

    private native long getMyBOAssignedObjectHandlerImpl(long j);

    private native long getMyBOJoinedObjectHandlerImpl(long j);

    private native long getMyBOObjectHandlerImpl(long j);

    private native long getMyBOUserHandlerImpl(long j);

    private native int getStopWaitingSecondsImpl(long j);

    private native int getTimerDurationImpl(long j);

    private native boolean isAutoJoinEnableImpl(long j);

    private native boolean isBOControllerImpl(long j);

    private native boolean isBOModeratorImpl(long j);

    private native boolean isBOTokenReadyImpl(long j);

    private native boolean isBackToMainSessionEnabledImpl(long j);

    private native boolean isInBOMeetingImpl(long j);

    private native boolean isTimerAutoEndEnabledImpl(long j);

    private native boolean isTimerEnabledImpl(long j);

    private native boolean joinBOImpl(long j, String str, int i);

    private native boolean leaveBOImpl(long j);

    private native boolean notifyHelpRequestHandledImpl(long j, String str, int i);

    private native boolean removeBOImpl(long j, String str);

    private native boolean requestForHelpImpl(long j);

    private native void resetNewNameIndexImpl(long j);

    private native boolean startImpl(long j);

    private native boolean stopImpl(long j, int i);

    private native boolean updateBOImpl(long j, String str, String str2, List<String> list);

    public BOMgr(long j) {
        this.mNativeHandle = j;
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }

    private boolean initialized() {
        return this.mNativeHandle != 0;
    }

    @Nullable
    public BOObject getMyBOObject(int i) {
        long j = i == 1 ? getMyBOAssignedObjectHandlerImpl(this.mNativeHandle) : i == 2 ? getMyBOJoinedObjectHandlerImpl(this.mNativeHandle) : i == 3 ? getMyBOObjectHandler() : 0;
        if (j == 0) {
            return null;
        }
        return new BOObject(j);
    }

    public int getBOObjectCount() {
        if (!initialized()) {
            return 0;
        }
        return getBOObjectCountImpl(this.mNativeHandle);
    }

    @Nullable
    public BOObject getBOObjectByIndex(int i) {
        if (!initialized() || i < 0) {
            return null;
        }
        long bOObjectHandleByIndexImpl = getBOObjectHandleByIndexImpl(this.mNativeHandle, i);
        if (bOObjectHandleByIndexImpl == 0) {
            return null;
        }
        return new BOObject(bOObjectHandleByIndexImpl);
    }

    @Nullable
    public BOObject getBOObjectByBId(String str) {
        if (!initialized() || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long bOObjectHandleByBIdImpl = getBOObjectHandleByBIdImpl(this.mNativeHandle, str);
        if (bOObjectHandleByBIdImpl == 0) {
            return null;
        }
        return new BOObject(bOObjectHandleByBIdImpl);
    }

    private long getMyBOObjectHandler() {
        if (!initialized()) {
            return 0;
        }
        return getMyBOObjectHandlerImpl(this.mNativeHandle);
    }

    private long getMyBOUserHandler() {
        if (!initialized()) {
            return 0;
        }
        return getMyBOUserHandlerImpl(this.mNativeHandle);
    }

    public boolean isInBOMeeting() {
        if (!initialized()) {
            return false;
        }
        return isInBOMeetingImpl(this.mNativeHandle);
    }

    public int getControlStatus() {
        if (!initialized()) {
            return 1;
        }
        return getControlStatusImpl(this.mNativeHandle);
    }

    public boolean isBOController() {
        if (!initialized()) {
            return false;
        }
        return isBOControllerImpl(this.mNativeHandle);
    }

    public boolean isBOModerator() {
        if (!initialized()) {
            return false;
        }
        return isBOModeratorImpl(this.mNativeHandle);
    }

    @Nullable
    public CmmUserList getMasterConfUserList() {
        if (!initialized()) {
            return null;
        }
        long masterConfUserListImpl = getMasterConfUserListImpl(this.mNativeHandle);
        if (masterConfUserListImpl == 0) {
            return null;
        }
        return new CmmUserList(masterConfUserListImpl);
    }

    public int getLasterError() {
        if (!initialized()) {
            return 0;
        }
        return getLasterErrorImpl(this.mNativeHandle);
    }

    public long createBO(String str, List<String> list) {
        if (!initialized()) {
            return 0;
        }
        return createBOImpl(this.mNativeHandle, str, list);
    }

    public boolean updateBOImpl(String str, String str2, List<String> list) {
        if (!initialized()) {
            return false;
        }
        return updateBOImpl(this.mNativeHandle, str, str2, list);
    }

    public boolean removeBO(String str) {
        if (!initialized() || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return removeBOImpl(this.mNativeHandle, str);
    }

    public boolean isBOTokenReady() {
        if (!initialized()) {
            return false;
        }
        return isBOTokenReadyImpl(this.mNativeHandle);
    }

    public boolean start() {
        if (!initialized()) {
            return false;
        }
        return startImpl(this.mNativeHandle);
    }

    public boolean stop(int i) {
        if (!initialized()) {
            return false;
        }
        if (i < 0) {
            i = 0;
        }
        return stopImpl(this.mNativeHandle, i);
    }

    public boolean clear(int i) {
        if (!initialized()) {
            return false;
        }
        if (i < 0) {
            i = 0;
        }
        return clearImpl(this.mNativeHandle, i);
    }

    public boolean notifyHelpRequestHandled(String str, int i) {
        if (!initialized()) {
            return false;
        }
        return notifyHelpRequestHandledImpl(this.mNativeHandle, str, i);
    }

    public int generateNewNameIndex() {
        if (!initialized()) {
            return 0;
        }
        return generateNewNameIndexImpl(this.mNativeHandle);
    }

    public void resetNewNameIndex() {
        if (initialized()) {
            resetNewNameIndexImpl(this.mNativeHandle);
        }
    }

    public boolean joinBO(String str, int i) {
        if (!initialized() || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return joinBOImpl(this.mNativeHandle, str, i);
    }

    public boolean leaveBO() {
        if (!initialized()) {
            return false;
        }
        return leaveBOImpl(this.mNativeHandle);
    }

    public boolean requestForHelp() {
        if (!initialized()) {
            return false;
        }
        return requestForHelpImpl(this.mNativeHandle);
    }

    public boolean isAutoJoinEnable() {
        if (!initialized()) {
            return false;
        }
        return isAutoJoinEnableImpl(this.mNativeHandle);
    }

    public boolean isBackToMainSessionEnabled() {
        if (!initialized()) {
            return false;
        }
        return isBackToMainSessionEnabledImpl(this.mNativeHandle);
    }

    public int getStopWaitingSeconds() {
        if (!initialized()) {
            return 0;
        }
        return getStopWaitingSecondsImpl(this.mNativeHandle);
    }

    public boolean isTimerEnabled() {
        if (!initialized()) {
            return false;
        }
        return isTimerEnabledImpl(this.mNativeHandle);
    }

    public boolean isTimerAutoEndEnabled() {
        if (!initialized()) {
            return false;
        }
        return isTimerAutoEndEnabledImpl(this.mNativeHandle);
    }

    public int getTimerDuration() {
        if (!initialized()) {
            return 0;
        }
        return getTimerDurationImpl(this.mNativeHandle);
    }

    public int getRunTimeElapsedTime() {
        if (!initialized()) {
            return 0;
        }
        return getElapseTimeImpl(this.mNativeHandle);
    }
}
