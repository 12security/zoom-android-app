package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTUI.IMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.ZmPtUtils;
import java.util.HashSet;
import java.util.Iterator;

public class ZMPTIMeetingMgr implements IMeetingMgrListener, IPTUIListener {
    @NonNull
    private static final String TAG = "com.zipow.videobox.ptapp.ZMPTIMeetingMgr";
    @Nullable
    private static ZMPTIMeetingMgr instance;
    private int mCountMeetingMgrListener = 0;
    private int mCountPTUIListener = 0;
    @NonNull
    private HashSet<IMeetingStatusListener> mIMeetingStatusListeners = new HashSet<>();
    @NonNull
    private HashSet<IPTUIStatusListener> mIPTUIStatusListeners = new HashSet<>();
    private boolean mIsPullingCalendarIntegrationConfig = false;
    private boolean mIsPullingCloudMeetings = false;

    public interface IMeetingStatusListener {
        void onMeetingListLoadDone(SourceMeetingList sourceMeetingList);
    }

    public interface IPTUIStatusListener {
        void onCalendarConfigReady(long j);

        void onCallStatusChanged(long j);

        void onRefreshMyNotes();

        void onWebLogin(long j);
    }

    public enum SourceMeetingList {
        CLOUD,
        Calendar
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onDeleteMeetingResult(int i) {
    }

    public void onPMIEvent(int i, int i2, MeetingInfoProto meetingInfoProto) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onScheduleMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str) {
    }

    public void onStartFailBeforeLaunch(int i) {
    }

    public void onUpdateMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str) {
    }

    @NonNull
    public static synchronized ZMPTIMeetingMgr getInstance() {
        ZMPTIMeetingMgr zMPTIMeetingMgr;
        synchronized (ZMPTIMeetingMgr.class) {
            if (instance == null) {
                instance = new ZMPTIMeetingMgr();
            }
            zMPTIMeetingMgr = instance;
        }
        return zMPTIMeetingMgr;
    }

    private ZMPTIMeetingMgr() {
    }

    public void addMySelfToPTUIListener() {
        this.mCountPTUIListener++;
        PTUI.getInstance().addPTUIListener(this);
    }

    public void removeMySelfFromPTUIListener() {
        this.mCountPTUIListener--;
        if (this.mCountPTUIListener <= 0) {
            PTUI.getInstance().removePTUIListener(this);
        }
    }

    public void addMySelfToMeetingMgrListener() {
        this.mCountMeetingMgrListener++;
        PTUI.getInstance().addMeetingMgrListener(this);
    }

    public void removeMySelfFromMeetingMgrListener() {
        this.mCountMeetingMgrListener--;
        if (this.mCountMeetingMgrListener <= 0) {
            PTUI.getInstance().removeMeetingMgrListener(this);
        }
    }

    public void addIMeetingStatusListener(@NonNull IMeetingStatusListener iMeetingStatusListener) {
        this.mIMeetingStatusListeners.add(iMeetingStatusListener);
    }

    public void removeIMeetingStatusListener(@NonNull IMeetingStatusListener iMeetingStatusListener) {
        this.mIMeetingStatusListeners.remove(iMeetingStatusListener);
    }

    public void addIPTUIStatusListener(@NonNull IPTUIStatusListener iPTUIStatusListener) {
        this.mIPTUIStatusListeners.add(iPTUIStatusListener);
    }

    public void removeIPTUIStatusListener(@NonNull IPTUIStatusListener iPTUIStatusListener) {
        this.mIPTUIStatusListeners.remove(iPTUIStatusListener);
    }

    public void setmIsPullingCalendarIntegrationConfig(boolean z) {
        this.mIsPullingCalendarIntegrationConfig = z;
    }

    public void setmIsPullingCloudMeetings(boolean z) {
        this.mIsPullingCloudMeetings = z;
    }

    public void clearPullingFlags() {
        this.mIsPullingCalendarIntegrationConfig = false;
        this.mIsPullingCloudMeetings = false;
    }

    public void pullCalendarIntegrationConfig() {
        if (!this.mIsPullingCalendarIntegrationConfig) {
            this.mIsPullingCalendarIntegrationConfig = true;
            PTApp.getInstance().getCalendarIntegrationConfig();
        }
    }

    public boolean pullCloudMeetings() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper == null || this.mIsPullingCloudMeetings) {
            return false;
        }
        this.mIsPullingCloudMeetings = true;
        return meetingHelper.listMeetingUpcoming();
    }

    private boolean pullCalendarMeetings() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null && ZmPtUtils.isCalendarServiceReady()) {
            return meetingHelper.listCalendarEvents();
        }
        return false;
    }

    public void onListMeetingResult(int i) {
        if (!ZmPtUtils.isCalendarServiceReady()) {
            onLoadDone(SourceMeetingList.CLOUD);
        } else if (!pullCalendarMeetings()) {
            onLoadDone(SourceMeetingList.CLOUD);
        }
    }

    public void onListCalendarEventsResult(int i) {
        onLoadDone(SourceMeetingList.Calendar);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            onWebLogin(j);
        } else if (i == 9 || i == 12) {
            onRefreshMyNotes();
        } else if (i == 22) {
            onCallStatusChanged(j);
        } else if (i == 68) {
            onCalendarConfigReady(j);
        }
    }

    private void onLoadDone(SourceMeetingList sourceMeetingList) {
        this.mIsPullingCalendarIntegrationConfig = false;
        this.mIsPullingCloudMeetings = false;
        Iterator it = this.mIMeetingStatusListeners.iterator();
        while (it.hasNext()) {
            ((IMeetingStatusListener) it.next()).onMeetingListLoadDone(sourceMeetingList);
        }
    }

    private void onCallStatusChanged(long j) {
        Iterator it = this.mIPTUIStatusListeners.iterator();
        while (it.hasNext()) {
            ((IPTUIStatusListener) it.next()).onCallStatusChanged(j);
        }
    }

    private void onWebLogin(long j) {
        Iterator it = this.mIPTUIStatusListeners.iterator();
        while (it.hasNext()) {
            ((IPTUIStatusListener) it.next()).onWebLogin(j);
        }
    }

    private void onCalendarConfigReady(long j) {
        Iterator it = this.mIPTUIStatusListeners.iterator();
        while (it.hasNext()) {
            ((IPTUIStatusListener) it.next()).onCalendarConfigReady(j);
        }
    }

    private void onRefreshMyNotes() {
        Iterator it = this.mIPTUIStatusListeners.iterator();
        while (it.hasNext()) {
            ((IPTUIStatusListener) it.next()).onRefreshMyNotes();
        }
    }
}
