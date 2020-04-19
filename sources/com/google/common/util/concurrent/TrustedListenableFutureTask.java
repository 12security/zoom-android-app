package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;

@GwtCompatible
class TrustedListenableFutureTask<V> extends TrustedFuture<V> implements RunnableFuture<V> {
    private TrustedFutureInterruptibleTask task;

    private final class TrustedFutureInterruptibleTask extends InterruptibleTask {
        private final Callable<V> callable;

        TrustedFutureInterruptibleTask(Callable<V> callable2) {
            this.callable = (Callable) Preconditions.checkNotNull(callable2);
        }

        /* access modifiers changed from: 0000 */
        public void runInterruptibly() {
            if (!TrustedListenableFutureTask.this.isDone()) {
                try {
                    TrustedListenableFutureTask.this.set(this.callable.call());
                } catch (Throwable th) {
                    TrustedListenableFutureTask.this.setException(th);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean wasInterrupted() {
            return TrustedListenableFutureTask.this.wasInterrupted();
        }

        public String toString() {
            return this.callable.toString();
        }
    }

    static <V> TrustedListenableFutureTask<V> create(Callable<V> callable) {
        return new TrustedListenableFutureTask<>(callable);
    }

    static <V> TrustedListenableFutureTask<V> create(Runnable runnable, @Nullable V v) {
        return new TrustedListenableFutureTask<>(Executors.callable(runnable, v));
    }

    TrustedListenableFutureTask(Callable<V> callable) {
        this.task = new TrustedFutureInterruptibleTask<>(callable);
    }

    public void run() {
        TrustedFutureInterruptibleTask trustedFutureInterruptibleTask = this.task;
        if (trustedFutureInterruptibleTask != null) {
            trustedFutureInterruptibleTask.run();
        }
    }

    /* access modifiers changed from: protected */
    public void afterDone() {
        super.afterDone();
        if (wasInterrupted()) {
            TrustedFutureInterruptibleTask trustedFutureInterruptibleTask = this.task;
            if (trustedFutureInterruptibleTask != null) {
                trustedFutureInterruptibleTask.interruptTask();
            }
        }
        this.task = null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" (delegate = ");
        sb.append(this.task);
        sb.append(")");
        return sb.toString();
    }
}
