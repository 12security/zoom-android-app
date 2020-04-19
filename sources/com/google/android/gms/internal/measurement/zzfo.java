package com.google.android.gms.internal.measurement;

public class zzfo {
    private static final zzem zzabo = zzem.zzls();
    private zzdp zzaie;
    private volatile zzgh zzaif;
    private volatile zzdp zzaig;

    public int hashCode() {
        return 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzfo)) {
            return false;
        }
        zzfo zzfo = (zzfo) obj;
        zzgh zzgh = this.zzaif;
        zzgh zzgh2 = zzfo.zzaif;
        if (zzgh == null && zzgh2 == null) {
            return zzjv().equals(zzfo.zzjv());
        }
        if (zzgh != null && zzgh2 != null) {
            return zzgh.equals(zzgh2);
        }
        if (zzgh != null) {
            return zzgh.equals(zzfo.zzh(zzgh.zzmm()));
        }
        return zzh(zzgh2.zzmm()).equals(zzgh2);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:7|8|9|10|11|12) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0012 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.google.android.gms.internal.measurement.zzgh zzh(com.google.android.gms.internal.measurement.zzgh r2) {
        /*
            r1 = this;
            com.google.android.gms.internal.measurement.zzgh r0 = r1.zzaif
            if (r0 != 0) goto L_0x001d
            monitor-enter(r1)
            com.google.android.gms.internal.measurement.zzgh r0 = r1.zzaif     // Catch:{ all -> 0x001a }
            if (r0 == 0) goto L_0x000b
            monitor-exit(r1)     // Catch:{ all -> 0x001a }
            goto L_0x001d
        L_0x000b:
            r1.zzaif = r2     // Catch:{ zzfh -> 0x0012 }
            com.google.android.gms.internal.measurement.zzdp r0 = com.google.android.gms.internal.measurement.zzdp.zzaby     // Catch:{ zzfh -> 0x0012 }
            r1.zzaig = r0     // Catch:{ zzfh -> 0x0012 }
            goto L_0x0018
        L_0x0012:
            r1.zzaif = r2     // Catch:{ all -> 0x001a }
            com.google.android.gms.internal.measurement.zzdp r2 = com.google.android.gms.internal.measurement.zzdp.zzaby     // Catch:{ all -> 0x001a }
            r1.zzaig = r2     // Catch:{ all -> 0x001a }
        L_0x0018:
            monitor-exit(r1)     // Catch:{ all -> 0x001a }
            goto L_0x001d
        L_0x001a:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x001a }
            throw r2
        L_0x001d:
            com.google.android.gms.internal.measurement.zzgh r2 = r1.zzaif
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzfo.zzh(com.google.android.gms.internal.measurement.zzgh):com.google.android.gms.internal.measurement.zzgh");
    }

    public final zzgh zzi(zzgh zzgh) {
        zzgh zzgh2 = this.zzaif;
        this.zzaie = null;
        this.zzaig = null;
        this.zzaif = zzgh;
        return zzgh2;
    }

    public final int zzly() {
        if (this.zzaig != null) {
            return this.zzaig.size();
        }
        if (this.zzaif != null) {
            return this.zzaif.zzly();
        }
        return 0;
    }

    public final zzdp zzjv() {
        if (this.zzaig != null) {
            return this.zzaig;
        }
        synchronized (this) {
            if (this.zzaig != null) {
                zzdp zzdp = this.zzaig;
                return zzdp;
            }
            if (this.zzaif == null) {
                this.zzaig = zzdp.zzaby;
            } else {
                this.zzaig = this.zzaif.zzjv();
            }
            zzdp zzdp2 = this.zzaig;
            return zzdp2;
        }
    }
}
