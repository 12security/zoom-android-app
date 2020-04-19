package com.dropbox.core.p005v2.async;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.async.PollErrorException */
public class PollErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final PollError errorValue;

    public PollErrorException(String str, String str2, LocalizedText localizedText, PollError pollError) {
        super(str2, localizedText, buildMessage(str, localizedText, pollError));
        if (pollError != null) {
            this.errorValue = pollError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
