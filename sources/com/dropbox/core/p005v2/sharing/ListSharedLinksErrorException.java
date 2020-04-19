package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksErrorException */
public class ListSharedLinksErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListSharedLinksError errorValue;

    public ListSharedLinksErrorException(String str, String str2, LocalizedText localizedText, ListSharedLinksError listSharedLinksError) {
        super(str2, localizedText, buildMessage(str, localizedText, listSharedLinksError));
        if (listSharedLinksError != null) {
            this.errorValue = listSharedLinksError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
