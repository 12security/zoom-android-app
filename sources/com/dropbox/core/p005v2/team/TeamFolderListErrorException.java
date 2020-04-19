package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamFolderListErrorException */
public class TeamFolderListErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamFolderListError errorValue;

    public TeamFolderListErrorException(String str, String str2, LocalizedText localizedText, TeamFolderListError teamFolderListError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamFolderListError));
        if (teamFolderListError != null) {
            this.errorValue = teamFolderListError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
