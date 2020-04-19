package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.LocalizedText;

/* renamed from: com.dropbox.core.v2.team.MembersSendWelcomeErrorException */
public class MembersSendWelcomeErrorException extends DbxApiException {
    private static final long serialVersionUID = 0;
    public final MembersSendWelcomeError errorValue;

    public MembersSendWelcomeErrorException(String str, String str2, LocalizedText localizedText, MembersSendWelcomeError membersSendWelcomeError) {
        super(str2, localizedText, buildMessage(str, localizedText, membersSendWelcomeError));
        if (membersSendWelcomeError != null) {
            this.errorValue = membersSendWelcomeError;
            return;
        }
        throw new NullPointerException("errorValue");
    }
}
