package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.fileproperties.AddPropertiesErrorException */
public class AddPropertiesErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final AddPropertiesError errorValue;

    public AddPropertiesErrorException(String str, String str2, LocalizedText localizedText, AddPropertiesError addPropertiesError) {
        super(str2, localizedText, buildMessage(str, localizedText, addPropertiesError));
        if (addPropertiesError != null) {
            this.errorValue = addPropertiesError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
