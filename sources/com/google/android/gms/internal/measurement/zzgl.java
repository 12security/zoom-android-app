package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import sun.misc.Unsafe;

final class zzgl<T> implements zzgy<T> {
    private static final int[] zzaiy = new int[0];
    private static final Unsafe zzaiz = zzhw.zzow();
    private final int[] zzaja;
    private final Object[] zzajb;
    private final int zzajc;
    private final int zzajd;
    private final zzgh zzaje;
    private final boolean zzajf;
    private final boolean zzajg;
    private final boolean zzajh;
    private final boolean zzaji;
    private final int[] zzajj;
    private final int zzajk;
    private final int zzajl;
    private final zzgp zzajm;
    private final zzfr zzajn;
    private final zzhq<?, ?> zzajo;
    private final zzen<?> zzajp;
    private final zzgc zzajq;

    private zzgl(int[] iArr, Object[] objArr, int i, int i2, zzgh zzgh, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzgp zzgp, zzfr zzfr, zzhq<?, ?> zzhq, zzen<?> zzen, zzgc zzgc) {
        this.zzaja = iArr;
        this.zzajb = objArr;
        this.zzajc = i;
        this.zzajd = i2;
        this.zzajg = zzgh instanceof zzez;
        this.zzajh = z;
        this.zzajf = zzen != null && zzen.zze(zzgh);
        this.zzaji = false;
        this.zzajj = iArr2;
        this.zzajk = i3;
        this.zzajl = i4;
        this.zzajm = zzgp;
        this.zzajn = zzfr;
        this.zzajo = zzhq;
        this.zzajp = zzen;
        this.zzaje = zzgh;
        this.zzajq = zzgc;
    }

    private static boolean zzbc(int i) {
        return (i & 536870912) != 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:165:0x0381  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03e5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static <T> com.google.android.gms.internal.measurement.zzgl<T> zza(java.lang.Class<T> r35, com.google.android.gms.internal.measurement.zzgf r36, com.google.android.gms.internal.measurement.zzgp r37, com.google.android.gms.internal.measurement.zzfr r38, com.google.android.gms.internal.measurement.zzhq<?, ?> r39, com.google.android.gms.internal.measurement.zzen<?> r40, com.google.android.gms.internal.measurement.zzgc r41) {
        /*
            r0 = r36
            boolean r1 = r0 instanceof com.google.android.gms.internal.measurement.zzgw
            if (r1 == 0) goto L_0x0454
            com.google.android.gms.internal.measurement.zzgw r0 = (com.google.android.gms.internal.measurement.zzgw) r0
            int r1 = r0.zzns()
            int r2 = com.google.android.gms.internal.measurement.zzez.zze.zzahd
            r3 = 0
            r4 = 1
            if (r1 != r2) goto L_0x0014
            r11 = 1
            goto L_0x0015
        L_0x0014:
            r11 = 0
        L_0x0015:
            java.lang.String r1 = r0.zzob()
            int r2 = r1.length()
            char r5 = r1.charAt(r3)
            r7 = 55296(0xd800, float:7.7486E-41)
            if (r5 < r7) goto L_0x003f
            r5 = r5 & 8191(0x1fff, float:1.1478E-41)
            r8 = r5
            r5 = 1
            r9 = 13
        L_0x002c:
            int r10 = r5 + 1
            char r5 = r1.charAt(r5)
            if (r5 < r7) goto L_0x003c
            r5 = r5 & 8191(0x1fff, float:1.1478E-41)
            int r5 = r5 << r9
            r8 = r8 | r5
            int r9 = r9 + 13
            r5 = r10
            goto L_0x002c
        L_0x003c:
            int r5 = r5 << r9
            r5 = r5 | r8
            goto L_0x0040
        L_0x003f:
            r10 = 1
        L_0x0040:
            int r8 = r10 + 1
            char r9 = r1.charAt(r10)
            if (r9 < r7) goto L_0x005f
            r9 = r9 & 8191(0x1fff, float:1.1478E-41)
            r10 = 13
        L_0x004c:
            int r12 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x005c
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r10
            r9 = r9 | r8
            int r10 = r10 + 13
            r8 = r12
            goto L_0x004c
        L_0x005c:
            int r8 = r8 << r10
            r9 = r9 | r8
            goto L_0x0060
        L_0x005f:
            r12 = r8
        L_0x0060:
            if (r9 != 0) goto L_0x006e
            int[] r8 = zzaiy
            r15 = r8
            r8 = 0
            r9 = 0
            r10 = 0
            r13 = 0
            r14 = 0
            r16 = 0
            goto L_0x01a0
        L_0x006e:
            int r8 = r12 + 1
            char r9 = r1.charAt(r12)
            if (r9 < r7) goto L_0x008e
            r9 = r9 & 8191(0x1fff, float:1.1478E-41)
            r10 = 13
        L_0x007a:
            int r12 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x008a
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r10
            r9 = r9 | r8
            int r10 = r10 + 13
            r8 = r12
            goto L_0x007a
        L_0x008a:
            int r8 = r8 << r10
            r8 = r8 | r9
            r9 = r8
            goto L_0x008f
        L_0x008e:
            r12 = r8
        L_0x008f:
            int r8 = r12 + 1
            char r10 = r1.charAt(r12)
            if (r10 < r7) goto L_0x00ae
            r10 = r10 & 8191(0x1fff, float:1.1478E-41)
            r12 = 13
        L_0x009b:
            int r13 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x00ab
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r12
            r10 = r10 | r8
            int r12 = r12 + 13
            r8 = r13
            goto L_0x009b
        L_0x00ab:
            int r8 = r8 << r12
            r10 = r10 | r8
            goto L_0x00af
        L_0x00ae:
            r13 = r8
        L_0x00af:
            int r8 = r13 + 1
            char r12 = r1.charAt(r13)
            if (r12 < r7) goto L_0x00cf
            r12 = r12 & 8191(0x1fff, float:1.1478E-41)
            r13 = 13
        L_0x00bb:
            int r14 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x00cb
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r13
            r12 = r12 | r8
            int r13 = r13 + 13
            r8 = r14
            goto L_0x00bb
        L_0x00cb:
            int r8 = r8 << r13
            r8 = r8 | r12
            r12 = r8
            goto L_0x00d0
        L_0x00cf:
            r14 = r8
        L_0x00d0:
            int r8 = r14 + 1
            char r13 = r1.charAt(r14)
            if (r13 < r7) goto L_0x00f0
            r13 = r13 & 8191(0x1fff, float:1.1478E-41)
            r14 = 13
        L_0x00dc:
            int r15 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x00ec
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r14
            r13 = r13 | r8
            int r14 = r14 + 13
            r8 = r15
            goto L_0x00dc
        L_0x00ec:
            int r8 = r8 << r14
            r8 = r8 | r13
            r13 = r8
            goto L_0x00f1
        L_0x00f0:
            r15 = r8
        L_0x00f1:
            int r8 = r15 + 1
            char r14 = r1.charAt(r15)
            if (r14 < r7) goto L_0x0113
            r14 = r14 & 8191(0x1fff, float:1.1478E-41)
            r15 = 13
        L_0x00fd:
            int r16 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x010e
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r15
            r14 = r14 | r8
            int r15 = r15 + 13
            r8 = r16
            goto L_0x00fd
        L_0x010e:
            int r8 = r8 << r15
            r8 = r8 | r14
            r14 = r8
            r8 = r16
        L_0x0113:
            int r15 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x0136
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            r16 = 13
        L_0x011f:
            int r17 = r15 + 1
            char r15 = r1.charAt(r15)
            if (r15 < r7) goto L_0x0131
            r15 = r15 & 8191(0x1fff, float:1.1478E-41)
            int r15 = r15 << r16
            r8 = r8 | r15
            int r16 = r16 + 13
            r15 = r17
            goto L_0x011f
        L_0x0131:
            int r15 = r15 << r16
            r8 = r8 | r15
            r15 = r17
        L_0x0136:
            int r16 = r15 + 1
            char r15 = r1.charAt(r15)
            if (r15 < r7) goto L_0x0162
            r15 = r15 & 8191(0x1fff, float:1.1478E-41)
            r17 = 13
            r34 = r16
            r16 = r15
            r15 = r34
        L_0x0148:
            int r18 = r15 + 1
            char r15 = r1.charAt(r15)
            if (r15 < r7) goto L_0x015b
            r15 = r15 & 8191(0x1fff, float:1.1478E-41)
            int r15 = r15 << r17
            r16 = r16 | r15
            int r17 = r17 + 13
            r15 = r18
            goto L_0x0148
        L_0x015b:
            int r15 = r15 << r17
            r15 = r16 | r15
            r3 = r18
            goto L_0x0164
        L_0x0162:
            r3 = r16
        L_0x0164:
            int r16 = r3 + 1
            char r3 = r1.charAt(r3)
            if (r3 < r7) goto L_0x018f
            r3 = r3 & 8191(0x1fff, float:1.1478E-41)
            r17 = 13
            r34 = r16
            r16 = r3
            r3 = r34
        L_0x0176:
            int r18 = r3 + 1
            char r3 = r1.charAt(r3)
            if (r3 < r7) goto L_0x0189
            r3 = r3 & 8191(0x1fff, float:1.1478E-41)
            int r3 = r3 << r17
            r16 = r16 | r3
            int r17 = r17 + 13
            r3 = r18
            goto L_0x0176
        L_0x0189:
            int r3 = r3 << r17
            r3 = r16 | r3
            r16 = r18
        L_0x018f:
            int r17 = r3 + r8
            int r15 = r17 + r15
            int[] r15 = new int[r15]
            int r17 = r9 << 1
            int r10 = r17 + r10
            r34 = r16
            r16 = r9
            r9 = r12
            r12 = r34
        L_0x01a0:
            sun.misc.Unsafe r6 = zzaiz
            java.lang.Object[] r17 = r0.zzoc()
            com.google.android.gms.internal.measurement.zzgh r18 = r0.zznu()
            java.lang.Class r7 = r18.getClass()
            r18 = r10
            int r10 = r14 * 3
            int[] r10 = new int[r10]
            int r14 = r14 << r4
            java.lang.Object[] r14 = new java.lang.Object[r14]
            int r20 = r3 + r8
            r22 = r3
            r23 = r20
            r8 = 0
            r21 = 0
        L_0x01c0:
            if (r12 >= r2) goto L_0x042a
            int r24 = r12 + 1
            char r12 = r1.charAt(r12)
            r4 = 55296(0xd800, float:7.7486E-41)
            if (r12 < r4) goto L_0x01f4
            r12 = r12 & 8191(0x1fff, float:1.1478E-41)
            r26 = 13
            r34 = r24
            r24 = r12
            r12 = r34
        L_0x01d7:
            int r27 = r12 + 1
            char r12 = r1.charAt(r12)
            if (r12 < r4) goto L_0x01ed
            r4 = r12 & 8191(0x1fff, float:1.1478E-41)
            int r4 = r4 << r26
            r24 = r24 | r4
            int r26 = r26 + 13
            r12 = r27
            r4 = 55296(0xd800, float:7.7486E-41)
            goto L_0x01d7
        L_0x01ed:
            int r4 = r12 << r26
            r12 = r24 | r4
            r4 = r27
            goto L_0x01f6
        L_0x01f4:
            r4 = r24
        L_0x01f6:
            int r24 = r4 + 1
            char r4 = r1.charAt(r4)
            r26 = r2
            r2 = 55296(0xd800, float:7.7486E-41)
            if (r4 < r2) goto L_0x022a
            r4 = r4 & 8191(0x1fff, float:1.1478E-41)
            r27 = 13
            r34 = r24
            r24 = r4
            r4 = r34
        L_0x020d:
            int r28 = r4 + 1
            char r4 = r1.charAt(r4)
            if (r4 < r2) goto L_0x0223
            r2 = r4 & 8191(0x1fff, float:1.1478E-41)
            int r2 = r2 << r27
            r24 = r24 | r2
            int r27 = r27 + 13
            r4 = r28
            r2 = 55296(0xd800, float:7.7486E-41)
            goto L_0x020d
        L_0x0223:
            int r2 = r4 << r27
            r4 = r24 | r2
            r2 = r28
            goto L_0x022c
        L_0x022a:
            r2 = r24
        L_0x022c:
            r24 = r3
            r3 = r4 & 255(0xff, float:3.57E-43)
            r27 = r11
            r11 = r4 & 1024(0x400, float:1.435E-42)
            if (r11 == 0) goto L_0x023b
            int r11 = r8 + 1
            r15[r8] = r21
            r8 = r11
        L_0x023b:
            r11 = 51
            r30 = r8
            if (r3 < r11) goto L_0x02e4
            int r11 = r2 + 1
            char r2 = r1.charAt(r2)
            r8 = 55296(0xd800, float:7.7486E-41)
            if (r2 < r8) goto L_0x026a
            r2 = r2 & 8191(0x1fff, float:1.1478E-41)
            r32 = 13
        L_0x0250:
            int r33 = r11 + 1
            char r11 = r1.charAt(r11)
            if (r11 < r8) goto L_0x0265
            r8 = r11 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r32
            r2 = r2 | r8
            int r32 = r32 + 13
            r11 = r33
            r8 = 55296(0xd800, float:7.7486E-41)
            goto L_0x0250
        L_0x0265:
            int r8 = r11 << r32
            r2 = r2 | r8
            r11 = r33
        L_0x026a:
            int r8 = r3 + -51
            r32 = r11
            r11 = 9
            if (r8 == r11) goto L_0x0290
            r11 = 17
            if (r8 != r11) goto L_0x0277
            goto L_0x0290
        L_0x0277:
            r11 = 12
            if (r8 != r11) goto L_0x028e
            r8 = r5 & 1
            r11 = 1
            if (r8 != r11) goto L_0x028e
            int r8 = r21 / 3
            int r8 = r8 << r11
            int r8 = r8 + r11
            int r11 = r18 + 1
            r18 = r17[r18]
            r14[r8] = r18
            r18 = r11
            r11 = 1
            goto L_0x029d
        L_0x028e:
            r11 = 1
            goto L_0x029d
        L_0x0290:
            int r8 = r21 / 3
            r11 = 1
            int r8 = r8 << r11
            int r8 = r8 + r11
            int r25 = r18 + 1
            r18 = r17[r18]
            r14[r8] = r18
            r18 = r25
        L_0x029d:
            int r2 = r2 << r11
            r8 = r17[r2]
            boolean r11 = r8 instanceof java.lang.reflect.Field
            if (r11 == 0) goto L_0x02a8
            java.lang.reflect.Field r8 = (java.lang.reflect.Field) r8
            r11 = r9
            goto L_0x02b1
        L_0x02a8:
            java.lang.String r8 = (java.lang.String) r8
            java.lang.reflect.Field r8 = zza(r7, r8)
            r17[r2] = r8
            r11 = r9
        L_0x02b1:
            long r8 = r6.objectFieldOffset(r8)
            int r8 = (int) r8
            int r2 = r2 + 1
            r9 = r17[r2]
            r28 = r8
            boolean r8 = r9 instanceof java.lang.reflect.Field
            if (r8 == 0) goto L_0x02c3
            java.lang.reflect.Field r9 = (java.lang.reflect.Field) r9
            goto L_0x02cb
        L_0x02c3:
            java.lang.String r9 = (java.lang.String) r9
            java.lang.reflect.Field r9 = zza(r7, r9)
            r17[r2] = r9
        L_0x02cb:
            long r8 = r6.objectFieldOffset(r9)
            int r2 = (int) r8
            r31 = r1
            r1 = r7
            r25 = r18
            r9 = r28
            r19 = 1
            r7 = r2
            r28 = r11
            r18 = r13
            r2 = 0
            r13 = r12
            r12 = r32
            goto L_0x03f2
        L_0x02e4:
            r11 = r9
            int r8 = r18 + 1
            r9 = r17[r18]
            java.lang.String r9 = (java.lang.String) r9
            java.lang.reflect.Field r9 = zza(r7, r9)
            r18 = r13
            r13 = 9
            if (r3 == r13) goto L_0x0369
            r13 = 17
            if (r3 != r13) goto L_0x02fb
            goto L_0x0369
        L_0x02fb:
            r13 = 27
            if (r3 == r13) goto L_0x0358
            r13 = 49
            if (r3 != r13) goto L_0x0304
            goto L_0x0358
        L_0x0304:
            r13 = 12
            if (r3 == r13) goto L_0x0343
            r13 = 30
            if (r3 == r13) goto L_0x0343
            r13 = 44
            if (r3 != r13) goto L_0x0311
            goto L_0x0343
        L_0x0311:
            r13 = 50
            if (r3 != r13) goto L_0x033f
            int r13 = r22 + 1
            r15[r22] = r21
            int r22 = r21 / 3
            r25 = 1
            int r22 = r22 << 1
            int r28 = r8 + 1
            r8 = r17[r8]
            r14[r22] = r8
            r8 = r4 & 2048(0x800, float:2.87E-42)
            if (r8 == 0) goto L_0x0337
            int r22 = r22 + 1
            int r8 = r28 + 1
            r28 = r17[r28]
            r14[r22] = r28
            r28 = r11
            r22 = r13
            r13 = r12
            goto L_0x0377
        L_0x0337:
            r22 = r13
            r8 = r28
            r28 = r11
            r13 = r12
            goto L_0x0377
        L_0x033f:
            r28 = r11
            r11 = 1
            goto L_0x0376
        L_0x0343:
            r13 = r5 & 1
            r28 = r11
            r11 = 1
            if (r13 != r11) goto L_0x0376
            int r13 = r21 / 3
            int r13 = r13 << r11
            int r13 = r13 + r11
            int r25 = r8 + 1
            r8 = r17[r8]
            r14[r13] = r8
            r13 = r12
            r8 = r25
            goto L_0x0377
        L_0x0358:
            r28 = r11
            r11 = 1
            int r13 = r21 / 3
            int r13 = r13 << r11
            int r13 = r13 + r11
            int r25 = r8 + 1
            r8 = r17[r8]
            r14[r13] = r8
            r13 = r12
            r8 = r25
            goto L_0x0377
        L_0x0369:
            r28 = r11
            r11 = 1
            int r13 = r21 / 3
            int r13 = r13 << r11
            int r13 = r13 + r11
            java.lang.Class r25 = r9.getType()
            r14[r13] = r25
        L_0x0376:
            r13 = r12
        L_0x0377:
            long r11 = r6.objectFieldOffset(r9)
            int r9 = (int) r11
            r11 = r5 & 1
            r12 = 1
            if (r11 != r12) goto L_0x03d7
            r11 = 17
            if (r3 > r11) goto L_0x03d7
            int r11 = r2 + 1
            char r2 = r1.charAt(r2)
            r12 = 55296(0xd800, float:7.7486E-41)
            if (r2 < r12) goto L_0x03ae
            r2 = r2 & 8191(0x1fff, float:1.1478E-41)
            r19 = 13
        L_0x0394:
            int r29 = r11 + 1
            char r11 = r1.charAt(r11)
            if (r11 < r12) goto L_0x03a6
            r11 = r11 & 8191(0x1fff, float:1.1478E-41)
            int r11 = r11 << r19
            r2 = r2 | r11
            int r19 = r19 + 13
            r11 = r29
            goto L_0x0394
        L_0x03a6:
            int r11 = r11 << r19
            r2 = r2 | r11
            r11 = r29
            r19 = 1
            goto L_0x03b0
        L_0x03ae:
            r19 = 1
        L_0x03b0:
            int r25 = r16 << 1
            int r29 = r2 / 32
            int r25 = r25 + r29
            r12 = r17[r25]
            r31 = r1
            boolean r1 = r12 instanceof java.lang.reflect.Field
            if (r1 == 0) goto L_0x03c4
            java.lang.reflect.Field r12 = (java.lang.reflect.Field) r12
            r1 = r7
            r25 = r8
            goto L_0x03cf
        L_0x03c4:
            java.lang.String r12 = (java.lang.String) r12
            java.lang.reflect.Field r12 = zza(r7, r12)
            r17[r25] = r12
            r1 = r7
            r25 = r8
        L_0x03cf:
            long r7 = r6.objectFieldOffset(r12)
            int r7 = (int) r7
            int r2 = r2 % 32
            goto L_0x03e1
        L_0x03d7:
            r31 = r1
            r1 = r7
            r25 = r8
            r19 = 1
            r11 = r2
            r2 = 0
            r7 = 0
        L_0x03e1:
            r8 = 18
            if (r3 < r8) goto L_0x03f1
            r8 = 49
            if (r3 > r8) goto L_0x03f1
            int r8 = r23 + 1
            r15[r23] = r9
            r23 = r8
            r12 = r11
            goto L_0x03f2
        L_0x03f1:
            r12 = r11
        L_0x03f2:
            int r8 = r21 + 1
            r10[r21] = r13
            int r11 = r8 + 1
            r13 = r4 & 512(0x200, float:7.175E-43)
            if (r13 == 0) goto L_0x03ff
            r13 = 536870912(0x20000000, float:1.0842022E-19)
            goto L_0x0400
        L_0x03ff:
            r13 = 0
        L_0x0400:
            r4 = r4 & 256(0x100, float:3.59E-43)
            if (r4 == 0) goto L_0x0407
            r4 = 268435456(0x10000000, float:2.5243549E-29)
            goto L_0x0408
        L_0x0407:
            r4 = 0
        L_0x0408:
            r4 = r4 | r13
            int r3 = r3 << 20
            r3 = r3 | r4
            r3 = r3 | r9
            r10[r8] = r3
            int r21 = r11 + 1
            int r2 = r2 << 20
            r2 = r2 | r7
            r10[r11] = r2
            r7 = r1
            r13 = r18
            r3 = r24
            r18 = r25
            r2 = r26
            r11 = r27
            r9 = r28
            r8 = r30
            r1 = r31
            r4 = 1
            goto L_0x01c0
        L_0x042a:
            r24 = r3
            r28 = r9
            r27 = r11
            r18 = r13
            com.google.android.gms.internal.measurement.zzgl r1 = new com.google.android.gms.internal.measurement.zzgl
            com.google.android.gms.internal.measurement.zzgh r0 = r0.zznu()
            r12 = 0
            r5 = r1
            r6 = r10
            r7 = r14
            r8 = r28
            r9 = r18
            r10 = r0
            r13 = r15
            r14 = r24
            r15 = r20
            r16 = r37
            r17 = r38
            r18 = r39
            r19 = r40
            r20 = r41
            r5.<init>(r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)
            return r1
        L_0x0454:
            com.google.android.gms.internal.measurement.zzhl r0 = (com.google.android.gms.internal.measurement.zzhl) r0
            int r0 = r0.zzns()
            int r1 = com.google.android.gms.internal.measurement.zzez.zze.zzahd
            java.lang.NoSuchMethodError r0 = new java.lang.NoSuchMethodError
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zza(java.lang.Class, com.google.android.gms.internal.measurement.zzgf, com.google.android.gms.internal.measurement.zzgp, com.google.android.gms.internal.measurement.zzfr, com.google.android.gms.internal.measurement.zzhq, com.google.android.gms.internal.measurement.zzen, com.google.android.gms.internal.measurement.zzgc):com.google.android.gms.internal.measurement.zzgl");
    }

    private static Field zza(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = cls.getName();
            String arrays = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 40 + String.valueOf(name).length() + String.valueOf(arrays).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(arrays);
            throw new RuntimeException(sb.toString());
        }
    }

    public final T newInstance() {
        return this.zzajm.newInstance(this.zzaje);
    }

    public final boolean equals(T t, T t2) {
        int length = this.zzaja.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i < length) {
                int zzba = zzba(i);
                long j = (long) (zzba & 1048575);
                switch ((zzba & 267386880) >>> 20) {
                    case 0:
                        if (!zzc(t, t2, i) || Double.doubleToLongBits(zzhw.zzo(t, j)) != Double.doubleToLongBits(zzhw.zzo(t2, j))) {
                            z = false;
                            break;
                        }
                    case 1:
                        if (!zzc(t, t2, i) || Float.floatToIntBits(zzhw.zzn(t, j)) != Float.floatToIntBits(zzhw.zzn(t2, j))) {
                            z = false;
                            break;
                        }
                    case 2:
                        if (!zzc(t, t2, i) || zzhw.zzl(t, j) != zzhw.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 3:
                        if (!zzc(t, t2, i) || zzhw.zzl(t, j) != zzhw.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 4:
                        if (!zzc(t, t2, i) || zzhw.zzk(t, j) != zzhw.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 5:
                        if (!zzc(t, t2, i) || zzhw.zzl(t, j) != zzhw.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 6:
                        if (!zzc(t, t2, i) || zzhw.zzk(t, j) != zzhw.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 7:
                        if (!zzc(t, t2, i) || zzhw.zzm(t, j) != zzhw.zzm(t2, j)) {
                            z = false;
                            break;
                        }
                    case 8:
                        if (!zzc(t, t2, i) || !zzha.zzd(zzhw.zzp(t, j), zzhw.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 9:
                        if (!zzc(t, t2, i) || !zzha.zzd(zzhw.zzp(t, j), zzhw.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 10:
                        if (!zzc(t, t2, i) || !zzha.zzd(zzhw.zzp(t, j), zzhw.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 11:
                        if (!zzc(t, t2, i) || zzhw.zzk(t, j) != zzhw.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 12:
                        if (!zzc(t, t2, i) || zzhw.zzk(t, j) != zzhw.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 13:
                        if (!zzc(t, t2, i) || zzhw.zzk(t, j) != zzhw.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 14:
                        if (!zzc(t, t2, i) || zzhw.zzl(t, j) != zzhw.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 15:
                        if (!zzc(t, t2, i) || zzhw.zzk(t, j) != zzhw.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 16:
                        if (!zzc(t, t2, i) || zzhw.zzl(t, j) != zzhw.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 17:
                        if (!zzc(t, t2, i) || !zzha.zzd(zzhw.zzp(t, j), zzhw.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                        z = zzha.zzd(zzhw.zzp(t, j), zzhw.zzp(t2, j));
                        break;
                    case 50:
                        z = zzha.zzd(zzhw.zzp(t, j), zzhw.zzp(t2, j));
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 68:
                        long zzbb = (long) (zzbb(i) & 1048575);
                        if (zzhw.zzk(t, zzbb) != zzhw.zzk(t2, zzbb) || !zzha.zzd(zzhw.zzp(t, j), zzhw.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                }
                if (!z) {
                    return false;
                }
                i += 3;
            } else if (!this.zzajo.zzw(t).equals(this.zzajo.zzw(t2))) {
                return false;
            } else {
                if (this.zzajf) {
                    return this.zzajp.zzg(t).equals(this.zzajp.zzg(t2));
                }
                return true;
            }
        }
    }

    public final int hashCode(T t) {
        int length = this.zzaja.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2 += 3) {
            int zzba = zzba(i2);
            int i3 = this.zzaja[i2];
            long j = (long) (1048575 & zzba);
            int i4 = 37;
            switch ((zzba & 267386880) >>> 20) {
                case 0:
                    i = (i * 53) + zzfb.zzba(Double.doubleToLongBits(zzhw.zzo(t, j)));
                    break;
                case 1:
                    i = (i * 53) + Float.floatToIntBits(zzhw.zzn(t, j));
                    break;
                case 2:
                    i = (i * 53) + zzfb.zzba(zzhw.zzl(t, j));
                    break;
                case 3:
                    i = (i * 53) + zzfb.zzba(zzhw.zzl(t, j));
                    break;
                case 4:
                    i = (i * 53) + zzhw.zzk(t, j);
                    break;
                case 5:
                    i = (i * 53) + zzfb.zzba(zzhw.zzl(t, j));
                    break;
                case 6:
                    i = (i * 53) + zzhw.zzk(t, j);
                    break;
                case 7:
                    i = (i * 53) + zzfb.zzo(zzhw.zzm(t, j));
                    break;
                case 8:
                    i = (i * 53) + ((String) zzhw.zzp(t, j)).hashCode();
                    break;
                case 9:
                    Object zzp = zzhw.zzp(t, j);
                    if (zzp != null) {
                        i4 = zzp.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 10:
                    i = (i * 53) + zzhw.zzp(t, j).hashCode();
                    break;
                case 11:
                    i = (i * 53) + zzhw.zzk(t, j);
                    break;
                case 12:
                    i = (i * 53) + zzhw.zzk(t, j);
                    break;
                case 13:
                    i = (i * 53) + zzhw.zzk(t, j);
                    break;
                case 14:
                    i = (i * 53) + zzfb.zzba(zzhw.zzl(t, j));
                    break;
                case 15:
                    i = (i * 53) + zzhw.zzk(t, j);
                    break;
                case 16:
                    i = (i * 53) + zzfb.zzba(zzhw.zzl(t, j));
                    break;
                case 17:
                    Object zzp2 = zzhw.zzp(t, j);
                    if (zzp2 != null) {
                        i4 = zzp2.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    i = (i * 53) + zzhw.zzp(t, j).hashCode();
                    break;
                case 50:
                    i = (i * 53) + zzhw.zzp(t, j).hashCode();
                    break;
                case 51:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzfb.zzba(Double.doubleToLongBits(zzf(t, j)));
                        break;
                    }
                case 52:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + Float.floatToIntBits(zzg(t, j));
                        break;
                    }
                case 53:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzfb.zzba(zzi(t, j));
                        break;
                    }
                case 54:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzfb.zzba(zzi(t, j));
                        break;
                    }
                case 55:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 56:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzfb.zzba(zzi(t, j));
                        break;
                    }
                case 57:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 58:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzfb.zzo(zzj(t, j));
                        break;
                    }
                case 59:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + ((String) zzhw.zzp(t, j)).hashCode();
                        break;
                    }
                case 60:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzhw.zzp(t, j).hashCode();
                        break;
                    }
                case 61:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzhw.zzp(t, j).hashCode();
                        break;
                    }
                case 62:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 63:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 64:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 65:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzfb.zzba(zzi(t, j));
                        break;
                    }
                case 66:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 67:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzfb.zzba(zzi(t, j));
                        break;
                    }
                case 68:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzhw.zzp(t, j).hashCode();
                        break;
                    }
            }
        }
        int hashCode = (i * 53) + this.zzajo.zzw(t).hashCode();
        return this.zzajf ? (hashCode * 53) + this.zzajp.zzg(t).hashCode() : hashCode;
    }

    public final void zzc(T t, T t2) {
        if (t2 != null) {
            for (int i = 0; i < this.zzaja.length; i += 3) {
                int zzba = zzba(i);
                long j = (long) (1048575 & zzba);
                int i2 = this.zzaja[i];
                switch ((zzba & 267386880) >>> 20) {
                    case 0:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzo(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 1:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzn(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 2:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzl(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 3:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzl(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 4:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zzb((Object) t, j, zzhw.zzk(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 5:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzl(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 6:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zzb((Object) t, j, zzhw.zzk(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 7:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzm(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 8:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzp(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 9:
                        zza(t, t2, i);
                        break;
                    case 10:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzp(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 11:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zzb((Object) t, j, zzhw.zzk(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 12:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zzb((Object) t, j, zzhw.zzk(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 13:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zzb((Object) t, j, zzhw.zzk(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 14:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzl(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 15:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zzb((Object) t, j, zzhw.zzk(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 16:
                        if (!zza(t2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzl(t2, j));
                            zzb(t, i);
                            break;
                        }
                    case 17:
                        zza(t, t2, i);
                        break;
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                        this.zzajn.zza(t, t2, j);
                        break;
                    case 50:
                        zzha.zza(this.zzajq, t, t2, j);
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        if (!zza(t2, i2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzp(t2, j));
                            zzb(t, i2, i);
                            break;
                        }
                    case 60:
                        zzb(t, t2, i);
                        break;
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                        if (!zza(t2, i2, i)) {
                            break;
                        } else {
                            zzhw.zza((Object) t, j, zzhw.zzp(t2, j));
                            zzb(t, i2, i);
                            break;
                        }
                    case 68:
                        zzb(t, t2, i);
                        break;
                }
            }
            if (!this.zzajh) {
                zzha.zza(this.zzajo, t, t2);
                if (this.zzajf) {
                    zzha.zza(this.zzajp, t, t2);
                    return;
                }
                return;
            }
            return;
        }
        throw new NullPointerException();
    }

    private final void zza(T t, T t2, int i) {
        long zzba = (long) (zzba(i) & 1048575);
        if (zza(t2, i)) {
            Object zzp = zzhw.zzp(t, zzba);
            Object zzp2 = zzhw.zzp(t2, zzba);
            if (zzp == null || zzp2 == null) {
                if (zzp2 != null) {
                    zzhw.zza((Object) t, zzba, zzp2);
                    zzb(t, i);
                }
                return;
            }
            zzhw.zza((Object) t, zzba, zzfb.zza(zzp, zzp2));
            zzb(t, i);
        }
    }

    private final void zzb(T t, T t2, int i) {
        int zzba = zzba(i);
        int i2 = this.zzaja[i];
        long j = (long) (zzba & 1048575);
        if (zza(t2, i2, i)) {
            Object zzp = zzhw.zzp(t, j);
            Object zzp2 = zzhw.zzp(t2, j);
            if (zzp == null || zzp2 == null) {
                if (zzp2 != null) {
                    zzhw.zza((Object) t, j, zzp2);
                    zzb(t, i2, i);
                }
                return;
            }
            zzhw.zza((Object) t, j, zzfb.zza(zzp, zzp2));
            zzb(t, i2, i);
        }
    }

    public final int zzs(T t) {
        int i;
        int i2;
        long j;
        T t2 = t;
        int i3 = 267386880;
        if (this.zzajh) {
            Unsafe unsafe = zzaiz;
            int i4 = 0;
            int i5 = 0;
            while (i4 < this.zzaja.length) {
                int zzba = zzba(i4);
                int i6 = (zzba & i3) >>> 20;
                int i7 = this.zzaja[i4];
                long j2 = (long) (zzba & 1048575);
                int i8 = (i6 < zzet.DOUBLE_LIST_PACKED.mo24343id() || i6 > zzet.SINT64_LIST_PACKED.mo24343id()) ? 0 : this.zzaja[i4 + 2] & 1048575;
                switch (i6) {
                    case 0:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzb(i7, 0.0d);
                            break;
                        }
                    case 1:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzb(i7, 0.0f);
                            break;
                        }
                    case 2:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzd(i7, zzhw.zzl(t2, j2));
                            break;
                        }
                    case 3:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zze(i7, zzhw.zzl(t2, j2));
                            break;
                        }
                    case 4:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzg(i7, zzhw.zzk(t2, j2));
                            break;
                        }
                    case 5:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzg(i7, 0);
                            break;
                        }
                    case 6:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzj(i7, 0);
                            break;
                        }
                    case 7:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzc(i7, true);
                            break;
                        }
                    case 8:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            Object zzp = zzhw.zzp(t2, j2);
                            if (!(zzp instanceof zzdp)) {
                                i5 += zzeg.zzc(i7, (String) zzp);
                                break;
                            } else {
                                i5 += zzeg.zzc(i7, (zzdp) zzp);
                                break;
                            }
                        }
                    case 9:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzha.zzc(i7, zzhw.zzp(t2, j2), zzax(i4));
                            break;
                        }
                    case 10:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzc(i7, (zzdp) zzhw.zzp(t2, j2));
                            break;
                        }
                    case 11:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzh(i7, zzhw.zzk(t2, j2));
                            break;
                        }
                    case 12:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzl(i7, zzhw.zzk(t2, j2));
                            break;
                        }
                    case 13:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzk(i7, 0);
                            break;
                        }
                    case 14:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzh(i7, 0);
                            break;
                        }
                    case 15:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzi(i7, zzhw.zzk(t2, j2));
                            break;
                        }
                    case 16:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzf(i7, zzhw.zzl(t2, j2));
                            break;
                        }
                    case 17:
                        if (!zza(t2, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzc(i7, (zzgh) zzhw.zzp(t2, j2), zzax(i4));
                            break;
                        }
                    case 18:
                        i5 += zzha.zzw(i7, zze(t2, j2), false);
                        break;
                    case 19:
                        i5 += zzha.zzv(i7, zze(t2, j2), false);
                        break;
                    case 20:
                        i5 += zzha.zzo(i7, zze(t2, j2), false);
                        break;
                    case 21:
                        i5 += zzha.zzp(i7, zze(t2, j2), false);
                        break;
                    case 22:
                        i5 += zzha.zzs(i7, zze(t2, j2), false);
                        break;
                    case 23:
                        i5 += zzha.zzw(i7, zze(t2, j2), false);
                        break;
                    case 24:
                        i5 += zzha.zzv(i7, zze(t2, j2), false);
                        break;
                    case 25:
                        i5 += zzha.zzx(i7, zze(t2, j2), false);
                        break;
                    case 26:
                        i5 += zzha.zzc(i7, zze(t2, j2));
                        break;
                    case 27:
                        i5 += zzha.zzc(i7, zze(t2, j2), zzax(i4));
                        break;
                    case 28:
                        i5 += zzha.zzd(i7, zze(t2, j2));
                        break;
                    case 29:
                        i5 += zzha.zzt(i7, zze(t2, j2), false);
                        break;
                    case 30:
                        i5 += zzha.zzr(i7, zze(t2, j2), false);
                        break;
                    case 31:
                        i5 += zzha.zzv(i7, zze(t2, j2), false);
                        break;
                    case 32:
                        i5 += zzha.zzw(i7, zze(t2, j2), false);
                        break;
                    case 33:
                        i5 += zzha.zzu(i7, zze(t2, j2), false);
                        break;
                    case 34:
                        i5 += zzha.zzq(i7, zze(t2, j2), false);
                        break;
                    case 35:
                        int zzab = zzha.zzab((List) unsafe.getObject(t2, j2));
                        if (zzab > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzab);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzab) + zzab;
                            break;
                        } else {
                            break;
                        }
                    case 36:
                        int zzaa = zzha.zzaa((List) unsafe.getObject(t2, j2));
                        if (zzaa > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzaa);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzaa) + zzaa;
                            break;
                        } else {
                            break;
                        }
                    case 37:
                        int zzt = zzha.zzt((List) unsafe.getObject(t2, j2));
                        if (zzt > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzt);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzt) + zzt;
                            break;
                        } else {
                            break;
                        }
                    case 38:
                        int zzu = zzha.zzu((List) unsafe.getObject(t2, j2));
                        if (zzu > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzu);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzu) + zzu;
                            break;
                        } else {
                            break;
                        }
                    case 39:
                        int zzx = zzha.zzx((List) unsafe.getObject(t2, j2));
                        if (zzx > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzx);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzx) + zzx;
                            break;
                        } else {
                            break;
                        }
                    case 40:
                        int zzab2 = zzha.zzab((List) unsafe.getObject(t2, j2));
                        if (zzab2 > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzab2);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzab2) + zzab2;
                            break;
                        } else {
                            break;
                        }
                    case 41:
                        int zzaa2 = zzha.zzaa((List) unsafe.getObject(t2, j2));
                        if (zzaa2 > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzaa2);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzaa2) + zzaa2;
                            break;
                        } else {
                            break;
                        }
                    case 42:
                        int zzac = zzha.zzac((List) unsafe.getObject(t2, j2));
                        if (zzac > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzac);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzac) + zzac;
                            break;
                        } else {
                            break;
                        }
                    case 43:
                        int zzy = zzha.zzy((List) unsafe.getObject(t2, j2));
                        if (zzy > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzy);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzy) + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 44:
                        int zzw = zzha.zzw((List) unsafe.getObject(t2, j2));
                        if (zzw > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzw);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzw) + zzw;
                            break;
                        } else {
                            break;
                        }
                    case 45:
                        int zzaa3 = zzha.zzaa((List) unsafe.getObject(t2, j2));
                        if (zzaa3 > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzaa3);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzaa3) + zzaa3;
                            break;
                        } else {
                            break;
                        }
                    case 46:
                        int zzab3 = zzha.zzab((List) unsafe.getObject(t2, j2));
                        if (zzab3 > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzab3);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzab3) + zzab3;
                            break;
                        } else {
                            break;
                        }
                    case 47:
                        int zzz = zzha.zzz((List) unsafe.getObject(t2, j2));
                        if (zzz > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzz);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzz) + zzz;
                            break;
                        } else {
                            break;
                        }
                    case 48:
                        int zzv = zzha.zzv((List) unsafe.getObject(t2, j2));
                        if (zzv > 0) {
                            if (this.zzaji) {
                                unsafe.putInt(t2, (long) i8, zzv);
                            }
                            i5 += zzeg.zzaj(i7) + zzeg.zzal(zzv) + zzv;
                            break;
                        } else {
                            break;
                        }
                    case 49:
                        i5 += zzha.zzd(i7, zze(t2, j2), zzax(i4));
                        break;
                    case 50:
                        i5 += this.zzajq.zzb(i7, zzhw.zzp(t2, j2), zzay(i4));
                        break;
                    case 51:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzb(i7, 0.0d);
                            break;
                        }
                    case 52:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzb(i7, 0.0f);
                            break;
                        }
                    case 53:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzd(i7, zzi(t2, j2));
                            break;
                        }
                    case 54:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zze(i7, zzi(t2, j2));
                            break;
                        }
                    case 55:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzg(i7, zzh(t2, j2));
                            break;
                        }
                    case 56:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzg(i7, 0);
                            break;
                        }
                    case 57:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzj(i7, 0);
                            break;
                        }
                    case 58:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzc(i7, true);
                            break;
                        }
                    case 59:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            Object zzp2 = zzhw.zzp(t2, j2);
                            if (!(zzp2 instanceof zzdp)) {
                                i5 += zzeg.zzc(i7, (String) zzp2);
                                break;
                            } else {
                                i5 += zzeg.zzc(i7, (zzdp) zzp2);
                                break;
                            }
                        }
                    case 60:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzha.zzc(i7, zzhw.zzp(t2, j2), zzax(i4));
                            break;
                        }
                    case 61:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzc(i7, (zzdp) zzhw.zzp(t2, j2));
                            break;
                        }
                    case 62:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzh(i7, zzh(t2, j2));
                            break;
                        }
                    case 63:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzl(i7, zzh(t2, j2));
                            break;
                        }
                    case 64:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzk(i7, 0);
                            break;
                        }
                    case 65:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzh(i7, 0);
                            break;
                        }
                    case 66:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzi(i7, zzh(t2, j2));
                            break;
                        }
                    case 67:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzf(i7, zzi(t2, j2));
                            break;
                        }
                    case 68:
                        if (!zza(t2, i7, i4)) {
                            break;
                        } else {
                            i5 += zzeg.zzc(i7, (zzgh) zzhw.zzp(t2, j2), zzax(i4));
                            break;
                        }
                }
                i4 += 3;
                i3 = 267386880;
            }
            return i5 + zza(this.zzajo, t2);
        }
        Unsafe unsafe2 = zzaiz;
        int i9 = 0;
        int i10 = 0;
        int i11 = -1;
        int i12 = 0;
        while (i9 < this.zzaja.length) {
            int zzba2 = zzba(i9);
            int[] iArr = this.zzaja;
            int i13 = iArr[i9];
            int i14 = (zzba2 & 267386880) >>> 20;
            if (i14 <= 17) {
                i2 = iArr[i9 + 2];
                int i15 = i2 & 1048575;
                i = 1 << (i2 >>> 20);
                if (i15 != i11) {
                    i12 = unsafe2.getInt(t2, (long) i15);
                } else {
                    i15 = i11;
                }
                i11 = i15;
            } else if (!this.zzaji || i14 < zzet.DOUBLE_LIST_PACKED.mo24343id() || i14 > zzet.SINT64_LIST_PACKED.mo24343id()) {
                i2 = 0;
                i = 0;
            } else {
                i2 = this.zzaja[i9 + 2] & 1048575;
                i = 0;
            }
            long j3 = (long) (zzba2 & 1048575);
            switch (i14) {
                case 0:
                    j = 0;
                    if ((i12 & i) == 0) {
                        break;
                    } else {
                        i10 += zzeg.zzb(i13, 0.0d);
                        break;
                    }
                case 1:
                    j = 0;
                    if ((i12 & i) == 0) {
                        break;
                    } else {
                        i10 += zzeg.zzb(i13, 0.0f);
                        break;
                    }
                case 2:
                    j = 0;
                    if ((i12 & i) == 0) {
                        break;
                    } else {
                        i10 += zzeg.zzd(i13, unsafe2.getLong(t2, j3));
                        break;
                    }
                case 3:
                    j = 0;
                    if ((i12 & i) == 0) {
                        break;
                    } else {
                        i10 += zzeg.zze(i13, unsafe2.getLong(t2, j3));
                        break;
                    }
                case 4:
                    j = 0;
                    if ((i12 & i) == 0) {
                        break;
                    } else {
                        i10 += zzeg.zzg(i13, unsafe2.getInt(t2, j3));
                        break;
                    }
                case 5:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        i10 += zzeg.zzg(i13, 0);
                        break;
                    }
                case 6:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzj(i13, 0);
                        j = 0;
                        break;
                    }
                case 7:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzc(i13, true);
                        j = 0;
                        break;
                    }
                case 8:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        Object object = unsafe2.getObject(t2, j3);
                        if (!(object instanceof zzdp)) {
                            i10 += zzeg.zzc(i13, (String) object);
                            j = 0;
                            break;
                        } else {
                            i10 += zzeg.zzc(i13, (zzdp) object);
                            j = 0;
                            break;
                        }
                    }
                case 9:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzha.zzc(i13, unsafe2.getObject(t2, j3), zzax(i9));
                        j = 0;
                        break;
                    }
                case 10:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzc(i13, (zzdp) unsafe2.getObject(t2, j3));
                        j = 0;
                        break;
                    }
                case 11:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzh(i13, unsafe2.getInt(t2, j3));
                        j = 0;
                        break;
                    }
                case 12:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzl(i13, unsafe2.getInt(t2, j3));
                        j = 0;
                        break;
                    }
                case 13:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzk(i13, 0);
                        j = 0;
                        break;
                    }
                case 14:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzh(i13, 0);
                        j = 0;
                        break;
                    }
                case 15:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzi(i13, unsafe2.getInt(t2, j3));
                        j = 0;
                        break;
                    }
                case 16:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzf(i13, unsafe2.getLong(t2, j3));
                        j = 0;
                        break;
                    }
                case 17:
                    if ((i12 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzc(i13, (zzgh) unsafe2.getObject(t2, j3), zzax(i9));
                        j = 0;
                        break;
                    }
                case 18:
                    i10 += zzha.zzw(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 19:
                    i10 += zzha.zzv(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 20:
                    i10 += zzha.zzo(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 21:
                    i10 += zzha.zzp(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 22:
                    i10 += zzha.zzs(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 23:
                    i10 += zzha.zzw(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 24:
                    i10 += zzha.zzv(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 25:
                    i10 += zzha.zzx(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 26:
                    i10 += zzha.zzc(i13, (List) unsafe2.getObject(t2, j3));
                    j = 0;
                    break;
                case 27:
                    i10 += zzha.zzc(i13, (List) unsafe2.getObject(t2, j3), zzax(i9));
                    j = 0;
                    break;
                case 28:
                    i10 += zzha.zzd(i13, (List) unsafe2.getObject(t2, j3));
                    j = 0;
                    break;
                case 29:
                    i10 += zzha.zzt(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 30:
                    i10 += zzha.zzr(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 31:
                    i10 += zzha.zzv(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 32:
                    i10 += zzha.zzw(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 33:
                    i10 += zzha.zzu(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 34:
                    i10 += zzha.zzq(i13, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 35:
                    int zzab4 = zzha.zzab((List) unsafe2.getObject(t2, j3));
                    if (zzab4 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzab4);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzab4) + zzab4;
                        j = 0;
                        break;
                    }
                case 36:
                    int zzaa4 = zzha.zzaa((List) unsafe2.getObject(t2, j3));
                    if (zzaa4 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzaa4);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzaa4) + zzaa4;
                        j = 0;
                        break;
                    }
                case 37:
                    int zzt2 = zzha.zzt((List) unsafe2.getObject(t2, j3));
                    if (zzt2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzt2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzt2) + zzt2;
                        j = 0;
                        break;
                    }
                case 38:
                    int zzu2 = zzha.zzu((List) unsafe2.getObject(t2, j3));
                    if (zzu2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzu2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzu2) + zzu2;
                        j = 0;
                        break;
                    }
                case 39:
                    int zzx2 = zzha.zzx((List) unsafe2.getObject(t2, j3));
                    if (zzx2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzx2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzx2) + zzx2;
                        j = 0;
                        break;
                    }
                case 40:
                    int zzab5 = zzha.zzab((List) unsafe2.getObject(t2, j3));
                    if (zzab5 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzab5);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzab5) + zzab5;
                        j = 0;
                        break;
                    }
                case 41:
                    int zzaa5 = zzha.zzaa((List) unsafe2.getObject(t2, j3));
                    if (zzaa5 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzaa5);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzaa5) + zzaa5;
                        j = 0;
                        break;
                    }
                case 42:
                    int zzac2 = zzha.zzac((List) unsafe2.getObject(t2, j3));
                    if (zzac2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzac2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzac2) + zzac2;
                        j = 0;
                        break;
                    }
                case 43:
                    int zzy2 = zzha.zzy((List) unsafe2.getObject(t2, j3));
                    if (zzy2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzy2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzy2) + zzy2;
                        j = 0;
                        break;
                    }
                case 44:
                    int zzw2 = zzha.zzw((List) unsafe2.getObject(t2, j3));
                    if (zzw2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzw2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzw2) + zzw2;
                        j = 0;
                        break;
                    }
                case 45:
                    int zzaa6 = zzha.zzaa((List) unsafe2.getObject(t2, j3));
                    if (zzaa6 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzaa6);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzaa6) + zzaa6;
                        j = 0;
                        break;
                    }
                case 46:
                    int zzab6 = zzha.zzab((List) unsafe2.getObject(t2, j3));
                    if (zzab6 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzab6);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzab6) + zzab6;
                        j = 0;
                        break;
                    }
                case 47:
                    int zzz2 = zzha.zzz((List) unsafe2.getObject(t2, j3));
                    if (zzz2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzz2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzz2) + zzz2;
                        j = 0;
                        break;
                    }
                case 48:
                    int zzv2 = zzha.zzv((List) unsafe2.getObject(t2, j3));
                    if (zzv2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzaji) {
                            unsafe2.putInt(t2, (long) i2, zzv2);
                        }
                        i10 += zzeg.zzaj(i13) + zzeg.zzal(zzv2) + zzv2;
                        j = 0;
                        break;
                    }
                case 49:
                    i10 += zzha.zzd(i13, (List) unsafe2.getObject(t2, j3), zzax(i9));
                    j = 0;
                    break;
                case 50:
                    i10 += this.zzajq.zzb(i13, unsafe2.getObject(t2, j3), zzay(i9));
                    j = 0;
                    break;
                case 51:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzb(i13, 0.0d);
                        j = 0;
                        break;
                    }
                case 52:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzb(i13, 0.0f);
                        j = 0;
                        break;
                    }
                case 53:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzd(i13, zzi(t2, j3));
                        j = 0;
                        break;
                    }
                case 54:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zze(i13, zzi(t2, j3));
                        j = 0;
                        break;
                    }
                case 55:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzg(i13, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 56:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzg(i13, 0);
                        j = 0;
                        break;
                    }
                case 57:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzj(i13, 0);
                        j = 0;
                        break;
                    }
                case 58:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzc(i13, true);
                        j = 0;
                        break;
                    }
                case 59:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        Object object2 = unsafe2.getObject(t2, j3);
                        if (!(object2 instanceof zzdp)) {
                            i10 += zzeg.zzc(i13, (String) object2);
                            j = 0;
                            break;
                        } else {
                            i10 += zzeg.zzc(i13, (zzdp) object2);
                            j = 0;
                            break;
                        }
                    }
                case 60:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzha.zzc(i13, unsafe2.getObject(t2, j3), zzax(i9));
                        j = 0;
                        break;
                    }
                case 61:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzc(i13, (zzdp) unsafe2.getObject(t2, j3));
                        j = 0;
                        break;
                    }
                case 62:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzh(i13, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 63:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzl(i13, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 64:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzk(i13, 0);
                        j = 0;
                        break;
                    }
                case 65:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzh(i13, 0);
                        j = 0;
                        break;
                    }
                case 66:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzi(i13, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 67:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzf(i13, zzi(t2, j3));
                        j = 0;
                        break;
                    }
                case 68:
                    if (!zza(t2, i13, i9)) {
                        j = 0;
                        break;
                    } else {
                        i10 += zzeg.zzc(i13, (zzgh) unsafe2.getObject(t2, j3), zzax(i9));
                        j = 0;
                        break;
                    }
                default:
                    j = 0;
                    break;
            }
            i9 += 3;
            long j4 = j;
        }
        int zza = i10 + zza(this.zzajo, t2);
        if (this.zzajf) {
            zza += this.zzajp.zzg(t2).zzly();
        }
        return zza;
    }

    private static <UT, UB> int zza(zzhq<UT, UB> zzhq, T t) {
        return zzhq.zzs(zzhq.zzw(t));
    }

    private static <E> List<E> zze(Object obj, long j) {
        return (List) zzhw.zzp(obj, j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x0511  */
    /* JADX WARNING: Removed duplicated region for block: B:178:0x054f  */
    /* JADX WARNING: Removed duplicated region for block: B:331:0x0a27  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(T r14, com.google.android.gms.internal.measurement.zzil r15) throws java.io.IOException {
        /*
            r13 = this;
            int r0 = r15.zzln()
            int r1 = com.google.android.gms.internal.measurement.zzez.zze.zzahg
            r2 = 267386880(0xff00000, float:2.3665827E-29)
            r3 = 0
            r4 = 1
            r5 = 0
            r6 = 1048575(0xfffff, float:1.469367E-39)
            if (r0 != r1) goto L_0x0527
            com.google.android.gms.internal.measurement.zzhq<?, ?> r0 = r13.zzajo
            zza(r0, (T) r14, r15)
            boolean r0 = r13.zzajf
            if (r0 == 0) goto L_0x0030
            com.google.android.gms.internal.measurement.zzen<?> r0 = r13.zzajp
            com.google.android.gms.internal.measurement.zzeq r0 = r0.zzg(r14)
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L_0x0030
            java.util.Iterator r0 = r0.descendingIterator()
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            goto L_0x0032
        L_0x0030:
            r0 = r3
            r1 = r0
        L_0x0032:
            int[] r7 = r13.zzaja
            int r7 = r7.length
            int r7 = r7 + -3
        L_0x0037:
            if (r7 < 0) goto L_0x050f
            int r8 = r13.zzba(r7)
            int[] r9 = r13.zzaja
            r9 = r9[r7]
        L_0x0041:
            if (r1 == 0) goto L_0x005f
            com.google.android.gms.internal.measurement.zzen<?> r10 = r13.zzajp
            int r10 = r10.zza(r1)
            if (r10 <= r9) goto L_0x005f
            com.google.android.gms.internal.measurement.zzen<?> r10 = r13.zzajp
            r10.zza(r15, r1)
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x005d
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            goto L_0x0041
        L_0x005d:
            r1 = r3
            goto L_0x0041
        L_0x005f:
            r10 = r8 & r2
            int r10 = r10 >>> 20
            switch(r10) {
                case 0: goto L_0x04fc;
                case 1: goto L_0x04ec;
                case 2: goto L_0x04dc;
                case 3: goto L_0x04cc;
                case 4: goto L_0x04bc;
                case 5: goto L_0x04ac;
                case 6: goto L_0x049c;
                case 7: goto L_0x048b;
                case 8: goto L_0x047a;
                case 9: goto L_0x0465;
                case 10: goto L_0x0452;
                case 11: goto L_0x0441;
                case 12: goto L_0x0430;
                case 13: goto L_0x041f;
                case 14: goto L_0x040e;
                case 15: goto L_0x03fd;
                case 16: goto L_0x03ec;
                case 17: goto L_0x03d7;
                case 18: goto L_0x03c6;
                case 19: goto L_0x03b5;
                case 20: goto L_0x03a4;
                case 21: goto L_0x0393;
                case 22: goto L_0x0382;
                case 23: goto L_0x0371;
                case 24: goto L_0x0360;
                case 25: goto L_0x034f;
                case 26: goto L_0x033e;
                case 27: goto L_0x0329;
                case 28: goto L_0x0318;
                case 29: goto L_0x0307;
                case 30: goto L_0x02f6;
                case 31: goto L_0x02e5;
                case 32: goto L_0x02d4;
                case 33: goto L_0x02c3;
                case 34: goto L_0x02b2;
                case 35: goto L_0x02a1;
                case 36: goto L_0x0290;
                case 37: goto L_0x027f;
                case 38: goto L_0x026e;
                case 39: goto L_0x025d;
                case 40: goto L_0x024c;
                case 41: goto L_0x023b;
                case 42: goto L_0x022a;
                case 43: goto L_0x0219;
                case 44: goto L_0x0208;
                case 45: goto L_0x01f7;
                case 46: goto L_0x01e6;
                case 47: goto L_0x01d5;
                case 48: goto L_0x01c4;
                case 49: goto L_0x01af;
                case 50: goto L_0x01a4;
                case 51: goto L_0x0193;
                case 52: goto L_0x0182;
                case 53: goto L_0x0171;
                case 54: goto L_0x0160;
                case 55: goto L_0x014f;
                case 56: goto L_0x013e;
                case 57: goto L_0x012d;
                case 58: goto L_0x011c;
                case 59: goto L_0x010b;
                case 60: goto L_0x00f6;
                case 61: goto L_0x00e3;
                case 62: goto L_0x00d2;
                case 63: goto L_0x00c1;
                case 64: goto L_0x00b0;
                case 65: goto L_0x009f;
                case 66: goto L_0x008e;
                case 67: goto L_0x007d;
                case 68: goto L_0x0068;
                default: goto L_0x0066;
            }
        L_0x0066:
            goto L_0x050b
        L_0x0068:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            com.google.android.gms.internal.measurement.zzgy r10 = r13.zzax(r7)
            r15.zzb(r9, r8, r10)
            goto L_0x050b
        L_0x007d:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzb(r9, r10)
            goto L_0x050b
        L_0x008e:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zze(r9, r8)
            goto L_0x050b
        L_0x009f:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzj(r9, r10)
            goto L_0x050b
        L_0x00b0:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzm(r9, r8)
            goto L_0x050b
        L_0x00c1:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzn(r9, r8)
            goto L_0x050b
        L_0x00d2:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzd(r9, r8)
            goto L_0x050b
        L_0x00e3:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            com.google.android.gms.internal.measurement.zzdp r8 = (com.google.android.gms.internal.measurement.zzdp) r8
            r15.zza(r9, r8)
            goto L_0x050b
        L_0x00f6:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            com.google.android.gms.internal.measurement.zzgy r10 = r13.zzax(r7)
            r15.zza(r9, r8, r10)
            goto L_0x050b
        L_0x010b:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            zza(r9, r8, r15)
            goto L_0x050b
        L_0x011c:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            boolean r8 = zzj(r14, r10)
            r15.zzb(r9, r8)
            goto L_0x050b
        L_0x012d:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzf(r9, r8)
            goto L_0x050b
        L_0x013e:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzc(r9, r10)
            goto L_0x050b
        L_0x014f:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzc(r9, r8)
            goto L_0x050b
        L_0x0160:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zza(r9, r10)
            goto L_0x050b
        L_0x0171:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzi(r9, r10)
            goto L_0x050b
        L_0x0182:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            float r8 = zzg(r14, r10)
            r15.zza(r9, r8)
            goto L_0x050b
        L_0x0193:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            double r10 = zzf(r14, r10)
            r15.zza(r9, r10)
            goto L_0x050b
        L_0x01a4:
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            r13.zza(r15, r9, r8, r7)
            goto L_0x050b
        L_0x01af:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzgy r10 = r13.zzax(r7)
            com.google.android.gms.internal.measurement.zzha.zzb(r9, r8, r15, r10)
            goto L_0x050b
        L_0x01c4:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zze(r9, r8, r15, r4)
            goto L_0x050b
        L_0x01d5:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzj(r9, r8, r15, r4)
            goto L_0x050b
        L_0x01e6:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzg(r9, r8, r15, r4)
            goto L_0x050b
        L_0x01f7:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzl(r9, r8, r15, r4)
            goto L_0x050b
        L_0x0208:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzm(r9, r8, r15, r4)
            goto L_0x050b
        L_0x0219:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzi(r9, r8, r15, r4)
            goto L_0x050b
        L_0x022a:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzn(r9, r8, r15, r4)
            goto L_0x050b
        L_0x023b:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzk(r9, r8, r15, r4)
            goto L_0x050b
        L_0x024c:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzf(r9, r8, r15, r4)
            goto L_0x050b
        L_0x025d:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzh(r9, r8, r15, r4)
            goto L_0x050b
        L_0x026e:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzd(r9, r8, r15, r4)
            goto L_0x050b
        L_0x027f:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzc(r9, r8, r15, r4)
            goto L_0x050b
        L_0x0290:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzb(r9, r8, r15, r4)
            goto L_0x050b
        L_0x02a1:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zza(r9, r8, r15, r4)
            goto L_0x050b
        L_0x02b2:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zze(r9, r8, r15, r5)
            goto L_0x050b
        L_0x02c3:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzj(r9, r8, r15, r5)
            goto L_0x050b
        L_0x02d4:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzg(r9, r8, r15, r5)
            goto L_0x050b
        L_0x02e5:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzl(r9, r8, r15, r5)
            goto L_0x050b
        L_0x02f6:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzm(r9, r8, r15, r5)
            goto L_0x050b
        L_0x0307:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzi(r9, r8, r15, r5)
            goto L_0x050b
        L_0x0318:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzb(r9, r8, r15)
            goto L_0x050b
        L_0x0329:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzgy r10 = r13.zzax(r7)
            com.google.android.gms.internal.measurement.zzha.zza(r9, r8, r15, r10)
            goto L_0x050b
        L_0x033e:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zza(r9, r8, r15)
            goto L_0x050b
        L_0x034f:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzn(r9, r8, r15, r5)
            goto L_0x050b
        L_0x0360:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzk(r9, r8, r15, r5)
            goto L_0x050b
        L_0x0371:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzf(r9, r8, r15, r5)
            goto L_0x050b
        L_0x0382:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzh(r9, r8, r15, r5)
            goto L_0x050b
        L_0x0393:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzd(r9, r8, r15, r5)
            goto L_0x050b
        L_0x03a4:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzc(r9, r8, r15, r5)
            goto L_0x050b
        L_0x03b5:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zzb(r9, r8, r15, r5)
            goto L_0x050b
        L_0x03c6:
            int[] r9 = r13.zzaja
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.measurement.zzha.zza(r9, r8, r15, r5)
            goto L_0x050b
        L_0x03d7:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            com.google.android.gms.internal.measurement.zzgy r10 = r13.zzax(r7)
            r15.zzb(r9, r8, r10)
            goto L_0x050b
        L_0x03ec:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r10)
            r15.zzb(r9, r10)
            goto L_0x050b
        L_0x03fd:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r10)
            r15.zze(r9, r8)
            goto L_0x050b
        L_0x040e:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r10)
            r15.zzj(r9, r10)
            goto L_0x050b
        L_0x041f:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r10)
            r15.zzm(r9, r8)
            goto L_0x050b
        L_0x0430:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r10)
            r15.zzn(r9, r8)
            goto L_0x050b
        L_0x0441:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r10)
            r15.zzd(r9, r8)
            goto L_0x050b
        L_0x0452:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            com.google.android.gms.internal.measurement.zzdp r8 = (com.google.android.gms.internal.measurement.zzdp) r8
            r15.zza(r9, r8)
            goto L_0x050b
        L_0x0465:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            com.google.android.gms.internal.measurement.zzgy r10 = r13.zzax(r7)
            r15.zza(r9, r8, r10)
            goto L_0x050b
        L_0x047a:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r10)
            zza(r9, r8, r15)
            goto L_0x050b
        L_0x048b:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            boolean r8 = com.google.android.gms.internal.measurement.zzhw.zzm(r14, r10)
            r15.zzb(r9, r8)
            goto L_0x050b
        L_0x049c:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r10)
            r15.zzf(r9, r8)
            goto L_0x050b
        L_0x04ac:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r10)
            r15.zzc(r9, r10)
            goto L_0x050b
        L_0x04bc:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r10)
            r15.zzc(r9, r8)
            goto L_0x050b
        L_0x04cc:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r10)
            r15.zza(r9, r10)
            goto L_0x050b
        L_0x04dc:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r10)
            r15.zzi(r9, r10)
            goto L_0x050b
        L_0x04ec:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            float r8 = com.google.android.gms.internal.measurement.zzhw.zzn(r14, r10)
            r15.zza(r9, r8)
            goto L_0x050b
        L_0x04fc:
            boolean r10 = r13.zza((T) r14, r7)
            if (r10 == 0) goto L_0x050b
            r8 = r8 & r6
            long r10 = (long) r8
            double r10 = com.google.android.gms.internal.measurement.zzhw.zzo(r14, r10)
            r15.zza(r9, r10)
        L_0x050b:
            int r7 = r7 + -3
            goto L_0x0037
        L_0x050f:
            if (r1 == 0) goto L_0x0526
            com.google.android.gms.internal.measurement.zzen<?> r14 = r13.zzajp
            r14.zza(r15, r1)
            boolean r14 = r0.hasNext()
            if (r14 == 0) goto L_0x0524
            java.lang.Object r14 = r0.next()
            java.util.Map$Entry r14 = (java.util.Map.Entry) r14
            r1 = r14
            goto L_0x050f
        L_0x0524:
            r1 = r3
            goto L_0x050f
        L_0x0526:
            return
        L_0x0527:
            boolean r0 = r13.zzajh
            if (r0 == 0) goto L_0x0a42
            boolean r0 = r13.zzajf
            if (r0 == 0) goto L_0x0546
            com.google.android.gms.internal.measurement.zzen<?> r0 = r13.zzajp
            com.google.android.gms.internal.measurement.zzeq r0 = r0.zzg(r14)
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L_0x0546
            java.util.Iterator r0 = r0.iterator()
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            goto L_0x0548
        L_0x0546:
            r0 = r3
            r1 = r0
        L_0x0548:
            int[] r7 = r13.zzaja
            int r7 = r7.length
            r8 = r1
            r1 = 0
        L_0x054d:
            if (r1 >= r7) goto L_0x0a25
            int r9 = r13.zzba(r1)
            int[] r10 = r13.zzaja
            r10 = r10[r1]
        L_0x0557:
            if (r8 == 0) goto L_0x0575
            com.google.android.gms.internal.measurement.zzen<?> r11 = r13.zzajp
            int r11 = r11.zza(r8)
            if (r11 > r10) goto L_0x0575
            com.google.android.gms.internal.measurement.zzen<?> r11 = r13.zzajp
            r11.zza(r15, r8)
            boolean r8 = r0.hasNext()
            if (r8 == 0) goto L_0x0573
            java.lang.Object r8 = r0.next()
            java.util.Map$Entry r8 = (java.util.Map.Entry) r8
            goto L_0x0557
        L_0x0573:
            r8 = r3
            goto L_0x0557
        L_0x0575:
            r11 = r9 & r2
            int r11 = r11 >>> 20
            switch(r11) {
                case 0: goto L_0x0a12;
                case 1: goto L_0x0a02;
                case 2: goto L_0x09f2;
                case 3: goto L_0x09e2;
                case 4: goto L_0x09d2;
                case 5: goto L_0x09c2;
                case 6: goto L_0x09b2;
                case 7: goto L_0x09a1;
                case 8: goto L_0x0990;
                case 9: goto L_0x097b;
                case 10: goto L_0x0968;
                case 11: goto L_0x0957;
                case 12: goto L_0x0946;
                case 13: goto L_0x0935;
                case 14: goto L_0x0924;
                case 15: goto L_0x0913;
                case 16: goto L_0x0902;
                case 17: goto L_0x08ed;
                case 18: goto L_0x08dc;
                case 19: goto L_0x08cb;
                case 20: goto L_0x08ba;
                case 21: goto L_0x08a9;
                case 22: goto L_0x0898;
                case 23: goto L_0x0887;
                case 24: goto L_0x0876;
                case 25: goto L_0x0865;
                case 26: goto L_0x0854;
                case 27: goto L_0x083f;
                case 28: goto L_0x082e;
                case 29: goto L_0x081d;
                case 30: goto L_0x080c;
                case 31: goto L_0x07fb;
                case 32: goto L_0x07ea;
                case 33: goto L_0x07d9;
                case 34: goto L_0x07c8;
                case 35: goto L_0x07b7;
                case 36: goto L_0x07a6;
                case 37: goto L_0x0795;
                case 38: goto L_0x0784;
                case 39: goto L_0x0773;
                case 40: goto L_0x0762;
                case 41: goto L_0x0751;
                case 42: goto L_0x0740;
                case 43: goto L_0x072f;
                case 44: goto L_0x071e;
                case 45: goto L_0x070d;
                case 46: goto L_0x06fc;
                case 47: goto L_0x06eb;
                case 48: goto L_0x06da;
                case 49: goto L_0x06c5;
                case 50: goto L_0x06ba;
                case 51: goto L_0x06a9;
                case 52: goto L_0x0698;
                case 53: goto L_0x0687;
                case 54: goto L_0x0676;
                case 55: goto L_0x0665;
                case 56: goto L_0x0654;
                case 57: goto L_0x0643;
                case 58: goto L_0x0632;
                case 59: goto L_0x0621;
                case 60: goto L_0x060c;
                case 61: goto L_0x05f9;
                case 62: goto L_0x05e8;
                case 63: goto L_0x05d7;
                case 64: goto L_0x05c6;
                case 65: goto L_0x05b5;
                case 66: goto L_0x05a4;
                case 67: goto L_0x0593;
                case 68: goto L_0x057e;
                default: goto L_0x057c;
            }
        L_0x057c:
            goto L_0x0a21
        L_0x057e:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            com.google.android.gms.internal.measurement.zzgy r11 = r13.zzax(r1)
            r15.zzb(r10, r9, r11)
            goto L_0x0a21
        L_0x0593:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzb(r10, r11)
            goto L_0x0a21
        L_0x05a4:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zze(r10, r9)
            goto L_0x0a21
        L_0x05b5:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzj(r10, r11)
            goto L_0x0a21
        L_0x05c6:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzm(r10, r9)
            goto L_0x0a21
        L_0x05d7:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzn(r10, r9)
            goto L_0x0a21
        L_0x05e8:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzd(r10, r9)
            goto L_0x0a21
        L_0x05f9:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            com.google.android.gms.internal.measurement.zzdp r9 = (com.google.android.gms.internal.measurement.zzdp) r9
            r15.zza(r10, r9)
            goto L_0x0a21
        L_0x060c:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            com.google.android.gms.internal.measurement.zzgy r11 = r13.zzax(r1)
            r15.zza(r10, r9, r11)
            goto L_0x0a21
        L_0x0621:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            zza(r10, r9, r15)
            goto L_0x0a21
        L_0x0632:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            boolean r9 = zzj(r14, r11)
            r15.zzb(r10, r9)
            goto L_0x0a21
        L_0x0643:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzf(r10, r9)
            goto L_0x0a21
        L_0x0654:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzc(r10, r11)
            goto L_0x0a21
        L_0x0665:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzc(r10, r9)
            goto L_0x0a21
        L_0x0676:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zza(r10, r11)
            goto L_0x0a21
        L_0x0687:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzi(r10, r11)
            goto L_0x0a21
        L_0x0698:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            float r9 = zzg(r14, r11)
            r15.zza(r10, r9)
            goto L_0x0a21
        L_0x06a9:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            double r11 = zzf(r14, r11)
            r15.zza(r10, r11)
            goto L_0x0a21
        L_0x06ba:
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            r13.zza(r15, r10, r9, r1)
            goto L_0x0a21
        L_0x06c5:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzgy r11 = r13.zzax(r1)
            com.google.android.gms.internal.measurement.zzha.zzb(r10, r9, r15, r11)
            goto L_0x0a21
        L_0x06da:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zze(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x06eb:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzj(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x06fc:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzg(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x070d:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzl(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x071e:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzm(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x072f:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzi(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x0740:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzn(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x0751:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzk(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x0762:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzf(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x0773:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzh(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x0784:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzd(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x0795:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzc(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x07a6:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzb(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x07b7:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zza(r10, r9, r15, r4)
            goto L_0x0a21
        L_0x07c8:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zze(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x07d9:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzj(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x07ea:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzg(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x07fb:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzl(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x080c:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzm(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x081d:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzi(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x082e:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzb(r10, r9, r15)
            goto L_0x0a21
        L_0x083f:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzgy r11 = r13.zzax(r1)
            com.google.android.gms.internal.measurement.zzha.zza(r10, r9, r15, r11)
            goto L_0x0a21
        L_0x0854:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zza(r10, r9, r15)
            goto L_0x0a21
        L_0x0865:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzn(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x0876:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzk(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x0887:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzf(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x0898:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzh(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x08a9:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzd(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x08ba:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzc(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x08cb:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzb(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x08dc:
            int[] r10 = r13.zzaja
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zza(r10, r9, r15, r5)
            goto L_0x0a21
        L_0x08ed:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            com.google.android.gms.internal.measurement.zzgy r11 = r13.zzax(r1)
            r15.zzb(r10, r9, r11)
            goto L_0x0a21
        L_0x0902:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r11)
            r15.zzb(r10, r11)
            goto L_0x0a21
        L_0x0913:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r11)
            r15.zze(r10, r9)
            goto L_0x0a21
        L_0x0924:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r11)
            r15.zzj(r10, r11)
            goto L_0x0a21
        L_0x0935:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r11)
            r15.zzm(r10, r9)
            goto L_0x0a21
        L_0x0946:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r11)
            r15.zzn(r10, r9)
            goto L_0x0a21
        L_0x0957:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r11)
            r15.zzd(r10, r9)
            goto L_0x0a21
        L_0x0968:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            com.google.android.gms.internal.measurement.zzdp r9 = (com.google.android.gms.internal.measurement.zzdp) r9
            r15.zza(r10, r9)
            goto L_0x0a21
        L_0x097b:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            com.google.android.gms.internal.measurement.zzgy r11 = r13.zzax(r1)
            r15.zza(r10, r9, r11)
            goto L_0x0a21
        L_0x0990:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r11)
            zza(r10, r9, r15)
            goto L_0x0a21
        L_0x09a1:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            boolean r9 = com.google.android.gms.internal.measurement.zzhw.zzm(r14, r11)
            r15.zzb(r10, r9)
            goto L_0x0a21
        L_0x09b2:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r11)
            r15.zzf(r10, r9)
            goto L_0x0a21
        L_0x09c2:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r11)
            r15.zzc(r10, r11)
            goto L_0x0a21
        L_0x09d2:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.measurement.zzhw.zzk(r14, r11)
            r15.zzc(r10, r9)
            goto L_0x0a21
        L_0x09e2:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r11)
            r15.zza(r10, r11)
            goto L_0x0a21
        L_0x09f2:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.measurement.zzhw.zzl(r14, r11)
            r15.zzi(r10, r11)
            goto L_0x0a21
        L_0x0a02:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            float r9 = com.google.android.gms.internal.measurement.zzhw.zzn(r14, r11)
            r15.zza(r10, r9)
            goto L_0x0a21
        L_0x0a12:
            boolean r11 = r13.zza((T) r14, r1)
            if (r11 == 0) goto L_0x0a21
            r9 = r9 & r6
            long r11 = (long) r9
            double r11 = com.google.android.gms.internal.measurement.zzhw.zzo(r14, r11)
            r15.zza(r10, r11)
        L_0x0a21:
            int r1 = r1 + 3
            goto L_0x054d
        L_0x0a25:
            if (r8 == 0) goto L_0x0a3c
            com.google.android.gms.internal.measurement.zzen<?> r1 = r13.zzajp
            r1.zza(r15, r8)
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0a3a
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            r8 = r1
            goto L_0x0a25
        L_0x0a3a:
            r8 = r3
            goto L_0x0a25
        L_0x0a3c:
            com.google.android.gms.internal.measurement.zzhq<?, ?> r0 = r13.zzajo
            zza(r0, (T) r14, r15)
            return
        L_0x0a42:
            r13.zzb((T) r14, r15)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zza(java.lang.Object, com.google.android.gms.internal.measurement.zzil):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:190:0x051c  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzb(T r19, com.google.android.gms.internal.measurement.zzil r20) throws java.io.IOException {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r20
            boolean r3 = r0.zzajf
            if (r3 == 0) goto L_0x0021
            com.google.android.gms.internal.measurement.zzen<?> r3 = r0.zzajp
            com.google.android.gms.internal.measurement.zzeq r3 = r3.zzg(r1)
            boolean r5 = r3.isEmpty()
            if (r5 != 0) goto L_0x0021
            java.util.Iterator r3 = r3.iterator()
            java.lang.Object r5 = r3.next()
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5
            goto L_0x0023
        L_0x0021:
            r3 = 0
            r5 = 0
        L_0x0023:
            r6 = -1
            int[] r7 = r0.zzaja
            int r7 = r7.length
            sun.misc.Unsafe r8 = zzaiz
            r10 = r5
            r5 = 0
            r11 = 0
        L_0x002c:
            if (r5 >= r7) goto L_0x0516
            int r12 = r0.zzba(r5)
            int[] r13 = r0.zzaja
            r14 = r13[r5]
            r15 = 267386880(0xff00000, float:2.3665827E-29)
            r15 = r15 & r12
            int r15 = r15 >>> 20
            boolean r4 = r0.zzajh
            r16 = 1048575(0xfffff, float:1.469367E-39)
            if (r4 != 0) goto L_0x0062
            r4 = 17
            if (r15 > r4) goto L_0x0062
            int r4 = r5 + 2
            r4 = r13[r4]
            r13 = r4 & r16
            if (r13 == r6) goto L_0x0056
            r17 = r10
            long r9 = (long) r13
            int r11 = r8.getInt(r1, r9)
            goto L_0x0059
        L_0x0056:
            r17 = r10
            r13 = r6
        L_0x0059:
            int r4 = r4 >>> 20
            r6 = 1
            int r9 = r6 << r4
            r6 = r13
            r10 = r17
            goto L_0x0067
        L_0x0062:
            r17 = r10
            r10 = r17
            r9 = 0
        L_0x0067:
            if (r10 == 0) goto L_0x0086
            com.google.android.gms.internal.measurement.zzen<?> r4 = r0.zzajp
            int r4 = r4.zza(r10)
            if (r4 > r14) goto L_0x0086
            com.google.android.gms.internal.measurement.zzen<?> r4 = r0.zzajp
            r4.zza(r2, r10)
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0084
            java.lang.Object r4 = r3.next()
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4
            r10 = r4
            goto L_0x0067
        L_0x0084:
            r10 = 0
            goto L_0x0067
        L_0x0086:
            r4 = r12 & r16
            long r12 = (long) r4
            switch(r15) {
                case 0: goto L_0x0506;
                case 1: goto L_0x04f9;
                case 2: goto L_0x04ec;
                case 3: goto L_0x04df;
                case 4: goto L_0x04d2;
                case 5: goto L_0x04c5;
                case 6: goto L_0x04b8;
                case 7: goto L_0x04ab;
                case 8: goto L_0x049d;
                case 9: goto L_0x048b;
                case 10: goto L_0x047b;
                case 11: goto L_0x046d;
                case 12: goto L_0x045f;
                case 13: goto L_0x0451;
                case 14: goto L_0x0443;
                case 15: goto L_0x0435;
                case 16: goto L_0x0427;
                case 17: goto L_0x0415;
                case 18: goto L_0x0405;
                case 19: goto L_0x03f5;
                case 20: goto L_0x03e5;
                case 21: goto L_0x03d5;
                case 22: goto L_0x03c5;
                case 23: goto L_0x03b5;
                case 24: goto L_0x03a5;
                case 25: goto L_0x0395;
                case 26: goto L_0x0385;
                case 27: goto L_0x0371;
                case 28: goto L_0x0361;
                case 29: goto L_0x0350;
                case 30: goto L_0x033f;
                case 31: goto L_0x032e;
                case 32: goto L_0x031d;
                case 33: goto L_0x030c;
                case 34: goto L_0x02fb;
                case 35: goto L_0x02ea;
                case 36: goto L_0x02d9;
                case 37: goto L_0x02c8;
                case 38: goto L_0x02b7;
                case 39: goto L_0x02a6;
                case 40: goto L_0x0295;
                case 41: goto L_0x0284;
                case 42: goto L_0x0273;
                case 43: goto L_0x0262;
                case 44: goto L_0x0251;
                case 45: goto L_0x0240;
                case 46: goto L_0x022f;
                case 47: goto L_0x021e;
                case 48: goto L_0x020d;
                case 49: goto L_0x01f9;
                case 50: goto L_0x01ef;
                case 51: goto L_0x01dc;
                case 52: goto L_0x01c9;
                case 53: goto L_0x01b6;
                case 54: goto L_0x01a3;
                case 55: goto L_0x0190;
                case 56: goto L_0x017d;
                case 57: goto L_0x016a;
                case 58: goto L_0x0157;
                case 59: goto L_0x0144;
                case 60: goto L_0x012d;
                case 61: goto L_0x0118;
                case 62: goto L_0x0105;
                case 63: goto L_0x00f2;
                case 64: goto L_0x00df;
                case 65: goto L_0x00cc;
                case 66: goto L_0x00b9;
                case 67: goto L_0x00a6;
                case 68: goto L_0x008f;
                default: goto L_0x008c;
            }
        L_0x008c:
            r15 = 0
            goto L_0x0512
        L_0x008f:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00a3
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.measurement.zzgy r9 = r0.zzax(r5)
            r2.zzb(r14, r4, r9)
            r15 = 0
            goto L_0x0512
        L_0x00a3:
            r15 = 0
            goto L_0x0512
        L_0x00a6:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00b6
            long r12 = zzi(r1, r12)
            r2.zzb(r14, r12)
            r15 = 0
            goto L_0x0512
        L_0x00b6:
            r15 = 0
            goto L_0x0512
        L_0x00b9:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00c9
            int r4 = zzh(r1, r12)
            r2.zze(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x00c9:
            r15 = 0
            goto L_0x0512
        L_0x00cc:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00dc
            long r12 = zzi(r1, r12)
            r2.zzj(r14, r12)
            r15 = 0
            goto L_0x0512
        L_0x00dc:
            r15 = 0
            goto L_0x0512
        L_0x00df:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00ef
            int r4 = zzh(r1, r12)
            r2.zzm(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x00ef:
            r15 = 0
            goto L_0x0512
        L_0x00f2:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0102
            int r4 = zzh(r1, r12)
            r2.zzn(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x0102:
            r15 = 0
            goto L_0x0512
        L_0x0105:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0115
            int r4 = zzh(r1, r12)
            r2.zzd(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x0115:
            r15 = 0
            goto L_0x0512
        L_0x0118:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x012a
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.measurement.zzdp r4 = (com.google.android.gms.internal.measurement.zzdp) r4
            r2.zza(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x012a:
            r15 = 0
            goto L_0x0512
        L_0x012d:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0141
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.measurement.zzgy r9 = r0.zzax(r5)
            r2.zza(r14, r4, r9)
            r15 = 0
            goto L_0x0512
        L_0x0141:
            r15 = 0
            goto L_0x0512
        L_0x0144:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0154
            java.lang.Object r4 = r8.getObject(r1, r12)
            zza(r14, r4, r2)
            r15 = 0
            goto L_0x0512
        L_0x0154:
            r15 = 0
            goto L_0x0512
        L_0x0157:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0167
            boolean r4 = zzj(r1, r12)
            r2.zzb(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x0167:
            r15 = 0
            goto L_0x0512
        L_0x016a:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x017a
            int r4 = zzh(r1, r12)
            r2.zzf(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x017a:
            r15 = 0
            goto L_0x0512
        L_0x017d:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x018d
            long r12 = zzi(r1, r12)
            r2.zzc(r14, r12)
            r15 = 0
            goto L_0x0512
        L_0x018d:
            r15 = 0
            goto L_0x0512
        L_0x0190:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01a0
            int r4 = zzh(r1, r12)
            r2.zzc(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x01a0:
            r15 = 0
            goto L_0x0512
        L_0x01a3:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01b3
            long r12 = zzi(r1, r12)
            r2.zza(r14, r12)
            r15 = 0
            goto L_0x0512
        L_0x01b3:
            r15 = 0
            goto L_0x0512
        L_0x01b6:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01c6
            long r12 = zzi(r1, r12)
            r2.zzi(r14, r12)
            r15 = 0
            goto L_0x0512
        L_0x01c6:
            r15 = 0
            goto L_0x0512
        L_0x01c9:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01d9
            float r4 = zzg(r1, r12)
            r2.zza(r14, r4)
            r15 = 0
            goto L_0x0512
        L_0x01d9:
            r15 = 0
            goto L_0x0512
        L_0x01dc:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01ec
            double r12 = zzf(r1, r12)
            r2.zza(r14, r12)
            r15 = 0
            goto L_0x0512
        L_0x01ec:
            r15 = 0
            goto L_0x0512
        L_0x01ef:
            java.lang.Object r4 = r8.getObject(r1, r12)
            r0.zza(r2, r14, r4, r5)
            r15 = 0
            goto L_0x0512
        L_0x01f9:
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzgy r12 = r0.zzax(r5)
            com.google.android.gms.internal.measurement.zzha.zzb(r4, r9, r2, r12)
            r15 = 0
            goto L_0x0512
        L_0x020d:
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            r14 = 1
            com.google.android.gms.internal.measurement.zzha.zze(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x021e:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzj(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x022f:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzg(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0240:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzl(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0251:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzm(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0262:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzi(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0273:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzn(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0284:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzk(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0295:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzf(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x02a6:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzh(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x02b7:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzd(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x02c8:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzc(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x02d9:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzb(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x02ea:
            r14 = 1
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zza(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x02fb:
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            r14 = 0
            com.google.android.gms.internal.measurement.zzha.zze(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x030c:
            r14 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzj(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x031d:
            r14 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzg(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x032e:
            r14 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzl(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x033f:
            r14 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzm(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0350:
            r14 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzi(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0512
        L_0x0361:
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzb(r4, r9, r2)
            r15 = 0
            goto L_0x0512
        L_0x0371:
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzgy r12 = r0.zzax(r5)
            com.google.android.gms.internal.measurement.zzha.zza(r4, r9, r2, r12)
            r15 = 0
            goto L_0x0512
        L_0x0385:
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zza(r4, r9, r2)
            r15 = 0
            goto L_0x0512
        L_0x0395:
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            r15 = 0
            com.google.android.gms.internal.measurement.zzha.zzn(r4, r9, r2, r15)
            goto L_0x0512
        L_0x03a5:
            r15 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzk(r4, r9, r2, r15)
            goto L_0x0512
        L_0x03b5:
            r15 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzf(r4, r9, r2, r15)
            goto L_0x0512
        L_0x03c5:
            r15 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzh(r4, r9, r2, r15)
            goto L_0x0512
        L_0x03d5:
            r15 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzd(r4, r9, r2, r15)
            goto L_0x0512
        L_0x03e5:
            r15 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzc(r4, r9, r2, r15)
            goto L_0x0512
        L_0x03f5:
            r15 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zzb(r4, r9, r2, r15)
            goto L_0x0512
        L_0x0405:
            r15 = 0
            int[] r4 = r0.zzaja
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.measurement.zzha.zza(r4, r9, r2, r15)
            goto L_0x0512
        L_0x0415:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.measurement.zzgy r9 = r0.zzax(r5)
            r2.zzb(r14, r4, r9)
            goto L_0x0512
        L_0x0427:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            long r12 = r8.getLong(r1, r12)
            r2.zzb(r14, r12)
            goto L_0x0512
        L_0x0435:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            int r4 = r8.getInt(r1, r12)
            r2.zze(r14, r4)
            goto L_0x0512
        L_0x0443:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            long r12 = r8.getLong(r1, r12)
            r2.zzj(r14, r12)
            goto L_0x0512
        L_0x0451:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            int r4 = r8.getInt(r1, r12)
            r2.zzm(r14, r4)
            goto L_0x0512
        L_0x045f:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            int r4 = r8.getInt(r1, r12)
            r2.zzn(r14, r4)
            goto L_0x0512
        L_0x046d:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            int r4 = r8.getInt(r1, r12)
            r2.zzd(r14, r4)
            goto L_0x0512
        L_0x047b:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.measurement.zzdp r4 = (com.google.android.gms.internal.measurement.zzdp) r4
            r2.zza(r14, r4)
            goto L_0x0512
        L_0x048b:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.measurement.zzgy r9 = r0.zzax(r5)
            r2.zza(r14, r4, r9)
            goto L_0x0512
        L_0x049d:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            java.lang.Object r4 = r8.getObject(r1, r12)
            zza(r14, r4, r2)
            goto L_0x0512
        L_0x04ab:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            boolean r4 = com.google.android.gms.internal.measurement.zzhw.zzm(r1, r12)
            r2.zzb(r14, r4)
            goto L_0x0512
        L_0x04b8:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            int r4 = r8.getInt(r1, r12)
            r2.zzf(r14, r4)
            goto L_0x0512
        L_0x04c5:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            long r12 = r8.getLong(r1, r12)
            r2.zzc(r14, r12)
            goto L_0x0512
        L_0x04d2:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            int r4 = r8.getInt(r1, r12)
            r2.zzc(r14, r4)
            goto L_0x0512
        L_0x04df:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            long r12 = r8.getLong(r1, r12)
            r2.zza(r14, r12)
            goto L_0x0512
        L_0x04ec:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            long r12 = r8.getLong(r1, r12)
            r2.zzi(r14, r12)
            goto L_0x0512
        L_0x04f9:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            float r4 = com.google.android.gms.internal.measurement.zzhw.zzn(r1, r12)
            r2.zza(r14, r4)
            goto L_0x0512
        L_0x0506:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0512
            double r12 = com.google.android.gms.internal.measurement.zzhw.zzo(r1, r12)
            r2.zza(r14, r12)
        L_0x0512:
            int r5 = r5 + 3
            goto L_0x002c
        L_0x0516:
            r17 = r10
            r4 = r17
        L_0x051a:
            if (r4 == 0) goto L_0x0530
            com.google.android.gms.internal.measurement.zzen<?> r5 = r0.zzajp
            r5.zza(r2, r4)
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x052e
            java.lang.Object r4 = r3.next()
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4
            goto L_0x051a
        L_0x052e:
            r4 = 0
            goto L_0x051a
        L_0x0530:
            com.google.android.gms.internal.measurement.zzhq<?, ?> r3 = r0.zzajo
            zza(r3, (T) r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zzb(java.lang.Object, com.google.android.gms.internal.measurement.zzil):void");
    }

    private final <K, V> void zza(zzil zzil, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            zzil.zza(i, this.zzajq.zzr(zzay(i2)), this.zzajq.zzn(obj));
        }
    }

    private static <UT, UB> void zza(zzhq<UT, UB> zzhq, T t, zzil zzil) throws IOException {
        zzhq.zza(zzhq.zzw(t), zzil);
    }

    /* JADX INFO: used method not loaded: com.google.android.gms.internal.measurement.zzhq.zza(java.lang.Object, com.google.android.gms.internal.measurement.zzgx):null, types can be incorrect */
    /* JADX WARNING: Code restructure failed: missing block: B:153:?, code lost:
        r7.zza(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:154:0x05a2, code lost:
        if (r10 == null) goto L_0x05a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x05a4, code lost:
        r10 = r7.zzx(r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:0x05ad, code lost:
        if (r7.zza(r10, r14) == false) goto L_0x05af;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:158:0x05af, code lost:
        r14 = r12.zzajk;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x05b3, code lost:
        if (r14 < r12.zzajl) goto L_0x05b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:161:0x05b5, code lost:
        r10 = zza((java.lang.Object) r13, r12.zzajj[r14], (UB) r10, r7);
        r14 = r14 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x05c0, code lost:
        if (r10 != null) goto L_0x05c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x05c2, code lost:
        r7.zzf(r13, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x05c5, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:152:0x059f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(T r13, com.google.android.gms.internal.measurement.zzgx r14, com.google.android.gms.internal.measurement.zzem r15) throws java.io.IOException {
        /*
            r12 = this;
            if (r15 == 0) goto L_0x05de
            com.google.android.gms.internal.measurement.zzhq<?, ?> r7 = r12.zzajo
            com.google.android.gms.internal.measurement.zzen<?> r8 = r12.zzajp
            r9 = 0
            r0 = r9
            r10 = r0
        L_0x0009:
            int r1 = r14.zzlh()     // Catch:{ all -> 0x05c6 }
            int r2 = r12.zzbd(r1)     // Catch:{ all -> 0x05c6 }
            if (r2 >= 0) goto L_0x0079
            r2 = 2147483647(0x7fffffff, float:NaN)
            if (r1 != r2) goto L_0x002f
            int r14 = r12.zzajk
        L_0x001a:
            int r15 = r12.zzajl
            if (r14 >= r15) goto L_0x0029
            int[] r15 = r12.zzajj
            r15 = r15[r14]
            java.lang.Object r10 = r12.zza(r13, r15, (UB) r10, r7)
            int r14 = r14 + 1
            goto L_0x001a
        L_0x0029:
            if (r10 == 0) goto L_0x002e
            r7.zzf(r13, r10)
        L_0x002e:
            return
        L_0x002f:
            boolean r2 = r12.zzajf     // Catch:{ all -> 0x05c6 }
            if (r2 != 0) goto L_0x0035
            r2 = r9
            goto L_0x003c
        L_0x0035:
            com.google.android.gms.internal.measurement.zzgh r2 = r12.zzaje     // Catch:{ all -> 0x05c6 }
            java.lang.Object r1 = r8.zza(r15, r2, r1)     // Catch:{ all -> 0x05c6 }
            r2 = r1
        L_0x003c:
            if (r2 == 0) goto L_0x0053
            if (r0 != 0) goto L_0x0046
            com.google.android.gms.internal.measurement.zzeq r0 = r8.zzh(r13)     // Catch:{ all -> 0x05c6 }
            r11 = r0
            goto L_0x0047
        L_0x0046:
            r11 = r0
        L_0x0047:
            r0 = r8
            r1 = r14
            r3 = r15
            r4 = r11
            r5 = r10
            r6 = r7
            java.lang.Object r10 = r0.zza(r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x05c6 }
            r0 = r11
            goto L_0x0009
        L_0x0053:
            r7.zza(r14)     // Catch:{ all -> 0x05c6 }
            if (r10 != 0) goto L_0x005c
            java.lang.Object r10 = r7.zzx(r13)     // Catch:{ all -> 0x05c6 }
        L_0x005c:
            boolean r1 = r7.zza(r10, r14)     // Catch:{ all -> 0x05c6 }
            if (r1 != 0) goto L_0x0009
            int r14 = r12.zzajk
        L_0x0064:
            int r15 = r12.zzajl
            if (r14 >= r15) goto L_0x0073
            int[] r15 = r12.zzajj
            r15 = r15[r14]
            java.lang.Object r10 = r12.zza(r13, r15, (UB) r10, r7)
            int r14 = r14 + 1
            goto L_0x0064
        L_0x0073:
            if (r10 == 0) goto L_0x0078
            r7.zzf(r13, r10)
        L_0x0078:
            return
        L_0x0079:
            int r3 = r12.zzba(r2)     // Catch:{ all -> 0x05c6 }
            r4 = 267386880(0xff00000, float:2.3665827E-29)
            r4 = r4 & r3
            int r4 = r4 >>> 20
            r5 = 1048575(0xfffff, float:1.469367E-39)
            switch(r4) {
                case 0: goto L_0x0573;
                case 1: goto L_0x0564;
                case 2: goto L_0x0555;
                case 3: goto L_0x0546;
                case 4: goto L_0x0537;
                case 5: goto L_0x0528;
                case 6: goto L_0x0519;
                case 7: goto L_0x050a;
                case 8: goto L_0x0502;
                case 9: goto L_0x04d1;
                case 10: goto L_0x04c2;
                case 11: goto L_0x04b3;
                case 12: goto L_0x0491;
                case 13: goto L_0x0482;
                case 14: goto L_0x0473;
                case 15: goto L_0x0464;
                case 16: goto L_0x0455;
                case 17: goto L_0x0424;
                case 18: goto L_0x0416;
                case 19: goto L_0x0408;
                case 20: goto L_0x03fa;
                case 21: goto L_0x03ec;
                case 22: goto L_0x03de;
                case 23: goto L_0x03d0;
                case 24: goto L_0x03c2;
                case 25: goto L_0x03b4;
                case 26: goto L_0x0392;
                case 27: goto L_0x0380;
                case 28: goto L_0x0372;
                case 29: goto L_0x0364;
                case 30: goto L_0x034f;
                case 31: goto L_0x0341;
                case 32: goto L_0x0333;
                case 33: goto L_0x0325;
                case 34: goto L_0x0317;
                case 35: goto L_0x0309;
                case 36: goto L_0x02fb;
                case 37: goto L_0x02ed;
                case 38: goto L_0x02df;
                case 39: goto L_0x02d1;
                case 40: goto L_0x02c3;
                case 41: goto L_0x02b5;
                case 42: goto L_0x02a7;
                case 43: goto L_0x0299;
                case 44: goto L_0x0284;
                case 45: goto L_0x0276;
                case 46: goto L_0x0268;
                case 47: goto L_0x025a;
                case 48: goto L_0x024c;
                case 49: goto L_0x023a;
                case 50: goto L_0x01f8;
                case 51: goto L_0x01e6;
                case 52: goto L_0x01d4;
                case 53: goto L_0x01c2;
                case 54: goto L_0x01b0;
                case 55: goto L_0x019e;
                case 56: goto L_0x018c;
                case 57: goto L_0x017a;
                case 58: goto L_0x0168;
                case 59: goto L_0x0160;
                case 60: goto L_0x012f;
                case 61: goto L_0x0121;
                case 62: goto L_0x010f;
                case 63: goto L_0x00ea;
                case 64: goto L_0x00d8;
                case 65: goto L_0x00c6;
                case 66: goto L_0x00b4;
                case 67: goto L_0x00a2;
                case 68: goto L_0x0090;
                default: goto L_0x0088;
            }
        L_0x0088:
            if (r10 != 0) goto L_0x0582
            java.lang.Object r10 = r7.zzoq()     // Catch:{ zzfi -> 0x059f }
            goto L_0x0582
        L_0x0090:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r5 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r5 = r14.zzb(r5, r15)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x00a2:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkx()     // Catch:{ zzfi -> 0x059f }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x00b4:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            int r5 = r14.zzkw()     // Catch:{ zzfi -> 0x059f }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x00c6:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkv()     // Catch:{ zzfi -> 0x059f }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x00d8:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            int r5 = r14.zzku()     // Catch:{ zzfi -> 0x059f }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x00ea:
            int r4 = r14.zzkt()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzfe r6 = r12.zzaz(r2)     // Catch:{ zzfi -> 0x059f }
            if (r6 == 0) goto L_0x0101
            boolean r6 = r6.zzf(r4)     // Catch:{ zzfi -> 0x059f }
            if (r6 == 0) goto L_0x00fb
            goto L_0x0101
        L_0x00fb:
            java.lang.Object r10 = com.google.android.gms.internal.measurement.zzha.zza(r1, r4, r10, r7)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0101:
            r3 = r3 & r5
            long r5 = (long) r3     // Catch:{ zzfi -> 0x059f }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r4)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r5, r3)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x010f:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            int r5 = r14.zzks()     // Catch:{ zzfi -> 0x059f }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0121:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzdp r5 = r14.zzkr()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x012f:
            boolean r4 = r12.zza((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            if (r4 == 0) goto L_0x014b
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r5 = com.google.android.gms.internal.measurement.zzhw.zzp(r13, r3)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r6 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r6 = r14.zza(r6, r15)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r5 = com.google.android.gms.internal.measurement.zzfb.zza(r5, r6)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            goto L_0x015b
        L_0x014b:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r5 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r5 = r14.zza(r5, r15)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
        L_0x015b:
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0160:
            r12.zza(r13, r3, r14)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0168:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            boolean r5 = r14.zzkp()     // Catch:{ zzfi -> 0x059f }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x017a:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            int r5 = r14.zzko()     // Catch:{ zzfi -> 0x059f }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x018c:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkn()     // Catch:{ zzfi -> 0x059f }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x019e:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            int r5 = r14.zzkm()     // Catch:{ zzfi -> 0x059f }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x01b0:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkk()     // Catch:{ zzfi -> 0x059f }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x01c2:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkl()     // Catch:{ zzfi -> 0x059f }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x01d4:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            float r5 = r14.readFloat()     // Catch:{ zzfi -> 0x059f }
            java.lang.Float r5 = java.lang.Float.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x01e6:
            r3 = r3 & r5
            long r3 = (long) r3     // Catch:{ zzfi -> 0x059f }
            double r5 = r14.readDouble()     // Catch:{ zzfi -> 0x059f }
            java.lang.Double r5 = java.lang.Double.valueOf(r5)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r1, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x01f8:
            java.lang.Object r1 = r12.zzay(r2)     // Catch:{ zzfi -> 0x059f }
            int r2 = r12.zzba(r2)     // Catch:{ zzfi -> 0x059f }
            r2 = r2 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r4 = com.google.android.gms.internal.measurement.zzhw.zzp(r13, r2)     // Catch:{ zzfi -> 0x059f }
            if (r4 != 0) goto L_0x0212
            com.google.android.gms.internal.measurement.zzgc r4 = r12.zzajq     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r4 = r4.zzq(r1)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r2, r4)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0229
        L_0x0212:
            com.google.android.gms.internal.measurement.zzgc r5 = r12.zzajq     // Catch:{ zzfi -> 0x059f }
            boolean r5 = r5.zzo(r4)     // Catch:{ zzfi -> 0x059f }
            if (r5 == 0) goto L_0x0229
            com.google.android.gms.internal.measurement.zzgc r5 = r12.zzajq     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r5 = r5.zzq(r1)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgc r6 = r12.zzajq     // Catch:{ zzfi -> 0x059f }
            r6.zzb(r5, r4)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r2, r5)     // Catch:{ zzfi -> 0x059f }
            r4 = r5
        L_0x0229:
            com.google.android.gms.internal.measurement.zzgc r2 = r12.zzajq     // Catch:{ zzfi -> 0x059f }
            java.util.Map r2 = r2.zzm(r4)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgc r3 = r12.zzajq     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzga r1 = r3.zzr(r1)     // Catch:{ zzfi -> 0x059f }
            r14.zza(r2, r1, r15)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x023a:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r1 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzfr r2 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            java.util.List r2 = r2.zza(r13, r3)     // Catch:{ zzfi -> 0x059f }
            r14.zzb(r2, r1, r15)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x024c:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzs(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x025a:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzr(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0268:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzq(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0276:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzp(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0284:
            com.google.android.gms.internal.measurement.zzfr r4 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r3 = r3 & r5
            long r5 = (long) r3     // Catch:{ zzfi -> 0x059f }
            java.util.List r3 = r4.zza(r13, r5)     // Catch:{ zzfi -> 0x059f }
            r14.zzo(r3)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzfe r2 = r12.zzaz(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r10 = com.google.android.gms.internal.measurement.zzha.zza(r1, r3, r2, r10, r7)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0299:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzn(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x02a7:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzk(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x02b5:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzj(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x02c3:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzi(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x02d1:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzh(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x02df:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzf(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x02ed:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzg(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x02fb:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zze(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0309:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzd(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0317:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzs(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0325:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzr(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0333:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzq(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0341:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzp(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x034f:
            com.google.android.gms.internal.measurement.zzfr r4 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r3 = r3 & r5
            long r5 = (long) r3     // Catch:{ zzfi -> 0x059f }
            java.util.List r3 = r4.zza(r13, r5)     // Catch:{ zzfi -> 0x059f }
            r14.zzo(r3)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzfe r2 = r12.zzaz(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r10 = com.google.android.gms.internal.measurement.zzha.zza(r1, r3, r2, r10, r7)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0364:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzn(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0372:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzm(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0380:
            com.google.android.gms.internal.measurement.zzgy r1 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzfr r4 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            java.util.List r2 = r4.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zza(r2, r1, r15)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0392:
            boolean r1 = zzbc(r3)     // Catch:{ zzfi -> 0x059f }
            if (r1 == 0) goto L_0x03a6
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzl(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x03a6:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.readStringList(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x03b4:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzk(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x03c2:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzj(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x03d0:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzi(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x03de:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzh(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x03ec:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzf(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x03fa:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzg(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0408:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zze(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0416:
            com.google.android.gms.internal.measurement.zzfr r1 = r12.zzajn     // Catch:{ zzfi -> 0x059f }
            r2 = r3 & r5
            long r2 = (long) r2     // Catch:{ zzfi -> 0x059f }
            java.util.List r1 = r1.zza(r13, r2)     // Catch:{ zzfi -> 0x059f }
            r14.zzd(r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0424:
            boolean r1 = r12.zza((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            if (r1 == 0) goto L_0x0442
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r1 = com.google.android.gms.internal.measurement.zzhw.zzp(r13, r3)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r2 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r2 = r14.zzb(r2, r15)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r1 = com.google.android.gms.internal.measurement.zzfb.zza(r1, r2)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0442:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r1 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r1 = r14.zzb(r1, r15)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0455:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkx()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0464:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            int r1 = r14.zzkw()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zzb(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0473:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkv()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0482:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            int r1 = r14.zzku()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zzb(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0491:
            int r4 = r14.zzkt()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzfe r6 = r12.zzaz(r2)     // Catch:{ zzfi -> 0x059f }
            if (r6 == 0) goto L_0x04a8
            boolean r6 = r6.zzf(r4)     // Catch:{ zzfi -> 0x059f }
            if (r6 == 0) goto L_0x04a2
            goto L_0x04a8
        L_0x04a2:
            java.lang.Object r10 = com.google.android.gms.internal.measurement.zzha.zza(r1, r4, r10, r7)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x04a8:
            r1 = r3 & r5
            long r5 = (long) r1     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zzb(r13, r5, r4)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x04b3:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            int r1 = r14.zzks()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zzb(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x04c2:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzdp r1 = r14.zzkr()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x04d1:
            boolean r1 = r12.zza((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            if (r1 == 0) goto L_0x04ef
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r1 = com.google.android.gms.internal.measurement.zzhw.zzp(r13, r3)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r2 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r2 = r14.zza(r2, r15)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r1 = com.google.android.gms.internal.measurement.zzfb.zza(r1, r2)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x04ef:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzgy r1 = r12.zzax(r2)     // Catch:{ zzfi -> 0x059f }
            java.lang.Object r1 = r14.zza(r1, r15)     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0502:
            r12.zza(r13, r3, r14)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x050a:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            boolean r1 = r14.zzkp()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0519:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            int r1 = r14.zzko()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zzb(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0528:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkn()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0537:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            int r1 = r14.zzkm()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zzb(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0546:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkk()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0555:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            long r5 = r14.zzkl()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0564:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            float r1 = r14.readFloat()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r1)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0573:
            r1 = r3 & r5
            long r3 = (long) r1     // Catch:{ zzfi -> 0x059f }
            double r5 = r14.readDouble()     // Catch:{ zzfi -> 0x059f }
            com.google.android.gms.internal.measurement.zzhw.zza(r13, r3, r5)     // Catch:{ zzfi -> 0x059f }
            r12.zzb((T) r13, r2)     // Catch:{ zzfi -> 0x059f }
            goto L_0x0009
        L_0x0582:
            boolean r1 = r7.zza(r10, r14)     // Catch:{ zzfi -> 0x059f }
            if (r1 != 0) goto L_0x0009
            int r14 = r12.zzajk
        L_0x058a:
            int r15 = r12.zzajl
            if (r14 >= r15) goto L_0x0599
            int[] r15 = r12.zzajj
            r15 = r15[r14]
            java.lang.Object r10 = r12.zza(r13, r15, (UB) r10, r7)
            int r14 = r14 + 1
            goto L_0x058a
        L_0x0599:
            if (r10 == 0) goto L_0x059e
            r7.zzf(r13, r10)
        L_0x059e:
            return
        L_0x059f:
            r7.zza(r14)     // Catch:{ all -> 0x05c6 }
            if (r10 != 0) goto L_0x05a9
            java.lang.Object r1 = r7.zzx(r13)     // Catch:{ all -> 0x05c6 }
            r10 = r1
        L_0x05a9:
            boolean r1 = r7.zza(r10, r14)     // Catch:{ all -> 0x05c6 }
            if (r1 != 0) goto L_0x0009
            int r14 = r12.zzajk
        L_0x05b1:
            int r15 = r12.zzajl
            if (r14 >= r15) goto L_0x05c0
            int[] r15 = r12.zzajj
            r15 = r15[r14]
            java.lang.Object r10 = r12.zza(r13, r15, (UB) r10, r7)
            int r14 = r14 + 1
            goto L_0x05b1
        L_0x05c0:
            if (r10 == 0) goto L_0x05c5
            r7.zzf(r13, r10)
        L_0x05c5:
            return
        L_0x05c6:
            r14 = move-exception
            int r15 = r12.zzajk
        L_0x05c9:
            int r0 = r12.zzajl
            if (r15 >= r0) goto L_0x05d8
            int[] r0 = r12.zzajj
            r0 = r0[r15]
            java.lang.Object r10 = r12.zza(r13, r0, (UB) r10, r7)
            int r15 = r15 + 1
            goto L_0x05c9
        L_0x05d8:
            if (r10 == 0) goto L_0x05dd
            r7.zzf(r13, r10)
        L_0x05dd:
            throw r14
        L_0x05de:
            java.lang.NullPointerException r13 = new java.lang.NullPointerException
            r13.<init>()
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zza(java.lang.Object, com.google.android.gms.internal.measurement.zzgx, com.google.android.gms.internal.measurement.zzem):void");
    }

    private static zzhr zzt(Object obj) {
        zzez zzez = (zzez) obj;
        zzhr zzhr = zzez.zzagn;
        if (zzhr != zzhr.zzor()) {
            return zzhr;
        }
        zzhr zzos = zzhr.zzos();
        zzez.zzagn = zzos;
        return zzos;
    }

    private static int zza(byte[] bArr, int i, int i2, zzif zzif, Class<?> cls, zzdm zzdm) throws IOException {
        switch (zzgm.zzacu[zzif.ordinal()]) {
            case 1:
                int zzb = zzdl.zzb(bArr, i, zzdm);
                zzdm.zzabu = Boolean.valueOf(zzdm.zzabt != 0);
                return zzb;
            case 2:
                return zzdl.zze(bArr, i, zzdm);
            case 3:
                zzdm.zzabu = Double.valueOf(zzdl.zzc(bArr, i));
                return i + 8;
            case 4:
            case 5:
                zzdm.zzabu = Integer.valueOf(zzdl.zza(bArr, i));
                return i + 4;
            case 6:
            case 7:
                zzdm.zzabu = Long.valueOf(zzdl.zzb(bArr, i));
                return i + 8;
            case 8:
                zzdm.zzabu = Float.valueOf(zzdl.zzd(bArr, i));
                return i + 4;
            case 9:
            case 10:
            case 11:
                int zza = zzdl.zza(bArr, i, zzdm);
                zzdm.zzabu = Integer.valueOf(zzdm.zzabs);
                return zza;
            case 12:
            case 13:
                int zzb2 = zzdl.zzb(bArr, i, zzdm);
                zzdm.zzabu = Long.valueOf(zzdm.zzabt);
                return zzb2;
            case 14:
                return zzdl.zza(zzgu.zznz().zzf(cls), bArr, i, i2, zzdm);
            case 15:
                int zza2 = zzdl.zza(bArr, i, zzdm);
                zzdm.zzabu = Integer.valueOf(zzeb.zzaa(zzdm.zzabs));
                return zza2;
            case 16:
                int zzb3 = zzdl.zzb(bArr, i, zzdm);
                zzdm.zzabu = Long.valueOf(zzeb.zzap(zzdm.zzabt));
                return zzb3;
            case 17:
                return zzdl.zzd(bArr, i, zzdm);
            default:
                throw new RuntimeException("unsupported field type.");
        }
    }

    private final int zza(T t, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, long j, int i7, long j2, zzdm zzdm) throws IOException {
        int i8;
        int i9;
        int i10;
        int i11;
        T t2 = t;
        byte[] bArr2 = bArr;
        int i12 = i;
        int i13 = i2;
        int i14 = i3;
        int i15 = i5;
        int i16 = i6;
        long j3 = j2;
        zzdm zzdm2 = zzdm;
        zzfg zzfg = (zzfg) zzaiz.getObject(t2, j3);
        if (!zzfg.zzjy()) {
            int size = zzfg.size();
            zzfg = zzfg.zzq(size == 0 ? 10 : size << 1);
            zzaiz.putObject(t2, j3, zzfg);
        }
        switch (i7) {
            case 18:
            case 35:
                if (i15 == 2) {
                    zzej zzej = (zzej) zzfg;
                    int zza = zzdl.zza(bArr2, i12, zzdm2);
                    int i17 = zzdm2.zzabs + zza;
                    while (zza < i17) {
                        zzej.zzf(zzdl.zzc(bArr2, zza));
                        zza += 8;
                    }
                    if (zza == i17) {
                        return zza;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 1) {
                    zzej zzej2 = (zzej) zzfg;
                    zzej2.zzf(zzdl.zzc(bArr, i));
                    int i18 = i12 + 8;
                    while (i18 < i13) {
                        int zza2 = zzdl.zza(bArr2, i18, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return i18;
                        }
                        zzej2.zzf(zzdl.zzc(bArr2, zza2));
                        i18 = zza2 + 8;
                    }
                    return i18;
                }
                break;
            case 19:
            case 36:
                if (i15 == 2) {
                    zzew zzew = (zzew) zzfg;
                    int zza3 = zzdl.zza(bArr2, i12, zzdm2);
                    int i19 = zzdm2.zzabs + zza3;
                    while (zza3 < i19) {
                        zzew.zzc(zzdl.zzd(bArr2, zza3));
                        zza3 += 4;
                    }
                    if (zza3 == i19) {
                        return zza3;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 5) {
                    zzew zzew2 = (zzew) zzfg;
                    zzew2.zzc(zzdl.zzd(bArr, i));
                    int i20 = i12 + 4;
                    while (i20 < i13) {
                        int zza4 = zzdl.zza(bArr2, i20, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return i20;
                        }
                        zzew2.zzc(zzdl.zzd(bArr2, zza4));
                        i20 = zza4 + 4;
                    }
                    return i20;
                }
                break;
            case 20:
            case 21:
            case 37:
            case 38:
                if (i15 == 2) {
                    zzfv zzfv = (zzfv) zzfg;
                    int zza5 = zzdl.zza(bArr2, i12, zzdm2);
                    int i21 = zzdm2.zzabs + zza5;
                    while (zza5 < i21) {
                        zza5 = zzdl.zzb(bArr2, zza5, zzdm2);
                        zzfv.zzbb(zzdm2.zzabt);
                    }
                    if (zza5 == i21) {
                        return zza5;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 0) {
                    zzfv zzfv2 = (zzfv) zzfg;
                    int zzb = zzdl.zzb(bArr2, i12, zzdm2);
                    zzfv2.zzbb(zzdm2.zzabt);
                    while (zzb < i13) {
                        int zza6 = zzdl.zza(bArr2, zzb, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return zzb;
                        }
                        zzb = zzdl.zzb(bArr2, zza6, zzdm2);
                        zzfv2.zzbb(zzdm2.zzabt);
                    }
                    return zzb;
                }
                break;
            case 22:
            case 29:
            case 39:
            case 43:
                if (i15 == 2) {
                    return zzdl.zza(bArr2, i12, zzfg, zzdm2);
                }
                if (i15 == 0) {
                    return zzdl.zza(i3, bArr, i, i2, zzfg, zzdm);
                }
                break;
            case 23:
            case 32:
            case 40:
            case 46:
                if (i15 == 2) {
                    zzfv zzfv3 = (zzfv) zzfg;
                    int zza7 = zzdl.zza(bArr2, i12, zzdm2);
                    int i22 = zzdm2.zzabs + zza7;
                    while (zza7 < i22) {
                        zzfv3.zzbb(zzdl.zzb(bArr2, zza7));
                        zza7 += 8;
                    }
                    if (zza7 == i22) {
                        return zza7;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 1) {
                    zzfv zzfv4 = (zzfv) zzfg;
                    zzfv4.zzbb(zzdl.zzb(bArr, i));
                    int i23 = i12 + 8;
                    while (i23 < i13) {
                        int zza8 = zzdl.zza(bArr2, i23, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return i23;
                        }
                        zzfv4.zzbb(zzdl.zzb(bArr2, zza8));
                        i23 = zza8 + 8;
                    }
                    return i23;
                }
                break;
            case 24:
            case 31:
            case 41:
            case 45:
                if (i15 == 2) {
                    zzfa zzfa = (zzfa) zzfg;
                    int zza9 = zzdl.zza(bArr2, i12, zzdm2);
                    int i24 = zzdm2.zzabs + zza9;
                    while (zza9 < i24) {
                        zzfa.zzau(zzdl.zza(bArr2, zza9));
                        zza9 += 4;
                    }
                    if (zza9 == i24) {
                        return zza9;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 5) {
                    zzfa zzfa2 = (zzfa) zzfg;
                    zzfa2.zzau(zzdl.zza(bArr, i));
                    int i25 = i12 + 4;
                    while (i25 < i13) {
                        int zza10 = zzdl.zza(bArr2, i25, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return i25;
                        }
                        zzfa2.zzau(zzdl.zza(bArr2, zza10));
                        i25 = zza10 + 4;
                    }
                    return i25;
                }
                break;
            case 25:
            case 42:
                if (i15 == 2) {
                    zzdn zzdn = (zzdn) zzfg;
                    int zza11 = zzdl.zza(bArr2, i12, zzdm2);
                    int i26 = zzdm2.zzabs + zza11;
                    while (zza11 < i26) {
                        zza11 = zzdl.zzb(bArr2, zza11, zzdm2);
                        zzdn.addBoolean(zzdm2.zzabt != 0);
                    }
                    if (zza11 == i26) {
                        return zza11;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 0) {
                    zzdn zzdn2 = (zzdn) zzfg;
                    int zzb2 = zzdl.zzb(bArr2, i12, zzdm2);
                    zzdn2.addBoolean(zzdm2.zzabt != 0);
                    while (zzb2 < i13) {
                        int zza12 = zzdl.zza(bArr2, zzb2, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return zzb2;
                        }
                        zzb2 = zzdl.zzb(bArr2, zza12, zzdm2);
                        zzdn2.addBoolean(zzdm2.zzabt != 0);
                    }
                    return zzb2;
                }
                break;
            case 26:
                if (i15 == 2) {
                    if ((j & 536870912) == 0) {
                        int zza13 = zzdl.zza(bArr2, i12, zzdm2);
                        int i27 = zzdm2.zzabs;
                        if (i27 >= 0) {
                            if (i27 == 0) {
                                zzfg.add("");
                            } else {
                                zzfg.add(new String(bArr2, zza13, i27, zzfb.UTF_8));
                                zza13 += i27;
                            }
                            while (i9 < i13) {
                                int zza14 = zzdl.zza(bArr2, i9, zzdm2);
                                if (i14 != zzdm2.zzabs) {
                                    return i9;
                                }
                                i9 = zzdl.zza(bArr2, zza14, zzdm2);
                                int i28 = zzdm2.zzabs;
                                if (i28 < 0) {
                                    throw zzfh.zzmv();
                                } else if (i28 == 0) {
                                    zzfg.add("");
                                } else {
                                    zzfg.add(new String(bArr2, i9, i28, zzfb.UTF_8));
                                    i9 += i28;
                                }
                            }
                            return i9;
                        }
                        throw zzfh.zzmv();
                    }
                    int zza15 = zzdl.zza(bArr2, i12, zzdm2);
                    int i29 = zzdm2.zzabs;
                    if (i29 >= 0) {
                        if (i29 == 0) {
                            zzfg.add("");
                        } else {
                            int i30 = zza15 + i29;
                            if (zzhy.zzf(bArr2, zza15, i30)) {
                                zzfg.add(new String(bArr2, zza15, i29, zzfb.UTF_8));
                                zza15 = i30;
                            } else {
                                throw zzfh.zznc();
                            }
                        }
                        while (i8 < i13) {
                            int zza16 = zzdl.zza(bArr2, i8, zzdm2);
                            if (i14 != zzdm2.zzabs) {
                                return i8;
                            }
                            i8 = zzdl.zza(bArr2, zza16, zzdm2);
                            int i31 = zzdm2.zzabs;
                            if (i31 < 0) {
                                throw zzfh.zzmv();
                            } else if (i31 == 0) {
                                zzfg.add("");
                            } else {
                                int i32 = i8 + i31;
                                if (zzhy.zzf(bArr2, i8, i32)) {
                                    zzfg.add(new String(bArr2, i8, i31, zzfb.UTF_8));
                                    i8 = i32;
                                } else {
                                    throw zzfh.zznc();
                                }
                            }
                        }
                        return i8;
                    }
                    throw zzfh.zzmv();
                }
                break;
            case 27:
                if (i15 == 2) {
                    return zzdl.zza(zzax(i16), i3, bArr, i, i2, zzfg, zzdm);
                }
                break;
            case 28:
                if (i15 == 2) {
                    int zza17 = zzdl.zza(bArr2, i12, zzdm2);
                    int i33 = zzdm2.zzabs;
                    if (i33 < 0) {
                        throw zzfh.zzmv();
                    } else if (i33 <= bArr2.length - zza17) {
                        if (i33 == 0) {
                            zzfg.add(zzdp.zzaby);
                        } else {
                            zzfg.add(zzdp.zzb(bArr2, zza17, i33));
                            zza17 += i33;
                        }
                        while (i10 < i13) {
                            int zza18 = zzdl.zza(bArr2, i10, zzdm2);
                            if (i14 != zzdm2.zzabs) {
                                return i10;
                            }
                            i10 = zzdl.zza(bArr2, zza18, zzdm2);
                            int i34 = zzdm2.zzabs;
                            if (i34 < 0) {
                                throw zzfh.zzmv();
                            } else if (i34 > bArr2.length - i10) {
                                throw zzfh.zzmu();
                            } else if (i34 == 0) {
                                zzfg.add(zzdp.zzaby);
                            } else {
                                zzfg.add(zzdp.zzb(bArr2, i10, i34));
                                i10 += i34;
                            }
                        }
                        return i10;
                    } else {
                        throw zzfh.zzmu();
                    }
                }
                break;
            case 30:
            case 44:
                if (i15 == 2) {
                    i11 = zzdl.zza(bArr2, i12, zzfg, zzdm2);
                } else if (i15 == 0) {
                    i11 = zzdl.zza(i3, bArr, i, i2, zzfg, zzdm);
                }
                zzez zzez = (zzez) t2;
                zzhr zzhr = zzez.zzagn;
                if (zzhr == zzhr.zzor()) {
                    zzhr = null;
                }
                zzhr zzhr2 = (zzhr) zzha.zza(i4, zzfg, zzaz(i16), zzhr, this.zzajo);
                if (zzhr2 != null) {
                    zzez.zzagn = zzhr2;
                }
                return i11;
            case 33:
            case 47:
                if (i15 == 2) {
                    zzfa zzfa3 = (zzfa) zzfg;
                    int zza19 = zzdl.zza(bArr2, i12, zzdm2);
                    int i35 = zzdm2.zzabs + zza19;
                    while (zza19 < i35) {
                        zza19 = zzdl.zza(bArr2, zza19, zzdm2);
                        zzfa3.zzau(zzeb.zzaa(zzdm2.zzabs));
                    }
                    if (zza19 == i35) {
                        return zza19;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 0) {
                    zzfa zzfa4 = (zzfa) zzfg;
                    int zza20 = zzdl.zza(bArr2, i12, zzdm2);
                    zzfa4.zzau(zzeb.zzaa(zzdm2.zzabs));
                    while (zza20 < i13) {
                        int zza21 = zzdl.zza(bArr2, zza20, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return zza20;
                        }
                        zza20 = zzdl.zza(bArr2, zza21, zzdm2);
                        zzfa4.zzau(zzeb.zzaa(zzdm2.zzabs));
                    }
                    return zza20;
                }
                break;
            case 34:
            case 48:
                if (i15 == 2) {
                    zzfv zzfv5 = (zzfv) zzfg;
                    int zza22 = zzdl.zza(bArr2, i12, zzdm2);
                    int i36 = zzdm2.zzabs + zza22;
                    while (zza22 < i36) {
                        zza22 = zzdl.zzb(bArr2, zza22, zzdm2);
                        zzfv5.zzbb(zzeb.zzap(zzdm2.zzabt));
                    }
                    if (zza22 == i36) {
                        return zza22;
                    }
                    throw zzfh.zzmu();
                } else if (i15 == 0) {
                    zzfv zzfv6 = (zzfv) zzfg;
                    int zzb3 = zzdl.zzb(bArr2, i12, zzdm2);
                    zzfv6.zzbb(zzeb.zzap(zzdm2.zzabt));
                    while (zzb3 < i13) {
                        int zza23 = zzdl.zza(bArr2, zzb3, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return zzb3;
                        }
                        zzb3 = zzdl.zzb(bArr2, zza23, zzdm2);
                        zzfv6.zzbb(zzeb.zzap(zzdm2.zzabt));
                    }
                    return zzb3;
                }
                break;
            case 49:
                if (i15 == 3) {
                    zzgy zzax = zzax(i16);
                    int i37 = (i14 & -8) | 4;
                    int zza24 = zzdl.zza(zzax, bArr, i, i2, i37, zzdm);
                    zzfg.add(zzdm2.zzabu);
                    while (zza24 < i13) {
                        int zza25 = zzdl.zza(bArr2, zza24, zzdm2);
                        if (i14 != zzdm2.zzabs) {
                            return zza24;
                        }
                        zza24 = zzdl.zza(zzax, bArr, zza25, i2, i37, zzdm);
                        zzfg.add(zzdm2.zzabu);
                    }
                    return zza24;
                }
                break;
        }
        return i12;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final <K, V> int zza(T r8, byte[] r9, int r10, int r11, int r12, long r13, com.google.android.gms.internal.measurement.zzdm r15) throws java.io.IOException {
        /*
            r7 = this;
            sun.misc.Unsafe r0 = zzaiz
            java.lang.Object r12 = r7.zzay(r12)
            java.lang.Object r1 = r0.getObject(r8, r13)
            com.google.android.gms.internal.measurement.zzgc r2 = r7.zzajq
            boolean r2 = r2.zzo(r1)
            if (r2 == 0) goto L_0x0021
            com.google.android.gms.internal.measurement.zzgc r2 = r7.zzajq
            java.lang.Object r2 = r2.zzq(r12)
            com.google.android.gms.internal.measurement.zzgc r3 = r7.zzajq
            r3.zzb(r2, r1)
            r0.putObject(r8, r13, r2)
            r1 = r2
        L_0x0021:
            com.google.android.gms.internal.measurement.zzgc r8 = r7.zzajq
            com.google.android.gms.internal.measurement.zzga r8 = r8.zzr(r12)
            com.google.android.gms.internal.measurement.zzgc r12 = r7.zzajq
            java.util.Map r12 = r12.zzm(r1)
            int r10 = com.google.android.gms.internal.measurement.zzdl.zza(r9, r10, r15)
            int r13 = r15.zzabs
            if (r13 < 0) goto L_0x0096
            int r14 = r11 - r10
            if (r13 > r14) goto L_0x0096
            int r13 = r13 + r10
            K r14 = r8.zzait
            V r0 = r8.zzzw
        L_0x003e:
            if (r10 >= r13) goto L_0x008b
            int r1 = r10 + 1
            byte r10 = r9[r10]
            if (r10 >= 0) goto L_0x004e
            int r1 = com.google.android.gms.internal.measurement.zzdl.zza(r10, r9, r1, r15)
            int r10 = r15.zzabs
            r2 = r1
            goto L_0x004f
        L_0x004e:
            r2 = r1
        L_0x004f:
            int r1 = r10 >>> 3
            r3 = r10 & 7
            switch(r1) {
                case 1: goto L_0x0071;
                case 2: goto L_0x0057;
                default: goto L_0x0056;
            }
        L_0x0056:
            goto L_0x0086
        L_0x0057:
            com.google.android.gms.internal.measurement.zzif r1 = r8.zzaiu
            int r1 = r1.zzpc()
            if (r3 != r1) goto L_0x0086
            com.google.android.gms.internal.measurement.zzif r4 = r8.zzaiu
            V r10 = r8.zzzw
            java.lang.Class r5 = r10.getClass()
            r1 = r9
            r3 = r11
            r6 = r15
            int r10 = zza(r1, r2, r3, r4, r5, r6)
            java.lang.Object r0 = r15.zzabu
            goto L_0x003e
        L_0x0071:
            com.google.android.gms.internal.measurement.zzif r1 = r8.zzais
            int r1 = r1.zzpc()
            if (r3 != r1) goto L_0x0086
            com.google.android.gms.internal.measurement.zzif r4 = r8.zzais
            r5 = 0
            r1 = r9
            r3 = r11
            r6 = r15
            int r10 = zza(r1, r2, r3, r4, r5, r6)
            java.lang.Object r14 = r15.zzabu
            goto L_0x003e
        L_0x0086:
            int r10 = com.google.android.gms.internal.measurement.zzdl.zza(r10, r9, r2, r11, r15)
            goto L_0x003e
        L_0x008b:
            if (r10 != r13) goto L_0x0091
            r12.put(r14, r0)
            return r13
        L_0x0091:
            com.google.android.gms.internal.measurement.zzfh r8 = com.google.android.gms.internal.measurement.zzfh.zznb()
            throw r8
        L_0x0096:
            com.google.android.gms.internal.measurement.zzfh r8 = com.google.android.gms.internal.measurement.zzfh.zzmu()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zza(java.lang.Object, byte[], int, int, int, long, com.google.android.gms.internal.measurement.zzdm):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:65:0x01a1, code lost:
        r12.putInt(r1, r13, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int zza(T r17, byte[] r18, int r19, int r20, int r21, int r22, int r23, int r24, int r25, long r26, int r28, com.google.android.gms.internal.measurement.zzdm r29) throws java.io.IOException {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r3 = r18
            r4 = r19
            r2 = r21
            r8 = r22
            r5 = r23
            r9 = r26
            r6 = r28
            r11 = r29
            sun.misc.Unsafe r12 = zzaiz
            int[] r7 = r0.zzaja
            int r13 = r6 + 2
            r7 = r7[r13]
            r13 = 1048575(0xfffff, float:1.469367E-39)
            r7 = r7 & r13
            long r13 = (long) r7
            r7 = 5
            r15 = 2
            switch(r25) {
                case 51: goto L_0x0191;
                case 52: goto L_0x0181;
                case 53: goto L_0x0171;
                case 54: goto L_0x0171;
                case 55: goto L_0x0161;
                case 56: goto L_0x0150;
                case 57: goto L_0x0140;
                case 58: goto L_0x0127;
                case 59: goto L_0x00f3;
                case 60: goto L_0x00c5;
                case 61: goto L_0x00b8;
                case 62: goto L_0x0161;
                case 63: goto L_0x008a;
                case 64: goto L_0x0140;
                case 65: goto L_0x0150;
                case 66: goto L_0x0075;
                case 67: goto L_0x0060;
                case 68: goto L_0x0028;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x01a5
        L_0x0028:
            r7 = 3
            if (r5 != r7) goto L_0x01a5
            r2 = r2 & -8
            r7 = r2 | 4
            com.google.android.gms.internal.measurement.zzgy r2 = r0.zzax(r6)
            r3 = r18
            r4 = r19
            r5 = r20
            r6 = r7
            r7 = r29
            int r2 = com.google.android.gms.internal.measurement.zzdl.zza(r2, r3, r4, r5, r6, r7)
            int r3 = r12.getInt(r1, r13)
            if (r3 != r8) goto L_0x004b
            java.lang.Object r15 = r12.getObject(r1, r9)
            goto L_0x004c
        L_0x004b:
            r15 = 0
        L_0x004c:
            if (r15 != 0) goto L_0x0055
            java.lang.Object r3 = r11.zzabu
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x0055:
            java.lang.Object r3 = r11.zzabu
            java.lang.Object r3 = com.google.android.gms.internal.measurement.zzfb.zza(r15, r3)
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x0060:
            if (r5 != 0) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zzb(r3, r4, r11)
            long r3 = r11.zzabt
            long r3 = com.google.android.gms.internal.measurement.zzeb.zzap(r3)
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x0075:
            if (r5 != 0) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zza(r3, r4, r11)
            int r3 = r11.zzabs
            int r3 = com.google.android.gms.internal.measurement.zzeb.zzaa(r3)
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x008a:
            if (r5 != 0) goto L_0x01a5
            int r3 = com.google.android.gms.internal.measurement.zzdl.zza(r3, r4, r11)
            int r4 = r11.zzabs
            com.google.android.gms.internal.measurement.zzfe r5 = r0.zzaz(r6)
            if (r5 == 0) goto L_0x00ae
            boolean r5 = r5.zzf(r4)
            if (r5 == 0) goto L_0x009f
            goto L_0x00ae
        L_0x009f:
            com.google.android.gms.internal.measurement.zzhr r1 = zzt(r17)
            long r4 = (long) r4
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            r1.zzb(r2, r4)
            r2 = r3
            goto L_0x01a6
        L_0x00ae:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)
            r12.putObject(r1, r9, r2)
            r2 = r3
            goto L_0x01a1
        L_0x00b8:
            if (r5 != r15) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zze(r3, r4, r11)
            java.lang.Object r3 = r11.zzabu
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x00c5:
            if (r5 != r15) goto L_0x01a5
            com.google.android.gms.internal.measurement.zzgy r2 = r0.zzax(r6)
            r5 = r20
            int r2 = com.google.android.gms.internal.measurement.zzdl.zza(r2, r3, r4, r5, r11)
            int r3 = r12.getInt(r1, r13)
            if (r3 != r8) goto L_0x00dc
            java.lang.Object r15 = r12.getObject(r1, r9)
            goto L_0x00dd
        L_0x00dc:
            r15 = 0
        L_0x00dd:
            if (r15 != 0) goto L_0x00e5
            java.lang.Object r3 = r11.zzabu
            r12.putObject(r1, r9, r3)
            goto L_0x00ee
        L_0x00e5:
            java.lang.Object r3 = r11.zzabu
            java.lang.Object r3 = com.google.android.gms.internal.measurement.zzfb.zza(r15, r3)
            r12.putObject(r1, r9, r3)
        L_0x00ee:
            r12.putInt(r1, r13, r8)
            goto L_0x01a6
        L_0x00f3:
            if (r5 != r15) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zza(r3, r4, r11)
            int r4 = r11.zzabs
            if (r4 != 0) goto L_0x0103
            java.lang.String r3 = ""
            r12.putObject(r1, r9, r3)
            goto L_0x0122
        L_0x0103:
            r5 = 536870912(0x20000000, float:1.0842022E-19)
            r5 = r24 & r5
            if (r5 == 0) goto L_0x0117
            int r5 = r2 + r4
            boolean r5 = com.google.android.gms.internal.measurement.zzhy.zzf(r3, r2, r5)
            if (r5 == 0) goto L_0x0112
            goto L_0x0117
        L_0x0112:
            com.google.android.gms.internal.measurement.zzfh r1 = com.google.android.gms.internal.measurement.zzfh.zznc()
            throw r1
        L_0x0117:
            java.lang.String r5 = new java.lang.String
            java.nio.charset.Charset r6 = com.google.android.gms.internal.measurement.zzfb.UTF_8
            r5.<init>(r3, r2, r4, r6)
            r12.putObject(r1, r9, r5)
            int r2 = r2 + r4
        L_0x0122:
            r12.putInt(r1, r13, r8)
            goto L_0x01a6
        L_0x0127:
            if (r5 != 0) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zzb(r3, r4, r11)
            long r3 = r11.zzabt
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x0137
            r15 = 1
            goto L_0x0138
        L_0x0137:
            r15 = 0
        L_0x0138:
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r15)
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x0140:
            if (r5 != r7) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zza(r18, r19)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r12.putObject(r1, r9, r2)
            int r2 = r4 + 4
            goto L_0x01a1
        L_0x0150:
            r2 = 1
            if (r5 != r2) goto L_0x01a5
            long r2 = com.google.android.gms.internal.measurement.zzdl.zzb(r18, r19)
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            r12.putObject(r1, r9, r2)
            int r2 = r4 + 8
            goto L_0x01a1
        L_0x0161:
            if (r5 != 0) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zza(r3, r4, r11)
            int r3 = r11.zzabs
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x0171:
            if (r5 != 0) goto L_0x01a5
            int r2 = com.google.android.gms.internal.measurement.zzdl.zzb(r3, r4, r11)
            long r3 = r11.zzabt
            java.lang.Long r3 = java.lang.Long.valueOf(r3)
            r12.putObject(r1, r9, r3)
            goto L_0x01a1
        L_0x0181:
            if (r5 != r7) goto L_0x01a5
            float r2 = com.google.android.gms.internal.measurement.zzdl.zzd(r18, r19)
            java.lang.Float r2 = java.lang.Float.valueOf(r2)
            r12.putObject(r1, r9, r2)
            int r2 = r4 + 4
            goto L_0x01a1
        L_0x0191:
            r2 = 1
            if (r5 != r2) goto L_0x01a5
            double r2 = com.google.android.gms.internal.measurement.zzdl.zzc(r18, r19)
            java.lang.Double r2 = java.lang.Double.valueOf(r2)
            r12.putObject(r1, r9, r2)
            int r2 = r4 + 8
        L_0x01a1:
            r12.putInt(r1, r13, r8)
            goto L_0x01a6
        L_0x01a5:
            r2 = r4
        L_0x01a6:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zza(java.lang.Object, byte[], int, int, int, int, int, int, int, long, int, com.google.android.gms.internal.measurement.zzdm):int");
    }

    private final zzgy zzax(int i) {
        int i2 = (i / 3) << 1;
        zzgy zzgy = (zzgy) this.zzajb[i2];
        if (zzgy != null) {
            return zzgy;
        }
        zzgy zzf = zzgu.zznz().zzf((Class) this.zzajb[i2 + 1]);
        this.zzajb[i2] = zzf;
        return zzf;
    }

    private final Object zzay(int i) {
        return this.zzajb[(i / 3) << 1];
    }

    private final zzfe zzaz(int i) {
        return (zzfe) this.zzajb[((i / 3) << 1) + 1];
    }

    /* JADX WARNING: type inference failed for: r30v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v0 */
    /* JADX WARNING: type inference failed for: r12v1, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r0v13, types: [int, byte] */
    /* JADX WARNING: type inference failed for: r5v1, types: [int] */
    /* JADX WARNING: type inference failed for: r12v2 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r0v17, types: [int] */
    /* JADX WARNING: type inference failed for: r1v10, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v3 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r0v26, types: [int] */
    /* JADX WARNING: type inference failed for: r1v14, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v4 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r19v2 */
    /* JADX WARNING: type inference failed for: r8v6 */
    /* JADX WARNING: type inference failed for: r12v6 */
    /* JADX WARNING: type inference failed for: r25v0 */
    /* JADX WARNING: type inference failed for: r7v7 */
    /* JADX WARNING: type inference failed for: r25v1 */
    /* JADX WARNING: type inference failed for: r2v11, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v8, types: [int] */
    /* JADX WARNING: type inference failed for: r7v9 */
    /* JADX WARNING: type inference failed for: r12v8 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r2v14, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r3v14 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r2v17, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v11, types: [int] */
    /* JADX WARNING: type inference failed for: r25v2 */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: type inference failed for: r3v16 */
    /* JADX WARNING: type inference failed for: r7v17 */
    /* JADX WARNING: type inference failed for: r25v3 */
    /* JADX WARNING: type inference failed for: r1v29, types: [int] */
    /* JADX WARNING: type inference failed for: r2v20, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v18 */
    /* JADX WARNING: type inference failed for: r8v11 */
    /* JADX WARNING: type inference failed for: r7v22 */
    /* JADX WARNING: type inference failed for: r8v12 */
    /* JADX WARNING: type inference failed for: r12v12, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v19 */
    /* JADX WARNING: type inference failed for: r8v13 */
    /* JADX WARNING: type inference failed for: r12v13, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v20 */
    /* JADX WARNING: type inference failed for: r8v14 */
    /* JADX WARNING: type inference failed for: r12v14, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v21 */
    /* JADX WARNING: type inference failed for: r8v15 */
    /* JADX WARNING: type inference failed for: r12v15, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v22 */
    /* JADX WARNING: type inference failed for: r8v16 */
    /* JADX WARNING: type inference failed for: r12v16, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v23 */
    /* JADX WARNING: type inference failed for: r8v17 */
    /* JADX WARNING: type inference failed for: r12v17, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v24 */
    /* JADX WARNING: type inference failed for: r8v18 */
    /* JADX WARNING: type inference failed for: r12v18, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v25 */
    /* JADX WARNING: type inference failed for: r8v19 */
    /* JADX WARNING: type inference failed for: r12v19, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v26 */
    /* JADX WARNING: type inference failed for: r8v20 */
    /* JADX WARNING: type inference failed for: r12v20, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v27 */
    /* JADX WARNING: type inference failed for: r8v21 */
    /* JADX WARNING: type inference failed for: r12v21, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v28 */
    /* JADX WARNING: type inference failed for: r8v22, types: [int] */
    /* JADX WARNING: type inference failed for: r12v22, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v29 */
    /* JADX WARNING: type inference failed for: r3v31 */
    /* JADX WARNING: type inference failed for: r8v23 */
    /* JADX WARNING: type inference failed for: r12v23, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v32 */
    /* JADX WARNING: type inference failed for: r8v24 */
    /* JADX WARNING: type inference failed for: r12v24 */
    /* JADX WARNING: type inference failed for: r12v25, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r3v33 */
    /* JADX WARNING: type inference failed for: r8v26 */
    /* JADX WARNING: type inference failed for: r12v26 */
    /* JADX WARNING: type inference failed for: r1v71, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r8v28 */
    /* JADX WARNING: type inference failed for: r3v35 */
    /* JADX WARNING: type inference failed for: r12v27 */
    /* JADX WARNING: type inference failed for: r8v29 */
    /* JADX WARNING: type inference failed for: r12v28 */
    /* JADX WARNING: type inference failed for: r7v46 */
    /* JADX WARNING: type inference failed for: r5v28 */
    /* JADX WARNING: type inference failed for: r3v36, types: [int] */
    /* JADX WARNING: type inference failed for: r5v29 */
    /* JADX WARNING: type inference failed for: r12v29 */
    /* JADX WARNING: type inference failed for: r3v37 */
    /* JADX WARNING: type inference failed for: r12v30 */
    /* JADX WARNING: type inference failed for: r12v31 */
    /* JADX WARNING: type inference failed for: r12v32 */
    /* JADX WARNING: type inference failed for: r12v33 */
    /* JADX WARNING: type inference failed for: r8v31 */
    /* JADX WARNING: type inference failed for: r12v34 */
    /* JADX WARNING: type inference failed for: r8v32 */
    /* JADX WARNING: type inference failed for: r12v35 */
    /* JADX WARNING: type inference failed for: r8v33 */
    /* JADX WARNING: type inference failed for: r12v36 */
    /* JADX WARNING: type inference failed for: r8v34 */
    /* JADX WARNING: type inference failed for: r12v37 */
    /* JADX WARNING: type inference failed for: r8v35 */
    /* JADX WARNING: type inference failed for: r12v38 */
    /* JADX WARNING: type inference failed for: r8v36 */
    /* JADX WARNING: type inference failed for: r12v39 */
    /* JADX WARNING: type inference failed for: r8v37 */
    /* JADX WARNING: type inference failed for: r12v40 */
    /* JADX WARNING: type inference failed for: r12v41 */
    /* JADX WARNING: type inference failed for: r8v38 */
    /* JADX WARNING: type inference failed for: r12v42 */
    /* JADX WARNING: type inference failed for: r8v39 */
    /* JADX WARNING: type inference failed for: r12v43 */
    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=null, for r0v13, types: [int, byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r30v0, types: [byte[]] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r12v2
      assigns: []
      uses: []
      mth insns count: 711
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 52 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int zza(T r29, byte[] r30, int r31, int r32, int r33, com.google.android.gms.internal.measurement.zzdm r34) throws java.io.IOException {
        /*
            r28 = this;
            r15 = r28
            r14 = r29
            r12 = r30
            r13 = r32
            r11 = r33
            r9 = r34
            sun.misc.Unsafe r10 = zzaiz
            r16 = 0
            r0 = r31
            r1 = -1
            r2 = 0
            r3 = 0
            r6 = 0
            r7 = -1
        L_0x0017:
            if (r0 >= r13) goto L_0x0553
            int r3 = r0 + 1
            byte r0 = r12[r0]
            if (r0 >= 0) goto L_0x0028
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r12, r3, r9)
            int r3 = r9.zzabs
            r4 = r0
            r5 = r3
            goto L_0x002a
        L_0x0028:
            r5 = r0
            r4 = r3
        L_0x002a:
            int r3 = r5 >>> 3
            r0 = r5 & 7
            r8 = 3
            if (r3 <= r1) goto L_0x0039
            int r2 = r2 / r8
            int r1 = r15.zzp(r3, r2)
            r2 = r1
            r1 = -1
            goto L_0x003f
        L_0x0039:
            int r1 = r15.zzbd(r3)
            r2 = r1
            r1 = -1
        L_0x003f:
            if (r2 != r1) goto L_0x0050
            r24 = r3
            r2 = r4
            r19 = r6
            r17 = r7
            r26 = r10
            r6 = r11
            r18 = 0
            r7 = r5
            goto L_0x04aa
        L_0x0050:
            int[] r1 = r15.zzaja
            int r18 = r2 + 1
            r8 = r1[r18]
            r18 = 267386880(0xff00000, float:2.3665827E-29)
            r18 = r8 & r18
            int r11 = r18 >>> 20
            r18 = 1048575(0xfffff, float:1.469367E-39)
            r19 = r5
            r5 = r8 & r18
            long r12 = (long) r5
            r5 = 17
            r20 = r8
            if (r11 > r5) goto L_0x037a
            int r5 = r2 + 2
            r1 = r1[r5]
            int r5 = r1 >>> 20
            r8 = 1
            int r22 = r8 << r5
            r1 = r1 & r18
            if (r1 == r7) goto L_0x0085
            r5 = -1
            if (r7 == r5) goto L_0x007e
            long r8 = (long) r7
            r10.putInt(r14, r8, r6)
        L_0x007e:
            long r6 = (long) r1
            int r6 = r10.getInt(r14, r6)
            r7 = r1
            goto L_0x0086
        L_0x0085:
            r5 = -1
        L_0x0086:
            r1 = 5
            switch(r11) {
                case 0: goto L_0x0342;
                case 1: goto L_0x031b;
                case 2: goto L_0x02f0;
                case 3: goto L_0x02f0;
                case 4: goto L_0x02c9;
                case 5: goto L_0x0295;
                case 6: goto L_0x026d;
                case 7: goto L_0x0238;
                case 8: goto L_0x0204;
                case 9: goto L_0x01c3;
                case 10: goto L_0x019b;
                case 11: goto L_0x02c9;
                case 12: goto L_0x0151;
                case 13: goto L_0x026d;
                case 14: goto L_0x0295;
                case 15: goto L_0x0126;
                case 16: goto L_0x00ee;
                case 17: goto L_0x0099;
                default: goto L_0x008a;
            }
        L_0x008a:
            r9 = r2
            r11 = r3
            r31 = r7
            r8 = r19
            r12 = r30
            r13 = r34
            r18 = -1
            r7 = r4
            goto L_0x036a
        L_0x0099:
            r8 = 3
            if (r0 != r8) goto L_0x00df
            int r0 = r3 << 3
            r8 = r0 | 4
            com.google.android.gms.internal.measurement.zzgy r0 = r15.zzax(r2)
            r1 = r30
            r9 = r2
            r2 = r4
            r11 = r3
            r3 = r32
            r4 = r8
            r8 = r19
            r18 = -1
            r5 = r34
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r1, r2, r3, r4, r5)
            r1 = r6 & r22
            if (r1 != 0) goto L_0x00c2
            r5 = r34
            java.lang.Object r1 = r5.zzabu
            r10.putObject(r14, r12, r1)
            goto L_0x00d1
        L_0x00c2:
            r5 = r34
            java.lang.Object r1 = r10.getObject(r14, r12)
            java.lang.Object r2 = r5.zzabu
            java.lang.Object r1 = com.google.android.gms.internal.measurement.zzfb.zza(r1, r2)
            r10.putObject(r14, r12, r1)
        L_0x00d1:
            r6 = r6 | r22
            r3 = r8
            r2 = r9
            r1 = r11
            r11 = r33
            r12 = r30
            r13 = r32
            r9 = r5
            goto L_0x0017
        L_0x00df:
            r9 = r2
            r11 = r3
            r8 = r19
            r18 = -1
            r31 = r7
            r12 = r30
            r13 = r34
            r7 = r4
            goto L_0x036a
        L_0x00ee:
            r9 = r2
            r11 = r3
            r8 = r19
            r5 = r34
            r18 = -1
            if (r0 != 0) goto L_0x011e
            r2 = r12
            r12 = r30
            int r13 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r4, r5)
            long r0 = r5.zzabt
            long r19 = com.google.android.gms.internal.measurement.zzeb.zzap(r0)
            r0 = r10
            r1 = r29
            r31 = r13
            r13 = r5
            r4 = r19
            r0.putLong(r1, r2, r4)
            r6 = r6 | r22
            r0 = r31
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x011e:
            r13 = r5
            r12 = r30
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x0126:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != 0) goto L_0x014c
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r4, r13)
            int r1 = r13.zzabs
            int r1 = com.google.android.gms.internal.measurement.zzeb.zzaa(r1)
            r10.putInt(r14, r2, r1)
            r6 = r6 | r22
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x014c:
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x0151:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != 0) goto L_0x0196
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r4, r13)
            int r1 = r13.zzabs
            com.google.android.gms.internal.measurement.zzfe r4 = r15.zzaz(r9)
            if (r4 == 0) goto L_0x0187
            boolean r4 = r4.zzf(r1)
            if (r4 == 0) goto L_0x0171
            goto L_0x0187
        L_0x0171:
            com.google.android.gms.internal.measurement.zzhr r2 = zzt(r29)
            long r3 = (long) r1
            java.lang.Long r1 = java.lang.Long.valueOf(r3)
            r2.zzb(r8, r1)
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x0187:
            r10.putInt(r14, r2, r1)
            r6 = r6 | r22
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x0196:
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x019b:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r1 = 2
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != r1) goto L_0x01be
            int r0 = com.google.android.gms.internal.measurement.zzdl.zze(r12, r4, r13)
            java.lang.Object r1 = r13.zzabu
            r10.putObject(r14, r2, r1)
            r6 = r6 | r22
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x01be:
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x01c3:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r1 = 2
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != r1) goto L_0x01fd
            com.google.android.gms.internal.measurement.zzgy r0 = r15.zzax(r9)
            r5 = r32
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r12, r4, r5, r13)
            r1 = r6 & r22
            if (r1 != 0) goto L_0x01e5
            java.lang.Object r1 = r13.zzabu
            r10.putObject(r14, r2, r1)
            goto L_0x01f2
        L_0x01e5:
            java.lang.Object r1 = r10.getObject(r14, r2)
            java.lang.Object r4 = r13.zzabu
            java.lang.Object r1 = com.google.android.gms.internal.measurement.zzfb.zza(r1, r4)
            r10.putObject(r14, r2, r1)
        L_0x01f2:
            r6 = r6 | r22
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r5
            goto L_0x0017
        L_0x01fd:
            r5 = r32
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x0204:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r1 = 2
            r5 = r32
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != r1) goto L_0x0233
            r0 = 536870912(0x20000000, float:1.0842022E-19)
            r0 = r20 & r0
            if (r0 != 0) goto L_0x021f
            int r0 = com.google.android.gms.internal.measurement.zzdl.zzc(r12, r4, r13)
            goto L_0x0223
        L_0x021f:
            int r0 = com.google.android.gms.internal.measurement.zzdl.zzd(r12, r4, r13)
        L_0x0223:
            java.lang.Object r1 = r13.zzabu
            r10.putObject(r14, r2, r1)
            r6 = r6 | r22
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r5
            goto L_0x0017
        L_0x0233:
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x0238:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r5 = r32
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != 0) goto L_0x0268
            int r0 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r4, r13)
            r31 = r0
            long r0 = r13.zzabt
            r19 = 0
            int r0 = (r0 > r19 ? 1 : (r0 == r19 ? 0 : -1))
            if (r0 == 0) goto L_0x0257
            r0 = 1
            goto L_0x0258
        L_0x0257:
            r0 = 0
        L_0x0258:
            com.google.android.gms.internal.measurement.zzhw.zza(r14, r2, r0)
            r6 = r6 | r22
            r0 = r31
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r5
            goto L_0x0017
        L_0x0268:
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x026d:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r5 = r32
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != r1) goto L_0x0290
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r4)
            r10.putInt(r14, r2, r0)
            int r0 = r4 + 4
            r6 = r6 | r22
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r5
            goto L_0x0017
        L_0x0290:
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x0295:
            r9 = r2
            r11 = r3
            r2 = r12
            r8 = r19
            r1 = 1
            r5 = r32
            r12 = r30
            r13 = r34
            r18 = -1
            if (r0 != r1) goto L_0x02c4
            long r19 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r4)
            r0 = r10
            r1 = r29
            r31 = r7
            r7 = r4
            r4 = r19
            r0.putLong(r1, r2, r4)
            int r0 = r7 + 8
            r6 = r6 | r22
            r7 = r31
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x02c4:
            r31 = r7
            r7 = r4
            goto L_0x036a
        L_0x02c9:
            r9 = r2
            r11 = r3
            r31 = r7
            r2 = r12
            r8 = r19
            r12 = r30
            r13 = r34
            r18 = -1
            r7 = r4
            if (r0 != 0) goto L_0x036a
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r7, r13)
            int r1 = r13.zzabs
            r10.putInt(r14, r2, r1)
            r6 = r6 | r22
            r7 = r31
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x02f0:
            r9 = r2
            r11 = r3
            r31 = r7
            r2 = r12
            r8 = r19
            r12 = r30
            r13 = r34
            r18 = -1
            r7 = r4
            if (r0 != 0) goto L_0x036a
            int r7 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r7, r13)
            long r4 = r13.zzabt
            r0 = r10
            r1 = r29
            r0.putLong(r1, r2, r4)
            r6 = r6 | r22
            r0 = r7
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r7 = r31
            r13 = r32
            goto L_0x0017
        L_0x031b:
            r9 = r2
            r11 = r3
            r31 = r7
            r2 = r12
            r8 = r19
            r12 = r30
            r13 = r34
            r18 = -1
            r7 = r4
            if (r0 != r1) goto L_0x036a
            float r0 = com.google.android.gms.internal.measurement.zzdl.zzd(r12, r7)
            com.google.android.gms.internal.measurement.zzhw.zza(r14, r2, r0)
            int r0 = r7 + 4
            r6 = r6 | r22
            r7 = r31
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x0342:
            r9 = r2
            r11 = r3
            r31 = r7
            r2 = r12
            r8 = r19
            r1 = 1
            r12 = r30
            r13 = r34
            r18 = -1
            r7 = r4
            if (r0 != r1) goto L_0x036a
            double r0 = com.google.android.gms.internal.measurement.zzdl.zzc(r12, r7)
            com.google.android.gms.internal.measurement.zzhw.zza(r14, r2, r0)
            int r0 = r7 + 8
            r6 = r6 | r22
            r7 = r31
            r3 = r8
            r2 = r9
            r1 = r11
            r9 = r13
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x036a:
            r17 = r31
            r19 = r6
            r2 = r7
            r7 = r8
            r18 = r9
            r26 = r10
            r24 = r11
            r6 = r33
            goto L_0x04aa
        L_0x037a:
            r5 = r3
            r17 = r7
            r8 = r19
            r18 = -1
            r7 = r4
            r27 = r9
            r9 = r2
            r2 = r12
            r12 = r30
            r13 = r27
            r1 = 27
            if (r11 != r1) goto L_0x03e1
            r1 = 2
            if (r0 != r1) goto L_0x03d4
            java.lang.Object r0 = r10.getObject(r14, r2)
            com.google.android.gms.internal.measurement.zzfg r0 = (com.google.android.gms.internal.measurement.zzfg) r0
            boolean r1 = r0.zzjy()
            if (r1 != 0) goto L_0x03b1
            int r1 = r0.size()
            if (r1 != 0) goto L_0x03a6
            r1 = 10
            goto L_0x03a8
        L_0x03a6:
            int r1 = r1 << 1
        L_0x03a8:
            com.google.android.gms.internal.measurement.zzfg r0 = r0.zzq(r1)
            r10.putObject(r14, r2, r0)
            r11 = r0
            goto L_0x03b2
        L_0x03b1:
            r11 = r0
        L_0x03b2:
            com.google.android.gms.internal.measurement.zzgy r0 = r15.zzax(r9)
            r1 = r8
            r2 = r30
            r3 = r7
            r4 = r32
            r7 = r5
            r5 = r11
            r19 = r6
            r6 = r34
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r1, r2, r3, r4, r5, r6)
            r1 = r7
            r3 = r8
            r2 = r9
            r9 = r13
            r7 = r17
            r6 = r19
            r11 = r33
            r13 = r32
            goto L_0x0017
        L_0x03d4:
            r19 = r6
            r24 = r5
            r15 = r7
            r25 = r8
            r18 = r9
            r26 = r10
            goto L_0x0482
        L_0x03e1:
            r19 = r6
            r6 = r5
            r1 = 49
            if (r11 > r1) goto L_0x0435
            r5 = r20
            long r4 = (long) r5
            r1 = r0
            r0 = r28
            r31 = r1
            r1 = r29
            r22 = r2
            r2 = r30
            r3 = r7
            r20 = r4
            r4 = r32
            r5 = r8
            r24 = r6
            r15 = r7
            r7 = r31
            r25 = r8
            r8 = r9
            r18 = r9
            r26 = r10
            r9 = r20
            r12 = r22
            r14 = r34
            int r0 = r0.zza((T) r1, r2, r3, r4, r5, r6, r7, r8, r9, r11, r12, r14)
            if (r0 != r15) goto L_0x041b
            r2 = r0
            r7 = r25
            r6 = r33
            goto L_0x04aa
        L_0x041b:
            r12 = r30
            r7 = r17
            r2 = r18
            r6 = r19
            r1 = r24
            r3 = r25
            r10 = r26
            r9 = r34
            r11 = r33
            r13 = r32
            r14 = r29
            r15 = r28
            goto L_0x0017
        L_0x0435:
            r31 = r0
            r22 = r2
            r24 = r6
            r15 = r7
            r25 = r8
            r18 = r9
            r26 = r10
            r5 = r20
            r0 = 50
            if (r11 != r0) goto L_0x0488
            r7 = r31
            r0 = 2
            if (r7 != r0) goto L_0x0482
            r0 = r28
            r1 = r29
            r2 = r30
            r3 = r15
            r4 = r32
            r5 = r18
            r6 = r22
            r8 = r34
            int r0 = r0.zza((T) r1, r2, r3, r4, r5, r6, r8)
            if (r0 != r15) goto L_0x0468
            r2 = r0
            r7 = r25
            r6 = r33
            goto L_0x04aa
        L_0x0468:
            r12 = r30
            r7 = r17
            r2 = r18
            r6 = r19
            r1 = r24
            r3 = r25
            r10 = r26
            r9 = r34
            r11 = r33
            r13 = r32
            r14 = r29
            r15 = r28
            goto L_0x0017
        L_0x0482:
            r2 = r15
            r7 = r25
            r6 = r33
            goto L_0x04aa
        L_0x0488:
            r7 = r31
            r0 = r28
            r1 = r29
            r2 = r30
            r3 = r15
            r4 = r32
            r8 = r5
            r5 = r25
            r6 = r24
            r9 = r11
            r10 = r22
            r12 = r18
            r13 = r34
            int r0 = r0.zza((T) r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r12, r13)
            if (r0 != r15) goto L_0x0537
            r2 = r0
            r7 = r25
            r6 = r33
        L_0x04aa:
            if (r7 != r6) goto L_0x04bb
            if (r6 != 0) goto L_0x04af
            goto L_0x04bb
        L_0x04af:
            r3 = r7
            r0 = r17
            r1 = r19
            r4 = -1
            r8 = r28
            r11 = r29
            goto L_0x0562
        L_0x04bb:
            r8 = r28
            boolean r0 = r8.zzajf
            if (r0 == 0) goto L_0x050f
            r9 = r34
            com.google.android.gms.internal.measurement.zzem r0 = r9.zzabv
            com.google.android.gms.internal.measurement.zzem r1 = com.google.android.gms.internal.measurement.zzem.zzls()
            if (r0 == r1) goto L_0x050a
            com.google.android.gms.internal.measurement.zzgh r0 = r8.zzaje
            com.google.android.gms.internal.measurement.zzem r1 = r9.zzabv
            r10 = r24
            com.google.android.gms.internal.measurement.zzez$zzd r0 = r1.zza(r0, r10)
            if (r0 != 0) goto L_0x04fa
            com.google.android.gms.internal.measurement.zzhr r4 = zzt(r29)
            r0 = r7
            r1 = r30
            r3 = r32
            r5 = r34
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r1, r2, r3, r4, r5)
            r12 = r30
            r11 = r6
            r3 = r7
            r15 = r8
            r1 = r10
            r7 = r17
            r2 = r18
            r6 = r19
            r10 = r26
            r13 = r32
            r14 = r29
            goto L_0x0017
        L_0x04fa:
            r11 = r29
            r0 = r11
            com.google.android.gms.internal.measurement.zzez$zzc r0 = (com.google.android.gms.internal.measurement.zzez.zzc) r0
            r0.zzms()
            com.google.android.gms.internal.measurement.zzeq<java.lang.Object> r0 = r0.zzagt
            java.lang.NoSuchMethodError r0 = new java.lang.NoSuchMethodError
            r0.<init>()
            throw r0
        L_0x050a:
            r10 = r24
            r11 = r29
            goto L_0x0515
        L_0x050f:
            r10 = r24
            r9 = r34
            r11 = r29
        L_0x0515:
            com.google.android.gms.internal.measurement.zzhr r4 = zzt(r29)
            r0 = r7
            r1 = r30
            r3 = r32
            r5 = r34
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r1, r2, r3, r4, r5)
            r12 = r30
            r3 = r7
            r15 = r8
            r1 = r10
            r14 = r11
            r7 = r17
            r2 = r18
            r10 = r26
            r13 = r32
            r11 = r6
            r6 = r19
            goto L_0x0017
        L_0x0537:
            r10 = r24
            r7 = r25
            r12 = r30
            r3 = r7
            r1 = r10
            r7 = r17
            r2 = r18
            r6 = r19
            r10 = r26
            r9 = r34
            r11 = r33
            r13 = r32
            r14 = r29
            r15 = r28
            goto L_0x0017
        L_0x0553:
            r19 = r6
            r17 = r7
            r26 = r10
            r6 = r11
            r11 = r14
            r8 = r15
            r2 = r0
            r0 = r17
            r1 = r19
            r4 = -1
        L_0x0562:
            if (r0 == r4) goto L_0x056a
            long r4 = (long) r0
            r0 = r26
            r0.putInt(r11, r4, r1)
        L_0x056a:
            r0 = 0
            int r1 = r8.zzajk
        L_0x056d:
            int r4 = r8.zzajl
            if (r1 >= r4) goto L_0x0580
            int[] r4 = r8.zzajj
            r4 = r4[r1]
            com.google.android.gms.internal.measurement.zzhq<?, ?> r5 = r8.zzajo
            java.lang.Object r0 = r8.zza(r11, r4, (UB) r0, r5)
            com.google.android.gms.internal.measurement.zzhr r0 = (com.google.android.gms.internal.measurement.zzhr) r0
            int r1 = r1 + 1
            goto L_0x056d
        L_0x0580:
            if (r0 == 0) goto L_0x0587
            com.google.android.gms.internal.measurement.zzhq<?, ?> r1 = r8.zzajo
            r1.zzf(r11, r0)
        L_0x0587:
            if (r6 != 0) goto L_0x0593
            r0 = r32
            if (r2 != r0) goto L_0x058e
            goto L_0x0599
        L_0x058e:
            com.google.android.gms.internal.measurement.zzfh r0 = com.google.android.gms.internal.measurement.zzfh.zznb()
            throw r0
        L_0x0593:
            r0 = r32
            if (r2 > r0) goto L_0x059a
            if (r3 != r6) goto L_0x059a
        L_0x0599:
            return r2
        L_0x059a:
            com.google.android.gms.internal.measurement.zzfh r0 = com.google.android.gms.internal.measurement.zzfh.zznb()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zza(java.lang.Object, byte[], int, int, int, com.google.android.gms.internal.measurement.zzdm):int");
    }

    /* JADX WARNING: type inference failed for: r29v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v0 */
    /* JADX WARNING: type inference failed for: r2v0, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v1, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r0v5, types: [int, byte] */
    /* JADX WARNING: type inference failed for: r17v0, types: [int] */
    /* JADX WARNING: type inference failed for: r12v2 */
    /* JADX WARNING: type inference failed for: r0v7, types: [int] */
    /* JADX WARNING: type inference failed for: r1v4, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v3 */
    /* JADX WARNING: type inference failed for: r2v7, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v3, types: [int] */
    /* JADX WARNING: type inference failed for: r12v5 */
    /* JADX WARNING: type inference failed for: r2v10, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r12v6 */
    /* JADX WARNING: type inference failed for: r2v13, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r5v5, types: [int] */
    /* JADX WARNING: type inference failed for: r12v8 */
    /* JADX WARNING: type inference failed for: r1v14, types: [int] */
    /* JADX WARNING: type inference failed for: r2v16, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r17v1 */
    /* JADX WARNING: type inference failed for: r3v13, types: [int] */
    /* JADX WARNING: type inference failed for: r17v2 */
    /* JADX WARNING: type inference failed for: r12v9 */
    /* JADX WARNING: type inference failed for: r12v10 */
    /* JADX WARNING: type inference failed for: r12v11 */
    /* JADX WARNING: type inference failed for: r12v12 */
    /* JADX WARNING: type inference failed for: r12v13 */
    /* JADX WARNING: type inference failed for: r12v14 */
    /* JADX WARNING: type inference failed for: r12v15 */
    /* JADX WARNING: type inference failed for: r12v16 */
    /* JADX WARNING: type inference failed for: r12v17 */
    /* JADX WARNING: type inference failed for: r12v18 */
    /* JADX WARNING: type inference failed for: r12v19 */
    /* JADX WARNING: type inference failed for: r12v20 */
    /* JADX WARNING: type inference failed for: r12v21 */
    /* JADX WARNING: type inference failed for: r12v22 */
    /* JADX WARNING: type inference failed for: r12v23 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=null, for r0v5, types: [int, byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r29v0, types: [byte[]] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r12v2
      assigns: []
      uses: []
      mth insns count: 402
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 16 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(T r28, byte[] r29, int r30, int r31, com.google.android.gms.internal.measurement.zzdm r32) throws java.io.IOException {
        /*
            r27 = this;
            r15 = r27
            r14 = r28
            r12 = r29
            r13 = r31
            r11 = r32
            boolean r0 = r15.zzajh
            if (r0 == 0) goto L_0x035a
            sun.misc.Unsafe r9 = zzaiz
            r10 = -1
            r16 = 0
            r0 = r30
            r1 = -1
            r2 = 0
        L_0x0017:
            if (r0 >= r13) goto L_0x0351
            int r3 = r0 + 1
            byte r0 = r12[r0]
            if (r0 >= 0) goto L_0x0029
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r12, r3, r11)
            int r3 = r11.zzabs
            r8 = r0
            r17 = r3
            goto L_0x002c
        L_0x0029:
            r17 = r0
            r8 = r3
        L_0x002c:
            int r7 = r17 >>> 3
            r6 = r17 & 7
            if (r7 <= r1) goto L_0x003a
            int r2 = r2 / 3
            int r0 = r15.zzp(r7, r2)
            r4 = r0
            goto L_0x003f
        L_0x003a:
            int r0 = r15.zzbd(r7)
            r4 = r0
        L_0x003f:
            if (r4 != r10) goto L_0x004c
            r24 = r7
            r2 = r8
            r18 = r9
            r19 = 0
            r26 = -1
            goto L_0x031b
        L_0x004c:
            int[] r0 = r15.zzaja
            int r1 = r4 + 1
            r5 = r0[r1]
            r0 = 267386880(0xff00000, float:2.3665827E-29)
            r0 = r0 & r5
            int r3 = r0 >>> 20
            r0 = 1048575(0xfffff, float:1.469367E-39)
            r0 = r0 & r5
            long r1 = (long) r0
            r0 = 17
            r10 = 2
            if (r3 > r0) goto L_0x0225
            r0 = 1
            switch(r3) {
                case 0: goto L_0x0208;
                case 1: goto L_0x01ea;
                case 2: goto L_0x01c9;
                case 3: goto L_0x01c9;
                case 4: goto L_0x01ac;
                case 5: goto L_0x018b;
                case 6: goto L_0x016e;
                case 7: goto L_0x0149;
                case 8: goto L_0x0123;
                case 9: goto L_0x00f3;
                case 10: goto L_0x00d7;
                case 11: goto L_0x01ac;
                case 12: goto L_0x00ba;
                case 13: goto L_0x016e;
                case 14: goto L_0x018b;
                case 15: goto L_0x0099;
                case 16: goto L_0x0070;
                default: goto L_0x0065;
            }
        L_0x0065:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x0070:
            if (r6 != 0) goto L_0x008e
            int r6 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r8, r11)
            r19 = r1
            long r0 = r11.zzabt
            long r21 = com.google.android.gms.internal.measurement.zzeb.zzap(r0)
            r0 = r9
            r2 = r19
            r1 = r28
            r10 = r4
            r4 = r21
            r0.putLong(r1, r2, r4)
            r0 = r6
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x008e:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x0099:
            r2 = r1
            r10 = r4
            if (r6 != 0) goto L_0x00af
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r8, r11)
            int r1 = r11.zzabs
            int r1 = com.google.android.gms.internal.measurement.zzeb.zzaa(r1)
            r9.putInt(r14, r2, r1)
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x00af:
            r24 = r7
            r15 = r8
            r18 = r9
            r19 = r10
            r26 = -1
            goto L_0x02fc
        L_0x00ba:
            r2 = r1
            r10 = r4
            if (r6 != 0) goto L_0x00cc
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r8, r11)
            int r1 = r11.zzabs
            r9.putInt(r14, r2, r1)
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x00cc:
            r24 = r7
            r15 = r8
            r18 = r9
            r19 = r10
            r26 = -1
            goto L_0x02fc
        L_0x00d7:
            r2 = r1
            if (r6 != r10) goto L_0x00e8
            int r0 = com.google.android.gms.internal.measurement.zzdl.zze(r12, r8, r11)
            java.lang.Object r1 = r11.zzabu
            r9.putObject(r14, r2, r1)
            r2 = r4
            r1 = r7
            r10 = -1
            goto L_0x0017
        L_0x00e8:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x00f3:
            r2 = r1
            if (r6 != r10) goto L_0x0118
            com.google.android.gms.internal.measurement.zzgy r0 = r15.zzax(r4)
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r12, r8, r13, r11)
            java.lang.Object r1 = r9.getObject(r14, r2)
            if (r1 != 0) goto L_0x010a
            java.lang.Object r1 = r11.zzabu
            r9.putObject(r14, r2, r1)
            goto L_0x0113
        L_0x010a:
            java.lang.Object r5 = r11.zzabu
            java.lang.Object r1 = com.google.android.gms.internal.measurement.zzfb.zza(r1, r5)
            r9.putObject(r14, r2, r1)
        L_0x0113:
            r2 = r4
            r1 = r7
            r10 = -1
            goto L_0x0017
        L_0x0118:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x0123:
            r2 = r1
            if (r6 != r10) goto L_0x013e
            r0 = 536870912(0x20000000, float:1.0842022E-19)
            r0 = r0 & r5
            if (r0 != 0) goto L_0x0130
            int r0 = com.google.android.gms.internal.measurement.zzdl.zzc(r12, r8, r11)
            goto L_0x0134
        L_0x0130:
            int r0 = com.google.android.gms.internal.measurement.zzdl.zzd(r12, r8, r11)
        L_0x0134:
            java.lang.Object r1 = r11.zzabu
            r9.putObject(r14, r2, r1)
            r2 = r4
            r1 = r7
            r10 = -1
            goto L_0x0017
        L_0x013e:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x0149:
            r2 = r1
            if (r6 != 0) goto L_0x0163
            int r1 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r8, r11)
            long r5 = r11.zzabt
            r19 = 0
            int r5 = (r5 > r19 ? 1 : (r5 == r19 ? 0 : -1))
            if (r5 == 0) goto L_0x0159
            goto L_0x015a
        L_0x0159:
            r0 = 0
        L_0x015a:
            com.google.android.gms.internal.measurement.zzhw.zza(r14, r2, r0)
            r0 = r1
            r2 = r4
            r1 = r7
            r10 = -1
            goto L_0x0017
        L_0x0163:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x016e:
            r2 = r1
            r0 = 5
            if (r6 != r0) goto L_0x0180
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r8)
            r9.putInt(r14, r2, r0)
            int r0 = r8 + 4
            r2 = r4
            r1 = r7
            r10 = -1
            goto L_0x0017
        L_0x0180:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x018b:
            r2 = r1
            if (r6 != r0) goto L_0x01a1
            long r5 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r8)
            r0 = r9
            r1 = r28
            r10 = r4
            r4 = r5
            r0.putLong(r1, r2, r4)
            int r0 = r8 + 8
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x01a1:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x01ac:
            r2 = r1
            r10 = r4
            if (r6 != 0) goto L_0x01be
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r12, r8, r11)
            int r1 = r11.zzabs
            r9.putInt(r14, r2, r1)
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x01be:
            r24 = r7
            r15 = r8
            r18 = r9
            r19 = r10
            r26 = -1
            goto L_0x02fc
        L_0x01c9:
            r2 = r1
            r10 = r4
            if (r6 != 0) goto L_0x01df
            int r6 = com.google.android.gms.internal.measurement.zzdl.zzb(r12, r8, r11)
            long r4 = r11.zzabt
            r0 = r9
            r1 = r28
            r0.putLong(r1, r2, r4)
            r0 = r6
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x01df:
            r24 = r7
            r15 = r8
            r18 = r9
            r19 = r10
            r26 = -1
            goto L_0x02fc
        L_0x01ea:
            r2 = r1
            r10 = r4
            r0 = 5
            if (r6 != r0) goto L_0x01fd
            float r0 = com.google.android.gms.internal.measurement.zzdl.zzd(r12, r8)
            com.google.android.gms.internal.measurement.zzhw.zza(r14, r2, r0)
            int r0 = r8 + 4
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x01fd:
            r24 = r7
            r15 = r8
            r18 = r9
            r19 = r10
            r26 = -1
            goto L_0x02fc
        L_0x0208:
            r2 = r1
            r10 = r4
            if (r6 != r0) goto L_0x021a
            double r0 = com.google.android.gms.internal.measurement.zzdl.zzc(r12, r8)
            com.google.android.gms.internal.measurement.zzhw.zza(r14, r2, r0)
            int r0 = r8 + 8
            r1 = r7
            r2 = r10
            r10 = -1
            goto L_0x0017
        L_0x021a:
            r24 = r7
            r15 = r8
            r18 = r9
            r19 = r10
            r26 = -1
            goto L_0x02fc
        L_0x0225:
            r0 = 27
            if (r3 != r0) goto L_0x0270
            if (r6 != r10) goto L_0x0265
            java.lang.Object r0 = r9.getObject(r14, r1)
            com.google.android.gms.internal.measurement.zzfg r0 = (com.google.android.gms.internal.measurement.zzfg) r0
            boolean r3 = r0.zzjy()
            if (r3 != 0) goto L_0x024b
            int r3 = r0.size()
            if (r3 != 0) goto L_0x0240
            r3 = 10
            goto L_0x0242
        L_0x0240:
            int r3 = r3 << 1
        L_0x0242:
            com.google.android.gms.internal.measurement.zzfg r0 = r0.zzq(r3)
            r9.putObject(r14, r1, r0)
            r5 = r0
            goto L_0x024c
        L_0x024b:
            r5 = r0
        L_0x024c:
            com.google.android.gms.internal.measurement.zzgy r0 = r15.zzax(r4)
            r1 = r17
            r2 = r29
            r3 = r8
            r19 = r4
            r4 = r31
            r6 = r32
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r1, r2, r3, r4, r5, r6)
            r1 = r7
            r2 = r19
            r10 = -1
            goto L_0x0017
        L_0x0265:
            r19 = r4
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            goto L_0x02fc
        L_0x0270:
            r19 = r4
            r0 = 49
            if (r3 > r0) goto L_0x02bb
            long r4 = (long) r5
            r0 = r27
            r20 = r1
            r1 = r28
            r2 = r29
            r10 = r3
            r3 = r8
            r22 = r4
            r4 = r31
            r5 = r17
            r30 = r6
            r6 = r7
            r24 = r7
            r7 = r30
            r15 = r8
            r8 = r19
            r18 = r9
            r25 = r10
            r26 = -1
            r9 = r22
            r11 = r25
            r12 = r20
            r14 = r32
            int r0 = r0.zza((T) r1, r2, r3, r4, r5, r6, r7, r8, r9, r11, r12, r14)
            if (r0 != r15) goto L_0x02a8
            r2 = r0
            goto L_0x031b
        L_0x02a8:
            r14 = r28
            r12 = r29
            r11 = r32
            r9 = r18
            r2 = r19
            r1 = r24
            r10 = -1
            r13 = r31
            r15 = r27
            goto L_0x0017
        L_0x02bb:
            r20 = r1
            r25 = r3
            r30 = r6
            r24 = r7
            r15 = r8
            r18 = r9
            r26 = -1
            r0 = 50
            r9 = r25
            if (r9 != r0) goto L_0x02fe
            r7 = r30
            if (r7 != r10) goto L_0x02fc
            r0 = r27
            r1 = r28
            r2 = r29
            r3 = r15
            r4 = r31
            r5 = r19
            r6 = r20
            r8 = r32
            int r0 = r0.zza((T) r1, r2, r3, r4, r5, r6, r8)
            if (r0 != r15) goto L_0x02e9
            r2 = r0
            goto L_0x031b
        L_0x02e9:
            r14 = r28
            r12 = r29
            r11 = r32
            r9 = r18
            r2 = r19
            r1 = r24
            r10 = -1
            r13 = r31
            r15 = r27
            goto L_0x0017
        L_0x02fc:
            r2 = r15
            goto L_0x031b
        L_0x02fe:
            r7 = r30
            r0 = r27
            r1 = r28
            r2 = r29
            r3 = r15
            r4 = r31
            r8 = r5
            r5 = r17
            r6 = r24
            r10 = r20
            r12 = r19
            r13 = r32
            int r0 = r0.zza((T) r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r12, r13)
            if (r0 != r15) goto L_0x033e
            r2 = r0
        L_0x031b:
            com.google.android.gms.internal.measurement.zzhr r4 = zzt(r28)
            r0 = r17
            r1 = r29
            r3 = r31
            r5 = r32
            int r0 = com.google.android.gms.internal.measurement.zzdl.zza(r0, r1, r2, r3, r4, r5)
            r14 = r28
            r12 = r29
            r11 = r32
            r9 = r18
            r2 = r19
            r1 = r24
            r10 = -1
            r13 = r31
            r15 = r27
            goto L_0x0017
        L_0x033e:
            r14 = r28
            r12 = r29
            r11 = r32
            r9 = r18
            r2 = r19
            r1 = r24
            r10 = -1
            r13 = r31
            r15 = r27
            goto L_0x0017
        L_0x0351:
            r4 = r13
            if (r0 != r4) goto L_0x0355
            return
        L_0x0355:
            com.google.android.gms.internal.measurement.zzfh r0 = com.google.android.gms.internal.measurement.zzfh.zznb()
            throw r0
        L_0x035a:
            r4 = r13
            r5 = 0
            r0 = r27
            r1 = r28
            r2 = r29
            r3 = r30
            r4 = r31
            r6 = r32
            r0.zza((T) r1, r2, r3, r4, r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zza(java.lang.Object, byte[], int, int, com.google.android.gms.internal.measurement.zzdm):void");
    }

    public final void zzi(T t) {
        int i;
        int i2 = this.zzajk;
        while (true) {
            i = this.zzajl;
            if (i2 >= i) {
                break;
            }
            long zzba = (long) (zzba(this.zzajj[i2]) & 1048575);
            Object zzp = zzhw.zzp(t, zzba);
            if (zzp != null) {
                zzhw.zza((Object) t, zzba, this.zzajq.zzp(zzp));
            }
            i2++;
        }
        int length = this.zzajj.length;
        while (i < length) {
            this.zzajn.zzb(t, (long) this.zzajj[i]);
            i++;
        }
        this.zzajo.zzi(t);
        if (this.zzajf) {
            this.zzajp.zzi(t);
        }
    }

    private final <UT, UB> UB zza(Object obj, int i, UB ub, zzhq<UT, UB> zzhq) {
        int i2 = this.zzaja[i];
        Object zzp = zzhw.zzp(obj, (long) (zzba(i) & 1048575));
        if (zzp == null) {
            return ub;
        }
        zzfe zzaz = zzaz(i);
        if (zzaz == null) {
            return ub;
        }
        return zza(i, i2, this.zzajq.zzm(zzp), zzaz, ub, zzhq);
    }

    private final <K, V, UT, UB> UB zza(int i, int i2, Map<K, V> map, zzfe zzfe, UB ub, zzhq<UT, UB> zzhq) {
        zzga zzr = this.zzajq.zzr(zzay(i));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (!zzfe.zzf(((Integer) entry.getValue()).intValue())) {
                if (ub == null) {
                    ub = zzhq.zzoq();
                }
                zzdx zzt = zzdp.zzt(zzfz.zza(zzr, entry.getKey(), entry.getValue()));
                try {
                    zzfz.zza(zzt.zzki(), zzr, entry.getKey(), entry.getValue());
                    zzhq.zza(ub, i2, zzt.zzkh());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ub;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0104, code lost:
        continue;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zzu(T r14) {
        /*
            r13 = this;
            r0 = 0
            r1 = -1
            r1 = 0
            r2 = -1
            r3 = 0
        L_0x0005:
            int r4 = r13.zzajk
            r5 = 1
            if (r1 >= r4) goto L_0x0108
            int[] r4 = r13.zzajj
            r4 = r4[r1]
            int[] r6 = r13.zzaja
            r6 = r6[r4]
            int r7 = r13.zzba(r4)
            boolean r8 = r13.zzajh
            r9 = 1048575(0xfffff, float:1.469367E-39)
            if (r8 != 0) goto L_0x0035
            int[] r8 = r13.zzaja
            int r10 = r4 + 2
            r8 = r8[r10]
            r10 = r8 & r9
            int r8 = r8 >>> 20
            int r8 = r5 << r8
            if (r10 == r2) goto L_0x0036
            sun.misc.Unsafe r2 = zzaiz
            long r11 = (long) r10
            int r2 = r2.getInt(r14, r11)
            r3 = r2
            r2 = r10
            goto L_0x0036
        L_0x0035:
            r8 = 0
        L_0x0036:
            r10 = 268435456(0x10000000, float:2.5243549E-29)
            r10 = r10 & r7
            if (r10 == 0) goto L_0x003d
            r10 = 1
            goto L_0x003e
        L_0x003d:
            r10 = 0
        L_0x003e:
            if (r10 == 0) goto L_0x0047
            boolean r10 = r13.zza((T) r14, r4, r3, r8)
            if (r10 != 0) goto L_0x0047
            return r0
        L_0x0047:
            r10 = 267386880(0xff00000, float:2.3665827E-29)
            r10 = r10 & r7
            int r10 = r10 >>> 20
            r11 = 9
            if (r10 == r11) goto L_0x00f3
            r11 = 17
            if (r10 == r11) goto L_0x00f3
            r8 = 27
            if (r10 == r8) goto L_0x00c7
            r8 = 60
            if (r10 == r8) goto L_0x00b6
            r8 = 68
            if (r10 == r8) goto L_0x00b6
            switch(r10) {
                case 49: goto L_0x00c7;
                case 50: goto L_0x0065;
                default: goto L_0x0063;
            }
        L_0x0063:
            goto L_0x0104
        L_0x0065:
            com.google.android.gms.internal.measurement.zzgc r6 = r13.zzajq
            r7 = r7 & r9
            long r7 = (long) r7
            java.lang.Object r7 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r7)
            java.util.Map r6 = r6.zzn(r7)
            boolean r7 = r6.isEmpty()
            if (r7 != 0) goto L_0x00b3
            java.lang.Object r4 = r13.zzay(r4)
            com.google.android.gms.internal.measurement.zzgc r7 = r13.zzajq
            com.google.android.gms.internal.measurement.zzga r4 = r7.zzr(r4)
            com.google.android.gms.internal.measurement.zzif r4 = r4.zzaiu
            com.google.android.gms.internal.measurement.zzik r4 = r4.zzpb()
            com.google.android.gms.internal.measurement.zzik r7 = com.google.android.gms.internal.measurement.zzik.MESSAGE
            if (r4 != r7) goto L_0x00b3
            r4 = 0
            java.util.Collection r6 = r6.values()
            java.util.Iterator r6 = r6.iterator()
        L_0x0094:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L_0x00b3
            java.lang.Object r7 = r6.next()
            if (r4 != 0) goto L_0x00ac
            com.google.android.gms.internal.measurement.zzgu r4 = com.google.android.gms.internal.measurement.zzgu.zznz()
            java.lang.Class r8 = r7.getClass()
            com.google.android.gms.internal.measurement.zzgy r4 = r4.zzf(r8)
        L_0x00ac:
            boolean r7 = r4.zzu(r7)
            if (r7 != 0) goto L_0x0094
            r5 = 0
        L_0x00b3:
            if (r5 != 0) goto L_0x0104
            return r0
        L_0x00b6:
            boolean r5 = r13.zza((T) r14, r6, r4)
            if (r5 == 0) goto L_0x0104
            com.google.android.gms.internal.measurement.zzgy r4 = r13.zzax(r4)
            boolean r4 = zza(r14, r7, r4)
            if (r4 != 0) goto L_0x0104
            return r0
        L_0x00c7:
            r6 = r7 & r9
            long r6 = (long) r6
            java.lang.Object r6 = com.google.android.gms.internal.measurement.zzhw.zzp(r14, r6)
            java.util.List r6 = (java.util.List) r6
            boolean r7 = r6.isEmpty()
            if (r7 != 0) goto L_0x00f0
            com.google.android.gms.internal.measurement.zzgy r4 = r13.zzax(r4)
            r7 = 0
        L_0x00db:
            int r8 = r6.size()
            if (r7 >= r8) goto L_0x00f0
            java.lang.Object r8 = r6.get(r7)
            boolean r8 = r4.zzu(r8)
            if (r8 != 0) goto L_0x00ed
            r5 = 0
            goto L_0x00f0
        L_0x00ed:
            int r7 = r7 + 1
            goto L_0x00db
        L_0x00f0:
            if (r5 != 0) goto L_0x0104
            return r0
        L_0x00f3:
            boolean r5 = r13.zza((T) r14, r4, r3, r8)
            if (r5 == 0) goto L_0x0104
            com.google.android.gms.internal.measurement.zzgy r4 = r13.zzax(r4)
            boolean r4 = zza(r14, r7, r4)
            if (r4 != 0) goto L_0x0104
            return r0
        L_0x0104:
            int r1 = r1 + 1
            goto L_0x0005
        L_0x0108:
            boolean r1 = r13.zzajf
            if (r1 == 0) goto L_0x0119
            com.google.android.gms.internal.measurement.zzen<?> r1 = r13.zzajp
            com.google.android.gms.internal.measurement.zzeq r14 = r1.zzg(r14)
            boolean r14 = r14.isInitialized()
            if (r14 != 0) goto L_0x0119
            return r0
        L_0x0119:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzgl.zzu(java.lang.Object):boolean");
    }

    private static boolean zza(Object obj, int i, zzgy zzgy) {
        return zzgy.zzu(zzhw.zzp(obj, (long) (i & 1048575)));
    }

    private static void zza(int i, Object obj, zzil zzil) throws IOException {
        if (obj instanceof String) {
            zzil.zzb(i, (String) obj);
        } else {
            zzil.zza(i, (zzdp) obj);
        }
    }

    private final void zza(Object obj, int i, zzgx zzgx) throws IOException {
        if (zzbc(i)) {
            zzhw.zza(obj, (long) (i & 1048575), (Object) zzgx.zzkq());
        } else if (this.zzajg) {
            zzhw.zza(obj, (long) (i & 1048575), (Object) zzgx.readString());
        } else {
            zzhw.zza(obj, (long) (i & 1048575), (Object) zzgx.zzkr());
        }
    }

    private final int zzba(int i) {
        return this.zzaja[i + 1];
    }

    private final int zzbb(int i) {
        return this.zzaja[i + 2];
    }

    private static <T> double zzf(T t, long j) {
        return ((Double) zzhw.zzp(t, j)).doubleValue();
    }

    private static <T> float zzg(T t, long j) {
        return ((Float) zzhw.zzp(t, j)).floatValue();
    }

    private static <T> int zzh(T t, long j) {
        return ((Integer) zzhw.zzp(t, j)).intValue();
    }

    private static <T> long zzi(T t, long j) {
        return ((Long) zzhw.zzp(t, j)).longValue();
    }

    private static <T> boolean zzj(T t, long j) {
        return ((Boolean) zzhw.zzp(t, j)).booleanValue();
    }

    private final boolean zzc(T t, T t2, int i) {
        return zza(t, i) == zza(t2, i);
    }

    private final boolean zza(T t, int i, int i2, int i3) {
        if (this.zzajh) {
            return zza(t, i);
        }
        return (i2 & i3) != 0;
    }

    private final boolean zza(T t, int i) {
        if (this.zzajh) {
            int zzba = zzba(i);
            long j = (long) (zzba & 1048575);
            switch ((zzba & 267386880) >>> 20) {
                case 0:
                    return zzhw.zzo(t, j) != 0.0d;
                case 1:
                    return zzhw.zzn(t, j) != 0.0f;
                case 2:
                    return zzhw.zzl(t, j) != 0;
                case 3:
                    return zzhw.zzl(t, j) != 0;
                case 4:
                    return zzhw.zzk(t, j) != 0;
                case 5:
                    return zzhw.zzl(t, j) != 0;
                case 6:
                    return zzhw.zzk(t, j) != 0;
                case 7:
                    return zzhw.zzm(t, j);
                case 8:
                    Object zzp = zzhw.zzp(t, j);
                    if (zzp instanceof String) {
                        return !((String) zzp).isEmpty();
                    }
                    if (zzp instanceof zzdp) {
                        return !zzdp.zzaby.equals(zzp);
                    }
                    throw new IllegalArgumentException();
                case 9:
                    return zzhw.zzp(t, j) != null;
                case 10:
                    return !zzdp.zzaby.equals(zzhw.zzp(t, j));
                case 11:
                    return zzhw.zzk(t, j) != 0;
                case 12:
                    return zzhw.zzk(t, j) != 0;
                case 13:
                    return zzhw.zzk(t, j) != 0;
                case 14:
                    return zzhw.zzl(t, j) != 0;
                case 15:
                    return zzhw.zzk(t, j) != 0;
                case 16:
                    return zzhw.zzl(t, j) != 0;
                case 17:
                    return zzhw.zzp(t, j) != null;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            int zzbb = zzbb(i);
            return (zzhw.zzk(t, (long) (zzbb & 1048575)) & (1 << (zzbb >>> 20))) != 0;
        }
    }

    private final void zzb(T t, int i) {
        if (!this.zzajh) {
            int zzbb = zzbb(i);
            long j = (long) (zzbb & 1048575);
            zzhw.zzb((Object) t, j, zzhw.zzk(t, j) | (1 << (zzbb >>> 20)));
        }
    }

    private final boolean zza(T t, int i, int i2) {
        return zzhw.zzk(t, (long) (zzbb(i2) & 1048575)) == i;
    }

    private final void zzb(T t, int i, int i2) {
        zzhw.zzb((Object) t, (long) (zzbb(i2) & 1048575), i);
    }

    private final int zzbd(int i) {
        if (i < this.zzajc || i > this.zzajd) {
            return -1;
        }
        return zzq(i, 0);
    }

    private final int zzp(int i, int i2) {
        if (i < this.zzajc || i > this.zzajd) {
            return -1;
        }
        return zzq(i, i2);
    }

    private final int zzq(int i, int i2) {
        int length = (this.zzaja.length / 3) - 1;
        while (i2 <= length) {
            int i3 = (length + i2) >>> 1;
            int i4 = i3 * 3;
            int i5 = this.zzaja[i4];
            if (i == i5) {
                return i4;
            }
            if (i < i5) {
                length = i3 - 1;
            } else {
                i2 = i3 + 1;
            }
        }
        return -1;
    }
}
