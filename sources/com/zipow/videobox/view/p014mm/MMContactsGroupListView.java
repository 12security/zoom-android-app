package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.fragment.IMAddrBookListFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import com.zipow.videobox.view.p014mm.MMChatsListView.ChatsListContextMenuItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContactsGroupListView */
public class MMContactsGroupListView extends ListView implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = "MMContactsGroupListView";
    private MMContactsGroupAdapter mAdapter;
    private IMAddrBookListFragment mParentFragment;
    private boolean pendingNotification = false;

    /* renamed from: com.zipow.videobox.view.mm.MMContactsGroupListView$GroupContextMenuItem */
    public static class GroupContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_AUDIO_CALL = 1;
        public static final int ACTION_VIDEO_CALL = 0;

        public GroupContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    private static void startGroupChat(@NonNull ZMActivity zMActivity, String str) {
        MMChatActivity.showAsGroupChat(zMActivity, str);
    }

    public MMContactsGroupListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMContactsGroupListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMContactsGroupListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mAdapter = new MMContactsGroupAdapter(getContext());
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
    }

    public void filter(String str) {
        this.mAdapter.filter(str);
    }

    public void refreshAllData() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            this.mAdapter.clearAll();
            int groupCount = zoomMessenger.getGroupCount();
            for (int i = 0; i <= groupCount; i++) {
                ZoomGroup groupAt = zoomMessenger.getGroupAt(i);
                if (groupAt != null) {
                    MMZoomGroup initWithZoomGroup = MMZoomGroup.initWithZoomGroup(groupAt);
                    if (initWithZoomGroup.isRoom()) {
                        this.mAdapter.addOrUpdateItem(initWithZoomGroup);
                    }
                }
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setParentFragment(IMAddrBookListFragment iMAddrBookListFragment) {
        this.mParentFragment = iMAddrBookListFragment;
    }

    public boolean isParentFragmentResumed() {
        IMAddrBookListFragment iMAddrBookListFragment = this.mParentFragment;
        if (iMAddrBookListFragment == null) {
            return false;
        }
        return iMAddrBookListFragment.isResumed();
    }

    public void notifyDataSetChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void addOrUpdateGroup(String str) {
        addOrUpdateGroup(str, true);
    }

    public void addOrUpdateGroup(String str, boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomGroup groupById = zoomMessenger.getGroupById(str);
            MMZoomGroup mMZoomGroup = null;
            boolean z2 = true;
            if (groupById != null) {
                mMZoomGroup = MMZoomGroup.initWithZoomGroup(groupById);
                if (mMZoomGroup.getMemberCount() > 0 && mMZoomGroup.isRoom()) {
                    z2 = false;
                }
            }
            if (z2) {
                removeGroupItem(str);
                return;
            }
            this.mAdapter.addOrUpdateItem(mMZoomGroup);
            if (isParentFragmentResumed() && z) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
        if (groupAction.getActionType() == 2 || groupAction.getActionType() == 5) {
            removeGroupItem(groupAction.getGroupId());
        } else {
            addOrUpdateGroup(groupAction.getGroupId());
        }
    }

    public void onGroupDestory(String str) {
        removeGroupItem(str);
    }

    public void removeGroupItem(String str) {
        MMContactsGroupAdapter mMContactsGroupAdapter = this.mAdapter;
        if (mMContactsGroupAdapter != null && mMContactsGroupAdapter.findGroup(str) != null) {
            this.mAdapter.removeItem(str);
            if (isParentFragmentResumed()) {
                this.mAdapter.notifyDataSetChanged();
            } else {
                this.pendingNotification = true;
            }
        }
    }

    public void onGroupMemberInfoUpdate(String str) {
        addOrUpdateGroup(str);
    }

    private void onItemClickGroup(@NonNull MMZoomGroup mMZoomGroup) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (zoomMessenger.imChatGetOption() == 2) {
                showGroupConferenceOption(mMZoomGroup);
            } else {
                startGroupChat((ZMActivity) getContext(), mMZoomGroup.getGroupId());
            }
        }
    }

    public void jump2Group(String str) {
        if (!TextUtils.isEmpty(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (!(zoomMessenger == null || zoomMessenger.imChatGetOption() == 2)) {
                PreferenceUtil.saveBooleanValue(PreferenceUtil.ZM_MM_GROUP_DESC_JOIN_FIRST, true);
                startGroupChat((ZMActivity) getContext(), str);
            }
        }
    }

    private void showGroupConferenceOption(@NonNull final MMZoomGroup mMZoomGroup) {
        Context context = getContext();
        if (context != null && PTApp.getInstance().getCallStatus() != 2) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
            ArrayList arrayList = new ArrayList();
            String groupName = mMZoomGroup.getGroupName();
            arrayList.add(new GroupContextMenuItem(context.getString(C4558R.string.zm_btn_video_call), 0));
            arrayList.add(new GroupContextMenuItem(context.getString(C4558R.string.zm_btn_audio_call), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            ZMAlertDialog create = new Builder(context).setTitle((CharSequence) groupName).setAdapter(zMMenuAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    MMContactsGroupListView.this.onSelectConferenceMenuItem(mMZoomGroup, (GroupContextMenuItem) zMMenuAdapter.getItem(i));
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void onSelectConferenceMenuItem(MMZoomGroup mMZoomGroup, GroupContextMenuItem groupContextMenuItem) {
        if (groupContextMenuItem.getAction() == 1) {
            if (PTApp.getInstance().getCallStatus() == 0) {
                startVoiceCall(mMZoomGroup);
            }
        } else if (groupContextMenuItem.getAction() == 0 && PTApp.getInstance().getCallStatus() == 0) {
            startVideoCall(mMZoomGroup);
        }
    }

    private void startVoiceCall(MMZoomGroup mMZoomGroup) {
        if (PTApp.getInstance().getCallStatus() == 0) {
            startConference(mMZoomGroup, 6);
        } else {
            alertAlreadyInMeeting();
        }
    }

    private void startVideoCall(MMZoomGroup mMZoomGroup) {
        if (PTApp.getInstance().getCallStatus() == 0) {
            startConference(mMZoomGroup, 3);
        } else {
            alertAlreadyInMeeting();
        }
    }

    private void alertAlreadyInMeeting() {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, C4558R.string.zm_msg_cannot_start_call_while_in_another_meeting, 1).show();
        }
    }

    private void startConference(final MMZoomGroup mMZoomGroup, final int i) {
        new Builder(getContext()).setTitle(C4558R.string.zm_title_start_group_call).setMessage(C4558R.string.zm_msg_confirm_group_call).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMContactsGroupListView.this.callABContact(mMZoomGroup, i);
            }
        }).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) null).show();
    }

    /* access modifiers changed from: private */
    public void callABContact(@Nullable MMZoomGroup mMZoomGroup, int i) {
        if (mMZoomGroup != null) {
            int startGroupCall = ConfActivity.startGroupCall(getContext(), mMZoomGroup.getGroupId(), i);
            if (startGroupCall != 0) {
                StartHangoutFailedDialog.show(((ZMActivity) getContext()).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), startGroupCall);
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mAdapter.getItem(i);
        if (item instanceof MMZoomGroup) {
            onItemClickGroup((MMZoomGroup) item);
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mAdapter.getItem(i);
        if (!(item instanceof MMZoomGroup)) {
            return false;
        }
        final MMZoomGroup mMZoomGroup = (MMZoomGroup) item;
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity == null) {
            return false;
        }
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
        ArrayList arrayList = new ArrayList();
        String string = zMActivity.getString(C4558R.string.zm_mm_title_chatslist_context_menu_channel_chat_59554);
        String string2 = zMActivity.getString(C4558R.string.zm_mm_lbl_delete_channel_chat_59554);
        if (!mMZoomGroup.isRoom()) {
            string2 = zMActivity.getString(C4558R.string.zm_mm_lbl_delete_muc_chat_59554);
            string = zMActivity.getString(C4558R.string.zm_mm_title_chatslist_context_menu_channel_chat_59554);
        }
        arrayList.add(new ChatsListContextMenuItem(string2, 0));
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        ZMAlertDialog create = new Builder(zMActivity).setTitle((CharSequence) string).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMContactsGroupListView.this.onSelectContextMenuItem(mMZoomGroup, (ChatsListContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return true;
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(@NonNull MMZoomGroup mMZoomGroup, ChatsListContextMenuItem chatsListContextMenuItem) {
        if (chatsListContextMenuItem.getAction() == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession findSessionById = zoomMessenger.findSessionById(mMZoomGroup.getGroupId());
                if (findSessionById != null && findSessionById.clearAllMessages()) {
                    this.mParentFragment.onGroupDelete(mMZoomGroup.getGroupId());
                }
            }
        }
    }

    public void onResume() {
        if (this.pendingNotification) {
            MMContactsGroupAdapter mMContactsGroupAdapter = this.mAdapter;
            if (mMContactsGroupAdapter != null) {
                mMContactsGroupAdapter.notifyDataSetChanged();
                this.pendingNotification = false;
            }
        }
    }
}
