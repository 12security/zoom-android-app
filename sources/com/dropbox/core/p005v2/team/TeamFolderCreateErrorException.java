package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamFolderCreateErrorException */
public class TeamFolderCreateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamFolderCreateError errorValue;

    public TeamFolderCreateErrorException(String str, String str2, LocalizedText localizedText, TeamFolderCreateError teamFolderCreateError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamFolderCreateError));
        if (teamFolderCreateError != null) {
            this.errorValue = teamFolderCreateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
