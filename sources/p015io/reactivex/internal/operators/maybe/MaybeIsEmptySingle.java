package p015io.reactivex.internal.operators.maybe;

import p015io.reactivex.Maybe;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.MaybeSource;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.fuseable.FuseToMaybe;
import p015io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmptySingle */
public final class MaybeIsEmptySingle<T> extends Single<Boolean> implements HasUpstreamMaybeSource<T>, FuseToMaybe<Boolean> {
    final MaybeSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeIsEmptySingle$IsEmptyMaybeObserver */
    static final class IsEmptyMaybeObserver<T> implements MaybeObserver<T>, Disposable {
        final SingleObserver<? super Boolean> downstream;
        Disposable upstream;

        IsEmptyMaybeObserver(SingleObserver<? super Boolean> singleObserver) {
            this.downstream = singleObserver;
        }

        public void dispose() {
            this.upstream.dispose();
            this.upstream = DisposableHelper.DISPOSED;
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
            this.upstream = DisposableHelper.DISPOSED;
            this.downstream.onSuccess(Boolean.valueOf(false));
        }

        public void onError(Throwable th) {
            this.upstream = DisposableHelper.DISPOSED;
            this.downstream.onError(th);
        }

        public void onComplete() {
            this.upstream = DisposableHelper.DISPOSED;
            this.downstream.onSuccess(Boolean.valueOf(true));
        }
    }

    public MaybeIsEmptySingle(MaybeSource<T> maybeSource) {
        this.source = maybeSource;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    public Maybe<Boolean> fuseToMaybe() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeIsEmpty<T>(this.source));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> singleObserver) {
        this.source.subscribe(new IsEmptyMaybeObserver(singleObserver));
    }
}
