package com.zipow.videobox.confapp.p009bo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.meeting.confhelper.BOComponent;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.confapp.bo.BOLeaveFragment */
public class BOLeaveFragment extends ZMDialogFragment implements OnClickListener {
    private static final String BO_CONTROLLER = "bo_controller";
    private static final String BO_IS_IN_BO_MEETING = "bo_is_in_bomeeting";
    public static final int BO_TYPE_CANCEL = 5;
    public static final int BO_TYPE_END_ALL_BO = 2;
    public static final int BO_TYPE_END_MEETING = 4;
    public static final int BO_TYPE_LEAVE_BO = 1;
    public static final int BO_TYPE_LEAVE_MEETING = 3;
    private boolean mBOController = false;
    private boolean mIsInBOMeeting = false;
    private LayoutParams mSaveLayoutParams;

    public static void showAsDialog(FragmentManager fragmentManager, boolean z, boolean z2, String str) {
        BOLeaveFragment bOLeaveFragment = new BOLeaveFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BO_CONTROLLER, z);
        bundle.putBoolean(BO_IS_IN_BO_MEETING, z2);
        bOLeaveFragment.setArguments(bundle);
        bOLeaveFragment.show(fragmentManager, str);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mBOController = arguments.getBoolean(BO_CONTROLLER);
        this.mIsInBOMeeting = arguments.getBoolean(BO_IS_IN_BO_MEETING);
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setView(createContent()).setTheme(C4558R.style.ZMDialog_Material_Transparent).create();
        create.setCanceledOnTouchOutside(true);
        Window window = create.getWindow();
        if (window != null) {
            LayoutParams attributes = window.getAttributes();
            this.mSaveLayoutParams = attributes;
            window.setAttributes(attributes);
        }
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_bo_leave_menu, null);
        View findViewById = inflate.findViewById(C4558R.C4560id.panelLeaveBO);
        View findViewById2 = inflate.findViewById(C4558R.C4560id.panelEndAllBO);
        View findViewById3 = inflate.findViewById(C4558R.C4560id.panelLeaveMeeting);
        View findViewById4 = inflate.findViewById(C4558R.C4560id.panelEndMeeting);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtLeaveMeeting);
        View findViewById5 = inflate.findViewById(C4558R.C4560id.panelLeaveBOPrompt);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtLeavePromt);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        findViewById3.setOnClickListener(this);
        findViewById4.setOnClickListener(this);
        if (this.mBOController) {
            findViewById3.setBackgroundResource(C4558R.C4559drawable.zm_btn_dialog_bg);
            textView.setTextColor(getResources().getColorStateList(C4558R.color.zm_popitem_btn_color));
        } else {
            findViewById4.setVisibility(8);
            findViewById2.setVisibility(8);
        }
        if (!this.mIsInBOMeeting) {
            findViewById5.setVisibility(8);
            findViewById.setVisibility(8);
        }
        if (this.mBOController || BOUtil.isBackToMainSessionEnabled()) {
            textView2.setText(C4558R.string.zm_bo_lbl_leave_bo);
        } else {
            findViewById.setVisibility(8);
            textView2.setText(C4558R.string.zm_bo_lbl_leave_meeting_34298);
        }
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        dismiss();
        if (id == C4558R.C4560id.panelLeaveBO) {
            onClickBoAction(1);
        } else if (id == C4558R.C4560id.panelEndAllBO) {
            onClickBoAction(2);
        } else if (id == C4558R.C4560id.panelLeaveMeeting) {
            onClickBoAction(3);
        } else if (id == C4558R.C4560id.panelEndMeeting) {
            onClickBoAction(4);
        }
    }

    private void onClickBoAction(int i) {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            BOComponent bOComponent = confActivity.getmBOComponent();
            if (bOComponent != null) {
                bOComponent.selectBOLeaveType(i);
            }
        }
    }

    public void dismiss() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setAttributes(this.mSaveLayoutParams);
            }
        }
        finishFragment(true);
    }
}
