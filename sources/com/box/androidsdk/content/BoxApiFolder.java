package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsFolder.AddFolderToCollection;
import com.box.androidsdk.content.requests.BoxRequestsFolder.CopyFolder;
import com.box.androidsdk.content.requests.BoxRequestsFolder.CreateFolder;
import com.box.androidsdk.content.requests.BoxRequestsFolder.DeleteFolder;
import com.box.androidsdk.content.requests.BoxRequestsFolder.DeleteFolderFromCollection;
import com.box.androidsdk.content.requests.BoxRequestsFolder.DeleteTrashedFolder;
import com.box.androidsdk.content.requests.BoxRequestsFolder.GetCollaborations;
import com.box.androidsdk.content.requests.BoxRequestsFolder.GetFolderInfo;
import com.box.androidsdk.content.requests.BoxRequestsFolder.GetFolderItems;
import com.box.androidsdk.content.requests.BoxRequestsFolder.GetTrashedFolder;
import com.box.androidsdk.content.requests.BoxRequestsFolder.GetTrashedItems;
import com.box.androidsdk.content.requests.BoxRequestsFolder.RestoreTrashedFolder;
import com.box.androidsdk.content.requests.BoxRequestsFolder.UpdateFolder;
import com.box.androidsdk.content.requests.BoxRequestsFolder.UpdateSharedFolder;

public class BoxApiFolder extends BoxApi {
    public BoxApiFolder(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getFoldersUrl() {
        return String.format("%s/folders", new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getFolderInfoUrl(String str) {
        return String.format("%s/%s", new Object[]{getFoldersUrl(), str});
    }

    /* access modifiers changed from: protected */
    public String getFolderItemsUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFolderInfoUrl(str));
        sb.append("/items");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getFolderCollaborationsUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFolderInfoUrl(str));
        sb.append("/collaborations");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getFolderCopyUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFolderInfoUrl(str));
        sb.append("/copy");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getTrashedFolderUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFolderInfoUrl(str));
        sb.append("/trash");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getTrashedItemsUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFoldersUrl());
        sb.append("/trash/items");
        return sb.toString();
    }

    public GetFolderInfo getInfoRequest(String str) {
        return new GetFolderInfo(str, getFolderInfoUrl(str), this.mSession);
    }

    public GetFolderItems getItemsRequest(String str) {
        return new GetFolderItems(str, getFolderItemsUrl(str), this.mSession);
    }

    public CreateFolder getCreateRequest(String str, String str2) {
        return new CreateFolder(str, str2, getFoldersUrl(), this.mSession);
    }

    public UpdateFolder getUpdateRequest(String str) {
        return new UpdateFolder(str, getFolderInfoUrl(str), this.mSession);
    }

    public UpdateFolder getRenameRequest(String str, String str2) {
        return (UpdateFolder) new UpdateFolder(str, getFolderInfoUrl(str), this.mSession).setName(str2);
    }

    public UpdateFolder getMoveRequest(String str, String str2) {
        return (UpdateFolder) new UpdateFolder(str, getFolderInfoUrl(str), this.mSession).setParentId(str2);
    }

    public CopyFolder getCopyRequest(String str, String str2) {
        return new CopyFolder(str, str2, getFolderCopyUrl(str), this.mSession);
    }

    public DeleteFolder getDeleteRequest(String str) {
        return new DeleteFolder(str, getFolderInfoUrl(str), this.mSession);
    }

    public GetCollaborations getCollaborationsRequest(String str) {
        return new GetCollaborations(str, getFolderCollaborationsUrl(str), this.mSession);
    }

    public UpdateSharedFolder getCreateSharedLinkRequest(String str) {
        return (UpdateSharedFolder) new UpdateSharedFolder(str, getFolderInfoUrl(str), this.mSession).setAccess(null);
    }

    public UpdateFolder getDisableSharedLinkRequest(String str) {
        return (UpdateFolder) new UpdateFolder(str, getFolderInfoUrl(str), this.mSession).setSharedLink(null);
    }

    public AddFolderToCollection getAddToCollectionRequest(String str, String str2) {
        return new AddFolderToCollection(str, str2, getFolderInfoUrl(str), this.mSession);
    }

    public DeleteFolderFromCollection getDeleteFromCollectionRequest(String str) {
        return new DeleteFolderFromCollection(str, getFolderInfoUrl(str), this.mSession);
    }

    public GetTrashedItems getTrashedItemsRequest() {
        return new GetTrashedItems(getTrashedItemsUrl(), this.mSession);
    }

    public GetTrashedFolder getTrashedFolderRequest(String str) {
        return new GetTrashedFolder(str, getTrashedFolderUrl(str), this.mSession);
    }

    public DeleteTrashedFolder getDeleteTrashedFolderRequest(String str) {
        return new DeleteTrashedFolder(str, getTrashedFolderUrl(str), this.mSession);
    }

    public RestoreTrashedFolder getRestoreTrashedFolderRequest(String str) {
        return new RestoreTrashedFolder(str, getFolderInfoUrl(str), this.mSession);
    }
}
