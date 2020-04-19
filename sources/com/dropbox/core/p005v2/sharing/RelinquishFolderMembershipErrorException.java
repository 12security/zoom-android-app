package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.RelinquishFolderMembershipErrorException */
public class RelinquishFolderMembershipErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final RelinquishFolderMembershipError errorValue;

    public RelinquishFolderMembershipErrorException(String str, String str2, LocalizedText localizedText, RelinquishFolderMembershipError relinquishFolderMembershipError) {
        super(str2, localizedText, buildMessage(str, localizedText, relinquishFolderMembershipError));
        if (relinquishFolderMembershipError != null) {
            this.errorValue = relinquishFolderMembershipError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
