package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.ModifySharedLinkSettingsErrorException */
public class ModifySharedLinkSettingsErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ModifySharedLinkSettingsError errorValue;

    public ModifySharedLinkSettingsErrorException(String str, String str2, LocalizedText localizedText, ModifySharedLinkSettingsError modifySharedLinkSettingsError) {
        super(str2, localizedText, buildMessage(str, localizedText, modifySharedLinkSettingsError));
        if (modifySharedLinkSettingsError != null) {
            this.errorValue = modifySharedLinkSettingsError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
