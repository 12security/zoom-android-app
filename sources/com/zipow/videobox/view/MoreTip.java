package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.InMeetingSettingsActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.RecordMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.dialog.ConfirmStopRecordDialog;
import com.zipow.videobox.fragment.LoginAsHostAlertDialog;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.share.ScreenShareMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMPolicyDataHelper;
import com.zipow.videobox.utils.meeting.ZmAudioUtils;
import com.zipow.videobox.view.MeetingReactionView.OnSelectListener;
import com.zipow.videobox.view.video.MeetingReactionMgr;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.videomeetings.C4558R;

public class MoreTip extends ZMTipFragment implements OnClickListener, OnSelectListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    private static final String TAG = "MoreTip";
    private View mBtnClaimHost;
    private View mBtnClaimHostByHostKey;
    private View mBtnDisableAnnotate;
    private View mBtnDisconnectAudio;
    private View mBtnEnableAnnotate;
    private View mBtnHideAnnotatorName;
    private View mBtnLoginAsHost;
    private ImageView mBtnPauseRecord;
    private View mBtnShowAnnotatorName;
    private View mBtnStartRecord;
    private ImageView mBtnStopRecord;
    private ImageView mImgRecording;
    private View mLanguageInterpretation;
    private View mLlRecordStatus;
    private MeetingReactionView mMeetingReactionView;
    private View mPanelClaimHost;
    private View mPanelDisableLiveTranscript;
    private View mPanelDisconnectAudio;
    private View mPanelEnableLiveTranscript;
    private View mPanelHandAction;
    private View mPanelHideMyVideoAction;
    private View mPanelHideNoVideoAction;
    private View mPanelMeetingReaction;
    private View mPanelMeetingSettings;
    private View mPanelNonHostAction;
    private View mPanelOriginalSound;
    private View mPanelRecord;
    private View mPanelShowJoinLeaveTipAction;
    private View mPanelUnreadMessage;
    private View mPanelViewFullTranscript;
    private ProgressBar mProgressStartingRecord;
    private TextView mTxtHandAction;
    private TextView mTxtHideMyVideoAction;
    private TextView mTxtHideNoVideoAction;
    private TextView mTxtMeetingSettings;
    private TextView mTxtOriginalSound;
    private TextView mTxtRecordStatus;
    private TextView mTxtShowJoinLeaveTipAction;
    private TextView mTxtUnreadMessageAction;

    public static void show(@Nullable FragmentManager fragmentManager, int i) {
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            MoreTip moreTip = new MoreTip();
            moreTip.setArguments(bundle);
            moreTip.show(fragmentManager, MoreTip.class.getName());
        }
    }

    public static void updateIfExists(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            MoreTip moreTip = (MoreTip) fragmentManager.findFragmentByTag(MoreTip.class.getName());
            if (moreTip != null) {
                moreTip.updateUI();
            }
        }
    }

    public static void updateRecordIfExists(@Nullable FragmentManager fragmentManager, boolean z, boolean z2) {
        if (fragmentManager != null) {
            MoreTip moreTip = (MoreTip) fragmentManager.findFragmentByTag(MoreTip.class.getName());
            if (moreTip != null) {
                moreTip.updateRecord(z, z2);
            }
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((MoreTip) fragmentManager.findFragmentByTag(MoreTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        MoreTip moreTip = (MoreTip) fragmentManager.findFragmentByTag(MoreTip.class.getName());
        if (moreTip == null) {
            return false;
        }
        moreTip.dismiss();
        return true;
    }

    public static boolean hasItemsToShow() {
        ConfMgr instance = ConfMgr.getInstance();
        CmmUser myself = instance.getMyself();
        CmmConfContext confContext = instance.getConfContext();
        if (!(myself == null || confContext == null)) {
            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
            if (myself.isBOModerator() && audioStatusObj != null && audioStatusObj.getAudiotype() == 2) {
                return false;
            }
        }
        return true;
    }

    public ZMTip onCreateTip(@NonNull Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_more_tip, null);
        this.mBtnStartRecord = inflate.findViewById(C4558R.C4560id.btnStartRecord);
        this.mLlRecordStatus = inflate.findViewById(C4558R.C4560id.llRecordStatus);
        this.mImgRecording = (ImageView) inflate.findViewById(C4558R.C4560id.imgRecording);
        this.mProgressStartingRecord = (ProgressBar) inflate.findViewById(C4558R.C4560id.progressStartingRecord);
        this.mTxtRecordStatus = (TextView) inflate.findViewById(C4558R.C4560id.txtRecordStatus);
        this.mBtnPauseRecord = (ImageView) inflate.findViewById(C4558R.C4560id.btn_pause_record);
        this.mBtnStopRecord = (ImageView) inflate.findViewById(C4558R.C4560id.btn_stop_record);
        this.mTxtHandAction = (TextView) inflate.findViewById(C4558R.C4560id.txtHandAction);
        this.mBtnEnableAnnotate = inflate.findViewById(C4558R.C4560id.btnEnableAnnotation);
        this.mBtnDisableAnnotate = inflate.findViewById(C4558R.C4560id.btnDisableAnnotation);
        this.mBtnShowAnnotatorName = inflate.findViewById(C4558R.C4560id.btnShowAnnotator);
        this.mBtnHideAnnotatorName = inflate.findViewById(C4558R.C4560id.btnHideAnnotator);
        this.mPanelNonHostAction = inflate.findViewById(C4558R.C4560id.panelNonHostAction);
        this.mBtnLoginAsHost = inflate.findViewById(C4558R.C4560id.btnLoginAsHost);
        this.mBtnClaimHostByHostKey = inflate.findViewById(C4558R.C4560id.btnClaimHostByHostkey);
        this.mPanelClaimHost = inflate.findViewById(C4558R.C4560id.panelClaimHost);
        this.mPanelEnableLiveTranscript = inflate.findViewById(C4558R.C4560id.panelEnableLiveTranscript);
        this.mPanelDisableLiveTranscript = inflate.findViewById(C4558R.C4560id.panelDisableLiveTranscript);
        this.mPanelViewFullTranscript = inflate.findViewById(C4558R.C4560id.panelViewFullTranscript);
        this.mPanelMeetingReaction = inflate.findViewById(C4558R.C4560id.panelMeetingReaction);
        this.mBtnClaimHost = inflate.findViewById(C4558R.C4560id.btnClaimHost);
        this.mPanelHandAction = inflate.findViewById(C4558R.C4560id.panelHandAction);
        this.mTxtMeetingSettings = (TextView) inflate.findViewById(C4558R.C4560id.txtMeetingSettings);
        this.mBtnDisconnectAudio = inflate.findViewById(C4558R.C4560id.btnDisconnectAudio);
        this.mPanelDisconnectAudio = inflate.findViewById(C4558R.C4560id.panelDisconnectAudio);
        this.mPanelRecord = inflate.findViewById(C4558R.C4560id.panelRecord);
        this.mLanguageInterpretation = inflate.findViewById(C4558R.C4560id.languageInterpretation);
        this.mPanelMeetingSettings = inflate.findViewById(C4558R.C4560id.panelMeetingSettings);
        this.mMeetingReactionView = (MeetingReactionView) inflate.findViewById(C4558R.C4560id.viewMeetingReaction);
        this.mPanelHideMyVideoAction = inflate.findViewById(C4558R.C4560id.panelHideMyVideoAction);
        this.mTxtHideMyVideoAction = (TextView) inflate.findViewById(C4558R.C4560id.txtHideMyVideoAction);
        this.mPanelHideNoVideoAction = inflate.findViewById(C4558R.C4560id.panelHideNoVideoAction);
        this.mTxtHideNoVideoAction = (TextView) inflate.findViewById(C4558R.C4560id.txtHideNoVideoAction);
        this.mPanelShowJoinLeaveTipAction = inflate.findViewById(C4558R.C4560id.panelShowJoinLeaveTip);
        this.mTxtShowJoinLeaveTipAction = (TextView) inflate.findViewById(C4558R.C4560id.txtShowJoinLeaveTip);
        this.mPanelOriginalSound = inflate.findViewById(C4558R.C4560id.panelOriginalSound);
        this.mTxtOriginalSound = (TextView) inflate.findViewById(C4558R.C4560id.txtOriginalSound);
        this.mPanelUnreadMessage = inflate.findViewById(C4558R.C4560id.panelUnreadMessage);
        this.mTxtUnreadMessageAction = (TextView) inflate.findViewById(C4558R.C4560id.txtUnreadMessage);
        updateUI();
        int displayWidth = UIUtil.getDisplayWidth(context);
        inflate.measure(MeasureSpec.makeMeasureSpec(displayWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(UIUtil.getDisplayHeight(context), Integer.MIN_VALUE));
        int i = (displayWidth * 7) / 8;
        if (inflate.getMeasuredWidth() > i) {
            inflate.setLayoutParams(new LayoutParams(i, -2));
        }
        Bundle arguments = getArguments();
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(inflate);
        int i2 = arguments.getInt("anchorId", 0);
        if (i2 > 0) {
            View findViewById = getActivity().findViewById(i2);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, UIMgr.isLargeMode(getActivity()) ? 1 : 3);
            }
        }
        this.mBtnLoginAsHost.setOnClickListener(this);
        this.mBtnClaimHostByHostKey.setOnClickListener(this);
        this.mBtnClaimHost.setOnClickListener(this);
        this.mBtnStartRecord.setOnClickListener(this);
        this.mBtnPauseRecord.setOnClickListener(this);
        this.mBtnStopRecord.setOnClickListener(this);
        this.mTxtHandAction.setOnClickListener(this);
        this.mBtnEnableAnnotate.setOnClickListener(this);
        this.mBtnDisableAnnotate.setOnClickListener(this);
        this.mBtnShowAnnotatorName.setOnClickListener(this);
        this.mBtnHideAnnotatorName.setOnClickListener(this);
        this.mTxtMeetingSettings.setOnClickListener(this);
        this.mBtnDisconnectAudio.setOnClickListener(this);
        this.mMeetingReactionView.setListener(this);
        this.mTxtHideMyVideoAction.setOnClickListener(this);
        this.mTxtHideNoVideoAction.setOnClickListener(this);
        this.mTxtShowJoinLeaveTipAction.setOnClickListener(this);
        this.mLanguageInterpretation.setOnClickListener(this);
        this.mPanelEnableLiveTranscript.setOnClickListener(this);
        this.mPanelDisableLiveTranscript.setOnClickListener(this);
        this.mPanelViewFullTranscript.setOnClickListener(this);
        this.mPanelOriginalSound.setOnClickListener(this);
        this.mPanelUnreadMessage.setOnClickListener(this);
        this.mBtnEnableAnnotate.setVisibility(8);
        this.mBtnDisableAnnotate.setVisibility(8);
        this.mBtnShowAnnotatorName.setVisibility(8);
        this.mBtnHideAnnotatorName.setVisibility(8);
        return zMTip;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateRecord(boolean z, boolean z2) {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                if (ConfLocalHelper.isViewOnlyButNotSpeakAttendee()) {
                    this.mLlRecordStatus.setVisibility(8);
                    return;
                }
                if (z) {
                    this.mLlRecordStatus.setVisibility(!z2 ? 0 : 8);
                    if (confStatusObj.isCMRInConnecting()) {
                        this.mImgRecording.setVisibility(8);
                        this.mBtnPauseRecord.setVisibility(4);
                        this.mBtnStopRecord.setVisibility(4);
                        this.mProgressStartingRecord.setVisibility(0);
                        this.mTxtRecordStatus.setText(C4558R.string.zm_record_status_preparing);
                    } else {
                        if (recordMgr.isCMRPaused()) {
                            this.mImgRecording.setVisibility(8);
                            this.mTxtRecordStatus.setText(C4558R.string.zm_record_status_paused);
                        } else {
                            this.mImgRecording.setVisibility(0);
                            this.mTxtRecordStatus.setText(C4558R.string.zm_record_status_recording);
                        }
                        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                        if (confContext == null || !confContext.isAutoCMRForbidManualStop() || recordMgr.isCMRPaused()) {
                            this.mBtnPauseRecord.setVisibility(0);
                            this.mBtnStopRecord.setVisibility(0);
                            this.mBtnPauseRecord.setImageResource(recordMgr.isCMRPaused() ? C4558R.C4559drawable.zm_btn_record_resume : C4558R.C4559drawable.zm_btn_record_pause);
                            this.mBtnPauseRecord.setContentDescription(getString(recordMgr.isCMRPaused() ? C4558R.string.zm_record_btn_resume : C4558R.string.zm_record_btn_pause));
                        } else {
                            this.mBtnPauseRecord.setVisibility(4);
                            this.mBtnStopRecord.setVisibility(4);
                        }
                        this.mProgressStartingRecord.setVisibility(8);
                    }
                } else {
                    this.mLlRecordStatus.setVisibility(8);
                }
            }
        }
    }

    private void updateUI() {
        if (!ConfMgr.getInstance().isConfConnected()) {
            dismiss();
            return;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            dismiss();
            return;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            dismiss();
            return;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            dismiss();
            return;
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            dismiss();
        } else if (ConfMgr.getInstance().getAudioObj() == null) {
            dismiss();
        } else {
            this.mPanelMeetingReaction.setVisibility(MeetingReactionMgr.getInstance().isMeetingReactionEnable() ? 0 : 8);
            boolean result = ZMPolicyDataHelper.getInstance().queryBooleanPolicy(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip).getResult();
            boolean orginalHost = confContext.getOrginalHost();
            BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
            if (myself.isHostCoHost()) {
                this.mPanelMeetingSettings.setVisibility(0);
            }
            this.mTxtMeetingSettings.setText(confContext.isWebinar() ? C4558R.string.zm_title_setting_webniar_147675 : C4558R.string.zm_title_setting_meeting);
            if (ZmAudioUtils.isShowOriginalSoundOption()) {
                this.mPanelOriginalSound.setVisibility(0);
                this.mTxtOriginalSound.setText(ZmAudioUtils.isEnableOriginalSound() ? C4558R.string.zm_lbl_disable_original_sound_145354 : C4558R.string.zm_lbl_enable_original_sound_145354);
            } else {
                this.mPanelOriginalSound.setVisibility(8);
            }
            long audiotype = audioStatusObj.getAudiotype();
            if (2 == audiotype) {
                this.mPanelDisconnectAudio.setVisibility(8);
            } else {
                this.mPanelDisconnectAudio.setVisibility((isDisconnectAudioDisabled() || (1 == audiotype && confStatusObj.isDialIn())) ? 8 : 0);
            }
            ConfActivity confActivity = (ConfActivity) getActivity();
            if (myself.isHostCoHost() || myself.isBOModerator()) {
                RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
                if (myself.isBOModerator() || recordMgr == null || !recordMgr.canStartCMR()) {
                    this.mPanelRecord.setVisibility(8);
                } else {
                    boolean isRecordingInProgress = recordMgr.isRecordingInProgress();
                    this.mBtnStartRecord.setVisibility(isRecordingInProgress ? 8 : 0);
                    this.mLlRecordStatus.setVisibility(isRecordingInProgress ? 0 : 8);
                    boolean isRecordingInProgress2 = recordMgr.isRecordingInProgress();
                    if (confActivity != null) {
                        updateRecord(isRecordingInProgress2, confActivity.isInDriveMode());
                    } else {
                        updateRecord(isRecordingInProgress2, false);
                    }
                }
                if (myself.isHostCoHost()) {
                    this.mPanelHideNoVideoAction.setVisibility(8);
                    this.mPanelShowJoinLeaveTipAction.setVisibility(8);
                    this.mPanelHideMyVideoAction.setVisibility(8);
                } else {
                    if (ZMPolicyUIHelper.isLockedAutoHideNoVideoUsers()) {
                        this.mPanelHideNoVideoAction.setVisibility(8);
                    } else {
                        this.mPanelHideNoVideoAction.setVisibility(0);
                        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                        this.mTxtHideNoVideoAction.setText(videoObj != null && videoObj.hideNoVideoUserInWallView() ? C4558R.string.zm_lbl_meeting_show_no_video_101131 : C4558R.string.zm_lbl_meeting_hide_no_video_101131);
                    }
                    this.mPanelShowJoinLeaveTipAction.setVisibility(0);
                    this.mTxtShowJoinLeaveTipAction.setText(result ? C4558R.string.zm_lbl_meeting_hide_join_leave_tip_117565 : C4558R.string.zm_lbl_meeting_show_join_leave_tip_117565);
                    this.mPanelHideMyVideoAction.setVisibility(ConfLocalHelper.isMeetShowMyVideoButton() ? 0 : 8);
                    this.mTxtHideMyVideoAction.setText(ConfMgr.getInstance().getConfDataHelper().ismIsShowMyVideoInGalleryView() ? C4558R.string.zm_lbl_meeting_hide_my_video_33098 : C4558R.string.zm_lbl_meeting_show_my_video_33098);
                }
                this.mPanelHandAction.setVisibility(8);
                this.mPanelNonHostAction.setVisibility(8);
            } else {
                this.mPanelRecord.setVisibility(8);
                if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                    this.mPanelHideMyVideoAction.setVisibility(8);
                    this.mPanelHandAction.setVisibility(8);
                    this.mPanelHideNoVideoAction.setVisibility(8);
                    this.mPanelShowJoinLeaveTipAction.setVisibility(8);
                } else {
                    if (ZMPolicyUIHelper.isLockedAutoHideNoVideoUsers()) {
                        this.mPanelHideNoVideoAction.setVisibility(8);
                    } else {
                        this.mPanelHideNoVideoAction.setVisibility(0);
                        VideoSessionMgr videoObj2 = ConfMgr.getInstance().getVideoObj();
                        this.mTxtHideNoVideoAction.setText(videoObj2 != null && videoObj2.hideNoVideoUserInWallView() ? C4558R.string.zm_lbl_meeting_show_no_video_101131 : C4558R.string.zm_lbl_meeting_hide_no_video_101131);
                    }
                    this.mPanelShowJoinLeaveTipAction.setVisibility(0);
                    this.mTxtShowJoinLeaveTipAction.setText(result ? C4558R.string.zm_lbl_meeting_hide_join_leave_tip_117565 : C4558R.string.zm_lbl_meeting_show_join_leave_tip_117565);
                    this.mPanelHideMyVideoAction.setVisibility(ConfLocalHelper.isMeetShowMyVideoButton() ? 0 : 8);
                    this.mTxtHideMyVideoAction.setText(ConfMgr.getInstance().getConfDataHelper().ismIsShowMyVideoInGalleryView() ? C4558R.string.zm_lbl_meeting_hide_my_video_33098 : C4558R.string.zm_lbl_meeting_show_my_video_33098);
                    this.mPanelHandAction.setVisibility(0);
                    if (myself.getRaiseHandState()) {
                        this.mTxtHandAction.setText(getString(C4558R.string.zm_btn_lower_hand));
                        this.mTxtHandAction.setContentDescription(getString(C4558R.string.zm_description_msg_myself_lower_hand_17843));
                    } else {
                        this.mTxtHandAction.setText(getString(C4558R.string.zm_btn_raise_hand));
                        this.mTxtHandAction.setContentDescription(getString(C4558R.string.zm_description_msg_myself_raise_hand_17843));
                    }
                }
                if (confStatusObj.hasHostinMeeting() || VideoBoxApplication.getInstance().isSDKMode() || (bOMgr != null && bOMgr.isInBOMeeting())) {
                    this.mPanelNonHostAction.setVisibility(8);
                } else {
                    this.mPanelNonHostAction.setVisibility(0);
                }
            }
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null && isStartingShare() && shareObj.senderSupportAnnotation(0) && (!myself.isHostCoHost() || myself.isBOModerator())) {
                boolean isAttendeeAnnotationDisabledForMySharedContent = shareObj.isAttendeeAnnotationDisabledForMySharedContent();
                this.mBtnEnableAnnotate.setVisibility(isAttendeeAnnotationDisabledForMySharedContent ? 0 : 8);
                this.mBtnDisableAnnotate.setVisibility(isAttendeeAnnotationDisabledForMySharedContent ? 8 : 0);
                boolean isShowAnnotatorName = shareObj.isShowAnnotatorName();
                this.mBtnShowAnnotatorName.setVisibility(isShowAnnotatorName ? 8 : 0);
                this.mBtnHideAnnotatorName.setVisibility(isShowAnnotatorName ? 0 : 8);
            }
            boolean z = bOMgr != null && bOMgr.isInBOMeeting();
            if (!orginalHost) {
                this.mPanelClaimHost.setVisibility(8);
            } else if ((z || myself.isHost()) && (!z || bOMgr.isBOController())) {
                this.mPanelClaimHost.setVisibility(8);
            } else {
                this.mPanelClaimHost.setVisibility(0);
            }
            this.mLanguageInterpretation.setVisibility(ConfLocalHelper.canUseInterpretation(ConfMgr.getInstance().getInterpretationObj()) ? 0 : 8);
            this.mPanelEnableLiveTranscript.setVisibility(8);
            this.mPanelDisableLiveTranscript.setVisibility(8);
            this.mPanelViewFullTranscript.setVisibility(8);
            if (!z && confContext.isLiveTranscriptionFeatureOn() && myself.isHost()) {
                if (confStatusObj.getLiveTranscriptionStatus() == 1) {
                    this.mPanelDisableLiveTranscript.setVisibility(0);
                } else {
                    this.mPanelEnableLiveTranscript.setVisibility(0);
                }
            }
            if (confContext.isLiveTranscriptionFeatureOn() || confContext.isClosedCaptionOn()) {
                this.mPanelViewFullTranscript.setVisibility(0);
            }
            if ((!confContext.isWebinar() || !ConfMgr.getInstance().isViewOnlyMeeting()) && !confContext.isChatOff()) {
                this.mPanelUnreadMessage.setVisibility(0);
                int unreadCount = ConfMgr.getInstance().getUnreadCount();
                if (unreadCount == 0) {
                    this.mTxtUnreadMessageAction.setText(getString(C4558R.string.zm_btn_chat_109011));
                } else {
                    this.mTxtUnreadMessageAction.setText(String.format(getString(C4558R.string.zm_lbl_unread_message_147675), new Object[]{Integer.valueOf(unreadCount)}));
                }
            } else {
                this.mPanelUnreadMessage.setVisibility(8);
            }
            ZMTip tip = getTip();
            if (tip != null) {
                ZMTipLayer zMTipLayer = (ZMTipLayer) tip.getParent();
                if (zMTipLayer != null) {
                    zMTipLayer.requestLayout();
                }
            }
        }
    }

    public void onDestroyView() {
        MeetingReactionView meetingReactionView = this.mMeetingReactionView;
        if (meetingReactionView != null) {
            meetingReactionView.setListener(null);
        }
        super.onDestroyView();
    }

    public void onSelectMeetingReaction(int i, int i2) {
        ConfMgr.getInstance().sendEmojiReaction(i, i2);
        dismiss();
    }

    public void onSelectMeetingReaction(String str) {
        ConfMgr.getInstance().sendEmojiReaction(str);
        dismiss();
    }

    public void onClick(View view) {
        if (view == this.mBtnStartRecord) {
            onClickBtnStartRecord();
            dismiss();
        } else if (view == this.mBtnStopRecord) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                ConfirmStopRecordDialog.showConfirmStopRecordDialog(zMActivity);
                dismiss();
            }
        } else if (view == this.mBtnPauseRecord) {
            onClickBtnPauseRecord();
        } else if (view == this.mTxtMeetingSettings) {
            InMeetingSettingsActivity.show((ConfActivity) getActivity());
            dismiss();
        } else if (view == this.mBtnDisconnectAudio) {
            onClickBtnDisconnectAudio();
            dismiss();
        } else if (view == this.mTxtHandAction) {
            ConfLocalHelper.performRaiseOrLowerHandAction((ZMActivity) getActivity(), this.mTxtHandAction);
            dismiss();
        } else if (view == this.mBtnEnableAnnotate) {
            onClickAnnotateDisable(false);
            dismiss();
        } else if (view == this.mBtnDisableAnnotate) {
            onClickAnnotateDisable(true);
            dismiss();
        } else if (view == this.mBtnShowAnnotatorName) {
            onClickEnableAnnotatorOption(true);
            dismiss();
        } else if (view == this.mBtnHideAnnotatorName) {
            onClickEnableAnnotatorOption(false);
            dismiss();
        } else if (view == this.mBtnLoginAsHost) {
            onClickLoginAsHost();
            dismiss();
        } else if (view == this.mBtnClaimHostByHostKey) {
            onClickClaimHostByHostKey();
            dismiss();
        } else if (view == this.mBtnClaimHost) {
            onClickClaimHost();
            dismiss();
        } else if (view == this.mTxtHideMyVideoAction) {
            onClickShowMyVideo();
            dismiss();
        } else if (view == this.mLanguageInterpretation) {
            dismiss();
            LanguageInterpretationDialog.showAsActivity((ZMActivity) getActivity());
        } else if (view == this.mPanelEnableLiveTranscript) {
            dismiss();
            ConfMgr.getInstance().handleConfCmd(143);
        } else if (view == this.mPanelDisableLiveTranscript) {
            dismiss();
            ConfMgr.getInstance().handleConfCmd(144);
        } else if (view == this.mPanelViewFullTranscript) {
            dismiss();
            RealTimeTranscriptionDialog.showAsActivity((ZMActivity) getActivity());
        } else if (view == this.mTxtHideNoVideoAction) {
            onClickHideNoVideo();
            dismiss();
        } else if (view == this.mTxtShowJoinLeaveTipAction) {
            onClickShowJoinLeaveTip();
            dismiss();
        } else if (view == this.mPanelUnreadMessage) {
            onClickUnreadMessageTip();
        } else if (view == this.mPanelOriginalSound) {
            onClickOriginalSound();
            dismiss();
        }
    }

    private void onClickUnreadMessageTip() {
        ConfLocalHelper.showChatUI((ZMActivity) getActivity(), 0);
        dismiss();
    }

    private void onClickOriginalSound() {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            audioObj.setEnableMicKeepOriInput(!ZmAudioUtils.isEnableOriginalSound());
        }
    }

    private void onClickShowJoinLeaveTip() {
        boolean result = ZMPolicyDataHelper.getInstance().queryBooleanPolicy(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip).getResult();
        ZMPolicyDataHelper.getInstance().setBooleanValue(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip, !result);
        this.mTxtShowJoinLeaveTipAction.setText(!result ? C4558R.string.zm_lbl_meeting_hide_join_leave_tip_117565 : C4558R.string.zm_lbl_meeting_show_join_leave_tip_117565);
    }

    private void onClickHideNoVideo() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            boolean z = !videoObj.hideNoVideoUserInWallView();
            this.mTxtHideNoVideoAction.setText(z ? C4558R.string.zm_lbl_meeting_show_no_video_101131 : C4558R.string.zm_lbl_meeting_hide_no_video_101131);
            ConfUI.getInstance().handleConfInnerEvent(1, !z ? 0 : 1);
        }
    }

    private void onClickClaimHostByHostKey() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null || !confStatusObj.hasHostinMeeting()) {
            ConfActivity confActivity = (ConfActivity) getActivity();
            if (confActivity != null) {
                confActivity.enterHostKeyToClaimHost();
            }
            return;
        }
        updateUI();
    }

    private void onClickClaimHost() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            ConfMgr.getInstance().handleUserCmd(31, myself.getNodeId());
        }
    }

    private void onClickLoginAsHost() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null || !confStatusObj.hasHostinMeeting()) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                LoginAsHostAlertDialog.showLoginAsHostAlertDialog(zMActivity);
                return;
            }
            return;
        }
        updateUI();
    }

    private void onClickBtnStartRecord() {
        ConfLocalHelper.startCMR((ZMActivity) getActivity());
    }

    private void onClickBtnPauseRecord() {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr == null) {
            return;
        }
        if (recordMgr.isCMRPaused()) {
            if (ConfLocalHelper.resumeRecord()) {
                this.mBtnPauseRecord.setImageResource(C4558R.C4559drawable.zm_btn_record_pause);
            }
            this.mBtnPauseRecord.setContentDescription(getString(C4558R.string.zm_record_btn_pause));
        } else if (((ZMActivity) getActivity()) != null && ConfLocalHelper.pauseRecord()) {
            this.mBtnPauseRecord.setImageResource(C4558R.C4559drawable.zm_btn_record_resume);
            this.mBtnPauseRecord.setContentDescription(getString(C4558R.string.zm_record_btn_resume));
        }
    }

    private void onClickBtnDisconnectAudio() {
        ConfLocalHelper.disconnectAudio();
    }

    private void onClickAnnotateDisable(boolean z) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.DisableAttendeeAnnotationForMySharedContent(z);
            AnnoDataMgr.getInstance().setAttendeeAnnotateDisable(z);
            if (!z && ScreenShareMgr.getInstance().isSharing()) {
                ScreenShareMgr.getInstance().setAnnoToolbarVisible(true);
            }
        }
    }

    private void onClickEnableAnnotatorOption(boolean z) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            shareObj.EnableShowAnnotatorName(z);
        }
    }

    private void onClickShowMyVideo() {
        int i = 1;
        boolean z = !ConfMgr.getInstance().getConfDataHelper().ismIsShowMyVideoInGalleryView();
        this.mTxtHideMyVideoAction.setText(z ? C4558R.string.zm_lbl_meeting_hide_my_video_33098 : C4558R.string.zm_lbl_meeting_show_my_video_33098);
        ConfUI instance = ConfUI.getInstance();
        if (!z) {
            i = 0;
        }
        instance.handleConfInnerEvent(0, i);
    }

    private boolean isStartingShare() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        boolean z = false;
        if (shareObj == null) {
            return false;
        }
        int shareStatus = shareObj.getShareStatus();
        if (shareStatus == 2 || shareStatus == 1) {
            z = true;
        }
        return z;
    }

    private boolean isDisconnectAudioDisabled() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        return confActivity != null && confActivity.getConfParams().isDisconnectAudioDisabled();
    }
}
