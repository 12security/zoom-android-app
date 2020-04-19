package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersSetPermissionsErrorException */
public class MembersSetPermissionsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersSetPermissionsError errorValue;

    public MembersSetPermissionsErrorException(String str, String str2, LocalizedText localizedText, MembersSetPermissionsError membersSetPermissionsError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersSetPermissionsError));
        if (membersSetPermissionsError != null) {
            this.errorValue = membersSetPermissionsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
