package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersGetInfoErrorException */
public class MembersGetInfoErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersGetInfoError errorValue;

    public MembersGetInfoErrorException(String str, String str2, LocalizedText localizedText, MembersGetInfoError membersGetInfoError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersGetInfoError));
        if (membersGetInfoError != null) {
            this.errorValue = membersGetInfoError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
