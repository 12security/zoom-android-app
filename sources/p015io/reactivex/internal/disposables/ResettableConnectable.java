package p015io.reactivex.internal.disposables;

import p015io.reactivex.annotations.Experimental;
import p015io.reactivex.disposables.Disposable;

@Experimental
/* renamed from: io.reactivex.internal.disposables.ResettableConnectable */
public interface ResettableConnectable {
    void resetIf(Disposable disposable);
}
