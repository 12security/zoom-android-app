package com.zipow.videobox.ptapp;

public interface StickerMgrReturnCode {
    public static final int DUPLICATE_REQUEST = 4;
    public static final int FAIL = 0;
    public static final int IMAGE_SIZE_EXCEEDS_LIMIT = 5;
    public static final int STICKER_DUPLICATE = 2;
    public static final int STICKER_NOT_EXIST = 3;
    public static final int SUCCEED = 1;
}
