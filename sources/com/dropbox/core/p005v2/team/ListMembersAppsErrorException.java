package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ListMembersAppsErrorException */
public class ListMembersAppsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListMembersAppsError errorValue;

    public ListMembersAppsErrorException(String str, String str2, LocalizedText localizedText, ListMembersAppsError listMembersAppsError) {
        super(str2, localizedText, buildMessage(str, localizedText, listMembersAppsError));
        if (listMembersAppsError != null) {
            this.errorValue = listMembersAppsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
