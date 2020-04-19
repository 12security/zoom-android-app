package com.zipow.videobox.view.sip;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.sip.server.CmmSIPAPI;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.view.sip.SipTransferOptionAdapter.SipTransferMenuItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class TransferToMeetingDialog extends ZMDialogFragment {
    private static final String ARG_CALL_ID = "call_id";
    private static final String TAG = "TransferToMeetingDialog";
    /* access modifiers changed from: private */
    public TransferToMeetingAdapter mAdapter;
    private SimpleSIPCallEventListener mCallEventListener = new SimpleSIPCallEventListener() {
        public void OnMeetingStateChanged(int i) {
            super.OnMeetingStateChanged(i);
            CmmSIPAPI.getMeetingState();
            VideoBoxApplication.getNonNullInstance().isConfProcessRunning();
            TransferToMeetingDialog.this.updateDialog();
        }
    };
    private String mCallId;
    private IConfProcessListener mConfProcessListener = new IConfProcessListener() {
        public void onConfProcessStarted() {
            CmmSIPAPI.getMeetingState();
            VideoBoxApplication.getNonNullInstance().isConfProcessRunning();
            TransferToMeetingDialog.this.updateDialog();
        }

        public void onConfProcessStopped() {
            CmmSIPAPI.getMeetingState();
            VideoBoxApplication.getNonNullInstance().isConfProcessRunning();
            TransferToMeetingDialog.this.updateDialog();
        }
    };

    public interface ITransferToMeeting {
        void onMergeIntoMeeting(String str);

        void onNewMeeting(String str);
    }

    private static class TransferToMeetingAdapter extends SipTransferOptionAdapter {
        public static final int ACTION_MERGE_INTO_MEETING = 11;
        public static final int ACTION_NEW_MEETING = 10;

        public TransferToMeetingAdapter(Context context) {
            this(context, false);
        }

        public TransferToMeetingAdapter(Context context, boolean z) {
            super(context, z);
        }

        /* access modifiers changed from: protected */
        public void onBindView(@NonNull View view, @NonNull SipTransferMenuItem sipTransferMenuItem) {
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtSubLabel);
            textView.setGravity(17);
            textView.setText(sipTransferMenuItem.getLabel());
            textView.setEnabled(!sipTransferMenuItem.isDisable());
            textView2.setGravity(17);
            textView2.setText(sipTransferMenuItem.getSubLabel());
            textView2.setEnabled(!sipTransferMenuItem.isDisable());
        }
    }

    public static void show(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            FragmentManager fragmentManager = null;
            if (context instanceof ZMActivity) {
                fragmentManager = ((ZMActivity) context).getSupportFragmentManager();
            }
            if (fragmentManager != null) {
                TransferToMeetingDialog transferToMeetingDialog = new TransferToMeetingDialog();
                Bundle bundle = new Bundle();
                bundle.putString(ARG_CALL_ID, str);
                transferToMeetingDialog.setArguments(bundle);
                transferToMeetingDialog.show(fragmentManager, TransferToMeetingDialog.class.getName());
            }
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mCallId = arguments.getString(ARG_CALL_ID, null);
        }
        if (TextUtils.isEmpty(this.mCallId)) {
            dismiss();
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        this.mAdapter = new TransferToMeetingAdapter(getActivity());
        SipTransferMenuItem sipTransferMenuItem = new SipTransferMenuItem(10, getString(C4558R.string.zm_sip_transfer_to_meeting_new_108093), getString(C4558R.string.zm_sip_transfer_to_meeting_new_des_108093));
        this.mAdapter.addItem(sipTransferMenuItem);
        SipTransferMenuItem sipTransferMenuItem2 = new SipTransferMenuItem(11, getString(C4558R.string.zm_sip_transfer_to_meeting_merge_108093), getString(C4558R.string.zm_sip_transfer_to_meeting_merge_des_108093));
        sipTransferMenuItem.setmDisable(!CmmSIPCallManager.getInstance().hasMeetings());
        this.mAdapter.addItem(sipTransferMenuItem2);
        int dimensionPixelSize = getResources().getDimensionPixelSize(C4558R.dimen.zm_dialog_radius_normal);
        ZMAlertDialog create = new Builder(getActivity()).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SipTransferMenuItem sipTransferMenuItem = (SipTransferMenuItem) TransferToMeetingDialog.this.mAdapter.getItem(i);
                if (sipTransferMenuItem != null) {
                    switch (sipTransferMenuItem.getAction()) {
                        case 10:
                            TransferToMeetingDialog.this.onNewMeeting();
                            return;
                        case 11:
                            TransferToMeetingDialog.this.onMergeIntoMeeting();
                            return;
                        default:
                            return;
                    }
                }
            }
        }).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_RoundRect_NormalCorners).setContentPadding(0, dimensionPixelSize, 0, dimensionPixelSize).create();
        CmmSIPCallManager.getInstance().addListener(this.mCallEventListener);
        VideoBoxApplication.getNonNullInstance().addConfProcessListener(this.mConfProcessListener);
        return create;
    }

    public void onDestroy() {
        CmmSIPCallManager.getInstance().removeListener(this.mCallEventListener);
        VideoBoxApplication.getNonNullInstance().removeConfProcessListener(this.mConfProcessListener);
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void updateDialog() {
        TransferToMeetingAdapter transferToMeetingAdapter = this.mAdapter;
        if (transferToMeetingAdapter != null && transferToMeetingAdapter.getCount() > 1) {
            ((SipTransferMenuItem) this.mAdapter.getItem(1)).setmDisable(true ^ CmmSIPCallManager.getInstance().hasMeetings());
            this.mAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void onNewMeeting() {
        if (getActivity() instanceof ITransferToMeeting) {
            ((ITransferToMeeting) getActivity()).onNewMeeting(this.mCallId);
        }
    }

    /* access modifiers changed from: private */
    public void onMergeIntoMeeting() {
        if (getActivity() instanceof ITransferToMeeting) {
            ((ITransferToMeeting) getActivity()).onMergeIntoMeeting(this.mCallId);
        }
    }
}
