package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzq;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public final class zzgd extends zzcu {
    private static final String[] zztb = {"firebase_", "google_", "ga_"};
    private int zzae;
    private SecureRandom zztc;
    private final AtomicLong zztd = new AtomicLong(0);
    private Integer zzte = null;

    zzgd(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return true;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzal() {
        zzq();
        SecureRandom secureRandom = new SecureRandom();
        long nextLong = secureRandom.nextLong();
        if (nextLong == 0) {
            nextLong = secureRandom.nextLong();
            if (nextLong == 0) {
                zzad().zzdd().zzaq("Utils falling back to Random for random id");
            }
        }
        this.zztd.set(nextLong);
    }

    public final long zzgk() {
        long andIncrement;
        long j;
        if (this.zztd.get() == 0) {
            synchronized (this.zztd) {
                long nextLong = new Random(System.nanoTime() ^ zzz().currentTimeMillis()).nextLong();
                int i = this.zzae + 1;
                this.zzae = i;
                j = nextLong + ((long) i);
            }
            return j;
        }
        synchronized (this.zztd) {
            this.zztd.compareAndSet(-1, 1);
            andIncrement = this.zztd.getAndIncrement();
        }
        return andIncrement;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final SecureRandom zzgl() {
        zzq();
        if (this.zztc == null) {
            this.zztc = new SecureRandom();
        }
        return this.zztc;
    }

    static boolean zzbm(String str) {
        Preconditions.checkNotEmpty(str);
        if (str.charAt(0) != '_' || str.equals("_ep")) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public final Bundle zza(@NonNull Uri uri) {
        String str;
        String str2;
        String str3;
        String str4;
        if (uri == null) {
            return null;
        }
        try {
            if (uri.isHierarchical()) {
                str4 = uri.getQueryParameter("utm_campaign");
                str3 = uri.getQueryParameter("utm_source");
                str2 = uri.getQueryParameter("utm_medium");
                str = uri.getQueryParameter("gclid");
            } else {
                str4 = null;
                str3 = null;
                str2 = null;
                str = null;
            }
            if (TextUtils.isEmpty(str4) && TextUtils.isEmpty(str3) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str)) {
                return null;
            }
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(str4)) {
                bundle.putString(Param.CAMPAIGN, str4);
            }
            if (!TextUtils.isEmpty(str3)) {
                bundle.putString("source", str3);
            }
            if (!TextUtils.isEmpty(str2)) {
                bundle.putString(Param.MEDIUM, str2);
            }
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("gclid", str);
            }
            String queryParameter = uri.getQueryParameter("utm_term");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString(Param.TERM, queryParameter);
            }
            String queryParameter2 = uri.getQueryParameter("utm_content");
            if (!TextUtils.isEmpty(queryParameter2)) {
                bundle.putString(Param.CONTENT, queryParameter2);
            }
            String queryParameter3 = uri.getQueryParameter(Param.ACLID);
            if (!TextUtils.isEmpty(queryParameter3)) {
                bundle.putString(Param.ACLID, queryParameter3);
            }
            String queryParameter4 = uri.getQueryParameter(Param.CP1);
            if (!TextUtils.isEmpty(queryParameter4)) {
                bundle.putString(Param.CP1, queryParameter4);
            }
            String queryParameter5 = uri.getQueryParameter("anid");
            if (!TextUtils.isEmpty(queryParameter5)) {
                bundle.putString("anid", queryParameter5);
            }
            return bundle;
        } catch (UnsupportedOperationException e) {
            zzad().zzdd().zza("Install referrer url isn't a hierarchical URI", e);
            return null;
        }
    }

    static boolean zzc(Intent intent) {
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return "android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra) || "android-app://com.google.appcrawler".equals(stringExtra);
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzp(String str, String str2) {
        if (str2 == null) {
            zzad().zzda().zza("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzad().zzda().zza("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (!Character.isLetter(codePointAt)) {
                zzad().zzda().zza("Name must start with a letter. Type, name", str, str2);
                return false;
            }
            int length = str2.length();
            int charCount = Character.charCount(codePointAt);
            while (charCount < length) {
                int codePointAt2 = str2.codePointAt(charCount);
                if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                    charCount += Character.charCount(codePointAt2);
                } else {
                    zzad().zzda().zza("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                    return false;
                }
            }
            return true;
        }
    }

    private final boolean zzq(String str, String str2) {
        if (str2 == null) {
            zzad().zzda().zza("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzad().zzda().zza("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (Character.isLetter(codePointAt) || codePointAt == 95) {
                int length = str2.length();
                int charCount = Character.charCount(codePointAt);
                while (charCount < length) {
                    int codePointAt2 = str2.codePointAt(charCount);
                    if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                        charCount += Character.charCount(codePointAt2);
                    } else {
                        zzad().zzda().zza("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzad().zzda().zza("Name must start with a letter or _ (underscore). Type, name", str, str2);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zza(String str, String[] strArr, String str2) {
        boolean z;
        boolean z2;
        if (str2 == null) {
            zzad().zzda().zza("Name is required and can't be null. Type", str);
            return false;
        }
        Preconditions.checkNotNull(str2);
        String[] strArr2 = zztb;
        int length = strArr2.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (str2.startsWith(strArr2[i])) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            zzad().zzda().zza("Name starts with reserved prefix. Type, name", str, str2);
            return false;
        }
        if (strArr != null) {
            Preconditions.checkNotNull(strArr);
            int length2 = strArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length2) {
                    z2 = false;
                    break;
                } else if (zzs(str2, strArr[i2])) {
                    z2 = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (z2) {
                zzad().zzda().zza("Name is reserved. Type, name", str, str2);
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public final boolean zza(String str, int i, String str2) {
        if (str2 == null) {
            zzad().zzda().zza("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.codePointCount(0, str2.length()) <= i) {
            return true;
        } else {
            zzad().zzda().zza("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i), str2);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public final int zzbn(String str) {
        if (!zzq("event", str)) {
            return 2;
        }
        if (!zza("event", zzcx.zzoy, str)) {
            return 13;
        }
        if (!zza("event", 40, str)) {
            return 2;
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public final int zzbo(String str) {
        if (!zzq("user property", str)) {
            return 6;
        }
        if (!zza("user property", zzcz.zzpc, str)) {
            return 15;
        }
        if (!zza("user property", 24, str)) {
            return 6;
        }
        return 0;
    }

    private final boolean zza(String str, String str2, int i, Object obj, boolean z) {
        Parcelable[] parcelableArr;
        if (obj == null || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Boolean) || (obj instanceof Double)) {
            return true;
        }
        if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
            String valueOf = String.valueOf(obj);
            if (valueOf.codePointCount(0, valueOf.length()) <= i) {
                return true;
            }
            zzad().zzdd().zza("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(valueOf.length()));
            return false;
        } else if ((obj instanceof Bundle) && z) {
            return true;
        } else {
            if ((obj instanceof Parcelable[]) && z) {
                for (Parcelable parcelable : (Parcelable[]) obj) {
                    if (!(parcelable instanceof Bundle)) {
                        zzad().zzdd().zza("All Parcelable[] elements must be of type Bundle. Value type, name", parcelable.getClass(), str2);
                        return false;
                    }
                }
                return true;
            } else if (!(obj instanceof ArrayList) || !z) {
                return false;
            } else {
                ArrayList arrayList = (ArrayList) obj;
                int size = arrayList.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj2 = arrayList.get(i2);
                    i2++;
                    if (!(obj2 instanceof Bundle)) {
                        zzad().zzdd().zza("All ArrayList elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                        return false;
                    }
                }
                return true;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzr(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            if (!zzbp(str)) {
                if (this.zzl.zzel()) {
                    zzad().zzda().zza("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", zzau.zzao(str));
                }
                return false;
            }
        } else if (TextUtils.isEmpty(str2)) {
            if (this.zzl.zzel()) {
                zzad().zzda().zzaq("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
            }
            return false;
        } else if (!zzbp(str2)) {
            zzad().zzda().zza("Invalid admob_app_id. Analytics disabled.", zzau.zzao(str2));
            return false;
        }
        return true;
    }

    static boolean zza(String str, String str2, String str3, String str4) {
        boolean isEmpty = TextUtils.isEmpty(str);
        boolean isEmpty2 = TextUtils.isEmpty(str2);
        if (!isEmpty && !isEmpty2) {
            return !str.equals(str2);
        }
        if (isEmpty && isEmpty2) {
            return (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) ? !TextUtils.isEmpty(str4) : !str3.equals(str4);
        }
        if (isEmpty || !isEmpty2) {
            return TextUtils.isEmpty(str3) || !str3.equals(str4);
        }
        if (TextUtils.isEmpty(str4)) {
            return false;
        }
        return TextUtils.isEmpty(str3) || !str3.equals(str4);
    }

    @VisibleForTesting
    private static boolean zzbp(String str) {
        Preconditions.checkNotNull(str);
        return str.matches("^(1:\\d+:android:[a-f0-9]+|ca-app-pub-.*)$");
    }

    private static Object zza(int i, Object obj, boolean z) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Double)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf((long) ((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf((long) ((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf((long) ((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        } else if (obj instanceof Float) {
            return Double.valueOf(((Float) obj).doubleValue());
        } else {
            if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
                return zza(String.valueOf(obj), i, z);
            }
            return null;
        }
    }

    public static String zza(String str, int i, boolean z) {
        if (str == null) {
            return null;
        }
        if (str.codePointCount(0, str.length()) <= i) {
            return str;
        }
        if (z) {
            return String.valueOf(str.substring(0, str.offsetByCodePoints(0, i))).concat("...");
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public final Object zzb(String str, Object obj) {
        int i = 256;
        if ("_ev".equals(str)) {
            return zza(256, obj, true);
        }
        if (!zzbs(str)) {
            i = 100;
        }
        return zza(i, obj, false);
    }

    static Bundle[] zzb(Object obj) {
        if (obj instanceof Bundle) {
            return new Bundle[]{(Bundle) obj};
        } else if (obj instanceof Parcelable[]) {
            Parcelable[] parcelableArr = (Parcelable[]) obj;
            return (Bundle[]) Arrays.copyOf(parcelableArr, parcelableArr.length, Bundle[].class);
        } else if (!(obj instanceof ArrayList)) {
            return null;
        } else {
            ArrayList arrayList = (ArrayList) obj;
            return (Bundle[]) arrayList.toArray(new Bundle[arrayList.size()]);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0160  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.os.Bundle zza(java.lang.String r18, java.lang.String r19, android.os.Bundle r20, @androidx.annotation.Nullable java.util.List<java.lang.String> r21, boolean r22, boolean r23) {
        /*
            r17 = this;
            r6 = r17
            r7 = r18
            r8 = r20
            r9 = r21
            r10 = 0
            if (r8 == 0) goto L_0x01a5
            android.os.Bundle r11 = new android.os.Bundle
            r11.<init>(r8)
            com.google.android.gms.measurement.internal.zzt r0 = r17.zzaf()
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r1 = com.google.android.gms.measurement.internal.zzal.zziy
            boolean r0 = r0.zze(r7, r1)
            if (r0 == 0) goto L_0x0026
            java.util.TreeSet r0 = new java.util.TreeSet
            java.util.Set r1 = r20.keySet()
            r0.<init>(r1)
            goto L_0x002a
        L_0x0026:
            java.util.Set r0 = r20.keySet()
        L_0x002a:
            java.util.Iterator r12 = r0.iterator()
            r14 = 0
        L_0x002f:
            boolean r0 = r12.hasNext()
            if (r0 == 0) goto L_0x01a6
            java.lang.Object r0 = r12.next()
            r15 = r0
            java.lang.String r15 = (java.lang.String) r15
            r5 = 40
            r0 = 3
            if (r9 == 0) goto L_0x004a
            boolean r1 = r9.contains(r15)
            if (r1 != 0) goto L_0x0048
            goto L_0x004a
        L_0x0048:
            r1 = 0
            goto L_0x0092
        L_0x004a:
            r1 = 14
            if (r22 == 0) goto L_0x006f
            java.lang.String r2 = "event param"
            boolean r2 = r6.zzp(r2, r15)
            if (r2 != 0) goto L_0x0058
            r2 = 3
            goto L_0x0070
        L_0x0058:
            java.lang.String r2 = "event param"
            boolean r2 = r6.zza(r2, r10, r15)
            if (r2 != 0) goto L_0x0063
            r2 = 14
            goto L_0x0070
        L_0x0063:
            java.lang.String r2 = "event param"
            boolean r2 = r6.zza(r2, r5, r15)
            if (r2 != 0) goto L_0x006d
            r2 = 3
            goto L_0x0070
        L_0x006d:
            r2 = 0
            goto L_0x0070
        L_0x006f:
            r2 = 0
        L_0x0070:
            if (r2 != 0) goto L_0x0091
            java.lang.String r2 = "event param"
            boolean r2 = r6.zzq(r2, r15)
            if (r2 != 0) goto L_0x007c
            r1 = 3
            goto L_0x0092
        L_0x007c:
            java.lang.String r2 = "event param"
            boolean r2 = r6.zza(r2, r10, r15)
            if (r2 != 0) goto L_0x0085
            goto L_0x0092
        L_0x0085:
            java.lang.String r1 = "event param"
            boolean r1 = r6.zza(r1, r5, r15)
            if (r1 != 0) goto L_0x008f
            r1 = 3
            goto L_0x0092
        L_0x008f:
            r1 = 0
            goto L_0x0092
        L_0x0091:
            r1 = r2
        L_0x0092:
            r4 = 1
            if (r1 == 0) goto L_0x00ae
            boolean r2 = zza(r11, r1)
            if (r2 == 0) goto L_0x00a9
            java.lang.String r2 = zza(r15, r5, r4)
            java.lang.String r3 = "_ev"
            r11.putString(r3, r2)
            if (r1 != r0) goto L_0x00a9
            zzb(r11, r15)
        L_0x00a9:
            r11.remove(r15)
            goto L_0x0157
        L_0x00ae:
            java.lang.Object r3 = r8.get(r15)
            r17.zzq()
            if (r23 == 0) goto L_0x00ef
            java.lang.String r0 = "param"
            boolean r1 = r3 instanceof android.os.Parcelable[]
            if (r1 == 0) goto L_0x00c2
            r1 = r3
            android.os.Parcelable[] r1 = (android.os.Parcelable[]) r1
            int r1 = r1.length
            goto L_0x00cd
        L_0x00c2:
            boolean r1 = r3 instanceof java.util.ArrayList
            if (r1 == 0) goto L_0x00e6
            r1 = r3
            java.util.ArrayList r1 = (java.util.ArrayList) r1
            int r1 = r1.size()
        L_0x00cd:
            r2 = 1000(0x3e8, float:1.401E-42)
            if (r1 <= r2) goto L_0x00e4
            com.google.android.gms.measurement.internal.zzau r2 = r17.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdd()
            java.lang.String r4 = "Parameter array is too long; discarded. Value kind, name, array length"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r2.zza(r4, r0, r15, r1)
            r0 = 0
            goto L_0x00e7
        L_0x00e4:
            r0 = 1
            goto L_0x00e7
        L_0x00e6:
            r0 = 1
        L_0x00e7:
            if (r0 != 0) goto L_0x00ef
            r0 = 17
            r10 = 1
            r13 = 40
            goto L_0x0134
        L_0x00ef:
            com.google.android.gms.measurement.internal.zzt r0 = r17.zzaf()
            boolean r0 = r0.zzn(r7)
            if (r0 == 0) goto L_0x00ff
            boolean r0 = zzbs(r19)
            if (r0 != 0) goto L_0x0105
        L_0x00ff:
            boolean r0 = zzbs(r15)
            if (r0 == 0) goto L_0x011b
        L_0x0105:
            java.lang.String r1 = "param"
            r4 = 256(0x100, float:3.59E-43)
            r0 = r17
            r2 = r15
            r16 = r3
            r3 = r4
            r10 = 1
            r4 = r16
            r13 = 40
            r5 = r23
            boolean r0 = r0.zza(r1, r2, r3, r4, r5)
            goto L_0x012f
        L_0x011b:
            r16 = r3
            r10 = 1
            r13 = 40
            java.lang.String r1 = "param"
            r3 = 100
            r0 = r17
            r2 = r15
            r4 = r16
            r5 = r23
            boolean r0 = r0.zza(r1, r2, r3, r4, r5)
        L_0x012f:
            if (r0 == 0) goto L_0x0133
            r0 = 0
            goto L_0x0134
        L_0x0133:
            r0 = 4
        L_0x0134:
            if (r0 == 0) goto L_0x015a
            java.lang.String r1 = "_ev"
            boolean r1 = r1.equals(r15)
            if (r1 != 0) goto L_0x015a
            boolean r0 = zza(r11, r0)
            if (r0 == 0) goto L_0x0154
            java.lang.String r0 = zza(r15, r13, r10)
            java.lang.String r1 = "_ev"
            r11.putString(r1, r0)
            java.lang.Object r0 = r8.get(r15)
            zzb(r11, r0)
        L_0x0154:
            r11.remove(r15)
        L_0x0157:
            r10 = 0
            goto L_0x002f
        L_0x015a:
            boolean r0 = zzbm(r15)
            if (r0 == 0) goto L_0x01a0
            int r14 = r14 + 1
            r0 = 25
            if (r14 <= r0) goto L_0x019d
            r0 = 48
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>(r0)
            java.lang.String r0 = "Event can't contain more than 25 params"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.google.android.gms.measurement.internal.zzau r1 = r17.zzad()
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()
            com.google.android.gms.measurement.internal.zzas r2 = r17.zzaa()
            r3 = r19
            java.lang.String r2 = r2.zzal(r3)
            com.google.android.gms.measurement.internal.zzas r4 = r17.zzaa()
            java.lang.String r4 = r4.zzc(r8)
            r1.zza(r0, r2, r4)
            r0 = 5
            zza(r11, r0)
            r11.remove(r15)
            r10 = 0
            goto L_0x002f
        L_0x019d:
            r3 = r19
            goto L_0x01a2
        L_0x01a0:
            r3 = r19
        L_0x01a2:
            r10 = 0
            goto L_0x002f
        L_0x01a5:
            r11 = 0
        L_0x01a6:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzgd.zza(java.lang.String, java.lang.String, android.os.Bundle, java.util.List, boolean, boolean):android.os.Bundle");
    }

    private static boolean zza(Bundle bundle, int i) {
        if (bundle.getLong("_err") != 0) {
            return false;
        }
        bundle.putLong("_err", (long) i);
        return true;
    }

    private static void zzb(Bundle bundle, Object obj) {
        Preconditions.checkNotNull(bundle);
        if (obj == null) {
            return;
        }
        if ((obj instanceof String) || (obj instanceof CharSequence)) {
            bundle.putLong("_el", (long) String.valueOf(obj).length());
        }
    }

    private static int zzbq(String str) {
        if ("_ldl".equals(str)) {
            return 2048;
        }
        return "_id".equals(str) ? 256 : 36;
    }

    /* access modifiers changed from: 0000 */
    public final int zzc(String str, Object obj) {
        boolean z;
        if ("_ldl".equals(str)) {
            z = zza("user property referrer", str, zzbq(str), obj, false);
        } else {
            z = zza("user property", str, zzbq(str), obj, false);
        }
        return z ? 0 : 7;
    }

    /* access modifiers changed from: 0000 */
    public final Object zzd(String str, Object obj) {
        if ("_ldl".equals(str)) {
            return zza(zzbq(str), obj, true);
        }
        return zza(zzbq(str), obj, false);
    }

    /* access modifiers changed from: 0000 */
    public final void zza(Bundle bundle, String str, Object obj) {
        if (bundle != null) {
            if (obj instanceof Long) {
                bundle.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof String) {
                bundle.putString(str, String.valueOf(obj));
            } else if (obj instanceof Double) {
                bundle.putDouble(str, ((Double) obj).doubleValue());
            } else {
                if (str != null) {
                    zzad().zzdf().zza("Not putting event parameter. Invalid value type. name, type", zzaa().zzam(str), obj != null ? obj.getClass().getSimpleName() : null);
                }
            }
        }
    }

    public final void zza(int i, String str, String str2, int i2) {
        zza((String) null, i, str, str2, i2);
    }

    /* access modifiers changed from: 0000 */
    public final void zza(String str, int i, String str2, String str3, int i2) {
        Bundle bundle = new Bundle();
        zza(bundle, i);
        if (zzaf().zze(str, zzal.zzis)) {
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
                bundle.putString(str2, str3);
            }
        } else if (!TextUtils.isEmpty(str2)) {
            bundle.putString(str2, str3);
        }
        if (i == 6 || i == 7 || i == 2) {
            bundle.putLong("_el", (long) i2);
        }
        this.zzl.zzag();
        this.zzl.zzs().logEvent("auto", "_err", bundle);
    }

    static MessageDigest getMessageDigest() {
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
                if (instance != null) {
                    return instance;
                }
                i++;
            } catch (NoSuchAlgorithmException unused) {
            }
        }
        return null;
    }

    @VisibleForTesting
    static long zzd(byte[] bArr) {
        Preconditions.checkNotNull(bArr);
        int i = 0;
        Preconditions.checkState(bArr.length > 0);
        long j = 0;
        int length = bArr.length - 1;
        while (length >= 0 && length >= bArr.length - 8) {
            j += (((long) bArr[length]) & 255) << i;
            i += 8;
            length--;
        }
        return j;
    }

    static boolean zzb(Context context, boolean z) {
        Preconditions.checkNotNull(context);
        if (VERSION.SDK_INT >= 24) {
            return zzb(context, "com.google.android.gms.measurement.AppMeasurementJobService");
        }
        return zzb(context, "com.google.android.gms.measurement.AppMeasurementService");
    }

    private static boolean zzb(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, str), 0);
            if (serviceInfo != null && serviceInfo.enabled) {
                return true;
            }
            return false;
        } catch (NameNotFoundException unused) {
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final boolean zzbr(String str) {
        zzq();
        if (Wrappers.packageManager(getContext()).checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        zzad().zzdh().zza("Permission not granted", str);
        return false;
    }

    static boolean zzbs(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("_");
    }

    static boolean zzs(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.equals(str2);
    }

    static boolean zza(Boolean bool, Boolean bool2) {
        if (bool == null && bool2 == null) {
            return true;
        }
        if (bool == null) {
            return false;
        }
        return bool.equals(bool2);
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzbt(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String zzbu = zzaf().zzbu();
        zzag();
        return zzbu.equals(str);
    }

    /* access modifiers changed from: 0000 */
    public final Bundle zzg(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (String str : bundle.keySet()) {
                Object zzb = zzb(str, bundle.get(str));
                if (zzb == null) {
                    zzad().zzdd().zza("Param value can't be null", zzaa().zzam(str));
                } else {
                    zza(bundle2, str, zzb);
                }
            }
        }
        return bundle2;
    }

    /* access modifiers changed from: 0000 */
    public final zzaj zza(String str, String str2, Bundle bundle, String str3, long j, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        if (zzbn(str2) == 0) {
            Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
            bundle2.putString("_o", str3);
            zzaj zzaj = new zzaj(str2, new zzag(zzg(zza(str, str2, bundle2, CollectionUtils.listOf("_o"), false, false))), str3, j);
            return zzaj;
        }
        zzad().zzda().zza("Invalid conditional property event name", zzaa().zzan(str2));
        throw new IllegalArgumentException();
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final long zzc(Context context, String str) {
        zzq();
        Preconditions.checkNotNull(context);
        Preconditions.checkNotEmpty(str);
        PackageManager packageManager = context.getPackageManager();
        MessageDigest messageDigest = getMessageDigest();
        if (messageDigest == null) {
            zzad().zzda().zzaq("Could not get MD5 instance");
            return -1;
        }
        if (packageManager != null) {
            try {
                if (zzd(context, str)) {
                    return 0;
                }
                PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(getContext().getPackageName(), 64);
                if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                    return zzd(messageDigest.digest(packageInfo.signatures[0].toByteArray()));
                }
                zzad().zzdd().zzaq("Could not get signatures");
                return -1;
            } catch (NameNotFoundException e) {
                zzad().zzda().zza("Package name not found", e);
            }
        }
        return 0;
    }

    @VisibleForTesting
    private final boolean zzd(Context context, String str) {
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
            }
        } catch (CertificateException e) {
            zzad().zzda().zza("Error obtaining certificate", e);
        } catch (NameNotFoundException e2) {
            zzad().zzda().zza("Package name not found", e2);
        }
        return true;
    }

    static byte[] zza(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(obtain, 0);
            return obtain.marshall();
        } finally {
            obtain.recycle();
        }
    }

    public static Bundle zzh(Bundle bundle) {
        if (bundle == null) {
            return new Bundle();
        }
        Bundle bundle2 = new Bundle(bundle);
        for (String str : bundle2.keySet()) {
            Object obj = bundle2.get(str);
            if (obj instanceof Bundle) {
                bundle2.putBundle(str, new Bundle((Bundle) obj));
            } else {
                int i = 0;
                if (obj instanceof Parcelable[]) {
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    while (i < parcelableArr.length) {
                        if (parcelableArr[i] instanceof Bundle) {
                            parcelableArr[i] = new Bundle((Bundle) parcelableArr[i]);
                        }
                        i++;
                    }
                } else if (obj instanceof List) {
                    List list = (List) obj;
                    while (i < list.size()) {
                        Object obj2 = list.get(i);
                        if (obj2 instanceof Bundle) {
                            list.set(i, new Bundle((Bundle) obj2));
                        }
                        i++;
                    }
                }
            }
        }
        return bundle2;
    }

    public final int zzgm() {
        if (this.zzte == null) {
            this.zzte = Integer.valueOf(GoogleApiAvailabilityLight.getInstance().getApkVersion(getContext()) / 1000);
        }
        return this.zzte.intValue();
    }

    public final int zzd(int i) {
        return GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(getContext(), GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    public static long zzc(long j, long j2) {
        return (j + (j2 * 60000)) / 86400000;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final String zzgn() {
        byte[] bArr = new byte[16];
        zzgl().nextBytes(bArr);
        return String.format(Locale.US, "%032x", new Object[]{new BigInteger(1, bArr)});
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzb(Bundle bundle, long j) {
        long j2 = bundle.getLong("_et");
        if (j2 != 0) {
            zzad().zzdd().zza("Params already contained engagement", Long.valueOf(j2));
        }
        bundle.putLong("_et", j + j2);
    }

    public final void zzb(zzq zzq, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("r", str);
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning string value to wrapper", e);
        }
    }

    public final void zza(zzq zzq, long j) {
        Bundle bundle = new Bundle();
        bundle.putLong("r", j);
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning long value to wrapper", e);
        }
    }

    public final void zza(zzq zzq, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("r", i);
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning int value to wrapper", e);
        }
    }

    public final void zza(zzq zzq, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("r", bArr);
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning byte array to wrapper", e);
        }
    }

    public final void zza(zzq zzq, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("r", z);
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning boolean value to wrapper", e);
        }
    }

    public final void zza(zzq zzq, Bundle bundle) {
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning bundle value to wrapper", e);
        }
    }

    public static Bundle zzb(List<zzga> list) {
        Bundle bundle = new Bundle();
        if (list == null) {
            return bundle;
        }
        for (zzga zzga : list) {
            if (zzga.zzki != null) {
                bundle.putString(zzga.name, zzga.zzki);
            } else if (zzga.zzsy != null) {
                bundle.putLong(zzga.name, zzga.zzsy.longValue());
            } else if (zzga.zzta != null) {
                bundle.putDouble(zzga.name, zzga.zzta.doubleValue());
            }
        }
        return bundle;
    }

    public final void zza(zzq zzq, ArrayList<Bundle> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("r", arrayList);
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning bundle list to wrapper", e);
        }
    }

    public static ArrayList<Bundle> zzc(List<zzr> list) {
        if (list == null) {
            return new ArrayList<>(0);
        }
        ArrayList<Bundle> arrayList = new ArrayList<>(list.size());
        for (zzr zzr : list) {
            Bundle bundle = new Bundle();
            bundle.putString("app_id", zzr.packageName);
            bundle.putString("origin", zzr.origin);
            bundle.putLong(ConditionalUserProperty.CREATION_TIMESTAMP, zzr.creationTimestamp);
            bundle.putString("name", zzr.zzdv.name);
            zzcw.zza(bundle, zzr.zzdv.getValue());
            bundle.putBoolean(ConditionalUserProperty.ACTIVE, zzr.active);
            if (zzr.triggerEventName != null) {
                bundle.putString(ConditionalUserProperty.TRIGGER_EVENT_NAME, zzr.triggerEventName);
            }
            if (zzr.zzdw != null) {
                bundle.putString(ConditionalUserProperty.TIMED_OUT_EVENT_NAME, zzr.zzdw.name);
                if (zzr.zzdw.zzfd != null) {
                    bundle.putBundle(ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, zzr.zzdw.zzfd.zzct());
                }
            }
            bundle.putLong(ConditionalUserProperty.TRIGGER_TIMEOUT, zzr.triggerTimeout);
            if (zzr.zzdx != null) {
                bundle.putString(ConditionalUserProperty.TRIGGERED_EVENT_NAME, zzr.zzdx.name);
                if (zzr.zzdx.zzfd != null) {
                    bundle.putBundle(ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, zzr.zzdx.zzfd.zzct());
                }
            }
            bundle.putLong(ConditionalUserProperty.TRIGGERED_TIMESTAMP, zzr.zzdv.zzsx);
            bundle.putLong(ConditionalUserProperty.TIME_TO_LIVE, zzr.timeToLive);
            if (zzr.zzdy != null) {
                bundle.putString(ConditionalUserProperty.EXPIRED_EVENT_NAME, zzr.zzdy.name);
                if (zzr.zzdy.zzfd != null) {
                    bundle.putBundle(ConditionalUserProperty.EXPIRED_EVENT_PARAMS, zzr.zzdy.zzfd.zzct());
                }
            }
            arrayList.add(bundle);
        }
        return arrayList;
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
