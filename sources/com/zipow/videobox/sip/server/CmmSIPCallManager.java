package com.zipow.videobox.sip.server;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxUser;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.PTService;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.dialog.ConfirmAlertDialog;
import com.zipow.videobox.dialog.ConfirmAlertDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.fragment.ErrorMsgConfirmDialog;
import com.zipow.videobox.fragment.ErrorMsgConfirmDialog.ErrorInfo;
import com.zipow.videobox.ptapp.IncomingCallManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRemoteMemberProto;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTAppProtos.PBXNumber;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.SystemInfoHelper;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistory;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.CmmCallPeerDataBean;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.PBXJoinMeetingRequest;
import com.zipow.videobox.sip.SipCallTimeoutMgr;
import com.zipow.videobox.sip.SipRingMgr;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.client.AssistantAppClientMgr;
import com.zipow.videobox.sip.server.PBXLoginConflictListenerUI.PBXLoginConflictListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.NotificationMgr.NotificationItem;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.util.ZMServiceHelper;
import com.zipow.videobox.view.SnackbarToast;
import com.zipow.videobox.view.sip.SipDialKeyboardFragment;
import com.zipow.videobox.view.sip.SipInCallActivity;
import com.zipow.videobox.view.sip.SipInCallFloatViewHelper;
import com.zipow.videobox.view.sip.SipIncomeActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkStatusReceiver;
import p021us.zoom.androidlib.util.NetworkStatusReceiver.SimpleNetworkStatusListener;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.SnackbarUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class CmmSIPCallManager implements IPTUIListener {
    private static final long DELAY_REQUEST_USER_INFO_ON_SIP_ACTIVED = 5000;
    public static final int MAX_SIP_CALLS = 4;
    private static final int MSG_REQUEST_USER_INFO = 191;
    private static final long PBX_CALL_REALTIME_POLICY_AUTO_RECORDING_INBOUND_ENABLED = 8;
    private static final long PBX_CALL_REALTIME_POLICY_AUTO_RECORDING_OUTBOUND_ENABLED = 16;
    private static final int SIPCALL_DEFAULT_COUNTRY_CODE = 1;
    private static final String TAG = "CmmSIPCallManager";
    private static final int TIPS_DURATION = 5000;
    private static CmmSIPCallManager mInstance;
    /* access modifiers changed from: private */
    public CmmSIPCallItemLocal mCallItemLocal;
    private IConfProcessListener mConfProcessListener = new IConfProcessListener() {
        public void onConfProcessStarted() {
            CmmSIPCallManager.this.checkShowSipFloatWindow();
        }

        public void onConfProcessStopped() {
            CmmSIPCallManager.this.checkHideSipFloatWindow();
        }
    };
    private int mCurrentIndexInCallCache = 0;
    /* access modifiers changed from: private */
    public HashSet<String> mFailedNumber = new HashSet<>();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            if (message.what == 191) {
                CmmSIPCallManager.this.queryUserPbxInfo();
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mHasNewMessage = false;
    private HashSet<String> mMediaOffLoadCalls = new HashSet<>(4);
    private HashMap<String, PBXJoinMeetingRequest> mMeetingCallIdCache = new HashMap<>(5);
    private SimpleNetworkStatusListener mNetworkListener = new SimpleNetworkStatusListener() {
        public void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2) {
            super.networkStatusChanged(z, i, str, z2, i2, str2);
            CmmSIPCallManager.this.onNetworkState(z, i, str);
        }
    };
    private NetworkStatusReceiver mNetworkRecevier;
    private long mOutOfServiceTime;
    private String mPendingNumber;
    private SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnSIPCallServiceStarted() {
            super.OnSIPCallServiceStarted();
            CmmSIPLineManager.getInstance().onSIPCallServiceStarted();
            CmmSIPNosManager.getInstance().printSavedPushCallLogs();
        }

        public void OnSipServiceNeedUnregisterForGracePeriod() {
            super.OnSipServiceNeedUnregisterForGracePeriod();
            CmmSIPCallManager.this.unRegistrarMyLine();
        }

        public void OnUnloadSIPService(int i) {
            super.OnUnloadSIPService(i);
            CmmSIPLineManager.getInstance().clearRegisterResult();
        }

        public void OnCallStatusUpdate(String str, int i) {
            super.OnCallStatusUpdate(str, i);
            CmmSIPCallManager.this.onSipCallStatusChange(i, str);
        }

        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            CmmSIPCallManager.this.onCallTerminated(str, i);
        }

        public void OnNewCallGenerate(String str, int i) {
            super.OnNewCallGenerate(str, i);
            if (CmmSIPCallManager.this.isLoginConflict()) {
                if (CmmSIPCallManager.this.isSipRegistered()) {
                    CmmSIPCallManager.this.unRegistrar();
                }
                return;
            }
            if (i == 0) {
                if (!CmmSIPCallManager.this.isInMaxCallsCount()) {
                    CmmSIPCallManager.this.handleCallForUnavailable(str);
                } else if (!CmmSIPCallManager.this.isNeedRing(str)) {
                    CmmSIPCallManager.this.handleCallForUnavailable(str);
                } else if (CmmSIPCallManager.this.hasOtherRinging(str)) {
                    CmmSIPCallManager.this.handleCallForUnavailable(str);
                } else if (CmmSIPCallManager.this.isInDND()) {
                    CmmSIPCallManager.this.handleCallForUnavailable(str);
                } else if (CmmSIPCallManager.this.isValidIncomingCall(str)) {
                    if (CmmSIPCallManager.isPhoneCallOffHook()) {
                        CmmSIPCallManager.this.handleCallForUnavailable(str, true);
                        CmmSIPCallManager.this.mSipCallIdsInCallOffhook.add(str);
                        return;
                    }
                    CmmSIPCallManager.this.onCallIncoming(str);
                }
            } else if (i == 1 || i == 4) {
                if (!(CmmSIPCallManager.this.mCallItemLocal == null || CmmSIPCallManager.this.getCallItemByCallID(str) == CmmSIPCallManager.this.mCallItemLocal)) {
                    CmmSIPCallManager cmmSIPCallManager = CmmSIPCallManager.this;
                    cmmSIPCallManager.popCallId(cmmSIPCallManager.mCallItemLocal.getCallID());
                    SipCallTimeoutMgr.getInstance().stopSipCallTimeout(CmmSIPCallManager.this.mCallItemLocal.getCallID());
                    CmmSIPCallManager.this.mCallItemLocal = null;
                }
                CmmSIPCallManager.this.showSipInCallUI(str);
            } else if (i == 6) {
                CmmSIPCallManager.this.showSipInCallUI(str);
            }
        }

        public void OnSIPCallServiceStoped(boolean z) {
            super.OnSIPCallServiceStoped(z);
            CmmSIPCallManager.this.clearSipCache();
            CmmSIPLineManager.getInstance().clearRegisterResult();
            CmmSIPCallManager.this.onSipCallServiceStoped();
        }

        public void OnCheckPhoneNumberFailed(String str) {
            super.OnCheckPhoneNumberFailed(str);
            CmmSIPCallManager.this.mFailedNumber.add(str);
            if (!(CmmSIPCallManager.this.mCallItemLocal == null || CmmSIPCallManager.this.mCallItemLocal.getPeerURI() == null || !CmmSIPCallManager.this.mCallItemLocal.getPeerURI().equals(str))) {
                CmmSIPCallManager cmmSIPCallManager = CmmSIPCallManager.this;
                cmmSIPCallManager.popCallId(cmmSIPCallManager.mCallItemLocal.getCallID());
                SipCallTimeoutMgr.getInstance().stopSipCallTimeout(CmmSIPCallManager.this.mCallItemLocal.getCallID());
                CmmSIPCallManager.this.mCallItemLocal = null;
            }
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                Toast.makeText(globalContext, C4558R.string.zm_sip_callout_invalid_number_27110, 1).show();
            }
        }

        public void OnMergeCallResult(boolean z, String str, String str2) {
            super.OnMergeCallResult(z, str, str2);
            CmmSIPCallManager.this.popCallId(str2);
            CmmSIPCallManager.this.resetCurrentCall(str);
        }

        public void OnMeetingStartedResult(String str, long j, String str2, boolean z) {
            super.OnMeetingStartedResult(str, j, str2, z);
            CmmSIPCallManager.this.onMeetingStartedResult(str, j, str2, z);
        }

        public void OnReceivedJoinMeetingRequest(String str, long j, String str2) {
            super.OnReceivedJoinMeetingRequest(str, j, str2);
            CmmSIPCallManager.this.onReceivedJoinMeetingRequest(str, j, str2);
        }

        public void OnMeetingJoinedResult(String str, boolean z) {
            super.OnMeetingJoinedResult(str, z);
            CmmSIPCallManager.this.onMeetingJoinedResult(str, z);
        }

        public void OnPBXUserStatusChange(int i) {
            super.OnPBXUserStatusChange(i);
            if (i == 1 || i == 3) {
                CmmSIPCallManager.this.unRegistrar();
            }
        }

        public void OnWMIMessageCountChanged(int i, int i2, boolean z) {
            super.OnWMIMessageCountChanged(i, i2, z);
            CmmSIPCallManager.this.mHasNewMessage = i2 > 0 || z;
        }

        public void OnCallRemoteOperationFail(String str, int i, String str2) {
            String str3;
            super.OnCallRemoteOperationFail(str, i, str2);
            VideoBoxApplication nonNullInstance = VideoBoxApplication.getNonNullInstance();
            if (nonNullInstance != null) {
                if (i == 404) {
                    str3 = nonNullInstance.getString(C4558R.string.zm_sip_error_call_404_124905);
                } else if (i != 408) {
                    if (i != 480) {
                        if (i == 486) {
                            str3 = nonNullInstance.getString(C4558R.string.zm_sip_error_call_486_129845);
                        } else if (i != 504) {
                            if (i == 603) {
                                str3 = nonNullInstance.getString(C4558R.string.zm_sip_error_call_603_99728);
                            } else if (i != 801) {
                                int i2 = C4558R.string.zm_sip_error_call_99728;
                                Object[] objArr = new Object[1];
                                if (StringUtil.isEmptyOrNull(str2)) {
                                    str2 = String.valueOf(i);
                                }
                                objArr[0] = str2;
                                str3 = nonNullInstance.getString(i2, objArr);
                            } else {
                                CmmSIPCallManager.this.setOutOfServiceTime();
                                CmmSIPCallManager.getInstance().playTone("oos.wav", 28, 1);
                                return;
                            }
                        }
                    }
                    str3 = nonNullInstance.getString(C4558R.string.zm_sip_error_call_480_99728);
                } else {
                    str3 = nonNullInstance.getString(C4558R.string.zm_sip_error_call_408_99728);
                }
                CmmSIPCallManager.this.showTipsOnUITop(str3);
            }
        }

        public void OnCallRemoteMergerEvent(String str, int i, CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto) {
            super.OnCallRemoteMergerEvent(str, i, cmmSIPCallRemoteMemberProto);
            if (!TextUtils.isEmpty(str)) {
                CmmSIPCallManager.this.showTipsOnRemoteMergerEvent(str, i, cmmSIPCallRemoteMemberProto);
                CmmSIPCallManager.this.playToneOnRemoveMergerEvent(i);
            }
        }

        public void OnSwitchCallToCarrierResult(String str, boolean z, int i) {
            int i2;
            int i3;
            super.OnSwitchCallToCarrierResult(str, z, i);
            CmmSIPCallManager.this.mSwitchingCallIds.remove(str);
            VideoBoxApplication nonNullInstance = VideoBoxApplication.getNonNullInstance();
            if (nonNullInstance != null && !z) {
                switch (i) {
                    case 100:
                        i3 = C4558R.string.zm_pbx_switch_to_carrier_error_100_102668;
                        i2 = C4558R.string.zm_pbx_switch_to_carrier_error_100_des_102668;
                        break;
                    case 101:
                        i3 = C4558R.string.zm_pbx_switch_to_carrier_error_101_102668;
                        i2 = C4558R.string.zm_pbx_switch_to_carrier_error_101_des_102668;
                        break;
                    default:
                        i3 = C4558R.string.zm_pbx_switch_to_carrier_error_102668;
                        i2 = C4558R.string.zm_pbx_switch_to_carrier_error_des_102668;
                        break;
                }
                CmmSIPCallManager.this.showErrorDialog(nonNullInstance.getString(i3), nonNullInstance.getString(i2), 0);
            }
        }

        public void OnCallActionResult(@NonNull String str, int i, boolean z) {
            super.OnCallActionResult(str, i, z);
            if (z) {
                switch (i) {
                    case 1:
                    case 2:
                    case 3:
                        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
                        if (instance.getCallItemByCallID(str) != null) {
                            instance.showSipInCallUI(str);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            super.OnPBXFeatureOptionsChanged(list);
            if (list != null && list.size() != 0 && ZMPhoneUtils.isPBXFeatureOptionChanged(list, CmmSIPCallManager.this.getLBREnabledBit()) && !CmmSIPCallManager.this.isLBREnabled()) {
                SIPCallEventListenerUI.getInstance().OnPBXServiceRangeChanged(0);
                SIPCallEventListenerUI.getInstance().OnPBXMediaModeUpdate(null, 0);
            }
        }

        public void OnPeerJoinMeetingResult(String str, long j, boolean z) {
            super.OnPeerJoinMeetingResult(str, j, z);
            if (!z && str != null && str.equals(CmmSIPCallManager.this.mTransferToMeetingCallId)) {
                CmmSIPCallManager.this.mTransferToMeetingCallId = null;
            }
        }

        public void OnMeetingStateChanged(int i) {
            super.OnMeetingStateChanged(i);
            if (i == 0) {
                CmmSIPCallManager.this.mTransferToMeetingCallId = null;
            }
        }
    };
    private int mServiceRangeState = 0;
    private Stack<String> mSipCallIds = new Stack<>();
    /* access modifiers changed from: private */
    public HashSet<String> mSipCallIdsInCallOffhook = new HashSet<>(3);
    private SipInCallFloatViewHelper mSipInCallFloatViewHelper;
    /* access modifiers changed from: private */
    public HashSet<String> mSwitchingCallIds = new HashSet<>(3);
    /* access modifiers changed from: private */
    public String mTransferToMeetingCallId = null;
    private Set<String> mUpgradeMeetingCallIds = new HashSet(3);

    public interface CallPeerResult {
        public static final int Result_Fail_Conf_Is_Running = -2;
        public static final int Result_Fail_NULL = -3;
        public static final int Result_Fail_Normal = -1;
        public static final int Result_Fail_OVER_MAX_COUNT = -4;
        public static final int Result_Fail_Other_Ring = -7;
        public static final int Result_Fail_PeerUri_EMPTY = -6;
        public static final int Result_Fail_PeerUri_Failed = -8;
        public static final int Result_Fail_Phone_Call_Confirm = -11;
        public static final int Result_Fail_Phone_Call_Confirm_UI_NULL = -11;
        public static final int Result_Fail_Phone_Call_Offhook = -10;
        public static final int Result_Fail_Register_Status_Error = -9;
        public static final int Result_Fail_USER_ERROR = -5;
        public static final int Result_Ok = 0;
    }

    public boolean isValidInCallStatus(int i) {
        return i == 28 || i == 26;
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onPTAppEvent(int i, long j) {
    }

    public String getPendingNumber() {
        return this.mPendingNumber;
    }

    public void setPendingNumber(String str) {
        this.mPendingNumber = str;
    }

    public static boolean isInit() {
        return mInstance != null;
    }

    private CmmSIPCallManager() {
        if (!VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            addListener(this.mSIPCallEventListener);
            CmmSipAudioMgr instance = CmmSipAudioMgr.getInstance();
            instance.init();
            addListener(instance);
            CmmSIPLineManager.getInstance().init();
            startToListenNetworkStatus();
            PTUI.getInstance().addPTUIListener(this);
            CmmSIPNosManager instance2 = CmmSIPNosManager.getInstance();
            instance2.init();
            addListener(instance2);
            SipCallTimeoutMgr instance3 = SipCallTimeoutMgr.getInstance();
            instance3.init();
            addListener(instance3);
            VideoBoxApplication.getNonNullInstance().addConfProcessListener(this.mConfProcessListener);
        }
    }

    public String getRegisterErrorString() {
        return CmmSIPLineManager.getInstance().getRegisterErrorString();
    }

    public boolean isShowSipRegisterError() {
        return CmmSIPLineManager.getInstance().isShowSipRegisterError();
    }

    public boolean isSipRegisterError() {
        boolean z = false;
        if (CmmSIPLineManager.getInstance().getMineExtensionLineRegResult() == null || PTApp.getInstance().getSipCallAPI() == null) {
            return false;
        }
        CmmSIPCallRegResult mineExtensionLineRegResult = CmmSIPLineManager.getInstance().getMineExtensionLineRegResult();
        if ((mineExtensionLineRegResult != null ? mineExtensionLineRegResult.getRegStatus() : 0) == 5) {
            z = true;
        }
        return z;
    }

    public boolean isSipError() {
        return !NetworkUtil.hasDataNetwork(VideoBoxApplication.getGlobalContext()) || isSipRegisterError();
    }

    public static CmmSIPCallManager getInstance() {
        synchronized (CmmSIPLineManager.class) {
            if (mInstance == null) {
                mInstance = new CmmSIPCallManager();
            }
        }
        return mInstance;
    }

    @Nullable
    public String getRecordErrorString(int i) {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext == null) {
            return null;
        }
        switch (i) {
            case 1:
                return globalContext.getString(C4558R.string.zm_sip_recording_incorrect_state_37980);
            case 2:
                return globalContext.getString(C4558R.string.zm_sip_recording_same_request_in_progress_37980);
            case 3:
                return globalContext.getString(C4558R.string.zm_sip_recording_disabled_37980);
            case 4:
                return globalContext.getString(C4558R.string.zm_sip_recording_internal_error_37980);
            default:
                return globalContext.getString(C4558R.string.zm_sip_recording_internal_error_37980);
        }
    }

    public void addListener(ISIPCallEventListener iSIPCallEventListener) {
        if (iSIPCallEventListener != null) {
            SIPCallEventListenerUI.getInstance().addListener(iSIPCallEventListener);
        }
    }

    public void removeListener(ISIPCallEventListener iSIPCallEventListener) {
        if (iSIPCallEventListener != null) {
            SIPCallEventListenerUI.getInstance().removeListener(iSIPCallEventListener);
        }
    }

    public boolean isCallMuted() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isCallMuted();
    }

    public boolean isSpeakerMuted() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isSpeakerMuted();
    }

    public void onSipCallStatusChange(int i, String str) {
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService != null) {
            try {
                confService.onSipCallEvent(i, str);
            } catch (RemoteException unused) {
            }
        }
        if (i == 28) {
            onInCall(str);
        }
    }

    private void startToListenNetworkStatus() {
        if (this.mNetworkRecevier == null) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                this.mNetworkRecevier = new NetworkStatusReceiver(globalContext);
                this.mNetworkRecevier.registerReceiver(globalContext);
                addNetworkListener(this.mNetworkListener);
            }
        }
    }

    public void addNetworkListener(SimpleNetworkStatusListener simpleNetworkStatusListener) {
        NetworkStatusReceiver networkStatusReceiver = this.mNetworkRecevier;
        if (networkStatusReceiver != null) {
            networkStatusReceiver.addListener(simpleNetworkStatusListener);
        }
    }

    public void removeNetworkListener(SimpleNetworkStatusListener simpleNetworkStatusListener) {
        NetworkStatusReceiver networkStatusReceiver = this.mNetworkRecevier;
        if (networkStatusReceiver != null) {
            networkStatusReceiver.removeListener(simpleNetworkStatusListener);
        }
    }

    private void sendSipCallStatusToConf(boolean z) {
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService != null) {
            try {
                confService.onSipStatusEvent(z);
            } catch (RemoteException unused) {
            }
        }
    }

    public void onZoomLoginFinished() {
        if (isSipCallEnabled()) {
            initSipIPC();
            if (!isLoginConflict()) {
                CmmPBXCallHistoryManager.getInstance().setEventSink();
            }
            if (PTApp.getInstance().isWebSignedOn()) {
                initSIPCall();
            }
            fetchMySelfProfile();
        }
    }

    private void fetchMySelfProfile() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                zoomMessenger.fetchUserProfileByJid(myself.getJid());
            }
        }
    }

    public void initSipIPC() {
        if (!AssistantAppClientMgr.getInstance().isInit()) {
            AssistantAppClientMgr.getInstance().init();
        }
    }

    public void onZoomLogoutFinished() {
        AssistantAppClientMgr.getInstance().unInit();
    }

    public void logout() {
        hangupAllCalls();
        CmmSIPNosManager.getInstance().releaseInboundCallWithCancel();
        CmmSIPNosManager.getInstance().cancelNosSIPCall();
        CmmSIPNosManager.getInstance().clear();
        SipCallTimeoutMgr.getInstance().stopAllSipCallTimeout();
        this.mCallItemLocal = null;
        this.mHasNewMessage = false;
        this.mMeetingCallIdCache.clear();
        this.mSipCallIdsInCallOffhook.clear();
        CmmSIPNosManager.getInstance().clearNosSIPCallItemInCallOffhookCache();
        clearSipCache();
        CmmSIPLineManager.getInstance().clearLineCache();
        checkHideSipFloatWindow();
        CmmSIPMessageManager.getInstance().release();
        unRegistrar();
        unloadSIPService();
        uninitSIPCallApi();
        AssistantAppClientMgr.getInstance().notifyAppStop();
        AssistantAppClientMgr.getInstance().dispatchIdleMessage();
        PTApp.getInstance().dispatchIdleMessage();
    }

    public void checkShowSipFloatWindow() {
        checkUpdateSipFloatWindow(true);
    }

    public void checkHideSipFloatWindow() {
        checkUpdateSipFloatWindow(false);
    }

    public void checkUpdateSipFloatWindow(boolean z) {
        if (!z) {
            SipInCallFloatViewHelper sipInCallFloatViewHelper = this.mSipInCallFloatViewHelper;
            if (sipInCallFloatViewHelper != null) {
                sipInCallFloatViewHelper.checkToUpdate();
            }
        } else if (!hasSipCallsInCache()) {
            SipInCallFloatViewHelper sipInCallFloatViewHelper2 = this.mSipInCallFloatViewHelper;
            if (sipInCallFloatViewHelper2 != null) {
                sipInCallFloatViewHelper2.checkToUpdate();
            }
        } else {
            if (this.mSipInCallFloatViewHelper == null) {
                this.mSipInCallFloatViewHelper = new SipInCallFloatViewHelper();
            }
            this.mSipInCallFloatViewHelper.show();
        }
    }

    public void onSipActivated() {
        if (!VideoBoxApplication.getNonNullInstance().isSDKMode() && isSipCallEnabled() && PTApp.getInstance().isWebSignedOn() && isSipCallEnabled() && !isLoginConflict()) {
            checkUpdateSipNotification();
            checkUserPbxInfo();
            if (!isSipCallExists()) {
                CmmSIPLineManager.getInstance().suspendToResume();
            }
            SipInCallFloatViewHelper sipInCallFloatViewHelper = this.mSipInCallFloatViewHelper;
            if (sipInCallFloatViewHelper != null) {
                sipInCallFloatViewHelper.onUIActivated();
            }
        }
    }

    public void onSipInactivated() {
        if (!VideoBoxApplication.getNonNullInstance().isSDKMode() && isSipCallEnabled()) {
            checkUpdateSipNotification();
            if (!isSipCallExists()) {
                CmmSIPLineManager.getInstance().resumeToSuspend();
            }
            SipInCallFloatViewHelper sipInCallFloatViewHelper = this.mSipInCallFloatViewHelper;
            if (sipInCallFloatViewHelper != null) {
                sipInCallFloatViewHelper.onUIInactivated();
            }
        }
    }

    public static void onConfUIMoveToFront() {
        getInstance().checkHideSipFloatWindow();
    }

    public static void onConfUIMoveToBackground() {
        getInstance().checkShowSipFloatWindow();
    }

    private void checkUserPbxInfo() {
        if (PTApp.getInstance().isWebSignedOn() && isSipCallEnabled() && !isLoginConflict()) {
            isSipInited();
            this.mHandler.removeMessages(191);
            this.mHandler.sendEmptyMessageDelayed(191, DELAY_REQUEST_USER_INFO_ON_SIP_ACTIVED);
        }
    }

    private void checkUpdateSipNotification() {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext != null) {
            if (hasSipCallsInCache()) {
                showSipNotification();
            } else {
                removeSipNotification();
            }
            if (getIncomingCall() == null && !CmmSIPNosManager.getInstance().isNosSIPCallRinging()) {
                NotificationMgr.removeSipIncomeNotification(globalContext);
                SipRingMgr.getInstance().stopRing();
            }
        }
    }

    public void onNetworkState(boolean z, int i, String str) {
        if (!VideoBoxApplication.getNonNullInstance().isSDKMode() && PTApp.getInstance().isWebSignedOn() && isSipCallEnabled() && !isLoginConflict()) {
            if (z) {
                checkUserPbxInfo();
            }
            if (isSipInited()) {
                ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
                if (sipCallAPI != null) {
                    sipCallAPI.notifyNetworkStateChanged(z ^ true ? 1 : 0, str);
                }
            }
        }
    }

    public boolean isLoginConflict() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isStreamConflict();
    }

    public void onSipCallServiceStoped() {
        sendSipCallStatusToConf(false);
        resetAudioDevice();
        checkUpdateSipNotification();
    }

    public void initSIPCall() {
        initSIPCallApi();
    }

    private boolean initSIPCallApi() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.initModule(getPlatformType(), SystemInfoHelper.getDeviceId(), NetworkUtil.getNetworkIP(VideoBoxApplication.getNonNullInstance()));
    }

    public static final String getPlatformType() {
        return String.format(UIUtil.isTablet(VideoBoxApplication.getGlobalContext()) ? "ZoomPbxPhone_Android_Pad(%s)" : "ZoomPbxPhone_Android_Phone(%s)", new Object[]{VideoBoxApplication.getNonNullInstance().getVersionName()});
    }

    public void initSIPCallWithoutWeblogin() {
        if (!isSipInited()) {
            ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
            if (sipCallAPI != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        sipCallAPI.initModuleForPushCall(myself.getJid(), getPlatformType(), SystemInfoHelper.getDeviceId(), NetworkUtil.getNetworkIP(VideoBoxApplication.getNonNullInstance()));
                    }
                }
            }
        }
    }

    private boolean uninitSIPCallApi() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        sipCallAPI.uninitModule();
        return true;
    }

    private boolean unloadSIPService() {
        CmmSIPLineManager.getInstance().clearRegisterResult();
        return unloadSIPServiceApi();
    }

    private boolean unloadSIPServiceApi() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return true;
        }
        return sipCallAPI.unloadSIPService();
    }

    public boolean queryUserPbxInfo() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.queryUserPbxInfo();
    }

    public long getPBXFeatureOptions() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getPBXFeatureOptions();
    }

    public long getSharedLineGroupEnabledBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getSharedLineGroupEnabledBit();
    }

    public boolean isEnableADHocCallRecording() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isEnableADHocCallRecording();
    }

    public long getADHocCallRecordingBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getADHocCallRecordingBit();
    }

    public long getUserInCallQueueBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getUserInCallQueueBit();
    }

    public boolean isInCallQueues() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isInCallQueues();
    }

    public long getReceiveCallsFromCallQueueBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getReceiveCallsFromCallQueueBit();
    }

    public boolean isReceiveCallsFromCallQueues() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isReceiveCallsFromCallQueues();
    }

    public int updateReceiveCallsFromCallQueues(boolean z, boolean z2) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 4;
        }
        return sipCallAPI.updateReceiveCallsFromCallQueues(z, z2);
    }

    public long getUserInSLGBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getUserInSLGBit();
    }

    public boolean isInSLG() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI != null) {
            return sipCallAPI.isInSLG();
        }
        return false;
    }

    public long getReceiveCallsFromSLGBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getReceiveCallsFromSLGBit();
    }

    public boolean isReceiveCallsFromSLG() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI != null) {
            return sipCallAPI.isReceiveCallsFromSLG();
        }
        return false;
    }

    public int updateReceiveCallsFromSLG(boolean z, boolean z2) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 4;
        }
        return sipCallAPI.updateReceiveCallsFromSLG(z, z2);
    }

    public long getLBREnabledBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getLBREnabledBit();
    }

    public boolean isLBREnabled() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isLBREnabled();
    }

    /* access modifiers changed from: private */
    public void unRegistrar() {
        CmmSIPLineManager.getInstance().unRegister();
    }

    /* access modifiers changed from: private */
    public void unRegistrarMyLine() {
        CmmSIPLineManager.getInstance().unRegistarExtLine();
    }

    private void updateCallHistory(String str, int i) {
        CmmSIPCallItem callItemByCallID = getInstance().getCallItemByCallID(str);
        if (callItemByCallID != null && !isCloudPBXEnabled() && isSipCallEnabled()) {
            updateLocalCallHistory(callItemByCallID, i);
        }
    }

    private void updateLocalCallHistory(CmmSIPCallItem cmmSIPCallItem, int i) {
        CallHistory callHistory = new CallHistory();
        callHistory.setType(3);
        String peerNumber = cmmSIPCallItem.getPeerNumber();
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(cmmSIPCallItem.getCountryCode());
        String sb2 = sb.toString();
        if (peerNumber != null && peerNumber.startsWith(sb2)) {
            peerNumber = peerNumber.substring(sb2.length());
        }
        boolean isIncomingCall = cmmSIPCallItem.isIncomingCall();
        callHistory.setNumber(peerNumber);
        callHistory.setId(cmmSIPCallItem.getCallID());
        if (isIncomingCall) {
            callHistory.setDirection(1);
            callHistory.setCallerDisplayName(getDisplayName(cmmSIPCallItem));
            callHistory.setCallerJid(cmmSIPCallItem.getCallID());
            callHistory.setCallerUri(cmmSIPCallItem.getPeerURI());
        } else {
            callHistory.setDirection(2);
            callHistory.setCalleeDisplayName(getDisplayName(cmmSIPCallItem));
            callHistory.setCalleeJid(cmmSIPCallItem.getCallID());
            callHistory.setCalleeUri(cmmSIPCallItem.getPeerURI());
        }
        long callStartTime = cmmSIPCallItem.getCallStartTime();
        long callGenerateTime = cmmSIPCallItem.getCallGenerateTime();
        int i2 = (callStartTime > 0 ? 1 : (callStartTime == 0 ? 0 : -1));
        callHistory.setTime(i2 == 0 ? callGenerateTime * 1000 : callStartTime * 1000);
        long time = (new Date().getTime() / 1000) - callStartTime;
        if (i2 == 0) {
            time = 0;
        }
        callHistory.setTimeLong(time);
        if (i2 > 0) {
            callHistory.setState(2);
        } else if (!isIncomingCall) {
            callHistory.setState(3);
        } else if (i == 5) {
            callHistory.setState(2);
        } else {
            callHistory.setState(1);
        }
        CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
        if (callHistoryMgr != null) {
            callHistoryMgr.addOrUpdateCallHistory(callHistory);
        }
    }

    private void updateCloudCallHistory(CmmSIPCallItem cmmSIPCallItem) {
        updateLocalCallHistory(cmmSIPCallItem, 0);
    }

    @Nullable
    public ZoomBuddy getZoomBuddyBySipPhone(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        return zoomMessenger.getBuddyWithSipPhone(str);
    }

    public String getDisplayName(String str) {
        return getDisplayName(getInstance().getCallItemByCallID(str));
    }

    public String getDisplayName(CmmSIPCallItem cmmSIPCallItem) {
        String str;
        if (cmmSIPCallItem == null) {
            return "";
        }
        String str2 = null;
        if (cmmSIPCallItem.getCallGenerate() == 1) {
            str2 = cmmSIPCallItem.getPeerDisplayName();
            if (!StringUtil.isEmptyOrNull(str2) && !str2.equals(cmmSIPCallItem.getPeerNumber())) {
                return str2.trim();
            }
        }
        String peerNumber = cmmSIPCallItem.getPeerNumber();
        if (TextUtils.isEmpty(peerNumber)) {
            peerNumber = cmmSIPCallItem.getPeerURI();
        }
        if (!TextUtils.isEmpty(peerNumber)) {
            str2 = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(peerNumber);
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = cmmSIPCallItem.getPeerDisplayName();
        }
        if (TextUtils.isEmpty(str2)) {
            str = cmmSIPCallItem.getPeerFormatNumber();
            if (TextUtils.isEmpty(str)) {
                str = peerNumber;
            }
        } else {
            str = str2;
        }
        return !StringUtil.isEmptyOrNull(str) ? str.trim() : "";
    }

    public void refreshVCardByCallId(@Nullable String str) {
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        if (callItemByCallID != null) {
            refreshVCardByNumber(callItemByCallID.getPeerNumber());
        }
    }

    public void refreshVCardByNumber(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(str);
            if (zoomBuddyByNumber != null) {
                String jid = zoomBuddyByNumber.getJid();
                if (!TextUtils.isEmpty(jid)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        zoomMessenger.refreshBuddyVCard(jid, true);
                        zoomMessenger.refreshBuddyBigPicture(jid);
                    }
                }
            }
        }
    }

    public String getSipCallDisplayName(CmmSIPCallItem cmmSIPCallItem) {
        String str;
        if (cmmSIPCallItem == null) {
            return "";
        }
        String str2 = " & ";
        if (!cmmSIPCallItem.isInConference() || cmmSIPCallItem.getConferenceRole() != 0) {
            String displayName = getDisplayName(cmmSIPCallItem);
            String remoteMembersDisplayName = getRemoteMembersDisplayName(cmmSIPCallItem);
            if (!TextUtils.isEmpty(remoteMembersDisplayName)) {
                StringBuilder sb = new StringBuilder();
                sb.append(displayName);
                sb.append(str2);
                sb.append(remoteMembersDisplayName);
                str = sb.toString();
            } else {
                str = displayName;
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getDisplayName(cmmSIPCallItem));
            String remoteMembersDisplayName2 = getRemoteMembersDisplayName(cmmSIPCallItem);
            if (!TextUtils.isEmpty(remoteMembersDisplayName2)) {
                sb2.append(str2);
                sb2.append(remoteMembersDisplayName2);
            }
            int conferenceParticipantsCount = cmmSIPCallItem.getConferenceParticipantsCount();
            for (int i = 0; i < conferenceParticipantsCount; i++) {
                CmmSIPCallItem callItemByCallID = getCallItemByCallID(cmmSIPCallItem.getConferenceParticipantCallItemByIndex(i));
                if (callItemByCallID != null) {
                    String displayName2 = getDisplayName(callItemByCallID);
                    if (!TextUtils.isEmpty(displayName2)) {
                        sb2.append(str2);
                        sb2.append(displayName2);
                    }
                    String remoteMembersDisplayName3 = getRemoteMembersDisplayName(cmmSIPCallItem);
                    if (!TextUtils.isEmpty(remoteMembersDisplayName3)) {
                        sb2.append(str2);
                        sb2.append(remoteMembersDisplayName3);
                    }
                }
            }
            str = sb2.toString();
        }
        if (StringUtil.isEmptyOrNull(str)) {
            str = cmmSIPCallItem.getCallID();
        }
        return str;
    }

    @Nullable
    private String getRemoteMembersDisplayName(@Nullable CmmSIPCallItem cmmSIPCallItem) {
        if (cmmSIPCallItem == null) {
            return null;
        }
        List remoteMergerMembers = cmmSIPCallItem.getRemoteMergerMembers();
        if (remoteMergerMembers == null || remoteMergerMembers.isEmpty()) {
            return null;
        }
        String str = " & ";
        StringBuilder sb = new StringBuilder();
        int size = remoteMergerMembers.size();
        for (int i = 0; i < size; i++) {
            sb.append(getRemoteMemberDisplayName((CmmSIPCallRemoteMemberProto) remoteMergerMembers.get(i)));
            if (i < size - 1) {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    @Nullable
    public String getRemoteMemberDisplayName(@Nullable CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto) {
        if (cmmSIPCallRemoteMemberProto == null) {
            return null;
        }
        String displayNameByNumber = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(cmmSIPCallRemoteMemberProto.getNumber());
        if (TextUtils.isEmpty(displayNameByNumber)) {
            displayNameByNumber = cmmSIPCallRemoteMemberProto.getDisplayName();
        }
        if (TextUtils.isEmpty(displayNameByNumber)) {
            displayNameByNumber = cmmSIPCallRemoteMemberProto.getDisplayNumber();
        }
        return displayNameByNumber;
    }

    public boolean isOldAccount() {
        return !isCloudPBXEnabled() && isSipCallEnabled();
    }

    public boolean hangupCall(CmmSIPCallItem cmmSIPCallItem) {
        if (cmmSIPCallItem == null) {
            return false;
        }
        return hangupCall(cmmSIPCallItem.getCallID());
    }

    public boolean hangupCall() {
        return hangupCall(getCurrentCallID());
    }

    public boolean hangupCall(String str) {
        if (str == null) {
            return false;
        }
        CmmSIPCallItemLocal cmmSIPCallItemLocal = this.mCallItemLocal;
        if (cmmSIPCallItemLocal == null || !str.equals(cmmSIPCallItemLocal.getCallID())) {
            return handleCallWithReason(str, 7, 10);
        }
        this.mCallItemLocal = null;
        SIPCallEventListenerUI.getInstance().handleLocalCallTerminate(str, 1);
        return true;
    }

    public void hangupCallsWithoutCallId(String... strArr) {
        if (strArr != null && strArr.length != 0) {
            List asList = Arrays.asList(strArr);
            Iterator it = getSipCallIds().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (!asList.contains(str)) {
                    hangupCall(str);
                }
            }
        }
    }

    public boolean hangupAllCalls() {
        if (this.mCallItemLocal != null) {
            this.mCallItemLocal = null;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return true;
        }
        return sipCallAPI.hangupAllCalls();
    }

    public boolean handleCallWithReason(String str, int i, int i2) {
        StringUtil.isEmptyOrNull(str);
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.handleCall(str, i, i2);
    }

    public boolean handleRecording(String str, int i) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.handleRecording(str, i);
    }

    private int getMySelfCountryCode() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return 0;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return 0;
        }
        String profileCountryCode = myself.getProfileCountryCode();
        if (StringUtil.isEmptyOrNull(profileCountryCode)) {
            return 1;
        }
        return Integer.parseInt(profileCountryCode);
    }

    private String[] parseRegisterSipNo(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        String[] split = str.split("-");
        if (split.length < 2) {
            return null;
        }
        return new String[]{split[0], split[1]};
    }

    public String[] getCallerInfoForCallpeer() {
        String str;
        String str2;
        String[] strArr = new String[2];
        if (isOldAccount()) {
            SipPhoneIntegration sipIntergration = getSipIntergration();
            String valueOf = String.valueOf(getCountryCode());
            if (sipIntergration != null) {
                str2 = sipIntergration.getUserName();
            } else {
                str2 = "";
            }
            strArr[0] = valueOf;
            strArr[1] = str2;
            return strArr;
        } else if (isBlockedCallerIDSelected()) {
            strArr[0] = String.valueOf(getCountryCode());
            strArr[1] = "";
            return strArr;
        } else {
            String selectedLineId = getSelectedLineId();
            String str3 = null;
            if (StringUtil.isEmptyOrNull(selectedLineId)) {
                str = getCallFromNumber();
            } else {
                CmmSIPLine mineExtensionLine = CmmSIPLineManager.getInstance().getMineExtensionLine();
                str = (mineExtensionLine == null || !selectedLineId.equals(mineExtensionLine.getID())) ? null : getCallFromNumber();
                if (TextUtils.isEmpty(str)) {
                    CmmSIPLine lineItemByID = CmmSIPLineManager.getInstance().getLineItemByID(selectedLineId);
                    if (lineItemByID != null) {
                        str = lineItemByID.getOwnerNumber();
                        str3 = lineItemByID.getCountryCode();
                    }
                }
            }
            if (StringUtil.isEmptyOrNull(str)) {
                List callerIdList = getCallerIdList();
                if (callerIdList != null && !callerIdList.isEmpty()) {
                    str = ((PBXNumber) callerIdList.get(0)).getNumber();
                }
            }
            if (str3 == null) {
                str3 = String.valueOf(getCountryCode());
            }
            strArr[0] = str3;
            strArr[1] = str;
            return strArr;
        }
    }

    public String getCallerNumberForCallpeer() {
        String[] callerInfoForCallpeer = getCallerInfoForCallpeer();
        return (callerInfoForCallpeer == null || callerInfoForCallpeer.length != 2) ? "" : StringUtil.emptyIfNull(callerInfoForCallpeer[1]);
    }

    @Nullable
    public String getCallFromNumber() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration != null) {
            return sipCallConfigration.getCallFromNumber();
        }
        return null;
    }

    public boolean setCallFromNumber(String str) {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.setCallFromNumber(str);
    }

    public boolean isBlockedCallerIDSelected() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.isBlockedCallerIDSelected();
    }

    public boolean selectBlockedCallerID(boolean z) {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.selectBlockedCallerID(z);
    }

    public void toggleSpeakerState(boolean z) {
        CmmSipAudioMgr.getInstance().toggleSpeakerState(z);
    }

    public int callPeer(@Nullable String str) {
        if (str != null && !TextUtils.isDigitsOnly(str)) {
            ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
            if (zMPhoneNumberHelper != null) {
                String formatCalloutPeerUriVanityNumber = zMPhoneNumberHelper.formatCalloutPeerUriVanityNumber(str);
                if (!StringUtil.isSameString(str, formatCalloutPeerUriVanityNumber)) {
                    return callPeer(formatCalloutPeerUriVanityNumber, str);
                }
            }
        }
        return callPeer(str, (String) null);
    }

    public int callPeer(@Nullable String str, String str2) {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return -3;
        }
        return callPeer(str, ZMPhoneUtils.getSipNumberType(str), str2);
    }

    public int callPeer(@Nullable String str, int i, String str2) {
        int i2;
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return -3;
        }
        String[] callerInfoForCallpeer = getCallerInfoForCallpeer();
        try {
            i2 = Integer.parseInt(callerInfoForCallpeer[0]);
        } catch (Exception unused) {
            i2 = getCountryCode();
        }
        String str3 = callerInfoForCallpeer[1];
        CmmCallPeerDataBean cmmCallPeerDataBean = new CmmCallPeerDataBean();
        cmmCallPeerDataBean.setPeerUri(str);
        cmmCallPeerDataBean.setPeerName(str2);
        cmmCallPeerDataBean.setNumberType(i);
        cmmCallPeerDataBean.setDisplayNumber(str3);
        cmmCallPeerDataBean.setCountryCode(i2);
        cmmCallPeerDataBean.setAnonymous(TextUtils.isEmpty(str3));
        return callPeer(cmmCallPeerDataBean, true);
    }

    public int callPeer(@Nullable String str, String str2, boolean z) {
        int i;
        String[] callerInfoForCallpeer = getCallerInfoForCallpeer();
        try {
            i = Integer.parseInt(callerInfoForCallpeer[0]);
        } catch (Exception unused) {
            i = getCountryCode();
        }
        return callPeer(str, str2, callerInfoForCallpeer[1], i, z);
    }

    public int callPeer(@Nullable String str, String str2, @Nullable String str3, int i, boolean z) {
        int sipNumberType = ZMPhoneUtils.getSipNumberType(str);
        CmmCallPeerDataBean cmmCallPeerDataBean = new CmmCallPeerDataBean();
        cmmCallPeerDataBean.setAnonymous(TextUtils.isEmpty(str3));
        cmmCallPeerDataBean.setPeerUri(str);
        cmmCallPeerDataBean.setPeerName(str2);
        cmmCallPeerDataBean.setDisplayNumber(str3);
        cmmCallPeerDataBean.setCountryCode(i);
        cmmCallPeerDataBean.setNumberType(sipNumberType);
        return callPeer(cmmCallPeerDataBean, z);
    }

    public int callPeer(@NonNull final CmmCallPeerDataBean cmmCallPeerDataBean, boolean z) {
        if (StringUtil.isEmptyOrNull(cmmCallPeerDataBean.getPeerUri())) {
            return -6;
        }
        if (isPhoneCallOffHook()) {
            return -10;
        }
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (isNumberFailed(cmmCallPeerDataBean.getPeerUri())) {
            if (globalContext != null) {
                Toast.makeText(globalContext, C4558R.string.zm_sip_callout_invalid_number_27110, 0).show();
            }
            return -8;
        } else if (!PTApp.getInstance().isWebSignedOn() || !isSipCallEnabled() || isLoginConflict()) {
            return -5;
        } else {
            if (!isInMaxCallsCount()) {
                if (globalContext != null) {
                    Toast.makeText(globalContext, C4558R.string.zm_sip_callout_failed_27110, 1).show();
                }
                return -4;
            } else if (z && hasOtherRinging()) {
                return -7;
            } else {
                if (!getInstance().hasMeetings() || !CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
                    return confirmCallPeer(cmmCallPeerDataBean);
                }
                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                if (frontActivity == null) {
                    return -11;
                }
                ConfirmAlertDialog.show(frontActivity, frontActivity.getString(C4558R.string.zm_sip_callpeer_inmeeting_title_108086), frontActivity.getString(C4558R.string.zm_sip_callpeer_inmeeting_msg_108086), new SimpleOnButtonClickListener() {
                    public void onPositiveClick() {
                        CmmSIPCallManager.this.confirmCallPeer(cmmCallPeerDataBean);
                    }
                });
                return -11;
            }
        }
    }

    /* access modifiers changed from: private */
    public int confirmCallPeer(@NonNull CmmCallPeerDataBean cmmCallPeerDataBean) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return -3;
        }
        if (getSipIdCountInCache() <= 0) {
            toggleSpeakerState(false);
        }
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        String peerUri = cmmCallPeerDataBean.getPeerUri();
        String peerName = cmmCallPeerDataBean.getPeerName();
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        if (zMPhoneNumberHelper != null && zMPhoneNumberHelper.isValidPhoneNumber(peerUri) && (peerName == null || peerName.equals(peerUri))) {
            cmmCallPeerDataBean.setPeerName(ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(peerUri));
        }
        if (!isSipRegistered()) {
            callPeerInLocal(cmmCallPeerDataBean);
            return -9;
        }
        if (!sipCallAPI.callPeerWithData(cmmCallPeerDataBean)) {
            if (isNumberFailed(peerUri)) {
                return -8;
            }
            callPeerInLocal(cmmCallPeerDataBean);
        }
        setPreviousCalloutNumber(peerUri);
        return 0;
    }

    public boolean isNumberFailed(String str) {
        return this.mFailedNumber.contains(str);
    }

    private void callPeerInLocal(@NonNull CmmCallPeerDataBean cmmCallPeerDataBean) {
        CmmSIPCallItemLocal cmmSIPCallItemLocal = new CmmSIPCallItemLocal(cmmCallPeerDataBean);
        this.mCallItemLocal = cmmSIPCallItemLocal;
        SIPCallEventListenerUI.getInstance().handleLocalNewCallGenerate(cmmSIPCallItemLocal.getCallID(), cmmSIPCallItemLocal.getCallGenerateType());
    }

    public void checkCallPeerInLocal(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mCallItemLocal == null) {
                ZMLog.m286i(TAG, "checkCallPeerInLocal, mCallItemLocal is null, line_id:%s", str);
                return;
            }
            String selectedLineId = getSelectedLineId();
            if (TextUtils.isEmpty(selectedLineId)) {
                CmmSIPLine mineExtensionLine = CmmSIPLineManager.getInstance().getMineExtensionLine();
                if (mineExtensionLine != null) {
                    selectedLineId = mineExtensionLine.getID();
                }
            }
            if (!StringUtil.isEmptyOrNull(selectedLineId) && selectedLineId.equals(str)) {
                CmmCallPeerDataBean callPeerData = this.mCallItemLocal.getCallPeerData();
                if (callPeerData != null) {
                    callPeer(callPeerData, false);
                }
            }
        }
    }

    public void showSipInCallUI(String str) {
        refreshVCardByCallId(str);
        addSip2Cache(str);
        resetCurrentCall(str);
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext != null) {
            SipInCallActivity.show(globalContext);
            showSipNotification();
            if (OsUtil.isAtLeastM() && Settings.canDrawOverlays(VideoBoxApplication.getNonNullInstance())) {
                checkShowSipFloatWindow();
            }
        }
    }

    public void showSipNotification() {
        ZMServiceHelper.doServiceActionInFront(PTService.ACTION_SHOW_SIP_NOTIFICATION, PTService.class);
    }

    public void removeSipNotification() {
        VideoBoxApplication.getNonNullInstance().sendBroadcast(new Intent(PTService.ACTION_REMOVE_SIP_NOTIFICATION));
    }

    public boolean acceptAndHoldCall(String str) {
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        return handleCallWithReason(str, 2, 10);
    }

    public boolean acceptAndEndCall(String str) {
        CmmSIPCallItem currentCallItem = getCurrentCallItem();
        if (currentCallItem != null && currentCallItem.isInConference() && currentCallItem.getConferenceRole() == 0) {
            int conferenceParticipantsCount = currentCallItem.getConferenceParticipantsCount();
            for (int i = 0; i < conferenceParticipantsCount; i++) {
                hangupCall(currentCallItem.getConferenceParticipantCallItemByIndex(i));
            }
        }
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        return handleCallWithReason(str, 3, 10);
    }

    public boolean acceptCall(String str) {
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        return handleCallWithReason(str, 1, 10);
    }

    public boolean holdCall() {
        return holdCall(getCurrentCallID());
    }

    public boolean holdCall(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        String str2 = this.mTransferToMeetingCallId;
        if (str2 == null || !str2.equals(str)) {
            return handleCallWithReason(str, 5, 10);
        }
        return false;
    }

    public boolean checkHoldCurrentCall() {
        return checkHoldCall(getCurrentCallID());
    }

    public boolean checkHoldCall(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        if (callItemByCallID != null && !isInLocalHold(callItemByCallID)) {
            return holdCall(callItemByCallID.getCallID());
        }
        return false;
    }

    public boolean resumeCall(String str) {
        StringUtil.isEmptyOrNull(str);
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        return handleCallWithReason(str, 6, 10);
    }

    public void leaveMeeting() {
        stopMeeting(false);
    }

    public void endMeeting() {
        stopMeeting(true);
    }

    private void stopMeeting(boolean z) {
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService != null) {
            try {
                confService.leaveCurrentMeeting(z);
            } catch (RemoteException unused) {
            }
        }
    }

    public boolean declineCallWithBusy(String str) {
        return handleCallWithReason(str, 4, 0);
    }

    public boolean declineCallWithNone(String str) {
        return handleCallWithReason(str, 4, 10);
    }

    public boolean declineCallWithNotAvailable(String str) {
        return handleCallWithReason(str, 4, 1);
    }

    public boolean skipInCQ(String str) {
        return handleCallWithReason(str, 4, 2);
    }

    public void dismissCall(String str) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI != null) {
            sipCallAPI.dismiss(str);
            checkUpdateSipNotification();
        }
    }

    public boolean transferCall(String str, String str2, int i) {
        return transferCall(str, str2, ZMPhoneUtils.getSipNumberType(str2), i);
    }

    public boolean transferCall(String str, String str2, int i, int i2) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        if (i2 == 2) {
            CmmSipAudioMgr.getInstance().enablePhoneAudio();
        }
        return sipCallAPI.transferCall(str, str2, i, i2);
    }

    public boolean completeWarmTransfer(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.completeWarmTransfer(str);
    }

    public boolean cancelWarmTransfer() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.cancelWarmTransfer();
    }

    public long getCallElapsedTime(String str) {
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        if (callItemByCallID == null) {
            return 0;
        }
        return getCallElapsedTime(callItemByCallID);
    }

    public long getCallElapsedTime(CmmSIPCallItem cmmSIPCallItem) {
        if (cmmSIPCallItem == null) {
            return 0;
        }
        return (new Date().getTime() / 1000) - cmmSIPCallItem.getCallStartTime();
    }

    public boolean muteCall(boolean z) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.muteCall(z);
    }

    public boolean muteSpeaker(boolean z) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.muteSpeaker(z);
    }

    public boolean sendDTMF(String str, String str2) {
        if (StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.sendDTMF(str, str2);
    }

    public boolean isCallExists() {
        return hasSipCallsInCache() || CmmSIPNosManager.getInstance().isNosSIPCallRinging() || getIncomingCall() != null;
    }

    public boolean isSipCallExists() {
        return hasSipCallsInCache() || getIncomingCall() != null;
    }

    public boolean isInHold(String str) {
        return isInHold(getCallItemByCallID(str));
    }

    public boolean isInHold(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        int callStatus = cmmSIPCallItem.getCallStatus();
        if (callStatus == 27 || callStatus == 30 || callStatus == 31) {
            z = true;
        }
        return z;
    }

    public boolean isInLocalHold(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        int callStatus = cmmSIPCallItem.getCallStatus();
        if (callStatus == 27 || callStatus == 31) {
            z = true;
        }
        return z;
    }

    public boolean isInRemoteHold(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        if (cmmSIPCallItem.getCallStatus() == 30) {
            z = true;
        }
        return z;
    }

    public boolean isInBothHold(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        if (cmmSIPCallItem.getCallStatus() == 31) {
            z = true;
        }
        return z;
    }

    public boolean isAccepted(String str) {
        return isAccepted(getCallItemByCallID(str));
    }

    public boolean isAccepted(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        if (cmmSIPCallItem.getCallStatus() == 26) {
            z = true;
        }
        return z;
    }

    public boolean isTransferring(String str) {
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        if (callItemByCallID == null) {
            return false;
        }
        String relatedCallID = callItemByCallID.getRelatedCallID();
        if (!StringUtil.isEmptyOrNull(relatedCallID)) {
            CmmSIPCallItem callItemByCallID2 = getCallItemByCallID(relatedCallID);
            if (callItemByCallID2 != null) {
                int callStatus = callItemByCallID2.getCallStatus();
                for (int i : getValidSipCallStatus()) {
                    if (callStatus == i) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public boolean isTransferring(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        String relatedCallID = cmmSIPCallItem.getRelatedCallID();
        if (!StringUtil.isEmptyOrNull(relatedCallID) && containsInCache(relatedCallID)) {
            z = true;
        }
        return z;
    }

    public boolean hasOtherTransfering(CmmSIPCallItem cmmSIPCallItem) {
        if (cmmSIPCallItem != null && getSipIdCountInCache() > 2) {
            String callID = cmmSIPCallItem.getCallID();
            ArrayList arrayList = new ArrayList(this.mSipCallIds);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                String str = (String) arrayList.get(i);
                if (!str.equals(callID) && isTransferring(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInSipAudio() {
        CmmSIPCallItem currentCallItem = getCurrentCallItem();
        boolean z = false;
        if (currentCallItem == null) {
            return false;
        }
        int callStatus = currentCallItem.getCallStatus();
        if (callStatus == 28 || callStatus == 26 || callStatus == 33 || callStatus == 31 || callStatus == 23 || callStatus == 27 || callStatus == 30) {
            z = true;
        }
        return z;
    }

    public boolean isCallout(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return isCallout(getCallItemByCallID(str));
    }

    public boolean isCallout(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        if (cmmSIPCallItem.getCallGenerate() != 0) {
            z = true;
        }
        return z;
    }

    public boolean isIncoming(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return isIncoming(getCallItemByCallID(str));
    }

    public boolean isIncoming(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        int callStatus = cmmSIPCallItem.getCallStatus();
        if (callStatus == 15 || callStatus == 0) {
            z = true;
        }
        return z;
    }

    public boolean isCallExecutive(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return isCallExecutive(getCallItemByCallID(str));
    }

    public boolean isCallExecutive(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        if (cmmSIPCallItem.getThirdpartyType() == 1) {
            z = true;
        }
        return z;
    }

    public boolean isCallQueue(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return isCallQueue(getCallItemByCallID(str));
    }

    public boolean isCallQueue(CmmSIPCallItem cmmSIPCallItem) {
        boolean z = false;
        if (cmmSIPCallItem == null) {
            return false;
        }
        if (cmmSIPCallItem.getThirdpartyType() == 2) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void handleCallForUnavailable(@Nullable String str) {
        handleCallForUnavailable(str, false);
    }

    /* access modifiers changed from: private */
    public void handleCallForUnavailable(@Nullable String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            if (isCallQueue(str)) {
                skipInCQ(str);
                return;
            }
            boolean isCloudPBXEnabled = isCloudPBXEnabled();
            if (isCloudPBXEnabled) {
                CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
                if (callItemByCallID != null) {
                    String lineId = callItemByCallID.getLineId();
                    if (!TextUtils.isEmpty(lineId)) {
                        CmmSIPLine lineItemByID = CmmSIPLineManager.getInstance().getLineItemByID(lineId);
                        if (lineItemByID != null && !lineItemByID.isShared()) {
                            declineCallWithBusy(str);
                            return;
                        }
                    }
                }
            }
            if (isCloudPBXEnabled || !isSipCallEnabled() || z) {
                dismissCall(str);
            } else {
                declineCallWithBusy(str);
            }
        }
    }

    @Nullable
    public CmmSIPCallItem getIncomingCall() {
        List<CmmSIPCallItem> allCallItemListByStatus = getAllCallItemListByStatus(15);
        if (allCallItemListByStatus != null && !allCallItemListByStatus.isEmpty()) {
            for (CmmSIPCallItem cmmSIPCallItem : allCallItemListByStatus) {
                if (!cmmSIPCallItem.isDismissed()) {
                    return cmmSIPCallItem;
                }
            }
        }
        return null;
    }

    public boolean isInCall() {
        boolean z = false;
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return false;
        }
        if (getInCallItem() != null) {
            z = true;
        }
        return z;
    }

    public boolean isInCall(String str) {
        return isInCall(getCallItemByCallID(str));
    }

    public boolean isInCall(CmmSIPCallItem cmmSIPCallItem) {
        return isValidInCallStatus(cmmSIPCallItem != null ? cmmSIPCallItem.getCallStatus() : 21);
    }

    public boolean isCalling(String str) {
        return isCalling(getCallItemByCallID(str));
    }

    public boolean isCalling(CmmSIPCallItem cmmSIPCallItem) {
        int callStatus = cmmSIPCallItem != null ? cmmSIPCallItem.getCallStatus() : 21;
        return callStatus == 20 || callStatus == 15;
    }

    public boolean isRecordingStarted(String str) {
        return isRecordingStarted(getCallItemByCallID(str));
    }

    public boolean isRecordingStarted(CmmSIPCallItem cmmSIPCallItem) {
        return (cmmSIPCallItem != null ? cmmSIPCallItem.getCallRecordingStatus() : 0) == 1;
    }

    public boolean isRecordingIdle(String str) {
        return isRecordingStarted(getCallItemByCallID(str));
    }

    public boolean isRecordingIdle(CmmSIPCallItem cmmSIPCallItem) {
        return (cmmSIPCallItem != null ? cmmSIPCallItem.getCallRecordingStatus() : 0) == 0;
    }

    public void onCallTerminated(String str, int i) {
        onSipCallStatusChange(29, str);
        CmmSIPCallItemLocal cmmSIPCallItemLocal = this.mCallItemLocal;
        if (!(cmmSIPCallItemLocal == null || str == null || !str.equals(cmmSIPCallItemLocal.getCallID()))) {
            this.mCallItemLocal = null;
        }
        popCallId(str);
        syncCallCache();
        removeJoinMeetingRequest(str);
        this.mSwitchingCallIds.remove(str);
        String str2 = this.mTransferToMeetingCallId;
        if (str2 != null && str2.equals(str)) {
            if (hasMeetings()) {
                CmmSipAudioMgr.getInstance().forceSwitchAudioToMeeting();
            }
            this.mTransferToMeetingCallId = null;
        }
        checkUpdateSipNotification();
        updateCallHistory(str, i);
        checkSIPCallCacheInCallOffhook(str);
        checkHideSipFloatWindow();
        if (i != 1 && hasMeetings() && !ZMPhoneUtils.isInSipInCallUI() && !isInUpgradeToMeeting(str)) {
            Toast.makeText(VideoBoxApplication.getNonNullInstance(), VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_sip_end_108086), 1).show();
        }
        if (this.mSipCallIds.isEmpty()) {
            resetAudioDevice();
            UIUtil.stopProximityScreenOffWakeLock();
            this.mUpgradeMeetingCallIds.clear();
            this.mTransferToMeetingCallId = null;
        }
    }

    private boolean isInUpgradeToMeeting(String str) {
        if (TextUtils.isEmpty(str) || this.mUpgradeMeetingCallIds.isEmpty()) {
            return false;
        }
        return this.mUpgradeMeetingCallIds.contains(str);
    }

    private void checkSIPCallCacheInCallOffhook(String str) {
        if (!TextUtils.isEmpty(str)) {
            HashSet<String> hashSet = this.mSipCallIdsInCallOffhook;
            if (hashSet != null && !hashSet.isEmpty() && this.mSipCallIdsInCallOffhook.contains(str)) {
                CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
                if (callItemByCallID != null && !CmmSIPNosManager.getInstance().containsInNosSIPCallItemInCallOffhookCache()) {
                    Context globalContext = VideoBoxApplication.getGlobalContext();
                    if (globalContext != null) {
                        String displayName = getDisplayName(callItemByCallID);
                        if (TextUtils.isEmpty(displayName)) {
                            displayName = callItemByCallID.getPeerFormatNumber();
                        }
                        NotificationMgr.showMissedSipCallNotification(globalContext, callItemByCallID.getCallID(), new NotificationItem(displayName, globalContext.getString(C4558R.string.zm_sip_missed_sip_call_title_111899)));
                        this.mSipCallIdsInCallOffhook.remove(str);
                    }
                }
            }
        }
    }

    public int getCallCount() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getCallCount();
    }

    public boolean hasOtherRinging() {
        return hasOtherRinging("");
    }

    /* access modifiers changed from: private */
    public boolean hasOtherRinging(String str) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        if (this.mCallItemLocal != null || CmmSIPNosManager.getInstance().isNosSIPCallRinging()) {
            return true;
        }
        int callCount = sipCallAPI.getCallCount();
        for (int i = 0; i < callCount; i++) {
            CmmSIPCallItem callItemByIndex = sipCallAPI.getCallItemByIndex(i);
            if (callItemByIndex != null) {
                String callID = callItemByIndex.getCallID();
                if (callID == null || !callID.equals(str)) {
                    int callStatus = callItemByIndex.getCallStatus();
                    if (!callItemByIndex.isDismissed() && (callStatus == 20 || callStatus == 33 || callStatus == 15 || callStatus == 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isValidIncomingCall(String str) {
        return isCallItemValid(str) && isIncoming(str);
    }

    private boolean isCallItemValid(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        if (callItemByCallID == null) {
            return false;
        }
        return isCallItemValid(callItemByCallID);
    }

    private boolean isCallItemValid(CmmSIPCallItem cmmSIPCallItem) {
        return cmmSIPCallItem != null && !TextUtils.isEmpty(cmmSIPCallItem.getCallID()) && !TextUtils.isEmpty(cmmSIPCallItem.getPeerURI());
    }

    public void onCallIncoming(String str) {
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        if (callItemByCallID != null) {
            onCallIncoming(callItemByCallID.getCallID(), callItemByCallID.getPeerURI(), callItemByCallID.getPeerNumber(), callItemByCallID.getPeerDisplayName(), true);
        }
    }

    public void onCallIncoming(String str, String str2, String str3, String str4, boolean z) {
        if (z) {
            refreshVCardByCallId(str);
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                SipIncomeActivity.show(globalContext, str);
                if (NotificationMgr.showSipIncomeNotification(globalContext, str)) {
                    SipRingMgr.getInstance().startRing(VideoBoxApplication.getGlobalContext());
                }
            }
        }
    }

    public void showErrorDialog(String str, String str2, int i) {
        showErrorDialog(str, str2, i, 1000);
    }

    public void showErrorDialogImmediately(String str, String str2, int i) {
        showErrorDialog(str, str2, i, 0);
    }

    public void showErrorDialog(final String str, final String str2, final int i, long j) {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity == null || !frontActivity.isActive()) {
            IntegrationActivity.promptErrorConfirmMsg(VideoBoxApplication.getNonNullInstance(), str, str2, i, 0, true);
        } else {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    final ZMActivity frontActivity = ZMActivity.getFrontActivity();
                    if (frontActivity != null) {
                        if (((frontActivity instanceof SipInCallActivity) || (((frontActivity instanceof SimpleActivity) && (((SimpleActivity) frontActivity).findMainFragment() instanceof SipDialKeyboardFragment)) || ((frontActivity instanceof IMActivity) && ((IMActivity) frontActivity).isPhoneTabSelected()))) && frontActivity.getEventTaskManager() != null) {
                            frontActivity.getEventTaskManager().push(new EventAction() {
                                public void run(IUIElement iUIElement) {
                                    ErrorInfo errorInfo = new ErrorInfo(str, str2, i);
                                    errorInfo.setFinishActivityOnDismiss(false);
                                    ErrorMsgConfirmDialog.show(frontActivity, errorInfo);
                                }
                            });
                        }
                    }
                }
            }, j);
        }
    }

    private void onInCall(String str) {
        resetCurrentCall(str);
        onCallEstablished();
    }

    private void checkDefaultLockedCallerId(String str) {
        if (!TextUtils.isEmpty(str)) {
            CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
            if (callItemByCallID != null && callItemByCallID.getCallGenerate() == 1 && TextUtils.isEmpty(callItemByCallID.getSid()) && isValidInCallStatus(callItemByCallID.getCallStatus())) {
                setbackDefaultLockedCallFromNumber();
            }
        }
    }

    public void onCallEstablished() {
        sendSipCallStatusToConf(true);
    }

    public void resetAudioDevice() {
        CmmSipAudioMgr.getInstance().resetAudioDevice();
    }

    @Nullable
    public SipPhoneIntegration getSipIntergration() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return null;
        }
        return sipCallConfigration.getRegsiterInfo();
    }

    @Nullable
    public ISIPCallConfigration getSipCallConfigration() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getConfiguration();
    }

    @Nullable
    public String getSelectedLineId() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration != null) {
            return sipCallConfigration.getSelectedLineId();
        }
        return null;
    }

    @Nullable
    public CmmSIPLine getSelectedLine() {
        String selectedLineId = getSelectedLineId();
        if (TextUtils.isEmpty(selectedLineId)) {
            return null;
        }
        return CmmSIPLineManager.getInstance().getLineItemByID(selectedLineId);
    }

    public boolean isCallerIdLocked() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        boolean z = false;
        if (sipCallConfigration == null) {
            return false;
        }
        if (sipCallConfigration.getCallerIDMode() == 2) {
            z = true;
        }
        return z;
    }

    private void setbackDefaultLockedCallFromNumber() {
        PBXNumber defaultLockedCallerId = getDefaultLockedCallerId();
        if (defaultLockedCallerId != null) {
            setCallFromNumber(defaultLockedCallerId.getNumber());
            getInstance().selectBlockedCallerID(false);
            CmmSIPLineManager.getInstance().switchMimeExtensionLine();
        }
    }

    private PBXNumber getDefaultLockedCallerId() {
        List callerIdList = getCallerIdList();
        if (!CollectionsUtil.isCollectionEmpty(callerIdList)) {
            for (int i = 0; i < callerIdList.size(); i++) {
                PBXNumber pBXNumber = (PBXNumber) callerIdList.get(i);
                if (pBXNumber.getStatus() == 1) {
                    return pBXNumber;
                }
            }
        }
        return null;
    }

    public boolean updateWebSIPConfiguration() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        boolean z = false;
        if (currentUserProfile == null) {
            return false;
        }
        SipPhoneIntegration sipPhoneIntegration = currentUserProfile.getSipPhoneIntegration();
        if (sipPhoneIntegration == null) {
            return false;
        }
        SipPhoneIntegration sipIntergration = getSipIntergration();
        if (sipIntergration == null) {
            return false;
        }
        if (!StringUtil.isSameString(sipIntergration.getUserName(), sipPhoneIntegration.getUserName()) || !StringUtil.isSameString(sipIntergration.getDomain(), sipPhoneIntegration.getDomain()) || !StringUtil.isSameString(sipIntergration.getPassword(), sipPhoneIntegration.getPassword()) || !StringUtil.isSameString(sipIntergration.getAuthoriztionName(), sipPhoneIntegration.getAuthoriztionName()) || !StringUtil.isSameString(sipIntergration.getRegisterServer(), sipPhoneIntegration.getRegisterServer()) || !StringUtil.isSameString(sipIntergration.getProxyServer(), sipPhoneIntegration.getProxyServer()) || sipIntergration.getProtocol() != sipPhoneIntegration.getProtocol()) {
            z = true;
        }
        return z;
    }

    public boolean isSipCallEnabled() {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        ISIPCallConfigration configuration = sipCallAPI.getConfiguration();
        if (configuration == null) {
            return false;
        }
        return configuration.isSIPCallEnabled();
    }

    public boolean isCloudPBXEnabled() {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return false;
        }
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.isCloudPBXEnabled();
    }

    public boolean isSharedLineEnabled() {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return false;
        }
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.isSharedLineEnabled();
    }

    public boolean isSharedLineGroupEnabled() {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isSharedLineGroupEnabled();
    }

    public boolean isCloudPBXAvaliable() {
        return isCloudPBXEnabled() && isSipRegistered();
    }

    @Nullable
    public String getMyselfSipNo() {
        String str = null;
        if (!isSipCallEnabled()) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                str = myself.getSipPhoneNumber();
            }
        } else {
            SipPhoneIntegration sipIntergration = getSipIntergration();
            if (sipIntergration != null) {
                str = sipIntergration.getUserName();
            }
        }
        return str;
    }

    public boolean isInDND() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr == null) {
            return false;
        }
        return notificationSettingMgr.isInDND();
    }

    public boolean isInOffline() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        if (zoomMessenger == null) {
            return false;
        }
        if (zoomMessenger.getMyPresence() == 0) {
            z = true;
        }
        return z;
    }

    public boolean isSipAvailable() {
        return isSipCallEnabled() && isSipRegistered();
    }

    public boolean isSipInited() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isInited();
    }

    public boolean isSipRegistered() {
        boolean z = false;
        if (PTApp.getInstance().getSipCallAPI() == null) {
            return false;
        }
        CmmSIPCallRegResult mineExtensionLineRegResult = CmmSIPLineManager.getInstance().getMineExtensionLineRegResult();
        if ((mineExtensionLineRegResult != null ? mineExtensionLineRegResult.getRegStatus() : 0) == 6) {
            z = true;
        }
        return z;
    }

    public boolean isSipNotRegisted() {
        boolean z = true;
        if (PTApp.getInstance().getSipCallAPI() == null) {
            return true;
        }
        CmmSIPCallRegResult mineExtensionLineRegResult = CmmSIPLineManager.getInstance().getMineExtensionLineRegResult();
        int regStatus = mineExtensionLineRegResult != null ? mineExtensionLineRegResult.getRegStatus() : 0;
        if (!(regStatus == 0 || regStatus == 5)) {
            z = false;
        }
        return z;
    }

    public boolean hasMeetings() {
        int meetingState = CmmSIPAPI.getMeetingState();
        if ((meetingState == 2 || meetingState == 1) && VideoBoxApplication.getNonNullInstance().isConfProcessRunning()) {
            return true;
        }
        return false;
    }

    public static boolean isPhoneCallOffHook() {
        return isPhoneCallOffHook(VideoBoxApplication.getGlobalContext());
    }

    public static boolean isPhoneCallOffHook(@Nullable Context context) {
        return CmmSipAudioMgr.isCallOffHook(context);
    }

    @Nullable
    public CmmSIPCallItem getCallItemByCallID(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        CmmSIPCallItemLocal cmmSIPCallItemLocal = this.mCallItemLocal;
        if (cmmSIPCallItemLocal != null && str.equals(cmmSIPCallItemLocal.getCallID())) {
            return this.mCallItemLocal;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getCallItemByCallID(str);
    }

    @Nullable
    public CmmSIPCallItem getCallItemByIndex(int i) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getCallItemByIndex(i);
    }

    @Nullable
    public CmmSIPCallItem getCallItemByPeerUri(String str) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        CmmSIPCallItemLocal cmmSIPCallItemLocal = this.mCallItemLocal;
        if (cmmSIPCallItemLocal != null && str.equals(cmmSIPCallItemLocal.getPeerURI())) {
            return this.mCallItemLocal;
        }
        int callCount = sipCallAPI.getCallCount();
        for (int i = 0; i < callCount; i++) {
            CmmSIPCallItem callItemByIndex = sipCallAPI.getCallItemByIndex(i);
            if (callItemByIndex != null) {
                String peerURI = callItemByIndex.getPeerURI();
                if (peerURI != null && peerURI.equals(str)) {
                    return callItemByIndex;
                }
            }
        }
        return null;
    }

    @Nullable
    public CmmSIPCallItem getInCallItem() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        int callCount = sipCallAPI.getCallCount();
        for (int i = 0; i < callCount; i++) {
            CmmSIPCallItem callItemByIndex = sipCallAPI.getCallItemByIndex(i);
            if (callItemByIndex != null && isValidInCallStatus(callItemByIndex.getCallStatus())) {
                return callItemByIndex;
            }
        }
        return null;
    }

    public boolean holdInCall() {
        CmmSIPCallItem inCallItem = getInCallItem();
        if (inCallItem != null) {
            return getInstance().holdCall(inCallItem.getCallID());
        }
        return true;
    }

    @Nullable
    public List<CmmSIPCallItem> getAllCallItemListByStatus(int... iArr) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        CmmSIPCallItemLocal cmmSIPCallItemLocal = this.mCallItemLocal;
        boolean z = true;
        if (cmmSIPCallItemLocal != null) {
            int callStatus = cmmSIPCallItemLocal.getCallStatus();
            int i = 0;
            boolean z2 = false;
            while (true) {
                if (i >= iArr.length) {
                    z = z2;
                    break;
                }
                if (iArr[i] <= -1) {
                    z2 = true;
                } else if (callStatus == iArr[i]) {
                    break;
                }
                i++;
            }
        } else {
            z = false;
        }
        int callCount = sipCallAPI.getCallCount();
        ArrayList arrayList = new ArrayList(z ? callCount + 1 : callCount);
        if (z) {
            arrayList.add(this.mCallItemLocal);
        }
        for (int i2 = 0; i2 < callCount; i2++) {
            CmmSIPCallItem callItemByIndex = sipCallAPI.getCallItemByIndex(i2);
            if (callItemByIndex != null) {
                int callStatus2 = callItemByIndex.getCallStatus();
                int i3 = 0;
                while (true) {
                    if (i3 >= iArr.length) {
                        break;
                    }
                    if (iArr[i3] <= -1) {
                        arrayList.add(callItemByIndex);
                    } else if (callStatus2 == iArr[i3]) {
                        arrayList.add(callItemByIndex);
                        break;
                    }
                    i3++;
                }
            }
        }
        return arrayList;
    }

    public List<CmmSIPCallItem> getAllCallItemList() {
        return getAllCallItemListByStatus(-1);
    }

    @Nullable
    public List<CmmSIPCallItem> getAllCallItemListWithoutCallId(String... strArr) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        CmmSIPCallItemLocal cmmSIPCallItemLocal = this.mCallItemLocal;
        boolean z = true;
        if (cmmSIPCallItemLocal != null) {
            String callID = cmmSIPCallItemLocal.getCallID();
            if (strArr != null && strArr.length > 0) {
                int i = 0;
                while (true) {
                    if (i >= strArr.length) {
                        z = false;
                        break;
                    } else if (!strArr[i].equals(callID)) {
                        break;
                    } else {
                        i++;
                    }
                }
            }
        } else {
            z = false;
        }
        int callCount = sipCallAPI.getCallCount();
        ArrayList arrayList = new ArrayList(z ? callCount + 1 : callCount);
        if (z) {
            arrayList.add(this.mCallItemLocal);
        }
        for (int i2 = 0; i2 < callCount; i2++) {
            CmmSIPCallItem callItemByIndex = sipCallAPI.getCallItemByIndex(i2);
            if (callItemByIndex != null) {
                String callID2 = callItemByIndex.getCallID();
                if (strArr == null || strArr.length <= 0) {
                    arrayList.add(callItemByIndex);
                } else {
                    for (String equals : strArr) {
                        if (!equals.equals(callID2)) {
                            arrayList.add(callItemByIndex);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public int getUnreadVoiceMailCount() {
        return PTApp.getInstance().getSipCallAPI() == null ? 0 : 0;
    }

    public void onAcceptMeeting() {
        if (!VideoBoxApplication.getNonNullInstance().isSDKMode() && isSipCallEnabled()) {
            CmmSipAudioMgr.getInstance().disablePhoneAudio();
        }
    }

    @Nullable
    public CloudPBX getCloudPBXInfo() {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            return null;
        }
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return null;
        }
        return sipCallConfigration.getCloudPBXInfo();
    }

    public boolean isSameCompanyWithLoginUser(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        String str2 = null;
        CloudPBX cloudPBXInfo = getInstance().getCloudPBXInfo();
        if (cloudPBXInfo != null) {
            str2 = cloudPBXInfo.getMainCompanyNumber();
        }
        return isSameCompany(str2, str);
    }

    public boolean isSameCompany(String str, String str2) {
        if (str != null) {
            str = str.replaceAll("[+\\s-_]*", "");
        }
        if (str2 != null) {
            str2 = str2.replaceAll("[[+\\s-_]]*", "");
        }
        if (str == null && str2 == null) {
            return true;
        }
        if (str != null) {
            return str.equals(str2);
        }
        return false;
    }

    @Nullable
    public List<PBXNumber> getCallerIdList() {
        CloudPBX cloudPBXInfo = getCloudPBXInfo();
        if (cloudPBXInfo == null) {
            return null;
        }
        return cloudPBXInfo.getCallerIDList();
    }

    @Nullable
    public List<String> getDirectNumberList() {
        CloudPBX cloudPBXInfo = getCloudPBXInfo();
        if (cloudPBXInfo == null) {
            return null;
        }
        return cloudPBXInfo.getDirectNumberList();
    }

    public int getCountryCode() {
        if (isCloudPBXEnabled()) {
            CloudPBX cloudPBXInfo = getCloudPBXInfo();
            if (cloudPBXInfo != null) {
                String countryCode = cloudPBXInfo.getCountryCode();
                if (!StringUtil.isEmptyOrNull(countryCode)) {
                    try {
                        return Integer.parseInt(countryCode);
                    } catch (Exception unused) {
                    }
                }
            }
        }
        return 1;
    }

    public boolean isInMaxCallsCount() {
        return this.mSipCallIds.size() < 4;
    }

    public boolean isNeedRing(String str) {
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        return callItemByCallID != null && callItemByCallID.isNeedRing();
    }

    public boolean isMultiCalls() {
        return this.mSipCallIds.size() > 1;
    }

    public boolean isCallingout(String str) {
        CmmSIPCallItem callItemByCallID = getInstance().getCallItemByCallID(str);
        boolean z = false;
        if (callItemByCallID == null) {
            return false;
        }
        int callGenerate = callItemByCallID.getCallGenerate();
        int callStatus = callItemByCallID.getCallStatus();
        if (callGenerate != 0 && (callStatus == 0 || callStatus == 33 || callStatus == 5 || callStatus == 20)) {
            z = true;
        }
        return z;
    }

    public Stack<String> getSipCallIds() {
        Stack<String> stack = new Stack<>();
        Stack<String> stack2 = this.mSipCallIds;
        if (stack2 != null) {
            stack.addAll(stack2);
        }
        return stack;
    }

    public boolean containsInCache(String str) {
        if (this.mSipCallIds.size() <= 0) {
            return false;
        }
        if (this.mSipCallIds.contains(str)) {
            return true;
        }
        CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
        if (callItemByCallID == null || !callItemByCallID.isInConference() || callItemByCallID.getConferenceRole() != 1) {
            return false;
        }
        return this.mSipCallIds.contains(callItemByCallID.getConferenceHostCallid());
    }

    public void clearSipCacheBeforeSync() {
        this.mSipCallIds.clear();
        this.mCurrentIndexInCallCache = 0;
    }

    public void clearSipCache() {
        this.mSipCallIds.clear();
        this.mCurrentIndexInCallCache = 0;
        this.mMediaOffLoadCalls.clear();
    }

    public void addSip2Cache(String str) {
        if (!StringUtil.isEmptyOrNull(str) && !this.mSipCallIds.contains(str)) {
            this.mSipCallIds.push(str);
            this.mCurrentIndexInCallCache = Math.max(this.mSipCallIds.size() - 1, 0);
        }
    }

    private boolean canAdd2Cache(String str) {
        CmmSIPCallItem callItemByCallID = getInstance().getCallItemByCallID(str);
        if (callItemByCallID == null) {
            return false;
        }
        return canAdd2Cache(callItemByCallID);
    }

    private boolean canAdd2Cache(CmmSIPCallItem cmmSIPCallItem) {
        return cmmSIPCallItem != null && (!cmmSIPCallItem.isInConference() || cmmSIPCallItem.getConferenceRole() == 0) && !cmmSIPCallItem.isDismissed() && isValidCacheSipCallStatus(cmmSIPCallItem);
    }

    public boolean isCurrentCallLocal() {
        return CmmSIPCallItem.isLocal(getCurrentCallID());
    }

    @Nullable
    public String getCurrentCallID() {
        if (this.mSipCallIds.isEmpty()) {
            return null;
        }
        return (String) this.mSipCallIds.get(this.mCurrentIndexInCallCache);
    }

    @Nullable
    public CmmSIPCallItem getCurrentCallItem() {
        String currentCallID = getCurrentCallID();
        if (!StringUtil.isEmptyOrNull(currentCallID)) {
            return getCallItemByCallID(currentCallID);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void popCallId(String str) {
        if (!this.mSipCallIds.isEmpty()) {
            if (this.mSipCallIds.contains(str)) {
                this.mSipCallIds.remove(str);
                this.mCurrentIndexInCallCache = Math.max(this.mSipCallIds.size() - 1, 0);
            }
            this.mMediaOffLoadCalls.remove(str);
        }
    }

    public void resetCurrentCall(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (!this.mSipCallIds.contains(str) && canAdd2Cache(str)) {
                addSip2Cache(str);
            }
            if (this.mSipCallIds.contains(str) && !str.equals(getCurrentCallID())) {
                ArrayList arrayList = new ArrayList(this.mSipCallIds);
                int size = arrayList.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        break;
                    } else if (str.equals((String) arrayList.get(i))) {
                        this.mCurrentIndexInCallCache = i;
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    public int getSipIdCountInCache() {
        return this.mSipCallIds.size();
    }

    public boolean hasSipCallsInCache() {
        return getSipIdCountInCache() > 0;
    }

    public int getCurrentIndexInCallCache() {
        return this.mCurrentIndexInCallCache;
    }

    public boolean hasConference() {
        ArrayList arrayList = new ArrayList(this.mSipCallIds);
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            CmmSIPCallItem callItemByCallID = getCallItemByCallID((String) arrayList.get(i));
            if (callItemByCallID != null && callItemByCallID.isInConference()) {
                return true;
            }
        }
        return false;
    }

    public boolean mergeCall(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        return sipCallAPI.mergeCall(str, str2);
    }

    private void syncCallCache() {
        List allCallItemList = getAllCallItemList();
        ArrayList arrayList = new ArrayList();
        String str = null;
        if (allCallItemList != null) {
            int size = allCallItemList.size();
            for (int i = 0; i < size; i++) {
                CmmSIPCallItem cmmSIPCallItem = (CmmSIPCallItem) allCallItemList.get(i);
                String callID = cmmSIPCallItem.getCallID();
                if (!TextUtils.isEmpty(callID) && canAdd2Cache(cmmSIPCallItem)) {
                    arrayList.add(callID);
                    if (isValidInCallStatus(cmmSIPCallItem.getCallStatus())) {
                        str = callID;
                    }
                }
            }
        }
        syncCallCache(arrayList);
        if (!TextUtils.isEmpty(str) && !TextUtils.equals(str, (CharSequence) arrayList.get(arrayList.size() - 1))) {
            resetCurrentCall(str);
        }
    }

    private void syncCallCache(List<String> list) {
        clearSipCacheBeforeSync();
        if (!CollectionsUtil.isListEmpty(list)) {
            this.mSipCallIds.addAll(list);
            this.mCurrentIndexInCallCache = list.size() - 1;
            return;
        }
        this.mCurrentIndexInCallCache = 0;
    }

    private int[] getValidSipCallStatus() {
        return new int[]{26, 33, 31, 28, 27, 30, 20, 0};
    }

    private boolean isValidCacheSipCallStatus(CmmSIPCallItem cmmSIPCallItem) {
        int callStatus = cmmSIPCallItem.getCallStatus();
        if (callStatus == 15) {
            int lastActionType = cmmSIPCallItem.getLastActionType();
            if (lastActionType == 3 || lastActionType == 1 || lastActionType == 2) {
                return true;
            }
        }
        return isValidCacheSipCallStatus(callStatus);
    }

    private boolean isValidCacheSipCallStatus(int i) {
        for (int i2 : getValidSipCallStatus()) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public boolean isE911ServicePromptReaded() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.isE911ServicePromptReaded();
    }

    public boolean setE911ServicePromptAsReaded() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.setE911ServicePromptAsReaded(true);
    }

    @Nullable
    public String getPreviousCalloutNumber() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return null;
        }
        return sipCallConfigration.getPreviousCalloutPhonenumber();
    }

    public boolean setPreviousCalloutNumber(String str) {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.setPreviousCalloutPhonenumber(str);
    }

    public boolean startMeeting(String str) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.startMeeting(str);
    }

    public List<String> upgradeToMeeting(String str, long j, String str2) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        ArrayList arrayList = new ArrayList();
        if (sipCallAPI != null && !sipCallAPI.upgradeToMeeting(str, j, str2)) {
            arrayList.add(str);
        }
        return arrayList;
    }

    public int transferToMeeting(String str) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 3;
        }
        int transferToMeeting = sipCallAPI.transferToMeeting(str);
        if (transferToMeeting == 0) {
            this.mTransferToMeetingCallId = str;
            this.mUpgradeMeetingCallIds.add(str);
        }
        return transferToMeeting;
    }

    /* access modifiers changed from: private */
    public void onMeetingStartedResult(String str, long j, String str2, boolean z) {
        if (z) {
            resetUpgradeToMeetingCache(str);
            List upgradeToMeeting = upgradeToMeeting(str, j, str2);
            if (VideoBoxApplication.getGlobalContext() != null && !CollectionsUtil.isListEmpty(upgradeToMeeting)) {
                int size = upgradeToMeeting.size();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    sb.append(getDisplayName((String) upgradeToMeeting.get(i)));
                    sb.append(PreferencesConstants.COOKIE_DELIMITER);
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    Toast.makeText(VideoBoxApplication.getGlobalContext(), VideoBoxApplication.getGlobalContext().getString(C4558R.string.zm_sip_upgrade_to_meeting_failed_with_name_53992, new Object[]{sb.toString()}), 1).show();
                }
            }
        } else if (VideoBoxApplication.getGlobalContext() != null) {
            String displayName = getDisplayName(str);
            Toast.makeText(VideoBoxApplication.getGlobalContext(), VideoBoxApplication.getGlobalContext().getString(C4558R.string.zm_sip_upgrade_to_meeting_failed_with_name_53992, new Object[]{displayName}), 1).show();
        }
    }

    private void resetUpgradeToMeetingCache(String str) {
        if (!this.mUpgradeMeetingCallIds.isEmpty()) {
            for (String str2 : new ArrayList(this.mUpgradeMeetingCallIds)) {
                if (getCallItemByCallID(str2) == null) {
                    this.mUpgradeMeetingCallIds.remove(str2);
                }
            }
        }
        if (!TextUtils.isEmpty(str)) {
            CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
            if (callItemByCallID != null) {
                this.mUpgradeMeetingCallIds.add(str);
                if (callItemByCallID.isInConference() && callItemByCallID.getConferenceRole() == 0) {
                    int conferenceParticipantsCount = callItemByCallID.getConferenceParticipantsCount();
                    for (int i = 0; i < conferenceParticipantsCount; i++) {
                        this.mUpgradeMeetingCallIds.add(callItemByCallID.getConferenceParticipantCallItemByIndex(i));
                    }
                }
            }
        }
    }

    public boolean joinMeeting(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        PBXJoinMeetingRequest joinMeetingRequest = getJoinMeetingRequest(str);
        if (joinMeetingRequest == null) {
            return false;
        }
        return joinMeeting(joinMeetingRequest.getCallId(), joinMeetingRequest.getMeetingNum(), joinMeetingRequest.getPwd());
    }

    public boolean joinMeeting(String str, long j, String str2) {
        return joinMeeting(str, j, str2, !isVideoTurnOffWhileJoinMeeting());
    }

    public boolean joinMeeting(String str, long j, String str2, boolean z) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.joinMeeting(str, j, str2, z);
    }

    /* access modifiers changed from: private */
    public void onReceivedJoinMeetingRequest(String str, long j, String str2) {
        addJoinMeetingRequest(str, new PBXJoinMeetingRequest(str, j, str2));
        String currentCallID = getCurrentCallID();
        if (currentCallID != null && currentCallID.equals(str)) {
            showJoinMeetingUI(str);
        }
    }

    @Nullable
    public PBXJoinMeetingRequest getJoinMeetingRequest(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        PBXJoinMeetingRequest pBXJoinMeetingRequest = (PBXJoinMeetingRequest) this.mMeetingCallIdCache.get(str);
        if (pBXJoinMeetingRequest == null) {
            CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
            if (callItemByCallID == null) {
                return null;
            }
            if (callItemByCallID.isInConference()) {
                int conferenceRole = callItemByCallID.getConferenceRole();
                if (conferenceRole == 1) {
                    pBXJoinMeetingRequest = (PBXJoinMeetingRequest) this.mMeetingCallIdCache.get(callItemByCallID.getConferenceHostCallid());
                } else if (conferenceRole == 0) {
                    int conferenceParticipantsCount = callItemByCallID.getConferenceParticipantsCount();
                    for (int i = 0; i < conferenceParticipantsCount; i++) {
                        pBXJoinMeetingRequest = (PBXJoinMeetingRequest) this.mMeetingCallIdCache.get(callItemByCallID.getConferenceParticipantCallItemByIndex(i));
                        if (pBXJoinMeetingRequest != null) {
                            break;
                        }
                    }
                }
            }
        }
        return pBXJoinMeetingRequest;
    }

    public void addJoinMeetingRequest(String str, PBXJoinMeetingRequest pBXJoinMeetingRequest) {
        this.mMeetingCallIdCache.put(str, pBXJoinMeetingRequest);
    }

    public void removeJoinMeetingRequest(String str) {
        this.mMeetingCallIdCache.remove(str);
    }

    public boolean isInJoinMeeingRequest(String str) {
        return getJoinMeetingRequest(str) != null;
    }

    public boolean showJoinMeetingUI(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        PBXJoinMeetingRequest joinMeetingRequest = getJoinMeetingRequest(str);
        if (joinMeetingRequest == null) {
            return false;
        }
        showJoinMeetingUI(joinMeetingRequest.getCallId(), joinMeetingRequest.getMeetingNum(), joinMeetingRequest.getPwd());
        return true;
    }

    public void showJoinMeetingUI(String str, long j, String str2) {
        String str3;
        if (!TextUtils.isEmpty(str)) {
            CmmSIPCallItem callItemByCallID = getCallItemByCallID(str);
            if (callItemByCallID == null) {
                str3 = "";
            } else {
                str3 = callItemByCallID.getPeerNumber();
            }
            String emptyIfNull = StringUtil.emptyIfNull(str3);
            ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(emptyIfNull);
            String emptyIfNull2 = StringUtil.emptyIfNull(zoomBuddyByNumber != null ? zoomBuddyByNumber.getJid() : "");
            IncomingCallManager.getInstance().onConfInvitation(InvitationItem.newBuilder().setCallerPhoneNumber(emptyIfNull).setFromUserID(emptyIfNull2).setFromUserScreenName(StringUtil.emptyIfNull(getDisplayName(callItemByCallID))).setIsAudioOnlyMeeting(false).setIsShareOnlyMeeting(false).setMeetingNumber(j).setPassword(StringUtil.emptyIfNull(str2)).setSenderJID(emptyIfNull2).setReceiverJID("").setPbxCallId(str).setMeetingId("").setMeetingOption(0).setFromUserDevice("").build());
        }
    }

    /* access modifiers changed from: private */
    public void onMeetingJoinedResult(String str, boolean z) {
        if (VideoBoxApplication.getGlobalContext() != null && !z) {
            Toast.makeText(VideoBoxApplication.getGlobalContext(), C4558R.string.zm_sip_join_meeting_failed_53992, 1).show();
        }
    }

    public boolean isVideoTurnOffWhileJoinMeeting() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isVideoTurnOffWhileJoinMeeting();
    }

    public void addPBXLoginConflictListener(PBXLoginConflictListener pBXLoginConflictListener) {
        if (pBXLoginConflictListener != null) {
            PBXLoginConflictListenerUI.getInstance().addListener(pBXLoginConflictListener);
        }
    }

    public void removePBXLoginConflictListener(PBXLoginConflictListener pBXLoginConflictListener) {
        if (pBXLoginConflictListener != null) {
            PBXLoginConflictListenerUI.getInstance().removeListener(pBXLoginConflictListener);
        }
    }

    public void uploadExceptionMemoryLog(int i, String str, String str2) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI != null) {
            sipCallAPI.uploadExceptionMemoryLog(i, str, str2);
        }
    }

    public boolean isPBXActive() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        return isCloudPBXEnabled() && sipCallConfigration != null && sipCallConfigration.getSIPUserStatus() == 4;
    }

    public boolean isPBXInactive() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        return isCloudPBXEnabled() && sipCallConfigration != null && sipCallConfigration.getSIPUserStatus() == 3;
    }

    public boolean checkNetwork(Context context) {
        if (NetworkUtil.hasDataNetwork(context)) {
            return true;
        }
        new Builder(context).setTitle(C4558R.string.zm_sip_error_network_unavailable_99728).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null).show();
        return false;
    }

    public boolean checkIsPbxActive(Context context) {
        if (!getInstance().isPBXInactive()) {
            return true;
        }
        new Builder(context).setTitle(C4558R.string.zm_sip_error_reg_403_99728).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null).show();
        return false;
    }

    public CmmSIPCallItem getOtherSIPCallItem(CmmSIPCallItem cmmSIPCallItem) {
        return getOtherSIPCallItem(cmmSIPCallItem, isTransferring(cmmSIPCallItem));
    }

    @Nullable
    public CmmSIPCallItem getOtherSIPCallItem(CmmSIPCallItem cmmSIPCallItem, boolean z) {
        String str;
        if (cmmSIPCallItem == null) {
            return null;
        }
        Stack sipCallIds = getSipCallIds();
        if (sipCallIds.size() != 2 && !z) {
            return null;
        }
        if (z) {
            str = cmmSIPCallItem.getRelatedCallID();
        } else {
            String str2 = (String) sipCallIds.get(0);
            str = str2.equals(cmmSIPCallItem.getCallID()) ? (String) sipCallIds.get(1) : str2;
        }
        return getCallItemByCallID(str);
    }

    @Nullable
    public String getCallerId(Context context, CmmSIPCallItem cmmSIPCallItem) {
        String str;
        String str2 = null;
        if (cmmSIPCallItem == null) {
            return null;
        }
        if (cmmSIPCallItem.isIncomingCall()) {
            str = cmmSIPCallItem.getCalledNumber();
            if (TextUtils.isEmpty(str)) {
                CmmSIPCallManager instance = getInstance();
                if (instance.isCloudPBXEnabled()) {
                    CloudPBX cloudPBXInfo = instance.getCloudPBXInfo();
                    if (cloudPBXInfo != null) {
                        str2 = cloudPBXInfo.getExtension();
                    }
                    str = str2;
                } else {
                    if (instance.isSipCallEnabled()) {
                        SipPhoneIntegration sipIntergration = instance.getSipIntergration();
                        if (sipIntergration != null) {
                            str = sipIntergration.getUserName();
                        }
                    }
                    str = null;
                }
            }
        } else {
            String callerID = cmmSIPCallItem.getCallerID();
            str = TextUtils.isEmpty(callerID) ? context.getString(C4558R.string.zm_sip_caller_id_hidden_64644) : callerID;
        }
        return str;
    }

    public boolean hasNewMessage() {
        return this.mHasNewMessage;
    }

    public void showTipsOnUITop(String str) {
        showTipsOnUITop(str, 5000, false);
    }

    public void showTipsOnUITop(final String str, final int i, final boolean z) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                if (!(frontActivity == null || frontActivity.getEventTaskManager() == null)) {
                    frontActivity.getEventTaskManager().push(new EventAction() {
                        public void run(IUIElement iUIElement) {
                            Toast toast;
                            Resources resources;
                            int i;
                            if (iUIElement instanceof ZMActivity) {
                                ZMActivity zMActivity = (ZMActivity) iUIElement;
                                int dimensionPixelSize = zMActivity instanceof IMActivity ? zMActivity.getResources().getDimensionPixelSize(C4558R.dimen.zm_pt_titlebar_height) : 0;
                                try {
                                    View findViewById = zMActivity.getWindow().getDecorView().findViewById(16908290);
                                    if (findViewById != null) {
                                        if (z) {
                                            resources = zMActivity.getResources();
                                            i = C4558R.color.zm_ui_kit_color_black_232333;
                                        } else {
                                            resources = zMActivity.getResources();
                                            i = C4558R.color.zm_white;
                                        }
                                        int color = resources.getColor(i);
                                        SnackbarUtils Short = SnackbarUtils.Short(findViewById, str);
                                        if (z) {
                                            Short.backColor(zMActivity.getResources().getColor(C4558R.color.zm_snackbar_error_bkg));
                                        } else {
                                            Short.backColor(zMActivity.getResources().getColor(C4558R.color.zm_snackbar_info_bkg));
                                        }
                                        Short.messageColor(color).messageCenter().gravity(48).margins(0, dimensionPixelSize, 0, 0).duration(i).show();
                                    }
                                } catch (Exception unused) {
                                    if (z) {
                                        toast = SnackbarToast.makeErrorText(VideoBoxApplication.getNonNullInstance(), str, i);
                                    } else {
                                        toast = SnackbarToast.makeText(VideoBoxApplication.getNonNullInstance(), str, i);
                                    }
                                    toast.setGravity(48, 0, dimensionPixelSize);
                                    toast.show();
                                }
                            }
                        }
                    });
                }
            }
        }, 500);
    }

    public void showErrorTipsOnUITop(String str) {
        showErrorTipsOnUITop(str, 5000);
    }

    public void showErrorTipsOnUITop(String str, int i) {
        showTipsOnUITop(str, i, true);
    }

    @Nullable
    public String getMyCarrierNumber(@Nullable Context context) {
        String str;
        String readStringValue = ZMPhoneUtils.readStringValue(PreferenceUtil.PBX_SIP_SWITCH_TO_CARRIER_NUMBER, null);
        if (!TextUtils.isEmpty(readStringValue)) {
            return readStringValue;
        }
        if (context != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(BoxUser.FIELD_PHONE);
            if (telephonyManager != null) {
                try {
                    str = telephonyManager.getLine1Number();
                } catch (SecurityException unused) {
                    str = null;
                }
                if (!TextUtils.isEmpty(str)) {
                    String isoCountryCode = CountryCodeUtil.getIsoCountryCode(context);
                    String isoCountryCode2PhoneCountryCode = CountryCodeUtil.isoCountryCode2PhoneCountryCode(isoCountryCode);
                    ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
                    if (zMPhoneNumberHelper == null) {
                        return PhoneNumberUtil.formatDisplayPhoneNumber(isoCountryCode2PhoneCountryCode, str);
                    }
                    return zMPhoneNumberHelper.formatPhoneNumberAsE164(str, isoCountryCode, "");
                }
            }
            String autoCallPhoneNumber = PTSettingHelper.getAutoCallPhoneNumber(context, null);
            if (!TextUtils.isEmpty(autoCallPhoneNumber)) {
                return autoCallPhoneNumber;
            }
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        String phoneNumber = myself.getPhoneNumber();
        if (!TextUtils.isEmpty(phoneNumber)) {
            return phoneNumber;
        }
        String profilePhoneNumber = myself.getProfilePhoneNumber();
        if (!TextUtils.isEmpty(profilePhoneNumber)) {
            String profileCountryCode = myself.getProfileCountryCode();
            ZMPhoneNumberHelper zMPhoneNumberHelper2 = PTApp.getInstance().getZMPhoneNumberHelper();
            if (zMPhoneNumberHelper2 == null) {
                return PhoneNumberUtil.formatDisplayPhoneNumber(profileCountryCode, profilePhoneNumber);
            }
            profilePhoneNumber = zMPhoneNumberHelper2.formatPhoneNumberAsE164(profilePhoneNumber, profileCountryCode, "");
        }
        return profilePhoneNumber;
    }

    public boolean switchCallToCarrier(@Nullable String str, @Nullable String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        boolean switchCallToCarrier = sipCallAPI.switchCallToCarrier(str, str2);
        if (switchCallToCarrier) {
            this.mSwitchingCallIds.add(str);
        }
        return switchCallToCarrier;
    }

    public boolean isInSwitchingToCarrier(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return this.mSwitchingCallIds.contains(str);
    }

    public boolean isEnableHasCallingPlan() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isEnableHasCallingPlan();
    }

    public long getHasCallingPlanBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getHasCallingPlanBit();
    }

    public boolean isAutoRecordingPoliciesEnable(CmmSIPCallItem cmmSIPCallItem) {
        if (cmmSIPCallItem == null) {
            return false;
        }
        int callGenerate = cmmSIPCallItem.getCallGenerate();
        boolean z = true;
        if (callGenerate == 0 || callGenerate == 2 ? (cmmSIPCallItem.getRealTimePolicies() & 8) != 8 : (cmmSIPCallItem.getRealTimePolicies() & 16) != 16) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void showTipsOnRemoteMergerEvent(String str, int i, CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto) {
        String str2;
        VideoBoxApplication nonNullInstance = VideoBoxApplication.getNonNullInstance();
        String currentCallID = getCurrentCallID();
        String remoteMemberDisplayName = getInstance().getRemoteMemberDisplayName(cmmSIPCallRemoteMemberProto);
        if (!TextUtils.isEmpty(remoteMemberDisplayName)) {
            if (TextUtils.isDigitsOnly(remoteMemberDisplayName)) {
                remoteMemberDisplayName = StringUtil.digitJoin(remoteMemberDisplayName.split(""), OAuth.SCOPE_DELIMITER);
            }
            switch (i) {
                case 1:
                    if (!str.equals(currentCallID)) {
                        String displayName = getDisplayName(str);
                        if (!TextUtils.isEmpty(displayName)) {
                            if (TextUtils.isDigitsOnly(displayName)) {
                                displayName = StringUtil.digitJoin(displayName.split(""), OAuth.SCOPE_DELIMITER);
                            }
                            str2 = nonNullInstance.getString(C4558R.string.zm_pbx_remote_member_joined_other_103630, new Object[]{remoteMemberDisplayName, displayName});
                            break;
                        } else {
                            return;
                        }
                    } else {
                        str2 = nonNullInstance.getString(C4558R.string.zm_pbx_remote_member_joined_current_103630, new Object[]{remoteMemberDisplayName});
                        break;
                    }
                case 2:
                    if (!str.equals(currentCallID)) {
                        String displayName2 = getDisplayName(str);
                        if (!TextUtils.isEmpty(displayName2)) {
                            if (TextUtils.isDigitsOnly(displayName2)) {
                                displayName2 = StringUtil.digitJoin(displayName2.split(""), OAuth.SCOPE_DELIMITER);
                            }
                            str2 = nonNullInstance.getString(C4558R.string.zm_pbx_remote_member_left_other_103630, new Object[]{remoteMemberDisplayName, displayName2});
                            break;
                        } else {
                            return;
                        }
                    } else {
                        str2 = nonNullInstance.getString(C4558R.string.zm_pbx_remote_member_left_current_103630, new Object[]{remoteMemberDisplayName});
                        break;
                    }
                default:
                    str2 = null;
                    break;
            }
            if (!TextUtils.isEmpty(str2)) {
                showTipsOnUITop(str2);
            }
        }
    }

    public boolean playToneOnRemoveMergerEvent(int i) {
        int i2;
        if (StringUtil.isEmptyOrNull(AppUtil.getDataPath(true, true))) {
            return false;
        }
        String str = null;
        switch (i) {
            case 1:
                str = "dingdong.pcm";
                i2 = 26;
                break;
            case 2:
                str = "leave.pcm";
                i2 = 27;
                break;
            default:
                i2 = 0;
                break;
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return playTone(str, i2, 2);
    }

    public boolean playTone(String str, int i, int i2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        String dataPath = AppUtil.getDataPath(true, true);
        if (StringUtil.isEmptyOrNull(dataPath)) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dataPath);
        sb.append(File.separator);
        sb.append(str);
        return sipCallAPI.playSoundFile(sb.toString(), i, i2);
    }

    /* access modifiers changed from: private */
    public void setOutOfServiceTime() {
        this.mServiceRangeState = 1;
        this.mOutOfServiceTime = System.currentTimeMillis();
    }

    public long getOutOfServiceTime() {
        return this.mOutOfServiceTime;
    }

    public int getServiceRangeState() {
        return this.mServiceRangeState;
    }

    public void setServiceRangeState(int i) {
        this.mServiceRangeState = i;
        if (i == 0) {
            this.mOutOfServiceTime = 0;
        }
    }

    public boolean isCallOffLoad(String str) {
        return this.mMediaOffLoadCalls.contains(str);
    }

    public void setMediaMode(@Nullable String str, int i) {
        if (str == null) {
            this.mMediaOffLoadCalls.clear();
            return;
        }
        if (i == 1) {
            this.mMediaOffLoadCalls.add(str);
        } else {
            this.mMediaOffLoadCalls.remove(str);
        }
    }
}
