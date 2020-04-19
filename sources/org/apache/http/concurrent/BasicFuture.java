package org.apache.http.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.util.Args;

public class BasicFuture<T> implements Future<T>, Cancellable {
    private final FutureCallback<T> callback;
    private volatile boolean cancelled;
    private volatile boolean completed;

    /* renamed from: ex */
    private volatile Exception f477ex;
    private volatile T result;

    public BasicFuture(FutureCallback<T> futureCallback) {
        this.callback = futureCallback;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isDone() {
        return this.completed;
    }

    private T getResult() throws ExecutionException {
        if (this.f477ex != null) {
            throw new ExecutionException(this.f477ex);
        } else if (!this.cancelled) {
            return this.result;
        } else {
            throw new CancellationException();
        }
    }

    public synchronized T get() throws InterruptedException, ExecutionException {
        while (!this.completed) {
            wait();
        }
        return getResult();
    }

    public synchronized T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        long j2;
        Args.notNull(timeUnit, "Time unit");
        long millis = timeUnit.toMillis(j);
        int i = (millis > 0 ? 1 : (millis == 0 ? 0 : -1));
        if (i <= 0) {
            j2 = 0;
        } else {
            j2 = System.currentTimeMillis();
        }
        if (this.completed) {
            return getResult();
        } else if (i > 0) {
            long j3 = millis;
            while (true) {
                wait(j3);
                if (this.completed) {
                    return getResult();
                }
                j3 = millis - (System.currentTimeMillis() - j2);
                if (j3 <= 0) {
                    throw new TimeoutException();
                }
            }
        } else {
            throw new TimeoutException();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        if (r1 == null) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        r1.completed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
        r1 = r2.callback;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean completed(T r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.completed     // Catch:{ all -> 0x0019 }
            if (r0 == 0) goto L_0x0008
            r3 = 0
            monitor-exit(r2)     // Catch:{ all -> 0x0019 }
            return r3
        L_0x0008:
            r0 = 1
            r2.completed = r0     // Catch:{ all -> 0x0019 }
            r2.result = r3     // Catch:{ all -> 0x0019 }
            r2.notifyAll()     // Catch:{ all -> 0x0019 }
            monitor-exit(r2)     // Catch:{ all -> 0x0019 }
            org.apache.http.concurrent.FutureCallback<T> r1 = r2.callback
            if (r1 == 0) goto L_0x0018
            r1.completed(r3)
        L_0x0018:
            return r0
        L_0x0019:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0019 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.concurrent.BasicFuture.completed(java.lang.Object):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        if (r1 == null) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        r1.failed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
        r1 = r2.callback;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean failed(java.lang.Exception r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.completed     // Catch:{ all -> 0x0019 }
            if (r0 == 0) goto L_0x0008
            r3 = 0
            monitor-exit(r2)     // Catch:{ all -> 0x0019 }
            return r3
        L_0x0008:
            r0 = 1
            r2.completed = r0     // Catch:{ all -> 0x0019 }
            r2.f477ex = r3     // Catch:{ all -> 0x0019 }
            r2.notifyAll()     // Catch:{ all -> 0x0019 }
            monitor-exit(r2)     // Catch:{ all -> 0x0019 }
            org.apache.http.concurrent.FutureCallback<T> r1 = r2.callback
            if (r1 == 0) goto L_0x0018
            r1.failed(r3)
        L_0x0018:
            return r0
        L_0x0019:
            r3 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0019 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.concurrent.BasicFuture.failed(java.lang.Exception):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        if (r0 == null) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0015, code lost:
        r0.cancelled();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
        r0 = r1.callback;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cancel(boolean r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r2 = r1.completed     // Catch:{ all -> 0x0019 }
            if (r2 == 0) goto L_0x0008
            r2 = 0
            monitor-exit(r1)     // Catch:{ all -> 0x0019 }
            return r2
        L_0x0008:
            r2 = 1
            r1.completed = r2     // Catch:{ all -> 0x0019 }
            r1.cancelled = r2     // Catch:{ all -> 0x0019 }
            r1.notifyAll()     // Catch:{ all -> 0x0019 }
            monitor-exit(r1)     // Catch:{ all -> 0x0019 }
            org.apache.http.concurrent.FutureCallback<T> r0 = r1.callback
            if (r0 == 0) goto L_0x0018
            r0.cancelled()
        L_0x0018:
            return r2
        L_0x0019:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0019 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.concurrent.BasicFuture.cancel(boolean):boolean");
    }

    public boolean cancel() {
        return cancel(true);
    }
}
