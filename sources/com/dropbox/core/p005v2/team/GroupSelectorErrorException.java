package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupSelectorErrorException */
public class GroupSelectorErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupSelectorError errorValue;

    public GroupSelectorErrorException(String str, String str2, LocalizedText localizedText, GroupSelectorError groupSelectorError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupSelectorError));
        if (groupSelectorError != null) {
            this.errorValue = groupSelectorError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
