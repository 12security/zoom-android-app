package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersSetProfileErrorException */
public class MembersSetProfileErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersSetProfileError errorValue;

    public MembersSetProfileErrorException(String str, String str2, LocalizedText localizedText, MembersSetProfileError membersSetProfileError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersSetProfileError));
        if (membersSetProfileError != null) {
            this.errorValue = membersSetProfileError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
