package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.filerequests.GetFileRequestErrorException */
public class GetFileRequestErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetFileRequestError errorValue;

    public GetFileRequestErrorException(String str, String str2, LocalizedText localizedText, GetFileRequestError getFileRequestError) {
        super(str2, localizedText, buildMessage(str, localizedText, getFileRequestError));
        if (getFileRequestError != null) {
            this.errorValue = getFileRequestError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
