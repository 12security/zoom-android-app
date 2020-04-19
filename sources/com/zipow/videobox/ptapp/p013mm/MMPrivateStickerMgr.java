package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.StickerInfo;
import com.zipow.videobox.ptapp.IMProtos.StickerInfoList;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.MMPrivateStickerMgr */
public class MMPrivateStickerMgr {
    private long mNativeHandle = 0;

    private native int discardPrivateStickerImpl(long j, String str);

    @Nullable
    private native String downloadStickerImpl(long j, String str, String str2);

    @Nullable
    private native String downloadStickerPreviewImpl(long j, String str);

    @Nullable
    private native byte[] getStickersImpl(long j);

    private native int makePrivateStickerImpl(long j, String str);

    private native void registerUICallBackImpl(long j, long j2);

    private native int sendStickerImpl(long j, byte[] bArr, String str);

    private native int sendStickerReplyImpl(long j, byte[] bArr, String str, String str2, String str3);

    private native int uploadAndMakePrivateStickerImpl(long j, String str);

    public MMPrivateStickerMgr(long j) {
        this.mNativeHandle = j;
    }

    public void registerUICallBack(@Nullable PrivateStickerUICallBack privateStickerUICallBack) {
        long j = this.mNativeHandle;
        if (j != 0 && privateStickerUICallBack != null) {
            registerUICallBackImpl(j, privateStickerUICallBack.getNativeHandle());
        }
    }

    @Nullable
    public StickerInfoList getStickers() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] stickersImpl = getStickersImpl(j);
        if (stickersImpl == null) {
            return null;
        }
        try {
            return StickerInfoList.parseFrom(stickersImpl);
        } catch (Exception unused) {
            return null;
        }
    }

    public int uploadAndMakePrivateSticker(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        return uploadAndMakePrivateStickerImpl(this.mNativeHandle, str);
    }

    public int makePrivateSticker(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        return makePrivateStickerImpl(this.mNativeHandle, str);
    }

    public int discardPrivateSticker(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        return discardPrivateStickerImpl(this.mNativeHandle, str);
    }

    @Nullable
    public String downloadSticker(String str, String str2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return null;
        }
        return downloadStickerImpl(this.mNativeHandle, str, str2);
    }

    public int sendSticker(@Nullable StickerInfo stickerInfo, String str) {
        if (this.mNativeHandle == 0 || stickerInfo == null || StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        byte[] byteArray = stickerInfo.toByteArray();
        if (byteArray == null) {
            return 0;
        }
        return sendStickerImpl(this.mNativeHandle, byteArray, str);
    }

    public int sendStickerReply(@Nullable StickerInfo stickerInfo, String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || stickerInfo == null || StringUtil.isEmptyOrNull(str)) {
            return 0;
        }
        byte[] byteArray = stickerInfo.toByteArray();
        if (byteArray == null) {
            return 0;
        }
        return sendStickerReplyImpl(this.mNativeHandle, byteArray, str, str2, str3);
    }

    @Nullable
    public String downloadStickerPreview(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return downloadStickerPreviewImpl(this.mNativeHandle, str);
    }
}
