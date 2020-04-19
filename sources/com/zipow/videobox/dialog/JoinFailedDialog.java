package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.UpgradeUtil;
import com.zipow.videobox.util.ZmPtUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class JoinFailedDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public boolean mIsNeedForceLeaveCall = false;
    @Nullable
    private JoinFailedDialogParam param;

    public static class JoinFailedDialogParam implements Parcelable {
        public static final Creator<JoinFailedDialogParam> CREATOR = new Creator<JoinFailedDialogParam>() {
            public JoinFailedDialogParam createFromParcel(Parcel parcel) {
                return new JoinFailedDialogParam(parcel);
            }

            public JoinFailedDialogParam[] newArray(int i) {
                return new JoinFailedDialogParam[i];
            }
        };
        public int errorCode;
        public int value;

        public int describeContents() {
            return 0;
        }

        public JoinFailedDialogParam(int i, int i2) {
            this.errorCode = i;
            this.value = i2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof JoinFailedDialogParam)) {
                return false;
            }
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            JoinFailedDialogParam joinFailedDialogParam = (JoinFailedDialogParam) obj;
            if (this.errorCode != joinFailedDialogParam.errorCode) {
                return false;
            }
            if (this.value != joinFailedDialogParam.value) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (this.errorCode * 31) + this.value;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.errorCode);
            parcel.writeInt(this.value);
        }

        protected JoinFailedDialogParam(Parcel parcel) {
            this.errorCode = parcel.readInt();
            this.value = parcel.readInt();
        }
    }

    public JoinFailedDialog() {
        setCancelable(true);
    }

    public static void show(FragmentManager fragmentManager, String str, int i, int i2) {
        JoinFailedDialogParam joinFailedDialogParam = new JoinFailedDialogParam(i, i2);
        if (shouldShow(fragmentManager, str, joinFailedDialogParam)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ZMDialogFragment.PARAMS, joinFailedDialogParam);
            JoinFailedDialog joinFailedDialog = new JoinFailedDialog();
            joinFailedDialog.setArguments(bundle);
            joinFailedDialog.showNow(fragmentManager, str);
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.param = (JoinFailedDialogParam) arguments.getParcelable(ZMDialogFragment.PARAMS);
        if (this.param == null) {
            return createEmptyDialog();
        }
        Builder builder = new Builder(getActivity());
        if (this.param.errorCode == 5003 || this.param.errorCode == 5004 || this.param.errorCode == 1006007000 || this.param.errorCode == 100006000 || this.param.errorCode == 10107000) {
            builder.setTitle(C4558R.string.zm_title_unable_to_connect_50129).setMessage(ZmPtUtils.errorCodeToMessageForJoinFail(getResources(), this.param.errorCode, this.param.value)).setNegativeButton(C4558R.string.zm_btn_ok, (OnClickListener) null);
            ZMAlertDialog create = builder.create();
            create.setCanceledOnTouchOutside(false);
            return create;
        }
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_dailog_msg_txt_view, null, false);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        textView.setText(ZmPtUtils.errorCodeToMessageForJoinFail(getResources(), this.param.errorCode, this.param.value));
        if (this.param.errorCode == 24) {
            final String string = getString(C4558R.string.zm_firewall_support_url);
            Linkify.addLinks(textView, Patterns.WEB_URL, "", new MatchFilter() {
                public boolean acceptMatch(@Nullable CharSequence charSequence, int i, int i2) {
                    return charSequence != null && charSequence.length() > 0 && charSequence.subSequence(i, i2).toString().equals(string);
                }
            }, null);
        }
        builder.setView(inflate);
        if (this.param.errorCode == 10) {
            builder.setPositiveButton(C4558R.string.zm_btn_update, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    UpgradeUtil.upgrade((ZMActivity) JoinFailedDialog.this.getActivity());
                }
            });
        } else if (this.param.errorCode == 23) {
            this.mIsNeedForceLeaveCall = true;
            builder.setTitle(C4558R.string.zm_msg_conffail_internal_only_17745).setMessage(C4558R.string.zm_msg_conffail_signin_join_17745).setPositiveButton(PTApp.getInstance().isWebSignedOn() ? C4558R.string.zm_btn_switch_account : C4558R.string.zm_btn_login, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    JoinFailedDialog.this.mIsNeedForceLeaveCall = false;
                    IPCHelper.getInstance().notifyPTStartLogin("Login to start meeting");
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null);
        } else {
            builder.setNegativeButton(C4558R.string.zm_btn_ok, (OnClickListener) null);
        }
        ZMAlertDialog create2 = builder.create();
        create2.setCanceledOnTouchOutside(false);
        return create2;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.mIsNeedForceLeaveCall) {
            PTApp.getInstance().forceSyncLeaveCurrentCall(false, true);
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
