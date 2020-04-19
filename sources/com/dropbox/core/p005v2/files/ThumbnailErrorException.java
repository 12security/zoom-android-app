package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.ThumbnailErrorException */
public class ThumbnailErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ThumbnailError errorValue;

    public ThumbnailErrorException(String str, String str2, LocalizedText localizedText, ThumbnailError thumbnailError) {
        super(str2, localizedText, buildMessage(str, localizedText, thumbnailError));
        if (thumbnailError != null) {
            this.errorValue = thumbnailError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
