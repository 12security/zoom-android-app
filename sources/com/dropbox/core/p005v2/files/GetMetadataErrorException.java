package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.GetMetadataErrorException */
public class GetMetadataErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetMetadataError errorValue;

    public GetMetadataErrorException(String str, String str2, LocalizedText localizedText, GetMetadataError getMetadataError) {
        super(str2, localizedText, buildMessage(str, localizedText, getMetadataError));
        if (getMetadataError != null) {
            this.errorValue = getMetadataError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
