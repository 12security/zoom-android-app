package com.zipow.videobox.sip.server;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.SipRingMgr;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.NotificationMgr.NotificationItem;
import com.zipow.videobox.view.sip.SipIncomePopActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class CmmSIPNosManager extends SimpleSIPCallEventListener {
    public static final String PUSH_CALL_LOG = "pushcalllog.txt";
    private static final int SHOW_INCOMING_CALL = 100;
    private static final long SHOW_INCOMING_CALL_EXPIRE_DELAY = 45000;
    private static final String TAG = "CmmSIPNosManager";
    private static CmmSIPNosManager instance;
    private Handler mIncomeCallTimeoutHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (CmmSIPNosManager.this.isNosSIPCallRinging() && CmmSIPNosManager.this.mNosSIPCallItem != null) {
                CmmSIPNosManager cmmSIPNosManager = CmmSIPNosManager.this;
                cmmSIPNosManager.printPushCallLog(2, cmmSIPNosManager.mNosSIPCallItem.getSid(), CmmSIPNosManager.this.mNosSIPCallItem.getTraceId(), "mIncomeCallTimeoutHandler, timeout");
                CmmSIPNosManager cmmSIPNosManager2 = CmmSIPNosManager.this;
                cmmSIPNosManager2.cancelNosSIPCall(cmmSIPNosManager2.mNosSIPCallItem.getSid());
            }
        }
    };
    private SimpleISIPLineMgrEventSinkListener mLineListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            if (cmmSIPCallRegResult.isRegistered() && CmmSIPLineManager.getInstance().isAllLineRegistered()) {
                CmmSIPNosManager.this.checkInboundCallToRelease();
            }
        }
    };
    /* access modifiers changed from: private */
    public NosSIPCallItem mNosSIPCallItem;
    private HashSet<String> mNosSIPCallItemCancelledSidList = new HashSet<>();
    private HashMap<String, NosSIPCallItem> mNosSIPCallItemInCallOffhookCache = new HashMap<>(3);
    private HashMap<String, NosSIPCallItem> mNosSIPCallItemReleaseCache = new HashMap<>(5);
    private List<NosSIPCallPopListener> mNosSIPCallPopListeners = new ArrayList(3);
    private boolean mNosSIPCallRinging;

    public interface NosSIPCallPopListener {
        void cancel(String str);

        void forceFinish();
    }

    public static class PushCallLogFileUtils {
        private static File getLogFile(boolean z) {
            StringBuilder sb = new StringBuilder();
            sb.append(AppUtil.getDataPath(z, false));
            sb.append(File.separator);
            sb.append(CmmSIPNosManager.PUSH_CALL_LOG);
            File file = new File(sb.toString());
            if (z && !file.exists()) {
                try {
                    File parentFile = file.getParentFile();
                    if (parentFile == null) {
                        return null;
                    }
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file.createNewFile();
                } catch (IOException unused) {
                    return null;
                }
            }
            return file;
        }

        public static void write(PushCallLog pushCallLog) {
            PrintWriter printWriter;
            Throwable th;
            if (pushCallLog != null) {
                File logFile = getLogFile(true);
                if (logFile != null) {
                    String json = new Gson().toJson((Object) pushCallLog);
                    try {
                        printWriter = new PrintWriter(new FileWriter(logFile, true));
                        printWriter.write(json);
                        printWriter.println();
                        printWriter.close();
                    } catch (Exception unused) {
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    return;
                }
                return;
            }
            return;
            throw th;
        }

        @Nullable
        public static List<PushCallLog> read() {
            BufferedReader bufferedReader;
            Throwable th;
            Throwable th2;
            File logFile = getLogFile(false);
            if (logFile == null || !logFile.exists()) {
                return null;
            }
            try {
                bufferedReader = new BufferedReader(new FileReader(logFile));
                try {
                    ArrayList arrayList = new ArrayList();
                    Gson gson = new Gson();
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            arrayList.add(gson.fromJson(readLine, PushCallLog.class));
                        } else {
                            bufferedReader.close();
                            return arrayList;
                        }
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    th = r0;
                    th2 = th4;
                }
            } catch (Exception unused) {
                return null;
            }
            if (th != null) {
                try {
                    bufferedReader.close();
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                }
            } else {
                bufferedReader.close();
            }
            throw th2;
            throw th2;
        }

        public static void clearPushCallLog() {
            File logFile = getLogFile(false);
            if (logFile != null && logFile.exists()) {
                logFile.delete();
            }
        }
    }

    public void OnSIPCallServiceStarted() {
        super.OnSIPCallServiceStarted();
        if (isNosSIPCallRinging() || !isNosSipCallValid()) {
            NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
            if (nosSIPCallItem != null) {
                printPushCallLog(2, nosSIPCallItem.getSid(), this.mNosSIPCallItem.getTraceId(), "OnSIPCallServiceStarted, not (!isNosSIPCallRinging() && isNosSipCallValid())");
                return;
            }
            return;
        }
        checkNosSipCall();
    }

    public void OnInboundCallPushDuplicateChecked(boolean z, String str) {
        NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
        if (nosSIPCallItem == null || str == null || !str.equals(nosSIPCallItem.getSid())) {
            printPushCallLog(2, StringUtil.safeString(str), "", "OnInboundCallPushDuplicateChecked, mNosSIPCallItem == null || sid == null || !sid.equals(mNosSIPCallItem.getSid())");
            return;
        }
        String sid = this.mNosSIPCallItem.getSid();
        String traceId = this.mNosSIPCallItem.getTraceId();
        StringBuilder sb = new StringBuilder();
        sb.append("OnInboundCallPushDuplicateChecked,is_duplicated:");
        sb.append(z);
        printPushCallLog(0, sid, traceId, sb.toString());
        if (!z) {
            CmmSIPCallManager instance2 = CmmSIPCallManager.getInstance();
            if (!isNosSipCallValid()) {
                printPushCallLog(2, StringUtil.safeString(str), this.mNosSIPCallItem.getTraceId(), "OnInboundCallPushDuplicateChecked, !isNosSipCallValid");
                return;
            }
            NosSIPCallItem nosSIPCallItem2 = this.mNosSIPCallItem;
            if (nosSIPCallItem2 == null || !TextUtils.equals(nosSIPCallItem2.getSid(), str)) {
                String safeString = StringUtil.safeString(str);
                NosSIPCallItem nosSIPCallItem3 = this.mNosSIPCallItem;
                printPushCallLog(2, safeString, nosSIPCallItem3 != null ? nosSIPCallItem3.getTraceId() : "", "OnInboundCallPushDuplicateChecked, !(mNosSIPCallItem!= null && TextUtils.equals(mNosSIPCallItem.getSid(),sid))");
            } else if (isNosSIPCallRinging()) {
                printPushCallLog(2, StringUtil.safeString(str), this.mNosSIPCallItem.getTraceId(), "OnInboundCallPushDuplicateChecked, isNosSIPCallRinging");
            } else if (instance2.hasOtherRinging()) {
                printPushCallLog(2, StringUtil.safeString(str), this.mNosSIPCallItem.getTraceId(), "OnInboundCallPushDuplicateChecked, hasOtherRinging");
            } else if (instance2.isInDND()) {
                printPushCallLog(2, StringUtil.safeString(str), this.mNosSIPCallItem.getTraceId(), "OnInboundCallPushDuplicateChecked, sipCallManager.isInDND");
            } else if (CmmSIPCallManager.isPhoneCallOffHook()) {
                printPushCallLog(2, this.mNosSIPCallItem.getSid(), this.mNosSIPCallItem.getTraceId(), "OnInboundCallPushDuplicateChecked, CmmSIPCallManager.isPhoneCallOffHook()");
                this.mNosSIPCallItemInCallOffhookCache.put(this.mNosSIPCallItem.getSid(), this.mNosSIPCallItem);
            } else {
                showSipIncomePop();
            }
        } else {
            printPushCallLog(2, StringUtil.safeString(str), this.mNosSIPCallItem.getTraceId(), "OnInboundCallPushDuplicateChecked, is_duplicated");
            checkNosSIPCallRinging(str);
            checkRemoveSipIncomeNotification(str);
            clearNosSIPCallItem();
        }
    }

    public static CmmSIPNosManager getInstance() {
        if (instance == null) {
            synchronized (CmmSIPNosManager.class) {
                if (instance == null) {
                    instance = new CmmSIPNosManager();
                }
            }
        }
        return instance;
    }

    private CmmSIPNosManager() {
    }

    public void init() {
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineListener);
    }

    /* access modifiers changed from: private */
    public void checkInboundCallToRelease() {
        if (CmmSIPCallManager.getInstance().isSipRegistered() && !this.mNosSIPCallItemReleaseCache.isEmpty()) {
            for (Entry value : this.mNosSIPCallItemReleaseCache.entrySet()) {
                inboundCallPushRelease((NosSIPCallItem) value.getValue());
            }
            this.mNosSIPCallItemReleaseCache.clear();
        }
    }

    public void releaseInboundCallWithCancel() {
        NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
        if (nosSIPCallItem != null) {
            releaseInboundCallWithCancel(nosSIPCallItem);
        }
    }

    public void releaseInboundCallWithCancel(NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem != null) {
            nosSIPCallItem.setFrom("");
            releaseInboundCall(nosSIPCallItem);
        }
    }

    public void releaseInboundCall(NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem != null) {
            if (!CmmSIPCallManager.isInit() || !CmmSIPCallManager.getInstance().isSipRegistered()) {
                addNosSIPCallItemRelease(nosSIPCallItem);
            } else {
                inboundCallPushRelease(nosSIPCallItem);
            }
            checkRemoveSipIncomeNotification(nosSIPCallItem.getSid());
            clearNosSIPCallItem(nosSIPCallItem.getSid());
        }
    }

    private boolean isNosSIPCallItemCancelled(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return this.mNosSIPCallItemCancelledSidList.contains(str);
    }

    public void addNosSIPCallItemCancelled(String str) {
        if (!TextUtils.isEmpty(str) && !this.mNosSIPCallItemCancelledSidList.contains(str)) {
            this.mNosSIPCallItemCancelledSidList.add(str);
        }
    }

    private void removeNosSIPCallItemCancelled(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mNosSIPCallItemCancelledSidList.remove(str);
        }
    }

    private void setNosSIPCallItem(NosSIPCallItem nosSIPCallItem) {
        this.mNosSIPCallItem = nosSIPCallItem;
    }

    public void clear() {
        clearNosSIPCallItem();
        this.mNosSIPCallItemReleaseCache.clear();
    }

    public boolean isCancelNosSIPCall(NosCancelSIPCallItem nosCancelSIPCallItem) {
        boolean z = false;
        if (nosCancelSIPCallItem == null) {
            return false;
        }
        String sid = nosCancelSIPCallItem.getSid();
        if (TextUtils.isEmpty(sid)) {
            return false;
        }
        if ("answer".equals(nosCancelSIPCallItem.getReason())) {
            CmmSIPCallManager instance2 = CmmSIPCallManager.getInstance();
            Iterator it = instance2.getSipCallIds().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String str = (String) it.next();
                if (!TextUtils.isEmpty(str)) {
                    CmmSIPCallItem callItemByCallID = instance2.getCallItemByCallID(str);
                    if (callItemByCallID != null && sid.equals(callItemByCallID.getSid())) {
                        z = true;
                        break;
                    }
                }
            }
        }
        return !z;
    }

    public void cancelNosSIPCall() {
        NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
        if (nosSIPCallItem != null) {
            cancelNosSIPCall(nosSIPCallItem.getSid());
        }
    }

    public void cancelNosSIPCall(String str) {
        if (!TextUtils.isEmpty(str)) {
            clearIncomingCallTimeoutMessage();
            checkRemoveSipIncomeNotification(str);
            addNosSIPCallItemCancelled(str);
            checkNosSIPCallRinging(str);
            checkNosSIPCallItemInCallOffhookCache(str);
            clearNosSIPCallItem(str);
            setNosSIPCallRinging(false);
        }
    }

    private void checkNosSIPCallItemInCallOffhookCache(String str) {
        if (!TextUtils.isEmpty(str)) {
            HashMap<String, NosSIPCallItem> hashMap = this.mNosSIPCallItemInCallOffhookCache;
            if (hashMap != null && !hashMap.isEmpty()) {
                NosSIPCallItem nosSIPCallItem = (NosSIPCallItem) this.mNosSIPCallItemInCallOffhookCache.get(str);
                if (nosSIPCallItem != null) {
                    Context globalContext = VideoBoxApplication.getGlobalContext();
                    if (globalContext != null) {
                        NotificationMgr.showMissedSipCallNotification(globalContext, str, new NotificationItem(nosSIPCallItem.getFromName(), globalContext.getString(C4558R.string.zm_sip_missed_sip_call_title_111899)));
                        this.mNosSIPCallItemInCallOffhookCache.remove(str);
                    }
                }
            }
        }
    }

    public void checkRemoveSipIncomeNotification(String str) {
        NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
        if (nosSIPCallItem != null && nosSIPCallItem.getSid() != null && this.mNosSIPCallItem.getSid().equals(str) && isNosSIPCallRinging()) {
            removeSipIncomeNotification();
        }
    }

    public void removeSipIncomeNotification() {
        NotificationMgr.removeSipIncomeNotification(VideoBoxApplication.getGlobalContext());
        SipRingMgr.getInstance().stopRing();
    }

    public boolean containsInNosSIPCallItemInCallOffhookCache() {
        HashMap<String, NosSIPCallItem> hashMap = this.mNosSIPCallItemInCallOffhookCache;
        return hashMap != null && !hashMap.isEmpty();
    }

    public void clearNosSIPCallItemInCallOffhookCache() {
        HashMap<String, NosSIPCallItem> hashMap = this.mNosSIPCallItemInCallOffhookCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public void handleDuplicateCheckIncomingPushCall(NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem != null) {
            printPushCallLog(0, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleDuplicateCheckIncomingPushCall");
            if (isNosSIPCallExist(nosSIPCallItem)) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleDuplicateCheckIncomingPushCall, isNosSIPCallExist");
            } else if (!isNosSipCallValid(nosSIPCallItem)) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleDuplicateCheckIncomingPushCall, !isNosSipCallValid(nosSIPCallItem)");
                handleCallForUnavailable(nosSIPCallItem);
            } else if (isNosSIPCallRinging()) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleDuplicateCheckIncomingPushCall, isNosSIPCallRinging");
                handleCallForUnavailable(nosSIPCallItem);
            } else if (isNosSipCallValid()) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleDuplicateCheckIncomingPushCall, isNosSipCallValid");
                handleCallForUnavailable(nosSIPCallItem);
            } else if (CmmSIPCallManager.getInstance().isInDND()) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleDuplicateCheckIncomingPushCall, isInDND");
                handleCallForUnavailable(nosSIPCallItem);
            } else if (CmmSIPCallManager.isPhoneCallOffHook()) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleDuplicateCheckIncomingPushCall, isPhoneCallOffHook");
                handleCallForUnavailable(nosSIPCallItem);
                this.mNosSIPCallItemInCallOffhookCache.put(nosSIPCallItem.getSid(), nosSIPCallItem);
            } else {
                setNosSIPCallItem(nosSIPCallItem);
                if (CmmSIPCallManager.isInit() && CmmSIPCallManager.getInstance().isSipInited()) {
                    checkNosSipCall();
                }
            }
        }
    }

    private void handleCallForUnavailable(@Nullable NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem != null) {
            boolean z = CmmSIPCallManager.isInit() && CmmSIPCallManager.getInstance().isCloudPBXEnabled();
            if (nosSIPCallItem.isCallQueue()) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleCallForUnavailable, item.isCallQueue()");
                releaseInboundCall(nosSIPCallItem);
                return;
            }
            if (z) {
                CmmSIPLine mineExtensionLine = CmmSIPLineManager.getInstance().getMineExtensionLine();
                if (mineExtensionLine != null && !mineExtensionLine.isShared() && CmmSIPLineManager.getInstance().isLineMatchesNosSIPCall(mineExtensionLine.getID(), nosSIPCallItem)) {
                    printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleCallForUnavailable, isLineMatchesNosSIPCall");
                    releaseInboundCall(nosSIPCallItem);
                    return;
                }
            }
            if (z || !CmmSIPCallManager.isInit() || !CmmSIPCallManager.getInstance().isSipCallEnabled()) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleCallForUnavailable, ignore");
                return;
            }
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleCallForUnavailable, isSipCallEnabled");
            releaseInboundCall(nosSIPCallItem);
        }
    }

    private boolean isNosSIPCallExist(NosSIPCallItem nosSIPCallItem) {
        NosSIPCallItem nosSIPCallItem2 = this.mNosSIPCallItem;
        return (nosSIPCallItem2 == null || nosSIPCallItem2.getSid() == null || nosSIPCallItem.getSid() == null || !this.mNosSIPCallItem.getSid().equals(nosSIPCallItem.getSid())) ? false : true;
    }

    private void addNosSIPCallItemRelease(NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem != null && !TextUtils.isEmpty(nosSIPCallItem.getSid())) {
            printPushCallLog(0, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "addNosSIPCallItemRelease");
            if (!this.mNosSIPCallItemReleaseCache.containsKey(nosSIPCallItem.getSid())) {
                this.mNosSIPCallItemReleaseCache.put(nosSIPCallItem.getSid(), nosSIPCallItem);
            }
        }
    }

    private boolean inBoundCallPushDuplicateCheck(String str) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        printPushCallLog(0, this.mNosSIPCallItem.getSid(), this.mNosSIPCallItem.getTraceId(), "inBoundCallPushDuplicateCheck");
        return sipCallAPI.inboundCallPushDuplicateCheck(str);
    }

    public boolean inboundCallPushPickup(NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem == null) {
            return false;
        }
        return inboundCallPushPickup(nosSIPCallItem.getFrom(), nosSIPCallItem.getFromName(), nosSIPCallItem.getSid(), nosSIPCallItem.getServerId(), nosSIPCallItem.getSiplb(), nosSIPCallItem.getTraceId(), nosSIPCallItem.getTo());
    }

    private boolean inboundCallPushPickup(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        int i;
        String str8;
        int i2;
        int i3;
        if (PTApp.getInstance().getSipCallAPI() == null) {
            return false;
        }
        CmmSIPLine mineExtensionLine = CmmSIPLineManager.getInstance().getMineExtensionLine();
        if (mineExtensionLine != null) {
            String countryCode = mineExtensionLine.getCountryCode();
            if (countryCode != null) {
                try {
                    i3 = Integer.parseInt(countryCode);
                } catch (Exception unused) {
                    i3 = CmmSIPCallManager.getInstance().getCountryCode();
                }
            } else {
                i3 = CmmSIPCallManager.getInstance().getCountryCode();
            }
            str8 = mineExtensionLine.getOwnerNumber();
            i = i3;
        } else {
            String[] callerInfoForCallpeer = CmmSIPCallManager.getInstance().getCallerInfoForCallpeer();
            try {
                i2 = Integer.parseInt(callerInfoForCallpeer[0]);
            } catch (Exception unused2) {
                i2 = CmmSIPCallManager.getInstance().getCountryCode();
            }
            str8 = callerInfoForCallpeer[1];
            i = i2;
        }
        return inboundCallPushPickup(str, str2, str8, i, false, str3, str4, str5, str6, str7);
    }

    private boolean inboundCallPushPickup(@Nullable String str, @Nullable String str2, @Nullable String str3, int i, boolean z, @Nullable String str4, @Nullable String str5, String str6, String str7, String str8) {
        String str9 = str4;
        String str10 = str7;
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        printPushCallLog(3, str9, str10, "inboundCallPushPickup");
        checkRemoveSipIncomeNotification(str9);
        clearNosSIPCallItem();
        boolean inboundCallPushPickup = sipCallAPI.inboundCallPushPickup(StringUtil.safeString(str), StringUtil.safeString(str2), StringUtil.safeString(str3), i, z, StringUtil.safeString(str4), StringUtil.safeString(str5), StringUtil.safeString(str6), StringUtil.safeString(str7), StringUtil.safeString(str8));
        if (!inboundCallPushPickup) {
            printPushCallLog(2, str9, str10, "inboundCallPushPickup fail");
        }
        return inboundCallPushPickup;
    }

    private boolean inboundCallPushRelease(NosSIPCallItem nosSIPCallItem) {
        boolean z;
        if (nosSIPCallItem == null) {
            return false;
        }
        if (TextUtils.isEmpty(nosSIPCallItem.getFrom())) {
            printPushCallLog(1, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "inboundCallPushRelease");
            boolean inboundCallPushRelease = inboundCallPushRelease(0, nosSIPCallItem.getSid(), nosSIPCallItem.getFrom(), nosSIPCallItem.getServerId(), nosSIPCallItem.getSiplb(), nosSIPCallItem.getTraceId());
            if (!inboundCallPushRelease) {
                printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "inboundCallPushRelease.kSIPCallInBoundPushReleaseAction_Cancel fail");
            }
            return inboundCallPushRelease;
        }
        printPushCallLog(4, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "inboundCallPushRelease");
        if (nosSIPCallItem.isCallQueue()) {
            z = inboundCallPushRelease(1, nosSIPCallItem.getSid(), nosSIPCallItem.getFrom(), nosSIPCallItem.getServerId(), nosSIPCallItem.getSiplb(), nosSIPCallItem.getTraceId());
        } else {
            z = inboundCallPushRelease(2, nosSIPCallItem.getSid(), nosSIPCallItem.getFrom(), nosSIPCallItem.getServerId(), nosSIPCallItem.getSiplb(), nosSIPCallItem.getTraceId());
        }
        if (!z) {
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), nosSIPCallItem.isCallQueue() ? "inboundCallPushRelease.kSIPCallInBoundPushReleaseAction_Skip fail" : "inboundCallPushRelease.kSIPCallInBoundPushReleaseAction_Decline fail");
        }
        return z;
    }

    private boolean inboundCallPushRelease(int i, String str, String str2, String str3, String str4, String str5) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        checkRemoveSipIncomeNotification(str);
        clearNosSIPCallItem();
        return sipCallAPI.inboundCallPushRelease(i, str, str2, str3, str4, str5);
    }

    public void checkNosSipCall() {
        printPushCallLog(0, this.mNosSIPCallItem.getSid(), this.mNosSIPCallItem.getTraceId(), "checkNosSipCall");
        if (!isNosSipCallDuplicateChecked()) {
            if (!isNosSipCallValid()) {
                printPushCallLog(2, this.mNosSIPCallItem.getSid(), this.mNosSIPCallItem.getTraceId(), "checkNosSipCall, not isNosSipCallValid");
                NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
                if (nosSIPCallItem != null) {
                    checkRemoveSipIncomeNotification(nosSIPCallItem.getSid());
                }
                clearNosSIPCallItem();
            } else if (inBoundCallPushDuplicateCheck(this.mNosSIPCallItem.getSid())) {
                this.mNosSIPCallItem.setDuplicateChecked(true);
            }
        }
    }

    private boolean isNosSipCallValid() {
        return isNosSipCallValid(this.mNosSIPCallItem);
    }

    public boolean isNosSipCallValid(NosSIPCallItem nosSIPCallItem) {
        return nosSIPCallItem != null && !TextUtils.isEmpty(nosSIPCallItem.getSid()) && !isNosSIPCallItemCancelled(nosSIPCallItem.getSid());
    }

    private boolean isNosSipCallExpired(NosSIPCallItem nosSIPCallItem) {
        return nosSIPCallItem != null && Math.abs(System.currentTimeMillis() - nosSIPCallItem.getTimestamp()) > 30000;
    }

    private boolean isNosSipCallDuplicateChecked() {
        NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
        return nosSIPCallItem != null && nosSIPCallItem.isDuplicateChecked();
    }

    public void prepareSipCall() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            if (VideoBoxApplication.getInstance() == null) {
                VideoBoxApplication.initialize(VideoBoxApplication.getGlobalContext(), false, 0);
            }
            VideoBoxApplication.getInstance().initPTMainboard();
            PTApp.getInstance().autoSignin();
            ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
            if (sipCallAPI != null) {
                sipCallAPI.registerUICallBack(SIPCallEventListenerUI.getInstance());
                ISIPLineMgrAPI sIPLineMgrAPI = sipCallAPI.getSIPLineMgrAPI();
                if (sIPLineMgrAPI != null) {
                    sIPLineMgrAPI.setLineEventSink(ISIPLineMgrEventSinkUI.getInstance());
                }
                IPBXMessageAPI messageAPI = sipCallAPI.getMessageAPI();
                if (messageAPI != null && !messageAPI.isInited()) {
                    messageAPI.initialize(IPBXMessageEventSinkUI.getInstance());
                }
            }
            CmmSIPCallManager instance2 = CmmSIPCallManager.getInstance();
            instance2.initSipIPC();
            instance2.initSIPCallWithoutWeblogin();
        }
    }

    public boolean handleIncomingPushCallInBG(NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem == null) {
            return false;
        }
        if (isNosSIPCallExist(nosSIPCallItem)) {
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleIncomingPushCallInBG, isNosSIPCallExist(nosSIPCallItem)");
            return false;
        } else if (!isNosSipCallValid(nosSIPCallItem)) {
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleIncomingPushCallInBG, !isNosSipCallValid(nosSIPCallItem)");
            handleCallForUnavailable(nosSIPCallItem);
            return false;
        } else if (isNosSIPCallRinging()) {
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleIncomingPushCallInBG, isNosSIPCallRinging");
            handleCallForUnavailable(nosSIPCallItem);
            return false;
        } else if (isNosSipCallValid()) {
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleIncomingPushCallInBG, isNosSipCallValid");
            handleCallForUnavailable(nosSIPCallItem);
            return false;
        } else if (CmmSIPCallManager.isInit() && CmmSIPCallManager.getInstance().isInDND()) {
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleIncomingPushCallInBG, isInDND");
            handleCallForUnavailable(nosSIPCallItem);
            return false;
        } else if (CmmSIPCallManager.isPhoneCallOffHook()) {
            printPushCallLog(2, nosSIPCallItem.getSid(), nosSIPCallItem.getTraceId(), "handleIncomingPushCallInBG, CmmSIPCallManager.isPhoneCallOffHook()");
            handleCallForUnavailable(nosSIPCallItem);
            this.mNosSIPCallItemInCallOffhookCache.put(nosSIPCallItem.getSid(), nosSIPCallItem);
            return false;
        } else {
            setNosSIPCallItem(nosSIPCallItem);
            if (CmmSIPCallManager.isInit() && CmmSIPCallManager.getInstance().isSipInited()) {
                checkNosSipCall();
            }
            return true;
        }
    }

    public void onNewNosCallInBG() {
        printPushCallLog(0, this.mNosSIPCallItem.getSid(), this.mNosSIPCallItem.getTraceId(), "onNewNosCallInBG");
        showSipIncomePop(true);
    }

    public void showSipIncomePop() {
        showSipIncomePop(false);
    }

    private void showSipIncomePop(boolean z) {
        NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
        if (nosSIPCallItem != null) {
            String sid = nosSIPCallItem.getSid();
            String traceId = this.mNosSIPCallItem.getTraceId();
            StringBuilder sb = new StringBuilder();
            sb.append("showSipIncomePop, needInitModule:");
            sb.append(z);
            printPushCallLog(0, sid, traceId, sb.toString());
            setNosSIPCallRinging(true);
            if (this.mNosSIPCallItem != null && CmmSIPCallManager.isInit()) {
                CmmSIPCallManager.getInstance().refreshVCardByNumber(this.mNosSIPCallItem.getFrom());
            }
            SipIncomePopActivity.show(VideoBoxApplication.getNonNullInstance(), this.mNosSIPCallItem, z);
            if (NotificationMgr.showSipIncomeNotification(VideoBoxApplication.getGlobalContext(), this.mNosSIPCallItem, z)) {
                SipRingMgr.getInstance().startRing(VideoBoxApplication.getGlobalContext());
            }
            this.mIncomeCallTimeoutHandler.sendEmptyMessageDelayed(100, SHOW_INCOMING_CALL_EXPIRE_DELAY);
        }
    }

    public void clearIncomingCallTimeoutMessage() {
        this.mIncomeCallTimeoutHandler.removeMessages(100);
    }

    public void finishSipIncomePop() {
        performForceFinishPop();
    }

    public void addNosSIPCallPopListener(NosSIPCallPopListener nosSIPCallPopListener) {
        if (!this.mNosSIPCallPopListeners.contains(nosSIPCallPopListener)) {
            this.mNosSIPCallPopListeners.add(nosSIPCallPopListener);
        }
    }

    public void removeNosSIPCallPopListener(NosSIPCallPopListener nosSIPCallPopListener) {
        this.mNosSIPCallPopListeners.remove(nosSIPCallPopListener);
    }

    public void checkNosSIPCallRinging(String str) {
        performCancelNosSIPCall(str);
    }

    private void performCancelNosSIPCall(String str) {
        if (this.mNosSIPCallPopListeners != null) {
            for (int i = 0; i < this.mNosSIPCallPopListeners.size(); i++) {
                ((NosSIPCallPopListener) this.mNosSIPCallPopListeners.get(i)).cancel(str);
            }
        }
    }

    private void performForceFinishPop() {
        if (this.mNosSIPCallPopListeners != null) {
            for (int i = 0; i < this.mNosSIPCallPopListeners.size(); i++) {
                ((NosSIPCallPopListener) this.mNosSIPCallPopListeners.get(i)).forceFinish();
            }
        }
    }

    public boolean isNosSIPCallRinging() {
        return this.mNosSIPCallRinging;
    }

    public void setNosSIPCallRinging(boolean z) {
        this.mNosSIPCallRinging = z;
    }

    public void clearNosSIPCallItem() {
        this.mNosSIPCallItem = null;
    }

    public void clearNosSIPCallItem(String str) {
        NosSIPCallItem nosSIPCallItem = this.mNosSIPCallItem;
        if (nosSIPCallItem != null && nosSIPCallItem.getSid() != null && this.mNosSIPCallItem.getSid().equals(str)) {
            this.mNosSIPCallItem = null;
        }
    }

    @Nullable
    public NosSIPCallItem getNosSIPCallItem() {
        return this.mNosSIPCallItem;
    }

    public boolean printPushCallLog(int i, String str, String str2, String str3) {
        return printPushCallLog(i, str, str2, str3, 0);
    }

    public boolean printPushCallLog(int i, String str, String str2, String str3, long j) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return checkAndPrintPushCallLog(i, String.valueOf(System.currentTimeMillis()), str, str2, str3, j);
    }

    private boolean checkAndPrintPushCallLog(int i, String str, String str2, String str3, String str4, long j) {
        boolean printPushCallLog = CmmSIPCallManager.isInit() ? printPushCallLog(i, str, str2, str3, str4, j) : false;
        if (!printPushCallLog) {
            PushCallLog pushCallLog = new PushCallLog();
            pushCallLog.setType(i);
            pushCallLog.setTime(str);
            pushCallLog.setSid(str2);
            pushCallLog.setTraceId(str3);
            pushCallLog.setFail(str4);
            pushCallLog.setnRecvPushElapse(j);
            savePushCallLog(pushCallLog);
        }
        return printPushCallLog;
    }

    private boolean printPushCallLog(int i, String str, String str2, String str3, String str4, long j) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.printPushCallLog(i, str, str2, str3, str4, j);
    }

    private void savePushCallLog(PushCallLog pushCallLog) {
        PushCallLogFileUtils.write(pushCallLog);
    }

    public void printSavedPushCallLogs() {
        List<PushCallLog> read = PushCallLogFileUtils.read();
        if (read != null && !read.isEmpty()) {
            for (PushCallLog pushCallLog : read) {
                printPushCallLog(pushCallLog.getType(), pushCallLog.getTime(), pushCallLog.getSid(), pushCallLog.getTraceId(), pushCallLog.getFail(), pushCallLog.getnRecvPushElapse());
            }
            PushCallLogFileUtils.clearPushCallLog();
        }
    }
}
