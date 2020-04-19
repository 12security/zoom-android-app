package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.ZoomXMPPRoom;
import com.zipow.videobox.ptapp.ZoomPublicRoomSearchUI;
import com.zipow.videobox.view.p014mm.MMZoomXMPPRoom;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomPublicRoomSearchData */
public class ZoomPublicRoomSearchData {
    private long mNativeHandle = 0;

    private native void cancelSearchImpl(long j);

    private native boolean getNextPageImpl(long j);

    private native int getRoomCountImpl(long j);

    @Nullable
    private native String getSearchKeyImpl(long j);

    @Nullable
    private native byte[] getZoomXMPPRoomAtImpl(long j, int i);

    private native boolean hasMoreDataOnServerSideImpl(long j);

    private native boolean isSearchingImpl(long j);

    private native boolean joinRoomImpl(long j, String str);

    private native boolean searchImpl(long j, String str, int i);

    private native void setCallbackImpl(long j, long j2);

    public ZoomPublicRoomSearchData(long j) {
        this.mNativeHandle = j;
    }

    public void setCallback(@NonNull ZoomPublicRoomSearchUI zoomPublicRoomSearchUI) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setCallbackImpl(j, zoomPublicRoomSearchUI.getNativeHandle());
        }
    }

    public boolean search(@Nullable String str, int i) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        if (str == null) {
            str = "";
        }
        return searchImpl(this.mNativeHandle, str, i);
    }

    public boolean getNextPage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return getNextPageImpl(j);
    }

    public void cancelSearch() {
        long j = this.mNativeHandle;
        if (j != 0) {
            cancelSearchImpl(j);
        }
    }

    public int getRoomCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRoomCountImpl(j);
    }

    @Nullable
    public MMZoomXMPPRoom getZoomXMPPRoomAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] zoomXMPPRoomAtImpl = getZoomXMPPRoomAtImpl(j, i);
        if (zoomXMPPRoomAtImpl == null) {
            return null;
        }
        try {
            ZoomXMPPRoom parseFrom = ZoomXMPPRoom.parseFrom(zoomXMPPRoomAtImpl);
            MMZoomXMPPRoom mMZoomXMPPRoom = new MMZoomXMPPRoom();
            mMZoomXMPPRoom.setJid(parseFrom.getJid());
            mMZoomXMPPRoom.setName(parseFrom.getName());
            mMZoomXMPPRoom.setCount(parseFrom.getMemberCount());
            return mMZoomXMPPRoom;
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public String getSearchKey() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSearchKeyImpl(j);
    }

    public boolean hasMoreDataOnServerSide() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasMoreDataOnServerSideImpl(j);
    }

    public boolean isSearching() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSearchingImpl(j);
    }

    public boolean joinRoom(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return joinRoomImpl(j, str);
    }
}
