package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.UploadSessionFinishErrorException */
public class UploadSessionFinishErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UploadSessionFinishError errorValue;

    public UploadSessionFinishErrorException(String str, String str2, LocalizedText localizedText, UploadSessionFinishError uploadSessionFinishError) {
        super(str2, localizedText, buildMessage(str, localizedText, uploadSessionFinishError));
        if (uploadSessionFinishError != null) {
            this.errorValue = uploadSessionFinishError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
