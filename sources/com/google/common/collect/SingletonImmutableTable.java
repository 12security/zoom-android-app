package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Map;

@GwtCompatible
class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    final C singleColumnKey;
    final R singleRowKey;
    final V singleValue;

    public int size() {
        return 1;
    }

    SingletonImmutableTable(R r, C c, V v) {
        this.singleRowKey = Preconditions.checkNotNull(r);
        this.singleColumnKey = Preconditions.checkNotNull(c);
        this.singleValue = Preconditions.checkNotNull(v);
    }

    SingletonImmutableTable(Cell<R, C, V> cell) {
        this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
    }

    public ImmutableMap<R, V> column(C c) {
        Preconditions.checkNotNull(c);
        return containsColumn(c) ? ImmutableMap.m133of(this.singleRowKey, this.singleValue) : ImmutableMap.m132of();
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.m133of(this.singleColumnKey, ImmutableMap.m133of(this.singleRowKey, this.singleValue));
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.m133of(this.singleRowKey, ImmutableMap.m133of(this.singleColumnKey, this.singleValue));
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSet<Cell<R, C, V>> createCellSet() {
        return ImmutableSet.m156of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
    }

    /* access modifiers changed from: 0000 */
    public ImmutableCollection<V> createValues() {
        return ImmutableSet.m156of(this.singleValue);
    }

    /* access modifiers changed from: 0000 */
    public SerializedForm createSerializedForm() {
        return SerializedForm.create(this, new int[]{0}, new int[]{0});
    }
}
