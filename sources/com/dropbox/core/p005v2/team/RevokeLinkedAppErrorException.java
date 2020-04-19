package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppErrorException */
public class RevokeLinkedAppErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RevokeLinkedAppError errorValue;

    public RevokeLinkedAppErrorException(String str, String str2, LocalizedText localizedText, RevokeLinkedAppError revokeLinkedAppError) {
        super(str2, localizedText, buildMessage(str, localizedText, revokeLinkedAppError));
        if (revokeLinkedAppError != null) {
            this.errorValue = revokeLinkedAppError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
