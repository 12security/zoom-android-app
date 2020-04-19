package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.ListFolderLongpollErrorException */
public class ListFolderLongpollErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFolderLongpollError errorValue;

    public ListFolderLongpollErrorException(String str, String str2, LocalizedText localizedText, ListFolderLongpollError listFolderLongpollError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFolderLongpollError));
        if (listFolderLongpollError != null) {
            this.errorValue = listFolderLongpollError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
