package com.zipow.videobox.ptapp;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.CommentDataResult;
import com.zipow.videobox.ptapp.IMProtos.DBExistResult;
import com.zipow.videobox.ptapp.IMProtos.EmojiCountMap;
import com.zipow.videobox.ptapp.IMProtos.EmojiDetailInfo;
import com.zipow.videobox.ptapp.IMProtos.ThreadDataResult;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class ThreadDataProvider {
    private static final String TAG = "ThreadDataProvider";
    private long mNativeHandle;

    private native void RegisterUICallBackImpl(long j, long j2);

    private native String addEmojiForMessageImpl(long j, String str, String str2, String str3);

    private native boolean discardFollowThreadImpl(long j, String str, String str2);

    private native boolean followThreadImpl(long j, String str, String str2);

    private native byte[] getCommentDataImpl(long j, String str, int i, String str2, long j2, String str3, long j3, int i2, boolean z);

    private native String getEmojiStrKeyImpl(long j, String str);

    private native byte[] getMessageEmojiCountInfoImpl(long j, boolean z, String str, String str2);

    private native byte[] getMessageEmojiDetailInfoImpl(long j, boolean z, String str, String str2, String str3);

    private native long getMessagePtrByStampImpl(long j, String str, long j2);

    private native long getMessagePtrImpl(long j, String str, String str2);

    private native long getServerVisibleTimeByPtrImpl(long j, long j2);

    private native long getServerVisibleTimeImpl(long j, String str, String str2);

    private native byte[] getThreadDataImpl(long j, String str, int i, String str2, long j2, long j3, int i2, boolean z);

    private native String getThreadReplyDraftImpl(long j, String str, String str2);

    private native int getThreadSortTypeImpl(long j);

    private native int havePendingThreadSortTypeImpl(long j);

    private native boolean isCommentDirtyImpl(long j, String str, String str2, String str3);

    private native boolean isMessageEmojiCountInfoDirtyImpl(long j, String str, String str2);

    private native boolean isMessageEmojiDetailInfoDirtyImpl(long j, String str, String str2);

    private native byte[] isMessageExistInDBImpl(long j, String str, String str2);

    private native boolean isThreadCommentCountSyncedImpl(long j, String str, long j2);

    private native boolean isThreadCommentInfoAccurateImpl(long j, long j2);

    private native boolean isThreadDirtyImpl(long j, String str, String str2);

    private native boolean isThreadFollowedImpl(long j, String str, String str2);

    private native boolean moreHistoricCommentsImpl(long j, String str, String str2, String str3);

    private native boolean moreHistoricThreadsImpl(long j, String str, String str2);

    private native boolean moreRecentCommentsImpl(long j, String str, String str2, String str3);

    private native boolean moreRecentThreadsImpl(long j, String str, String str2);

    private native boolean needRecallDeletedThreadImpl(long j, long j2);

    private native String removeEmojiForMessageImpl(long j, String str, String str2, String str3);

    private native boolean setThreadReplyDraftImpl(long j, String str, String str2, String str3);

    private native boolean setThreadSortTypeImpl(long j, int i);

    private native String syncMessageEmojiCountInfoImpl(long j, String str, List<String> list);

    private native String syncSingleThreadContextImpl(long j, String str, String str2, long j2);

    private native String syncThreadCommentCountImpl(long j, String str, List<Long> list);

    private native boolean threadHasCommentsImpl(long j, long j2);

    private native int threadHasCommentsOddsImpl(long j, long j2);

    private native boolean threadInCacheImpl(long j, String str, String str2);

    public ThreadDataProvider(long j) {
        this.mNativeHandle = j;
    }

    public void setMsgUI(ThreadDataUI threadDataUI) {
        long j = this.mNativeHandle;
        if (j != 0 && threadDataUI != null) {
            RegisterUICallBackImpl(j, threadDataUI.getNativeHandle());
        }
    }

    @Nullable
    public ZoomMessage getMessagePtr(String str, String str2) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        long messagePtrImpl = getMessagePtrImpl(this.mNativeHandle, str, str2);
        if (messagePtrImpl == 0) {
            return null;
        }
        return new ZoomMessage(messagePtrImpl);
    }

    @Nullable
    public ZoomMessage getMessagePtr(String str, long j) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || j == 0) {
            return null;
        }
        long messagePtrByStampImpl = getMessagePtrByStampImpl(this.mNativeHandle, str, j);
        if (messagePtrByStampImpl == 0) {
            return null;
        }
        return new ZoomMessage(messagePtrByStampImpl);
    }

    public long getServerVisibleTime(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return getServerVisibleTimeImpl(this.mNativeHandle, str, str2);
        }
        return 0;
    }

    public long getServerVisibleTime(ZoomMessage zoomMessage) {
        if (zoomMessage == null) {
            return 0;
        }
        return getServerVisibleTimeByPtrImpl(this.mNativeHandle, zoomMessage.getNativeHandle());
    }

    public boolean isThreadFollowed(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return isThreadFollowedImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean followThread(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return followThreadImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean discardFollowThread(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return discardFollowThreadImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    @Nullable
    public ThreadDataResult getThreadData(String str, int i, String str2, int i2) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        return getThreadData(str, i, str2, 0, i2);
    }

    @Nullable
    public ThreadDataResult getThreadData(String str, int i, String str2, long j, int i2) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || i <= 0) {
            return null;
        }
        try {
            return ThreadDataResult.parseFrom(getThreadDataImpl(this.mNativeHandle, str, i, str2, j, 0, i2, false));
        } catch (Exception unused) {
            return null;
        }
    }

    @Nullable
    public ThreadDataResult getThreadData(String str, int i, long j, int i2) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || i <= 0) {
            return null;
        }
        try {
            return ThreadDataResult.parseFrom(getThreadDataImpl(this.mNativeHandle, str, i, "", 0, j, i2, false));
        } catch (Exception unused) {
            return null;
        }
    }

    @Nullable
    public CommentDataResult getCommentData(String str, int i, String str2, long j, String str3, int i2, boolean z) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        return getCommentData(str, i, str2, j, str3, 0, i2, z);
    }

    @Nullable
    public CommentDataResult getCommentData(String str, int i, String str2, long j, long j2, int i2, boolean z) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        return getCommentData(str, i, str2, j, "", j2, i2, z);
    }

    public CommentDataResult getCommentData(String str, int i, String str2, long j, String str3, long j2, int i2, boolean z) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || i <= 0 || (TextUtils.isEmpty(str2) && j == 0)) {
            return null;
        }
        try {
            return CommentDataResult.parseFrom(getCommentDataImpl(this.mNativeHandle, str, i, str2 == null ? "" : str2, j, str3, j2, i2, z));
        } catch (Exception unused) {
            return null;
        }
    }

    public String syncSingleThreadContext(String str, String str2, long j) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        return syncSingleThreadContextImpl(this.mNativeHandle, str, str2, j);
    }

    public boolean threadInCache(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return threadInCacheImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean isThreadDirty(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return isThreadDirtyImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean moreHistoricThreads(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return moreHistoricThreadsImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean moreRecentThreads(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return moreRecentThreadsImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean isCommentDirty(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return false;
        }
        return isCommentDirtyImpl(this.mNativeHandle, str, str2, str3);
    }

    public boolean moreHistoricComments(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return false;
        }
        return moreHistoricCommentsImpl(this.mNativeHandle, str, str2, str3);
    }

    public boolean moreRecentComments(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return false;
        }
        return moreRecentCommentsImpl(this.mNativeHandle, str, str2, str3);
    }

    public boolean isMessageEmojiCountInfoDirty(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return isMessageEmojiCountInfoDirtyImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public boolean isMessageEmojiDetailInfoDirty(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return isMessageEmojiDetailInfoDirtyImpl(this.mNativeHandle, str, str2);
        }
        return false;
    }

    public EmojiCountMap getMessageEmojiCountInfo(boolean z, String str, String str2) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            return EmojiCountMap.parseFrom(getMessageEmojiCountInfoImpl(this.mNativeHandle, z, str, str2));
        } catch (Exception unused) {
            return null;
        }
    }

    public EmojiDetailInfo getMessageEmojiDetailInfo(boolean z, String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return null;
        }
        try {
            return EmojiDetailInfo.parseFrom(getMessageEmojiDetailInfoImpl(this.mNativeHandle, z, str, str2, str3));
        } catch (Exception unused) {
            return null;
        }
    }

    public String addEmojiForMessage(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return null;
        }
        return addEmojiForMessageImpl(this.mNativeHandle, str, str2, str3);
    }

    public String removeEmojiForMessage(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return null;
        }
        return removeEmojiForMessageImpl(this.mNativeHandle, str, str2, str3);
    }

    public String syncMessageEmojiCountInfo(String str, List<String> list) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !CollectionsUtil.isCollectionEmpty(list)) {
            return syncMessageEmojiCountInfoImpl(this.mNativeHandle, str, list);
        }
        return null;
    }

    public String getEmojiStrKey(String str) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return getEmojiStrKeyImpl(this.mNativeHandle, str);
    }

    public int threadHasCommentsOdds(ZoomMessage zoomMessage) {
        if (this.mNativeHandle == 0 || zoomMessage == null || zoomMessage.getNativeHandle() == 0) {
            return 2;
        }
        return threadHasCommentsOddsImpl(this.mNativeHandle, zoomMessage.getNativeHandle());
    }

    public DBExistResult isMessageExistInDB(String str, String str2) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        byte[] isMessageExistInDBImpl = isMessageExistInDBImpl(this.mNativeHandle, str, str2);
        if (isMessageExistInDBImpl == null) {
            return null;
        }
        try {
            return DBExistResult.parseFrom(isMessageExistInDBImpl);
        } catch (Exception unused) {
            return null;
        }
    }

    public int getThreadSortType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getThreadSortTypeImpl(j);
    }

    public boolean setThreadSortType(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setThreadSortTypeImpl(j, i);
    }

    public int havePendingThreadSortType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return havePendingThreadSortTypeImpl(j);
    }

    @Nullable
    public String getThreadReplyDraft(String str, String str2) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            return getThreadReplyDraftImpl(this.mNativeHandle, str, str2);
        }
        return null;
    }

    public boolean setThreadReplyDraft(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        long j = this.mNativeHandle;
        if (str3 == null) {
            str3 = "";
        }
        return setThreadReplyDraftImpl(j, str, str2, str3);
    }

    public boolean isThreadCommentCountSynced(String str, long j) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return false;
        }
        return isThreadCommentCountSyncedImpl(this.mNativeHandle, str, j);
    }

    public String syncThreadCommentCount(String str, List<Long> list) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str) || list == null) {
            return null;
        }
        return syncThreadCommentCountImpl(this.mNativeHandle, str, list);
    }

    public boolean isThreadCommentInfoAccurate(ZoomMessage zoomMessage) {
        long j = this.mNativeHandle;
        if (j == 0 || zoomMessage == null) {
            return false;
        }
        return isThreadCommentInfoAccurateImpl(j, zoomMessage.getNativeHandle());
    }

    public boolean needRecallDeletedThread(ZoomMessage zoomMessage) {
        long j = this.mNativeHandle;
        if (j == 0 || zoomMessage == null) {
            return false;
        }
        return needRecallDeletedThreadImpl(j, zoomMessage.getNativeHandle());
    }
}
