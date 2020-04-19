package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.UploadSessionLookupErrorException */
public class UploadSessionLookupErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UploadSessionLookupError errorValue;

    public UploadSessionLookupErrorException(String str, String str2, LocalizedText localizedText, UploadSessionLookupError uploadSessionLookupError) {
        super(str2, localizedText, buildMessage(str, localizedText, uploadSessionLookupError));
        if (uploadSessionLookupError != null) {
            this.errorValue = uploadSessionLookupError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
