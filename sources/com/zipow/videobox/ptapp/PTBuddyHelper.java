package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;

public class PTBuddyHelper {
    private static final String TAG = "PTBuddyHelper";
    private long mNativeHandle = 0;

    @Nullable
    private native String[] filterBuddyWithInputImpl(long j, String str);

    private native int getBuddyItemCountImpl(long j);

    @Nullable
    private native String getBuddyItemJidImpl(long j, int i);

    @Nullable
    private native byte[] getBuddyItemProtoData(long j, int i);

    @Nullable
    private native byte[] getBuddyItemProtoDataByJid(long j, String str);

    public PTBuddyHelper(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public BuddyItem getBuddyItem(int i) {
        BuddyItem buddyItem = null;
        if (i < 0 || i >= getBuddyItemCount()) {
            return null;
        }
        byte[] buddyItemProtoData = getBuddyItemProtoData(this.mNativeHandle, i);
        if (buddyItemProtoData == null || buddyItemProtoData.length == 0) {
            return null;
        }
        try {
            buddyItem = BuddyItem.parseFrom(buddyItemProtoData);
        } catch (InvalidProtocolBufferException unused) {
        }
        return buddyItem;
    }

    @Nullable
    public String getBuddyItemJid(int i) {
        return getBuddyItemJidImpl(this.mNativeHandle, i);
    }

    public int getBuddyItemCount() {
        return getBuddyItemCountImpl(this.mNativeHandle);
    }

    @Nullable
    public BuddyItem getBuddyItemByJid(@Nullable String str) {
        BuddyItem buddyItem = null;
        if (str == null) {
            return null;
        }
        byte[] buddyItemProtoDataByJid = getBuddyItemProtoDataByJid(this.mNativeHandle, str);
        if (buddyItemProtoDataByJid == null || buddyItemProtoDataByJid.length == 0) {
            return null;
        }
        try {
            buddyItem = BuddyItem.parseFrom(buddyItemProtoDataByJid);
        } catch (InvalidProtocolBufferException unused) {
        }
        return buddyItem;
    }

    @Nullable
    public String[] filterBuddyWithInput(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        return filterBuddyWithInputImpl(this.mNativeHandle, str);
    }

    @Nullable
    public byte[] getBuddyItemData(int i) {
        return getBuddyItemProtoData(this.mNativeHandle, i);
    }

    @Nullable
    public byte[] getBuddyItemDataByJid(String str) {
        return getBuddyItemProtoDataByJid(this.mNativeHandle, str);
    }
}
