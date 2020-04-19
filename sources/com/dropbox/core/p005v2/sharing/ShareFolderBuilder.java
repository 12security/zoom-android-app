package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.ShareFolderArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ShareFolderBuilder */
public class ShareFolderBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    ShareFolderBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public ShareFolderBuilder withAclUpdatePolicy(AclUpdatePolicy aclUpdatePolicy) {
        this._builder.withAclUpdatePolicy(aclUpdatePolicy);
        return this;
    }

    public ShareFolderBuilder withForceAsync(Boolean bool) {
        this._builder.withForceAsync(bool);
        return this;
    }

    public ShareFolderBuilder withMemberPolicy(MemberPolicy memberPolicy) {
        this._builder.withMemberPolicy(memberPolicy);
        return this;
    }

    public ShareFolderBuilder withSharedLinkPolicy(SharedLinkPolicy sharedLinkPolicy) {
        this._builder.withSharedLinkPolicy(sharedLinkPolicy);
        return this;
    }

    public ShareFolderBuilder withViewerInfoPolicy(ViewerInfoPolicy viewerInfoPolicy) {
        this._builder.withViewerInfoPolicy(viewerInfoPolicy);
        return this;
    }

    public ShareFolderBuilder withAccessInheritance(AccessInheritance accessInheritance) {
        this._builder.withAccessInheritance(accessInheritance);
        return this;
    }

    public ShareFolderBuilder withActions(List<FolderAction> list) {
        this._builder.withActions(list);
        return this;
    }

    public ShareFolderBuilder withLinkSettings(LinkSettings linkSettings) {
        this._builder.withLinkSettings(linkSettings);
        return this;
    }

    public ShareFolderLaunch start() throws ShareFolderErrorException, DbxException {
        return this._client.shareFolder(this._builder.build());
    }
}
