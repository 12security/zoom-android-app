package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.AddBookmarkToCollection;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.AddCommentToBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.CopyBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.CreateBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.DeleteBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.DeleteBookmarkFromCollection;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.DeleteTrashedBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.GetBookmarkComments;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.GetBookmarkInfo;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.GetTrashedBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.RestoreTrashedBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.UpdateBookmark;
import com.box.androidsdk.content.requests.BoxRequestsBookmark.UpdateSharedBookmark;

public class BoxApiBookmark extends BoxApi {
    public BoxApiBookmark(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getBookmarksUrl() {
        return String.format("%s/web_links", new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getBookmarkInfoUrl(String str) {
        return String.format("%s/%s", new Object[]{getBookmarksUrl(), str});
    }

    /* access modifiers changed from: protected */
    public String getBookmarkCopyUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getBookmarkInfoUrl(str));
        sb.append("/copy");
        return String.format(sb.toString(), new Object[0]);
    }

    /* access modifiers changed from: protected */
    public String getTrashedBookmarkUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getBookmarkInfoUrl(str));
        sb.append("/trash");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getBookmarkCommentsUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getBookmarkInfoUrl(str));
        sb.append(BoxApiComment.COMMENTS_ENDPOINT);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getCommentUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUri());
        sb.append(BoxApiComment.COMMENTS_ENDPOINT);
        return sb.toString();
    }

    public GetBookmarkInfo getInfoRequest(String str) {
        return new GetBookmarkInfo(str, getBookmarkInfoUrl(str), this.mSession);
    }

    public CreateBookmark getCreateRequest(String str, String str2) {
        return new CreateBookmark(str, str2, getBookmarksUrl(), this.mSession);
    }

    public UpdateBookmark getUpdateRequest(String str) {
        return new UpdateBookmark(str, getBookmarkInfoUrl(str), this.mSession);
    }

    public CopyBookmark getCopyRequest(String str, String str2) {
        return new CopyBookmark(str, str2, getBookmarkCopyUrl(str), this.mSession);
    }

    public UpdateBookmark getRenameRequest(String str, String str2) {
        UpdateBookmark updateBookmark = new UpdateBookmark(str, getBookmarkInfoUrl(str), this.mSession);
        updateBookmark.setName(str2);
        return updateBookmark;
    }

    public UpdateBookmark getMoveRequest(String str, String str2) {
        UpdateBookmark updateBookmark = new UpdateBookmark(str, getBookmarkInfoUrl(str), this.mSession);
        updateBookmark.setParentId(str2);
        return updateBookmark;
    }

    public DeleteBookmark getDeleteRequest(String str) {
        return new DeleteBookmark(str, getBookmarkInfoUrl(str), this.mSession);
    }

    public UpdateSharedBookmark getCreateSharedLinkRequest(String str) {
        return (UpdateSharedBookmark) new UpdateSharedBookmark(str, getBookmarkInfoUrl(str), this.mSession).setAccess(null);
    }

    public UpdateBookmark getDisableSharedLinkRequest(String str) {
        return (UpdateBookmark) new UpdateBookmark(str, getBookmarkInfoUrl(str), this.mSession).setSharedLink(null);
    }

    public AddCommentToBookmark getAddCommentRequest(String str, String str2) {
        return new AddCommentToBookmark(str, str2, getCommentUrl(), this.mSession);
    }

    public GetTrashedBookmark getTrashedBookmarkRequest(String str) {
        return new GetTrashedBookmark(str, getTrashedBookmarkUrl(str), this.mSession);
    }

    public DeleteTrashedBookmark getDeleteTrashedBookmarkRequest(String str) {
        return new DeleteTrashedBookmark(str, getTrashedBookmarkUrl(str), this.mSession);
    }

    public RestoreTrashedBookmark getRestoreTrashedBookmarkRequest(String str) {
        return new RestoreTrashedBookmark(str, getBookmarkInfoUrl(str), this.mSession);
    }

    public GetBookmarkComments getCommentsRequest(String str) {
        return new GetBookmarkComments(str, getBookmarkCommentsUrl(str), this.mSession);
    }

    public AddBookmarkToCollection getAddToCollectionRequest(String str, String str2) {
        return new AddBookmarkToCollection(str, str2, getBookmarkInfoUrl(str), this.mSession);
    }

    public DeleteBookmarkFromCollection getDeleteFromCollectionRequest(String str) {
        return new DeleteBookmarkFromCollection(str, getBookmarkInfoUrl(str), this.mSession);
    }
}
