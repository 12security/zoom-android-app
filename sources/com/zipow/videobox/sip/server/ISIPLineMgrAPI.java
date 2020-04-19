package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLine;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLineCallItem;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPUser;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPUsers;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSipLineInfoForCallerID;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSipLineInfoForCallerIDList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class ISIPLineMgrAPI {
    private long mNativeHandle;

    private native byte[] getAllLineInfoforCallerIDImpl(long j);

    private native long getCurrentSelectedLineImpl(long j);

    private native long getLineCallItemByIDImpl(long j, String str);

    private native byte[] getLineCallItemProtoByIDImpl(long j, String str);

    private native long getLineItemByIDImpl(long j, String str);

    private native byte[] getLineItemProtoByIDImpl(long j, String str);

    private native long getMineExtensionLineImpl(long j);

    private native long getMySelfImpl(long j);

    private native byte[] getMySelfProtoImpl(long j);

    private native long getSharedUserByIDImpl(long j, String str);

    private native long getSharedUserByIndexImpl(long j, int i);

    private native int getSharedUserCountImpl(long j);

    private native byte[] getSharedUserProtoByIDImpl(long j, String str);

    private native byte[] getSharedUsersImpl(long j);

    private native boolean pickupImpl(long j, String str);

    private native boolean registerImpl(long j);

    private native boolean registerLineImpl(long j, String str);

    private native void setLineEventSinkImpl(long j, long j2);

    private native boolean switchLineImpl(long j, String str);

    private native boolean unRegisterImpl(long j);

    private native boolean unRegisterLineImpl(long j, String str);

    public ISIPLineMgrAPI(long j) {
        this.mNativeHandle = j;
    }

    public void setLineEventSink(ISIPLineMgrEventSinkUI iSIPLineMgrEventSinkUI) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setLineEventSinkImpl(j, iSIPLineMgrEventSinkUI.getNativeHandle());
        }
    }

    @Nullable
    public CmmSIPUser getMySelf() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long mySelfImpl = getMySelfImpl(j);
        if (mySelfImpl == 0) {
            return null;
        }
        return new CmmSIPUser(mySelfImpl);
    }

    @Nullable
    public CmmSIPUser getMySelfProto() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            byte[] mySelfProtoImpl = getMySelfProtoImpl(j);
            if (mySelfProtoImpl != null && mySelfProtoImpl.length > 0) {
                return CmmSIPUser.parseFrom(mySelfProtoImpl);
            }
        } catch (InvalidProtocolBufferException unused) {
        }
        return null;
    }

    @Nullable
    public CmmSIPLine getMineExtensionLine() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long mineExtensionLineImpl = getMineExtensionLineImpl(j);
        if (mineExtensionLineImpl == 0) {
            return null;
        }
        return new CmmSIPLine(mineExtensionLineImpl);
    }

    @Nullable
    public CmmSIPLine getCurrentSelectedLine() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long currentSelectedLineImpl = getCurrentSelectedLineImpl(j);
        if (currentSelectedLineImpl == 0) {
            return null;
        }
        return new CmmSIPLine(currentSelectedLineImpl);
    }

    public int getSharedUserCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSharedUserCountImpl(j);
    }

    @Nullable
    public CmmSIPUser getSharedUserByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sharedUserByIndexImpl = getSharedUserByIndexImpl(j, i);
        if (sharedUserByIndexImpl == 0) {
            return null;
        }
        return new CmmSIPUser(sharedUserByIndexImpl);
    }

    @Nullable
    public CmmSIPUser getSharedUserByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sharedUserByIDImpl = getSharedUserByIDImpl(j, StringUtil.safeString(str));
        if (sharedUserByIDImpl == 0) {
            return null;
        }
        return new CmmSIPUser(sharedUserByIDImpl);
    }

    @Nullable
    public CmmSIPUser getSharedUserProtoByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            byte[] sharedUserProtoByIDImpl = getSharedUserProtoByIDImpl(j, StringUtil.safeString(str));
            if (sharedUserProtoByIDImpl != null && sharedUserProtoByIDImpl.length > 0) {
                return CmmSIPUser.parseFrom(sharedUserProtoByIDImpl);
            }
        } catch (InvalidProtocolBufferException unused) {
        }
        return null;
    }

    @Nullable
    public List<CmmSIPUser> getSharedUsers() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] sharedUsersImpl = getSharedUsersImpl(j);
        if (sharedUsersImpl == null || sharedUsersImpl.length <= 0) {
            return null;
        }
        try {
            CmmSIPUsers parseFrom = CmmSIPUsers.parseFrom(sharedUsersImpl);
            if (parseFrom != null) {
                if (parseFrom.getUsersCount() > 0) {
                    return parseFrom.getUsersList();
                }
            }
            return null;
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public List<CmmSipLineInfoForCallerID> getAllLineInfoforCallerID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] allLineInfoforCallerIDImpl = getAllLineInfoforCallerIDImpl(j);
        if (allLineInfoforCallerIDImpl == null || allLineInfoforCallerIDImpl.length <= 0) {
            return null;
        }
        try {
            CmmSipLineInfoForCallerIDList parseFrom = CmmSipLineInfoForCallerIDList.parseFrom(allLineInfoforCallerIDImpl);
            if (parseFrom != null) {
                if (parseFrom.getCallerIdCount() > 0) {
                    return parseFrom.getCallerIdList();
                }
            }
            return null;
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public CmmSIPLine getLineItemByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long lineItemByIDImpl = getLineItemByIDImpl(j, StringUtil.safeString(str));
        if (lineItemByIDImpl == 0) {
            return null;
        }
        return new CmmSIPLine(lineItemByIDImpl);
    }

    @Nullable
    public CmmSIPLine getLineItemProtoByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            byte[] lineItemProtoByIDImpl = getLineItemProtoByIDImpl(j, StringUtil.safeString(str));
            if (lineItemProtoByIDImpl != null && lineItemProtoByIDImpl.length > 0) {
                return CmmSIPLine.parseFrom(lineItemProtoByIDImpl);
            }
        } catch (InvalidProtocolBufferException unused) {
        }
        return null;
    }

    @Nullable
    public CmmSIPLineCallItem getLineCallItemByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long lineCallItemByIDImpl = getLineCallItemByIDImpl(j, StringUtil.safeString(str));
        if (lineCallItemByIDImpl == 0) {
            return null;
        }
        return new CmmSIPLineCallItem(lineCallItemByIDImpl);
    }

    @Nullable
    public CmmSIPLineCallItem getLineCallItemProtoByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            byte[] lineCallItemProtoByIDImpl = getLineCallItemProtoByIDImpl(j, StringUtil.safeString(str));
            if (lineCallItemProtoByIDImpl != null && lineCallItemProtoByIDImpl.length > 0) {
                return CmmSIPLineCallItem.parseFrom(lineCallItemProtoByIDImpl);
            }
        } catch (InvalidProtocolBufferException unused) {
        }
        return null;
    }

    public boolean register() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return registerImpl(j);
    }

    public boolean unRegister() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return unRegisterImpl(j);
    }

    public boolean registerLine(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return registerLineImpl(j, StringUtil.safeString(str));
    }

    public boolean unRegisterLine(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return unRegisterLineImpl(j, StringUtil.safeString(str));
    }

    public boolean pickup(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return pickupImpl(j, StringUtil.safeString(str));
    }

    public boolean switchLine(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return switchLineImpl(j, StringUtil.safeString(str));
    }
}
