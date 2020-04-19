package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.GetTemporaryLinkErrorException */
public class GetTemporaryLinkErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetTemporaryLinkError errorValue;

    public GetTemporaryLinkErrorException(String str, String str2, LocalizedText localizedText, GetTemporaryLinkError getTemporaryLinkError) {
        super(str2, localizedText, buildMessage(str, localizedText, getTemporaryLinkError));
        if (getTemporaryLinkError != null) {
            this.errorValue = getTemporaryLinkError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
