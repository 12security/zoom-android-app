package p015io.reactivex.internal.operators.mixed;

import java.util.concurrent.Callable;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.CompletableSource;
import p015io.reactivex.MaybeSource;
import p015io.reactivex.Observer;
import p015io.reactivex.SingleSource;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Function;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.operators.maybe.MaybeToObservable;
import p015io.reactivex.internal.operators.single.SingleToObservable;

/* renamed from: io.reactivex.internal.operators.mixed.ScalarXMapZHelper */
final class ScalarXMapZHelper {
    private ScalarXMapZHelper() {
        throw new IllegalStateException("No instances!");
    }

    static <T> boolean tryAsCompletable(Object obj, Function<? super T, ? extends CompletableSource> function, CompletableObserver completableObserver) {
        if (!(obj instanceof Callable)) {
            return false;
        }
        CompletableSource completableSource = null;
        try {
            Object call = ((Callable) obj).call();
            if (call != null) {
                completableSource = (CompletableSource) ObjectHelper.requireNonNull(function.apply(call), "The mapper returned a null CompletableSource");
            }
            if (completableSource == null) {
                EmptyDisposable.complete(completableObserver);
            } else {
                completableSource.subscribe(completableObserver);
            }
            return true;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, completableObserver);
            return true;
        }
    }

    static <T, R> boolean tryAsMaybe(Object obj, Function<? super T, ? extends MaybeSource<? extends R>> function, Observer<? super R> observer) {
        if (!(obj instanceof Callable)) {
            return false;
        }
        MaybeSource maybeSource = null;
        try {
            Object call = ((Callable) obj).call();
            if (call != null) {
                maybeSource = (MaybeSource) ObjectHelper.requireNonNull(function.apply(call), "The mapper returned a null MaybeSource");
            }
            if (maybeSource == null) {
                EmptyDisposable.complete(observer);
            } else {
                maybeSource.subscribe(MaybeToObservable.create(observer));
            }
            return true;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
            return true;
        }
    }

    static <T, R> boolean tryAsSingle(Object obj, Function<? super T, ? extends SingleSource<? extends R>> function, Observer<? super R> observer) {
        if (!(obj instanceof Callable)) {
            return false;
        }
        SingleSource singleSource = null;
        try {
            Object call = ((Callable) obj).call();
            if (call != null) {
                singleSource = (SingleSource) ObjectHelper.requireNonNull(function.apply(call), "The mapper returned a null SingleSource");
            }
            if (singleSource == null) {
                EmptyDisposable.complete(observer);
            } else {
                singleSource.subscribe(SingleToObservable.create(observer));
            }
            return true;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
            return true;
        }
    }
}
