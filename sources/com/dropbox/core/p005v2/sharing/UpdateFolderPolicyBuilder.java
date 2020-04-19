package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.UpdateFolderPolicyArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyBuilder */
public class UpdateFolderPolicyBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    UpdateFolderPolicyBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public UpdateFolderPolicyBuilder withMemberPolicy(MemberPolicy memberPolicy) {
        this._builder.withMemberPolicy(memberPolicy);
        return this;
    }

    public UpdateFolderPolicyBuilder withAclUpdatePolicy(AclUpdatePolicy aclUpdatePolicy) {
        this._builder.withAclUpdatePolicy(aclUpdatePolicy);
        return this;
    }

    public UpdateFolderPolicyBuilder withViewerInfoPolicy(ViewerInfoPolicy viewerInfoPolicy) {
        this._builder.withViewerInfoPolicy(viewerInfoPolicy);
        return this;
    }

    public UpdateFolderPolicyBuilder withSharedLinkPolicy(SharedLinkPolicy sharedLinkPolicy) {
        this._builder.withSharedLinkPolicy(sharedLinkPolicy);
        return this;
    }

    public UpdateFolderPolicyBuilder withLinkSettings(LinkSettings linkSettings) {
        this._builder.withLinkSettings(linkSettings);
        return this;
    }

    public UpdateFolderPolicyBuilder withActions(List<FolderAction> list) {
        this._builder.withActions(list);
        return this;
    }

    public SharedFolderMetadata start() throws UpdateFolderPolicyErrorException, DbxException {
        return this._client.updateFolderPolicy(this._builder.build());
    }
}
