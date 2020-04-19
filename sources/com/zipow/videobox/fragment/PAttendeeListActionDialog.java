package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.meeting.PromoteOrDowngradeItem;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.dialog.ExpelAttendeeAlertDialog;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ZMConfUtil;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class PAttendeeListActionDialog extends ZMDialogFragment {
    private static final String ARG_CONF_ATTENDEE_ITEM = "conf_attendee_item";
    private static final String TAG = "PAttendeeListActionDialog";
    @Nullable
    private OptionListAdapter mAdapter;
    @Nullable
    private ConfChatAttendeeItem mConfChatAttendeeItem;

    enum AttendeeActonMenu {
        PROMOTE_TO_PANELIST,
        EXPEL,
        LOWERHAND,
        CHAT,
        TEMPORARILY_TALK,
        MUTE_UNMUTE,
        RENAME
    }

    static class OptionListAdapter extends BaseAdapter {
        private ZMActivity mActivity;
        private ConfChatAttendeeItem mConfChatAttendeeItem;
        @NonNull
        private List<ZMSimpleMenuItem> mList = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public OptionListAdapter(ZMActivity zMActivity, ConfChatAttendeeItem confChatAttendeeItem) {
            this.mActivity = zMActivity;
            this.mConfChatAttendeeItem = confChatAttendeeItem;
        }

        public void setmConfChatAttendeeItem(ConfChatAttendeeItem confChatAttendeeItem) {
            this.mConfChatAttendeeItem = confChatAttendeeItem;
        }

        public void reloadAll() {
            this.mList.clear();
            ZMActivity zMActivity = this.mActivity;
            if (zMActivity != null) {
                loadActions(this.mList, zMActivity, this.mConfChatAttendeeItem);
            }
        }

        /* access modifiers changed from: private */
        public static int loadActions(@NonNull List<ZMSimpleMenuItem> list, @NonNull Context context, @Nullable ConfChatAttendeeItem confChatAttendeeItem) {
            boolean z = false;
            if (confChatAttendeeItem == null || confChatAttendeeItem.isPlaceholder) {
                return 0;
            }
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null || ConfMgr.getInstance().isViewOnlyMeeting()) {
                return 0;
            }
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                return 0;
            }
            if (myself.isHost() || myself.isCoHost()) {
                z = true;
            }
            if (z) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (confContext.isWebinar() && confContext.isMMRSupportViewOnlyClient() && confChatAttendeeItem.isSupportTempTalk && meetingItem != null && !meetingItem.getIsSelfTelephonyOn()) {
                    if (confChatAttendeeItem.isAllowTalked) {
                        if (confChatAttendeeItem.audioType != 2) {
                            list.add(new ZMSimpleMenuItem(AttendeeActonMenu.MUTE_UNMUTE.ordinal(), context.getResources().getString(confChatAttendeeItem.audioOn ? C4558R.string.zm_mi_mute : C4558R.string.zm_mi_unmute)));
                        }
                        list.add(new ZMSimpleMenuItem(AttendeeActonMenu.TEMPORARILY_TALK.ordinal(), context.getString(C4558R.string.zm_mi_forbid_talk_15294)));
                    } else {
                        list.add(new ZMSimpleMenuItem(AttendeeActonMenu.TEMPORARILY_TALK.ordinal(), context.getString(C4558R.string.zm_mi_allow_talk_15294)));
                    }
                }
                if (ConfLocalHelper.isHaisedHand(confChatAttendeeItem.jid)) {
                    list.add(new ZMSimpleMenuItem(AttendeeActonMenu.LOWERHAND.ordinal(), context.getString(C4558R.string.zm_btn_lower_hand)));
                }
            }
            if (!confContext.isChatOff() && !confContext.isPrivateChatOFF() && !confChatAttendeeItem.isTelephone) {
                list.add(new ZMSimpleMenuItem(AttendeeActonMenu.CHAT.ordinal(), context.getString(C4558R.string.zm_mi_chat)));
            }
            if (z) {
                if (!confChatAttendeeItem.isTelephone) {
                    list.add(new ZMSimpleMenuItem(AttendeeActonMenu.PROMOTE_TO_PANELIST.ordinal(), context.getString(C4558R.string.zm_webinar_mi_promote_to_panelist)));
                }
                list.add(new ZMSimpleMenuItem(AttendeeActonMenu.RENAME.ordinal(), context.getString(C4558R.string.zm_btn_rename)));
                list.add(new ZMSimpleMenuItem(AttendeeActonMenu.EXPEL.ordinal(), context.getString(C4558R.string.zm_mi_expel)));
            }
            return list.size();
        }

        public int getCount() {
            return this.mList.size();
        }

        public ZMSimpleMenuItem getItem(int i) {
            return (ZMSimpleMenuItem) this.mList.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(this.mActivity, C4558R.layout.zm_menu_item, null);
            }
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgIcon);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            View findViewById = view.findViewById(C4558R.C4560id.check);
            textView.setText(getItem(i).getLabel());
            imageView.setVisibility(8);
            findViewById.setVisibility(8);
            return view;
        }
    }

    public static boolean show(FragmentManager fragmentManager, ConfChatAttendeeItem confChatAttendeeItem) {
        if (!hasActionsForUser(VideoBoxApplication.getInstance(), confChatAttendeeItem)) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CONF_ATTENDEE_ITEM, confChatAttendeeItem);
        PAttendeeListActionDialog pAttendeeListActionDialog = new PAttendeeListActionDialog();
        pAttendeeListActionDialog.setArguments(bundle);
        pAttendeeListActionDialog.show(fragmentManager, PAttendeeListActionDialog.class.getName());
        return true;
    }

    public PAttendeeListActionDialog() {
        setCancelable(true);
    }

    @Nullable
    private static PAttendeeListActionDialog getActivePListItemActionDialog(FragmentManager fragmentManager) {
        return (PAttendeeListActionDialog) fragmentManager.findFragmentByTag(PAttendeeListActionDialog.class.getName());
    }

    public static void dismissPAttendeeListActionDialogForUserId(@NonNull FragmentManager fragmentManager, long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            PAttendeeListActionDialog activePListItemActionDialog = getActivePListItemActionDialog(fragmentManager);
            if (activePListItemActionDialog != null) {
                ConfChatAttendeeItem confChatAttendeeItem = activePListItemActionDialog.getConfChatAttendeeItem();
                if (confChatAttendeeItem != null && confStatusObj.isSameUser(j, confChatAttendeeItem.nodeID)) {
                    activePListItemActionDialog.dismiss();
                }
            }
            ExpelAttendeeAlertDialog expelAttendeeAlertDialog = ExpelAttendeeAlertDialog.getExpelAttendeeAlertDialog(fragmentManager);
            if (expelAttendeeAlertDialog != null) {
                ConfChatAttendeeItem confChatAttendeeItem2 = expelAttendeeAlertDialog.getConfChatAttendeeItem();
                if (confChatAttendeeItem2 != null && confStatusObj.isSameUser(j, confChatAttendeeItem2.nodeID)) {
                    expelAttendeeAlertDialog.dismiss();
                }
            }
        }
    }

    public static void refreshAction(@NonNull FragmentManager fragmentManager, long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            PAttendeeListActionDialog activePListItemActionDialog = getActivePListItemActionDialog(fragmentManager);
            if (activePListItemActionDialog != null) {
                ConfChatAttendeeItem confChatAttendeeItem = activePListItemActionDialog.getConfChatAttendeeItem();
                if (confChatAttendeeItem != null && confStatusObj.isSameUser(j, confChatAttendeeItem.nodeID)) {
                    if (ConfLocalHelper.isNeedShowAttendeeActionList()) {
                        activePListItemActionDialog.refresh();
                    } else {
                        activePListItemActionDialog.dismiss();
                    }
                }
            }
        }
    }

    public static void dismissPAttendeeListActionDialogForUserId(@NonNull FragmentManager fragmentManager, @NonNull String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            PAttendeeListActionDialog activePListItemActionDialog = getActivePListItemActionDialog(fragmentManager);
            if (activePListItemActionDialog != null) {
                ConfChatAttendeeItem confChatAttendeeItem = activePListItemActionDialog.getConfChatAttendeeItem();
                if (confChatAttendeeItem != null && str.equals(confChatAttendeeItem.jid)) {
                    activePListItemActionDialog.dismiss();
                }
            }
            ExpelAttendeeAlertDialog expelAttendeeAlertDialog = ExpelAttendeeAlertDialog.getExpelAttendeeAlertDialog(fragmentManager);
            if (expelAttendeeAlertDialog != null) {
                ConfChatAttendeeItem confChatAttendeeItem2 = expelAttendeeAlertDialog.getConfChatAttendeeItem();
                if (confChatAttendeeItem2 != null && str.equals(confChatAttendeeItem2.jid)) {
                    expelAttendeeAlertDialog.dismiss();
                }
            }
        }
    }

    private static boolean hasActionsForUser(@NonNull Context context, ConfChatAttendeeItem confChatAttendeeItem) {
        return OptionListAdapter.loadActions(new ArrayList(), context, confChatAttendeeItem) > 0;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mConfChatAttendeeItem = (ConfChatAttendeeItem) arguments.getSerializable(ARG_CONF_ATTENDEE_ITEM);
        if (this.mConfChatAttendeeItem == null) {
            return new Builder(getActivity()).create();
        }
        this.mAdapter = new OptionListAdapter((ZMActivity) getActivity(), this.mConfChatAttendeeItem);
        ZMAlertDialog create = new Builder(getActivity()).setTheme(C4558R.style.ZMDialog_Material).setListDividerHeight(0).setTitleView(DialogUtils.createAvatarDialogTitleView(getActivity(), this.mConfChatAttendeeItem.name, null)).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PAttendeeListActionDialog.this.onClickItem(i);
            }
        }).setListDividerHeight(0).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void onResume() {
        super.onResume();
        if (this.mConfChatAttendeeItem != null) {
            refresh();
        } else {
            dismiss();
        }
    }

    @Nullable
    private ConfChatAttendeeItem getConfChatAttendeeItem() {
        return this.mConfChatAttendeeItem;
    }

    private void refresh() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null) {
            ConfChatAttendeeItem confChatAttendeeItem = this.mConfChatAttendeeItem;
            if (confChatAttendeeItem != null) {
                ZoomQABuddy buddyByNodeID = qAComponent.getBuddyByNodeID(confChatAttendeeItem.nodeID);
                if (buddyByNodeID != null) {
                    this.mConfChatAttendeeItem = new ConfChatAttendeeItem(buddyByNodeID);
                    this.mAdapter.setmConfChatAttendeeItem(this.mConfChatAttendeeItem);
                }
            }
        }
        this.mAdapter.reloadAll();
        this.mAdapter.notifyDataSetChanged();
        if (this.mAdapter.getCount() == 0) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void onClickItem(int i) {
        ZMSimpleMenuItem item = this.mAdapter.getItem(i);
        if (this.mConfChatAttendeeItem != null) {
            int action = item.getAction();
            if (action == AttendeeActonMenu.PROMOTE_TO_PANELIST.ordinal()) {
                onClickPromotePanelist(this.mConfChatAttendeeItem);
            } else if (action == AttendeeActonMenu.EXPEL.ordinal()) {
                onClickMiExpel(this.mConfChatAttendeeItem);
            } else if (action == AttendeeActonMenu.LOWERHAND.ordinal()) {
                onClickLowerAttendeeHand(this.mConfChatAttendeeItem);
            } else if (action == AttendeeActonMenu.CHAT.ordinal()) {
                FragmentActivity activity = getActivity();
                if (activity instanceof ZMActivity) {
                    ConfChatFragment.showAsActivity((ZMActivity) activity, 0, this.mConfChatAttendeeItem);
                }
            } else if (action == AttendeeActonMenu.TEMPORARILY_TALK.ordinal()) {
                ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(this.mConfChatAttendeeItem.nodeID);
                if (zoomQABuddyByNodeId != null) {
                    ConfMgr.getInstance().handleUserCmd(zoomQABuddyByNodeId.isAttendeeCanTalk() ? 29 : 28, this.mConfChatAttendeeItem.nodeID);
                }
            } else if (action == AttendeeActonMenu.MUTE_UNMUTE.ordinal()) {
                onClickMiMute(this.mConfChatAttendeeItem.nodeID);
            } else if (action == AttendeeActonMenu.RENAME.ordinal()) {
                onClickRename(this.mConfChatAttendeeItem.jid);
            }
        }
    }

    private void onClickLowerAttendeeHand(@NonNull ConfChatAttendeeItem confChatAttendeeItem) {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null) {
            raiseHandAPIObj.lowerHand(confChatAttendeeItem.jid);
        }
    }

    private void onClickPromotePanelist(@NonNull ConfChatAttendeeItem confChatAttendeeItem) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            QAWebinarAttendeeListFragment qAWebinarAttendeeListFragment = (QAWebinarAttendeeListFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(QAWebinarAttendeeListFragment.class.getName());
            if (qAWebinarAttendeeListFragment != null) {
                qAWebinarAttendeeListFragment.promotePanelist(confChatAttendeeItem);
                return;
            }
            WebinarRaiseHandFragment webinarRaiseHandFragment = (WebinarRaiseHandFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(WebinarRaiseHandFragment.class.getName());
            PromoteOrDowngradeItem promoteOrDowngradeItem = new PromoteOrDowngradeItem(confChatAttendeeItem, 1);
            if (webinarRaiseHandFragment != null) {
                webinarRaiseHandFragment.promoteOrDowngrade(promoteOrDowngradeItem);
                return;
            }
            PListFragment pListFragment = (PListFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(PListFragment.class.getName());
            if (pListFragment != null) {
                pListFragment.promoteOrDowngrade(promoteOrDowngradeItem);
            }
        }
    }

    private void onClickRename(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ChangeScreenNameDialog.showDialog(getFragmentManager(), str);
        }
    }

    private void onClickMiMute(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null) {
            if (userById.getAudioStatusObj().getIsMuted()) {
                ConfMgr.getInstance().handleUserCmd(48, j);
            } else {
                ConfMgr.getInstance().handleUserCmd(47, j);
            }
        }
    }

    private void onClickMiExpel(@Nullable ConfChatAttendeeItem confChatAttendeeItem) {
        FragmentActivity activity = getActivity();
        if (activity != null && confChatAttendeeItem != null) {
            ExpelAttendeeAlertDialog.showExpelAttendeeAlertDialog((ZMActivity) activity, confChatAttendeeItem);
            ZMConfEventTracking.eventTrackParticipantMenuRemove();
        }
    }
}
