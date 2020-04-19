package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersTransferFormerMembersFilesErrorException */
public class MembersTransferFormerMembersFilesErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersTransferFormerMembersFilesError errorValue;

    public MembersTransferFormerMembersFilesErrorException(String str, String str2, LocalizedText localizedText, MembersTransferFormerMembersFilesError membersTransferFormerMembersFilesError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersTransferFormerMembersFilesError));
        if (membersTransferFormerMembersFilesError != null) {
            this.errorValue = membersTransferFormerMembersFilesError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
