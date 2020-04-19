package p021us.zoom.androidlib.utils;

import android.content.Context;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.utils.ZmAppUtils */
public class ZmAppUtils {
    @Nullable
    public static String getHostPackageName(@Nullable Context context) {
        if (context == null) {
            return null;
        }
        String string = ResourcesUtil.getString(context, C4409R.string.zm_config_sdk_host_app_package_name);
        return StringUtil.isEmptyOrNull(string) ? context.getPackageName() : string;
    }
}
