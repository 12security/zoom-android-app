package com.zipow.videobox.confapp;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CmmUserList {
    private long mNativeHandle = 0;

    private native long getHostUserImpl(long j);

    private native long getLeftUserByIdImpl(long j, long j2);

    private native long getMyselfImpl(long j);

    private native long[] getNoAudioClientUsersImpl(long j, boolean z);

    private native long getPeerUserImpl(long j, boolean z, boolean z2);

    private native long[] getPureCallInUsersImpl(long j, boolean z);

    private native int getSilentModeUserCountImpl(long j);

    private native long getUserAtImpl(long j, int i);

    private native long getUserByIdImpl(long j, long j2);

    private native int getUserCountImpl(long j);

    private native boolean hasCoHostUserInMeetingImpl(long j);

    private native boolean hasNoAudioClientUserImpl(long j, boolean z);

    private native boolean hasPureCallInUserImpl(long j, boolean z);

    public CmmUserList(long j) {
        this.mNativeHandle = j;
    }

    public int getUserCount() {
        return getUserCountImpl(this.mNativeHandle);
    }

    @Nullable
    public CmmUser getUserAt(int i) {
        long userAtImpl = getUserAtImpl(this.mNativeHandle, i);
        if (userAtImpl == 0) {
            return null;
        }
        return new CmmUser(userAtImpl);
    }

    @Deprecated
    @Nullable
    public CmmUser getUserById(long j) {
        long userByIdImpl = getUserByIdImpl(this.mNativeHandle, j);
        if (userByIdImpl == 0) {
            return null;
        }
        return new CmmUser(userByIdImpl);
    }

    @Nullable
    public CmmUser getLeftUserById(long j) {
        long leftUserByIdImpl = getLeftUserByIdImpl(this.mNativeHandle, j);
        if (leftUserByIdImpl == 0) {
            return null;
        }
        return new CmmUser(leftUserByIdImpl);
    }

    @Nullable
    public CmmUser getMyself() {
        long myselfImpl = getMyselfImpl(this.mNativeHandle);
        if (myselfImpl == 0) {
            return null;
        }
        return new CmmUser(myselfImpl);
    }

    @Nullable
    public CmmUser getPeerUser(boolean z, boolean z2) {
        long peerUserImpl = getPeerUserImpl(this.mNativeHandle, z, z2);
        if (peerUserImpl == 0) {
            return null;
        }
        return new CmmUser(peerUserImpl);
    }

    @Nullable
    public CmmUser getHostUser() {
        long hostUserImpl = getHostUserImpl(this.mNativeHandle);
        if (hostUserImpl == 0) {
            return null;
        }
        return new CmmUser(hostUserImpl);
    }

    public boolean hasPureCallInUser() {
        return hasPureCallInUser(false);
    }

    public boolean hasPureCallInUser(boolean z) {
        return hasPureCallInUserImpl(this.mNativeHandle, z);
    }

    public boolean hasNoAudioClientUser() {
        return hasNoAudioClientUser(false);
    }

    public boolean hasNoAudioClientUser(boolean z) {
        return hasNoAudioClientUserImpl(this.mNativeHandle, z);
    }

    public List<CmmUser> getPureCallInUsers() {
        return getPureCallInUsers(false);
    }

    public List<CmmUser> getPureCallInUsers(boolean z) {
        long[] pureCallInUsersImpl = getPureCallInUsersImpl(this.mNativeHandle, z);
        ArrayList arrayList = new ArrayList();
        for (long cmmUser : pureCallInUsersImpl) {
            arrayList.add(new CmmUser(cmmUser));
        }
        return arrayList;
    }

    public List<CmmUser> getNoAudioClientUsers() {
        return getNoAudioClientUsers(false);
    }

    public List<CmmUser> getNoAudioClientUsers(boolean z) {
        long[] noAudioClientUsersImpl = getNoAudioClientUsersImpl(this.mNativeHandle, z);
        ArrayList arrayList = new ArrayList();
        for (long cmmUser : noAudioClientUsersImpl) {
            arrayList.add(new CmmUser(cmmUser));
        }
        return arrayList;
    }

    public int getSilentModeUserCount() {
        return getSilentModeUserCountImpl(this.mNativeHandle);
    }

    public boolean hasCoHostUserInMeeting() {
        return hasCoHostUserInMeetingImpl(this.mNativeHandle);
    }
}
