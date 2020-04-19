package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.TokenGetAuthenticatedAdminErrorException */
public class TokenGetAuthenticatedAdminErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TokenGetAuthenticatedAdminError errorValue;

    public TokenGetAuthenticatedAdminErrorException(String str, String str2, LocalizedText localizedText, TokenGetAuthenticatedAdminError tokenGetAuthenticatedAdminError) {
        super(str2, localizedText, buildMessage(str, localizedText, tokenGetAuthenticatedAdminError));
        if (tokenGetAuthenticatedAdminError != null) {
            this.errorValue = tokenGetAuthenticatedAdminError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
