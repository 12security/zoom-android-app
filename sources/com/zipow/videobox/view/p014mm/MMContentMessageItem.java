package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.PresenceStateView;
import java.io.Serializable;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMEllipsisTextView;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentMessageItem */
public class MMContentMessageItem {
    private static final String ELLIPSIS_NORMAL = "…";
    @Nullable
    private IMAddrBookItem addrbookItem;
    private CharSequence content;
    private boolean isComment;
    private boolean isGroup;
    private String keyWord;
    @Nullable
    private String msgId;
    @Nullable
    private String screenName;
    private long sendTime;
    @Nullable
    private String senderJid;
    @Nullable
    private String senderName;
    @Nullable
    private String sessionId;
    @Nullable
    private String thrId;
    private long thrSvr;

    /* renamed from: com.zipow.videobox.view.mm.MMContentMessageItem$HighlightPosition */
    public static class HighlightPosition {
        int end;
        int start;
    }

    /* renamed from: com.zipow.videobox.view.mm.MMContentMessageItem$MMContentMessageAnchorInfo */
    public static class MMContentMessageAnchorInfo implements Serializable {
        public static final int TYPE_NORMAL = 0;
        public static final int TYPE_SERVER = 1;
        private static final long serialVersionUID = 1;
        private boolean isComment;
        private boolean isFromMarkUnread;
        public int mType = 0;
        private String msgGuid;
        private long sendTime;
        private long serverTime;
        private String sessionId;
        private String thrId;
        private long thrSvr;

        public int getmType() {
            return this.mType;
        }

        public void setmType(int i) {
            this.mType = i;
        }

        public long getServerTime() {
            return this.serverTime;
        }

        public void setServerTime(long j) {
            this.serverTime = j;
        }

        public String getMsgGuid() {
            return this.msgGuid;
        }

        public void setMsgGuid(String str) {
            this.msgGuid = str;
        }

        public String getSessionId() {
            return this.sessionId;
        }

        public void setSessionId(String str) {
            this.sessionId = str;
        }

        public long getSendTime() {
            return this.sendTime;
        }

        public void setSendTime(long j) {
            this.sendTime = j;
        }

        public boolean isComment() {
            return this.isComment;
        }

        public void setComment(boolean z) {
            this.isComment = z;
        }

        public String getThrId() {
            return this.thrId;
        }

        public void setThrId(String str) {
            this.thrId = str;
        }

        public long getThrSvr() {
            return this.thrSvr;
        }

        public void setThrSvr(long j) {
            this.thrSvr = j;
        }

        public boolean isFromMarkUnread() {
            return this.isFromMarkUnread;
        }

        public void setFromMarkUnread(boolean z) {
            this.isFromMarkUnread = z;
        }
    }

    public long getThrSvr() {
        return this.thrSvr;
    }

    public void setThrSvr(long j) {
        this.thrSvr = j;
    }

    @Nullable
    public static MMContentMessageItem initWithZoomMessage(@NonNull Context context, @NonNull MMContentMessageItem mMContentMessageItem, @Nullable ZoomMessage zoomMessage) {
        ZoomBuddy zoomBuddy;
        if (zoomMessage == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        MMContentMessageItem mMContentMessageItem2 = new MMContentMessageItem();
        mMContentMessageItem2.msgId = zoomMessage.getMessageID();
        mMContentMessageItem2.sessionId = mMContentMessageItem.sessionId;
        mMContentMessageItem2.senderJid = zoomMessage.getSenderID();
        mMContentMessageItem2.senderName = zoomMessage.getSenderName();
        mMContentMessageItem2.sendTime = zoomMessage.getStamp();
        mMContentMessageItem2.screenName = mMContentMessageItem2.senderName;
        mMContentMessageItem2.content = mMContentMessageItem.content;
        if (!StringUtil.isSameString(mMContentMessageItem2.sessionId, mMContentMessageItem2.senderJid)) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(mMContentMessageItem2.sessionId);
            if (sessionById != null && sessionById.isGroup()) {
                mMContentMessageItem2.isGroup = true;
                ZoomGroup sessionGroup = sessionById.getSessionGroup();
                if (sessionGroup == null) {
                    return null;
                }
                mMContentMessageItem2.screenName = sessionGroup.getGroupDisplayName(context);
            } else if (zoomMessenger.getBuddyWithJID(mMContentMessageItem2.sessionId) == null) {
                return null;
            } else {
                mMContentMessageItem2.isGroup = false;
            }
        }
        if (!mMContentMessageItem2.isGroup) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself == null) {
                return null;
            }
            String jid = myself.getJid();
            if (!StringUtil.isSameString(jid, mMContentMessageItem2.sessionId)) {
                zoomBuddy = zoomMessenger.getBuddyWithJID(mMContentMessageItem2.sessionId);
            } else if (StringUtil.isSameString(jid, mMContentMessageItem2.getSenderJid())) {
                return null;
            } else {
                zoomBuddy = zoomMessenger.getBuddyWithJID(mMContentMessageItem2.getSenderJid());
            }
            if (zoomBuddy != null) {
                mMContentMessageItem2.addrbookItem = IMAddrBookItem.fromZoomBuddy(zoomBuddy);
            }
        }
        return mMContentMessageItem2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:68:0x01a9  */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.view.p014mm.MMContentMessageItem initWithFileFilterSearchResult(@androidx.annotation.Nullable com.zipow.videobox.ptapp.IMProtos.MessageSearchResult r11, @androidx.annotation.Nullable android.content.Context r12) {
        /*
            r0 = 0
            if (r12 == 0) goto L_0x0201
            if (r11 == 0) goto L_0x0201
            java.lang.String r1 = r11.getMsgId()
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r1 == 0) goto L_0x0011
            goto L_0x0201
        L_0x0011:
            com.zipow.videobox.view.mm.MMContentMessageItem r1 = new com.zipow.videobox.view.mm.MMContentMessageItem
            r1.<init>()
            java.lang.String r2 = r11.getMsgId()
            r1.msgId = r2
            java.lang.String r2 = r11.getSessionId()
            r1.sessionId = r2
            java.lang.String r2 = r11.getSenderJid()
            r1.senderJid = r2
            java.lang.String r2 = r11.getSenderName()
            r1.senderName = r2
            long r2 = r11.getSendTime()
            r1.sendTime = r2
            java.lang.String r2 = r11.getKeyWord()
            r1.keyWord = r2
            java.lang.String r2 = r11.getContent()
            r1.content = r2
            java.lang.String r2 = r1.senderName
            r1.screenName = r2
            boolean r2 = r11.getIsComment()
            r1.isComment = r2
            java.lang.String r2 = r11.getThrId()
            r1.thrId = r2
            long r2 = r11.getThrSvrT()
            r1.thrSvr = r2
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r2 = r2.getZoomMessenger()
            if (r2 != 0) goto L_0x0061
            return r0
        L_0x0061:
            java.lang.String r3 = r1.sessionId
            java.lang.String r4 = r1.senderJid
            boolean r3 = p021us.zoom.androidlib.util.StringUtil.isSameString(r3, r4)
            r4 = 1
            r5 = 0
            if (r3 != 0) goto L_0x0097
            java.lang.String r3 = r1.sessionId
            com.zipow.videobox.ptapp.mm.ZoomChatSession r3 = r2.getSessionById(r3)
            if (r3 == 0) goto L_0x008b
            boolean r6 = r3.isGroup()
            if (r6 == 0) goto L_0x008b
            r1.isGroup = r4
            com.zipow.videobox.ptapp.mm.ZoomGroup r3 = r3.getSessionGroup()
            if (r3 != 0) goto L_0x0084
            return r0
        L_0x0084:
            java.lang.String r3 = r3.getGroupDisplayName(r12)
            r1.screenName = r3
            goto L_0x0097
        L_0x008b:
            java.lang.String r3 = r1.sessionId
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getBuddyWithJID(r3)
            if (r3 == 0) goto L_0x0096
            r1.isGroup = r5
            goto L_0x0097
        L_0x0096:
            return r0
        L_0x0097:
            boolean r3 = r1.isGroup
            if (r3 != 0) goto L_0x00e4
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getMyself()
            if (r3 != 0) goto L_0x00a2
            return r1
        L_0x00a2:
            java.lang.String r6 = r3.getJid()
            java.lang.String r7 = r1.getSessionId()
            boolean r7 = p021us.zoom.androidlib.util.StringUtil.isSameString(r6, r7)
            if (r7 != 0) goto L_0x00b9
            java.lang.String r0 = r1.getSessionId()
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getBuddyWithJID(r0)
            goto L_0x00da
        L_0x00b9:
            java.lang.String r7 = r1.getSenderJid()
            boolean r6 = p021us.zoom.androidlib.util.StringUtil.isSameString(r6, r7)
            if (r6 != 0) goto L_0x00d2
            java.lang.String r0 = r1.getSenderJid()
            com.zipow.videobox.ptapp.mm.ZoomBuddy r3 = r2.getBuddyWithJID(r0)
            java.lang.String r0 = r1.getSenderJid()
            r1.sessionId = r0
            goto L_0x00da
        L_0x00d2:
            java.lang.String r2 = r1.sessionId
            boolean r2 = com.zipow.videobox.util.UIMgr.isMyNotes(r2)
            if (r2 == 0) goto L_0x00e3
        L_0x00da:
            if (r3 == 0) goto L_0x00e4
            com.zipow.videobox.view.IMAddrBookItem r0 = com.zipow.videobox.view.IMAddrBookItem.fromZoomBuddy(r3)
            r1.addrbookItem = r0
            goto L_0x00e4
        L_0x00e3:
            return r0
        L_0x00e4:
            java.lang.String r0 = r11.getContent()
            java.util.List r2 = r11.getHighlightPosList()
            if (r2 == 0) goto L_0x0195
            java.util.List r2 = r11.getHighlightPosList()
            int r2 = r2.size()
            if (r2 <= 0) goto L_0x0195
            com.zipow.videobox.ptapp.IMProtos$HighlightPositionItem r2 = r11.getHighlightPos(r5)
            int r3 = r2.getStart()
            if (r3 <= 0) goto L_0x0195
            int r2 = r2.getStart()
            int r3 = r11.getHighlightType()
            if (r3 != r4) goto L_0x0110
            int r2 = p021us.zoom.androidlib.util.StringUtil.utf8ToUtf16Index(r0, r2)
        L_0x0110:
            java.lang.String r3 = r0.substring(r5, r2)
            r6 = 10
            int r6 = r3.lastIndexOf(r6)
            r7 = -1
            if (r6 == r7) goto L_0x0123
            int r6 = r6 + r4
            java.lang.String r3 = r3.substring(r6)
            goto L_0x0124
        L_0x0123:
            r6 = 0
        L_0x0124:
            android.widget.TextView r7 = new android.widget.TextView
            r7.<init>(r12)
            int r8 = android.os.Build.VERSION.SDK_INT
            r9 = 23
            if (r8 < r9) goto L_0x0135
            int r8 = p021us.zoom.videomeetings.C4558R.style.UIKitTextView_SecondaryText_Small_Dimmed
            r7.setTextAppearance(r8)
            goto L_0x013a
        L_0x0135:
            int r8 = p021us.zoom.videomeetings.C4558R.style.UIKitTextView_SecondaryText_Small_Dimmed
            r7.setTextAppearance(r12, r8)
        L_0x013a:
            android.text.TextPaint r7 = r7.getPaint()
            r8 = 1133903872(0x43960000, float:300.0)
            int r8 = p021us.zoom.androidlib.util.UIUtil.dip2px(r12, r8)
            float r9 = r7.measureText(r3)
            double r9 = (double) r9
            double r9 = java.lang.Math.ceil(r9)
            int r9 = (int) r9
            if (r9 <= r8) goto L_0x0176
            float r8 = (float) r8
            android.text.TextUtils$TruncateAt r9 = android.text.TextUtils.TruncateAt.START
            java.lang.CharSequence r7 = android.text.TextUtils.ellipsize(r3, r7, r8, r9)
            int r3 = r3.length()
            int r8 = r7.length()
            int r3 = r3 - r8
            int r3 = r3 + r6
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append(r7)
            java.lang.String r0 = r0.substring(r2)
            r6.append(r0)
            java.lang.String r0 = r6.toString()
            r2 = r3
            goto L_0x0196
        L_0x0176:
            if (r6 <= 0) goto L_0x0193
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "…"
            r2.append(r3)
            java.lang.String r0 = r0.substring(r6)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            int r2 = r6 + -1
            if (r2 >= 0) goto L_0x0196
            r2 = 0
            goto L_0x0196
        L_0x0193:
            r2 = r6
            goto L_0x0196
        L_0x0195:
            r2 = 0
        L_0x0196:
            android.text.SpannableStringBuilder r3 = new android.text.SpannableStringBuilder
            r3.<init>(r0)
            java.util.List r0 = r11.getHighlightPosList()
            java.util.Iterator r0 = r0.iterator()
        L_0x01a3:
            boolean r6 = r0.hasNext()
            if (r6 == 0) goto L_0x01fe
            java.lang.Object r6 = r0.next()
            com.zipow.videobox.ptapp.IMProtos$HighlightPositionItem r6 = (com.zipow.videobox.ptapp.IMProtos.HighlightPositionItem) r6
            android.text.style.ForegroundColorSpan r7 = new android.text.style.ForegroundColorSpan
            android.content.res.Resources r8 = r12.getResources()
            int r9 = p021us.zoom.videomeetings.C4558R.color.zm_highlight
            int r8 = r8.getColor(r9)
            r7.<init>(r8)
            int r8 = r11.getHighlightType()     // Catch:{ Exception -> 0x01a3 }
            r9 = 33
            if (r8 != r4) goto L_0x01e8
            java.lang.String r8 = r11.getContent()     // Catch:{ Exception -> 0x01a3 }
            int r10 = r6.getStart()     // Catch:{ Exception -> 0x01a3 }
            int r10 = p021us.zoom.androidlib.util.StringUtil.utf8ToUtf16Index(r8, r10)     // Catch:{ Exception -> 0x01a3 }
            int r10 = r10 - r2
            int r10 = java.lang.Math.max(r10, r5)     // Catch:{ Exception -> 0x01a3 }
            int r6 = r6.getEnd()     // Catch:{ Exception -> 0x01a3 }
            int r6 = p021us.zoom.androidlib.util.StringUtil.utf8ToUtf16Index(r8, r6)     // Catch:{ Exception -> 0x01a3 }
            int r6 = r6 - r2
            int r6 = java.lang.Math.max(r6, r5)     // Catch:{ Exception -> 0x01a3 }
            r3.setSpan(r7, r10, r6, r9)     // Catch:{ Exception -> 0x01a3 }
            goto L_0x01a3
        L_0x01e8:
            int r8 = r6.getStart()     // Catch:{ Exception -> 0x01a3 }
            int r8 = r8 - r2
            int r8 = java.lang.Math.max(r8, r5)     // Catch:{ Exception -> 0x01a3 }
            int r6 = r6.getEnd()     // Catch:{ Exception -> 0x01a3 }
            int r6 = r6 - r2
            int r6 = java.lang.Math.max(r6, r5)     // Catch:{ Exception -> 0x01a3 }
            r3.setSpan(r7, r8, r6, r9)     // Catch:{ Exception -> 0x01a3 }
            goto L_0x01a3
        L_0x01fe:
            r1.content = r3
            return r1
        L_0x0201:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMContentMessageItem.initWithFileFilterSearchResult(com.zipow.videobox.ptapp.IMProtos$MessageSearchResult, android.content.Context):com.zipow.videobox.view.mm.MMContentMessageItem");
    }

    public boolean isComment() {
        return this.isComment;
    }

    @Nullable
    public String getThrId() {
        return this.thrId;
    }

    @Nullable
    public String getMsgId() {
        return this.msgId;
    }

    @Nullable
    public String getSessionId() {
        return this.sessionId;
    }

    @Nullable
    public String getSenderJid() {
        return this.senderJid;
    }

    @Nullable
    public String getSenderName() {
        return this.senderName;
    }

    public long getSendTime() {
        return this.sendTime;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public CharSequence getContent() {
        return this.content;
    }

    @Nullable
    public View getView(@NonNull Context context, int i, @Nullable View view, ViewGroup viewGroup, String str, MemCache<String, Drawable> memCache) {
        View view2;
        int i2;
        Context context2 = context;
        String str2 = str;
        if (view == null) {
            view2 = View.inflate(context2, C4558R.layout.zm_mm_chats_list_item, null);
            view2.setTag(this);
        } else {
            view2 = view;
        }
        AvatarView avatarView = (AvatarView) view2.findViewById(C4558R.C4560id.avatarView);
        ZMEllipsisTextView zMEllipsisTextView = (ZMEllipsisTextView) view2.findViewById(C4558R.C4560id.txtTitle);
        TextView textView = (TextView) view2.findViewById(C4558R.C4560id.txtMessage);
        TextView textView2 = (TextView) view2.findViewById(C4558R.C4560id.txtTime);
        TextView textView3 = (TextView) view2.findViewById(C4558R.C4560id.txtNoteBubble);
        PresenceStateView presenceStateView = (PresenceStateView) view2.findViewById(C4558R.C4560id.imgPresence);
        ImageView imageView = (ImageView) view2.findViewById(C4558R.C4560id.imgE2EFlag);
        if (zMEllipsisTextView != null) {
            String str3 = "";
            if (!this.isGroup) {
                String sessionId2 = getSessionId();
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(sessionId2);
                    if (buddyWithJID != null) {
                        int accountStatus = buddyWithJID.getAccountStatus();
                        i2 = buddyWithJID.getAccountStatus() == 1 ? C4558R.string.zm_lbl_deactivated_62074 : buddyWithJID.getAccountStatus() == 2 ? C4558R.string.zm_lbl_terminated_62074 : 0;
                        if (StringUtil.isSameString(str2, getSenderJid())) {
                            str3 = buddyWithJID.getScreenName();
                        } else {
                            str3 = getScreenName();
                        }
                    }
                }
                i2 = 0;
            } else {
                str3 = getScreenName();
                i2 = 0;
            }
            zMEllipsisTextView.setEllipsisText(str3, i2);
        }
        if (textView != null) {
            textView.setSingleLine(false);
            textView.setMaxLines(2);
            if (!isGroup()) {
                textView.setText(getContent());
            } else if (StringUtil.isSameString(str2, getSenderJid())) {
                textView.setText(getContent());
            } else {
                String str4 = this.senderName;
                if (str4 != null) {
                    str4 = TextUtils.ellipsize(str4, textView.getPaint(), (float) UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 150.0f), TruncateAt.END).toString();
                }
                textView.setText(TextUtils.concat(new CharSequence[]{str4, ": ", getContent()}));
            }
        }
        if (textView2 != null && !textView2.isInEditMode()) {
            if (getSendTime() > 0) {
                textView2.setText(formatTime(context2, getSendTime()));
            } else {
                textView2.setText("");
            }
        }
        if (textView3 != null) {
            textView3.setVisibility(8);
        }
        presenceStateView.setVisibility(8);
        imageView.setVisibility(8);
        if (avatarView != null) {
            if (!this.isGroup) {
                IMAddrBookItem iMAddrBookItem = this.addrbookItem;
                if (iMAddrBookItem != null) {
                    avatarView.show(iMAddrBookItem.getAvatarParamsBuilder());
                } else {
                    avatarView.show(new ParamsBuilder().setName(getScreenName(), getSessionId()));
                }
            } else {
                avatarView.show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_avatar_group, null));
            }
        }
        return view2;
    }

    private String getAvatar() {
        IMAddrBookItem iMAddrBookItem = this.addrbookItem;
        if (iMAddrBookItem == null) {
            return null;
        }
        return iMAddrBookItem.getAvatarPath();
    }

    public boolean isBlockedByIBMsg() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.sessionId);
        if (buddyWithJID == null) {
            return false;
        }
        return buddyWithJID.isIMBlockedByIB();
    }

    private String formatTime(@NonNull Context context, long j) {
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = currentTimeMillis - 86400000;
        if (TimeUtil.isSameDate(j, currentTimeMillis)) {
            return TimeUtil.formatTime(context, j);
        }
        if (TimeUtil.isSameDate(j, j2)) {
            return context.getString(C4558R.string.zm_lbl_yesterday);
        }
        if (TimeUtil.yearsDiff(currentTimeMillis, j) == 0) {
            return TimeUtil.formatDate(context, j);
        }
        return TimeUtil.formatDateWithYear(context, j);
    }

    public boolean isGroup() {
        return this.isGroup;
    }

    public void setGroup(boolean z) {
        this.isGroup = z;
    }

    @Nullable
    public String getScreenName() {
        return this.screenName;
    }

    public void setScreenName(@Nullable String str) {
        this.screenName = str;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof MMContentMessageItem)) {
            return false;
        }
        return StringUtil.isSameString(this.msgId, ((MMContentMessageItem) obj).getMsgId());
    }

    public int hashCode() {
        String str = this.msgId;
        if (str == null) {
            return 0;
        }
        return str.hashCode();
    }
}
