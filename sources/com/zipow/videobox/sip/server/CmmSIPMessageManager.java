package com.zipow.videobox.sip.server;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.ptapp.PTAppProtos.PBXSessionUnreadCountList;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.IPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.SimpleIPBXMessageEventSinkUIListener;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.sip.sms.IPBXMessageSessionItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmSIPMessageManager {
    private static final String TAG = "CmmSIPMessageManager";
    private static CmmSIPMessageManager ourInstance;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper());
    private IPBXMessageEventSinkUIListener mIPBXMessageEventSinkUIListener = new SimpleIPBXMessageEventSinkUIListener() {
        public void OnNewMessageCreated(final String str, final String str2) {
            super.OnNewMessageCreated(str, str2);
            CmmSIPMessageManager.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    IPBXMessageSession sessionById = CmmSIPMessageManager.getInstance().getSessionById(str);
                    if (sessionById != null) {
                        IPBXMessage messageByID = sessionById.getMessageByID(str2);
                        if (messageByID != null && messageByID.getDirection() != 1 && messageByID.getReadStatus() != 1 && messageByID.getRequestStatus() != 1) {
                            String displayName = IPBXMessageSessionItem.fromMessageSession(sessionById).getDisplayName();
                            if (!TextUtils.isEmpty(displayName)) {
                                CmmPBXMessageNotificationManager.onNewMessageReceived(VideoBoxApplication.getNonNullInstance(), str, displayName, messageByID.getText(), null);
                            }
                        }
                    }
                }
            }, 1500);
        }

        public void OnSessionUnreadMessageCountUpdated(PBXSessionUnreadCountList pBXSessionUnreadCountList) {
            super.OnSessionUnreadMessageCountUpdated(pBXSessionUnreadCountList);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }

        public void OnRequestDoneForMarkSessionAsRead(int i, String str, String str2) {
            super.OnRequestDoneForMarkSessionAsRead(i, str, str2);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }

        public void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list) {
            super.OnRequestDoneForUpdateMessageReadStatus(i, str, str2, list);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }

        public void OnFullSyncedSessions(int i) {
            super.OnFullSyncedSessions(i);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }

        public void OnRequestDoneForSyncOldSessions(int i, String str, List<String> list) {
            super.OnRequestDoneForSyncOldSessions(i, str, list);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }

        public void OnSyncedNewSessions(int i, String str, List<String> list, List<String> list2, List<String> list3) {
            super.OnSyncedNewSessions(i, str, list, list2, list3);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }

        public void OnRequestDoneForDeleteSessions(int i, String str, List<String> list) {
            super.OnRequestDoneForDeleteSessions(i, str, list);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }

        public void OnSessionsDeleted(List<String> list) {
            super.OnSessionsDeleted(list);
            IPBXMessageEventSinkUI.getInstance().OnTotalUnreadCountChanged();
        }
    };

    public CmmSIPMessageManager() {
        if (!VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            addListener(this.mIPBXMessageEventSinkUIListener);
        }
    }

    public static CmmSIPMessageManager getInstance() {
        synchronized (CmmSIPMessageManager.class) {
            if (ourInstance == null) {
                ourInstance = new CmmSIPMessageManager();
            }
        }
        return ourInstance;
    }

    public long getMessageEnabledBit() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getMessageEnabledBit();
    }

    public boolean isMessageEnabled() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.isMessageEnabled();
    }

    @Nullable
    public IPBXMessageAPI getMessageAPI() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getMessageAPI();
    }

    public void addListener(IPBXMessageEventSinkUIListener iPBXMessageEventSinkUIListener) {
        IPBXMessageEventSinkUI.getInstance().addListener(iPBXMessageEventSinkUIListener);
    }

    public void removeListener(IPBXMessageEventSinkUIListener iPBXMessageEventSinkUIListener) {
        IPBXMessageEventSinkUI.getInstance().removeListener(iPBXMessageEventSinkUIListener);
    }

    @Nullable
    public String generateLocalSid(String str, List<String> list) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.generateLocalSid(str, list);
    }

    public boolean isLocalSession(@NonNull String str) {
        List allLocalSessionId = getAllLocalSessionId();
        return allLocalSessionId != null && allLocalSessionId.contains(str);
    }

    @Nullable
    public List<String> getAllLocalSessionId() {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.getAllLocalSessionId();
    }

    public int getMessageCountInLocalSession(String str) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return 0;
        }
        return messageAPI.getMessageCountInLocalSession(str);
    }

    @Nullable
    public IPBXMessage getMessageByIndexInLocalSession(String str, int i) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.getMessageByIndexInLocalSession(str, i);
    }

    public IPBXMessage getMessageByIdInLocalSession(String str, String str2) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.getMessageByIdInLocalSession(str, str2);
    }

    @Nullable
    public List<IPBXMessage> getAllMessagesInLocalSession(String str) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.getAllMessagesInLocalSession(str);
    }

    public int getCountOfSession() {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return 0;
        }
        return messageAPI.getCountOfSession();
    }

    @Nullable
    public IPBXMessageSession getSessionByIndex(int i) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.getSessionByIndex(i);
    }

    @Nullable
    public IPBXMessageSession getSessionById(@NonNull String str) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.getSessionById(str);
    }

    public int getNextPageSessions(@NonNull String str, int i) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return 0;
        }
        return messageAPI.getNextPageSessions(str, i);
    }

    public boolean hasMoreOldSessionsToSync() {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return false;
        }
        return messageAPI.hasMoreOldSessionsToSync();
    }

    @Nullable
    public String requestQuerySessionByFromToNumbers(String str, List<String> list) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.requestQuerySessionByFromToNumbers(str, list);
    }

    @Nullable
    public String requestSyncMoreOldSessions(int i) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.requestSyncMoreOldSessions(i);
    }

    @Nullable
    public String requestDeleteSession(String str) {
        return requestDeleteSessions(Collections.singletonList(str));
    }

    @Nullable
    public String requestDeleteSessions(List<String> list) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.requestDeleteSessions(list);
    }

    public void deleteLocalSession(String str) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI != null) {
            messageAPI.deleteLocalSession(str);
            notifyLocalSessionDeleted(str);
        }
    }

    @Nullable
    public String getSessionByToNumbers(@Nullable List<String> list) {
        if (CollectionsUtil.isListEmpty(list)) {
            return null;
        }
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        List<String> directNumberList = CmmSIPCallManager.getInstance().getDirectNumberList();
        if (CollectionsUtil.isListEmpty(directNumberList)) {
            return null;
        }
        for (String sessionByFromToNumbers : directNumberList) {
            IPBXMessageSession sessionByFromToNumbers2 = messageAPI.getSessionByFromToNumbers(sessionByFromToNumbers, list);
            if (sessionByFromToNumbers2 != null) {
                return sessionByFromToNumbers2.getID();
            }
        }
        return null;
    }

    @Nullable
    public String getLocalSessionByToNumbers(@Nullable List<String> list) {
        if (CollectionsUtil.isListEmpty(list)) {
            return null;
        }
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        List<String> allLocalSessionId = messageAPI.getAllLocalSessionId();
        if (CollectionsUtil.isListEmpty(allLocalSessionId)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String formatPhoneNumberAsE164 : list) {
            arrayList.add(ZMPhoneUtils.formatPhoneNumberAsE164(formatPhoneNumberAsE164));
        }
        for (String str : allLocalSessionId) {
            IPBXMessageSessionItem fromMessageSession = IPBXMessageSessionItem.fromMessageSession(str, messageAPI);
            PBXMessageContact me = fromMessageSession.getMe();
            List others = fromMessageSession.getOthers();
            if (me != null && !CollectionsUtil.isListEmpty(others) && arrayList.size() == others.size()) {
                boolean z = true;
                Iterator it = others.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (!arrayList.contains(((PBXMessageContact) it.next()).getPhoneNumber())) {
                            z = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (z) {
                    return str;
                }
            }
        }
        return null;
    }

    @Nullable
    public IPBXMessageSession getSessionByFromToNumbers(@Nullable String str, @Nullable List<String> list) {
        if (StringUtil.isEmptyOrNull(str) || CollectionsUtil.isListEmpty(list)) {
            return null;
        }
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.getSessionByFromToNumbers(str, list);
    }

    @Nullable
    public String sendMessage(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull List<String> list) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.requestSendMessage(str, str2, str3, list);
    }

    @Nullable
    public String requestRetrySendMessage(@NonNull String str, @NonNull String str2) {
        if (!isLocalSession(str)) {
            return null;
        }
        return getMessageAPI().requestRetrySendMessage(str, str2);
    }

    @Nullable
    public String requestMarkSessionAsRead(@NonNull String str) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return null;
        }
        return messageAPI.requestMarkSessionAsRead(str);
    }

    public void handlePushMessage(@NonNull String str) {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI != null) {
            messageAPI.handlePushMessage(str);
        }
    }

    public int getTotalUnreadCount() {
        if (!isMessageEnabled()) {
            return 0;
        }
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI == null) {
            return 0;
        }
        return messageAPI.getTotalUnreadCount();
    }

    public void notifySessionUpdated(String str) {
        if (!TextUtils.isEmpty(str)) {
            IPBXMessageEventSinkUI.getInstance().OnSessionUpdated(str);
        }
    }

    public void notifyNewLocalSessionCreated(@NonNull String str) {
        IPBXMessageEventSinkUI.getInstance().OnNewLocalSessionCreated(str);
    }

    public void notifyLocalSessionDeleted(@NonNull String str) {
        IPBXMessageEventSinkUI.getInstance().OnLocalSessionDeleted(str);
    }

    public void notifyNewLocalSessionMessageCreated(@NonNull String str, @NonNull String str2) {
        if (isLocalSession(str)) {
            IPBXMessageEventSinkUI.getInstance().OnNewLocalSessionMessageCreated(str, str2);
        }
    }

    public void notifyLocalSessionMessageDeleted(@NonNull String str, @NonNull String str2) {
        if (isLocalSession(str)) {
            IPBXMessageEventSinkUI.getInstance().OnLocalSessionMessageDeleted(str, str2);
        }
    }

    public void release() {
        IPBXMessageAPI messageAPI = getMessageAPI();
        if (messageAPI != null) {
            messageAPI.release();
        }
    }
}
