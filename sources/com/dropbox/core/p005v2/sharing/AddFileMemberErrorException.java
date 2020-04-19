package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.AddFileMemberErrorException */
public class AddFileMemberErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final AddFileMemberError errorValue;

    public AddFileMemberErrorException(String str, String str2, LocalizedText localizedText, AddFileMemberError addFileMemberError) {
        super(str2, localizedText, buildMessage(str, localizedText, addFileMemberError));
        if (addFileMemberError != null) {
            this.errorValue = addFileMemberError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
