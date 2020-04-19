package com.zipow.videobox.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.BuddyInviteActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.IMChatActivity;
import com.zipow.videobox.fragment.BuddyInviteFragment;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMSession;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTBuddyHelper;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class IMBuddyListView extends ListView implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = "IMBuddyListView";
    private IMBuddyListAdapter mAdapter;
    @Nullable
    private String mFilter;
    private boolean mShowOfflineUser = true;

    public static class ContextMenuFragment extends ZMDialogFragment {
        private static final String ARG_BUDDYITEM = "buddyItem";
        public static final int MI_INVITE_AGAIN = 3;
        public static final int MI_PHONE_CALL = 1;
        public static final int MI_REMOVE_BUDDY = 2;
        public static final int MI_VIDEO_CALL = 0;
        private ZMMenuAdapter<ContextMenuItem> mAdapter;

        public static void show(FragmentManager fragmentManager, IMBuddyItem iMBuddyItem) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("buddyItem", iMBuddyItem);
            ContextMenuFragment contextMenuFragment = new ContextMenuFragment();
            contextMenuFragment.setArguments(bundle);
            contextMenuFragment.show(fragmentManager, ContextMenuFragment.class.getName());
        }

        public ContextMenuFragment() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            IMBuddyItem iMBuddyItem = (IMBuddyItem) arguments.getSerializable("buddyItem");
            this.mAdapter = createUpdateAdapter();
            ZMAlertDialog create = new Builder(getActivity()).setTitle((CharSequence) iMBuddyItem == null ? "" : iMBuddyItem.screenName).setAdapter(this.mAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ContextMenuFragment.this.onSelectItem(i);
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            return create;
        }

        private ZMMenuAdapter<ContextMenuItem> createUpdateAdapter() {
            ContextMenuItem[] contextMenuItemArr;
            Bundle arguments = getArguments();
            if (arguments == null) {
                return null;
            }
            boolean z = PTApp.getInstance().getPTLoginType() == 2;
            IMBuddyItem iMBuddyItem = (IMBuddyItem) arguments.getSerializable("buddyItem");
            int i = 3;
            if (z && iMBuddyItem != null && (iMBuddyItem.isPending || iMBuddyItem.isNoneFriend)) {
                contextMenuItemArr = new ContextMenuItem[]{new ContextMenuItem(2, getText(C4558R.string.zm_mi_remove_buddy).toString()), new ContextMenuItem(3, getText(C4558R.string.zm_mi_invite_again).toString())};
            } else if (isInCall()) {
                ContextMenuItem[] contextMenuItemArr2 = new ContextMenuItem[(z ? 2 : 1)];
                contextMenuItemArr2[0] = new ContextMenuItem(0, getText(getConfMenuItemText(iMBuddyItem)).toString());
                if (z) {
                    contextMenuItemArr2[1] = new ContextMenuItem(2, getText(C4558R.string.zm_mi_remove_buddy).toString());
                }
                contextMenuItemArr = contextMenuItemArr2;
            } else {
                if (!z) {
                    i = 2;
                }
                contextMenuItemArr = new ContextMenuItem[i];
                contextMenuItemArr[0] = new ContextMenuItem(0, getText(C4558R.string.zm_btn_video_call).toString());
                contextMenuItemArr[1] = new ContextMenuItem(1, getText(C4558R.string.zm_btn_audio_call).toString());
                if (z) {
                    contextMenuItemArr[2] = new ContextMenuItem(2, getText(C4558R.string.zm_mi_remove_buddy).toString());
                }
            }
            ZMMenuAdapter<ContextMenuItem> zMMenuAdapter = this.mAdapter;
            if (zMMenuAdapter == null) {
                this.mAdapter = new ZMMenuAdapter<>(getActivity(), false);
            } else {
                zMMenuAdapter.clear();
            }
            this.mAdapter.addAll((MenuItemType[]) contextMenuItemArr);
            return this.mAdapter;
        }

        public void refresh() {
            ZMMenuAdapter createUpdateAdapter = createUpdateAdapter();
            if (createUpdateAdapter != null) {
                createUpdateAdapter.notifyDataSetChanged();
            }
        }

        private int getConfMenuItemText(@Nullable IMBuddyItem iMBuddyItem) {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                    return C4558R.string.zm_mi_return_to_conf;
                case 2:
                    if (iMBuddyItem == null || !PTApp.getInstance().probeUserStatus(iMBuddyItem.userId)) {
                        return C4558R.string.zm_mi_invite_to_conf;
                    }
                    return C4558R.string.zm_mi_return_to_conf;
                default:
                    return C4558R.string.zm_mi_start_conf;
            }
        }

        private boolean isInCall() {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                case 2:
                    return true;
                default:
                    return false;
            }
        }

        /* access modifiers changed from: private */
        public void onSelectItem(int i) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                IMBuddyItem iMBuddyItem = (IMBuddyItem) arguments.getSerializable("buddyItem");
                switch (((ContextMenuItem) this.mAdapter.getItem(i)).getAction()) {
                    case 0:
                        onSelectVideoCall(iMBuddyItem);
                        break;
                    case 1:
                        onSelectPhoneCall(iMBuddyItem);
                        break;
                    case 2:
                        onSelectRemoveBuddy(iMBuddyItem);
                        break;
                    case 3:
                        onSelectInviteAgain(iMBuddyItem);
                        break;
                }
            }
        }

        private void onSelectVideoCall(@Nullable IMBuddyItem iMBuddyItem) {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                    returnToConf();
                    return;
                case 2:
                    if (iMBuddyItem == null || !PTApp.getInstance().probeUserStatus(iMBuddyItem.userId)) {
                        inviteToConf(iMBuddyItem);
                        return;
                    } else {
                        returnToConf();
                        return;
                    }
                default:
                    startConf(iMBuddyItem, true);
                    return;
            }
        }

        private void onSelectPhoneCall(@Nullable IMBuddyItem iMBuddyItem) {
            switch (PTApp.getInstance().getCallStatus()) {
                case 1:
                    returnToConf();
                    return;
                case 2:
                    if (iMBuddyItem == null || !PTApp.getInstance().probeUserStatus(iMBuddyItem.userId)) {
                        inviteToConf(iMBuddyItem);
                        return;
                    } else {
                        returnToConf();
                        return;
                    }
                default:
                    startConf(iMBuddyItem, false);
                    return;
            }
        }

        private void startConf(@Nullable IMBuddyItem iMBuddyItem, boolean z) {
            if (iMBuddyItem != null) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    int inviteToVideoCall = ConfActivity.inviteToVideoCall(activity, iMBuddyItem.userId, z ? 1 : 0);
                    if (inviteToVideoCall != 0) {
                        StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
                    }
                }
            }
        }

        private void inviteToConf(@Nullable IMBuddyItem iMBuddyItem) {
            if (iMBuddyItem != null) {
                PTApp instance = PTApp.getInstance();
                String activeCallId = instance.getActiveCallId();
                if (!StringUtil.isEmptyOrNull(activeCallId)) {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        if (instance.inviteBuddiesToConf(new String[]{iMBuddyItem.userId}, null, activeCallId, 0, activity.getString(C4558R.string.zm_msg_invitation_message_template)) == 0) {
                            ConfLocalHelper.returnToConf(activity);
                        }
                    }
                }
            }
        }

        private void returnToConf() {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                ConfLocalHelper.returnToConf(activity);
            }
        }

        private void onSelectRemoveBuddy(IMBuddyItem iMBuddyItem) {
            RemoveBuddyConfirmDialog.show(getFragmentManager(), iMBuddyItem);
        }

        private void onSelectInviteAgain(IMBuddyItem iMBuddyItem) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                IMBuddyListView.showBuddyInviteUI(zMActivity, iMBuddyItem);
            }
        }
    }

    static class ContextMenuItem extends ZMSimpleMenuItem {
        public ContextMenuItem(int i, String str) {
            super(i, str);
        }
    }

    public static class RemoveBuddyConfirmDialog extends ZMDialogFragment {
        private static final String ARG_BUDDYITEM = "buddyItem";

        public RemoveBuddyConfirmDialog() {
            setCancelable(true);
        }

        public static void show(FragmentManager fragmentManager, IMBuddyItem iMBuddyItem) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("buddyItem", iMBuddyItem);
            RemoveBuddyConfirmDialog removeBuddyConfirmDialog = new RemoveBuddyConfirmDialog();
            removeBuddyConfirmDialog.setArguments(bundle);
            removeBuddyConfirmDialog.show(fragmentManager, RemoveBuddyConfirmDialog.class.getName());
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return createEmptyDialog();
            }
            IMBuddyItem iMBuddyItem = (IMBuddyItem) arguments.getSerializable("buddyItem");
            int i = C4558R.string.zm_msg_remove_buddy_confirm;
            Object[] objArr = new Object[1];
            objArr[0] = iMBuddyItem == null ? "" : iMBuddyItem.screenName;
            return new Builder(getActivity()).setTitle((CharSequence) getString(i, objArr)).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    RemoveBuddyConfirmDialog.this.onClickOK();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).create();
        }

        /* access modifiers changed from: private */
        public void onClickOK() {
            Bundle arguments = getArguments();
            if (arguments != null) {
                IMBuddyItem iMBuddyItem = (IMBuddyItem) arguments.getSerializable("buddyItem");
                IMHelper iMHelper = PTApp.getInstance().getIMHelper();
                if (!(iMHelper == null || iMBuddyItem == null)) {
                    iMHelper.unsubscribeBuddy(iMBuddyItem.userId);
                }
            }
        }
    }

    public IMBuddyListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public IMBuddyListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMBuddyListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mAdapter = new IMBuddyListAdapter(getContext());
        if (isInEditMode()) {
            _editmode_loadAllBuddyItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
    }

    private void _editmode_loadAllBuddyItems(@NonNull IMBuddyListAdapter iMBuddyListAdapter) {
        for (int i = 0; i < 20; i++) {
            IMBuddyItem iMBuddyItem = new IMBuddyItem();
            StringBuilder sb = new StringBuilder();
            sb.append("Buddy ");
            sb.append(i);
            iMBuddyItem.screenName = sb.toString();
            iMBuddyItem.userId = String.valueOf(i);
            iMBuddyListAdapter.addItem(iMBuddyItem);
        }
    }

    private void loadAllBuddyItems(@NonNull IMBuddyListAdapter iMBuddyListAdapter) {
        this.mShowOfflineUser = PTSettingHelper.getShowOfflineBuddies();
        PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
        if (buddyHelper != null) {
            String str = this.mFilter;
            if (str == null || str.length() <= 0) {
                int buddyItemCount = buddyHelper.getBuddyItemCount();
                for (int i = 0; i < buddyItemCount; i++) {
                    BuddyItem buddyItem = buddyHelper.getBuddyItem(i);
                    if (buddyItem != null) {
                        int unreadMessageCount = getUnreadMessageCount(buddyItem.getJid());
                        if (this.mShowOfflineUser || buddyItem.getIsOnline() || unreadMessageCount > 0) {
                            iMBuddyListAdapter.addItem(new IMBuddyItem(buddyItem, unreadMessageCount));
                        }
                    }
                }
                iMBuddyListAdapter.sort(true);
            } else {
                String lowerCase = this.mFilter.toLowerCase(CompatUtils.getLocalDefault());
                int buddyItemCount2 = buddyHelper.getBuddyItemCount();
                for (int i2 = 0; i2 < buddyItemCount2; i2++) {
                    BuddyItem buddyItem2 = buddyHelper.getBuddyItem(i2);
                    if (buddyItem2 != null && buddyItem2.getScreenName().toLowerCase(CompatUtils.getLocalDefault()).indexOf(lowerCase) >= 0) {
                        iMBuddyListAdapter.addItem(new IMBuddyItem(buddyItem2));
                    }
                }
                iMBuddyListAdapter.sort(false);
            }
        }
    }

    private int getUnreadMessageCount(String str) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper == null) {
            return 0;
        }
        IMSession sessionBySessionName = iMHelper.getSessionBySessionName(str);
        if (sessionBySessionName == null) {
            return 0;
        }
        return sessionBySessionName.getUnreadMessageCount();
    }

    public void updateBuddyItem(@Nullable BuddyItem buddyItem) {
        if (buddyItem != null) {
            String str = this.mFilter;
            if (str == null || str.length() <= 0) {
                int unreadMessageCount = getUnreadMessageCount(buddyItem.getJid());
                if (this.mShowOfflineUser || buddyItem.getIsOnline() || unreadMessageCount > 0) {
                    this.mAdapter.updateItem(new IMBuddyItem(buddyItem, unreadMessageCount));
                    this.mAdapter.sort(true);
                } else {
                    this.mAdapter.removeItem(buddyItem.getJid());
                }
                this.mAdapter.notifyDataSetChanged();
            } else {
                String screenName = buddyItem.getScreenName();
                if (!StringUtil.isEmptyOrNull(screenName)) {
                    if (screenName.toLowerCase(CompatUtils.getLocalDefault()).indexOf(this.mFilter.toLowerCase(CompatUtils.getLocalDefault())) >= 0) {
                        this.mAdapter.updateItem(new IMBuddyItem(buddyItem, getUnreadMessageCount(buddyItem.getJid())));
                        this.mAdapter.sort(false);
                    } else {
                        this.mAdapter.removeItem(buddyItem.getJid());
                    }
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void updateBuddyItem(String str) {
        PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
        if (buddyHelper != null) {
            BuddyItem buddyItemByJid = buddyHelper.getBuddyItemByJid(str);
            if (buddyItemByJid != null) {
                updateBuddyItem(buddyItemByJid);
            }
        }
    }

    public void reloadAllBuddyItems() {
        this.mAdapter.clear();
        loadAllBuddyItems(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
    }

    public void sort() {
        IMBuddyListAdapter iMBuddyListAdapter = this.mAdapter;
        String str = this.mFilter;
        iMBuddyListAdapter.sort(str == null || str.length() == 0);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition instanceof IMBuddyItem) {
            IMBuddyItem iMBuddyItem = (IMBuddyItem) itemAtPosition;
            if (iMBuddyItem.isPending || iMBuddyItem.isNoneFriend) {
                showBuddyInviteUI((ZMActivity) getContext(), iMBuddyItem);
            } else {
                showChatUI(iMBuddyItem);
            }
        }
    }

    private void showChatUI(@Nullable IMBuddyItem iMBuddyItem) {
        if (iMBuddyItem != null) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                if (currentUserProfile != null) {
                    if (UIMgr.isLargeMode(zMActivity)) {
                        ((IMActivity) getContext()).showChatUI(iMBuddyItem);
                    } else {
                        Intent intent = new Intent(zMActivity, IMChatActivity.class);
                        intent.setFlags(131072);
                        intent.putExtra("buddyItem", iMBuddyItem);
                        intent.putExtra("myName", currentUserProfile.getUserName());
                        zMActivity.startActivityForResult(intent, zMActivity instanceof IMActivity ? 100 : 0);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void showBuddyInviteUI(@Nullable ZMActivity zMActivity, @Nullable IMBuddyItem iMBuddyItem) {
        if (iMBuddyItem != null && zMActivity != null) {
            if (UIMgr.isLargeMode(zMActivity)) {
                BuddyInviteFragment.showDialog(zMActivity.getSupportFragmentManager(), iMBuddyItem.userId);
            } else {
                BuddyInviteActivity.show(zMActivity, zMActivity instanceof IMActivity ? 102 : 0, iMBuddyItem.userId);
            }
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (!(itemAtPosition instanceof IMBuddyItem)) {
            return false;
        }
        ContextMenuFragment.show(((ZMActivity) getContext()).getSupportFragmentManager(), (IMBuddyItem) itemAtPosition);
        return true;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            Parcelable parcelable2 = bundle.getParcelable("IMBuddyListView.superState");
            this.mFilter = bundle.getString("IMBuddyListView.mFilter");
            parcelable = parcelable2;
        }
        super.onRestoreInstanceState(parcelable);
    }

    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("IMBuddyListView.superState", onSaveInstanceState);
        bundle.putString("IMBuddyListView.mFilter", this.mFilter);
        return bundle;
    }

    public void setFilter(@Nullable String str) {
        this.mFilter = str;
    }

    @Nullable
    public String getFilter() {
        return this.mFilter;
    }

    public void filter(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        String lowerCase = str.trim().toLowerCase(CompatUtils.getLocalDefault());
        String str2 = this.mFilter;
        this.mFilter = lowerCase;
        if (str2 == null) {
            str2 = "";
        }
        if (!str2.equals(lowerCase)) {
            if (StringUtil.isEmptyOrNull(lowerCase)) {
                reloadAllBuddyItems();
            } else if (StringUtil.isEmptyOrNull(str2) || !lowerCase.contains(str2)) {
                reloadAllBuddyItems();
            } else {
                this.mAdapter.filter(lowerCase);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refreshContextMenu() {
        ContextMenuFragment contextMenuFragment = (ContextMenuFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(ContextMenuFragment.class.getName());
        if (contextMenuFragment != null) {
            contextMenuFragment.refresh();
        }
    }
}
