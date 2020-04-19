package com.dropbox.core.p005v2;

import com.dropbox.core.p005v2.auth.DbxUserAuthRequests;
import com.dropbox.core.p005v2.fileproperties.DbxUserFilePropertiesRequests;
import com.dropbox.core.p005v2.filerequests.DbxUserFileRequestsRequests;
import com.dropbox.core.p005v2.files.DbxUserFilesRequests;
import com.dropbox.core.p005v2.paper.DbxUserPaperRequests;
import com.dropbox.core.p005v2.sharing.DbxUserSharingRequests;
import com.dropbox.core.p005v2.users.DbxUserUsersRequests;

/* renamed from: com.dropbox.core.v2.DbxClientV2Base */
public class DbxClientV2Base {
    protected final DbxRawClientV2 _client;
    private final DbxUserAuthRequests auth;
    private final DbxUserFilePropertiesRequests fileProperties;
    private final DbxUserFileRequestsRequests fileRequests;
    private final DbxUserFilesRequests files;
    private final DbxUserPaperRequests paper;
    private final DbxUserSharingRequests sharing;
    private final DbxUserUsersRequests users;

    protected DbxClientV2Base(DbxRawClientV2 dbxRawClientV2) {
        this._client = dbxRawClientV2;
        this.auth = new DbxUserAuthRequests(dbxRawClientV2);
        this.fileProperties = new DbxUserFilePropertiesRequests(dbxRawClientV2);
        this.fileRequests = new DbxUserFileRequestsRequests(dbxRawClientV2);
        this.files = new DbxUserFilesRequests(dbxRawClientV2);
        this.paper = new DbxUserPaperRequests(dbxRawClientV2);
        this.sharing = new DbxUserSharingRequests(dbxRawClientV2);
        this.users = new DbxUserUsersRequests(dbxRawClientV2);
    }

    public DbxUserAuthRequests auth() {
        return this.auth;
    }

    public DbxUserFilePropertiesRequests fileProperties() {
        return this.fileProperties;
    }

    public DbxUserFileRequestsRequests fileRequests() {
        return this.fileRequests;
    }

    public DbxUserFilesRequests files() {
        return this.files;
    }

    public DbxUserPaperRequests paper() {
        return this.paper;
    }

    public DbxUserSharingRequests sharing() {
        return this.sharing;
    }

    public DbxUserUsersRequests users() {
        return this.users;
    }
}
