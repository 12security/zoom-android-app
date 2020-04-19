package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupMembersAddErrorException */
public class GroupMembersAddErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupMembersAddError errorValue;

    public GroupMembersAddErrorException(String str, String str2, LocalizedText localizedText, GroupMembersAddError groupMembersAddError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupMembersAddError));
        if (groupMembersAddError != null) {
            this.errorValue = groupMembersAddError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
