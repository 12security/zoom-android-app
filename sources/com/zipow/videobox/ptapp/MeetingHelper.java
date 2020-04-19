package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTAppProtos.GoogCalendarEventList;
import java.util.List;
import java.util.TimeZone;
import p021us.zoom.androidlib.util.StringUtil;

public class MeetingHelper {
    private static final String TAG = "MeetingHelper";
    private long mNativeHandle = 0;

    private native boolean alwaysMobileVideoOnImpl(long j);

    private native boolean alwaysUsePMIImpl(long j);

    private native boolean callOutRoomSystemImpl(long j, String str, int i, int i2);

    private native boolean cancelRoomDeviceImpl(long j);

    private native void checkIfNeedToListUpcomingMeetingImpl(long j);

    private native boolean clearAllHistoriesImpl(long j);

    private native boolean createIcsFileFromMeetingImpl(long j, byte[] bArr, String[] strArr, String str);

    private native boolean deleteHistoryImpl(long j, long j2);

    private native boolean deleteMeetingImpl(long j, long j2);

    private native boolean editMeetingImpl(long j, byte[] bArr, String str);

    private native byte[] getCalendarEventsImpl(long j);

    @Nullable
    private native byte[] getEWSCalEventsImpl(long j);

    private native int getFilteredMeetingCountImpl(long j);

    @Nullable
    private native byte[] getFilteredMeetingItemByIndexImpl(long j, int i);

    @Nullable
    private native byte[] getGoogleCalEventsImpl(long j);

    private native long getHistoryAtIndexImpl(long j, int i);

    private native long getHistoryByIDImpl(long j, long j2);

    private native int getHistoryCountImpl(long j);

    private native boolean getMeetingInfoImpl(long j, String str, String str2);

    @Nullable
    private native byte[] getMeetingItemDataByID(long j, String str);

    @Nullable
    private native byte[] getMeetingItemDataByNumber(long j, long j2);

    private native boolean getMeetingStatusImpl(long j, String str, String str2);

    private native byte[] getOutlookCalendarEventsImpl(long j);

    private native boolean getRoomDevicesImpl(long j, List<RoomDevice> list);

    private native boolean isLoadingMeetingListImpl(long j);

    private native boolean listCalendarEventsImpl(long j);

    private native boolean listEWSCalendarEventsImpl(long j);

    private native boolean listGoogleCalendarEventsImpl(long j);

    private native boolean listMeetingUpcomingImpl(long j);

    private native boolean listOutlookCalendarEventsImpl(long j);

    private native boolean modifyPMIImpl(long j, long j2, long j3);

    private native boolean needFilterOutCalendarEventsImpl(long j);

    private native boolean scheduleMeetingImpl(long j, byte[] bArr, String str, String str2);

    private native boolean sendMeetingParingCodeImpl(long j, long j2, String str);

    private native boolean setAlwaysMobileVideoOnImpl(long j, boolean z);

    private native boolean setAlwaysUsePMIImpl(long j, boolean z);

    private native void setFilterPersonImpl(long j, String str);

    public MeetingHelper(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public MeetingInfoProto getMeetingItemByID(String str) {
        byte[] meetingItemDataByID = getMeetingItemDataByID(this.mNativeHandle, str);
        MeetingInfoProto meetingInfoProto = null;
        if (meetingItemDataByID == null || meetingItemDataByID.length == 0) {
            return null;
        }
        try {
            meetingInfoProto = MeetingInfoProto.parseFrom(meetingItemDataByID);
        } catch (InvalidProtocolBufferException unused) {
        }
        return meetingInfoProto;
    }

    @Nullable
    public MeetingInfoProto getMeetingItemByNumber(long j) {
        byte[] meetingItemDataByNumber = getMeetingItemDataByNumber(this.mNativeHandle, j);
        MeetingInfoProto meetingInfoProto = null;
        if (meetingItemDataByNumber == null || meetingItemDataByNumber.length == 0) {
            return null;
        }
        try {
            meetingInfoProto = MeetingInfoProto.parseFrom(meetingItemDataByNumber);
        } catch (InvalidProtocolBufferException unused) {
        }
        return meetingInfoProto;
    }

    @Nullable
    public MeetingInfoProto getPmiMeetingItem() {
        int filteredMeetingCount = getFilteredMeetingCount();
        for (int i = 0; i < filteredMeetingCount; i++) {
            MeetingInfoProto filteredMeetingItemByIndex = getFilteredMeetingItemByIndex(i);
            if (filteredMeetingItemByIndex != null && filteredMeetingItemByIndex.getExtendMeetingType() == 1) {
                return filteredMeetingItemByIndex;
            }
        }
        return null;
    }

    public boolean scheduleMeeting(@Nullable MeetingInfoProto meetingInfoProto, @Nullable String str, @Nullable String str2) {
        if (meetingInfoProto == null) {
            return false;
        }
        return scheduleMeetingImpl(this.mNativeHandle, meetingInfoProto.toByteArray(), str == null ? TimeZone.getDefault().getID() : str, str2 == null ? "" : str2);
    }

    public boolean editMeeting(@Nullable MeetingInfoProto meetingInfoProto, @Nullable String str) {
        if (meetingInfoProto == null) {
            return false;
        }
        if (str == null) {
            str = TimeZone.getDefault().getID();
        }
        return editMeetingImpl(this.mNativeHandle, meetingInfoProto.toByteArray(), str);
    }

    public boolean deleteMeeting(long j) {
        return deleteMeetingImpl(this.mNativeHandle, j);
    }

    public boolean getMeetingInfo(String str, String str2) {
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return false;
        }
        return getMeetingInfoImpl(this.mNativeHandle, str, str2);
    }

    public boolean getMeetingStatus(String str, @Nullable String str2) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        if (str2 == null) {
            str2 = "";
        }
        return getMeetingStatusImpl(this.mNativeHandle, str, str2);
    }

    public boolean createIcsFileFromMeeting(@Nullable MeetingInfoProto meetingInfoProto, @Nullable String[] strArr, String str) {
        if (meetingInfoProto == null || strArr == null || strArr.length == 0) {
            return false;
        }
        return createIcsFileFromMeetingImpl(this.mNativeHandle, meetingInfoProto.toByteArray(), strArr, str);
    }

    public boolean listMeetingUpcoming() {
        return listMeetingUpcomingImpl(this.mNativeHandle);
    }

    public boolean isLoadingMeetingList() {
        return isLoadingMeetingListImpl(this.mNativeHandle);
    }

    public boolean deleteHistory(long j) {
        return deleteHistoryImpl(this.mNativeHandle, j);
    }

    public boolean clearAllHistories() {
        return clearAllHistoriesImpl(this.mNativeHandle);
    }

    public int getHistoryCount() {
        return getHistoryCountImpl(this.mNativeHandle);
    }

    @Nullable
    public MeetingHistory getHistoryAtIndex(int i) {
        long historyAtIndexImpl = getHistoryAtIndexImpl(this.mNativeHandle, i);
        if (historyAtIndexImpl == 0) {
            return null;
        }
        return new MeetingHistory(historyAtIndexImpl);
    }

    @Nullable
    public MeetingHistory getHistoryByID(long j) {
        long historyByIDImpl = getHistoryByIDImpl(this.mNativeHandle, j);
        if (historyByIDImpl == 0) {
            return null;
        }
        return new MeetingHistory(historyByIDImpl);
    }

    public boolean modifyPMI(long j, long j2) {
        long j3 = this.mNativeHandle;
        if (j3 == 0) {
            return false;
        }
        return modifyPMIImpl(j3, j, j2);
    }

    public void checkIfNeedToListUpcomingMeeting() {
        long j = this.mNativeHandle;
        if (j != 0) {
            checkIfNeedToListUpcomingMeetingImpl(j);
        }
    }

    public boolean alwaysUsePMI() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return alwaysUsePMIImpl(j);
    }

    public boolean setAlwaysUsePMI(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setAlwaysUsePMIImpl(j, z);
    }

    public boolean alwaysMobileVideoOn() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return alwaysMobileVideoOnImpl(j);
    }

    public boolean setAlwaysMobileVideoOn(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setAlwaysMobileVideoOnImpl(j, z);
    }

    public boolean callOutRoomSystem(String str, int i, int i2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return callOutRoomSystemImpl(j, str, i, i2);
    }

    public boolean getRoomDevices(@Nullable List<RoomDevice> list) {
        long j = this.mNativeHandle;
        if (j == 0 || list == null) {
            return false;
        }
        return getRoomDevicesImpl(j, list);
    }

    public boolean cancelRoomDevice() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return cancelRoomDeviceImpl(j);
    }

    public boolean sendMeetingParingCode(long j, String str) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return sendMeetingParingCodeImpl(j2, j, str);
    }

    public boolean listCalendarEvents() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null || !currentUserProfile.canAccessOutlookExchange()) {
            return listGoogleCalendarEventsImpl(this.mNativeHandle);
        }
        return listOutlookCalendarEventsImpl(this.mNativeHandle);
    }

    @Nullable
    public GoogCalendarEventList getCalendarEvents() {
        byte[] bArr;
        GoogCalendarEventList googCalendarEventList = null;
        if (this.mNativeHandle == 0) {
            return null;
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null || !currentUserProfile.canAccessOutlookExchange()) {
            bArr = getGoogleCalEventsImpl(this.mNativeHandle);
        } else {
            bArr = getOutlookCalendarEventsImpl(this.mNativeHandle);
        }
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        try {
            googCalendarEventList = GoogCalendarEventList.parseFrom(bArr);
        } catch (InvalidProtocolBufferException unused) {
        }
        return googCalendarEventList;
    }

    public void setFilterPerson(String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setFilterPersonImpl(j, str);
        }
    }

    public int getFilteredMeetingCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFilteredMeetingCountImpl(j);
    }

    @Nullable
    public MeetingInfoProto getFilteredMeetingItemByIndex(int i) {
        long j = this.mNativeHandle;
        MeetingInfoProto meetingInfoProto = null;
        if (j == 0) {
            return null;
        }
        byte[] filteredMeetingItemByIndexImpl = getFilteredMeetingItemByIndexImpl(j, i);
        if (filteredMeetingItemByIndexImpl == null || filteredMeetingItemByIndexImpl.length == 0) {
            return null;
        }
        try {
            meetingInfoProto = MeetingInfoProto.parseFrom(filteredMeetingItemByIndexImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return meetingInfoProto;
    }

    public boolean needFilterOutCalendarEvents() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return needFilterOutCalendarEventsImpl(j);
    }
}
