package com.google.api.client.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GenericData extends AbstractMap<String, Object> implements Cloneable {
    final ClassInfo classInfo;
    Map<String, Object> unknownFields;

    final class EntryIterator implements Iterator<Entry<String, Object>> {
        private final Iterator<Entry<String, Object>> fieldIterator;
        private boolean startedUnknown;
        private final Iterator<Entry<String, Object>> unknownIterator;

        EntryIterator(EntrySet entrySet) {
            this.fieldIterator = entrySet.iterator();
            this.unknownIterator = GenericData.this.unknownFields.entrySet().iterator();
        }

        public boolean hasNext() {
            return this.fieldIterator.hasNext() || this.unknownIterator.hasNext();
        }

        public Entry<String, Object> next() {
            if (!this.startedUnknown) {
                if (this.fieldIterator.hasNext()) {
                    return (Entry) this.fieldIterator.next();
                }
                this.startedUnknown = true;
            }
            return (Entry) this.unknownIterator.next();
        }

        public void remove() {
            if (this.startedUnknown) {
                this.unknownIterator.remove();
            }
            this.fieldIterator.remove();
        }
    }

    final class EntrySet extends AbstractSet<Entry<String, Object>> {
        private final EntrySet dataEntrySet;

        EntrySet() {
            this.dataEntrySet = new DataMap(GenericData.this, GenericData.this.classInfo.getIgnoreCase()).entrySet();
        }

        public Iterator<Entry<String, Object>> iterator() {
            return new EntryIterator(this.dataEntrySet);
        }

        public int size() {
            return GenericData.this.unknownFields.size() + this.dataEntrySet.size();
        }

        public void clear() {
            GenericData.this.unknownFields.clear();
            this.dataEntrySet.clear();
        }
    }

    public enum Flags {
        IGNORE_CASE
    }

    public GenericData() {
        this(EnumSet.noneOf(Flags.class));
    }

    public GenericData(EnumSet<Flags> enumSet) {
        this.unknownFields = ArrayMap.create();
        this.classInfo = ClassInfo.m67of(getClass(), enumSet.contains(Flags.IGNORE_CASE));
    }

    public final Object get(Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }
        String str = (String) obj;
        FieldInfo fieldInfo = this.classInfo.getFieldInfo(str);
        if (fieldInfo != null) {
            return fieldInfo.getValue(this);
        }
        if (this.classInfo.getIgnoreCase()) {
            str = str.toLowerCase(Locale.US);
        }
        return this.unknownFields.get(str);
    }

    public final Object put(String str, Object obj) {
        FieldInfo fieldInfo = this.classInfo.getFieldInfo(str);
        if (fieldInfo != null) {
            Object value = fieldInfo.getValue(this);
            fieldInfo.setValue(this, obj);
            return value;
        }
        if (this.classInfo.getIgnoreCase()) {
            str = str.toLowerCase(Locale.US);
        }
        return this.unknownFields.put(str, obj);
    }

    public GenericData set(String str, Object obj) {
        FieldInfo fieldInfo = this.classInfo.getFieldInfo(str);
        if (fieldInfo != null) {
            fieldInfo.setValue(this, obj);
        } else {
            if (this.classInfo.getIgnoreCase()) {
                str = str.toLowerCase(Locale.US);
            }
            this.unknownFields.put(str, obj);
        }
        return this;
    }

    public final void putAll(Map<? extends String, ?> map) {
        for (Entry entry : map.entrySet()) {
            set((String) entry.getKey(), entry.getValue());
        }
    }

    public final Object remove(Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }
        String str = (String) obj;
        if (this.classInfo.getFieldInfo(str) == null) {
            if (this.classInfo.getIgnoreCase()) {
                str = str.toLowerCase(Locale.US);
            }
            return this.unknownFields.remove(str);
        }
        throw new UnsupportedOperationException();
    }

    public Set<Entry<String, Object>> entrySet() {
        return new EntrySet();
    }

    public GenericData clone() {
        try {
            GenericData genericData = (GenericData) super.clone();
            Data.deepCopy(this, genericData);
            genericData.unknownFields = (Map) Data.clone(this.unknownFields);
            return genericData;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    public final Map<String, Object> getUnknownKeys() {
        return this.unknownFields;
    }

    public final void setUnknownKeys(Map<String, Object> map) {
        this.unknownFields = map;
    }

    public final ClassInfo getClassInfo() {
        return this.classInfo;
    }
}
