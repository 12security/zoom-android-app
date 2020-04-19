package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class ListenerCallQueue<L> implements Runnable {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final Executor executor;
    @GuardedBy("this")
    private boolean isThreadScheduled;
    private final L listener;
    @GuardedBy("this")
    private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();

    static abstract class Callback<L> {
        /* access modifiers changed from: private */
        public final String methodCall;

        /* access modifiers changed from: 0000 */
        public abstract void call(L l);

        Callback(String str) {
            this.methodCall = str;
        }

        /* access modifiers changed from: 0000 */
        public void enqueueOn(Iterable<ListenerCallQueue<L>> iterable) {
            for (ListenerCallQueue add : iterable) {
                add.add(this);
            }
        }
    }

    ListenerCallQueue(L l, Executor executor2) {
        this.listener = Preconditions.checkNotNull(l);
        this.executor = (Executor) Preconditions.checkNotNull(executor2);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void add(Callback<L> callback) {
        this.waitQueue.add(callback);
    }

    /* access modifiers changed from: 0000 */
    public void execute() {
        boolean z;
        synchronized (this) {
            z = true;
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
            } else {
                z = false;
            }
        }
        if (z) {
            try {
                this.executor.execute(this);
            } catch (RuntimeException e) {
                synchronized (this) {
                    this.isThreadScheduled = false;
                    Logger logger2 = logger;
                    Level level = Level.SEVERE;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Exception while running callbacks for ");
                    sb.append(this.listener);
                    sb.append(" on ");
                    sb.append(this.executor);
                    logger2.log(level, sb.toString(), e);
                    throw e;
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r2.call(r8.listener);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r8 = this;
        L_0x0000:
            r0 = 0
            r1 = 1
            monitor-enter(r8)     // Catch:{ all -> 0x004c }
            boolean r2 = r8.isThreadScheduled     // Catch:{ all -> 0x0049 }
            com.google.common.base.Preconditions.checkState(r2)     // Catch:{ all -> 0x0049 }
            java.util.Queue<com.google.common.util.concurrent.ListenerCallQueue$Callback<L>> r2 = r8.waitQueue     // Catch:{ all -> 0x0049 }
            java.lang.Object r2 = r2.poll()     // Catch:{ all -> 0x0049 }
            com.google.common.util.concurrent.ListenerCallQueue$Callback r2 = (com.google.common.util.concurrent.ListenerCallQueue.Callback) r2     // Catch:{ all -> 0x0049 }
            if (r2 != 0) goto L_0x001a
            r8.isThreadScheduled = r0     // Catch:{ all -> 0x0049 }
            monitor-exit(r8)     // Catch:{ all -> 0x0016 }
            return
        L_0x0016:
            r1 = move-exception
            r2 = r1
            r1 = 0
            goto L_0x004a
        L_0x001a:
            monitor-exit(r8)     // Catch:{ all -> 0x0049 }
            L r3 = r8.listener     // Catch:{ RuntimeException -> 0x0021 }
            r2.call(r3)     // Catch:{ RuntimeException -> 0x0021 }
            goto L_0x0000
        L_0x0021:
            r3 = move-exception
            java.util.logging.Logger r4 = logger     // Catch:{ all -> 0x004c }
            java.util.logging.Level r5 = java.util.logging.Level.SEVERE     // Catch:{ all -> 0x004c }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x004c }
            r6.<init>()     // Catch:{ all -> 0x004c }
            java.lang.String r7 = "Exception while executing callback: "
            r6.append(r7)     // Catch:{ all -> 0x004c }
            L r7 = r8.listener     // Catch:{ all -> 0x004c }
            r6.append(r7)     // Catch:{ all -> 0x004c }
            java.lang.String r7 = "."
            r6.append(r7)     // Catch:{ all -> 0x004c }
            java.lang.String r2 = r2.methodCall     // Catch:{ all -> 0x004c }
            r6.append(r2)     // Catch:{ all -> 0x004c }
            java.lang.String r2 = r6.toString()     // Catch:{ all -> 0x004c }
            r4.log(r5, r2, r3)     // Catch:{ all -> 0x004c }
            goto L_0x0000
        L_0x0049:
            r2 = move-exception
        L_0x004a:
            monitor-exit(r8)     // Catch:{ all -> 0x0049 }
            throw r2     // Catch:{ all -> 0x004c }
        L_0x004c:
            r2 = move-exception
            if (r1 == 0) goto L_0x0057
            monitor-enter(r8)
            r8.isThreadScheduled = r0     // Catch:{ all -> 0x0054 }
            monitor-exit(r8)     // Catch:{ all -> 0x0054 }
            goto L_0x0057
        L_0x0054:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0054 }
            throw r0
        L_0x0057:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ListenerCallQueue.run():void");
    }
}
