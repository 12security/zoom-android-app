package com.dropbox.core.p005v2;

import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxRequestUtil;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.p005v2.common.PathRoot;
import java.util.List;

/* renamed from: com.dropbox.core.v2.DbxAppClientV2 */
public class DbxAppClientV2 extends DbxAppClientV2Base {

    /* renamed from: com.dropbox.core.v2.DbxAppClientV2$DbxAppRawClientV2 */
    private static final class DbxAppRawClientV2 extends DbxRawClientV2 {
        private final String key;
        private final String secret;

        private DbxAppRawClientV2(DbxRequestConfig dbxRequestConfig, String str, String str2, DbxHost dbxHost, String str3) {
            super(dbxRequestConfig, dbxHost, str3, null);
            this.key = str;
            this.secret = str2;
        }

        /* access modifiers changed from: protected */
        public void addAuthHeaders(List<Header> list) {
            DbxRequestUtil.addBasicAuthHeader(list, this.key, this.secret);
        }

        /* access modifiers changed from: protected */
        public DbxRawClientV2 withPathRoot(PathRoot pathRoot) {
            throw new UnsupportedOperationException("App endpoints don't support Dropbox-API-Path-Root header.");
        }
    }

    public DbxAppClientV2(DbxRequestConfig dbxRequestConfig, String str, String str2) {
        this(dbxRequestConfig, str, str2, DbxHost.DEFAULT);
    }

    public DbxAppClientV2(DbxRequestConfig dbxRequestConfig, String str, String str2, DbxHost dbxHost) {
        DbxAppRawClientV2 dbxAppRawClientV2 = new DbxAppRawClientV2(dbxRequestConfig, str, str2, dbxHost, null);
        super(dbxAppRawClientV2);
    }

    public DbxAppClientV2(DbxRequestConfig dbxRequestConfig, String str, String str2, DbxHost dbxHost, String str3) {
        DbxAppRawClientV2 dbxAppRawClientV2 = new DbxAppRawClientV2(dbxRequestConfig, str, str2, dbxHost, str3);
        super(dbxAppRawClientV2);
    }
}
