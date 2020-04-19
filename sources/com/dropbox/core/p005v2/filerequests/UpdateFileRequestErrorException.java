package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.filerequests.UpdateFileRequestErrorException */
public class UpdateFileRequestErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UpdateFileRequestError errorValue;

    public UpdateFileRequestErrorException(String str, String str2, LocalizedText localizedText, UpdateFileRequestError updateFileRequestError) {
        super(str2, localizedText, buildMessage(str, localizedText, updateFileRequestError));
        if (updateFileRequestError != null) {
            this.errorValue = updateFileRequestError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
