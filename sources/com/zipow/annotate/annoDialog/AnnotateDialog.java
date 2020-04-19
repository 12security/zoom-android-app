package com.zipow.annotate.annoDialog;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateDialog extends AnnotateDialogFragment {
    private static int ANNOTATE_TIMER_ERROR_DIALOG = 2000;
    private static int ANNOTATE_TIMER_SAVE_DIALOG = 1500;
    private View convertView;
    private boolean mBShowErrorDialog;
    private Handler mHandler = new Handler();
    private TextView mIcon;
    private TextView mText;
    private Runnable mTimer = new Runnable() {
        public void run() {
            AnnotateDialog.this.dismiss();
        }
    };

    public enum AnnoDialogModel {
        ANNO_DIALOG_ERROR,
        ANNO_DIALOG_CONFIRM
    }

    @NonNull
    public static AnnotateDialog getInstance(FragmentManager fragmentManager) {
        AnnotateDialog annotateDialog = (AnnotateDialog) fragmentManager.findFragmentByTag(AnnotateDialog.class.getName());
        return annotateDialog == null ? new AnnotateDialog() : annotateDialog;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = this.convertView;
        if (view == null) {
            this.convertView = layoutInflater.inflate(C4558R.layout.zm_anno_dialog, viewGroup, false);
            this.mIcon = (TextView) this.convertView.findViewById(C4558R.C4560id.id_wb_dialog_icon);
            this.mText = (TextView) this.convertView.findViewById(C4558R.C4560id.id_wb_dialog_title);
            if (this.mBShowErrorDialog) {
                Drawable drawable = getResources().getDrawable(C4558R.C4559drawable.zm_anno_wb_error);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                this.mIcon.setCompoundDrawables(drawable, null, null, null);
                this.mText.setText(C4558R.string.zm_anno_dialog_error_tip_46296);
                startTimer(ANNOTATE_TIMER_ERROR_DIALOG);
            } else {
                Drawable drawable2 = getResources().getDrawable(C4558R.C4559drawable.zm_anno_wb_confirm);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                this.mIcon.setCompoundDrawables(drawable2, null, null, null);
                this.mText.setText(C4558R.string.zm_anno_save_to_photos_46296);
                startTimer(ANNOTATE_TIMER_SAVE_DIALOG);
            }
        } else {
            ViewManager viewManager = (ViewManager) view.getParent();
            if (viewManager != null) {
                viewManager.removeView(this.convertView);
            }
        }
        return this.convertView;
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    public void setIsShowErrowDialog(boolean z) {
        this.mBShowErrorDialog = z;
    }

    private void startTimer(int i) {
        this.mHandler.removeCallbacks(this.mTimer);
        this.mHandler.postDelayed(this.mTimer, (long) i);
    }
}
