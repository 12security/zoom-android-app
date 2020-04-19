package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.TransferFolderErrorException */
public class TransferFolderErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final TransferFolderError errorValue;

    public TransferFolderErrorException(String str, String str2, LocalizedText localizedText, TransferFolderError transferFolderError) {
        super(str2, localizedText, buildMessage(str, localizedText, transferFolderError));
        if (transferFolderError != null) {
            this.errorValue = transferFolderError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
