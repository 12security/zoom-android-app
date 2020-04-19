package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchErrorException */
public class RevokeDeviceSessionBatchErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RevokeDeviceSessionBatchError errorValue;

    public RevokeDeviceSessionBatchErrorException(String str, String str2, LocalizedText localizedText, RevokeDeviceSessionBatchError revokeDeviceSessionBatchError) {
        super(str2, localizedText, buildMessage(str, localizedText, revokeDeviceSessionBatchError));
        if (revokeDeviceSessionBatchError != null) {
            this.errorValue = revokeDeviceSessionBatchError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
