package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.paper.DocLookupErrorException */
public class DocLookupErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final DocLookupError errorValue;

    public DocLookupErrorException(String str, String str2, LocalizedText localizedText, DocLookupError docLookupError) {
        super(str2, localizedText, buildMessage(str, localizedText, docLookupError));
        if (docLookupError != null) {
            this.errorValue = docLookupError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
