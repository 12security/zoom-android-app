package com.zipow.videobox.dialog;

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
import com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMChoiceAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class ChooseHostDialog extends ZMDialogFragment {
    private static final String ARG_PERSON_ID = "ARG_PERSON_ID";
    private ZMChoiceAdapter<MeetingHostByItem> mAdapter;
    private String mPersionId;
    private OnItemSelectedListener onItemSelectedListener;

    public class MeetingHostByItem extends ZMSimpleMenuItem {
        private String personId;

        public MeetingHostByItem() {
        }

        public MeetingHostByItem(String str, String str2, Drawable drawable) {
            this.personId = str2;
            super.setLabel(str);
            super.setIcon(drawable);
        }

        public String getPersonId() {
            return this.personId;
        }

        public void setPersonId(String str) {
            this.personId = str;
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(MeetingHostByItem meetingHostByItem);
    }

    public static void show(@NonNull FragmentManager fragmentManager, String str, OnItemSelectedListener onItemSelectedListener2) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PERSON_ID, str);
        ChooseHostDialog chooseHostDialog = new ChooseHostDialog();
        chooseHostDialog.setArguments(bundle);
        if (onItemSelectedListener2 != null) {
            chooseHostDialog.setOnItemSelectedListener(onItemSelectedListener2);
        }
        chooseHostDialog.show(fragmentManager, ChooseHostDialog.class.getName());
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ChooseHostDialog chooseHostDialog = (ChooseHostDialog) fragmentManager.findFragmentByTag(ChooseHostDialog.class.getName());
            if (chooseHostDialog != null) {
                chooseHostDialog.dismiss();
            }
        }
    }

    public ChooseHostDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mPersionId = arguments.getString(ARG_PERSON_ID, "");
        }
        this.mAdapter = createUpdateAdapter(activity);
        ZMAlertDialog create = new Builder(activity).setTitle((CharSequence) getString(C4558R.string.zm_lbl_host_by_title_101105, "")).setTitleFontSize(18.0f).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChooseHostDialog.this.onSelectItem(i);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener2) {
        this.onItemSelectedListener = onItemSelectedListener2;
    }

    private ZMChoiceAdapter<MeetingHostByItem> createUpdateAdapter(@NonNull Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new MeetingHostByItem(context.getString(C4558R.string.zm_lbl_everyone_101105), "", null));
        if (PTApp.getInstance().isWebSignedOn()) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                arrayList.add(new MeetingHostByItem(context.getString(C4558R.string.zm_lbl_content_me), currentUserProfile.getUserID(), null));
            }
        }
        PTApp instance = PTApp.getInstance();
        int altHostCount = instance.getAltHostCount();
        for (int i = 0; i < altHostCount; i++) {
            AlterHost altHostAt = instance.getAltHostAt(i);
            if (altHostAt != null) {
                arrayList.add(new MeetingHostByItem(StringUtil.formatPersonName(altHostAt.getFirstName(), altHostAt.getLastName(), PTApp.getInstance().getRegionCodeForNameFormating()), altHostAt.getHostID(), null));
            }
        }
        Iterator it = arrayList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            MeetingHostByItem meetingHostByItem = (MeetingHostByItem) it.next();
            if (this.mPersionId.equalsIgnoreCase(meetingHostByItem.getPersonId())) {
                meetingHostByItem.setSelected(true);
                break;
            }
        }
        ZMChoiceAdapter<MeetingHostByItem> zMChoiceAdapter = this.mAdapter;
        if (zMChoiceAdapter == null) {
            this.mAdapter = new ZMChoiceAdapter<>(getActivity(), C4558R.C4559drawable.zm_group_type_select, context.getString(C4558R.string.zm_accessibility_icon_item_selected_19247), 16.0f);
        } else {
            zMChoiceAdapter.clear();
        }
        this.mAdapter.addAll((List<MenuItemType>) arrayList);
        return this.mAdapter;
    }

    /* access modifiers changed from: private */
    public void onSelectItem(int i) {
        MeetingHostByItem meetingHostByItem = (MeetingHostByItem) this.mAdapter.getItem(i);
        if (meetingHostByItem != null) {
            OnItemSelectedListener onItemSelectedListener2 = this.onItemSelectedListener;
            if (onItemSelectedListener2 != null) {
                onItemSelectedListener2.onItemSelected(meetingHostByItem);
            }
        }
    }
}
