package p015io.reactivex.internal.operators.maybe;

import p015io.reactivex.MaybeObserver;
import p015io.reactivex.MaybeSource;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmpty */
public final class MaybeIsEmpty<T> extends AbstractMaybeWithUpstream<T, Boolean> {

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmpty$IsEmptyMaybeObserver */
    static final class IsEmptyMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super Boolean> downstream;
        Disposable upstream;

        IsEmptyMaybeObserver(MaybeObserver<? super Boolean> maybeObserver) {
            this.downstream = maybeObserver;
        }

        public void dispose() {
            this.upstream.dispose();
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                this.downstream.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.downstream.onSuccess(Boolean.valueOf(false));
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }

        public void onComplete() {
            this.downstream.onSuccess(Boolean.valueOf(true));
        }
    }

    public MaybeIsEmpty(MaybeSource<T> maybeSource) {
        super(maybeSource);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super Boolean> maybeObserver) {
        this.source.subscribe(new IsEmptyMaybeObserver(maybeObserver));
    }
}
