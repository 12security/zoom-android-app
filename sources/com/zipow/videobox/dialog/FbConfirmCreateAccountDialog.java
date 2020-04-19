package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ZMDomainUtil;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.thirdparty.login.facebook.FbUserProfile;
import p021us.zoom.videomeetings.C4558R;

public class FbConfirmCreateAccountDialog extends ZMDialogFragment {
    public static int ACTION_LOGIN_FB_FIRST = 1;
    private static final String TAG = "com.zipow.videobox.dialog.FbConfirmCreateAccountDialog";
    /* access modifiers changed from: private */
    public DialogActionCallBack mDialogActionCallBack;

    public FbConfirmCreateAccountDialog() {
        setCancelable(true);
    }

    public static void show(@Nullable ZMActivity zMActivity, @NonNull FbUserProfile fbUserProfile) {
        if (zMActivity != null) {
            FbConfirmCreateAccountDialog fbConfirmCreateAccountDialog = new FbConfirmCreateAccountDialog();
            Bundle bundle = new Bundle();
            bundle.putParcelable(FbUserProfile.class.getName(), fbUserProfile);
            fbConfirmCreateAccountDialog.setArguments(bundle);
            fbConfirmCreateAccountDialog.show(zMActivity.getSupportFragmentManager(), TAG);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mDialogActionCallBack = (DialogActionCallBack) context;
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View createContent = createContent();
        if (createContent == null) {
            return createEmptyDialog();
        }
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setView(createContent).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material_RoundRect), C4558R.layout.zm_fb_confirm_create_account, null);
        ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.avatarView);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtHi);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtEmail);
        TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.txtTerms);
        inflate.findViewById(C4558R.C4560id.btnCreateAccount).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (FbConfirmCreateAccountDialog.this.mDialogActionCallBack != null) {
                    FbConfirmCreateAccountDialog.this.mDialogActionCallBack.performDialogAction(0, FbConfirmCreateAccountDialog.ACTION_LOGIN_FB_FIRST, null);
                }
                FbConfirmCreateAccountDialog.this.dismiss();
            }
        });
        Bundle arguments = getArguments();
        if (arguments == null) {
            return null;
        }
        FbUserProfile fbUserProfile = (FbUserProfile) arguments.getParcelable(FbUserProfile.class.getName());
        if (fbUserProfile == null) {
            return null;
        }
        textView.setText(getString(C4558R.string.zm_msg_confirm_hi_create_account_31350, StringUtil.safeString(fbUserProfile.getName())));
        textView2.setText(fbUserProfile.getEmail());
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
        String uRLByType = PTApp.getInstance().getURLByType(10);
        if (!StringUtil.isEmptyOrNull(uRLByType)) {
            textView3.setText(ZMHtmlUtil.fromHtml(getContext(), getString(C4558R.string.zm_msg_confirm_terms_create_account_31350, uRLByType, ZMDomainUtil.getZmUrlPrivacyPolicy()), new OnURLSpanClickListener() {
                public void onClick(View view, String str, String str2) {
                    ZMWebPageUtil.startWebPage((Fragment) FbConfirmCreateAccountDialog.this, str, str2);
                }
            }));
        }
        Glide.with((Fragment) this).load(fbUserProfile.getAvatarUrl()).apply(new RequestOptions().circleCrop().placeholder(C4558R.C4559drawable.zm_no_avatar).error(C4558R.C4559drawable.zm_no_avatar)).into(imageView);
        return inflate;
    }
}
