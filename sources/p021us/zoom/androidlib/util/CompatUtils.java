package p021us.zoom.androidlib.util;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Locale.Category;

/* renamed from: us.zoom.androidlib.util.CompatUtils */
public class CompatUtils {
    public static final String ARG_ISSTARTFOREGROUNDSERVICE = "isStartForegroundService";

    public static Charset getStardardCharSetUTF8() {
        if (OsUtil.isAtLeastKLP()) {
            return StandardCharsets.UTF_8;
        }
        return Charset.forName("UTF-8");
    }

    public static Locale getLocalDefault() {
        if (OsUtil.isAtLeastN()) {
            return Locale.getDefault(Category.DISPLAY);
        }
        return Locale.getDefault();
    }

    public static int getSystemAlertWindowType(int i) {
        if (!OsUtil.isAtLeastO() || (i != 2002 && i != 2007 && i != 2003 && i != 2006 && i != 2010 && i != 2005)) {
            return i;
        }
        return 2038;
    }

    public static void startService(@NonNull Context context, @NonNull Intent intent, boolean z, boolean z2) {
        if (intent.getComponent() != null) {
            try {
                if (!OsUtil.isAtLeastO() || !z) {
                    intent.putExtra(ARG_ISSTARTFOREGROUNDSERVICE, false);
                    context.startService(intent);
                } else {
                    intent.putExtra(ARG_ISSTARTFOREGROUNDSERVICE, true);
                    context.startForegroundService(intent);
                }
            } catch (Exception unused) {
            }
        }
    }
}
