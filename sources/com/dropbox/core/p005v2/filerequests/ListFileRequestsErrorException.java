package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.filerequests.ListFileRequestsErrorException */
public class ListFileRequestsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFileRequestsError errorValue;

    public ListFileRequestsErrorException(String str, String str2, LocalizedText localizedText, ListFileRequestsError listFileRequestsError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFileRequestsError));
        if (listFileRequestsError != null) {
            this.errorValue = listFileRequestsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
