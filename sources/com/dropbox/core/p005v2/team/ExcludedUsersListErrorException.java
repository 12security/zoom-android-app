package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersListErrorException */
public class ExcludedUsersListErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ExcludedUsersListError errorValue;

    public ExcludedUsersListErrorException(String str, String str2, LocalizedText localizedText, ExcludedUsersListError excludedUsersListError) {
        super(str2, localizedText, buildMessage(str, localizedText, excludedUsersListError));
        if (excludedUsersListError != null) {
            this.errorValue = excludedUsersListError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
