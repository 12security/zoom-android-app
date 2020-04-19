package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersErrorException */
public class ListFileMembersErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFileMembersError errorValue;

    public ListFileMembersErrorException(String str, String str2, LocalizedText localizedText, ListFileMembersError listFileMembersError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFileMembersError));
        if (listFileMembersError != null) {
            this.errorValue = listFileMembersError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
