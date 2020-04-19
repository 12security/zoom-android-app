package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.CreateFolderErrorException */
public class CreateFolderErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final CreateFolderError errorValue;

    public CreateFolderErrorException(String str, String str2, LocalizedText localizedText, CreateFolderError createFolderError) {
        super(str2, localizedText, buildMessage(str, localizedText, createFolderError));
        if (createFolderError != null) {
            this.errorValue = createFolderError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
