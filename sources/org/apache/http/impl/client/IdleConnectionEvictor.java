package org.apache.http.impl.client;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.util.Args;

public final class IdleConnectionEvictor {
    private final HttpClientConnectionManager connectionManager;
    /* access modifiers changed from: private */
    public volatile Exception exception;
    /* access modifiers changed from: private */
    public final long maxIdleTimeMs;
    /* access modifiers changed from: private */
    public final long sleepTimeMs;
    private final Thread thread;
    private final ThreadFactory threadFactory;

    static class DefaultThreadFactory implements ThreadFactory {
        DefaultThreadFactory() {
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "Connection evictor");
            thread.setDaemon(true);
            return thread;
        }
    }

    public IdleConnectionEvictor(final HttpClientConnectionManager httpClientConnectionManager, ThreadFactory threadFactory2, long j, TimeUnit timeUnit, long j2, TimeUnit timeUnit2) {
        this.connectionManager = (HttpClientConnectionManager) Args.notNull(httpClientConnectionManager, "Connection manager");
        if (threadFactory2 == null) {
            threadFactory2 = new DefaultThreadFactory();
        }
        this.threadFactory = threadFactory2;
        if (timeUnit != null) {
            j = timeUnit.toMillis(j);
        }
        this.sleepTimeMs = j;
        if (timeUnit2 != null) {
            j2 = timeUnit2.toMillis(j2);
        }
        this.maxIdleTimeMs = j2;
        this.thread = this.threadFactory.newThread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(IdleConnectionEvictor.this.sleepTimeMs);
                        httpClientConnectionManager.closeExpiredConnections();
                        if (IdleConnectionEvictor.this.maxIdleTimeMs > 0) {
                            httpClientConnectionManager.closeIdleConnections(IdleConnectionEvictor.this.maxIdleTimeMs, TimeUnit.MILLISECONDS);
                        }
                    } catch (Exception e) {
                        IdleConnectionEvictor.this.exception = e;
                        return;
                    }
                }
            }
        });
    }

    public IdleConnectionEvictor(HttpClientConnectionManager httpClientConnectionManager, long j, TimeUnit timeUnit, long j2, TimeUnit timeUnit2) {
        this(httpClientConnectionManager, null, j, timeUnit, j2, timeUnit2);
    }

    public IdleConnectionEvictor(HttpClientConnectionManager httpClientConnectionManager, long j, TimeUnit timeUnit) {
        this(httpClientConnectionManager, null, j > 0 ? j : 5, timeUnit != null ? timeUnit : TimeUnit.SECONDS, j, timeUnit);
    }

    public void start() {
        this.thread.start();
    }

    public void shutdown() {
        this.thread.interrupt();
    }

    public boolean isRunning() {
        return this.thread.isAlive();
    }

    public void awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
        Thread thread2 = this.thread;
        if (timeUnit == null) {
            timeUnit = TimeUnit.MILLISECONDS;
        }
        thread2.join(timeUnit.toMillis(j));
    }
}
