package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.paper.PaperDocCreateErrorException */
public class PaperDocCreateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final PaperDocCreateError errorValue;

    public PaperDocCreateErrorException(String str, String str2, LocalizedText localizedText, PaperDocCreateError paperDocCreateError) {
        super(str2, localizedText, buildMessage(str, localizedText, paperDocCreateError));
        if (paperDocCreateError != null) {
            this.errorValue = paperDocCreateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
