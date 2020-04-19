package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.core.ClientException;

public interface ICallback<Result> {
    void failure(ClientException clientException);

    void success(Result result);
}
