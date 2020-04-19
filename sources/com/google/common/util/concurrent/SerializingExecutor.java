package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class SerializingExecutor implements Executor {
    /* access modifiers changed from: private */
    public static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
    private final Executor executor;
    /* access modifiers changed from: private */
    public final Object internalLock = new Object();
    /* access modifiers changed from: private */
    @GuardedBy("internalLock")
    public boolean isWorkerRunning = false;
    /* access modifiers changed from: private */
    @GuardedBy("internalLock")
    public final Deque<Runnable> queue = new ArrayDeque();
    /* access modifiers changed from: private */
    @GuardedBy("internalLock")
    public int suspensions = 0;

    private final class QueueWorker implements Runnable {
        private QueueWorker() {
        }

        public void run() {
            try {
                workOnQueue();
            } catch (Error e) {
                synchronized (SerializingExecutor.this.internalLock) {
                    SerializingExecutor.this.isWorkerRunning = false;
                    throw e;
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
            r0.run();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x002b, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
            r2 = com.google.common.util.concurrent.SerializingExecutor.access$500();
            r3 = java.util.logging.Level.SEVERE;
            r4 = new java.lang.StringBuilder();
            r4.append("Exception while executing runnable ");
            r4.append(r0);
            r2.log(r3, r4.toString(), r1);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void workOnQueue() {
            /*
                r6 = this;
            L_0x0000:
                r0 = 0
                com.google.common.util.concurrent.SerializingExecutor r1 = com.google.common.util.concurrent.SerializingExecutor.this
                java.lang.Object r1 = r1.internalLock
                monitor-enter(r1)
                com.google.common.util.concurrent.SerializingExecutor r2 = com.google.common.util.concurrent.SerializingExecutor.this     // Catch:{ all -> 0x0047 }
                int r2 = r2.suspensions     // Catch:{ all -> 0x0047 }
                if (r2 != 0) goto L_0x001c
                com.google.common.util.concurrent.SerializingExecutor r0 = com.google.common.util.concurrent.SerializingExecutor.this     // Catch:{ all -> 0x0047 }
                java.util.Deque r0 = r0.queue     // Catch:{ all -> 0x0047 }
                java.lang.Object r0 = r0.poll()     // Catch:{ all -> 0x0047 }
                java.lang.Runnable r0 = (java.lang.Runnable) r0     // Catch:{ all -> 0x0047 }
            L_0x001c:
                if (r0 != 0) goto L_0x0026
                com.google.common.util.concurrent.SerializingExecutor r0 = com.google.common.util.concurrent.SerializingExecutor.this     // Catch:{ all -> 0x0047 }
                r2 = 0
                r0.isWorkerRunning = r2     // Catch:{ all -> 0x0047 }
                monitor-exit(r1)     // Catch:{ all -> 0x0047 }
                return
            L_0x0026:
                monitor-exit(r1)     // Catch:{ all -> 0x0047 }
                r0.run()     // Catch:{ RuntimeException -> 0x002b }
                goto L_0x0000
            L_0x002b:
                r1 = move-exception
                java.util.logging.Logger r2 = com.google.common.util.concurrent.SerializingExecutor.log
                java.util.logging.Level r3 = java.util.logging.Level.SEVERE
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Exception while executing runnable "
                r4.append(r5)
                r4.append(r0)
                java.lang.String r0 = r4.toString()
                r2.log(r3, r0, r1)
                goto L_0x0000
            L_0x0047:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0047 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.SerializingExecutor.QueueWorker.workOnQueue():void");
        }
    }

    public SerializingExecutor(Executor executor2) {
        this.executor = (Executor) Preconditions.checkNotNull(executor2);
    }

    public void execute(Runnable runnable) {
        synchronized (this.internalLock) {
            this.queue.add(runnable);
        }
        startQueueWorker();
    }

    public void executeFirst(Runnable runnable) {
        synchronized (this.internalLock) {
            this.queue.addFirst(runnable);
        }
        startQueueWorker();
    }

    public void suspend() {
        synchronized (this.internalLock) {
            this.suspensions++;
        }
    }

    public void resume() {
        synchronized (this.internalLock) {
            Preconditions.checkState(this.suspensions > 0);
            this.suspensions--;
        }
        startQueueWorker();
    }

    private void startQueueWorker() {
        synchronized (this.internalLock) {
            if (this.queue.peek() != null) {
                if (this.suspensions <= 0) {
                    if (!this.isWorkerRunning) {
                        this.isWorkerRunning = true;
                        try {
                            this.executor.execute(new QueueWorker());
                        } catch (Throwable th) {
                            synchronized (this.internalLock) {
                                this.isWorkerRunning = false;
                                throw th;
                            }
                        }
                    }
                }
            }
        }
    }
}
