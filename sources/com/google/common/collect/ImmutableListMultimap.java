package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public class ImmutableListMultimap<K, V> extends ImmutableMultimap<K, V> implements ListMultimap<K, V> {
    @GwtIncompatible
    private static final long serialVersionUID = 0;
    @RetainedWith
    @LazyInit
    private transient ImmutableListMultimap<V, K> inverse;

    public static final class Builder<K, V> extends com.google.common.collect.ImmutableMultimap.Builder<K, V> {
        @CanIgnoreReturnValue
        public Builder<K, V> put(K k, V v) {
            super.put(k, v);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
            super.putAll(iterable);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(K k, Iterable<? extends V> iterable) {
            super.putAll(k, iterable);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(K k, V... vArr) {
            super.putAll(k, vArr);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            super.putAll(multimap);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> orderKeysBy(Comparator<? super K> comparator) {
            super.orderKeysBy(comparator);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> orderValuesBy(Comparator<? super V> comparator) {
            super.orderValuesBy(comparator);
            return this;
        }

        public ImmutableListMultimap<K, V> build() {
            return (ImmutableListMultimap) super.build();
        }
    }

    /* renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m126of() {
        return EmptyImmutableListMultimap.INSTANCE;
    }

    /* renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m127of(K k, V v) {
        Builder builder = builder();
        builder.put(k, v);
        return builder.build();
    }

    /* renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m128of(K k, V v, K k2, V v2) {
        Builder builder = builder();
        builder.put(k, v);
        builder.put(k2, v2);
        return builder.build();
    }

    /* renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m129of(K k, V v, K k2, V v2, K k3, V v3) {
        Builder builder = builder();
        builder.put(k, v);
        builder.put(k2, v2);
        builder.put(k3, v3);
        return builder.build();
    }

    /* renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m130of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        Builder builder = builder();
        builder.put(k, v);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        return builder.build();
    }

    /* renamed from: of */
    public static <K, V> ImmutableListMultimap<K, V> m131of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Builder builder = builder();
        builder.put(k, v);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        builder.put(k5, v5);
        return builder.build();
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        if (multimap.isEmpty()) {
            return m126of();
        }
        if (multimap instanceof ImmutableListMultimap) {
            ImmutableListMultimap<K, V> immutableListMultimap = (ImmutableListMultimap) multimap;
            if (!immutableListMultimap.isPartialView()) {
                return immutableListMultimap;
            }
        }
        com.google.common.collect.ImmutableMap.Builder builder = new com.google.common.collect.ImmutableMap.Builder(multimap.asMap().size());
        int i = 0;
        for (Entry entry : multimap.asMap().entrySet()) {
            ImmutableList copyOf = ImmutableList.copyOf((Collection) entry.getValue());
            if (!copyOf.isEmpty()) {
                builder.put(entry.getKey(), copyOf);
                i += copyOf.size();
            }
        }
        return new ImmutableListMultimap<>(builder.build(), i);
    }

    @Beta
    public static <K, V> ImmutableListMultimap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        return new Builder().putAll(iterable).build();
    }

    ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> immutableMap, int i) {
        super(immutableMap, i);
    }

    public ImmutableList<V> get(@Nullable K k) {
        ImmutableList<V> immutableList = (ImmutableList) this.map.get(k);
        return immutableList == null ? ImmutableList.m113of() : immutableList;
    }

    public ImmutableListMultimap<V, K> inverse() {
        ImmutableListMultimap<V, K> immutableListMultimap = this.inverse;
        if (immutableListMultimap != null) {
            return immutableListMultimap;
        }
        ImmutableListMultimap<V, K> invert = invert();
        this.inverse = invert;
        return invert;
    }

    private ImmutableListMultimap<V, K> invert() {
        Builder builder = builder();
        Iterator it = entries().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            builder.put(entry.getValue(), entry.getKey());
        }
        ImmutableListMultimap<V, K> build = builder.build();
        build.inverse = this;
        return build;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public ImmutableList<V> removeAll(Object obj) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public ImmutableList<V> replaceValues(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Serialization.writeMultimap(this, objectOutputStream);
    }

    @GwtIncompatible
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt >= 0) {
            com.google.common.collect.ImmutableMap.Builder builder = ImmutableMap.builder();
            int i = 0;
            int i2 = 0;
            while (i < readInt) {
                Object readObject = objectInputStream.readObject();
                int readInt2 = objectInputStream.readInt();
                if (readInt2 > 0) {
                    com.google.common.collect.ImmutableList.Builder builder2 = ImmutableList.builder();
                    for (int i3 = 0; i3 < readInt2; i3++) {
                        builder2.add(objectInputStream.readObject());
                    }
                    builder.put(readObject, builder2.build());
                    i2 += readInt2;
                    i++;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid value count ");
                    sb.append(readInt2);
                    throw new InvalidObjectException(sb.toString());
                }
            }
            try {
                FieldSettersHolder.MAP_FIELD_SETTER.set(this, (Object) builder.build());
                FieldSettersHolder.SIZE_FIELD_SETTER.set(this, i2);
            } catch (IllegalArgumentException e) {
                throw ((InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e));
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid key count ");
            sb2.append(readInt);
            throw new InvalidObjectException(sb2.toString());
        }
    }
}
