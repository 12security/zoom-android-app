package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataErrorException */
public class GetFileMetadataErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetFileMetadataError errorValue;

    public GetFileMetadataErrorException(String str, String str2, LocalizedText localizedText, GetFileMetadataError getFileMetadataError) {
        super(str2, localizedText, buildMessage(str, localizedText, getFileMetadataError));
        if (getFileMetadataError != null) {
            this.errorValue = getFileMetadataError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
