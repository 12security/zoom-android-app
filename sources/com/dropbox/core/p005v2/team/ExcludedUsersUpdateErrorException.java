package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateErrorException */
public class ExcludedUsersUpdateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ExcludedUsersUpdateError errorValue;

    public ExcludedUsersUpdateErrorException(String str, String str2, LocalizedText localizedText, ExcludedUsersUpdateError excludedUsersUpdateError) {
        super(str2, localizedText, buildMessage(str, localizedText, excludedUsersUpdateError));
        if (excludedUsersUpdateError != null) {
            this.errorValue = excludedUsersUpdateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
