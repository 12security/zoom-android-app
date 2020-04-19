package com.zipow.videobox;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import com.zipow.videobox.util.ZMUtils;
import p021us.zipow.mdm.ZMMdmManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.thirdparty.common.ZMThirdPartyUtils;

public class ZoomApplication extends Application {
    /* access modifiers changed from: private */
    public int mActivityCount = 0;
    private volatile boolean mInitFailed = false;

    public boolean isAtFront() {
        return this.mActivityCount > 0;
    }

    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            public void onActivityDestroyed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityCreated(Activity activity, Bundle bundle) {
                ZMActivity.setHasActivityCreated(true);
            }

            public void onActivityStarted(Activity activity) {
                ZoomApplication.this.mActivityCount = ZoomApplication.this.mActivityCount + 1;
            }

            public void onActivityStopped(Activity activity) {
                ZoomApplication.this.mActivityCount = ZoomApplication.this.mActivityCount - 1;
            }
        });
        try {
            VideoBoxApplication.initialize(this, false);
            if (VideoBoxApplication.getInstance().isPTApp()) {
                ZMThirdPartyUtils.checkShareCloudFileClientInfo(this, ZMUtils.isZoomApp(this));
            }
            if (OsUtil.isAtLeastP()) {
                WebView.setDataDirectorySuffix(getProcessName());
            }
            if (OsUtil.isAtLeastL()) {
                ZMMdmManager.getInstance().registerRestrictionChangesReceiver(this);
            }
        } catch (UnsatisfiedLinkError unused) {
            this.mInitFailed = true;
        }
    }

    public boolean isInitFailed() {
        return this.mInitFailed;
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        try {
            Class cls = Class.forName("androidx.multidex.MultiDex");
            cls.getMethod("install", new Class[]{Context.class}).invoke(cls, new Object[]{this});
        } catch (Exception unused) {
        }
    }

    public void onTerminate() {
        super.onTerminate();
        VideoBoxApplication.getInstance().onApplicationTerminated();
    }
}
