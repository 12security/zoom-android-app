package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.fileproperties.InvalidPropertyGroupErrorException */
public class InvalidPropertyGroupErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final InvalidPropertyGroupError errorValue;

    public InvalidPropertyGroupErrorException(String str, String str2, LocalizedText localizedText, InvalidPropertyGroupError invalidPropertyGroupError) {
        super(str2, localizedText, buildMessage(str, localizedText, invalidPropertyGroupError));
        if (invalidPropertyGroupError != null) {
            this.errorValue = invalidPropertyGroupError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
