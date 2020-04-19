package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupsGetInfoErrorException */
public class GroupsGetInfoErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupsGetInfoError errorValue;

    public GroupsGetInfoErrorException(String str, String str2, LocalizedText localizedText, GroupsGetInfoError groupsGetInfoError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupsGetInfoError));
        if (groupsGetInfoError != null) {
            this.errorValue = groupsGetInfoError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
