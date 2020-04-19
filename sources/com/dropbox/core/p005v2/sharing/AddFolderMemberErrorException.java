package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberErrorException */
public class AddFolderMemberErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final AddFolderMemberError errorValue;

    public AddFolderMemberErrorException(String str, String str2, LocalizedText localizedText, AddFolderMemberError addFolderMemberError) {
        super(str2, localizedText, buildMessage(str, localizedText, addFolderMemberError));
        if (addFolderMemberError != null) {
            this.errorValue = addFolderMemberError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
