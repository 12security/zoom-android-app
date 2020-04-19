package com.google.api.client.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K, V> implements Cloneable {
    private Object[] data;
    int size;

    final class Entry implements java.util.Map.Entry<K, V> {
        private int index;

        Entry(int i) {
            this.index = i;
        }

        public K getKey() {
            return ArrayMap.this.getKey(this.index);
        }

        public V getValue() {
            return ArrayMap.this.getValue(this.index);
        }

        public V setValue(V v) {
            return ArrayMap.this.set(this.index, v);
        }

        public int hashCode() {
            Object key = getKey();
            Object value = getValue();
            int i = 0;
            int hashCode = key != null ? key.hashCode() : 0;
            if (value != null) {
                i = value.hashCode();
            }
            return hashCode ^ i;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry) obj;
            if (!Objects.equal(getKey(), entry.getKey()) || !Objects.equal(getValue(), entry.getValue())) {
                z = false;
            }
            return z;
        }
    }

    final class EntryIterator implements Iterator<java.util.Map.Entry<K, V>> {
        private int nextIndex;
        private boolean removed;

        EntryIterator() {
        }

        public boolean hasNext() {
            return this.nextIndex < ArrayMap.this.size;
        }

        public java.util.Map.Entry<K, V> next() {
            int i = this.nextIndex;
            if (i != ArrayMap.this.size) {
                this.nextIndex++;
                this.removed = false;
                return new Entry(i);
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            int i = this.nextIndex - 1;
            if (this.removed || i < 0) {
                throw new IllegalArgumentException();
            }
            ArrayMap.this.remove(i);
            this.nextIndex--;
            this.removed = true;
        }
    }

    final class EntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {
        EntrySet() {
        }

        public Iterator<java.util.Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public int size() {
            return ArrayMap.this.size;
        }
    }

    public static <K, V> ArrayMap<K, V> create() {
        return new ArrayMap<>();
    }

    public static <K, V> ArrayMap<K, V> create(int i) {
        ArrayMap<K, V> create = create();
        create.ensureCapacity(i);
        return create;
    }

    /* renamed from: of */
    public static <K, V> ArrayMap<K, V> m65of(Object... objArr) {
        ArrayMap<K, V> create = create(1);
        int length = objArr.length;
        if (1 != length % 2) {
            create.size = objArr.length / 2;
            Object[] objArr2 = new Object[length];
            create.data = objArr2;
            System.arraycopy(objArr, 0, objArr2, 0, length);
            return create;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("missing value for last key: ");
        sb.append(objArr[length - 1]);
        throw new IllegalArgumentException(sb.toString());
    }

    public final int size() {
        return this.size;
    }

    public final K getKey(int i) {
        if (i < 0 || i >= this.size) {
            return null;
        }
        return this.data[i << 1];
    }

    public final V getValue(int i) {
        if (i < 0 || i >= this.size) {
            return null;
        }
        return valueAtDataIndex((i << 1) + 1);
    }

    public final V set(int i, K k, V v) {
        if (i >= 0) {
            int i2 = i + 1;
            ensureCapacity(i2);
            int i3 = i << 1;
            V valueAtDataIndex = valueAtDataIndex(i3 + 1);
            setData(i3, k, v);
            if (i2 > this.size) {
                this.size = i2;
            }
            return valueAtDataIndex;
        }
        throw new IndexOutOfBoundsException();
    }

    public final V set(int i, V v) {
        int i2 = this.size;
        if (i < 0 || i >= i2) {
            throw new IndexOutOfBoundsException();
        }
        int i3 = (i << 1) + 1;
        V valueAtDataIndex = valueAtDataIndex(i3);
        this.data[i3] = v;
        return valueAtDataIndex;
    }

    public final void add(K k, V v) {
        set(this.size, k, v);
    }

    public final V remove(int i) {
        return removeFromDataIndexOfKey(i << 1);
    }

    public final boolean containsKey(Object obj) {
        return -2 != getDataIndexOfKey(obj);
    }

    public final int getIndexOfKey(K k) {
        return getDataIndexOfKey(k) >> 1;
    }

    public final V get(Object obj) {
        return valueAtDataIndex(getDataIndexOfKey(obj) + 1);
    }

    public final V put(K k, V v) {
        int indexOfKey = getIndexOfKey(k);
        if (indexOfKey == -1) {
            indexOfKey = this.size;
        }
        return set(indexOfKey, k, v);
    }

    public final V remove(Object obj) {
        return removeFromDataIndexOfKey(getDataIndexOfKey(obj));
    }

    public final void trim() {
        setDataCapacity(this.size << 1);
    }

    public final void ensureCapacity(int i) {
        int i2;
        if (i >= 0) {
            Object[] objArr = this.data;
            int i3 = i << 1;
            if (objArr == null) {
                i2 = 0;
            } else {
                i2 = objArr.length;
            }
            if (i3 > i2) {
                int i4 = ((i2 / 2) * 3) + 1;
                if (i4 % 2 != 0) {
                    i4++;
                }
                if (i4 >= i3) {
                    i3 = i4;
                }
                setDataCapacity(i3);
                return;
            }
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    private void setDataCapacity(int i) {
        if (i == 0) {
            this.data = null;
            return;
        }
        int i2 = this.size;
        Object[] objArr = this.data;
        if (i2 == 0 || i != objArr.length) {
            Object[] objArr2 = new Object[i];
            this.data = objArr2;
            if (i2 != 0) {
                System.arraycopy(objArr, 0, objArr2, 0, i2 << 1);
            }
        }
    }

    private void setData(int i, K k, V v) {
        Object[] objArr = this.data;
        objArr[i] = k;
        objArr[i + 1] = v;
    }

    private V valueAtDataIndex(int i) {
        if (i < 0) {
            return null;
        }
        return this.data[i];
    }

    private int getDataIndexOfKey(Object obj) {
        int i = this.size << 1;
        Object[] objArr = this.data;
        int i2 = 0;
        while (i2 < i) {
            Object obj2 = objArr[i2];
            if (obj == null) {
                if (obj2 != null) {
                    i2 += 2;
                }
            } else if (!obj.equals(obj2)) {
                i2 += 2;
            }
            return i2;
        }
        return -2;
    }

    private V removeFromDataIndexOfKey(int i) {
        int i2 = this.size << 1;
        if (i < 0 || i >= i2) {
            return null;
        }
        V valueAtDataIndex = valueAtDataIndex(i + 1);
        Object[] objArr = this.data;
        int i3 = (i2 - i) - 2;
        if (i3 != 0) {
            System.arraycopy(objArr, i + 2, objArr, i, i3);
        }
        this.size--;
        setData(i2 - 2, null, null);
        return valueAtDataIndex;
    }

    public void clear() {
        this.size = 0;
        this.data = null;
    }

    public final boolean containsValue(Object obj) {
        int i = this.size << 1;
        Object[] objArr = this.data;
        int i2 = 1;
        while (i2 < i) {
            Object obj2 = objArr[i2];
            if (obj == null) {
                if (obj2 != null) {
                    i2 += 2;
                }
            } else if (!obj.equals(obj2)) {
                i2 += 2;
            }
            return true;
        }
        return false;
    }

    public final Set<java.util.Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    public ArrayMap<K, V> clone() {
        try {
            ArrayMap<K, V> arrayMap = (ArrayMap) super.clone();
            Object[] objArr = this.data;
            if (objArr != null) {
                int length = objArr.length;
                Object[] objArr2 = new Object[length];
                arrayMap.data = objArr2;
                System.arraycopy(objArr, 0, objArr2, 0, length);
            }
            return arrayMap;
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }
}
