package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class SwitchAudioSourceDialog extends ZMDialogFragment implements IHeadsetConnectionListener {
    @Nullable
    private ConfActivity mConfActivity;
    /* access modifiers changed from: private */
    public ZMMenuAdapter<SwitchAudioSourceMenuItem> mMenuAdapter;

    static class SwitchAudioSourceMenuItem extends ZMSimpleMenuItem {
        public SwitchAudioSourceMenuItem(int i, String str, boolean z) {
            super(i, str, null, z);
        }
    }

    public static void showDialog(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            new SwitchAudioSourceDialog().show(fragmentManager, SwitchAudioSourceDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        final ConfActivity confActivity = getConfActivity();
        if (confActivity == null) {
            return createEmptyDialog();
        }
        this.mMenuAdapter = new ZMMenuAdapter<>(confActivity, false);
        this.mMenuAdapter.setShowSelectedStatus(true);
        ArrayList buildMenuItems = buildMenuItems();
        if (buildMenuItems == null) {
            return createEmptyDialog();
        }
        this.mMenuAdapter.addAll((List<MenuItemType>) buildMenuItems);
        ZMAlertDialog create = new Builder(confActivity).setTitle(C4558R.string.zm_btn_switch_audio_source).setAdapter(this.mMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SwitchAudioSourceDialog.this.onSelectContextMenuItem((SwitchAudioSourceMenuItem) SwitchAudioSourceDialog.this.mMenuAdapter.getItem(i), confActivity);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private ArrayList<SwitchAudioSourceMenuItem> buildMenuItems() {
        ArrayList<SwitchAudioSourceMenuItem> arrayList = new ArrayList<>();
        if (ConfMgr.getInstance().getMyself() == null || ConfMgr.getInstance().getAudioObj() == null) {
            return null;
        }
        int currentAudioSourceType = ConfUI.getInstance().getCurrentAudioSourceType();
        int switchableAudioSourceType = ConfUI.getInstance().getSwitchableAudioSourceType();
        if (currentAudioSourceType != 0) {
            arrayList.add(new SwitchAudioSourceMenuItem(0, getString(C4558R.string.zm_mi_speaker_phone), false));
            switch (currentAudioSourceType) {
                case 1:
                    arrayList.add(new SwitchAudioSourceMenuItem(currentAudioSourceType, getString(C4558R.string.zm_mi_ear_phone), true));
                    break;
                case 2:
                    arrayList.add(new SwitchAudioSourceMenuItem(currentAudioSourceType, getString(C4558R.string.zm_mi_wired_headset), true));
                    break;
                case 3:
                    arrayList.add(new SwitchAudioSourceMenuItem(currentAudioSourceType, getString(C4558R.string.zm_mi_bluetooth), true));
                    break;
            }
        } else {
            arrayList.add(new SwitchAudioSourceMenuItem(0, getString(C4558R.string.zm_mi_speaker_phone), true));
            switch (switchableAudioSourceType) {
                case 1:
                    arrayList.add(new SwitchAudioSourceMenuItem(switchableAudioSourceType, getString(C4558R.string.zm_mi_ear_phone), false));
                    break;
                case 2:
                    arrayList.add(new SwitchAudioSourceMenuItem(switchableAudioSourceType, getString(C4558R.string.zm_mi_wired_headset), false));
                    break;
                case 3:
                    arrayList.add(new SwitchAudioSourceMenuItem(switchableAudioSourceType, getString(C4558R.string.zm_mi_bluetooth), false));
                    break;
            }
        }
        return arrayList;
    }

    @Nullable
    private ConfActivity getConfActivity() {
        if (this.mConfActivity == null) {
            this.mConfActivity = (ConfActivity) getActivity();
        }
        return this.mConfActivity;
    }

    public void onResume() {
        super.onResume();
        HeadsetUtil.getInstance().addListener(this);
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            if (!confActivity.canSwitchAudioSource()) {
                dismiss();
            }
            updateMenuAdapter();
        }
    }

    private void updateMenuAdapter() {
        ZMMenuAdapter<SwitchAudioSourceMenuItem> zMMenuAdapter = this.mMenuAdapter;
        if (zMMenuAdapter != null) {
            zMMenuAdapter.clear();
            ArrayList buildMenuItems = buildMenuItems();
            if (buildMenuItems != null) {
                this.mMenuAdapter.addAll((List<MenuItemType>) buildMenuItems);
            }
            this.mMenuAdapter.notifyDataSetChanged();
        }
    }

    public void onPause() {
        super.onPause();
        HeadsetUtil.getInstance().removeListener(this);
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
        updateMenuAdapter();
    }

    public void onBluetoothScoAudioStatus(boolean z) {
        updateMenuAdapter();
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(SwitchAudioSourceMenuItem switchAudioSourceMenuItem, @NonNull ConfActivity confActivity) {
        if (switchAudioSourceMenuItem.getAction() != ConfUI.getInstance().getCurrentAudioSourceType()) {
            ConfLocalHelper.switchAudioSource(confActivity);
        }
    }
}
