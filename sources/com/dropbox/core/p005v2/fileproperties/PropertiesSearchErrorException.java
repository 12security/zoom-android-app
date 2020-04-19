package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchErrorException */
public class PropertiesSearchErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final PropertiesSearchError errorValue;

    public PropertiesSearchErrorException(String str, String str2, LocalizedText localizedText, PropertiesSearchError propertiesSearchError) {
        super(str2, localizedText, buildMessage(str, localizedText, propertiesSearchError));
        if (propertiesSearchError != null) {
            this.errorValue = propertiesSearchError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
