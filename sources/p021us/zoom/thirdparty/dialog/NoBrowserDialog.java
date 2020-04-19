package p021us.zoom.thirdparty.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMDialogFragment.ZMDialogParam;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.thirdparty.login.C4538R;

/* renamed from: us.zoom.thirdparty.dialog.NoBrowserDialog */
public class NoBrowserDialog extends ZMDialogFragment {
    private static final String TAG = "NoBrowserDialog";
    public static final int TYPE_GO_TO_STORE = 2;
    public static final int TYPE_NORMAL = 1;
    private ZMDialogParam param;

    public static void showDialog(FragmentManager fragmentManager, String str, int i) {
        ZMDialogParam zMDialogParam = new ZMDialogParam(i);
        if (shouldShow(fragmentManager, str, zMDialogParam)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ZMDialogFragment.PARAMS, zMDialogParam);
            NoBrowserDialog noBrowserDialog = new NoBrowserDialog();
            noBrowserDialog.setArguments(bundle);
            noBrowserDialog.showNow(fragmentManager, str);
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.param = (ZMDialogParam) arguments.getParcelable(ZMDialogFragment.PARAMS);
        ZMDialogParam zMDialogParam = this.param;
        if (zMDialogParam == null) {
            return createEmptyDialog();
        }
        Builder builder = null;
        if (zMDialogParam.intParam == 1) {
            builder = new Builder(getActivity()).setTitle(C4538R.string.zm_alert_no_browser_title_100635).setMessage(C4538R.string.zm_alert_no_browser_message_100635).setPositiveButton(C4538R.string.zm_btn_ok, (OnClickListener) null);
        } else if (this.param.intParam == 2) {
            builder = new Builder(getActivity()).setTitle(C4538R.string.zm_alert_no_browser_title_100635).setMessage(C4538R.string.zm_alert_no_browser_message_100635).setNegativeButton(C4538R.string.zm_btn_cancel, (OnClickListener) null).setPositiveButton(C4538R.string.zm_alert_no_browser_btn_go_to_download_100635, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    NoBrowserDialog.this.downloadBrowser();
                }
            });
        }
        return builder == null ? createEmptyDialog() : builder.create();
    }

    /* access modifiers changed from: private */
    public void downloadBrowser() {
        String str = "com.android.chrome";
        if (getActivity() != null) {
            try {
                FragmentActivity activity = getActivity();
                StringBuilder sb = new StringBuilder();
                sb.append("market://details?id=");
                sb.append(str);
                activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
            } catch (ActivityNotFoundException unused) {
            }
        }
    }
}
