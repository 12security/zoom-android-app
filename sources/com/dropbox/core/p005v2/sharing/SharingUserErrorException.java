package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.SharingUserErrorException */
public class SharingUserErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final SharingUserError errorValue;

    public SharingUserErrorException(String str, String str2, LocalizedText localizedText, SharingUserError sharingUserError) {
        super(str2, localizedText, buildMessage(str, localizedText, sharingUserError));
        if (sharingUserError != null) {
            this.errorValue = sharingUserError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
