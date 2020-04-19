package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IConfService.Stub;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfIPCPort;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.delegate.PTUIDelegation;
import com.zipow.videobox.util.AppStateMonitor;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.FloatWindow;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import p021us.zoom.androidlib.app.ForegroundTaskManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.ZMLog;

public class ConfService extends ZMBaseService {
    public static final String ARGS_EXTRA = "args";
    public static final String ARG_COMMAND_LINE = "commandLine";
    public static final String ARG_COMMAND_TYPE = "commandType";
    public static final String ARG_CONF_NUMBER = "confno";
    public static final String ARG_SCREEN_NAME = "screenName";
    public static final int COMMAND_JOIN_BY_ID = 1;
    public static final int COMMAND_JOIN_BY_URL = 2;
    /* access modifiers changed from: private */
    public static final String TAG = "ConfService";
    private boolean mIsCommandLineExecuted = false;

    private static class ServiceBinder extends Stub {
        @NonNull
        private Handler mHandler = new Handler();

        public void onSipCallEvent(int i, String str) throws RemoteException {
        }

        public void onSipStatusEvent(boolean z) throws RemoteException {
        }

        public void sendMessage(byte[] bArr) throws RemoteException {
            ConfIPCPort.getInstance().onMessageReceived(bArr);
        }

        public boolean isConfAppAtFront() throws RemoteException {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            return frontActivity != null && frontActivity.isActive();
        }

        public void sinkPTAppEvent(final int i, final long j) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().dispatchPTAppEvent(i, j);
                }
            });
        }

        public void sinkDataNetworkStatusChanged(final boolean z) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().sinkNetworkState(z);
                }
            });
        }

        public void sinkIMLocalStatusChanged(final int i) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().sinkIMLocalStatusChanged(i);
                }
            });
        }

        public void sinkIMReceived(@NonNull final byte[] bArr) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().sinkIMReceived(bArr);
                }
            });
        }

        public void sinkIMBuddyPresence(@NonNull final byte[] bArr) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().sinkIMBuddyPresence(bArr);
                }
            });
        }

        public void sinkIMBuddyPic(@NonNull final byte[] bArr) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().sinkIMBuddyPic(bArr);
                }
            });
        }

        public void sinkIMBuddySort() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().sinkIMBuddySort();
                }
            });
        }

        public void sinkPTAppCustomEvent(final int i, final long j) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().dispatchPTAppCustomEvent(i, j);
                }
            });
        }

        public void sinkPTPresentToRoomEvent(final int i) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().sinkPTPresentToRoomEvent(i);
                }
            });
        }

        public void sinkPTCommonEvent(final int i, final byte[] bArr) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUIDelegation.getInstance().dispatchPTCommonEvent(i, bArr);
                }
            });
        }

        public void onPTUIMoveToFront(final String str) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ForegroundTaskManager.getInstance().onAnotherProcessMoveToFront(str);
                    AppStateMonitor.getInstance().onPTUIMoveToFront();
                }
            });
        }

        public void onPTUIMoveToBackground() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    AppStateMonitor.getInstance().onPTUIMoveToBackground();
                }
            });
        }

        public void leaveCurrentMeeting(final boolean z) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ConfMgr instance = ConfMgr.getInstance();
                    CmmConfStatus confStatusObj = instance.getConfStatusObj();
                    if (confStatusObj == null || !z || !confStatusObj.isHost()) {
                        instance.leaveConference();
                    } else {
                        instance.endConference();
                    }
                }
            });
        }

        public void pauseCurrentMeeting() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ConfUI.getInstance().pauseAudio();
                }
            });
        }

        public void resumeCurrentMeeting() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ConfUI.getInstance().resumeAudio();
                }
            });
        }

        public boolean isCurrentMeetingLocked() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                    if (confStatusObj == null || !confStatusObj.isConfLocked()) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(true);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean isCurrentMeetingHost() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    CmmUser myself = ConfMgr.getInstance().getMyself();
                    if (myself == null || !myself.isHost()) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(true);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean startCallOut(final String str) throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                    if (confStatusObj == null || !confStatusObj.startCallOut(str)) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(true);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean hangUpCallOut() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                    if (confStatusObj == null || !confStatusObj.hangUp()) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(true);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean isCallOutInProgress() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                    return Boolean.valueOf(confStatusObj != null && confStatusObj.isCallOutInProgress());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public int getCallMeStatus() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Integer>() {
                public Integer call() throws Exception {
                    CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                    if (confStatusObj != null) {
                        return Integer.valueOf(confStatusObj.getCallMeStatus());
                    }
                    return Integer.valueOf(0);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Integer) futureTask.get()).intValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return 0;
            }
        }

        public boolean isCallOutSupported() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                    boolean z = false;
                    if (confContext == null) {
                        return Boolean.valueOf(false);
                    }
                    MeetingInfoProto meetingItem = confContext.getMeetingItem();
                    if (meetingItem == null) {
                        return Boolean.valueOf(false);
                    }
                    if (meetingItem.getSupportCallOutType() != 0 && !meetingItem.getTelephonyOff()) {
                        z = true;
                    }
                    return Boolean.valueOf(z);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean isInviteRoomSystemSupported() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                    if (confContext == null) {
                        return Boolean.valueOf(false);
                    }
                    MeetingInfoProto meetingItem = confContext.getMeetingItem();
                    if (meetingItem == null) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(meetingItem.getIsH323Enabled());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public void onRoomCallEvent(int i, long j, boolean z) throws RemoteException {
            Handler handler = this.mHandler;
            final int i2 = i;
            final long j2 = j;
            final boolean z2 = z;
            C204124 r1 = new Runnable() {
                public void run() {
                    ConfUI.getInstance().onRoomSystemCallStatus(i2, j2, z2);
                }
            };
            handler.post(r1);
        }

        public void onNewIncomingCallCanceled(final long j) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ConfUI.getInstance().onNewIncomingCallCanceled(j);
                }
            });
        }

        public boolean tryRetrieveMicrophone() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    ConfUI.getInstance().tryTurnOnAudioSession();
                    boolean tryRetrieveMicrophone = ConfUI.getInstance().tryRetrieveMicrophone();
                    ConfUI.getInstance().checkOpenLoudSpeaker();
                    return Boolean.valueOf(tryRetrieveMicrophone);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean onAlertWhenAvailable(String str, @NonNull String str2, String str3, boolean z, String str4) throws RemoteException {
            ZMActivity frontActivity = ConfActivity.getFrontActivity();
            if (frontActivity == null || !frontActivity.isActive()) {
                return false;
            }
            FloatWindow.getInstance().checkToShowFloatWindow(frontActivity, str, str2, str3, z, str4);
            return true;
        }

        public void dispacthPtLoginResultEvent(final boolean z, final String str, final String str2) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ConfUI.getInstance().onPtLoginResultEvent(z, str, str2);
                }
            });
        }

        public boolean notifyPTStartLogin(final String str) {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(ConfMgr.getInstance().notifyPTStartLogin(str));
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean disableConfAudio() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() {
                    return Boolean.valueOf(ConfLocalHelper.disconnectAudioAndMic());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(ConfService.TAG, e, "", new Object[0]);
                return false;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    /* access modifiers changed from: protected */
    public void doJoinById(@NonNull Bundle bundle) {
        bundle.getLong("confno");
        String string = bundle.getString("screenName");
    }

    /* access modifiers changed from: protected */
    public void doJoinByUrl(@NonNull Bundle bundle) {
        String string = bundle.getString("screenName");
        if (string == null) {
            string = "";
        }
        ConfMgr.getInstance().onUserConfirmToJoin(true, string);
    }

    private void doCommandLine(String str) {
        if (!this.mIsCommandLineExecuted) {
            Mainboard mainboard = Mainboard.getMainboard();
            if (mainboard != null && !mainboard.isInitialized()) {
                try {
                    VideoBoxApplication.getInstance().initConfMainboard(str);
                    this.mIsCommandLineExecuted = true;
                } catch (UnsatisfiedLinkError unused) {
                    MeetingEndMessageActivity.showDeviceNotSupported(VideoBoxApplication.getNonNullInstance());
                }
            }
        }
    }

    public void onCreate() {
        this.mIsNeedCheckStartService = false;
        super.onCreate();
        if (OsUtil.isAtLeastO()) {
            showConfNotification();
        }
        if (VideoBoxApplication.getInstance() == null) {
            Context applicationContext = getApplicationContext();
            if (applicationContext instanceof ZoomApplication) {
                VideoBoxApplication.initialize(applicationContext, false, 1, null);
            } else {
                VideoBoxApplication.initialize(applicationContext, true, 1, "zoom_meeting");
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        ConfMgr.getInstance().leaveConference();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (OsUtil.isAtLeastO()) {
            showConfNotification();
        }
        super.onStartCommand(intent, i, i2);
        doCommand(intent);
        return 2;
    }

    private void doCommand(@Nullable Intent intent) {
        if (intent != null) {
            Bundle bundleExtra = intent.getBundleExtra(ARGS_EXTRA);
            if (bundleExtra != null) {
                String string = bundleExtra.getString(ARG_COMMAND_LINE);
                if (string != null) {
                    doCommandLine(string);
                } else if (bundleExtra.getInt(ARG_COMMAND_TYPE) == 1) {
                    doJoinById(bundleExtra);
                } else if (bundleExtra.getInt(ARG_COMMAND_TYPE) == 2) {
                    doJoinByUrl(bundleExtra);
                }
            }
        }
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
