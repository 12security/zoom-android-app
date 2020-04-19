package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkFileErrorException */
public class GetSharedLinkFileErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetSharedLinkFileError errorValue;

    public GetSharedLinkFileErrorException(String str, String str2, LocalizedText localizedText, GetSharedLinkFileError getSharedLinkFileError) {
        super(str2, localizedText, buildMessage(str, localizedText, getSharedLinkFileError));
        if (getSharedLinkFileError != null) {
            this.errorValue = getSharedLinkFileError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
