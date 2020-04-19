package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.GetSharedLinksErrorException */
public class GetSharedLinksErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetSharedLinksError errorValue;

    public GetSharedLinksErrorException(String str, String str2, LocalizedText localizedText, GetSharedLinksError getSharedLinksError) {
        super(str2, localizedText, buildMessage(str, localizedText, getSharedLinksError));
        if (getSharedLinksError != null) {
            this.errorValue = getSharedLinksError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
