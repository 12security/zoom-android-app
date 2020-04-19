package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMChoiceAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class HostControlAccessDialog extends ZMDialogFragment {
    private static final String CURRENT_PRIVILEDGE = "CURRENT_PRIVILEDGE";
    private static final String MODE = "MODE";
    public static final int MODE_MEETING = 0;
    public static final int MODE_WEBINAR = 1;
    private ZMChoiceAdapter<ZMSimpleMenuItem> mAdapter;
    private int mCurPrivilege;
    private int mMode;

    public static void show(@NonNull FragmentManager fragmentManager, int i, int i2) {
        Bundle bundle = new Bundle();
        bundle.putInt(MODE, i);
        bundle.putInt(CURRENT_PRIVILEDGE, i2);
        HostControlAccessDialog hostControlAccessDialog = new HostControlAccessDialog();
        hostControlAccessDialog.setArguments(bundle);
        hostControlAccessDialog.show(fragmentManager, HostControlAccessDialog.class.getName());
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            HostControlAccessDialog hostControlAccessDialog = (HostControlAccessDialog) fragmentManager.findFragmentByTag(HostControlAccessDialog.class.getName());
            if (hostControlAccessDialog != null) {
                hostControlAccessDialog.dismiss();
            }
        }
    }

    public HostControlAccessDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mMode = arguments.getInt(MODE, 0);
            this.mCurPrivilege = arguments.getInt(CURRENT_PRIVILEDGE, 1);
        }
        this.mAdapter = createUpdateAdapter(activity);
        int i = C4558R.string.zm_mi_allow_participants_chat_75334;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.isWebinar()) {
            i = C4558R.string.zm_mi_allow_attendees_chat_75334;
        }
        ZMAlertDialog create = new Builder(activity).setTitle(i).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                HostControlAccessDialog.this.onSelectItem(i);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private ZMChoiceAdapter<ZMSimpleMenuItem> createUpdateAdapter(@NonNull Context context) {
        ArrayList arrayList = new ArrayList();
        if (this.mMode == 0) {
            arrayList.add(new ZMSimpleMenuItem(context.getString(C4558R.string.zm_mi_no_one_65892), (Drawable) null));
            arrayList.add(new ZMSimpleMenuItem(context.getString(C4558R.string.zm_mi_host_only_11380), (Drawable) null));
            arrayList.add(new ZMSimpleMenuItem(context.getString(C4558R.string.zm_mi_host_and_public_65892), (Drawable) null));
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && !confContext.isPrivateChatOFF()) {
                arrayList.add(new ZMSimpleMenuItem(context.getString(C4558R.string.zm_mi_everyone_122046), (Drawable) null));
            }
            int i = this.mCurPrivilege;
            if (i != 1) {
                switch (i) {
                    case 3:
                        ((ZMSimpleMenuItem) arrayList.get(1)).setSelected(true);
                        break;
                    case 4:
                        ((ZMSimpleMenuItem) arrayList.get(0)).setSelected(true);
                        break;
                    case 5:
                        ((ZMSimpleMenuItem) arrayList.get(2)).setSelected(true);
                        break;
                }
            } else if (confContext != null && !confContext.isPrivateChatOFF()) {
                ((ZMSimpleMenuItem) arrayList.get(3)).setSelected(true);
            }
        } else {
            arrayList.add(new ZMSimpleMenuItem(context.getString(C4558R.string.zm_mi_no_one_11380), (Drawable) null));
            arrayList.add(new ZMSimpleMenuItem(context.getString(C4558R.string.zm_webinar_txt_all_panelists), (Drawable) null));
            arrayList.add(new ZMSimpleMenuItem(context.getString(C4558R.string.zm_mi_panelists_and_attendees_11380), (Drawable) null));
            if (!ConfMgr.getInstance().isAllowAttendeeChat()) {
                ((ZMSimpleMenuItem) arrayList.get(0)).setSelected(true);
            } else if (this.mCurPrivilege == 2) {
                ((ZMSimpleMenuItem) arrayList.get(1)).setSelected(true);
            } else {
                ((ZMSimpleMenuItem) arrayList.get(2)).setSelected(true);
            }
        }
        ZMChoiceAdapter<ZMSimpleMenuItem> zMChoiceAdapter = this.mAdapter;
        if (zMChoiceAdapter == null) {
            this.mAdapter = new ZMChoiceAdapter<>(getActivity(), C4558R.C4559drawable.zm_group_type_select, context.getString(C4558R.string.zm_accessibility_icon_item_selected_19247));
        } else {
            zMChoiceAdapter.clear();
        }
        this.mAdapter.addAll((List<MenuItemType>) arrayList);
        return this.mAdapter;
    }

    /* access modifiers changed from: private */
    public void onSelectItem(int i) {
        ConfMgr instance = ConfMgr.getInstance();
        CmmConfStatus confStatusObj = instance.getConfStatusObj();
        if (confStatusObj != null) {
            if (this.mMode == 0) {
                switch (i) {
                    case 0:
                        confStatusObj.changeAttendeeChatPriviledge(4);
                        ZMConfEventTracking.eventTrackInMeetingSettingChatWith(53);
                        break;
                    case 1:
                        confStatusObj.changeAttendeeChatPriviledge(3);
                        ZMConfEventTracking.eventTrackInMeetingSettingChatWith(54);
                        break;
                    case 2:
                        confStatusObj.changeAttendeeChatPriviledge(5);
                        ZMConfEventTracking.eventTrackInMeetingSettingChatWith(55);
                        break;
                    case 3:
                        confStatusObj.changeAttendeeChatPriviledge(1);
                        ZMConfEventTracking.eventTrackInMeetingSettingChatWith(56);
                        break;
                }
            } else if (i == 0) {
                if (instance.isAllowAttendeeChat()) {
                    instance.handleConfCmd(117);
                }
                ZMConfEventTracking.eventTrackInMeetingSettingChatWith(53);
            } else if (i == 1) {
                if (!instance.isAllowAttendeeChat()) {
                    instance.handleConfCmd(116);
                }
                confStatusObj.changeAttendeeChatPriviledge(2);
                ZMConfEventTracking.eventTrackInMeetingSettingChatWith(57);
            } else {
                if (!instance.isAllowAttendeeChat()) {
                    instance.handleConfCmd(116);
                }
                confStatusObj.changeAttendeeChatPriviledge(1);
                ZMConfEventTracking.eventTrackInMeetingSettingChatWith(58);
            }
        }
    }
}
