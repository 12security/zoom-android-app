package com.onedrive.sdk.authentication;

import android.app.Activity;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.logger.ILogger;

class DisambiguationRequest {
    /* access modifiers changed from: private */
    public final Activity mActivity;
    private final ICallback<DisambiguationResponse> mCallback;
    private final ILogger mLogger;

    public DisambiguationRequest(Activity activity, ICallback<DisambiguationResponse> iCallback, ILogger iLogger) {
        this.mActivity = activity;
        this.mCallback = iCallback;
        this.mLogger = iLogger;
    }

    public void execute() {
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                new DisambiguationDialog(DisambiguationRequest.this.mActivity, DisambiguationRequest.this).show();
            }
        });
    }

    public ICallback<DisambiguationResponse> getCallback() {
        return this.mCallback;
    }

    public ILogger getLogger() {
        return this.mLogger;
    }
}
