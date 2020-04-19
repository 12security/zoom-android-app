package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.primitives.Primitives;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@GwtIncompatible
public final class MutableClassToInstanceMap<B> extends ConstrainedMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
    private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>() {
        public void checkKeyValue(Class<?> cls, Object obj) {
            MutableClassToInstanceMap.cast(cls, obj);
        }
    };

    private static final class SerializedForm<B> implements Serializable {
        private static final long serialVersionUID = 0;
        private final Map<Class<? extends B>, B> backingMap;

        SerializedForm(Map<Class<? extends B>, B> map) {
            this.backingMap = map;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return MutableClassToInstanceMap.create(this.backingMap);
        }
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public /* bridge */ /* synthetic */ void putAll(Map map) {
        super.putAll(map);
    }

    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap<>(new HashMap());
    }

    public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> map) {
        return new MutableClassToInstanceMap<>(map);
    }

    private MutableClassToInstanceMap(Map<Class<? extends B>, B> map) {
        super(map, VALUE_CAN_BE_CAST_TO_KEY);
    }

    @CanIgnoreReturnValue
    public <T extends B> T putInstance(Class<T> cls, T t) {
        return cast(cls, put(cls, t));
    }

    public <T extends B> T getInstance(Class<T> cls) {
        return cast(cls, get(cls));
    }

    /* access modifiers changed from: private */
    @CanIgnoreReturnValue
    public static <B, T extends B> T cast(Class<T> cls, B b) {
        return Primitives.wrap(cls).cast(b);
    }

    private Object writeReplace() {
        return new SerializedForm(delegate());
    }
}
