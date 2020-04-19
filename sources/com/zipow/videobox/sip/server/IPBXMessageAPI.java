package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class IPBXMessageAPI {
    private long mNativeHandle;

    private native void deleteLocalSessionImpl(long j, String str);

    private native void deleteMessagesInLocalSessionImpl(long j, String str, List<String> list);

    private native String generateLocalSidImpl(long j, String str, List<String> list);

    private native List<String> getAllLocalSessionIdImpl(long j);

    private native List<Long> getAllMessagesInLocalSessionImpl(long j, String str);

    private native int getCountOfSessionImpl(long j);

    private native long getMessageByIdInLocalSessionImpl(long j, String str, String str2);

    private native long getMessageByIndexInLocalSessionImpl(long j, String str, int i);

    private native int getMessageCountInLocalSessionImpl(long j, String str);

    private native int getNextPageSessionsImpl(long j, String str, int i);

    private native long getSessionByFromToNumbersImpl(long j, String str, List<String> list);

    private native long getSessionByIdImpl(long j, String str);

    private native long getSessionByIndexImpl(long j, int i);

    private native int getTotalUnreadCountImpl(long j);

    private native void handlePushMessageImpl(long j, String str);

    private native boolean hasMoreOldSessionsToSyncImpl(long j);

    private native void initializeImpl(long j, long j2);

    private native boolean isInitedImpl(long j);

    private native void releaseImpl(long j);

    private native String requestDeleteSessionsImpl(long j, List<String> list);

    private native String requestMarkSessionAsReadImpl(long j, String str);

    private native String requestQuerySessionByFromToNumbersImpl(long j, String str, List<String> list);

    private native String requestRetrySendMessageImpl(long j, String str, String str2);

    private native String requestSendMessageImpl(long j, String str, String str2, String str3, List<String> list);

    private native String requestSyncMoreOldSessionsImpl(long j, int i);

    private native String requestSyncNewSessionsImpl(long j);

    public IPBXMessageAPI(long j) {
        this.mNativeHandle = j;
    }

    public void initialize(@NonNull IPBXMessageEventSinkUI iPBXMessageEventSinkUI) {
        long j = this.mNativeHandle;
        if (j != 0) {
            initializeImpl(j, iPBXMessageEventSinkUI.getNativeHandle());
        }
    }

    public void release() {
        long j = this.mNativeHandle;
        if (j != 0) {
            releaseImpl(j);
        }
    }

    @Nullable
    public String generateLocalSid(@NonNull String str, @NonNull List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return generateLocalSidImpl(j, str, list);
    }

    @Nullable
    public List<String> getAllLocalSessionId() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAllLocalSessionIdImpl(j);
    }

    public int getMessageCountInLocalSession(@NonNull String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMessageCountInLocalSessionImpl(j, str);
    }

    @Nullable
    public IPBXMessage getMessageByIndexInLocalSession(@NonNull String str, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long messageByIndexInLocalSessionImpl = getMessageByIndexInLocalSessionImpl(j, str, i);
        if (messageByIndexInLocalSessionImpl == 0) {
            return null;
        }
        return new IPBXMessage(messageByIndexInLocalSessionImpl);
    }

    @Nullable
    public IPBXMessage getMessageByIdInLocalSession(@NonNull String str, @NonNull String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long messageByIdInLocalSessionImpl = getMessageByIdInLocalSessionImpl(j, str, str2);
        if (messageByIdInLocalSessionImpl == 0) {
            return null;
        }
        return new IPBXMessage(messageByIdInLocalSessionImpl);
    }

    @Nullable
    public List<IPBXMessage> getAllMessagesInLocalSession(@NonNull String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        List<Long> allMessagesInLocalSessionImpl = getAllMessagesInLocalSessionImpl(j, str);
        if (CollectionsUtil.isListEmpty(allMessagesInLocalSessionImpl)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Long l : allMessagesInLocalSessionImpl) {
            if (!(l == null || l.longValue() == 0)) {
                arrayList.add(new IPBXMessage(l.longValue()));
            }
        }
        return arrayList;
    }

    public int getCountOfSession() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCountOfSessionImpl(j);
    }

    @Nullable
    public IPBXMessageSession getSessionByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sessionByIndexImpl = getSessionByIndexImpl(j, i);
        if (sessionByIndexImpl == 0) {
            return null;
        }
        return new IPBXMessageSession(sessionByIndexImpl);
    }

    @Nullable
    public IPBXMessageSession getSessionById(@NonNull String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sessionByIdImpl = getSessionByIdImpl(j, str);
        if (sessionByIdImpl == 0) {
            return null;
        }
        return new IPBXMessageSession(sessionByIdImpl);
    }

    public int getNextPageSessions(@NonNull String str, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getNextPageSessionsImpl(j, str, i);
    }

    public boolean hasMoreOldSessionsToSync() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasMoreOldSessionsToSyncImpl(j);
    }

    @Nullable
    public IPBXMessageSession getSessionByFromToNumbers(@NonNull String str, @NonNull List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sessionByFromToNumbersImpl = getSessionByFromToNumbersImpl(j, str, list);
        if (sessionByFromToNumbersImpl == 0) {
            return null;
        }
        return new IPBXMessageSession(sessionByFromToNumbersImpl);
    }

    @Nullable
    public String requestQuerySessionByFromToNumbers(@NonNull String str, @NonNull List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestQuerySessionByFromToNumbersImpl(j, str, list);
    }

    @Nullable
    public String requestSyncNewSessions() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestSyncNewSessionsImpl(j);
    }

    @Nullable
    public String requestSyncMoreOldSessions(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestSyncMoreOldSessionsImpl(j, i);
    }

    @Nullable
    public String requestDeleteSessions(@NonNull List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestDeleteSessionsImpl(j, list);
    }

    public void deleteLocalSession(String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            deleteLocalSessionImpl(j, str);
        }
    }

    public void deleteMessagesInLocalSession(@NonNull String str, @NonNull List<String> list) {
        long j = this.mNativeHandle;
        if (j != 0) {
            deleteMessagesInLocalSessionImpl(j, str, list);
        }
    }

    @Nullable
    public String requestSendMessage(String str, String str2, @NonNull String str3, @NonNull List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestSendMessageImpl(j, str, str2, str3, list);
    }

    @Nullable
    public String requestRetrySendMessage(@NonNull String str, @NonNull String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestRetrySendMessageImpl(j, str, str2);
    }

    @Nullable
    public String requestMarkSessionAsRead(@NonNull String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestMarkSessionAsReadImpl(j, str);
    }

    public void handlePushMessage(@NonNull String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            handlePushMessageImpl(j, str);
        }
    }

    public int getTotalUnreadCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTotalUnreadCountImpl(j);
    }

    public boolean isInited() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInitedImpl(j);
    }
}
