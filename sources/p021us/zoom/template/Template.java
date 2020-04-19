package p021us.zoom.template;

import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import p021us.zoom.androidlib.cache.IoUtils;

/* renamed from: us.zoom.template.Template */
public class Template {
    private static final String TAG = "Template";
    private int mStackSize = 0;
    private ArrayList<IStatement> mStatements = new ArrayList<>();

    public Template(String str) {
        if (str != null) {
            parse(preParse(str));
        }
    }

    private String preParse(String str) {
        BufferedReader bufferedReader;
        Throwable th;
        Throwable th2;
        Throwable th3;
        String readLine;
        try {
            bufferedReader = new BufferedReader(new StringReader(str));
            try {
                StringWriter stringWriter = new StringWriter();
                boolean z = true;
                do {
                    try {
                        readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            if (!z) {
                                stringWriter.append(10);
                            }
                            stringWriter.append(readLine);
                            if (readLine.matches("<[#|/][^<>]*>")) {
                                z = true;
                                continue;
                            } else {
                                z = false;
                                continue;
                            }
                        }
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        th3 = r9;
                        th2 = th5;
                    }
                } while (readLine != null);
                if (str.endsWith(FontStyleHelper.SPLITOR)) {
                    stringWriter.append(10);
                }
                String stringWriter2 = stringWriter.toString();
                stringWriter.close();
                bufferedReader.close();
                return stringWriter2;
                throw th;
                throw th2;
                if (th3 != null) {
                    stringWriter.close();
                } else {
                    stringWriter.close();
                }
                throw th2;
            } catch (Throwable th6) {
                th = th6;
                throw th;
            }
        } catch (Exception unused) {
            return "";
        } catch (Throwable th7) {
            th.addSuppressed(th7);
        }
    }

    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r10v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v1 */
    /* JADX WARNING: type inference failed for: r10v2 */
    /* JADX WARNING: type inference failed for: r10v3, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r10v5 */
    /* JADX WARNING: type inference failed for: r10v6 */
    /* JADX WARNING: type inference failed for: r0v5, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r10v7 */
    /* JADX WARNING: type inference failed for: r0v7, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r10v8, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r10v9 */
    /* JADX WARNING: type inference failed for: r10v10 */
    /* JADX WARNING: type inference failed for: r10v11, types: [java.io.StringWriter] */
    /* JADX WARNING: type inference failed for: r10v12, types: [java.io.StringWriter] */
    /* JADX WARNING: type inference failed for: r0v9, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r0v10, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v11 */
    /* JADX WARNING: type inference failed for: r10v13 */
    /* JADX WARNING: type inference failed for: r0v12 */
    /* JADX WARNING: type inference failed for: r4v7, types: [java.io.StringWriter] */
    /* JADX WARNING: type inference failed for: r10v15 */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r10v16, types: [java.lang.Throwable] */
    /* JADX WARNING: type inference failed for: r0v13 */
    /* JADX WARNING: type inference failed for: r10v17 */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r10v18 */
    /* JADX WARNING: type inference failed for: r4v12, types: [java.io.StringWriter] */
    /* JADX WARNING: type inference failed for: r10v20 */
    /* JADX WARNING: type inference failed for: r0v14 */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* JADX WARNING: type inference failed for: r0v16 */
    /* JADX WARNING: type inference failed for: r0v17 */
    /* JADX WARNING: type inference failed for: r0v18 */
    /* JADX WARNING: type inference failed for: r0v19 */
    /* JADX WARNING: type inference failed for: r10v21 */
    /* JADX WARNING: type inference failed for: r10v22 */
    /* JADX WARNING: type inference failed for: r10v23 */
    /* JADX WARNING: type inference failed for: r0v20 */
    /* JADX WARNING: type inference failed for: r10v24 */
    /* JADX WARNING: type inference failed for: r10v25 */
    /* JADX WARNING: type inference failed for: r10v26 */
    /* JADX WARNING: type inference failed for: r10v27 */
    /* JADX WARNING: type inference failed for: r10v28 */
    /* JADX WARNING: type inference failed for: r0v21 */
    /* JADX WARNING: type inference failed for: r0v22 */
    /* JADX WARNING: type inference failed for: r0v23 */
    /* JADX WARNING: type inference failed for: r0v24 */
    /* JADX WARNING: type inference failed for: r0v25 */
    /* JADX WARNING: type inference failed for: r4v13 */
    /* JADX WARNING: type inference failed for: r4v14 */
    /* JADX WARNING: type inference failed for: r4v15 */
    /* JADX WARNING: type inference failed for: r4v16 */
    /* JADX WARNING: type inference failed for: r4v17 */
    /* JADX WARNING: type inference failed for: r4v18 */
    /* JADX WARNING: type inference failed for: r4v19 */
    /* JADX WARNING: type inference failed for: r4v20 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r10v6
      assigns: []
      uses: []
      mth insns count: 117
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
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00cb A[SYNTHETIC, Splitter:B:52:0x00cb] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00d4 A[Catch:{ IOException -> 0x00e3, all -> 0x00d8 }] */
    /* JADX WARNING: Unknown variable types count: 21 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parse(java.lang.String r10) {
        /*
            r9 = this;
            r0 = 0
            java.io.StringReader r1 = new java.io.StringReader     // Catch:{ IOException -> 0x00e2, all -> 0x00da }
            r1.<init>(r10)     // Catch:{ IOException -> 0x00e2, all -> 0x00da }
            java.io.StringWriter r10 = new java.io.StringWriter     // Catch:{ Throwable -> 0x00c3, all -> 0x00c0 }
            r10.<init>()     // Catch:{ Throwable -> 0x00c3, all -> 0x00c0 }
        L_0x000b:
            int r2 = r1.read()     // Catch:{ Throwable -> 0x00be }
            r3 = -1
            if (r2 != r3) goto L_0x0021
            us.zoom.template.SimpleStringStatement r2 = new us.zoom.template.SimpleStringStatement     // Catch:{ Throwable -> 0x00be }
            java.lang.String r3 = r10.toString()     // Catch:{ Throwable -> 0x00be }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x00be }
            java.util.ArrayList<us.zoom.template.IStatement> r3 = r9.mStatements     // Catch:{ Throwable -> 0x00be }
            r3.add(r2)     // Catch:{ Throwable -> 0x00be }
            goto L_0x003c
        L_0x0021:
            r4 = 36
            if (r2 != r4) goto L_0x0070
            int r4 = r1.read()     // Catch:{ Throwable -> 0x00be }
            if (r4 != r3) goto L_0x003f
            r10.write(r2)     // Catch:{ Throwable -> 0x00be }
            us.zoom.template.SimpleStringStatement r2 = new us.zoom.template.SimpleStringStatement     // Catch:{ Throwable -> 0x00be }
            java.lang.String r3 = r10.toString()     // Catch:{ Throwable -> 0x00be }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x00be }
            java.util.ArrayList<us.zoom.template.IStatement> r3 = r9.mStatements     // Catch:{ Throwable -> 0x00be }
            r3.add(r2)     // Catch:{ Throwable -> 0x00be }
        L_0x003c:
            r0 = r10
            goto L_0x00b7
        L_0x003f:
            r5 = 123(0x7b, float:1.72E-43)
            if (r4 != r5) goto L_0x006c
            us.zoom.template.SimpleStringStatement r4 = new us.zoom.template.SimpleStringStatement     // Catch:{ Throwable -> 0x00be }
            java.lang.String r5 = r10.toString()     // Catch:{ Throwable -> 0x00be }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x00be }
            java.util.ArrayList<us.zoom.template.IStatement> r5 = r9.mStatements     // Catch:{ Throwable -> 0x00be }
            r5.add(r4)     // Catch:{ Throwable -> 0x00be }
            r10.close()     // Catch:{ Throwable -> 0x00be }
            java.io.StringWriter r4 = new java.io.StringWriter     // Catch:{ Throwable -> 0x00be }
            r4.<init>()     // Catch:{ Throwable -> 0x00be }
            us.zoom.template.VariableStatement r10 = r9.parseVariableStatement(r1)     // Catch:{ Throwable -> 0x0068, all -> 0x0064 }
            java.util.ArrayList<us.zoom.template.IStatement> r5 = r9.mStatements     // Catch:{ Throwable -> 0x0068, all -> 0x0064 }
            r5.add(r10)     // Catch:{ Throwable -> 0x0068, all -> 0x0064 }
            r10 = r4
            goto L_0x00b4
        L_0x0064:
            r2 = move-exception
            r10 = r4
            goto L_0x00c9
        L_0x0068:
            r10 = move-exception
            r0 = r10
            r10 = r4
            goto L_0x00c7
        L_0x006c:
            r10.write(r2)     // Catch:{ Throwable -> 0x00be }
            goto L_0x00b4
        L_0x0070:
            r4 = 60
            if (r2 != r4) goto L_0x00b1
            r4 = 4
            char[] r4 = new char[r4]     // Catch:{ Throwable -> 0x00be }
            int r5 = r1.read(r4)     // Catch:{ Throwable -> 0x00be }
            java.lang.String r6 = new java.lang.String     // Catch:{ Throwable -> 0x00be }
            r7 = 0
            r6.<init>(r4, r7, r5)     // Catch:{ Throwable -> 0x00be }
            java.lang.String r4 = "#if "
            boolean r4 = r6.equals(r4)     // Catch:{ Throwable -> 0x00be }
            if (r4 == 0) goto L_0x00aa
            us.zoom.template.SimpleStringStatement r4 = new us.zoom.template.SimpleStringStatement     // Catch:{ Throwable -> 0x00be }
            java.lang.String r5 = r10.toString()     // Catch:{ Throwable -> 0x00be }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x00be }
            java.util.ArrayList<us.zoom.template.IStatement> r5 = r9.mStatements     // Catch:{ Throwable -> 0x00be }
            r5.add(r4)     // Catch:{ Throwable -> 0x00be }
            r10.close()     // Catch:{ Throwable -> 0x00be }
            java.io.StringWriter r4 = new java.io.StringWriter     // Catch:{ Throwable -> 0x00be }
            r4.<init>()     // Catch:{ Throwable -> 0x00be }
            us.zoom.template.IfStatement r10 = r9.parseIfStatement(r1)     // Catch:{ Throwable -> 0x0068, all -> 0x0064 }
            java.util.ArrayList<us.zoom.template.IStatement> r5 = r9.mStatements     // Catch:{ Throwable -> 0x0068, all -> 0x0064 }
            r5.add(r10)     // Catch:{ Throwable -> 0x0068, all -> 0x0064 }
            r10 = r4
            goto L_0x00b4
        L_0x00aa:
            r10.write(r2)     // Catch:{ Throwable -> 0x00be }
            r10.write(r6)     // Catch:{ Throwable -> 0x00be }
            goto L_0x00b4
        L_0x00b1:
            r10.write(r2)     // Catch:{ Throwable -> 0x00be }
        L_0x00b4:
            if (r2 != r3) goto L_0x000b
            r0 = r10
        L_0x00b7:
            r1.close()     // Catch:{ IOException -> 0x00e2, all -> 0x00da }
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
            goto L_0x00e6
        L_0x00be:
            r0 = move-exception
            goto L_0x00c7
        L_0x00c0:
            r2 = move-exception
            r10 = r0
            goto L_0x00c9
        L_0x00c3:
            r10 = move-exception
            r8 = r0
            r0 = r10
            r10 = r8
        L_0x00c7:
            throw r0     // Catch:{ all -> 0x00c8 }
        L_0x00c8:
            r2 = move-exception
        L_0x00c9:
            if (r0 == 0) goto L_0x00d4
            r1.close()     // Catch:{ Throwable -> 0x00cf }
            goto L_0x00d7
        L_0x00cf:
            r1 = move-exception
            r0.addSuppressed(r1)     // Catch:{ IOException -> 0x00e3, all -> 0x00d8 }
            goto L_0x00d7
        L_0x00d4:
            r1.close()     // Catch:{ IOException -> 0x00e3, all -> 0x00d8 }
        L_0x00d7:
            throw r2     // Catch:{ IOException -> 0x00e3, all -> 0x00d8 }
        L_0x00d8:
            r0 = move-exception
            goto L_0x00de
        L_0x00da:
            r10 = move-exception
            r8 = r0
            r0 = r10
            r10 = r8
        L_0x00de:
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r10)
            throw r0
        L_0x00e2:
            r10 = r0
        L_0x00e3:
            p021us.zoom.androidlib.cache.IoUtils.closeSilently(r10)
        L_0x00e6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.template.Template.parse(java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0085, code lost:
        r3 = null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private p021us.zoom.template.IfStatement parseIfStatement(java.io.StringReader r11) {
        /*
            r10 = this;
            r0 = 0
            java.io.StringWriter r1 = new java.io.StringWriter     // Catch:{ IOException -> 0x00c2 }
            r1.<init>()     // Catch:{ IOException -> 0x00c2 }
        L_0x0006:
            int r2 = r11.read()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r3 = -1
            if (r2 == r3) goto L_0x008a
            r4 = 62
            r5 = 0
            r6 = 1
            if (r2 != r4) goto L_0x003a
            java.lang.String r2 = r1.toString()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r2 = r2.trim()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r10.mStackSize = r6     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String[] r11 = r10.readToEndIf(r11)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            if (r11 == 0) goto L_0x0085
            int r3 = r11.length     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            if (r3 <= r6) goto L_0x0085
            us.zoom.template.IfStatement r3 = new us.zoom.template.IfStatement     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            us.zoom.template.Template r4 = new us.zoom.template.Template     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r5 = r11[r5]     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            us.zoom.template.Template r5 = new us.zoom.template.Template     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r11 = r11[r6]     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r5.<init>(r11)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r3.<init>(r2, r4, r5)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            goto L_0x0086
        L_0x003a:
            r4 = 63
            if (r2 != r4) goto L_0x0080
            r4 = 2
            char[] r4 = new char[r4]     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            int r7 = r11.read(r4)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r8 = new java.lang.String     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r8.<init>(r4, r5, r7)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r4 = "?>"
            boolean r4 = r8.equals(r4)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            if (r4 == 0) goto L_0x0079
            java.lang.String r2 = r1.toString()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r2 = r2.trim()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r10.mStackSize = r6     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String[] r11 = r10.readToEndIf(r11)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            if (r11 == 0) goto L_0x0085
            int r3 = r11.length     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            if (r3 <= r6) goto L_0x0085
            us.zoom.template.IfStatement r3 = new us.zoom.template.IfStatement     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            us.zoom.template.Template r4 = new us.zoom.template.Template     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r5 = r11[r5]     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            us.zoom.template.Template r5 = new us.zoom.template.Template     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r11 = r11[r6]     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r5.<init>(r11)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r3.<init>(r2, r4, r5)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            goto L_0x0086
        L_0x0079:
            r1.write(r2)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r1.write(r8)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            goto L_0x0083
        L_0x0080:
            r1.write(r2)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
        L_0x0083:
            if (r2 != r3) goto L_0x0006
        L_0x0085:
            r3 = r0
        L_0x0086:
            r1.close()     // Catch:{ IOException -> 0x00c2 }
            return r3
        L_0x008a:
            java.lang.RuntimeException r11 = new java.lang.RuntimeException     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r2.<init>()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r3 = "\"??>\" or \">\" expected but not found after \""
            r2.append(r3)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r3 = r1.toString()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r2.append(r3)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r3 = "\""
            r2.append(r3)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            r11.<init>(r2)     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
            throw r11     // Catch:{ Throwable -> 0x00ad, all -> 0x00aa }
        L_0x00aa:
            r11 = move-exception
            r2 = r0
            goto L_0x00b3
        L_0x00ad:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x00af }
        L_0x00af:
            r2 = move-exception
            r9 = r2
            r2 = r11
            r11 = r9
        L_0x00b3:
            if (r2 == 0) goto L_0x00be
            r1.close()     // Catch:{ Throwable -> 0x00b9 }
            goto L_0x00c1
        L_0x00b9:
            r1 = move-exception
            r2.addSuppressed(r1)     // Catch:{ IOException -> 0x00c2 }
            goto L_0x00c1
        L_0x00be:
            r1.close()     // Catch:{ IOException -> 0x00c2 }
        L_0x00c1:
            throw r11     // Catch:{ IOException -> 0x00c2 }
        L_0x00c2:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.template.Template.parseIfStatement(java.io.StringReader):us.zoom.template.IfStatement");
    }

    private String[] readToEndIf(StringReader stringReader) {
        StringWriter stringWriter;
        try {
            stringWriter = new StringWriter();
            try {
                String[] strArr = new String[2];
                int i = 0;
                while (true) {
                    int read = stringReader.read();
                    if (read != -1) {
                        if (read == 60) {
                            char[] cArr = new char[5];
                            String str = new String(cArr, 0, stringReader.read(cArr));
                            if (str.startsWith("#if ")) {
                                stringWriter.write(read);
                                stringWriter.write(str);
                                this.mStackSize++;
                            } else if (str.equals("/#if>")) {
                                if (this.mStackSize > 1) {
                                    stringWriter.write(read);
                                    stringWriter.write(str);
                                    this.mStackSize--;
                                } else {
                                    strArr[i] = stringWriter.toString();
                                    IoUtils.closeSilently(stringWriter);
                                    return strArr;
                                }
                            } else if (str.equals("#else")) {
                                read = stringReader.read();
                                if (read == 62) {
                                    if (this.mStackSize <= 1) {
                                        int i2 = i + 1;
                                        strArr[i] = stringWriter.toString();
                                        stringWriter.close();
                                        stringWriter = new StringWriter();
                                        i = i2;
                                    }
                                }
                                stringWriter.write(read);
                                stringWriter.write(str);
                            } else {
                                stringWriter.write(read);
                                stringWriter.write(str);
                            }
                        } else {
                            stringWriter.write(read);
                        }
                        if (read == -1) {
                            stringWriter.close();
                            StringBuilder sb = new StringBuilder();
                            sb.append("\"</#if>\" expected but not found after \"");
                            sb.append(stringWriter.toString());
                            sb.append("\"");
                            throw new RuntimeException(sb.toString());
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("\"<#if>\" expected but not found after \"");
                        sb2.append(stringWriter.toString());
                        sb2.append("\"");
                        throw new RuntimeException(sb2.toString());
                    }
                }
            } catch (IOException unused) {
                IoUtils.closeSilently(stringWriter);
                return null;
            } catch (Throwable th) {
                th = th;
                IoUtils.closeSilently(stringWriter);
                throw th;
            }
        } catch (IOException unused2) {
            stringWriter = null;
            IoUtils.closeSilently(stringWriter);
            return null;
        } catch (Throwable th2) {
            th = th2;
            stringWriter = null;
            IoUtils.closeSilently(stringWriter);
            throw th;
        }
    }

    private VariableStatement parseVariableStatement(StringReader stringReader) {
        Throwable th;
        Throwable th2;
        VariableStatement variableStatement;
        try {
            StringWriter stringWriter = new StringWriter();
            while (true) {
                try {
                    int read = stringReader.read();
                    if (read != -1) {
                        if (read != 125) {
                            stringWriter.write(read);
                            if (read == -1) {
                                variableStatement = null;
                                break;
                            }
                        } else {
                            variableStatement = new VariableStatement(stringWriter.toString().trim());
                            break;
                        }
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("\"}\" expected but not found after \"");
                        sb.append(stringWriter.toString());
                        sb.append("\"");
                        throw new RuntimeException(sb.toString());
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    th2 = r7;
                    th = th4;
                }
            }
            stringWriter.close();
            return variableStatement;
            throw th;
            if (th2 != null) {
                try {
                    stringWriter.close();
                } catch (Throwable th5) {
                    th2.addSuppressed(th5);
                }
            } else {
                stringWriter.close();
            }
            throw th;
        } catch (IOException unused) {
            return null;
        }
    }

    public String format(Map<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = this.mStatements.iterator();
        while (it.hasNext()) {
            stringBuffer.append(((IStatement) it.next()).format(map));
        }
        return stringBuffer.toString();
    }
}
