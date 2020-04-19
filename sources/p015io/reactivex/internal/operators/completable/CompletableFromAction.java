package p015io.reactivex.internal.operators.completable;

import p015io.reactivex.Completable;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.disposables.Disposables;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Action;

/* renamed from: io.reactivex.internal.operators.completable.CompletableFromAction */
public final class CompletableFromAction extends Completable {
    final Action run;

    public CompletableFromAction(Action action) {
        this.run = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        Disposable empty = Disposables.empty();
        completableObserver.onSubscribe(empty);
        try {
            this.run.run();
            if (!empty.isDisposed()) {
                completableObserver.onComplete();
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            if (!empty.isDisposed()) {
                completableObserver.onError(th);
            }
        }
    }
}
