package com.onedrive.sdk.concurrency;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronousExecutor implements Executor {
    /* access modifiers changed from: private */
    public AtomicInteger mActiveCount = new AtomicInteger(0);

    public void execute(@NonNull final Runnable runnable) {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                SynchronousExecutor.this.mActiveCount.incrementAndGet();
                runnable.run();
                SynchronousExecutor.this.mActiveCount.decrementAndGet();
            }
        }.execute(new Void[0]);
    }

    public int getActiveCount() {
        return this.mActiveCount.get();
    }
}
