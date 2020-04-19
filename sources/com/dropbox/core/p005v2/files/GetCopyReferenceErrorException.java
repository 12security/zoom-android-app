package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.GetCopyReferenceErrorException */
public class GetCopyReferenceErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetCopyReferenceError errorValue;

    public GetCopyReferenceErrorException(String str, String str2, LocalizedText localizedText, GetCopyReferenceError getCopyReferenceError) {
        super(str2, localizedText, buildMessage(str, localizedText, getCopyReferenceError));
        if (getCopyReferenceError != null) {
            this.errorValue = getCopyReferenceError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
