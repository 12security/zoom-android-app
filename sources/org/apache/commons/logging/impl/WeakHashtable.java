package org.apache.commons.logging.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public final class WeakHashtable extends Hashtable {
    private static final int MAX_CHANGES_BEFORE_PURGE = 100;
    private static final int PARTIAL_PURGE_COUNT = 10;
    private static final long serialVersionUID = -1546036869799732453L;
    private int changeCount = 0;
    private final ReferenceQueue queue = new ReferenceQueue();

    private static final class Entry implements java.util.Map.Entry {
        private final Object key;
        private final Object value;

        private Entry(Object obj, Object obj2) {
            this.key = obj;
            this.value = obj2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof java.util.Map.Entry)) {
                return false;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry) obj;
            if (getKey() == null) {
                if (entry.getKey() != null) {
                    return false;
                }
            } else if (!getKey().equals(entry.getKey())) {
                return false;
            }
            if (getValue() == null) {
                if (entry.getValue() != null) {
                    return false;
                }
            } else if (!getValue().equals(entry.getValue())) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = getKey() == null ? 0 : getKey().hashCode();
            if (getValue() != null) {
                i = getValue().hashCode();
            }
            return hashCode ^ i;
        }

        public Object setValue(Object obj) {
            throw new UnsupportedOperationException("Entry.setValue is not supported.");
        }

        public Object getValue() {
            return this.value;
        }

        public Object getKey() {
            return this.key;
        }
    }

    private static final class Referenced {
        private final int hashCode;
        private final WeakReference reference;

        private Referenced(Object obj) {
            this.reference = new WeakReference(obj);
            this.hashCode = obj.hashCode();
        }

        private Referenced(Object obj, ReferenceQueue referenceQueue) {
            this.reference = new WeakKey(obj, referenceQueue, this);
            this.hashCode = obj.hashCode();
        }

        public int hashCode() {
            return this.hashCode;
        }

        /* access modifiers changed from: private */
        public Object getValue() {
            return this.reference.get();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Referenced)) {
                return false;
            }
            Referenced referenced = (Referenced) obj;
            Object value = getValue();
            Object value2 = referenced.getValue();
            if (value != null) {
                return value.equals(value2);
            }
            if (!(value2 == null) || hashCode() != referenced.hashCode()) {
                return false;
            }
            return true;
        }
    }

    private static final class WeakKey extends WeakReference {
        private final Referenced referenced;

        private WeakKey(Object obj, ReferenceQueue referenceQueue, Referenced referenced2) {
            super(obj, referenceQueue);
            this.referenced = referenced2;
        }

        /* access modifiers changed from: private */
        public Referenced getReferenced() {
            return this.referenced;
        }
    }

    public boolean containsKey(Object obj) {
        return super.containsKey(new Referenced(obj));
    }

    public Enumeration elements() {
        purge();
        return super.elements();
    }

    public Set entrySet() {
        purge();
        Set<java.util.Map.Entry> entrySet = super.entrySet();
        HashSet hashSet = new HashSet();
        for (java.util.Map.Entry entry : entrySet) {
            Object access$100 = ((Referenced) entry.getKey()).getValue();
            Object value = entry.getValue();
            if (access$100 != null) {
                hashSet.add(new Entry(access$100, value));
            }
        }
        return hashSet;
    }

    public Object get(Object obj) {
        return super.get(new Referenced(obj));
    }

    public Enumeration keys() {
        purge();
        final Enumeration keys = super.keys();
        return new Enumeration() {
            public boolean hasMoreElements() {
                return keys.hasMoreElements();
            }

            public Object nextElement() {
                return ((Referenced) keys.nextElement()).getValue();
            }
        };
    }

    public Set keySet() {
        purge();
        Set<Referenced> keySet = super.keySet();
        HashSet hashSet = new HashSet();
        for (Referenced access$100 : keySet) {
            Object access$1002 = access$100.getValue();
            if (access$1002 != null) {
                hashSet.add(access$1002);
            }
        }
        return hashSet;
    }

    public synchronized Object put(Object obj, Object obj2) {
        if (obj == null) {
            throw new NullPointerException("Null keys are not allowed");
        } else if (obj2 != null) {
            int i = this.changeCount;
            this.changeCount = i + 1;
            if (i > 100) {
                purge();
                this.changeCount = 0;
            } else if (this.changeCount % 10 == 0) {
                purgeOne();
            }
        } else {
            throw new NullPointerException("Null values are not allowed");
        }
        return super.put(new Referenced(obj, this.queue), obj2);
    }

    public void putAll(Map map) {
        if (map != null) {
            for (java.util.Map.Entry entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public Collection values() {
        purge();
        return super.values();
    }

    public synchronized Object remove(Object obj) {
        int i = this.changeCount;
        this.changeCount = i + 1;
        if (i > 100) {
            purge();
            this.changeCount = 0;
        } else if (this.changeCount % 10 == 0) {
            purgeOne();
        }
        return super.remove(new Referenced(obj));
    }

    public boolean isEmpty() {
        purge();
        return super.isEmpty();
    }

    public int size() {
        purge();
        return super.size();
    }

    public String toString() {
        purge();
        return super.toString();
    }

    /* access modifiers changed from: protected */
    public void rehash() {
        purge();
        super.rehash();
    }

    private void purge() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.queue) {
            while (true) {
                WeakKey weakKey = (WeakKey) this.queue.poll();
                if (weakKey != null) {
                    arrayList.add(weakKey.getReferenced());
                }
            }
            while (true) {
            }
        }
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            super.remove(arrayList.get(i));
        }
    }

    private void purgeOne() {
        synchronized (this.queue) {
            WeakKey weakKey = (WeakKey) this.queue.poll();
            if (weakKey != null) {
                super.remove(weakKey.getReferenced());
            }
        }
    }
}
