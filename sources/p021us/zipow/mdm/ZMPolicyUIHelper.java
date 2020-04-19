package p021us.zipow.mdm;

import android.content.Context;
import android.view.View;
import android.widget.CheckedTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.util.ZMPolicyDataHelper;
import com.zipow.videobox.util.ZMPolicyDataHelper.BooleanQueryResult;
import com.zipow.videobox.util.ZMUtils;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: us.zipow.mdm.ZMPolicyUIHelper */
public class ZMPolicyUIHelper {
    private static final String TAG = "us.zipow.mdm.ZMPolicyUIHelper";

    public static void applyNotOpenCamera(@NonNull CheckedTextView checkedTextView, @Nullable View view) {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider != null) {
            boolean z = !zoomMdmPolicyProvider.isLockPolicy(24);
            if (view != null) {
                view.setEnabled(z);
            }
            checkedTextView.setEnabled(z);
            checkedTextView.setChecked(zoomMdmPolicyProvider.queryBooleanPolicy(24));
        }
    }

    public static void applyAutoHideNoVideoUsers(@NonNull CheckedTextView checkedTextView, @Nullable View view) {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider != null) {
            boolean z = !zoomMdmPolicyProvider.isLockPolicy(38);
            if (view != null) {
                view.setEnabled(z);
            }
            checkedTextView.setEnabled(z);
            checkedTextView.setChecked(!zoomMdmPolicyProvider.queryBooleanPolicy(38));
        }
    }

    public static void applyShowJoinLeaveTip(@NonNull CheckedTextView checkedTextView, @Nullable View view) {
        BooleanQueryResult queryBooleanPolicy = ZMPolicyDataHelper.getInstance().queryBooleanPolicy(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip);
        if (queryBooleanPolicy.isSuccess()) {
            boolean z = !queryBooleanPolicy.isMandatory();
            if (view != null) {
                view.setEnabled(z);
            }
            checkedTextView.setEnabled(z);
            checkedTextView.setChecked(queryBooleanPolicy.getResult());
        }
    }

    public static void applyOriginalAudio(@NonNull CheckedTextView checkedTextView, @Nullable View view) {
        BooleanQueryResult IsOriginalSoundChangable = PTSettingHelper.IsOriginalSoundChangable();
        if (IsOriginalSoundChangable != null && IsOriginalSoundChangable.isSuccess()) {
            boolean z = !IsOriginalSoundChangable.isMandatory();
            if (view != null) {
                view.setEnabled(z);
            }
            checkedTextView.setEnabled(z);
            checkedTextView.setChecked(IsOriginalSoundChangable.getResult());
        }
    }

    public static boolean getAutoCopyLink() {
        BooleanQueryResult queryBooleanPolicy = ZMPolicyDataHelper.getInstance().queryBooleanPolicy(DummyPolicyIDType.zPolicy_ShowInviteUrl);
        if (!queryBooleanPolicy.isSuccess()) {
            return false;
        }
        return queryBooleanPolicy.getResult();
    }

    public static boolean isLockedAutoHideNoVideoUsers() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        return zoomMdmPolicyProvider != null && zoomMdmPolicyProvider.isLockPolicy(38);
    }

    public static boolean isDisableDirectShare() {
        BooleanQueryResult queryBooleanPolicy = ZMPolicyDataHelper.getInstance().queryBooleanPolicy(41);
        if (queryBooleanPolicy.isSuccess()) {
            return queryBooleanPolicy.getResult();
        }
        return false;
    }

    public static boolean isComputerAudioDisabled() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        return zoomMdmPolicyProvider != null && zoomMdmPolicyProvider.hasPolicy(22) && zoomMdmPolicyProvider.queryBooleanPolicy(22);
    }

    public static boolean shouldAutoShowSsoLogin() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        boolean z = false;
        if (zoomMdmPolicyProvider == null) {
            return false;
        }
        if (!zoomMdmPolicyProvider.queryBooleanPolicy(8) && zoomMdmPolicyProvider.queryBooleanPolicy(4)) {
            z = true;
        }
        return z;
    }

    public static boolean isForceSsoLogin() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        boolean z = false;
        if (zoomMdmPolicyProvider == null) {
            return false;
        }
        if (shouldAutoShowSsoLogin() && zoomMdmPolicyProvider.isPolicyLocked(4)) {
            z = true;
        }
        return z;
    }

    public static boolean isSupportSsoLogin(@NonNull Context context) {
        boolean z = false;
        if (!ResourcesUtil.getBoolean(context, C4558R.bool.zm_config_enable_sso_login, true)) {
            return false;
        }
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider == null || !zoomMdmPolicyProvider.queryBooleanPolicy(8)) {
            z = true;
        }
        return z;
    }

    public static boolean isSupportGoogleLogin(@NonNull Context context) {
        boolean z = true;
        if (!ResourcesUtil.getBoolean(context, C4558R.bool.zm_config_enable_google_login, true) || ZMUtils.isItuneApp(context)) {
            return false;
        }
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider != null && zoomMdmPolicyProvider.queryBooleanPolicy(6)) {
            z = false;
        }
        return z;
    }

    public static boolean isSupportFaceBookLogin(@NonNull Context context) {
        boolean z = false;
        if (!ResourcesUtil.getBoolean(context, C4558R.bool.zm_config_enable_facebook_login, true)) {
            return false;
        }
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider == null || !zoomMdmPolicyProvider.queryBooleanPolicy(7)) {
            z = true;
        }
        return z;
    }

    public static boolean isShowConfirmDialogWhenWebJoin() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider == null) {
            return false;
        }
        return zoomMdmPolicyProvider.queryBooleanPolicy(47);
    }

    public static boolean isDisableDeviceAudio() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = ZMMdmManager.getInstance().getZoomMdmPolicyProvider();
        if (zoomMdmPolicyProvider == null) {
            return false;
        }
        return zoomMdmPolicyProvider.queryBooleanPolicy(22);
    }
}
