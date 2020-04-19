package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupUpdateErrorException */
public class GroupUpdateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupUpdateError errorValue;

    public GroupUpdateErrorException(String str, String str2, LocalizedText localizedText, GroupUpdateError groupUpdateError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupUpdateError));
        if (groupUpdateError != null) {
            this.errorValue = groupUpdateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
