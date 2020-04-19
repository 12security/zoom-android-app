package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ListFoldersContinueErrorException */
public class ListFoldersContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFoldersContinueError errorValue;

    public ListFoldersContinueErrorException(String str, String str2, LocalizedText localizedText, ListFoldersContinueError listFoldersContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFoldersContinueError));
        if (listFoldersContinueError != null) {
            this.errorValue = listFoldersContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
