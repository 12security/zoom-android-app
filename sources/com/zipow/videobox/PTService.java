package com.zipow.videobox;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IPTService.Stub;
import com.zipow.videobox.confapp.p009bo.BOStatusChangeMgrOnPT;
import com.zipow.videobox.fragment.ForceUpdateDialogFragment;
import com.zipow.videobox.fragment.RateZoomDialogFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.FavoriteMgr;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.PT4SIPIPCPort;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTBuddyHelper;
import com.zipow.videobox.ptapp.PTIPCPort;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.AppStateMonitor;
import com.zipow.videobox.util.FloatWindow;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import p021us.zoom.androidlib.app.ForegroundTaskManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;

public class PTService extends ZMBaseService {
    public static final String ACTION_DEAMON;
    public static final String ACTION_REMOVE_CONF_NOTIFICATION;
    public static final String ACTION_REMOVE_SIP_NOTIFICATION;
    public static final String ACTION_SHOW_CONF_NOTIFICATION;
    public static final String ACTION_SHOW_SIP_NOTIFICATION;
    public static final String ACTION_START_FOREGROUND;
    public static final String ACTION_STOP_FOREGROUND;
    public static final String ARG_IN_MEETING = "in_meeting";
    public static final String FILE_NAME = "alert_available";
    /* access modifiers changed from: private */
    public static final String TAG = "PTService";
    private boolean mIsInForeground = false;
    private boolean mIsInMeeting = false;
    private boolean mIsInSIP = false;
    private PTBroadcastReceiver mReceiver;

    private class PTBroadcastReceiver extends BroadcastReceiver {
        private PTBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (PTService.ACTION_REMOVE_SIP_NOTIFICATION.equals(action)) {
                PTService.this.removeSipNotification();
            } else if (PTService.ACTION_REMOVE_CONF_NOTIFICATION.equals(action)) {
                PTService.this.removeConfNotification();
            }
        }
    }

    private static class ServiceBinder extends Stub {
        @NonNull
        private Handler mHandler = new Handler();

        public void sendMessage(byte[] bArr) throws RemoteException {
            PTIPCPort.getInstance().onMessageReceived(bArr);
        }

        public boolean isSignedIn() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(PTApp.getInstance().isWebSignedOn());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean isIMSignedIn() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    IMHelper iMHelper = PTApp.getInstance().getIMHelper();
                    return Boolean.valueOf(iMHelper != null && iMHelper.isIMSignedOn());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean isPTAppAtFront() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                @NonNull
                public Boolean call() throws Exception {
                    ZMActivity frontActivity = ZMActivity.getFrontActivity();
                    return Boolean.valueOf(frontActivity != null && frontActivity.isActive());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public int inviteBuddiesToConf(String[] strArr, String[] strArr2, String str, long j, String str2) throws RemoteException {
            final String[] strArr3 = strArr;
            final String[] strArr4 = strArr2;
            final String str3 = str;
            final long j2 = j;
            final String str4 = str2;
            C21504 r1 = new Callable<Integer>() {
                public Integer call() throws Exception {
                    return Integer.valueOf(PTApp.getInstance().inviteBuddiesToConf(strArr3, strArr4, str3, j2, str4));
                }
            };
            FutureTask futureTask = new FutureTask(r1);
            this.mHandler.post(futureTask);
            try {
                return ((Integer) futureTask.get()).intValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return -1;
            }
        }

        public int getBuddyItemCount() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Integer>() {
                public Integer call() throws Exception {
                    PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
                    if (buddyHelper == null) {
                        return Integer.valueOf(0);
                    }
                    return Integer.valueOf(buddyHelper.getBuddyItemCount());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Integer) futureTask.get()).intValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return 0;
            }
        }

        public void reloadAllBuddyItems() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    IConfService confService = VideoBoxApplication.getInstance().getConfService();
                    if (confService != null) {
                        try {
                            PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
                            if (buddyHelper != null) {
                                int buddyItemCount = buddyHelper.getBuddyItemCount();
                                for (int i = 0; i < buddyItemCount; i++) {
                                    confService.sinkIMBuddyPresence(buddyHelper.getBuddyItemData(i));
                                }
                            }
                        } catch (RemoteException unused) {
                        }
                    }
                }
            });
        }

        @Nullable
        public String[] filterBuddyWithInput(final String str) throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<String[]>() {
                @Nullable
                public String[] call() throws Exception {
                    PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
                    if (buddyHelper == null) {
                        return null;
                    }
                    return buddyHelper.filterBuddyWithInput(str);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return (String[]) futureTask.get();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return null;
            }
        }

        public void showNeedUpdate() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ZMActivity frontActivity = ZMActivity.getFrontActivity();
                    if (!(frontActivity instanceof IMActivity) || !frontActivity.isActive()) {
                        IMActivity.showStartCallFailedNeedUpdateOnResume();
                        if (frontActivity != null) {
                            IMActivity.show(frontActivity);
                            return;
                        }
                        Intent intent = new Intent(VideoBoxApplication.getInstance(), IMActivity.class);
                        intent.addFlags(268566528);
                        ActivityStartHelper.startActivityForeground(VideoBoxApplication.getInstance(), intent);
                        return;
                    }
                    ForceUpdateDialogFragment.show(frontActivity.getSupportFragmentManager());
                }
            });
        }

        public int getPTLoginType() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Integer>() {
                public Integer call() throws Exception {
                    return Integer.valueOf(PTApp.getInstance().getPTLoginType());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Integer) futureTask.get()).intValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return 102;
            }
        }

        @Nullable
        public String FavoriteMgr_getLocalPicturePath(final String str) throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<String>() {
                @Nullable
                public String call() throws Exception {
                    FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
                    if (favoriteMgr == null) {
                        return null;
                    }
                    return favoriteMgr.getLocalPicturePath(str);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return (String) futureTask.get();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return null;
            }
        }

        @Nullable
        public byte[] FavoriteMgr_getFavoriteListWithFilter(final String str) throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<byte[]>() {
                /* JADX WARNING: Code restructure failed: missing block: B:32:0x004a, code lost:
                    r2 = th;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:33:0x004b, code lost:
                    r3 = null;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:37:0x004f, code lost:
                    r3 = move-exception;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:38:0x0050, code lost:
                    r5 = r3;
                    r3 = r2;
                    r2 = r5;
                 */
                /* JADX WARNING: Failed to process nested try/catch */
                /* JADX WARNING: Removed duplicated region for block: B:32:0x004a A[ExcHandler: all (th java.lang.Throwable), Splitter:B:8:0x001f] */
                @androidx.annotation.Nullable
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public byte[] call() throws java.lang.Exception {
                    /*
                        r6 = this;
                        com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
                        com.zipow.videobox.ptapp.FavoriteMgr r0 = r0.getFavoriteMgr()
                        r1 = 0
                        if (r0 != 0) goto L_0x000c
                        return r1
                    L_0x000c:
                        java.util.ArrayList r2 = new java.util.ArrayList
                        r2.<init>()
                        java.lang.String r3 = r4
                        boolean r0 = r0.getFavoriteListWithFilter(r3, r2)
                        if (r0 != 0) goto L_0x001a
                        return r1
                    L_0x001a:
                        java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0062 }
                        r0.<init>()     // Catch:{ IOException -> 0x0062 }
                        java.io.ObjectOutputStream r3 = new java.io.ObjectOutputStream     // Catch:{ Throwable -> 0x004d, all -> 0x004a }
                        r3.<init>(r0)     // Catch:{ Throwable -> 0x004d, all -> 0x004a }
                        r3.writeObject(r2)     // Catch:{ Throwable -> 0x0035, all -> 0x0032 }
                        byte[] r2 = r0.toByteArray()     // Catch:{ Throwable -> 0x0035, all -> 0x0032 }
                        r3.close()     // Catch:{ Throwable -> 0x004d, all -> 0x004a }
                        r0.close()     // Catch:{ IOException -> 0x0062 }
                        return r2
                    L_0x0032:
                        r2 = move-exception
                        r4 = r1
                        goto L_0x003b
                    L_0x0035:
                        r2 = move-exception
                        throw r2     // Catch:{ all -> 0x0037 }
                    L_0x0037:
                        r4 = move-exception
                        r5 = r4
                        r4 = r2
                        r2 = r5
                    L_0x003b:
                        if (r4 == 0) goto L_0x0046
                        r3.close()     // Catch:{ Throwable -> 0x0041, all -> 0x004a }
                        goto L_0x0049
                    L_0x0041:
                        r3 = move-exception
                        r4.addSuppressed(r3)     // Catch:{ Throwable -> 0x004d, all -> 0x004a }
                        goto L_0x0049
                    L_0x0046:
                        r3.close()     // Catch:{ Throwable -> 0x004d, all -> 0x004a }
                    L_0x0049:
                        throw r2     // Catch:{ Throwable -> 0x004d, all -> 0x004a }
                    L_0x004a:
                        r2 = move-exception
                        r3 = r1
                        goto L_0x0053
                    L_0x004d:
                        r2 = move-exception
                        throw r2     // Catch:{ all -> 0x004f }
                    L_0x004f:
                        r3 = move-exception
                        r5 = r3
                        r3 = r2
                        r2 = r5
                    L_0x0053:
                        if (r3 == 0) goto L_0x005e
                        r0.close()     // Catch:{ Throwable -> 0x0059 }
                        goto L_0x0061
                    L_0x0059:
                        r0 = move-exception
                        r3.addSuppressed(r0)     // Catch:{ IOException -> 0x0062 }
                        goto L_0x0061
                    L_0x005e:
                        r0.close()     // Catch:{ IOException -> 0x0062 }
                    L_0x0061:
                        throw r2     // Catch:{ IOException -> 0x0062 }
                    L_0x0062:
                        return r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.PTService.ServiceBinder.C211911.call():byte[]");
                }
            });
            this.mHandler.post(futureTask);
            try {
                return (byte[]) futureTask.get();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return null;
            }
        }

        @Nullable
        public String[] ABContactsHelper_getMatchedPhoneNumbers() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<String[]>() {
                @Nullable
                public String[] call() throws Exception {
                    ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
                    if (aBContactsHelper == null) {
                        return null;
                    }
                    ArrayList arrayList = new ArrayList();
                    aBContactsHelper.getMatchedPhoneNumbers(arrayList);
                    return (String[]) arrayList.toArray(new String[arrayList.size()]);
                }
            });
            this.mHandler.post(futureTask);
            try {
                return (String[]) futureTask.get();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return null;
            }
        }

        public int ABContactsHelper_inviteABContacts(@Nullable final String[] strArr, @Nullable final String str) throws RemoteException {
            if (strArr == null || str == null) {
                return 1;
            }
            FutureTask futureTask = new FutureTask(new Callable<Integer>() {
                public Integer call() throws Exception {
                    ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
                    if (aBContactsHelper == null) {
                        return Integer.valueOf(1);
                    }
                    ArrayList arrayList = new ArrayList();
                    int i = 0;
                    while (true) {
                        String[] strArr = strArr;
                        if (i >= strArr.length) {
                            return Integer.valueOf(aBContactsHelper.inviteABContacts(arrayList, str));
                        }
                        arrayList.add(strArr[i]);
                        i++;
                    }
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Integer) futureTask.get()).intValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return 11;
            }
        }

        @Nullable
        public String[] getLatestMeetingInfo() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<String[]>() {
                @NonNull
                public String[] call() throws Exception {
                    return new String[]{PTUI.getInstance().getLatestMeetingId(), String.valueOf(PTUI.getInstance().getLatestMeetingNumber())};
                }
            });
            this.mHandler.post(futureTask);
            try {
                return (String[]) futureTask.get();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return null;
            }
        }

        public boolean inviteCallOutUser(final String str, final String str2) throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(PTApp.getInstance().inviteCallOutUser(str, str2));
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean cancelCallOut() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(PTApp.getInstance().cancelCallOut());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean isCallOutInProgress() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(PTApp.getInstance().isCallOutInProgress(null));
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public int getCallOutStatus() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Integer>() {
                public Integer call() throws Exception {
                    return Integer.valueOf(PTApp.getInstance().getCallOutStatus());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Integer) futureTask.get()).intValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return 0;
            }
        }

        @Nullable
        public String[] getH323Gateway() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<String[]>() {
                @NonNull
                public String[] call() throws Exception {
                    return StringUtil.safeString(PTApp.getInstance().getH323Gateway()).split(";");
                }
            });
            this.mHandler.post(futureTask);
            try {
                return (String[]) futureTask.get();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return null;
            }
        }

        @Nullable
        public String getH323Password() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<String>() {
                @Nullable
                public String call() throws Exception {
                    return PTApp.getInstance().getH323Password();
                }
            });
            this.mHandler.post(futureTask);
            try {
                return (String) futureTask.get();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return null;
            }
        }

        public boolean sendMeetingParingCode(final long j, final String str) throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
                    if (meetingHelper == null) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(meetingHelper.sendMeetingParingCode(j, str));
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean callOutRoomSystem(final String str, final int i) throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
                    if (meetingHelper == null) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(meetingHelper.callOutRoomSystem(str, i, 2));
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean cancelCallOutRoomSystem() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
                    if (meetingHelper == null) {
                        return Boolean.valueOf(false);
                    }
                    return Boolean.valueOf(meetingHelper.cancelRoomDevice());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public void onConfUIMoveToFront(final String str) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ForegroundTaskManager.getInstance().onAnotherProcessMoveToFront(str);
                    AppStateMonitor.getInstance().onConfUIMoveToFront();
                    CmmSIPCallManager.onConfUIMoveToFront();
                    if (ServiceBinder.this.checkSyncFile()) {
                        FloatWindow.getInstance().showAllPendingAlertAvailable();
                    }
                }
            });
        }

        public void onConfUIMoveToBackground() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    AppStateMonitor.getInstance().onConfUIMoveToBackground();
                    CmmSIPCallManager.onConfUIMoveToBackground();
                }
            });
        }

        public void onBOStatusChangeStart(final boolean z, final int i, final String str) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    BOStatusChangeMgrOnPT.getInstance().handleStatusChangeStart(z, i, str);
                }
            });
        }

        public void onBOStatusChangeComplete() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    BOStatusChangeMgrOnPT.getInstance().handleStatusChangeCompeleted();
                }
            });
        }

        public void onJoinConfMeetingStatus(final boolean z, final boolean z2) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUI.getInstance().onJoinConfMeetingStatus(z, z2);
                }
            });
        }

        public void onCallOutStatus(final int i) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUI.getInstance().dispatchCallMeStatusChanged(i);
                }
            });
        }

        public boolean isSdkNeedWaterMark() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(PTApp.getInstance().isSdkNeedWaterMark());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public void sendMessageFromSip(byte[] bArr) throws RemoteException {
            PT4SIPIPCPort.getInstance().onMessageReceived(bArr);
        }

        public void showRateZoomDialog() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ZMActivity frontActivity = ZMActivity.getFrontActivity();
                    if ((frontActivity instanceof IMActivity) && frontActivity.isActive()) {
                        RateZoomDialogFragment.show(frontActivity.getSupportFragmentManager());
                    } else if (PTApp.getInstance().getPTLoginType() == 102 || PTApp.getInstance().getPTLoginType() == 97) {
                        WelcomeActivity.showRateRoomDialogOnResume();
                    } else {
                        IMActivity.showRateRoomDialogOnResume();
                    }
                }
            });
        }

        public boolean presentToRoom(int i, String str, long j, boolean z) throws RemoteException {
            final int i2 = i;
            final String str2 = str;
            final long j2 = j;
            final boolean z2 = z;
            C214232 r1 = new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(PTApp.getInstance().presentToRoom(i2, str2, j2, z2));
                }
            };
            FutureTask futureTask = new FutureTask(r1);
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public void stopPresentToRoom(final boolean z) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTApp.getInstance().stopPresentToRoom(z);
                }
            });
        }

        public boolean isAuthenticating() {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() {
                    return Boolean.valueOf(PTApp.getInstance().isAuthenticating());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public void setNeedCheckSwitchCall(final boolean z) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTApp.getInstance().setNeedCheckSwitchCall(z);
                }
            });
        }

        public void logout() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTApp.getInstance().logout(0, false);
                }
            });
        }

        public void notifyLeaveAndPerformAction(final int i, final int i2, final int i3) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    PTUI.getInstance().notifyLeaveAndPerformAction(i, i2, i3);
                }
            });
        }

        public boolean disablePhoneAudio() throws RemoteException {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(CmmSipAudioMgr.getInstance().disablePhoneAudio());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        public boolean isTaiWanZH() {
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() {
                    return Boolean.valueOf(PTApp.getInstance().isTaiWanZH());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception e) {
                ZMLog.m281e(PTService.TAG, e, "", new Object[0]);
                return false;
            }
        }

        /* access modifiers changed from: private */
        public boolean checkSyncFile() {
            File filesDir = VideoBoxApplication.getInstance().getFilesDir();
            if (filesDir == null) {
                return false;
            }
            filesDir.mkdir();
            if (!filesDir.exists() || !filesDir.isDirectory()) {
                return false;
            }
            String absolutePath = filesDir.getAbsolutePath();
            if (!absolutePath.endsWith("/")) {
                StringBuilder sb = new StringBuilder();
                sb.append(absolutePath);
                sb.append("/");
                absolutePath = sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(absolutePath);
            sb2.append(PTService.FILE_NAME);
            File file = new File(sb2.toString());
            if (!file.exists()) {
                return false;
            }
            file.delete();
            return true;
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(PTService.class.getName());
        sb.append(".ACTION_DEAMON");
        ACTION_DEAMON = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(PTService.class.getName());
        sb2.append(".ACTION_START_FOREGROUND");
        ACTION_START_FOREGROUND = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(PTService.class.getName());
        sb3.append(".ACTION_STOP_FOREGROUND");
        ACTION_STOP_FOREGROUND = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(PTService.class.getName());
        sb4.append(".ACTION_SHOW_CONF_NOTIFICATION");
        ACTION_SHOW_CONF_NOTIFICATION = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(PTService.class.getName());
        sb5.append(".ACTION_REMOVE_CONF_NOTIFICATION");
        ACTION_REMOVE_CONF_NOTIFICATION = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(PTService.class.getName());
        sb6.append(".ACTION_SHOW_SIP_NOTIFICATION");
        ACTION_SHOW_SIP_NOTIFICATION = sb6.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(PTService.class.getName());
        sb7.append(".ACTION_REMOVE_SIP_NOTIFICATION");
        ACTION_REMOVE_SIP_NOTIFICATION = sb7.toString();
    }

    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @TargetApi(14)
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        if (VideoBoxApplication.getInstance() == null) {
            stopSelf();
            return;
        }
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.GCM_REGISTRATION_ID, null);
        Mainboard mainboard = Mainboard.getMainboard();
        if ((!StringUtil.isEmptyOrNull(readStringValue) || (mainboard != null && !PTApp.getInstance().isDirectCallAvailable())) && PTApp.getInstance().getCallStatus() != 2) {
            stopSelf();
        }
    }

    public void onCreate() {
        super.onCreate();
        this.mReceiver = new PTBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_REMOVE_CONF_NOTIFICATION);
        intentFilter.addAction(ACTION_REMOVE_SIP_NOTIFICATION);
        registerReceiver(this.mReceiver, intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        PTBroadcastReceiver pTBroadcastReceiver = this.mReceiver;
        if (pTBroadcastReceiver != null) {
            unregisterReceiver(pTBroadcastReceiver);
        }
    }

    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public int onStartCommand(@Nullable Intent intent, int i, int i2) {
        int onStartCommand = super.onStartCommand(intent, i, i2);
        if (intent == null) {
            return onStartCommand;
        }
        String action = intent.getAction();
        if (ACTION_DEAMON.equalsIgnoreCase(action)) {
            onStartCommand = (VideoBoxApplication.getInstance() == null || !VideoBoxApplication.getInstance().isSDKMode()) ? 1 : 2;
            if (intent.hasExtra(ARG_IN_MEETING)) {
                this.mIsInMeeting = intent.getBooleanExtra(ARG_IN_MEETING, false);
            }
        } else if (!ACTION_START_FOREGROUND.equalsIgnoreCase(action) && !ACTION_STOP_FOREGROUND.equalsIgnoreCase(action)) {
            if (ACTION_SHOW_CONF_NOTIFICATION.equalsIgnoreCase(action)) {
                showConfNotification();
                this.mIsInMeeting = true;
            } else if (ACTION_REMOVE_CONF_NOTIFICATION.equalsIgnoreCase(action)) {
                removeConfNotification();
            } else if (ACTION_SHOW_SIP_NOTIFICATION.equalsIgnoreCase(action)) {
                showSipNotification();
            } else if (ACTION_REMOVE_SIP_NOTIFICATION.equalsIgnoreCase(action)) {
                removeSipNotification();
            }
        }
        return onStartCommand;
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /* access modifiers changed from: protected */
    public void startForeground() {
        if (this.mIsInMeeting) {
            this.mIsInForeground = true;
        }
    }

    /* access modifiers changed from: protected */
    public void stopForeground() {
        if (!this.mIsInMeeting && !this.mIsInSIP) {
            super.stopForeground(true);
        } else if (this.mIsInMeeting) {
            showConfNotification();
        } else {
            showSipNotification();
        }
        this.mIsInForeground = false;
    }

    /* access modifiers changed from: private */
    public void removeConfNotification() {
        this.mIsInMeeting = false;
        if (this.mIsInForeground) {
            startForeground();
        } else {
            stopForeground();
        }
    }

    /* access modifiers changed from: private */
    public void removeSipNotification() {
        this.mIsInSIP = false;
        if (this.mIsInForeground) {
            startForeground();
        } else {
            stopForeground();
        }
    }

    private void showSipNotification() {
        startForeground(6, NotificationMgr.getSipNotification(this));
        this.mIsInSIP = true;
    }
}
