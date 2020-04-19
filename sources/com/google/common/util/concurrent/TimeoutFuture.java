package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;

@GwtIncompatible
final class TimeoutFuture<V> extends TrustedFuture<V> {
    /* access modifiers changed from: private */
    @Nullable
    public ListenableFuture<V> delegateRef;
    @Nullable
    private Future<?> timer;

    private static final class Fire<V> implements Runnable {
        @Nullable
        TimeoutFuture<V> timeoutFutureRef;

        Fire(TimeoutFuture<V> timeoutFuture) {
            this.timeoutFutureRef = timeoutFuture;
        }

        public void run() {
            TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
            if (timeoutFuture != null) {
                ListenableFuture access$000 = timeoutFuture.delegateRef;
                if (access$000 != null) {
                    this.timeoutFutureRef = null;
                    if (access$000.isDone()) {
                        timeoutFuture.setFuture(access$000);
                    } else {
                        try {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Future timed out: ");
                            sb.append(access$000);
                            timeoutFuture.setException(new TimeoutException(sb.toString()));
                        } finally {
                            access$000.cancel(true);
                        }
                    }
                }
            }
        }
    }

    static <V> ListenableFuture<V> create(ListenableFuture<V> listenableFuture, long j, TimeUnit timeUnit, ScheduledExecutorService scheduledExecutorService) {
        TimeoutFuture timeoutFuture = new TimeoutFuture(listenableFuture);
        Fire fire = new Fire(timeoutFuture);
        timeoutFuture.timer = scheduledExecutorService.schedule(fire, j, timeUnit);
        listenableFuture.addListener(fire, MoreExecutors.directExecutor());
        return timeoutFuture;
    }

    private TimeoutFuture(ListenableFuture<V> listenableFuture) {
        this.delegateRef = (ListenableFuture) Preconditions.checkNotNull(listenableFuture);
    }

    /* access modifiers changed from: protected */
    public void afterDone() {
        maybePropagateCancellation(this.delegateRef);
        Future<?> future = this.timer;
        if (future != null) {
            future.cancel(false);
        }
        this.delegateRef = null;
        this.timer = null;
    }
}
