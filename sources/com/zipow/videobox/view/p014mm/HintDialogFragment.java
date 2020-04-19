package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;

/* renamed from: com.zipow.videobox.view.mm.HintDialogFragment */
public class HintDialogFragment extends ZMDialogFragment {
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_NEGATIVE_TEXT = "negativeText";
    private static final String ARG_POSITIVE_TEXT = "positiveText";
    private static final String ARG_TITLE = "title";
    private static final String TAG = "HintDialogFragment";
    @Nullable
    private String mMessage;
    @Nullable
    private String mNegativeText;
    @Nullable
    private String mPositiveText;
    @Nullable
    private String mTitle;

    public static void showHintDialog(@Nullable FragmentManager fragmentManager, @Nullable Fragment fragment, int i, String str, String str2, String str3, String str4) {
        if (fragmentManager != null) {
            HintDialogFragment hintDialogFragment = new HintDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            bundle.putString("message", str2);
            bundle.putString(ARG_POSITIVE_TEXT, str3);
            bundle.putString(ARG_NEGATIVE_TEXT, str4);
            hintDialogFragment.setArguments(bundle);
            if (fragment != null) {
                hintDialogFragment.setTargetFragment(fragment, i);
            }
            hintDialogFragment.show(fragmentManager, HintDialogFragment.class.getName());
        }
    }

    public HintDialogFragment() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mTitle = arguments.getString("title");
            this.mMessage = arguments.getString("message");
            this.mPositiveText = arguments.getString(ARG_POSITIVE_TEXT);
            this.mNegativeText = arguments.getString(ARG_NEGATIVE_TEXT);
        }
        Builder builder = new Builder(getActivity());
        if (!TextUtils.isEmpty(this.mTitle)) {
            builder.setTitle((CharSequence) this.mTitle);
        }
        if (!TextUtils.isEmpty(this.mMessage)) {
            builder.setMessage(this.mMessage);
        }
        if (!TextUtils.isEmpty(this.mPositiveText)) {
            builder.setPositiveButton(this.mPositiveText, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Fragment targetFragment = HintDialogFragment.this.getTargetFragment();
                    if (targetFragment != null) {
                        targetFragment.onActivityResult(HintDialogFragment.this.getTargetRequestCode(), -1, null);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(this.mNegativeText)) {
            builder.setNegativeButton(this.mNegativeText, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    HintDialogFragment.this.dismiss();
                }
            });
        }
        return builder.create();
    }
}
