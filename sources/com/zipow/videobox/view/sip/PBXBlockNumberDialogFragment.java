package com.zipow.videobox.view.sip;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.videomeetings.C4558R;

public class PBXBlockNumberDialogFragment extends ZMDialogFragment {
    private static final String ARGS_HISTORY = "args_history";
    private static final String ARGS_REASON = "args_reason";
    /* access modifiers changed from: private */
    public PBXCallHistory mPBXCallHistory;
    /* access modifiers changed from: private */
    public PhonePBXBlockReasonItem mReason;

    public interface ClickListener {
        void onCancel();

        void onConfirm();
    }

    public static void showInActivity(ZMActivity zMActivity, PBXCallHistory pBXCallHistory) {
        if (zMActivity != null && pBXCallHistory != null) {
            PBXBlockNumberDialogFragment pBXBlockNumberDialogFragment = new PBXBlockNumberDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARGS_HISTORY, pBXCallHistory);
            pBXBlockNumberDialogFragment.setArguments(bundle);
            pBXBlockNumberDialogFragment.show(zMActivity.getSupportFragmentManager(), PBXBlockNumberDialogFragment.class.getName());
        }
    }

    public static void showInActivity(ZMActivity zMActivity, PBXCallHistory pBXCallHistory, PhonePBXBlockReasonItem phonePBXBlockReasonItem) {
        if (zMActivity != null && pBXCallHistory != null && phonePBXBlockReasonItem != null) {
            PBXBlockNumberDialogFragment pBXBlockNumberDialogFragment = new PBXBlockNumberDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARGS_HISTORY, pBXCallHistory);
            bundle.putParcelable(ARGS_REASON, phonePBXBlockReasonItem);
            pBXBlockNumberDialogFragment.setArguments(bundle);
            pBXBlockNumberDialogFragment.show(zMActivity.getSupportFragmentManager(), PBXBlockNumberDialogFragment.class.getName());
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments == null) {
            dismiss();
            return;
        }
        this.mPBXCallHistory = (PBXCallHistory) arguments.getParcelable(ARGS_HISTORY);
        this.mReason = (PhonePBXBlockReasonItem) arguments.getParcelable(ARGS_REASON);
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        ZMAlertDialog zMAlertDialog;
        if (getContext() == null) {
            dismiss();
            return super.onCreateDialog(bundle);
        }
        if (this.mReason == null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(getString(C4558R.string.zm_sip_block_number_reason_spam_125232));
            arrayList.add(getString(C4558R.string.zm_sip_block_number_reason_other_125232));
            for (String str : arrayList) {
                PhonePBXBlockReasonItem phonePBXBlockReasonItem = new PhonePBXBlockReasonItem();
                phonePBXBlockReasonItem.setLabel(str);
                zMMenuAdapter.addItem(phonePBXBlockReasonItem);
            }
            zMAlertDialog = new Builder(getContext()).setTitle((CharSequence) getContext().getString(C4558R.string.zm_sip_block_number_choose_reason_title_125232)).setAdapter(zMMenuAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Object item = zMMenuAdapter.getItem(i);
                    if (item != null && (item instanceof PhonePBXBlockReasonItem)) {
                        PBXBlockNumberDialogFragment.showInActivity((ZMActivity) PBXBlockNumberDialogFragment.this.getContext(), PBXBlockNumberDialogFragment.this.mPBXCallHistory, (PhonePBXBlockReasonItem) item);
                    }
                }
            }).create();
            zMAlertDialog.setCanceledOnTouchOutside(true);
        } else {
            String displayNameAndNumber = this.mPBXCallHistory.getDisplayNameAndNumber();
            String string = getContext().getString(C4558R.string.zm_sip_block_number_title_125232, new Object[]{displayNameAndNumber});
            zMAlertDialog = new Builder(getContext()).setTitle((CharSequence) string).setMessage(getContext().getString(C4558R.string.zm_sip_block_number_nodid_message_125232)).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_sip_block_number_button_125232, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String label = PBXBlockNumberDialogFragment.this.mReason.getLabel();
                    if (NetworkUtil.hasDataNetwork(PBXBlockNumberDialogFragment.this.getContext())) {
                        CmmPBXCallHistoryManager.getInstance().blockPhoneNumber(PBXBlockNumberDialogFragment.this.mPBXCallHistory.number, label);
                    }
                }
            }).create();
            zMAlertDialog.setCanceledOnTouchOutside(true);
        }
        return zMAlertDialog;
    }

    public void onCancel(@NonNull DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
    }
}
