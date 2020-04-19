package p015io.reactivex.internal.operators.maybe;

import p015io.reactivex.Maybe;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.SingleSource;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Predicate;
import p015io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFilterSingle */
public final class MaybeFilterSingle<T> extends Maybe<T> {
    final Predicate<? super T> predicate;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeFilterSingle$FilterMaybeObserver */
    static final class FilterMaybeObserver<T> implements SingleObserver<T>, Disposable {
        final MaybeObserver<? super T> downstream;
        final Predicate<? super T> predicate;
        Disposable upstream;

        FilterMaybeObserver(MaybeObserver<? super T> maybeObserver, Predicate<? super T> predicate2) {
            this.downstream = maybeObserver;
            this.predicate = predicate2;
        }

        public void dispose() {
            Disposable disposable = this.upstream;
            this.upstream = DisposableHelper.DISPOSED;
            disposable.dispose();
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
            try {
                if (this.predicate.test(t)) {
                    this.downstream.onSuccess(t);
                } else {
                    this.downstream.onComplete();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.downstream.onError(th);
            }
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }
    }

    public MaybeFilterSingle(SingleSource<T> singleSource, Predicate<? super T> predicate2) {
        this.source = singleSource;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new FilterMaybeObserver(maybeObserver, this.predicate));
    }
}
