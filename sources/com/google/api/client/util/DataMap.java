package com.google.api.client.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

final class DataMap extends AbstractMap<String, Object> {
    final ClassInfo classInfo;
    final Object object;

    final class Entry implements java.util.Map.Entry<String, Object> {
        private final FieldInfo fieldInfo;
        private Object fieldValue;

        Entry(FieldInfo fieldInfo2, Object obj) {
            this.fieldInfo = fieldInfo2;
            this.fieldValue = Preconditions.checkNotNull(obj);
        }

        public String getKey() {
            String name = this.fieldInfo.getName();
            return DataMap.this.classInfo.getIgnoreCase() ? name.toLowerCase(Locale.US) : name;
        }

        public Object getValue() {
            return this.fieldValue;
        }

        public Object setValue(Object obj) {
            Object obj2 = this.fieldValue;
            this.fieldValue = Preconditions.checkNotNull(obj);
            this.fieldInfo.setValue(DataMap.this.object, obj);
            return obj2;
        }

        public int hashCode() {
            return getKey().hashCode() ^ getValue().hashCode();
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
            if (!getKey().equals(entry.getKey()) || !getValue().equals(entry.getValue())) {
                z = false;
            }
            return z;
        }
    }

    final class EntryIterator implements Iterator<java.util.Map.Entry<String, Object>> {
        private FieldInfo currentFieldInfo;
        private boolean isComputed;
        private boolean isRemoved;
        private FieldInfo nextFieldInfo;
        private Object nextFieldValue;
        private int nextKeyIndex = -1;

        EntryIterator() {
        }

        public boolean hasNext() {
            if (!this.isComputed) {
                this.isComputed = true;
                this.nextFieldValue = null;
                while (this.nextFieldValue == null) {
                    int i = this.nextKeyIndex + 1;
                    this.nextKeyIndex = i;
                    if (i >= DataMap.this.classInfo.names.size()) {
                        break;
                    }
                    this.nextFieldInfo = DataMap.this.classInfo.getFieldInfo((String) DataMap.this.classInfo.names.get(this.nextKeyIndex));
                    this.nextFieldValue = this.nextFieldInfo.getValue(DataMap.this.object);
                }
            }
            if (this.nextFieldValue != null) {
                return true;
            }
            return false;
        }

        public java.util.Map.Entry<String, Object> next() {
            if (hasNext()) {
                this.currentFieldInfo = this.nextFieldInfo;
                Object obj = this.nextFieldValue;
                this.isComputed = false;
                this.isRemoved = false;
                this.nextFieldInfo = null;
                this.nextFieldValue = null;
                return new Entry(this.currentFieldInfo, obj);
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Preconditions.checkState(this.currentFieldInfo != null && !this.isRemoved);
            this.isRemoved = true;
            this.currentFieldInfo.setValue(DataMap.this.object, null);
        }
    }

    final class EntrySet extends AbstractSet<java.util.Map.Entry<String, Object>> {
        EntrySet() {
        }

        public EntryIterator iterator() {
            return new EntryIterator();
        }

        public int size() {
            int i = 0;
            for (String fieldInfo : DataMap.this.classInfo.names) {
                if (DataMap.this.classInfo.getFieldInfo(fieldInfo).getValue(DataMap.this.object) != null) {
                    i++;
                }
            }
            return i;
        }

        public void clear() {
            for (String fieldInfo : DataMap.this.classInfo.names) {
                DataMap.this.classInfo.getFieldInfo(fieldInfo).setValue(DataMap.this.object, null);
            }
        }

        public boolean isEmpty() {
            for (String fieldInfo : DataMap.this.classInfo.names) {
                if (DataMap.this.classInfo.getFieldInfo(fieldInfo).getValue(DataMap.this.object) != null) {
                    return false;
                }
            }
            return true;
        }
    }

    DataMap(Object obj, boolean z) {
        this.object = obj;
        this.classInfo = ClassInfo.m67of(obj.getClass(), z);
    }

    public EntrySet entrySet() {
        return new EntrySet();
    }

    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    public Object get(Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }
        FieldInfo fieldInfo = this.classInfo.getFieldInfo((String) obj);
        if (fieldInfo == null) {
            return null;
        }
        return fieldInfo.getValue(this.object);
    }

    public Object put(String str, Object obj) {
        FieldInfo fieldInfo = this.classInfo.getFieldInfo(str);
        StringBuilder sb = new StringBuilder();
        sb.append("no field of key ");
        sb.append(str);
        Preconditions.checkNotNull(fieldInfo, sb.toString());
        Object value = fieldInfo.getValue(this.object);
        fieldInfo.setValue(this.object, Preconditions.checkNotNull(obj));
        return value;
    }
}
