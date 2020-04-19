package p021us.zoom.videomeetings;

import android.content.Context;
import android.util.Log;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.BuildTarget;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.videomeetings.ZMBuildConfig */
public final class ZMBuildConfig {
    public static final int BUILD_TARGET;

    static {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        int i = 0;
        if (globalContext != null) {
            String string = ResourcesUtil.getString(globalContext, C4558R.string.zm_config_build_target);
            if (StringUtil.isEmptyOrNull(string)) {
                string = "TARGET_ZOOM";
            }
            try {
                i = BuildTarget.class.getField(string).getInt(BuildTarget.class);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("parse build target failed. value=");
                sb.append(string);
                Log.e("ZMBuildConfig", sb.toString(), e);
                Runtime.getRuntime().exit(0);
            }
        }
        BUILD_TARGET = i;
    }
}
