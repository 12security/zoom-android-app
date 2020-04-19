package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersRecoverErrorException */
public class MembersRecoverErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersRecoverError errorValue;

    public MembersRecoverErrorException(String str, String str2, LocalizedText localizedText, MembersRecoverError membersRecoverError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersRecoverError));
        if (membersRecoverError != null) {
            this.errorValue = membersRecoverError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
