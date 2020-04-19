package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.stone.StoneSerializers;

/* renamed from: com.dropbox.core.v2.filerequests.DbxUserFileRequestsRequests */
public class DbxUserFileRequestsRequests {
    private final DbxRawClientV2 client;

    public DbxUserFileRequestsRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public FileRequest create(CreateFileRequestArgs createFileRequestArgs) throws CreateFileRequestErrorException, DbxException {
        try {
            return (FileRequest) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_requests/create", createFileRequestArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new CreateFileRequestErrorException("2/file_requests/create", e.getRequestId(), e.getUserMessage(), (CreateFileRequestError) e.getErrorValue());
        }
    }

    public FileRequest create(String str, String str2) throws CreateFileRequestErrorException, DbxException {
        return create(new CreateFileRequestArgs(str, str2));
    }

    public CreateBuilder createBuilder(String str, String str2) {
        return new CreateBuilder(this, CreateFileRequestArgs.newBuilder(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public FileRequest get(GetFileRequestArgs getFileRequestArgs) throws GetFileRequestErrorException, DbxException {
        try {
            return (FileRequest) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_requests/get", getFileRequestArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetFileRequestErrorException("2/file_requests/get", e.getRequestId(), e.getUserMessage(), (GetFileRequestError) e.getErrorValue());
        }
    }

    public FileRequest get(String str) throws GetFileRequestErrorException, DbxException {
        return get(new GetFileRequestArgs(str));
    }

    public ListFileRequestsResult list() throws ListFileRequestsErrorException, DbxException {
        try {
            return (ListFileRequestsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_requests/list", null, false, StoneSerializers.void_(), Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFileRequestsErrorException("2/file_requests/list", e.getRequestId(), e.getUserMessage(), (ListFileRequestsError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public FileRequest update(UpdateFileRequestArgs updateFileRequestArgs) throws UpdateFileRequestErrorException, DbxException {
        try {
            return (FileRequest) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_requests/update", updateFileRequestArgs, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UpdateFileRequestErrorException("2/file_requests/update", e.getRequestId(), e.getUserMessage(), (UpdateFileRequestError) e.getErrorValue());
        }
    }

    public FileRequest update(String str) throws UpdateFileRequestErrorException, DbxException {
        return update(new UpdateFileRequestArgs(str));
    }

    public UpdateBuilder updateBuilder(String str) {
        return new UpdateBuilder(this, UpdateFileRequestArgs.newBuilder(str));
    }
}
