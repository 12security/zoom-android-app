package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ListMemberAppsErrorException */
public class ListMemberAppsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListMemberAppsError errorValue;

    public ListMemberAppsErrorException(String str, String str2, LocalizedText localizedText, ListMemberAppsError listMemberAppsError) {
        super(str2, localizedText, buildMessage(str, localizedText, listMemberAppsError));
        if (listMemberAppsError != null) {
            this.errorValue = listMemberAppsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
