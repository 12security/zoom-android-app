package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

final class zzgn<T> implements zzgy<T> {
    private final zzgh zzaje;
    private final boolean zzajf;
    private final zzhq<?, ?> zzajo;
    private final zzen<?> zzajp;

    private zzgn(zzhq<?, ?> zzhq, zzen<?> zzen, zzgh zzgh) {
        this.zzajo = zzhq;
        this.zzajf = zzen.zze(zzgh);
        this.zzajp = zzen;
        this.zzaje = zzgh;
    }

    static <T> zzgn<T> zza(zzhq<?, ?> zzhq, zzen<?> zzen, zzgh zzgh) {
        return new zzgn<>(zzhq, zzen, zzgh);
    }

    public final T newInstance() {
        return this.zzaje.zzml().zzmq();
    }

    public final boolean equals(T t, T t2) {
        if (!this.zzajo.zzw(t).equals(this.zzajo.zzw(t2))) {
            return false;
        }
        if (this.zzajf) {
            return this.zzajp.zzg(t).equals(this.zzajp.zzg(t2));
        }
        return true;
    }

    public final int hashCode(T t) {
        int hashCode = this.zzajo.zzw(t).hashCode();
        return this.zzajf ? (hashCode * 53) + this.zzajp.zzg(t).hashCode() : hashCode;
    }

    public final void zzc(T t, T t2) {
        zzha.zza(this.zzajo, t, t2);
        if (this.zzajf) {
            zzha.zza(this.zzajp, t, t2);
        }
    }

    public final void zza(T t, zzil zzil) throws IOException {
        Iterator it = this.zzajp.zzg(t).iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            zzes zzes = (zzes) entry.getKey();
            if (zzes.zzmb() != zzik.MESSAGE || zzes.zzmc() || zzes.zzmd()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            } else if (entry instanceof zzfm) {
                zzil.zza(zzes.zzgp(), (Object) ((zzfm) entry).zznf().zzjv());
            } else {
                zzil.zza(zzes.zzgp(), entry.getValue());
            }
        }
        zzhq<?, ?> zzhq = this.zzajo;
        zzhq.zzc(zzhq.zzw(t), zzil);
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(T r9, byte[] r10, int r11, int r12, com.google.android.gms.internal.measurement.zzdm r13) throws java.io.IOException {
        /*
            r8 = this;
            r0 = r9
            com.google.android.gms.internal.measurement.zzez r0 = (com.google.android.gms.internal.measurement.zzez) r0
            com.google.android.gms.internal.measurement.zzhr r1 = r0.zzagn
            com.google.android.gms.internal.measurement.zzhr r2 = com.google.android.gms.internal.measurement.zzhr.zzor()
            if (r1 != r2) goto L_0x0011
            com.google.android.gms.internal.measurement.zzhr r1 = com.google.android.gms.internal.measurement.zzhr.zzos()
            r0.zzagn = r1
        L_0x0011:
            com.google.android.gms.internal.measurement.zzez$zzc r9 = (com.google.android.gms.internal.measurement.zzez.zzc) r9
            r9.zzms()
            r9 = 0
            r0 = r9
        L_0x0018:
            if (r11 >= r12) goto L_0x00a2
            int r4 = com.google.android.gms.internal.measurement.zzdl.zza(r10, r11, r13)
            int r2 = r13.zzabs
            r11 = 11
            r3 = 2
            if (r2 == r11) goto L_0x0051
            r11 = r2 & 7
            if (r11 != r3) goto L_0x004c
            com.google.android.gms.internal.measurement.zzen<?> r11 = r8.zzajp
            com.google.android.gms.internal.measurement.zzem r0 = r13.zzabv
            com.google.android.gms.internal.measurement.zzgh r3 = r8.zzaje
            int r5 = r2 >>> 3
            java.lang.Object r11 = r11.zza(r0, r3, r5)
            r0 = r11
            com.google.android.gms.internal.measurement.zzez$zzd r0 = (com.google.android.gms.internal.measurement.zzez.zzd) r0
            if (r0 != 0) goto L_0x0043
            r3 = r10
            r5 = r12
            r6 = r1
            r7 = r13
            int r11 = com.google.android.gms.internal.measurement.zzdl.zza(r2, r3, r4, r5, r6, r7)
            goto L_0x0018
        L_0x0043:
            com.google.android.gms.internal.measurement.zzgu.zznz()
            java.lang.NoSuchMethodError r9 = new java.lang.NoSuchMethodError
            r9.<init>()
            throw r9
        L_0x004c:
            int r11 = com.google.android.gms.internal.measurement.zzdl.zza(r2, r10, r4, r12, r13)
            goto L_0x0018
        L_0x0051:
            r11 = 0
            r2 = r9
        L_0x0053:
            if (r4 >= r12) goto L_0x0097
            int r4 = com.google.android.gms.internal.measurement.zzdl.zza(r10, r4, r13)
            int r5 = r13.zzabs
            int r6 = r5 >>> 3
            r7 = r5 & 7
            switch(r6) {
                case 2: goto L_0x0079;
                case 3: goto L_0x0063;
                default: goto L_0x0062;
            }
        L_0x0062:
            goto L_0x008e
        L_0x0063:
            if (r0 != 0) goto L_0x0070
            if (r7 != r3) goto L_0x008e
            int r4 = com.google.android.gms.internal.measurement.zzdl.zze(r10, r4, r13)
            java.lang.Object r2 = r13.zzabu
            com.google.android.gms.internal.measurement.zzdp r2 = (com.google.android.gms.internal.measurement.zzdp) r2
            goto L_0x0053
        L_0x0070:
            com.google.android.gms.internal.measurement.zzgu.zznz()
            java.lang.NoSuchMethodError r9 = new java.lang.NoSuchMethodError
            r9.<init>()
            throw r9
        L_0x0079:
            if (r7 != 0) goto L_0x008e
            int r4 = com.google.android.gms.internal.measurement.zzdl.zza(r10, r4, r13)
            int r11 = r13.zzabs
            com.google.android.gms.internal.measurement.zzen<?> r0 = r8.zzajp
            com.google.android.gms.internal.measurement.zzem r5 = r13.zzabv
            com.google.android.gms.internal.measurement.zzgh r6 = r8.zzaje
            java.lang.Object r0 = r0.zza(r5, r6, r11)
            com.google.android.gms.internal.measurement.zzez$zzd r0 = (com.google.android.gms.internal.measurement.zzez.zzd) r0
            goto L_0x0053
        L_0x008e:
            r6 = 12
            if (r5 == r6) goto L_0x0097
            int r4 = com.google.android.gms.internal.measurement.zzdl.zza(r5, r10, r4, r12, r13)
            goto L_0x0053
        L_0x0097:
            if (r2 == 0) goto L_0x009f
            int r11 = r11 << 3
            r11 = r11 | r3
            r1.zzb(r11, r2)
        L_0x009f:
            r11 = r4
            goto L_0x0018
        L_0x00a2:
            if (r11 != r12) goto L_0x00a5
            return
        L_0x00a5:
            com.google.android.gms.internal.measurement.zzfh r9 = com.google.android.gms.internal.measurement.zzfh.zznb()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgn.zza(java.lang.Object, byte[], int, int, com.google.android.gms.internal.measurement.zzdm):void");
    }

    public final void zza(T t, zzgx zzgx, zzem zzem) throws IOException {
        boolean z;
        zzhq<?, ?> zzhq = this.zzajo;
        zzen<?> zzen = this.zzajp;
        Object zzx = zzhq.zzx(t);
        zzeq zzh = zzen.zzh(t);
        do {
            try {
                if (zzgx.zzlh() == Integer.MAX_VALUE) {
                    zzhq.zzf(t, zzx);
                    return;
                }
                int tag = zzgx.getTag();
                if (tag == 11) {
                    int i = 0;
                    Object obj = null;
                    zzdp zzdp = null;
                    while (zzgx.zzlh() != Integer.MAX_VALUE) {
                        int tag2 = zzgx.getTag();
                        if (tag2 == 16) {
                            i = zzgx.zzks();
                            obj = zzen.zza(zzem, this.zzaje, i);
                        } else if (tag2 == 26) {
                            if (obj != null) {
                                zzen.zza(zzgx, obj, zzem, zzh);
                            } else {
                                zzdp = zzgx.zzkr();
                            }
                        } else if (!zzgx.zzli()) {
                            break;
                        }
                    }
                    if (zzgx.getTag() != 12) {
                        throw zzfh.zzmy();
                    } else if (zzdp != null) {
                        if (obj != null) {
                            zzen.zza(zzdp, obj, zzem, zzh);
                        } else {
                            zzhq.zza(zzx, i, zzdp);
                        }
                    }
                } else if ((tag & 7) == 2) {
                    Object zza = zzen.zza(zzem, this.zzaje, tag >>> 3);
                    if (zza != null) {
                        zzen.zza(zzgx, zza, zzem, zzh);
                    } else {
                        z = zzhq.zza(zzx, zzgx);
                        continue;
                    }
                } else {
                    z = zzgx.zzli();
                    continue;
                }
                z = true;
                continue;
            } finally {
                zzhq.zzf(t, zzx);
            }
        } while (z);
    }

    public final void zzi(T t) {
        this.zzajo.zzi(t);
        this.zzajp.zzi(t);
    }

    public final boolean zzu(T t) {
        return this.zzajp.zzg(t).isInitialized();
    }

    public final int zzs(T t) {
        zzhq<?, ?> zzhq = this.zzajo;
        int zzy = zzhq.zzy(zzhq.zzw(t)) + 0;
        return this.zzajf ? zzy + this.zzajp.zzg(t).zzlz() : zzy;
    }
}
