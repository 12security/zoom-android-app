package com.zipow.videobox.ptapp.p013mm;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMProtos.FileSearchFilter;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchContactFilter;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchFileFilter;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchMSGFilter;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchFilter;
import java.util.List;

/* renamed from: com.zipow.videobox.ptapp.mm.SearchMgr */
public class SearchMgr {
    private long mNativeHandle;

    private native boolean CancelSearchFileRequestImpl(long j, String str);

    private native boolean CancelSearchMessageRequestImpl(long j, String str);

    @Nullable
    private native String LocalSearchFileImpl(long j, byte[] bArr);

    @Nullable
    private native String LocalSearchMessageImpl(long j, byte[] bArr);

    @Nullable
    private native String QueryLocalMsgCtxImpl(long j, String str, long j2, int i);

    @Nullable
    private native String SearchFilesContentImpl(long j, byte[] bArr);

    @Nullable
    private native String SearchMessageContentImpl(long j, byte[] bArr);

    @Nullable
    private native String SearchMyNotesFileForTimedChatImpl(long j, String str);

    @Nullable
    private native String SearchMyNotesMessageForTimedChatImpl(long j, String str);

    private native int getSearchMessageSortTypeImpl(long j);

    private native String localSearchContactImpl(long j, byte[] bArr);

    private native void setMsgUIImpl(long j, long j2);

    private native void setSearchMessageSortTypeImpl(long j, int i);

    private native List<String> sortContactSearchResultImpl(long j, List<String> list);

    public SearchMgr(long j) {
        this.mNativeHandle = j;
    }

    public void setMsgUI(@Nullable IMCallbackUI iMCallbackUI) {
        long j = this.mNativeHandle;
        if (j != 0 && iMCallbackUI != null) {
            setMsgUIImpl(j, iMCallbackUI.getSearchMgrUICallBackHandleImpl());
        }
    }

    @Nullable
    public String searchMessageContent(@Nullable MessageContentSearchFilter messageContentSearchFilter) {
        if (this.mNativeHandle == 0 || messageContentSearchFilter == null) {
            return null;
        }
        return SearchMessageContentImpl(this.mNativeHandle, messageContentSearchFilter.toByteArray());
    }

    @Nullable
    public String searchMyNotesMessageForTimedChat(String str) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        return SearchMyNotesMessageForTimedChatImpl(this.mNativeHandle, str);
    }

    public boolean cancelSearchMessageRequest(String str) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return false;
        }
        return CancelSearchMessageRequestImpl(this.mNativeHandle, str);
    }

    @Nullable
    public String searchFilesContent(@Nullable FileSearchFilter fileSearchFilter) {
        if (this.mNativeHandle == 0 || fileSearchFilter == null) {
            return null;
        }
        return SearchFilesContentImpl(this.mNativeHandle, fileSearchFilter.toByteArray());
    }

    @Nullable
    public String searchMyNotesFileForTimedChat(String str) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return null;
        }
        return SearchMyNotesFileForTimedChatImpl(this.mNativeHandle, str);
    }

    public boolean cancelSearchFileRequest(String str) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return false;
        }
        return CancelSearchFileRequestImpl(this.mNativeHandle, str);
    }

    @Nullable
    public String LocalSearchMessage(@Nullable LocalSearchMSGFilter localSearchMSGFilter) {
        if (this.mNativeHandle == 0 || localSearchMSGFilter == null) {
            return null;
        }
        return LocalSearchMessageImpl(this.mNativeHandle, localSearchMSGFilter.toByteArray());
    }

    @Nullable
    public String LocalSearchFile(@Nullable LocalSearchFileFilter localSearchFileFilter) {
        if (this.mNativeHandle == 0 || localSearchFileFilter == null) {
            return null;
        }
        return LocalSearchFileImpl(this.mNativeHandle, localSearchFileFilter.toByteArray());
    }

    @Nullable
    public String queryLocalMsgCtx(String str, long j, int i) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return null;
        }
        return QueryLocalMsgCtxImpl(j2, str, j, i);
    }

    public void setSearchMessageSortType(int i) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setSearchMessageSortTypeImpl(j, i);
        }
    }

    public int getSearchMessageSortType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 2;
        }
        return getSearchMessageSortTypeImpl(j);
    }

    @Nullable
    public String localSearchContact(@Nullable LocalSearchContactFilter localSearchContactFilter) {
        if (this.mNativeHandle == 0 || localSearchContactFilter == null) {
            return null;
        }
        return localSearchContactImpl(this.mNativeHandle, localSearchContactFilter.toByteArray());
    }

    @Nullable
    public List<String> sortContactSearchResult(List<String> list) {
        if (this.mNativeHandle == 0 || list == null || list.isEmpty()) {
            return null;
        }
        return sortContactSearchResultImpl(this.mNativeHandle, list);
    }
}
