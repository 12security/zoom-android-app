package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.SharedLinkErrorException */
public class SharedLinkErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final SharedLinkError errorValue;

    public SharedLinkErrorException(String str, String str2, LocalizedText localizedText, SharedLinkError sharedLinkError) {
        super(str2, localizedText, buildMessage(str, localizedText, sharedLinkError));
        if (sharedLinkError != null) {
            this.errorValue = sharedLinkError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
