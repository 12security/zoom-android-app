package com.onedrive.sdk.core;

public class ClientException extends RuntimeException {
    private final OneDriveErrorCodes mErrorCode;

    public ClientException(String str, Throwable th, OneDriveErrorCodes oneDriveErrorCodes) {
        super(str, th);
        this.mErrorCode = oneDriveErrorCodes;
    }

    public boolean isError(OneDriveErrorCodes oneDriveErrorCodes) {
        return this.mErrorCode == oneDriveErrorCodes;
    }
}
