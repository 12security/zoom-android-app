package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.UploadErrorWithPropertiesException */
public class UploadErrorWithPropertiesException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UploadErrorWithProperties errorValue;

    public UploadErrorWithPropertiesException(String str, String str2, LocalizedText localizedText, UploadErrorWithProperties uploadErrorWithProperties) {
        super(str2, localizedText, buildMessage(str, localizedText, uploadErrorWithProperties));
        if (uploadErrorWithProperties != null) {
            this.errorValue = uploadErrorWithProperties;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
