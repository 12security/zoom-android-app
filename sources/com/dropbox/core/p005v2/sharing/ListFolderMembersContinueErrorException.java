package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersContinueErrorException */
public class ListFolderMembersContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFolderMembersContinueError errorValue;

    public ListFolderMembersContinueErrorException(String str, String str2, LocalizedText localizedText, ListFolderMembersContinueError listFolderMembersContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFolderMembersContinueError));
        if (listFolderMembersContinueError != null) {
            this.errorValue = listFolderMembersContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
