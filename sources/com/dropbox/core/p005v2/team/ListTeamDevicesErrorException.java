package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ListTeamDevicesErrorException */
public class ListTeamDevicesErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListTeamDevicesError errorValue;

    public ListTeamDevicesErrorException(String str, String str2, LocalizedText localizedText, ListTeamDevicesError listTeamDevicesError) {
        super(str2, localizedText, buildMessage(str, localizedText, listTeamDevicesError));
        if (listTeamDevicesError != null) {
            this.errorValue = listTeamDevicesError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
