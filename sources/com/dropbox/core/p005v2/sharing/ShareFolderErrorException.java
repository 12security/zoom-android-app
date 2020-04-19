package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ShareFolderErrorException */
public class ShareFolderErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ShareFolderError errorValue;

    public ShareFolderErrorException(String str, String str2, LocalizedText localizedText, ShareFolderError shareFolderError) {
        super(str2, localizedText, buildMessage(str, localizedText, shareFolderError));
        if (shareFolderError != null) {
            this.errorValue = shareFolderError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
