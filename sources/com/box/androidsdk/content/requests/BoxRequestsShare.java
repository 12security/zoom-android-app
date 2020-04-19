package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.models.BoxBookmark;
import com.box.androidsdk.content.models.BoxCollaboration;
import com.box.androidsdk.content.models.BoxCollaboration.Role;
import com.box.androidsdk.content.models.BoxCollaboration.Status;
import com.box.androidsdk.content.models.BoxCollaborator;
import com.box.androidsdk.content.models.BoxEntity;
import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxGroup;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxListCollaborations;
import com.box.androidsdk.content.models.BoxMapJsonObject;
import com.box.androidsdk.content.models.BoxObject;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxSharedLinkSession;
import com.box.androidsdk.content.models.BoxUser;
import com.box.androidsdk.content.models.BoxVoid;
import com.box.androidsdk.content.requests.BoxRequest.BoxRequestHandler;
import com.box.androidsdk.content.requests.BoxRequest.ContentTypes;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.box.androidsdk.content.utils.SdkUtils;
import java.util.HashMap;

public class BoxRequestsShare {

    public static class AddCollaboration extends BoxRequest<BoxCollaboration, AddCollaboration> {
        public static final String ERROR_CODE_USER_ALREADY_COLLABORATOR = "user_already_collaborator";
        private final String mFolderId;

        public AddCollaboration(String str, String str2, Role role, String str3, BoxSession boxSession) {
            super(BoxCollaboration.class, str, boxSession);
            this.mRequestMethod = Methods.POST;
            this.mFolderId = str2;
            setFolder(str2);
            setAccessibleBy(null, str3, "user");
            this.mBodyMap.put("role", role.toString());
        }

        public AddCollaboration(String str, String str2, Role role, BoxCollaborator boxCollaborator, BoxSession boxSession) {
            super(BoxCollaboration.class, str, boxSession);
            this.mRequestMethod = Methods.POST;
            this.mFolderId = str2;
            setFolder(str2);
            setAccessibleBy(boxCollaborator.getId(), null, boxCollaborator.getType());
            this.mBodyMap.put("role", role.toString());
        }

        public AddCollaboration notifyCollaborators(boolean z) {
            this.mQueryMap.put("notify", Boolean.toString(z));
            return this;
        }

        public String getFolderId() {
            return this.mFolderId;
        }

        private void setFolder(String str) {
            HashMap hashMap = new HashMap();
            hashMap.put("id", str);
            hashMap.put("type", BoxFolder.TYPE);
            this.mBodyMap.put("item", new BoxMapJsonObject(hashMap));
        }

        private void setAccessibleBy(String str, String str2, String str3) {
            Object obj;
            HashMap hashMap = new HashMap();
            if (!SdkUtils.isEmptyString(str)) {
                hashMap.put("id", str);
            }
            if (!SdkUtils.isEmptyString(str2)) {
                hashMap.put("login", str2);
            }
            hashMap.put("type", str3);
            if (str3.equals("user")) {
                obj = new BoxUser(hashMap);
            } else if (str3.equals(BoxGroup.TYPE)) {
                obj = new BoxGroup(hashMap);
            } else {
                throw new IllegalArgumentException("AccessibleBy property can only be set with type BoxUser.TYPE or BoxGroup.TYPE");
            }
            this.mBodyMap.put("accessible_by", obj);
        }

        public BoxCollaborator getAccessibleBy() {
            if (this.mBodyMap.containsKey("accessible_by")) {
                return (BoxCollaborator) this.mBodyMap.get("accessible_by");
            }
            return null;
        }
    }

    public static class DeleteCollaboration extends BoxRequest<BoxVoid, DeleteCollaboration> {
        private String mId;

        public DeleteCollaboration(String str, String str2, BoxSession boxSession) {
            super(BoxVoid.class, str2, boxSession);
            this.mId = str;
            this.mRequestMethod = Methods.DELETE;
        }

        public String getId() {
            return this.mId;
        }
    }

    public static class GetCollaborationInfo extends BoxRequest<BoxCollaboration, GetCollaborationInfo> {
        private final String mId;

        public GetCollaborationInfo(String str, String str2, BoxSession boxSession) {
            super(BoxCollaboration.class, str2, boxSession);
            this.mRequestMethod = Methods.GET;
            this.mId = str;
        }

        public String getId() {
            return this.mId;
        }
    }

    public static class GetPendingCollaborations extends BoxRequest<BoxListCollaborations, GetPendingCollaborations> {
        public GetPendingCollaborations(String str, BoxSession boxSession) {
            super(BoxListCollaborations.class, str, boxSession);
            this.mRequestMethod = Methods.GET;
            this.mQueryMap.put("status", Status.PENDING.toString());
        }
    }

    public static class GetSharedLink extends BoxRequest<BoxItem, GetSharedLink> {
        public GetSharedLink(String str, BoxSharedLinkSession boxSharedLinkSession) {
            super(BoxItem.class, str, boxSharedLinkSession);
            this.mRequestMethod = Methods.GET;
            setRequestHandler(new BoxRequestHandler<GetSharedLink>(this) {
                public <T extends BoxObject> T onResponse(Class<T> cls, BoxHttpResponse boxHttpResponse) throws BoxException {
                    if (boxHttpResponse.getResponseCode() == 429) {
                        return retryRateLimited(boxHttpResponse);
                    }
                    String contentType = boxHttpResponse.getContentType();
                    T boxEntity = new BoxEntity();
                    if (contentType.contains(ContentTypes.JSON.toString())) {
                        String stringBody = boxHttpResponse.getStringBody();
                        boxEntity.createFromJson(stringBody);
                        if (boxEntity.getType().equals(BoxFolder.TYPE)) {
                            boxEntity = new BoxFolder();
                            boxEntity.createFromJson(stringBody);
                        } else if (boxEntity.getType().equals(BoxFile.TYPE)) {
                            boxEntity = new BoxFile();
                            boxEntity.createFromJson(stringBody);
                        } else if (boxEntity.getType().equals(BoxBookmark.TYPE)) {
                            boxEntity = new BoxBookmark();
                            boxEntity.createFromJson(stringBody);
                        }
                    }
                    return boxEntity;
                }
            });
        }

        public GetSharedLink setIfNoneMatchEtag(String str) {
            return (GetSharedLink) super.setIfNoneMatchEtag(str);
        }

        public String getIfNoneMatchEtag() {
            return super.getIfNoneMatchEtag();
        }
    }

    public static class UpdateCollaboration extends BoxRequest<BoxCollaboration, UpdateCollaboration> {
        private String mId;

        public UpdateCollaboration(String str, String str2, BoxSession boxSession) {
            super(BoxCollaboration.class, str2, boxSession);
            this.mId = str;
            this.mRequestMethod = Methods.PUT;
        }

        public String getId() {
            return this.mId;
        }

        public UpdateCollaboration setNewRole(Role role) {
            this.mBodyMap.put("role", role.toString());
            return this;
        }

        public UpdateCollaboration setNewStatus(String str) {
            this.mBodyMap.put("status", str);
            return this;
        }
    }
}
