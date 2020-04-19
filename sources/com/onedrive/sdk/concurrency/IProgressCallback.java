package com.onedrive.sdk.concurrency;

public interface IProgressCallback<Result> extends ICallback<Result> {
    void progress(long j, long j2);
}
