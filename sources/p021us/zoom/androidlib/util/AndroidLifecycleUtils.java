package p021us.zoom.androidlib.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import androidx.fragment.app.Fragment;
import p021us.zoom.androidlib.app.ZMActivity;

/* renamed from: us.zoom.androidlib.util.AndroidLifecycleUtils */
public class AndroidLifecycleUtils {
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return canLoadImage((Activity) fragment.getActivity());
    }

    public static boolean canLoadImage(Context context) {
        if (context != null && (context instanceof Activity)) {
            return canLoadImage((Activity) context);
        }
        return true;
    }

    public static boolean canLoadImage(Activity activity) {
        if (activity == null) {
            return true;
        }
        if (activity instanceof ZMActivity) {
            return !((ZMActivity) activity).isDestroyed();
        }
        if ((VERSION.SDK_INT >= 17 && activity.isDestroyed()) || activity.isFinishing()) {
            return false;
        }
        return true;
    }
}
