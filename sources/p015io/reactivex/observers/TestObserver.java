package p015io.reactivex.observers;

import com.google.api.client.googleapis.notifications.ResourceStates;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.Observer;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.fuseable.QueueDisposable;
import p015io.reactivex.internal.util.ExceptionHelper;

/* renamed from: io.reactivex.observers.TestObserver */
public class TestObserver<T> extends BaseTestConsumer<T, TestObserver<T>> implements Observer<T>, Disposable, MaybeObserver<T>, SingleObserver<T>, CompletableObserver {
    private final Observer<? super T> downstream;

    /* renamed from: qd */
    private QueueDisposable<T> f467qd;
    private final AtomicReference<Disposable> upstream;

    /* renamed from: io.reactivex.observers.TestObserver$EmptyObserver */
    enum EmptyObserver implements Observer<Object> {
        INSTANCE;

        public void onComplete() {
        }

        public void onError(Throwable th) {
        }

        public void onNext(Object obj) {
        }

        public void onSubscribe(Disposable disposable) {
        }
    }

    public static <T> TestObserver<T> create() {
        return new TestObserver<>();
    }

    public static <T> TestObserver<T> create(Observer<? super T> observer) {
        return new TestObserver<>(observer);
    }

    public TestObserver() {
        this(EmptyObserver.INSTANCE);
    }

    public TestObserver(Observer<? super T> observer) {
        this.upstream = new AtomicReference<>();
        this.downstream = observer;
    }

    public void onSubscribe(Disposable disposable) {
        this.lastThread = Thread.currentThread();
        if (disposable == null) {
            this.errors.add(new NullPointerException("onSubscribe received a null Subscription"));
        } else if (!this.upstream.compareAndSet(null, disposable)) {
            disposable.dispose();
            if (this.upstream.get() != DisposableHelper.DISPOSED) {
                List list = this.errors;
                StringBuilder sb = new StringBuilder();
                sb.append("onSubscribe received multiple subscriptions: ");
                sb.append(disposable);
                list.add(new IllegalStateException(sb.toString()));
            }
        } else {
            if (this.initialFusionMode != 0 && (disposable instanceof QueueDisposable)) {
                this.f467qd = (QueueDisposable) disposable;
                int requestFusion = this.f467qd.requestFusion(this.initialFusionMode);
                this.establishedFusionMode = requestFusion;
                if (requestFusion == 1) {
                    this.checkSubscriptionOnce = true;
                    this.lastThread = Thread.currentThread();
                    while (true) {
                        try {
                            Object poll = this.f467qd.poll();
                            if (poll == null) {
                                break;
                            }
                            this.values.add(poll);
                        } catch (Throwable th) {
                            this.errors.add(th);
                        }
                    }
                    this.completions++;
                    this.upstream.lazySet(DisposableHelper.DISPOSED);
                    return;
                }
            }
            this.downstream.onSubscribe(disposable);
        }
    }

    public void onNext(T t) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.upstream.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        this.lastThread = Thread.currentThread();
        if (this.establishedFusionMode == 2) {
            while (true) {
                try {
                    Object poll = this.f467qd.poll();
                    if (poll == null) {
                        break;
                    }
                    this.values.add(poll);
                } catch (Throwable th) {
                    this.errors.add(th);
                    this.f467qd.dispose();
                }
            }
            return;
        }
        this.values.add(t);
        if (t == null) {
            this.errors.add(new NullPointerException("onNext received a null value"));
        }
        this.downstream.onNext(t);
    }

    public void onError(Throwable th) {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.upstream.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            if (th == null) {
                this.errors.add(new NullPointerException("onError received a null Throwable"));
            } else {
                this.errors.add(th);
            }
            this.downstream.onError(th);
        } finally {
            this.done.countDown();
        }
    }

    public void onComplete() {
        if (!this.checkSubscriptionOnce) {
            this.checkSubscriptionOnce = true;
            if (this.upstream.get() == null) {
                this.errors.add(new IllegalStateException("onSubscribe not called in proper order"));
            }
        }
        try {
            this.lastThread = Thread.currentThread();
            this.completions++;
            this.downstream.onComplete();
        } finally {
            this.done.countDown();
        }
    }

    public final boolean isCancelled() {
        return isDisposed();
    }

    public final void cancel() {
        dispose();
    }

    public final void dispose() {
        DisposableHelper.dispose(this.upstream);
    }

    public final boolean isDisposed() {
        return DisposableHelper.isDisposed((Disposable) this.upstream.get());
    }

    public final boolean hasSubscription() {
        return this.upstream.get() != null;
    }

    public final TestObserver<T> assertSubscribed() {
        if (this.upstream.get() != null) {
            return this;
        }
        throw fail("Not subscribed!");
    }

    public final TestObserver<T> assertNotSubscribed() {
        if (this.upstream.get() != null) {
            throw fail("Subscribed!");
        } else if (this.errors.isEmpty()) {
            return this;
        } else {
            throw fail("Not subscribed but errors found");
        }
    }

    public final TestObserver<T> assertOf(Consumer<? super TestObserver<T>> consumer) {
        try {
            consumer.accept(this);
            return this;
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> setInitialFusionMode(int i) {
        this.initialFusionMode = i;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> assertFusionMode(int i) {
        int i2 = this.establishedFusionMode;
        if (i2 == i) {
            return this;
        }
        if (this.f467qd != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Fusion mode different. Expected: ");
            sb.append(fusionModeToString(i));
            sb.append(", actual: ");
            sb.append(fusionModeToString(i2));
            throw new AssertionError(sb.toString());
        }
        throw fail("Upstream is not fuseable");
    }

    static String fusionModeToString(int i) {
        switch (i) {
            case 0:
                return "NONE";
            case 1:
                return ResourceStates.SYNC;
            case 2:
                return "ASYNC";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown(");
                sb.append(i);
                sb.append(")");
                return sb.toString();
        }
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> assertFuseable() {
        if (this.f467qd != null) {
            return this;
        }
        throw new AssertionError("Upstream is not fuseable.");
    }

    /* access modifiers changed from: 0000 */
    public final TestObserver<T> assertNotFuseable() {
        if (this.f467qd == null) {
            return this;
        }
        throw new AssertionError("Upstream is fuseable.");
    }

    public void onSuccess(T t) {
        onNext(t);
        onComplete();
    }
}
