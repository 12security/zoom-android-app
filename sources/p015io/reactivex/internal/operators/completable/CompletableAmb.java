package p015io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicBoolean;
import p015io.reactivex.Completable;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.CompletableSource;
import p015io.reactivex.disposables.CompositeDisposable;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableAmb */
public final class CompletableAmb extends Completable {
    private final CompletableSource[] sources;
    private final Iterable<? extends CompletableSource> sourcesIterable;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableAmb$Amb */
    static final class Amb implements CompletableObserver {
        final CompletableObserver downstream;
        final AtomicBoolean once;
        final CompositeDisposable set;
        Disposable upstream;

        Amb(AtomicBoolean atomicBoolean, CompositeDisposable compositeDisposable, CompletableObserver completableObserver) {
            this.once = atomicBoolean;
            this.set = compositeDisposable;
            this.downstream = completableObserver;
        }

        public void onComplete() {
            if (this.once.compareAndSet(false, true)) {
                this.set.delete(this.upstream);
                this.set.dispose();
                this.downstream.onComplete();
            }
        }

        public void onError(Throwable th) {
            if (this.once.compareAndSet(false, true)) {
                this.set.delete(this.upstream);
                this.set.dispose();
                this.downstream.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onSubscribe(Disposable disposable) {
            this.upstream = disposable;
            this.set.add(disposable);
        }
    }

    public CompletableAmb(CompletableSource[] completableSourceArr, Iterable<? extends CompletableSource> iterable) {
        this.sources = completableSourceArr;
        this.sourcesIterable = iterable;
    }

    public void subscribeActual(CompletableObserver completableObserver) {
        int i;
        CompletableSource[] completableSourceArr = this.sources;
        if (completableSourceArr == null) {
            completableSourceArr = new CompletableSource[8];
            try {
                i = 0;
                for (CompletableSource completableSource : this.sourcesIterable) {
                    if (completableSource == null) {
                        EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), completableObserver);
                        return;
                    }
                    if (i == completableSourceArr.length) {
                        CompletableSource[] completableSourceArr2 = new CompletableSource[((i >> 2) + i)];
                        System.arraycopy(completableSourceArr, 0, completableSourceArr2, 0, i);
                        completableSourceArr = completableSourceArr2;
                    }
                    int i2 = i + 1;
                    completableSourceArr[i] = completableSource;
                    i = i2;
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                EmptyDisposable.error(th, completableObserver);
                return;
            }
        } else {
            i = completableSourceArr.length;
        }
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        completableObserver.onSubscribe(compositeDisposable);
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        int i3 = 0;
        while (i3 < i) {
            CompletableSource completableSource2 = completableSourceArr[i3];
            if (!compositeDisposable.isDisposed()) {
                if (completableSource2 == null) {
                    NullPointerException nullPointerException = new NullPointerException("One of the sources is null");
                    if (atomicBoolean.compareAndSet(false, true)) {
                        compositeDisposable.dispose();
                        completableObserver.onError(nullPointerException);
                    } else {
                        RxJavaPlugins.onError(nullPointerException);
                    }
                    return;
                }
                completableSource2.subscribe(new Amb(atomicBoolean, compositeDisposable, completableObserver));
                i3++;
            } else {
                return;
            }
        }
        if (i == 0) {
            completableObserver.onComplete();
        }
    }
}
