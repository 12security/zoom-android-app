package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zipow.cmmlib.AppUtil;
import com.zipow.nydus.NydusUtil;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.IncomingCallManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IConfInvitationListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.sip.server.CmmSIPAPI;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ContactsAvatarCache;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.NotificationMgr.NotificationType;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.SipPopUtils;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMFacebookUtils;
import com.zipow.videobox.util.ZMUtils;
import com.zipow.videobox.view.AudioClip;
import com.zipow.videobox.view.AvatarView;
import java.util.Timer;
import java.util.TimerTask;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class CallingActivity extends ZMActivity implements OnClickListener, Callback, IConfInvitationListener, IABContactsCacheListener {
    public static final String ARG_INVITATION = "invitation";
    public static final long MEETTING_FROM_PBX_TIMEOUT_VALUE = 10800000;
    private static final String TAG = "CallingActivity";
    public static final int TIMEOUT_VALUE = 60000;
    private static final long[] VIBRATES = {2000, 1000, 2000, 1000};
    private AvatarView mAvatarView;
    /* access modifiers changed from: private */
    public Button mBtnAccept;
    private Button mBtnDecline;
    @Nullable
    private Camera mCamera;
    private int mFailedTimes = 0;
    private final Runnable mFirstFocusRunnable = new Runnable() {
        public void run() {
            CallingActivity.this.mBtnAccept.setContentDescription(CallingActivity.this.getScreenContent());
            AccessibilityUtil.sendAccessibilityFocusEvent(CallingActivity.this.mBtnAccept);
            CallingActivity.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (CallingActivity.this.isActive() && CallingActivity.this.mBtnAccept != null) {
                        CallingActivity.this.mBtnAccept.setContentDescription(null);
                    }
                }
            }, 200);
        }
    };
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public InvitationItem mInvitation;
    private boolean mIsSurfaceReady = false;
    private long mLastActivatedTime = 0;
    private View mPanelSurfaceHolder;
    @Nullable
    private AudioClip mRingClip = null;
    /* access modifiers changed from: private */
    public SurfaceView mSvPreview;
    private Timer mTimer;
    /* access modifiers changed from: private */
    public TextView mTxtCallerName;
    private TextView mTxtMsgCalling;
    private TextView mUnlockMsg;
    @Nullable
    private Vibrator mVibrator;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onIndicate_BuddyBigPictureDownloaded(String str, int i) {
            if (CallingActivity.this.mInvitation != null && StringUtil.isSameString(str, CallingActivity.this.mInvitation.getSenderJID())) {
                CallingActivity.this.updateCallerInfo();
            }
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            if (CallingActivity.this.mInvitation != null && StringUtil.isSameString(str, CallingActivity.this.mInvitation.getSenderJID())) {
                CallingActivity.this.updateCallerInfo();
            }
        }

        public void indicate_CallActionRespondedIml(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
            if (CallingActivity.this.mInvitation != null) {
                CallingActivity.this.indicate_CallActionRespondedIml(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
            }
        }
    };

    public void onConfInvitation(InvitationItem invitationItem) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    public static void show(@Nullable Context context, InvitationItem invitationItem) {
        if (context != null) {
            Intent intent = new Intent(context, CallingActivity.class);
            intent.addFlags(268435456);
            intent.putExtra("invitation", invitationItem);
            ActivityStartHelper.startActivity(context, intent, NotificationType.MEETING_CALL_NOTIFICATION.name(), null);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        NotificationMgr.removeNotification(this, 11);
        disableFinishActivityByGesture(true);
        if (UIMgr.isLargeMode(this)) {
            setRequestedOrientation(0);
        } else if (UIUtil.getDisplayMinWidthInDip(this) >= 500.0f) {
            setRequestedOrientation(UIUtil.getCurrentOrientation(this) == 2 ? 6 : 7);
        } else {
            setRequestedOrientation(1);
        }
        getWindow().addFlags(6848640);
        setContentView(C4558R.layout.zm_calling);
        this.mSvPreview = (SurfaceView) findViewById(C4558R.C4560id.svPreview);
        this.mBtnAccept = (Button) findViewById(C4558R.C4560id.btnAccept);
        this.mBtnDecline = (Button) findViewById(C4558R.C4560id.btnDecline);
        this.mPanelSurfaceHolder = findViewById(C4558R.C4560id.panelSurfaceHolder);
        this.mTxtCallerName = (TextView) findViewById(C4558R.C4560id.txtCallerName);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mUnlockMsg = (TextView) findViewById(C4558R.C4560id.unlock_msg);
        this.mTxtMsgCalling = (TextView) findViewById(C4558R.C4560id.txtMsgCalling);
        this.mBtnAccept.setOnClickListener(this);
        this.mBtnDecline.setOnClickListener(this);
        if (IncomingCallManager.getInstance().getCurrentCall() == null) {
            ignoreCall();
            return;
        }
        Intent intent = getIntent();
        if ((intent.getFlags() & 1048576) != 0) {
            IncomingCallManager.getInstance().declineCall();
            finish();
            return;
        }
        this.mInvitation = (InvitationItem) intent.getSerializableExtra("invitation");
        if (this.mInvitation == null) {
            IncomingCallManager.getInstance().declineCall();
            finish();
            return;
        }
        PTUI.getInstance().addConfInvitationListener(this);
        long j = IncomingCallManager.getInstance().isFromPbxCall(this.mInvitation) ? MEETTING_FROM_PBX_TIMEOUT_VALUE : 60000;
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                CallingActivity.this.onTimeOut();
            }
        }, j);
        startRing();
        initUIForCallType();
        initUIForPBX();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
    }

    /* access modifiers changed from: private */
    public void indicate_CallActionRespondedIml(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        InvitationItem invitationItem = this.mInvitation;
        if (invitationItem != null && invitationItem.getMeetingNumber() == j4 && i != 53) {
            if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
                AccessibilityUtil.announceForAccessibilityCompat(this.mTxtCallerName, getString(C4558R.string.zm_accessibility_call_missed_22876, new Object[]{StringUtil.safeString(this.mTxtCallerName.getText().toString())}), true);
            }
            stopPreview();
            stopRing();
            finish();
        }
    }

    private String getCallingMsg() {
        InvitationItem invitationItem = this.mInvitation;
        if (invitationItem == null || StringUtil.isEmptyOrNull(invitationItem.getGroupName())) {
            return getString(C4558R.string.zm_msg_calling_11_54639);
        }
        String groupName = this.mInvitation.getGroupName();
        int groupmembercount = this.mInvitation.getGroupmembercount();
        return getString(C4558R.string.zm_msg_calling_group_54639, new Object[]{groupName, Integer.valueOf(groupmembercount)});
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00d7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCallerInfo() {
        /*
            r10 = this;
            com.zipow.videobox.ptapp.PTAppProtos$InvitationItem r0 = r10.mInvitation
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.String r0 = r0.getSenderJID()
            com.zipow.videobox.ptapp.PTAppProtos$InvitationItem r1 = r10.mInvitation
            java.lang.String r1 = r1.getCallerPhoneNumber()
            android.widget.TextView r2 = r10.mTxtMsgCalling
            java.lang.String r3 = r10.getCallingMsg()
            r2.setText(r3)
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
            if (r2 != 0) goto L_0x01d0
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r2 = r2.getZoomMessenger()
            r3 = 0
            r4 = 0
            r5 = 1
            if (r2 == 0) goto L_0x00d4
            r6 = 64
            int r6 = r0.indexOf(r6)
            if (r6 <= 0) goto L_0x0038
            com.zipow.videobox.ptapp.mm.ZoomBuddy r6 = r2.getBuddyWithJID(r0)
            goto L_0x003c
        L_0x0038:
            com.zipow.videobox.ptapp.mm.ZoomBuddy r6 = r2.getBuddyWithPhoneNumber(r0)
        L_0x003c:
            if (r6 == 0) goto L_0x00d4
            com.zipow.videobox.ptapp.PTAppProtos$InvitationItem r7 = r10.mInvitation
            java.lang.String r7 = r7.getFromUserScreenName()
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r7)
            if (r8 == 0) goto L_0x004e
            java.lang.String r7 = com.zipow.videobox.ptapp.p013mm.BuddyNameUtil.getBuddyDisplayName(r6, r3)
        L_0x004e:
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r7)
            if (r8 == 0) goto L_0x005e
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r8 != 0) goto L_0x005e
            java.lang.String r7 = r10.getContactLocalNameFromPhoneNumber(r1)
        L_0x005e:
            if (r7 != 0) goto L_0x0062
            java.lang.String r7 = ""
        L_0x0062:
            android.widget.TextView r8 = r10.mTxtCallerName
            r8.setText(r7)
            com.zipow.videobox.view.AvatarView$ParamsBuilder r7 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r7.<init>()
            java.lang.String r8 = r6.getLocalBigPicturePath()
            boolean r9 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r8)
            if (r9 != 0) goto L_0x0087
            boolean r9 = com.zipow.videobox.util.ImageUtil.isValidImageFile(r8)
            if (r9 == 0) goto L_0x0087
            com.zipow.videobox.view.AvatarView r6 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r8 = r7.setPath(r8)
            r6.show(r8)
            r6 = 1
            goto L_0x00b7
        L_0x0087:
            boolean r9 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r8)
            if (r9 != 0) goto L_0x009b
            java.io.File r9 = new java.io.File
            r9.<init>(r8)
            boolean r8 = r9.exists()
            if (r8 == 0) goto L_0x009b
            r9.delete()
        L_0x009b:
            java.lang.String r6 = r6.getLocalPicturePath()
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r6)
            if (r8 != 0) goto L_0x00b6
            boolean r8 = com.zipow.videobox.util.ImageUtil.isValidImageFile(r6)
            if (r8 == 0) goto L_0x00b6
            com.zipow.videobox.view.AvatarView r8 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r6 = r7.setPath(r6)
            r8.show(r6)
            r6 = 1
            goto L_0x00b7
        L_0x00b6:
            r6 = 0
        L_0x00b7:
            if (r6 != 0) goto L_0x00cc
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r6 != 0) goto L_0x00cc
            java.lang.String r6 = r10.getLocalContactAvatarPathFromPhoneNumber(r1)
            com.zipow.videobox.view.AvatarView r8 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r6 = r7.setPath(r6)
            r8.show(r6)
        L_0x00cc:
            r2.refreshBuddyVCard(r0)
            r2.refreshBuddyBigPicture(r0)
            r2 = 1
            goto L_0x00d5
        L_0x00d4:
            r2 = 0
        L_0x00d5:
            if (r2 != 0) goto L_0x01ef
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.PTBuddyHelper r2 = r2.getBuddyHelper()
            if (r2 == 0) goto L_0x00e5
            com.zipow.videobox.ptapp.IMProtos$BuddyItem r3 = r2.getBuddyItemByJid(r0)
        L_0x00e5:
            com.zipow.videobox.view.AvatarView$ParamsBuilder r2 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r2.<init>()
            if (r3 == 0) goto L_0x0140
            com.zipow.videobox.ptapp.PTAppProtos$InvitationItem r4 = r10.mInvitation
            java.lang.String r4 = r4.getFromUserScreenName()
            boolean r5 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r4)
            if (r5 == 0) goto L_0x00fc
            java.lang.String r4 = r3.getScreenName()
        L_0x00fc:
            boolean r5 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r4)
            if (r5 == 0) goto L_0x010c
            boolean r5 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r5 != 0) goto L_0x010c
            java.lang.String r4 = r10.getContactLocalNameFromPhoneNumber(r1)
        L_0x010c:
            if (r4 != 0) goto L_0x0110
            java.lang.String r4 = ""
        L_0x0110:
            android.widget.TextView r1 = r10.mTxtCallerName
            r1.setText(r4)
            java.lang.String r1 = r3.getLocalPicturePath()
            boolean r4 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r4 == 0) goto L_0x0123
            java.lang.String r1 = r3.getPicture()
        L_0x0123:
            boolean r3 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r3 == 0) goto L_0x0135
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            int r1 = r1.getPTLoginType()
            java.lang.String r1 = r10.getPicPathFromJid(r1, r0)
        L_0x0135:
            com.zipow.videobox.view.AvatarView r0 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r1 = r2.setPath(r1)
            r0.show(r1)
            goto L_0x01ef
        L_0x0140:
            com.zipow.videobox.ptapp.PTAppProtos$InvitationItem r3 = r10.mInvitation
            java.lang.String r3 = r3.getFromUserScreenName()
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            if (r6 == 0) goto L_0x0156
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r6 != 0) goto L_0x0156
            java.lang.String r3 = r10.getContactLocalNameFromPhoneNumber(r1)
        L_0x0156:
            if (r3 != 0) goto L_0x015a
            java.lang.String r3 = ""
        L_0x015a:
            android.widget.TextView r6 = r10.mTxtCallerName
            r6.setText(r3)
            com.zipow.videobox.ptapp.PTApp r3 = com.zipow.videobox.ptapp.PTApp.getInstance()
            int r3 = r3.getPTLoginType()
            java.lang.String r3 = r10.getPicPathFromJid(r3, r0)
            if (r3 == 0) goto L_0x017d
            boolean r6 = com.zipow.videobox.util.ImageUtil.isValidImageFile(r3)
            if (r6 == 0) goto L_0x017d
            com.zipow.videobox.view.AvatarView r0 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r3 = r2.setPath(r3)
            r0.show(r3)
            goto L_0x01ba
        L_0x017d:
            java.lang.String r3 = r10.getPicPathFromZoomJid(r0, r5)
            boolean r6 = com.zipow.videobox.util.ImageUtil.isValidImageFile(r3)
            if (r6 == 0) goto L_0x0191
            com.zipow.videobox.view.AvatarView r0 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r3 = r2.setPath(r3)
            r0.show(r3)
            goto L_0x01ba
        L_0x0191:
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            if (r6 != 0) goto L_0x01a5
            java.io.File r6 = new java.io.File
            r6.<init>(r3)
            boolean r3 = r6.exists()
            if (r3 == 0) goto L_0x01a5
            r6.delete()
        L_0x01a5:
            java.lang.String r0 = r10.getPicPathFromZoomJid(r0, r4)
            boolean r3 = com.zipow.videobox.util.ImageUtil.isValidImageFile(r0)
            if (r3 == 0) goto L_0x01b9
            com.zipow.videobox.view.AvatarView r3 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r0 = r2.setPath(r0)
            r3.show(r0)
            goto L_0x01ba
        L_0x01b9:
            r5 = 0
        L_0x01ba:
            if (r5 != 0) goto L_0x01ef
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r0 != 0) goto L_0x01ef
            java.lang.String r0 = r10.getLocalContactAvatarPathFromPhoneNumber(r1)
            com.zipow.videobox.view.AvatarView r1 = r10.mAvatarView
            com.zipow.videobox.view.AvatarView$ParamsBuilder r0 = r2.setPath(r0)
            r1.show(r0)
            goto L_0x01ef
        L_0x01d0:
            com.zipow.videobox.ptapp.PTAppProtos$InvitationItem r0 = r10.mInvitation
            java.lang.String r0 = r0.getFromUserScreenName()
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
            if (r2 == 0) goto L_0x01e6
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r2 != 0) goto L_0x01e6
            java.lang.String r0 = r10.getContactLocalNameFromPhoneNumber(r1)
        L_0x01e6:
            if (r0 != 0) goto L_0x01ea
            java.lang.String r0 = ""
        L_0x01ea:
            android.widget.TextView r1 = r10.mTxtCallerName
            r1.setText(r0)
        L_0x01ef:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.CallingActivity.updateCallerInfo():void");
    }

    private String getContactLocalNameFromPhoneNumber(String str) {
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        if (!instance.isCached() && !instance.reloadAllContacts()) {
            return str;
        }
        Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(str);
        return (firstContactByPhoneNumber == null || StringUtil.isEmptyOrNull(firstContactByPhoneNumber.displayName)) ? str : firstContactByPhoneNumber.displayName;
    }

    private Bitmap getLocalContactAvatarFromPhoneNumber(String str) {
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        if (!instance.isCached() && !instance.reloadAllContacts()) {
            return null;
        }
        Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(str);
        if (firstContactByPhoneNumber == null) {
            return null;
        }
        return ContactsAvatarCache.getInstance().getContactAvatar(this, firstContactByPhoneNumber.contactId);
    }

    private String getLocalContactAvatarPathFromPhoneNumber(String str) {
        ABContactsCache instance = ABContactsCache.getInstance();
        instance.addListener(this);
        if (!instance.isCached() && !instance.reloadAllContacts()) {
            return null;
        }
        Contact firstContactByPhoneNumber = instance.getFirstContactByPhoneNumber(str);
        if (firstContactByPhoneNumber == null) {
            return null;
        }
        return ContactsAvatarCache.getInstance().getContactAvatarPath(firstContactByPhoneNumber.contactId);
    }

    private void initUIForCallType() {
        if (this.mInvitation.getIsAudioOnlyMeeting() || this.mInvitation.getIsShareOnlyMeeting()) {
            this.mAvatarView.setVisibility(0);
            this.mPanelSurfaceHolder.setVisibility(8);
            return;
        }
        this.mAvatarView.setVisibility(4);
        this.mPanelSurfaceHolder.setVisibility(0);
        SurfaceHolder holder = this.mSvPreview.getHolder();
        holder.setType(3);
        holder.addCallback(this);
    }

    private void initUIForPBX() {
        if (CmmSIPCallManager.getInstance().getSipIdCountInCache() <= 0 || IncomingCallManager.getInstance().isFromPbxCall()) {
            this.mBtnAccept.setText(C4558R.string.zm_btn_accept);
            return;
        }
        this.mBtnAccept.setText(C4558R.string.zm_sip_income_meeting_insip_108086);
        if (!CmmSIPAPI.isAudioTransferToMeetingPromptReaded()) {
            CmmSIPAPI.setAudioTransferToMeetingPromptAsReaded();
            this.mBtnAccept.postDelayed(new Runnable() {
                public void run() {
                    CallingActivity.this.showAudioTransferToMeetingPop();
                }
            }, 500);
        }
    }

    /* access modifiers changed from: private */
    public void showAudioTransferToMeetingPop() {
        SipPopUtils.showAudioTransferToMeetingPop(this, this.mBtnAccept);
    }

    @Nullable
    private String getPicPathFromJid(int i, @Nullable String str) {
        if (str != null && PTApp.getInstance().getPTLoginType() == 0) {
            int indexOf = str.indexOf(64);
            if (indexOf > 0) {
                str = str.substring(0, indexOf);
            }
        }
        return ZMFacebookUtils.getVCardFileName(i, str);
    }

    private String getPicPathFromZoomJid(@Nullable String str, boolean z) {
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        String recentZoomJid = PTApp.getInstance().getRecentZoomJid();
        if (StringUtil.isEmptyOrNull(recentZoomJid)) {
            return "";
        }
        if (str.indexOf(64) < 0) {
            int indexOf = recentZoomJid.indexOf(64);
            if (indexOf < 0) {
                return "";
            }
            String substring = recentZoomJid.substring(indexOf);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(substring);
            str = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(AppUtil.getDataPath());
        sb2.append("/");
        sb2.append(PTApp.getInstance().getRecentZoomJid());
        if (z) {
            sb2.append("/pic_");
        } else {
            sb2.append("/avatar_");
        }
        sb2.append(str);
        return sb2.toString();
    }

    /* access modifiers changed from: private */
    public void onTimeOut() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (AccessibilityUtil.isSpokenFeedbackEnabled(CallingActivity.this)) {
                    AccessibilityUtil.announceForAccessibilityCompat(CallingActivity.this.mTxtCallerName, CallingActivity.this.getString(C4558R.string.zm_accessibility_call_missed_22876, new Object[]{StringUtil.safeString(CallingActivity.this.mTxtCallerName.getText().toString())}), true);
                }
                CallingActivity.this.stopPreview();
                CallingActivity.this.stopRing();
                IncomingCallManager.getInstance().onCallTimeout();
                CallingActivity.this.finish();
            }
        });
    }

    public void onPause() {
        super.onPause();
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastActivatedTime;
        if (!UIUtil.isScreenLocked(this) && !isFinishing() && elapsedRealtime > 1000) {
            onClickBtnDecline();
        }
        ABContactsCache.getInstance().removeListener(this);
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void onResume() {
        super.onResume();
        if (IncomingCallManager.getInstance().getCurrentCall() == null) {
            ignoreCall();
            return;
        }
        this.mLastActivatedTime = SystemClock.elapsedRealtime();
        if (this.mIsSurfaceReady) {
            startPreview(this.mSvPreview.getHolder());
        }
        updateCallerInfo();
        this.mUnlockMsg.setVisibility(UIUtil.isScreenLocked(this) ? 0 : 8);
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
            this.mHandler.removeCallbacks(this.mFirstFocusRunnable);
            this.mHandler.postDelayed(this.mFirstFocusRunnable, 1000);
        }
    }

    public void onContactsCacheUpdated() {
        ABContactsCache.getInstance().removeListener(this);
        if (!StringUtil.isEmptyOrNull(this.mInvitation.getCallerPhoneNumber())) {
            updateCallerInfo();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        PTUI.getInstance().removeConfInvitationListener(this);
        stopRing();
        if (isFinishing()) {
            Timer timer = this.mTimer;
            if (timer != null) {
                timer.cancel();
            }
            IncomingCallManager.getInstance().clearCurrentInvitation();
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.mInvitation = (InvitationItem) intent.getSerializableExtra("invitation");
        if (this.mInvitation == null) {
            IncomingCallManager.getInstance().declineCall();
            finish();
            return;
        }
        updateCallerInfo();
    }

    public void onBackPressed() {
        onClickBtnDecline();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnAccept) {
            onClickBtnAccept();
        } else if (id == C4558R.C4560id.btnDecline) {
            onClickBtnDecline();
        }
    }

    private void onClickBtnAccept() {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
            AccessibilityUtil.announceForAccessibilityCompat((View) this.mBtnAccept, C4558R.string.zm_accessibility_call_accepted_22876);
        }
        this.mBtnAccept.setEnabled(false);
        this.mBtnDecline.setEnabled(false);
        stopPreview();
        stopRing();
        IncomingCallManager.getInstance().acceptCall(this, false);
        finish();
    }

    private void onClickBtnDecline() {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
            AccessibilityUtil.announceForAccessibilityCompat((View) this.mBtnDecline, C4558R.string.zm_accessibility_call_declined_22876);
        }
        this.mBtnAccept.setEnabled(false);
        this.mBtnDecline.setEnabled(false);
        stopPreview();
        stopRing();
        IncomingCallManager.getInstance().declineCall();
        finish();
    }

    private void startRing() {
        int i;
        if (!IncomingCallManager.getInstance().isFromPbxCall(this.mInvitation)) {
            try {
                AudioManager audioManager = (AudioManager) getSystemService("audio");
                if (audioManager != null) {
                    i = audioManager.getRingerMode();
                    if (i == 2 && this.mRingClip == null) {
                        this.mRingClip = new AudioClip(C4558R.raw.zm_ring, CmmSIPCallManager.getInstance().isInCall() ? 0 : 2);
                        this.mRingClip.startPlay();
                    }
                    if ((i == 2 || i == 1) && this.mVibrator == null) {
                        this.mVibrator = (Vibrator) getSystemService("vibrator");
                        Vibrator vibrator = this.mVibrator;
                        if (vibrator != null) {
                            vibrator.vibrate(VIBRATES, 0);
                        }
                    }
                }
            } catch (Exception unused) {
                i = 2;
            }
        }
    }

    public void stopRing() {
        AudioClip audioClip = this.mRingClip;
        if (audioClip != null) {
            audioClip.stopPlay();
            this.mRingClip = null;
        }
        Vibrator vibrator = this.mVibrator;
        if (vibrator != null) {
            vibrator.cancel();
            this.mVibrator = null;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        stopPreview();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.mIsSurfaceReady = true;
        if (isActive()) {
            startPreview(surfaceHolder);
        }
        this.mLastActivatedTime = SystemClock.elapsedRealtime();
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mIsSurfaceReady = false;
        stopPreview();
    }

    /* access modifiers changed from: private */
    public void startPreview(SurfaceHolder surfaceHolder) {
        if (this.mCamera == null && !PreferenceUtil.readBooleanValue(PreferenceUtil.CAMERA_IS_FREEZED, false) && NydusUtil.getNumberOfCameras() != 0) {
            int frontCameraId = NydusUtil.getFrontCameraId();
            if (frontCameraId < 0) {
                frontCameraId = 0;
            }
            try {
                this.mCamera = Camera.open(frontCameraId);
                this.mCamera.setPreviewDisplay(surfaceHolder);
                int cameraDisplayOrientation = setCameraDisplayOrientation(frontCameraId, this.mCamera);
                Size previewSize = setPreviewSize(this.mCamera);
                if (cameraDisplayOrientation % 180 == 90) {
                    int i = previewSize.width;
                    previewSize.width = previewSize.height;
                    previewSize.height = i;
                }
                this.mCamera.startPreview();
                relayoutSurfaceView(previewSize);
            } catch (Exception unused) {
                Camera camera = this.mCamera;
                if (camera != null) {
                    camera.release();
                }
                this.mCamera = null;
                int i2 = this.mFailedTimes + 1;
                this.mFailedTimes = i2;
                if (i2 < 4) {
                    getWindow().getDecorView().postDelayed(new Runnable() {
                        public void run() {
                            if (CallingActivity.this.isActive()) {
                                CallingActivity callingActivity = CallingActivity.this;
                                callingActivity.startPreview(callingActivity.mSvPreview.getHolder());
                            }
                        }
                    }, 300);
                }
            }
        }
    }

    private Size setPreviewSize(Camera camera) {
        return camera.getParameters().getPreviewSize();
    }

    private void relayoutSurfaceView(@Nullable Size size) {
        if (size != null) {
            int width = getWindow().getDecorView().getWidth();
            int height = getWindow().getDecorView().getHeight();
            LayoutParams layoutParams = (LayoutParams) this.mSvPreview.getLayoutParams();
            if (NydusUtil.isSamsungIncorrectPreviewSizeFrontCameraDevice()) {
                int i = (width * 4) / 3;
                int i2 = (height - i) / 2;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.width = width;
                layoutParams.topMargin = i2;
                layoutParams.bottomMargin = i2;
                layoutParams.height = i;
                layoutParams.gravity = 17;
            } else if (size.width * height > size.height * width) {
                int i3 = (size.width * height) / size.height;
                layoutParams.leftMargin = (width - i3) / 2;
                layoutParams.width = i3;
                layoutParams.topMargin = 0;
                layoutParams.height = height;
            } else {
                int i4 = (size.height * width) / size.width;
                layoutParams.topMargin = (height - i4) / 2;
                layoutParams.height = i4;
                layoutParams.leftMargin = 0;
                layoutParams.width = width;
            }
            this.mSvPreview.setLayoutParams(layoutParams);
            this.mSvPreview.getParent().requestLayout();
        }
    }

    private int setCameraDisplayOrientation(int i, @NonNull Camera camera) {
        int i2;
        CameraInfo cameraInfo = new CameraInfo();
        NydusUtil.getCameraInfo(i, cameraInfo);
        int realCameraOrientation = NydusUtil.getRealCameraOrientation(i);
        int i3 = 0;
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case 1:
                i3 = 90;
                break;
            case 2:
                i3 = 180;
                break;
            case 3:
                i3 = SubsamplingScaleImageView.ORIENTATION_270;
                break;
        }
        if (NydusUtil.hasIssueForDevicePreview()) {
            i2 = ZMUtils.getRotation(String.valueOf(i), i3);
        } else if (cameraInfo.facing == 1) {
            i2 = (360 - ((realCameraOrientation + i3) % 360)) % 360;
        } else {
            i2 = ((realCameraOrientation - i3) + 360) % 360;
        }
        camera.setDisplayOrientation(i2);
        return i2;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:3|4|5|6|7|9) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0008 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stopPreview() {
        /*
            r1 = this;
            android.hardware.Camera r0 = r1.mCamera
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            r0.stopPreview()     // Catch:{ Exception -> 0x0008 }
        L_0x0008:
            android.hardware.Camera r0 = r1.mCamera     // Catch:{ Exception -> 0x000d }
            r0.release()     // Catch:{ Exception -> 0x000d }
        L_0x000d:
            r0 = 0
            r1.mCamera = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.CallingActivity.stopPreview():void");
    }

    public void onCallAccepted(@Nullable InvitationItem invitationItem) {
        if (!(invitationItem == null || this.mInvitation == null || invitationItem.getMeetingNumber() != this.mInvitation.getMeetingNumber())) {
            ignoreCall();
        }
    }

    public void onCallDeclined(@Nullable InvitationItem invitationItem) {
        if (!(invitationItem == null || this.mInvitation == null || invitationItem.getMeetingNumber() != this.mInvitation.getMeetingNumber())) {
            IncomingCallManager.getInstance().insertDeclineCallMsg();
            ignoreCall();
        }
    }

    private void ignoreCall() {
        stopPreview();
        stopRing();
        IncomingCallManager.getInstance().ignoreCall();
        finish();
    }

    /* access modifiers changed from: private */
    public String getScreenContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.safeString(this.mTxtCallerName.getText().toString()));
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(getCallingMsg());
        sb.append(", ");
        sb.append(getString(C4558R.string.zm_accessibility_someone_accept_decline_call_22876));
        sb.append(", ");
        sb.append(getString(C4558R.string.zm_btn_accept));
        return sb.toString();
    }
}
