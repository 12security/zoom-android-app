package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.zipow.videobox.markdown.MarkDownUtils;
import com.zipow.videobox.markdown.URLImageParser.OnUrlDrawableUpdateListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.tempbean.IMessageTemplateExtendMessage;
import com.zipow.videobox.tempbean.IMessageTemplateHead;
import com.zipow.videobox.tempbean.IMessageTemplateSettings;
import com.zipow.videobox.tempbean.IMessageTemplateSubHead;
import com.zipow.videobox.tempbean.IMessageTemplateTextStyle;
import com.zipow.videobox.tempbean.IZoomMessageTemplate;
import com.zipow.videobox.util.TintUtil;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMMessageTemplateSectionGroupView;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageTemplateView */
public class MessageTemplateView extends AbsMessageView {
    private LinearLayout contactLinear;
    private TextView contactName;
    private TextView editTime;
    private TextView groupContact;
    private LinearLayout groupLinear;
    private TextView groupName;
    private ImageView mAppIcon;
    private AvatarView mAvatarView;
    protected ImageView mImgStarred;
    protected MMMessageItem mMessageItem;
    protected ReactionLabelsView mReactionLabels;
    private LinearLayout mScreenNameLinear;
    private MMMessageTemplateSectionGroupView mSectionGroupView;
    private ImageView mSideBar;
    private TextView mTxtScreenName;
    private LinearLayout starredMsgTitleLinear;
    /* access modifiers changed from: private */
    public TextView subTitle;
    private TextView time;
    /* access modifiers changed from: private */
    public TextView title;
    private TextView txtStarDes;
    private LinearLayout visibleToYouLinear;

    public MessageTemplateView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MessageTemplateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageTemplateView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_template, this);
    }

    private void initView() {
        inflateLayout();
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.title = (TextView) findViewById(C4558R.C4560id.titleTxt);
        this.subTitle = (TextView) findViewById(C4558R.C4560id.subTitleTxt);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mScreenNameLinear = (LinearLayout) findViewById(C4558R.C4560id.screenNameLinear);
        this.mSectionGroupView = (MMMessageTemplateSectionGroupView) findViewById(C4558R.C4560id.zm_mm_section_group);
        this.visibleToYouLinear = (LinearLayout) findViewById(C4558R.C4560id.visibleToYouLinear);
        this.starredMsgTitleLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_title_linear);
        this.contactLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_linear);
        this.contactName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_name);
        this.groupLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_linear);
        this.groupContact = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_contact);
        this.groupName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_name);
        this.time = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_time);
        this.mSideBar = (ImageView) findViewById(C4558R.C4560id.zm_mm_sidebar);
        this.editTime = (TextView) findViewById(C4558R.C4560id.zm_mm_section_edit_time);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.mAppIcon = (ImageView) findViewById(C4558R.C4560id.appImg);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        this.txtStarDes = (TextView) findViewById(C4558R.C4560id.txtStarDes);
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    public void changeAvatar(boolean z) {
        if (z) {
            LayoutParams layoutParams = (LayoutParams) this.mAvatarView.getLayoutParams();
            layoutParams.width = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.height = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 16.0f);
            this.mAvatarView.setLayoutParams(layoutParams);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) this.mAvatarView.getLayoutParams();
        layoutParams2.width = UIUtil.dip2px(getContext(), 40.0f);
        layoutParams2.height = UIUtil.dip2px(getContext(), 40.0f);
        this.mAvatarView.setLayoutParams(layoutParams2);
    }

    public void setVisibleToYou(String str, String str2) {
        this.visibleToYouLinear.setVisibility(8);
        ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
        if (zoomMessageTemplate != null && zoomMessageTemplate.isOnlyVisibleToYou(str, str2)) {
            this.visibleToYouLinear.setVisibility(0);
        }
    }

    public void setStarredMessage(@NonNull MMMessageItem mMMessageItem) {
        if (mMMessageItem.hideStarView) {
            this.mSectionGroupView.setFocusable(false);
            this.starredMsgTitleLinear.setVisibility(0);
            this.mScreenNameLinear.setVisibility(8);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                    if (sessionById != null) {
                        if (mMMessageItem.isGroupMessage) {
                            this.contactLinear.setVisibility(8);
                            this.groupLinear.setVisibility(0);
                            ZoomGroup sessionGroup = sessionById.getSessionGroup();
                            if (sessionGroup != null) {
                                this.groupName.setText(sessionGroup.getGroupName());
                            }
                        } else {
                            this.contactLinear.setVisibility(0);
                            this.groupLinear.setVisibility(8);
                            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                            if (sessionBuddy != null) {
                                this.groupName.setText(BuddyNameUtil.getMyDisplayName(sessionBuddy));
                            } else if (TextUtils.equals(mMMessageItem.sessionId, myself.getJid())) {
                                this.groupName.setText(BuddyNameUtil.getMyDisplayName(myself));
                            }
                        }
                        this.time.setText(TimeUtil.formatDateTimeCap(getContext(), mMMessageItem.serverSideTime));
                        String string = StringUtil.isSameString(myself.getJid(), mMMessageItem.fromJid) ? getContext().getString(C4558R.string.zm_lbl_content_you) : mMMessageItem.fromScreenName;
                        this.groupContact.setText(string);
                        this.contactName.setText(string);
                        if (mMMessageItem.isComment) {
                            this.txtStarDes.setText(C4558R.string.zm_lbl_from_thread_88133);
                            this.txtStarDes.setVisibility(0);
                        } else if (mMMessageItem.commentsCount > 0) {
                            this.txtStarDes.setText(getResources().getQuantityString(C4558R.plurals.zm_lbl_comment_reply_title_88133, (int) mMMessageItem.commentsCount, new Object[]{Integer.valueOf((int) mMMessageItem.commentsCount)}));
                            this.txtStarDes.setVisibility(0);
                        } else {
                            this.txtStarDes.setVisibility(8);
                        }
                    }
                }
            }
        } else {
            this.starredMsgTitleLinear.setVisibility(8);
            this.txtStarDes.setVisibility(8);
        }
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        setScreenName(mMMessageItem.fromScreenName);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
            this.mImgStarred.setVisibility(8);
        } else {
            this.mImgStarred.setVisibility(0);
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
                    }
                }
            }
        }
        if (mMMessageItem.onlyMessageShow) {
            AvatarView avatarView2 = this.mAvatarView;
            if (avatarView2 != null) {
                avatarView2.setVisibility(4);
            }
            LinearLayout linearLayout = this.mScreenNameLinear;
            if (linearLayout != null) {
                linearLayout.setVisibility(8);
            }
        } else {
            AvatarView avatarView3 = this.mAvatarView;
            if (avatarView3 != null) {
                avatarView3.setVisibility(0);
            }
            LinearLayout linearLayout2 = this.mScreenNameLinear;
            if (linearLayout2 != null) {
                linearLayout2.setVisibility(0);
            }
        }
        IZoomMessageTemplate iZoomMessageTemplate = mMMessageItem.template;
        if (iZoomMessageTemplate != null) {
            setTitle(iZoomMessageTemplate.getHead());
            IMessageTemplateSettings settings = iZoomMessageTemplate.getSettings();
            if (settings != null) {
                setSideBar(settings.getDefault_sidebar_color(), settings.isIs_split_sidebar());
            } else {
                setSideBar(null, false);
            }
        } else {
            setTitle(null);
            setSideBar(null, true);
        }
        setSectionGroup(iZoomMessageTemplate);
        setStarredMessage(mMMessageItem);
        setVisibleToYou(mMMessageItem.sessionId, mMMessageItem.messageXMPPId);
        setReactionLabels(mMMessageItem);
        if (mMMessageItem.editMessageTime > 0) {
            this.editTime.setVisibility(0);
        } else {
            this.editTime.setVisibility(8);
        }
        AvatarView avatarView4 = this.mAvatarView;
        if (avatarView4 != null) {
            avatarView4.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageTemplateView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageTemplateView.this.mMessageItem);
                    }
                }
            });
        }
        findViewById(C4558R.C4560id.templateTitle).setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                OnShowContextMenuListener onShowContextMenuListener = MessageTemplateView.this.getOnShowContextMenuListener();
                if (onShowContextMenuListener != null) {
                    onShowContextMenuListener.onShowContextMenu(view, MessageTemplateView.this.mMessageItem);
                }
                return false;
            }
        });
        findViewById(C4558R.C4560id.templateTitle).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OnClickMessageListener onClickMessageListener = MessageTemplateView.this.getOnClickMessageListener();
                if (onClickMessageListener != null) {
                    onClickMessageListener.onClickMessage(MessageTemplateView.this.mMessageItem);
                }
            }
        });
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

    private void setTitle(IMessageTemplateHead iMessageTemplateHead) {
        TextView textView = this.title;
        if (textView != null) {
            if (iMessageTemplateHead == null) {
                textView.setText("");
                TextView textView2 = this.subTitle;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                }
                return;
            }
            int i = 0;
            if (iMessageTemplateHead.isSupportItem()) {
                IMessageTemplateTextStyle style = iMessageTemplateHead.getStyle();
                if (style == null || !CollectionsUtil.isListEmpty(iMessageTemplateHead.getExtendMessages())) {
                    this.title.setTextAppearance(getContext(), C4558R.style.UIKitTextView_PrimaryText_Normal);
                } else {
                    style.applyStyle(this.title);
                }
                if (!CollectionsUtil.isListEmpty(iMessageTemplateHead.getExtendMessages())) {
                    this.title.setMovementMethod(LinkMovementMethod.getInstance());
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    int i2 = 0;
                    while (i2 < iMessageTemplateHead.getExtendMessages().size()) {
                        int i3 = i2 + 1;
                        ((IMessageTemplateExtendMessage) iMessageTemplateHead.getExtendMessages().get(i2)).emitter(spannableStringBuilder, this.title, i3 >= iMessageTemplateHead.getExtendMessages().size() ? null : (IMessageTemplateExtendMessage) iMessageTemplateHead.getExtendMessages().get(i3), new OnUrlDrawableUpdateListener() {
                            public void onUrlDrawableUpdate() {
                                MessageTemplateView.this.title.invalidate();
                            }
                        });
                        i2 = i3;
                    }
                    this.title.setText(spannableStringBuilder);
                } else {
                    this.title.setText(iMessageTemplateHead.getText());
                }
                MarkDownUtils.addAutoLink(this.title);
            } else {
                this.title.setText(iMessageTemplateHead.getFall_back());
            }
            if (this.subTitle != null) {
                final IMessageTemplateSubHead subHead = iMessageTemplateHead.getSubHead();
                if (subHead == null) {
                    TextView textView3 = this.subTitle;
                    if (textView3 != null) {
                        textView3.setVisibility(8);
                    }
                    return;
                }
                this.subTitle.setVisibility(0);
                if (subHead.isSupportItem()) {
                    if (!TextUtils.isEmpty(subHead.getLink())) {
                        this.subTitle.setMovementMethod(LinkMovementMethod.getInstance());
                        SpannableString spannableString = new SpannableString(subHead.getText());
                        spannableString.setSpan(new ClickableSpan() {
                            public void onClick(@NonNull View view) {
                                UIUtil.openURL(MessageTemplateView.this.getContext(), subHead.getLink());
                            }

                            public void updateDrawState(@NonNull TextPaint textPaint) {
                                textPaint.setColor(ContextCompat.getColor(MessageTemplateView.this.getContext(), C4558R.color.zm_template_link));
                                textPaint.setUnderlineText(false);
                            }
                        }, 0, spannableString.length(), 33);
                        this.subTitle.setText(spannableString);
                    } else if (!CollectionsUtil.isListEmpty(subHead.getExtendMessages())) {
                        this.subTitle.setMovementMethod(LinkMovementMethod.getInstance());
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                        while (i < subHead.getExtendMessages().size()) {
                            int i4 = i + 1;
                            ((IMessageTemplateExtendMessage) subHead.getExtendMessages().get(i)).emitter(spannableStringBuilder2, this.subTitle, i4 >= subHead.getExtendMessages().size() ? null : (IMessageTemplateExtendMessage) subHead.getExtendMessages().get(i4), new OnUrlDrawableUpdateListener() {
                                public void onUrlDrawableUpdate() {
                                    MessageTemplateView.this.subTitle.invalidate();
                                }
                            });
                            i = i4;
                        }
                        this.subTitle.setText(spannableStringBuilder2);
                    } else {
                        this.subTitle.setText(subHead.getText());
                    }
                    MarkDownUtils.addAutoLink(this.subTitle);
                    IMessageTemplateTextStyle style2 = subHead.getStyle();
                    if (style2 == null || !CollectionsUtil.isListEmpty(subHead.getExtendMessages())) {
                        this.subTitle.setTextAppearance(getContext(), C4558R.style.UIKitTextView_SecondaryText_Small);
                        this.subTitle.setTextColor(ContextCompat.getColor(getContext(), C4558R.color.zm_gray_6C6C7F));
                    } else {
                        style2.applyStyle(this.subTitle);
                    }
                } else {
                    this.subTitle.setText(subHead.getFall_back());
                }
            }
        }
    }

    private void setSectionGroup(IZoomMessageTemplate iZoomMessageTemplate) {
        MMMessageTemplateSectionGroupView mMMessageTemplateSectionGroupView = this.mSectionGroupView;
        if (mMMessageTemplateSectionGroupView != null) {
            mMMessageTemplateSectionGroupView.setOnClickMessageListener(getOnClickMessageListener());
            this.mSectionGroupView.setOnShowContextMenuListener(getOnShowContextMenuListener());
            this.mSectionGroupView.setmOnClickTemplateListener(getmOnClickTemplateListener());
            this.mSectionGroupView.setmOnClickActionMoreListener(getmOnClickActionMoreListener());
            this.mSectionGroupView.setmOnClickTemplateActionMoreListener(getmOnClickTemplateActionMoreListener());
            this.mSectionGroupView.setData(this.mMessageItem, iZoomMessageTemplate);
        }
    }

    private void setSideBar(String str, boolean z) {
        ImageView imageView = this.mSideBar;
        if (imageView != null) {
            if (z) {
                imageView.setVisibility(8);
            } else {
                imageView.setVisibility(0);
                setSideBarColor(str);
            }
        }
    }

    private void setSideBarColor(String str) {
        if (this.mSideBar != null) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), C4558R.C4559drawable.zm_mm_template_side_bar);
            if (TextUtils.isEmpty(str)) {
                int color = ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_blue_0E71EB);
                if (drawable != null) {
                    this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, color));
                }
                return;
            }
            if (drawable != null) {
                try {
                    this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, Color.parseColor(str)));
                } catch (Exception unused) {
                    if (drawable != null) {
                        if ("orange".equalsIgnoreCase(str)) {
                            this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, Color.parseColor("#FFA500")));
                        } else {
                            this.mSideBar.setBackgroundDrawable(TintUtil.tintColor(drawable, ContextCompat.getColor(getContext(), C4558R.color.zm_ui_kit_color_blue_0E71EB)));
                        }
                    }
                }
            }
        }
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

    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }
}
