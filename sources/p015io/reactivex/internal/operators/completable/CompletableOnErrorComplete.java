package p015io.reactivex.internal.operators.completable;

import p015io.reactivex.Completable;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.CompletableSource;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.CompositeException;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Predicate;

/* renamed from: io.reactivex.internal.operators.completable.CompletableOnErrorComplete */
public final class CompletableOnErrorComplete extends Completable {
    final Predicate<? super Throwable> predicate;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableOnErrorComplete$OnError */
    final class OnError implements CompletableObserver {
        private final CompletableObserver downstream;

        OnError(CompletableObserver completableObserver) {
            this.downstream = completableObserver;
        }

        public void onComplete() {
            this.downstream.onComplete();
        }

        public void onError(Throwable th) {
            try {
                if (CompletableOnErrorComplete.this.predicate.test(th)) {
                    this.downstream.onComplete();
                } else {
                    this.downstream.onError(th);
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.downstream.onError(new CompositeException(th, th2));
            }
        }

        public void onSubscribe(Disposable disposable) {
            this.downstream.onSubscribe(disposable);
        }
    }

    public CompletableOnErrorComplete(CompletableSource completableSource, Predicate<? super Throwable> predicate2) {
        this.source = completableSource;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new OnError(completableObserver));
    }
}
