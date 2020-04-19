package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.RecordMgr;
import com.zipow.videobox.dialog.ConfirmStopRecordDialog;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class RecordControlDialog extends ZMDialogFragment implements OnClickListener {
    private View mPauseRecord;
    private View mResumeRecord;
    private View mStopRecord;

    public static void show(FragmentManager fragmentManager) {
        RecordControlDialog recordControlDialog = new RecordControlDialog();
        recordControlDialog.setArguments(new Bundle());
        recordControlDialog.show(fragmentManager, RecordControlDialog.class.getName());
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setCancelable(true).setView(createContent()).setTheme(C4558R.style.ZMDialog_Material_Transparent).create();
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_record_control, null);
        this.mPauseRecord = inflate.findViewById(C4558R.C4560id.btnPauseRecord);
        this.mStopRecord = inflate.findViewById(C4558R.C4560id.btnStopRecord);
        this.mResumeRecord = inflate.findViewById(C4558R.C4560id.btnResumeRecord);
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr == null || !recordMgr.isCMRPaused()) {
            this.mPauseRecord.setVisibility(0);
            this.mResumeRecord.setVisibility(8);
            this.mPauseRecord.setOnClickListener(this);
        } else {
            this.mPauseRecord.setVisibility(8);
            this.mResumeRecord.setVisibility(0);
            this.mResumeRecord.setOnClickListener(this);
        }
        this.mStopRecord.setOnClickListener(this);
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnPauseRecord) {
            ConfLocalHelper.pauseRecord();
        } else if (id == C4558R.C4560id.btnStopRecord) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                ConfirmStopRecordDialog.showConfirmStopRecordDialog(zMActivity);
            }
        } else if (id == C4558R.C4560id.btnResumeRecord) {
            ConfLocalHelper.resumeRecord();
        }
        dismiss();
    }

    public void dismiss() {
        finishFragment(true);
    }
}
