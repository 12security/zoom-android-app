package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchErrorException */
public class FeaturesGetValuesBatchErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final FeaturesGetValuesBatchError errorValue;

    public FeaturesGetValuesBatchErrorException(String str, String str2, LocalizedText localizedText, FeaturesGetValuesBatchError featuresGetValuesBatchError) {
        super(str2, localizedText, buildMessage(str, localizedText, featuresGetValuesBatchError));
        if (featuresGetValuesBatchError != null) {
            this.errorValue = featuresGetValuesBatchError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
