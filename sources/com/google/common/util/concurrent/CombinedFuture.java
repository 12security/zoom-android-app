package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.Nullable;

@GwtCompatible
final class CombinedFuture<V> extends AggregateFuture<Object, V> {

    private final class AsyncCallableInterruptibleTask extends CombinedFutureInterruptibleTask {
        private final AsyncCallable<V> callable;

        public AsyncCallableInterruptibleTask(AsyncCallable<V> asyncCallable, Executor executor) {
            super(executor);
            this.callable = (AsyncCallable) Preconditions.checkNotNull(asyncCallable);
        }

        /* access modifiers changed from: 0000 */
        public void setValue() throws Exception {
            CombinedFuture.this.setFuture(this.callable.call());
        }
    }

    private final class CallableInterruptibleTask extends CombinedFutureInterruptibleTask {
        private final Callable<V> callable;

        public CallableInterruptibleTask(Callable<V> callable2, Executor executor) {
            super(executor);
            this.callable = (Callable) Preconditions.checkNotNull(callable2);
        }

        /* access modifiers changed from: 0000 */
        public void setValue() throws Exception {
            CombinedFuture.this.set(this.callable.call());
        }
    }

    private abstract class CombinedFutureInterruptibleTask extends InterruptibleTask {
        private final Executor listenerExecutor;
        volatile boolean thrownByExecute = true;

        /* access modifiers changed from: 0000 */
        public abstract void setValue() throws Exception;

        public CombinedFutureInterruptibleTask(Executor executor) {
            this.listenerExecutor = (Executor) Preconditions.checkNotNull(executor);
        }

        /* access modifiers changed from: 0000 */
        public final void runInterruptibly() {
            this.thrownByExecute = false;
            if (!CombinedFuture.this.isDone()) {
                try {
                    setValue();
                } catch (ExecutionException e) {
                    CombinedFuture.this.setException(e.getCause());
                } catch (CancellationException unused) {
                    CombinedFuture.this.cancel(false);
                } catch (Throwable th) {
                    CombinedFuture.this.setException(th);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public final boolean wasInterrupted() {
            return CombinedFuture.this.wasInterrupted();
        }

        /* access modifiers changed from: 0000 */
        public final void execute() {
            try {
                this.listenerExecutor.execute(this);
            } catch (RejectedExecutionException e) {
                if (this.thrownByExecute) {
                    CombinedFuture.this.setException(e);
                }
            }
        }
    }

    private final class CombinedFutureRunningState extends RunningState {
        private CombinedFutureInterruptibleTask task;

        /* access modifiers changed from: 0000 */
        public void collectOneValue(boolean z, int i, @Nullable Object obj) {
        }

        CombinedFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends Object>> immutableCollection, boolean z, CombinedFutureInterruptibleTask combinedFutureInterruptibleTask) {
            super(immutableCollection, z, false);
            this.task = combinedFutureInterruptibleTask;
        }

        /* access modifiers changed from: 0000 */
        public void handleAllCompleted() {
            CombinedFutureInterruptibleTask combinedFutureInterruptibleTask = this.task;
            if (combinedFutureInterruptibleTask != null) {
                combinedFutureInterruptibleTask.execute();
            } else {
                Preconditions.checkState(CombinedFuture.this.isDone());
            }
        }

        /* access modifiers changed from: 0000 */
        public void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.task = null;
        }

        /* access modifiers changed from: 0000 */
        public void interruptTask() {
            CombinedFutureInterruptibleTask combinedFutureInterruptibleTask = this.task;
            if (combinedFutureInterruptibleTask != null) {
                combinedFutureInterruptibleTask.interruptTask();
            }
        }
    }

    CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> immutableCollection, boolean z, Executor executor, AsyncCallable<V> asyncCallable) {
        init(new CombinedFutureRunningState(immutableCollection, z, new AsyncCallableInterruptibleTask(asyncCallable, executor)));
    }

    CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> immutableCollection, boolean z, Executor executor, Callable<V> callable) {
        init(new CombinedFutureRunningState(immutableCollection, z, new CallableInterruptibleTask(callable, executor)));
    }
}
