package com.dropbox.core.p005v2;

import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxRequestUtil;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.p005v2.common.PathRoot;
import java.util.List;

/* renamed from: com.dropbox.core.v2.DbxTeamClientV2 */
public class DbxTeamClientV2 extends DbxTeamClientV2Base {
    private final String accessToken;

    /* renamed from: com.dropbox.core.v2.DbxTeamClientV2$DbxTeamRawClientV2 */
    private static final class DbxTeamRawClientV2 extends DbxRawClientV2 {
        private final String accessToken;
        private final String adminId;
        private final String memberId;

        private DbxTeamRawClientV2(DbxRequestConfig dbxRequestConfig, DbxHost dbxHost, String str) {
            this(dbxRequestConfig, dbxHost, str, null, null, null, null);
        }

        private DbxTeamRawClientV2(DbxRequestConfig dbxRequestConfig, DbxHost dbxHost, String str, String str2, String str3, String str4, PathRoot pathRoot) {
            super(dbxRequestConfig, dbxHost, str2, pathRoot);
            if (str != null) {
                this.accessToken = str;
                this.memberId = str3;
                this.adminId = str4;
                return;
            }
            throw new NullPointerException("accessToken");
        }

        /* access modifiers changed from: protected */
        public void addAuthHeaders(List<Header> list) {
            DbxRequestUtil.addAuthHeader(list, this.accessToken);
            String str = this.memberId;
            if (str != null) {
                DbxRequestUtil.addSelectUserHeader(list, str);
            }
            String str2 = this.adminId;
            if (str2 != null) {
                DbxRequestUtil.addSelectAdminHeader(list, str2);
            }
        }

        /* access modifiers changed from: protected */
        public DbxRawClientV2 withPathRoot(PathRoot pathRoot) {
            DbxTeamRawClientV2 dbxTeamRawClientV2 = new DbxTeamRawClientV2(getRequestConfig(), getHost(), this.accessToken, getUserId(), this.memberId, this.adminId, pathRoot);
            return dbxTeamRawClientV2;
        }
    }

    public DbxTeamClientV2(DbxRequestConfig dbxRequestConfig, String str) {
        this(dbxRequestConfig, str, DbxHost.DEFAULT);
    }

    public DbxTeamClientV2(DbxRequestConfig dbxRequestConfig, String str, DbxHost dbxHost) {
        this(dbxRequestConfig, str, dbxHost, null);
    }

    public DbxTeamClientV2(DbxRequestConfig dbxRequestConfig, String str, DbxHost dbxHost, String str2) {
        DbxTeamRawClientV2 dbxTeamRawClientV2 = new DbxTeamRawClientV2(dbxRequestConfig, dbxHost, str, str2, null, null, null);
        super(dbxTeamRawClientV2);
        this.accessToken = str;
    }

    public DbxClientV2 asMember(String str) {
        if (str != null) {
            DbxTeamRawClientV2 dbxTeamRawClientV2 = new DbxTeamRawClientV2(this._client.getRequestConfig(), this._client.getHost(), this.accessToken, this._client.getUserId(), str, null, null);
            return new DbxClientV2(dbxTeamRawClientV2);
        }
        throw new IllegalArgumentException("'memberId' should not be null");
    }

    public DbxClientV2 asAdmin(String str) {
        if (str != null) {
            DbxTeamRawClientV2 dbxTeamRawClientV2 = new DbxTeamRawClientV2(this._client.getRequestConfig(), this._client.getHost(), this.accessToken, this._client.getUserId(), null, str, null);
            return new DbxClientV2(dbxTeamRawClientV2);
        }
        throw new IllegalArgumentException("'adminId' should not be null");
    }
}
