package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupMemberSetAccessTypeErrorException */
public class GroupMemberSetAccessTypeErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupMemberSetAccessTypeError errorValue;

    public GroupMemberSetAccessTypeErrorException(String str, String str2, LocalizedText localizedText, GroupMemberSetAccessTypeError groupMemberSetAccessTypeError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupMemberSetAccessTypeError));
        if (groupMemberSetAccessTypeError != null) {
            this.errorValue = groupMemberSetAccessTypeError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
