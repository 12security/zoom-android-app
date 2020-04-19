package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
    @GwtIncompatible
    private static final long serialVersionUID = -2250766705698539974L;
    /* access modifiers changed from: private */
    public transient Map<E, Count> backingMap;
    /* access modifiers changed from: private */
    public transient long size = ((long) super.size());

    private class MapBasedMultisetIterator implements Iterator<E> {
        boolean canRemove;
        Entry<E, Count> currentEntry;
        final Iterator<Entry<E, Count>> entryIterator;
        int occurrencesLeft;

        MapBasedMultisetIterator() {
            this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
        }

        public boolean hasNext() {
            return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
        }

        public E next() {
            if (this.occurrencesLeft == 0) {
                this.currentEntry = (Entry) this.entryIterator.next();
                this.occurrencesLeft = ((Count) this.currentEntry.getValue()).get();
            }
            this.occurrencesLeft--;
            this.canRemove = true;
            return this.currentEntry.getKey();
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            if (((Count) this.currentEntry.getValue()).get() > 0) {
                if (((Count) this.currentEntry.getValue()).addAndGet(-1) == 0) {
                    this.entryIterator.remove();
                }
                AbstractMapBasedMultiset.this.size = AbstractMapBasedMultiset.this.size - 1;
                this.canRemove = false;
                return;
            }
            throw new ConcurrentModificationException();
        }
    }

    static /* synthetic */ long access$122(AbstractMapBasedMultiset abstractMapBasedMultiset, long j) {
        long j2 = abstractMapBasedMultiset.size - j;
        abstractMapBasedMultiset.size = j2;
        return j2;
    }

    protected AbstractMapBasedMultiset(Map<E, Count> map) {
        this.backingMap = (Map) Preconditions.checkNotNull(map);
    }

    /* access modifiers changed from: 0000 */
    public void setBackingMap(Map<E, Count> map) {
        this.backingMap = map;
    }

    public Set<Multiset.Entry<E>> entrySet() {
        return super.entrySet();
    }

    /* access modifiers changed from: 0000 */
    public Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator it = this.backingMap.entrySet().iterator();
        return new Iterator<Multiset.Entry<E>>() {
            Entry<E, Count> toRemove;

            public boolean hasNext() {
                return it.hasNext();
            }

            public Multiset.Entry<E> next() {
                final Entry<E, Count> entry = (Entry) it.next();
                this.toRemove = entry;
                return new AbstractEntry<E>() {
                    public E getElement() {
                        return entry.getKey();
                    }

                    public int getCount() {
                        int i;
                        Count count = (Count) entry.getValue();
                        if (count == null || count.get() == 0) {
                            Count count2 = (Count) AbstractMapBasedMultiset.this.backingMap.get(getElement());
                            if (count2 != null) {
                                return count2.get();
                            }
                        }
                        if (count == null) {
                            i = 0;
                        } else {
                            i = count.get();
                        }
                        return i;
                    }
                };
            }

            public void remove() {
                CollectPreconditions.checkRemove(this.toRemove != null);
                AbstractMapBasedMultiset.access$122(AbstractMapBasedMultiset.this, (long) ((Count) this.toRemove.getValue()).getAndSet(0));
                it.remove();
                this.toRemove = null;
            }
        };
    }

    public void clear() {
        for (Count count : this.backingMap.values()) {
            count.set(0);
        }
        this.backingMap.clear();
        this.size = 0;
    }

    /* access modifiers changed from: 0000 */
    public int distinctElements() {
        return this.backingMap.size();
    }

    public int size() {
        return Ints.saturatedCast(this.size);
    }

    public Iterator<E> iterator() {
        return new MapBasedMultisetIterator();
    }

    public int count(@Nullable Object obj) {
        Count count = (Count) Maps.safeGet(this.backingMap, obj);
        if (count == null) {
            return 0;
        }
        return count.get();
    }

    @CanIgnoreReturnValue
    public int add(@Nullable E e, int i) {
        int i2;
        if (i == 0) {
            return count(e);
        }
        boolean z = true;
        Preconditions.checkArgument(i > 0, "occurrences cannot be negative: %s", i);
        Count count = (Count) this.backingMap.get(e);
        if (count == null) {
            this.backingMap.put(e, new Count(i));
            i2 = 0;
        } else {
            i2 = count.get();
            long j = ((long) i2) + ((long) i);
            if (j > 2147483647L) {
                z = false;
            }
            Preconditions.checkArgument(z, "too many occurrences: %s", j);
            count.add(i);
        }
        this.size += (long) i;
        return i2;
    }

    @CanIgnoreReturnValue
    public int remove(@Nullable Object obj, int i) {
        if (i == 0) {
            return count(obj);
        }
        Preconditions.checkArgument(i > 0, "occurrences cannot be negative: %s", i);
        Count count = (Count) this.backingMap.get(obj);
        if (count == null) {
            return 0;
        }
        int i2 = count.get();
        if (i2 <= i) {
            this.backingMap.remove(obj);
            i = i2;
        }
        count.add(-i);
        this.size -= (long) i;
        return i2;
    }

    @CanIgnoreReturnValue
    public int setCount(@Nullable E e, int i) {
        int i2;
        CollectPreconditions.checkNonnegative(i, "count");
        if (i == 0) {
            i2 = getAndSet((Count) this.backingMap.remove(e), i);
        } else {
            Count count = (Count) this.backingMap.get(e);
            int andSet = getAndSet(count, i);
            if (count == null) {
                this.backingMap.put(e, new Count(i));
            }
            i2 = andSet;
        }
        this.size += (long) (i - i2);
        return i2;
    }

    private static int getAndSet(@Nullable Count count, int i) {
        if (count == null) {
            return 0;
        }
        return count.getAndSet(i);
    }

    @GwtIncompatible
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }
}
