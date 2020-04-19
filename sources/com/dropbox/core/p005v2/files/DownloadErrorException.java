package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.DownloadErrorException */
public class DownloadErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final DownloadError errorValue;

    public DownloadErrorException(String str, String str2, LocalizedText localizedText, DownloadError downloadError) {
        super(str2, localizedText, buildMessage(str, localizedText, downloadError));
        if (downloadError != null) {
            this.errorValue = downloadError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
