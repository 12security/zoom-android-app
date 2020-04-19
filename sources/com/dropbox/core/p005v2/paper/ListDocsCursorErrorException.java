package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.paper.ListDocsCursorErrorException */
public class ListDocsCursorErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListDocsCursorError errorValue;

    public ListDocsCursorErrorException(String str, String str2, LocalizedText localizedText, ListDocsCursorError listDocsCursorError) {
        super(str2, localizedText, buildMessage(str, localizedText, listDocsCursorError));
        if (listDocsCursorError != null) {
            this.errorValue = listDocsCursorError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
