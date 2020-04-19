package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsComment.AddReplyComment;
import com.box.androidsdk.content.requests.BoxRequestsComment.DeleteComment;
import com.box.androidsdk.content.requests.BoxRequestsComment.GetCommentInfo;
import com.box.androidsdk.content.requests.BoxRequestsComment.UpdateComment;

public class BoxApiComment extends BoxApi {
    public static final String COMMENTS_ENDPOINT = "/comments";

    public BoxApiComment(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getCommentsUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUri());
        sb.append(COMMENTS_ENDPOINT);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getCommentInfoUrl(String str) {
        return String.format("%s/%s", new Object[]{getCommentsUrl(), str});
    }

    public GetCommentInfo getInfoRequest(String str) {
        return new GetCommentInfo(str, getCommentInfoUrl(str), this.mSession);
    }

    public AddReplyComment getAddCommentReplyRequest(String str, String str2) {
        return new AddReplyComment(str, str2, getCommentsUrl(), this.mSession);
    }

    public UpdateComment getUpdateRequest(String str, String str2) {
        return new UpdateComment(str, str2, getCommentInfoUrl(str), this.mSession);
    }

    public DeleteComment getDeleteRequest(String str) {
        return new DeleteComment(str, getCommentInfoUrl(str), this.mSession);
    }
}
