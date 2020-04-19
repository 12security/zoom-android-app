package com.zipow.videobox.view.p014mm.contentfile;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.PresenceStateView;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.contentfile.ContentFileChatListHolder */
public class ContentFileChatListHolder extends ViewHolder {
    private AvatarView avatarView;
    private ImageView imgBell;
    private ImageView imgE2EFlag;
    private PresenceStateView imgPresence;
    private Context mContext;
    private TextView txtMessage;
    private TextView txtNoteBubble;
    private TextView txtTime;
    private ZMEllipsisTextView txtTitle;
    private View unreadBubble;

    public ContentFileChatListHolder(@NonNull View view, Context context) {
        super(view);
        this.mContext = context;
        this.avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
        this.txtTitle = (ZMEllipsisTextView) view.findViewById(C4558R.C4560id.txtTitle);
        this.txtMessage = (TextView) view.findViewById(C4558R.C4560id.txtMessage);
        this.txtTime = (TextView) view.findViewById(C4558R.C4560id.txtTime);
        this.txtNoteBubble = (TextView) view.findViewById(C4558R.C4560id.txtNoteBubble);
        this.imgPresence = (PresenceStateView) view.findViewById(C4558R.C4560id.imgPresence);
        this.imgE2EFlag = (ImageView) view.findViewById(C4558R.C4560id.imgE2EFlag);
        this.imgBell = (ImageView) view.findViewById(C4558R.C4560id.imgBell);
        this.unreadBubble = view.findViewById(C4558R.C4560id.unreadBubble);
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00af  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindView(@androidx.annotation.NonNull com.zipow.videobox.view.p014mm.MMChatsListItem r12) {
        /*
            r11 = this;
            java.lang.String r0 = r12.getSessionId()
            boolean r0 = com.zipow.videobox.util.UIMgr.isMyNotes(r0)
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r1 = r1.getZoomMessenger()
            if (r1 != 0) goto L_0x0013
            return
        L_0x0013:
            java.lang.String r2 = r1.getContactRequestsSessionID()
            com.zipow.videobox.view.AvatarView r3 = r11.avatarView
            if (r3 == 0) goto L_0x005d
            java.lang.String r3 = r12.getSessionId()
            boolean r3 = p021us.zoom.androidlib.util.StringUtil.isSameString(r2, r3)
            r4 = 0
            if (r3 == 0) goto L_0x0032
            com.zipow.videobox.view.AvatarView$ParamsBuilder r3 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r3.<init>()
            int r5 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_im_contact_request
            com.zipow.videobox.view.AvatarView$ParamsBuilder r4 = r3.setResource(r5, r4)
            goto L_0x0058
        L_0x0032:
            boolean r3 = r12.isGroup()
            if (r3 != 0) goto L_0x0047
            com.zipow.videobox.view.IMAddrBookItem r3 = r12.getFromContact()
            if (r3 == 0) goto L_0x0047
            com.zipow.videobox.view.IMAddrBookItem r3 = r12.getFromContact()
            com.zipow.videobox.view.AvatarView$ParamsBuilder r4 = r3.getAvatarParamsBuilder()
            goto L_0x0058
        L_0x0047:
            boolean r3 = r12.isGroup()
            if (r3 == 0) goto L_0x0058
            com.zipow.videobox.view.AvatarView$ParamsBuilder r3 = new com.zipow.videobox.view.AvatarView$ParamsBuilder
            r3.<init>()
            int r5 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_ic_avatar_group
            com.zipow.videobox.view.AvatarView$ParamsBuilder r4 = r3.setResource(r5, r4)
        L_0x0058:
            com.zipow.videobox.view.AvatarView r3 = r11.avatarView
            r3.show(r4)
        L_0x005d:
            us.zoom.androidlib.widget.ZMEllipsisTextView r3 = r11.txtTitle
            r4 = 2
            r5 = 1
            r6 = 0
            if (r3 == 0) goto L_0x00f5
            java.lang.String r3 = r12.getTitle()
            if (r3 == 0) goto L_0x00f5
            if (r0 == 0) goto L_0x0089
            us.zoom.androidlib.widget.ZMEllipsisTextView r0 = r11.txtTitle
            android.text.TextUtils$TruncateAt r3 = android.text.TextUtils.TruncateAt.MIDDLE
            r0.setEllipsize(r3)
            us.zoom.androidlib.widget.ZMEllipsisTextView r0 = r11.txtTitle
            android.content.Context r3 = r11.mContext
            int r7 = p021us.zoom.videomeetings.C4558R.string.zm_mm_msg_my_notes_65147
            java.lang.Object[] r8 = new java.lang.Object[r5]
            java.lang.String r9 = r12.getTitle()
            r8[r6] = r9
            java.lang.String r3 = r3.getString(r7, r8)
            r0.setText(r3)
            goto L_0x00f5
        L_0x0089:
            boolean r0 = r12.isGroup()
            if (r0 != 0) goto L_0x00c2
            com.zipow.videobox.view.IMAddrBookItem r0 = r12.getFromContact()
            if (r0 == 0) goto L_0x00a7
            int r3 = r0.getAccountStatus()
            if (r3 != r5) goto L_0x009e
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_deactivated_62074
            goto L_0x00a8
        L_0x009e:
            int r0 = r0.getAccountStatus()
            if (r0 != r4) goto L_0x00a7
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_terminated_62074
            goto L_0x00a8
        L_0x00a7:
            r0 = 0
        L_0x00a8:
            if (r0 != 0) goto L_0x00af
            java.lang.String r3 = r12.getTitle()
            goto L_0x00e7
        L_0x00af:
            android.content.Context r3 = r11.mContext
            android.content.res.Resources r3 = r3.getResources()
            java.lang.Object[] r7 = new java.lang.Object[r5]
            java.lang.String r8 = r12.getTitle()
            r7[r6] = r8
            java.lang.String r3 = r3.getString(r0, r7)
            goto L_0x00e7
        L_0x00c2:
            java.lang.String r0 = r12.getSessionId()
            boolean r0 = android.text.TextUtils.equals(r2, r0)
            if (r0 == 0) goto L_0x00d2
            java.lang.String r3 = r12.getTitle()
            r0 = 0
            goto L_0x00e7
        L_0x00d2:
            android.content.Context r0 = r11.mContext
            android.content.res.Resources r0 = r0.getResources()
            int r3 = p021us.zoom.videomeetings.C4558R.string.zm_accessibility_group_pre_77383
            java.lang.Object[] r7 = new java.lang.Object[r5]
            java.lang.String r8 = r12.getTitle()
            r7[r6] = r8
            java.lang.String r3 = r0.getString(r3, r7)
            r0 = 0
        L_0x00e7:
            us.zoom.androidlib.widget.ZMEllipsisTextView r7 = r11.txtTitle
            java.lang.String r8 = r12.getTitle()
            r7.setEllipsisText(r8, r0)
            us.zoom.androidlib.widget.ZMEllipsisTextView r0 = r11.txtTitle
            r0.setContentDescription(r3)
        L_0x00f5:
            android.widget.TextView r0 = r11.txtMessage
            if (r0 == 0) goto L_0x011e
            r0.setVisibility(r6)
            boolean r0 = r12.hasDraftMessage()
            if (r0 == 0) goto L_0x010c
            android.widget.TextView r0 = r11.txtMessage
            java.lang.CharSequence r3 = r12.getDraftMessage()
            r0.setText(r3)
            goto L_0x011e
        L_0x010c:
            android.widget.TextView r0 = r11.txtMessage
            java.lang.CharSequence r3 = r12.getLatestMessage()
            if (r3 == 0) goto L_0x0119
            java.lang.CharSequence r3 = r12.getLatestMessage()
            goto L_0x011b
        L_0x0119:
            java.lang.String r3 = ""
        L_0x011b:
            r0.setText(r3)
        L_0x011e:
            android.view.View r0 = r11.unreadBubble
            r3 = 8
            if (r0 == 0) goto L_0x0162
            int r0 = r12.getUnreadMessageCountBySetting()
            if (r0 != 0) goto L_0x015d
            int r0 = r12.getUnreadMessageCount()
            if (r0 <= 0) goto L_0x015d
            int r0 = r12.getMarkUnreadMessageCount()
            if (r0 > 0) goto L_0x015d
            boolean r0 = r12.isGroup()
            if (r0 == 0) goto L_0x015d
            java.lang.String r0 = r12.getSessionId()
            boolean r0 = android.text.TextUtils.equals(r2, r0)
            if (r0 != 0) goto L_0x015d
            android.view.View r0 = r11.unreadBubble
            android.content.Context r7 = r11.mContext
            android.content.res.Resources r7 = r7.getResources()
            int r8 = p021us.zoom.videomeetings.C4558R.string.zm_mm_lbl_new_message_14491
            java.lang.String r7 = r7.getString(r8)
            r0.setContentDescription(r7)
            android.view.View r0 = r11.unreadBubble
            r0.setVisibility(r6)
            goto L_0x0162
        L_0x015d:
            android.view.View r0 = r11.unreadBubble
            r0.setVisibility(r3)
        L_0x0162:
            android.widget.TextView r0 = r11.txtNoteBubble
            if (r0 == 0) goto L_0x01c2
            boolean r0 = r12.isGroup()
            if (r0 == 0) goto L_0x0171
            int r0 = r12.getUnreadMessageCountBySetting()
            goto L_0x0175
        L_0x0171:
            int r0 = r12.getUnreadMessageCount()
        L_0x0175:
            int r7 = r12.getMarkUnreadMessageCount()
            int r0 = r0 + r7
            java.lang.String r7 = r12.getSessionId()
            boolean r2 = android.text.TextUtils.equals(r2, r7)
            if (r2 == 0) goto L_0x0188
            int r0 = r12.getUnreadMessageCount()
        L_0x0188:
            if (r0 != 0) goto L_0x0190
            android.widget.TextView r0 = r11.txtNoteBubble
            r0.setVisibility(r3)
            goto L_0x01c2
        L_0x0190:
            android.widget.TextView r2 = r11.txtNoteBubble
            r7 = 99
            if (r0 <= r7) goto L_0x0199
            java.lang.String r7 = "99+"
            goto L_0x019d
        L_0x0199:
            java.lang.String r7 = java.lang.String.valueOf(r0)
        L_0x019d:
            r2.setText(r7)
            android.widget.TextView r2 = r11.txtNoteBubble
            r2.setVisibility(r6)
            android.content.Context r2 = r11.mContext
            android.content.res.Resources r2 = r2.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.plurals.zm_msg_notification_unread_num_8295
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.String r8 = ""
            r4[r6] = r8
            java.lang.Integer r8 = java.lang.Integer.valueOf(r0)
            r4[r5] = r8
            java.lang.String r0 = r2.getQuantityString(r7, r0, r4)
            android.widget.TextView r2 = r11.txtNoteBubble
            r2.setContentDescription(r0)
        L_0x01c2:
            android.widget.TextView r0 = r11.txtTime
            if (r0 == 0) goto L_0x01ed
            boolean r0 = r0.isInEditMode()
            if (r0 != 0) goto L_0x01ed
            long r7 = r12.getTimeStamp()
            r9 = 0
            int r0 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r0 <= 0) goto L_0x01e6
            android.widget.TextView r0 = r11.txtTime
            android.content.Context r2 = r11.mContext
            long r7 = r12.getTimeStamp()
            java.lang.String r2 = r12.formatTime(r2, r7)
            r0.setText(r2)
            goto L_0x01ed
        L_0x01e6:
            android.widget.TextView r0 = r11.txtTime
            java.lang.String r2 = ""
            r0.setText(r2)
        L_0x01ed:
            boolean r0 = r12.isGroup()
            if (r0 != 0) goto L_0x0228
            com.zipow.videobox.view.IMAddrBookItem r0 = r12.getFromContact()
            if (r0 != 0) goto L_0x01fa
            return
        L_0x01fa:
            java.lang.String r2 = r0.getJid()
            com.zipow.videobox.ptapp.mm.ZoomBuddy r1 = r1.getBuddyWithJID(r2)
            if (r1 != 0) goto L_0x0205
            return
        L_0x0205:
            com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r1)
            android.widget.ImageView r1 = r11.imgE2EFlag
            r1.setVisibility(r3)
            com.zipow.videobox.util.AlertWhenAvailableHelper r1 = com.zipow.videobox.util.AlertWhenAvailableHelper.getInstance()
            java.lang.String r0 = r0.getJid()
            boolean r0 = r1.isInAlertQueen(r0)
            android.widget.ImageView r1 = r11.imgBell
            boolean r2 = r12.isRoom()
            r2 = r2 ^ r5
            r0 = r0 & r2
            if (r0 == 0) goto L_0x0224
            r3 = 0
        L_0x0224:
            r1.setVisibility(r3)
            goto L_0x0239
        L_0x0228:
            android.widget.ImageView r0 = r11.imgBell
            r0.setVisibility(r3)
            android.widget.ImageView r0 = r11.imgE2EFlag
            boolean r1 = r12.isE2E()
            if (r1 == 0) goto L_0x0236
            r3 = 0
        L_0x0236:
            r0.setVisibility(r3)
        L_0x0239:
            us.zoom.androidlib.widget.ZMEllipsisTextView r0 = r11.txtTitle
            if (r0 == 0) goto L_0x024a
            boolean r12 = r12.isNotifyOff()
            if (r12 == 0) goto L_0x0246
            int r12 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_notifications_off
            goto L_0x0247
        L_0x0246:
            r12 = 0
        L_0x0247:
            r0.setCompoundDrawablesWithIntrinsicBounds(r6, r6, r12, r6)
        L_0x024a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.contentfile.ContentFileChatListHolder.bindView(com.zipow.videobox.view.mm.MMChatsListItem):void");
    }
}
