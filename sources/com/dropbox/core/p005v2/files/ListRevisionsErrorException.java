package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.ListRevisionsErrorException */
public class ListRevisionsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListRevisionsError errorValue;

    public ListRevisionsErrorException(String str, String str2, LocalizedText localizedText, ListRevisionsError listRevisionsError) {
        super(str2, localizedText, buildMessage(str, localizedText, listRevisionsError));
        if (listRevisionsError != null) {
            this.errorValue = listRevisionsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
