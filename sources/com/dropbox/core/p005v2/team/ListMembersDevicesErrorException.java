package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ListMembersDevicesErrorException */
public class ListMembersDevicesErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListMembersDevicesError errorValue;

    public ListMembersDevicesErrorException(String str, String str2, LocalizedText localizedText, ListMembersDevicesError listMembersDevicesError) {
        super(str2, localizedText, buildMessage(str, localizedText, listMembersDevicesError));
        if (listMembersDevicesError != null) {
            this.errorValue = listMembersDevicesError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
