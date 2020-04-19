package com.zipow.videobox.view.p014mm.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZMWebLinkFilter;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMeetingNOListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickStatusImageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMMessageItemAtNameSpan;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMTextView;
import p021us.zoom.androidlib.widget.ZMTextView.LocalLinkMovementMethod;
import p021us.zoom.androidlib.widget.ZMTextView.OnClickLinkListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageTextView */
public class MessageTextView extends AbsMessageView implements OnClickLinkListener {
    public static String TAG = "MessageTextView";
    protected AvatarView mAvatarView;
    protected ImageView mImgStarred;
    protected ImageView mImgStatus;
    protected MMMessageItem mMessageItem;
    protected TextView mNewMessage;
    protected LinearLayout mPanelMessage;
    protected ProgressBar mProgressBar;
    protected ReactionLabelsView mReactionLabels;
    protected View mTitleLinear;
    protected TextView mTxtEditTime;
    protected TextView mTxtMessage;
    protected TextView mTxtMessageForBigEmoji;
    protected TextView mTxtScreenName;

    /* renamed from: com.zipow.videobox.view.mm.message.MessageTextView$MeetingNoHookInfo */
    static class MeetingNoHookInfo {
        public int end;
        public String lable;

        /* renamed from: no */
        public String f346no;
        public int start;

        MeetingNoHookInfo() {
        }
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Drawable getMesageBackgroudDrawable() {
        return null;
    }

    public MessageTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MessageTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageTextView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void initView() {
        inflateLayout();
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mImgStatus = (ImageView) findViewById(C4558R.C4560id.imgStatus);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progressBar1);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mTxtEditTime = (TextView) findViewById(C4558R.C4560id.txtMessage_edit_time);
        this.mPanelMessage = (LinearLayout) findViewById(C4558R.C4560id.panel_textMessage);
        this.mNewMessage = (TextView) findViewById(C4558R.C4560id.newMessage);
        this.mTxtMessageForBigEmoji = (TextView) findViewById(C4558R.C4560id.txtMessageForBigEmoji);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        this.mTitleLinear = findViewById(C4558R.C4560id.zm_message_list_item_title_linear);
        setStatusImage(false, 0);
        LinearLayout linearLayout = this.mPanelMessage;
        if (linearLayout != null) {
            linearLayout.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnShowContextMenuListener onShowContextMenuListener = MessageTextView.this.getOnShowContextMenuListener();
                    if (onShowContextMenuListener != null) {
                        return onShowContextMenuListener.onShowContextMenu(view, MessageTextView.this.mMessageItem);
                    }
                    return false;
                }
            });
            this.mPanelMessage.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessageTextView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessageTextView.this.mMessageItem);
                    }
                }
            });
        }
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickStatusImageListener onClickStatusImageListener = MessageTextView.this.getOnClickStatusImageListener();
                    if (onClickStatusImageListener != null) {
                        onClickStatusImageListener.onClickStatusImage(MessageTextView.this.mMessageItem);
                    }
                }
            });
        }
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageTextView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageTextView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MessageTextView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MessageTextView.this.mMessageItem);
                    }
                    return false;
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_text_receive, this);
    }

    public void changeAvatar(boolean z) {
        if (z) {
            LayoutParams layoutParams = (LayoutParams) this.mAvatarView.getLayoutParams();
            layoutParams.width = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.height = UIUtil.dip2px(getContext(), 24.0f);
            this.mAvatarView.setLayoutParams(layoutParams);
            LayoutParams layoutParams2 = (LayoutParams) this.mTitleLinear.getLayoutParams();
            layoutParams2.leftMargin = UIUtil.dip2px(getContext(), 40.0f);
            this.mTitleLinear.setLayoutParams(layoutParams2);
            return;
        }
        LayoutParams layoutParams3 = (LayoutParams) this.mAvatarView.getLayoutParams();
        layoutParams3.width = UIUtil.dip2px(getContext(), 40.0f);
        layoutParams3.height = UIUtil.dip2px(getContext(), 40.0f);
        this.mAvatarView.setLayoutParams(layoutParams3);
        LayoutParams layoutParams4 = (LayoutParams) this.mTitleLinear.getLayoutParams();
        layoutParams4.leftMargin = UIUtil.dip2px(getContext(), 56.0f);
        this.mTitleLinear.setLayoutParams(layoutParams4);
    }

    public void reSizeTitleBarForReplyPage() {
        LayoutParams layoutParams = (LayoutParams) this.mTitleLinear.getLayoutParams();
        if (layoutParams.leftMargin != UIUtil.dip2px(getContext(), 56.0f)) {
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 56.0f);
            this.mTitleLinear.setLayoutParams(layoutParams);
            LayoutParams layoutParams2 = (LayoutParams) this.mAvatarView.getLayoutParams();
            layoutParams2.leftMargin = UIUtil.dip2px(getContext(), 16.0f);
            this.mAvatarView.setLayoutParams(layoutParams2);
        }
    }

    /* access modifiers changed from: protected */
    public int getTextColor() {
        int i;
        if (!this.mMessageItem.isE2E) {
            i = C4558R.color.zm_text_on_light;
        } else if (this.mMessageItem.messageState == 9 || this.mMessageItem.messageState == 8 || this.mMessageItem.messageState == 10) {
            i = C4558R.color.zm_chat_msg_txt_e2e_warn;
        } else if (this.mMessageItem.messageState == 3 || this.mMessageItem.messageState == 11 || this.mMessageItem.messageState == 13) {
            i = C4558R.color.zm_half_translucent_black;
        } else {
            i = C4558R.color.zm_text_on_light;
        }
        return getResources().getColor(i);
    }

    private int getLinkTextColor() {
        int i;
        if (!this.mMessageItem.isE2E) {
            i = C4558R.color.zm_link_text_color;
        } else if (this.mMessageItem.messageState == 9 || this.mMessageItem.messageState == 8 || this.mMessageItem.messageState == 10) {
            i = C4558R.color.zm_chat_msg_txt_e2e_warn;
        } else if (this.mMessageItem.messageState == 3 || this.mMessageItem.messageState == 11 || this.mMessageItem.messageState == 13) {
            i = C4558R.color.zm_half_translucent_black;
        } else {
            i = C4558R.color.zm_link_text_color;
        }
        return getResources().getColor(i);
    }

    public void setMessage(@Nullable CharSequence charSequence, long j) {
        if (!(charSequence == null || this.mTxtMessage == null)) {
            CommonEmojiHelper instance = CommonEmojiHelper.getInstance();
            if (this.mTxtMessageForBigEmoji == null) {
                this.mTxtMessage.setText(charSequence);
            } else if (instance.isAllEmojis(charSequence)) {
                this.mTxtMessageForBigEmoji.setText(charSequence);
                this.mTxtMessageForBigEmoji.setVisibility(0);
                this.mTxtMessage.setVisibility(8);
            } else {
                this.mTxtMessage.setText(charSequence);
                this.mTxtMessageForBigEmoji.setVisibility(8);
                this.mTxtMessage.setVisibility(0);
            }
            this.mTxtMessage.setMovementMethod(LocalLinkMovementMethod.getInstance());
            this.mTxtMessage.setTextColor(getTextColor());
            this.mTxtMessage.setLinkTextColor(getLinkTextColor());
            TextView textView = this.mTxtMessage;
            if (textView instanceof ZMTextView) {
                ((ZMTextView) textView).setOnClickLinkListener(this);
            }
            if (j > 0) {
                this.mTxtEditTime.setVisibility(0);
                this.mTxtEditTime.setText(getResources().getString(C4558R.string.zm_mm_edit_message_time_19884));
            } else {
                this.mTxtEditTime.setVisibility(8);
            }
        }
        hookZoomURL(this.mTxtMessage);
        ZMWebLinkFilter.filter(this.mTxtMessage);
        filterAtNames(this.mTxtMessage);
    }

    public void setStatusImage(boolean z, int i) {
        ImageView imageView = this.mImgStatus;
        if (imageView != null) {
            imageView.setVisibility(z ? 0 : 8);
            this.mImgStatus.setImageResource(i);
        }
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    @SuppressLint({"NewApi"})
    private void updateChatMsgBackground() {
        if (VERSION.SDK_INT < 16) {
            this.mPanelMessage.setBackgroundDrawable(getMesageBackgroudDrawable());
        } else {
            this.mPanelMessage.setBackground(getMesageBackgroudDrawable());
        }
    }

    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        boolean z;
        this.mMessageItem = mMMessageItem;
        this.mNewMessage.setVisibility(8);
        setMessage(mMMessageItem.message, mMMessageItem.editMessageTime);
        setReactionLabels(mMMessageItem);
        updateChatMsgBackground();
        LinearLayout linearLayout = (LinearLayout) findViewById(C4558R.C4560id.panelMsgLayout);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
            z = sessionById != null ? sessionById.isMessageMarkUnread(mMMessageItem.messageXMPPId) : false;
            if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
                this.mImgStarred.setVisibility(8);
            } else {
                this.mImgStarred.setVisibility(0);
            }
        } else {
            z = false;
        }
        if (z) {
            this.mNewMessage.setVisibility(0);
        }
        if (!mMMessageItem.onlyMessageShow || z) {
            linearLayout.setPadding(linearLayout.getPaddingLeft(), linearLayout.getPaddingBottom(), linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
            this.mAvatarView.setVisibility(0);
            if (this.mTxtScreenName != null && mMMessageItem.isIncomingMessage()) {
                setScreenName(mMMessageItem.fromScreenName);
                TextView textView = this.mTxtScreenName;
                if (textView != null) {
                    textView.setVisibility(0);
                }
            } else if (this.mTxtScreenName == null || !mMMessageItem.isSendingMessage() || getContext() == null) {
                TextView textView2 = this.mTxtScreenName;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                }
            } else {
                setScreenName(getContext().getString(C4558R.string.zm_lbl_content_you));
                this.mTxtScreenName.setVisibility(0);
            }
            if (!isInEditMode()) {
                String str = mMMessageItem.fromJid;
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself == null || str == null || !str.equals(myself.getJid())) {
                        myself = zoomMessenger.getBuddyWithJID(str);
                    }
                    if (mMMessageItem.fromContact == null && myself != null) {
                        mMMessageItem.fromContact = IMAddrBookItem.fromZoomBuddy(myself);
                    }
                    if (mMMessageItem.fromContact != null) {
                        AvatarView avatarView = this.mAvatarView;
                        if (avatarView != null) {
                            avatarView.show(mMMessageItem.fromContact.getAvatarParamsBuilder());
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        this.mAvatarView.setVisibility(4);
        TextView textView3 = this.mTxtScreenName;
        if (textView3 != null) {
            textView3.setVisibility(8);
        }
        linearLayout.setPadding(linearLayout.getPaddingLeft(), 0, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
        setMessageItem(mMMessageItem);
        if (z) {
            this.mAvatarView.setVisibility(4);
            this.mReactionLabels.setVisibility(8);
            if (this.mTxtScreenName.getVisibility() == 0) {
                this.mTxtScreenName.setVisibility(4);
            }
        }
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        ReactionLabelsView reactionLabelsView = this.mReactionLabels;
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), (iArr[1] + getHeight()) - ((reactionLabelsView == null || reactionLabelsView.getVisibility() == 8) ? 0 : this.mReactionLabels.getHeight() + (UIUtil.dip2px(getContext(), 4.0f) * 2)));
    }

    public void setNewMessageGone() {
        this.mNewMessage.setVisibility(8);
    }

    public void setReactionLabels(MMMessageItem mMMessageItem) {
        if (!(mMMessageItem == null || this.mReactionLabels == null)) {
            if (mMMessageItem.hideStarView) {
                this.mReactionLabels.setVisibility(8);
                return;
            }
            this.mReactionLabels.setLabels(mMMessageItem, getOnClickReactionLabelListener());
        }
    }

    public boolean onLongClickLink(String str) {
        if (this.mPanelMessage == null) {
            return false;
        }
        OnShowContextMenuListener onShowContextMenuListener = getOnShowContextMenuListener();
        if (onShowContextMenuListener != null) {
            return onShowContextMenuListener.onShowLinkContextMenu(this.mPanelMessage, this.mMessageItem, str);
        }
        return false;
    }

    public boolean onLongClickWhole(String str) {
        if (this.mPanelMessage == null) {
            return false;
        }
        OnShowContextMenuListener onShowContextMenuListener = getOnShowContextMenuListener();
        if (onShowContextMenuListener != null) {
            return onShowContextMenuListener.onShowContextMenu(this.mPanelMessage, this.mMessageItem);
        }
        return false;
    }

    private void removeMeetingNoHookInfoInArea(@Nullable List<MeetingNoHookInfo> list, int i, int i2) {
        if (!CollectionsUtil.isListEmpty(list) && i >= 0 && i < i2) {
            int i3 = 0;
            while (i3 < list.size()) {
                MeetingNoHookInfo meetingNoHookInfo = (MeetingNoHookInfo) list.get(i3);
                if (meetingNoHookInfo.start >= i && meetingNoHookInfo.end <= i2) {
                    list.remove(i3);
                    i3--;
                }
                i3++;
            }
        }
    }

    private void hookZoomURL(@Nullable TextView textView) {
        if (textView != null) {
            CharSequence text = textView.getText();
            Matcher matcher = Pattern.compile("(?<!\\d)(?:([0-9]{9,11}))(?!\\d)|(?<!\\d)(?:([0-9]{3})-([0-9]{3})-([0-9]{3,5}))(?!\\d)|(?<!\\d)(?:([0-9]{3}) ([0-9]{3}) ([0-9]{3,5}))(?!\\d)").matcher(text);
            ArrayList<MeetingNoHookInfo> arrayList = new ArrayList<>();
            while (matcher.find()) {
                MeetingNoHookInfo meetingNoHookInfo = new MeetingNoHookInfo();
                meetingNoHookInfo.end = matcher.end();
                meetingNoHookInfo.start = matcher.start();
                meetingNoHookInfo.lable = matcher.group();
                meetingNoHookInfo.f346no = meetingNoHookInfo.lable.replace("-", "").replace(OAuth.SCOPE_DELIMITER, "");
                arrayList.add(meetingNoHookInfo);
            }
            if (arrayList.size() > 0 && !(text instanceof Spannable)) {
                CharSequence spannableString = new SpannableString(text);
                textView.setText(spannableString);
                text = spannableString;
            }
            if (text instanceof Spannable) {
                Spannable spannable = (Spannable) text;
                URLSpan[] urls = textView.getUrls();
                if ((urls != null && urls.length >= 1) || arrayList.size() != 0) {
                    if (urls != null && urls.length > 0) {
                        for (URLSpan uRLSpan : urls) {
                            final String url = uRLSpan.getURL();
                            if (url.startsWith("http://https://") || url.startsWith("http://http://")) {
                                url = url.substring(7);
                            } else if (url.startsWith("tel:")) {
                                url = url.substring(4);
                            }
                            if (ZMIMUtils.isZoomURL(url)) {
                                C39496 r5 = new URLSpan(url) {
                                    public void onClick(View view) {
                                        MessageTextView.this.joinByURL(url);
                                    }
                                };
                                int spanStart = spannable.getSpanStart(uRLSpan);
                                int spanEnd = spannable.getSpanEnd(uRLSpan);
                                int spanFlags = spannable.getSpanFlags(uRLSpan);
                                if (spanStart >= 0 && spanEnd > spanStart) {
                                    spannable.removeSpan(uRLSpan);
                                    spannable.setSpan(r5, spanStart, spanEnd, spanFlags);
                                }
                                removeMeetingNoHookInfoInArea(arrayList, spanStart, spanEnd);
                            } else if (ZMIMUtils.isZoomMeetingNo(url)) {
                                C39507 r52 = new URLSpan(url) {
                                    public void onClick(View view) {
                                        OnClickMeetingNOListener onClickMeetingNOListener = MessageTextView.this.getOnClickMeetingNOListener();
                                        if (onClickMeetingNOListener != null) {
                                            onClickMeetingNOListener.onClickMeetingNO(url);
                                        }
                                    }
                                };
                                int spanStart2 = spannable.getSpanStart(uRLSpan);
                                int spanEnd2 = spannable.getSpanEnd(uRLSpan);
                                int spanFlags2 = spannable.getSpanFlags(uRLSpan);
                                if (spanStart2 >= 0 && spanEnd2 > spanStart2) {
                                    spannable.removeSpan(uRLSpan);
                                    spannable.setSpan(r52, spanStart2, spanEnd2, spanFlags2);
                                }
                                removeMeetingNoHookInfoInArea(arrayList, spanStart2, spanEnd2);
                            }
                        }
                    }
                    for (MeetingNoHookInfo meetingNoHookInfo2 : arrayList) {
                        final String str = meetingNoHookInfo2.f346no;
                        C39518 r3 = new URLSpan(str) {
                            public void onClick(View view) {
                                OnClickMeetingNOListener onClickMeetingNOListener = MessageTextView.this.getOnClickMeetingNOListener();
                                if (onClickMeetingNOListener != null) {
                                    onClickMeetingNOListener.onClickMeetingNO(str);
                                }
                            }
                        };
                        if (meetingNoHookInfo2.start >= 0 && meetingNoHookInfo2.end > meetingNoHookInfo2.start) {
                            spannable.setSpan(r3, meetingNoHookInfo2.start, meetingNoHookInfo2.end, 33);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void joinByURL(String str) {
        Intent intent = new Intent(getContext(), JoinByURLActivity.class);
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        getContext().startActivity(intent);
    }

    private void filterAtNames(@Nullable TextView textView) {
        if (textView != null && this.mMessageItem.atNameSpanList != null && this.mMessageItem.atNameSpanList.size() > 0) {
            Spannable spannable = null;
            if (textView.getText() instanceof Spannable) {
                spannable = (Spannable) textView.getText();
            }
            if (spannable != null && spannable.length() > 0) {
                URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, spannable.length(), URLSpan.class);
                if (uRLSpanArr != null && uRLSpanArr.length > 0) {
                    int size = this.mMessageItem.atNameSpanList.size();
                    for (URLSpan uRLSpan : uRLSpanArr) {
                        int spanStart = spannable.getSpanStart(uRLSpan);
                        int spanEnd = spannable.getSpanEnd(uRLSpan);
                        int i = 0;
                        while (true) {
                            if (i >= size) {
                                break;
                            }
                            MMMessageItemAtNameSpan mMMessageItemAtNameSpan = (MMMessageItemAtNameSpan) this.mMessageItem.atNameSpanList.get(i);
                            if (mMMessageItemAtNameSpan.start <= spanStart && mMMessageItemAtNameSpan.end >= spanEnd) {
                                spannable.removeSpan(uRLSpan);
                                break;
                            }
                            i++;
                        }
                    }
                }
            }
        }
    }
}
