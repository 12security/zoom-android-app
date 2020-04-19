package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamFolderListContinueErrorException */
public class TeamFolderListContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamFolderListContinueError errorValue;

    public TeamFolderListContinueErrorException(String str, String str2, LocalizedText localizedText, TeamFolderListContinueError teamFolderListContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamFolderListContinueError));
        if (teamFolderListContinueError != null) {
            this.errorValue = teamFolderListContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
