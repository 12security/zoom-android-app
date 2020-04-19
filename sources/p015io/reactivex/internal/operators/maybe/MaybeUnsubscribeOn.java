package p015io.reactivex.internal.operators.maybe;

import java.util.concurrent.atomic.AtomicReference;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.MaybeSource;
import p015io.reactivex.Scheduler;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeUnsubscribeOn */
public final class MaybeUnsubscribeOn<T> extends AbstractMaybeWithUpstream<T, T> {
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeUnsubscribeOn$UnsubscribeOnMaybeObserver */
    static final class UnsubscribeOnMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
        private static final long serialVersionUID = 3256698449646456986L;
        final MaybeObserver<? super T> downstream;

        /* renamed from: ds */
        Disposable f411ds;
        final Scheduler scheduler;

        UnsubscribeOnMaybeObserver(MaybeObserver<? super T> maybeObserver, Scheduler scheduler2) {
            this.downstream = maybeObserver;
            this.scheduler = scheduler2;
        }

        public void dispose() {
            Disposable disposable = (Disposable) getAndSet(DisposableHelper.DISPOSED);
            if (disposable != DisposableHelper.DISPOSED) {
                this.f411ds = disposable;
                this.scheduler.scheduleDirect(this);
            }
        }

        public void run() {
            this.f411ds.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.downstream.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.downstream.onSuccess(t);
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }

        public void onComplete() {
            this.downstream.onComplete();
        }
    }

    public MaybeUnsubscribeOn(MaybeSource<T> maybeSource, Scheduler scheduler2) {
        super(maybeSource);
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new UnsubscribeOnMaybeObserver(maybeObserver, this.scheduler));
    }
}
