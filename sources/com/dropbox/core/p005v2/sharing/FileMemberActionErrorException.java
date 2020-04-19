package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.FileMemberActionErrorException */
public class FileMemberActionErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final FileMemberActionError errorValue;

    public FileMemberActionErrorException(String str, String str2, LocalizedText localizedText, FileMemberActionError fileMemberActionError) {
        super(str2, localizedText, buildMessage(str, localizedText, fileMemberActionError));
        if (fileMemberActionError != null) {
            this.errorValue = fileMemberActionError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
