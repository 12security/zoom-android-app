package com.zipow.videobox.confapp.p009bo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.confapp.bo.BOMeetingEndDialogFragment */
public class BOMeetingEndDialogFragment extends ZMDialogFragment {
    public static final int BO_END_TYPE_CLOSE_IN_BO = 0;
    public static final int BO_END_TYPE_CLOSE_IN_MASTER = 1;
    public static final int BO_END_TYPE_LEAVE = 0;
    private static final String BO_MEETING_END_AUTO = "bo_meeting_end_auto";
    private static final String BO_MEETING_END_TYPE = "bo_meeting_end_type";
    private static final String BO_MEETING_END_WAIT_SECONDS = "bo_meeting_end_wait_seconds";
    /* access modifiers changed from: private */
    public boolean mAuto;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mBOTimeoutRunnable = new Runnable() {
        public void run() {
            if (BOMeetingEndDialogFragment.this.mBOWaitSeconds <= 0 || !BOMeetingEndDialogFragment.this.isAdded()) {
                BOMeetingEndDialogFragment.this.closeBO();
                return;
            }
            BOMeetingEndDialogFragment.this.updateLeftSecondsUI();
            if (BOMeetingEndDialogFragment.this.mAuto) {
                BOMeetingEndDialogFragment.this.mBOWaitSeconds = BOMeetingEndDialogFragment.this.mBOWaitSeconds - 1;
                BOMeetingEndDialogFragment.this.mBoHandler.postDelayed(BOMeetingEndDialogFragment.this.mBOTimeoutRunnable, 1000);
            }
        }
    };
    /* access modifiers changed from: private */
    public int mBOWaitSeconds;
    /* access modifiers changed from: private */
    public Handler mBoHandler;
    private int mEndType;

    public static void showDialogFragment(FragmentManager fragmentManager, int i, boolean z, int i2, String str) {
        BOMeetingEndDialogFragment bOMeetingEndDialogFragment = new BOMeetingEndDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BO_MEETING_END_WAIT_SECONDS, i);
        bundle.putBoolean(BO_MEETING_END_AUTO, z);
        bundle.putInt(BO_MEETING_END_TYPE, i2);
        bOMeetingEndDialogFragment.setArguments(bundle);
        bOMeetingEndDialogFragment.show(fragmentManager, str);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mBOWaitSeconds = arguments.getInt(BO_MEETING_END_WAIT_SECONDS, 30);
        this.mAuto = arguments.getBoolean(BO_MEETING_END_AUTO, true);
        this.mEndType = arguments.getInt(BO_MEETING_END_TYPE, 0);
        if (this.mAuto) {
            this.mBoHandler = new Handler();
            this.mBoHandler.postDelayed(this.mBOTimeoutRunnable, 1000);
        }
        Builder title = new Builder(getActivity()).setMessage(C4558R.string.zm_bo_msg_close).setTitle((CharSequence) getTitleInfo());
        if (this.mEndType == 1) {
            title.setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        } else {
            title.setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_bo_btn_leave_now, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    BOUtil.leaveBO();
                }
            });
        }
        return title.create();
    }

    public void onDestroyView() {
        this.mBOWaitSeconds = 0;
        Handler handler = this.mBoHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mBOTimeoutRunnable);
        }
        super.onDestroyView();
    }

    public void updateWaitingSeconds(int i) {
        if (i <= 0) {
            closeBO();
            return;
        }
        this.mBOWaitSeconds = i;
        updateLeftSecondsUI();
    }

    private void finish() {
        if (isAdded()) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void closeBO() {
        BOUtil.leaveBO();
        finish();
    }

    @NonNull
    private String getTitleInfo() {
        if (this.mEndType == 0) {
            return getResources().getString(C4558R.string.zm_bo_title_close, new Object[]{Integer.valueOf(this.mBOWaitSeconds)});
        }
        return getResources().getString(C4558R.string.zm_bo_msg_end_all_bo, new Object[]{Integer.valueOf(this.mBOWaitSeconds)});
    }

    /* access modifiers changed from: private */
    public void updateLeftSecondsUI() {
        Dialog dialog = getDialog();
        if (dialog != null && dialog.isShowing()) {
            dialog.setTitle(getTitleInfo());
        }
    }
}
