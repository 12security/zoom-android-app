package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import com.google.android.gms.internal.firebase_messaging.zzn;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

final class zzx {
    zzx() {
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final zzaa zzb(Context context, String str) throws zzz {
        zzaa zzd = zzd(context, str);
        if (zzd != null) {
            return zzd;
        }
        return zzc(context, str);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final zzaa zzc(Context context, String str) {
        zzaa zzaa = new zzaa(zza.zzc(), System.currentTimeMillis());
        zzaa zza = zza(context, str, zzaa, true);
        if (zza == null || zza.equals(zzaa)) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Generated new key");
            }
            zza(context, str, zzaa);
            return zzaa;
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Loaded key after generating new one, using loaded one");
        }
        return zza;
    }

    static void zza(Context context) {
        File[] listFiles;
        for (File file : zzb(context).listFiles()) {
            if (file.getName().startsWith("com.google.InstanceId")) {
                file.delete();
            }
        }
    }

    @Nullable
    private final zzaa zzd(Context context, String str) throws zzz {
        try {
            zzaa zze = zze(context, str);
            if (zze != null) {
                zza(context, str, zze);
                return zze;
            }
            e = null;
            try {
                zzaa zza = zza(context.getSharedPreferences("com.google.android.gms.appid", 0), str);
                if (zza != null) {
                    zza(context, str, zza, false);
                    return zza;
                }
            } catch (zzz e) {
                e = e;
            }
            if (e == null) {
                return null;
            }
            throw e;
        } catch (zzz e2) {
            e = e2;
        }
    }

    private static KeyPair zzc(String str, String str2) throws zzz {
        try {
            byte[] decode = Base64.decode(str, 8);
            byte[] decode2 = Base64.decode(str2, 8);
            try {
                KeyFactory instance = KeyFactory.getInstance("RSA");
                return new KeyPair(instance.generatePublic(new X509EncodedKeySpec(decode)), instance.generatePrivate(new PKCS8EncodedKeySpec(decode2)));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                String valueOf = String.valueOf(e);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 19);
                sb.append("Invalid key stored ");
                sb.append(valueOf);
                Log.w("FirebaseInstanceId", sb.toString());
                throw new zzz((Exception) e);
            }
        } catch (IllegalArgumentException e2) {
            throw new zzz((Exception) e2);
        }
    }

    @Nullable
    private final zzaa zze(Context context, String str) throws zzz {
        File zzf = zzf(context, str);
        if (!zzf.exists()) {
            return null;
        }
        try {
            return zza(zzf);
        } catch (zzz | IOException e) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(e);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 40);
                sb.append("Failed to read key from file, retrying: ");
                sb.append(valueOf);
                Log.d("FirebaseInstanceId", sb.toString());
            }
            try {
                return zza(zzf);
            } catch (IOException e2) {
                String valueOf2 = String.valueOf(e2);
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 45);
                sb2.append("IID file exists, but failed to read from it: ");
                sb2.append(valueOf2);
                Log.w("FirebaseInstanceId", sb2.toString());
                throw new zzz((Exception) e2);
            }
        }
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:19:0x0062=Splitter:B:19:0x0062, B:32:0x00a2=Splitter:B:32:0x00a2, B:53:0x00be=Splitter:B:53:0x00be} */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.google.firebase.iid.zzaa zza(android.content.Context r9, java.lang.String r10, com.google.firebase.iid.zzaa r11, boolean r12) {
        /*
            r8 = this;
            java.lang.String r0 = "FirebaseInstanceId"
            r1 = 3
            boolean r0 = android.util.Log.isLoggable(r0, r1)
            if (r0 == 0) goto L_0x0010
            java.lang.String r0 = "FirebaseInstanceId"
            java.lang.String r2 = "Writing key to properties file"
            android.util.Log.d(r0, r2)
        L_0x0010:
            java.util.Properties r0 = new java.util.Properties
            r0.<init>()
            java.lang.String r2 = "pub"
            java.lang.String r3 = r11.zzv()
            r0.setProperty(r2, r3)
            java.lang.String r2 = "pri"
            java.lang.String r3 = r11.zzw()
            r0.setProperty(r2, r3)
            java.lang.String r2 = "cre"
            long r3 = r11.zzbz
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r0.setProperty(r2, r3)
            java.io.File r9 = zzf(r9, r10)
            r10 = 0
            r9.createNewFile()     // Catch:{ IOException -> 0x00c2 }
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x00c2 }
            java.lang.String r3 = "rw"
            r2.<init>(r9, r3)     // Catch:{ IOException -> 0x00c2 }
            java.nio.channels.FileChannel r9 = r2.getChannel()     // Catch:{ Throwable -> 0x00b8, all -> 0x00b5 }
            r9.lock()     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            r3 = 0
            if (r12 == 0) goto L_0x0093
            long r5 = r9.size()     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            int r12 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r12 <= 0) goto L_0x0093
            r9.position(r3)     // Catch:{ zzz | IOException -> 0x0066 }
            com.google.firebase.iid.zzaa r11 = zza(r9)     // Catch:{ zzz | IOException -> 0x0066 }
            if (r9 == 0) goto L_0x0062
            zza(r10, r9)     // Catch:{ Throwable -> 0x00b8, all -> 0x00b5 }
        L_0x0062:
            zza(r10, r2)     // Catch:{ IOException -> 0x00c2 }
            return r11
        L_0x0066:
            r12 = move-exception
            java.lang.String r5 = "FirebaseInstanceId"
            boolean r1 = android.util.Log.isLoggable(r5, r1)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            if (r1 == 0) goto L_0x0093
            java.lang.String r1 = "FirebaseInstanceId"
            java.lang.String r12 = java.lang.String.valueOf(r12)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            java.lang.String r5 = java.lang.String.valueOf(r12)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            int r5 = r5.length()     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            int r5 = r5 + 64
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            r6.<init>(r5)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            java.lang.String r5 = "Tried reading key pair before writing new one, but failed with: "
            r6.append(r5)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            r6.append(r12)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            java.lang.String r12 = r6.toString()     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            android.util.Log.d(r1, r12)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
        L_0x0093:
            r9.position(r3)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            java.io.OutputStream r12 = java.nio.channels.Channels.newOutputStream(r9)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            r0.store(r12, r10)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a6 }
            if (r9 == 0) goto L_0x00a2
            zza(r10, r9)     // Catch:{ Throwable -> 0x00b8, all -> 0x00b5 }
        L_0x00a2:
            zza(r10, r2)     // Catch:{ IOException -> 0x00c2 }
            return r11
        L_0x00a6:
            r11 = move-exception
            r12 = r10
            goto L_0x00af
        L_0x00a9:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x00ab }
        L_0x00ab:
            r12 = move-exception
            r7 = r12
            r12 = r11
            r11 = r7
        L_0x00af:
            if (r9 == 0) goto L_0x00b4
            zza(r12, r9)     // Catch:{ Throwable -> 0x00b8, all -> 0x00b5 }
        L_0x00b4:
            throw r11     // Catch:{ Throwable -> 0x00b8, all -> 0x00b5 }
        L_0x00b5:
            r9 = move-exception
            r11 = r10
            goto L_0x00be
        L_0x00b8:
            r9 = move-exception
            throw r9     // Catch:{ all -> 0x00ba }
        L_0x00ba:
            r11 = move-exception
            r7 = r11
            r11 = r9
            r9 = r7
        L_0x00be:
            zza(r11, r2)     // Catch:{ IOException -> 0x00c2 }
            throw r9     // Catch:{ IOException -> 0x00c2 }
        L_0x00c2:
            r9 = move-exception
            java.lang.String r11 = "FirebaseInstanceId"
            java.lang.String r9 = java.lang.String.valueOf(r9)
            java.lang.String r12 = java.lang.String.valueOf(r9)
            int r12 = r12.length()
            int r12 = r12 + 21
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>(r12)
            java.lang.String r12 = "Failed to write key: "
            r0.append(r12)
            r0.append(r9)
            java.lang.String r9 = r0.toString()
            android.util.Log.w(r11, r9)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzx.zza(android.content.Context, java.lang.String, com.google.firebase.iid.zzaa, boolean):com.google.firebase.iid.zzaa");
    }

    private static File zzb(Context context) {
        File noBackupFilesDir = ContextCompat.getNoBackupFilesDir(context);
        if (noBackupFilesDir != null && noBackupFilesDir.isDirectory()) {
            return noBackupFilesDir;
        }
        Log.w("FirebaseInstanceId", "noBackupFilesDir doesn't exist, using regular files directory instead");
        return context.getFilesDir();
    }

    private static File zzf(Context context, String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            str2 = "com.google.InstanceId.properties";
        } else {
            try {
                String encodeToString = Base64.encodeToString(str.getBytes("UTF-8"), 11);
                StringBuilder sb = new StringBuilder(String.valueOf(encodeToString).length() + 33);
                sb.append("com.google.InstanceId_");
                sb.append(encodeToString);
                sb.append(".properties");
                str2 = sb.toString();
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
        return new File(zzb(context), str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0032, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0036, code lost:
        zza(r10, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0039, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.google.firebase.iid.zzaa zza(java.io.File r10) throws com.google.firebase.iid.zzz, java.io.IOException {
        /*
            r9 = this;
            java.io.FileInputStream r0 = new java.io.FileInputStream
            r0.<init>(r10)
            r10 = 0
            java.nio.channels.FileChannel r7 = r0.getChannel()     // Catch:{ Throwable -> 0x0034 }
            r2 = 0
            r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r6 = 1
            r1 = r7
            r1.lock(r2, r4, r6)     // Catch:{ Throwable -> 0x0026, all -> 0x0023 }
            com.google.firebase.iid.zzaa r1 = zza(r7)     // Catch:{ Throwable -> 0x0026, all -> 0x0023 }
            if (r7 == 0) goto L_0x001f
            zza(r10, r7)     // Catch:{ Throwable -> 0x0034 }
        L_0x001f:
            zza(r10, r0)
            return r1
        L_0x0023:
            r1 = move-exception
            r2 = r10
            goto L_0x002c
        L_0x0026:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0028 }
        L_0x0028:
            r2 = move-exception
            r8 = r2
            r2 = r1
            r1 = r8
        L_0x002c:
            if (r7 == 0) goto L_0x0031
            zza(r2, r7)     // Catch:{ Throwable -> 0x0034 }
        L_0x0031:
            throw r1     // Catch:{ Throwable -> 0x0034 }
        L_0x0032:
            r1 = move-exception
            goto L_0x0036
        L_0x0034:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0032 }
        L_0x0036:
            zza(r10, r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzx.zza(java.io.File):com.google.firebase.iid.zzaa");
    }

    private static zzaa zza(FileChannel fileChannel) throws zzz, IOException {
        Properties properties = new Properties();
        properties.load(Channels.newInputStream(fileChannel));
        String property = properties.getProperty("pub");
        String property2 = properties.getProperty("pri");
        if (property == null || property2 == null) {
            throw new zzz("Invalid properties file");
        }
        try {
            return new zzaa(zzc(property, property2), Long.parseLong(properties.getProperty("cre")));
        } catch (NumberFormatException e) {
            throw new zzz((Exception) e);
        }
    }

    @Nullable
    private static zzaa zza(SharedPreferences sharedPreferences, String str) throws zzz {
        String string = sharedPreferences.getString(zzax.zzd(str, "|P|"), null);
        String string2 = sharedPreferences.getString(zzax.zzd(str, "|K|"), null);
        if (string == null || string2 == null) {
            return null;
        }
        return new zzaa(zzc(string, string2), zzb(sharedPreferences, str));
    }

    private final void zza(Context context, String str, zzaa zzaa) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gms.appid", 0);
        try {
            if (zzaa.equals(zza(sharedPreferences, str))) {
                return;
            }
        } catch (zzz unused) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to shared preferences");
        }
        Editor edit = sharedPreferences.edit();
        edit.putString(zzax.zzd(str, "|P|"), zzaa.zzv());
        edit.putString(zzax.zzd(str, "|K|"), zzaa.zzw());
        edit.putString(zzax.zzd(str, "cre"), String.valueOf(zzaa.zzbz));
        edit.commit();
    }

    private static long zzb(SharedPreferences sharedPreferences, String str) {
        String string = sharedPreferences.getString(zzax.zzd(str, "cre"), null);
        if (string != null) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException unused) {
            }
        }
        return 0;
    }

    private static /* synthetic */ void zza(Throwable th, FileChannel fileChannel) {
        if (th != null) {
            try {
                fileChannel.close();
            } catch (Throwable th2) {
                zzn.zza(th, th2);
            }
        } else {
            fileChannel.close();
        }
    }

    private static /* synthetic */ void zza(Throwable th, RandomAccessFile randomAccessFile) {
        if (th != null) {
            try {
                randomAccessFile.close();
            } catch (Throwable th2) {
                zzn.zza(th, th2);
            }
        } else {
            randomAccessFile.close();
        }
    }

    private static /* synthetic */ void zza(Throwable th, FileInputStream fileInputStream) {
        if (th != null) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                zzn.zza(th, th2);
            }
        } else {
            fileInputStream.close();
        }
    }
}
