package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMSpanny;
import p021us.zoom.videomeetings.C4558R;

public class ZMAlertConnectAudioDialog extends ZMDialogFragment implements OnClickListener {
    private static final String ARG_USER_ID = "arg_user_id";
    private Button mBtnRaiseHand;
    @NonNull
    private IConfUIListener mConfUIListener = new SimpleConfUIListener() {
        public boolean onUserStatusChanged(int i, long j, int i2) {
            if (i == 21) {
                ZMAlertConnectAudioDialog.this.onAudioTypeChanged(j);
                return true;
            } else if (i != 36) {
                return false;
            } else {
                ZMAlertConnectAudioDialog.this.onUserRaisedHand(j);
                return true;
            }
        }

        public boolean onConfStatusChanged2(int i, long j) {
            if (i != 39) {
                return false;
            }
            ZMAlertConnectAudioDialog.this.onSilentModeChanged(j);
            return true;
        }
    };
    private View mImgAudioConnected;
    private TextView mTxtMsg;
    private TextView mTxtTitle;
    private long mUserId = -1;

    public static void showConnectAudioDialog(@Nullable ZMActivity zMActivity, long j) {
        if (zMActivity != null) {
            ZMAlertConnectAudioDialog zMAlertConnectAudioDialog = new ZMAlertConnectAudioDialog();
            Bundle bundle = new Bundle();
            bundle.putLong(ARG_USER_ID, j);
            zMAlertConnectAudioDialog.setArguments(bundle);
            zMAlertConnectAudioDialog.show(zMActivity.getSupportFragmentManager(), ZMAlertConnectAudioDialog.class.getName());
        }
    }

    public static void dismiss(@NonNull FragmentManager fragmentManager) {
        ZMAlertConnectAudioDialog zMAlertConnectAudioDialog = getZMAlertConnectAudioDialog(fragmentManager);
        if (zMAlertConnectAudioDialog != null) {
            zMAlertConnectAudioDialog.dismiss();
        }
    }

    @Nullable
    private static ZMAlertConnectAudioDialog getZMAlertConnectAudioDialog(FragmentManager fragmentManager) {
        return (ZMAlertConnectAudioDialog) fragmentManager.findFragmentByTag(ZMAlertConnectAudioDialog.class.getName());
    }

    public ZMAlertConnectAudioDialog() {
        setCancelable(true);
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
        this.mUserId = arguments.getLong(ARG_USER_ID);
        View createContent = createContent();
        if (createContent == null) {
            return createEmptyDialog();
        }
        ZMAlertDialog create = new Builder(getActivity()).setTheme(C4558R.style.ZMDialog_Material_Transparent).setView(createContent).create();
        create.setCanceledOnTouchOutside(false);
        ConfUI.getInstance().addListener(this.mConfUIListener);
        return create;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        super.onDismiss(dialogInterface);
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_alert_connect_audio, null);
        this.mImgAudioConnected = inflate.findViewById(C4558R.C4560id.imgAudioConnected);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mTxtMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtMsg);
        this.mBtnRaiseHand = (Button) inflate.findViewById(C4558R.C4560id.btRaiseHand);
        this.mBtnRaiseHand.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btCancel).setOnClickListener(this);
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            updateUI(myself);
        }
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btRaiseHand) {
            onClickRaiseHand();
            dismiss();
        } else if (id == C4558R.C4560id.btCancel) {
            dismiss();
        }
    }

    private void onClickRaiseHand() {
        if (ConfMgr.getInstance().handleUserCmd(36, this.mUserId) && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            AccessibilityUtil.announceForAccessibilityCompat((View) this.mBtnRaiseHand, C4558R.string.zm_description_msg_myself_already_raise_hand_17843);
        }
    }

    private void onClickCancel() {
        dismiss();
    }

    /* access modifiers changed from: private */
    public void onAudioTypeChanged(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null && confStatusObj.isMyself(j)) {
                updateUI(myself);
            }
        }
    }

    private void updateUI(@NonNull CmmUser cmmUser) {
        if (ConfLocalHelper.getMyAudioType() == 2) {
            this.mTxtTitle.setText(C4558R.string.zm_title_audio_not_connected_45416);
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(String.valueOf(cmmUser.getAttendeeID()));
            sb.append("#");
            String sb2 = sb.toString();
            ZMSpanny zMSpanny = new ZMSpanny(getString(C4558R.string.zm_msg_audio_not_connected_45416, sb2));
            zMSpanny.setSpans(sb2, new StyleSpan(1), new ForegroundColorSpan(-16777216), new AbsoluteSizeSpan(15, true));
            this.mTxtMsg.setText(zMSpanny);
            this.mBtnRaiseHand.setTextColor(getResources().getColorStateList(C4558R.color.zm_disable_text_color));
            this.mTxtTitle.setTextColor(getResources().getColor(C4558R.color.zm_black));
            this.mBtnRaiseHand.setTypeface(null, 0);
            this.mImgAudioConnected.setVisibility(8);
            return;
        }
        this.mTxtTitle.setText(C4558R.string.zm_title_audio_connected_45416);
        this.mTxtMsg.setText(C4558R.string.zm_msg_audio_connected_45416);
        this.mBtnRaiseHand.setTextColor(getResources().getColorStateList(C4558R.color.zm_popitem_btn_color));
        this.mTxtTitle.setTextColor(getResources().getColor(C4558R.color.zm_green));
        this.mBtnRaiseHand.setTypeface(null, 1);
        this.mImgAudioConnected.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void onSilentModeChanged(long j) {
        if (ConfLocalHelper.isInSilentMode() || ConfLocalHelper.isDirectShareClient()) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void onUserRaisedHand(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null && myself.getRaiseHandState()) {
                dismiss();
            }
        }
    }
}
