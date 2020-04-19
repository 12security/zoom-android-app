package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.AtInfoList;
import com.zipow.videobox.ptapp.IMProtos.EmojiList;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo;
import com.zipow.videobox.ptapp.IMProtos.FontStyte;
import com.zipow.videobox.util.EmojiHelper;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomMessage */
public class ZoomMessage {
    private static final String TAG = "ZoomMessage";
    private long mNativeHandle = 0;

    /* renamed from: com.zipow.videobox.ptapp.mm.ZoomMessage$FileInfo */
    public static class FileInfo {
        public String name;
        public long size;
    }

    /* renamed from: com.zipow.videobox.ptapp.mm.ZoomMessage$FileTransferInfo */
    public static class FileTransferInfo {
        public long bitsPerSecond;
        public int percentage;
        public int prevError;
        public int state;
        public long transferredSize;
    }

    private native boolean IsDeletedThreadImpl(long j);

    private native boolean IsFollowedThreadImpl(long j);

    private native boolean IsOfflineMessageImpl(long j);

    private native int commentThreadCloudStoreStateImpl(long j);

    private native boolean containCommentFeatureImpl(long j);

    private native boolean couldReallySupportImpl(long j);

    private native int getAudioLengthImpl(long j);

    @Nullable
    private native String getBodyImpl(long j);

    private native String getDeleteThreadOperatorImpl(long j);

    private native long getEditActionMilliSecTimeImpl(long j);

    @Nullable
    private native byte[] getEmojiListImpl(long j);

    private native boolean getFileInfoImpl(long j, Object[] objArr);

    private native boolean getFileTransferInfoImpl(long j, Object[] objArr);

    private native long getFileWithMessageIDImpl(long j);

    @Nullable
    private native String getGiphyIDImpl(long j);

    private native String getGroupIDImpl(long j);

    private native long getLastEmojiTimeImpl(long j);

    @Nullable
    private native String getLocalFilePathImpl(long j);

    private native long getLocalLastCommentTimeImpl(long j);

    @Nullable
    private native byte[] getMessageAtInfoListImpl(long j);

    @Nullable
    private native List<String> getMessageAtListImpl(long j);

    private native int getMessageFilterResultImpl(long j);

    private native String getMessageIDImpl(long j);

    private native int getMessageStateImpl(long j);

    private native int getMessageTypeImpl(long j);

    @Nullable
    private native String getMessageXMPPGuidImpl(long j);

    @Nullable
    private native String getPicturePreviewPathImpl(long j);

    private native String getReceiverIDImpl(long j);

    private native String getSenderIDImpl(long j);

    @Nullable
    private native String getSenderNameImpl(long j);

    private native long getServerSideTimeImpl(long j);

    private native long getStampImpl(long j);

    private native byte[] getStyleOffsetImpl(long j);

    private native String getThreadIDImpl(long j);

    private native long getThreadTimeImpl(long j);

    private native long getTotalCommentsCountImpl(long j);

    private native int getVideoLengthImpl(long j);

    private native boolean isCommentImpl(long j);

    private native boolean isE2EMessageImpl(long j);

    private native boolean isFileDownloadedImpl(long j);

    private native boolean isHistorySyncMessageImpl(long j);

    private native boolean isMessageAtEveryoneImpl(long j);

    private native boolean isMessageAtMeImpl(long j);

    private native boolean isNotExistThreadImpl(long j);

    private native boolean isPlayedImpl(long j);

    private native boolean isStickerMessageImpl(long j);

    private native boolean isThreadImpl(long j);

    private native boolean isUnreadImpl(long j);

    private native boolean needTriggerUpdateImpl(long j);

    private native void setAsPlayedImpl(long j, boolean z);

    public ZoomMessage(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getMessageID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMessageIDImpl(j);
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }

    public int getMessageType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getMessageTypeImpl(j);
    }

    public int getMessageState() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMessageStateImpl(j);
    }

    public boolean isUnread() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isUnreadImpl(j);
    }

    public boolean isPlayed() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPlayedImpl(j);
    }

    public void setAsPlayed(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setAsPlayedImpl(j, z);
        }
    }

    @Nullable
    public String getReceiverID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getReceiverIDImpl(j);
    }

    @Nullable
    public String getSenderID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSenderIDImpl(j);
    }

    @Nullable
    public String getGroupID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getGroupIDImpl(j);
    }

    public long getStamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getStampImpl(j);
    }

    public long getServerSideTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getServerSideTimeImpl(j);
    }

    @Nullable
    public CharSequence getBodyWithShortcut() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        String bodyImpl = getBodyImpl(j);
        if (bodyImpl != null) {
            CharSequence compatNewLineForAllOS = StringUtil.compatNewLineForAllOS(bodyImpl);
            if (compatNewLineForAllOS != null) {
                bodyImpl = compatNewLineForAllOS.toString();
            }
        }
        return EmojiHelper.getInstance().tranToShortcutText(bodyImpl, getEmojiList());
    }

    @Nullable
    public CharSequence getBody() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        String bodyImpl = getBodyImpl(j);
        if (bodyImpl != null) {
            CharSequence compatNewLineForAllOS = StringUtil.compatNewLineForAllOS(bodyImpl);
            if (compatNewLineForAllOS != null) {
                bodyImpl = compatNewLineForAllOS.toString();
            }
        }
        return bodyImpl;
    }

    @Nullable
    public List<String> getMsgAtList() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMessageAtListImpl(j);
    }

    @Nullable
    public AtInfoList getMsgAtInfoList() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] messageAtInfoListImpl = getMessageAtInfoListImpl(j);
        if (messageAtInfoListImpl == null || messageAtInfoListImpl.length <= 0) {
            return null;
        }
        try {
            return AtInfoList.parseFrom(messageAtInfoListImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public FontStyte getFontStyte() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] styleOffsetImpl = getStyleOffsetImpl(j);
        if (styleOffsetImpl == null || styleOffsetImpl.length <= 0) {
            return null;
        }
        try {
            return FontStyte.parseFrom(styleOffsetImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public String getLocalFilePath() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLocalFilePathImpl(j);
    }

    @Nullable
    public String getPicturePreviewPath() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPicturePreviewPathImpl(j);
    }

    public boolean isFileDownloaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isFileDownloadedImpl(j);
    }

    public int getAudioLength() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getAudioLengthImpl(j);
    }

    public int getVideoLength() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getVideoLengthImpl(j);
    }

    public boolean isE2EMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isE2EMessageImpl(j);
    }

    public boolean isMessageAtEveryone() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMessageAtEveryoneImpl(j);
    }

    public boolean isHistorySyncMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isHistorySyncMessageImpl(j);
    }

    @Nullable
    public FileInfo getFileInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        Object[] objArr = new Object[2];
        if (!getFileInfoImpl(j, objArr)) {
            return null;
        }
        FileInfo fileInfo = new FileInfo();
        if (objArr[0] instanceof Number) {
            fileInfo.size = ((Number) objArr[0]).longValue();
        }
        if (objArr[1] instanceof String) {
            fileInfo.name = (String) objArr[1];
        }
        return fileInfo;
    }

    @Nullable
    public FileTransferInfo getFileTransferInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        Object[] objArr = new Object[5];
        if (!getFileTransferInfoImpl(j, objArr)) {
            return null;
        }
        FileTransferInfo fileTransferInfo = new FileTransferInfo();
        if (objArr[0] instanceof Number) {
            fileTransferInfo.state = ((Number) objArr[0]).intValue();
        }
        if (objArr[1] instanceof Number) {
            fileTransferInfo.percentage = ((Number) objArr[1]).intValue();
        }
        if (objArr[2] instanceof Number) {
            fileTransferInfo.bitsPerSecond = ((Number) objArr[2]).longValue();
        }
        if (objArr[3] instanceof Number) {
            fileTransferInfo.transferredSize = ((Number) objArr[3]).longValue();
        }
        if (objArr[4] instanceof Number) {
            fileTransferInfo.prevError = ((Number) objArr[4]).intValue();
        }
        return fileTransferInfo;
    }

    @Nullable
    public EmojiList getEmojiList() {
        long j = this.mNativeHandle;
        EmojiList emojiList = null;
        if (j == 0) {
            return null;
        }
        byte[] emojiListImpl = getEmojiListImpl(j);
        if (emojiListImpl != null) {
            try {
                emojiList = EmojiList.parseFrom(emojiListImpl);
            } catch (InvalidProtocolBufferException unused) {
                return null;
            }
        }
        return emojiList;
    }

    @Nullable
    public String getMessageXMPPGuid() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMessageXMPPGuidImpl(j);
    }

    @Nullable
    public String getSenderName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSenderNameImpl(j);
    }

    public boolean isStickerMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isStickerMessageImpl(j);
    }

    public boolean couldReallySupport() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return couldReallySupportImpl(j);
    }

    public long getEditActionMilliSecTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getEditActionMilliSecTimeImpl(j);
    }

    @Nullable
    public String getGiphyID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getGiphyIDImpl(j);
    }

    public boolean isMessageAtMe() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMessageAtMeImpl(j);
    }

    public int getMessageFilterResult() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 1;
        }
        return getMessageFilterResultImpl(j);
    }

    public boolean isOfflineMessage() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return IsOfflineMessageImpl(j);
    }

    public boolean needTriggerUpdate() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return needTriggerUpdateImpl(j);
    }

    @Nullable
    public ZoomFile getFileWithMessageID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long fileWithMessageIDImpl = getFileWithMessageIDImpl(j);
        if (fileWithMessageIDImpl == 0) {
            return null;
        }
        return new ZoomFile(fileWithMessageIDImpl);
    }

    @Nullable
    public FileIntegrationInfo getFileIntegrationShareInfo() {
        ZoomFile fileWithMessageID = getFileWithMessageID();
        if (fileWithMessageID == null || fileWithMessageID.getFileIntegrationShareInfo() == null) {
            return null;
        }
        return FileIntegrationInfo.newBuilder(fileWithMessageID.getFileIntegrationShareInfo()).build();
    }

    public String getThreadID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getThreadIDImpl(j);
    }

    public long getTotalCommentsCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTotalCommentsCountImpl(j);
    }

    public boolean isThread() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isThreadImpl(j);
    }

    public boolean isComment() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isCommentImpl(j);
    }

    public long getLastEmojiTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLastEmojiTimeImpl(j);
    }

    public long getLastLocalCommentTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLocalLastCommentTimeImpl(j);
    }

    public boolean IsFollowedThread() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return IsFollowedThreadImpl(j);
    }

    public boolean IsDeletedThread() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return IsDeletedThreadImpl(j);
    }

    public String getDeleteThreadOperator() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getDeleteThreadOperatorImpl(j);
    }

    public boolean containCommentFeature() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return containCommentFeatureImpl(j);
    }

    public long getThreadTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getThreadTimeImpl(j);
    }

    public boolean isNotExistThread() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isNotExistThreadImpl(j);
    }

    public int commentThreadCloudStoreState() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return commentThreadCloudStoreStateImpl(j);
    }
}
