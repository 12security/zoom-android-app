package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CodeSnipptUtils {
    public static final String TAG = "CodeSnipptUtils";

    public static class CodeSnippetInfo {
        /* access modifiers changed from: private */
        public List<CharSequence> contents;
        /* access modifiers changed from: private */
        public int lineNo;
        /* access modifiers changed from: private */
        public String src;

        public int getLineNo() {
            return this.lineNo;
        }

        public List<CharSequence> getContents() {
            return this.contents;
        }

        public String getSrc() {
            return this.src;
        }
    }

    @NonNull
    private static String validateFilename(@NonNull String str, @NonNull String str2) throws IOException {
        String canonicalPath = new File(str).getCanonicalPath();
        if (canonicalPath.startsWith(new File(str2).getCanonicalPath())) {
            return canonicalPath;
        }
        throw new IllegalStateException("File is outside extraction target directory.");
    }

    public static String parseZipSnippetSrc(ZoomMessage zoomMessage, String str) {
        CodeSnippetInfo parseZipSnippet = parseZipSnippet(zoomMessage, str, -1);
        return parseZipSnippet != null ? parseZipSnippet.getSrc() : "";
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r10v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r5v0, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r10v2 */
    /* JADX WARNING: type inference failed for: r5v1 */
    /* JADX WARNING: type inference failed for: r10v3, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.BufferedReader] */
    /* JADX WARNING: type inference failed for: r10v4 */
    /* JADX WARNING: type inference failed for: r10v5 */
    /* JADX WARNING: type inference failed for: r5v2 */
    /* JADX WARNING: type inference failed for: r10v7 */
    /* JADX WARNING: type inference failed for: r10v9 */
    /* JADX WARNING: type inference failed for: r5v4 */
    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r10v12 */
    /* JADX WARNING: type inference failed for: r10v13 */
    /* JADX WARNING: type inference failed for: r10v14 */
    /* JADX WARNING: type inference failed for: r10v15 */
    /* JADX WARNING: type inference failed for: r10v16 */
    /* JADX WARNING: type inference failed for: r5v6 */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:42|43|44) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006c, code lost:
        r10 = r1.getInputStream(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r5 = new java.io.BufferedReader(new java.io.InputStreamReader(r10));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r6 = r5.readLine();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x007e, code lost:
        if (r6 == null) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0080, code lost:
        if (r11 >= 0) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0082, code lost:
        r4.append(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x008b, code lost:
        if (r6.endsWith("<br />") == false) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x008d, code lost:
        if (r2 >= r11) goto L_0x009b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x008f, code lost:
        r3.add(new android.text.SpannableString(android.text.Html.fromHtml(r6)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009b, code lost:
        if (r11 >= 0) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x009d, code lost:
        r4.append(com.zipow.videobox.view.p014mm.message.FontStyleHelper.SPLITOR);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a2, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a5, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        r10 = new com.zipow.videobox.util.CodeSnipptUtils.CodeSnippetInfo();
        com.zipow.videobox.util.CodeSnipptUtils.CodeSnippetInfo.access$002(r10, r2);
        com.zipow.videobox.util.CodeSnipptUtils.CodeSnippetInfo.access$102(r10, r3);
        com.zipow.videobox.util.CodeSnipptUtils.CodeSnippetInfo.access$202(r10, r4.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r9.closeEntry();
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c3, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c4, code lost:
        r11 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00c5, code lost:
        r0 = r5;
        r10 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r10 = r10;
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00c9, code lost:
        r11 = th;
        r0 = r0;
        r10 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00cb, code lost:
        r5 = 0;
        r10 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0123, code lost:
        if (r1 == null) goto L_0x0126;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:42:0x00c0 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:54:0x00d3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x00eb A[SYNTHETIC, Splitter:B:68:0x00eb] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00f2 A[SYNTHETIC, Splitter:B:72:0x00f2] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00f9 A[SYNTHETIC, Splitter:B:76:0x00f9] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0103 A[SYNTHETIC, Splitter:B:80:0x0103] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x010d A[SYNTHETIC, Splitter:B:87:0x010d] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0114 A[SYNTHETIC, Splitter:B:91:0x0114] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x011b A[SYNTHETIC, Splitter:B:95:0x011b] */
    /* JADX WARNING: Unknown variable types count: 5 */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.util.CodeSnipptUtils.CodeSnippetInfo parseZipSnippet(@androidx.annotation.Nullable com.zipow.videobox.ptapp.p013mm.ZoomMessage r9, java.lang.String r10, int r11) {
        /*
            r0 = 0
            if (r9 == 0) goto L_0x0127
            java.lang.String r1 = r9.getLocalFilePath()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x0127
            boolean r1 = android.text.TextUtils.isEmpty(r10)
            if (r1 == 0) goto L_0x0015
            goto L_0x0127
        L_0x0015:
            java.lang.String r9 = r9.getLocalFilePath()     // Catch:{ Exception -> 0x0107, all -> 0x00e5 }
            java.util.zip.ZipFile r1 = new java.util.zip.ZipFile     // Catch:{ Exception -> 0x0107, all -> 0x00e5 }
            r1.<init>(r9)     // Catch:{ Exception -> 0x0107, all -> 0x00e5 }
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x00e1, all -> 0x00dd }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00e1, all -> 0x00dd }
            r3.<init>(r9)     // Catch:{ Exception -> 0x00e1, all -> 0x00dd }
            r2.<init>(r3)     // Catch:{ Exception -> 0x00e1, all -> 0x00dd }
            java.util.zip.ZipInputStream r9 = new java.util.zip.ZipInputStream     // Catch:{ Exception -> 0x00e1, all -> 0x00dd }
            r9.<init>(r2)     // Catch:{ Exception -> 0x00e1, all -> 0x00dd }
            r2 = 0
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r3.<init>()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r4.<init>()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
        L_0x0038:
            java.util.zip.ZipEntry r5 = r9.getNextEntry()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            if (r5 == 0) goto L_0x00cd
            java.lang.String r6 = r5.getName()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            java.lang.String r7 = "."
            java.lang.String r6 = validateFilename(r6, r7)     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            boolean r7 = r5.isDirectory()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            if (r7 == 0) goto L_0x004f
            goto L_0x0038
        L_0x004f:
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            if (r7 != 0) goto L_0x0038
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r7.<init>()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            java.lang.String r8 = "_"
            r7.append(r8)     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r7.append(r10)     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            boolean r6 = r6.endsWith(r7)     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            if (r6 == 0) goto L_0x0038
            java.io.InputStream r10 = r1.getInputStream(r5)     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00cb, all -> 0x00c9 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00cb, all -> 0x00c9 }
            r6.<init>(r10)     // Catch:{ Exception -> 0x00cb, all -> 0x00c9 }
            r5.<init>(r6)     // Catch:{ Exception -> 0x00cb, all -> 0x00c9 }
        L_0x007a:
            java.lang.String r6 = r5.readLine()     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
            if (r6 == 0) goto L_0x00a5
            if (r11 >= 0) goto L_0x0085
            r4.append(r6)     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
        L_0x0085:
            java.lang.String r7 = "<br />"
            boolean r7 = r6.endsWith(r7)     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
            if (r7 == 0) goto L_0x007a
            if (r2 >= r11) goto L_0x009b
            android.text.Spanned r6 = android.text.Html.fromHtml(r6)     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
            android.text.SpannableString r7 = new android.text.SpannableString     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
            r7.<init>(r6)     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
            r3.add(r7)     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
        L_0x009b:
            if (r11 >= 0) goto L_0x00a2
            java.lang.String r6 = "\n"
            r4.append(r6)     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
        L_0x00a2:
            int r2 = r2 + 1
            goto L_0x007a
        L_0x00a5:
            r5.close()     // Catch:{ Exception -> 0x00c7, all -> 0x00c4 }
            com.zipow.videobox.util.CodeSnipptUtils$CodeSnippetInfo r10 = new com.zipow.videobox.util.CodeSnipptUtils$CodeSnippetInfo     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r10.<init>()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r10.lineNo = r2     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r10.contents = r3     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            java.lang.String r11 = r4.toString()     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r10.src = r11     // Catch:{ Exception -> 0x00db, all -> 0x00d8 }
            r9.closeEntry()     // Catch:{ IOException -> 0x00c0 }
            r9.close()     // Catch:{ IOException -> 0x00c0 }
        L_0x00c0:
            r1.close()     // Catch:{ IOException -> 0x00c3 }
        L_0x00c3:
            return r10
        L_0x00c4:
            r11 = move-exception
            r0 = r5
            goto L_0x00e9
        L_0x00c7:
            goto L_0x010b
        L_0x00c9:
            r11 = move-exception
            goto L_0x00e9
        L_0x00cb:
            r5 = r0
            goto L_0x010b
        L_0x00cd:
            r9.closeEntry()     // Catch:{ IOException -> 0x00d3 }
            r9.close()     // Catch:{ IOException -> 0x00d3 }
        L_0x00d3:
            r1.close()     // Catch:{ IOException -> 0x0126 }
            goto L_0x0126
        L_0x00d8:
            r11 = move-exception
            r10 = r0
            goto L_0x00e9
        L_0x00db:
            r10 = r0
            goto L_0x00e3
        L_0x00dd:
            r11 = move-exception
            r9 = r0
            r10 = r9
            goto L_0x00e9
        L_0x00e1:
            r9 = r0
            r10 = r9
        L_0x00e3:
            r5 = r10
            goto L_0x010b
        L_0x00e5:
            r11 = move-exception
            r9 = r0
            r10 = r9
            r1 = r10
        L_0x00e9:
            if (r0 == 0) goto L_0x00f0
            r0.close()     // Catch:{ IOException -> 0x00ef }
            goto L_0x00f0
        L_0x00ef:
        L_0x00f0:
            if (r10 == 0) goto L_0x00f7
            r10.close()     // Catch:{ IOException -> 0x00f6 }
            goto L_0x00f7
        L_0x00f6:
        L_0x00f7:
            if (r9 == 0) goto L_0x0101
            r9.closeEntry()     // Catch:{ IOException -> 0x0100 }
            r9.close()     // Catch:{ IOException -> 0x0100 }
            goto L_0x0101
        L_0x0100:
        L_0x0101:
            if (r1 == 0) goto L_0x0106
            r1.close()     // Catch:{ IOException -> 0x0106 }
        L_0x0106:
            throw r11
        L_0x0107:
            r9 = r0
            r10 = r9
            r1 = r10
            r5 = r1
        L_0x010b:
            if (r5 == 0) goto L_0x0112
            r5.close()     // Catch:{ IOException -> 0x0111 }
            goto L_0x0112
        L_0x0111:
        L_0x0112:
            if (r10 == 0) goto L_0x0119
            r10.close()     // Catch:{ IOException -> 0x0118 }
            goto L_0x0119
        L_0x0118:
        L_0x0119:
            if (r9 == 0) goto L_0x0123
            r9.closeEntry()     // Catch:{ IOException -> 0x0122 }
            r9.close()     // Catch:{ IOException -> 0x0122 }
            goto L_0x0123
        L_0x0122:
        L_0x0123:
            if (r1 == 0) goto L_0x0126
            goto L_0x00d3
        L_0x0126:
            return r0
        L_0x0127:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.CodeSnipptUtils.parseZipSnippet(com.zipow.videobox.ptapp.mm.ZoomMessage, java.lang.String, int):com.zipow.videobox.util.CodeSnipptUtils$CodeSnippetInfo");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r3.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.util.CodeSnipptUtils.CodeSnippetInfo parseSnippet(@androidx.annotation.NonNull java.lang.String r7, int r8) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            r1 = 0
            if (r0 != 0) goto L_0x008a
            if (r8 > 0) goto L_0x000b
            goto L_0x008a
        L_0x000b:
            java.io.File r0 = new java.io.File
            r0.<init>(r7)
            boolean r0 = r0.isFile()
            if (r0 != 0) goto L_0x0017
            return r1
        L_0x0017:
            r0 = 0
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x007e }
            r3.<init>(r7)     // Catch:{ Exception -> 0x007e }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x006c }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x006c }
            r4.<init>(r3)     // Catch:{ Throwable -> 0x006c }
            r7.<init>(r4)     // Catch:{ Throwable -> 0x006c }
        L_0x002c:
            java.lang.String r4 = r7.readLine()     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            if (r4 == 0) goto L_0x004b
            java.lang.String r5 = "<br />"
            boolean r5 = r4.endsWith(r5)     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            if (r5 == 0) goto L_0x002c
            if (r0 >= r8) goto L_0x0048
            android.text.Spanned r4 = android.text.Html.fromHtml(r4)     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            android.text.SpannableString r5 = new android.text.SpannableString     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            r5.<init>(r4)     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            r2.add(r5)     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
        L_0x0048:
            int r0 = r0 + 1
            goto L_0x002c
        L_0x004b:
            r7.close()     // Catch:{ Throwable -> 0x006c }
            r3.close()     // Catch:{ Exception -> 0x007e }
            goto L_0x007e
        L_0x0052:
            r8 = move-exception
            r4 = r1
            goto L_0x005b
        L_0x0055:
            r8 = move-exception
            throw r8     // Catch:{ all -> 0x0057 }
        L_0x0057:
            r4 = move-exception
            r6 = r4
            r4 = r8
            r8 = r6
        L_0x005b:
            if (r4 == 0) goto L_0x0066
            r7.close()     // Catch:{ Throwable -> 0x0061 }
            goto L_0x0069
        L_0x0061:
            r7 = move-exception
            r4.addSuppressed(r7)     // Catch:{ Throwable -> 0x006c }
            goto L_0x0069
        L_0x0066:
            r7.close()     // Catch:{ Throwable -> 0x006c }
        L_0x0069:
            throw r8     // Catch:{ Throwable -> 0x006c }
        L_0x006a:
            r7 = move-exception
            goto L_0x006f
        L_0x006c:
            r7 = move-exception
            r1 = r7
            throw r1     // Catch:{ all -> 0x006a }
        L_0x006f:
            if (r1 == 0) goto L_0x007a
            r3.close()     // Catch:{ Throwable -> 0x0075 }
            goto L_0x007d
        L_0x0075:
            r8 = move-exception
            r1.addSuppressed(r8)     // Catch:{ Exception -> 0x007e }
            goto L_0x007d
        L_0x007a:
            r3.close()     // Catch:{ Exception -> 0x007e }
        L_0x007d:
            throw r7     // Catch:{ Exception -> 0x007e }
        L_0x007e:
            com.zipow.videobox.util.CodeSnipptUtils$CodeSnippetInfo r7 = new com.zipow.videobox.util.CodeSnipptUtils$CodeSnippetInfo
            r7.<init>()
            r7.lineNo = r0
            r7.contents = r2
            return r7
        L_0x008a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.CodeSnipptUtils.parseSnippet(java.lang.String, int):com.zipow.videobox.util.CodeSnipptUtils$CodeSnippetInfo");
    }
}
