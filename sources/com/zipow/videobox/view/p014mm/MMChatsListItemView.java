package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.PresenceStateView;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMChatsListItemView */
public class MMChatsListItemView extends LinearLayout {
    private AvatarView mAvatarView;
    private ImageView mImgBell;
    private ImageView mImgE2EFlag;
    private ImageView mImgErrorMessage;
    private PresenceStateView mImgPresence;
    private TextView mTxtAt;
    private TextView mTxtMessag;
    private TextView mTxtNoteBubble;
    private TextView mTxtTime;
    private ZMEllipsisTextView mTxtTitle;
    private View mUnreadBubble;

    public MMChatsListItemView(Context context) {
        super(context);
        initViews();
    }

    public MMChatsListItemView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public MMChatsListItemView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        View.inflate(getContext(), C4558R.layout.zm_mm_chats_list_swipe_able_item, this);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtTitle = (ZMEllipsisTextView) findViewById(C4558R.C4560id.txtTitle);
        this.mTxtMessag = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.mTxtTime = (TextView) findViewById(C4558R.C4560id.txtTime);
        this.mTxtNoteBubble = (TextView) findViewById(C4558R.C4560id.txtNoteBubble);
        this.mImgPresence = (PresenceStateView) findViewById(C4558R.C4560id.imgPresence);
        this.mImgE2EFlag = (ImageView) findViewById(C4558R.C4560id.imgE2EFlag);
        this.mImgBell = (ImageView) findViewById(C4558R.C4560id.imgBell);
        this.mUnreadBubble = findViewById(C4558R.C4560id.unreadBubble);
        this.mImgErrorMessage = (ImageView) findViewById(C4558R.C4560id.imgErrorMessage);
        this.mTxtAt = (TextView) findViewById(C4558R.C4560id.txtAt);
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e3  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00e5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindViews(@androidx.annotation.NonNull com.zipow.videobox.view.p014mm.MMChatsListItem r17) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r2 = r2.getZoomMessenger()
            if (r2 != 0) goto L_0x000f
            return
        L_0x000f:
            java.lang.String r3 = r17.getSessionId()
            java.lang.String r4 = r17.getTitle()
            boolean r5 = r17.isGroup()
            java.lang.String r6 = r17.getSessionId()
            boolean r6 = com.zipow.videobox.util.UIMgr.isMyNotes(r6)
            int r7 = r17.getThreadSortType()
            r8 = 1
            r9 = 0
            if (r7 != 0) goto L_0x0033
            boolean r7 = r2.hasFailedMessage(r3)
            if (r7 == 0) goto L_0x0033
            r7 = 1
            goto L_0x0034
        L_0x0033:
            r7 = 0
        L_0x0034:
            java.lang.String r10 = r2.getContactRequestsSessionID()
            com.zipow.videobox.view.AvatarView r11 = r0.mAvatarView
            if (r11 == 0) goto L_0x0084
            boolean r11 = p021us.zoom.androidlib.util.StringUtil.isSameString(r10, r3)
            r12 = 0
            if (r11 == 0) goto L_0x004f
            com.zipow.videobox.view.AvatarView$ParamsBuilder r11 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r11.<init>()
            int r13 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_im_contact_request
            com.zipow.videobox.view.AvatarView$ParamsBuilder r12 = r11.setResource(r13, r12)
            goto L_0x007f
        L_0x004f:
            if (r5 != 0) goto L_0x0060
            com.zipow.videobox.view.IMAddrBookItem r11 = r17.getFromContact()
            if (r11 == 0) goto L_0x0060
            com.zipow.videobox.view.IMAddrBookItem r11 = r17.getFromContact()
            com.zipow.videobox.view.AvatarView$ParamsBuilder r12 = r11.getAvatarParamsBuilder()
            goto L_0x007f
        L_0x0060:
            if (r5 == 0) goto L_0x007f
            boolean r11 = r1.isAnnounceMent(r3)
            if (r11 == 0) goto L_0x0074
            com.zipow.videobox.view.AvatarView$ParamsBuilder r11 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r11.<init>()
            int r13 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_announcement
            com.zipow.videobox.view.AvatarView$ParamsBuilder r12 = r11.setResource(r13, r12)
            goto L_0x007f
        L_0x0074:
            com.zipow.videobox.view.AvatarView$ParamsBuilder r11 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r11.<init>()
            int r13 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_avatar_group
            com.zipow.videobox.view.AvatarView$ParamsBuilder r12 = r11.setResource(r13, r12)
        L_0x007f:
            com.zipow.videobox.view.AvatarView r11 = r0.mAvatarView
            r11.show(r12)
        L_0x0084:
            us.zoom.androidlib.widget.ZMEllipsisTextView r11 = r0.mTxtTitle
            r12 = 2
            if (r11 == 0) goto L_0x0115
            if (r4 == 0) goto L_0x0115
            boolean r11 = r1.isAnnounceMent(r3)
            if (r11 == 0) goto L_0x00a9
            us.zoom.androidlib.widget.ZMEllipsisTextView r4 = r0.mTxtTitle
            android.text.TextUtils$TruncateAt r11 = android.text.TextUtils.TruncateAt.MIDDLE
            r4.setEllipsize(r11)
            us.zoom.androidlib.widget.ZMEllipsisTextView r4 = r0.mTxtTitle
            android.content.res.Resources r11 = r16.getResources()
            int r13 = p021us.zoom.videomeetings.C4558R.string.zm_msg_announcements_108966
            java.lang.String r11 = r11.getString(r13)
            r4.setText(r11)
            goto L_0x0115
        L_0x00a9:
            if (r6 == 0) goto L_0x00c6
            us.zoom.androidlib.widget.ZMEllipsisTextView r11 = r0.mTxtTitle
            android.text.TextUtils$TruncateAt r13 = android.text.TextUtils.TruncateAt.MIDDLE
            r11.setEllipsize(r13)
            us.zoom.androidlib.widget.ZMEllipsisTextView r11 = r0.mTxtTitle
            android.content.res.Resources r13 = r16.getResources()
            int r14 = p021us.zoom.videomeetings.C4558R.string.zm_mm_msg_my_notes_65147
            java.lang.Object[] r15 = new java.lang.Object[r8]
            r15[r9] = r4
            java.lang.String r4 = r13.getString(r14, r15)
            r11.setText(r4)
            goto L_0x0115
        L_0x00c6:
            if (r5 != 0) goto L_0x00f2
            com.zipow.videobox.view.IMAddrBookItem r11 = r17.getFromContact()
            if (r11 == 0) goto L_0x00e0
            int r13 = r11.getAccountStatus()
            if (r13 != r8) goto L_0x00d7
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_deactivated_62074
            goto L_0x00e1
        L_0x00d7:
            int r11 = r11.getAccountStatus()
            if (r11 != r12) goto L_0x00e0
            int r11 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_terminated_62074
            goto L_0x00e1
        L_0x00e0:
            r11 = 0
        L_0x00e1:
            if (r11 != 0) goto L_0x00e5
            r13 = r4
            goto L_0x010b
        L_0x00e5:
            android.content.res.Resources r13 = r16.getResources()
            java.lang.Object[] r14 = new java.lang.Object[r8]
            r14[r9] = r4
            java.lang.String r13 = r13.getString(r11, r14)
            goto L_0x010b
        L_0x00f2:
            boolean r11 = android.text.TextUtils.equals(r10, r3)
            if (r11 == 0) goto L_0x00fb
            r13 = r4
            r11 = 0
            goto L_0x010b
        L_0x00fb:
            android.content.res.Resources r11 = r16.getResources()
            int r13 = p021us.zoom.videomeetings.C4558R.string.zm_accessibility_group_pre_77383
            java.lang.Object[] r14 = new java.lang.Object[r8]
            r14[r9] = r4
            java.lang.String r11 = r11.getString(r13, r14)
            r13 = r11
            r11 = 0
        L_0x010b:
            us.zoom.androidlib.widget.ZMEllipsisTextView r14 = r0.mTxtTitle
            r14.setEllipsisText(r4, r11)
            us.zoom.androidlib.widget.ZMEllipsisTextView r4 = r0.mTxtTitle
            r4.setContentDescription(r13)
        L_0x0115:
            us.zoom.androidlib.widget.ZMEllipsisTextView r4 = r0.mTxtTitle
            if (r4 == 0) goto L_0x0126
            boolean r11 = r17.isNotifyOff()
            if (r11 == 0) goto L_0x0122
            int r11 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_notifications_off
            goto L_0x0123
        L_0x0122:
            r11 = 0
        L_0x0123:
            r4.setCompoundDrawablesWithIntrinsicBounds(r9, r9, r11, r9)
        L_0x0126:
            android.widget.TextView r4 = r0.mTxtMessag
            if (r4 == 0) goto L_0x014b
            r4.setVisibility(r9)
            boolean r4 = r17.hasDraftMessage()
            if (r4 == 0) goto L_0x013d
            android.widget.TextView r4 = r0.mTxtMessag
            java.lang.CharSequence r11 = r17.getDraftMessage()
            r4.setText(r11)
            goto L_0x014b
        L_0x013d:
            java.lang.CharSequence r4 = r17.getLatestMessage()
            android.widget.TextView r11 = r0.mTxtMessag
            if (r4 == 0) goto L_0x0146
            goto L_0x0148
        L_0x0146:
            java.lang.String r4 = ""
        L_0x0148:
            r11.setText(r4)
        L_0x014b:
            int r4 = r17.getUnreadMessageCountBySetting()
            int r11 = r17.getUnreadMessageCount()
            int r13 = r17.getMarkUnreadMessageCount()
            android.view.View r14 = r0.mUnreadBubble
            r15 = 8
            if (r14 == 0) goto L_0x0187
            if (r7 != 0) goto L_0x0182
            if (r4 != 0) goto L_0x0182
            if (r11 <= 0) goto L_0x0182
            if (r13 > 0) goto L_0x0182
            if (r5 == 0) goto L_0x0182
            boolean r14 = android.text.TextUtils.equals(r10, r3)
            if (r14 != 0) goto L_0x0182
            android.view.View r14 = r0.mUnreadBubble
            android.content.res.Resources r8 = r16.getResources()
            int r12 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_new_message_14491
            java.lang.String r8 = r8.getString(r12)
            r14.setContentDescription(r8)
            android.view.View r8 = r0.mUnreadBubble
            r8.setVisibility(r9)
            goto L_0x0187
        L_0x0182:
            android.view.View r8 = r0.mUnreadBubble
            r8.setVisibility(r15)
        L_0x0187:
            android.widget.TextView r8 = r0.mTxtNoteBubble
            if (r8 == 0) goto L_0x01d4
            if (r5 == 0) goto L_0x018e
            goto L_0x018f
        L_0x018e:
            r4 = r11
        L_0x018f:
            int r4 = r4 + r13
            boolean r3 = android.text.TextUtils.equals(r10, r3)
            if (r3 == 0) goto L_0x0197
            r4 = r11
        L_0x0197:
            if (r7 != 0) goto L_0x01cf
            if (r4 != 0) goto L_0x019c
            goto L_0x01cf
        L_0x019c:
            android.widget.TextView r3 = r0.mTxtNoteBubble
            r8 = 99
            if (r4 <= r8) goto L_0x01a5
            java.lang.String r8 = "99+"
            goto L_0x01a9
        L_0x01a5:
            java.lang.String r8 = java.lang.String.valueOf(r4)
        L_0x01a9:
            r3.setText(r8)
            android.widget.TextView r3 = r0.mTxtNoteBubble
            r3.setVisibility(r9)
            android.content.res.Resources r3 = r16.getResources()
            int r8 = p021us.zoom.videomeetings.C4558R.plurals.zm_msg_notification_unread_num_8295
            r10 = 2
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.String r11 = ""
            r10[r9] = r11
            java.lang.Integer r11 = java.lang.Integer.valueOf(r4)
            r12 = 1
            r10[r12] = r11
            java.lang.String r3 = r3.getQuantityString(r8, r4, r10)
            android.widget.TextView r4 = r0.mTxtNoteBubble
            r4.setContentDescription(r3)
            goto L_0x01d4
        L_0x01cf:
            android.widget.TextView r3 = r0.mTxtNoteBubble
            r3.setVisibility(r15)
        L_0x01d4:
            android.widget.TextView r3 = r0.mTxtTime
            if (r3 == 0) goto L_0x01fd
            boolean r3 = r3.isInEditMode()
            if (r3 != 0) goto L_0x01fd
            long r3 = r17.getTimeStamp()
            r10 = 0
            int r8 = (r3 > r10 ? 1 : (r3 == r10 ? 0 : -1))
            if (r8 <= 0) goto L_0x01f6
            android.widget.TextView r8 = r0.mTxtTime
            android.content.Context r10 = r16.getContext()
            java.lang.String r3 = r1.formatTime(r10, r3)
            r8.setText(r3)
            goto L_0x01fd
        L_0x01f6:
            android.widget.TextView r3 = r0.mTxtTime
            java.lang.String r4 = ""
            r3.setText(r4)
        L_0x01fd:
            android.widget.TextView r3 = r0.mTxtAt
            if (r3 == 0) goto L_0x021f
            java.lang.String r3 = r17.getAt()
            android.widget.TextView r4 = r0.mTxtAt
            boolean r8 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            if (r8 == 0) goto L_0x0210
            r8 = 8
            goto L_0x0211
        L_0x0210:
            r8 = 0
        L_0x0211:
            r4.setVisibility(r8)
            boolean r4 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r3)
            if (r4 != 0) goto L_0x021f
            android.widget.TextView r4 = r0.mTxtAt
            r4.setText(r3)
        L_0x021f:
            android.widget.ImageView r3 = r0.mImgErrorMessage
            if (r7 == 0) goto L_0x0225
            r4 = 0
            goto L_0x0227
        L_0x0225:
            r4 = 8
        L_0x0227:
            r3.setVisibility(r4)
            if (r5 != 0) goto L_0x027a
            com.zipow.videobox.view.IMAddrBookItem r3 = r17.getFromContact()
            if (r3 != 0) goto L_0x0233
            return
        L_0x0233:
            java.lang.String r4 = r3.getJid()
            com.zipow.videobox.ptapp.mm.ZoomBuddy r2 = r2.getBuddyWithJID(r4)
            if (r2 != 0) goto L_0x023e
            return
        L_0x023e:
            com.zipow.videobox.view.IMAddrBookItem r2 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r2)
            if (r6 == 0) goto L_0x024a
            com.zipow.videobox.view.PresenceStateView r2 = r0.mImgPresence
            r2.setVisibility(r15)
            goto L_0x0259
        L_0x024a:
            com.zipow.videobox.view.PresenceStateView r4 = r0.mImgPresence
            r4.setVisibility(r9)
            com.zipow.videobox.view.PresenceStateView r4 = r0.mImgPresence
            r4.setState(r2)
            com.zipow.videobox.view.PresenceStateView r2 = r0.mImgPresence
            r2.setmTxtDeviceTypeGone()
        L_0x0259:
            android.widget.ImageView r2 = r0.mImgE2EFlag
            r2.setVisibility(r15)
            com.zipow.videobox.util.AlertWhenAvailableHelper r2 = com.zipow.videobox.util.AlertWhenAvailableHelper.getInstance()
            java.lang.String r3 = r3.getJid()
            boolean r2 = r2.isInAlertQueen(r3)
            android.widget.ImageView r3 = r0.mImgBell
            boolean r1 = r17.isRoom()
            r4 = 1
            r1 = r1 ^ r4
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0276
            r15 = 0
        L_0x0276:
            r3.setVisibility(r15)
            goto L_0x0290
        L_0x027a:
            android.widget.ImageView r2 = r0.mImgBell
            r2.setVisibility(r15)
            com.zipow.videobox.view.PresenceStateView r2 = r0.mImgPresence
            r2.setVisibility(r15)
            android.widget.ImageView r2 = r0.mImgE2EFlag
            boolean r1 = r17.isE2E()
            if (r1 == 0) goto L_0x028d
            r15 = 0
        L_0x028d:
            r2.setVisibility(r15)
        L_0x0290:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMChatsListItemView.bindViews(com.zipow.videobox.view.mm.MMChatsListItem):void");
    }
}
