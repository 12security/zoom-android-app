package com.zipow.videobox.sip.server;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionChangedBits;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRemoteMemberProto;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import java.io.IOException;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class SIPCallEventListenerUI {
    private static final String TAG = "SIPCallEventListenerUI";
    @Nullable
    private static SIPCallEventListenerUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface ISIPCallEventListener extends IListener {
        void HandleUrlAction(int i, String str);

        void OnAudioDeviceFailed(int i);

        void OnAudioDeviceSpecialInfoUpdate(int i, int i2);

        void OnCallActionResult(String str, int i, boolean z);

        void OnCallAutoRecordingEvent(String str, int i);

        void OnCallMediaStatusUpdate(String str, int i, String str2);

        void OnCallRecordingResult(String str, int i, int i2);

        void OnCallRecordingStatusUpdate(String str, int i);

        void OnCallRemoteMergerEvent(String str, int i, CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto);

        void OnCallRemoteOperationFail(String str, int i, String str2);

        void OnCallStatusUpdate(String str, int i);

        void OnCallTerminate(String str, int i);

        void OnCallTransferResult(String str, int i);

        void OnCheckPhoneNumberFailed(String str);

        void OnEnableSIPAudio(int i);

        void OnHangupAllCallsResult(boolean z);

        void OnInboundCallPushDuplicateChecked(boolean z, String str);

        void OnMeetingAskToEnableSipAudio(boolean z);

        void OnMeetingAudioSessionStatus(boolean z);

        void OnMeetingJoinedResult(String str, boolean z);

        void OnMeetingStartedResult(String str, long j, String str2, boolean z);

        void OnMeetingStateChanged(int i);

        void OnMergeCallHostChanged(boolean z, String str, String str2);

        void OnMergeCallResult(boolean z, String str, String str2);

        void OnMuteCallResult(boolean z);

        void OnNewCallGenerate(String str, int i);

        void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list);

        void OnPBXMediaModeUpdate(String str, int i);

        void OnPBXServiceRangeChanged(int i);

        void OnPBXUserStatusChange(int i);

        void OnPeerJoinMeetingResult(String str, long j, boolean z);

        void OnReceivedJoinMeetingRequest(String str, long j, String str2);

        void OnRegisterResult(CmmSIPCallRegResult cmmSIPCallRegResult);

        void OnRequestDoneForQueryPBXUserInfo(boolean z);

        void OnRequestDoneForUpdatePBXFeatureOptions(boolean z, List<CmmPBXFeatureOptionBit> list);

        void OnSIPCallServiceStarted();

        void OnSIPCallServiceStoped(boolean z);

        void OnSendDTMFResult(String str, String str2, boolean z);

        void OnSipAudioQualityNotification(int i);

        void OnSipServiceNeedRegiste(boolean z, int i);

        void OnSipServiceNeedUnregisterForGracePeriod();

        void OnSwitchCallToCarrierResult(String str, boolean z, int i);

        void OnTalkingStatusChanged(boolean z);

        void OnUnloadSIPService(int i);

        void OnUnreadVoiceMailCountChanged(int i);

        void OnUnregisterDone();

        void OnWMIActive(boolean z);

        void OnWMIMessageCountChanged(int i, int i2, boolean z);

        void OnZPNSLoginStatus(int i);
    }

    public static class SimpleSIPCallEventListener implements ISIPCallEventListener {
        public void HandleUrlAction(int i, String str) {
        }

        public void OnAudioDeviceFailed(int i) {
        }

        public void OnAudioDeviceSpecialInfoUpdate(int i, int i2) {
        }

        public void OnCallActionResult(String str, int i, boolean z) {
        }

        public void OnCallAutoRecordingEvent(String str, int i) {
        }

        public void OnCallMediaStatusUpdate(String str, int i, String str2) {
        }

        public void OnCallRecordingResult(String str, int i, int i2) {
        }

        public void OnCallRecordingStatusUpdate(String str, int i) {
        }

        public void OnCallRemoteMergerEvent(String str, int i, CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto) {
        }

        public void OnCallRemoteOperationFail(String str, int i, String str2) {
        }

        public void OnCallStatusUpdate(String str, int i) {
        }

        public void OnCallTerminate(String str, int i) {
        }

        public void OnCallTransferResult(String str, int i) {
        }

        public void OnCheckPhoneNumberFailed(String str) {
        }

        public void OnEnableSIPAudio(int i) {
        }

        public void OnHangupAllCallsResult(boolean z) {
        }

        public void OnInboundCallPushDuplicateChecked(boolean z, String str) {
        }

        public void OnMeetingAskToEnableSipAudio(boolean z) {
        }

        public void OnMeetingAudioSessionStatus(boolean z) {
        }

        public void OnMeetingJoinedResult(String str, boolean z) {
        }

        public void OnMeetingStartedResult(String str, long j, String str2, boolean z) {
        }

        public void OnMeetingStateChanged(int i) {
        }

        public void OnMergeCallHostChanged(boolean z, String str, String str2) {
        }

        public void OnMergeCallResult(boolean z, String str, String str2) {
        }

        public void OnMuteCallResult(boolean z) {
        }

        public void OnNewCallGenerate(String str, int i) {
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
        }

        public void OnPBXMediaModeUpdate(String str, int i) {
        }

        public void OnPBXServiceRangeChanged(int i) {
        }

        public void OnPBXUserStatusChange(int i) {
        }

        public void OnPeerJoinMeetingResult(String str, long j, boolean z) {
        }

        public void OnReceivedJoinMeetingRequest(String str, long j, String str2) {
        }

        public void OnRegisterResult(CmmSIPCallRegResult cmmSIPCallRegResult) {
        }

        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
        }

        public void OnRequestDoneForUpdatePBXFeatureOptions(boolean z, List<CmmPBXFeatureOptionBit> list) {
        }

        public void OnSIPCallServiceStarted() {
        }

        public void OnSIPCallServiceStoped(boolean z) {
        }

        public void OnSendDTMFResult(String str, String str2, boolean z) {
        }

        public void OnSipAudioQualityNotification(int i) {
        }

        public void OnSipServiceNeedRegiste(boolean z, int i) {
        }

        public void OnSipServiceNeedUnregisterForGracePeriod() {
        }

        public void OnSwitchCallToCarrierResult(String str, boolean z, int i) {
        }

        public void OnTalkingStatusChanged(boolean z) {
        }

        public void OnUnloadSIPService(int i) {
        }

        public void OnUnreadVoiceMailCountChanged(int i) {
        }

        public void OnUnregisterDone() {
        }

        public void OnWMIActive(boolean z) {
        }

        public void OnWMIMessageCountChanged(int i, int i2, boolean z) {
        }

        public void OnZPNSLoginStatus(int i) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    @NonNull
    public static synchronized SIPCallEventListenerUI getInstance() {
        SIPCallEventListenerUI sIPCallEventListenerUI;
        synchronized (SIPCallEventListenerUI.class) {
            if (instance == null) {
                instance = new SIPCallEventListenerUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            sIPCallEventListenerUI = instance;
        }
        return sIPCallEventListenerUI;
    }

    private SIPCallEventListenerUI() {
        init();
    }

    private boolean initialized() {
        return this.mNativeHandle != 0;
    }

    private void init() {
        try {
            this.mNativeHandle = nativeInit();
        } catch (Throwable unused) {
        }
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        long j = this.mNativeHandle;
        if (j != 0) {
            nativeUninit(j);
        }
        super.finalize();
    }

    public void addListener(@Nullable ISIPCallEventListener iSIPCallEventListener) {
        if (iSIPCallEventListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iSIPCallEventListener) {
                    removeListener((ISIPCallEventListener) all[i]);
                }
            }
            this.mListenerList.add(iSIPCallEventListener);
        }
    }

    public void removeListener(ISIPCallEventListener iSIPCallEventListener) {
        this.mListenerList.remove(iSIPCallEventListener);
    }

    /* access modifiers changed from: protected */
    public void OnSIPCallServiceStoped(boolean z) {
        try {
            OnSIPCallServiceStopedImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSIPCallServiceStopedImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnSIPCallServiceStoped(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSIPCallServiceStarted() {
        try {
            OnSIPCallServiceStartedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSIPCallServiceStartedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnSIPCallServiceStarted();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCheckPhoneNumberFailed(String str) {
        try {
            OnCheckPhoneNumberFailedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCheckPhoneNumberFailedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCheckPhoneNumberFailed(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnInboundCallPushDuplicateChecked(boolean z, String str) {
        try {
            OnInboundCallPushDuplicateCheckedImpl(z, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnInboundCallPushDuplicateCheckedImpl(boolean z, String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnInboundCallPushDuplicateChecked(z, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRegisterResult(int i, int i2, String str, String str2) {
        try {
            OnRegisterResultImpl(new CmmSIPCallRegResult(i, i2, str, str2));
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRegisterResultImpl(@Nullable CmmSIPCallRegResult cmmSIPCallRegResult) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnRegisterResult(cmmSIPCallRegResult);
            }
        }
    }

    public void OnUnregisterDone() {
        try {
            OnUnregisterDoneImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnUnregisterDoneImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnUnregisterDone();
            }
        }
    }

    public void handleLocalNewCallGenerate(String str, int i) {
        OnNewCallGenerate(str, i);
    }

    public void handleLocalCallTerminate(String str, int i) {
        OnCallTerminate(str, i);
    }

    /* access modifiers changed from: protected */
    public void OnNewCallGenerate(String str, int i) {
        try {
            OnNewCallGenerateImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnNewCallGenerateImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnNewCallGenerate(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallTerminate(String str, int i) {
        try {
            OnCallTerminateImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallTerminateImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallTerminate(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallStatusUpdate(String str, int i) {
        try {
            OnCallStatusUpdateImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallStatusUpdateImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallStatusUpdate(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallMediaStatusUpdate(String str, int i, String str2) {
        try {
            OnCallMediaStatusUpdateImpl(str, i, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallMediaStatusUpdateImpl(String str, int i, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallMediaStatusUpdate(str, i, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallRecordingResult(String str, int i, int i2) {
        try {
            OnCallRecordingResultImpl(str, i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallRecordingResultImpl(String str, int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallRecordingResult(str, i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallRecordingStatusUpdate(String str, int i) {
        try {
            OnCallRecordingStatusUpdateImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallRecordingStatusUpdateImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallRecordingStatusUpdate(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallActionResult(String str, int i, boolean z) {
        try {
            OnCallActionResultImpl(str, i, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallActionResultImpl(String str, int i, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallActionResult(str, i, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallRemoteOperationFail(String str, int i, String str2) {
        try {
            OnCallRemoteOperationFailImpl(str, i, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallRemoteOperationFailImpl(String str, int i, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallRemoteOperationFail(str, i, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallTransferResult(String str, int i) {
        try {
            OnCallTransferResultImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallTransferResultImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallTransferResult(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMuteCallResult(boolean z) {
        try {
            OnMuteCallResultImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMuteCallResultImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnMuteCallResult(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSendDTMFResult(String str, String str2, boolean z) {
        try {
            OnSendDTMFResultImpl(str, str2, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSendDTMFResultImpl(String str, String str2, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnSendDTMFResult(str, str2, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnHangupAllCallsResult(boolean z) {
        try {
            OnHangupAllCallsResultImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnHangupAllCallsResultImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnHangupAllCallsResult(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnTalkingStatusChanged(boolean z) {
        try {
            OnTalkingStatusChangedImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnTalkingStatusChangedImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnTalkingStatusChanged(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnAudioDeviceFailed(int i) {
        try {
            OnAudioDeviceFailedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnAudioDeviceFailedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnAudioDeviceFailed(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSipAudioQualityNotification(int i) {
        try {
            OnSipAudioQualityNotificationImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSipAudioQualityNotificationImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnSipAudioQualityNotification(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnAudioDeviceSpecialInfoUpdate(int i, int i2) {
        try {
            OnAudioDeviceSpecialInfoUpdateImpl(i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnAudioDeviceSpecialInfoUpdateImpl(int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnAudioDeviceSpecialInfoUpdate(i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnEnableSIPAudio(int i) {
        try {
            OnEnableSIPAudioImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnEnableSIPAudioImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnEnableSIPAudio(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnUnloadSIPService(int i) {
        try {
            OnUnloadSIPServiceImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnUnloadSIPServiceImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnUnloadSIPService(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnUnreadVoiceMailCountChanged(int i) {
        try {
            OnUnreadVoiceMailCountChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnUnreadVoiceMailCountChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnUnreadVoiceMailCountChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnWMIMessageCountChanged(int i, int i2, boolean z) {
        try {
            OnWMIMessageCountChangedImpl(i, i2, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnWMIMessageCountChangedImpl(int i, int i2, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnWMIMessageCountChanged(i, i2, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnWMIActive(boolean z) {
        try {
            OnWMIActiveImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnWMIActiveImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnWMIActive(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMergeCallResult(boolean z, String str, String str2) {
        try {
            OnMergeCallResultImpl(z, str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMergeCallResultImpl(boolean z, String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnMergeCallResult(z, str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMergeCallHostChanged(boolean z, String str, String str2) {
        try {
            OnMergeCallHostChangedImpl(z, str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMergeCallHostChangedImpl(boolean z, String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnMergeCallHostChanged(z, str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallRemoteMergerEvent(String str, int i, byte[] bArr) {
        try {
            OnCallRemoteMergerEventImpl(str, i, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallRemoteMergerEventImpl(String str, int i, byte[] bArr) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto = null;
            if (bArr != null && bArr.length > 0) {
                try {
                    cmmSIPCallRemoteMemberProto = CmmSIPCallRemoteMemberProto.parseFrom(bArr);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallRemoteMergerEvent(str, i, cmmSIPCallRemoteMemberProto);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
        try {
            OnRequestDoneForQueryPBXUserInfoImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForQueryPBXUserInfoImpl(boolean z) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null) {
            CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
            if (cloudPBXInfo != null) {
                mainboard.setPBXExtensionNumber(cloudPBXInfo.getExtension());
            }
        }
        if (z) {
            CmmSIPLineManager.getInstance().loadSharedUser();
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnRequestDoneForQueryPBXUserInfo(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForUpdatePBXFeatureOptions(boolean z, byte[] bArr) {
        try {
            OnRequestDoneForUpdatePBXFeatureOptionsImpl(z, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForUpdatePBXFeatureOptionsImpl(boolean z, byte[] bArr) {
        CmmPBXFeatureOptionChangedBits cmmPBXFeatureOptionChangedBits;
        try {
            cmmPBXFeatureOptionChangedBits = CmmPBXFeatureOptionChangedBits.parseFrom(bArr);
        } catch (IOException unused) {
            cmmPBXFeatureOptionChangedBits = null;
        }
        if (cmmPBXFeatureOptionChangedBits != null) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((ISIPCallEventListener) iListener).OnRequestDoneForUpdatePBXFeatureOptions(z, cmmPBXFeatureOptionChangedBits.getChangedBitList());
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPBXFeatureOptionsChanged(byte[] bArr) {
        try {
            OnPBXFeatureOptionsChangedImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPBXFeatureOptionsChangedImpl(byte[] bArr) {
        CmmPBXFeatureOptionChangedBits cmmPBXFeatureOptionChangedBits;
        try {
            cmmPBXFeatureOptionChangedBits = CmmPBXFeatureOptionChangedBits.parseFrom(bArr);
        } catch (IOException unused) {
            cmmPBXFeatureOptionChangedBits = null;
        }
        if (cmmPBXFeatureOptionChangedBits != null) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((ISIPCallEventListener) iListener).OnPBXFeatureOptionsChanged(cmmPBXFeatureOptionChangedBits.getChangedBitList());
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSipServiceNeedUnregisterForGracePeriod() {
        try {
            OnSipServiceNeedUnregisterForGracePeriodImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSipServiceNeedUnregisterForGracePeriodImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnSipServiceNeedUnregisterForGracePeriod();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSipServiceNeedRegiste(boolean z, int i) {
        try {
            OnSipServiceNeedRegisteImpl(z, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSipServiceNeedRegisteImpl(boolean z, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnSipServiceNeedRegiste(z, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMeetingStartedResult(String str, long j, String str2, boolean z) {
        try {
            OnMeetingStartedResultImpl(str, j, str2, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMeetingStartedResultImpl(String str, long j, String str2, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i = 0; i < length; i++) {
                ((ISIPCallEventListener) all[i]).OnMeetingStartedResult(str, j, str2, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnReceivedJoinMeetingRequest(String str, long j, String str2) {
        try {
            OnReceivedJoinMeetingRequestImpl(str, j, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnReceivedJoinMeetingRequestImpl(String str, long j, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnReceivedJoinMeetingRequest(str, j, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMeetingJoinedResult(String str, boolean z) {
        try {
            OnMeetingJoinedResultImpl(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMeetingJoinedResultImpl(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnMeetingJoinedResult(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPeerJoinMeetingResult(String str, long j, boolean z) {
        try {
            OnPeerJoinMeetingResultImpl(str, j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPeerJoinMeetingResultImpl(String str, long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnPeerJoinMeetingResult(str, j, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMeetingStateChanged(int i) {
        try {
            OnMeetingStateChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMeetingStateChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnMeetingStateChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMeetingAudioSessionStatus(boolean z) {
        try {
            OnMeetingAudioSessionStatusImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMeetingAudioSessionStatusImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnMeetingAudioSessionStatus(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMeetingAskToEnableSipAudio(boolean z) {
        try {
            OnMeetingAskToEnableSipAudioImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMeetingAskToEnableSipAudioImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnMeetingAskToEnableSipAudio(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPBXUserStatusChange(int i) {
        try {
            OnPBXUserStatusChangeImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPBXUserStatusChangeImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnPBXUserStatusChange(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSwitchCallToCarrierResult(String str, boolean z, int i) {
        try {
            OnSwitchCallToCarrierResultImpl(str, z, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSwitchCallToCarrierResultImpl(String str, boolean z, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnSwitchCallToCarrierResult(str, z, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPBXMediaModeUpdate(String str, int i) {
        try {
            OnPBXMediaModeUpdateImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPBXMediaModeUpdateImpl(String str, int i) {
        CmmSIPCallManager.getInstance().setMediaMode(str, i);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnPBXMediaModeUpdate(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPBXServiceRangeChanged(int i) {
        try {
            OnPBXServiceRangeChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPBXServiceRangeChangedImpl(int i) {
        CmmSIPCallManager.getInstance().setServiceRangeState(i);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnPBXServiceRangeChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallAutoRecordingEvent(String str, int i) {
        try {
            OnCallAutoRecordingEventImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallAutoRecordingEventImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnCallAutoRecordingEvent(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void HandleUrlAction(int i, String str) {
        try {
            HandleUrlActionImpl(i, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void HandleUrlActionImpl(int i, String str) {
        if (!TextUtils.isEmpty(str) && CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            IntegrationActivity.showSIPCallFromSchema(VideoBoxApplication.getInstance(), str, i);
        }
    }

    /* access modifiers changed from: protected */
    public void OnZPNSLoginStatus(int i) {
        try {
            OnZPNSLoginStatusImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnZPNSLoginStatusImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallEventListener) iListener).OnZPNSLoginStatus(i);
            }
        }
    }
}
