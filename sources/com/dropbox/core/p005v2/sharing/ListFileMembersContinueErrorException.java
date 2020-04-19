package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersContinueErrorException */
public class ListFileMembersContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFileMembersContinueError errorValue;

    public ListFileMembersContinueErrorException(String str, String str2, LocalizedText localizedText, ListFileMembersContinueError listFileMembersContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFileMembersContinueError));
        if (listFileMembersContinueError != null) {
            this.errorValue = listFileMembersContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
