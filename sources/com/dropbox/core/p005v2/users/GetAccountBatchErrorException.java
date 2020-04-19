package com.dropbox.core.p005v2.users;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.users.GetAccountBatchErrorException */
public class GetAccountBatchErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final GetAccountBatchError errorValue;

    public GetAccountBatchErrorException(String str, String str2, LocalizedText localizedText, GetAccountBatchError getAccountBatchError) {
        super(str2, localizedText, buildMessage(str, localizedText, getAccountBatchError));
        if (getAccountBatchError != null) {
            this.errorValue = getAccountBatchError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
