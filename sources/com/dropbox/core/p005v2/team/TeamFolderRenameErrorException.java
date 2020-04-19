package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamFolderRenameErrorException */
public class TeamFolderRenameErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamFolderRenameError errorValue;

    public TeamFolderRenameErrorException(String str, String str2, LocalizedText localizedText, TeamFolderRenameError teamFolderRenameError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamFolderRenameError));
        if (teamFolderRenameError != null) {
            this.errorValue = teamFolderRenameError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
