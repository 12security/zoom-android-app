package com.dropbox.core.p005v2.users;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.LocalizedText;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.p005v2.users.GetAccountArg.Serializer;
import com.dropbox.core.stone.StoneSerializers;
import java.util.List;

/* renamed from: com.dropbox.core.v2.users.DbxUserUsersRequests */
public class DbxUserUsersRequests {
    private final DbxRawClientV2 client;

    public DbxUserUsersRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public BasicAccount getAccount(GetAccountArg getAccountArg) throws GetAccountErrorException, DbxException {
        try {
            return (BasicAccount) this.client.rpcStyle(this.client.getHost().getApi(), "2/users/get_account", getAccountArg, false, Serializer.INSTANCE, BasicAccount.Serializer.INSTANCE, GetAccountError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetAccountErrorException("2/users/get_account", e.getRequestId(), e.getUserMessage(), (GetAccountError) e.getErrorValue());
        }
    }

    public BasicAccount getAccount(String str) throws GetAccountErrorException, DbxException {
        return getAccount(new GetAccountArg(str));
    }

    /* access modifiers changed from: 0000 */
    public List<BasicAccount> getAccountBatch(GetAccountBatchArg getAccountBatchArg) throws GetAccountBatchErrorException, DbxException {
        try {
            return (List) this.client.rpcStyle(this.client.getHost().getApi(), "2/users/get_account_batch", getAccountBatchArg, false, GetAccountBatchArg.Serializer.INSTANCE, StoneSerializers.list(BasicAccount.Serializer.INSTANCE), GetAccountBatchError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetAccountBatchErrorException("2/users/get_account_batch", e.getRequestId(), e.getUserMessage(), (GetAccountBatchError) e.getErrorValue());
        }
    }

    public List<BasicAccount> getAccountBatch(List<String> list) throws GetAccountBatchErrorException, DbxException {
        return getAccountBatch(new GetAccountBatchArg(list));
    }

    public FullAccount getCurrentAccount() throws DbxApiException, DbxException {
        try {
            return (FullAccount) this.client.rpcStyle(this.client.getHost().getApi(), "2/users/get_current_account", null, false, StoneSerializers.void_(), Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"get_current_account\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public SpaceUsage getSpaceUsage() throws DbxApiException, DbxException {
        try {
            return (SpaceUsage) this.client.rpcStyle(this.client.getHost().getApi(), "2/users/get_space_usage", null, false, StoneSerializers.void_(), Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"get_space_usage\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }
}
