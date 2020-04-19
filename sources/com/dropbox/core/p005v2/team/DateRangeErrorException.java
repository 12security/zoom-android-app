package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.DateRangeErrorException */
public class DateRangeErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final DateRangeError errorValue;

    public DateRangeErrorException(String str, String str2, LocalizedText localizedText, DateRangeError dateRangeError) {
        super(str2, localizedText, buildMessage(str, localizedText, dateRangeError));
        if (dateRangeError != null) {
            this.errorValue = dateRangeError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
