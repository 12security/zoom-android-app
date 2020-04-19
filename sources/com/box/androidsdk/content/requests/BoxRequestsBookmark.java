package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxBookmark;
import com.box.androidsdk.content.models.BoxComment;
import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxListComments;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import java.util.HashMap;

public class BoxRequestsBookmark {

    public static class AddBookmarkToCollection extends BoxRequestCollectionUpdate<BoxBookmark, AddBookmarkToCollection> {
        public AddBookmarkToCollection(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxBookmark.class, str, str3, boxSession);
            setCollectionId(str2);
        }

        public AddBookmarkToCollection setCollectionId(String str) {
            return (AddBookmarkToCollection) super.setCollectionId(str);
        }
    }

    public static class AddCommentToBookmark extends BoxRequestCommentAdd<BoxComment, AddCommentToBookmark> {
        public /* bridge */ /* synthetic */ String getItemId() {
            return super.getItemId();
        }

        public /* bridge */ /* synthetic */ String getItemType() {
            return super.getItemType();
        }

        public /* bridge */ /* synthetic */ String getMessage() {
            return super.getMessage();
        }

        public AddCommentToBookmark(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxComment.class, str3, boxSession);
            setItemId(str);
            setItemType(BoxBookmark.TYPE);
            setMessage(str2);
        }
    }

    public static class CopyBookmark extends BoxRequestItemCopy<BoxBookmark, CopyBookmark> {
        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getParentId() {
            return super.getParentId();
        }

        public CopyBookmark(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxBookmark.class, str, str2, str3, boxSession);
        }
    }

    public static class CreateBookmark extends BoxRequestItem<BoxBookmark, CreateBookmark> {
        public CreateBookmark(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxBookmark.class, null, str3, boxSession);
            this.mRequestMethod = Methods.POST;
            setParentId(str);
            setUrl(str2);
        }

        public String getParentId() {
            if (this.mBodyMap.containsKey("parent")) {
                return (String) this.mBodyMap.get("id");
            }
            return null;
        }

        public CreateBookmark setParentId(String str) {
            HashMap hashMap = new HashMap();
            hashMap.put("id", str);
            this.mBodyMap.put("parent", new BoxFolder(hashMap));
            return this;
        }

        public String getUrl() {
            return (String) this.mBodyMap.get("url");
        }

        public CreateBookmark setUrl(String str) {
            this.mBodyMap.put("url", str);
            return this;
        }

        public String getName() {
            return (String) this.mBodyMap.get("name");
        }

        public CreateBookmark setName(String str) {
            this.mBodyMap.put("name", str);
            return this;
        }

        public String getDescription() {
            return (String) this.mBodyMap.get(BoxItem.FIELD_DESCRIPTION);
        }

        public CreateBookmark setDescription(String str) {
            this.mBodyMap.put(BoxItem.FIELD_DESCRIPTION, str);
            return this;
        }
    }

    public static class DeleteBookmark extends BoxRequestItemDelete<DeleteBookmark> {
        public /* bridge */ /* synthetic */ String getId() {
            return super.getId();
        }

        public /* bridge */ /* synthetic */ String getIfMatchEtag() {
            return super.getIfMatchEtag();
        }

        public DeleteBookmark(String str, String str2, BoxSession boxSession) {
            super(str, str2, boxSession);
        }
    }

    public static class DeleteBookmarkFromCollection extends BoxRequestCollectionUpdate<BoxBookmark, DeleteBookmarkFromCollection> {
        public DeleteBookmarkFromCollection(String str, String str2, BoxSession boxSession) {
            super(BoxBookmark.class, str, str2, boxSession);
            setCollectionId(null);
        }
    }

    public static class DeleteTrashedBookmark extends BoxRequestItemDelete<DeleteTrashedBookmark> {
        public /* bridge */ /* synthetic */ String getId() {
            return super.getId();
        }

        public /* bridge */ /* synthetic */ String getIfMatchEtag() {
            return super.getIfMatchEtag();
        }

        public DeleteTrashedBookmark(String str, String str2, BoxSession boxSession) {
            super(str, str2, boxSession);
        }
    }

    public static class GetBookmarkComments extends BoxRequestItem<BoxListComments, GetBookmarkComments> {
        public GetBookmarkComments(String str, String str2, BoxSession boxSession) {
            super(BoxListComments.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class GetBookmarkInfo extends BoxRequestItem<BoxBookmark, GetBookmarkInfo> {
        public GetBookmarkInfo(String str, String str2, BoxSession boxSession) {
            super(BoxBookmark.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }

        public GetBookmarkInfo setIfNoneMatchEtag(String str) {
            return (GetBookmarkInfo) super.setIfNoneMatchEtag(str);
        }

        public String getIfNoneMatchEtag() {
            return super.getIfNoneMatchEtag();
        }
    }

    public static class GetTrashedBookmark extends BoxRequestItem<BoxBookmark, GetTrashedBookmark> {
        public GetTrashedBookmark(String str, String str2, BoxSession boxSession) {
            super(BoxBookmark.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }

        public GetTrashedBookmark setIfNoneMatchEtag(String str) {
            return (GetTrashedBookmark) super.setIfNoneMatchEtag(str);
        }

        public String getIfNoneMatchEtag() {
            return super.getIfNoneMatchEtag();
        }
    }

    public static class RestoreTrashedBookmark extends BoxRequestItemRestoreTrashed<BoxBookmark, RestoreTrashedBookmark> {
        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getParentId() {
            return super.getParentId();
        }

        public RestoreTrashedBookmark(String str, String str2, BoxSession boxSession) {
            super(BoxBookmark.class, str, str2, boxSession);
        }
    }

    public static class UpdateBookmark extends BoxRequestItemUpdate<BoxBookmark, UpdateBookmark> {
        public UpdateBookmark(String str, String str2, BoxSession boxSession) {
            super(BoxBookmark.class, str, str2, boxSession);
        }

        public String getUrl() {
            return (String) this.mBodyMap.get("url");
        }

        public UpdateBookmark setUrl(String str) {
            this.mBodyMap.put("url", str);
            return this;
        }

        public UpdateSharedBookmark updateSharedLink() {
            return new UpdateSharedBookmark(this);
        }
    }

    public static class UpdateSharedBookmark extends BoxRequestUpdateSharedItem<BoxBookmark, UpdateSharedBookmark> {
        public UpdateSharedBookmark(String str, String str2, BoxSession boxSession) {
            super(BoxBookmark.class, str, str2, boxSession);
        }

        public UpdateSharedBookmark(UpdateBookmark updateBookmark) {
            super(updateBookmark);
        }
    }
}
