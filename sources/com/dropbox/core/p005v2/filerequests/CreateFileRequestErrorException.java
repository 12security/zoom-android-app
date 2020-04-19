package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.filerequests.CreateFileRequestErrorException */
public class CreateFileRequestErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final CreateFileRequestError errorValue;

    public CreateFileRequestErrorException(String str, String str2, LocalizedText localizedText, CreateFileRequestError createFileRequestError) {
        super(str2, localizedText, buildMessage(str, localizedText, createFileRequestError));
        if (createFileRequestError != null) {
            this.errorValue = createFileRequestError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
