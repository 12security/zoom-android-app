package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsErrorException */
public class GetTeamEventsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetTeamEventsError errorValue;

    public GetTeamEventsErrorException(String str, String str2, LocalizedText localizedText, GetTeamEventsError getTeamEventsError) {
        super(str2, localizedText, buildMessage(str, localizedText, getTeamEventsError));
        if (getTeamEventsError != null) {
            this.errorValue = getTeamEventsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
