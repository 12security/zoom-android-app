package com.zipow.videobox.sip;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CallHistoryList;
import com.zipow.videobox.ptapp.PTAppProtos.CallHistoryList.Builder;
import com.zipow.videobox.ptapp.PTAppProtos.CallHistoryProto;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class CallHistoryMgr {
    private static final String TAG = "CallHistoryMgr";
    private long mNativeHandle;

    private native boolean addCallHistoryImpl(long j, byte[] bArr);

    private native boolean addCallHistoryListImpl(long j, byte[] bArr);

    private native boolean clearAllCallHistoryImpl(long j);

    private native boolean clearMissedCallInImpl(long j);

    private native boolean deleteCallHistoryImpl(long j, String str);

    @Nullable
    private native byte[] getAllMissedCallInImpl(long j);

    @Nullable
    private native byte[] getCallHistoryByIDImpl(long j, String str);

    @Nullable
    private native byte[] getCallHistoryImpl(long j);

    private native int getMissedCallInCountImpl(long j);

    private native boolean hasHistoryWithIdImpl(long j, String str);

    private native boolean updateCallHistoryImpl(long j, byte[] bArr);

    public CallHistoryMgr(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public List<CallHistory> getCallHistory(boolean z) {
        CallHistoryList callHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] callHistoryImpl = getCallHistoryImpl(j);
        if (callHistoryImpl == null) {
            return null;
        }
        try {
            callHistoryList = CallHistoryList.parseFrom(callHistoryImpl);
        } catch (InvalidProtocolBufferException unused) {
            callHistoryList = null;
        }
        if (callHistoryList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int callhistorysCount = callHistoryList.getCallhistorysCount();
        for (int i = 0; i < callhistorysCount; i++) {
            CallHistoryProto callhistorys = callHistoryList.getCallhistorys(i);
            if (z) {
                int state = callhistorys.getState();
                if (callhistorys.getDirection() == 1) {
                    if (!(state == 1 || state == 4)) {
                    }
                }
            }
            CallHistory callHistory = new CallHistory();
            callHistory.setCalleeJid(callhistorys.getCalleeJid());
            callHistory.setCalleeUri(callhistorys.getCalleeUri());
            callHistory.setCalleeDisplayName(callhistorys.getCalleeDisplayName());
            callHistory.setCallerJid(callhistorys.getCallerJid());
            callHistory.setCallerUri(callhistorys.getCallerUri());
            callHistory.setCallerDisplayName(callhistorys.getCallerDisplayName());
            callHistory.setId(callhistorys.getId());
            callHistory.setNumber(callhistorys.getNumber());
            callHistory.setState(callhistorys.getState());
            callHistory.setTime(callhistorys.getTime());
            callHistory.setTimeLong(callhistorys.getTimeLong());
            callHistory.setType(callhistorys.getType());
            callHistory.setDirection(callhistorys.getDirection());
            callHistory.updateZOOMDisplayName();
            arrayList.add(callHistory);
        }
        return arrayList;
    }

    @Nullable
    public List<CallHistory> getSipCallHistory(boolean z) {
        CallHistoryList callHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] callHistoryImpl = getCallHistoryImpl(j);
        if (callHistoryImpl == null) {
            return null;
        }
        try {
            callHistoryList = CallHistoryList.parseFrom(callHistoryImpl);
        } catch (InvalidProtocolBufferException unused) {
            callHistoryList = null;
        }
        if (callHistoryList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int callhistorysCount = callHistoryList.getCallhistorysCount();
        for (int i = 0; i < callhistorysCount; i++) {
            CallHistoryProto callhistorys = callHistoryList.getCallhistorys(i);
            if (callhistorys.getType() == 3) {
                if (z) {
                    int state = callhistorys.getState();
                    if (callhistorys.getDirection() == 1) {
                        if (!(state == 1 || state == 4)) {
                        }
                    }
                }
                CallHistory callHistory = new CallHistory();
                callHistory.setCalleeJid(callhistorys.getCalleeJid());
                callHistory.setCalleeUri(callhistorys.getCalleeUri());
                callHistory.setCalleeDisplayName(callhistorys.getCalleeDisplayName());
                callHistory.setCallerJid(callhistorys.getCallerJid());
                callHistory.setCallerUri(callhistorys.getCallerUri());
                callHistory.setCallerDisplayName(callhistorys.getCallerDisplayName());
                callHistory.setId(callhistorys.getId());
                callHistory.setNumber(callhistorys.getNumber());
                callHistory.setState(callhistorys.getState());
                callHistory.setTime(callhistorys.getTime());
                callHistory.setTimeLong(callhistorys.getTimeLong());
                callHistory.setType(callhistorys.getType());
                callHistory.setDirection(callhistorys.getDirection());
                callHistory.updateZOOMDisplayName();
                arrayList.add(callHistory);
            }
        }
        return arrayList;
    }

    public void insertZoomMeetingHistory(String str, int i, String str2, boolean z, boolean z2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            CallHistory callHistory = new CallHistory();
            callHistory.setType(z ? 2 : 1);
            callHistory.setState(i);
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str2);
            if (buddyWithJID != null) {
                String buddyDisplayName = BuddyNameUtil.getBuddyDisplayName(buddyWithJID, null);
                if (z2) {
                    callHistory.setCalleeJid(str2);
                    callHistory.setDirection(2);
                    callHistory.setCalleeDisplayName(buddyDisplayName);
                } else {
                    callHistory.setCallerJid(str2);
                    callHistory.setDirection(1);
                    callHistory.setCallerDisplayName(buddyDisplayName);
                }
                callHistory.setId(str);
                callHistory.setNumber(str);
                callHistory.setTime(CmmTime.getMMNow());
                addCallHistory(callHistory);
            }
        }
    }

    public boolean clearAllCallHistory() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return clearAllCallHistoryImpl(j);
    }

    public boolean deleteCallHistory(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return deleteCallHistoryImpl(this.mNativeHandle, str);
    }

    public boolean addCallHistory(@Nullable CallHistory callHistory) {
        if (this.mNativeHandle == 0 || callHistory == null || StringUtil.isEmptyOrNull(callHistory.getId())) {
            return false;
        }
        CallHistoryProto itemToProto = itemToProto(callHistory);
        if (itemToProto == null) {
            return false;
        }
        return addCallHistoryImpl(this.mNativeHandle, itemToProto.toByteArray());
    }

    public boolean addOrUpdateCallHistory(@Nullable CallHistory callHistory) {
        if (callHistory == null || StringUtil.isEmptyOrNull(callHistory.getId())) {
            return false;
        }
        if (hasHistoryWithIdImpl(this.mNativeHandle, callHistory.getId())) {
            return updateCallHistory(callHistory);
        }
        return addCallHistory(callHistory);
    }

    public boolean addCallHistoryList(@NonNull List<CallHistory> list) {
        if (this.mNativeHandle == 0 || CollectionsUtil.isListEmpty(list)) {
            return false;
        }
        Builder newBuilder = CallHistoryList.newBuilder();
        for (CallHistory itemToProto : list) {
            CallHistoryProto itemToProto2 = itemToProto(itemToProto);
            if (itemToProto2 != null) {
                newBuilder.addCallhistorys(itemToProto2);
            }
        }
        return addCallHistoryListImpl(this.mNativeHandle, newBuilder.build().toByteArray());
    }

    public boolean updateCallHistory(@Nullable CallHistory callHistory) {
        if (this.mNativeHandle == 0 || callHistory == null || StringUtil.isEmptyOrNull(callHistory.getId())) {
            return false;
        }
        CallHistoryProto itemToProto = itemToProto(callHistory);
        if (itemToProto == null) {
            return false;
        }
        return updateCallHistoryImpl(this.mNativeHandle, itemToProto.toByteArray());
    }

    @Nullable
    private CallHistoryProto itemToProto(@Nullable CallHistory callHistory) {
        if (callHistory == null) {
            return null;
        }
        try {
            CallHistoryProto.Builder newBuilder = CallHistoryProto.newBuilder();
            if (callHistory.getCalleeJid() != null) {
                newBuilder.setCalleeJid(callHistory.getCalleeJid());
            }
            if (callHistory.getCalleeUri() != null) {
                newBuilder.setCalleeUri(callHistory.getCalleeUri());
            }
            if (callHistory.getCalleeDisplayName() != null) {
                newBuilder.setCalleeDisplayName(callHistory.getCalleeDisplayName());
            }
            if (callHistory.getCallerJid() != null) {
                newBuilder.setCallerJid(callHistory.getCallerJid());
            }
            if (callHistory.getCallerUri() != null) {
                newBuilder.setCallerUri(callHistory.getCallerUri());
            }
            if (callHistory.getCallerDisplayName() != null) {
                newBuilder.setCallerDisplayName(callHistory.getCallerDisplayName());
            }
            newBuilder.setId(callHistory.getId());
            newBuilder.setNumber(callHistory.getNumber());
            newBuilder.setState(callHistory.getState());
            newBuilder.setTime(callHistory.getTime());
            newBuilder.setTimeLong(callHistory.getTimeLong());
            newBuilder.setType(callHistory.getType());
            newBuilder.setDirection(callHistory.getDirection());
            return newBuilder.build();
        } catch (Exception unused) {
            return null;
        }
    }

    public int getMissedCallInCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMissedCallInCountImpl(j);
    }

    public boolean clearMissedCallIn() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return clearMissedCallInImpl(j);
    }

    @Nullable
    public CallHistory getCallHistoryByID(String str) {
        CallHistoryProto callHistoryProto;
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] callHistoryByIDImpl = getCallHistoryByIDImpl(this.mNativeHandle, str);
        if (callHistoryByIDImpl == null || callHistoryByIDImpl.length <= 0) {
            return null;
        }
        try {
            callHistoryProto = CallHistoryProto.parseFrom(callHistoryByIDImpl);
        } catch (InvalidProtocolBufferException unused) {
            callHistoryProto = null;
        }
        if (callHistoryProto == null) {
            return null;
        }
        CallHistory callHistory = new CallHistory();
        callHistory.setCalleeJid(callHistoryProto.getCalleeJid());
        callHistory.setCalleeUri(callHistoryProto.getCalleeUri());
        callHistory.setCalleeDisplayName(callHistoryProto.getCalleeDisplayName());
        callHistory.setCallerJid(callHistoryProto.getCallerJid());
        callHistory.setCallerUri(callHistoryProto.getCallerUri());
        callHistory.setCallerDisplayName(callHistoryProto.getCallerDisplayName());
        callHistory.setId(callHistoryProto.getId());
        callHistory.setNumber(callHistoryProto.getNumber());
        callHistory.setState(callHistoryProto.getState());
        callHistory.setTime(callHistoryProto.getTime());
        callHistory.setTimeLong(callHistoryProto.getTimeLong());
        callHistory.setType(callHistoryProto.getType());
        callHistory.setDirection(callHistoryProto.getDirection());
        callHistory.updateZOOMDisplayName();
        return callHistory;
    }
}
