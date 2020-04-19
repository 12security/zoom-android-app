package com.microsoft.aad.adal;

public class ResourceAuthenticationChallengeException extends AuthenticationException {
    static final long serialVersionUID = 1;

    public ResourceAuthenticationChallengeException(String str) {
        super(ADALError.RESOURCE_AUTHENTICATION_CHALLENGE_FAILURE, str);
    }
}
