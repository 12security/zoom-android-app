package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.fragment.MMNotificationExceptionGroupFragment;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettingItem;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettings;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMNotificationExceptionGroupListView */
public class MMNotificationExceptionGroupListView extends ListView implements OnClickListener {
    private MMNotificationExceptionGroupAdapter mAdapter;
    private View mLineHeadDivider;
    private View mPaneResetAll;
    private MMNotificationExceptionGroupFragment mParentFragment;
    private View mTxtExceptionGroups;

    /* renamed from: com.zipow.videobox.view.mm.MMNotificationExceptionGroupListView$MMNotificationExceptionGroupAdapter */
    static class MMNotificationExceptionGroupAdapter extends BaseAdapter {
        private Context mContext;
        @NonNull
        private List<MMZoomGroup> mData = new ArrayList();

        public long getItemId(int i) {
            return 0;
        }

        public MMNotificationExceptionGroupAdapter(Context context) {
            this.mContext = context;
        }

        public void setGroups(@Nullable List<MMZoomGroup> list) {
            if (list != null) {
                this.mData.clear();
                for (MMZoomGroup add : list) {
                    this.mData.add(add);
                }
            }
        }

        public void notifyDataSetChanged() {
            Collections.sort(this.mData, new MMZoomGroupComparator(CompatUtils.getLocalDefault()));
            super.notifyDataSetChanged();
        }

        public int getCount() {
            return this.mData.size();
        }

        @Nullable
        public MMZoomGroup getItem(int i) {
            return (MMZoomGroup) this.mData.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            MMZoomGroup item = getItem(i);
            if (item == null) {
                return null;
            }
            if (view == null) {
                view = View.inflate(this.mContext, C4558R.layout.zm_notification_group_item, null);
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtGroupName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtNotifyDes);
            View findViewById = view.findViewById(C4558R.C4560id.listDivider);
            textView.setText(String.format("%s(%d)", new Object[]{item.getGroupName(), Integer.valueOf(item.getMemberCount())}));
            findViewById.setBackgroundResource(i == getCount() - 1 ? C4558R.C4559drawable.zm_settings_bottom_divider : C4558R.C4559drawable.zm_settings_center_divider);
            switch (item.getNotifyType()) {
                case 1:
                    textView2.setText(C4558R.string.zm_lbl_notification_all_msg_19898);
                    break;
                case 2:
                    textView2.setText(C4558R.string.zm_lbl_notification_private_msg_19898);
                    break;
                case 3:
                    textView2.setText(C4558R.string.zm_lbl_notification_nothing_19898);
                    break;
                default:
                    textView2.setText("");
                    break;
            }
            return view;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMNotificationExceptionGroupListView$ResetAllGroupNotificationDialog */
    public static class ResetAllGroupNotificationDialog extends ZMDialogFragment {
        /* access modifiers changed from: private */
        public DialogInterface.OnClickListener mOnClickListener;

        @NonNull
        public static ResetAllGroupNotificationDialog showResetAllGroupNotificationDialog(ZMActivity zMActivity) {
            ResetAllGroupNotificationDialog resetAllGroupNotificationDialog = new ResetAllGroupNotificationDialog();
            resetAllGroupNotificationDialog.setArguments(new Bundle());
            resetAllGroupNotificationDialog.show(zMActivity.getSupportFragmentManager(), ResetAllGroupNotificationDialog.class.getName());
            return resetAllGroupNotificationDialog;
        }

        @Nullable
        public static ResetAllGroupNotificationDialog getResetAllGroupNotificationDialog(FragmentManager fragmentManager) {
            return (ResetAllGroupNotificationDialog) fragmentManager.findFragmentByTag(ResetAllGroupNotificationDialog.class.getName());
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            return new Builder(getActivity()).setTitle(C4558R.string.zm_lbl_notification_reset_exception_group_des_19898).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).setPositiveButton(C4558R.string.zm_btn_confirm_19898, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (ResetAllGroupNotificationDialog.this.mOnClickListener != null) {
                        ResetAllGroupNotificationDialog.this.mOnClickListener.onClick(dialogInterface, i);
                    }
                }
            }).create();
        }

        public void setOnDialogClickListener(DialogInterface.OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
        }
    }

    public MMNotificationExceptionGroupListView(Context context) {
        super(context);
        init();
    }

    public MMNotificationExceptionGroupListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMNotificationExceptionGroupListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        initHeadView();
        initFooterView();
        this.mAdapter = new MMNotificationExceptionGroupAdapter(getContext());
        setAdapter(this.mAdapter);
    }

    public void setParentFragment(MMNotificationExceptionGroupFragment mMNotificationExceptionGroupFragment) {
        this.mParentFragment = mMNotificationExceptionGroupFragment;
    }

    public void updateData() {
        List loadAllGroups = loadAllGroups();
        this.mAdapter.setGroups(loadAllGroups);
        if (CollectionsUtil.isListEmpty(loadAllGroups)) {
            this.mLineHeadDivider.setVisibility(8);
            this.mTxtExceptionGroups.setVisibility(8);
            this.mPaneResetAll.setVisibility(8);
        } else {
            this.mLineHeadDivider.setVisibility(0);
            this.mTxtExceptionGroups.setVisibility(0);
            this.mPaneResetAll.setVisibility(0);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    @Nullable
    private List<MMZoomGroup> loadAllGroups() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr == null) {
            return null;
        }
        MUCNotifySettings mUCSettings = notificationSettingMgr.getMUCSettings();
        if (mUCSettings == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < mUCSettings.getItemsCount(); i++) {
            MUCNotifySettingItem items = mUCSettings.getItems(i);
            ZoomGroup groupById = zoomMessenger.getGroupById(items.getSessionId());
            if (groupById != null) {
                MMZoomGroup initWithZoomGroup = MMZoomGroup.initWithZoomGroup(groupById);
                initWithZoomGroup.setNotifyType(items.getType());
                arrayList.add(initWithZoomGroup);
            }
        }
        return arrayList;
    }

    private void initHeadView() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_notification_exception_group_head, null);
        inflate.findViewById(C4558R.C4560id.panelAddGroup).setOnClickListener(this);
        this.mLineHeadDivider = inflate.findViewById(C4558R.C4560id.lineHeadDivider);
        this.mTxtExceptionGroups = inflate.findViewById(C4558R.C4560id.txtExceptionGroups);
        addHeaderView(inflate);
    }

    private void initFooterView() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_notification_exception_group_foot, null);
        this.mPaneResetAll = inflate.findViewById(C4558R.C4560id.paneResetAll);
        this.mPaneResetAll.setOnClickListener(this);
        addFooterView(inflate);
    }

    @Nullable
    public MMZoomGroup getItem(int i) {
        return this.mAdapter.getItem(i - getHeaderViewsCount());
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.panelAddGroup) {
            onClickPanelAddGroup();
        } else if (id == C4558R.C4560id.paneResetAll) {
            onClickPaneResetAll();
        }
    }

    private void onClickPaneResetAll() {
        ResetAllGroupNotificationDialog.showResetAllGroupNotificationDialog((ZMActivity) getContext()).setOnDialogClickListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                    if (notificationSettingMgr != null) {
                        MUCNotifySettings mUCSettings = notificationSettingMgr.getMUCSettings();
                        if (mUCSettings != null) {
                            ArrayList arrayList = new ArrayList();
                            for (MUCNotifySettingItem sessionId : mUCSettings.getItemsList()) {
                                arrayList.add(sessionId.getSessionId());
                            }
                            if (arrayList.size() != 0) {
                                notificationSettingMgr.resetMUCSettings(arrayList);
                                MMNotificationExceptionGroupListView.this.updateData();
                            }
                        }
                    }
                }
            }
        });
    }

    private void onClickPanelAddGroup() {
        MMNotificationExceptionGroupFragment mMNotificationExceptionGroupFragment = this.mParentFragment;
        if (mMNotificationExceptionGroupFragment != null) {
            mMNotificationExceptionGroupFragment.addExceptionGroup();
        }
    }
}
