package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.UnmountFolderErrorException */
public class UnmountFolderErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UnmountFolderError errorValue;

    public UnmountFolderErrorException(String str, String str2, LocalizedText localizedText, UnmountFolderError unmountFolderError) {
        super(str2, localizedText, buildMessage(str, localizedText, unmountFolderError));
        if (unmountFolderError != null) {
            this.errorValue = unmountFolderError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
