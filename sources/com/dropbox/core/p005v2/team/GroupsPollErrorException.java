package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.GroupsPollErrorException */
public class GroupsPollErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GroupsPollError errorValue;

    public GroupsPollErrorException(String str, String str2, LocalizedText localizedText, GroupsPollError groupsPollError) {
        super(str2, localizedText, buildMessage(str, localizedText, groupsPollError));
        if (groupsPollError != null) {
            this.errorValue = groupsPollError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
