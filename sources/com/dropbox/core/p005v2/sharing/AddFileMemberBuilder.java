package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.AddFileMemberArgs.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.AddFileMemberBuilder */
public class AddFileMemberBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    AddFileMemberBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
        if (dbxUserSharingRequests != null) {
            this._client = dbxUserSharingRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public AddFileMemberBuilder withCustomMessage(String str) {
        this._builder.withCustomMessage(str);
        return this;
    }

    public AddFileMemberBuilder withQuiet(Boolean bool) {
        this._builder.withQuiet(bool);
        return this;
    }

    public AddFileMemberBuilder withAccessLevel(AccessLevel accessLevel) {
        this._builder.withAccessLevel(accessLevel);
        return this;
    }

    public AddFileMemberBuilder withAddMessageAsComment(Boolean bool) {
        this._builder.withAddMessageAsComment(bool);
        return this;
    }

    public List<FileMemberActionResult> start() throws AddFileMemberErrorException, DbxException {
        return this._client.addFileMember(this._builder.build());
    }
}
