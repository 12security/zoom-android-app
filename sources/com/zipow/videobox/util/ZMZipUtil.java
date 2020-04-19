package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZMZipUtil {
    private static final int BUFF_SIZE = 2048;
    private static final String TAG = "ZMZipUtil";

    @NonNull
    private static String validateFilename(@NonNull String str, @NonNull String str2) throws IOException {
        String canonicalPath = new File(str).getCanonicalPath();
        if (canonicalPath.startsWith(new File(str2).getCanonicalPath())) {
            return canonicalPath;
        }
        throw new IllegalStateException("File is outside extraction target directory.");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005b, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006a, code lost:
        if (r3 == null) goto L_0x006d;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005b A[ExcHandler: all (th java.lang.Throwable), Splitter:B:7:0x001b] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0061 A[SYNTHETIC, Splitter:B:28:0x0061] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean zipFile(@androidx.annotation.Nullable java.io.File[] r7, @androidx.annotation.Nullable java.lang.String r8) {
        /*
            r0 = 0
            if (r7 == 0) goto L_0x006e
            boolean r1 = android.text.TextUtils.isEmpty(r8)
            if (r1 == 0) goto L_0x000a
            goto L_0x006e
        L_0x000a:
            r1 = 0
            r2 = 1
            java.util.zip.ZipOutputStream r3 = new java.util.zip.ZipOutputStream     // Catch:{ Exception -> 0x0068, all -> 0x005d }
            java.io.BufferedOutputStream r4 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0068, all -> 0x005d }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0068, all -> 0x005d }
            r5.<init>(r8)     // Catch:{ Exception -> 0x0068, all -> 0x005d }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0068, all -> 0x005d }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0068, all -> 0x005d }
            int r8 = r7.length     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            r1 = 0
        L_0x001d:
            if (r1 >= r8) goto L_0x0051
            r4 = r7[r1]     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            if (r4 == 0) goto L_0x004e
            boolean r5 = r4.exists()     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            if (r5 != 0) goto L_0x002a
            goto L_0x004e
        L_0x002a:
            boolean r5 = r4.isDirectory()     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            if (r5 == 0) goto L_0x0049
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            r5.<init>()     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            java.lang.String r6 = r4.getName()     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            r5.append(r6)     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            java.lang.String r6 = java.io.File.separator     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            r5.append(r6)     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            recursionZip(r3, r4, r5)     // Catch:{ Exception -> 0x0069, all -> 0x005b }
            goto L_0x004e
        L_0x0049:
            java.lang.String r5 = ""
            recursionZip(r3, r4, r5)     // Catch:{ Exception -> 0x0069, all -> 0x005b }
        L_0x004e:
            int r1 = r1 + 1
            goto L_0x001d
        L_0x0051:
            r3.flush()     // Catch:{ Exception -> 0x006a, all -> 0x005b }
        L_0x0054:
            r3.closeEntry()     // Catch:{ IOException -> 0x006d }
            r3.close()     // Catch:{ IOException -> 0x006d }
            goto L_0x006d
        L_0x005b:
            r7 = move-exception
            goto L_0x005f
        L_0x005d:
            r7 = move-exception
            r3 = r1
        L_0x005f:
            if (r3 == 0) goto L_0x0067
            r3.closeEntry()     // Catch:{ IOException -> 0x0067 }
            r3.close()     // Catch:{ IOException -> 0x0067 }
        L_0x0067:
            throw r7
        L_0x0068:
            r3 = r1
        L_0x0069:
            r2 = 0
        L_0x006a:
            if (r3 == 0) goto L_0x006d
            goto L_0x0054
        L_0x006d:
            return r2
        L_0x006e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ZMZipUtil.zipFile(java.io.File[], java.lang.String):boolean");
    }

    private static void recursionZip(@NonNull ZipOutputStream zipOutputStream, @NonNull File file, @NonNull String str) throws Exception {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2 != null) {
                        if (file2.isDirectory()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(file.getName());
                            sb.append(File.separator);
                            sb.append(file2.getName());
                            sb.append(File.separator);
                            str = sb.toString();
                            recursionZip(zipOutputStream, file2, str);
                        } else {
                            recursionZip(zipOutputStream, file2, str);
                        }
                    }
                }
            }
        } else {
            byte[] bArr = new byte[2048];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(file.getName());
            zipOutputStream.putNextEntry(new ZipEntry(sb2.toString()));
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                zipOutputStream.write(bArr, 0, read);
            }
            bufferedInputStream.close();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0093 A[SYNTHETIC, Splitter:B:33:0x0093] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x009e A[Catch:{ IOException -> 0x009a }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00a9 A[SYNTHETIC, Splitter:B:45:0x00a9] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b1 A[Catch:{ IOException -> 0x0088 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean unZipFile(@androidx.annotation.NonNull java.lang.String r7, @androidx.annotation.NonNull java.lang.String r8) {
        /*
            java.lang.String r7 = createSeparator(r7)
            r0 = 0
            r1 = 0
            java.util.zip.ZipInputStream r2 = new java.util.zip.ZipInputStream     // Catch:{ IOException -> 0x00a6, all -> 0x008f }
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x00a6, all -> 0x008f }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ IOException -> 0x00a6, all -> 0x008f }
            r4.<init>(r8)     // Catch:{ IOException -> 0x00a6, all -> 0x008f }
            r3.<init>(r4)     // Catch:{ IOException -> 0x00a6, all -> 0x008f }
            r2.<init>(r3)     // Catch:{ IOException -> 0x00a6, all -> 0x008f }
            r8 = 2048(0x800, float:2.87E-42)
            byte[] r8 = new byte[r8]     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
        L_0x0019:
            java.util.zip.ZipEntry r3 = r2.getNextEntry()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            if (r3 == 0) goto L_0x007b
            java.lang.String r4 = r3.getName()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            java.lang.String r5 = "."
            java.lang.String r4 = validateFilename(r4, r5)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            createSubFolders(r4, r7)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            boolean r3 = r3.isDirectory()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            if (r3 == 0) goto L_0x004a
            java.io.File r3 = new java.io.File     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r5.<init>()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r5.append(r7)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r5.append(r4)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            java.lang.String r4 = r5.toString()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r3.<init>(r4)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r3.mkdirs()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            goto L_0x0019
        L_0x004a:
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r6.<init>()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r6.append(r7)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r6.append(r4)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            java.lang.String r4 = r6.toString()     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r5.<init>(r4)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
            r3.<init>(r5)     // Catch:{ IOException -> 0x00a7, all -> 0x008d }
        L_0x0063:
            int r1 = r2.read(r8)     // Catch:{ IOException -> 0x0079, all -> 0x0076 }
            r4 = -1
            if (r1 == r4) goto L_0x006e
            r3.write(r8, r0, r1)     // Catch:{ IOException -> 0x0079, all -> 0x0076 }
            goto L_0x0063
        L_0x006e:
            r3.flush()     // Catch:{ IOException -> 0x0079, all -> 0x0076 }
            r3.close()     // Catch:{ IOException -> 0x0079, all -> 0x0076 }
            r1 = r3
            goto L_0x0019
        L_0x0076:
            r7 = move-exception
            r1 = r3
            goto L_0x0091
        L_0x0079:
            r1 = r3
            goto L_0x00a7
        L_0x007b:
            r0 = 1
            r2.closeEntry()     // Catch:{ IOException -> 0x0088 }
            r2.close()     // Catch:{ IOException -> 0x0088 }
            if (r1 == 0) goto L_0x00b4
            r1.close()     // Catch:{ IOException -> 0x0088 }
            goto L_0x00b4
        L_0x0088:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x00b4
        L_0x008d:
            r7 = move-exception
            goto L_0x0091
        L_0x008f:
            r7 = move-exception
            r2 = r1
        L_0x0091:
            if (r2 == 0) goto L_0x009c
            r2.closeEntry()     // Catch:{ IOException -> 0x009a }
            r2.close()     // Catch:{ IOException -> 0x009a }
            goto L_0x009c
        L_0x009a:
            r8 = move-exception
            goto L_0x00a2
        L_0x009c:
            if (r1 == 0) goto L_0x00a5
            r1.close()     // Catch:{ IOException -> 0x009a }
            goto L_0x00a5
        L_0x00a2:
            r8.printStackTrace()
        L_0x00a5:
            throw r7
        L_0x00a6:
            r2 = r1
        L_0x00a7:
            if (r2 == 0) goto L_0x00af
            r2.closeEntry()     // Catch:{ IOException -> 0x0088 }
            r2.close()     // Catch:{ IOException -> 0x0088 }
        L_0x00af:
            if (r1 == 0) goto L_0x00b4
            r1.close()     // Catch:{ IOException -> 0x0088 }
        L_0x00b4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ZMZipUtil.unZipFile(java.lang.String, java.lang.String):boolean");
    }

    private static void createSubFolders(@NonNull String str, @NonNull String str2) {
        String[] split = str.split("/");
        if (split.length > 1) {
            for (int i = 0; i < split.length - 1; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(split[i]);
                sb.append("/");
                str2 = sb.toString();
                File file = new File(str2);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        }
    }

    private static String createSeparator(@NonNull String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (str.endsWith("/")) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append('/');
        return sb.toString();
    }
}
