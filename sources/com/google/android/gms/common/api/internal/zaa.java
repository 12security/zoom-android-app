package com.google.android.gms.common.api.internal;

import android.app.Activity;
import androidx.annotation.MainThread;
import androidx.annotation.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class zaa extends ActivityLifecycleObserver {
    private final WeakReference<C4563zaa> zack;

    @VisibleForTesting(otherwise = 2)
    /* renamed from: com.google.android.gms.common.api.internal.zaa$zaa reason: collision with other inner class name */
    static class C4563zaa extends LifecycleCallback {
        private List<Runnable> zacl = new ArrayList();

        /* access modifiers changed from: private */
        public static C4563zaa zaa(Activity activity) {
            C4563zaa zaa;
            synchronized (activity) {
                LifecycleFragment fragment = getFragment(activity);
                zaa = (C4563zaa) fragment.getCallbackOrNull("LifecycleObserverOnStop", C4563zaa.class);
                if (zaa == null) {
                    zaa = new C4563zaa(fragment);
                }
            }
            return zaa;
        }

        private C4563zaa(LifecycleFragment lifecycleFragment) {
            super(lifecycleFragment);
            this.mLifecycleFragment.addCallback("LifecycleObserverOnStop", this);
        }

        /* access modifiers changed from: private */
        public final synchronized void zaa(Runnable runnable) {
            this.zacl.add(runnable);
        }

        @MainThread
        public void onStop() {
            List<Runnable> list;
            synchronized (this) {
                list = this.zacl;
                this.zacl = new ArrayList();
            }
            for (Runnable run : list) {
                run.run();
            }
        }
    }

    public zaa(Activity activity) {
        this(C4563zaa.zaa(activity));
    }

    @VisibleForTesting(otherwise = 2)
    private zaa(C4563zaa zaa) {
        this.zack = new WeakReference<>(zaa);
    }

    public final ActivityLifecycleObserver onStopCallOnce(Runnable runnable) {
        C4563zaa zaa = (C4563zaa) this.zack.get();
        if (zaa != null) {
            zaa.zaa(runnable);
            return this;
        }
        throw new IllegalStateException("The target activity has already been GC'd");
    }
}
