package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.LoginUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMNoticeChoiceDomainDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    @Nullable
    public ZMNoticeChoiceDomainDialogParam mParams;

    public static class ZMNoticeChoiceDomainDialogParam implements Parcelable {
        public static final Creator<ZMNoticeChoiceDomainDialogParam> CREATOR = new Creator<ZMNoticeChoiceDomainDialogParam>() {
            public ZMNoticeChoiceDomainDialogParam createFromParcel(Parcel parcel) {
                return new ZMNoticeChoiceDomainDialogParam(parcel);
            }

            public ZMNoticeChoiceDomainDialogParam[] newArray(int i) {
                return new ZMNoticeChoiceDomainDialogParam[i];
            }
        };
        private String accountName;
        private boolean couldSkip;
        private String detailLinkUrl;
        private String emailDomain;

        public int describeContents() {
            return 0;
        }

        public ZMNoticeChoiceDomainDialogParam(String str, boolean z, String str2, String str3) {
            this.detailLinkUrl = str;
            this.couldSkip = z;
            this.accountName = str2;
            this.emailDomain = str3;
        }

        public String getDetailLinkUrl() {
            return this.detailLinkUrl;
        }

        public boolean isCouldSkip() {
            return this.couldSkip;
        }

        public String getAccountName() {
            return this.accountName;
        }

        public String getEmailDomain() {
            return this.emailDomain;
        }

        public boolean isValid() {
            return !StringUtil.isEmptyOrNull(this.detailLinkUrl) && !StringUtil.isEmptyOrNull(this.accountName);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ZMNoticeChoiceDomainDialogParam zMNoticeChoiceDomainDialogParam = (ZMNoticeChoiceDomainDialogParam) obj;
            if (this.couldSkip != zMNoticeChoiceDomainDialogParam.couldSkip) {
                return false;
            }
            String str = this.detailLinkUrl;
            if (str == null ? zMNoticeChoiceDomainDialogParam.detailLinkUrl != null : !str.equals(zMNoticeChoiceDomainDialogParam.detailLinkUrl)) {
                return false;
            }
            String str2 = this.accountName;
            if (str2 == null ? zMNoticeChoiceDomainDialogParam.accountName != null : !str2.equals(zMNoticeChoiceDomainDialogParam.accountName)) {
                return false;
            }
            String str3 = this.emailDomain;
            if (str3 != null) {
                z = str3.equals(zMNoticeChoiceDomainDialogParam.emailDomain);
            } else if (zMNoticeChoiceDomainDialogParam.emailDomain != null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            String str = this.detailLinkUrl;
            int i = 0;
            int hashCode = (((str != null ? str.hashCode() : 0) * 31) + (this.couldSkip ? 1 : 0)) * 31;
            String str2 = this.accountName;
            int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
            String str3 = this.emailDomain;
            if (str3 != null) {
                i = str3.hashCode();
            }
            return hashCode2 + i;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.detailLinkUrl);
            parcel.writeByte(this.couldSkip ? (byte) 1 : 0);
            parcel.writeString(this.accountName);
            parcel.writeString(this.emailDomain);
        }

        public ZMNoticeChoiceDomainDialogParam() {
        }

        protected ZMNoticeChoiceDomainDialogParam(Parcel parcel) {
            this.detailLinkUrl = parcel.readString();
            this.couldSkip = parcel.readByte() != 0;
            this.accountName = parcel.readString();
            this.emailDomain = parcel.readString();
        }
    }

    public ZMNoticeChoiceDomainDialog() {
        setCancelable(false);
    }

    public static void show(FragmentManager fragmentManager, String str, @Nullable String str2, boolean z, @Nullable String str3, @Nullable String str4) {
        ZMNoticeChoiceDomainDialogParam zMNoticeChoiceDomainDialogParam = new ZMNoticeChoiceDomainDialogParam(str2, z, str3, str4);
        if (zMNoticeChoiceDomainDialogParam.isValid() && shouldShow(fragmentManager, str, zMNoticeChoiceDomainDialogParam)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ZMDialogFragment.PARAMS, zMNoticeChoiceDomainDialogParam);
            ZMNoticeChoiceDomainDialog zMNoticeChoiceDomainDialog = new ZMNoticeChoiceDomainDialog();
            zMNoticeChoiceDomainDialog.setArguments(bundle);
            zMNoticeChoiceDomainDialog.show(fragmentManager, str);
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mParams = (ZMNoticeChoiceDomainDialogParam) arguments.getParcelable(ZMDialogFragment.PARAMS);
        ZMNoticeChoiceDomainDialogParam zMNoticeChoiceDomainDialogParam = this.mParams;
        if (zMNoticeChoiceDomainDialogParam == null || !zMNoticeChoiceDomainDialogParam.isValid()) {
            return createEmptyDialog();
        }
        Builder builder = new Builder(getActivity());
        builder.setTitle((CharSequence) getString(C4558R.string.zm_title_join_zoom_account_114850, StringUtil.safeString(this.mParams.getAccountName()))).setMessage(getString(C4558R.string.zm_msg_notice_choose_domain_114850, StringUtil.safeString(this.mParams.getAccountName()), StringUtil.safeString(this.mParams.getEmailDomain()))).setEnableAutoClickBtnDismiss(false).setPositiveButton(C4558R.string.zm_btn_view_detail_choose_114850, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMActivity zMActivity = (ZMActivity) ZMNoticeChoiceDomainDialog.this.getActivity();
                if (zMActivity != null) {
                    UIUtil.openURL(zMActivity, ZMNoticeChoiceDomainDialog.this.mParams.getDetailLinkUrl());
                }
            }
        });
        if (this.mParams.isCouldSkip()) {
            builder.setNegativeButton(C4558R.string.zm_btn_skip_this_time_114850, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PTApp.getInstance().onUserSkipSignToJoinOption();
                    ZMNoticeChoiceDomainDialog.this.dismiss();
                }
            });
        } else {
            builder.setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    LoginUtil.launchLogin(true, false);
                    ZMNoticeChoiceDomainDialog.this.dismiss();
                }
            });
        }
        ZMAlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }
}
