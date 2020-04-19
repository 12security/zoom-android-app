package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.google.android.material.card.MaterialCardView;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MoreReplyView extends MaterialCardView {
    private static final int PRIORITY_AT_ALL = 2;
    private static final int PRIORITY_AT_ME = 3;
    private static final int PRIORITY_MARK_UNREAD = 4;
    private static final int PRIORITY_NEW_REPLIES = 1;
    private static final int PRIORITY_NONE = 0;
    private static final int PRIORITY_UNSENT_REPLY = 5;
    private ImageView imgErrorMessage;
    private MMMessageItem mMessageItem;
    private TextView moreReply;
    private int priority = 0;
    private View redBubble;
    private ImageView rightArrow;
    private TextView txtAtAll;
    private TextView txtAtMe;
    private TextView txtDraft;
    private TextView txtErrorMsg;
    private TextView txtMarkUnread;
    private TextView txtMarkUnreadMsg;
    private TextView txtNewReply;

    public MoreReplyView(Context context) {
        super(context);
        init();
    }

    public MoreReplyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MoreReplyView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        addView(View.inflate(getContext(), C4558R.layout.zm_more_reply_view, null), new LayoutParams(-2, -1));
        this.moreReply = (TextView) findViewById(C4558R.C4560id.moreReply);
        this.txtMarkUnread = (TextView) findViewById(C4558R.C4560id.txtMarkUnread);
        this.txtMarkUnreadMsg = (TextView) findViewById(C4558R.C4560id.txtMarkUnreadMsg);
        this.redBubble = findViewById(C4558R.C4560id.redBubble);
        this.txtAtMe = (TextView) findViewById(C4558R.C4560id.txtAtMe);
        this.txtAtAll = (TextView) findViewById(C4558R.C4560id.txtAtAll);
        this.txtDraft = (TextView) findViewById(C4558R.C4560id.txtDraft);
        this.txtErrorMsg = (TextView) findViewById(C4558R.C4560id.txtErrorMsg);
        this.imgErrorMessage = (ImageView) findViewById(C4558R.C4560id.imgErrorMessage);
        this.rightArrow = (ImageView) findViewById(C4558R.C4560id.rightArrow);
        this.txtNewReply = (TextView) findViewById(C4558R.C4560id.txtNewReply);
        setStrokeWidth(UIUtil.dip2px(getContext(), 1.0f));
        setStrokeColor(ContextCompat.getColor(getContext(), C4558R.color.zm_transparent));
        setRadius((float) UIUtil.dip2px(getContext(), 12.0f));
        setClickable(true);
        setCardElevation(0.0f);
    }

    public void setData(MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            this.mMessageItem = mMMessageItem;
            updateViews();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0133  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0135  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0140  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0142  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x014d  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x014f  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x015c  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x017b  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0186  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0188  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0195  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x01a0  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01a2  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01ad  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01b7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateViews() {
        /*
            r13 = this;
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            r1 = 8
            if (r0 == 0) goto L_0x024e
            int r0 = r0.hasCommentsOdds
            r2 = 0
            r4 = 1
            if (r0 == r4) goto L_0x0033
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            long r5 = r0.commentsCount
            int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0033
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            long r5 = r0.unreadCommentCount
            int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0033
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            java.util.List r0 = r0.getComments()
            boolean r0 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r0)
            if (r0 == 0) goto L_0x0033
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            java.lang.String r0 = r0.draftReply
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x024e
        L_0x0033:
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            boolean r0 = r0.isComment
            if (r0 == 0) goto L_0x003b
            goto L_0x024e
        L_0x003b:
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            long r5 = r0.commentsCount
            r0 = 0
            int r5 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r5 != 0) goto L_0x004c
            android.widget.TextView r5 = r13.moreReply
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_reply_nosure_count_88133
            r5.setText(r6)
            goto L_0x006d
        L_0x004c:
            android.widget.TextView r5 = r13.moreReply
            android.content.res.Resources r6 = r13.getResources()
            int r7 = p021us.zoom.videomeetings.C4558R.plurals.zm_lbl_comment_more_reply_88133
            com.zipow.videobox.view.mm.MMMessageItem r8 = r13.mMessageItem
            long r8 = r8.commentsCount
            int r8 = (int) r8
            java.lang.Object[] r9 = new java.lang.Object[r4]
            com.zipow.videobox.view.mm.MMMessageItem r10 = r13.mMessageItem
            long r10 = r10.commentsCount
            int r10 = (int) r10
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            r9[r0] = r10
            java.lang.String r6 = r6.getQuantityString(r7, r8, r9)
            r5.setText(r6)
        L_0x006d:
            com.zipow.videobox.view.mm.MMMessageItem r5 = r13.mMessageItem
            int r5 = r5.atMeCommentCount
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            int r6 = r6.atAllCommentCount
            int r5 = r5 + r6
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            boolean r6 = r6.hasFailedMessage
            r7 = 2
            r8 = 4
            r9 = 5
            r10 = 3
            if (r6 == 0) goto L_0x0084
            r13.priority = r9
            goto L_0x00fe
        L_0x0084:
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            int r6 = r6.markUnreadCommentCount
            if (r6 <= 0) goto L_0x00b4
            r13.priority = r8
            android.widget.TextView r2 = r13.txtMarkUnread
            if (r2 == 0) goto L_0x00fe
            com.zipow.videobox.view.mm.MMMessageItem r3 = r13.mMessageItem
            int r3 = r3.markUnreadCommentCount
            r6 = 99
            if (r3 <= r6) goto L_0x009b
            java.lang.String r3 = "99+"
            goto L_0x00b0
        L_0x009b:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            int r6 = r6.markUnreadCommentCount
            r3.append(r6)
            java.lang.String r6 = ""
            r3.append(r6)
            java.lang.String r3 = r3.toString()
        L_0x00b0:
            r2.setText(r3)
            goto L_0x00fe
        L_0x00b4:
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            int r6 = r6.atMeCommentCount
            if (r6 <= 0) goto L_0x00d4
            r13.priority = r10
            android.widget.TextView r2 = r13.txtAtMe
            android.content.res.Resources r3 = r13.getResources()
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_comment_at_me_88133
            java.lang.Object[] r11 = new java.lang.Object[r4]
            java.lang.Integer r12 = java.lang.Integer.valueOf(r5)
            r11[r0] = r12
            java.lang.String r3 = r3.getString(r6, r11)
            r2.setText(r3)
            goto L_0x00fe
        L_0x00d4:
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            int r6 = r6.atAllCommentCount
            if (r6 <= 0) goto L_0x00f4
            r13.priority = r7
            android.widget.TextView r2 = r13.txtAtAll
            android.content.res.Resources r3 = r13.getResources()
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_lbl_comment_at_all_88133
            java.lang.Object[] r11 = new java.lang.Object[r4]
            java.lang.Integer r12 = java.lang.Integer.valueOf(r5)
            r11[r0] = r12
            java.lang.String r3 = r3.getString(r6, r11)
            r2.setText(r3)
            goto L_0x00fe
        L_0x00f4:
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            long r11 = r6.unreadCommentCount
            int r2 = (r11 > r2 ? 1 : (r11 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0100
            r13.priority = r4
        L_0x00fe:
            r2 = 0
            goto L_0x0122
        L_0x0100:
            r13.priority = r0
            com.google.gson.Gson r2 = new com.google.gson.Gson
            r2.<init>()
            com.zipow.videobox.view.mm.MMMessageItem r3 = r13.mMessageItem
            java.lang.String r3 = r3.draftReply
            java.lang.Class<com.zipow.videobox.util.TextCommandHelper$DraftBean> r6 = com.zipow.videobox.util.TextCommandHelper.DraftBean.class
            java.lang.Object r2 = r2.fromJson(r3, r6)
            com.zipow.videobox.util.TextCommandHelper$DraftBean r2 = (com.zipow.videobox.util.TextCommandHelper.DraftBean) r2
            if (r2 == 0) goto L_0x0121
            java.lang.String r2 = r2.getLabel()
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x0121
            r2 = 1
            goto L_0x0122
        L_0x0121:
            r2 = 0
        L_0x0122:
            android.widget.TextView r3 = r13.txtDraft
            if (r2 == 0) goto L_0x0128
            r2 = 0
            goto L_0x012a
        L_0x0128:
            r2 = 8
        L_0x012a:
            r3.setVisibility(r2)
            android.widget.ImageView r2 = r13.imgErrorMessage
            int r3 = r13.priority
            if (r3 != r9) goto L_0x0135
            r3 = 0
            goto L_0x0137
        L_0x0135:
            r3 = 8
        L_0x0137:
            r2.setVisibility(r3)
            android.widget.TextView r2 = r13.txtErrorMsg
            int r3 = r13.priority
            if (r3 != r9) goto L_0x0142
            r3 = 0
            goto L_0x0144
        L_0x0142:
            r3 = 8
        L_0x0144:
            r2.setVisibility(r3)
            android.widget.TextView r2 = r13.txtMarkUnread
            int r3 = r13.priority
            if (r3 != r8) goto L_0x014f
            r3 = 0
            goto L_0x0151
        L_0x014f:
            r3 = 8
        L_0x0151:
            r2.setVisibility(r3)
            android.widget.TextView r2 = r13.txtMarkUnreadMsg
            int r3 = r13.priority
            if (r3 != r8) goto L_0x015c
            r3 = 0
            goto L_0x015e
        L_0x015c:
            r3 = 8
        L_0x015e:
            r2.setVisibility(r3)
            android.view.View r2 = r13.redBubble
            int r3 = r13.priority
            if (r3 == r10) goto L_0x016f
            if (r3 == r7) goto L_0x016f
            if (r3 != r4) goto L_0x016c
            goto L_0x016f
        L_0x016c:
            r3 = 8
            goto L_0x0170
        L_0x016f:
            r3 = 0
        L_0x0170:
            r2.setVisibility(r3)
            android.widget.TextView r2 = r13.txtAtMe
            int r3 = r13.priority
            if (r3 != r10) goto L_0x017b
            r3 = 0
            goto L_0x017d
        L_0x017b:
            r3 = 8
        L_0x017d:
            r2.setVisibility(r3)
            android.widget.TextView r2 = r13.txtAtAll
            int r3 = r13.priority
            if (r3 != r7) goto L_0x0188
            r3 = 0
            goto L_0x018a
        L_0x0188:
            r3 = 8
        L_0x018a:
            r2.setVisibility(r3)
            android.widget.TextView r2 = r13.txtNewReply
            int r3 = r13.priority
            if (r3 != r4) goto L_0x0195
            r3 = 0
            goto L_0x0197
        L_0x0195:
            r3 = 8
        L_0x0197:
            r2.setVisibility(r3)
            android.widget.TextView r2 = r13.moreReply
            int r3 = r13.priority
            if (r3 != 0) goto L_0x01a2
            r3 = 0
            goto L_0x01a4
        L_0x01a2:
            r3 = 8
        L_0x01a4:
            r2.setVisibility(r3)
            android.widget.ImageView r2 = r13.rightArrow
            int r3 = r13.priority
            if (r3 != 0) goto L_0x01ae
            r1 = 0
        L_0x01ae:
            r2.setVisibility(r1)
            android.content.res.Resources r1 = r13.getResources()
            if (r1 == 0) goto L_0x024d
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            int r3 = p021us.zoom.videomeetings.C4558R.plurals.zm_accessibility_view_reply_88133
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            long r6 = r6.commentsCount
            int r6 = (int) r6
            java.lang.Object[] r7 = new java.lang.Object[r4]
            com.zipow.videobox.view.mm.MMMessageItem r8 = r13.mMessageItem
            long r8 = r8.commentsCount
            java.lang.Long r8 = java.lang.Long.valueOf(r8)
            r7[r0] = r8
            java.lang.String r3 = r1.getQuantityString(r3, r6, r7)
            r2.append(r3)
            int r3 = p021us.zoom.videomeetings.C4558R.plurals.zm_accessibility_view_reply_new_msg_88133
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            int r6 = r6.markUnreadCommentCount
            java.lang.Object[] r7 = new java.lang.Object[r4]
            com.zipow.videobox.view.mm.MMMessageItem r8 = r13.mMessageItem
            int r8 = r8.markUnreadCommentCount
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r7[r0] = r8
            java.lang.String r3 = r1.getQuantityString(r3, r6, r7)
            r2.append(r3)
            int r3 = r13.priority
            if (r3 != r10) goto L_0x0205
            int r3 = p021us.zoom.videomeetings.C4558R.plurals.zm_accessibility_view_reply_new_me_88133
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.Integer r6 = java.lang.Integer.valueOf(r5)
            r4[r0] = r6
            java.lang.String r0 = r1.getQuantityString(r3, r5, r4)
            r2.append(r0)
            goto L_0x0224
        L_0x0205:
            com.zipow.videobox.view.mm.MMMessageItem r3 = r13.mMessageItem
            int r3 = r3.atAllCommentCount
            if (r3 <= 0) goto L_0x0224
            int r3 = p021us.zoom.videomeetings.C4558R.plurals.zm_accessibility_view_reply_new_all_88133
            com.zipow.videobox.view.mm.MMMessageItem r5 = r13.mMessageItem
            int r5 = r5.atAllCommentCount
            java.lang.Object[] r4 = new java.lang.Object[r4]
            com.zipow.videobox.view.mm.MMMessageItem r6 = r13.mMessageItem
            int r6 = r6.atAllCommentCount
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)
            r4[r0] = r6
            java.lang.String r0 = r1.getQuantityString(r3, r5, r4)
            r2.append(r0)
        L_0x0224:
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            java.lang.String r0 = r0.draftReply
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0237
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_accessibility_view_reply_draf_88133
            java.lang.String r0 = r1.getString(r0)
            r2.append(r0)
        L_0x0237:
            com.zipow.videobox.view.mm.MMMessageItem r0 = r13.mMessageItem
            boolean r0 = r0.hasFailedMessage
            if (r0 == 0) goto L_0x0246
            int r0 = p021us.zoom.videomeetings.C4558R.string.zm_accessibility_view_reply_pending_88133
            java.lang.String r0 = r1.getString(r0)
            r2.append(r0)
        L_0x0246:
            java.lang.String r0 = r2.toString()
            r13.setContentDescription(r0)
        L_0x024d:
            return
        L_0x024e:
            r13.setVisibility(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.MoreReplyView.updateViews():void");
    }
}
