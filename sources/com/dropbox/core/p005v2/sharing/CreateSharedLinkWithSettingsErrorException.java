package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException */
public class CreateSharedLinkWithSettingsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final CreateSharedLinkWithSettingsError errorValue;

    public CreateSharedLinkWithSettingsErrorException(String str, String str2, LocalizedText localizedText, CreateSharedLinkWithSettingsError createSharedLinkWithSettingsError) {
        super(str2, localizedText, buildMessage(str, localizedText, createSharedLinkWithSettingsError));
        if (createSharedLinkWithSettingsError != null) {
            this.errorValue = createSharedLinkWithSettingsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
