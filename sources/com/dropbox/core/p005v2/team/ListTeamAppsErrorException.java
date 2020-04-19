package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ListTeamAppsErrorException */
public class ListTeamAppsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListTeamAppsError errorValue;

    public ListTeamAppsErrorException(String str, String str2, LocalizedText localizedText, ListTeamAppsError listTeamAppsError) {
        super(str2, localizedText, buildMessage(str, localizedText, listTeamAppsError));
        if (listTeamAppsError != null) {
            this.errorValue = listTeamAppsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
