package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContactList;
import java.util.List;

public class IPBXMessageSession {
    private long mNativeHandle;

    private native int getCountOfMessageImpl(long j);

    private native int getDirectionImpl(long j);

    private native String getDraftTextImpl(long j);

    private native String getIDImpl(long j);

    private native String getLastViewedMessageIdImpl(long j);

    private native long getLatestMessageImpl(long j);

    private native byte[] getMeImpl(long j);

    private native long getMessageByIDImpl(long j, String str);

    private native long getMessageByIndexImpl(long j, int i);

    private native int getNextPageMessagesImpl(long j, String str, int i);

    private native byte[] getOthersImpl(long j);

    private native String getSummaryImpl(long j);

    private native int getTotalUnReadCountImpl(long j);

    private native long getUpdatedTimeImpl(long j);

    private native boolean hasMoreOldMessagesToSyncImpl(long j);

    private native boolean initializeImpl(long j);

    private native boolean isInitedImpl(long j);

    private native String requestDeleteMessageImpl(long j, List<String> list);

    private native String requestRetrySendMessageImpl(long j, String str);

    private native String requestSendMessageImpl(long j, String str);

    private native String requestSyncNewMessagesImpl(long j);

    private native String requestSyncOldMessagesImpl(long j, int i);

    private native String requestUpdateAllMessageAsReadImpl(long j);

    private native String requestUpdateMessageReadStatusImpl(long j, List<String> list, int i);

    private native void setDraftTextImpl(long j, String str);

    private native void setLastViewedMessageIdImpl(long j, String str);

    public IPBXMessageSession(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getIDImpl(j);
    }

    @Nullable
    public IPBXMessage getLatestMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long latestMessageImpl = getLatestMessageImpl(j);
        if (latestMessageImpl == 0) {
            return null;
        }
        return new IPBXMessage(latestMessageImpl);
    }

    @Nullable
    public String getSummary() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSummaryImpl(j);
    }

    @Nullable
    public PBXMessageContact getMe() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] meImpl = getMeImpl(j);
        if (meImpl == null || meImpl.length <= 0) {
            return null;
        }
        try {
            return PBXMessageContact.parseFrom(meImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public List<PBXMessageContact> getOthers() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] othersImpl = getOthersImpl(j);
        if (othersImpl == null || othersImpl.length <= 0) {
            return null;
        }
        try {
            PBXMessageContactList parseFrom = PBXMessageContactList.parseFrom(othersImpl);
            if (parseFrom != null) {
                if (parseFrom.getContactsCount() > 0) {
                    return parseFrom.getContactsList();
                }
            }
            return null;
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public int getDirection() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getDirectionImpl(j);
    }

    public int getTotalUnreadCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTotalUnReadCountImpl(j);
    }

    @Nullable
    public String getDraftText() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getDraftTextImpl(j);
    }

    public void setDraftText(String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setDraftTextImpl(j, str);
        }
    }

    public long getUpdatedTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUpdatedTimeImpl(j);
    }

    @Nullable
    public String getLastViewedMessageId() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLastViewedMessageIdImpl(j);
    }

    public void setLastViewedMessageId(String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setLastViewedMessageIdImpl(j, str);
        }
    }

    public void initialize() {
        long j = this.mNativeHandle;
        if (j != 0) {
            initializeImpl(j);
        }
    }

    @Nullable
    public String requestSyncNewMessages() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestSyncNewMessagesImpl(j);
    }

    @Nullable
    public String requestSyncOldMessages(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestSyncOldMessagesImpl(j, i);
    }

    public int getCountOfMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCountOfMessageImpl(j);
    }

    @Nullable
    public IPBXMessage getMessageByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long messageByIndexImpl = getMessageByIndexImpl(j, i);
        if (messageByIndexImpl == 0) {
            return null;
        }
        return new IPBXMessage(messageByIndexImpl);
    }

    @Nullable
    public IPBXMessage getMessageByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long messageByIDImpl = getMessageByIDImpl(j, str);
        if (messageByIDImpl == 0) {
            return null;
        }
        return new IPBXMessage(messageByIDImpl);
    }

    public int getNextPageMessages(@NonNull String str, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getNextPageMessagesImpl(j, str, i);
    }

    @Nullable
    public String requestDeleteMessage(List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestDeleteMessageImpl(j, list);
    }

    @Nullable
    public String requestSendMessage(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestSendMessageImpl(j, str);
    }

    @Nullable
    public String requestRetrySendMessage(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestRetrySendMessageImpl(j, str);
    }

    public boolean hasMoreOldMessagesToSync() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasMoreOldMessagesToSyncImpl(j);
    }

    public String requestUpdateMessageReadStatus(@NonNull List<String> list, int i) {
        if (this.mNativeHandle != 0 && !list.isEmpty()) {
            return requestUpdateMessageReadStatusImpl(this.mNativeHandle, list, i);
        }
        return null;
    }

    public String requestUpdateAllMessageAsRead() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return requestUpdateAllMessageAsReadImpl(j);
    }

    public boolean isInited() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInitedImpl(j);
    }
}
