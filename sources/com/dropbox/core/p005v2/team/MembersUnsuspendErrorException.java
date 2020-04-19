package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersUnsuspendErrorException */
public class MembersUnsuspendErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersUnsuspendError errorValue;

    public MembersUnsuspendErrorException(String str, String str2, LocalizedText localizedText, MembersUnsuspendError membersUnsuspendError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersUnsuspendError));
        if (membersUnsuspendError != null) {
            this.errorValue = membersUnsuspendError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
