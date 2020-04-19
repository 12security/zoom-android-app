package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzdg;
import com.google.android.gms.internal.measurement.zzdh;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class zzdg<MessageType extends zzdg<MessageType, BuilderType>, BuilderType extends zzdh<MessageType, BuilderType>> implements zzgh {
    private static boolean zzabn = false;
    protected int zzabm = 0;

    public final zzdp zzjv() {
        try {
            zzdx zzt = zzdp.zzt(zzly());
            zzb(zzt.zzki());
            return zzt.zzkh();
        } catch (IOException e) {
            String str = "ByteString";
            String name = getClass().getName();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 62 + String.valueOf(str).length());
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a ");
            sb.append(str);
            sb.append(" threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public final byte[] toByteArray() {
        try {
            byte[] bArr = new byte[zzly()];
            zzeg zzh = zzeg.zzh(bArr);
            zzb(zzh);
            zzh.zzlk();
            return bArr;
        } catch (IOException e) {
            String str = "byte array";
            String name = getClass().getName();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 62 + String.valueOf(str).length());
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a ");
            sb.append(str);
            sb.append(" threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    /* access modifiers changed from: 0000 */
    public int zzjw() {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: 0000 */
    public void zzn(int i) {
        throw new UnsupportedOperationException();
    }

    protected static <T> void zza(Iterable<T> iterable, List<? super T> list) {
        zzfb.checkNotNull(iterable);
        if (iterable instanceof zzfq) {
            List zzng = ((zzfq) iterable).zzng();
            zzfq zzfq = (zzfq) list;
            int size = list.size();
            for (Object next : zzng) {
                if (next == null) {
                    int size2 = zzfq.size() - size;
                    StringBuilder sb = new StringBuilder(37);
                    sb.append("Element at index ");
                    sb.append(size2);
                    sb.append(" is null.");
                    String sb2 = sb.toString();
                    for (int size3 = zzfq.size() - 1; size3 >= size; size3--) {
                        zzfq.remove(size3);
                    }
                    throw new NullPointerException(sb2);
                } else if (next instanceof zzdp) {
                    zzfq.zzc((zzdp) next);
                } else {
                    zzfq.add((String) next);
                }
            }
        } else if (iterable instanceof zzgt) {
            list.addAll((Collection) iterable);
        } else {
            if ((list instanceof ArrayList) && (iterable instanceof Collection)) {
                ((ArrayList) list).ensureCapacity(list.size() + ((Collection) iterable).size());
            }
            int size4 = list.size();
            for (Object next2 : iterable) {
                if (next2 == null) {
                    int size5 = list.size() - size4;
                    StringBuilder sb3 = new StringBuilder(37);
                    sb3.append("Element at index ");
                    sb3.append(size5);
                    sb3.append(" is null.");
                    String sb4 = sb3.toString();
                    for (int size6 = list.size() - 1; size6 >= size4; size6--) {
                        list.remove(size6);
                    }
                    throw new NullPointerException(sb4);
                }
                list.add(next2);
            }
        }
    }
}
