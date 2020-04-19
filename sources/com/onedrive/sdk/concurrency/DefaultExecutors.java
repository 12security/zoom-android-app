package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.logger.ILogger;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DefaultExecutors implements IExecutors {
    private final ThreadPoolExecutor mBackgroundExecutor = ((ThreadPoolExecutor) Executors.newCachedThreadPool());
    private final SynchronousExecutor mForegroundExecutor = new SynchronousExecutor();
    private final ILogger mLogger;

    public DefaultExecutors(ILogger iLogger) {
        this.mLogger = iLogger;
    }

    public void performOnBackground(Runnable runnable) {
        ILogger iLogger = this.mLogger;
        StringBuilder sb = new StringBuilder();
        sb.append("Starting background task, current active count: ");
        sb.append(this.mBackgroundExecutor.getActiveCount());
        iLogger.logDebug(sb.toString());
        this.mBackgroundExecutor.execute(runnable);
    }

    public <Result> void performOnForeground(final Result result, final ICallback<Result> iCallback) {
        ILogger iLogger = this.mLogger;
        StringBuilder sb = new StringBuilder();
        sb.append("Starting foreground task, current active count:");
        sb.append(this.mForegroundExecutor.getActiveCount());
        sb.append(", with result ");
        sb.append(result);
        iLogger.logDebug(sb.toString());
        this.mForegroundExecutor.execute(new Runnable() {
            public void run() {
                iCallback.success(result);
            }
        });
    }

    public <Result> void performOnForeground(final int i, final int i2, final IProgressCallback<Result> iProgressCallback) {
        ILogger iLogger = this.mLogger;
        StringBuilder sb = new StringBuilder();
        sb.append("Starting foreground task, current active count:");
        sb.append(this.mForegroundExecutor.getActiveCount());
        sb.append(", with progress  ");
        sb.append(i);
        sb.append(", max progress");
        sb.append(i2);
        iLogger.logDebug(sb.toString());
        this.mForegroundExecutor.execute(new Runnable() {
            public void run() {
                iProgressCallback.progress((long) i, (long) i2);
            }
        });
    }

    public <Result> void performOnForeground(final ClientException clientException, final ICallback<Result> iCallback) {
        ILogger iLogger = this.mLogger;
        StringBuilder sb = new StringBuilder();
        sb.append("Starting foreground task, current active count:");
        sb.append(this.mForegroundExecutor.getActiveCount());
        sb.append(", with exception ");
        sb.append(clientException);
        iLogger.logDebug(sb.toString());
        this.mForegroundExecutor.execute(new Runnable() {
            public void run() {
                iCallback.failure(clientException);
            }
        });
    }
}
