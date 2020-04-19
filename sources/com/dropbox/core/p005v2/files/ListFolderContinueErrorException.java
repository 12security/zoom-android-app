package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.ListFolderContinueErrorException */
public class ListFolderContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFolderContinueError errorValue;

    public ListFolderContinueErrorException(String str, String str2, LocalizedText localizedText, ListFolderContinueError listFolderContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFolderContinueError));
        if (listFolderContinueError != null) {
            this.errorValue = listFolderContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
