package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.DeleteErrorException */
public class DeleteErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final DeleteError errorValue;

    public DeleteErrorException(String str, String str2, LocalizedText localizedText, DeleteError deleteError) {
        super(str2, localizedText, buildMessage(str, localizedText, deleteError));
        if (deleteError != null) {
            this.errorValue = deleteError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
