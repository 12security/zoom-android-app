package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMGDPRConfirmDialog extends ZMDialogFragment {
    public static final String ARGS_PRIVACY_URL = "args_privacy_url";
    private static final String ARGS_REQUEST_CODE = "args_request_code";
    public static final String ARGS_TERMS_URL = "args_terms_url";
    private static final String ARGS_TYPE = "args_type";
    @NonNull
    private static String TAG = "com.zipow.videobox.dialog.ZMGDPRConfirmDialog";
    public static final int TYPE_CANNOT_JOIN_MEETING = 3;
    public static final int TYPE_JOIN_MEETING = 2;
    public static final int TYPE_SIGN_IN = 1;
    @Nullable
    private DialogActionCallBack mDialogActionCallBack;
    @Nullable
    private String mPrivacyUrl;
    private int mRequestCode;
    @Nullable
    private String mTosUrl;
    private int mType;

    public static void checkShowDialog(@NonNull ZMActivity zMActivity, int i, int i2, @NonNull String str, @NonNull String str2) {
        dismiss(zMActivity.getSupportFragmentManager());
        showDialog(zMActivity, i, i2, str, str2);
    }

    public static void showDialog(@NonNull ZMActivity zMActivity, int i, int i2, @NonNull String str, @NonNull String str2) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_REQUEST_CODE, i);
        bundle.putInt(ARGS_TYPE, i2);
        bundle.putString(ARGS_TERMS_URL, str);
        bundle.putString(ARGS_PRIVACY_URL, str2);
        ZMGDPRConfirmDialog zMGDPRConfirmDialog = new ZMGDPRConfirmDialog();
        zMGDPRConfirmDialog.setArguments(bundle);
        zMGDPRConfirmDialog.show(zMActivity.getSupportFragmentManager(), ZMGDPRConfirmDialog.class.getName());
    }

    @Nullable
    public static ZMGDPRConfirmDialog findFragment(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return null;
        }
        return (ZMGDPRConfirmDialog) fragmentManager.findFragmentByTag(ZMGDPRConfirmDialog.class.getName());
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        ZMGDPRConfirmDialog findFragment = findFragment(fragmentManager);
        if (findFragment == null) {
            return false;
        }
        findFragment.dismiss();
        return true;
    }

    public void onAttach(@Nullable Context context) {
        super.onAttach(context);
        if (context instanceof DialogActionCallBack) {
            this.mDialogActionCallBack = (DialogActionCallBack) context;
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ARGS_REQUEST_CODE, this.mRequestCode);
        bundle.putInt(ARGS_TYPE, this.mType);
        bundle.putString(ARGS_TERMS_URL, this.mTosUrl);
        bundle.putString(ARGS_PRIVACY_URL, this.mPrivacyUrl);
    }

    public void onViewStateRestored(@Nullable Bundle bundle) {
        super.onViewStateRestored(bundle);
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mRequestCode = arguments.getInt(ARGS_REQUEST_CODE);
            this.mType = arguments.getInt(ARGS_TYPE);
            this.mTosUrl = arguments.getString(ARGS_TERMS_URL);
            this.mPrivacyUrl = arguments.getString(ARGS_PRIVACY_URL);
        } else if (bundle != null) {
            this.mRequestCode = bundle.getInt(ARGS_REQUEST_CODE);
            this.mType = bundle.getInt(ARGS_TYPE);
            this.mTosUrl = bundle.getString(ARGS_TERMS_URL);
            this.mPrivacyUrl = bundle.getString(ARGS_PRIVACY_URL);
        }
        Builder view = new Builder(getActivity()).setView(createContent());
        int i = this.mType;
        if (i == 1) {
            view.setCancelable(true);
            view.setTitle(C4558R.string.zm_title_gdpr_sing_in_41396);
            view.setPositiveButton(C4558R.string.zm_btn_continue, (OnClickListener) new OnClickListener() {
                public void onClick(@Nullable DialogInterface dialogInterface, int i) {
                    ZMGDPRConfirmDialog.this.onClickPositiveBtn();
                    if (dialogInterface != null) {
                        dialogInterface.cancel();
                    }
                }
            });
        } else if (i == 2) {
            view.setCancelable(false);
            view.setTitle(C4558R.string.zm_msg_gdrp_new_user_join_meeting_41396);
            view.setPositiveButton(C4558R.string.zm_btn_agree_41396, (OnClickListener) new OnClickListener() {
                public void onClick(@Nullable DialogInterface dialogInterface, int i) {
                    ZMGDPRConfirmDialog.this.onClickPositiveBtn();
                    if (dialogInterface != null) {
                        dialogInterface.cancel();
                    }
                }
            });
            view.setNegativeButton(C4558R.string.zm_btn_disagree_41396, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ZMGDPRConfirmDialog.this.onClickNegativeBtn();
                }
            });
        } else if (i == 3) {
            view.setCancelable(false);
            view.setTitle(C4558R.string.zm_msg_cannot_join_meeting_41396);
            view.setPositiveButton(C4558R.string.zm_btn_agree_41396, (OnClickListener) new OnClickListener() {
                public void onClick(@Nullable DialogInterface dialogInterface, int i) {
                    ZMGDPRConfirmDialog.this.onClickPositiveBtn();
                    if (dialogInterface != null) {
                        dialogInterface.cancel();
                    }
                }
            });
            view.setNegativeButton(C4558R.string.zm_btn_leave_conference, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ZMGDPRConfirmDialog.this.onClickNegativeBtn();
                }
            });
        }
        ZMAlertDialog create = view.create();
        create.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                ZMGDPRConfirmDialog.this.onClickBackPressed();
                return true;
            }
        });
        create.setCanceledOnTouchOutside(false);
        create.show();
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_gdpr_layout, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtTerms);
        TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.txtPrivacy);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
        if (this.mType == 1) {
            textView.setText(C4558R.string.zm_msg_gdpr_sing_in_41396);
        } else {
            textView.setVisibility(8);
        }
        textView2.setText(ZMHtmlUtil.fromHtml(getContext(), getString(C4558R.string.zm_msg_terms_of_services_41396, this.mTosUrl), new OnURLSpanClickListener() {
            public void onClick(View view, String str, String str2) {
                ZMWebPageUtil.startWebPage((Fragment) ZMGDPRConfirmDialog.this, str, str2);
            }
        }));
        textView3.setText(ZMHtmlUtil.fromHtml(getContext(), getString(C4558R.string.zm_msg_privacy_policy_41396, this.mPrivacyUrl), new OnURLSpanClickListener() {
            public void onClick(View view, String str, String str2) {
                ZMWebPageUtil.startWebPage((Fragment) ZMGDPRConfirmDialog.this, str, str2);
            }
        }));
        return inflate;
    }

    /* access modifiers changed from: private */
    public void onClickPositiveBtn() {
        DialogActionCallBack dialogActionCallBack = this.mDialogActionCallBack;
        if (dialogActionCallBack != null) {
            dialogActionCallBack.performDialogAction(this.mRequestCode, -1, null);
        }
    }

    /* access modifiers changed from: private */
    public void onClickNegativeBtn() {
        if (this.mDialogActionCallBack != null) {
            Bundle bundle = new Bundle();
            bundle.putString(ARGS_TERMS_URL, this.mTosUrl);
            bundle.putString(ARGS_PRIVACY_URL, this.mPrivacyUrl);
            this.mDialogActionCallBack.performDialogAction(this.mRequestCode, -2, bundle);
        }
    }

    public void onClickBackPressed() {
        DialogActionCallBack dialogActionCallBack = this.mDialogActionCallBack;
        if (dialogActionCallBack != null) {
            dialogActionCallBack.performDialogAction(this.mRequestCode, 1, null);
        }
    }

    public void dismiss() {
        finishFragment(false);
    }
}
