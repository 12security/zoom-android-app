package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.LocalizedText;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.p005v2.sharing.MemberSelector;
import com.dropbox.core.stone.StoneSerializers;
import java.util.Collections;
import java.util.List;

/* renamed from: com.dropbox.core.v2.paper.DbxUserPaperRequests */
public class DbxUserPaperRequests {
    private final DbxRawClientV2 client;

    public DbxUserPaperRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public void docsArchive(RefPaperDoc refPaperDoc) throws DocLookupErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/archive", refPaperDoc, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/archive", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public void docsArchive(String str) throws DocLookupErrorException, DbxException {
        docsArchive(new RefPaperDoc(str));
    }

    /* access modifiers changed from: 0000 */
    public DocsCreateUploader docsCreate(PaperDocCreateArgs paperDocCreateArgs) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new DocsCreateUploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getApi(), "2/paper/docs/create", paperDocCreateArgs, false, Serializer.INSTANCE), this.client.getUserId());
    }

    public DocsCreateUploader docsCreate(ImportFormat importFormat) throws DbxException {
        return docsCreate(new PaperDocCreateArgs(importFormat));
    }

    public DocsCreateUploader docsCreate(ImportFormat importFormat, String str) throws DbxException {
        return docsCreate(new PaperDocCreateArgs(importFormat, str));
    }

    /* access modifiers changed from: 0000 */
    public DbxDownloader<PaperDocExportResult> docsDownload(PaperDocExport paperDocExport, List<Header> list) throws DocLookupErrorException, DbxException {
        try {
            return this.client.downloadStyle(this.client.getHost().getApi(), "2/paper/docs/download", paperDocExport, false, list, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/download", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public DbxDownloader<PaperDocExportResult> docsDownload(String str, ExportFormat exportFormat) throws DocLookupErrorException, DbxException {
        return docsDownload(new PaperDocExport(str, exportFormat), Collections.emptyList());
    }

    public DocsDownloadBuilder docsDownloadBuilder(String str, ExportFormat exportFormat) {
        return new DocsDownloadBuilder(this, str, exportFormat);
    }

    /* access modifiers changed from: 0000 */
    public ListUsersOnFolderResponse docsFolderUsersList(ListUsersOnFolderArgs listUsersOnFolderArgs) throws DocLookupErrorException, DbxException {
        try {
            return (ListUsersOnFolderResponse) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/folder_users/list", listUsersOnFolderArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/folder_users/list", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public ListUsersOnFolderResponse docsFolderUsersList(String str) throws DocLookupErrorException, DbxException {
        return docsFolderUsersList(new ListUsersOnFolderArgs(str));
    }

    public ListUsersOnFolderResponse docsFolderUsersList(String str, int i) throws DocLookupErrorException, DbxException {
        if (i < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1");
        } else if (i <= 1000) {
            return docsFolderUsersList(new ListUsersOnFolderArgs(str, i));
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000");
        }
    }

    /* access modifiers changed from: 0000 */
    public ListUsersOnFolderResponse docsFolderUsersListContinue(ListUsersOnFolderContinueArgs listUsersOnFolderContinueArgs) throws ListUsersCursorErrorException, DbxException {
        try {
            return (ListUsersOnFolderResponse) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/folder_users/list/continue", listUsersOnFolderContinueArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListUsersCursorErrorException("2/paper/docs/folder_users/list/continue", e.getRequestId(), e.getUserMessage(), (ListUsersCursorError) e.getErrorValue());
        }
    }

    public ListUsersOnFolderResponse docsFolderUsersListContinue(String str, String str2) throws ListUsersCursorErrorException, DbxException {
        return docsFolderUsersListContinue(new ListUsersOnFolderContinueArgs(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public FoldersContainingPaperDoc docsGetFolderInfo(RefPaperDoc refPaperDoc) throws DocLookupErrorException, DbxException {
        try {
            return (FoldersContainingPaperDoc) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/get_folder_info", refPaperDoc, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/get_folder_info", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public FoldersContainingPaperDoc docsGetFolderInfo(String str) throws DocLookupErrorException, DbxException {
        return docsGetFolderInfo(new RefPaperDoc(str));
    }

    /* access modifiers changed from: 0000 */
    public ListPaperDocsResponse docsList(ListPaperDocsArgs listPaperDocsArgs) throws DbxApiException, DbxException {
        try {
            return (ListPaperDocsResponse) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/list", listPaperDocsArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"docs/list\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public ListPaperDocsResponse docsList() throws DbxApiException, DbxException {
        return docsList(new ListPaperDocsArgs());
    }

    public DocsListBuilder docsListBuilder() {
        return new DocsListBuilder(this, ListPaperDocsArgs.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public ListPaperDocsResponse docsListContinue(ListPaperDocsContinueArgs listPaperDocsContinueArgs) throws ListDocsCursorErrorException, DbxException {
        try {
            return (ListPaperDocsResponse) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/list/continue", listPaperDocsContinueArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListDocsCursorErrorException("2/paper/docs/list/continue", e.getRequestId(), e.getUserMessage(), (ListDocsCursorError) e.getErrorValue());
        }
    }

    public ListPaperDocsResponse docsListContinue(String str) throws ListDocsCursorErrorException, DbxException {
        return docsListContinue(new ListPaperDocsContinueArgs(str));
    }

    /* access modifiers changed from: 0000 */
    public void docsPermanentlyDelete(RefPaperDoc refPaperDoc) throws DocLookupErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/permanently_delete", refPaperDoc, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/permanently_delete", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public void docsPermanentlyDelete(String str) throws DocLookupErrorException, DbxException {
        docsPermanentlyDelete(new RefPaperDoc(str));
    }

    /* access modifiers changed from: 0000 */
    public SharingPolicy docsSharingPolicyGet(RefPaperDoc refPaperDoc) throws DocLookupErrorException, DbxException {
        try {
            return (SharingPolicy) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/sharing_policy/get", refPaperDoc, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/sharing_policy/get", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public SharingPolicy docsSharingPolicyGet(String str) throws DocLookupErrorException, DbxException {
        return docsSharingPolicyGet(new RefPaperDoc(str));
    }

    /* access modifiers changed from: 0000 */
    public void docsSharingPolicySet(PaperDocSharingPolicy paperDocSharingPolicy) throws DocLookupErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/sharing_policy/set", paperDocSharingPolicy, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/sharing_policy/set", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public void docsSharingPolicySet(String str, SharingPolicy sharingPolicy) throws DocLookupErrorException, DbxException {
        docsSharingPolicySet(new PaperDocSharingPolicy(str, sharingPolicy));
    }

    /* access modifiers changed from: 0000 */
    public DocsUpdateUploader docsUpdate(PaperDocUpdateArgs paperDocUpdateArgs) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new DocsUpdateUploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getApi(), "2/paper/docs/update", paperDocUpdateArgs, false, Serializer.INSTANCE), this.client.getUserId());
    }

    public DocsUpdateUploader docsUpdate(String str, PaperDocUpdatePolicy paperDocUpdatePolicy, long j, ImportFormat importFormat) throws DbxException {
        PaperDocUpdateArgs paperDocUpdateArgs = new PaperDocUpdateArgs(str, paperDocUpdatePolicy, j, importFormat);
        return docsUpdate(paperDocUpdateArgs);
    }

    /* access modifiers changed from: 0000 */
    public List<AddPaperDocUserMemberResult> docsUsersAdd(AddPaperDocUser addPaperDocUser) throws DocLookupErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/users/add", addPaperDocUser, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/users/add", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public List<AddPaperDocUserMemberResult> docsUsersAdd(String str, List<AddMember> list) throws DocLookupErrorException, DbxException {
        return docsUsersAdd(new AddPaperDocUser(str, list));
    }

    public DocsUsersAddBuilder docsUsersAddBuilder(String str, List<AddMember> list) {
        return new DocsUsersAddBuilder(this, AddPaperDocUser.newBuilder(str, list));
    }

    /* access modifiers changed from: 0000 */
    public ListUsersOnPaperDocResponse docsUsersList(ListUsersOnPaperDocArgs listUsersOnPaperDocArgs) throws DocLookupErrorException, DbxException {
        try {
            return (ListUsersOnPaperDocResponse) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/users/list", listUsersOnPaperDocArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/users/list", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public ListUsersOnPaperDocResponse docsUsersList(String str) throws DocLookupErrorException, DbxException {
        return docsUsersList(new ListUsersOnPaperDocArgs(str));
    }

    public DocsUsersListBuilder docsUsersListBuilder(String str) {
        return new DocsUsersListBuilder(this, ListUsersOnPaperDocArgs.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public ListUsersOnPaperDocResponse docsUsersListContinue(ListUsersOnPaperDocContinueArgs listUsersOnPaperDocContinueArgs) throws ListUsersCursorErrorException, DbxException {
        try {
            return (ListUsersOnPaperDocResponse) this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/users/list/continue", listUsersOnPaperDocContinueArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListUsersCursorErrorException("2/paper/docs/users/list/continue", e.getRequestId(), e.getUserMessage(), (ListUsersCursorError) e.getErrorValue());
        }
    }

    public ListUsersOnPaperDocResponse docsUsersListContinue(String str, String str2) throws ListUsersCursorErrorException, DbxException {
        return docsUsersListContinue(new ListUsersOnPaperDocContinueArgs(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public void docsUsersRemove(RemovePaperDocUser removePaperDocUser) throws DocLookupErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/paper/docs/users/remove", removePaperDocUser, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DocLookupErrorException("2/paper/docs/users/remove", e.getRequestId(), e.getUserMessage(), (DocLookupError) e.getErrorValue());
        }
    }

    public void docsUsersRemove(String str, MemberSelector memberSelector) throws DocLookupErrorException, DbxException {
        docsUsersRemove(new RemovePaperDocUser(str, memberSelector));
    }
}
