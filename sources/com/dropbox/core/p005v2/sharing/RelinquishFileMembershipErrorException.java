package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.RelinquishFileMembershipErrorException */
public class RelinquishFileMembershipErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RelinquishFileMembershipError errorValue;

    public RelinquishFileMembershipErrorException(String str, String str2, LocalizedText localizedText, RelinquishFileMembershipError relinquishFileMembershipError) {
        super(str2, localizedText, buildMessage(str, localizedText, relinquishFileMembershipError));
        if (relinquishFileMembershipError != null) {
            this.errorValue = relinquishFileMembershipError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
