package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

final class zzha {
    private static final Class<?> zzajx = zzog();
    private static final zzhq<?, ?> zzajy = zzp(false);
    private static final zzhq<?, ?> zzajz = zzp(true);
    private static final zzhq<?, ?> zzaka = new zzhs();

    public static void zzg(Class<?> cls) {
        if (!zzez.class.isAssignableFrom(cls)) {
            Class<?> cls2 = zzajx;
            if (cls2 != null && !cls2.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
            }
        }
    }

    public static void zza(int i, List<Double> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzg(i, list, z);
        }
    }

    public static void zzb(int i, List<Float> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzf(i, list, z);
        }
    }

    public static void zzc(int i, List<Long> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzc(i, list, z);
        }
    }

    public static void zzd(int i, List<Long> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzd(i, list, z);
        }
    }

    public static void zze(int i, List<Long> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzn(i, list, z);
        }
    }

    public static void zzf(int i, List<Long> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zze(i, list, z);
        }
    }

    public static void zzg(int i, List<Long> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzl(i, list, z);
        }
    }

    public static void zzh(int i, List<Integer> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zza(i, list, z);
        }
    }

    public static void zzi(int i, List<Integer> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzj(i, list, z);
        }
    }

    public static void zzj(int i, List<Integer> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzm(i, list, z);
        }
    }

    public static void zzk(int i, List<Integer> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzb(i, list, z);
        }
    }

    public static void zzl(int i, List<Integer> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzk(i, list, z);
        }
    }

    public static void zzm(int i, List<Integer> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzh(i, list, z);
        }
    }

    public static void zzn(int i, List<Boolean> list, zzil zzil, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzi(i, list, z);
        }
    }

    public static void zza(int i, List<String> list, zzil zzil) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zza(i, list);
        }
    }

    public static void zzb(int i, List<zzdp> list, zzil zzil) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzb(i, list);
        }
    }

    public static void zza(int i, List<?> list, zzil zzil, zzgy zzgy) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zza(i, list, zzgy);
        }
    }

    public static void zzb(int i, List<?> list, zzil zzil, zzgy zzgy) throws IOException {
        if (list != null && !list.isEmpty()) {
            zzil.zzb(i, list, zzgy);
        }
    }

    static int zzt(List<Long> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            i = 0;
            while (i2 < size) {
                i += zzeg.zzat(zzfv.getLong(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + zzeg.zzat(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }

    static int zzo(int i, List<Long> list, boolean z) {
        if (list.size() == 0) {
            return 0;
        }
        return zzt(list) + (list.size() * zzeg.zzaj(i));
    }

    static int zzu(List<Long> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            i = 0;
            while (i2 < size) {
                i += zzeg.zzau(zzfv.getLong(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + zzeg.zzau(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }

    static int zzp(int i, List<Long> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzu(list) + (size * zzeg.zzaj(i));
    }

    static int zzv(List<Long> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfv) {
            zzfv zzfv = (zzfv) list;
            i = 0;
            while (i2 < size) {
                i += zzeg.zzav(zzfv.getLong(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + zzeg.zzav(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }

    static int zzq(int i, List<Long> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzv(list) + (size * zzeg.zzaj(i));
    }

    static int zzw(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            i = 0;
            while (i2 < size) {
                i += zzeg.zzap(zzfa.getInt(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + zzeg.zzap(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    static int zzr(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzw(list) + (size * zzeg.zzaj(i));
    }

    static int zzx(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            i = 0;
            while (i2 < size) {
                i += zzeg.zzak(zzfa.getInt(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + zzeg.zzak(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    static int zzs(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzx(list) + (size * zzeg.zzaj(i));
    }

    static int zzy(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            i = 0;
            while (i2 < size) {
                i += zzeg.zzal(zzfa.getInt(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + zzeg.zzal(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    static int zzt(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzy(list) + (size * zzeg.zzaj(i));
    }

    static int zzz(List<Integer> list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof zzfa) {
            zzfa zzfa = (zzfa) list;
            i = 0;
            while (i2 < size) {
                i += zzeg.zzam(zzfa.getInt(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + zzeg.zzam(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    static int zzu(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzz(list) + (size * zzeg.zzaj(i));
    }

    static int zzaa(List<?> list) {
        return list.size() << 2;
    }

    static int zzv(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzeg.zzj(i, 0);
    }

    static int zzab(List<?> list) {
        return list.size() << 3;
    }

    static int zzw(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzeg.zzg(i, 0);
    }

    static int zzac(List<?> list) {
        return list.size();
    }

    static int zzx(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzeg.zzc(i, true);
    }

    static int zzc(int i, List<?> list) {
        int i2;
        int i3;
        int size = list.size();
        int i4 = 0;
        if (size == 0) {
            return 0;
        }
        int zzaj = zzeg.zzaj(i) * size;
        if (list instanceof zzfq) {
            zzfq zzfq = (zzfq) list;
            while (i4 < size) {
                Object zzaw = zzfq.zzaw(i4);
                if (zzaw instanceof zzdp) {
                    i3 = zzeg.zzb((zzdp) zzaw);
                } else {
                    i3 = zzeg.zzcp((String) zzaw);
                }
                zzaj += i3;
                i4++;
            }
        } else {
            while (i4 < size) {
                Object obj = list.get(i4);
                if (obj instanceof zzdp) {
                    i2 = zzeg.zzb((zzdp) obj);
                } else {
                    i2 = zzeg.zzcp((String) obj);
                }
                zzaj += i2;
                i4++;
            }
        }
        return zzaj;
    }

    static int zzc(int i, Object obj, zzgy zzgy) {
        if (obj instanceof zzfo) {
            return zzeg.zza(i, (zzfo) obj);
        }
        return zzeg.zzb(i, (zzgh) obj, zzgy);
    }

    static int zzc(int i, List<?> list, zzgy zzgy) {
        int i2;
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzaj = zzeg.zzaj(i) * size;
        for (int i3 = 0; i3 < size; i3++) {
            Object obj = list.get(i3);
            if (obj instanceof zzfo) {
                i2 = zzeg.zza((zzfo) obj);
            } else {
                i2 = zzeg.zzb((zzgh) obj, zzgy);
            }
            zzaj += i2;
        }
        return zzaj;
    }

    static int zzd(int i, List<zzdp> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzaj = size * zzeg.zzaj(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            zzaj += zzeg.zzb((zzdp) list.get(i2));
        }
        return zzaj;
    }

    static int zzd(int i, List<zzgh> list, zzgy zzgy) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i2 += zzeg.zzc(i, (zzgh) list.get(i3), zzgy);
        }
        return i2;
    }

    public static zzhq<?, ?> zzod() {
        return zzajy;
    }

    public static zzhq<?, ?> zzoe() {
        return zzajz;
    }

    public static zzhq<?, ?> zzof() {
        return zzaka;
    }

    private static zzhq<?, ?> zzp(boolean z) {
        try {
            Class zzoh = zzoh();
            if (zzoh == null) {
                return null;
            }
            return (zzhq) zzoh.getConstructor(new Class[]{Boolean.TYPE}).newInstance(new Object[]{Boolean.valueOf(z)});
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Class<?> zzog() {
        try {
            return Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Class<?> zzoh() {
        try {
            return Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused) {
            return null;
        }
    }

    static boolean zzd(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    static <T> void zza(zzgc zzgc, T t, T t2, long j) {
        zzhw.zza((Object) t, j, zzgc.zzb(zzhw.zzp(t, j), zzhw.zzp(t2, j)));
    }

    static <T, FT extends zzes<FT>> void zza(zzen<FT> zzen, T t, T t2) {
        zzeq zzg = zzen.zzg(t2);
        if (!zzg.isEmpty()) {
            zzen.zzh(t).zza(zzg);
        }
    }

    static <T, UT, UB> void zza(zzhq<UT, UB> zzhq, T t, T t2) {
        zzhq.zze(t, zzhq.zzg(zzhq.zzw(t), zzhq.zzw(t2)));
    }

    static <UT, UB> UB zza(int i, List<Integer> list, zzfe zzfe, UB ub, zzhq<UT, UB> zzhq) {
        UB ub2;
        if (zzfe == null) {
            return ub;
        }
        if (list instanceof RandomAccess) {
            int size = list.size();
            ub2 = ub;
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                int intValue = ((Integer) list.get(i3)).intValue();
                if (zzfe.zzf(intValue)) {
                    if (i3 != i2) {
                        list.set(i2, Integer.valueOf(intValue));
                    }
                    i2++;
                } else {
                    ub2 = zza(i, intValue, ub2, zzhq);
                }
            }
            if (i2 != size) {
                list.subList(i2, size).clear();
            }
        } else {
            Iterator it = list.iterator();
            ub2 = ub;
            while (it.hasNext()) {
                int intValue2 = ((Integer) it.next()).intValue();
                if (!zzfe.zzf(intValue2)) {
                    UB zza = zza(i, intValue2, ub2, zzhq);
                    it.remove();
                    ub2 = zza;
                }
            }
        }
        return ub2;
    }

    static <UT, UB> UB zza(int i, int i2, UB ub, zzhq<UT, UB> zzhq) {
        if (ub == null) {
            ub = zzhq.zzoq();
        }
        zzhq.zza(ub, i, (long) i2);
        return ub;
    }
}
