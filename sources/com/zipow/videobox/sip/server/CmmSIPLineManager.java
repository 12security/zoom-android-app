package com.zipow.videobox.sip.server;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRegData;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLine;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLineCallItem;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPUser;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSipLineInfoForCallerID;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.SIPConfiguration;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.ISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.view.SnackbarToast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class CmmSIPLineManager {
    private static final int MSG_NEED_REGISTE_LINE = 193;
    private static final String TAG = "CmmSIPLineManager";
    private static CmmSIPLineManager ourInstance;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 193) {
                CmmSIPLineManager.this.registerLine(message.obj.toString());
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsConflict = false;
    private SimpleISIPLineMgrEventSinkListener mLineEventListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnMySelfInfoUpdated(boolean z, int i) {
            super.OnMySelfInfoUpdated(z, i);
            if (z) {
                CmmSIPLine mineExtensionLine = CmmSIPLineManager.this.getMineExtensionLine();
                if (mineExtensionLine != null) {
                    CmmSIPLineManager.this.postRegisterLineDelay(mineExtensionLine.getID(), i);
                }
            }
        }

        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            CmmSIPLineManager.this.setRegisterResult(str, cmmSIPCallRegResult);
            CmmSIPLineManager.this.notifyWebSipStatus();
            if (!cmmSIPCallRegResult.isRegistered()) {
                return;
            }
            if (CmmSIPCallManager.getInstance().isLoginConflict()) {
                CmmSIPLineManager.this.unRegister();
            } else {
                CmmSIPCallManager.getInstance().checkCallPeerInLocal(str);
            }
        }

        public void OnSharedLineUpdated(String str, boolean z, int i) {
            super.OnSharedLineUpdated(str, z, i);
            if (z) {
                CmmSIPLineManager.this.postRegisterLineDelay(str, i);
            }
        }

        public void OnLineCallItemCreated(String str, int i) {
            super.OnLineCallItemCreated(str, i);
            CmmSIPLineManager.this.showActionTips(str, i);
        }

        public void OnLineCallItemUpdate(String str, int i) {
            super.OnLineCallItemUpdate(str, i);
            CmmSIPLineManager.this.showActionTips(str, i);
        }
    };
    private HashMap<String, CmmSIPCallRegResult> mLineRegResult = new HashMap<>();
    private SimpleZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onConnectReturn(int i) {
            super.onConnectReturn(i);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (zoomMessenger.isStreamConflict()) {
                    CmmSIPLineManager.this.mIsConflict = true;
                    CmmSIPLineManager.this.unRegister();
                    CmmPBXCallHistoryManager.getInstance().clearEventSink();
                    PBXLoginConflictListenerUI.getInstance().handleOnConflict();
                } else if (zoomMessenger.isConnectionGood() && CmmSIPLineManager.this.mIsConflict) {
                    CmmPBXCallHistoryManager.getInstance().setEventSink();
                    CmmSIPLineManager.this.register();
                    CmmSIPLineManager.this.mIsConflict = false;
                    PBXLoginConflictListenerUI.getInstance().handleOnResumeFromConflict();
                }
            }
        }
    };
    private LinkedHashMap<String, CmmSIPUserBean> mShareUsers = new LinkedHashMap<>();
    private LinkedHashMap<String, CmmSIPLineCallItemBean> mSharedLineCallItems = new LinkedHashMap<>();
    private LinkedHashMap<String, String> mUserCallMapping = new LinkedHashMap<>();

    public static CmmSIPLineManager getInstance() {
        synchronized (CmmSIPLineManager.class) {
            if (ourInstance == null) {
                ourInstance = new CmmSIPLineManager();
            }
        }
        return ourInstance;
    }

    private CmmSIPLineManager() {
    }

    public void init() {
        addISIPLineMgrEventSinkUI(this.mLineEventListener);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
    }

    public void onSIPCallServiceStarted() {
        if (!CmmSIPCallManager.getInstance().isLoginConflict() || CmmSIPNosManager.getInstance().isNosSIPCallRinging()) {
            register();
        }
    }

    public void suspendToResume() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI != null) {
            sipCallAPI.suspendToResume(NetworkUtil.hasDataNetwork(VideoBoxApplication.getGlobalContext()), NetworkUtil.getNetworkIP(VideoBoxApplication.getGlobalContext()));
        }
    }

    public void resumeToSuspend() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI != null) {
            sipCallAPI.resumeToSuspend();
        }
    }

    /* access modifiers changed from: private */
    public void register() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI != null) {
            sIPLineMgrAPI.register();
        }
    }

    private void registerLine(CmmSIPLine cmmSIPLine) {
        if (cmmSIPLine != null) {
            registerLine(cmmSIPLine.getID());
        }
    }

    /* access modifiers changed from: private */
    public void registerLine(String str) {
        if (!TextUtils.isEmpty(str)) {
            ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
            if (sIPLineMgrAPI != null) {
                sIPLineMgrAPI.registerLine(str);
            }
        }
    }

    private void registerUser(CmmSIPUser cmmSIPUser) {
        if (cmmSIPUser != null) {
            int lineCount = cmmSIPUser.getLineCount();
            for (int i = 0; i < lineCount; i++) {
                registerLine(cmmSIPUser.getLineByIndex(i));
            }
        }
    }

    private void registerUser(String str) {
        CmmSIPUser sharedUserByID = getSharedUserByID(str);
        if (sharedUserByID != null) {
            registerUser(sharedUserByID);
        }
    }

    public boolean unRegister() {
        return unRegisterSIPCallApi();
    }

    public boolean unRegistarExtLine() {
        return unRegisterExtLine();
    }

    private boolean unRegisterExtLine() {
        if (getSIPLineMgrAPI() == null) {
            return false;
        }
        CmmSIPLine mineExtensionLine = getMineExtensionLine();
        if (mineExtensionLine == null) {
            return false;
        }
        return unRegisterLine(mineExtensionLine.getID());
    }

    private void unRegisterUser(String str) {
        CmmSIPUser sharedUserByID = getSharedUserByID(str);
        if (sharedUserByID != null) {
            unRegisterUser(sharedUserByID);
        }
    }

    private void unRegisterUser(CmmSIPUser cmmSIPUser) {
        if (cmmSIPUser != null) {
            int lineCount = cmmSIPUser.getLineCount();
            for (int i = 0; i < lineCount; i++) {
                unRegisterLine(cmmSIPUser.getLineByIndex(i));
            }
        }
    }

    public boolean isMineLine(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CmmSIPLine mineExtensionLine = getMineExtensionLine();
        if (mineExtensionLine == null) {
            return false;
        }
        return str.equals(mineExtensionLine.getID());
    }

    public boolean isAllLineRegistered() {
        CmmSIPCallRegResult mineExtensionLineRegResult = getMineExtensionLineRegResult();
        if (mineExtensionLineRegResult == null || !mineExtensionLineRegResult.isRegistered()) {
            return false;
        }
        List<CmmSipLineInfoForCallerID> allLineInfoforCallerID = getInstance().getAllLineInfoforCallerID();
        if (allLineInfoforCallerID != null) {
            for (CmmSipLineInfoForCallerID lineId : allLineInfoforCallerID) {
                CmmSIPCallRegResult lineRegResult = getLineRegResult(lineId.getLineId());
                if (lineRegResult != null) {
                    if (!lineRegResult.isRegistered()) {
                    }
                }
                return false;
            }
        }
        return true;
    }

    public boolean isLineRegistered(@Nullable NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem == null) {
            return false;
        }
        String to = nosSIPCallItem.getTo();
        if (TextUtils.isEmpty(to)) {
            return false;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return false;
        }
        CmmSIPLine mineExtensionLine = getMineExtensionLine();
        if (mineExtensionLine != null) {
            CmmSIPCallRegData registerData = mineExtensionLine.getRegisterData();
            if (registerData != null && to.equals(registerData.getUserName())) {
                PTAppProtos.CmmSIPCallRegResult registerResult = mineExtensionLine.getRegisterResult();
                if (registerResult != null && registerResult.getRegStatus() == 6) {
                    return true;
                }
            }
        }
        List<CmmSipLineInfoForCallerID> allLineInfoforCallerID = getInstance().getAllLineInfoforCallerID();
        if (allLineInfoforCallerID != null) {
            for (CmmSipLineInfoForCallerID lineId : allLineInfoforCallerID) {
                CmmSIPLine lineItemByID = sIPLineMgrAPI.getLineItemByID(lineId.getLineId());
                if (lineItemByID != null) {
                    CmmSIPCallRegData registerData2 = lineItemByID.getRegisterData();
                    if (registerData2 != null && to.equals(registerData2.getUserName())) {
                        PTAppProtos.CmmSIPCallRegResult registerResult2 = lineItemByID.getRegisterResult();
                        if (registerResult2 != null && registerResult2.getRegStatus() == 6) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isLineMatchesNosSIPCall(@Nullable String str, @Nullable NosSIPCallItem nosSIPCallItem) {
        if (TextUtils.isEmpty(str) || nosSIPCallItem == null) {
            return false;
        }
        String to = nosSIPCallItem.getTo();
        if (TextUtils.isEmpty(to)) {
            return false;
        }
        CmmSIPLine lineItemByID = getLineItemByID(str);
        if (lineItemByID == null) {
            return false;
        }
        CmmSIPCallRegData registerData = lineItemByID.getRegisterData();
        if (registerData != null) {
            return to.equals(registerData.getUserName());
        }
        return false;
    }

    @Nullable
    public CmmSIPLine getMineExtensionLine() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getMineExtensionLine();
    }

    private boolean unRegisterLine(CmmSIPLine cmmSIPLine) {
        if (cmmSIPLine == null) {
            return false;
        }
        return unRegisterLine(cmmSIPLine.getID());
    }

    private boolean unRegisterLine(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return false;
        }
        return sIPLineMgrAPI.unRegisterLine(str);
    }

    private boolean unRegisterSIPCallApi() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return false;
        }
        return sIPLineMgrAPI.unRegister();
    }

    @Nullable
    public List<CmmSipLineInfoForCallerID> getAllLineInfoforCallerID() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getAllLineInfoforCallerID();
    }

    public boolean switchMimeExtensionLine() {
        CmmSIPLine mineExtensionLine = getMineExtensionLine();
        if (mineExtensionLine == null) {
            return false;
        }
        return switchLine(mineExtensionLine.getID());
    }

    public boolean switchLine(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return false;
        }
        return sIPLineMgrAPI.switchLine(str);
    }

    @Nullable
    public ISIPLineMgrAPI getSIPLineMgrAPI() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getSIPLineMgrAPI();
    }

    /* access modifiers changed from: private */
    public void postRegisterLineDelay(String str, int i) {
        this.mHandler.removeMessages(193);
        Message obtainMessage = this.mHandler.obtainMessage(193);
        obtainMessage.obj = str;
        this.mHandler.sendMessageDelayed(obtainMessage, (long) (i * 1000));
    }

    public void notifyWebSipStatus() {
        SipPhoneIntegration sipIntergration = CmmSIPCallManager.getInstance().getSipIntergration();
        if (sipIntergration != null) {
            SIPConfiguration sIPConfiguration = new SIPConfiguration();
            CmmSIPCallRegResult mineExtensionLineRegResult = getMineExtensionLineRegResult();
            sIPConfiguration.respCode = mineExtensionLineRegResult != null ? mineExtensionLineRegResult.getRespCode() : 0;
            sIPConfiguration.respDescription = mineExtensionLineRegResult != null ? mineExtensionLineRegResult.getRespDesc() : "";
            sIPConfiguration.authName = sipIntergration.getAuthoriztionName();
            sIPConfiguration.domain = sipIntergration.getDomain();
            sIPConfiguration.protocol = sipIntergration.getProtocol();
            sIPConfiguration.proxy = sipIntergration.getProxyServer();
            sIPConfiguration.registrationExpiry = sipIntergration.getRegistrationExpiry();
            sIPConfiguration.regServerAddress = sipIntergration.getRegisterServer();
            sIPConfiguration.status = sipIntergration.getStatus();
            sIPConfiguration.userDisplayName = sipIntergration.getUserName();
            sIPConfiguration.userName = sipIntergration.getUserName();
            sIPConfiguration.userPassword = sipIntergration.getPassword();
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                sIPConfiguration.userDisplayName = currentUserProfile.getUserName();
            }
            ZoomMessengerUI.getInstance().notifyWebSipStatus(sIPConfiguration);
        }
    }

    public void clearRegisterResult() {
        this.mLineRegResult.clear();
    }

    public boolean isRegisterEmpty() {
        HashMap<String, CmmSIPCallRegResult> hashMap = this.mLineRegResult;
        return hashMap == null || hashMap.isEmpty();
    }

    /* access modifiers changed from: private */
    public void setRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
        if (cmmSIPCallRegResult != null && !TextUtils.isEmpty(str)) {
            if (cmmSIPCallRegResult.isRegisterIdle()) {
                this.mLineRegResult.remove(str);
            } else {
                if (!ZMActivity.isAppInForeground() && cmmSIPCallRegResult.isRegisterFailed()) {
                    cmmSIPCallRegResult.setRegStatus(0);
                    cmmSIPCallRegResult.setRespCodeDetail("");
                    cmmSIPCallRegResult.setRespCode(0);
                    cmmSIPCallRegResult.setRespDesc("");
                }
                this.mLineRegResult.put(str, cmmSIPCallRegResult);
            }
        }
    }

    public boolean isLineRegistered(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CmmSIPCallRegResult cmmSIPCallRegResult = (CmmSIPCallRegResult) this.mLineRegResult.get(str);
        if (cmmSIPCallRegResult == null) {
            return false;
        }
        return cmmSIPCallRegResult.isRegistered();
    }

    @Nullable
    public String getRegisterErrorString() {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (!NetworkUtil.hasDataNetwork(globalContext)) {
            return globalContext.getString(C4558R.string.zm_sip_error_network_unavailable_99728);
        }
        CmmSIPCallRegResult mineExtensionLineRegResult = getMineExtensionLineRegResult();
        if (mineExtensionLineRegResult != null) {
            return getRegisterErrorStringOnPbx(globalContext, mineExtensionLineRegResult.getRespCode(), mineExtensionLineRegResult.getRespCodeDetail());
        }
        return null;
    }

    @Nullable
    public String getRegisterErrorStringOnPbx(Context context, int i, String str) {
        if (context == null) {
            return null;
        }
        if (CmmSIPCallManager.getInstance().isPBXInactive()) {
            i = 403;
        }
        if (i != 401) {
            if (i == 403) {
                return context.getString(C4558R.string.zm_sip_error_reg_403_99728);
            }
            if (i != 407) {
                if (i == 702) {
                    return context.getString(C4558R.string.zm_sip_error_certificate);
                }
                int i2 = C4558R.string.zm_sip_error_reg_99728;
                Object[] objArr = new Object[1];
                if (StringUtil.isEmptyOrNull(str)) {
                    str = String.valueOf(i);
                }
                objArr[0] = str;
                return context.getString(i2, objArr);
            }
        }
        return context.getString(C4558R.string.zm_sip_error_reg_401_99728);
    }

    public int getRegisterErrorCode() {
        CmmSIPCallRegResult mineExtensionLineRegResult = getMineExtensionLineRegResult();
        if (mineExtensionLineRegResult != null) {
            return mineExtensionLineRegResult.getRespCode();
        }
        return 200;
    }

    public boolean isShowSipRegisterError() {
        boolean z = false;
        if (getMineExtensionLineRegResult() == null || PTApp.getInstance().getSipCallAPI() == null) {
            return false;
        }
        CmmSIPCallRegResult mineExtensionLineRegResult = getInstance().getMineExtensionLineRegResult();
        int regStatus = mineExtensionLineRegResult != null ? mineExtensionLineRegResult.getRegStatus() : 0;
        if (regStatus == 0 || regStatus == 7) {
            z = true;
        }
        return z;
    }

    @Nullable
    public CmmSIPCallRegResult getMineExtensionLineRegResult() {
        CmmSIPLine mineExtensionLine = getMineExtensionLine();
        if (mineExtensionLine == null) {
            return null;
        }
        return getLineRegResult(mineExtensionLine.getID());
    }

    @Nullable
    public CmmSIPCallRegResult getLineRegResult(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (CmmSIPCallRegResult) this.mLineRegResult.get(str);
    }

    @Nullable
    public CmmSIPUser getMySelf() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getMySelf();
    }

    @Nullable
    public CmmSIPUser getMySelfProto() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getMySelfProto();
    }

    @Nullable
    public CmmSIPLine getLineItemByID(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getLineItemByID(str);
    }

    @Nullable
    public CmmSIPLine getLineItemProtoByID(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getLineItemProtoByID(str);
    }

    @Nullable
    public CmmSIPLineCallItem GetLineCallItemByID(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getLineCallItemByID(str);
    }

    @Nullable
    public CmmSIPLineCallItem GetLineCallItemProtoByID(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getLineCallItemProtoByID(str);
    }

    @Nullable
    public CmmSIPLine getCurrentSelectedLine() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getCurrentSelectedLine();
    }

    @Nullable
    public CmmSIPUser getSharedUserByID(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return null;
        }
        return sIPLineMgrAPI.getSharedUserByID(str);
    }

    public void addISIPLineMgrEventSinkUI(ISIPLineMgrEventSinkListener iSIPLineMgrEventSinkListener) {
        if (iSIPLineMgrEventSinkListener != null) {
            ISIPLineMgrEventSinkUI.getInstance().addListener(iSIPLineMgrEventSinkListener);
        }
    }

    public void removeISIPLineMgrEventSinkUI(ISIPLineMgrEventSinkListener iSIPLineMgrEventSinkListener) {
        if (iSIPLineMgrEventSinkListener != null) {
            ISIPLineMgrEventSinkUI.getInstance().removeListener(iSIPLineMgrEventSinkListener);
        }
    }

    public boolean pickUp(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI == null) {
            return false;
        }
        CmmSipAudioMgr.getInstance().enablePhoneAudio();
        return sIPLineMgrAPI.pickup(str);
    }

    @Nullable
    public String getOwnerNameByLineCallItem(String str) {
        CmmSIPLineCallItem GetLineCallItemByID = GetLineCallItemByID(str);
        if (GetLineCallItemByID == null) {
            return null;
        }
        return getOwnerNameByLineCallItem(GetLineCallItemByID);
    }

    @Nullable
    public String getOwnerNameByLineCallItem(CmmSIPLineCallItem cmmSIPLineCallItem) {
        if (cmmSIPLineCallItem == null) {
            return null;
        }
        String ownerNumber = cmmSIPLineCallItem.getOwnerNumber();
        if (TextUtils.isEmpty(ownerNumber)) {
            return null;
        }
        String displayNameByNumber = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(ownerNumber);
        if (!TextUtils.isEmpty(displayNameByNumber)) {
            return displayNameByNumber;
        }
        String ownerName = cmmSIPLineCallItem.getOwnerName();
        return !StringUtil.isEmptyOrNull(ownerName) ? ownerName.trim() : "";
    }

    @Nullable
    public String getPeerNameByLineCallItem(CmmSIPLineCallItem cmmSIPLineCallItem) {
        if (cmmSIPLineCallItem == null) {
            return null;
        }
        String peerNumber = cmmSIPLineCallItem.getPeerNumber();
        if (TextUtils.isEmpty(peerNumber)) {
            return null;
        }
        String displayNameByNumber = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(peerNumber);
        if (!TextUtils.isEmpty(displayNameByNumber)) {
            return displayNameByNumber;
        }
        String peerName = cmmSIPLineCallItem.getPeerName();
        return !TextUtils.isEmpty(peerName) ? peerName.trim() : "";
    }

    /* access modifiers changed from: private */
    public void showActionTips(String str, int i) {
        CmmSIPLineCallItem GetLineCallItemByID = GetLineCallItemByID(str);
        if (GetLineCallItemByID != null) {
            switch (i) {
                case 1:
                    if (!isToMe(GetLineCallItemByID)) {
                        showAnsweredTips(GetLineCallItemByID);
                        break;
                    } else {
                        return;
                    }
                case 2:
                    if (!isToMe(GetLineCallItemByID)) {
                        showPickedupTips(GetLineCallItemByID);
                        break;
                    } else {
                        return;
                    }
            }
        }
    }

    private boolean isToMe(CmmSIPLineCallItem cmmSIPLineCallItem) {
        if (cmmSIPLineCallItem == null) {
            return false;
        }
        if (cmmSIPLineCallItem.isItBelongToMe()) {
            return true;
        }
        String peerNumber = cmmSIPLineCallItem.getPeerNumber();
        if (StringUtil.isEmptyOrNull(peerNumber)) {
            return false;
        }
        CmmSIPLine mineExtensionLine = getMineExtensionLine();
        return mineExtensionLine != null && peerNumber.equals(mineExtensionLine.getOwnerNumber());
    }

    private void showAnsweredTips(CmmSIPLineCallItem cmmSIPLineCallItem) {
        if (cmmSIPLineCallItem != null) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                showTipsOnUI(globalContext.getString(C4558R.string.zm_sip_call_answered_by_99631, new Object[]{getOwnerNameByLineCallItem(cmmSIPLineCallItem), getPeerNameByLineCallItem(cmmSIPLineCallItem)}));
            }
        }
    }

    private void showPickedupTips(CmmSIPLineCallItem cmmSIPLineCallItem) {
        if (cmmSIPLineCallItem != null) {
            Context globalContext = VideoBoxApplication.getGlobalContext();
            if (globalContext != null) {
                showTipsOnUI(globalContext.getString(C4558R.string.zm_sip_call_pickedup_by_99631, new Object[]{getOwnerNameByLineCallItem(cmmSIPLineCallItem), getPeerNameByLineCallItem(cmmSIPLineCallItem)}));
            }
        }
    }

    public void showTipsOnUI(final String str) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                int dimensionPixelSize = frontActivity instanceof IMActivity ? frontActivity.getResources().getDimensionPixelSize(C4558R.dimen.zm_home_page_bottom_tab_bar_height) : 0;
                Toast makeText = SnackbarToast.makeText(VideoBoxApplication.getNonNullInstance(), str, 0);
                makeText.setGravity(80, 0, dimensionPixelSize);
                makeText.show();
            }
        }, 500);
    }

    public void loadSharedUser() {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI != null) {
            CmmSIPUser mySelfProto = getMySelfProto();
            if (mySelfProto != null) {
                this.mShareUsers.clear();
                this.mShareUsers.put(mySelfProto.getID(), new CmmSIPUserBean(mySelfProto));
                List<CmmSIPUser> sharedUsers = sIPLineMgrAPI.getSharedUsers();
                if (!CollectionsUtil.isListEmpty(sharedUsers)) {
                    for (CmmSIPUser cmmSIPUser : sharedUsers) {
                        this.mShareUsers.put(cmmSIPUser.getID(), new CmmSIPUserBean(cmmSIPUser));
                    }
                }
            }
        }
    }

    public void clearLineCache() {
        this.mShareUsers.clear();
        this.mSharedLineCallItems.clear();
        this.mUserCallMapping.clear();
    }

    @NonNull
    public List<CmmSIPUserBean> getSharedUsers() {
        if (this.mShareUsers.isEmpty()) {
            loadSharedUser();
        }
        ArrayList arrayList = new ArrayList();
        for (Entry value : this.mShareUsers.entrySet()) {
            arrayList.add(value.getValue());
        }
        return arrayList;
    }

    public void updateSelfInfo() {
        if (!this.mShareUsers.isEmpty()) {
            CmmSIPUser mySelfProto = getMySelfProto();
            if (mySelfProto != null) {
                this.mShareUsers.put(mySelfProto.getID(), new CmmSIPUserBean(mySelfProto));
            }
        }
    }

    public void addSharedUser(@NonNull String str) {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI != null) {
            CmmSIPUser sharedUserProtoByID = sIPLineMgrAPI.getSharedUserProtoByID(str);
            if (sharedUserProtoByID != null) {
                if (this.mShareUsers.isEmpty()) {
                    loadSharedUser();
                }
                this.mShareUsers.put(str, new CmmSIPUserBean(sharedUserProtoByID));
            }
        }
    }

    public void removeSharedUser(@NonNull String str) {
        this.mShareUsers.remove(str);
    }

    @Nullable
    public CmmSIPUserBean getSharedUserBeanById(@NonNull String str) {
        return (CmmSIPUserBean) this.mShareUsers.get(str);
    }

    @Nullable
    public CmmSIPUserBean getShareUserBeanByLineId(@NonNull String str) {
        for (CmmSIPUserBean cmmSIPUserBean : this.mShareUsers.values()) {
            if (cmmSIPUserBean.getLineMap().containsKey(str)) {
                return cmmSIPUserBean;
            }
        }
        return null;
    }

    public void addSharedLine(@NonNull String str) {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI != null) {
            CmmSIPLine lineItemProtoByID = sIPLineMgrAPI.getLineItemProtoByID(str);
            if (lineItemProtoByID != null) {
                CmmSIPUserBean cmmSIPUserBean = (CmmSIPUserBean) this.mShareUsers.get(lineItemProtoByID.getUserID());
                if (cmmSIPUserBean != null) {
                    cmmSIPUserBean.getLineMap().put(str, new CmmSIPLineBean(lineItemProtoByID));
                }
            }
        }
    }

    @Nullable
    public CmmSIPLineBean getSharedLineBeanById(@NonNull String str) {
        for (CmmSIPUserBean cmmSIPUserBean : this.mShareUsers.values()) {
            if (cmmSIPUserBean != null && cmmSIPUserBean.getLineMap().containsKey(str)) {
                return (CmmSIPLineBean) cmmSIPUserBean.getLineMap().get(str);
            }
        }
        return null;
    }

    public void removeSharedLine(@NonNull String str) {
        for (CmmSIPUserBean cmmSIPUserBean : this.mShareUsers.values()) {
            if (cmmSIPUserBean != null) {
                cmmSIPUserBean.getLineMap().remove(str);
                return;
            }
        }
    }

    public void addLineCallItem(@NonNull String str) {
        ISIPLineMgrAPI sIPLineMgrAPI = getSIPLineMgrAPI();
        if (sIPLineMgrAPI != null) {
            CmmSIPLineCallItem lineCallItemProtoByID = sIPLineMgrAPI.getLineCallItemProtoByID(str);
            if (lineCallItemProtoByID != null) {
                CmmSIPUserBean shareUserBeanByLineId = getShareUserBeanByLineId(lineCallItemProtoByID.getLineID());
                if (shareUserBeanByLineId != null) {
                    this.mUserCallMapping.put(str, shareUserBeanByLineId.getID());
                    this.mSharedLineCallItems.put(str, new CmmSIPLineCallItemBean(lineCallItemProtoByID));
                }
            }
        }
    }

    public void removeLineCallItem(@NonNull String str) {
        this.mUserCallMapping.remove(str);
        this.mSharedLineCallItems.remove(str);
    }

    @Nullable
    public CmmSIPLineCallItemBean getLineCallItemBeanById(@NonNull String str) {
        return (CmmSIPLineCallItemBean) this.mSharedLineCallItems.get(str);
    }

    @Nullable
    public String getLineCallItemUserId(@NonNull String str) {
        return (String) this.mUserCallMapping.get(str);
    }

    @NonNull
    public List<CmmSIPLineCallItemBean> getUserLineCallItemsByUserId(@NonNull String str) {
        Set<Entry> entrySet = this.mUserCallMapping.entrySet();
        ArrayList arrayList = new ArrayList();
        for (Entry entry : entrySet) {
            if (str.equals(entry.getValue())) {
                CmmSIPLineCallItemBean lineCallItemBeanById = getLineCallItemBeanById((String) entry.getKey());
                if (lineCallItemBeanById != null) {
                    arrayList.add(lineCallItemBeanById);
                }
            }
        }
        return arrayList;
    }
}
