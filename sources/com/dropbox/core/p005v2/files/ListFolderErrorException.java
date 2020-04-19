package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.ListFolderErrorException */
public class ListFolderErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFolderError errorValue;

    public ListFolderErrorException(String str, String str2, LocalizedText localizedText, ListFolderError listFolderError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFolderError));
        if (listFolderError != null) {
            this.errorValue = listFolderError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
