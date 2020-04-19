package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsErrorException */
public class TeamFolderUpdateSyncSettingsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TeamFolderUpdateSyncSettingsError errorValue;

    public TeamFolderUpdateSyncSettingsErrorException(String str, String str2, LocalizedText localizedText, TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError) {
        super(str2, localizedText, buildMessage(str, localizedText, teamFolderUpdateSyncSettingsError));
        if (teamFolderUpdateSyncSettingsError != null) {
            this.errorValue = teamFolderUpdateSyncSettingsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
