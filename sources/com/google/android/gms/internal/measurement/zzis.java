package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class zzis implements Cloneable {
    private Object value;
    private zziq<?, ?> zzanj;
    private List<zzix> zzank = new ArrayList();

    zzis() {
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzix zzix) throws IOException {
        List<zzix> list = this.zzank;
        if (list != null) {
            list.add(zzix);
            return;
        }
        Object obj = this.value;
        if (obj instanceof zziv) {
            byte[] bArr = zzix.zzacg;
            zzim zzj = zzim.zzj(bArr, 0, bArr.length);
            int zzlb = zzj.zzlb();
            if (zzlb == bArr.length - zzin.zzak(zzlb)) {
                zziv zza = ((zziv) this.value).zza(zzj);
                this.zzanj = this.zzanj;
                this.value = zza;
                this.zzank = null;
                return;
            }
            throw zziu.zzpg();
        } else if (obj instanceof zziv[]) {
            Collections.singletonList(zzix);
            throw new NoSuchMethodError();
        } else if (obj instanceof zzgh) {
            Collections.singletonList(zzix);
            throw new NoSuchMethodError();
        } else if (obj instanceof zzgh[]) {
            Collections.singletonList(zzix);
            throw new NoSuchMethodError();
        } else {
            Collections.singletonList(zzix);
            throw new NoSuchMethodError();
        }
    }

    /* access modifiers changed from: 0000 */
    public final int zzja() {
        if (this.value == null) {
            int i = 0;
            for (zzix zzix : this.zzank) {
                i += zzin.zzar(zzix.tag) + 0 + zzix.zzacg.length;
            }
            return i;
        }
        throw new NoSuchMethodError();
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzin zzin) throws IOException {
        if (this.value == null) {
            for (zzix zzix : this.zzank) {
                zzin.zzbl(zzix.tag);
                zzin.zzm(zzix.zzacg);
            }
            return;
        }
        throw new NoSuchMethodError();
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzis)) {
            return false;
        }
        zzis zzis = (zzis) obj;
        if (this.value == null || zzis.value == null) {
            List<zzix> list = this.zzank;
            if (list != null) {
                List<zzix> list2 = zzis.zzank;
                if (list2 != null) {
                    return list.equals(list2);
                }
            }
            try {
                return Arrays.equals(toByteArray(), zzis.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            zziq<?, ?> zziq = this.zzanj;
            if (zziq != zzis.zzanj) {
                return false;
            }
            if (!zziq.zzane.isArray()) {
                return this.value.equals(zzis.value);
            }
            Object obj2 = this.value;
            if (obj2 instanceof byte[]) {
                return Arrays.equals((byte[]) obj2, (byte[]) zzis.value);
            }
            if (obj2 instanceof int[]) {
                return Arrays.equals((int[]) obj2, (int[]) zzis.value);
            }
            if (obj2 instanceof long[]) {
                return Arrays.equals((long[]) obj2, (long[]) zzis.value);
            }
            if (obj2 instanceof float[]) {
                return Arrays.equals((float[]) obj2, (float[]) zzis.value);
            }
            if (obj2 instanceof double[]) {
                return Arrays.equals((double[]) obj2, (double[]) zzis.value);
            }
            if (obj2 instanceof boolean[]) {
                return Arrays.equals((boolean[]) obj2, (boolean[]) zzis.value);
            }
            return Arrays.deepEquals((Object[]) obj2, (Object[]) zzis.value);
        }
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzja()];
        zza(zzin.zzl(bArr));
        return bArr;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzpf */
    public final zzis clone() {
        zzis zzis = new zzis();
        try {
            zzis.zzanj = this.zzanj;
            if (this.zzank == null) {
                zzis.zzank = null;
            } else {
                zzis.zzank.addAll(this.zzank);
            }
            if (this.value != null) {
                if (this.value instanceof zziv) {
                    zzis.value = (zziv) ((zziv) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    zzis.value = ((byte[]) this.value).clone();
                } else {
                    int i = 0;
                    if (this.value instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.value;
                        byte[][] bArr2 = new byte[bArr.length][];
                        zzis.value = bArr2;
                        while (i < bArr.length) {
                            bArr2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (this.value instanceof boolean[]) {
                        zzis.value = ((boolean[]) this.value).clone();
                    } else if (this.value instanceof int[]) {
                        zzis.value = ((int[]) this.value).clone();
                    } else if (this.value instanceof long[]) {
                        zzis.value = ((long[]) this.value).clone();
                    } else if (this.value instanceof float[]) {
                        zzis.value = ((float[]) this.value).clone();
                    } else if (this.value instanceof double[]) {
                        zzis.value = ((double[]) this.value).clone();
                    } else if (this.value instanceof zziv[]) {
                        zziv[] zzivArr = (zziv[]) this.value;
                        zziv[] zzivArr2 = new zziv[zzivArr.length];
                        zzis.value = zzivArr2;
                        while (i < zzivArr.length) {
                            zzivArr2[i] = (zziv) zzivArr[i].clone();
                            i++;
                        }
                    }
                }
            }
            return zzis;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
