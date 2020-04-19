package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxCollaboration.Role;
import com.box.androidsdk.content.models.BoxCollaborator;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsShare.AddCollaboration;
import com.box.androidsdk.content.requests.BoxRequestsShare.DeleteCollaboration;
import com.box.androidsdk.content.requests.BoxRequestsShare.GetCollaborationInfo;
import com.box.androidsdk.content.requests.BoxRequestsShare.GetPendingCollaborations;
import com.box.androidsdk.content.requests.BoxRequestsShare.UpdateCollaboration;

public class BoxApiCollaboration extends BoxApi {
    /* access modifiers changed from: protected */
    public String getCollaborationsUrl() {
        return String.format("%s/collaborations", new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getCollaborationInfoUrl(String str) {
        return String.format("%s/%s", new Object[]{getCollaborationsUrl(), str});
    }

    public BoxApiCollaboration(BoxSession boxSession) {
        super(boxSession);
    }

    public GetCollaborationInfo getInfoRequest(String str) {
        return new GetCollaborationInfo(str, getCollaborationInfoUrl(str), this.mSession);
    }

    public AddCollaboration getAddRequest(String str, Role role, BoxCollaborator boxCollaborator) {
        AddCollaboration addCollaboration = new AddCollaboration(getCollaborationsUrl(), str, role, boxCollaborator, this.mSession);
        return addCollaboration;
    }

    public AddCollaboration getAddRequest(String str, Role role, String str2) {
        AddCollaboration addCollaboration = new AddCollaboration(getCollaborationsUrl(), str, role, str2, this.mSession);
        return addCollaboration;
    }

    public GetPendingCollaborations getPendingCollaborationsRequest() {
        return new GetPendingCollaborations(getCollaborationsUrl(), this.mSession);
    }

    public DeleteCollaboration getDeleteRequest(String str) {
        return new DeleteCollaboration(str, getCollaborationInfoUrl(str), this.mSession);
    }

    public UpdateCollaboration getUpdateRequest(String str) {
        return new UpdateCollaboration(str, getCollaborationInfoUrl(str), this.mSession);
    }
}
