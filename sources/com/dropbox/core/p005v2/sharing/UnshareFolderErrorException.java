package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.UnshareFolderErrorException */
public class UnshareFolderErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UnshareFolderError errorValue;

    public UnshareFolderErrorException(String str, String str2, LocalizedText localizedText, UnshareFolderError unshareFolderError) {
        super(str2, localizedText, buildMessage(str, localizedText, unshareFolderError));
        if (unshareFolderError != null) {
            this.errorValue = unshareFolderError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
