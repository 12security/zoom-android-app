package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.RemoveFileMemberErrorException */
public class RemoveFileMemberErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RemoveFileMemberError errorValue;

    public RemoveFileMemberErrorException(String str, String str2, LocalizedText localizedText, RemoveFileMemberError removeFileMemberError) {
        super(str2, localizedText, buildMessage(str, localizedText, removeFileMemberError));
        if (removeFileMemberError != null) {
            this.errorValue = removeFileMemberError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
