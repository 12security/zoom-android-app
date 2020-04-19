package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsContinueErrorException */
public class GetTeamEventsContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetTeamEventsContinueError errorValue;

    public GetTeamEventsContinueErrorException(String str, String str2, LocalizedText localizedText, GetTeamEventsContinueError getTeamEventsContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, getTeamEventsContinueError));
        if (getTeamEventsContinueError != null) {
            this.errorValue = getTeamEventsContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
