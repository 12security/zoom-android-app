package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamFolderActivateErrorException */
public class TeamFolderActivateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamFolderActivateError errorValue;

    public TeamFolderActivateErrorException(String str, String str2, LocalizedText localizedText, TeamFolderActivateError teamFolderActivateError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamFolderActivateError));
        if (teamFolderActivateError != null) {
            this.errorValue = teamFolderActivateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
