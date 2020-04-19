package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupMembersRemoveErrorException */
public class GroupMembersRemoveErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupMembersRemoveError errorValue;

    public GroupMembersRemoveErrorException(String str, String str2, LocalizedText localizedText, GroupMembersRemoveError groupMembersRemoveError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupMembersRemoveError));
        if (groupMembersRemoveError != null) {
            this.errorValue = groupMembersRemoveError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
