package com.zipow.videobox.dialog.conf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.videobox.InMeetingSettingsActivity;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.fragment.ConfMeetingTopicFragment;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.share.ScreenShareMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.util.ZMPolicyDataHelper;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZmInMeetingSettingDialog extends ZMDialogFragment implements OnClickListener {
    private static final String TAG = "com.zipow.videobox.dialog.conf.ZmInMeetingSettingDialog";
    @Nullable
    private CheckedTextView mChkAllowAnnotation;
    @Nullable
    private CheckedTextView mChkAllowAttendeeRaiseHand;
    @Nullable
    private CheckedTextView mChkAllowPanelistVideo;
    @Nullable
    private CheckedTextView mChkAllowRename;
    @Nullable
    private CheckedTextView mChkEnableWaitingRoom;
    @Nullable
    private CheckedTextView mChkLockMeeting;
    @Nullable
    private CheckedTextView mChkMuteOnEntry;
    @Nullable
    private CheckedTextView mChkPlayEnterExitChime;
    @Nullable
    private CheckedTextView mChkPlayMessageRaiseHandChime;
    @Nullable
    private CheckedTextView mChkShareScreen;
    @Nullable
    private CheckedTextView mChkShowAnnotatorName;
    @Nullable
    private CheckedTextView mChkShowJoinLeaveTip;
    @Nullable
    private CheckedTextView mChkShowMyVideo;
    @Nullable
    private CheckedTextView mChkShowNoVideo;
    @Nullable
    private IConfUIListener mConfUIListener;
    @Nullable
    private View mOptionAllowAnnotation;
    @Nullable
    private View mOptionShowAnnotatorName;
    @Nullable
    private View mPanelAllowAttendeesChatWith;
    @Nullable
    private View mPanelAllowPanelistChatWith;
    @Nullable
    private View mPanelHostAllowAttendees;
    @Nullable
    private View mPanelHostAllowParticipants;
    @Nullable
    private View mPanelHostHostControl;
    @Nullable
    private View mPanelHostSecurity;
    @Nullable
    private View mPanelMeetingTopic;
    @Nullable
    private View mPanelNonHostContentShare;
    @Nullable
    private View mPanelNonHostGeneral;
    @Nullable
    private View mPanelOptionAllowAttendeeRaiseHand;
    @Nullable
    private View mPanelOptionAllowPanelistVideo;
    @Nullable
    private View mPanelOptionAllowRename;
    @Nullable
    private View mPanelOptionEnableWaitingRoom;
    @Nullable
    private View mPanelOptionLockMeeting;
    @Nullable
    private View mPanelOptionMuteOnEntry;
    @Nullable
    private View mPanelOptionNonEditMeetingTopic;
    @Nullable
    private View mPanelOptionPlayEnterExitChime;
    @Nullable
    private View mPanelOptionPlayMessageRaiseHandChime;
    @Nullable
    private View mPanelOptionShareScreen;
    @Nullable
    private View mPanelOptionShowJoinLeaveTip;
    @Nullable
    private View mPanelOptionShowMyVideo;
    @Nullable
    private View mPanelOptionShowNoVideo;
    @Nullable
    private TextView mTatAttendeesCurPrivilege;
    @Nullable
    private TextView mTxtGeneral;
    @Nullable
    private TextView mTxtMeetingTopic;
    @Nullable
    private TextView mTxtMeetingTopicTitle;
    @Nullable
    private TextView mTxtNonEditMeetingTopic;
    @Nullable
    private TextView mTxtPanelistCurPrivildge;

    public static ZmInMeetingSettingDialog newInstance() {
        return new ZmInMeetingSettingDialog();
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_in_meeting_settings, null);
        initSecurity(inflate);
        initAllowPanelistAndAttendees(inflate);
        initHostControl(inflate);
        initContentShare(inflate);
        initGeneral(inflate);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtLockMeeting);
            TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtAllowParticipants);
            TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
            if (confContext.isWebinar()) {
                textView3.setText(C4558R.string.zm_title_setting_webniar_147675);
                textView.setText(getString(C4558R.string.zm_mi_lock_webinar_18265));
                textView2.setText(C4558R.string.zm_lbl_in_meeting_settings_allow_panelist_150183);
            } else {
                textView3.setText(C4558R.string.zm_title_setting_meeting);
                textView.setText(getString(C4558R.string.zm_mi_lock_meeting));
                textView2.setText(C4558R.string.zm_lbl_in_meeting_settings_allow_participants_150183);
            }
        }
        updateUI();
        return inflate;
    }

    public void onResume() {
        super.onResume();
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onUserEvent(int i, long j, int i2) {
                    return ZmInMeetingSettingDialog.this.onUserEvent(i, j, i2);
                }

                public boolean onConfStatusChanged2(int i, long j) {
                    if (!(i == 3 || i == 23)) {
                        if (i == 28) {
                            ZmInMeetingSettingDialog.this.updatePrivildge();
                            HostControlAccessDialog.hide(ZmInMeetingSettingDialog.this.getFragmentManager());
                        } else if (!(i == 31 || i == 38)) {
                            if (i != 157) {
                                switch (i) {
                                    case 139:
                                        break;
                                    case 140:
                                        ZmInMeetingSettingDialog.this.sinkMeetingSettingUpdateUI();
                                        break;
                                }
                            } else {
                                ZmInMeetingSettingDialog.this.sinkMeetingTopicUpdateUI();
                            }
                        }
                        return false;
                    }
                    ZmInMeetingSettingDialog.this.sinkStatusChanged();
                    return false;
                }

                public boolean onUserStatusChanged(int i, long j, int i2) {
                    if (i == 4) {
                        ZmInMeetingSettingDialog.this.sinkMeetingSettingUpdateUI();
                    } else if (i == 44) {
                        ZmInMeetingSettingDialog.this.sinkCoHostChanged(j);
                    }
                    return false;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        updateUI();
    }

    public void onPause() {
        super.onPause();
        if (this.mConfUIListener != null) {
            ConfUI.getInstance().removeListener(this.mConfUIListener);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            finishActivity();
        } else if (id == C4558R.C4560id.panelOptionLockMeeting) {
            onClickLockMeeting();
        } else if (id == C4558R.C4560id.optionEnableWaitingRoom) {
            onClickEnableWaitingRoom();
        } else if (id == C4558R.C4560id.optionShareScreen) {
            onClickShareScreen();
        } else if (id == C4558R.C4560id.panelAllowParticipantsChatWith) {
            onClickAllowParticipantsOrAttendeeChatWith();
        } else if (id == C4558R.C4560id.optionAllowPanelistVideo) {
            onClickAllowPanelistVideo();
        } else if (id == C4558R.C4560id.panelAllowAttendeesChatWith) {
            onClickAllowParticipantsOrAttendeeChatWith();
        } else if (id == C4558R.C4560id.optionAllowRename) {
            onClickAllowRename();
        } else if (id == C4558R.C4560id.panelMeetingTopic) {
            onClickMeetingTopic();
        } else if (id == C4558R.C4560id.optionPlayEnterExitChime) {
            onClickPlayEnterExitChime();
        } else if (id == C4558R.C4560id.optionAllowAnnotation) {
            onClickAnnotate();
        } else if (id == C4558R.C4560id.optionShowAnnotatorName) {
            onClickShowAnnotatorName();
        } else if (id == C4558R.C4560id.optionMuteOnEntry) {
            onClickMuteOnEntry();
        } else if (id == C4558R.C4560id.optionPlayMessageRaiseHandChime) {
            onClickPlayMessageRaiseHandChime();
        } else if (id == C4558R.C4560id.optionAllowAttendeeRaiseHand) {
            onClickAllowAttendeeRaiseHand();
        } else if (id == C4558R.C4560id.optionShowMyVideo) {
            onClickShowMyVideo();
        } else if (id == C4558R.C4560id.optionShowNoVideo) {
            onClickShowNoVideo();
        } else if (id == C4558R.C4560id.optionShowJoinLeaveTip) {
            onClickShowJoinLeaveTip();
        }
    }

    private void onClickShowJoinLeaveTip() {
        CheckedTextView checkedTextView = this.mChkShowJoinLeaveTip;
        if (checkedTextView != null) {
            checkedTextView.setChecked(!checkedTextView.isChecked());
            ZMPolicyDataHelper.getInstance().setBooleanValue(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip, this.mChkShowJoinLeaveTip.isChecked());
        }
    }

    private void onClickShowNoVideo() {
        if (ConfMgr.getInstance().getVideoObj() != null) {
            CheckedTextView checkedTextView = this.mChkShowNoVideo;
            if (checkedTextView != null) {
                checkedTextView.setChecked(!checkedTextView.isChecked());
                ConfUI.getInstance().handleConfInnerEvent(1, this.mChkShowNoVideo.isChecked() ^ true ? 1 : 0);
            }
        }
    }

    private void onClickShowMyVideo() {
        CheckedTextView checkedTextView = this.mChkShowMyVideo;
        if (checkedTextView != null) {
            checkedTextView.setChecked(!checkedTextView.isChecked());
            ConfUI.getInstance().handleConfInnerEvent(0, this.mChkShowMyVideo.isChecked() ? 1 : 0);
        }
    }

    private void onClickShowAnnotatorName() {
        CheckedTextView checkedTextView = this.mChkShowAnnotatorName;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            this.mChkShowAnnotatorName.setChecked(z);
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                shareObj.EnableShowAnnotatorName(z);
            }
            ZMConfEventTracking.eventTrackInMeetingSettingShowAnnotatorNames(z);
        }
    }

    private void onClickAllowAttendeeRaiseHand() {
        CheckedTextView checkedTextView = this.mChkAllowAttendeeRaiseHand;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            this.mChkAllowAttendeeRaiseHand.setChecked(z);
            ConfMgr.getInstance().handleConfCmd(z ? 128 : 129);
            ZMConfEventTracking.eventTrackInMeetingSettingRaiseHand(z);
        }
    }

    private void onClickAllowPanelistVideo() {
        CheckedTextView checkedTextView = this.mChkAllowPanelistVideo;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            this.mChkAllowPanelistVideo.setChecked(z);
            ConfMgr.getInstance().handleConfCmd(z ? 113 : 114);
            ZMConfEventTracking.eventTrackInMeetingSettingStartVideo(z);
        }
    }

    private void onClickPlayMessageRaiseHandChime() {
        CheckedTextView checkedTextView = this.mChkPlayMessageRaiseHandChime;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            if (ConfMgr.getInstance().handleConfCmd(z ? 92 : 93)) {
                this.mChkPlayMessageRaiseHandChime.setChecked(z);
            }
        }
    }

    private void onClickMuteOnEntry() {
        CheckedTextView checkedTextView = this.mChkMuteOnEntry;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            this.mChkMuteOnEntry.setChecked(z);
            AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
            if (audioObj != null) {
                audioObj.setMuteOnEntry(z);
            }
        }
    }

    private void onClickPlayEnterExitChime() {
        CheckedTextView checkedTextView = this.mChkPlayEnterExitChime;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            this.mChkPlayEnterExitChime.setChecked(z);
            ConfMgr.getInstance().setPlayChimeOnOff(z);
        }
    }

    private void onClickMeetingTopic() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.getOrginalHost()) {
            ConfMeetingTopicFragment.showAsActivity(this);
        }
    }

    private void onClickAnnotate() {
        if (this.mChkAllowAnnotation != null) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                boolean isChecked = this.mChkAllowAnnotation.isChecked();
                shareObj.DisableAttendeeAnnotationForMySharedContent(isChecked);
                AnnoDataMgr.getInstance().setAttendeeAnnotateDisable(isChecked);
                if (!isChecked && ScreenShareMgr.getInstance().isSharing()) {
                    ScreenShareMgr.getInstance().setAnnoToolbarVisible(true);
                }
                this.mChkAllowAnnotation.setChecked(!isChecked);
                ZMConfEventTracking.eventTrackInMeetingSettingAnnotate(true ^ isChecked);
            }
        }
    }

    private void onClickAllowRename() {
        CheckedTextView checkedTextView = this.mChkAllowRename;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            this.mChkAllowRename.setChecked(z);
            ConfMgr.getInstance().handleConfCmd(z ? 91 : 94);
            ZMConfEventTracking.eventTrackInMeetingSettingRenameThemselves(z);
        }
    }

    private void onClickAllowParticipantsOrAttendeeChatWith() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null) {
                    HostControlAccessDialog.show(supportFragmentManager, confContext.isWebinar() ? 1 : 0, confStatusObj.getAttendeeChatPriviledge());
                }
            }
        }
    }

    private void onClickShareScreen() {
        CheckedTextView checkedTextView = this.mChkShareScreen;
        if (checkedTextView != null && checkedTextView.isEnabled()) {
            boolean z = !this.mChkShareScreen.isChecked();
            ConfMgr.getInstance().handleConfCmd(z ? 83 : 82);
            this.mChkShareScreen.setChecked(z);
            ZMConfEventTracking.eventTrackInMeetingSettingShareScreen(z);
        }
    }

    private void onClickEnableWaitingRoom() {
        CheckedTextView checkedTextView = this.mChkEnableWaitingRoom;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            ConfMgr.getInstance().setPutOnHoldOnEntry(z);
            this.mChkEnableWaitingRoom.setChecked(z);
            ZMConfEventTracking.eventTrackInMeetingSettingEnableWaitingRoom(z);
        }
    }

    private void onClickLockMeeting() {
        CheckedTextView checkedTextView = this.mChkLockMeeting;
        if (checkedTextView != null) {
            boolean z = !checkedTextView.isChecked();
            ConfMgr.getInstance().handleConfCmd(z ? 58 : 59);
            this.mChkLockMeeting.setChecked(z);
            ZMConfEventTracking.eventTrackInMeetingSettingLockMeeting(z);
        }
    }

    /* access modifiers changed from: private */
    public void finishActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof InMeetingSettingsActivity) {
            ((InMeetingSettingsActivity) activity).onClickBack();
        }
    }

    private void initSecurity(@NonNull View view) {
        this.mPanelHostSecurity = view.findViewById(C4558R.C4560id.hostSecurityPanel);
        this.mPanelOptionLockMeeting = view.findViewById(C4558R.C4560id.panelOptionLockMeeting);
        this.mChkLockMeeting = (CheckedTextView) view.findViewById(C4558R.C4560id.chkLockMeeting);
        View view2 = this.mPanelOptionLockMeeting;
        if (view2 != null) {
            view2.setOnClickListener(this);
        }
        this.mPanelOptionEnableWaitingRoom = view.findViewById(C4558R.C4560id.optionEnableWaitingRoom);
        this.mChkEnableWaitingRoom = (CheckedTextView) view.findViewById(C4558R.C4560id.chkEnableWaitingRoom);
        View view3 = this.mPanelOptionEnableWaitingRoom;
        if (view3 != null) {
            view3.setOnClickListener(this);
        }
    }

    private void initAllowPanelistAndAttendees(@NonNull View view) {
        this.mPanelHostAllowParticipants = view.findViewById(C4558R.C4560id.hostAllowParticipantsPanel);
        this.mPanelHostAllowAttendees = view.findViewById(C4558R.C4560id.hostAllowAttendeesPanel);
        this.mPanelOptionShareScreen = view.findViewById(C4558R.C4560id.optionShareScreen);
        this.mChkShareScreen = (CheckedTextView) view.findViewById(C4558R.C4560id.chkShareScreen);
        View view2 = this.mPanelOptionShareScreen;
        if (view2 != null) {
            view2.setOnClickListener(this);
        }
        this.mPanelAllowPanelistChatWith = view.findViewById(C4558R.C4560id.panelAllowParticipantsChatWith);
        this.mTxtPanelistCurPrivildge = (TextView) view.findViewById(C4558R.C4560id.txtCurParticipantsPrivildge);
        View view3 = this.mPanelAllowPanelistChatWith;
        if (view3 != null) {
            view3.setOnClickListener(this);
        }
        this.mPanelOptionAllowPanelistVideo = view.findViewById(C4558R.C4560id.optionAllowPanelistVideo);
        this.mChkAllowPanelistVideo = (CheckedTextView) view.findViewById(C4558R.C4560id.chkAllowPanelistVideo);
        View view4 = this.mPanelOptionAllowPanelistVideo;
        if (view4 != null) {
            view4.setOnClickListener(this);
        }
        this.mPanelOptionAllowRename = view.findViewById(C4558R.C4560id.optionAllowRename);
        this.mChkAllowRename = (CheckedTextView) view.findViewById(C4558R.C4560id.chkAllowRename);
        View view5 = this.mPanelOptionAllowRename;
        if (view5 != null) {
            view5.setOnClickListener(this);
        }
        this.mPanelOptionAllowAttendeeRaiseHand = view.findViewById(C4558R.C4560id.optionAllowAttendeeRaiseHand);
        this.mChkAllowAttendeeRaiseHand = (CheckedTextView) view.findViewById(C4558R.C4560id.chkAllowAttendeeRaiseHand);
        View view6 = this.mPanelOptionAllowAttendeeRaiseHand;
        if (view6 != null) {
            view6.setOnClickListener(this);
        }
        this.mPanelAllowAttendeesChatWith = view.findViewById(C4558R.C4560id.panelAllowAttendeesChatWith);
        this.mTatAttendeesCurPrivilege = (TextView) view.findViewById(C4558R.C4560id.txtCurAttendeesPrivildge);
        View view7 = this.mPanelAllowAttendeesChatWith;
        if (view7 != null) {
            view7.setOnClickListener(this);
        }
    }

    private void initHostControl(@NonNull View view) {
        this.mPanelHostHostControl = view.findViewById(C4558R.C4560id.hostHostControlPanel);
        this.mPanelMeetingTopic = view.findViewById(C4558R.C4560id.panelMeetingTopic);
        this.mTxtMeetingTopic = (TextView) view.findViewById(C4558R.C4560id.txtMeetingTopic);
        this.mTxtMeetingTopicTitle = (TextView) view.findViewById(C4558R.C4560id.txtMeetingTopicTitle);
        View view2 = this.mPanelMeetingTopic;
        if (view2 != null) {
            view2.setOnClickListener(this);
        }
        TextView textView = this.mTxtMeetingTopicTitle;
        if (textView != null) {
            textView.setText(ConfLocalHelper.isWebinar() ? C4558R.string.zm_mi_webinar_topic_title_150183 : C4558R.string.zm_mi_meeting_topic_title_105983);
        }
        this.mPanelOptionPlayEnterExitChime = view.findViewById(C4558R.C4560id.optionPlayEnterExitChime);
        this.mChkPlayEnterExitChime = (CheckedTextView) view.findViewById(C4558R.C4560id.chkPlayEnterExitChime);
        View view3 = this.mPanelOptionPlayEnterExitChime;
        if (view3 != null) {
            view3.setOnClickListener(this);
        }
    }

    private void initContentShare(@NonNull View view) {
        this.mPanelNonHostContentShare = view.findViewById(C4558R.C4560id.nonHostContentShare);
        this.mOptionShowAnnotatorName = view.findViewById(C4558R.C4560id.optionShowAnnotatorName);
        this.mChkShowAnnotatorName = (CheckedTextView) view.findViewById(C4558R.C4560id.chkShowAnnotatorName);
        View view2 = this.mOptionShowAnnotatorName;
        if (view2 != null) {
            view2.setOnClickListener(this);
        }
        this.mOptionAllowAnnotation = view.findViewById(C4558R.C4560id.optionAllowAnnotation);
        this.mChkAllowAnnotation = (CheckedTextView) view.findViewById(C4558R.C4560id.chkAllowAnnotation);
        View view3 = this.mOptionAllowAnnotation;
        if (view3 != null) {
            view3.setOnClickListener(this);
        }
    }

    private void initGeneral(@NonNull View view) {
        this.mPanelNonHostGeneral = view.findViewById(C4558R.C4560id.nonHostGeneralPanel);
        this.mTxtGeneral = (TextView) view.findViewById(C4558R.C4560id.txtGeneral);
        this.mPanelOptionNonEditMeetingTopic = view.findViewById(C4558R.C4560id.optionNonEditMeetingTopic);
        this.mTxtNonEditMeetingTopic = (TextView) view.findViewById(C4558R.C4560id.txtNonEditMeetingTopic);
        this.mPanelOptionMuteOnEntry = view.findViewById(C4558R.C4560id.optionMuteOnEntry);
        this.mChkMuteOnEntry = (CheckedTextView) view.findViewById(C4558R.C4560id.chkMuteOnEntry);
        View view2 = this.mPanelOptionMuteOnEntry;
        if (view2 != null) {
            view2.setOnClickListener(this);
        }
        this.mPanelOptionPlayMessageRaiseHandChime = view.findViewById(C4558R.C4560id.optionPlayMessageRaiseHandChime);
        this.mChkPlayMessageRaiseHandChime = (CheckedTextView) view.findViewById(C4558R.C4560id.chkPlayMessageRaiseHandChime);
        View view3 = this.mPanelOptionPlayMessageRaiseHandChime;
        if (view3 != null) {
            view3.setOnClickListener(this);
        }
        this.mPanelOptionShowMyVideo = view.findViewById(C4558R.C4560id.optionShowMyVideo);
        this.mChkShowMyVideo = (CheckedTextView) view.findViewById(C4558R.C4560id.chkShowMyVideo);
        View view4 = this.mPanelOptionShowMyVideo;
        if (view4 != null) {
            view4.setOnClickListener(this);
        }
        this.mPanelOptionShowNoVideo = view.findViewById(C4558R.C4560id.optionShowNoVideo);
        this.mChkShowNoVideo = (CheckedTextView) view.findViewById(C4558R.C4560id.chkShowNoVideo);
        View view5 = this.mPanelOptionShowNoVideo;
        if (view5 != null) {
            view5.setOnClickListener(this);
        }
        this.mPanelOptionShowJoinLeaveTip = view.findViewById(C4558R.C4560id.optionShowJoinLeaveTip);
        this.mChkShowJoinLeaveTip = (CheckedTextView) view.findViewById(C4558R.C4560id.chkShowJoinLeaveTip);
        View view6 = this.mPanelOptionShowJoinLeaveTip;
        if (view6 != null) {
            view6.setOnClickListener(this);
        }
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        updateSecurity();
        updateAllowPanelistAndAttendee();
        updateHostControl();
        updateContentShare();
        updateGeneral();
    }

    private void updateSecurity() {
        if (this.mPanelHostSecurity == null || this.mPanelOptionLockMeeting == null || this.mPanelOptionEnableWaitingRoom == null || this.mChkLockMeeting == null || this.mChkEnableWaitingRoom == null) {
            finishActivity();
        } else if (!ConfMgr.getInstance().isConfConnected()) {
            finishActivity();
        } else {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null) {
                finishActivity();
                return;
            }
            if (!myself.isHostCoHost() || myself.isBOModerator()) {
                this.mPanelHostSecurity.setVisibility(8);
            } else {
                this.mPanelHostSecurity.setVisibility(0);
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj == null) {
                    finishActivity();
                    return;
                }
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext == null) {
                    finishActivity();
                    return;
                }
                this.mPanelOptionLockMeeting.setVisibility(0);
                this.mChkLockMeeting.setChecked(confStatusObj.isConfLocked());
                if (confContext.supportPutUserinWaitingListUponEntryFeature()) {
                    this.mPanelOptionEnableWaitingRoom.setVisibility(0);
                    this.mChkEnableWaitingRoom.setChecked(ConfMgr.getInstance().isPutOnHoldOnEntryOn());
                } else {
                    this.mPanelOptionEnableWaitingRoom.setVisibility(8);
                }
                if (ConfMgr.getInstance().isPutOnHoldOnEntryLocked()) {
                    this.mPanelOptionEnableWaitingRoom.setEnabled(false);
                    this.mChkEnableWaitingRoom.setEnabled(false);
                } else {
                    this.mPanelOptionEnableWaitingRoom.setEnabled(true);
                    this.mChkEnableWaitingRoom.setEnabled(true);
                }
            }
        }
    }

    private void updateAllowPanelistAndAttendee() {
        boolean z;
        boolean z2;
        if (this.mPanelHostAllowParticipants == null || this.mPanelHostAllowAttendees == null || this.mPanelOptionShareScreen == null || this.mPanelOptionAllowPanelistVideo == null || this.mChkAllowPanelistVideo == null || this.mChkShareScreen == null || this.mPanelOptionAllowAttendeeRaiseHand == null || this.mChkAllowAttendeeRaiseHand == null || this.mPanelAllowPanelistChatWith == null || this.mPanelAllowAttendeesChatWith == null || this.mPanelOptionAllowRename == null || this.mChkAllowRename == null) {
            finishActivity();
        } else if (!ConfMgr.getInstance().isConfConnected()) {
            finishActivity();
        } else {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null) {
                finishActivity();
                return;
            }
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                finishActivity();
                return;
            }
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj == null) {
                finishActivity();
                return;
            }
            int i = 8;
            if (myself.isHostCoHost() || myself.isBOModerator()) {
                if (myself.isBOModerator() || confContext.isScreenShareDisabled()) {
                    this.mPanelOptionShareScreen.setVisibility(8);
                    z = false;
                } else {
                    this.mPanelOptionShareScreen.setVisibility(0);
                    this.mChkShareScreen.setChecked(!ConfMgr.getInstance().isShareLocked());
                    if (confContext.isShareSettingTypeLocked()) {
                        this.mPanelOptionShareScreen.setEnabled(false);
                        this.mChkShareScreen.setEnabled(false);
                    } else {
                        this.mPanelOptionShareScreen.setEnabled(true);
                        this.mChkShareScreen.setEnabled(true);
                    }
                    z = true;
                }
                if (confContext.isWebinar()) {
                    this.mPanelOptionAllowPanelistVideo.setVisibility(0);
                    this.mPanelOptionAllowAttendeeRaiseHand.setVisibility(0);
                    this.mChkAllowPanelistVideo.setChecked(!confStatusObj.isStartVideoDisabled());
                    this.mChkAllowAttendeeRaiseHand.setChecked(confStatusObj.isAllowRaiseHand());
                    z2 = true;
                    z = true;
                } else {
                    this.mPanelOptionAllowPanelistVideo.setVisibility(8);
                    this.mPanelOptionAllowAttendeeRaiseHand.setVisibility(8);
                    z2 = false;
                }
                if (confContext.isChatOff() || !myself.isHostCoHost()) {
                    this.mPanelAllowPanelistChatWith.setVisibility(8);
                    this.mPanelAllowAttendeesChatWith.setVisibility(8);
                } else {
                    if (confContext.isWebinar()) {
                        this.mPanelAllowPanelistChatWith.setVisibility(8);
                        this.mPanelAllowAttendeesChatWith.setVisibility(0);
                        z2 = true;
                    } else {
                        this.mPanelAllowPanelistChatWith.setVisibility(0);
                        this.mPanelAllowAttendeesChatWith.setVisibility(8);
                        z = true;
                    }
                    updatePrivildge();
                }
                if (confContext.isAllowParticipantRenameEnabled()) {
                    this.mPanelOptionAllowRename.setVisibility(0);
                    this.mChkAllowRename.setChecked(ConfLocalHelper.isAllowParticipantRename());
                    z = true;
                } else {
                    this.mPanelOptionAllowRename.setVisibility(8);
                }
            } else {
                z2 = false;
                z = false;
            }
            this.mPanelHostAllowParticipants.setVisibility(z ? 0 : 8);
            View view = this.mPanelHostAllowAttendees;
            if (z2) {
                i = 0;
            }
            view.setVisibility(i);
        }
    }

    private void updateHostControl() {
        boolean z;
        if (this.mPanelHostHostControl == null || this.mPanelOptionPlayEnterExitChime == null || this.mPanelMeetingTopic == null || this.mChkPlayEnterExitChime == null) {
            finishActivity();
        } else if (!ConfMgr.getInstance().isConfConnected()) {
            finishActivity();
        } else {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null) {
                finishActivity();
                return;
            }
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                finishActivity();
                return;
            }
            int i = 8;
            if (myself.isHostCoHost() || myself.isBOModerator()) {
                if (confContext.getOrginalHost()) {
                    this.mPanelMeetingTopic.setVisibility(0);
                    updateMeetingTopic();
                    z = true;
                } else {
                    this.mPanelMeetingTopic.setVisibility(8);
                    z = false;
                }
                if (!myself.isBOModerator()) {
                    this.mPanelOptionPlayEnterExitChime.setVisibility(0);
                    this.mChkPlayEnterExitChime.setChecked(ConfMgr.getInstance().isPlayChimeOn());
                    z = true;
                } else {
                    this.mPanelOptionPlayEnterExitChime.setVisibility(8);
                }
            } else {
                z = false;
            }
            View view = this.mPanelHostHostControl;
            if (z) {
                i = 0;
            }
            view.setVisibility(i);
        }
    }

    private void updateContentShare() {
        if (this.mOptionShowAnnotatorName == null || this.mChkShowAnnotatorName == null || this.mOptionAllowAnnotation == null || this.mChkAllowAnnotation == null || this.mPanelNonHostContentShare == null) {
            finishActivity();
        } else if (!ConfMgr.getInstance().isConfConnected()) {
            finishActivity();
        } else if (ConfMgr.getInstance().getMyself() == null) {
            finishActivity();
        } else {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                finishActivity();
                return;
            }
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            boolean z = true;
            int i = 8;
            if (shareObj == null || confContext.isAnnoationOff() || !ConfShareLocalHelper.isSendSharing() || !shareObj.senderSupportAnnotation(0)) {
                this.mOptionAllowAnnotation.setVisibility(8);
                this.mOptionShowAnnotatorName.setVisibility(8);
                z = false;
            } else {
                this.mOptionShowAnnotatorName.setVisibility(0);
                this.mChkShowAnnotatorName.setChecked(shareObj.isShowAnnotatorName());
                this.mOptionAllowAnnotation.setVisibility(0);
                this.mChkAllowAnnotation.setChecked(!shareObj.isAttendeeAnnotationDisabledForMySharedContent());
            }
            View view = this.mPanelNonHostContentShare;
            if (z) {
                i = 0;
            }
            view.setVisibility(i);
        }
    }

    /* access modifiers changed from: private */
    public void updateMeetingTopic() {
        if (this.mTxtMeetingTopic == null || this.mTxtNonEditMeetingTopic == null) {
            finishActivity();
            return;
        }
        String meetingTopic = ConfMgr.getInstance().getMeetingTopic();
        if (!StringUtil.isEmptyOrNull(meetingTopic)) {
            this.mTxtMeetingTopic.setText(meetingTopic);
            this.mTxtNonEditMeetingTopic.setText(meetingTopic);
        } else {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                this.mTxtMeetingTopic.setText(String.format(getString(C4558R.string.zm_mi_meeting_topic_name_105983), new Object[]{myself.getScreenName()}));
                this.mTxtNonEditMeetingTopic.setText(String.format(getString(C4558R.string.zm_mi_meeting_topic_name_105983), new Object[]{myself.getScreenName()}));
            }
        }
    }

    private void updateGeneral() {
        if (this.mPanelNonHostGeneral == null || this.mPanelOptionMuteOnEntry == null || this.mPanelOptionPlayMessageRaiseHandChime == null || this.mChkMuteOnEntry == null || this.mTxtGeneral == null || this.mChkPlayMessageRaiseHandChime == null || this.mPanelOptionShowMyVideo == null || this.mPanelOptionNonEditMeetingTopic == null || this.mChkShowMyVideo == null || this.mPanelOptionShowNoVideo == null || this.mChkShowNoVideo == null || this.mChkShowJoinLeaveTip == null) {
            finishActivity();
        } else if (!ConfMgr.getInstance().isConfConnected()) {
            finishActivity();
        } else {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null) {
                finishActivity();
                return;
            }
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null) {
                finishActivity();
                return;
            }
            if (myself.isHostCoHost() || myself.isBOModerator()) {
                AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
                if (audioObj == null) {
                    finishActivity();
                    return;
                }
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj == null) {
                    finishActivity();
                    return;
                }
                this.mTxtGeneral.setText(C4558R.string.zm_lbl_in_meeting_settings_general_147675);
                if (!myself.isBOModerator()) {
                    this.mPanelOptionMuteOnEntry.setVisibility(0);
                    this.mChkMuteOnEntry.setChecked(audioObj.isMuteOnEntryOn());
                } else {
                    this.mPanelOptionMuteOnEntry.setVisibility(8);
                }
                if (confContext.isMessageAndFeedbackNotifyEnabled()) {
                    this.mPanelOptionPlayMessageRaiseHandChime.setVisibility(0);
                    this.mChkPlayMessageRaiseHandChime.setChecked(confStatusObj.isAllowMessageAndFeedbackNotify());
                } else {
                    this.mPanelOptionPlayMessageRaiseHandChime.setVisibility(8);
                }
            } else {
                this.mTxtGeneral.setText(C4558R.string.zm_lbl_meetings_75334);
                this.mPanelOptionMuteOnEntry.setVisibility(8);
                this.mPanelOptionPlayMessageRaiseHandChime.setVisibility(8);
            }
            if (confContext.getOrginalHost()) {
                this.mPanelOptionNonEditMeetingTopic.setVisibility(8);
            } else {
                this.mPanelOptionNonEditMeetingTopic.setVisibility(0);
                updateMeetingTopic();
            }
            if (ConfLocalHelper.isMeetShowMyVideoButton()) {
                this.mPanelOptionShowMyVideo.setVisibility(0);
                this.mChkShowMyVideo.setChecked(ConfMgr.getInstance().getConfDataHelper().ismIsShowMyVideoInGalleryView());
            } else {
                this.mPanelOptionShowMyVideo.setVisibility(8);
            }
            if (ZMPolicyUIHelper.isLockedAutoHideNoVideoUsers()) {
                this.mPanelOptionShowNoVideo.setVisibility(8);
            } else {
                this.mPanelOptionShowNoVideo.setVisibility(0);
                ZMPolicyUIHelper.applyAutoHideNoVideoUsers(this.mChkShowNoVideo, this.mPanelOptionShowNoVideo);
            }
            ZMPolicyUIHelper.applyShowJoinLeaveTip(this.mChkShowJoinLeaveTip, this.mPanelOptionShowJoinLeaveTip);
            this.mPanelNonHostGeneral.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void updatePrivildge() {
        if (this.mTatAttendeesCurPrivilege == null || this.mTxtPanelistCurPrivildge == null) {
            finishActivity();
            return;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            finishActivity();
            return;
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            finishActivity();
            return;
        }
        int attendeeChatPriviledge = confStatusObj.getAttendeeChatPriviledge();
        if (confContext.isWebinar()) {
            if (!ConfMgr.getInstance().isAllowAttendeeChat()) {
                this.mTatAttendeesCurPrivilege.setText(C4558R.string.zm_mi_no_one_11380);
            } else if (attendeeChatPriviledge == 1) {
                this.mTatAttendeesCurPrivilege.setText(C4558R.string.zm_mi_panelists_and_attendees_11380);
            } else {
                this.mTatAttendeesCurPrivilege.setText(C4558R.string.zm_webinar_txt_all_panelists);
            }
        } else if (attendeeChatPriviledge == 3) {
            this.mTxtPanelistCurPrivildge.setText(C4558R.string.zm_mi_host_only_11380);
        } else if (attendeeChatPriviledge == 1) {
            this.mTxtPanelistCurPrivildge.setText(C4558R.string.zm_mi_everyone_122046);
        } else if (attendeeChatPriviledge == 5) {
            this.mTxtPanelistCurPrivildge.setText(C4558R.string.zm_mi_host_and_public_65892);
        } else if (attendeeChatPriviledge == 4) {
            this.mTxtPanelistCurPrivildge.setText(C4558R.string.zm_mi_no_one_65892);
        }
    }

    /* access modifiers changed from: private */
    public boolean onUserEvent(int i, long j, int i2) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final int i3 = i;
        final long j2 = j;
        final int i4 = i2;
        C24212 r1 = new EventAction("onUserEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ZmInMeetingSettingDialog.this.handleOnUserEvent(i3, j2, i4);
            }
        };
        nonNullEventTaskManagerOrThrowException.pushLater(r1);
        return true;
    }

    /* access modifiers changed from: private */
    public void handleOnUserEvent(int i, long j, int i2) {
        if (i == 0 || i == 1) {
            updateUI();
        }
    }

    /* access modifiers changed from: private */
    public void sinkStatusChanged() {
        getNonNullEventTaskManagerOrThrowException().pushLater("updateMeetingSettings", new EventAction("updateMeetingSettings_rename") {
            public void run(IUIElement iUIElement) {
                ZmInMeetingSettingDialog.this.updateUI();
            }
        });
    }

    /* access modifiers changed from: private */
    public void sinkCoHostChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().pushLater("onCoHostChange", new EventAction("onHostChange") {
            public void run(IUIElement iUIElement) {
                if (ConfLocalHelper.isMySelf(j)) {
                    ZmInMeetingSettingDialog.this.finishActivity();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sinkMeetingSettingUpdateUI() {
        getNonNullEventTaskManagerOrThrowException().pushLater("sinkMeetingSettingUpdateUI", new EventAction("sinkMeetingSettingUpdateUI") {
            public void run(IUIElement iUIElement) {
                ZmInMeetingSettingDialog.this.updateUI();
            }
        });
    }

    /* access modifiers changed from: private */
    public void sinkMeetingTopicUpdateUI() {
        getNonNullEventTaskManagerOrThrowException().pushLater("sinkMeetingTopicUpdateUI", new EventAction("sinkMeetingTopicUpdateUI") {
            public void run(IUIElement iUIElement) {
                ZmInMeetingSettingDialog.this.updateMeetingTopic();
            }
        });
    }
}
