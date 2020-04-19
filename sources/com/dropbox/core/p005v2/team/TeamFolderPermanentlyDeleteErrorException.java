package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamFolderPermanentlyDeleteErrorException */
public class TeamFolderPermanentlyDeleteErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamFolderPermanentlyDeleteError errorValue;

    public TeamFolderPermanentlyDeleteErrorException(String str, String str2, LocalizedText localizedText, TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamFolderPermanentlyDeleteError));
        if (teamFolderPermanentlyDeleteError != null) {
            this.errorValue = teamFolderPermanentlyDeleteError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
