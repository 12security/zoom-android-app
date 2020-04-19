package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.RemoveFolderMemberErrorException */
public class RemoveFolderMemberErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RemoveFolderMemberError errorValue;

    public RemoveFolderMemberErrorException(String str, String str2, LocalizedText localizedText, RemoveFolderMemberError removeFolderMemberError) {
        super(str2, localizedText, buildMessage(str, localizedText, removeFolderMemberError));
        if (removeFolderMemberError != null) {
            this.errorValue = removeFolderMemberError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
