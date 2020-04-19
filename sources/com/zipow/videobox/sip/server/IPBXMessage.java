package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContactList;
import java.util.List;

public class IPBXMessage {
    private long mNativeHandle;

    private native long getCreateTimeImpl(long j);

    private native int getDirectionImpl(long j);

    private native byte[] getFromContactImpl(long j);

    private native String getIDImpl(long j);

    private native String getLocalSIDImpl(long j);

    private native List<String> getMediaUrlsImpl(long j);

    private native byte[] getOwnerContactImpl(long j);

    private native String getPreviousIDImpl(long j);

    private native int getReadStatusImpl(long j);

    private native int getRequestStatusImpl(long j);

    private native int getSendErrorCodeImpl(long j);

    private native int getSendStatusImpl(long j);

    private native String getSessionIDImpl(long j);

    private native String getTextImpl(long j);

    private native byte[] getToContactsImpl(long j);

    private native long getUpdatedTimeImpl(long j);

    public IPBXMessage(long j) {
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
    public String getPreviousID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPreviousIDImpl(j);
    }

    @Nullable
    public String getSessionID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSessionIDImpl(j);
    }

    @Nullable
    public PBXMessageContact getFromContact() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] fromContactImpl = getFromContactImpl(j);
        if (fromContactImpl == null || fromContactImpl.length <= 0) {
            return null;
        }
        try {
            return PBXMessageContact.parseFrom(fromContactImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public List<PBXMessageContact> getToContacts() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] toContactsImpl = getToContactsImpl(j);
        if (toContactsImpl == null || toContactsImpl.length <= 0) {
            return null;
        }
        try {
            PBXMessageContactList parseFrom = PBXMessageContactList.parseFrom(toContactsImpl);
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

    @Nullable
    public PBXMessageContact getOwnerContact() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] ownerContactImpl = getOwnerContactImpl(j);
        if (ownerContactImpl == null || ownerContactImpl.length <= 0) {
            return null;
        }
        try {
            return PBXMessageContact.parseFrom(ownerContactImpl);
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

    @Nullable
    public String getText() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getTextImpl(j);
    }

    public long getCreateTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCreateTimeImpl(j);
    }

    public long getUpdatedTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUpdatedTimeImpl(j);
    }

    @Nullable
    public List<String> getMediaUrls() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMediaUrlsImpl(j);
    }

    public int getReadStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getReadStatusImpl(j);
    }

    public int getSendStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSendStatusImpl(j);
    }

    public int getSendErrorCode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSendErrorCodeImpl(j);
    }

    public int getRequestStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRequestStatusImpl(j);
    }

    @Nullable
    public String getLocalSID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLocalSIDImpl(j);
    }
}
