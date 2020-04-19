package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.box.androidsdk.content.BoxConstants;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.internal.measurement.zzbt;
import com.google.android.gms.internal.measurement.zzbt.zzb;
import com.google.android.gms.internal.measurement.zzbt.zzd;
import com.google.android.gms.internal.measurement.zzbt.zzf;
import com.google.android.gms.internal.measurement.zzbt.zzg;
import com.google.android.gms.internal.measurement.zzbt.zzh;
import com.google.android.gms.internal.measurement.zzbt.zzh.zza;
import com.google.android.gms.internal.measurement.zzby;
import com.google.android.gms.internal.measurement.zzbz;
import com.google.android.gms.internal.measurement.zzca;
import com.google.android.gms.internal.measurement.zzcb;
import com.google.android.gms.internal.measurement.zzcc;
import com.google.android.gms.internal.measurement.zzcf;
import com.google.android.gms.internal.measurement.zzcg;
import com.google.android.gms.internal.measurement.zzch;
import com.google.android.gms.internal.measurement.zzez;
import com.google.android.gms.internal.measurement.zzin;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class zzfz extends zzfs {
    zzfz(zzft zzft) {
        super(zzft);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zza zza, Object obj) {
        Preconditions.checkNotNull(obj);
        zza.zziw().zzix().zziy();
        if (obj instanceof String) {
            zza.zzbz((String) obj);
        } else if (obj instanceof Long) {
            zza.zzao(((Long) obj).longValue());
        } else if (obj instanceof Double) {
            zza.zzc(((Double) obj).doubleValue());
        } else {
            zzad().zzda().zza("Ignoring invalid (type) user attribute value", obj);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzd.zza zza, Object obj) {
        Preconditions.checkNotNull(obj);
        zza.zzhv().zzhw().zzhx();
        if (obj instanceof String) {
            zza.zzbx((String) obj);
        } else if (obj instanceof Long) {
            zza.zzaj(((Long) obj).longValue());
        } else if (obj instanceof Double) {
            zza.zzb(((Double) obj).doubleValue());
        } else {
            zzad().zzda().zza("Ignoring invalid (type) event param value", obj);
        }
    }

    /* access modifiers changed from: 0000 */
    public final byte[] zza(zzcg zzcg) {
        try {
            byte[] bArr = new byte[zzcg.zzly()];
            zzin zzk = zzin.zzk(bArr, 0, bArr.length);
            zzcg.zza(zzk);
            zzk.zzlk();
            return bArr;
        } catch (IOException e) {
            zzad().zzda().zza("Data loss. Failed to serialize batch", e);
            return null;
        }
    }

    static zzd zza(zzcf zzcf, String str) {
        zzd[] zzdArr;
        for (zzd zzd : zzcf.zzxi) {
            if (zzd.getName().equals(str)) {
                return zzd;
            }
        }
        return null;
    }

    static Object zzb(zzcf zzcf, String str) {
        zzd zza = zza(zzcf, str);
        if (zza != null) {
            if (zza.zzhk()) {
                return zza.zzhl();
            }
            if (zza.zzhn()) {
                return Long.valueOf(zza.zzho());
            }
            if (zza.zzhq()) {
                return Double.valueOf(zza.zzhr());
            }
        }
        return null;
    }

    static zzd[] zza(zzd[] zzdArr, String str, Object obj) {
        for (int i = 0; i < zzdArr.length; i++) {
            zzd.zza zza = (zzd.zza) zzdArr[i].zzmh();
            if (str.equals(zza.getName())) {
                zza.zzhw().zzhv().zzhx();
                if (obj instanceof Long) {
                    zza.zzaj(((Long) obj).longValue());
                } else if (obj instanceof String) {
                    zza.zzbx((String) obj);
                } else if (obj instanceof Double) {
                    zza.zzb(((Double) obj).doubleValue());
                }
                zzdArr[i] = (zzd) ((zzez) zza.zzmr());
                return zzdArr;
            }
        }
        zzd[] zzdArr2 = new zzd[(zzdArr.length + 1)];
        System.arraycopy(zzdArr, 0, zzdArr2, 0, zzdArr.length);
        zzd.zza zzbw = zzd.zzht().zzbw(str);
        if (obj instanceof Long) {
            zzbw.zzaj(((Long) obj).longValue());
        } else if (obj instanceof String) {
            zzbw.zzbx((String) obj);
        } else if (obj instanceof Double) {
            zzbw.zzb(((Double) obj).doubleValue());
        }
        zzdArr2[zzdArr.length] = (zzd) ((zzez) zzbw.zzmr());
        return zzdArr2;
    }

    /* access modifiers changed from: 0000 */
    public final String zzb(zzcg zzcg) {
        zzch[] zzchArr;
        int i;
        int i2;
        String str;
        int i3;
        zzcg zzcg2 = zzcg;
        StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        if (zzcg2.zzxl != null) {
            for (zzch zzch : zzcg2.zzxl) {
                if (!(zzch == null || zzch == null)) {
                    zza(sb, 1);
                    sb.append("bundle {\n");
                    zza(sb, 1, "protocol_version", (Object) zzch.zzxn);
                    zza(sb, 1, "platform", (Object) zzch.zzxv);
                    zza(sb, 1, "gmp_version", (Object) zzch.zzxz);
                    zza(sb, 1, "uploading_gmp_version", (Object) zzch.zzya);
                    zza(sb, 1, "dynamite_version", (Object) zzch.zzys);
                    zza(sb, 1, "config_version", (Object) zzch.zzyl);
                    zza(sb, 1, "gmp_app_id", (Object) zzch.zzch);
                    zza(sb, 1, "admob_app_id", (Object) zzch.zzxf);
                    zza(sb, 1, "app_id", (Object) zzch.zzcf);
                    zza(sb, 1, "app_version", (Object) zzch.zzcn);
                    zza(sb, 1, "app_version_major", (Object) zzch.zzyh);
                    zza(sb, 1, "firebase_instance_id", (Object) zzch.zzcj);
                    zza(sb, 1, "dev_cert_hash", (Object) zzch.zzyd);
                    zza(sb, 1, "app_store", (Object) zzch.zzcp);
                    zza(sb, 1, "upload_timestamp_millis", (Object) zzch.zzxq);
                    zza(sb, 1, "start_timestamp_millis", (Object) zzch.zzxr);
                    zza(sb, 1, "end_timestamp_millis", (Object) zzch.zzxs);
                    zza(sb, 1, "previous_bundle_start_timestamp_millis", (Object) zzch.zzxt);
                    zza(sb, 1, "previous_bundle_end_timestamp_millis", (Object) zzch.zzxu);
                    zza(sb, 1, "app_instance_id", (Object) zzch.zzcg);
                    zza(sb, 1, "resettable_device_id", (Object) zzch.zzyb);
                    zza(sb, 1, BoxConstants.KEY_BOX_DEVICE_ID, (Object) zzch.zzyk);
                    zza(sb, 1, "ds_id", (Object) zzch.zzyn);
                    zza(sb, 1, "limited_ad_tracking", (Object) zzch.zzyc);
                    zza(sb, 1, "os_version", (Object) zzch.zzxw);
                    zza(sb, 1, "device_model", (Object) zzch.zzxx);
                    zza(sb, 1, "user_default_language", (Object) zzch.zzex);
                    zza(sb, 1, "time_zone_offset_minutes", (Object) zzch.zzxy);
                    zza(sb, 1, "bundle_sequential_index", (Object) zzch.zzye);
                    zza(sb, 1, "service_upload", (Object) zzch.zzyf);
                    zza(sb, 1, "health_monitor", (Object) zzch.zzdn);
                    if (!(zzch.zzym == null || zzch.zzym.longValue() == 0)) {
                        zza(sb, 1, "android_id", (Object) zzch.zzym);
                    }
                    if (zzch.zzyp != null) {
                        zza(sb, 1, "retry_counter", (Object) zzch.zzyp);
                    }
                    zzh[] zzhArr = zzch.zzxp;
                    if (zzhArr != null) {
                        for (zzh zzh : zzhArr) {
                            if (zzh != null) {
                                zza(sb, 2);
                                sb.append("user_property {\n");
                                zza(sb, 2, "set_timestamp_millis", (Object) zzh.zzis() ? Long.valueOf(zzh.zzit()) : null);
                                zza(sb, 2, "name", (Object) zzaa().zzan(zzh.getName()));
                                zza(sb, 2, "string_value", (Object) zzh.zzhl());
                                zza(sb, 2, "int_value", (Object) zzh.zzhn() ? Long.valueOf(zzh.zzho()) : null);
                                zza(sb, 2, "double_value", (Object) zzh.zzhq() ? Double.valueOf(zzh.zzhr()) : null);
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                        }
                    }
                    zzbt.zza[] zzaArr = zzch.zzyg;
                    String str2 = zzch.zzcf;
                    if (zzaArr != null) {
                        int length = zzaArr.length;
                        int i4 = 0;
                        while (i4 < length) {
                            zzbt.zza zza = zzaArr[i4];
                            if (zza != null) {
                                zza(sb, 2);
                                sb.append("audience_membership {\n");
                                if (zza.zzgu()) {
                                    zza(sb, 2, "audience_id", (Object) Integer.valueOf(zza.zzgv()));
                                }
                                if (zza.zzgz()) {
                                    zza(sb, 2, "new_audience", (Object) Boolean.valueOf(zza.zzha()));
                                }
                                StringBuilder sb2 = sb;
                                i2 = i4;
                                i3 = length;
                                str = str2;
                                zza(sb2, 2, "current_data", zza.zzgw(), str2);
                                zza(sb2, 2, "previous_data", zza.zzgy(), str2);
                                zza(sb, 2);
                                sb.append("}\n");
                            } else {
                                i2 = i4;
                                i3 = length;
                                str = str2;
                            }
                            i4 = i2 + 1;
                            length = i3;
                            str2 = str;
                        }
                    }
                    zzcf[] zzcfArr = zzch.zzxo;
                    if (zzcfArr != null) {
                        for (zzcf zzcf : zzcfArr) {
                            if (zzcf != null) {
                                zza(sb, 2);
                                sb.append("event {\n");
                                zza(sb, 2, "name", (Object) zzaa().zzal(zzcf.name));
                                zza(sb, 2, "timestamp_millis", (Object) zzcf.zzxj);
                                zza(sb, 2, "previous_timestamp_millis", (Object) zzcf.zzxk);
                                zza(sb, 2, "count", (Object) zzcf.count);
                                zzd[] zzdArr = zzcf.zzxi;
                                if (zzdArr != null) {
                                    for (zzd zzd : zzdArr) {
                                        if (zzd != null) {
                                            zza(sb, 3);
                                            sb.append("param {\n");
                                            zza(sb, 3, "name", (Object) zzaa().zzam(zzd.getName()));
                                            zza(sb, 3, "string_value", (Object) zzd.zzhl());
                                            zza(sb, 3, "int_value", (Object) zzd.zzhn() ? Long.valueOf(zzd.zzho()) : null);
                                            zza(sb, 3, "double_value", (Object) zzd.zzhq() ? Double.valueOf(zzd.zzhr()) : null);
                                            zza(sb, 3);
                                            sb.append("}\n");
                                        }
                                    }
                                }
                                zza(sb, 2);
                                sb.append("}\n");
                            }
                        }
                        i = 1;
                    } else {
                        i = 1;
                    }
                    zza(sb, i);
                    sb.append("}\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public final String zza(zzby zzby) {
        if (zzby == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nevent_filter {\n");
        zza(sb, 0, "filter_id", (Object) zzby.zzwa);
        zza(sb, 0, "event_name", (Object) zzaa().zzal(zzby.zzwb));
        zza(sb, 1, "event_count_filter", zzby.zzwe);
        sb.append("  filters {\n");
        for (zzbz zza : zzby.zzwc) {
            zza(sb, 2, zza);
        }
        zza(sb, 1);
        sb.append("}\n}\n");
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public final String zzb(zzcb zzcb) {
        if (zzcb == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nproperty_filter {\n");
        zza(sb, 0, "filter_id", (Object) zzcb.zzwa);
        zza(sb, 0, "property_name", (Object) zzaa().zzan(zzcb.zzwq));
        zza(sb, 1, zzcb.zzwr);
        sb.append("}\n");
        return sb.toString();
    }

    private final void zza(StringBuilder sb, int i, String str, zzf zzf, String str2) {
        if (zzf != null) {
            zza(sb, 3);
            sb.append(str);
            sb.append(" {\n");
            if (zzf.zzid() != 0) {
                zza(sb, 4);
                sb.append("results: ");
                int i2 = 0;
                for (Long l : zzf.zzic()) {
                    int i3 = i2 + 1;
                    if (i2 != 0) {
                        sb.append(", ");
                    }
                    sb.append(l);
                    i2 = i3;
                }
                sb.append(10);
            }
            if (zzf.zzib() != 0) {
                zza(sb, 4);
                sb.append("status: ");
                int i4 = 0;
                for (Long l2 : zzf.zzia()) {
                    int i5 = i4 + 1;
                    if (i4 != 0) {
                        sb.append(", ");
                    }
                    sb.append(l2);
                    i4 = i5;
                }
                sb.append(10);
            }
            if (zzaf().zzt(str2)) {
                if (zzf.zzif() != 0) {
                    zza(sb, 4);
                    sb.append("dynamic_filter_timestamps: {");
                    int i6 = 0;
                    for (zzb zzb : zzf.zzie()) {
                        int i7 = i6 + 1;
                        if (i6 != 0) {
                            sb.append(", ");
                        }
                        sb.append(zzb.zzhd() ? Integer.valueOf(zzb.getIndex()) : null);
                        sb.append(":");
                        sb.append(zzb.zzhe() ? Long.valueOf(zzb.zzhf()) : null);
                        i6 = i7;
                    }
                    sb.append("}\n");
                }
                if (zzf.zzih() != 0) {
                    zza(sb, 4);
                    sb.append("sequence_filter_timestamps: {");
                    int i8 = 0;
                    for (zzg zzg : zzf.zzig()) {
                        int i9 = i8 + 1;
                        if (i8 != 0) {
                            sb.append(", ");
                        }
                        sb.append(zzg.zzhd() ? Integer.valueOf(zzg.getIndex()) : null);
                        sb.append(": [");
                        int i10 = 0;
                        for (Long longValue : zzg.zzil()) {
                            long longValue2 = longValue.longValue();
                            int i11 = i10 + 1;
                            if (i10 != 0) {
                                sb.append(", ");
                            }
                            sb.append(longValue2);
                            i10 = i11;
                        }
                        sb.append("]");
                        i8 = i9;
                    }
                    sb.append("}\n");
                }
            }
            zza(sb, 3);
            sb.append("}\n");
        }
    }

    private final void zza(StringBuilder sb, int i, String str, zzca zzca) {
        if (zzca != null) {
            zza(sb, i);
            sb.append(str);
            sb.append(" {\n");
            if (zzca.zzwk != null) {
                zza(sb, i, "comparison_type", (Object) zzca.zzwk.name());
            }
            zza(sb, i, "match_as_float", (Object) zzca.zzwl);
            zza(sb, i, "comparison_value", (Object) zzca.zzwm);
            zza(sb, i, "min_comparison_value", (Object) zzca.zzwn);
            zza(sb, i, "max_comparison_value", (Object) zzca.zzwo);
            zza(sb, i);
            sb.append("}\n");
        }
    }

    private final void zza(StringBuilder sb, int i, zzbz zzbz) {
        String[] strArr;
        if (zzbz != null) {
            zza(sb, i);
            sb.append("filter {\n");
            zza(sb, i, "complement", (Object) zzbz.zzwi);
            zza(sb, i, "param_name", (Object) zzaa().zzam(zzbz.zzwj));
            int i2 = i + 1;
            String str = "string_filter";
            zzcc zzcc = zzbz.zzwg;
            if (zzcc != null) {
                zza(sb, i2);
                sb.append(str);
                sb.append(" {\n");
                if (zzcc.zzws != null) {
                    zza(sb, i2, "match_type", (Object) zzcc.zzws.name());
                }
                zza(sb, i2, "expression", (Object) zzcc.zzwt);
                zza(sb, i2, "case_sensitive", (Object) zzcc.zzwu);
                if (zzcc.zzwv.length > 0) {
                    zza(sb, i2 + 1);
                    sb.append("expression_list {\n");
                    for (String str2 : zzcc.zzwv) {
                        zza(sb, i2 + 2);
                        sb.append(str2);
                        sb.append(FontStyleHelper.SPLITOR);
                    }
                    sb.append("}\n");
                }
                zza(sb, i2);
                sb.append("}\n");
            }
            zza(sb, i2, "number_filter", zzbz.zzwh);
            zza(sb, i);
            sb.append("}\n");
        }
    }

    private static void zza(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("  ");
        }
    }

    private static void zza(StringBuilder sb, int i, String str, Object obj) {
        if (obj != null) {
            zza(sb, i + 1);
            sb.append(str);
            sb.append(": ");
            sb.append(obj);
            sb.append(10);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        zzad().zzda().zzaq("Failed to load parcelable from buffer");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002c, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
        r1.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0030, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x001c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <T extends android.os.Parcelable> T zza(byte[] r5, android.os.Parcelable.Creator<T> r6) {
        /*
            r4 = this;
            r0 = 0
            if (r5 != 0) goto L_0x0004
            return r0
        L_0x0004:
            android.os.Parcel r1 = android.os.Parcel.obtain()
            int r2 = r5.length     // Catch:{ ParseException -> 0x001c }
            r3 = 0
            r1.unmarshall(r5, r3, r2)     // Catch:{ ParseException -> 0x001c }
            r1.setDataPosition(r3)     // Catch:{ ParseException -> 0x001c }
            java.lang.Object r5 = r6.createFromParcel(r1)     // Catch:{ ParseException -> 0x001c }
            android.os.Parcelable r5 = (android.os.Parcelable) r5     // Catch:{ ParseException -> 0x001c }
            r1.recycle()
            return r5
        L_0x001a:
            r5 = move-exception
            goto L_0x002d
        L_0x001c:
            com.google.android.gms.measurement.internal.zzau r5 = r4.zzad()     // Catch:{ all -> 0x001a }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzda()     // Catch:{ all -> 0x001a }
            java.lang.String r6 = "Failed to load parcelable from buffer"
            r5.zzaq(r6)     // Catch:{ all -> 0x001a }
            r1.recycle()
            return r0
        L_0x002d:
            r1.recycle()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzfz.zza(byte[], android.os.Parcelable$Creator):android.os.Parcelable");
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zze(zzaj zzaj, zzm zzm) {
        Preconditions.checkNotNull(zzaj);
        Preconditions.checkNotNull(zzm);
        if (!TextUtils.isEmpty(zzm.zzch) || !TextUtils.isEmpty(zzm.zzcv)) {
            return true;
        }
        zzag();
        return false;
    }

    static boolean zzbl(String str) {
        return str != null && str.matches("([+-])?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    static boolean zza(List<Long> list, int i) {
        if (i < (list.size() << 6)) {
            if (((1 << (i % 64)) & ((Long) list.get(i / 64)).longValue()) != 0) {
                return true;
            }
        }
        return false;
    }

    static List<Long> zza(BitSet bitSet) {
        int length = (bitSet.length() + 63) / 64;
        ArrayList arrayList = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            long j = 0;
            for (int i2 = 0; i2 < 64; i2++) {
                int i3 = (i << 6) + i2;
                if (i3 >= bitSet.length()) {
                    break;
                }
                if (bitSet.get(i3)) {
                    j |= 1 << i2;
                }
            }
            arrayList.add(Long.valueOf(j));
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzb(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(zzz().currentTimeMillis() - j) > j2;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final long zza(byte[] bArr) {
        Preconditions.checkNotNull(bArr);
        zzab().zzq();
        MessageDigest messageDigest = zzgd.getMessageDigest();
        if (messageDigest != null) {
            return zzgd.zzd(messageDigest.digest(bArr));
        }
        zzad().zzda().zzaq("Failed to get MD5");
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public final byte[] zzb(byte[] bArr) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr2);
                if (read > 0) {
                    byteArrayOutputStream.write(bArr2, 0, read);
                } else {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (IOException e) {
            zzad().zzda().zza("Failed to ungzip content", e);
            throw e;
        }
    }

    /* access modifiers changed from: 0000 */
    public final byte[] zzc(byte[] bArr) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            zzad().zzda().zza("Failed to gzip content", e);
            throw e;
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public final int[] zzgj() {
        Map zzk = zzal.zzk(this.zzkt.getContext());
        if (zzk == null || zzk.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int intValue = ((Integer) zzal.zzhp.get(null)).intValue();
        Iterator it = zzk.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Entry entry = (Entry) it.next();
            if (((String) entry.getKey()).startsWith("measurement.id.")) {
                try {
                    int parseInt = Integer.parseInt((String) entry.getValue());
                    if (parseInt != 0) {
                        arrayList.add(Integer.valueOf(parseInt));
                        if (arrayList.size() >= intValue) {
                            zzad().zzdd().zza("Too many experiment IDs. Number of IDs", Integer.valueOf(arrayList.size()));
                            break;
                        }
                    } else {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    zzad().zzdd().zza("Experiment ID NumberFormatException", e);
                }
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        int[] iArr = new int[arrayList.size()];
        ArrayList arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        int i2 = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            int i3 = i2 + 1;
            iArr[i2] = ((Integer) obj).intValue();
            i2 = i3;
        }
        return iArr;
    }

    public final /* bridge */ /* synthetic */ zzfz zzdm() {
        return super.zzdm();
    }

    public final /* bridge */ /* synthetic */ zzo zzdn() {
        return super.zzdn();
    }

    public final /* bridge */ /* synthetic */ zzw zzdo() {
        return super.zzdo();
    }

    public final /* bridge */ /* synthetic */ zzbs zzdp() {
        return super.zzdp();
    }

    public final /* bridge */ /* synthetic */ void zzn() {
        super.zzn();
    }

    public final /* bridge */ /* synthetic */ void zzo() {
        super.zzo();
    }

    public final /* bridge */ /* synthetic */ void zzp() {
        super.zzp();
    }

    public final /* bridge */ /* synthetic */ void zzq() {
        super.zzq();
    }

    public final /* bridge */ /* synthetic */ zzad zzy() {
        return super.zzy();
    }

    public final /* bridge */ /* synthetic */ Clock zzz() {
        return super.zzz();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzas zzaa() {
        return super.zzaa();
    }

    public final /* bridge */ /* synthetic */ zzgd zzab() {
        return super.zzab();
    }

    public final /* bridge */ /* synthetic */ zzbt zzac() {
        return super.zzac();
    }

    public final /* bridge */ /* synthetic */ zzau zzad() {
        return super.zzad();
    }

    public final /* bridge */ /* synthetic */ zzbf zzae() {
        return super.zzae();
    }

    public final /* bridge */ /* synthetic */ zzt zzaf() {
        return super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzq zzag() {
        return super.zzag();
    }
}
