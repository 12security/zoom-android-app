package com.onedrive.sdk.authentication;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;

public class ClientAuthenticatorException extends ClientException {
    public ClientAuthenticatorException(String str, Throwable th, OneDriveErrorCodes oneDriveErrorCodes) {
        super(str, th, oneDriveErrorCodes);
    }

    public ClientAuthenticatorException(String str, OneDriveErrorCodes oneDriveErrorCodes) {
        this(str, null, oneDriveErrorCodes);
    }
}
