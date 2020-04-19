package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.view.IMAddrBookItem;
import java.io.Serializable;
import java.util.Date;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomSubscribeRequest */
public class ZoomSubscribeRequest implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean isMyBuddy;
    private IMAddrBookItem mIMAddrBookItem;
    private long mNativeHandle = 0;

    private native int getRequestIndexImpl(long j);

    @Nullable
    private native String getRequestJIDImpl(long j);

    @Nullable
    private native String getRequestMsgImpl(long j);

    private native int getRequestStatusImpl(long j);

    private native long getRequestTimeStampImpl(long j);

    private native int getRequestTypeImpl(long j);

    public IMAddrBookItem getIMAddrBookItem() {
        return this.mIMAddrBookItem;
    }

    public void setIMAddrBookItem(IMAddrBookItem iMAddrBookItem) {
        this.mIMAddrBookItem = iMAddrBookItem;
    }

    public ZoomSubscribeRequest(long j) {
        this.mNativeHandle = j;
    }

    public int getRequestIndex() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRequestIndexImpl(j);
    }

    @Nullable
    public String getRequestJID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getRequestJIDImpl(j);
    }

    @Nullable
    public Date getRequestTimeStamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return new Date(getRequestTimeStampImpl(j));
    }

    @Nullable
    public String getRequestMsg() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getRequestMsgImpl(j);
    }

    public int getRequestStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return getRequestStatusImpl(j);
    }

    public int getRequestType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRequestTypeImpl(j);
    }

    public boolean isMyBuddy() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isMyContact(getRequestJID());
    }

    public void setMyBuddy(boolean z) {
        this.isMyBuddy = z;
    }
}
