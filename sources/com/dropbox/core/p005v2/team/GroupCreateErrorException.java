package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupCreateErrorException */
public class GroupCreateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupCreateError errorValue;

    public GroupCreateErrorException(String str, String str2, LocalizedText localizedText, GroupCreateError groupCreateError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupCreateError));
        if (groupCreateError != null) {
            this.errorValue = groupCreateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
