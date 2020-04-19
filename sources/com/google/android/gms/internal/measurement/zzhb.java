package com.google.android.gms.internal.measurement;

import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

class zzhb<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    private boolean zzadr;
    private final int zzakb;
    /* access modifiers changed from: private */
    public List<zzhi> zzakc;
    /* access modifiers changed from: private */
    public Map<K, V> zzakd;
    private volatile zzhk zzake;
    /* access modifiers changed from: private */
    public Map<K, V> zzakf;
    private volatile zzhe zzakg;

    static <FieldDescriptorType extends zzes<FieldDescriptorType>> zzhb<FieldDescriptorType, Object> zzbe(int i) {
        return new zzhc(i);
    }

    private zzhb(int i) {
        this.zzakb = i;
        this.zzakc = Collections.emptyList();
        this.zzakd = Collections.emptyMap();
        this.zzakf = Collections.emptyMap();
    }

    public void zzjz() {
        Map<K, V> map;
        Map<K, V> map2;
        if (!this.zzadr) {
            if (this.zzakd.isEmpty()) {
                map = Collections.emptyMap();
            } else {
                map = Collections.unmodifiableMap(this.zzakd);
            }
            this.zzakd = map;
            if (this.zzakf.isEmpty()) {
                map2 = Collections.emptyMap();
            } else {
                map2 = Collections.unmodifiableMap(this.zzakf);
            }
            this.zzakf = map2;
            this.zzadr = true;
        }
    }

    public final boolean isImmutable() {
        return this.zzadr;
    }

    public final int zzoi() {
        return this.zzakc.size();
    }

    public final Entry<K, V> zzbf(int i) {
        return (Entry) this.zzakc.get(i);
    }

    public final Iterable<Entry<K, V>> zzoj() {
        if (this.zzakd.isEmpty()) {
            return zzhf.zzoo();
        }
        return this.zzakd.entrySet();
    }

    public int size() {
        return this.zzakc.size() + this.zzakd.size();
    }

    public boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return zza((K) comparable) >= 0 || this.zzakd.containsKey(comparable);
    }

    public V get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int zza = zza((K) comparable);
        if (zza >= 0) {
            return ((zzhi) this.zzakc.get(zza)).getValue();
        }
        return this.zzakd.get(comparable);
    }

    /* renamed from: zza */
    public final V put(K k, V v) {
        zzol();
        int zza = zza(k);
        if (zza >= 0) {
            return ((zzhi) this.zzakc.get(zza)).setValue(v);
        }
        zzol();
        if (this.zzakc.isEmpty() && !(this.zzakc instanceof ArrayList)) {
            this.zzakc = new ArrayList(this.zzakb);
        }
        int i = -(zza + 1);
        if (i >= this.zzakb) {
            return zzom().put(k, v);
        }
        int size = this.zzakc.size();
        int i2 = this.zzakb;
        if (size == i2) {
            zzhi zzhi = (zzhi) this.zzakc.remove(i2 - 1);
            zzom().put((Comparable) zzhi.getKey(), zzhi.getValue());
        }
        this.zzakc.add(i, new zzhi(this, k, v));
        return null;
    }

    public void clear() {
        zzol();
        if (!this.zzakc.isEmpty()) {
            this.zzakc.clear();
        }
        if (!this.zzakd.isEmpty()) {
            this.zzakd.clear();
        }
    }

    public V remove(Object obj) {
        zzol();
        Comparable comparable = (Comparable) obj;
        int zza = zza((K) comparable);
        if (zza >= 0) {
            return zzbg(zza);
        }
        if (this.zzakd.isEmpty()) {
            return null;
        }
        return this.zzakd.remove(comparable);
    }

    /* access modifiers changed from: private */
    public final V zzbg(int i) {
        zzol();
        V value = ((zzhi) this.zzakc.remove(i)).getValue();
        if (!this.zzakd.isEmpty()) {
            Iterator it = zzom().entrySet().iterator();
            this.zzakc.add(new zzhi(this, (Entry) it.next()));
            it.remove();
        }
        return value;
    }

    private final int zza(K k) {
        int size = this.zzakc.size() - 1;
        if (size >= 0) {
            int compareTo = k.compareTo((Comparable) ((zzhi) this.zzakc.get(size)).getKey());
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) / 2;
            int compareTo2 = k.compareTo((Comparable) ((zzhi) this.zzakc.get(i2)).getKey());
            if (compareTo2 < 0) {
                size = i2 - 1;
            } else if (compareTo2 <= 0) {
                return i2;
            } else {
                i = i2 + 1;
            }
        }
        return -(i + 1);
    }

    public Set<Entry<K, V>> entrySet() {
        if (this.zzake == null) {
            this.zzake = new zzhk(this, null);
        }
        return this.zzake;
    }

    /* access modifiers changed from: 0000 */
    public final Set<Entry<K, V>> zzok() {
        if (this.zzakg == null) {
            this.zzakg = new zzhe(this, null);
        }
        return this.zzakg;
    }

    /* access modifiers changed from: private */
    public final void zzol() {
        if (this.zzadr) {
            throw new UnsupportedOperationException();
        }
    }

    private final SortedMap<K, V> zzom() {
        zzol();
        if (this.zzakd.isEmpty() && !(this.zzakd instanceof TreeMap)) {
            this.zzakd = new TreeMap();
            this.zzakf = ((TreeMap) this.zzakd).descendingMap();
        }
        return (SortedMap) this.zzakd;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzhb)) {
            return super.equals(obj);
        }
        zzhb zzhb = (zzhb) obj;
        int size = size();
        if (size != zzhb.size()) {
            return false;
        }
        int zzoi = zzoi();
        if (zzoi != zzhb.zzoi()) {
            return entrySet().equals(zzhb.entrySet());
        }
        for (int i = 0; i < zzoi; i++) {
            if (!zzbf(i).equals(zzhb.zzbf(i))) {
                return false;
            }
        }
        if (zzoi != size) {
            return this.zzakd.equals(zzhb.zzakd);
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        for (int i2 = 0; i2 < zzoi(); i2++) {
            i += ((zzhi) this.zzakc.get(i2)).hashCode();
        }
        return this.zzakd.size() > 0 ? i + this.zzakd.hashCode() : i;
    }

    /* synthetic */ zzhb(int i, zzhc zzhc) {
        this(i);
    }
}
