package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.fileproperties.RemovePropertiesErrorException */
public class RemovePropertiesErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RemovePropertiesError errorValue;

    public RemovePropertiesErrorException(String str, String str2, LocalizedText localizedText, RemovePropertiesError removePropertiesError) {
        super(str2, localizedText, buildMessage(str, localizedText, removePropertiesError));
        if (removePropertiesError != null) {
            this.errorValue = removePropertiesError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
