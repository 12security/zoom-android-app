package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettings;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.NotificationSettingMgr */
public class NotificationSettingMgr {
    private long mNativeHandle;

    /* renamed from: com.zipow.videobox.ptapp.mm.NotificationSettingMgr$DndSetting */
    public static class DndSetting {
        int[] params = new int[5];

        DndSetting(int[] iArr) {
            this.params = iArr;
        }

        public boolean isEnable() {
            int[] iArr = this.params;
            return iArr != null && iArr[0] == 1;
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=boolean, code=int, for r3v0, types: [int, boolean] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setEnable(int r3) {
            /*
                r2 = this;
                int[] r0 = r2.params
                r1 = 0
                r0[r1] = r3
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr.DndSetting.setEnable(boolean):void");
        }

        @Nullable
        public Calendar getStart() {
            if (this.params == null) {
                return null;
            }
            Calendar instance = Calendar.getInstance();
            instance.set(11, this.params[1]);
            instance.set(12, this.params[2]);
            return instance;
        }

        @Nullable
        public Calendar getEnd() {
            if (this.params == null) {
                return null;
            }
            Calendar instance = Calendar.getInstance();
            instance.set(11, this.params[3]);
            instance.set(12, this.params[4]);
            return instance;
        }

        public void setStart(@Nullable Calendar calendar) {
            if (calendar != null) {
                this.params[1] = calendar.get(11);
                this.params[2] = calendar.get(12);
            }
        }

        public void setEnd(@Nullable Calendar calendar) {
            if (calendar != null) {
                this.params[3] = calendar.get(11);
                this.params[4] = calendar.get(12);
            }
        }
    }

    private native boolean applyBlockAllSettingsImpl(long j, int i, int i2, int i3);

    private native boolean applyDNDNowSettingImpl(long j, int i);

    private native boolean applyDndSettingsImpl(long j, boolean z, int i, int i2, int i3, int i4);

    private native boolean applyFollowedThreadNotifySettingImpl(long j, boolean z);

    private native boolean applyInCallSettingsImpl(long j, boolean z);

    private native boolean applyKeywordSettingImpl(long j, List<String> list, List<String> list2);

    private native boolean applyMUCSettingsImpl(long j, List<String> list, int i);

    private native boolean applyPersonSettingImpl(long j, List<String> list, List<String> list2);

    private native boolean applySnoozeSettingsImpl(long j, long j2, long j3, long j4);

    private native boolean getBlockAllSettingsImpl(long j, int[] iArr);

    private native int getDNDNowSettingImpl(long j);

    @Nullable
    private native List<String> getDisableMUCSettingsImpl(long j);

    private native boolean getDndSettingsImpl(long j, int[] iArr);

    private native boolean getFollowedThreadNotifySettingImpl(long j);

    @Nullable
    private native List<String> getHLMUCSettingsImpl(long j);

    private native int getHintLineForChannelsImpl(long j);

    private native boolean getHistoryDNDSettingImpl(long j, int[] iArr);

    private native boolean getInCallSettingsImpl(long j);

    @Nullable
    private native List<String> getKeywordSettingImpl(long j);

    @Nullable
    private native byte[] getMUCDiffFromGeneralSettingImpl(long j);

    @Nullable
    private native byte[] getMUCSettingsImpl(long j);

    @Nullable
    private native List<String> getPersonSettingImpl(long j);

    @Nullable
    private native List<String> getReceiveAllMUCSettingsImpl(long j);

    private native boolean getSnoozeSettingsImpl(long j, long[] jArr);

    private native boolean isInDNDImpl(long j);

    private native boolean isMsgBlockedImpl(long j, String str, String str2, int i);

    private native boolean isSessionBlockedImpl(long j, String str);

    private native boolean keepAllUnreadChannelOnTopImpl(long j);

    private native void registerUICallBackImpl(long j, long j2);

    private native boolean resetMUCSettingsImpl(long j, List<String> list);

    private native boolean sessionShowUnreadBadgeImpl(long j, String str);

    private native boolean setHintLineForChannelsImpl(long j, int i);

    private native boolean setKeepAllUnreadChannelOnTopImpl(long j, boolean z);

    private native boolean setShowUnreadBadgeImpl(long j, String str, boolean z);

    private native boolean setShowUnreadForChannelsImpl(long j, boolean z);

    private native boolean showUnreadForChannelsImpl(long j);

    public NotificationSettingMgr(long j) {
        this.mNativeHandle = j;
    }

    public void registerUICallBack(@Nullable NotificationSettingUI notificationSettingUI) {
        long j = this.mNativeHandle;
        if (j != 0 && notificationSettingUI != null) {
            registerUICallBackImpl(j, notificationSettingUI.getNativeHandle());
        }
    }

    public boolean applyBlockAllSettings(int i, int i2, int i3) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return applyBlockAllSettingsImpl(j, i, i2, i3);
    }

    @Nullable
    public int[] getBlockAllSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        int[] iArr = new int[3];
        if (getBlockAllSettingsImpl(j, iArr)) {
            return iArr;
        }
        return null;
    }

    public boolean applySnoozeSettings(long j, long j2, long j3) {
        long j4 = this.mNativeHandle;
        if (j4 == 0) {
            return false;
        }
        return applySnoozeSettingsImpl(j4, j, j2, j3);
    }

    @Nullable
    public long[] getSnoozeSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long[] jArr = new long[3];
        if (getSnoozeSettingsImpl(j, jArr)) {
            return jArr;
        }
        return null;
    }

    public boolean applyDndSettings(@Nullable DndSetting dndSetting) {
        long j = this.mNativeHandle;
        if (j == 0 || dndSetting == null) {
            return false;
        }
        return applyDndSettingsImpl(j, dndSetting.params[0] == 1, dndSetting.params[1], dndSetting.params[2], dndSetting.params[3], dndSetting.params[4]);
    }

    @Nullable
    public DndSetting getDndSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        int[] iArr = new int[5];
        if (getDndSettingsImpl(j, iArr)) {
            return new DndSetting(iArr);
        }
        return null;
    }

    @Nullable
    public List<String> getDisableMUCSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getDisableMUCSettingsImpl(j);
    }

    @Nullable
    public List<String> getReceiveAllMUCSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getReceiveAllMUCSettingsImpl(j);
    }

    public boolean applyMUCSettings(String str, int i) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        return applyMUCSettings((List<String>) arrayList, i);
    }

    public boolean applyMUCSettings(List<String> list, int i) {
        if (this.mNativeHandle == 0 || CollectionsUtil.isListEmpty(list)) {
            return false;
        }
        return applyMUCSettingsImpl(this.mNativeHandle, list, i);
    }

    @Nullable
    public List<String> getHLMUCSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getHLMUCSettingsImpl(j);
    }

    @Nullable
    public MUCNotifySettings getMUCSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] mUCSettingsImpl = getMUCSettingsImpl(j);
        if (mUCSettingsImpl == null) {
            return null;
        }
        try {
            return MUCNotifySettings.parseFrom(mUCSettingsImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public MUCNotifySettings getMUCDiffFromGeneralSetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] mUCDiffFromGeneralSettingImpl = getMUCDiffFromGeneralSettingImpl(j);
        if (mUCDiffFromGeneralSettingImpl == null) {
            return null;
        }
        try {
            return MUCNotifySettings.parseFrom(mUCDiffFromGeneralSettingImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean isSessionBlocked(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSessionBlockedImpl(j, str);
    }

    public boolean isMsgBlocked(String str, String str2, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMsgBlockedImpl(j, str, str2, i);
    }

    public boolean applyInCallSettings(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return applyInCallSettingsImpl(j, z);
    }

    public boolean getInCallSettings() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return getInCallSettingsImpl(j);
    }

    @Nullable
    public DndSetting getHistoryDNDSetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        int[] iArr = new int[5];
        if (!getHistoryDNDSettingImpl(j, iArr)) {
            return null;
        }
        if (iArr[1] == 0 && iArr[2] == 0 && iArr[3] == 0 && iArr[4] == 0) {
            return null;
        }
        return new DndSetting(iArr);
    }

    public int getDNDNowSetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getDNDNowSettingImpl(j);
    }

    public boolean applyDNDNowSetting(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return applyDNDNowSettingImpl(j, i);
    }

    public boolean resetMUCSettings(List<String> list) {
        if (this.mNativeHandle == 0 || CollectionsUtil.isListEmpty(list)) {
            return false;
        }
        return resetMUCSettingsImpl(this.mNativeHandle, list);
    }

    @Nullable
    public List<String> getKeywordSetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getKeywordSettingImpl(j);
    }

    public boolean applyKeyword(@Nullable List<String> list, @Nullable List<String> list2) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        if ((list == null || list.isEmpty()) && (list2 == null || list2.isEmpty())) {
            return false;
        }
        return applyKeywordSettingImpl(this.mNativeHandle, list, list2);
    }

    @Nullable
    public List<String> getPersonSetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPersonSettingImpl(j);
    }

    public boolean applyPersonSetting(@Nullable List<String> list, @Nullable List<String> list2) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        if ((list == null || list.isEmpty()) && (list2 == null || list2.isEmpty())) {
            return false;
        }
        return applyPersonSettingImpl(this.mNativeHandle, list, list2);
    }

    public boolean isInDND() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInDNDImpl(j);
    }

    public boolean setShowUnreadForChannels(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setShowUnreadForChannelsImpl(j, z);
    }

    public boolean showUnreadForChannels() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return showUnreadForChannelsImpl(j);
    }

    public boolean setShowUnreadBadge(String str, boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setShowUnreadBadgeImpl(j, str, z);
    }

    public boolean sessionShowUnreadBadge(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return sessionShowUnreadBadgeImpl(j, str);
    }

    public boolean setKeepAllUnreadChannelOnTop(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setKeepAllUnreadChannelOnTopImpl(j, z);
    }

    public boolean keepAllUnreadChannelOnTop() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return keepAllUnreadChannelOnTopImpl(j);
    }

    public boolean setHintLineForChannels(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setHintLineForChannelsImpl(j, i);
    }

    public int getHintLineForChannels() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 1;
        }
        return getHintLineForChannelsImpl(j);
    }

    public boolean applyFollowedThreadNotifySetting(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return applyFollowedThreadNotifySettingImpl(j, z);
    }

    public boolean getFollowedThreadNotifySetting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return getFollowedThreadNotifySettingImpl(j);
    }
}
