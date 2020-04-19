package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.RevokeSharedLinkErrorException */
public class RevokeSharedLinkErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RevokeSharedLinkError errorValue;

    public RevokeSharedLinkErrorException(String str, String str2, LocalizedText localizedText, RevokeSharedLinkError revokeSharedLinkError) {
        super(str2, localizedText, buildMessage(str, localizedText, revokeSharedLinkError));
        if (revokeSharedLinkError != null) {
            this.errorValue = revokeSharedLinkError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
