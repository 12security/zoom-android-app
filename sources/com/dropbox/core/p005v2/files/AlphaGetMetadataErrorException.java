package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataErrorException */
public class AlphaGetMetadataErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final AlphaGetMetadataError errorValue;

    public AlphaGetMetadataErrorException(String str, String str2, LocalizedText localizedText, AlphaGetMetadataError alphaGetMetadataError) {
        super(str2, localizedText, buildMessage(str, localizedText, alphaGetMetadataError));
        if (alphaGetMetadataError != null) {
            this.errorValue = alphaGetMetadataError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
