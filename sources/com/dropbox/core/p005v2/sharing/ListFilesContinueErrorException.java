package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ListFilesContinueErrorException */
public class ListFilesContinueErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListFilesContinueError errorValue;

    public ListFilesContinueErrorException(String str, String str2, LocalizedText localizedText, ListFilesContinueError listFilesContinueError) {
        super(str2, localizedText, buildMessage(str, localizedText, listFilesContinueError));
        if (listFilesContinueError != null) {
            this.errorValue = listFilesContinueError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
