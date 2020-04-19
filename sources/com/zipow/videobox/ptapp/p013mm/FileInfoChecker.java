package com.zipow.videobox.ptapp.p013mm;

/* renamed from: com.zipow.videobox.ptapp.mm.FileInfoChecker */
public class FileInfoChecker {
    private long mNativeHandle = 0;

    private native boolean isGifFileImpl(long j, String str);

    private native boolean isLegalGifImpl(long j, String str);

    public FileInfoChecker(long j) {
        this.mNativeHandle = j;
    }

    public boolean isGifFile(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isGifFileImpl(j, str);
    }

    public boolean isLegalGif(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLegalGifImpl(j, str);
    }
}
