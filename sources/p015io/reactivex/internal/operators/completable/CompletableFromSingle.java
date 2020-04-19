package p015io.reactivex.internal.operators.completable;

import p015io.reactivex.Completable;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.SingleSource;
import p015io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromSingle */
public final class CompletableFromSingle<T> extends Completable {
    final SingleSource<T> single;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableFromSingle$CompletableFromSingleObserver */
    static final class CompletableFromSingleObserver<T> implements SingleObserver<T> {

        /* renamed from: co */
        final CompletableObserver f379co;

        CompletableFromSingleObserver(CompletableObserver completableObserver) {
            this.f379co = completableObserver;
        }

        public void onError(Throwable th) {
            this.f379co.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.f379co.onSubscribe(disposable);
        }

        public void onSuccess(T t) {
            this.f379co.onComplete();
        }
    }

    public CompletableFromSingle(SingleSource<T> singleSource) {
        this.single = singleSource;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.single.subscribe(new CompletableFromSingleObserver(completableObserver));
    }
}
