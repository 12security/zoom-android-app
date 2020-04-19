package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.paper.PaperDocUpdateErrorException */
public class PaperDocUpdateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final PaperDocUpdateError errorValue;

    public PaperDocUpdateErrorException(String str, String str2, LocalizedText localizedText, PaperDocUpdateError paperDocUpdateError) {
        super(str2, localizedText, buildMessage(str, localizedText, paperDocUpdateError));
        if (paperDocUpdateError != null) {
            this.errorValue = paperDocUpdateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
