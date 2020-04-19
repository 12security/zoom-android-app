package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.UpdateFolderMemberErrorException */
public class UpdateFolderMemberErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UpdateFolderMemberError errorValue;

    public UpdateFolderMemberErrorException(String str, String str2, LocalizedText localizedText, UpdateFolderMemberError updateFolderMemberError) {
        super(str2, localizedText, buildMessage(str, localizedText, updateFolderMemberError));
        if (updateFolderMemberError != null) {
            this.errorValue = updateFolderMemberError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
