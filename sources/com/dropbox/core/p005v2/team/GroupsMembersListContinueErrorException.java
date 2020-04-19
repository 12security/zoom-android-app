package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupsMembersListContinueErrorException */
public class GroupsMembersListContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupsMembersListContinueError errorValue;

    public GroupsMembersListContinueErrorException(String str, String str2, LocalizedText localizedText, GroupsMembersListContinueError groupsMembersListContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupsMembersListContinueError));
        if (groupsMembersListContinueError != null) {
            this.errorValue = groupsMembersListContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
