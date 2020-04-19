package com.google.api.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.WeakHashMap;

public class FieldInfo {
    private static final Map<Field, FieldInfo> CACHE = new WeakHashMap();
    private final Field field;
    private final boolean isPrimitive;
    private final String name;

    /* renamed from: of */
    public static FieldInfo m68of(Enum<?> enumR) {
        try {
            FieldInfo of = m69of(enumR.getClass().getField(enumR.name()));
            Preconditions.checkArgument(of != null, "enum constant missing @Value or @NullValue annotation: %s", enumR);
            return of;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006a, code lost:
        return r2;
     */
    /* renamed from: of */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.api.client.util.FieldInfo m69of(java.lang.reflect.Field r5) {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.util.Map<java.lang.reflect.Field, com.google.api.client.util.FieldInfo> r1 = CACHE
            monitor-enter(r1)
            java.util.Map<java.lang.reflect.Field, com.google.api.client.util.FieldInfo> r2 = CACHE     // Catch:{ all -> 0x006b }
            java.lang.Object r2 = r2.get(r5)     // Catch:{ all -> 0x006b }
            com.google.api.client.util.FieldInfo r2 = (com.google.api.client.util.FieldInfo) r2     // Catch:{ all -> 0x006b }
            boolean r3 = r5.isEnumConstant()     // Catch:{ all -> 0x006b }
            if (r2 != 0) goto L_0x0069
            if (r3 != 0) goto L_0x0021
            int r4 = r5.getModifiers()     // Catch:{ all -> 0x006b }
            boolean r4 = java.lang.reflect.Modifier.isStatic(r4)     // Catch:{ all -> 0x006b }
            if (r4 != 0) goto L_0x0069
        L_0x0021:
            if (r3 == 0) goto L_0x003f
            java.lang.Class<com.google.api.client.util.Value> r2 = com.google.api.client.util.Value.class
            java.lang.annotation.Annotation r2 = r5.getAnnotation(r2)     // Catch:{ all -> 0x006b }
            com.google.api.client.util.Value r2 = (com.google.api.client.util.Value) r2     // Catch:{ all -> 0x006b }
            if (r2 == 0) goto L_0x0032
            java.lang.String r0 = r2.value()     // Catch:{ all -> 0x006b }
            goto L_0x0053
        L_0x0032:
            java.lang.Class<com.google.api.client.util.NullValue> r2 = com.google.api.client.util.NullValue.class
            java.lang.annotation.Annotation r2 = r5.getAnnotation(r2)     // Catch:{ all -> 0x006b }
            com.google.api.client.util.NullValue r2 = (com.google.api.client.util.NullValue) r2     // Catch:{ all -> 0x006b }
            if (r2 == 0) goto L_0x003d
            goto L_0x0053
        L_0x003d:
            monitor-exit(r1)     // Catch:{ all -> 0x006b }
            return r0
        L_0x003f:
            java.lang.Class<com.google.api.client.util.Key> r2 = com.google.api.client.util.Key.class
            java.lang.annotation.Annotation r2 = r5.getAnnotation(r2)     // Catch:{ all -> 0x006b }
            com.google.api.client.util.Key r2 = (com.google.api.client.util.Key) r2     // Catch:{ all -> 0x006b }
            if (r2 != 0) goto L_0x004b
            monitor-exit(r1)     // Catch:{ all -> 0x006b }
            return r0
        L_0x004b:
            java.lang.String r0 = r2.value()     // Catch:{ all -> 0x006b }
            r2 = 1
            r5.setAccessible(r2)     // Catch:{ all -> 0x006b }
        L_0x0053:
            java.lang.String r2 = "##default"
            boolean r2 = r2.equals(r0)     // Catch:{ all -> 0x006b }
            if (r2 == 0) goto L_0x005f
            java.lang.String r0 = r5.getName()     // Catch:{ all -> 0x006b }
        L_0x005f:
            com.google.api.client.util.FieldInfo r2 = new com.google.api.client.util.FieldInfo     // Catch:{ all -> 0x006b }
            r2.<init>(r5, r0)     // Catch:{ all -> 0x006b }
            java.util.Map<java.lang.reflect.Field, com.google.api.client.util.FieldInfo> r0 = CACHE     // Catch:{ all -> 0x006b }
            r0.put(r5, r2)     // Catch:{ all -> 0x006b }
        L_0x0069:
            monitor-exit(r1)     // Catch:{ all -> 0x006b }
            return r2
        L_0x006b:
            r5 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x006b }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.util.FieldInfo.m69of(java.lang.reflect.Field):com.google.api.client.util.FieldInfo");
    }

    FieldInfo(Field field2, String str) {
        String str2;
        this.field = field2;
        if (str == null) {
            str2 = null;
        } else {
            str2 = str.intern();
        }
        this.name = str2;
        this.isPrimitive = Data.isPrimitive(getType());
    }

    public Field getField() {
        return this.field;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return this.field.getType();
    }

    public Type getGenericType() {
        return this.field.getGenericType();
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.field.getModifiers());
    }

    public boolean isPrimitive() {
        return this.isPrimitive;
    }

    public Object getValue(Object obj) {
        return getFieldValue(this.field, obj);
    }

    public void setValue(Object obj, Object obj2) {
        setFieldValue(this.field, obj, obj2);
    }

    public ClassInfo getClassInfo() {
        return ClassInfo.m66of(this.field.getDeclaringClass());
    }

    public <T extends Enum<T>> T enumValue() {
        return Enum.valueOf(this.field.getDeclaringClass(), this.field.getName());
    }

    public static Object getFieldValue(Field field2, Object obj) {
        try {
            return field2.get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void setFieldValue(Field field2, Object obj, Object obj2) {
        if (Modifier.isFinal(field2.getModifiers())) {
            Object fieldValue = getFieldValue(field2, obj);
            if (obj2 == null) {
                if (fieldValue == null) {
                    return;
                }
            } else if (obj2.equals(fieldValue)) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("expected final value <");
            sb.append(fieldValue);
            sb.append("> but was <");
            sb.append(obj2);
            sb.append("> on ");
            sb.append(field2.getName());
            sb.append(" field in ");
            sb.append(obj.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        try {
            field2.set(obj, obj2);
        } catch (SecurityException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException(e2);
        }
    }
}
