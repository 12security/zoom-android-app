package com.zipow.videobox.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfAppProtos.VanityURLInfo;
import com.zipow.videobox.confapp.ConfAppProtos.VanityURLInfoList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class SelectPersonalLinkToJoinDialog extends ZMDialogFragment {
    private static final String TAG = "SelectPersonalLinkToJoinDialog";
    private ZMMenuAdapter<VanityURLInfoItem> mAdapter;

    public class VanityURLInfoItem extends ZMSimpleMenuItem {
        private VanityURLInfo vanityURLInfo;

        public VanityURLInfoItem() {
        }

        public VanityURLInfoItem(VanityURLInfo vanityURLInfo2, Drawable drawable) {
            this.vanityURLInfo = vanityURLInfo2;
            super.setLabel(vanityURLInfo2.getVanityURL());
            super.setIcon(drawable);
        }

        public VanityURLInfo getVanityURLInfo() {
            return this.vanityURLInfo;
        }

        public void setVanityURLInfo(VanityURLInfo vanityURLInfo2) {
            this.vanityURLInfo = vanityURLInfo2;
        }
    }

    public static void show(@NonNull FragmentManager fragmentManager) {
        new SelectPersonalLinkToJoinDialog().show(fragmentManager, SelectPersonalLinkToJoinDialog.class.getName());
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            SelectPersonalLinkToJoinDialog selectPersonalLinkToJoinDialog = (SelectPersonalLinkToJoinDialog) fragmentManager.findFragmentByTag(SelectPersonalLinkToJoinDialog.class.getName());
            if (selectPersonalLinkToJoinDialog != null) {
                selectPersonalLinkToJoinDialog.dismiss();
            }
        }
    }

    public SelectPersonalLinkToJoinDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        final FragmentActivity activity = getActivity();
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        String str = "";
        if (confContext != null) {
            str = confContext.getVanityMeetingID();
        }
        String string = getString(C4558R.string.zm_lbl_select_personal_link_title_100629, str);
        this.mAdapter = createUpdateAdapter();
        return new Builder(activity).setTitle((CharSequence) string).setSmallTitleMutlilineStyle(true).setTitleFontSize(13.0f).setTitleTxtColor(getResources().getColor(C4558R.color.zm_ui_kit_color_gray_747487)).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SelectPersonalLinkToJoinDialog.this.onSelectItem(i);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                Activity activity = activity;
                if (activity instanceof ConfActivity) {
                    ConfLocalHelper.endCall((ConfActivity) activity);
                }
            }
        }).create();
    }

    private ZMMenuAdapter<VanityURLInfoItem> createUpdateAdapter() {
        ArrayList arrayList = new ArrayList();
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return null;
        }
        VanityURLInfoList multiVanityURLs = confContext.getMultiVanityURLs();
        if (multiVanityURLs == null) {
            return null;
        }
        List<VanityURLInfo> vanityURLInfosList = multiVanityURLs.getVanityURLInfosList();
        if (vanityURLInfosList == null || vanityURLInfosList.size() == 0) {
            return null;
        }
        for (VanityURLInfo vanityURLInfo : vanityURLInfosList) {
            VanityURLInfoItem vanityURLInfoItem = new VanityURLInfoItem(vanityURLInfo, null);
            if (vanityURLInfo.getSameAccount()) {
                String string = getString(C4558R.string.zm_lbl_your_company_100629);
                StringBuilder sb = new StringBuilder();
                sb.append(vanityURLInfoItem.getLabel());
                sb.append(OAuth.SCOPE_DELIMITER);
                sb.append(string);
                vanityURLInfoItem.setLabel(sb.toString());
            }
            arrayList.add(vanityURLInfoItem);
        }
        ZMMenuAdapter<VanityURLInfoItem> zMMenuAdapter = this.mAdapter;
        if (zMMenuAdapter == null) {
            this.mAdapter = new ZMMenuAdapter<>(getActivity(), true, 16.0f);
        } else {
            zMMenuAdapter.clear();
        }
        this.mAdapter.addAll((List<MenuItemType>) arrayList);
        return this.mAdapter;
    }

    /* access modifiers changed from: private */
    public void onSelectItem(int i) {
        ConfMgr.getInstance().onUserConfirmOptionalVanityURLs(((VanityURLInfoItem) this.mAdapter.getItem(i)).getVanityURLInfo().getMeetingNO());
    }
}
