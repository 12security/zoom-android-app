package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.core.ClientException;

public interface IExecutors {
    void performOnBackground(Runnable runnable);

    <Result> void performOnForeground(int i, int i2, IProgressCallback<Result> iProgressCallback);

    <Result> void performOnForeground(ClientException clientException, ICallback<Result> iCallback);

    <Result> void performOnForeground(Result result, ICallback<Result> iCallback);
}
