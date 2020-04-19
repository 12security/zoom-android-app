package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.sharing.SetAccessInheritanceErrorException */
public class SetAccessInheritanceErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final SetAccessInheritanceError errorValue;

    public SetAccessInheritanceErrorException(String str, String str2, LocalizedText localizedText, SetAccessInheritanceError setAccessInheritanceError) {
        super(str2, localizedText, buildMessage(str, localizedText, setAccessInheritanceError));
        if (setAccessInheritanceError != null) {
            this.errorValue = setAccessInheritanceError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
