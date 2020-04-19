package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupDeleteErrorException */
public class GroupDeleteErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupDeleteError errorValue;

    public GroupDeleteErrorException(String str, String str2, LocalizedText localizedText, GroupDeleteError groupDeleteError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupDeleteError));
        if (groupDeleteError != null) {
            this.errorValue = groupDeleteError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
