package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.util.ZMConfUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class PListItem {
    public boolean audioOn;
    public long audioType = 0;
    @Nullable
    public String avatar;
    @NonNull
    private ComparablePItemFields comparablePItemFields = new ComparablePItemFields();
    public boolean hasCamera = true;
    private boolean isAttentionMode = false;
    private boolean isSetAvatar = false;
    private boolean isWebinar = false;
    public CmmUser mCmmUser;
    private long mRaiseHandTimestamp;
    public String screenName;
    public int unreadMessageCount;
    public String userFBID;
    public long userId;
    public boolean videoOn;

    public PListItem(CmmUser cmmUser) {
        update(cmmUser);
    }

    @NonNull
    public PListItem update(long j) {
        update(ConfMgr.getInstance().getUserById(j));
        return this;
    }

    @NonNull
    public PListItem update(@Nullable CmmUser cmmUser) {
        this.mCmmUser = cmmUser;
        if (cmmUser == null) {
            return this;
        }
        if (cmmUser.isViewOnlyUserCanTalk()) {
            ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(cmmUser.getNodeId());
            if (zoomQABuddyByNodeId != null) {
                this.mRaiseHandTimestamp = zoomQABuddyByNodeId.getRaiseHandTimestamp();
            }
        } else {
            this.mRaiseHandTimestamp = cmmUser.getRaiseHandTimestamp();
        }
        this.screenName = cmmUser.getScreenName();
        this.userFBID = cmmUser.getUserFBID();
        this.userId = cmmUser.getNodeId();
        int i = 0;
        this.isSetAvatar = false;
        String[] unreadChatMessagesByUser = ConfMgr.getInstance().getUnreadChatMessagesByUser(this.userId, false);
        if (unreadChatMessagesByUser == null) {
            this.unreadMessageCount = 0;
        } else {
            if (!this.isWebinar) {
                i = unreadChatMessagesByUser.length;
            }
            this.unreadMessageCount = i;
        }
        this.isAttentionMode = cmmUser.isInAttentionMode();
        CmmAudioStatus audioStatusObj = cmmUser.getAudioStatusObj();
        if (audioStatusObj != null) {
            this.audioOn = !audioStatusObj.getIsMuted();
            this.audioType = audioStatusObj.getAudiotype();
        }
        CmmVideoStatus videoStatusObj = cmmUser.getVideoStatusObj();
        if (videoStatusObj != null) {
            this.videoOn = videoStatusObj.getIsSending();
            this.hasCamera = videoStatusObj.getIsSource();
        }
        return this;
    }

    public boolean containsKeyInScreenName(@Nullable String str) {
        return StringUtil.isEmptyOrNull(str) || StringUtil.safeString(this.screenName).toLowerCase(CompatUtils.getLocalDefault()).contains(str);
    }

    @NonNull
    private View createViewByUserId(Context context) {
        View inflate = View.inflate(context, C4558R.layout.zm_plist_item, null);
        inflate.setTag("paneList");
        return inflate;
    }

    @Nullable
    public View getView(PListView pListView, @NonNull Context context, View view) {
        if (view == null || !"paneList".equals(view.getTag())) {
            view = createViewByUserId(context);
        }
        if ("paneList".equals(view.getTag())) {
            bindView(context, view);
        }
        return view;
    }

    /* JADX WARNING: Removed duplicated region for block: B:117:0x02c8  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x02da  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0182  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0199  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x01cf  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x021e  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0220  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0240  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x026c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void bindView(@androidx.annotation.NonNull android.content.Context r24, android.view.View r25) {
        /*
            r23 = this;
            r0 = r23
            r1 = r25
            int r2 = p021us.zoom.videomeetings.C4558R.C4560id.avatarView
            android.view.View r2 = r1.findViewById(r2)
            com.zipow.videobox.view.AvatarView r2 = (com.zipow.videobox.view.AvatarView) r2
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.txtScreenName
            android.view.View r3 = r1.findViewById(r3)
            android.widget.TextView r3 = (android.widget.TextView) r3
            int r4 = p021us.zoom.videomeetings.C4558R.C4560id.txtRole
            android.view.View r4 = r1.findViewById(r4)
            android.widget.TextView r4 = (android.widget.TextView) r4
            int r5 = p021us.zoom.videomeetings.C4558R.C4560id.txtUnreadMessageCount
            android.view.View r5 = r1.findViewById(r5)
            android.widget.TextView r5 = (android.widget.TextView) r5
            int r6 = p021us.zoom.videomeetings.C4558R.C4560id.imgAudio
            android.view.View r6 = r1.findViewById(r6)
            android.widget.ImageView r6 = (android.widget.ImageView) r6
            int r7 = p021us.zoom.videomeetings.C4558R.C4560id.imgVideo
            android.view.View r7 = r1.findViewById(r7)
            android.widget.ImageView r7 = (android.widget.ImageView) r7
            int r8 = p021us.zoom.videomeetings.C4558R.C4560id.imgRecording
            android.view.View r8 = r1.findViewById(r8)
            android.widget.ImageView r8 = (android.widget.ImageView) r8
            int r9 = p021us.zoom.videomeetings.C4558R.C4560id.imgCMRRecording
            android.view.View r9 = r1.findViewById(r9)
            android.widget.ImageView r9 = (android.widget.ImageView) r9
            int r10 = p021us.zoom.videomeetings.C4558R.C4560id.imgRaiseHand
            android.view.View r10 = r1.findViewById(r10)
            android.widget.ImageView r10 = (android.widget.ImageView) r10
            int r11 = p021us.zoom.videomeetings.C4558R.C4560id.imgLan
            android.view.View r11 = r1.findViewById(r11)
            android.widget.ImageView r11 = (android.widget.ImageView) r11
            int r12 = p021us.zoom.videomeetings.C4558R.C4560id.imgAttention
            android.view.View r12 = r1.findViewById(r12)
            android.widget.ImageView r12 = (android.widget.ImageView) r12
            int r13 = p021us.zoom.videomeetings.C4558R.C4560id.imgCc
            android.view.View r13 = r1.findViewById(r13)
            android.widget.ImageView r13 = (android.widget.ImageView) r13
            int r14 = p021us.zoom.videomeetings.C4558R.C4560id.imgPureAudio
            android.view.View r14 = r1.findViewById(r14)
            android.widget.ImageView r14 = (android.widget.ImageView) r14
            java.lang.String r15 = r0.screenName
            r3.setText(r15)
            int r3 = p021us.zoom.videomeetings.C4558R.color.zm_transparent
            r1.setBackgroundResource(r3)
            boolean r3 = r25.isInEditMode()
            if (r3 != 0) goto L_0x02de
            com.zipow.videobox.confapp.ConfMgr r3 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfStatus r15 = r3.getConfStatusObj()
            com.zipow.videobox.confapp.CmmUser r16 = r3.getMyself()
            r17 = r5
            r18 = r6
            long r5 = r0.userId
            com.zipow.videobox.confapp.CmmUser r5 = r3.getUserById(r5)
            com.zipow.videobox.confapp.CmmAttentionTrackMgr r6 = r3.getAttentionTrackAPI()
            com.zipow.videobox.confapp.ShareSessionMgr r3 = r3.getShareObj()
            if (r5 != 0) goto L_0x009d
            return
        L_0x009d:
            r19 = r7
            com.zipow.videobox.confapp.ConfUI r7 = com.zipow.videobox.confapp.ConfUI.getInstance()
            r20 = r11
            r21 = r12
            long r11 = r0.userId
            boolean r7 = r7.isDisplayAsHost(r11)
            com.zipow.videobox.confapp.ConfUI r11 = com.zipow.videobox.confapp.ConfUI.getInstance()
            r12 = r2
            r22 = r3
            long r2 = r0.userId
            boolean r2 = r11.isDisplayAsCohost(r2)
            r3 = 0
            r4.setVisibility(r3)
            if (r15 == 0) goto L_0x00f8
            r3 = r10
            long r10 = r0.userId
            boolean r10 = r15.isMyself(r10)
            if (r10 == 0) goto L_0x00f9
            if (r7 == 0) goto L_0x00da
            android.content.res.Resources r10 = r24.getResources()
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_role_me_host
            java.lang.String r10 = r10.getString(r11)
            r4.setText(r10)
            goto L_0x0146
        L_0x00da:
            if (r2 == 0) goto L_0x00ea
            android.content.res.Resources r10 = r24.getResources()
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_role_me_cohost
            java.lang.String r10 = r10.getString(r11)
            r4.setText(r10)
            goto L_0x0146
        L_0x00ea:
            android.content.res.Resources r10 = r24.getResources()
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_role_me
            java.lang.String r10 = r10.getString(r11)
            r4.setText(r10)
            goto L_0x0146
        L_0x00f8:
            r3 = r10
        L_0x00f9:
            boolean r10 = com.zipow.videobox.util.ConfLocalHelper.isGuest(r5)
            if (r10 == 0) goto L_0x0108
            boolean r10 = com.zipow.videobox.util.ConfLocalHelper.isGuestForMyself()
            if (r10 != 0) goto L_0x0108
            int r10 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_list_selector_guest
            goto L_0x010a
        L_0x0108:
            int r10 = p021us.zoom.videomeetings.C4558R.color.zm_transparent
        L_0x010a:
            r1.setBackgroundResource(r10)
            if (r7 == 0) goto L_0x011d
            android.content.res.Resources r10 = r24.getResources()
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_role_host
            java.lang.String r10 = r10.getString(r11)
            r4.setText(r10)
            goto L_0x0146
        L_0x011d:
            if (r2 == 0) goto L_0x012d
            android.content.res.Resources r10 = r24.getResources()
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_role_cohost
            java.lang.String r10 = r10.getString(r11)
            r4.setText(r10)
            goto L_0x0146
        L_0x012d:
            boolean r10 = r5.inSilentMode()
            if (r10 == 0) goto L_0x0141
            android.content.res.Resources r10 = r24.getResources()
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_role_in_silent_mode
            java.lang.String r10 = r10.getString(r11)
            r4.setText(r10)
            goto L_0x0146
        L_0x0141:
            r10 = 8
            r4.setVisibility(r10)
        L_0x0146:
            boolean r4 = com.zipow.videobox.util.ConfLocalHelper.isHost()
            if (r4 == 0) goto L_0x015a
            boolean r4 = r5.canActAsCCEditor()
            if (r4 == 0) goto L_0x015a
            boolean r4 = r5.canEditCC()
            if (r4 == 0) goto L_0x015a
            r4 = 0
            goto L_0x015c
        L_0x015a:
            r4 = 8
        L_0x015c:
            r13.setVisibility(r4)
            boolean r4 = r5.isRecording()
            com.zipow.videobox.confapp.ConfMgr r10 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.RecordMgr r10 = r10.getRecordMgr()
            if (r10 == 0) goto L_0x017f
            boolean r11 = r10.recordingMeetingOnCloud()
            if (r11 == 0) goto L_0x017f
            boolean r10 = r10.isCMRPaused()
            if (r10 != 0) goto L_0x017f
            if (r7 != 0) goto L_0x017d
            if (r2 == 0) goto L_0x017f
        L_0x017d:
            r2 = 1
            goto L_0x0180
        L_0x017f:
            r2 = 0
        L_0x0180:
            if (r2 == 0) goto L_0x0199
            r2 = 8
            r8.setVisibility(r2)
            r7 = 0
            r9.setVisibility(r7)
            android.content.res.Resources r4 = r24.getResources()
            int r8 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_recording
            java.lang.String r4 = r4.getString(r8)
            r9.setContentDescription(r4)
            goto L_0x01b8
        L_0x0199:
            r2 = 8
            r7 = 0
            if (r4 == 0) goto L_0x01b2
            r9.setVisibility(r2)
            r8.setVisibility(r7)
            android.content.res.Resources r4 = r24.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_recording
            java.lang.String r4 = r4.getString(r7)
            r8.setContentDescription(r4)
            goto L_0x01b8
        L_0x01b2:
            r8.setVisibility(r2)
            r9.setVisibility(r2)
        L_0x01b8:
            com.zipow.videobox.view.AvatarView$ParamsBuilder r2 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r2.<init>()
            java.lang.String r4 = r0.screenName
            r2.setName(r4, r4)
            boolean r4 = r5.isPureCallInUser()
            r7 = 0
            if (r4 == 0) goto L_0x01cf
            int r4 = p021us.zoom.videomeetings.C4558R.C4559drawable.avatar_phone_green
            r2.setResource(r4, r7)
            goto L_0x01ed
        L_0x01cf:
            boolean r4 = r5.isH323User()
            if (r4 == 0) goto L_0x01db
            int r4 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_h323_avatar
            r2.setResource(r4, r7)
            goto L_0x01ed
        L_0x01db:
            boolean r4 = r0.isSetAvatar
            if (r4 != 0) goto L_0x01e8
            java.lang.String r4 = r5.getSmallPicPath()
            r0.avatar = r4
            r4 = 1
            r0.isSetAvatar = r4
        L_0x01e8:
            java.lang.String r4 = r0.avatar
            r2.setPath(r4)
        L_0x01ed:
            r12.show(r2)
            com.zipow.videobox.confapp.ConfMgr r2 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfContext r2 = r2.getConfContext()
            if (r2 == 0) goto L_0x0218
            boolean r2 = r2.isFeedbackEnable()
            if (r2 == 0) goto L_0x0218
            int r2 = r5.getFeedback()
            int r2 = com.zipow.videobox.confapp.CmmFeedbackMgr.getIconIdByFeedback(r2)
            if (r2 != 0) goto L_0x0210
            r4 = 8
            r3.setVisibility(r4)
            goto L_0x0232
        L_0x0210:
            r4 = 0
            r3.setVisibility(r4)
            r3.setImageResource(r2)
            goto L_0x0232
        L_0x0218:
            boolean r2 = r5.getRaiseHandState()
            if (r2 == 0) goto L_0x0220
            r2 = 0
            goto L_0x0222
        L_0x0220:
            r2 = 8
        L_0x0222:
            r3.setVisibility(r2)
            android.content.res.Resources r2 = r24.getResources()
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_raise_hand
            java.lang.String r2 = r2.getString(r4)
            r3.setContentDescription(r2)
        L_0x0232:
            com.zipow.videobox.confapp.ConfMgr r2 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.InterpretationMgr r2 = r2.getInterpretationObj()
            boolean r2 = com.zipow.videobox.util.ConfLocalHelper.isInterpretationStarted(r2)
            if (r2 == 0) goto L_0x026c
            r11 = r20
            r2 = 0
            r11.setVisibility(r2)
            boolean r2 = r5.isInterpreter()
            if (r2 == 0) goto L_0x0251
            int r2 = r5.getInterpreterActiveLan()
            goto L_0x0255
        L_0x0251:
            int r2 = r5.getParticipantActiveLan()
        L_0x0255:
            if (r2 < 0) goto L_0x0266
            r3 = 9
            if (r2 < r3) goto L_0x025c
            goto L_0x0266
        L_0x025c:
            int[] r3 = com.zipow.videobox.confapp.InterpretationMgr.LAN_RES_IDS
            r2 = r3[r2]
            r11.setImageResource(r2)
            r2 = 8
            goto L_0x0273
        L_0x0266:
            r2 = 8
            r11.setVisibility(r2)
            goto L_0x0273
        L_0x026c:
            r11 = r20
            r2 = 8
            r11.setVisibility(r2)
        L_0x0273:
            if (r22 == 0) goto L_0x0285
            int r2 = r22.getShareStatus()
            r3 = 3
            if (r2 == r3) goto L_0x0283
            int r2 = r22.getShareStatus()
            r3 = 2
            if (r2 != r3) goto L_0x0285
        L_0x0283:
            r2 = 1
            goto L_0x0286
        L_0x0285:
            r2 = 0
        L_0x0286:
            if (r6 == 0) goto L_0x02bb
            boolean r3 = r6.isConfAttentionTrackEnabled()
            if (r3 == 0) goto L_0x02bb
            if (r2 == 0) goto L_0x02bb
            if (r16 == 0) goto L_0x02bb
            boolean r2 = r16.isHost()
            if (r2 != 0) goto L_0x02aa
            boolean r2 = r16.isCoHost()
            if (r2 != 0) goto L_0x02aa
            boolean r2 = r16.isBOModerator()
            if (r2 == 0) goto L_0x02a5
            goto L_0x02aa
        L_0x02a5:
            r2 = r21
            r3 = 8
            goto L_0x02bf
        L_0x02aa:
            boolean r2 = r0.isAttentionMode
            if (r2 == 0) goto L_0x02b2
            r2 = r21
            r3 = 4
            goto L_0x02b5
        L_0x02b2:
            r2 = r21
            r3 = 0
        L_0x02b5:
            r2.setVisibility(r3)
            r3 = 8
            goto L_0x02c2
        L_0x02bb:
            r2 = r21
            r3 = 8
        L_0x02bf:
            r2.setVisibility(r3)
        L_0x02c2:
            boolean r2 = r5.isSharingPureComputerAudio()
            if (r2 == 0) goto L_0x02da
            r2 = 0
            r14.setVisibility(r2)
            android.content.res.Resources r2 = r24.getResources()
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_accessibility_audio_sharing_41468
            java.lang.String r2 = r2.getString(r4)
            r14.setContentDescription(r2)
            goto L_0x02e5
        L_0x02da:
            r14.setVisibility(r3)
            goto L_0x02e5
        L_0x02de:
            r12 = r2
            r17 = r5
            r18 = r6
            r19 = r7
        L_0x02e5:
            long r2 = r0.audioType
            r4 = 2
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x02ef
            r2 = 1
            goto L_0x02f0
        L_0x02ef:
            r2 = 0
        L_0x02f0:
            r3 = 0
            r12.setVisibility(r3)
            if (r2 == 0) goto L_0x02fa
            r6 = r18
            r2 = 0
            goto L_0x02fd
        L_0x02fa:
            r6 = r18
            r2 = 4
        L_0x02fd:
            r6.setVisibility(r2)
            boolean r2 = r0.hasCamera
            if (r2 == 0) goto L_0x0308
            r7 = r19
            r2 = 0
            goto L_0x030b
        L_0x0308:
            r7 = r19
            r2 = 4
        L_0x030b:
            r7.setVisibility(r2)
            boolean r8 = r25.isInEditMode()
            boolean r9 = r0.audioOn
            long r10 = r0.audioType
            long r12 = r0.userId
            int r1 = com.zipow.videobox.util.ZMConfUtil.getAudioImageResId(r8, r9, r10, r12)
            r6.setImageResource(r1)
            boolean r1 = r0.videoOn
            if (r1 == 0) goto L_0x0326
            int r1 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_video_on
            goto L_0x0328
        L_0x0326:
            int r1 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_video_off
        L_0x0328:
            r7.setImageResource(r1)
            android.content.res.Resources r1 = r24.getResources()
            boolean r2 = r0.audioOn
            if (r2 == 0) goto L_0x0336
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_audio_on
            goto L_0x0338
        L_0x0336:
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_audio_off
        L_0x0338:
            java.lang.String r1 = r1.getString(r2)
            r6.setContentDescription(r1)
            android.content.res.Resources r1 = r24.getResources()
            boolean r2 = r0.videoOn
            if (r2 == 0) goto L_0x034a
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_video_on
            goto L_0x034c
        L_0x034a:
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_video_off
        L_0x034c:
            java.lang.String r1 = r1.getString(r2)
            r7.setContentDescription(r1)
            android.graphics.drawable.Drawable r1 = r6.getDrawable()
            boolean r2 = r1 instanceof android.graphics.drawable.AnimationDrawable
            if (r2 == 0) goto L_0x0360
            android.graphics.drawable.AnimationDrawable r1 = (android.graphics.drawable.AnimationDrawable) r1
            r1.start()
        L_0x0360:
            boolean r1 = r0.isWebinar
            if (r1 != 0) goto L_0x0392
            int r1 = r0.unreadMessageCount
            if (r1 <= 0) goto L_0x0392
            r5 = r17
            r1 = 0
            r5.setVisibility(r1)
            int r1 = r0.unreadMessageCount
            r2 = 100
            if (r1 >= r2) goto L_0x0379
            java.lang.String r1 = java.lang.String.valueOf(r1)
            goto L_0x037b
        L_0x0379:
            java.lang.String r1 = "99+"
        L_0x037b:
            r5.setText(r1)
            android.content.res.Resources r2 = r24.getResources()
            int r3 = p021us.zoom.videomeetings.C4558R.string.zm_description_plist_status_unread_chat_message
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r6 = 0
            r4[r6] = r1
            java.lang.String r1 = r2.getString(r3, r4)
            r5.setContentDescription(r1)
            goto L_0x0399
        L_0x0392:
            r5 = r17
            r1 = 8
            r5.setVisibility(r1)
        L_0x0399:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.PListItem.bindView(android.content.Context, android.view.View):void");
    }

    public void setWebinar(boolean z) {
        this.isWebinar = z;
    }

    public long getmRaiseHandTimestamp() {
        return this.mRaiseHandTimestamp;
    }

    @NonNull
    public ComparablePItemFields getComparablePItemFields() {
        return this.comparablePItemFields;
    }
}
