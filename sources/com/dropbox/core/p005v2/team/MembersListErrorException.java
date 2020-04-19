package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersListErrorException */
public class MembersListErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersListError errorValue;

    public MembersListErrorException(String str, String str2, LocalizedText localizedText, MembersListError membersListError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersListError));
        if (membersListError != null) {
            this.errorValue = membersListError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
