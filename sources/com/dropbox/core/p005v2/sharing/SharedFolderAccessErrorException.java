package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.SharedFolderAccessErrorException */
public class SharedFolderAccessErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final SharedFolderAccessError errorValue;

    public SharedFolderAccessErrorException(String str, String str2, LocalizedText localizedText, SharedFolderAccessError sharedFolderAccessError) {
        super(str2, localizedText, buildMessage(str, localizedText, sharedFolderAccessError));
        if (sharedFolderAccessError != null) {
            this.errorValue = sharedFolderAccessError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
