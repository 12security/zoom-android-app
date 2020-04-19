package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersSuspendErrorException */
public class MembersSuspendErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersSuspendError errorValue;

    public MembersSuspendErrorException(String str, String str2, LocalizedText localizedText, MembersSuspendError membersSuspendError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersSuspendError));
        if (membersSuspendError != null) {
            this.errorValue = membersSuspendError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
