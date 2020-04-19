package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.RelocationErrorException */
public class RelocationErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RelocationError errorValue;

    public RelocationErrorException(String str, String str2, LocalizedText localizedText, RelocationError relocationError) {
        super(str2, localizedText, buildMessage(str, localizedText, relocationError));
        if (relocationError != null) {
            this.errorValue = relocationError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
