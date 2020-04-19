package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.ForOverride;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractTransformFuture<I, O, F, T> extends TrustedFuture<O> implements Runnable {
    @Nullable
    F function;
    @Nullable
    ListenableFuture<? extends I> inputFuture;

    private static final class AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>> {
        AsyncTransformFuture(ListenableFuture<? extends I> listenableFuture, AsyncFunction<? super I, ? extends O> asyncFunction) {
            super(listenableFuture, asyncFunction);
        }

        /* access modifiers changed from: 0000 */
        public ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> asyncFunction, @Nullable I i) throws Exception {
            ListenableFuture<? extends O> apply = asyncFunction.apply(i);
            Preconditions.checkNotNull(apply, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
            return apply;
        }

        /* access modifiers changed from: 0000 */
        public void setResult(ListenableFuture<? extends O> listenableFuture) {
            setFuture(listenableFuture);
        }
    }

    private static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O> {
        TransformFuture(ListenableFuture<? extends I> listenableFuture, Function<? super I, ? extends O> function) {
            super(listenableFuture, function);
        }

        /* access modifiers changed from: 0000 */
        @Nullable
        public O doTransform(Function<? super I, ? extends O> function, @Nullable I i) {
            return function.apply(i);
        }

        /* access modifiers changed from: 0000 */
        public void setResult(@Nullable O o) {
            set(o);
        }
    }

    /* access modifiers changed from: 0000 */
    @ForOverride
    @Nullable
    public abstract T doTransform(F f, @Nullable I i) throws Exception;

    /* access modifiers changed from: 0000 */
    @ForOverride
    public abstract void setResult(@Nullable T t);

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> listenableFuture, AsyncFunction<? super I, ? extends O> asyncFunction) {
        AsyncTransformFuture asyncTransformFuture = new AsyncTransformFuture(listenableFuture, asyncFunction);
        listenableFuture.addListener(asyncTransformFuture, MoreExecutors.directExecutor());
        return asyncTransformFuture;
    }

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> listenableFuture, AsyncFunction<? super I, ? extends O> asyncFunction, Executor executor) {
        Preconditions.checkNotNull(executor);
        AsyncTransformFuture asyncTransformFuture = new AsyncTransformFuture(listenableFuture, asyncFunction);
        listenableFuture.addListener(asyncTransformFuture, MoreExecutors.rejectionPropagatingExecutor(executor, asyncTransformFuture));
        return asyncTransformFuture;
    }

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> listenableFuture, Function<? super I, ? extends O> function2) {
        Preconditions.checkNotNull(function2);
        TransformFuture transformFuture = new TransformFuture(listenableFuture, function2);
        listenableFuture.addListener(transformFuture, MoreExecutors.directExecutor());
        return transformFuture;
    }

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> listenableFuture, Function<? super I, ? extends O> function2, Executor executor) {
        Preconditions.checkNotNull(function2);
        TransformFuture transformFuture = new TransformFuture(listenableFuture, function2);
        listenableFuture.addListener(transformFuture, MoreExecutors.rejectionPropagatingExecutor(executor, transformFuture));
        return transformFuture;
    }

    AbstractTransformFuture(ListenableFuture<? extends I> listenableFuture, F f) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(listenableFuture);
        this.function = Preconditions.checkNotNull(f);
    }

    public final void run() {
        ListenableFuture<? extends I> listenableFuture = this.inputFuture;
        F f = this.function;
        boolean z = true;
        boolean isCancelled = isCancelled() | (listenableFuture == null);
        if (f != null) {
            z = false;
        }
        if (!isCancelled && !z) {
            this.inputFuture = null;
            this.function = null;
            try {
                try {
                    setResult(doTransform(f, Futures.getDone(listenableFuture)));
                } catch (UndeclaredThrowableException e) {
                    setException(e.getCause());
                } catch (Throwable th) {
                    setException(th);
                }
            } catch (CancellationException unused) {
                cancel(false);
            } catch (ExecutionException e2) {
                setException(e2.getCause());
            } catch (RuntimeException e3) {
                setException(e3);
            } catch (Error e4) {
                setException(e4);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void afterDone() {
        maybePropagateCancellation(this.inputFuture);
        this.inputFuture = null;
        this.function = null;
    }
}
