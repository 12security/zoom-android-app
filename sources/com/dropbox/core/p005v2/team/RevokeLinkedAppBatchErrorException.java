package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppBatchErrorException */
public class RevokeLinkedAppBatchErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RevokeLinkedAppBatchError errorValue;

    public RevokeLinkedAppBatchErrorException(String str, String str2, LocalizedText localizedText, RevokeLinkedAppBatchError revokeLinkedAppBatchError) {
        super(str2, localizedText, buildMessage(str, localizedText, revokeLinkedAppBatchError));
        if (revokeLinkedAppBatchError != null) {
            this.errorValue = revokeLinkedAppBatchError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
