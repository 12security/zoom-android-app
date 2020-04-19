package com.zipow.videobox.view.p014mm.sticker;

import com.zipow.videobox.util.EmojiHelper.EmojiIndex;

/* renamed from: com.zipow.videobox.view.mm.sticker.StickerEvent */
public class StickerEvent {
    public static final int EVENT_TYPE_COMMON_EMOJI = 4;
    public static final int EVENT_TYPE_DELETE = 2;
    public static final int EVENT_TYPE_STICKER = 3;
    public static final int EVENT_TYPE_UNKNOW = 0;
    public static final int EVENT_TYPE_ZOOM_EMOJI = 1;
    private EmojiIndex mEmoji;
    private CommonEmoji mEmojiOne;
    private int mEventType;
    private int mStatus;
    private String mStickerId;
    private String mStickerPath;

    public StickerEvent(EmojiIndex emojiIndex) {
        this.mEventType = 0;
        this.mEventType = 1;
        this.mEmoji = emojiIndex;
    }

    public StickerEvent(String str) {
        this.mEventType = 0;
        this.mEventType = 3;
        this.mStickerId = str;
    }

    public StickerEvent(CommonEmoji commonEmoji) {
        this.mEventType = 0;
        this.mEventType = 4;
        this.mEmojiOne = commonEmoji;
    }

    public StickerEvent() {
        this.mEventType = 0;
        this.mEventType = 2;
    }

    public int getEventType() {
        return this.mEventType;
    }

    public String getStickerId() {
        return this.mStickerId;
    }

    public EmojiIndex getEmoji() {
        return this.mEmoji;
    }

    public String getStickerPath() {
        return this.mStickerPath;
    }

    public void setStickerPath(String str) {
        this.mStickerPath = str;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setStatus(int i) {
        this.mStatus = i;
    }

    public CommonEmoji getCommonEmoji() {
        return this.mEmojiOne;
    }
}
