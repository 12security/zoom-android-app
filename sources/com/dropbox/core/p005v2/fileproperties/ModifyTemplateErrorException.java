package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.fileproperties.ModifyTemplateErrorException */
public class ModifyTemplateErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ModifyTemplateError errorValue;

    public ModifyTemplateErrorException(String str, String str2, LocalizedText localizedText, ModifyTemplateError modifyTemplateError) {
        super(str2, localizedText, buildMessage(str, localizedText, modifyTemplateError));
        if (modifyTemplateError != null) {
            this.errorValue = modifyTemplateError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
