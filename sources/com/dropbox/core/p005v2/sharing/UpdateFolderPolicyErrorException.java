package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyErrorException */
public class UpdateFolderPolicyErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final UpdateFolderPolicyError errorValue;

    public UpdateFolderPolicyErrorException(String str, String str2, LocalizedText localizedText, UpdateFolderPolicyError updateFolderPolicyError) {
        super(str2, localizedText, buildMessage(str, localizedText, updateFolderPolicyError));
        if (updateFolderPolicyError != null) {
            this.errorValue = updateFolderPolicyError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
