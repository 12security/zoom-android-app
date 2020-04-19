package com.zipow.videobox.confapp.meeting.confhelper;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.confapp.p009bo.BOLeaveFragment;
import com.zipow.videobox.confapp.p009bo.BOMeetingEndDialogFragment;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.confapp.p009bo.BOObject;
import com.zipow.videobox.confapp.p009bo.BOUI;
import com.zipow.videobox.confapp.p009bo.BOUI.SimpleBOUIListener;
import com.zipow.videobox.confapp.p009bo.BOUpdatedUser;
import com.zipow.videobox.confapp.p009bo.BOUtil;
import com.zipow.videobox.dialog.BOStartRequestDialog;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.view.MessageTip;
import com.zipow.videobox.view.NormalMessageButtonTip;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import com.zipow.videobox.view.video.DriverModeVideoScene;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.videomeetings.C4558R;

public class BOComponent extends SimpleBOUIListener implements OnClickListener {
    private static final String BO_MEETING_END_ALL_BO_IN_BO_TAG = "bo_end_all_bo_in_bo_tag";
    private static final String BO_MEETING_END_ALL_BO_IN_MASTER_TAG = "bo_end_all_bo_in_master_tag";
    private static final String BO_MEETING_LEAVE_BO_TAG = "bo_leave_bo_tag";
    private static final String BO_NEW_ATTENDEE_UNASSIGNED_TAG = "bo_new_attendee_unassigned_tag";
    private static final String TAG = "com.zipow.videobox.confapp.meeting.confhelper.BOComponent";
    /* access modifiers changed from: private */
    @Nullable
    public ZMAlertDialog mBOCannotForHelpDialog = null;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAlertDialog mBOControllerHasEndedDialog = null;
    private boolean mBOControllerStopTickStarted = false;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAlertDialog mBOHelpDialog = null;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAlertDialog mBOTimeUpForHostDialog = null;
    private View mBtnBO;
    private View mBtnBOHelp;
    @NonNull
    private final ConfActivity mConfActivity;
    private final View mPanelBOStatusChange;
    /* access modifiers changed from: private */
    public boolean mbBOInRequestHelp = false;

    public BOComponent(@NonNull ConfActivity confActivity) {
        this.mConfActivity = confActivity;
        this.mBtnBOHelp = confActivity.findViewById(C4558R.C4560id.btnBOHelp);
        this.mBtnBO = confActivity.findViewById(C4558R.C4560id.btnBreakout);
        this.mPanelBOStatusChange = confActivity.findViewById(C4558R.C4560id.panelBOStatusChange);
        this.mBtnBOHelp.setVisibility(8);
        this.mBtnBO.setVisibility(8);
        BOUI.getInstance().addListener(this);
        View view = this.mBtnBOHelp;
        if (view != null) {
            view.setOnClickListener(this);
        }
        View view2 = this.mBtnBO;
        if (view2 != null) {
            view2.setOnClickListener(this);
        }
    }

    public void onDestroy() {
        BOUI.getInstance().removeListener(this);
    }

    public void processLaunchConfReason(@NonNull CmmConfContext cmmConfContext, int i) {
        if (i == 10) {
            showBOStatusChangeUI(true, cmmConfContext.getBOJoinReason());
        } else if (i == 11) {
            showBOStatusChangeUI(false, 0);
        }
    }

    public void selectBOLeaveType(int i) {
        if (i == 1) {
            BOUtil.leaveBO();
        } else if (i == 2) {
            endAllBO();
        } else if (i == 3) {
            ConfLocalHelper.leaveCall(this.mConfActivity);
        } else if (i == 4) {
            ConfLocalHelper.endCall(this.mConfActivity);
        }
    }

    public void clearAllBOUI() {
        FragmentManager supportFragmentManager = this.mConfActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            BOStartRequestDialog bOStartRequestDialog = (BOStartRequestDialog) supportFragmentManager.findFragmentByTag(BOStartRequestDialog.class.getName());
            if (bOStartRequestDialog != null) {
                bOStartRequestDialog.dismiss();
            }
            BOMeetingEndDialogFragment bOMeetingEndDialogFragment = (BOMeetingEndDialogFragment) supportFragmentManager.findFragmentByTag(BO_MEETING_LEAVE_BO_TAG);
            if (bOMeetingEndDialogFragment != null) {
                bOMeetingEndDialogFragment.dismiss();
            }
            BOMeetingEndDialogFragment bOMeetingEndDialogFragment2 = (BOMeetingEndDialogFragment) supportFragmentManager.findFragmentByTag(BO_MEETING_END_ALL_BO_IN_BO_TAG);
            if (bOMeetingEndDialogFragment2 != null) {
                bOMeetingEndDialogFragment2.dismiss();
            }
            BOMeetingEndDialogFragment bOMeetingEndDialogFragment3 = (BOMeetingEndDialogFragment) supportFragmentManager.findFragmentByTag(BO_MEETING_END_ALL_BO_IN_MASTER_TAG);
            if (bOMeetingEndDialogFragment3 != null) {
                bOMeetingEndDialogFragment3.dismiss();
            }
            BOLeaveFragment bOLeaveFragment = (BOLeaveFragment) supportFragmentManager.findFragmentByTag(BOLeaveFragment.class.getSimpleName());
            if (bOLeaveFragment != null) {
                bOLeaveFragment.dismiss();
            }
            NormalMessageButtonTip.dismiss(supportFragmentManager, BO_NEW_ATTENDEE_UNASSIGNED_TAG);
            NormalMessageTip.dismiss(supportFragmentManager, TipMessageType.TIP_BO_JOIN_BREAKOUT_SESSION.name());
            ZMAlertDialog zMAlertDialog = this.mBOHelpDialog;
            if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
                this.mBOHelpDialog.dismiss();
            }
            this.mBOHelpDialog = null;
            ZMAlertDialog zMAlertDialog2 = this.mBOTimeUpForHostDialog;
            if (zMAlertDialog2 != null && zMAlertDialog2.isShowing()) {
                this.mBOTimeUpForHostDialog.dismiss();
            }
            this.mBOTimeUpForHostDialog = null;
            ZMAlertDialog zMAlertDialog3 = this.mBOCannotForHelpDialog;
            if (zMAlertDialog3 != null && zMAlertDialog3.isShowing()) {
                this.mBOCannotForHelpDialog.dismiss();
            }
            this.mBOCannotForHelpDialog = null;
            ZMAlertDialog zMAlertDialog4 = this.mBOControllerHasEndedDialog;
            if (zMAlertDialog4 != null && zMAlertDialog4.isShowing()) {
                this.mBOControllerHasEndedDialog.dismiss();
            }
            this.mBOControllerHasEndedDialog = null;
        }
    }

    public void pendingBOStartRequest() {
        updateBOButton();
        this.mConfActivity.showToolbar(true, false);
        this.mConfActivity.disableToolbarAutoHide();
        AbsVideoSceneMgr videoSceneMgr = this.mConfActivity.getVideoSceneMgr();
        if (videoSceneMgr != null) {
            AbsVideoScene activeScene = videoSceneMgr.getActiveScene();
            if (activeScene != null && this.mBtnBO != null && isBOJoinButtonNeedShow() && !ConfShareLocalHelper.isSharingOut() && !(activeScene instanceof DriverModeVideoScene)) {
                NormalMessageTip.show(this.mConfActivity.getSupportFragmentManager(), TipMessageType.TIP_BO_JOIN_BREAKOUT_SESSION.name(), (String) null, this.mConfActivity.getString(C4558R.string.zm_bo_lbl_join_bo), C4558R.C4560id.btnBreakout, 1);
            }
        }
    }

    public boolean joinBO(String str) {
        int bOControlStatus = BOUtil.getBOControlStatus();
        if (bOControlStatus != 3 && bOControlStatus != 4) {
            return BOUtil.joinBO(str, 0);
        }
        showBOMeetingHasEndedDialog();
        return false;
    }

    public void onUserEventForBO(int i, @Nullable List<ConfUserInfoEvent> list) {
        if (i == 0) {
            CmmUser bOHostUser = BOUtil.getBOHostUser();
            if (bOHostUser != null && list != null) {
                String userGUID = bOHostUser.getUserGUID();
                Iterator it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    CmmUser userById = ConfMgr.getInstance().getUserById(((ConfUserInfoEvent) it.next()).getUserId());
                    if (userById != null) {
                        String userGUID2 = userById.getUserGUID();
                        if (userGUID != null && !userGUID.isEmpty() && userGUID.equals(userGUID2)) {
                            this.mbBOInRequestHelp = false;
                            break;
                        }
                    }
                }
            }
        }
        updateBOButton();
    }

    /* access modifiers changed from: private */
    public void requestBOForHelp() {
        if (BOUtil.isHostInThisBoMeeting()) {
            showTipBoHostInCurrentMeeting();
        } else {
            BOUtil.requestBOForHelp();
        }
    }

    public void boCheckShowNewAttendeeWaitUnassignedDialog() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && !myself.inSilentMode() && BOUtil.isBOStartedAndUnassigned() && !NormalMessageButtonTip.hasTip(this.mConfActivity.getSupportFragmentManager(), BO_NEW_ATTENDEE_UNASSIGNED_TAG)) {
            NormalMessageButtonTip.show(this.mConfActivity.getSupportFragmentManager(), BO_NEW_ATTENDEE_UNASSIGNED_TAG, this.mConfActivity.getString(C4558R.string.zm_bo_lbl_wait_assigned), this.mConfActivity.getString(C4558R.string.zm_btn_ok));
        }
    }

    public void updateBOButton() {
        boolean isInBOMeeting = BOUtil.isInBOMeeting();
        int i = 8;
        if (this.mBtnBOHelp != null) {
            this.mBtnBOHelp.setVisibility(!BOUtil.isInBOController() && !BOUtil.isHostInThisBoMeeting() && isInBOMeeting && BOUtil.getBOControlStatus() == 2 && !this.mbBOInRequestHelp ? 0 : 8);
        }
        boolean isBOJoinButtonNeedShow = isBOJoinButtonNeedShow();
        View view = this.mBtnBO;
        if (view != null) {
            if (isBOJoinButtonNeedShow) {
                i = 0;
            }
            view.setVisibility(i);
        }
        if (NormalMessageTip.hasTip(this.mConfActivity.getSupportFragmentManager(), TipMessageType.TIP_BO_JOIN_BREAKOUT_SESSION.name())) {
            if (isBOJoinButtonNeedShow) {
                ZMTipLayer zMTipLayer = this.mConfActivity.getmZMTipLayer();
                if (zMTipLayer != null) {
                    zMTipLayer.requestLayout();
                }
            } else {
                NormalMessageTip.dismiss(this.mConfActivity.getSupportFragmentManager(), TipMessageType.TIP_BO_JOIN_BREAKOUT_SESSION.name());
                this.mConfActivity.hideToolbarDelayed(5000);
            }
        }
        this.mConfActivity.updateTitleBar();
    }

    public void hideBOStatusChangeUI() {
        this.mConfActivity.updateSystemStatusBar();
        this.mPanelBOStatusChange.setVisibility(4);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBreakout) {
            onClickJoinBO();
        } else if (id == C4558R.C4560id.btnBOHelp) {
            onClickBOHelp();
        }
    }

    public void onBOUserUpdated(@Nullable final BOObject bOObject, final List<BOUpdatedUser> list) {
        if (bOObject != null) {
            this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onBOUserUpdated", new EventAction("onBOUserUpdated") {
                public void run(IUIElement iUIElement) {
                    if (!BOUtil.isInBOMeeting() && !BOUtil.isInBOController()) {
                        BOComponent.this.updateBOUserList(bOObject, list);
                    }
                }
            });
        }
    }

    public void onMasterConfHostChanged(String str, boolean z) {
        this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onMasterConfHostChanged", new EventAction("onMasterConfHostChanged") {
            public void run(IUIElement iUIElement) {
                if (((ConfActivity) iUIElement) != null) {
                    BOComponent.this.boCheckHideNewAttendeeWaitUnAssignedDialog();
                }
            }
        });
    }

    public void onBOControlStatusChanged(int i) {
        this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onBOControlStatusChanged", new EventAction("onBOControlStatusChanged") {
            public void run(IUIElement iUIElement) {
                if (((ConfActivity) iUIElement) != null) {
                    BOComponent.this.boCheckHideNewAttendeeWaitUnAssignedDialog();
                }
            }
        });
    }

    public void onBORunTimeElapsed(final int i, final int i2) {
        checkShowCountdown(i, i2);
        if (BOUtil.isTimerEnabled() && BOUtil.isInBOController() && i >= i2 && i2 > 0 && BOUtil.getBOControlStatus() == 2) {
            if (BOUtil.isTimerAutoEndEnabled()) {
                endAllBO();
            } else {
                this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onBORunTimeElapsed", new EventAction("onBORunTimeElapsed") {
                    public void run(IUIElement iUIElement) {
                        if (((ConfActivity) iUIElement) != null) {
                            BOComponent.this.showBORunTimeUpDialog(i, i2);
                        }
                    }
                });
            }
        }
    }

    public void onBOStoppingTick(final int i) {
        this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onBOStoppingTick", new EventAction("onBOStoppingTick") {
            public void run(IUIElement iUIElement) {
                if (BOUtil.isInBOMeeting() && BOUtil.isInBOController()) {
                    BOComponent.this.sinkBOStoppingTick(i);
                }
            }
        });
    }

    public void onHelpRequestReceived(String str) {
        if (BOUtil.isInBOController()) {
            BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
            if (bOMgr != null) {
                bOMgr.notifyHelpRequestHandled(str, 1);
            }
        }
    }

    public void onBOStartRequestReceived(final BOObject bOObject) {
        this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onBOStartRequestReceived", new EventAction("onBOStartRequestReceived") {
            public void run(IUIElement iUIElement) {
                BOComponent.this.sinkBOStartRequest(bOObject);
            }
        });
    }

    public void onBOStopRequestReceived(final int i) {
        this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onBOStopRequestReceived", new EventAction("onBOStopRequestReceived") {
            public void run(IUIElement iUIElement) {
                BOComponent.this.sinkBOStopRequestReceived(i);
            }
        });
    }

    public void onHelpRequestHandleResultReceived(final int i) {
        this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onHelpRequestHandleResultReceived", new EventAction("onHelpRequestHandleResultReceived") {
            public void run(IUIElement iUIElement) {
                if (BOUtil.isInBOMeeting() && !BOUtil.isInBOController()) {
                    BOComponent.this.sinkBOHelpRequestHandleResult(i);
                }
            }
        });
    }

    public void OnBONewBroadcastMessageReceived(String str, long j) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = this.mConfActivity.getNonNullEventTaskManagerOrThrowException();
        final String str2 = str;
        final long j2 = j;
        C22749 r1 = new EventAction("OnBONewBroadcastMessageReceived") {
            public void run(IUIElement iUIElement) {
                if (((ConfActivity) iUIElement) != null) {
                    BOComponent.this.sinkBONewBroadcastMessageReceived(str2, j2);
                }
            }
        };
        nonNullEventTaskManagerOrThrowException.pushLater(r1);
    }

    public void onBOSwitchRequestReceived(final BOObject bOObject) {
        this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("onBOSwitchRequestReceived", new EventAction("onBOSwitchRequestReceived") {
            public void run(IUIElement iUIElement) {
                if (((ConfActivity) iUIElement) != null) {
                    BOComponent.this.sinkBOSwitchRequested(bOObject);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void boCheckHideNewAttendeeWaitUnAssignedDialog() {
        if (!BOUtil.isBOStartedAndUnassigned()) {
            NormalMessageButtonTip.dismiss(this.mConfActivity.getSupportFragmentManager(), BO_NEW_ATTENDEE_UNASSIGNED_TAG);
        }
    }

    private void showEndAllBODialogInMasterConf(int i) {
        if (((BOMeetingEndDialogFragment) this.mConfActivity.getSupportFragmentManager().findFragmentByTag(BO_MEETING_END_ALL_BO_IN_MASTER_TAG)) == null) {
            BOMeetingEndDialogFragment.showDialogFragment(this.mConfActivity.getSupportFragmentManager(), i, true, 1, BO_MEETING_END_ALL_BO_IN_MASTER_TAG);
        }
    }

    /* access modifiers changed from: private */
    public boolean endAllBO() {
        boolean z = false;
        this.mBOControllerStopTickStarted = false;
        if (BOUtil.isInBOController() && !BOUtil.isInBOMeeting()) {
            z = true;
        }
        int stopWaitingSeconds = BOUtil.getStopWaitingSeconds();
        if (stopWaitingSeconds < 0) {
            stopWaitingSeconds = 60;
        }
        boolean endBO = BOUtil.endBO(stopWaitingSeconds);
        if (endBO && z && stopWaitingSeconds > 0) {
            showEndAllBODialogInMasterConf(stopWaitingSeconds);
        }
        return endBO;
    }

    private void checkShowCountdown(final int i, final int i2) {
        if (BOUtil.isInBOMeeting() && BOUtil.isTimerEnabled() && !BOUtil.isInBOController() && i <= i2) {
            this.mConfActivity.getNonNullEventTaskManagerOrThrowException().pushLater("BO_checkShowCountdown", new EventAction("BO_checkShowCountdown") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof ConfActivityNormal) {
                        ((ConfActivityNormal) iUIElement).onBOCountdown(TimeUtil.formateDuration((long) (i2 - i)));
                    }
                }
            });
        }
    }

    private void showBOStatusChangeUI(final boolean z, final int i) {
        this.mConfActivity.getWindow().setFlags(1024, 1024);
        this.mPanelBOStatusChange.setVisibility(0);
        this.mPanelBOStatusChange.post(new Runnable() {
            public void run() {
                BOComponent.this.showBOStatusChange(z, i);
            }
        });
    }

    private void showTipBoHelpRequestNotified() {
        NormalMessageTip.show(this.mConfActivity.getSupportFragmentManager(), TipMessageType.TIP_BO_HELP_REQUEST_NOTIFIED.name(), (String) null, this.mConfActivity.getString(C4558R.string.zm_bo_msg_host_notified), 6000);
    }

    private void showTipBoHostInCurrentMeeting() {
        NormalMessageTip.show(this.mConfActivity.getSupportFragmentManager(), TipMessageType.TIP_BO_HOST_IN_CURRENT_MEETING.name(), (String) null, this.mConfActivity.getString(C4558R.string.zm_bo_msg_host_been_in_session), 6000);
    }

    /* access modifiers changed from: private */
    public void showBOStatusChange(boolean z, int i) {
        ViewStub viewStub = (ViewStub) this.mConfActivity.findViewById(C4558R.C4560id.vBOStatusChange);
        if (viewStub != null) {
            viewStub.inflate();
            ImageView imageView = (ImageView) this.mConfActivity.findViewById(C4558R.C4560id.joiningImage);
            ImageView imageView2 = (ImageView) this.mConfActivity.findViewById(C4558R.C4560id.leavingImage);
            ImageView imageView3 = (ImageView) this.mConfActivity.findViewById(C4558R.C4560id.waitingAnimation);
            TextView textView = (TextView) this.mConfActivity.findViewById(C4558R.C4560id.txtJoiningPrompt);
            TextView textView2 = (TextView) this.mConfActivity.findViewById(C4558R.C4560id.txtLeavingPrompt);
            if (z) {
                imageView.setVisibility(0);
                textView.setVisibility(0);
                imageView2.setVisibility(8);
                textView2.setVisibility(8);
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                String str = "";
                if (confContext != null) {
                    str = confContext.getBOName();
                }
                if (i == 1) {
                    textView.setText(this.mConfActivity.getResources().getString(C4558R.string.zm_bo_lbl_join_by_host_prompt, new Object[]{str}));
                } else {
                    textView.setText(this.mConfActivity.getResources().getString(C4558R.string.zm_bo_lbl_joining_prompt, new Object[]{str}));
                }
            } else {
                imageView.setVisibility(8);
                textView.setVisibility(8);
                imageView2.setVisibility(0);
                textView2.setVisibility(0);
            }
            imageView3.setImageResource(C4558R.C4559drawable.zm_bo_connecting);
            Drawable drawable = imageView3.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).start();
            }
        }
    }

    private void onBORequestHelpDialog() {
        ZMAlertDialog zMAlertDialog = this.mBOHelpDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mBOHelpDialog.dismiss();
        }
        this.mBOHelpDialog = new Builder(this.mConfActivity).setMessage(C4558R.string.zm_bo_msg_ask_for_help).setCancelable(false).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BOComponent.this.mBOHelpDialog = null;
            }
        }).setPositiveButton(C4558R.string.zm_bo_btn_ask_for_help, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BOComponent.this.requestBOForHelp();
                BOComponent.this.mbBOInRequestHelp = true;
                BOComponent.this.updateBOButton();
                BOComponent.this.mBOHelpDialog = null;
            }
        }).create();
        this.mBOHelpDialog.show();
    }

    private void hostCannotForHelpDialog() {
        ZMAlertDialog zMAlertDialog = this.mBOCannotForHelpDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mBOCannotForHelpDialog.dismiss();
        }
        this.mBOCannotForHelpDialog = new Builder(this.mConfActivity).setMessage(C4558R.string.zm_bo_msg_host_cannot_help).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BOComponent.this.mBOCannotForHelpDialog = null;
            }
        }).create();
        this.mBOCannotForHelpDialog.show();
    }

    private void showBOMeetingHasEndedDialog() {
        ZMAlertDialog zMAlertDialog = this.mBOControllerHasEndedDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mBOControllerHasEndedDialog.dismiss();
        }
        this.mBOControllerHasEndedDialog = new Builder(this.mConfActivity).setMessage(C4558R.string.zm_bo_msg_been_ended).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BOComponent.this.mBOControllerHasEndedDialog = null;
            }
        }).create();
        this.mBOControllerHasEndedDialog.show();
    }

    /* access modifiers changed from: private */
    public void sinkBOStoppingTick(int i) {
        if (BOUtil.getBOControlStatus() == 3) {
            BOMeetingEndDialogFragment bOMeetingEndDialogFragment = (BOMeetingEndDialogFragment) this.mConfActivity.getSupportFragmentManager().findFragmentByTag(BO_MEETING_END_ALL_BO_IN_BO_TAG);
            if (bOMeetingEndDialogFragment != null) {
                bOMeetingEndDialogFragment.updateWaitingSeconds(i);
            } else if (!this.mBOControllerStopTickStarted) {
                BOMeetingEndDialogFragment.showDialogFragment(this.mConfActivity.getSupportFragmentManager(), i, false, 0, BO_MEETING_END_ALL_BO_IN_BO_TAG);
            }
            this.mBOControllerStopTickStarted = true;
        }
    }

    /* access modifiers changed from: private */
    public void sinkBOStartRequest(@Nullable BOObject bOObject) {
        if (bOObject != null) {
            this.mBOTimeUpForHostDialog = null;
            if (!BOUtil.isAutoJoinEnable()) {
                boCheckHideNewAttendeeWaitUnAssignedDialog();
                if (!BOUtil.isInBOMeeting() && !BOUtil.isInBOController() && BOUtil.isBOControllerStarted()) {
                    BOStartRequestDialog.showDialog(this.mConfActivity.getSupportFragmentManager(), bOObject.getBID());
                }
            } else {
                joinBO(bOObject.getBID());
            }
        }
    }

    /* access modifiers changed from: private */
    public void sinkBOStopRequestReceived(int i) {
        if (NormalMessageTip.hasTip(this.mConfActivity.getSupportFragmentManager(), TipMessageType.TIP_BO_JOIN_BREAKOUT_SESSION.name())) {
            this.mConfActivity.hideToolbarDelayed(5000);
        }
        this.mConfActivity.refreshToolbar();
        clearAllBOUI();
        this.mbBOInRequestHelp = false;
        if (BOUtil.isInBOMeeting()) {
            if (i <= 0) {
                BOUtil.leaveBO();
                return;
            }
            if (((BOMeetingEndDialogFragment) this.mConfActivity.getSupportFragmentManager().findFragmentByTag(BO_MEETING_LEAVE_BO_TAG)) == null) {
                BOMeetingEndDialogFragment.showDialogFragment(this.mConfActivity.getSupportFragmentManager(), i, true, 0, BO_MEETING_LEAVE_BO_TAG);
            }
        }
    }

    /* access modifiers changed from: private */
    public void sinkBOHelpRequestHandleResult(int i) {
        if (i == 0) {
            showTipBoHelpRequestNotified();
        } else if (i == 1 || i == 2) {
            this.mbBOInRequestHelp = false;
            updateBOButton();
            hostCannotForHelpDialog();
        }
    }

    /* access modifiers changed from: private */
    public void sinkBONewBroadcastMessageReceived(String str, long j) {
        if (!StringUtil.isEmptyOrNull(str) && !this.mConfActivity.isInDriveMode() && !ConfLocalHelper.isDirectShareClient()) {
            String str2 = "";
            String str3 = null;
            CmmUser bOUser = BOUtil.getBOUser(j);
            if (bOUser != null) {
                str2 = bOUser.getScreenName();
                str3 = bOUser.getSmallPicPath();
            }
            MessageTip.show(this.mConfActivity.getSupportFragmentManager(), str3, this.mConfActivity.getString(C4558R.string.zm_bo_msg_to_everyone, new Object[]{str2}), str, 0);
        }
    }

    /* access modifiers changed from: private */
    public void sinkBOSwitchRequested(@Nullable BOObject bOObject) {
        if (bOObject != null) {
            if (BOUtil.isInBOMeeting()) {
                BOUtil.joinBO(bOObject.getBID(), 1);
            } else {
                updateBOSwitchRequestedUI(bOObject);
            }
        }
    }

    private void updateBOSwitchRequestedUI(@Nullable BOObject bOObject) {
        if (bOObject != null && ConfMgr.getInstance().getBOMgr() != null) {
            BOStartRequestDialog bOStartRequestDialog = (BOStartRequestDialog) this.mConfActivity.getSupportFragmentManager().findFragmentByTag(BOStartRequestDialog.class.getName());
            if (bOStartRequestDialog != null) {
                bOStartRequestDialog.dismiss();
            }
            BOStartRequestDialog.showDialog(this.mConfActivity.getSupportFragmentManager(), bOObject.getBID());
        }
    }

    /* access modifiers changed from: private */
    public void updateBOUserList(BOObject bOObject, List<BOUpdatedUser> list) {
        if (BOUtil.isHostInThisBoMeeting()) {
            this.mbBOInRequestHelp = false;
        }
        updateBOButton();
    }

    private void onClickJoinBO() {
        updateBOButton();
        BOObject myBOMeeting = BOUtil.getMyBOMeeting(1);
        if (myBOMeeting != null) {
            BOUtil.joinBO(myBOMeeting.getBID(), 0);
        }
    }

    /* access modifiers changed from: private */
    public void showBORunTimeUpDialog(int i, int i2) {
        if (this.mBOTimeUpForHostDialog == null) {
            clearAllBOUI();
            int i3 = i2 / 60;
            this.mBOTimeUpForHostDialog = new Builder(this.mConfActivity).setMessage(this.mConfActivity.getResources().getString(C4558R.string.zm_bo_msg_time_up_notification_34298, new Object[]{Integer.valueOf(i3)})).setCancelable(false).setNegativeButton(C4558R.string.zm_btn_keep_open_34298, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_close_now_34298, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    BOComponent.this.endAllBO();
                    BOComponent.this.mBOTimeUpForHostDialog = null;
                }
            }).create();
            this.mBOTimeUpForHostDialog.show();
        }
    }

    private void onClickBOHelp() {
        if (BOUtil.isHostInThisBoMeeting()) {
            showTipBoHostInCurrentMeeting();
        } else {
            onBORequestHelpDialog();
        }
    }

    private boolean isBOJoinButtonNeedShow() {
        BOObject myBOMeeting = BOUtil.getMyBOMeeting(1);
        if (BOUtil.isInBOController() || BOUtil.getBOControlStatus() != 2 || BOUtil.isInBOMeeting() || myBOMeeting == null) {
            return false;
        }
        return true;
    }
}
