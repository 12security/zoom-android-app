package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class zzeq<FieldDescriptorType extends zzes<FieldDescriptorType>> {
    private static final zzeq zzadt = new zzeq(true);
    private final zzhb<FieldDescriptorType, Object> zzadq = zzhb.zzbe(16);
    private boolean zzadr;
    private boolean zzads = false;

    private zzeq() {
    }

    private zzeq(boolean z) {
        zzjz();
    }

    public static <T extends zzes<T>> zzeq<T> zzlx() {
        return zzadt;
    }

    /* access modifiers changed from: 0000 */
    public final boolean isEmpty() {
        return this.zzadq.isEmpty();
    }

    public final void zzjz() {
        if (!this.zzadr) {
            this.zzadq.zzjz();
            this.zzadr = true;
        }
    }

    public final boolean isImmutable() {
        return this.zzadr;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzeq)) {
            return false;
        }
        return this.zzadq.equals(((zzeq) obj).zzadq);
    }

    public final int hashCode() {
        return this.zzadq.hashCode();
    }

    public final Iterator<Entry<FieldDescriptorType, Object>> iterator() {
        if (this.zzads) {
            return new zzfn(this.zzadq.entrySet().iterator());
        }
        return this.zzadq.entrySet().iterator();
    }

    /* access modifiers changed from: 0000 */
    public final Iterator<Entry<FieldDescriptorType, Object>> descendingIterator() {
        if (this.zzads) {
            return new zzfn(this.zzadq.zzok().iterator());
        }
        return this.zzadq.zzok().iterator();
    }

    private final Object zza(FieldDescriptorType fielddescriptortype) {
        Object obj = this.zzadq.get(fielddescriptortype);
        return obj instanceof zzfk ? zzfk.zzne() : obj;
    }

    private final void zza(FieldDescriptorType fielddescriptortype, Object obj) {
        if (!fielddescriptortype.zzmc()) {
            zza(fielddescriptortype.zzma(), obj);
            r7 = obj;
        } else if (obj instanceof List) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj2 = arrayList2.get(i);
                i++;
                zza(fielddescriptortype.zzma(), obj2);
            }
            r7 = arrayList;
        } else {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
        if (r7 instanceof zzfk) {
            this.zzads = true;
        }
        this.zzadq.put(fielddescriptortype, r7);
    }

    private static void zza(zzif zzif, Object obj) {
        zzfb.checkNotNull(obj);
        boolean z = false;
        switch (zzer.zzadu[zzif.zzpb().ordinal()]) {
            case 1:
                z = obj instanceof Integer;
                break;
            case 2:
                z = obj instanceof Long;
                break;
            case 3:
                z = obj instanceof Float;
                break;
            case 4:
                z = obj instanceof Double;
                break;
            case 5:
                z = obj instanceof Boolean;
                break;
            case 6:
                z = obj instanceof String;
                break;
            case 7:
                if ((obj instanceof zzdp) || (obj instanceof byte[])) {
                    z = true;
                    break;
                }
            case 8:
                if ((obj instanceof Integer) || (obj instanceof zzfc)) {
                    z = true;
                    break;
                }
            case 9:
                if ((obj instanceof zzgh) || (obj instanceof zzfk)) {
                    z = true;
                    break;
                }
        }
        if (!z) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    public final boolean isInitialized() {
        for (int i = 0; i < this.zzadq.zzoi(); i++) {
            if (!zzb(this.zzadq.zzbf(i))) {
                return false;
            }
        }
        for (Entry zzb : this.zzadq.zzoj()) {
            if (!zzb(zzb)) {
                return false;
            }
        }
        return true;
    }

    private static boolean zzb(Entry<FieldDescriptorType, Object> entry) {
        zzes zzes = (zzes) entry.getKey();
        if (zzes.zzmb() == zzik.MESSAGE) {
            if (zzes.zzmc()) {
                for (zzgh isInitialized : (List) entry.getValue()) {
                    if (!isInitialized.isInitialized()) {
                        return false;
                    }
                }
            } else {
                Object value = entry.getValue();
                if (value instanceof zzgh) {
                    if (!((zzgh) value).isInitialized()) {
                        return false;
                    }
                } else if (value instanceof zzfk) {
                    return true;
                } else {
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
            }
        }
        return true;
    }

    public final void zza(zzeq<FieldDescriptorType> zzeq) {
        for (int i = 0; i < zzeq.zzadq.zzoi(); i++) {
            zzc(zzeq.zzadq.zzbf(i));
        }
        for (Entry zzc : zzeq.zzadq.zzoj()) {
            zzc(zzc);
        }
    }

    private static Object zzj(Object obj) {
        if (obj instanceof zzgo) {
            return ((zzgo) obj).zznv();
        }
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    private final void zzc(Entry<FieldDescriptorType, Object> entry) {
        Object obj;
        zzes zzes = (zzes) entry.getKey();
        Object value = entry.getValue();
        if (value instanceof zzfk) {
            value = zzfk.zzne();
        }
        if (zzes.zzmc()) {
            Object zza = zza((FieldDescriptorType) zzes);
            if (zza == null) {
                zza = new ArrayList();
            }
            for (Object zzj : (List) value) {
                ((List) zza).add(zzj(zzj));
            }
            this.zzadq.put(zzes, zza);
        } else if (zzes.zzmb() == zzik.MESSAGE) {
            Object zza2 = zza((FieldDescriptorType) zzes);
            if (zza2 == null) {
                this.zzadq.put(zzes, zzj(value));
                return;
            }
            if (zza2 instanceof zzgo) {
                obj = zzes.zza((zzgo) zza2, (zzgo) value);
            } else {
                obj = zzes.zza(((zzgh) zza2).zzmk(), (zzgh) value).zzmr();
            }
            this.zzadq.put(zzes, obj);
        } else {
            this.zzadq.put(zzes, zzj(value));
        }
    }

    static void zza(zzeg zzeg, zzif zzif, int i, Object obj) throws IOException {
        if (zzif == zzif.GROUP) {
            zzgh zzgh = (zzgh) obj;
            zzfb.zzf(zzgh);
            zzeg.zzb(i, 3);
            zzgh.zzb(zzeg);
            zzeg.zzb(i, 4);
            return;
        }
        zzeg.zzb(i, zzif.zzpc());
        switch (zzer.zzacu[zzif.ordinal()]) {
            case 1:
                zzeg.zzd(((Double) obj).doubleValue());
                return;
            case 2:
                zzeg.zza(((Float) obj).floatValue());
                return;
            case 3:
                zzeg.zzaq(((Long) obj).longValue());
                return;
            case 4:
                zzeg.zzaq(((Long) obj).longValue());
                return;
            case 5:
                zzeg.zzaf(((Integer) obj).intValue());
                return;
            case 6:
                zzeg.zzas(((Long) obj).longValue());
                return;
            case 7:
                zzeg.zzai(((Integer) obj).intValue());
                return;
            case 8:
                zzeg.zzm(((Boolean) obj).booleanValue());
                return;
            case 9:
                ((zzgh) obj).zzb(zzeg);
                return;
            case 10:
                zzeg.zzb((zzgh) obj);
                return;
            case 11:
                if (obj instanceof zzdp) {
                    zzeg.zza((zzdp) obj);
                    return;
                } else {
                    zzeg.zzco((String) obj);
                    return;
                }
            case 12:
                if (obj instanceof zzdp) {
                    zzeg.zza((zzdp) obj);
                    return;
                }
                byte[] bArr = (byte[]) obj;
                zzeg.zze(bArr, 0, bArr.length);
                return;
            case 13:
                zzeg.zzag(((Integer) obj).intValue());
                return;
            case 14:
                zzeg.zzai(((Integer) obj).intValue());
                return;
            case 15:
                zzeg.zzas(((Long) obj).longValue());
                return;
            case 16:
                zzeg.zzah(((Integer) obj).intValue());
                return;
            case 17:
                zzeg.zzar(((Long) obj).longValue());
                return;
            case 18:
                if (!(obj instanceof zzfc)) {
                    zzeg.zzaf(((Integer) obj).intValue());
                    break;
                } else {
                    zzeg.zzaf(((zzfc) obj).zzgp());
                    return;
                }
        }
    }

    public final int zzly() {
        int i = 0;
        for (int i2 = 0; i2 < this.zzadq.zzoi(); i2++) {
            Entry zzbf = this.zzadq.zzbf(i2);
            i += zzb((zzes) zzbf.getKey(), zzbf.getValue());
        }
        for (Entry entry : this.zzadq.zzoj()) {
            i += zzb((zzes) entry.getKey(), entry.getValue());
        }
        return i;
    }

    public final int zzlz() {
        int i = 0;
        for (int i2 = 0; i2 < this.zzadq.zzoi(); i2++) {
            i += zzd(this.zzadq.zzbf(i2));
        }
        for (Entry zzd : this.zzadq.zzoj()) {
            i += zzd(zzd);
        }
        return i;
    }

    private static int zzd(Entry<FieldDescriptorType, Object> entry) {
        zzes zzes = (zzes) entry.getKey();
        Object value = entry.getValue();
        if (zzes.zzmb() != zzik.MESSAGE || zzes.zzmc() || zzes.zzmd()) {
            return zzb(zzes, value);
        }
        if (value instanceof zzfk) {
            return zzeg.zzb(((zzes) entry.getKey()).zzgp(), (zzfo) (zzfk) value);
        }
        return zzeg.zzd(((zzes) entry.getKey()).zzgp(), (zzgh) value);
    }

    static int zza(zzif zzif, int i, Object obj) {
        int zzaj = zzeg.zzaj(i);
        if (zzif == zzif.GROUP) {
            zzfb.zzf((zzgh) obj);
            zzaj <<= 1;
        }
        return zzaj + zzb(zzif, obj);
    }

    private static int zzb(zzif zzif, Object obj) {
        switch (zzer.zzacu[zzif.ordinal()]) {
            case 1:
                return zzeg.zze(((Double) obj).doubleValue());
            case 2:
                return zzeg.zzb(((Float) obj).floatValue());
            case 3:
                return zzeg.zzat(((Long) obj).longValue());
            case 4:
                return zzeg.zzau(((Long) obj).longValue());
            case 5:
                return zzeg.zzak(((Integer) obj).intValue());
            case 6:
                return zzeg.zzaw(((Long) obj).longValue());
            case 7:
                return zzeg.zzan(((Integer) obj).intValue());
            case 8:
                return zzeg.zzn(((Boolean) obj).booleanValue());
            case 9:
                return zzeg.zzd((zzgh) obj);
            case 10:
                if (obj instanceof zzfk) {
                    return zzeg.zza((zzfo) (zzfk) obj);
                }
                return zzeg.zzc((zzgh) obj);
            case 11:
                if (obj instanceof zzdp) {
                    return zzeg.zzb((zzdp) obj);
                }
                return zzeg.zzcp((String) obj);
            case 12:
                if (obj instanceof zzdp) {
                    return zzeg.zzb((zzdp) obj);
                }
                return zzeg.zzi((byte[]) obj);
            case 13:
                return zzeg.zzal(((Integer) obj).intValue());
            case 14:
                return zzeg.zzao(((Integer) obj).intValue());
            case 15:
                return zzeg.zzax(((Long) obj).longValue());
            case 16:
                return zzeg.zzam(((Integer) obj).intValue());
            case 17:
                return zzeg.zzav(((Long) obj).longValue());
            case 18:
                if (obj instanceof zzfc) {
                    return zzeg.zzap(((zzfc) obj).zzgp());
                }
                return zzeg.zzap(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    private static int zzb(zzes<?> zzes, Object obj) {
        zzif zzma = zzes.zzma();
        int zzgp = zzes.zzgp();
        if (!zzes.zzmc()) {
            return zza(zzma, zzgp, obj);
        }
        int i = 0;
        if (zzes.zzmd()) {
            for (Object zzb : (List) obj) {
                i += zzb(zzma, zzb);
            }
            return zzeg.zzaj(zzgp) + i + zzeg.zzar(i);
        }
        for (Object zza : (List) obj) {
            i += zza(zzma, zzgp, zza);
        }
        return i;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzeq zzeq = new zzeq();
        for (int i = 0; i < this.zzadq.zzoi(); i++) {
            Entry zzbf = this.zzadq.zzbf(i);
            zzeq.zza((FieldDescriptorType) (zzes) zzbf.getKey(), zzbf.getValue());
        }
        for (Entry entry : this.zzadq.zzoj()) {
            zzeq.zza((FieldDescriptorType) (zzes) entry.getKey(), entry.getValue());
        }
        zzeq.zzads = this.zzads;
        return zzeq;
    }
}
