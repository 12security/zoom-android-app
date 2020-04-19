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
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.meeting.PromoteOrDowngradeItem;
import com.zipow.videobox.dialog.SeparateAudioDialog;
import com.zipow.videobox.dialog.ZMAlertConnectAudioDialog;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.DialogUtils;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PListItemActionDialog extends ZMDialogFragment {
    private static final String ARG_USERID = "userId";
    /* access modifiers changed from: private */
    @Nullable
    public OptionListAdapter mAdapter;
    private long mUserId = 0;

    static class OptionListAdapter extends BaseAdapter {
        private ZMActivity mActivity;
        @NonNull
        private List<PListActionItem> mList = new ArrayList();
        private long mUserId = 0;

        public long getItemId(int i) {
            return (long) i;
        }

        public OptionListAdapter(ZMActivity zMActivity, long j) {
            this.mActivity = zMActivity;
            this.mUserId = j;
        }

        public void reloadAll() {
            this.mList.clear();
            ZMActivity zMActivity = this.mActivity;
            if (zMActivity != null) {
                loadActions(this.mList, zMActivity, this.mUserId);
            }
        }

        /* access modifiers changed from: private */
        public static int loadActions(@NonNull List<PListActionItem> list, @NonNull Context context, long j) {
            boolean z;
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                return 0;
            }
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj == null) {
                return 0;
            }
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById == null) {
                return 0;
            }
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null) {
                return 0;
            }
            boolean z2 = true;
            if (!confContext.isMeetingSupportSilentMode() || (userById.getClientCapability() & 8) == 0) {
                z = false;
                z2 = false;
            } else if (userById.inSilentMode()) {
                z = true;
            } else {
                z = true;
                z2 = false;
            }
            if (confStatusObj.isMyself(j)) {
                loadSelfActions(list, context, confContext, userById);
            } else if (myself.isHost() || myself.isCoHost() || myself.isBOModerator()) {
                loadAttendeeActionsForHostOrCoHost(list, context, confContext, userById, z, z2);
            } else if (confContext.isWebinar()) {
                loadAttendeeActionsForWebinarPanelist(list, context, confContext, userById, z, z2);
            } else {
                loadAttendeeActions(list, context, confContext, userById, z, z2);
            }
            return list.size();
        }

        private static void loadSelfActions(@NonNull List<PListActionItem> list, @NonNull Context context, @NonNull CmmConfContext cmmConfContext, CmmUser cmmUser) {
            if (cmmUser.getRaiseHandState()) {
                list.add(new PListActionItem(10, context.getResources().getString(C4558R.string.zm_btn_lower_hand)));
            } else if (!cmmUser.isHost() && !cmmUser.isBOModerator() && !cmmConfContext.isWebinar()) {
                list.add(new PListActionItem(9, context.getResources().getString(C4558R.string.zm_btn_raise_hand)));
            }
            if (cmmUser.isHost() || cmmUser.isCoHost() || cmmUser.isBOModerator()) {
                list.add(new PListActionItem(11, context.getResources().getString(C4558R.string.zm_btn_rename)));
            } else if (ConfLocalHelper.isAllowParticipantRename()) {
                list.add(new PListActionItem(11, context.getResources().getString(C4558R.string.zm_btn_rename)));
            }
            if ((cmmUser.isHost() || cmmUser.isCoHost()) && !cmmUser.isBOModerator()) {
                CmmVideoStatus videoStatusObj = cmmUser.getVideoStatusObj();
                boolean z = false;
                if (videoStatusObj != null) {
                    z = videoStatusObj.getIsSending();
                }
                if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) > 2) {
                    VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                    if (videoObj != null) {
                        if (videoObj.isLeaderofLeadMode(cmmUser.getNodeId())) {
                            list.add(new PListActionItem(6, context.getResources().getString(C4558R.string.zm_mi_unspotlight_video)));
                        } else if (z) {
                            list.add(new PListActionItem(5, context.getResources().getString(C4558R.string.zm_mi_spotlight_video)));
                        }
                    }
                }
            }
            if (!ConfLocalHelper.isInBOMeeting()) {
                CmmUserList userList = ConfMgr.getInstance().getUserList();
                if (cmmConfContext.isBindTelephoneUserEnable() && cmmUser.isHostCoHost() && cmmUser.isNoAudioClientUser() && userList != null && userList.hasPureCallInUser()) {
                    list.add(new PListActionItem(24, context.getResources().getString(C4558R.string.zm_mi_merge_audio_116180)));
                } else if (cmmConfContext.isUnbindTelephoneUserEnable() && cmmUser.isHostCoHost() && cmmUser.isBoundTelClientUser()) {
                    list.add(new PListActionItem(25, context.getResources().getString(C4558R.string.zm_mi_separate_audio_116180)));
                }
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:22:0x0044  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static void loadAttendeeActions(@androidx.annotation.NonNull java.util.List<com.zipow.videobox.fragment.PListItemActionDialog.PListActionItem> r3, @androidx.annotation.NonNull android.content.Context r4, @androidx.annotation.NonNull com.zipow.videobox.confapp.CmmConfContext r5, @androidx.annotation.NonNull com.zipow.videobox.confapp.CmmUser r6, boolean r7, boolean r8) {
            /*
                if (r8 != 0) goto L_0x00bf
                boolean r7 = r5.isPrivateChatOFF()
                r8 = 1
                r0 = 0
                if (r7 != 0) goto L_0x0056
                boolean r7 = r5.isChatOff()
                if (r7 != 0) goto L_0x0056
                boolean r7 = r6.isH323User()
                if (r7 != 0) goto L_0x0056
                boolean r7 = r6.isPureCallInUser()
                if (r7 != 0) goto L_0x0056
                boolean r7 = r6.isHost()
                if (r7 != 0) goto L_0x0041
                boolean r7 = r6.isCoHost()
                if (r7 != 0) goto L_0x0041
                boolean r7 = r6.isBOModerator()
                if (r7 != 0) goto L_0x0041
                com.zipow.videobox.confapp.ConfMgr r7 = com.zipow.videobox.confapp.ConfMgr.getInstance()
                com.zipow.videobox.confapp.CmmConfStatus r7 = r7.getConfStatusObj()
                if (r7 == 0) goto L_0x0041
                int r7 = r7.getAttendeeChatPriviledge()
                r1 = 3
                if (r7 != r1) goto L_0x0041
                r7 = 0
                goto L_0x0042
            L_0x0041:
                r7 = 1
            L_0x0042:
                if (r7 == 0) goto L_0x0056
                com.zipow.videobox.fragment.PListItemActionDialog$PListActionItem r7 = new com.zipow.videobox.fragment.PListItemActionDialog$PListActionItem
                android.content.res.Resources r1 = r4.getResources()
                int r2 = p021us.zoom.videomeetings.C4558R.string.zm_mi_chat
                java.lang.String r1 = r1.getString(r2)
                r7.<init>(r0, r1)
                r3.add(r7)
            L_0x0056:
                com.zipow.videobox.confapp.ConfAppProtos$CmmVideoStatus r7 = r6.getVideoStatusObj()
                if (r7 == 0) goto L_0x0069
                boolean r1 = r7.getIsSending()
                boolean r2 = r7.getIsSource()
                int r7 = r7.getCamFecc()
                goto L_0x006c
            L_0x0069:
                r7 = 0
                r1 = 0
                r2 = 0
            L_0x006c:
                boolean r5 = r5.isMeetingSupportCameraControl()
                if (r5 == 0) goto L_0x007d
                boolean r5 = r6.supportSwitchCam()
                if (r5 == 0) goto L_0x007a
                if (r1 != 0) goto L_0x007e
            L_0x007a:
                if (r7 <= 0) goto L_0x007d
                goto L_0x007e
            L_0x007d:
                r8 = 0
            L_0x007e:
                if (r8 == 0) goto L_0x00bf
                if (r1 == 0) goto L_0x00bf
                if (r2 == 0) goto L_0x00bf
                com.zipow.videobox.confapp.ConfMgr r5 = com.zipow.videobox.confapp.ConfMgr.getInstance()
                com.zipow.videobox.confapp.VideoSessionMgr r5 = r5.getVideoObj()
                long r6 = r6.getNodeId()
                boolean r5 = r5.canControlltheCam(r6)
                if (r5 == 0) goto L_0x00ab
                com.zipow.videobox.fragment.PListItemActionDialog$PListActionItem r5 = new com.zipow.videobox.fragment.PListItemActionDialog$PListActionItem
                r6 = 19
                android.content.res.Resources r4 = r4.getResources()
                int r7 = p021us.zoom.videomeetings.C4558R.string.zm_fecc_btn_give_up
                java.lang.String r4 = r4.getString(r7)
                r5.<init>(r6, r4)
                r3.add(r5)
                goto L_0x00bf
            L_0x00ab:
                com.zipow.videobox.fragment.PListItemActionDialog$PListActionItem r5 = new com.zipow.videobox.fragment.PListItemActionDialog$PListActionItem
                r6 = 18
                android.content.res.Resources r4 = r4.getResources()
                int r7 = p021us.zoom.videomeetings.C4558R.string.zm_fecc_btn_request
                java.lang.String r4 = r4.getString(r7)
                r5.<init>(r6, r4)
                r3.add(r5)
            L_0x00bf:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.PListItemActionDialog.OptionListAdapter.loadAttendeeActions(java.util.List, android.content.Context, com.zipow.videobox.confapp.CmmConfContext, com.zipow.videobox.confapp.CmmUser, boolean, boolean):void");
        }

        private static void loadAttendeeActionsForHostOrCoHost(@NonNull List<PListActionItem> list, @NonNull Context context, @NonNull CmmConfContext cmmConfContext, @NonNull CmmUser cmmUser, boolean z, boolean z2) {
            boolean z3;
            boolean z4;
            boolean z5;
            boolean z6;
            int i;
            List<PListActionItem> list2 = list;
            if (z2) {
                list.add(new PListActionItem(3, context.getResources().getString(C4558R.string.zm_mi_expel)));
                list.add(new PListActionItem(8, context.getResources().getString(C4558R.string.zm_mi_leave_silent_mode)));
                return;
            }
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            boolean z7 = false;
            if (confStatusObj != null) {
                z4 = confStatusObj.isHost();
                z3 = confStatusObj.isBOModerator();
            } else {
                z4 = false;
                z3 = false;
            }
            if (cmmUser.getRaiseHandState()) {
                list.add(new PListActionItem(10, context.getResources().getString(C4558R.string.zm_btn_lower_hand)));
            }
            CmmAudioStatus audioStatusObj = cmmUser.getAudioStatusObj();
            if (!(audioStatusObj == null || audioStatusObj.getAudiotype() == 2)) {
                if (audioStatusObj.getIsMuted()) {
                    list.add(new PListActionItem(1, context.getResources().getString(C4558R.string.zm_mi_unmute)));
                } else {
                    list.add(new PListActionItem(1, context.getResources().getString(C4558R.string.zm_mi_mute)));
                }
            }
            if (!cmmConfContext.isPrivateChatOFF() && !cmmConfContext.isChatOff() && !cmmUser.isH323User() && !cmmUser.isPureCallInUser()) {
                list.add(new PListActionItem(0, context.getResources().getString(C4558R.string.zm_mi_chat)));
            }
            CmmVideoStatus videoStatusObj = cmmUser.getVideoStatusObj();
            if (videoStatusObj != null) {
                z6 = videoStatusObj.getIsSending();
                z5 = videoStatusObj.getIsSource();
                i = videoStatusObj.getCamFecc();
            } else {
                i = 0;
                z6 = false;
                z5 = false;
            }
            if (!z3 && ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) > 2 && !cmmUser.isPureCallInUser()) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    if (videoObj.isLeaderofLeadMode(cmmUser.getNodeId())) {
                        list.add(new PListActionItem(6, context.getResources().getString(C4558R.string.zm_mi_unspotlight_video)));
                    } else if (z6) {
                        list.add(new PListActionItem(5, context.getResources().getString(C4558R.string.zm_mi_spotlight_video)));
                    }
                }
            }
            if (!z3 && z4 && !cmmUser.isNoHostUser()) {
                list.add(new PListActionItem(2, context.getResources().getString(C4558R.string.zm_mi_make_host)));
                if (!cmmUser.isCoHost() && cmmUser.canActAsCoHost()) {
                    list.add(new PListActionItem(15, context.getResources().getString(C4558R.string.zm_mi_assign_cohost)));
                }
            }
            if (z6 && cmmUser.videoCanMuteByHost()) {
                list.add(new PListActionItem(12, context.getResources().getString(C4558R.string.zm_mi_video_stop)));
            }
            if (!z6 && z5 && cmmUser.videoCanUnmuteByHost() && confStatusObj != null && !confStatusObj.isStartVideoDisabled()) {
                list.add(new PListActionItem(14, context.getResources().getString(C4558R.string.zm_mi_video_ask_to_start)));
            }
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                if (ConfLocalHelper.isHost() && cmmConfContext.canActAsCCEditor() && cmmUser.canActAsCCEditor()) {
                    if (cmmUser.canEditCC()) {
                        list.add(new PListActionItem(21, context.getResources().getString(C4558R.string.zm_btn_disassign_cc_typer_16896)));
                    } else {
                        list.add(new PListActionItem(20, context.getResources().getString(C4558R.string.zm_btn_assign_cc_typer_16896)));
                    }
                }
                boolean z8 = !ConfUI.getInstance().isDisplayAsHost(cmmUser.getNodeId()) && !ConfUI.getInstance().isDisplayAsCohost(cmmUser.getNodeId());
                if (ConfUI.getInstance().isDisplayAsHost(myself.getNodeId()) || z8) {
                    list.add(new PListActionItem(11, context.getResources().getString(C4558R.string.zm_btn_rename)));
                }
                ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                if (shareObj != null && shareObj.isViewingPureComputerAudio() && (myself.isHost() || myself.isCoHost() || myself.isBOModerator())) {
                    long pureComputerAudioSharingUserID = shareObj.getPureComputerAudioSharingUserID();
                    if (confStatusObj != null && confStatusObj.isSameUser(cmmUser.getNodeId(), pureComputerAudioSharingUserID)) {
                        list.add(new PListActionItem(22, context.getResources().getString(C4558R.string.zm_mi_host_stop_audio_share_41468)));
                    }
                }
            }
            if (z4 && cmmUser.isCoHost()) {
                list.add(new PListActionItem(16, context.getResources().getString(C4558R.string.zm_mi_withdraw_cohost)));
            }
            if (cmmConfContext.isMeetingSupportCameraControl() && ((cmmUser.supportSwitchCam() && z6) || i > 0)) {
                z7 = true;
            }
            if (z7 && z6 && z5) {
                VideoSessionMgr videoObj2 = ConfMgr.getInstance().getVideoObj();
                if (videoObj2 == null || !videoObj2.canControlltheCam(cmmUser.getNodeId())) {
                    list.add(new PListActionItem(18, context.getResources().getString(C4558R.string.zm_fecc_btn_request)));
                } else {
                    list.add(new PListActionItem(19, context.getResources().getString(C4558R.string.zm_fecc_btn_give_up)));
                }
            }
            if (!ConfLocalHelper.isInBOMeeting()) {
                CmmUserList userList = ConfMgr.getInstance().getUserList();
                if (cmmConfContext.isBindTelephoneUserEnable() && cmmUser.isPureCallInUser() && userList != null && userList.hasNoAudioClientUser()) {
                    list.add(new PListActionItem(23, context.getResources().getString(C4558R.string.zm_mi_merge_video_116180)));
                } else if (cmmConfContext.isBindTelephoneUserEnable() && cmmUser.isNoAudioClientUser() && userList != null && userList.hasPureCallInUser()) {
                    list.add(new PListActionItem(24, context.getResources().getString(C4558R.string.zm_mi_merge_audio_116180)));
                } else if (cmmConfContext.isUnbindTelephoneUserEnable() && cmmUser.isBoundTelClientUser()) {
                    list.add(new PListActionItem(25, context.getResources().getString(C4558R.string.zm_mi_separate_audio_116180)));
                }
            }
            boolean isUserOriginalorAltHost = ConfMgr.getInstance().isUserOriginalorAltHost(cmmUser.getUserZoomID());
            if (!cmmUser.isHost() && !cmmUser.isCoHost()) {
                if (!cmmConfContext.isAuthLocalRecordDisabled() && !cmmConfContext.isLocalRecordDisabled()) {
                    if (cmmUser.canRecord()) {
                        list.add(new PListActionItem(4, context.getResources().getString(C4558R.string.zm_mi_disallow_record)));
                    } else if (cmmUser.clientOSSupportRecord() && !cmmConfContext.isRecordDisabled()) {
                        list.add(new PListActionItem(4, context.getResources().getString(C4558R.string.zm_mi_allow_record)));
                    }
                }
                if (!z3 && z) {
                    if (cmmConfContext.supportPutUserinWaitingListUponEntryFeature()) {
                        list.add(new PListActionItem(7, context.getResources().getString(C4558R.string.zm_mi_put_on_waiting)));
                    } else {
                        list.add(new PListActionItem(7, context.getResources().getString(C4558R.string.zm_mi_enter_silent_mode)));
                    }
                }
                if (cmmConfContext.isWebinar() && !cmmUser.isPureCallInUser() && !isUserOriginalorAltHost) {
                    list.add(new PListActionItem(13, context.getResources().getString(C4558R.string.zm_webinar_mi_downgrade_to_attendee)));
                }
                if (!z3) {
                    list.add(new PListActionItem(3, context.getResources().getString(C4558R.string.zm_mi_expel)));
                }
            }
        }

        private static void loadAttendeeActionsForWebinarPanelist(@NonNull List<PListActionItem> list, @NonNull Context context, @NonNull CmmConfContext cmmConfContext, @NonNull CmmUser cmmUser, boolean z, boolean z2) {
            if (!z2 && !cmmConfContext.isChatOff() && !cmmConfContext.isPrivateChatOFF() && !cmmUser.isH323User() && !cmmUser.isPureCallInUser()) {
                list.add(new PListActionItem(0, context.getResources().getString(C4558R.string.zm_mi_chat)));
            }
        }

        public int getCount() {
            return this.mList.size();
        }

        public Object getItem(int i) {
            return this.mList.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(this.mActivity, C4558R.layout.zm_dialog_list_item, null);
            }
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgIcon);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
            View findViewById = view.findViewById(C4558R.C4560id.check);
            PListActionItem pListActionItem = (PListActionItem) getItem(i);
            textView.setText(pListActionItem.label);
            if (pListActionItem.imgIconId == 0) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                imageView.setImageResource(pListActionItem.imgIconId);
            }
            findViewById.setVisibility(8);
            return view;
        }
    }

    static class PListActionItem {
        public static final int ITEM_ALLOW_ATTENDEE_CHAT = 17;
        public static final int ITEM_ALLOW_RECORD = 4;
        public static final int ITEM_ASK_TO_START_VIDEO = 14;
        public static final int ITEM_ASSIGN_CC_TYPER = 20;
        public static final int ITEM_ASSIGN_COHOST = 15;
        public static final int ITEM_CHANGE_NAME = 11;
        public static final int ITEM_CHAT = 0;
        public static final int ITEM_DOWNGRADE_TO_ATTENDEE = 13;
        public static final int ITEM_ENTER_SILENT_MODE = 7;
        public static final int ITEM_EXPEL = 3;
        public static final int ITEM_FECC_GIVE_UP_CTRL = 19;
        public static final int ITEM_FECC_REQUEST_CTRL = 18;
        public static final int ITEM_LEAVE_SILENT_MODE = 8;
        public static final int ITEM_LOWER_HAND = 10;
        public static final int ITEM_MAKE_HOST = 2;
        public static final int ITEM_MUTE = 1;
        public static final int ITEM_RAISE_HAND = 9;
        public static final int ITEM_SELECT_AUDIO = 24;
        public static final int ITEM_SELECT_VIDEO = 23;
        public static final int ITEM_SEPARATE_AUDIO = 25;
        public static final int ITEM_SPOTLIGHT_VIDEO = 5;
        public static final int ITEM_STOP_AUDIO_SHARE = 22;
        public static final int ITEM_STOP_VIDEO = 12;
        public static final int ITEM_UNSPOTLIGHT_VIDEO = 6;
        public static final int ITEM_WITHDRAW_CC_TYPER = 21;
        public static final int ITEM_WITHDRAW_COHOST = 16;
        public int imgIconId;
        public String label;
        public int type;

        public PListActionItem(int i, String str) {
            this.type = i;
            this.label = str;
            this.imgIconId = 0;
        }

        public PListActionItem(int i, String str, int i2) {
            this.type = i;
            this.label = str;
            this.imgIconId = i2;
        }
    }

    public static boolean show(FragmentManager fragmentManager, long j) {
        if (!hasActionsForUser(VideoBoxApplication.getInstance(), j)) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("userId", j);
        PListItemActionDialog pListItemActionDialog = new PListItemActionDialog();
        pListItemActionDialog.setArguments(bundle);
        pListItemActionDialog.show(fragmentManager, PListItemActionDialog.class.getName());
        return true;
    }

    public static void refreshAction(@NonNull FragmentManager fragmentManager, long j) {
        PListItemActionDialog activePListItemActionDialog = getActivePListItemActionDialog(fragmentManager);
        if (activePListItemActionDialog != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isSameUser(j, activePListItemActionDialog.getUserId())) {
                activePListItemActionDialog.refresh();
            }
        }
    }

    public static void refreshAction(@NonNull FragmentManager fragmentManager) {
        PListItemActionDialog activePListItemActionDialog = getActivePListItemActionDialog(fragmentManager);
        if (!(activePListItemActionDialog == null || ConfMgr.getInstance().getConfStatusObj() == null)) {
            activePListItemActionDialog.refresh();
        }
    }

    public PListItemActionDialog() {
        setCancelable(true);
    }

    @Nullable
    public static PListItemActionDialog getActivePListItemActionDialog(FragmentManager fragmentManager) {
        return (PListItemActionDialog) fragmentManager.findFragmentByTag(PListItemActionDialog.class.getName());
    }

    public static void dismissPListItemActionDialog(@NonNull FragmentManager fragmentManager) {
        PListItemActionDialog activePListItemActionDialog = getActivePListItemActionDialog(fragmentManager);
        if (activePListItemActionDialog != null) {
            activePListItemActionDialog.dismiss();
        }
        MakeHostAlertDialog makeHostAlertDialog = MakeHostAlertDialog.getMakeHostAlertDialog(fragmentManager);
        if (makeHostAlertDialog != null) {
            makeHostAlertDialog.dismiss();
        }
        ExpelUserAlertDialog expelUserAlertDialog = ExpelUserAlertDialog.getExpelUserAlertDialog(fragmentManager);
        if (expelUserAlertDialog != null) {
            expelUserAlertDialog.dismiss();
        }
        MakeCoHostAlertDialog makeCoHostAlertDialog = MakeCoHostAlertDialog.getMakeCoHostAlertDialog(fragmentManager);
        if (makeCoHostAlertDialog != null) {
            makeCoHostAlertDialog.dismiss();
        }
    }

    public static void dismissPListActionDialogForUserId(@NonNull FragmentManager fragmentManager, long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            PListItemActionDialog activePListItemActionDialog = getActivePListItemActionDialog(fragmentManager);
            if (activePListItemActionDialog != null && confStatusObj.isSameUser(j, activePListItemActionDialog.getUserId())) {
                activePListItemActionDialog.dismiss();
            }
            MakeHostAlertDialog makeHostAlertDialog = MakeHostAlertDialog.getMakeHostAlertDialog(fragmentManager);
            if (makeHostAlertDialog != null && confStatusObj.isSameUser(j, makeHostAlertDialog.getUserId())) {
                makeHostAlertDialog.dismiss();
            }
            ExpelUserAlertDialog expelUserAlertDialog = ExpelUserAlertDialog.getExpelUserAlertDialog(fragmentManager);
            if (expelUserAlertDialog != null && confStatusObj.isSameUser(j, expelUserAlertDialog.getUserId())) {
                expelUserAlertDialog.dismiss();
            }
            MakeCoHostAlertDialog makeCoHostAlertDialog = MakeCoHostAlertDialog.getMakeCoHostAlertDialog(fragmentManager);
            if (makeCoHostAlertDialog != null && confStatusObj.isSameUser(j, makeCoHostAlertDialog.getUserId())) {
                makeCoHostAlertDialog.dismiss();
            }
        }
    }

    private static boolean hasActionsForUser(@NonNull Context context, long j) {
        return OptionListAdapter.loadActions(new ArrayList(), context, j) > 0;
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Object obj;
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mUserId = arguments.getLong("userId");
        CmmUser userById = ConfMgr.getInstance().getUserById(this.mUserId);
        if (userById == null) {
            this.mUserId = 0;
            return new Builder(getActivity()).create();
        }
        this.mAdapter = new OptionListAdapter((ZMActivity) getActivity(), this.mUserId);
        if (userById.isPureCallInUser()) {
            obj = Integer.valueOf(C4558R.C4559drawable.avatar_phone_green);
        } else if (userById.isH323User()) {
            obj = Integer.valueOf(C4558R.C4559drawable.zm_h323_avatar);
        } else {
            obj = userById.getSmallPicPath();
        }
        ZMAlertDialog create = new Builder(getActivity()).setTitleView(DialogUtils.createAvatarDialogTitleView(getActivity(), userById.getScreenName(), obj)).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PListItemActionDialog pListItemActionDialog = PListItemActionDialog.this;
                pListItemActionDialog.onClickItem(pListItemActionDialog.mAdapter, i);
            }
        }).setListDividerHeight(0).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void onResume() {
        super.onResume();
        if (this.mUserId != 0) {
            refresh();
        } else {
            dismiss();
        }
    }

    public long getUserId() {
        return this.mUserId;
    }

    public void refresh() {
        this.mAdapter.reloadAll();
        this.mAdapter.notifyDataSetChanged();
        if (this.mAdapter.getCount() == 0) {
            dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void onClickItem(OptionListAdapter optionListAdapter, int i) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            long j = arguments.getLong("userId");
            PListActionItem pListActionItem = (PListActionItem) this.mAdapter.getItem(i);
            if (pListActionItem != null) {
                switch (pListActionItem.type) {
                    case 0:
                        onClickMiChat(j);
                        break;
                    case 1:
                        onClickMiMute(j);
                        break;
                    case 2:
                        onClickMiMakeHost(j);
                        break;
                    case 3:
                        onClickMiExpel(j);
                        break;
                    case 4:
                        onClickMiAllowRecord(j);
                        break;
                    case 5:
                        onClickSpotLightVideo(j);
                        break;
                    case 6:
                        onClickUnspotLightVideo(j);
                        break;
                    case 7:
                        onClickEnterSilentMode(j);
                        break;
                    case 8:
                        onClickLeaveSilentMode(j);
                        break;
                    case 9:
                        onClickRaiseHand(j);
                        break;
                    case 10:
                        onClickLowerHand(j);
                        break;
                    case 11:
                        onClickRename(j);
                        break;
                    case 12:
                        onClickVideoStop(j);
                        break;
                    case 13:
                        onClickDowngradeToAttendee(j);
                        break;
                    case 14:
                        onClickVideoAskToStart(j);
                        break;
                    case 15:
                        onClickMiAssignCoHost(j);
                        break;
                    case 16:
                        onClickMiWithdrawCoHost(j);
                        break;
                    case 17:
                        onClickAllowAttendeeChat();
                        break;
                    case 18:
                        onClickFeccRequestCotrolCamera(j);
                        break;
                    case 19:
                        onClickFeccGiveUpCtrolCamera(j);
                        break;
                    case 20:
                        onClickAssignCcTyper(j);
                        break;
                    case 21:
                        onClickWithdrawCcTyper(j);
                        break;
                    case 22:
                        onClickStopShareAudio();
                        break;
                    case 23:
                        onClickSelectVideo(j);
                        break;
                    case 24:
                        onClickSelectAudio(j);
                        break;
                    case 25:
                        onClickSeparateAudio(j);
                        break;
                }
            }
        }
    }

    private void onClickSelectVideo(long j) {
        SelectParticipantsFragment.showAsActivity((ZMActivity) getActivity(), 1, j);
    }

    private void onClickSelectAudio(long j) {
        SelectParticipantsFragment.showAsActivity((ZMActivity) getActivity(), 2, j);
    }

    private void onClickSeparateAudio(long j) {
        if (ConfLocalHelper.isMySelf(j)) {
            SeparateAudioDialog.showDialog(getFragmentManager(), j, "");
            return;
        }
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null) {
            SeparateAudioDialog.showDialog(getFragmentManager(), j, userById.getScreenName());
        }
    }

    private void onClickStopShareAudio() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.requestToStopPureComputerAudioShare(shareObj.getPureComputerAudioSharingUserID());
        }
    }

    private void onClickAssignCcTyper(long j) {
        ConfMgr.getInstance().handleUserCmd(34, j);
    }

    private void onClickWithdrawCcTyper(long j) {
        ConfMgr.getInstance().handleUserCmd(35, j);
    }

    private void onClickFeccRequestCotrolCamera(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.handleFECCCmd(11, j);
        }
    }

    private void onClickFeccGiveUpCtrolCamera(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.handleFECCCmd(14, j);
        }
    }

    private void onClickAllowAttendeeChat() {
        ConfMgr instance = ConfMgr.getInstance();
        instance.handleConfCmd(instance.isAllowAttendeeChat() ? 117 : 116);
    }

    private void onClickSpotLightVideo(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.setLeadShipMode(true, j);
        }
    }

    private void onClickUnspotLightVideo(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.setLeadShipMode(false, j);
        }
    }

    private void onClickMiMakeHost(long j) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MakeHostAlertDialog.showMakeHostAlertDialog((ZMActivity) activity, j);
        }
    }

    private void onClickMiAssignCoHost(long j) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MakeCoHostAlertDialog.showMakeCoHostAlertDialog((ZMActivity) activity, j);
        }
    }

    private void onClickMiWithdrawCoHost(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && userById.isCoHost()) {
            ConfMgr.getInstance().handleUserCmd(45, j);
        }
    }

    private void onClickMiChat(long j) {
        showChatUI(j);
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

    private void onClickMiExpel(long j) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ExpelUserAlertDialog.showExpelUserAlertDialog((ZMActivity) activity, j);
            ZMConfEventTracking.eventTrackParticipantMenuRemove();
        }
    }

    private void onClickMiAllowRecord(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null) {
            if (userById.canRecord()) {
                ConfMgr.getInstance().handleUserCmd(33, j);
            } else {
                ConfMgr.getInstance().handleUserCmd(32, j);
            }
        }
    }

    private void onClickEnterSilentMode(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && !userById.inSilentMode()) {
            ConfMgr.getInstance().handleUserCmd(42, j);
        }
    }

    private void onClickLeaveSilentMode(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && userById.inSilentMode()) {
            ConfMgr.getInstance().handleUserCmd(43, j);
        }
    }

    private void onClickRaiseHand(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && !userById.getRaiseHandState()) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                if (!ConfLocalHelper.isMySelf(j) || ConfLocalHelper.handleMySelfRaisHandAction(zMActivity, null)) {
                    ConfMgr.getInstance().handleUserCmd(36, j);
                } else {
                    ZMAlertConnectAudioDialog.showConnectAudioDialog(zMActivity, j);
                }
            }
        }
    }

    private void onClickLowerHand(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && userById.getRaiseHandState()) {
            ConfMgr.getInstance().handleUserCmd(37, j);
        }
    }

    private void onClickRename(long j) {
        ChangeScreenNameDialog.showDialog(getFragmentManager(), j);
    }

    private void onClickVideoStop(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && userById.videoCanMuteByHost()) {
            ConfMgr.getInstance().handleUserCmd(62, j);
        }
    }

    private void onClickVideoAskToStart(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && userById.videoCanUnmuteByHost()) {
            ConfMgr.getInstance().handleUserCmd(63, j);
        }
    }

    private void onClickDowngradeToAttendee(long j) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            PListFragment pListFragment = PListFragment.getPListFragment(fragmentManager);
            WebinarRaiseHandFragment fragment = WebinarRaiseHandFragment.getFragment(getFragmentManager());
            PromoteOrDowngradeItem promoteAttendeeItem = PromoteOrDowngradeItem.getPromoteAttendeeItem(j, 2);
            if (promoteAttendeeItem != null) {
                if (pListFragment != null) {
                    pListFragment.promoteOrDowngrade(promoteAttendeeItem);
                } else if (fragment != null) {
                    fragment.promoteOrDowngrade(promoteAttendeeItem);
                }
            }
        }
    }

    private void showChatUI(long j) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ConfLocalHelper.showChatUI(zMActivity, j);
        }
    }
}
