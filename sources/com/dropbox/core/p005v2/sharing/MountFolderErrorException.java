package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.MountFolderErrorException */
public class MountFolderErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MountFolderError errorValue;

    public MountFolderErrorException(String str, String str2, LocalizedText localizedText, MountFolderError mountFolderError) {
        super(str2, localizedText, buildMessage(str, localizedText, mountFolderError));
        if (mountFolderError != null) {
            this.errorValue = mountFolderError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
