package com.google.android.gms.measurement.internal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.measurement.zzbr.zza;
import com.google.android.gms.internal.measurement.zzbx;
import com.google.android.gms.internal.measurement.zzby;
import com.google.android.gms.internal.measurement.zzbz;
import com.google.android.gms.internal.measurement.zzcb;
import com.google.android.gms.internal.measurement.zzcd;
import com.google.android.gms.internal.measurement.zzce;
import com.google.android.gms.internal.measurement.zzim;
import com.google.android.gms.internal.measurement.zzin;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import java.io.IOException;
import java.util.Map;

public final class zzbs extends zzfs implements zzv {
    @VisibleForTesting
    private static int zzmp = 65535;
    @VisibleForTesting
    private static int zzmq = 2;
    private final Map<String, Map<String, String>> zzmr = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzms = new ArrayMap();
    private final Map<String, Map<String, Boolean>> zzmt = new ArrayMap();
    private final Map<String, zzce> zzmu = new ArrayMap();
    private final Map<String, Map<String, Integer>> zzmv = new ArrayMap();
    private final Map<String, String> zzmw = new ArrayMap();

    zzbs(zzft zzft) {
        super(zzft);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    @WorkerThread
    private final void zzax(String str) {
        zzah();
        zzq();
        Preconditions.checkNotEmpty(str);
        if (this.zzmu.get(str) == null) {
            byte[] zzag = zzdo().zzag(str);
            if (zzag == null) {
                this.zzmr.put(str, null);
                this.zzms.put(str, null);
                this.zzmt.put(str, null);
                this.zzmu.put(str, null);
                this.zzmw.put(str, null);
                this.zzmv.put(str, null);
                return;
            }
            zzce zza = zza(str, zzag);
            this.zzmr.put(str, zza(zza));
            zza(str, zza);
            this.zzmu.put(str, zza);
            this.zzmw.put(str, null);
        }
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final zzce zzay(String str) {
        zzah();
        zzq();
        Preconditions.checkNotEmpty(str);
        zzax(str);
        return (zzce) this.zzmu.get(str);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final String zzaz(String str) {
        zzq();
        return (String) this.zzmw.get(str);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzba(String str) {
        zzq();
        this.zzmw.put(str, null);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzbb(String str) {
        zzq();
        this.zzmu.remove(str);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzbc(String str) {
        zzq();
        Boolean bool = zzay(str).zzxg;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @WorkerThread
    public final String zzb(String str, String str2) {
        zzq();
        zzax(str);
        Map map = (Map) this.zzmr.get(str);
        if (map != null) {
            return (String) map.get(str2);
        }
        return null;
    }

    private static Map<String, String> zza(zzce zzce) {
        zza[] zzaArr;
        ArrayMap arrayMap = new ArrayMap();
        if (!(zzce == null || zzce.zzxc == null)) {
            for (zza zza : zzce.zzxc) {
                if (zza != null) {
                    arrayMap.put(zza.getKey(), zza.getValue());
                }
            }
        }
        return arrayMap;
    }

    private final void zza(String str, zzce zzce) {
        zzcd[] zzcdArr;
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        if (!(zzce == null || zzce.zzxd == null)) {
            for (zzcd zzcd : zzce.zzxd) {
                if (TextUtils.isEmpty(zzcd.name)) {
                    zzad().zzdd().zzaq("EventConfig contained null event name");
                } else {
                    String zzbh = zzcx.zzbh(zzcd.name);
                    if (!TextUtils.isEmpty(zzbh)) {
                        zzcd.name = zzbh;
                    }
                    arrayMap.put(zzcd.name, zzcd.zzwx);
                    arrayMap2.put(zzcd.name, zzcd.zzwy);
                    if (zzcd.zzwz != null) {
                        if (zzcd.zzwz.intValue() < zzmq || zzcd.zzwz.intValue() > zzmp) {
                            zzad().zzdd().zza("Invalid sampling rate. Event name, sample rate", zzcd.name, zzcd.zzwz);
                        } else {
                            arrayMap3.put(zzcd.name, zzcd.zzwz);
                        }
                    }
                }
            }
        }
        this.zzms.put(str, arrayMap);
        this.zzmt.put(str, arrayMap2);
        this.zzmv.put(str, arrayMap3);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final boolean zza(String str, byte[] bArr, String str2) {
        byte[] bArr2;
        zzby[] zzbyArr;
        zzcb[] zzcbArr;
        zzbz[] zzbzArr;
        String str3 = str;
        zzah();
        zzq();
        Preconditions.checkNotEmpty(str);
        zzce zza = zza(str, bArr);
        if (zza == null) {
            return false;
        }
        zza(str3, zza);
        this.zzmu.put(str3, zza);
        this.zzmw.put(str3, str2);
        this.zzmr.put(str3, zza(zza));
        zzo zzdn = zzdn();
        zzbx[] zzbxArr = zza.zzxe;
        Preconditions.checkNotNull(zzbxArr);
        for (zzbx zzbx : zzbxArr) {
            for (zzby zzby : zzbx.zzvw) {
                String zzbh = zzcx.zzbh(zzby.zzwb);
                if (zzbh != null) {
                    zzby.zzwb = zzbh;
                }
                for (zzbz zzbz : zzby.zzwc) {
                    String zzbh2 = zzcy.zzbh(zzbz.zzwj);
                    if (zzbh2 != null) {
                        zzbz.zzwj = zzbh2;
                    }
                }
            }
            for (zzcb zzcb : zzbx.zzvv) {
                String zzbh3 = zzcz.zzbh(zzcb.zzwq);
                if (zzbh3 != null) {
                    zzcb.zzwq = zzbh3;
                }
            }
        }
        zzdn.zzdo().zza(str3, zzbxArr);
        try {
            zza.zzxe = null;
            bArr2 = new byte[zza.zzly()];
            zza.zza(zzin.zzk(bArr2, 0, bArr2.length));
        } catch (IOException e) {
            zzad().zzdd().zza("Unable to serialize reduced-size config. Storing full config instead. appId", zzau.zzao(str), e);
            bArr2 = bArr;
        }
        zzw zzdo = zzdo();
        Preconditions.checkNotEmpty(str);
        zzdo.zzq();
        zzdo.zzah();
        ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", bArr2);
        try {
            if (((long) zzdo.getWritableDatabase().update("apps", contentValues, "app_id = ?", new String[]{str3})) == 0) {
                zzdo.zzad().zzda().zza("Failed to update remote config (got 0). appId", zzau.zzao(str));
            }
        } catch (SQLiteException e2) {
            zzdo.zzad().zzda().zza("Error storing remote config. appId", zzau.zzao(str), e2);
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzk(String str, String str2) {
        zzq();
        zzax(str);
        if (zzbe(str) && zzgd.zzbs(str2)) {
            return true;
        }
        if (zzbf(str) && zzgd.zzbm(str2)) {
            return true;
        }
        Map map = (Map) this.zzms.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzl(String str, String str2) {
        zzq();
        zzax(str);
        if (Event.ECOMMERCE_PURCHASE.equals(str2)) {
            return true;
        }
        Map map = (Map) this.zzmt.get(str);
        if (map == null) {
            return false;
        }
        Boolean bool = (Boolean) map.get(str2);
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final int zzm(String str, String str2) {
        zzq();
        zzax(str);
        Map map = (Map) this.zzmv.get(str);
        if (map == null) {
            return 1;
        }
        Integer num = (Integer) map.get(str2);
        if (num == null) {
            return 1;
        }
        return num.intValue();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final long zzbd(String str) {
        String zzb = zzb(str, "measurement.account.time_zone_offset_minutes");
        if (!TextUtils.isEmpty(zzb)) {
            try {
                return Long.parseLong(zzb);
            } catch (NumberFormatException e) {
                zzad().zzdd().zza("Unable to parse timezone offset. appId", zzau.zzao(str), e);
            }
        }
        return 0;
    }

    @WorkerThread
    private final zzce zza(String str, byte[] bArr) {
        if (bArr == null) {
            return new zzce();
        }
        zzim zzj = zzim.zzj(bArr, 0, bArr.length);
        zzce zzce = new zzce();
        try {
            zzce.zza(zzj);
            zzad().zzdi().zza("Parsed config. version, gmp_app_id", zzce.zzxa, zzce.zzch);
            return zzce;
        } catch (IOException e) {
            zzad().zzdd().zza("Unable to merge remote config. appId", zzau.zzao(str), e);
            return new zzce();
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzbe(String str) {
        return "1".equals(zzb(str, "measurement.upload.blacklist_internal"));
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzbf(String str) {
        return "1".equals(zzb(str, "measurement.upload.blacklist_public"));
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
