package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkErrorException */
public class CreateSharedLinkErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final CreateSharedLinkError errorValue;

    public CreateSharedLinkErrorException(String str, String str2, LocalizedText localizedText, CreateSharedLinkError createSharedLinkError) {
        super(str2, localizedText, buildMessage(str, localizedText, createSharedLinkError));
        if (createSharedLinkError != null) {
            this.errorValue = createSharedLinkError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
