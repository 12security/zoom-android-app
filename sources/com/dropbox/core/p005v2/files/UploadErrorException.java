package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.UploadErrorException */
public class UploadErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UploadError errorValue;

    public UploadErrorException(String str, String str2, LocalizedText localizedText, UploadError uploadError) {
        super(str2, localizedText, buildMessage(str, localizedText, uploadError));
        if (uploadError != null) {
            this.errorValue = uploadError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
