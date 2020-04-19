package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.fileproperties.TemplateErrorException */
public class TemplateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TemplateError errorValue;

    public TemplateErrorException(String str, String str2, LocalizedText localizedText, TemplateError templateError) {
        super(str2, localizedText, buildMessage(str, localizedText, templateError));
        if (templateError != null) {
            this.errorValue = templateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
