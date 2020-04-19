package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public final class Functions {

    private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
        private static final long serialVersionUID = 0;
        private final E value;

        public ConstantFunction(@Nullable E e) {
            this.value = e;
        }

        public E apply(@Nullable Object obj) {
            return this.value;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ConstantFunction)) {
                return false;
            }
            return Objects.equal(this.value, ((ConstantFunction) obj).value);
        }

        public int hashCode() {
            E e = this.value;
            if (e == null) {
                return 0;
            }
            return e.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Functions.constant(");
            sb.append(this.value);
            sb.append(")");
            return sb.toString();
        }
    }

    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final V defaultValue;
        final Map<K, ? extends V> map;

        ForMapWithDefault(Map<K, ? extends V> map2, @Nullable V v) {
            this.map = (Map) Preconditions.checkNotNull(map2);
            this.defaultValue = v;
        }

        public V apply(@Nullable K k) {
            V v = this.map.get(k);
            return (v != null || this.map.containsKey(k)) ? v : this.defaultValue;
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof ForMapWithDefault)) {
                return false;
            }
            ForMapWithDefault forMapWithDefault = (ForMapWithDefault) obj;
            if (this.map.equals(forMapWithDefault.map) && Objects.equal(this.defaultValue, forMapWithDefault.defaultValue)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hashCode(this.map, this.defaultValue);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Functions.forMap(");
            sb.append(this.map);
            sb.append(", defaultValue=");
            sb.append(this.defaultValue);
            sb.append(")");
            return sb.toString();
        }
    }

    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: f */
        private final Function<A, ? extends B> f233f;

        /* renamed from: g */
        private final Function<B, C> f234g;

        public FunctionComposition(Function<B, C> function, Function<A, ? extends B> function2) {
            this.f234g = (Function) Preconditions.checkNotNull(function);
            this.f233f = (Function) Preconditions.checkNotNull(function2);
        }

        public C apply(@Nullable A a) {
            return this.f234g.apply(this.f233f.apply(a));
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof FunctionComposition)) {
                return false;
            }
            FunctionComposition functionComposition = (FunctionComposition) obj;
            if (this.f233f.equals(functionComposition.f233f) && this.f234g.equals(functionComposition.f234g)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.f233f.hashCode() ^ this.f234g.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.f234g);
            sb.append("(");
            sb.append(this.f233f);
            sb.append(")");
            return sb.toString();
        }
    }

    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final Map<K, V> map;

        FunctionForMapNoDefault(Map<K, V> map2) {
            this.map = (Map) Preconditions.checkNotNull(map2);
        }

        public V apply(@Nullable K k) {
            V v = this.map.get(k);
            Preconditions.checkArgument(v != null || this.map.containsKey(k), "Key '%s' not present in map", (Object) k);
            return v;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof FunctionForMapNoDefault)) {
                return false;
            }
            return this.map.equals(((FunctionForMapNoDefault) obj).map);
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Functions.forMap(");
            sb.append(this.map);
            sb.append(")");
            return sb.toString();
        }
    }

    private enum IdentityFunction implements Function<Object, Object> {
        INSTANCE;

        @Nullable
        public Object apply(@Nullable Object obj) {
            return obj;
        }

        public String toString() {
            return "Functions.identity()";
        }
    }

    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
        private static final long serialVersionUID = 0;
        private final Predicate<T> predicate;

        private PredicateFunction(Predicate<T> predicate2) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate2);
        }

        public Boolean apply(@Nullable T t) {
            return Boolean.valueOf(this.predicate.apply(t));
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof PredicateFunction)) {
                return false;
            }
            return this.predicate.equals(((PredicateFunction) obj).predicate);
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Functions.forPredicate(");
            sb.append(this.predicate);
            sb.append(")");
            return sb.toString();
        }
    }

    private static class SupplierFunction<T> implements Function<Object, T>, Serializable {
        private static final long serialVersionUID = 0;
        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> supplier2) {
            this.supplier = (Supplier) Preconditions.checkNotNull(supplier2);
        }

        public T apply(@Nullable Object obj) {
            return this.supplier.get();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof SupplierFunction)) {
                return false;
            }
            return this.supplier.equals(((SupplierFunction) obj).supplier);
        }

        public int hashCode() {
            return this.supplier.hashCode();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Functions.forSupplier(");
            sb.append(this.supplier);
            sb.append(")");
            return sb.toString();
        }
    }

    private enum ToStringFunction implements Function<Object, String> {
        INSTANCE;

        public String toString() {
            return "Functions.toStringFunction()";
        }

        public String apply(Object obj) {
            Preconditions.checkNotNull(obj);
            return obj.toString();
        }
    }

    private Functions() {
    }

    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }

    public static <E> Function<E, E> identity() {
        return IdentityFunction.INSTANCE;
    }

    public static <K, V> Function<K, V> forMap(Map<K, V> map) {
        return new FunctionForMapNoDefault(map);
    }

    public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V v) {
        return new ForMapWithDefault(map, v);
    }

    public static <A, B, C> Function<A, C> compose(Function<B, C> function, Function<A, ? extends B> function2) {
        return new FunctionComposition(function, function2);
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
        return new PredicateFunction(predicate);
    }

    public static <E> Function<Object, E> constant(@Nullable E e) {
        return new ConstantFunction(e);
    }

    public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
        return new SupplierFunction(supplier);
    }
}
