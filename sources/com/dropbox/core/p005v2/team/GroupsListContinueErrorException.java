package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupsListContinueErrorException */
public class GroupsListContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupsListContinueError errorValue;

    public GroupsListContinueErrorException(String str, String str2, LocalizedText localizedText, GroupsListContinueError groupsListContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupsListContinueError));
        if (groupsListContinueError != null) {
            this.errorValue = groupsListContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
