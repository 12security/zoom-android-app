package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.NoSuchElementException;

@GwtCompatible
abstract class MultitransformedIterator<F, T> implements Iterator<T> {
    final Iterator<? extends F> backingIterator;
    private Iterator<? extends T> current = Iterators.emptyIterator();
    private Iterator<? extends T> removeFrom;

    /* access modifiers changed from: 0000 */
    public abstract Iterator<? extends T> transform(F f);

    MultitransformedIterator(Iterator<? extends F> it) {
        this.backingIterator = (Iterator) Preconditions.checkNotNull(it);
    }

    public boolean hasNext() {
        Preconditions.checkNotNull(this.current);
        if (this.current.hasNext()) {
            return true;
        }
        while (this.backingIterator.hasNext()) {
            Iterator<? extends T> transform = transform(this.backingIterator.next());
            this.current = transform;
            Preconditions.checkNotNull(transform);
            if (this.current.hasNext()) {
                return true;
            }
        }
        return false;
    }

    public T next() {
        if (hasNext()) {
            Iterator<? extends T> it = this.current;
            this.removeFrom = it;
            return it.next();
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        CollectPreconditions.checkRemove(this.removeFrom != null);
        this.removeFrom.remove();
        this.removeFrom = null;
    }
}
