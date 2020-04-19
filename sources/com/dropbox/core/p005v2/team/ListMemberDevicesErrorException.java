package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.ListMemberDevicesErrorException */
public class ListMemberDevicesErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final ListMemberDevicesError errorValue;

    public ListMemberDevicesErrorException(String str, String str2, LocalizedText localizedText, ListMemberDevicesError listMemberDevicesError) {
        super(str2, localizedText, buildMessage(str, localizedText, listMemberDevicesError));
        if (listMemberDevicesError != null) {
            this.errorValue = listMemberDevicesError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
