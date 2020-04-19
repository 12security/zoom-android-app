package com.google.android.gms.measurement.internal;

import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.measurement.zzbl.zzb.C4565zzb;
import com.google.android.gms.internal.measurement.zzbt.zzb;
import com.google.android.gms.internal.measurement.zzbt.zzd;
import com.google.android.gms.internal.measurement.zzbt.zzh;
import com.google.android.gms.internal.measurement.zzby;
import com.google.android.gms.internal.measurement.zzbz;
import com.google.android.gms.internal.measurement.zzca;
import com.google.android.gms.internal.measurement.zzcb;
import com.google.android.gms.internal.measurement.zzcc;
import com.google.android.gms.internal.measurement.zzez;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

final class zzo extends zzfs {
    zzo(zzft zzft) {
        super(zzft);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x02f7  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x0312  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x0333  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x036e  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x03cd  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x0425  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x0479  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x048d  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x049c  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0226  */
    @androidx.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.internal.measurement.zzbt.zza[] zza(java.lang.String r67, com.google.android.gms.internal.measurement.zzcf[] r68, com.google.android.gms.internal.measurement.zzbt.zzh[] r69) {
        /*
            r66 = this;
            r7 = r66
            r15 = r67
            r13 = r68
            r14 = r69
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r67)
            java.util.HashSet r11 = new java.util.HashSet
            r11.<init>()
            androidx.collection.ArrayMap r12 = new androidx.collection.ArrayMap
            r12.<init>()
            androidx.collection.ArrayMap r10 = new androidx.collection.ArrayMap
            r10.<init>()
            androidx.collection.ArrayMap r9 = new androidx.collection.ArrayMap
            r9.<init>()
            androidx.collection.ArrayMap r8 = new androidx.collection.ArrayMap
            r8.<init>()
            androidx.collection.ArrayMap r6 = new androidx.collection.ArrayMap
            r6.<init>()
            com.google.android.gms.measurement.internal.zzt r0 = r66.zzaf()
            boolean r23 = r0.zzt(r15)
            com.google.android.gms.measurement.internal.zzt r0 = r66.zzaf()
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r1 = com.google.android.gms.measurement.internal.zzal.zzit
            boolean r24 = r0.zzd(r15, r1)
            com.google.android.gms.measurement.internal.zzw r0 = r66.zzdo()
            java.util.Map r0 = r0.zzah(r15)
            if (r0 == 0) goto L_0x01cf
            java.util.Set r1 = r0.keySet()
            java.util.Iterator r1 = r1.iterator()
        L_0x004d:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x01c6
            java.lang.Object r2 = r1.next()
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            java.lang.Integer r4 = java.lang.Integer.valueOf(r2)
            java.lang.Object r4 = r0.get(r4)
            com.google.android.gms.internal.measurement.zzbt$zzf r4 = (com.google.android.gms.internal.measurement.zzbt.zzf) r4
            java.lang.Integer r3 = java.lang.Integer.valueOf(r2)
            java.lang.Object r3 = r10.get(r3)
            java.util.BitSet r3 = (java.util.BitSet) r3
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)
            java.lang.Object r5 = r9.get(r5)
            java.util.BitSet r5 = (java.util.BitSet) r5
            if (r23 == 0) goto L_0x00e7
            r19 = r0
            androidx.collection.ArrayMap r0 = new androidx.collection.ArrayMap
            r0.<init>()
            if (r4 == 0) goto L_0x00db
            int r20 = r4.zzif()
            if (r20 != 0) goto L_0x008d
            goto L_0x00db
        L_0x008d:
            java.util.List r20 = r4.zzie()
            java.util.Iterator r20 = r20.iterator()
        L_0x0095:
            boolean r21 = r20.hasNext()
            if (r21 == 0) goto L_0x00d6
            java.lang.Object r21 = r20.next()
            com.google.android.gms.internal.measurement.zzbt$zzb r21 = (com.google.android.gms.internal.measurement.zzbt.zzb) r21
            boolean r22 = r21.zzhd()
            if (r22 == 0) goto L_0x00cd
            int r22 = r21.getIndex()
            r25 = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r22)
            boolean r22 = r21.zzhe()
            if (r22 == 0) goto L_0x00c6
            long r21 = r21.zzhf()
            java.lang.Long r21 = java.lang.Long.valueOf(r21)
            r64 = r21
            r21 = r5
            r5 = r64
            goto L_0x00c9
        L_0x00c6:
            r21 = r5
            r5 = 0
        L_0x00c9:
            r0.put(r1, r5)
            goto L_0x00d1
        L_0x00cd:
            r25 = r1
            r21 = r5
        L_0x00d1:
            r5 = r21
            r1 = r25
            goto L_0x0095
        L_0x00d6:
            r25 = r1
            r21 = r5
            goto L_0x00df
        L_0x00db:
            r25 = r1
            r21 = r5
        L_0x00df:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
            r8.put(r1, r0)
            goto L_0x00ee
        L_0x00e7:
            r19 = r0
            r25 = r1
            r21 = r5
            r0 = 0
        L_0x00ee:
            if (r3 != 0) goto L_0x0109
            java.util.BitSet r3 = new java.util.BitSet
            r3.<init>()
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
            r10.put(r1, r3)
            java.util.BitSet r5 = new java.util.BitSet
            r5.<init>()
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
            r9.put(r1, r5)
            goto L_0x010b
        L_0x0109:
            r5 = r21
        L_0x010b:
            r1 = 0
        L_0x010c:
            int r20 = r4.zzib()
            r21 = r8
            int r8 = r20 << 6
            if (r1 >= r8) goto L_0x016a
            java.util.List r8 = r4.zzia()
            boolean r8 = com.google.android.gms.measurement.internal.zzfz.zza(r8, r1)
            if (r8 == 0) goto L_0x014d
            com.google.android.gms.measurement.internal.zzau r8 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzdi()
            r20 = r9
            java.lang.String r9 = "Filter already evaluated. audience ID, filter ID"
            r22 = r10
            java.lang.Integer r10 = java.lang.Integer.valueOf(r2)
            r26 = r11
            java.lang.Integer r11 = java.lang.Integer.valueOf(r1)
            r8.zza(r9, r10, r11)
            r5.set(r1)
            java.util.List r8 = r4.zzic()
            boolean r8 = com.google.android.gms.measurement.internal.zzfz.zza(r8, r1)
            if (r8 == 0) goto L_0x0153
            r3.set(r1)
            r8 = 1
            goto L_0x0154
        L_0x014d:
            r20 = r9
            r22 = r10
            r26 = r11
        L_0x0153:
            r8 = 0
        L_0x0154:
            if (r0 == 0) goto L_0x015f
            if (r8 != 0) goto L_0x015f
            java.lang.Integer r8 = java.lang.Integer.valueOf(r1)
            r0.remove(r8)
        L_0x015f:
            int r1 = r1 + 1
            r9 = r20
            r8 = r21
            r10 = r22
            r11 = r26
            goto L_0x010c
        L_0x016a:
            r20 = r9
            r22 = r10
            r26 = r11
            com.google.android.gms.internal.measurement.zzbt$zza$zza r1 = com.google.android.gms.internal.measurement.zzbt.zza.zzhb()
            r8 = 0
            com.google.android.gms.internal.measurement.zzbt$zza$zza r1 = r1.zzl(r8)
            com.google.android.gms.internal.measurement.zzbt$zza$zza r1 = r1.zzb(r4)
            com.google.android.gms.internal.measurement.zzbt$zzf$zza r4 = com.google.android.gms.internal.measurement.zzbt.zzf.zzii()
            java.util.List r3 = com.google.android.gms.measurement.internal.zzfz.zza(r3)
            com.google.android.gms.internal.measurement.zzbt$zzf$zza r3 = r4.zzf(r3)
            java.util.List r4 = com.google.android.gms.measurement.internal.zzfz.zza(r5)
            com.google.android.gms.internal.measurement.zzbt$zzf$zza r3 = r3.zze(r4)
            if (r23 == 0) goto L_0x01a6
            java.util.List r0 = zza(r0)
            r3.zzg(r0)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)
            androidx.collection.ArrayMap r4 = new androidx.collection.ArrayMap
            r4.<init>()
            r6.put(r0, r4)
        L_0x01a6:
            r1.zzb(r3)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)
            com.google.android.gms.internal.measurement.zzgh r1 = r1.zzmr()
            com.google.android.gms.internal.measurement.zzez r1 = (com.google.android.gms.internal.measurement.zzez) r1
            com.google.android.gms.internal.measurement.zzbt$zza r1 = (com.google.android.gms.internal.measurement.zzbt.zza) r1
            r12.put(r0, r1)
            r0 = r19
            r9 = r20
            r8 = r21
            r10 = r22
            r1 = r25
            r11 = r26
            goto L_0x004d
        L_0x01c6:
            r21 = r8
            r20 = r9
            r22 = r10
            r26 = r11
            goto L_0x01d7
        L_0x01cf:
            r21 = r8
            r20 = r9
            r22 = r10
            r26 = r11
        L_0x01d7:
            if (r13 == 0) goto L_0x087a
            androidx.collection.ArrayMap r9 = new androidx.collection.ArrayMap
            r9.<init>()
            int r8 = r13.length
            r27 = 0
            r2 = r27
            r0 = 0
            r1 = 0
            r4 = 0
        L_0x01e6:
            if (r4 >= r8) goto L_0x086b
            r5 = r13[r4]
            java.lang.String r10 = r5.name
            com.google.android.gms.internal.measurement.zzbt$zzd[] r11 = r5.zzxi
            java.util.List r11 = java.util.Arrays.asList(r11)
            r29 = r2
            com.google.android.gms.measurement.internal.zzt r2 = r66.zzaf()
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r3 = com.google.android.gms.measurement.internal.zzal.zzhr
            boolean r2 = r2.zzd(r15, r3)
            r31 = 1
            if (r2 == 0) goto L_0x03b3
            r66.zzdm()
            java.lang.String r2 = "_eid"
            java.lang.Object r2 = com.google.android.gms.measurement.internal.zzfz.zzb(r5, r2)
            r3 = r2
            java.lang.Long r3 = (java.lang.Long) r3
            if (r3 == 0) goto L_0x0212
            r2 = 1
            goto L_0x0213
        L_0x0212:
            r2 = 0
        L_0x0213:
            if (r2 == 0) goto L_0x0221
            r33 = r4
            java.lang.String r4 = "_ep"
            boolean r4 = r10.equals(r4)
            if (r4 == 0) goto L_0x0223
            r4 = 1
            goto L_0x0224
        L_0x0221:
            r33 = r4
        L_0x0223:
            r4 = 0
        L_0x0224:
            if (r4 == 0) goto L_0x036e
            r66.zzdm()
            java.lang.String r2 = "_en"
            java.lang.Object r2 = com.google.android.gms.measurement.internal.zzfz.zzb(r5, r2)
            r10 = r2
            java.lang.String r10 = (java.lang.String) r10
            boolean r2 = android.text.TextUtils.isEmpty(r10)
            if (r2 == 0) goto L_0x0249
            com.google.android.gms.measurement.internal.zzau r2 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()
            java.lang.String r4 = "Extra parameter without an event name. eventId"
            r2.zza(r4, r3)
            r38 = r6
            goto L_0x035c
        L_0x0249:
            if (r0 == 0) goto L_0x025e
            if (r1 == 0) goto L_0x025e
            long r34 = r3.longValue()
            long r36 = r1.longValue()
            int r2 = (r34 > r36 ? 1 : (r34 == r36 ? 0 : -1))
            if (r2 == 0) goto L_0x025a
            goto L_0x025e
        L_0x025a:
            r4 = r0
            r34 = r1
            goto L_0x028a
        L_0x025e:
            com.google.android.gms.measurement.internal.zzw r2 = r66.zzdo()
            android.util.Pair r2 = r2.zza(r15, r3)
            if (r2 == 0) goto L_0x034d
            java.lang.Object r4 = r2.first
            if (r4 != 0) goto L_0x026e
            goto L_0x034d
        L_0x026e:
            java.lang.Object r0 = r2.first
            com.google.android.gms.internal.measurement.zzcf r0 = (com.google.android.gms.internal.measurement.zzcf) r0
            java.lang.Object r1 = r2.second
            java.lang.Long r1 = (java.lang.Long) r1
            long r1 = r1.longValue()
            r66.zzdm()
            java.lang.String r4 = "_eid"
            java.lang.Object r4 = com.google.android.gms.measurement.internal.zzfz.zzb(r0, r4)
            java.lang.Long r4 = (java.lang.Long) r4
            r29 = r1
            r34 = r4
            r4 = r0
        L_0x028a:
            long r29 = r29 - r31
            int r0 = (r29 > r27 ? 1 : (r29 == r27 ? 0 : -1))
            if (r0 > 0) goto L_0x02d5
            com.google.android.gms.measurement.internal.zzw r1 = r66.zzdo()
            r1.zzq()
            com.google.android.gms.measurement.internal.zzau r0 = r1.zzad()
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdi()
            java.lang.String r2 = "Clearing complex main event info. appId"
            r0.zza(r2, r15)
            android.database.sqlite.SQLiteDatabase r0 = r1.getWritableDatabase()     // Catch:{ SQLiteException -> 0x02be }
            java.lang.String r2 = "delete from main_event_params where app_id=?"
            r17 = r4
            r3 = 1
            java.lang.String[] r4 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x02bc }
            r18 = 0
            r4[r18] = r15     // Catch:{ SQLiteException -> 0x02ba }
            r0.execSQL(r2, r4)     // Catch:{ SQLiteException -> 0x02ba }
            r13 = r5
            r38 = r6
            goto L_0x02ea
        L_0x02ba:
            r0 = move-exception
            goto L_0x02c4
        L_0x02bc:
            r0 = move-exception
            goto L_0x02c2
        L_0x02be:
            r0 = move-exception
            r17 = r4
            r3 = 1
        L_0x02c2:
            r18 = 0
        L_0x02c4:
            com.google.android.gms.measurement.internal.zzau r1 = r1.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()
            java.lang.String r2 = "Error clearing complex main event"
            r1.zza(r2, r0)
            r13 = r5
            r38 = r6
            goto L_0x02ea
        L_0x02d5:
            r17 = r4
            r4 = 1
            r18 = 0
            com.google.android.gms.measurement.internal.zzw r1 = r66.zzdo()
            r2 = r67
            r13 = r5
            r4 = r29
            r38 = r6
            r6 = r17
            r1.zza(r2, r3, r4, r6)
        L_0x02ea:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = r17
            com.google.android.gms.internal.measurement.zzbt$zzd[] r2 = r1.zzxi
            int r3 = r2.length
            r4 = 0
        L_0x02f5:
            if (r4 >= r3) goto L_0x030c
            r5 = r2[r4]
            r66.zzdm()
            java.lang.String r6 = r5.getName()
            com.google.android.gms.internal.measurement.zzbt$zzd r6 = com.google.android.gms.measurement.internal.zzfz.zza(r13, r6)
            if (r6 != 0) goto L_0x0309
            r0.add(r5)
        L_0x0309:
            int r4 = r4 + 1
            goto L_0x02f5
        L_0x030c:
            boolean r2 = r0.isEmpty()
            if (r2 != 0) goto L_0x0333
            java.util.Iterator r2 = r11.iterator()
        L_0x0316:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0326
            java.lang.Object r3 = r2.next()
            com.google.android.gms.internal.measurement.zzbt$zzd r3 = (com.google.android.gms.internal.measurement.zzbt.zzd) r3
            r0.add(r3)
            goto L_0x0316
        L_0x0326:
            r36 = r0
            r0 = r10
            r64 = r29
            r29 = r1
            r30 = r34
            r34 = r64
            goto L_0x03c1
        L_0x0333:
            com.google.android.gms.measurement.internal.zzau r0 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdd()
            java.lang.String r2 = "No unique parameters in main event. eventName"
            r0.zza(r2, r10)
            r0 = r10
            r36 = r11
            r64 = r29
            r29 = r1
            r30 = r34
            r34 = r64
            goto L_0x03c1
        L_0x034d:
            r38 = r6
            com.google.android.gms.measurement.internal.zzau r2 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()
            java.lang.String r4 = "Extra parameter without existing main event. eventName, eventId"
            r2.zza(r4, r10, r3)
        L_0x035c:
            r41 = r8
            r43 = r9
            r46 = r12
            r63 = r20
            r47 = r21
            r44 = r22
            r14 = r26
            r2 = r29
            goto L_0x084f
        L_0x036e:
            r13 = r5
            r38 = r6
            if (r2 == 0) goto L_0x03b8
            r66.zzdm()
            java.lang.String r0 = "_epc"
            java.lang.Long r1 = java.lang.Long.valueOf(r27)
            java.lang.Object r0 = com.google.android.gms.measurement.internal.zzfz.zzb(r13, r0)
            if (r0 != 0) goto L_0x0383
            r0 = r1
        L_0x0383:
            java.lang.Long r0 = (java.lang.Long) r0
            long r16 = r0.longValue()
            int r0 = (r16 > r27 ? 1 : (r16 == r27 ? 0 : -1))
            if (r0 > 0) goto L_0x039c
            com.google.android.gms.measurement.internal.zzau r0 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdd()
            java.lang.String r1 = "Complex event with zero extra param count. eventName"
            r0.zza(r1, r10)
            r0 = r3
            goto L_0x03a9
        L_0x039c:
            com.google.android.gms.measurement.internal.zzw r1 = r66.zzdo()
            r2 = r67
            r0 = r3
            r4 = r16
            r6 = r13
            r1.zza(r2, r3, r4, r6)
        L_0x03a9:
            r30 = r0
            r0 = r10
            r36 = r11
            r29 = r13
            r34 = r16
            goto L_0x03c1
        L_0x03b3:
            r33 = r4
            r13 = r5
            r38 = r6
        L_0x03b8:
            r36 = r11
            r34 = r29
            r29 = r0
            r30 = r1
            r0 = r10
        L_0x03c1:
            com.google.android.gms.measurement.internal.zzw r1 = r66.zzdo()
            java.lang.String r2 = r13.name
            com.google.android.gms.measurement.internal.zzaf r1 = r1.zzc(r15, r2)
            if (r1 != 0) goto L_0x0425
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdd()
            java.lang.String r2 = "Event aggregate wasn't created during raw event logging. appId, event"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r67)
            com.google.android.gms.measurement.internal.zzas r4 = r66.zzaa()
            java.lang.String r4 = r4.zzal(r0)
            r1.zza(r2, r3, r4)
            com.google.android.gms.measurement.internal.zzaf r1 = new com.google.android.gms.measurement.internal.zzaf
            java.lang.String r10 = r13.name
            r2 = 1
            r4 = 1
            java.lang.Long r6 = r13.zzxj
            long r16 = r6.longValue()
            r31 = 0
            r6 = 0
            r37 = 0
            r39 = 0
            r40 = 0
            r41 = r8
            r11 = r21
            r8 = r1
            r43 = r9
            r42 = r20
            r9 = r67
            r44 = r22
            r47 = r11
            r46 = r12
            r45 = r26
            r11 = r2
            r2 = r13
            r3 = r14
            r13 = r4
            r5 = r15
            r15 = r16
            r17 = r31
            r19 = r6
            r20 = r37
            r21 = r39
            r22 = r40
            r8.<init>(r9, r10, r11, r13, r15, r17, r19, r20, r21, r22)
            goto L_0x0466
        L_0x0425:
            r41 = r8
            r43 = r9
            r46 = r12
            r2 = r13
            r3 = r14
            r5 = r15
            r42 = r20
            r47 = r21
            r44 = r22
            r45 = r26
            com.google.android.gms.measurement.internal.zzaf r4 = new com.google.android.gms.measurement.internal.zzaf
            java.lang.String r6 = r1.zzcf
            java.lang.String r8 = r1.name
            long r9 = r1.zzfe
            long r51 = r9 + r31
            long r9 = r1.zzff
            long r53 = r9 + r31
            long r9 = r1.zzfg
            long r11 = r1.zzfh
            java.lang.Long r13 = r1.zzfi
            java.lang.Long r14 = r1.zzfj
            java.lang.Long r15 = r1.zzfk
            java.lang.Boolean r1 = r1.zzfl
            r48 = r4
            r49 = r6
            r50 = r8
            r55 = r9
            r57 = r11
            r59 = r13
            r60 = r14
            r61 = r15
            r62 = r1
            r48.<init>(r49, r50, r51, r53, r55, r57, r59, r60, r61, r62)
            r1 = r4
        L_0x0466:
            com.google.android.gms.measurement.internal.zzw r4 = r66.zzdo()
            r4.zza(r1)
            long r8 = r1.zzfe
            r10 = r43
            java.lang.Object r1 = r10.get(r0)
            java.util.Map r1 = (java.util.Map) r1
            if (r1 != 0) goto L_0x048d
            com.google.android.gms.measurement.internal.zzw r1 = r66.zzdo()
            java.util.Map r1 = r1.zzh(r5, r0)
            if (r1 != 0) goto L_0x0488
            androidx.collection.ArrayMap r1 = new androidx.collection.ArrayMap
            r1.<init>()
        L_0x0488:
            r10.put(r0, r1)
            r11 = r1
            goto L_0x048e
        L_0x048d:
            r11 = r1
        L_0x048e:
            java.util.Set r1 = r11.keySet()
            java.util.Iterator r12 = r1.iterator()
        L_0x0496:
            boolean r1 = r12.hasNext()
            if (r1 == 0) goto L_0x0843
            java.lang.Object r1 = r12.next()
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r13 = r1.intValue()
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r14 = r45
            boolean r1 = r14.contains(r1)
            if (r1 == 0) goto L_0x04c6
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r4 = "Skipping failed audience ID"
            java.lang.Integer r6 = java.lang.Integer.valueOf(r13)
            r1.zza(r4, r6)
            r45 = r14
            goto L_0x0496
        L_0x04c6:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r15 = r44
            java.lang.Object r1 = r15.get(r1)
            java.util.BitSet r1 = (java.util.BitSet) r1
            java.lang.Integer r4 = java.lang.Integer.valueOf(r13)
            r6 = r42
            java.lang.Object r4 = r6.get(r4)
            java.util.BitSet r4 = (java.util.BitSet) r4
            if (r23 == 0) goto L_0x0503
            r16 = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r43 = r10
            r10 = r47
            java.lang.Object r1 = r10.get(r1)
            java.util.Map r1 = (java.util.Map) r1
            r17 = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r18 = r12
            r12 = r38
            java.lang.Object r1 = r12.get(r1)
            java.util.Map r1 = (java.util.Map) r1
            r19 = r1
            goto L_0x0511
        L_0x0503:
            r16 = r1
            r43 = r10
            r18 = r12
            r12 = r38
            r10 = r47
            r17 = 0
            r19 = 0
        L_0x0511:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r7 = r46
            java.lang.Object r1 = r7.get(r1)
            com.google.android.gms.internal.measurement.zzbt$zza r1 = (com.google.android.gms.internal.measurement.zzbt.zza) r1
            if (r1 != 0) goto L_0x0587
            com.google.android.gms.internal.measurement.zzbt$zza$zza r1 = com.google.android.gms.internal.measurement.zzbt.zza.zzhb()
            r4 = 1
            r1.zzl(r4)
            java.lang.Integer r4 = java.lang.Integer.valueOf(r13)
            com.google.android.gms.internal.measurement.zzgh r1 = r1.zzmr()
            com.google.android.gms.internal.measurement.zzez r1 = (com.google.android.gms.internal.measurement.zzez) r1
            com.google.android.gms.internal.measurement.zzbt$zza r1 = (com.google.android.gms.internal.measurement.zzbt.zza) r1
            r7.put(r4, r1)
            java.util.BitSet r1 = new java.util.BitSet
            r1.<init>()
            java.lang.Integer r4 = java.lang.Integer.valueOf(r13)
            r15.put(r4, r1)
            java.util.BitSet r4 = new java.util.BitSet
            r4.<init>()
            r16 = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r6.put(r1, r4)
            if (r23 == 0) goto L_0x0579
            androidx.collection.ArrayMap r1 = new androidx.collection.ArrayMap
            r1.<init>()
            r20 = r2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r13)
            r10.put(r2, r1)
            androidx.collection.ArrayMap r2 = new androidx.collection.ArrayMap
            r2.<init>()
            r17 = r1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r12.put(r1, r2)
            r1 = r4
            r46 = r7
            r38 = r12
            r4 = r16
            r7 = r17
            r12 = r2
            goto L_0x0594
        L_0x0579:
            r20 = r2
            r1 = r4
            r46 = r7
            r38 = r12
            r4 = r16
            r7 = r17
            r12 = r19
            goto L_0x0594
        L_0x0587:
            r20 = r2
            r1 = r4
            r46 = r7
            r38 = r12
            r4 = r16
            r7 = r17
            r12 = r19
        L_0x0594:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r13)
            java.lang.Object r2 = r11.get(r2)
            java.util.List r2 = (java.util.List) r2
            java.util.Iterator r16 = r2.iterator()
        L_0x05a2:
            boolean r2 = r16.hasNext()
            if (r2 == 0) goto L_0x082c
            java.lang.Object r2 = r16.next()
            com.google.android.gms.internal.measurement.zzby r2 = (com.google.android.gms.internal.measurement.zzby) r2
            r19 = r1
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            r21 = r11
            r11 = 2
            boolean r1 = r1.isLoggable(r11)
            if (r1 == 0) goto L_0x05f4
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r11 = "Evaluating filter. audience, filter, event"
            java.lang.Integer r3 = java.lang.Integer.valueOf(r13)
            java.lang.Integer r5 = r2.zzwa
            r42 = r6
            com.google.android.gms.measurement.internal.zzas r6 = r66.zzaa()
            r47 = r10
            java.lang.String r10 = r2.zzwb
            java.lang.String r6 = r6.zzal(r10)
            r1.zza(r11, r3, r5, r6)
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r3 = "Filter definition"
            com.google.android.gms.measurement.internal.zzfz r5 = r66.zzdm()
            java.lang.String r5 = r5.zza(r2)
            r1.zza(r3, r5)
            goto L_0x05f8
        L_0x05f4:
            r42 = r6
            r47 = r10
        L_0x05f8:
            java.lang.Integer r1 = r2.zzwa
            if (r1 == 0) goto L_0x07f3
            java.lang.Integer r1 = r2.zzwa
            int r1 = r1.intValue()
            r10 = 256(0x100, float:3.59E-43)
            if (r1 <= r10) goto L_0x0608
            goto L_0x07f3
        L_0x0608:
            if (r23 == 0) goto L_0x0748
            if (r2 == 0) goto L_0x061a
            java.lang.Boolean r1 = r2.zzvx
            if (r1 == 0) goto L_0x061a
            java.lang.Boolean r1 = r2.zzvx
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x061a
            r11 = 1
            goto L_0x061b
        L_0x061a:
            r11 = 0
        L_0x061b:
            if (r2 == 0) goto L_0x062c
            java.lang.Boolean r1 = r2.zzvy
            if (r1 == 0) goto L_0x062c
            java.lang.Boolean r1 = r2.zzvy
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x062c
            r22 = 1
            goto L_0x062e
        L_0x062c:
            r22 = 0
        L_0x062e:
            java.lang.Integer r1 = r2.zzwa
            int r1 = r1.intValue()
            boolean r1 = r4.get(r1)
            if (r1 == 0) goto L_0x065f
            if (r11 != 0) goto L_0x065f
            if (r22 != 0) goto L_0x065f
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r3 = "Event filter already evaluated true and it is not associated with a dynamic audience. audience ID, filter ID"
            java.lang.Integer r5 = java.lang.Integer.valueOf(r13)
            java.lang.Integer r2 = r2.zzwa
            r1.zza(r3, r5, r2)
            r1 = r19
            r11 = r21
            r6 = r42
            r10 = r47
            r3 = r69
            r5 = r67
            goto L_0x05a2
        L_0x065f:
            r5 = r19
            r1 = r66
            r3 = r2
            r6 = r20
            r10 = 1
            r44 = r15
            r10 = r69
            r15 = r3
            r3 = r0
            r10 = r4
            r4 = r36
            r20 = r0
            r0 = r5
            r19 = r7
            r63 = r42
            r7 = r6
            r5 = r8
            java.lang.Boolean r1 = r1.zza(r2, r3, r4, r5)
            com.google.android.gms.measurement.internal.zzau r2 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdi()
            java.lang.String r3 = "Event filter result"
            if (r1 != 0) goto L_0x068c
            java.lang.String r4 = "null"
            goto L_0x068d
        L_0x068c:
            r4 = r1
        L_0x068d:
            r2.zza(r3, r4)
            if (r1 != 0) goto L_0x06af
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r14.add(r1)
            r1 = r0
            r4 = r10
            r0 = r20
            r11 = r21
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r19
            goto L_0x05a2
        L_0x06af:
            java.lang.Integer r2 = r15.zzwa
            int r2 = r2.intValue()
            r0.set(r2)
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x0732
            java.lang.Integer r1 = r15.zzwa
            int r1 = r1.intValue()
            r10.set(r1)
            if (r11 != 0) goto L_0x06cb
            if (r22 == 0) goto L_0x071c
        L_0x06cb:
            java.lang.Long r1 = r7.zzxj
            if (r1 == 0) goto L_0x071c
            if (r22 == 0) goto L_0x06f6
            java.lang.Integer r1 = r15.zzwa
            int r1 = r1.intValue()
            java.lang.Long r2 = r7.zzxj
            long r2 = r2.longValue()
            zzb(r12, r1, r2)
            r1 = r0
            r4 = r10
            r0 = r20
            r11 = r21
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r19
            goto L_0x05a2
        L_0x06f6:
            java.lang.Integer r1 = r15.zzwa
            int r1 = r1.intValue()
            java.lang.Long r2 = r7.zzxj
            long r2 = r2.longValue()
            r11 = r19
            zza(r11, r1, r2)
            r1 = r0
            r4 = r10
            r0 = r20
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r11
            r11 = r21
            goto L_0x05a2
        L_0x071c:
            r1 = r0
            r4 = r10
            r0 = r20
            r11 = r21
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r19
            goto L_0x05a2
        L_0x0732:
            r1 = r0
            r4 = r10
            r0 = r20
            r11 = r21
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r19
            goto L_0x05a2
        L_0x0748:
            r10 = r4
            r11 = r7
            r44 = r15
            r7 = r20
            r63 = r42
            r20 = r0
            r15 = r2
            r0 = r19
            java.lang.Integer r1 = r15.zzwa
            int r1 = r1.intValue()
            boolean r1 = r10.get(r1)
            if (r1 == 0) goto L_0x0789
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r2 = "Event filter already evaluated true. audience ID, filter ID"
            java.lang.Integer r3 = java.lang.Integer.valueOf(r13)
            java.lang.Integer r4 = r15.zzwa
            r1.zza(r2, r3, r4)
            r1 = r0
            r4 = r10
            r0 = r20
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r11
            r11 = r21
            goto L_0x05a2
        L_0x0789:
            r1 = r66
            r2 = r15
            r3 = r20
            r4 = r36
            r5 = r8
            java.lang.Boolean r1 = r1.zza(r2, r3, r4, r5)
            com.google.android.gms.measurement.internal.zzau r2 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdi()
            java.lang.String r3 = "Event filter result"
            if (r1 != 0) goto L_0x07a4
            java.lang.String r4 = "null"
            goto L_0x07a5
        L_0x07a4:
            r4 = r1
        L_0x07a5:
            r2.zza(r3, r4)
            if (r1 != 0) goto L_0x07c6
            java.lang.Integer r1 = java.lang.Integer.valueOf(r13)
            r14.add(r1)
            r1 = r0
            r4 = r10
            r0 = r20
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r11
            r11 = r21
            goto L_0x05a2
        L_0x07c6:
            java.lang.Integer r2 = r15.zzwa
            int r2 = r2.intValue()
            r0.set(r2)
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x07de
            java.lang.Integer r1 = r15.zzwa
            int r1 = r1.intValue()
            r10.set(r1)
        L_0x07de:
            r1 = r0
            r4 = r10
            r0 = r20
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r11
            r11 = r21
            goto L_0x05a2
        L_0x07f3:
            r10 = r4
            r11 = r7
            r44 = r15
            r7 = r20
            r63 = r42
            r20 = r0
            r15 = r2
            r0 = r19
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdd()
            java.lang.String r2 = "Invalid event filter ID. appId, id"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzau.zzao(r67)
            java.lang.Integer r4 = r15.zzwa
            java.lang.String r4 = java.lang.String.valueOf(r4)
            r1.zza(r2, r3, r4)
            r1 = r0
            r4 = r10
            r0 = r20
            r15 = r44
            r10 = r47
            r6 = r63
            r3 = r69
            r5 = r67
            r20 = r7
            r7 = r11
            r11 = r21
            goto L_0x05a2
        L_0x082c:
            r7 = r20
            r42 = r6
            r2 = r7
            r47 = r10
            r45 = r14
            r44 = r15
            r12 = r18
            r10 = r43
            r3 = r69
            r5 = r67
            r7 = r66
            goto L_0x0496
        L_0x0843:
            r43 = r10
            r63 = r42
            r14 = r45
            r0 = r29
            r1 = r30
            r2 = r34
        L_0x084f:
            int r4 = r33 + 1
            r13 = r68
            r26 = r14
            r6 = r38
            r8 = r41
            r9 = r43
            r22 = r44
            r12 = r46
            r21 = r47
            r20 = r63
            r7 = r66
            r14 = r69
            r15 = r67
            goto L_0x01e6
        L_0x086b:
            r38 = r6
            r46 = r12
            r63 = r20
            r47 = r21
            r44 = r22
            r14 = r26
            r1 = r69
            goto L_0x0888
        L_0x087a:
            r38 = r6
            r46 = r12
            r63 = r20
            r47 = r21
            r44 = r22
            r14 = r26
            r1 = r69
        L_0x0888:
            if (r1 == 0) goto L_0x0c39
            androidx.collection.ArrayMap r0 = new androidx.collection.ArrayMap
            r0.<init>()
            int r2 = r1.length
            r3 = 0
        L_0x0891:
            if (r3 >= r2) goto L_0x0c32
            r4 = r1[r3]
            java.lang.String r5 = r4.getName()
            java.lang.Object r5 = r0.get(r5)
            java.util.Map r5 = (java.util.Map) r5
            if (r5 != 0) goto L_0x08be
            com.google.android.gms.measurement.internal.zzw r5 = r66.zzdo()
            java.lang.String r6 = r4.getName()
            r7 = r67
            java.util.Map r5 = r5.zzi(r7, r6)
            if (r5 != 0) goto L_0x08b6
            androidx.collection.ArrayMap r5 = new androidx.collection.ArrayMap
            r5.<init>()
        L_0x08b6:
            java.lang.String r6 = r4.getName()
            r0.put(r6, r5)
            goto L_0x08c0
        L_0x08be:
            r7 = r67
        L_0x08c0:
            java.util.Set r6 = r5.keySet()
            java.util.Iterator r6 = r6.iterator()
        L_0x08c8:
            boolean r8 = r6.hasNext()
            if (r8 == 0) goto L_0x0c22
            java.lang.Object r8 = r6.next()
            java.lang.Integer r8 = (java.lang.Integer) r8
            int r8 = r8.intValue()
            java.lang.Integer r9 = java.lang.Integer.valueOf(r8)
            boolean r9 = r14.contains(r9)
            if (r9 == 0) goto L_0x08f4
            com.google.android.gms.measurement.internal.zzau r9 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r9 = r9.zzdi()
            java.lang.String r10 = "Skipping failed audience ID"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r9.zza(r10, r8)
            goto L_0x08c8
        L_0x08f4:
            java.lang.Integer r9 = java.lang.Integer.valueOf(r8)
            r10 = r44
            java.lang.Object r9 = r10.get(r9)
            java.util.BitSet r9 = (java.util.BitSet) r9
            java.lang.Integer r11 = java.lang.Integer.valueOf(r8)
            r12 = r63
            java.lang.Object r11 = r12.get(r11)
            java.util.BitSet r11 = (java.util.BitSet) r11
            if (r23 == 0) goto L_0x092b
            java.lang.Integer r13 = java.lang.Integer.valueOf(r8)
            r15 = r47
            java.lang.Object r13 = r15.get(r13)
            java.util.Map r13 = (java.util.Map) r13
            r68 = r0
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)
            r1 = r38
            java.lang.Object r0 = r1.get(r0)
            java.util.Map r0 = (java.util.Map) r0
            r16 = r0
            goto L_0x0934
        L_0x092b:
            r68 = r0
            r1 = r38
            r15 = r47
            r13 = 0
            r16 = 0
        L_0x0934:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)
            r18 = r2
            r2 = r46
            java.lang.Object r0 = r2.get(r0)
            com.google.android.gms.internal.measurement.zzbt$zza r0 = (com.google.android.gms.internal.measurement.zzbt.zza) r0
            if (r0 != 0) goto L_0x0995
            com.google.android.gms.internal.measurement.zzbt$zza$zza r0 = com.google.android.gms.internal.measurement.zzbt.zza.zzhb()
            r9 = 1
            r0.zzl(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r8)
            com.google.android.gms.internal.measurement.zzgh r0 = r0.zzmr()
            com.google.android.gms.internal.measurement.zzez r0 = (com.google.android.gms.internal.measurement.zzez) r0
            com.google.android.gms.internal.measurement.zzbt$zza r0 = (com.google.android.gms.internal.measurement.zzbt.zza) r0
            r2.put(r9, r0)
            java.util.BitSet r9 = new java.util.BitSet
            r9.<init>()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)
            r10.put(r0, r9)
            java.util.BitSet r11 = new java.util.BitSet
            r11.<init>()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)
            r12.put(r0, r11)
            if (r23 == 0) goto L_0x0990
            androidx.collection.ArrayMap r13 = new androidx.collection.ArrayMap
            r13.<init>()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)
            r15.put(r0, r13)
            androidx.collection.ArrayMap r0 = new androidx.collection.ArrayMap
            r0.<init>()
            r46 = r2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r8)
            r1.put(r2, r0)
            goto L_0x0999
        L_0x0990:
            r46 = r2
            r0 = r16
            goto L_0x0999
        L_0x0995:
            r46 = r2
            r0 = r16
        L_0x0999:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r8)
            java.lang.Object r2 = r5.get(r2)
            java.util.List r2 = (java.util.List) r2
            java.util.Iterator r2 = r2.iterator()
        L_0x09a7:
            boolean r16 = r2.hasNext()
            if (r16 == 0) goto L_0x0c0e
            java.lang.Object r16 = r2.next()
            r19 = r2
            r2 = r16
            com.google.android.gms.internal.measurement.zzcb r2 = (com.google.android.gms.internal.measurement.zzcb) r2
            r16 = r5
            com.google.android.gms.measurement.internal.zzau r5 = r66.zzad()
            r20 = r6
            r6 = 2
            boolean r5 = r5.isLoggable(r6)
            if (r5 == 0) goto L_0x09ff
            com.google.android.gms.measurement.internal.zzau r5 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdi()
            java.lang.String r6 = "Evaluating filter. audience, filter, property"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            r38 = r1
            java.lang.Integer r1 = r2.zzwa
            r21 = r15
            com.google.android.gms.measurement.internal.zzas r15 = r66.zzaa()
            r42 = r12
            java.lang.String r12 = r2.zzwq
            java.lang.String r12 = r15.zzan(r12)
            r5.zza(r6, r7, r1, r12)
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r5 = "Filter definition"
            com.google.android.gms.measurement.internal.zzfz r6 = r66.zzdm()
            java.lang.String r6 = r6.zzb(r2)
            r1.zza(r5, r6)
            goto L_0x0a05
        L_0x09ff:
            r38 = r1
            r42 = r12
            r21 = r15
        L_0x0a05:
            java.lang.Integer r1 = r2.zzwa
            if (r1 == 0) goto L_0x0bda
            java.lang.Integer r1 = r2.zzwa
            int r1 = r1.intValue()
            r5 = 256(0x100, float:3.59E-43)
            if (r1 <= r5) goto L_0x0a15
            goto L_0x0bda
        L_0x0a15:
            if (r23 == 0) goto L_0x0b49
            boolean r1 = zza(r2)
            if (r2 == 0) goto L_0x0a2b
            java.lang.Boolean r6 = r2.zzvy
            if (r6 == 0) goto L_0x0a2b
            java.lang.Boolean r6 = r2.zzvy
            boolean r6 = r6.booleanValue()
            if (r6 == 0) goto L_0x0a2b
            r6 = 1
            goto L_0x0a2c
        L_0x0a2b:
            r6 = 0
        L_0x0a2c:
            java.lang.Integer r7 = r2.zzwa
            int r7 = r7.intValue()
            boolean r7 = r9.get(r7)
            if (r7 == 0) goto L_0x0a5f
            if (r1 != 0) goto L_0x0a5f
            if (r6 != 0) goto L_0x0a5f
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r6 = "Property filter already evaluated true and it is not associated with a dynamic audience. audience ID, filter ID"
            java.lang.Integer r7 = java.lang.Integer.valueOf(r8)
            java.lang.Integer r2 = r2.zzwa
            r1.zza(r6, r7, r2)
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r7 = r67
            goto L_0x09a7
        L_0x0a5f:
            r12 = r46
            r7 = r66
            java.lang.Boolean r15 = r7.zza(r2, r4)
            com.google.android.gms.measurement.internal.zzau r22 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r5 = r22.zzdi()
            r46 = r12
            java.lang.String r12 = "Property filter result"
            if (r15 != 0) goto L_0x0a7c
            java.lang.String r22 = "null"
            r44 = r10
            r10 = r22
            goto L_0x0a7f
        L_0x0a7c:
            r44 = r10
            r10 = r15
        L_0x0a7f:
            r5.zza(r12, r10)
            if (r15 != 0) goto L_0x0a9d
            java.lang.Integer r1 = java.lang.Integer.valueOf(r8)
            r14.add(r1)
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0a9d:
            java.lang.Integer r5 = r2.zzwa
            int r5 = r5.intValue()
            r11.set(r5)
            if (r24 == 0) goto L_0x0ac8
            java.lang.Integer r5 = r2.zzwa
            int r5 = r5.intValue()
            boolean r5 = r9.get(r5)
            if (r5 == 0) goto L_0x0aba
            boolean r5 = zza(r2)
            if (r5 == 0) goto L_0x0ad5
        L_0x0aba:
            java.lang.Integer r5 = r2.zzwa
            int r5 = r5.intValue()
            boolean r10 = r15.booleanValue()
            r9.set(r5, r10)
            goto L_0x0ad5
        L_0x0ac8:
            java.lang.Integer r5 = r2.zzwa
            int r5 = r5.intValue()
            boolean r10 = r15.booleanValue()
            r9.set(r5, r10)
        L_0x0ad5:
            boolean r5 = r15.booleanValue()
            if (r5 == 0) goto L_0x0b37
            if (r1 != 0) goto L_0x0adf
            if (r6 == 0) goto L_0x0b37
        L_0x0adf:
            boolean r1 = r4.zzis()
            if (r1 == 0) goto L_0x0b25
            if (r6 == 0) goto L_0x0b06
            java.lang.Integer r1 = r2.zzwa
            int r1 = r1.intValue()
            long r5 = r4.zzit()
            zzb(r0, r1, r5)
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0b06:
            java.lang.Integer r1 = r2.zzwa
            int r1 = r1.intValue()
            long r5 = r4.zzit()
            zza(r13, r1, r5)
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0b25:
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0b37:
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0b49:
            r44 = r10
            r7 = r66
            java.lang.Integer r1 = r2.zzwa
            int r1 = r1.intValue()
            boolean r1 = r9.get(r1)
            if (r1 == 0) goto L_0x0b7e
            com.google.android.gms.measurement.internal.zzau r1 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()
            java.lang.String r5 = "Property filter already evaluated true. audience ID, filter ID"
            java.lang.Integer r6 = java.lang.Integer.valueOf(r8)
            java.lang.Integer r2 = r2.zzwa
            r1.zza(r5, r6, r2)
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0b7e:
            java.lang.Boolean r1 = r7.zza(r2, r4)
            com.google.android.gms.measurement.internal.zzau r5 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdi()
            java.lang.String r6 = "Property filter result"
            if (r1 != 0) goto L_0x0b91
            java.lang.String r10 = "null"
            goto L_0x0b92
        L_0x0b91:
            r10 = r1
        L_0x0b92:
            r5.zza(r6, r10)
            if (r1 != 0) goto L_0x0bb0
            java.lang.Integer r1 = java.lang.Integer.valueOf(r8)
            r14.add(r1)
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0bb0:
            java.lang.Integer r5 = r2.zzwa
            int r5 = r5.intValue()
            r11.set(r5)
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x0bc8
            java.lang.Integer r1 = r2.zzwa
            int r1 = r1.intValue()
            r9.set(r1)
        L_0x0bc8:
            r5 = r16
            r2 = r19
            r6 = r20
            r15 = r21
            r1 = r38
            r12 = r42
            r10 = r44
            r7 = r67
            goto L_0x09a7
        L_0x0bda:
            r44 = r10
            r7 = r66
            com.google.android.gms.measurement.internal.zzau r0 = r66.zzad()
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdd()
            java.lang.String r1 = "Invalid property filter ID. appId, id"
            java.lang.Object r5 = com.google.android.gms.measurement.internal.zzau.zzao(r67)
            java.lang.Integer r2 = r2.zzwa
            java.lang.String r2 = java.lang.String.valueOf(r2)
            r0.zza(r1, r5, r2)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)
            r14.add(r0)
            r0 = r68
            r5 = r16
            r2 = r18
            r6 = r20
            r47 = r21
            r63 = r42
            r1 = r69
            r7 = r67
            goto L_0x08c8
        L_0x0c0e:
            r7 = r66
            r0 = r68
            r38 = r1
            r44 = r10
            r63 = r12
            r47 = r15
            r2 = r18
            r1 = r69
            r7 = r67
            goto L_0x08c8
        L_0x0c22:
            r68 = r0
            r18 = r2
            r21 = r47
            r42 = r63
            r7 = r66
            int r3 = r3 + 1
            r1 = r69
            goto L_0x0891
        L_0x0c32:
            r21 = r47
            r42 = r63
            r7 = r66
            goto L_0x0c3f
        L_0x0c39:
            r21 = r47
            r42 = r63
            r7 = r66
        L_0x0c3f:
            int r0 = r44.size()
            com.google.android.gms.internal.measurement.zzbt$zza[] r1 = new com.google.android.gms.internal.measurement.zzbt.zza[r0]
            java.util.Set r0 = r44.keySet()
            java.util.Iterator r2 = r0.iterator()
            r5 = 0
        L_0x0c4e:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x0f16
            java.lang.Object r0 = r2.next()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r0)
            boolean r3 = r14.contains(r3)
            if (r3 != 0) goto L_0x0f10
            java.lang.Integer r3 = java.lang.Integer.valueOf(r0)
            r4 = r46
            java.lang.Object r3 = r4.get(r3)
            com.google.android.gms.internal.measurement.zzbt$zza r3 = (com.google.android.gms.internal.measurement.zzbt.zza) r3
            if (r3 != 0) goto L_0x0c7b
            com.google.android.gms.internal.measurement.zzbt$zza$zza r3 = com.google.android.gms.internal.measurement.zzbt.zza.zzhb()
            goto L_0x0c83
        L_0x0c7b:
            com.google.android.gms.internal.measurement.zzez$zza r3 = r3.zzmh()
            com.google.android.gms.internal.measurement.zzez$zza r3 = (com.google.android.gms.internal.measurement.zzez.zza) r3
            com.google.android.gms.internal.measurement.zzbt$zza$zza r3 = (com.google.android.gms.internal.measurement.zzbt.zza.C4567zza) r3
        L_0x0c83:
            r3.zzi(r0)
            com.google.android.gms.internal.measurement.zzbt$zzf$zza r6 = com.google.android.gms.internal.measurement.zzbt.zzf.zzii()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r9 = r44
            java.lang.Object r8 = r9.get(r8)
            java.util.BitSet r8 = (java.util.BitSet) r8
            java.util.List r8 = com.google.android.gms.measurement.internal.zzfz.zza(r8)
            com.google.android.gms.internal.measurement.zzbt$zzf$zza r6 = r6.zzf(r8)
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r10 = r42
            java.lang.Object r8 = r10.get(r8)
            java.util.BitSet r8 = (java.util.BitSet) r8
            java.util.List r8 = com.google.android.gms.measurement.internal.zzfz.zza(r8)
            com.google.android.gms.internal.measurement.zzbt$zzf$zza r6 = r6.zze(r8)
            if (r23 == 0) goto L_0x0e72
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r11 = r21
            java.lang.Object r8 = r11.get(r8)
            java.util.Map r8 = (java.util.Map) r8
            java.util.List r8 = zza(r8)
            r6.zzg(r8)
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r12 = r38
            java.lang.Object r8 = r12.get(r8)
            java.util.Map r8 = (java.util.Map) r8
            if (r8 != 0) goto L_0x0ce0
            java.util.List r8 = java.util.Collections.emptyList()
            r68 = r2
            r13 = r8
            r44 = r9
            goto L_0x0d53
        L_0x0ce0:
            java.util.ArrayList r13 = new java.util.ArrayList
            int r15 = r8.size()
            r13.<init>(r15)
            java.util.Set r15 = r8.keySet()
            java.util.Iterator r15 = r15.iterator()
        L_0x0cf1:
            boolean r16 = r15.hasNext()
            if (r16 == 0) goto L_0x0d4f
            java.lang.Object r16 = r15.next()
            r68 = r2
            r2 = r16
            java.lang.Integer r2 = (java.lang.Integer) r2
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r7 = com.google.android.gms.internal.measurement.zzbt.zzg.zzip()
            r44 = r9
            int r9 = r2.intValue()
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r7 = r7.zzm(r9)
            java.lang.Object r2 = r8.get(r2)
            java.util.List r2 = (java.util.List) r2
            if (r2 == 0) goto L_0x0d39
            java.util.Collections.sort(r2)
            java.util.Iterator r2 = r2.iterator()
        L_0x0d1e:
            boolean r9 = r2.hasNext()
            if (r9 == 0) goto L_0x0d36
            java.lang.Object r9 = r2.next()
            java.lang.Long r9 = (java.lang.Long) r9
            r69 = r8
            long r8 = r9.longValue()
            r7.zzal(r8)
            r8 = r69
            goto L_0x0d1e
        L_0x0d36:
            r69 = r8
            goto L_0x0d3b
        L_0x0d39:
            r69 = r8
        L_0x0d3b:
            com.google.android.gms.internal.measurement.zzgh r2 = r7.zzmr()
            com.google.android.gms.internal.measurement.zzez r2 = (com.google.android.gms.internal.measurement.zzez) r2
            com.google.android.gms.internal.measurement.zzbt$zzg r2 = (com.google.android.gms.internal.measurement.zzbt.zzg) r2
            r13.add(r2)
            r2 = r68
            r8 = r69
            r9 = r44
            r7 = r66
            goto L_0x0cf1
        L_0x0d4f:
            r68 = r2
            r44 = r9
        L_0x0d53:
            if (r24 == 0) goto L_0x0e68
            boolean r2 = r3.zzgx()
            if (r2 == 0) goto L_0x0e68
            com.google.android.gms.internal.measurement.zzbt$zzf r2 = r3.zzgy()
            java.util.List r2 = r2.zzig()
            boolean r7 = r2.isEmpty()
            if (r7 == 0) goto L_0x0d71
            r42 = r10
            r21 = r11
            r16 = 1
            goto L_0x0e6e
        L_0x0d71:
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>(r13)
            androidx.collection.ArrayMap r8 = new androidx.collection.ArrayMap
            r8.<init>()
            java.util.Iterator r2 = r2.iterator()
        L_0x0d7f:
            boolean r9 = r2.hasNext()
            if (r9 == 0) goto L_0x0db9
            java.lang.Object r9 = r2.next()
            com.google.android.gms.internal.measurement.zzbt$zzg r9 = (com.google.android.gms.internal.measurement.zzbt.zzg) r9
            boolean r13 = r9.zzhd()
            if (r13 == 0) goto L_0x0db6
            int r13 = r9.zzim()
            if (r13 <= 0) goto L_0x0db3
            int r13 = r9.getIndex()
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)
            int r15 = r9.zzim()
            r16 = 1
            int r15 = r15 + -1
            long r17 = r9.zzl(r15)
            java.lang.Long r9 = java.lang.Long.valueOf(r17)
            r8.put(r13, r9)
            goto L_0x0d7f
        L_0x0db3:
            r16 = 1
            goto L_0x0d7f
        L_0x0db6:
            r16 = 1
            goto L_0x0d7f
        L_0x0db9:
            r16 = 1
            r2 = 0
        L_0x0dbc:
            int r9 = r7.size()
            if (r2 >= r9) goto L_0x0e22
            java.lang.Object r9 = r7.get(r2)
            com.google.android.gms.internal.measurement.zzbt$zzg r9 = (com.google.android.gms.internal.measurement.zzbt.zzg) r9
            boolean r13 = r9.zzhd()
            if (r13 == 0) goto L_0x0dd7
            int r13 = r9.getIndex()
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)
            goto L_0x0dd8
        L_0x0dd7:
            r13 = 0
        L_0x0dd8:
            java.lang.Object r13 = r8.remove(r13)
            java.lang.Long r13 = (java.lang.Long) r13
            if (r13 == 0) goto L_0x0e1a
            java.util.ArrayList r15 = new java.util.ArrayList
            r15.<init>()
            long r17 = r13.longValue()
            r42 = r10
            r10 = 0
            long r19 = r9.zzl(r10)
            int r17 = (r17 > r19 ? 1 : (r17 == r19 ? 0 : -1))
            if (r17 >= 0) goto L_0x0df7
            r15.add(r13)
        L_0x0df7:
            java.util.List r13 = r9.zzil()
            r15.addAll(r13)
            com.google.android.gms.internal.measurement.zzez$zza r9 = r9.zzmh()
            com.google.android.gms.internal.measurement.zzez$zza r9 = (com.google.android.gms.internal.measurement.zzez.zza) r9
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r9 = (com.google.android.gms.internal.measurement.zzbt.zzg.zza) r9
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r9 = r9.zzir()
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r9 = r9.zzj(r15)
            com.google.android.gms.internal.measurement.zzgh r9 = r9.zzmr()
            com.google.android.gms.internal.measurement.zzez r9 = (com.google.android.gms.internal.measurement.zzez) r9
            com.google.android.gms.internal.measurement.zzbt$zzg r9 = (com.google.android.gms.internal.measurement.zzbt.zzg) r9
            r7.set(r2, r9)
            goto L_0x0e1d
        L_0x0e1a:
            r42 = r10
            r10 = 0
        L_0x0e1d:
            int r2 = r2 + 1
            r10 = r42
            goto L_0x0dbc
        L_0x0e22:
            r42 = r10
            r10 = 0
            java.util.Set r2 = r8.keySet()
            java.util.Iterator r2 = r2.iterator()
        L_0x0e2d:
            boolean r9 = r2.hasNext()
            if (r9 == 0) goto L_0x0e64
            java.lang.Object r9 = r2.next()
            java.lang.Integer r9 = (java.lang.Integer) r9
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r13 = com.google.android.gms.internal.measurement.zzbt.zzg.zzip()
            int r15 = r9.intValue()
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r13 = r13.zzm(r15)
            java.lang.Object r9 = r8.get(r9)
            java.lang.Long r9 = (java.lang.Long) r9
            r21 = r11
            long r10 = r9.longValue()
            com.google.android.gms.internal.measurement.zzbt$zzg$zza r9 = r13.zzal(r10)
            com.google.android.gms.internal.measurement.zzgh r9 = r9.zzmr()
            com.google.android.gms.internal.measurement.zzez r9 = (com.google.android.gms.internal.measurement.zzez) r9
            com.google.android.gms.internal.measurement.zzbt$zzg r9 = (com.google.android.gms.internal.measurement.zzbt.zzg) r9
            r7.add(r9)
            r11 = r21
            r10 = 0
            goto L_0x0e2d
        L_0x0e64:
            r21 = r11
            r13 = r7
            goto L_0x0e6e
        L_0x0e68:
            r42 = r10
            r21 = r11
            r16 = 1
        L_0x0e6e:
            r6.zzh(r13)
            goto L_0x0e7c
        L_0x0e72:
            r68 = r2
            r44 = r9
            r42 = r10
            r12 = r38
            r16 = 1
        L_0x0e7c:
            r3.zzb(r6)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)
            com.google.android.gms.internal.measurement.zzgh r6 = r3.zzmr()
            com.google.android.gms.internal.measurement.zzez r6 = (com.google.android.gms.internal.measurement.zzez) r6
            com.google.android.gms.internal.measurement.zzbt$zza r6 = (com.google.android.gms.internal.measurement.zzbt.zza) r6
            r4.put(r2, r6)
            int r2 = r5 + 1
            com.google.android.gms.internal.measurement.zzgh r6 = r3.zzmr()
            com.google.android.gms.internal.measurement.zzez r6 = (com.google.android.gms.internal.measurement.zzez) r6
            com.google.android.gms.internal.measurement.zzbt$zza r6 = (com.google.android.gms.internal.measurement.zzbt.zza) r6
            r1[r5] = r6
            com.google.android.gms.measurement.internal.zzw r5 = r66.zzdo()
            com.google.android.gms.internal.measurement.zzbt$zzf r3 = r3.zzgw()
            r5.zzah()
            r5.zzq()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r67)
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r3)
            byte[] r3 = r3.toByteArray()
            android.content.ContentValues r6 = new android.content.ContentValues
            r6.<init>()
            java.lang.String r7 = "app_id"
            r8 = r67
            r6.put(r7, r8)
            java.lang.String r7 = "audience_id"
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r6.put(r7, r0)
            java.lang.String r0 = "current_results"
            r6.put(r0, r3)
            android.database.sqlite.SQLiteDatabase r0 = r5.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0ef2 }
            java.lang.String r3 = "audience_filter_values"
            r7 = 5
            r9 = 0
            long r6 = r0.insertWithOnConflict(r3, r9, r6, r7)     // Catch:{ SQLiteException -> 0x0ef0 }
            r10 = -1
            int r0 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r0 != 0) goto L_0x0f05
            com.google.android.gms.measurement.internal.zzau r0 = r5.zzad()     // Catch:{ SQLiteException -> 0x0ef0 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ SQLiteException -> 0x0ef0 }
            java.lang.String r3 = "Failed to insert filter results (got -1). appId"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r67)     // Catch:{ SQLiteException -> 0x0ef0 }
            r0.zza(r3, r6)     // Catch:{ SQLiteException -> 0x0ef0 }
            goto L_0x0f05
        L_0x0ef0:
            r0 = move-exception
            goto L_0x0ef4
        L_0x0ef2:
            r0 = move-exception
            r9 = 0
        L_0x0ef4:
            com.google.android.gms.measurement.internal.zzau r3 = r5.zzad()
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()
            java.lang.String r5 = "Error storing filter results. appId"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r67)
            r3.zza(r5, r6, r0)
        L_0x0f05:
            r5 = r2
            r46 = r4
            r38 = r12
            r7 = r66
            r2 = r68
            goto L_0x0c4e
        L_0x0f10:
            r8 = r67
            r7 = r66
            goto L_0x0c4e
        L_0x0f16:
            java.lang.Object[] r0 = java.util.Arrays.copyOf(r1, r5)
            com.google.android.gms.internal.measurement.zzbt$zza[] r0 = (com.google.android.gms.internal.measurement.zzbt.zza[]) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzo.zza(java.lang.String, com.google.android.gms.internal.measurement.zzcf[], com.google.android.gms.internal.measurement.zzbt$zzh[]):com.google.android.gms.internal.measurement.zzbt$zza[]");
    }

    private final Boolean zza(zzby zzby, String str, List<zzd> list, long j) {
        zzbz[] zzbzArr;
        zzbz[] zzbzArr2;
        Boolean bool;
        if (zzby.zzwe != null) {
            Boolean zza = zza(j, zzby.zzwe);
            if (zza == null) {
                return null;
            }
            if (!zza.booleanValue()) {
                return Boolean.valueOf(false);
            }
        }
        HashSet hashSet = new HashSet();
        for (zzbz zzbz : zzby.zzwc) {
            if (TextUtils.isEmpty(zzbz.zzwj)) {
                zzad().zzdd().zza("null or empty param name in filter. event", zzaa().zzal(str));
                return null;
            }
            hashSet.add(zzbz.zzwj);
        }
        ArrayMap arrayMap = new ArrayMap();
        for (zzd zzd : list) {
            if (hashSet.contains(zzd.getName())) {
                if (zzd.zzhn()) {
                    arrayMap.put(zzd.getName(), zzd.zzhn() ? Long.valueOf(zzd.zzho()) : null);
                } else if (zzd.zzhq()) {
                    arrayMap.put(zzd.getName(), zzd.zzhq() ? Double.valueOf(zzd.zzhr()) : null);
                } else if (zzd.zzhk()) {
                    arrayMap.put(zzd.getName(), zzd.zzhl());
                } else {
                    zzad().zzdd().zza("Unknown value for param. event, param", zzaa().zzal(str), zzaa().zzam(zzd.getName()));
                    return null;
                }
            }
        }
        for (zzbz zzbz2 : zzby.zzwc) {
            boolean equals = Boolean.TRUE.equals(zzbz2.zzwi);
            String str2 = zzbz2.zzwj;
            if (TextUtils.isEmpty(str2)) {
                zzad().zzdd().zza("Event has empty param name. event", zzaa().zzal(str));
                return null;
            }
            Object obj = arrayMap.get(str2);
            if (obj instanceof Long) {
                if (zzbz2.zzwh == null) {
                    zzad().zzdd().zza("No number filter for long param. event, param", zzaa().zzal(str), zzaa().zzam(str2));
                    return null;
                }
                Boolean zza2 = zza(((Long) obj).longValue(), zzbz2.zzwh);
                if (zza2 == null) {
                    return null;
                }
                if ((true ^ zza2.booleanValue()) ^ equals) {
                    return Boolean.valueOf(false);
                }
            } else if (obj instanceof Double) {
                if (zzbz2.zzwh == null) {
                    zzad().zzdd().zza("No number filter for double param. event, param", zzaa().zzal(str), zzaa().zzam(str2));
                    return null;
                }
                Boolean zza3 = zza(((Double) obj).doubleValue(), zzbz2.zzwh);
                if (zza3 == null) {
                    return null;
                }
                if ((true ^ zza3.booleanValue()) ^ equals) {
                    return Boolean.valueOf(false);
                }
            } else if (obj instanceof String) {
                if (zzbz2.zzwg != null) {
                    bool = zza((String) obj, zzbz2.zzwg);
                } else if (zzbz2.zzwh != null) {
                    String str3 = (String) obj;
                    if (zzfz.zzbl(str3)) {
                        bool = zza(str3, zzbz2.zzwh);
                    } else {
                        zzad().zzdd().zza("Invalid param value for number filter. event, param", zzaa().zzal(str), zzaa().zzam(str2));
                        return null;
                    }
                } else {
                    zzad().zzdd().zza("No filter for String param. event, param", zzaa().zzal(str), zzaa().zzam(str2));
                    return null;
                }
                if (bool == null) {
                    return null;
                }
                if ((true ^ bool.booleanValue()) ^ equals) {
                    return Boolean.valueOf(false);
                }
            } else if (obj == null) {
                zzad().zzdi().zza("Missing param for filter. event, param", zzaa().zzal(str), zzaa().zzam(str2));
                return Boolean.valueOf(false);
            } else {
                zzad().zzdd().zza("Unknown param type. event, param", zzaa().zzal(str), zzaa().zzam(str2));
                return null;
            }
        }
        return Boolean.valueOf(true);
    }

    private final Boolean zza(zzcb zzcb, zzh zzh) {
        zzbz zzbz = zzcb.zzwr;
        if (zzbz == null) {
            zzad().zzdd().zza("Missing property filter. property", zzaa().zzan(zzh.getName()));
            return null;
        }
        boolean equals = Boolean.TRUE.equals(zzbz.zzwi);
        if (zzh.zzhn()) {
            if (zzbz.zzwh != null) {
                return zza(zza(zzh.zzho(), zzbz.zzwh), equals);
            }
            zzad().zzdd().zza("No number filter for long property. property", zzaa().zzan(zzh.getName()));
            return null;
        } else if (zzh.zzhq()) {
            if (zzbz.zzwh != null) {
                return zza(zza(zzh.zzhr(), zzbz.zzwh), equals);
            }
            zzad().zzdd().zza("No number filter for double property. property", zzaa().zzan(zzh.getName()));
            return null;
        } else if (!zzh.zzhk()) {
            zzad().zzdd().zza("User property has no value, property", zzaa().zzan(zzh.getName()));
            return null;
        } else if (zzbz.zzwg != null) {
            return zza(zza(zzh.zzhl(), zzbz.zzwg), equals);
        } else {
            if (zzbz.zzwh == null) {
                zzad().zzdd().zza("No string or number filter defined. property", zzaa().zzan(zzh.getName()));
            } else if (zzfz.zzbl(zzh.zzhl())) {
                return zza(zza(zzh.zzhl(), zzbz.zzwh), equals);
            } else {
                zzad().zzdd().zza("Invalid user property value for Numeric number filter. property, value", zzaa().zzan(zzh.getName()), zzh.zzhl());
            }
            return null;
        }
    }

    @VisibleForTesting
    private static Boolean zza(Boolean bool, boolean z) {
        if (bool == null) {
            return null;
        }
        return Boolean.valueOf(bool.booleanValue() ^ z);
    }

    @VisibleForTesting
    private final Boolean zza(String str, zzcc zzcc) {
        String str2;
        List list;
        Preconditions.checkNotNull(zzcc);
        if (str == null || zzcc.zzws == null || zzcc.zzws == C4565zzb.UNKNOWN_MATCH_TYPE) {
            return null;
        }
        if (zzcc.zzws == C4565zzb.IN_LIST) {
            if (zzcc.zzwv == null || zzcc.zzwv.length == 0) {
                return null;
            }
        } else if (zzcc.zzwt == null) {
            return null;
        }
        C4565zzb zzb = zzcc.zzws;
        boolean z = zzcc.zzwu != null && zzcc.zzwu.booleanValue();
        if (z || zzb == C4565zzb.REGEXP || zzb == C4565zzb.IN_LIST) {
            str2 = zzcc.zzwt;
        } else {
            str2 = zzcc.zzwt.toUpperCase(Locale.ENGLISH);
        }
        if (zzcc.zzwv == null) {
            list = null;
        } else {
            String[] strArr = zzcc.zzwv;
            if (z) {
                list = Arrays.asList(strArr);
            } else {
                ArrayList arrayList = new ArrayList();
                for (String upperCase : strArr) {
                    arrayList.add(upperCase.toUpperCase(Locale.ENGLISH));
                }
                list = arrayList;
            }
        }
        return zza(str, zzb, z, str2, list, zzb == C4565zzb.REGEXP ? str2 : null);
    }

    private final Boolean zza(String str, C4565zzb zzb, boolean z, String str2, List<String> list, String str3) {
        if (str == null) {
            return null;
        }
        if (zzb == C4565zzb.IN_LIST) {
            if (list == null || list.size() == 0) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!z && zzb != C4565zzb.REGEXP) {
            str = str.toUpperCase(Locale.ENGLISH);
        }
        switch (zzp.zzds[zzb.ordinal()]) {
            case 1:
                try {
                    return Boolean.valueOf(Pattern.compile(str3, z ? 0 : 66).matcher(str).matches());
                } catch (PatternSyntaxException unused) {
                    zzad().zzdd().zza("Invalid regular expression in REGEXP audience filter. expression", str3);
                    return null;
                }
            case 2:
                return Boolean.valueOf(str.startsWith(str2));
            case 3:
                return Boolean.valueOf(str.endsWith(str2));
            case 4:
                return Boolean.valueOf(str.contains(str2));
            case 5:
                return Boolean.valueOf(str.equals(str2));
            case 6:
                return Boolean.valueOf(list.contains(str));
            default:
                return null;
        }
    }

    private final Boolean zza(long j, zzca zzca) {
        try {
            return zza(new BigDecimal(j), zzca, 0.0d);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private final Boolean zza(double d, zzca zzca) {
        try {
            return zza(new BigDecimal(d), zzca, Math.ulp(d));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private final Boolean zza(String str, zzca zzca) {
        if (!zzfz.zzbl(str)) {
            return null;
        }
        try {
            return zza(new BigDecimal(str), zzca, 0.0d);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006a, code lost:
        if (r2 != null) goto L_0x006c;
     */
    @com.google.android.gms.common.util.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Boolean zza(java.math.BigDecimal r7, com.google.android.gms.internal.measurement.zzca r8, double r9) {
        /*
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r8)
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r0 = r8.zzwk
            r1 = 0
            if (r0 == 0) goto L_0x00f0
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r0 = r8.zzwk
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r2 = com.google.android.gms.internal.measurement.zzbl.zza.zzb.UNKNOWN_COMPARISON_TYPE
            if (r0 != r2) goto L_0x0010
            goto L_0x00f0
        L_0x0010:
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r0 = r8.zzwk
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r2 = com.google.android.gms.internal.measurement.zzbl.zza.zzb.BETWEEN
            if (r0 != r2) goto L_0x001f
            java.lang.String r0 = r8.zzwn
            if (r0 == 0) goto L_0x001e
            java.lang.String r0 = r8.zzwo
            if (r0 != 0) goto L_0x0024
        L_0x001e:
            return r1
        L_0x001f:
            java.lang.String r0 = r8.zzwm
            if (r0 != 0) goto L_0x0024
            return r1
        L_0x0024:
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r0 = r8.zzwk
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r2 = r8.zzwk
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r3 = com.google.android.gms.internal.measurement.zzbl.zza.zzb.BETWEEN
            if (r2 != r3) goto L_0x0050
            java.lang.String r2 = r8.zzwn
            boolean r2 = com.google.android.gms.measurement.internal.zzfz.zzbl(r2)
            if (r2 == 0) goto L_0x004f
            java.lang.String r2 = r8.zzwo
            boolean r2 = com.google.android.gms.measurement.internal.zzfz.zzbl(r2)
            if (r2 != 0) goto L_0x003d
            goto L_0x004f
        L_0x003d:
            java.math.BigDecimal r2 = new java.math.BigDecimal     // Catch:{ NumberFormatException -> 0x004e }
            java.lang.String r3 = r8.zzwn     // Catch:{ NumberFormatException -> 0x004e }
            r2.<init>(r3)     // Catch:{ NumberFormatException -> 0x004e }
            java.math.BigDecimal r3 = new java.math.BigDecimal     // Catch:{ NumberFormatException -> 0x004e }
            java.lang.String r8 = r8.zzwo     // Catch:{ NumberFormatException -> 0x004e }
            r3.<init>(r8)     // Catch:{ NumberFormatException -> 0x004e }
            r8 = r2
            r2 = r1
            goto L_0x0062
        L_0x004e:
            return r1
        L_0x004f:
            return r1
        L_0x0050:
            java.lang.String r2 = r8.zzwm
            boolean r2 = com.google.android.gms.measurement.internal.zzfz.zzbl(r2)
            if (r2 != 0) goto L_0x0059
            return r1
        L_0x0059:
            java.math.BigDecimal r2 = new java.math.BigDecimal     // Catch:{ NumberFormatException -> 0x00ef }
            java.lang.String r8 = r8.zzwm     // Catch:{ NumberFormatException -> 0x00ef }
            r2.<init>(r8)     // Catch:{ NumberFormatException -> 0x00ef }
            r8 = r1
            r3 = r8
        L_0x0062:
            com.google.android.gms.internal.measurement.zzbl$zza$zzb r4 = com.google.android.gms.internal.measurement.zzbl.zza.zzb.BETWEEN
            if (r0 != r4) goto L_0x006a
            if (r8 == 0) goto L_0x0069
            goto L_0x006c
        L_0x0069:
            return r1
        L_0x006a:
            if (r2 == 0) goto L_0x00ee
        L_0x006c:
            int[] r4 = com.google.android.gms.measurement.internal.zzp.zzdt
            int r0 = r0.ordinal()
            r0 = r4[r0]
            r4 = -1
            r5 = 0
            r6 = 1
            switch(r0) {
                case 1: goto L_0x00e2;
                case 2: goto L_0x00d6;
                case 3: goto L_0x008d;
                case 4: goto L_0x007b;
                default: goto L_0x007a;
            }
        L_0x007a:
            goto L_0x00ee
        L_0x007b:
            int r8 = r7.compareTo(r8)
            if (r8 == r4) goto L_0x0088
            int r7 = r7.compareTo(r3)
            if (r7 == r6) goto L_0x0088
            r5 = 1
        L_0x0088:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)
            return r7
        L_0x008d:
            r0 = 0
            int r8 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r8 == 0) goto L_0x00ca
            java.math.BigDecimal r8 = new java.math.BigDecimal
            r8.<init>(r9)
            java.math.BigDecimal r0 = new java.math.BigDecimal
            r1 = 2
            r0.<init>(r1)
            java.math.BigDecimal r8 = r8.multiply(r0)
            java.math.BigDecimal r8 = r2.subtract(r8)
            int r8 = r7.compareTo(r8)
            if (r8 != r6) goto L_0x00c5
            java.math.BigDecimal r8 = new java.math.BigDecimal
            r8.<init>(r9)
            java.math.BigDecimal r9 = new java.math.BigDecimal
            r9.<init>(r1)
            java.math.BigDecimal r8 = r8.multiply(r9)
            java.math.BigDecimal r8 = r2.add(r8)
            int r7 = r7.compareTo(r8)
            if (r7 != r4) goto L_0x00c5
            r5 = 1
        L_0x00c5:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)
            return r7
        L_0x00ca:
            int r7 = r7.compareTo(r2)
            if (r7 != 0) goto L_0x00d1
            r5 = 1
        L_0x00d1:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)
            return r7
        L_0x00d6:
            int r7 = r7.compareTo(r2)
            if (r7 != r6) goto L_0x00dd
            r5 = 1
        L_0x00dd:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)
            return r7
        L_0x00e2:
            int r7 = r7.compareTo(r2)
            if (r7 != r4) goto L_0x00e9
            r5 = 1
        L_0x00e9:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)
            return r7
        L_0x00ee:
            return r1
        L_0x00ef:
            return r1
        L_0x00f0:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzo.zza(java.math.BigDecimal, com.google.android.gms.internal.measurement.zzca, double):java.lang.Boolean");
    }

    private static List<zzb> zza(Map<Integer, Long> map) {
        if (map == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(map.size());
        for (Integer intValue : map.keySet()) {
            int intValue2 = intValue.intValue();
            arrayList.add((zzb) ((zzez) zzb.zzhg().zzj(intValue2).zzag(((Long) map.get(Integer.valueOf(intValue2))).longValue()).zzmr()));
        }
        return arrayList;
    }

    private static boolean zza(zzcb zzcb) {
        return (zzcb == null || zzcb.zzvx == null || !zzcb.zzvx.booleanValue()) ? false : true;
    }

    private static void zza(Map<Integer, Long> map, int i, long j) {
        Long l = (Long) map.get(Integer.valueOf(i));
        long j2 = j / 1000;
        if (l == null || j2 > l.longValue()) {
            map.put(Integer.valueOf(i), Long.valueOf(j2));
        }
    }

    private static void zzb(Map<Integer, List<Long>> map, int i, long j) {
        List list = (List) map.get(Integer.valueOf(i));
        if (list == null) {
            list = new ArrayList();
            map.put(Integer.valueOf(i), list);
        }
        list.add(Long.valueOf(j / 1000));
    }
}
