package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.SaveUrlErrorException */
public class SaveUrlErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final SaveUrlError errorValue;

    public SaveUrlErrorException(String str, String str2, LocalizedText localizedText, SaveUrlError saveUrlError) {
        super(str2, localizedText, buildMessage(str, localizedText, saveUrlError));
        if (saveUrlError != null) {
            this.errorValue = saveUrlError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
