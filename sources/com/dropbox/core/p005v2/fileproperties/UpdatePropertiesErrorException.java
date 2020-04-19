package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.fileproperties.UpdatePropertiesErrorException */
public class UpdatePropertiesErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UpdatePropertiesError errorValue;

    public UpdatePropertiesErrorException(String str, String str2, LocalizedText localizedText, UpdatePropertiesError updatePropertiesError) {
        super(str2, localizedText, buildMessage(str, localizedText, updatePropertiesError));
        if (updatePropertiesError != null) {
            this.errorValue = updatePropertiesError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
