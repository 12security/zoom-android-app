package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.UnshareFileErrorException */
public class UnshareFileErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UnshareFileError errorValue;

    public UnshareFileErrorException(String str, String str2, LocalizedText localizedText, UnshareFileError unshareFileError) {
        super(str2, localizedText, buildMessage(str, localizedText, unshareFileError));
        if (unshareFileError != null) {
            this.errorValue = unshareFileError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
