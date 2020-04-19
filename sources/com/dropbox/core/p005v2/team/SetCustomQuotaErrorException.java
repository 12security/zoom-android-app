package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.SetCustomQuotaErrorException */
public class SetCustomQuotaErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final SetCustomQuotaError errorValue;

    public SetCustomQuotaErrorException(String str, String str2, LocalizedText localizedText, SetCustomQuotaError setCustomQuotaError) {
        super(str2, localizedText, buildMessage(str, localizedText, setCustomQuotaError));
        if (setCustomQuotaError != null) {
            this.errorValue = setCustomQuotaError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
