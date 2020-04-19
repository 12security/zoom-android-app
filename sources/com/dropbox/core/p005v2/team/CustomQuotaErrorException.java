package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.CustomQuotaErrorException */
public class CustomQuotaErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final CustomQuotaError errorValue;

    public CustomQuotaErrorException(String str, String str2, LocalizedText localizedText, CustomQuotaError customQuotaError) {
        super(str2, localizedText, buildMessage(str, localizedText, customQuotaError));
        if (customQuotaError != null) {
            this.errorValue = customQuotaError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
