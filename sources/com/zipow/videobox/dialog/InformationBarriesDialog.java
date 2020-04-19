package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class InformationBarriesDialog extends ZMDialogFragment {
    private static final String MSG_BODY = "msg_body";
    private static boolean mNeedFinishParent;

    public static void show(@Nullable Context context, @StringRes int i, boolean z) {
        if (context != null) {
            show(context, context.getString(i), z);
        }
    }

    public static void show(@Nullable Context context, @StringRes int i) {
        show(context, i, true);
    }

    public static void show(@Nullable Context context, @NonNull String str) {
        show(context, str, true);
    }

    public static void show(@Nullable Context context, @Nullable String str, boolean z) {
        mNeedFinishParent = z;
        if (str != null) {
            FragmentManager fragmentManager = null;
            if (context instanceof ZMActivity) {
                fragmentManager = ((ZMActivity) context).getSupportFragmentManager();
            }
            if (fragmentManager != null) {
                InformationBarriesDialog informationBarriesDialog = new InformationBarriesDialog();
                Bundle bundle = new Bundle();
                bundle.putString(MSG_BODY, str);
                informationBarriesDialog.setArguments(bundle);
                informationBarriesDialog.show(fragmentManager, InformationBarriesDialog.class.getName());
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        String string = arguments.getString(MSG_BODY);
        final FragmentActivity activity = getActivity();
        return new Builder(activity).setCancelable(false).setTitle(C4558R.string.zm_mm_information_barries_dialog_title_115072).setMessage(string).setNegativeButton(C4558R.string.zm_btn_learn_more_115072, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                UIUtil.openURL(activity, PTApp.getInstance().getURLByType(24));
            }
        }).setPositiveButton(C4558R.string.zm_btn_got_it, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
    }

    public void onDismiss(@NonNull DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (mNeedFinishParent) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
