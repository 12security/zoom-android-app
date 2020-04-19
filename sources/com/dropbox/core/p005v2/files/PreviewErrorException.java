package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.PreviewErrorException */
public class PreviewErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final PreviewError errorValue;

    public PreviewErrorException(String str, String str2, LocalizedText localizedText, PreviewError previewError) {
        super(str2, localizedText, buildMessage(str, localizedText, previewError));
        if (previewError != null) {
            this.errorValue = previewError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
