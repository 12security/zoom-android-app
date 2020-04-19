package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxComment;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxVoid;
import com.box.androidsdk.content.requests.BoxRequest.Methods;

public class BoxRequestsComment {

    public static class AddReplyComment extends BoxRequestCommentAdd<BoxComment, AddReplyComment> {
        public /* bridge */ /* synthetic */ String getItemId() {
            return super.getItemId();
        }

        public /* bridge */ /* synthetic */ String getItemType() {
            return super.getItemType();
        }

        public /* bridge */ /* synthetic */ String getMessage() {
            return super.getMessage();
        }

        public AddReplyComment(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxComment.class, str3, boxSession);
            setItemId(str);
            setItemType("comment");
            setMessage(str2);
        }
    }

    public static class DeleteComment extends BoxRequest<BoxVoid, DeleteComment> {
        private final String mId;

        public DeleteComment(String str, String str2, BoxSession boxSession) {
            super(BoxVoid.class, str2, boxSession);
            this.mRequestMethod = Methods.DELETE;
            this.mId = str;
        }

        public String getId() {
            return this.mId;
        }
    }

    public static class GetCommentInfo extends BoxRequestItem<BoxComment, GetCommentInfo> {
        public GetCommentInfo(String str, String str2, BoxSession boxSession) {
            super(BoxComment.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class UpdateComment extends BoxRequest<BoxComment, UpdateComment> {
        String mId;

        public UpdateComment(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxComment.class, str3, boxSession);
            this.mId = str;
            this.mRequestMethod = Methods.PUT;
            setMessage(str2);
        }

        public String getId() {
            return this.mId;
        }

        public String getMessage() {
            return (String) this.mBodyMap.get("message");
        }

        public UpdateComment setMessage(String str) {
            this.mBodyMap.put("message", str);
            return this;
        }
    }
}
