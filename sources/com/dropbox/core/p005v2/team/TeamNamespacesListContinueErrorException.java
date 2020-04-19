package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamNamespacesListContinueErrorException */
public class TeamNamespacesListContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamNamespacesListContinueError errorValue;

    public TeamNamespacesListContinueErrorException(String str, String str2, LocalizedText localizedText, TeamNamespacesListContinueError teamNamespacesListContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamNamespacesListContinueError));
        if (teamNamespacesListContinueError != null) {
            this.errorValue = teamNamespacesListContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
