package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.paper.ListUsersCursorErrorException */
public class ListUsersCursorErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListUsersCursorError errorValue;

    public ListUsersCursorErrorException(String str, String str2, LocalizedText localizedText, ListUsersCursorError listUsersCursorError) {
        super(str2, localizedText, buildMessage(str, localizedText, listUsersCursorError));
        if (listUsersCursorError != null) {
            this.errorValue = listUsersCursorError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
