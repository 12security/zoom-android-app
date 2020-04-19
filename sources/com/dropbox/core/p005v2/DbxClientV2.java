package com.dropbox.core.p005v2;

import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxRequestUtil;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.p005v2.common.PathRoot;
import java.util.List;

/* renamed from: com.dropbox.core.v2.DbxClientV2 */
public class DbxClientV2 extends DbxClientV2Base {

    /* renamed from: com.dropbox.core.v2.DbxClientV2$DbxUserRawClientV2 */
    private static final class DbxUserRawClientV2 extends DbxRawClientV2 {
        private final String accessToken;

        public DbxUserRawClientV2(DbxRequestConfig dbxRequestConfig, String str, DbxHost dbxHost, String str2, PathRoot pathRoot) {
            super(dbxRequestConfig, dbxHost, str2, pathRoot);
            if (str != null) {
                this.accessToken = str;
                return;
            }
            throw new NullPointerException("accessToken");
        }

        /* access modifiers changed from: protected */
        public void addAuthHeaders(List<Header> list) {
            DbxRequestUtil.addAuthHeader(list, this.accessToken);
        }

        /* access modifiers changed from: protected */
        public DbxRawClientV2 withPathRoot(PathRoot pathRoot) {
            DbxUserRawClientV2 dbxUserRawClientV2 = new DbxUserRawClientV2(getRequestConfig(), this.accessToken, getHost(), getUserId(), pathRoot);
            return dbxUserRawClientV2;
        }
    }

    public DbxClientV2(DbxRequestConfig dbxRequestConfig, String str) {
        this(dbxRequestConfig, str, DbxHost.DEFAULT, null);
    }

    public DbxClientV2(DbxRequestConfig dbxRequestConfig, String str, String str2) {
        this(dbxRequestConfig, str, DbxHost.DEFAULT, str2);
    }

    public DbxClientV2(DbxRequestConfig dbxRequestConfig, String str, DbxHost dbxHost) {
        this(dbxRequestConfig, str, dbxHost, null);
    }

    public DbxClientV2(DbxRequestConfig dbxRequestConfig, String str, DbxHost dbxHost, String str2) {
        DbxUserRawClientV2 dbxUserRawClientV2 = new DbxUserRawClientV2(dbxRequestConfig, str, dbxHost, str2, null);
        super(dbxUserRawClientV2);
    }

    DbxClientV2(DbxRawClientV2 dbxRawClientV2) {
        super(dbxRawClientV2);
    }

    public DbxClientV2 withPathRoot(PathRoot pathRoot) {
        if (pathRoot != null) {
            return new DbxClientV2(this._client.withPathRoot(pathRoot));
        }
        throw new IllegalArgumentException("'pathRoot' should not be null");
    }
}
