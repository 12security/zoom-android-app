package com.google.api.client.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public final class ArrayValueMap {
    private final Object destination;
    private final Map<Field, ArrayValue> fieldMap = ArrayMap.create();
    private final Map<String, ArrayValue> keyMap = ArrayMap.create();

    static class ArrayValue {
        final Class<?> componentType;
        final ArrayList<Object> values = new ArrayList<>();

        ArrayValue(Class<?> cls) {
            this.componentType = cls;
        }

        /* access modifiers changed from: 0000 */
        public Object toArray() {
            return Types.toArray(this.values, this.componentType);
        }

        /* access modifiers changed from: 0000 */
        public void addValue(Class<?> cls, Object obj) {
            Preconditions.checkArgument(cls == this.componentType);
            this.values.add(obj);
        }
    }

    public ArrayValueMap(Object obj) {
        this.destination = obj;
    }

    public void setValues() {
        for (Entry entry : this.keyMap.entrySet()) {
            ((Map) this.destination).put(entry.getKey(), ((ArrayValue) entry.getValue()).toArray());
        }
        for (Entry entry2 : this.fieldMap.entrySet()) {
            FieldInfo.setFieldValue((Field) entry2.getKey(), this.destination, ((ArrayValue) entry2.getValue()).toArray());
        }
    }

    public void put(Field field, Class<?> cls, Object obj) {
        ArrayValue arrayValue = (ArrayValue) this.fieldMap.get(field);
        if (arrayValue == null) {
            arrayValue = new ArrayValue(cls);
            this.fieldMap.put(field, arrayValue);
        }
        arrayValue.addValue(cls, obj);
    }

    public void put(String str, Class<?> cls, Object obj) {
        ArrayValue arrayValue = (ArrayValue) this.keyMap.get(str);
        if (arrayValue == null) {
            arrayValue = new ArrayValue(cls);
            this.keyMap.put(str, arrayValue);
        }
        arrayValue.addValue(cls, obj);
    }
}
