package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersListContinueErrorException */
public class MembersListContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersListContinueError errorValue;

    public MembersListContinueErrorException(String str, String str2, LocalizedText localizedText, MembersListContinueError membersListContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersListContinueError));
        if (membersListContinueError != null) {
            this.errorValue = membersListContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
