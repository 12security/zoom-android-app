package com.zipow.videobox.util;

import android.content.Context;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.AppUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import p021us.zoom.androidlib.util.StringUtil;

public class RawFileUtil {
    private static final String TAG = "RawFileUtil";

    public static boolean installRawFileToLocal(Context context, int i, String str, boolean z) {
        if (context == null) {
            return false;
        }
        String dataPath = AppUtil.getDataPath(true, true);
        if (StringUtil.isEmptyOrNull(dataPath)) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dataPath);
        sb.append(File.separator);
        sb.append(str);
        return copyFromRawToLocal(context, i, sb.toString(), z);
    }

    public static boolean copyFromRawToLocal(@Nullable Context context, int i, @Nullable String str, boolean z) {
        InputStream openRawResource;
        FileOutputStream fileOutputStream;
        Throwable th;
        Throwable th2;
        if (context == null || i == 0 || str == null) {
            return false;
        }
        File file = new File(str);
        if (z && file.exists() && file.length() > 0) {
            return false;
        }
        if (!file.exists() || file.length() == 0) {
            try {
                openRawResource = context.getResources().openRawResource(i);
                fileOutputStream = new FileOutputStream(file);
                try {
                    byte[] bArr = new byte[openRawResource.available()];
                    int read = openRawResource.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                        fileOutputStream.close();
                        if (openRawResource != null) {
                            openRawResource.close();
                        }
                        return true;
                    }
                    fileOutputStream.close();
                    if (openRawResource != null) {
                        openRawResource.close();
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    th2 = r8;
                    th = th4;
                }
            } catch (Exception unused) {
            } catch (Throwable th5) {
                r6.addSuppressed(th5);
            }
        }
        return false;
        throw th;
        if (th2 != null) {
            try {
                fileOutputStream.close();
            } catch (Throwable th6) {
                th2.addSuppressed(th6);
            }
        } else {
            fileOutputStream.close();
        }
        throw th;
        throw th;
    }
}
