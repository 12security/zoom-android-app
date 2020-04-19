package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.UnSupportEmojiDialog */
public class UnSupportEmojiDialog extends ZMDialogFragment {
    private CheckBox mChkNoMoreShow;

    public static UnSupportEmojiDialog getUnSupportEmojiDialog(ZMActivity zMActivity) {
        return (UnSupportEmojiDialog) zMActivity.getSupportFragmentManager().findFragmentByTag(UnSupportEmojiDialog.class.getName());
    }

    public static void show(final ZMActivity zMActivity) {
        if (PreferenceUtil.readIntValue(PreferenceUtil.UN_SUPPORT_EMOJI_DIALOG_SHOW_TIMES, 0) >= 0 && zMActivity != null) {
            EventTaskManager eventTaskManager = zMActivity.getEventTaskManager();
            if (eventTaskManager != null) {
                eventTaskManager.push(UnSupportEmojiDialog.class.getName(), new EventAction() {
                    public void run(IUIElement iUIElement) {
                        if (UnSupportEmojiDialog.getUnSupportEmojiDialog(zMActivity) == null && PreferenceUtil.readIntValue(PreferenceUtil.UN_SUPPORT_EMOJI_DIALOG_SHOW_TIMES, 0) >= 0) {
                            UnSupportEmojiDialog unSupportEmojiDialog = new UnSupportEmojiDialog();
                            unSupportEmojiDialog.setArguments(new Bundle());
                            unSupportEmojiDialog.show(((ZMActivity) iUIElement).getSupportFragmentManager(), UnSupportEmojiDialog.class.getName());
                        }
                    }
                });
            }
        }
    }

    public void onStart() {
        super.onStart();
    }

    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = View.inflate(getActivity(), C4558R.layout.zm_mm_unsupport_emoji_dialog_view, null);
        int readIntValue = PreferenceUtil.readIntValue(PreferenceUtil.UN_SUPPORT_EMOJI_DIALOG_SHOW_TIMES, 0);
        this.mChkNoMoreShow = (CheckBox) inflate.findViewById(C4558R.C4560id.chkNoMoreShow);
        if (readIntValue == 0) {
            this.mChkNoMoreShow.setVisibility(8);
        }
        PreferenceUtil.saveIntValue(PreferenceUtil.UN_SUPPORT_EMOJI_DIALOG_SHOW_TIMES, 1);
        return new Builder(getActivity()).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).setPositiveButton(C4558R.string.zm_btn_download, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferenceUtil.saveIntValue(PreferenceUtil.UN_SUPPORT_EMOJI_DIALOG_SHOW_TIMES, -1);
                CommonEmojiHelper.getInstance().installEmoji();
            }
        }).create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        CheckBox checkBox = this.mChkNoMoreShow;
        if (checkBox != null && checkBox.isChecked()) {
            PreferenceUtil.saveIntValue(PreferenceUtil.UN_SUPPORT_EMOJI_DIALOG_SHOW_TIMES, -1);
        }
    }
}
