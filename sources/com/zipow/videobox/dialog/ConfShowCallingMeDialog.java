package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ConfShowCallingMeDialog extends ZMDialogFragment {
    public static final String ARGS_PHONE_NUMBER = "args_phone_number";

    public static void showConfShowCallingMeDialog(ZMActivity zMActivity, @NonNull String str) {
        ConfShowCallingMeDialog confShowCallingMeDialog = new ConfShowCallingMeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_PHONE_NUMBER, str);
        confShowCallingMeDialog.setArguments(bundle);
        confShowCallingMeDialog.show(zMActivity.getSupportFragmentManager(), ConfShowCallingMeDialog.class.getName());
    }

    public static void dismiss(@NonNull FragmentManager fragmentManager) {
        ConfShowCallingMeDialog confShowCallingMeDialog = getConfShowCallingMeDialog(fragmentManager);
        if (confShowCallingMeDialog != null) {
            confShowCallingMeDialog.dismiss();
        }
    }

    @Nullable
    private static ConfShowCallingMeDialog getConfShowCallingMeDialog(FragmentManager fragmentManager) {
        return (ConfShowCallingMeDialog) fragmentManager.findFragmentByTag(ConfShowCallingMeDialog.class.getName());
    }

    public ConfShowCallingMeDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        if (((ZMActivity) getActivity()) == null) {
            return createEmptyDialog();
        }
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        String string = arguments.getString(ARGS_PHONE_NUMBER);
        if (StringUtil.isEmptyOrNull(string)) {
            return createEmptyDialog();
        }
        View createContent = createContent(string);
        if (createContent == null) {
            return createEmptyDialog();
        }
        return new Builder(getActivity()).setView(createContent).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfShowCallingMeDialog.this.cancelAutoCall();
            }
        }).create();
    }

    private View createContent(@NonNull String str) {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_dialog_auto_calling, null);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtPhoneNumber)).setText(str);
        return inflate;
    }

    /* access modifiers changed from: private */
    public void cancelAutoCall() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            confStatusObj.hangUp();
        }
        ConfMgr.getInstance().getConfDataHelper().setmIsAutoCalledOrCanceledCall(true);
    }

    public void dismiss() {
        super.dismiss();
        ConfMgr.getInstance().getConfDataHelper().setmIsNeedHandleCallOutStatusChangedInMeeting(false);
    }
}
