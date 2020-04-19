package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.DownloadZipErrorException */
public class DownloadZipErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final DownloadZipError errorValue;

    public DownloadZipErrorException(String str, String str2, LocalizedText localizedText, DownloadZipError downloadZipError) {
        super(str2, localizedText, buildMessage(str, localizedText, downloadZipError));
        if (downloadZipError != null) {
            this.errorValue = downloadZipError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
