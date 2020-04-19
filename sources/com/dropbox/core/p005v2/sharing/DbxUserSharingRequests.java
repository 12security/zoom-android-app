package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.LocalizedText;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.p005v2.async.LaunchEmptyResult;
import com.dropbox.core.p005v2.async.LaunchResultBase;
import com.dropbox.core.p005v2.async.PollArg;
import com.dropbox.core.p005v2.async.PollArg.Serializer;
import com.dropbox.core.p005v2.async.PollError;
import com.dropbox.core.p005v2.async.PollErrorException;
import com.dropbox.core.stone.StoneSerializers;
import java.util.Collections;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.DbxUserSharingRequests */
public class DbxUserSharingRequests {
    private final DbxRawClientV2 client;

    public DbxUserSharingRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public List<FileMemberActionResult> addFileMember(AddFileMemberArgs addFileMemberArgs) throws AddFileMemberErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/add_file_member", addFileMemberArgs, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new AddFileMemberErrorException("2/sharing/add_file_member", e.getRequestId(), e.getUserMessage(), (AddFileMemberError) e.getErrorValue());
        }
    }

    public List<FileMemberActionResult> addFileMember(String str, List<MemberSelector> list) throws AddFileMemberErrorException, DbxException {
        return addFileMember(new AddFileMemberArgs(str, list));
    }

    public AddFileMemberBuilder addFileMemberBuilder(String str, List<MemberSelector> list) {
        return new AddFileMemberBuilder(this, AddFileMemberArgs.newBuilder(str, list));
    }

    /* access modifiers changed from: 0000 */
    public void addFolderMember(AddFolderMemberArg addFolderMemberArg) throws AddFolderMemberErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/add_folder_member", addFolderMemberArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new AddFolderMemberErrorException("2/sharing/add_folder_member", e.getRequestId(), e.getUserMessage(), (AddFolderMemberError) e.getErrorValue());
        }
    }

    public void addFolderMember(String str, List<AddMember> list) throws AddFolderMemberErrorException, DbxException {
        addFolderMember(new AddFolderMemberArg(str, list));
    }

    public AddFolderMemberBuilder addFolderMemberBuilder(String str, List<AddMember> list) {
        return new AddFolderMemberBuilder(this, AddFolderMemberArg.newBuilder(str, list));
    }

    /* access modifiers changed from: 0000 */
    public FileMemberActionResult changeFileMemberAccess(ChangeFileMemberAccessArgs changeFileMemberAccessArgs) throws FileMemberActionErrorException, DbxException {
        try {
            return (FileMemberActionResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/change_file_member_access", changeFileMemberAccessArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new FileMemberActionErrorException("2/sharing/change_file_member_access", e.getRequestId(), e.getUserMessage(), (FileMemberActionError) e.getErrorValue());
        }
    }

    @Deprecated
    public FileMemberActionResult changeFileMemberAccess(String str, MemberSelector memberSelector, AccessLevel accessLevel) throws FileMemberActionErrorException, DbxException {
        return changeFileMemberAccess(new ChangeFileMemberAccessArgs(str, memberSelector, accessLevel));
    }

    /* access modifiers changed from: 0000 */
    public JobStatus checkJobStatus(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (JobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/check_job_status", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/sharing/check_job_status", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public JobStatus checkJobStatus(String str) throws PollErrorException, DbxException {
        return checkJobStatus(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public RemoveMemberJobStatus checkRemoveMemberJobStatus(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (RemoveMemberJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/check_remove_member_job_status", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/sharing/check_remove_member_job_status", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public RemoveMemberJobStatus checkRemoveMemberJobStatus(String str) throws PollErrorException, DbxException {
        return checkRemoveMemberJobStatus(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ShareFolderJobStatus checkShareJobStatus(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (ShareFolderJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/check_share_job_status", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/sharing/check_share_job_status", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public ShareFolderJobStatus checkShareJobStatus(String str) throws PollErrorException, DbxException {
        return checkShareJobStatus(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public PathLinkMetadata createSharedLink(CreateSharedLinkArg createSharedLinkArg) throws CreateSharedLinkErrorException, DbxException {
        try {
            return (PathLinkMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/create_shared_link", createSharedLinkArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new CreateSharedLinkErrorException("2/sharing/create_shared_link", e.getRequestId(), e.getUserMessage(), (CreateSharedLinkError) e.getErrorValue());
        }
    }

    @Deprecated
    public PathLinkMetadata createSharedLink(String str) throws CreateSharedLinkErrorException, DbxException {
        return createSharedLink(new CreateSharedLinkArg(str));
    }

    @Deprecated
    public CreateSharedLinkBuilder createSharedLinkBuilder(String str) {
        return new CreateSharedLinkBuilder(this, CreateSharedLinkArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public SharedLinkMetadata createSharedLinkWithSettings(CreateSharedLinkWithSettingsArg createSharedLinkWithSettingsArg) throws CreateSharedLinkWithSettingsErrorException, DbxException {
        try {
            return (SharedLinkMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/create_shared_link_with_settings", createSharedLinkWithSettingsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new CreateSharedLinkWithSettingsErrorException("2/sharing/create_shared_link_with_settings", e.getRequestId(), e.getUserMessage(), (CreateSharedLinkWithSettingsError) e.getErrorValue());
        }
    }

    public SharedLinkMetadata createSharedLinkWithSettings(String str) throws CreateSharedLinkWithSettingsErrorException, DbxException {
        return createSharedLinkWithSettings(new CreateSharedLinkWithSettingsArg(str));
    }

    public SharedLinkMetadata createSharedLinkWithSettings(String str, SharedLinkSettings sharedLinkSettings) throws CreateSharedLinkWithSettingsErrorException, DbxException {
        return createSharedLinkWithSettings(new CreateSharedLinkWithSettingsArg(str, sharedLinkSettings));
    }

    /* access modifiers changed from: 0000 */
    public SharedFileMetadata getFileMetadata(GetFileMetadataArg getFileMetadataArg) throws GetFileMetadataErrorException, DbxException {
        try {
            return (SharedFileMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/get_file_metadata", getFileMetadataArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetFileMetadataErrorException("2/sharing/get_file_metadata", e.getRequestId(), e.getUserMessage(), (GetFileMetadataError) e.getErrorValue());
        }
    }

    public SharedFileMetadata getFileMetadata(String str) throws GetFileMetadataErrorException, DbxException {
        return getFileMetadata(new GetFileMetadataArg(str));
    }

    public SharedFileMetadata getFileMetadata(String str, List<FileAction> list) throws GetFileMetadataErrorException, DbxException {
        if (list != null) {
            for (FileAction fileAction : list) {
                if (fileAction == null) {
                    throw new IllegalArgumentException("An item in list 'actions' is null");
                }
            }
        }
        return getFileMetadata(new GetFileMetadataArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public List<GetFileMetadataBatchResult> getFileMetadataBatch(GetFileMetadataBatchArg getFileMetadataBatchArg) throws SharingUserErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/get_file_metadata/batch", getFileMetadataBatchArg, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SharingUserErrorException("2/sharing/get_file_metadata/batch", e.getRequestId(), e.getUserMessage(), (SharingUserError) e.getErrorValue());
        }
    }

    public List<GetFileMetadataBatchResult> getFileMetadataBatch(List<String> list) throws SharingUserErrorException, DbxException {
        return getFileMetadataBatch(new GetFileMetadataBatchArg(list));
    }

    public List<GetFileMetadataBatchResult> getFileMetadataBatch(List<String> list, List<FileAction> list2) throws SharingUserErrorException, DbxException {
        if (list2 != null) {
            for (FileAction fileAction : list2) {
                if (fileAction == null) {
                    throw new IllegalArgumentException("An item in list 'actions' is null");
                }
            }
        }
        return getFileMetadataBatch(new GetFileMetadataBatchArg(list, list2));
    }

    /* access modifiers changed from: 0000 */
    public SharedFolderMetadata getFolderMetadata(GetMetadataArgs getMetadataArgs) throws SharedFolderAccessErrorException, DbxException {
        try {
            return (SharedFolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/get_folder_metadata", getMetadataArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SharedFolderAccessErrorException("2/sharing/get_folder_metadata", e.getRequestId(), e.getUserMessage(), (SharedFolderAccessError) e.getErrorValue());
        }
    }

    public SharedFolderMetadata getFolderMetadata(String str) throws SharedFolderAccessErrorException, DbxException {
        return getFolderMetadata(new GetMetadataArgs(str));
    }

    public SharedFolderMetadata getFolderMetadata(String str, List<FolderAction> list) throws SharedFolderAccessErrorException, DbxException {
        if (list != null) {
            for (FolderAction folderAction : list) {
                if (folderAction == null) {
                    throw new IllegalArgumentException("An item in list 'actions' is null");
                }
            }
        }
        return getFolderMetadata(new GetMetadataArgs(str, list));
    }

    /* access modifiers changed from: 0000 */
    public DbxDownloader<SharedLinkMetadata> getSharedLinkFile(GetSharedLinkMetadataArg getSharedLinkMetadataArg, List<Header> list) throws GetSharedLinkFileErrorException, DbxException {
        try {
            return this.client.downloadStyle(this.client.getHost().getContent(), "2/sharing/get_shared_link_file", getSharedLinkMetadataArg, false, list, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetSharedLinkFileErrorException("2/sharing/get_shared_link_file", e.getRequestId(), e.getUserMessage(), (GetSharedLinkFileError) e.getErrorValue());
        }
    }

    public DbxDownloader<SharedLinkMetadata> getSharedLinkFile(String str) throws GetSharedLinkFileErrorException, DbxException {
        return getSharedLinkFile(new GetSharedLinkMetadataArg(str), Collections.emptyList());
    }

    public GetSharedLinkFileBuilder getSharedLinkFileBuilder(String str) {
        return new GetSharedLinkFileBuilder(this, GetSharedLinkMetadataArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public SharedLinkMetadata getSharedLinkMetadata(GetSharedLinkMetadataArg getSharedLinkMetadataArg) throws SharedLinkErrorException, DbxException {
        try {
            return (SharedLinkMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/get_shared_link_metadata", getSharedLinkMetadataArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SharedLinkErrorException("2/sharing/get_shared_link_metadata", e.getRequestId(), e.getUserMessage(), (SharedLinkError) e.getErrorValue());
        }
    }

    public SharedLinkMetadata getSharedLinkMetadata(String str) throws SharedLinkErrorException, DbxException {
        return getSharedLinkMetadata(new GetSharedLinkMetadataArg(str));
    }

    public GetSharedLinkMetadataBuilder getSharedLinkMetadataBuilder(String str) {
        return new GetSharedLinkMetadataBuilder(this, GetSharedLinkMetadataArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public GetSharedLinksResult getSharedLinks(GetSharedLinksArg getSharedLinksArg) throws GetSharedLinksErrorException, DbxException {
        try {
            return (GetSharedLinksResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/get_shared_links", getSharedLinksArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetSharedLinksErrorException("2/sharing/get_shared_links", e.getRequestId(), e.getUserMessage(), (GetSharedLinksError) e.getErrorValue());
        }
    }

    @Deprecated
    public GetSharedLinksResult getSharedLinks() throws GetSharedLinksErrorException, DbxException {
        return getSharedLinks(new GetSharedLinksArg());
    }

    @Deprecated
    public GetSharedLinksResult getSharedLinks(String str) throws GetSharedLinksErrorException, DbxException {
        return getSharedLinks(new GetSharedLinksArg(str));
    }

    /* access modifiers changed from: 0000 */
    public SharedFileMembers listFileMembers(ListFileMembersArg listFileMembersArg) throws ListFileMembersErrorException, DbxException {
        try {
            return (SharedFileMembers) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_file_members", listFileMembersArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFileMembersErrorException("2/sharing/list_file_members", e.getRequestId(), e.getUserMessage(), (ListFileMembersError) e.getErrorValue());
        }
    }

    public SharedFileMembers listFileMembers(String str) throws ListFileMembersErrorException, DbxException {
        return listFileMembers(new ListFileMembersArg(str));
    }

    public ListFileMembersBuilder listFileMembersBuilder(String str) {
        return new ListFileMembersBuilder(this, ListFileMembersArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public List<ListFileMembersBatchResult> listFileMembersBatch(ListFileMembersBatchArg listFileMembersBatchArg) throws SharingUserErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_file_members/batch", listFileMembersBatchArg, false, Serializer.INSTANCE, StoneSerializers.list(Serializer.INSTANCE), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SharingUserErrorException("2/sharing/list_file_members/batch", e.getRequestId(), e.getUserMessage(), (SharingUserError) e.getErrorValue());
        }
    }

    public List<ListFileMembersBatchResult> listFileMembersBatch(List<String> list) throws SharingUserErrorException, DbxException {
        return listFileMembersBatch(new ListFileMembersBatchArg(list));
    }

    public List<ListFileMembersBatchResult> listFileMembersBatch(List<String> list, long j) throws SharingUserErrorException, DbxException {
        if (j <= 20) {
            return listFileMembersBatch(new ListFileMembersBatchArg(list, j));
        }
        throw new IllegalArgumentException("Number 'limit' is larger than 20L");
    }

    /* access modifiers changed from: 0000 */
    public SharedFileMembers listFileMembersContinue(ListFileMembersContinueArg listFileMembersContinueArg) throws ListFileMembersContinueErrorException, DbxException {
        try {
            return (SharedFileMembers) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_file_members/continue", listFileMembersContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFileMembersContinueErrorException("2/sharing/list_file_members/continue", e.getRequestId(), e.getUserMessage(), (ListFileMembersContinueError) e.getErrorValue());
        }
    }

    public SharedFileMembers listFileMembersContinue(String str) throws ListFileMembersContinueErrorException, DbxException {
        return listFileMembersContinue(new ListFileMembersContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public SharedFolderMembers listFolderMembers(ListFolderMembersArgs listFolderMembersArgs) throws SharedFolderAccessErrorException, DbxException {
        try {
            return (SharedFolderMembers) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_folder_members", listFolderMembersArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SharedFolderAccessErrorException("2/sharing/list_folder_members", e.getRequestId(), e.getUserMessage(), (SharedFolderAccessError) e.getErrorValue());
        }
    }

    public SharedFolderMembers listFolderMembers(String str) throws SharedFolderAccessErrorException, DbxException {
        return listFolderMembers(new ListFolderMembersArgs(str));
    }

    public ListFolderMembersBuilder listFolderMembersBuilder(String str) {
        return new ListFolderMembersBuilder(this, ListFolderMembersArgs.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public SharedFolderMembers listFolderMembersContinue(ListFolderMembersContinueArg listFolderMembersContinueArg) throws ListFolderMembersContinueErrorException, DbxException {
        try {
            return (SharedFolderMembers) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_folder_members/continue", listFolderMembersContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFolderMembersContinueErrorException("2/sharing/list_folder_members/continue", e.getRequestId(), e.getUserMessage(), (ListFolderMembersContinueError) e.getErrorValue());
        }
    }

    public SharedFolderMembers listFolderMembersContinue(String str) throws ListFolderMembersContinueErrorException, DbxException {
        return listFolderMembersContinue(new ListFolderMembersContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ListFoldersResult listFolders(ListFoldersArgs listFoldersArgs) throws DbxApiException, DbxException {
        try {
            return (ListFoldersResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_folders", listFoldersArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"list_folders\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public ListFoldersResult listFolders() throws DbxApiException, DbxException {
        return listFolders(new ListFoldersArgs());
    }

    public ListFoldersBuilder listFoldersBuilder() {
        return new ListFoldersBuilder(this, ListFoldersArgs.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public ListFoldersResult listFoldersContinue(ListFoldersContinueArg listFoldersContinueArg) throws ListFoldersContinueErrorException, DbxException {
        try {
            return (ListFoldersResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_folders/continue", listFoldersContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFoldersContinueErrorException("2/sharing/list_folders/continue", e.getRequestId(), e.getUserMessage(), (ListFoldersContinueError) e.getErrorValue());
        }
    }

    public ListFoldersResult listFoldersContinue(String str) throws ListFoldersContinueErrorException, DbxException {
        return listFoldersContinue(new ListFoldersContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ListFoldersResult listMountableFolders(ListFoldersArgs listFoldersArgs) throws DbxApiException, DbxException {
        try {
            return (ListFoldersResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_mountable_folders", listFoldersArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"list_mountable_folders\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public ListFoldersResult listMountableFolders() throws DbxApiException, DbxException {
        return listMountableFolders(new ListFoldersArgs());
    }

    public ListMountableFoldersBuilder listMountableFoldersBuilder() {
        return new ListMountableFoldersBuilder(this, ListFoldersArgs.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public ListFoldersResult listMountableFoldersContinue(ListFoldersContinueArg listFoldersContinueArg) throws ListFoldersContinueErrorException, DbxException {
        try {
            return (ListFoldersResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_mountable_folders/continue", listFoldersContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFoldersContinueErrorException("2/sharing/list_mountable_folders/continue", e.getRequestId(), e.getUserMessage(), (ListFoldersContinueError) e.getErrorValue());
        }
    }

    public ListFoldersResult listMountableFoldersContinue(String str) throws ListFoldersContinueErrorException, DbxException {
        return listMountableFoldersContinue(new ListFoldersContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ListFilesResult listReceivedFiles(ListFilesArg listFilesArg) throws SharingUserErrorException, DbxException {
        try {
            return (ListFilesResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_received_files", listFilesArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SharingUserErrorException("2/sharing/list_received_files", e.getRequestId(), e.getUserMessage(), (SharingUserError) e.getErrorValue());
        }
    }

    public ListFilesResult listReceivedFiles() throws SharingUserErrorException, DbxException {
        return listReceivedFiles(new ListFilesArg());
    }

    public ListReceivedFilesBuilder listReceivedFilesBuilder() {
        return new ListReceivedFilesBuilder(this, ListFilesArg.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public ListFilesResult listReceivedFilesContinue(ListFilesContinueArg listFilesContinueArg) throws ListFilesContinueErrorException, DbxException {
        try {
            return (ListFilesResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_received_files/continue", listFilesContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFilesContinueErrorException("2/sharing/list_received_files/continue", e.getRequestId(), e.getUserMessage(), (ListFilesContinueError) e.getErrorValue());
        }
    }

    public ListFilesResult listReceivedFilesContinue(String str) throws ListFilesContinueErrorException, DbxException {
        return listReceivedFilesContinue(new ListFilesContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ListSharedLinksResult listSharedLinks(ListSharedLinksArg listSharedLinksArg) throws ListSharedLinksErrorException, DbxException {
        try {
            return (ListSharedLinksResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/list_shared_links", listSharedLinksArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListSharedLinksErrorException("2/sharing/list_shared_links", e.getRequestId(), e.getUserMessage(), (ListSharedLinksError) e.getErrorValue());
        }
    }

    public ListSharedLinksResult listSharedLinks() throws ListSharedLinksErrorException, DbxException {
        return listSharedLinks(new ListSharedLinksArg());
    }

    public ListSharedLinksBuilder listSharedLinksBuilder() {
        return new ListSharedLinksBuilder(this, ListSharedLinksArg.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public SharedLinkMetadata modifySharedLinkSettings(ModifySharedLinkSettingsArgs modifySharedLinkSettingsArgs) throws ModifySharedLinkSettingsErrorException, DbxException {
        try {
            return (SharedLinkMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/modify_shared_link_settings", modifySharedLinkSettingsArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ModifySharedLinkSettingsErrorException("2/sharing/modify_shared_link_settings", e.getRequestId(), e.getUserMessage(), (ModifySharedLinkSettingsError) e.getErrorValue());
        }
    }

    public SharedLinkMetadata modifySharedLinkSettings(String str, SharedLinkSettings sharedLinkSettings) throws ModifySharedLinkSettingsErrorException, DbxException {
        return modifySharedLinkSettings(new ModifySharedLinkSettingsArgs(str, sharedLinkSettings));
    }

    public SharedLinkMetadata modifySharedLinkSettings(String str, SharedLinkSettings sharedLinkSettings, boolean z) throws ModifySharedLinkSettingsErrorException, DbxException {
        return modifySharedLinkSettings(new ModifySharedLinkSettingsArgs(str, sharedLinkSettings, z));
    }

    /* access modifiers changed from: 0000 */
    public SharedFolderMetadata mountFolder(MountFolderArg mountFolderArg) throws MountFolderErrorException, DbxException {
        try {
            return (SharedFolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/mount_folder", mountFolderArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new MountFolderErrorException("2/sharing/mount_folder", e.getRequestId(), e.getUserMessage(), (MountFolderError) e.getErrorValue());
        }
    }

    public SharedFolderMetadata mountFolder(String str) throws MountFolderErrorException, DbxException {
        return mountFolder(new MountFolderArg(str));
    }

    /* access modifiers changed from: 0000 */
    public void relinquishFileMembership(RelinquishFileMembershipArg relinquishFileMembershipArg) throws RelinquishFileMembershipErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/relinquish_file_membership", relinquishFileMembershipArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RelinquishFileMembershipErrorException("2/sharing/relinquish_file_membership", e.getRequestId(), e.getUserMessage(), (RelinquishFileMembershipError) e.getErrorValue());
        }
    }

    public void relinquishFileMembership(String str) throws RelinquishFileMembershipErrorException, DbxException {
        relinquishFileMembership(new RelinquishFileMembershipArg(str));
    }

    /* access modifiers changed from: 0000 */
    public LaunchEmptyResult relinquishFolderMembership(RelinquishFolderMembershipArg relinquishFolderMembershipArg) throws RelinquishFolderMembershipErrorException, DbxException {
        try {
            return (LaunchEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/relinquish_folder_membership", relinquishFolderMembershipArg, false, Serializer.INSTANCE, LaunchEmptyResult.Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RelinquishFolderMembershipErrorException("2/sharing/relinquish_folder_membership", e.getRequestId(), e.getUserMessage(), (RelinquishFolderMembershipError) e.getErrorValue());
        }
    }

    public LaunchEmptyResult relinquishFolderMembership(String str) throws RelinquishFolderMembershipErrorException, DbxException {
        return relinquishFolderMembership(new RelinquishFolderMembershipArg(str));
    }

    public LaunchEmptyResult relinquishFolderMembership(String str, boolean z) throws RelinquishFolderMembershipErrorException, DbxException {
        return relinquishFolderMembership(new RelinquishFolderMembershipArg(str, z));
    }

    /* access modifiers changed from: 0000 */
    public FileMemberActionIndividualResult removeFileMember(RemoveFileMemberArg removeFileMemberArg) throws RemoveFileMemberErrorException, DbxException {
        try {
            return (FileMemberActionIndividualResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/remove_file_member", removeFileMemberArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RemoveFileMemberErrorException("2/sharing/remove_file_member", e.getRequestId(), e.getUserMessage(), (RemoveFileMemberError) e.getErrorValue());
        }
    }

    @Deprecated
    public FileMemberActionIndividualResult removeFileMember(String str, MemberSelector memberSelector) throws RemoveFileMemberErrorException, DbxException {
        return removeFileMember(new RemoveFileMemberArg(str, memberSelector));
    }

    /* access modifiers changed from: 0000 */
    public FileMemberRemoveActionResult removeFileMember2(RemoveFileMemberArg removeFileMemberArg) throws RemoveFileMemberErrorException, DbxException {
        try {
            return (FileMemberRemoveActionResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/remove_file_member_2", removeFileMemberArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RemoveFileMemberErrorException("2/sharing/remove_file_member_2", e.getRequestId(), e.getUserMessage(), (RemoveFileMemberError) e.getErrorValue());
        }
    }

    public FileMemberRemoveActionResult removeFileMember2(String str, MemberSelector memberSelector) throws RemoveFileMemberErrorException, DbxException {
        return removeFileMember2(new RemoveFileMemberArg(str, memberSelector));
    }

    /* access modifiers changed from: 0000 */
    public LaunchResultBase removeFolderMember(RemoveFolderMemberArg removeFolderMemberArg) throws RemoveFolderMemberErrorException, DbxException {
        try {
            return (LaunchResultBase) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/remove_folder_member", removeFolderMemberArg, false, Serializer.INSTANCE, LaunchResultBase.Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RemoveFolderMemberErrorException("2/sharing/remove_folder_member", e.getRequestId(), e.getUserMessage(), (RemoveFolderMemberError) e.getErrorValue());
        }
    }

    public LaunchResultBase removeFolderMember(String str, MemberSelector memberSelector, boolean z) throws RemoveFolderMemberErrorException, DbxException {
        return removeFolderMember(new RemoveFolderMemberArg(str, memberSelector, z));
    }

    /* access modifiers changed from: 0000 */
    public void revokeSharedLink(RevokeSharedLinkArg revokeSharedLinkArg) throws RevokeSharedLinkErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/revoke_shared_link", revokeSharedLinkArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RevokeSharedLinkErrorException("2/sharing/revoke_shared_link", e.getRequestId(), e.getUserMessage(), (RevokeSharedLinkError) e.getErrorValue());
        }
    }

    public void revokeSharedLink(String str) throws RevokeSharedLinkErrorException, DbxException {
        revokeSharedLink(new RevokeSharedLinkArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ShareFolderLaunch setAccessInheritance(SetAccessInheritanceArg setAccessInheritanceArg) throws SetAccessInheritanceErrorException, DbxException {
        try {
            return (ShareFolderLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/set_access_inheritance", setAccessInheritanceArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SetAccessInheritanceErrorException("2/sharing/set_access_inheritance", e.getRequestId(), e.getUserMessage(), (SetAccessInheritanceError) e.getErrorValue());
        }
    }

    public ShareFolderLaunch setAccessInheritance(String str) throws SetAccessInheritanceErrorException, DbxException {
        return setAccessInheritance(new SetAccessInheritanceArg(str));
    }

    public ShareFolderLaunch setAccessInheritance(String str, AccessInheritance accessInheritance) throws SetAccessInheritanceErrorException, DbxException {
        if (accessInheritance != null) {
            return setAccessInheritance(new SetAccessInheritanceArg(str, accessInheritance));
        }
        throw new IllegalArgumentException("Required value for 'accessInheritance' is null");
    }

    /* access modifiers changed from: 0000 */
    public ShareFolderLaunch shareFolder(ShareFolderArg shareFolderArg) throws ShareFolderErrorException, DbxException {
        try {
            return (ShareFolderLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/share_folder", shareFolderArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ShareFolderErrorException("2/sharing/share_folder", e.getRequestId(), e.getUserMessage(), (ShareFolderError) e.getErrorValue());
        }
    }

    public ShareFolderLaunch shareFolder(String str) throws ShareFolderErrorException, DbxException {
        return shareFolder(new ShareFolderArg(str));
    }

    public ShareFolderBuilder shareFolderBuilder(String str) {
        return new ShareFolderBuilder(this, ShareFolderArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public void transferFolder(TransferFolderArg transferFolderArg) throws TransferFolderErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/transfer_folder", transferFolderArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TransferFolderErrorException("2/sharing/transfer_folder", e.getRequestId(), e.getUserMessage(), (TransferFolderError) e.getErrorValue());
        }
    }

    public void transferFolder(String str, String str2) throws TransferFolderErrorException, DbxException {
        transferFolder(new TransferFolderArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public void unmountFolder(UnmountFolderArg unmountFolderArg) throws UnmountFolderErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/unmount_folder", unmountFolderArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UnmountFolderErrorException("2/sharing/unmount_folder", e.getRequestId(), e.getUserMessage(), (UnmountFolderError) e.getErrorValue());
        }
    }

    public void unmountFolder(String str) throws UnmountFolderErrorException, DbxException {
        unmountFolder(new UnmountFolderArg(str));
    }

    /* access modifiers changed from: 0000 */
    public void unshareFile(UnshareFileArg unshareFileArg) throws UnshareFileErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/unshare_file", unshareFileArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UnshareFileErrorException("2/sharing/unshare_file", e.getRequestId(), e.getUserMessage(), (UnshareFileError) e.getErrorValue());
        }
    }

    public void unshareFile(String str) throws UnshareFileErrorException, DbxException {
        unshareFile(new UnshareFileArg(str));
    }

    /* access modifiers changed from: 0000 */
    public LaunchEmptyResult unshareFolder(UnshareFolderArg unshareFolderArg) throws UnshareFolderErrorException, DbxException {
        try {
            return (LaunchEmptyResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/unshare_folder", unshareFolderArg, false, Serializer.INSTANCE, LaunchEmptyResult.Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UnshareFolderErrorException("2/sharing/unshare_folder", e.getRequestId(), e.getUserMessage(), (UnshareFolderError) e.getErrorValue());
        }
    }

    public LaunchEmptyResult unshareFolder(String str) throws UnshareFolderErrorException, DbxException {
        return unshareFolder(new UnshareFolderArg(str));
    }

    public LaunchEmptyResult unshareFolder(String str, boolean z) throws UnshareFolderErrorException, DbxException {
        return unshareFolder(new UnshareFolderArg(str, z));
    }

    /* access modifiers changed from: 0000 */
    public MemberAccessLevelResult updateFileMember(UpdateFileMemberArgs updateFileMemberArgs) throws FileMemberActionErrorException, DbxException {
        try {
            return (MemberAccessLevelResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/update_file_member", updateFileMemberArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new FileMemberActionErrorException("2/sharing/update_file_member", e.getRequestId(), e.getUserMessage(), (FileMemberActionError) e.getErrorValue());
        }
    }

    public MemberAccessLevelResult updateFileMember(String str, MemberSelector memberSelector, AccessLevel accessLevel) throws FileMemberActionErrorException, DbxException {
        return updateFileMember(new UpdateFileMemberArgs(str, memberSelector, accessLevel));
    }

    /* access modifiers changed from: 0000 */
    public MemberAccessLevelResult updateFolderMember(UpdateFolderMemberArg updateFolderMemberArg) throws UpdateFolderMemberErrorException, DbxException {
        try {
            return (MemberAccessLevelResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/update_folder_member", updateFolderMemberArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UpdateFolderMemberErrorException("2/sharing/update_folder_member", e.getRequestId(), e.getUserMessage(), (UpdateFolderMemberError) e.getErrorValue());
        }
    }

    public MemberAccessLevelResult updateFolderMember(String str, MemberSelector memberSelector, AccessLevel accessLevel) throws UpdateFolderMemberErrorException, DbxException {
        return updateFolderMember(new UpdateFolderMemberArg(str, memberSelector, accessLevel));
    }

    /* access modifiers changed from: 0000 */
    public SharedFolderMetadata updateFolderPolicy(UpdateFolderPolicyArg updateFolderPolicyArg) throws UpdateFolderPolicyErrorException, DbxException {
        try {
            return (SharedFolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/sharing/update_folder_policy", updateFolderPolicyArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UpdateFolderPolicyErrorException("2/sharing/update_folder_policy", e.getRequestId(), e.getUserMessage(), (UpdateFolderPolicyError) e.getErrorValue());
        }
    }

    public SharedFolderMetadata updateFolderPolicy(String str) throws UpdateFolderPolicyErrorException, DbxException {
        return updateFolderPolicy(new UpdateFolderPolicyArg(str));
    }

    public UpdateFolderPolicyBuilder updateFolderPolicyBuilder(String str) {
        return new UpdateFolderPolicyBuilder(this, UpdateFolderPolicyArg.newBuilder(str));
    }
}
