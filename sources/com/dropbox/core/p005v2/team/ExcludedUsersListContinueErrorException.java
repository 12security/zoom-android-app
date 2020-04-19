package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersListContinueErrorException */
public class ExcludedUsersListContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ExcludedUsersListContinueError errorValue;

    public ExcludedUsersListContinueErrorException(String str, String str2, LocalizedText localizedText, ExcludedUsersListContinueError excludedUsersListContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, excludedUsersListContinueError));
        if (excludedUsersListContinueError != null) {
            this.errorValue = excludedUsersListContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
