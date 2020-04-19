package p021us.zoom.androidlib.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxItem;
import com.google.common.base.Ascii;
import com.zipow.videobox.tempbean.IMessageTemplateActionItem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.AndroidAppUtil.MimeType;

/* renamed from: us.zoom.androidlib.util.FileUtils */
public class FileUtils {
    private static final int GIGA_BYTE = 1073741824;
    private static final long INTERVAL_GET_DATA_PATH = 200;
    private static final int KELO_BYTE = 1024;
    private static final int MEGA_BYTE = 1048576;
    private static final String SAMSUNG_CLOUD_AGENT_PREFIX = "/storage/emulated/0/.cloudagent";
    private static final String TAG = "FileUtils";
    private static long mLastGetDataPathTime;
    private static String mPrivatePath;
    private static String mPublicPath;

    @SuppressLint({"NewApi"})
    @Nullable
    public static String getPathFromUri(Context context, Uri uri) {
        String str;
        if (context == null || uri == null) {
            return null;
        }
        if (!(VERSION.SDK_INT >= 19)) {
            str = getPathFromUri_Normal(context, uri);
        } else if (DocumentsContract.isDocumentUri(context, uri)) {
            str = getPathFromUri_KitKat(context, uri);
        } else {
            str = getPathFromUri_Normal(context, uri);
        }
        return str;
    }

    @Nullable
    public static File createInternalShareCopyFile(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        String dataPath = getDataPath(context, true, true);
        if (TextUtils.isEmpty(dataPath)) {
            return null;
        }
        try {
            File file = new File(String.format(Locale.getDefault(), "%s/fromShare/%d", new Object[]{dataPath, Long.valueOf(System.currentTimeMillis())}));
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, str);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            return file2;
        } catch (Exception unused) {
            return null;
        }
    }

    @Nullable
    public static File createPublicShareCopyFile(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        String publicDataPath = getPublicDataPath(context);
        if (TextUtils.isEmpty(publicDataPath)) {
            return null;
        }
        try {
            File file = new File(String.format(Locale.getDefault(), "%s/forShare/%d", new Object[]{publicDataPath, Long.valueOf(System.currentTimeMillis())}));
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, str);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            return file2;
        } catch (Exception unused) {
            return null;
        }
    }

    public static File createUnDuplicateNameFile(String str, String str2) {
        String str3;
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return null;
        }
        File file = new File(str2);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        File file2 = new File(file, str);
        if (!file2.exists()) {
            return file2;
        }
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf >= 0) {
            String substring = str.substring(0, lastIndexOf);
            str3 = str.substring(lastIndexOf);
            str = substring;
        } else {
            str3 = "";
        }
        int i = 2;
        while (true) {
            File file3 = new File(file, String.format("%s(%d)%s", new Object[]{str, Integer.valueOf(i), str3}));
            if (!file3.exists()) {
                return file3;
            }
            i++;
        }
    }

    public static boolean copyImageFileToDCIM(Context context, String str, String str2, String str3) {
        FileInputStream fileInputStream;
        Throwable th;
        OutputStream openOutputStream;
        Throwable th2;
        Throwable th3;
        if (!OsUtil.isAtLeastQ()) {
            return copyFile(str, str2);
        }
        if (context == null || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str3)) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", str3);
        contentValues.put(BoxItem.FIELD_DESCRIPTION, str3);
        contentValues.put("mime_type", AndroidAppUtil.IMAGE_MIME_TYPE_PNG);
        contentValues.put("relative_path", str2);
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Uri insert = contentResolver.insert(uri, contentValues);
        if (insert == null) {
            return false;
        }
        try {
            fileInputStream = new FileInputStream(new File(str));
            try {
                openOutputStream = contentResolver.openOutputStream(insert);
                if (openOutputStream != null) {
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int read = fileInputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            openOutputStream.write(bArr, 0, read);
                        }
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        th3 = r6;
                        th2 = th5;
                    }
                }
                if (openOutputStream != null) {
                    openOutputStream.close();
                }
                fileInputStream.close();
                return true;
            } catch (Throwable th6) {
                th = th6;
                throw th;
            }
        } catch (IOException unused) {
            return false;
        } catch (Throwable th7) {
            th.addSuppressed(th7);
        }
        throw th;
        if (openOutputStream != null) {
            if (th3 != null) {
                openOutputStream.close();
            } else {
                openOutputStream.close();
            }
        }
        throw th2;
        throw th2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0069, code lost:
        r3 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x006a, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x006e, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x006f, code lost:
        r12 = r4;
        r4 = r3;
        r3 = r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0083, code lost:
        r14 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0084, code lost:
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0088, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0089, code lost:
        r12 = r3;
        r3 = r14;
        r14 = r12;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0069 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:10:0x0016] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0083 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:8:0x0012] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:23:0x0036=Splitter:B:23:0x0036, B:34:0x0047=Splitter:B:34:0x0047} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFile(java.lang.String r13, java.lang.String r14) {
        /*
            r0 = 0
            if (r13 == 0) goto L_0x00af
            if (r14 != 0) goto L_0x0007
            goto L_0x00af
        L_0x0007:
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00ae }
            r1.<init>(r13)     // Catch:{ Exception -> 0x00ae }
            r13 = 0
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x009d }
            r2.<init>(r14)     // Catch:{ Throwable -> 0x009d }
            java.nio.channels.FileChannel r14 = r1.getChannel()     // Catch:{ Throwable -> 0x0086, all -> 0x0083 }
            java.nio.channels.FileChannel r9 = r2.getChannel()     // Catch:{ Throwable -> 0x006c, all -> 0x0069 }
            long r10 = r14.size()     // Catch:{ Throwable -> 0x0052, all -> 0x004f }
            r5 = 0
            r3 = r9
            r4 = r14
            r7 = r10
            long r3 = r3.transferFrom(r4, r5, r7)     // Catch:{ Throwable -> 0x0052, all -> 0x004f }
            int r3 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
            if (r3 != 0) goto L_0x003d
            r3 = 1
            if (r9 == 0) goto L_0x0031
            r9.close()     // Catch:{ Throwable -> 0x006c, all -> 0x0069 }
        L_0x0031:
            if (r14 == 0) goto L_0x0036
            r14.close()     // Catch:{ Throwable -> 0x0086, all -> 0x0083 }
        L_0x0036:
            r2.close()     // Catch:{ Throwable -> 0x009d }
            r1.close()     // Catch:{ Exception -> 0x00ae }
            return r3
        L_0x003d:
            if (r9 == 0) goto L_0x0042
            r9.close()     // Catch:{ Throwable -> 0x006c, all -> 0x0069 }
        L_0x0042:
            if (r14 == 0) goto L_0x0047
            r14.close()     // Catch:{ Throwable -> 0x0086, all -> 0x0083 }
        L_0x0047:
            r2.close()     // Catch:{ Throwable -> 0x009d }
            r1.close()     // Catch:{ Exception -> 0x00ae }
            goto L_0x00ae
        L_0x004f:
            r3 = move-exception
            r4 = r13
            goto L_0x0058
        L_0x0052:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0054 }
        L_0x0054:
            r4 = move-exception
            r12 = r4
            r4 = r3
            r3 = r12
        L_0x0058:
            if (r9 == 0) goto L_0x0068
            if (r4 == 0) goto L_0x0065
            r9.close()     // Catch:{ Throwable -> 0x0060, all -> 0x0069 }
            goto L_0x0068
        L_0x0060:
            r5 = move-exception
            r4.addSuppressed(r5)     // Catch:{ Throwable -> 0x006c, all -> 0x0069 }
            goto L_0x0068
        L_0x0065:
            r9.close()     // Catch:{ Throwable -> 0x006c, all -> 0x0069 }
        L_0x0068:
            throw r3     // Catch:{ Throwable -> 0x006c, all -> 0x0069 }
        L_0x0069:
            r3 = move-exception
            r4 = r13
            goto L_0x0072
        L_0x006c:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x006e }
        L_0x006e:
            r4 = move-exception
            r12 = r4
            r4 = r3
            r3 = r12
        L_0x0072:
            if (r14 == 0) goto L_0x0082
            if (r4 == 0) goto L_0x007f
            r14.close()     // Catch:{ Throwable -> 0x007a, all -> 0x0083 }
            goto L_0x0082
        L_0x007a:
            r14 = move-exception
            r4.addSuppressed(r14)     // Catch:{ Throwable -> 0x0086, all -> 0x0083 }
            goto L_0x0082
        L_0x007f:
            r14.close()     // Catch:{ Throwable -> 0x0086, all -> 0x0083 }
        L_0x0082:
            throw r3     // Catch:{ Throwable -> 0x0086, all -> 0x0083 }
        L_0x0083:
            r14 = move-exception
            r3 = r13
            goto L_0x008c
        L_0x0086:
            r14 = move-exception
            throw r14     // Catch:{ all -> 0x0088 }
        L_0x0088:
            r3 = move-exception
            r12 = r3
            r3 = r14
            r14 = r12
        L_0x008c:
            if (r3 == 0) goto L_0x0097
            r2.close()     // Catch:{ Throwable -> 0x0092 }
            goto L_0x009a
        L_0x0092:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch:{ Throwable -> 0x009d }
            goto L_0x009a
        L_0x0097:
            r2.close()     // Catch:{ Throwable -> 0x009d }
        L_0x009a:
            throw r14     // Catch:{ Throwable -> 0x009d }
        L_0x009b:
            r14 = move-exception
            goto L_0x009f
        L_0x009d:
            r13 = move-exception
            throw r13     // Catch:{ all -> 0x009b }
        L_0x009f:
            if (r13 == 0) goto L_0x00aa
            r1.close()     // Catch:{ Throwable -> 0x00a5 }
            goto L_0x00ad
        L_0x00a5:
            r1 = move-exception
            r13.addSuppressed(r1)     // Catch:{ Exception -> 0x00ae }
            goto L_0x00ad
        L_0x00aa:
            r1.close()     // Catch:{ Exception -> 0x00ae }
        L_0x00ad:
            throw r14     // Catch:{ Exception -> 0x00ae }
        L_0x00ae:
            return r0
        L_0x00af:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.copyFile(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x00cd, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x00d1, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x00d2, code lost:
        r6 = r0;
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x00d5, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x00d6, code lost:
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:?, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x00d8, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x00d9, code lost:
        r7 = r6;
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x013e, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x013f, code lost:
        r2 = r0;
        r3 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x0142, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x0143, code lost:
        r2 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:?, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x0145, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x0146, code lost:
        r3 = r2;
        r2 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0077, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        r9.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0081, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0082, code lost:
        r8 = r0;
        r9 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x008d, code lost:
        if (r9 != null) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        r14.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0099, code lost:
        r14.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x009d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x009e, code lost:
        r8 = r0;
        r9 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00a1, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x00a2, code lost:
        r8 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:?, code lost:
        throw r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x00a4, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x00a5, code lost:
        r9 = r8;
        r8 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x00ad, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:?, code lost:
        r9.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x00b7, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x00b8, code lost:
        r7 = r0;
        r8 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x00c3 A[SYNTHETIC, Splitter:B:104:0x00c3] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x00cd A[Catch:{ Throwable -> 0x00d5, all -> 0x00d1, all -> 0x00d8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x00d1 A[ExcHandler: all (r0v19 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:3:0x0010] */
    /* JADX WARNING: Removed duplicated region for block: B:170:0x013e A[ExcHandler: all (r0v9 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:136:0x0101] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0081 A[ExcHandler: all (r0v32 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:9:0x0022] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x009d A[ExcHandler: all (r0v28 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:7:0x001e] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x00b7 A[ExcHandler: all (r0v24 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:5:0x0019] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x0042=Splitter:B:21:0x0042, B:35:0x0058=Splitter:B:35:0x0058} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFileFromUri(@androidx.annotation.NonNull android.content.Context r18, @androidx.annotation.NonNull android.net.Uri r19, @androidx.annotation.NonNull java.lang.String r20) {
        /*
            r1 = r19
            r2 = r20
            android.content.ContentResolver r0 = r18.getContentResolver()
            r3 = 1
            r4 = 0
            java.lang.String r5 = "r"
            android.os.ParcelFileDescriptor r5 = r0.openFileDescriptor(r1, r5)     // Catch:{ Exception -> 0x00ed }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
            java.io.FileDescriptor r0 = r5.getFileDescriptor()     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
            r6.<init>(r0)     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00bb, all -> 0x00b7 }
            r7.<init>(r2)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b7 }
            java.nio.channels.FileChannel r14 = r6.getChannel()     // Catch:{ Throwable -> 0x00a1, all -> 0x009d }
            java.nio.channels.FileChannel r15 = r7.getChannel()     // Catch:{ Throwable -> 0x0085, all -> 0x0081 }
            long r16 = r14.size()     // Catch:{ Throwable -> 0x0069, all -> 0x0065 }
            r10 = 0
            r8 = r15
            r9 = r14
            r12 = r16
            long r8 = r8.transferFrom(r9, r10, r12)     // Catch:{ Throwable -> 0x0069, all -> 0x0065 }
            int r0 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1))
            if (r0 != 0) goto L_0x004e
            if (r15 == 0) goto L_0x003d
            r15.close()     // Catch:{ Throwable -> 0x0085, all -> 0x0081 }
        L_0x003d:
            if (r14 == 0) goto L_0x0042
            r14.close()     // Catch:{ Throwable -> 0x00a1, all -> 0x009d }
        L_0x0042:
            r7.close()     // Catch:{ Throwable -> 0x00bb, all -> 0x00b7 }
            r6.close()     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
            if (r5 == 0) goto L_0x004d
            r5.close()     // Catch:{ Exception -> 0x00ed }
        L_0x004d:
            return r3
        L_0x004e:
            if (r15 == 0) goto L_0x0053
            r15.close()     // Catch:{ Throwable -> 0x0085, all -> 0x0081 }
        L_0x0053:
            if (r14 == 0) goto L_0x0058
            r14.close()     // Catch:{ Throwable -> 0x00a1, all -> 0x009d }
        L_0x0058:
            r7.close()     // Catch:{ Throwable -> 0x00bb, all -> 0x00b7 }
            r6.close()     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
            if (r5 == 0) goto L_0x00ed
            r5.close()     // Catch:{ Exception -> 0x00ed }
            goto L_0x00ed
        L_0x0065:
            r0 = move-exception
            r8 = r0
            r9 = r4
            goto L_0x006f
        L_0x0069:
            r0 = move-exception
            r8 = r0
            throw r8     // Catch:{ all -> 0x006c }
        L_0x006c:
            r0 = move-exception
            r9 = r8
            r8 = r0
        L_0x006f:
            if (r15 == 0) goto L_0x0080
            if (r9 == 0) goto L_0x007d
            r15.close()     // Catch:{ Throwable -> 0x0077, all -> 0x0081 }
            goto L_0x0080
        L_0x0077:
            r0 = move-exception
            r10 = r0
            r9.addSuppressed(r10)     // Catch:{ Throwable -> 0x0085, all -> 0x0081 }
            goto L_0x0080
        L_0x007d:
            r15.close()     // Catch:{ Throwable -> 0x0085, all -> 0x0081 }
        L_0x0080:
            throw r8     // Catch:{ Throwable -> 0x0085, all -> 0x0081 }
        L_0x0081:
            r0 = move-exception
            r8 = r0
            r9 = r4
            goto L_0x008b
        L_0x0085:
            r0 = move-exception
            r8 = r0
            throw r8     // Catch:{ all -> 0x0088 }
        L_0x0088:
            r0 = move-exception
            r9 = r8
            r8 = r0
        L_0x008b:
            if (r14 == 0) goto L_0x009c
            if (r9 == 0) goto L_0x0099
            r14.close()     // Catch:{ Throwable -> 0x0093, all -> 0x009d }
            goto L_0x009c
        L_0x0093:
            r0 = move-exception
            r10 = r0
            r9.addSuppressed(r10)     // Catch:{ Throwable -> 0x00a1, all -> 0x009d }
            goto L_0x009c
        L_0x0099:
            r14.close()     // Catch:{ Throwable -> 0x00a1, all -> 0x009d }
        L_0x009c:
            throw r8     // Catch:{ Throwable -> 0x00a1, all -> 0x009d }
        L_0x009d:
            r0 = move-exception
            r8 = r0
            r9 = r4
            goto L_0x00a7
        L_0x00a1:
            r0 = move-exception
            r8 = r0
            throw r8     // Catch:{ all -> 0x00a4 }
        L_0x00a4:
            r0 = move-exception
            r9 = r8
            r8 = r0
        L_0x00a7:
            if (r9 == 0) goto L_0x00b3
            r7.close()     // Catch:{ Throwable -> 0x00ad, all -> 0x00b7 }
            goto L_0x00b6
        L_0x00ad:
            r0 = move-exception
            r7 = r0
            r9.addSuppressed(r7)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b7 }
            goto L_0x00b6
        L_0x00b3:
            r7.close()     // Catch:{ Throwable -> 0x00bb, all -> 0x00b7 }
        L_0x00b6:
            throw r8     // Catch:{ Throwable -> 0x00bb, all -> 0x00b7 }
        L_0x00b7:
            r0 = move-exception
            r7 = r0
            r8 = r4
            goto L_0x00c1
        L_0x00bb:
            r0 = move-exception
            r7 = r0
            throw r7     // Catch:{ all -> 0x00be }
        L_0x00be:
            r0 = move-exception
            r8 = r7
            r7 = r0
        L_0x00c1:
            if (r8 == 0) goto L_0x00cd
            r6.close()     // Catch:{ Throwable -> 0x00c7, all -> 0x00d1 }
            goto L_0x00d0
        L_0x00c7:
            r0 = move-exception
            r6 = r0
            r8.addSuppressed(r6)     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
            goto L_0x00d0
        L_0x00cd:
            r6.close()     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
        L_0x00d0:
            throw r7     // Catch:{ Throwable -> 0x00d5, all -> 0x00d1 }
        L_0x00d1:
            r0 = move-exception
            r6 = r0
            r7 = r4
            goto L_0x00db
        L_0x00d5:
            r0 = move-exception
            r6 = r0
            throw r6     // Catch:{ all -> 0x00d8 }
        L_0x00d8:
            r0 = move-exception
            r7 = r6
            r6 = r0
        L_0x00db:
            if (r5 == 0) goto L_0x00ec
            if (r7 == 0) goto L_0x00e9
            r5.close()     // Catch:{ Throwable -> 0x00e3 }
            goto L_0x00ec
        L_0x00e3:
            r0 = move-exception
            r5 = r0
            r7.addSuppressed(r5)     // Catch:{ Exception -> 0x00ed }
            goto L_0x00ec
        L_0x00e9:
            r5.close()     // Catch:{ Exception -> 0x00ed }
        L_0x00ec:
            throw r6     // Catch:{ Exception -> 0x00ed }
        L_0x00ed:
            r5 = 0
            android.content.ContentResolver r0 = r18.getContentResolver()     // Catch:{ Exception -> 0x0170 }
            java.lang.String r6 = "r"
            android.os.ParcelFileDescriptor r1 = r0.openFileDescriptor(r1, r6)     // Catch:{ Exception -> 0x0170 }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x015b }
            java.io.FileDescriptor r0 = r1.getFileDescriptor()     // Catch:{ Throwable -> 0x015b }
            r6.<init>(r0)     // Catch:{ Throwable -> 0x015b }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0142, all -> 0x013e }
            r7.<init>(r2)     // Catch:{ Throwable -> 0x0142, all -> 0x013e }
            r0 = 8192(0x2000, float:1.14794E-41)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x0128, all -> 0x0124 }
        L_0x010a:
            int r2 = r6.read(r0)     // Catch:{ Throwable -> 0x0128, all -> 0x0124 }
            r8 = -1
            if (r2 == r8) goto L_0x0115
            r7.write(r0, r5, r2)     // Catch:{ Throwable -> 0x0128, all -> 0x0124 }
            goto L_0x010a
        L_0x0115:
            r7.flush()     // Catch:{ Throwable -> 0x0128, all -> 0x0124 }
            r7.close()     // Catch:{ Throwable -> 0x0142, all -> 0x013e }
            r6.close()     // Catch:{ Throwable -> 0x015b }
            if (r1 == 0) goto L_0x0123
            r1.close()     // Catch:{ Exception -> 0x0170 }
        L_0x0123:
            return r3
        L_0x0124:
            r0 = move-exception
            r2 = r0
            r3 = r4
            goto L_0x012e
        L_0x0128:
            r0 = move-exception
            r2 = r0
            throw r2     // Catch:{ all -> 0x012b }
        L_0x012b:
            r0 = move-exception
            r3 = r2
            r2 = r0
        L_0x012e:
            if (r3 == 0) goto L_0x013a
            r7.close()     // Catch:{ Throwable -> 0x0134, all -> 0x013e }
            goto L_0x013d
        L_0x0134:
            r0 = move-exception
            r7 = r0
            r3.addSuppressed(r7)     // Catch:{ Throwable -> 0x0142, all -> 0x013e }
            goto L_0x013d
        L_0x013a:
            r7.close()     // Catch:{ Throwable -> 0x0142, all -> 0x013e }
        L_0x013d:
            throw r2     // Catch:{ Throwable -> 0x0142, all -> 0x013e }
        L_0x013e:
            r0 = move-exception
            r2 = r0
            r3 = r4
            goto L_0x0148
        L_0x0142:
            r0 = move-exception
            r2 = r0
            throw r2     // Catch:{ all -> 0x0145 }
        L_0x0145:
            r0 = move-exception
            r3 = r2
            r2 = r0
        L_0x0148:
            if (r3 == 0) goto L_0x0154
            r6.close()     // Catch:{ Throwable -> 0x014e }
            goto L_0x0157
        L_0x014e:
            r0 = move-exception
            r6 = r0
            r3.addSuppressed(r6)     // Catch:{ Throwable -> 0x015b }
            goto L_0x0157
        L_0x0154:
            r6.close()     // Catch:{ Throwable -> 0x015b }
        L_0x0157:
            throw r2     // Catch:{ Throwable -> 0x015b }
        L_0x0158:
            r0 = move-exception
            r2 = r0
            goto L_0x015e
        L_0x015b:
            r0 = move-exception
            r4 = r0
            throw r4     // Catch:{ all -> 0x0158 }
        L_0x015e:
            if (r1 == 0) goto L_0x016f
            if (r4 == 0) goto L_0x016c
            r1.close()     // Catch:{ Throwable -> 0x0166 }
            goto L_0x016f
        L_0x0166:
            r0 = move-exception
            r1 = r0
            r4.addSuppressed(r1)     // Catch:{ Exception -> 0x0170 }
            goto L_0x016f
        L_0x016c:
            r1.close()     // Catch:{ Exception -> 0x0170 }
        L_0x016f:
            throw r2     // Catch:{ Exception -> 0x0170 }
        L_0x0170:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.copyFileFromUri(android.content.Context, android.net.Uri, java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0074, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0075, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0079, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x007a, code lost:
        r10 = r2;
        r2 = r1;
        r1 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x008e, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x008f, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0093, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0094, code lost:
        r10 = r1;
        r1 = r12;
        r12 = r10;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0074 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:8:0x0014] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x008e A[ExcHandler: all (th java.lang.Throwable), Splitter:B:6:0x0010] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:33:0x0050=Splitter:B:33:0x0050, B:21:0x003d=Splitter:B:21:0x003d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFileToUri(@androidx.annotation.NonNull android.content.Context r11, @androidx.annotation.NonNull java.io.File r12, @androidx.annotation.NonNull android.net.Uri r13) {
        /*
            android.content.ContentResolver r11 = r11.getContentResolver()
            java.lang.String r0 = "w"
            android.os.ParcelFileDescriptor r11 = r11.openFileDescriptor(r13, r0)     // Catch:{ Exception -> 0x00bc }
            r13 = 0
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x00a8 }
            r0.<init>(r12)     // Catch:{ Throwable -> 0x00a8 }
            java.nio.channels.FileChannel r12 = r0.getChannel()     // Catch:{ Throwable -> 0x0091, all -> 0x008e }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
            java.io.FileDescriptor r2 = r11.getFileDescriptor()     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
            java.nio.channels.FileChannel r7 = r1.getChannel()     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
            long r8 = r12.size()     // Catch:{ Throwable -> 0x005d, all -> 0x005a }
            r3 = 0
            r1 = r7
            r2 = r12
            r5 = r8
            long r1 = r1.transferFrom(r2, r3, r5)     // Catch:{ Throwable -> 0x005d, all -> 0x005a }
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 != 0) goto L_0x0046
            r1 = 1
            if (r7 == 0) goto L_0x0038
            r7.close()     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
        L_0x0038:
            if (r12 == 0) goto L_0x003d
            r12.close()     // Catch:{ Throwable -> 0x0091, all -> 0x008e }
        L_0x003d:
            r0.close()     // Catch:{ Throwable -> 0x00a8 }
            if (r11 == 0) goto L_0x0045
            r11.close()     // Catch:{ Exception -> 0x00bc }
        L_0x0045:
            return r1
        L_0x0046:
            if (r7 == 0) goto L_0x004b
            r7.close()     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
        L_0x004b:
            if (r12 == 0) goto L_0x0050
            r12.close()     // Catch:{ Throwable -> 0x0091, all -> 0x008e }
        L_0x0050:
            r0.close()     // Catch:{ Throwable -> 0x00a8 }
            if (r11 == 0) goto L_0x00bc
            r11.close()     // Catch:{ Exception -> 0x00bc }
            goto L_0x00bc
        L_0x005a:
            r1 = move-exception
            r2 = r13
            goto L_0x0063
        L_0x005d:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x005f }
        L_0x005f:
            r2 = move-exception
            r10 = r2
            r2 = r1
            r1 = r10
        L_0x0063:
            if (r7 == 0) goto L_0x0073
            if (r2 == 0) goto L_0x0070
            r7.close()     // Catch:{ Throwable -> 0x006b, all -> 0x0074 }
            goto L_0x0073
        L_0x006b:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
            goto L_0x0073
        L_0x0070:
            r7.close()     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
        L_0x0073:
            throw r1     // Catch:{ Throwable -> 0x0077, all -> 0x0074 }
        L_0x0074:
            r1 = move-exception
            r2 = r13
            goto L_0x007d
        L_0x0077:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0079 }
        L_0x0079:
            r2 = move-exception
            r10 = r2
            r2 = r1
            r1 = r10
        L_0x007d:
            if (r12 == 0) goto L_0x008d
            if (r2 == 0) goto L_0x008a
            r12.close()     // Catch:{ Throwable -> 0x0085, all -> 0x008e }
            goto L_0x008d
        L_0x0085:
            r12 = move-exception
            r2.addSuppressed(r12)     // Catch:{ Throwable -> 0x0091, all -> 0x008e }
            goto L_0x008d
        L_0x008a:
            r12.close()     // Catch:{ Throwable -> 0x0091, all -> 0x008e }
        L_0x008d:
            throw r1     // Catch:{ Throwable -> 0x0091, all -> 0x008e }
        L_0x008e:
            r12 = move-exception
            r1 = r13
            goto L_0x0097
        L_0x0091:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x0093 }
        L_0x0093:
            r1 = move-exception
            r10 = r1
            r1 = r12
            r12 = r10
        L_0x0097:
            if (r1 == 0) goto L_0x00a2
            r0.close()     // Catch:{ Throwable -> 0x009d }
            goto L_0x00a5
        L_0x009d:
            r0 = move-exception
            r1.addSuppressed(r0)     // Catch:{ Throwable -> 0x00a8 }
            goto L_0x00a5
        L_0x00a2:
            r0.close()     // Catch:{ Throwable -> 0x00a8 }
        L_0x00a5:
            throw r12     // Catch:{ Throwable -> 0x00a8 }
        L_0x00a6:
            r12 = move-exception
            goto L_0x00ab
        L_0x00a8:
            r12 = move-exception
            r13 = r12
            throw r13     // Catch:{ all -> 0x00a6 }
        L_0x00ab:
            if (r11 == 0) goto L_0x00bb
            if (r13 == 0) goto L_0x00b8
            r11.close()     // Catch:{ Throwable -> 0x00b3 }
            goto L_0x00bb
        L_0x00b3:
            r11 = move-exception
            r13.addSuppressed(r11)     // Catch:{ Exception -> 0x00bc }
            goto L_0x00bb
        L_0x00b8:
            r11.close()     // Catch:{ Exception -> 0x00bc }
        L_0x00bb:
            throw r12     // Catch:{ Exception -> 0x00bc }
        L_0x00bc:
            r11 = 0
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.copyFileToUri(android.content.Context, java.io.File, android.net.Uri):boolean");
    }

    public static Uri insertFileIntoMediaStore(@NonNull Context context, @NonNull File file) {
        Uri uri;
        String str;
        String str2 = "";
        if (!StringUtil.isEmptyOrNull(AndroidAppUtil.getFileExtendName(file.getAbsolutePath()))) {
            MimeType mimeTypeOfFile = AndroidAppUtil.getMimeTypeOfFile(file.getAbsolutePath());
            if (mimeTypeOfFile != null) {
                str2 = mimeTypeOfFile.mimeType;
            }
        }
        if (StringUtil.isSameString(str2, "video/mp4")) {
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.DIRECTORY_MOVIES);
            sb.append(File.separator);
            sb.append("zoom.us");
            str = sb.toString();
            uri = Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(Environment.DIRECTORY_PICTURES);
            sb2.append(File.separator);
            sb2.append("zoom.us");
            str = sb2.toString();
            uri = Media.EXTERNAL_CONTENT_URI;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", file.getName());
        contentValues.put("mime_type", str2);
        if (OsUtil.isAtLeastQ()) {
            contentValues.put("datetaken", Long.valueOf(file.lastModified()));
            contentValues.put("relative_path", str);
        }
        try {
            return context.getContentResolver().insert(uri, contentValues);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0061, code lost:
        if (r7 != null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006d, code lost:
        if (r7 != null) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006f, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0072, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0068  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getDataColumn(android.content.Context r7, android.net.Uri r8, java.lang.String r9, java.lang.String[] r10) {
        /*
            java.lang.String r0 = "_data"
            java.lang.String[] r3 = new java.lang.String[]{r0}
            r0 = 0
            android.content.ContentResolver r1 = r7.getContentResolver()     // Catch:{ Exception -> 0x006c, all -> 0x0064 }
            r6 = 0
            r2 = r8
            r4 = r9
            r5 = r10
            android.database.Cursor r7 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x006c, all -> 0x0064 }
            if (r7 == 0) goto L_0x0061
            boolean r8 = r7.moveToFirst()     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            if (r8 == 0) goto L_0x0061
            java.lang.String r8 = "_data"
            int r8 = r7.getColumnIndexOrThrow(r8)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            java.lang.String r9 = r7.getString(r8)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            if (r9 == 0) goto L_0x0053
            java.lang.String r10 = "/storage/emulated/0/.cloudagent"
            boolean r10 = r9.startsWith(r10)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            if (r10 == 0) goto L_0x0053
            java.io.File r10 = new java.io.File     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            r10.<init>(r9)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            boolean r9 = r10.exists()     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            if (r9 != 0) goto L_0x0053
            java.lang.String r9 = "thumb_data_path"
            int r9 = r7.getColumnIndex(r9)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            r10 = -1
            if (r9 == r10) goto L_0x0053
            java.lang.String r9 = r7.getString(r9)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            boolean r10 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r9)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            if (r10 != 0) goto L_0x0053
            if (r7 == 0) goto L_0x0052
            r7.close()
        L_0x0052:
            return r9
        L_0x0053:
            java.lang.String r8 = r7.getString(r8)     // Catch:{ Exception -> 0x005f, all -> 0x005d }
            if (r7 == 0) goto L_0x005c
            r7.close()
        L_0x005c:
            return r8
        L_0x005d:
            r8 = move-exception
            goto L_0x0066
        L_0x005f:
            goto L_0x006d
        L_0x0061:
            if (r7 == 0) goto L_0x0072
            goto L_0x006f
        L_0x0064:
            r8 = move-exception
            r7 = r0
        L_0x0066:
            if (r7 == 0) goto L_0x006b
            r7.close()
        L_0x006b:
            throw r8
        L_0x006c:
            r7 = r0
        L_0x006d:
            if (r7 == 0) goto L_0x0072
        L_0x006f:
            r7.close()
        L_0x0072:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String");
    }

    @TargetApi(19)
    private static String getPathFromUri_KitKat(Context context, Uri uri) {
        Uri uri2 = null;
        if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if (!IMessageTemplateActionItem.STYLE_PRIMARY.equalsIgnoreCase(split[0])) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory());
            sb.append("/");
            sb.append(split[1]);
            return sb.toString();
        } else if (isDownloadsDocument(uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (!TextUtils.isEmpty(documentId)) {
                if (documentId.startsWith("raw:")) {
                    return documentId.replaceFirst("raw:", "");
                }
                if (documentId.startsWith("msf:")) {
                    documentId = documentId.replaceFirst("msf:", "");
                }
            }
            if (!OsUtil.isAtLeastQ()) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/my_downloads"), Long.valueOf(documentId).longValue()), null, null);
            }
            DownloadManager downloadManager = (DownloadManager) context.getSystemService("download");
            if (downloadManager == null) {
                return null;
            }
            Uri uriForDownloadedFile = downloadManager.getUriForDownloadedFile(Long.valueOf(documentId).longValue());
            if (uriForDownloadedFile == null) {
                return null;
            }
            return getDataColumn(context, uriForDownloadedFile, null, null);
        } else if (!isMediaDocument(uri)) {
            return null;
        } else {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String str = split2[0];
            if ("image".equals(str)) {
                uri2 = Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(str)) {
                uri2 = Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(str)) {
                uri2 = Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0070, code lost:
        if (r7 == null) goto L_0x0078;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getPathFromUri_Normal(android.content.Context r7, android.net.Uri r8) {
        /*
            java.lang.String r0 = "content"
            java.lang.String r1 = r8.getScheme()
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x00b3
            boolean r0 = isGooglePhotosUri(r8)
            r1 = 0
            if (r0 == 0) goto L_0x0018
            java.lang.String r0 = r8.getLastPathSegment()
            goto L_0x001c
        L_0x0018:
            java.lang.String r0 = getDataColumn(r7, r8, r1, r1)
        L_0x001c:
            if (r0 != 0) goto L_0x00c8
            java.lang.String r2 = r8.getLastPathSegment()
            boolean r3 = p021us.zoom.androidlib.util.AndroidAppUtil.isValidFileType(r2)
            r4 = 0
            if (r3 == 0) goto L_0x0043
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r5 = 1
            java.lang.String r5 = getDataPath(r7, r4, r5)
            r3.append(r5)
            java.lang.String r5 = "/"
            r3.append(r5)
            r3.append(r2)
            java.lang.String r2 = r3.toString()
            goto L_0x0055
        L_0x0043:
            android.content.ContentResolver r2 = r7.getContentResolver()
            java.lang.String r2 = r2.getType(r8)
            java.lang.String r2 = p021us.zoom.androidlib.util.AndroidAppUtil.getFileExtendNameFromMimType(r2)
            java.lang.String r3 = "tmp"
            java.lang.String r2 = createTempFile(r7, r3, r1, r2)
        L_0x0055:
            android.content.ContentResolver r7 = r7.getContentResolver()     // Catch:{ Exception -> 0x00c8 }
            java.io.InputStream r7 = r7.openInputStream(r8)     // Catch:{ Exception -> 0x00c8 }
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x009f }
            r8.<init>(r2)     // Catch:{ Throwable -> 0x009f }
        L_0x0062:
            r3 = 102400(0x19000, float:1.43493E-40)
            byte[] r3 = new byte[r3]     // Catch:{ Throwable -> 0x0088, all -> 0x0085 }
            int r5 = r7.read(r3)     // Catch:{ Throwable -> 0x0088, all -> 0x0085 }
            if (r5 > 0) goto L_0x0081
            r8.close()     // Catch:{ Throwable -> 0x007d, all -> 0x007a }
            if (r7 == 0) goto L_0x0078
            r7.close()     // Catch:{ Exception -> 0x0076 }
            goto L_0x0078
        L_0x0076:
            r0 = r2
            goto L_0x00c8
        L_0x0078:
            r0 = r2
            goto L_0x00c8
        L_0x007a:
            r8 = move-exception
            r0 = r2
            goto L_0x00a2
        L_0x007d:
            r8 = move-exception
            r1 = r8
            r0 = r2
            goto L_0x00a1
        L_0x0081:
            r8.write(r3, r4, r5)     // Catch:{ Throwable -> 0x0088, all -> 0x0085 }
            goto L_0x0062
        L_0x0085:
            r2 = move-exception
            r3 = r1
            goto L_0x008e
        L_0x0088:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x008a }
        L_0x008a:
            r3 = move-exception
            r6 = r3
            r3 = r2
            r2 = r6
        L_0x008e:
            if (r3 == 0) goto L_0x0099
            r8.close()     // Catch:{ Throwable -> 0x0094 }
            goto L_0x009c
        L_0x0094:
            r8 = move-exception
            r3.addSuppressed(r8)     // Catch:{ Throwable -> 0x009f }
            goto L_0x009c
        L_0x0099:
            r8.close()     // Catch:{ Throwable -> 0x009f }
        L_0x009c:
            throw r2     // Catch:{ Throwable -> 0x009f }
        L_0x009d:
            r8 = move-exception
            goto L_0x00a2
        L_0x009f:
            r8 = move-exception
            r1 = r8
        L_0x00a1:
            throw r1     // Catch:{ all -> 0x009d }
        L_0x00a2:
            if (r7 == 0) goto L_0x00b2
            if (r1 == 0) goto L_0x00af
            r7.close()     // Catch:{ Throwable -> 0x00aa }
            goto L_0x00b2
        L_0x00aa:
            r7 = move-exception
            r1.addSuppressed(r7)     // Catch:{ Exception -> 0x00c8 }
            goto L_0x00b2
        L_0x00af:
            r7.close()     // Catch:{ Exception -> 0x00c8 }
        L_0x00b2:
            throw r8     // Catch:{ Exception -> 0x00c8 }
        L_0x00b3:
            java.lang.String r7 = "file"
            java.lang.String r0 = r8.getScheme()
            boolean r7 = r7.equalsIgnoreCase(r0)
            if (r7 == 0) goto L_0x00c4
            java.lang.String r0 = r8.getPath()
            goto L_0x00c8
        L_0x00c4:
            java.lang.String r0 = r8.toString()
        L_0x00c8:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.getPathFromUri_Normal(android.content.Context, android.net.Uri):java.lang.String");
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getFileName(String str) {
        File file = new File(str);
        if (file.exists()) {
            return file.getName();
        }
        return null;
    }

    public static String getMD5(File file) {
        FileInputStream fileInputStream;
        Throwable th;
        Throwable th2;
        int read;
        int i;
        byte[] digest;
        if (!file.exists()) {
            return null;
        }
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    byte[] bArr = new byte[10240];
                    do {
                        read = fileInputStream.read(bArr);
                        if (read > 0) {
                            instance.update(bArr, 0, read);
                            continue;
                        }
                    } while (read > 0);
                    char[] cArr2 = new char[(r2 * 2)];
                    int i2 = 0;
                    for (byte b : instance.digest()) {
                        int i3 = i2 + 1;
                        cArr2[i2] = cArr[(b >>> 4) & 15];
                        i2 = i3 + 1;
                        cArr2[i3] = cArr[b & Ascii.f228SI];
                    }
                    String str = new String(cArr2);
                    fileInputStream.close();
                    return str;
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    th2 = r11;
                    th = th4;
                }
            } catch (Exception unused) {
                return null;
            }
        } catch (NoSuchAlgorithmException unused2) {
            return null;
        }
        throw th;
        if (th2 != null) {
            try {
                fileInputStream.close();
            } catch (Throwable th5) {
                th2.addSuppressed(th5);
            }
        } else {
            fileInputStream.close();
        }
        throw th;
    }

    public static boolean isSameFile(File file, File file2) {
        return StringUtil.isSameString(getMD5(file), getMD5(file2));
    }

    public static String toFileSizeString(Context context, long j) {
        String str;
        if (j < 0 || context == null) {
            return "";
        }
        if (j >= 1073741824) {
            str = context.getString(C4409R.string.zm_file_size_gb, new Object[]{getFileSizeString(((double) j) / 1.073741824E9d)});
        } else if (j >= 1048576) {
            str = context.getString(C4409R.string.zm_file_size_mb, new Object[]{getFileSizeString(((double) j) / 1048576.0d)});
        } else if (j >= 1024) {
            str = context.getString(C4409R.string.zm_file_size_kb, new Object[]{getFileSizeString(((double) j) / 1024.0d)});
        } else {
            str = context.getString(C4409R.string.zm_file_size_bytes, new Object[]{getFileSizeString((double) j)});
        }
        return str;
    }

    public static String toTemplateFileSizeString(Context context, long j) {
        String str;
        if (j < 0 || context == null) {
            return "";
        }
        if (j >= 1073741824) {
            str = context.getString(C4409R.string.zm_file_size_gb, new Object[]{getTemplateFileSizeString(((double) j) / 1.073741824E9d)});
        } else if (j >= 1048576) {
            str = context.getString(C4409R.string.zm_file_size_mb, new Object[]{getTemplateFileSizeString(((double) j) / 1048576.0d)});
        } else if (j >= 1024) {
            str = context.getString(C4409R.string.zm_file_size_kb, new Object[]{getTemplateFileSizeString((double) (j / 1024), 0)});
        } else {
            str = context.getString(C4409R.string.zm_template_file_size_bytes_63441, new Object[]{getTemplateFileSizeString((double) j, 0)});
        }
        return str;
    }

    private static String getTemplateFileSizeString(double d) {
        return getTemplateFileSizeString(d, 1, 1);
    }

    private static String getTemplateFileSizeString(double d, int i) {
        return getTemplateFileSizeString(d, i, 1);
    }

    private static String getTemplateFileSizeString(double d, int i, int i2) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMinimumFractionDigits(i);
        numberInstance.setMaximumFractionDigits(i2);
        return numberInstance.format(d);
    }

    private static String getFileSizeString(double d) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(2);
        return numberInstance.format(d);
    }

    public static String shrinkFileName(String str, int i) {
        if (str == null || str.length() <= i || i <= 0) {
            return str;
        }
        String str2 = "";
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf >= 0) {
            int i2 = lastIndexOf - 1;
            if (i2 < 0) {
                i2 = 0;
            }
            str2 = str.substring(i2);
        }
        int length = (i - str2.length()) - 3;
        if (length <= 0) {
            return str.substring(0, i);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, length));
        sb.append("...");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(str2);
        return sb3.toString();
    }

    public static String getPublicDataPath(Context context) {
        if (!TextUtils.equals("mounted", Environment.getExternalStorageState())) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            sb.append(externalFilesDir.getParent());
            sb.append("/data");
        } else {
            sb.append("/sdcard/Android/data/");
            sb.append(context.getPackageName());
            sb.append("/data");
        }
        File file = new File(sb.toString());
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        if (file.exists() && file.isDirectory()) {
            return sb.toString();
        }
        if (file.mkdirs()) {
            return sb.toString();
        }
        return null;
    }

    @Nullable
    public static String getDataPath(Context context, boolean z, boolean z2) {
        if (context == null) {
            return null;
        }
        if (!z2 && System.currentTimeMillis() - mLastGetDataPathTime < 200) {
            mLastGetDataPathTime = System.currentTimeMillis();
            String str = mPrivatePath;
            if (str != null) {
                return str;
            }
        }
        mLastGetDataPathTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            sb.append(filesDir.getParent());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("/data/data/");
            sb2.append(context.getPackageName());
            sb.append(sb2.toString());
        }
        sb.append("/data");
        String sb3 = sb.toString();
        File file = new File(sb3);
        if (z2 && !file.exists()) {
            file.mkdirs();
        }
        mPrivatePath = sb3;
        return sb3;
    }

    public static String getTempPath(Context context) {
        return getDataPath(context, false, true);
    }

    public static String createTempFile(Context context, String str, String str2, String str3) {
        if (str2 == null || str2.length() == 0) {
            str2 = getDataPath(context, false, true);
        } else {
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        if (StringUtil.isEmptyOrNull(str3)) {
            str3 = "tmp";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("/");
        sb.append(str);
        sb.append("-");
        sb.append(UUID.randomUUID().toString());
        if (!str3.startsWith(".")) {
            sb.append(".");
        }
        sb.append(str3);
        return sb.toString();
    }

    public static String downloadFile(Context context, URL url) {
        try {
            return saveFileToTempLocalPath(context, url.openStream());
        } catch (IOException unused) {
            return null;
        }
    }

    public static String saveFileFromContentProvider(Context context, Uri uri) {
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            if (openInputStream == null) {
                return null;
            }
            return saveFileToTempLocalPath(context, openInputStream);
        } catch (FileNotFoundException unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0036, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r2.addSuppressed(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x003f, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0040, code lost:
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0044, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0045, code lost:
        r5 = r7;
        r7 = r6;
        r6 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0017, code lost:
        if (r7 == null) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:8:0x0019, B:25:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x003f A[ExcHandler: all (th java.lang.Throwable), Splitter:B:8:0x0019] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String saveFileToTempLocalPath(android.content.Context r6, java.io.InputStream r7) {
        /*
            java.lang.String r0 = "tmp"
            r1 = 0
            java.lang.String r6 = createTempFile(r6, r0, r1, r1)
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0057 }
            r0.<init>(r6)     // Catch:{ IOException -> 0x0057 }
        L_0x000c:
            r2 = 102400(0x19000, float:1.43493E-40)
            byte[] r2 = new byte[r2]     // Catch:{ Throwable -> 0x0028, all -> 0x0025 }
            int r3 = r7.read(r2)     // Catch:{ Throwable -> 0x0028, all -> 0x0025 }
            if (r3 > 0) goto L_0x0020
            if (r7 == 0) goto L_0x001c
            r7.close()     // Catch:{ Throwable -> 0x0042, all -> 0x003f }
        L_0x001c:
            r0.close()     // Catch:{ IOException -> 0x0057 }
            return r6
        L_0x0020:
            r4 = 0
            r0.write(r2, r4, r3)     // Catch:{ Throwable -> 0x0028, all -> 0x0025 }
            goto L_0x000c
        L_0x0025:
            r6 = move-exception
            r2 = r1
            goto L_0x002e
        L_0x0028:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x002a }
        L_0x002a:
            r2 = move-exception
            r5 = r2
            r2 = r6
            r6 = r5
        L_0x002e:
            if (r7 == 0) goto L_0x003e
            if (r2 == 0) goto L_0x003b
            r7.close()     // Catch:{ Throwable -> 0x0036, all -> 0x003f }
            goto L_0x003e
        L_0x0036:
            r7 = move-exception
            r2.addSuppressed(r7)     // Catch:{ Throwable -> 0x0042, all -> 0x003f }
            goto L_0x003e
        L_0x003b:
            r7.close()     // Catch:{ Throwable -> 0x0042, all -> 0x003f }
        L_0x003e:
            throw r6     // Catch:{ Throwable -> 0x0042, all -> 0x003f }
        L_0x003f:
            r6 = move-exception
            r7 = r1
            goto L_0x0048
        L_0x0042:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x0044 }
        L_0x0044:
            r7 = move-exception
            r5 = r7
            r7 = r6
            r6 = r5
        L_0x0048:
            if (r7 == 0) goto L_0x0053
            r0.close()     // Catch:{ Throwable -> 0x004e }
            goto L_0x0056
        L_0x004e:
            r0 = move-exception
            r7.addSuppressed(r0)     // Catch:{ IOException -> 0x0057 }
            goto L_0x0056
        L_0x0053:
            r0.close()     // Catch:{ IOException -> 0x0057 }
        L_0x0056:
            throw r6     // Catch:{ IOException -> 0x0057 }
        L_0x0057:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.saveFileToTempLocalPath(android.content.Context, java.io.InputStream):java.lang.String");
    }

    @Nullable
    public static String makeNewFilePathWithName(String str, String str2) {
        if (str == null) {
            return null;
        }
        File file = new File(str);
        if (!file.exists() || !file.canWrite()) {
            return null;
        }
        if (StringUtil.isEmptyOrNull(str2)) {
            str2 = "unnamed";
        }
        int lastIndexOf = str2.lastIndexOf(47);
        if (lastIndexOf > 0) {
            str2 = str2.substring(lastIndexOf + 1);
        }
        String str3 = "";
        int lastIndexOf2 = str2.lastIndexOf(46);
        String substring = lastIndexOf2 >= 0 ? str2.substring(lastIndexOf2) : str3;
        int i = 0;
        String substring2 = str2.substring(0, str2.length() - substring.length());
        while (true) {
            StringBuilder sb = new StringBuilder(str);
            if (!str.endsWith("/")) {
                sb.append('/');
            }
            sb.append(substring2);
            if (i > 0) {
                sb.append('(');
                sb.append(i);
                sb.append(')');
            }
            i++;
            sb.append(substring);
            File file2 = new File(sb.toString());
            sb.append(".zmdownload");
            File file3 = new File(sb.toString());
            if (!file2.exists() && !file3.exists()) {
                return file2.getAbsolutePath();
            }
        }
    }

    private static String validateFilename(String str, String str2) throws IOException {
        String canonicalPath = new File(str).getCanonicalPath();
        if (canonicalPath.startsWith(new File(str2).getCanonicalPath())) {
            return canonicalPath;
        }
        throw new IllegalStateException("File is outside extraction target directory.");
    }

    public static String readZipFile(String str, String str2) {
        return readZipFile(str, str2, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x008f, code lost:
        if (r5 != null) goto L_0x0078;
     */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0087 A[SYNTHETIC, Splitter:B:35:0x0087] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readZipFile(java.lang.String r5, java.lang.String r6, boolean r7) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r5)
            r1 = 0
            if (r0 != 0) goto L_0x0095
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L_0x000f
            goto L_0x0095
        L_0x000f:
            java.util.zip.ZipFile r0 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            r0.<init>(r5)     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            r3.<init>(r5)     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            java.util.zip.ZipInputStream r5 = new java.util.zip.ZipInputStream     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            r5.<init>(r2)     // Catch:{ Exception -> 0x008e, all -> 0x0083 }
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            r1.<init>()     // Catch:{ Exception -> 0x0081, all -> 0x007f }
        L_0x0028:
            java.util.zip.ZipEntry r2 = r5.getNextEntry()     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            if (r2 == 0) goto L_0x0078
            java.lang.String r3 = r2.getName()     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            java.lang.String r4 = "."
            java.lang.String r3 = validateFilename(r3, r4)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            boolean r4 = r2.isDirectory()     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            if (r4 == 0) goto L_0x003f
            goto L_0x0028
        L_0x003f:
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            if (r4 != 0) goto L_0x0028
            boolean r3 = r3.endsWith(r6)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            if (r3 == 0) goto L_0x0028
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            java.io.InputStream r0 = r0.getInputStream(r2)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            r3.<init>(r0)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            r6.<init>(r3)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
        L_0x0059:
            java.lang.String r0 = r6.readLine()     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            if (r0 == 0) goto L_0x006a
            r1.append(r0)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            if (r7 == 0) goto L_0x0059
            java.lang.String r0 = "\n"
            r1.append(r0)     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            goto L_0x0059
        L_0x006a:
            r6.close()     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            java.lang.String r6 = r1.toString()     // Catch:{ Exception -> 0x0081, all -> 0x007f }
            r5.closeEntry()     // Catch:{ IOException -> 0x0077 }
            r5.close()     // Catch:{ IOException -> 0x0077 }
        L_0x0077:
            return r6
        L_0x0078:
            r5.closeEntry()     // Catch:{ IOException -> 0x0092 }
            r5.close()     // Catch:{ IOException -> 0x0092 }
            goto L_0x0092
        L_0x007f:
            r6 = move-exception
            goto L_0x0085
        L_0x0081:
            goto L_0x008f
        L_0x0083:
            r6 = move-exception
            r5 = r1
        L_0x0085:
            if (r5 == 0) goto L_0x008d
            r5.closeEntry()     // Catch:{ IOException -> 0x008d }
            r5.close()     // Catch:{ IOException -> 0x008d }
        L_0x008d:
            throw r6
        L_0x008e:
            r5 = r1
        L_0x008f:
            if (r5 == 0) goto L_0x0092
            goto L_0x0078
        L_0x0092:
            java.lang.String r5 = ""
            return r5
        L_0x0095:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.readZipFile(java.lang.String, java.lang.String, boolean):java.lang.String");
    }

    public static void deleteFile(String str) {
        File file = new File(str);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File file2 : listFiles) {
                        if (file2 != null) {
                            deleteFile(file2.getAbsolutePath());
                        }
                    }
                }
            }
            file.delete();
        }
    }

    public static boolean isFile(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (file.isFile() && file.exists()) {
            z = true;
        }
        return z;
    }

    public static boolean fileIsExists(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }
        try {
            if (!new File(str).exists()) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean createFile(String str, boolean z) {
        boolean z2 = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        File parentFile = file.getParentFile();
        boolean mkdirs = !parentFile.exists() ? parentFile.mkdirs() : true;
        if (mkdirs) {
            if (z) {
                file.deleteOnExit();
            }
            if (!file.exists()) {
                try {
                    z2 = file.createNewFile();
                } catch (IOException unused) {
                }
                return z2;
            }
        }
        z2 = mkdirs;
        return z2;
    }

    public static String guessContentTypeFromUri(@NonNull Context context, @NonNull Uri uri) {
        return context.getContentResolver() != null ? context.getContentResolver().getType(uri) : "";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0083, code lost:
        if (r0 == null) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0085, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0089, code lost:
        if (r0 != null) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x008c, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static p021us.zoom.androidlib.data.FileInfo dumpImageMetaData(@androidx.annotation.NonNull android.content.Context r11, @androidx.annotation.NonNull android.net.Uri r12) {
        /*
            android.content.ContentResolver r0 = r11.getContentResolver()
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r1 = r12
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5, r6)
            r1 = 0
            if (r0 == 0) goto L_0x0089
            boolean r2 = r0.moveToFirst()     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            if (r2 == 0) goto L_0x0089
            java.lang.String r2 = "_display_name"
            int r2 = r0.getColumnIndex(r2)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            java.lang.String r2 = r0.getString(r2)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            java.lang.String r3 = "_size"
            int r3 = r0.getColumnIndex(r3)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            java.lang.String r4 = ""
            boolean r5 = r0.isNull(r3)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            if (r5 != 0) goto L_0x0033
            java.lang.String r4 = r0.getString(r3)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
        L_0x0033:
            android.content.ContentResolver r11 = r11.getContentResolver()     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            java.lang.String r9 = r11.getType(r12)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            java.lang.String r11 = p021us.zoom.androidlib.util.AndroidAppUtil.getFileExtendName(r2)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            boolean r12 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r11)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            if (r12 == 0) goto L_0x004b
            java.lang.String r11 = p021us.zoom.androidlib.util.AndroidAppUtil.getFileExtendNameFromMimType(r9)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            r10 = r11
            goto L_0x005a
        L_0x004b:
            r12 = 0
            int r3 = r2.length()     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            int r5 = r11.length()     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            int r3 = r3 - r5
            java.lang.String r2 = r2.substring(r12, r3)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            r10 = r11
        L_0x005a:
            us.zoom.androidlib.data.FileInfo r11 = new us.zoom.androidlib.data.FileInfo     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            boolean r12 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r2)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            if (r12 == 0) goto L_0x0066
            java.lang.String r12 = ""
            r6 = r12
            goto L_0x0067
        L_0x0066:
            r6 = r2
        L_0x0067:
            boolean r12 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r4)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            if (r12 == 0) goto L_0x0070
            r2 = 0
            goto L_0x0074
        L_0x0070:
            long r2 = java.lang.Long.parseLong(r4)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
        L_0x0074:
            r7 = r2
            r5 = r11
            r5.<init>(r6, r7, r9, r10)     // Catch:{ Exception -> 0x0082, all -> 0x007b }
            r1 = r11
            goto L_0x0089
        L_0x007b:
            r11 = move-exception
            if (r0 == 0) goto L_0x0081
            r0.close()
        L_0x0081:
            throw r11
        L_0x0082:
            if (r0 == 0) goto L_0x008c
        L_0x0085:
            r0.close()
            goto L_0x008c
        L_0x0089:
            if (r0 == 0) goto L_0x008c
            goto L_0x0085
        L_0x008c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.FileUtils.dumpImageMetaData(android.content.Context, android.net.Uri):us.zoom.androidlib.data.FileInfo");
    }

    public static boolean isInvalid(String str, Uri uri) {
        if (!OsUtil.isAtLeastQ()) {
            return StringUtil.isEmptyOrNull(str);
        }
        return StringUtil.isEmptyOrNull(str) && uri == null;
    }

    public static boolean isLocalPath(String str, String str2) {
        if (!OsUtil.isAtLeastQ()) {
            return str.startsWith(File.separator);
        }
        if (StringUtil.isEmptyOrNull(str) || !str.startsWith(File.separator) || !str.contains(str2)) {
            return false;
        }
        return true;
    }
}
