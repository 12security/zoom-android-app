package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageLoader;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickActionListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickActionMoreListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAddonListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMAddonMessage;
import com.zipow.videobox.view.p014mm.MMAddonMessage.ActionNode;
import com.zipow.videobox.view.p014mm.MMAddonMessage.AddonNode;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeBR;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeFooter;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeLabel;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeMsgHref;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeP;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageAddonView */
public class MessageAddonView extends AbsMessageView {
    private final String TAG = MessageAddonView.class.getSimpleName();
    private LinearLayout contactLinear;
    private TextView contactName;
    private TextView groupContact;
    private LinearLayout groupLinear;
    private TextView groupName;
    protected LinearLayout mActionBarLinear;
    protected TextView mActionBtn1;
    protected TextView mActionBtn2;
    protected TextView mActionBtnMore;
    protected AvatarView mAvatarView;
    protected ImageView mImgIcon;
    protected ImageView mImgStarred;
    protected MMMessageItem mMessageItem;
    protected View mPanelAddon;
    protected View mPanelTitle;
    protected ReactionLabelsView mReactionLabels;
    protected TextView mTxtBody;
    protected TextView mTxtFooter;
    protected TextView mTxtScreenName;
    protected TextView mTxtSummary;
    private LinearLayout starredMsgTitleLinear;
    private TextView time;
    private TextView txtStarDes;

    /* renamed from: com.zipow.videobox.view.mm.message.MessageAddonView$ForegroundColorClickableSpan */
    class ForegroundColorClickableSpan extends ClickableSpan {
        NodeMsgHref href;
        ForegroundColorSpan[] spans;

        public ForegroundColorClickableSpan(ForegroundColorSpan[] foregroundColorSpanArr, NodeMsgHref nodeMsgHref) {
            this.spans = foregroundColorSpanArr;
            this.href = nodeMsgHref;
        }

        public void updateDrawState(@NonNull TextPaint textPaint) {
            ForegroundColorSpan[] foregroundColorSpanArr = this.spans;
            if (foregroundColorSpanArr == null || foregroundColorSpanArr.length <= 0) {
                textPaint.setColor(textPaint.linkColor);
            } else {
                textPaint.setColor(foregroundColorSpanArr[0].getForegroundColor());
            }
            textPaint.setUnderlineText(true);
        }

        public void onClick(@NonNull View view) {
            OnClickAddonListener onClickAddonListener = MessageAddonView.this.getOnClickAddonListener();
            if (onClickAddonListener != null) {
                onClickAddonListener.onClickAddon(this.href);
            }
        }
    }

    public MessageAddonView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageAddonView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_addon, this);
    }

    private void initView() {
        inflateLayout();
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.starredMsgTitleLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_title_linear);
        this.contactLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_linear);
        this.contactName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_name);
        this.groupLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_linear);
        this.groupContact = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_contact);
        this.groupName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_name);
        this.time = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_time);
        this.mPanelAddon = findViewById(C4558R.C4560id.panelAddon);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.avatarView);
        this.mTxtScreenName = (TextView) findViewById(C4558R.C4560id.txtScreenName);
        this.mPanelTitle = findViewById(C4558R.C4560id.panelTitle);
        this.mImgIcon = (ImageView) findViewById(C4558R.C4560id.imgIcon);
        this.mTxtSummary = (TextView) findViewById(C4558R.C4560id.txtSummary);
        this.mTxtBody = (TextView) findViewById(C4558R.C4560id.txtBody);
        this.mTxtFooter = (TextView) findViewById(C4558R.C4560id.txtFooter);
        this.mActionBarLinear = (LinearLayout) findViewById(C4558R.C4560id.addon_action_bar_linear);
        this.mActionBtn1 = (TextView) findViewById(C4558R.C4560id.addon_action_btn1);
        this.mActionBtn2 = (TextView) findViewById(C4558R.C4560id.addon_action_btn2);
        this.mActionBtnMore = (TextView) findViewById(C4558R.C4560id.addon_action_btn_more);
        this.txtStarDes = (TextView) findViewById(C4558R.C4560id.txtStarDes);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        this.mTxtSummary.setMovementMethod(LinkMovementMethod.getInstance());
        this.mTxtBody.setMovementMethod(LinkMovementMethod.getInstance());
        this.mTxtSummary.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                OnShowContextMenuListener onShowContextMenuListener = MessageAddonView.this.getOnShowContextMenuListener();
                if (onShowContextMenuListener != null) {
                    return onShowContextMenuListener.onShowContextMenu(view, MessageAddonView.this.mMessageItem);
                }
                return false;
            }
        });
        this.mTxtBody.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                OnShowContextMenuListener onShowContextMenuListener = MessageAddonView.this.getOnShowContextMenuListener();
                if (onShowContextMenuListener != null) {
                    return onShowContextMenuListener.onShowContextMenu(view, MessageAddonView.this.mMessageItem);
                }
                return false;
            }
        });
        View view = this.mPanelAddon;
        if (view != null) {
            view.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnShowContextMenuListener onShowContextMenuListener = MessageAddonView.this.getOnShowContextMenuListener();
                    if (onShowContextMenuListener != null) {
                        return onShowContextMenuListener.onShowContextMenu(view, MessageAddonView.this.mMessageItem);
                    }
                    return false;
                }
            });
            this.mPanelAddon.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessageAddonView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessageAddonView.this.mMessageItem);
                    }
                }
            });
        }
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageAddonView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageAddonView.this.mMessageItem);
                    }
                }
            });
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

    public void setStarredMessage(MMMessageItem mMMessageItem) {
        if (mMMessageItem.hideStarView) {
            this.mTxtBody.setFocusable(false);
            this.mTxtSummary.setFocusable(false);
            this.mTxtBody.setClickable(false);
            this.mTxtSummary.setClickable(false);
            this.mTxtSummary.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessageAddonView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessageAddonView.this.mMessageItem);
                    }
                }
            });
            this.mTxtBody.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickMessageListener onClickMessageListener = MessageAddonView.this.getOnClickMessageListener();
                    if (onClickMessageListener != null) {
                        onClickMessageListener.onClickMessage(MessageAddonView.this.mMessageItem);
                    }
                }
            });
            this.starredMsgTitleLinear.setVisibility(0);
            this.mTxtScreenName.setVisibility(8);
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
        setReactionLabels(mMMessageItem);
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
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setVisibility(8);
            }
        } else {
            AvatarView avatarView3 = this.mAvatarView;
            if (avatarView3 != null) {
                avatarView3.setVisibility(0);
            }
            TextView textView2 = this.mTxtScreenName;
            if (textView2 != null) {
                textView2.setVisibility(0);
            }
        }
        MMAddonMessage mMAddonMessage = mMMessageItem.addonMsg;
        if (mMAddonMessage != null) {
            int i = C4558R.C4559drawable.zm_msg_github_title_bg_normal;
            int i2 = C4558R.C4559drawable.zm_msg_addon_action_btn_jira_bg;
            switch (mMAddonMessage.getType()) {
                case 1:
                    i = this.mMessageItem.onlyMessageShow ? C4558R.C4559drawable.zm_msg_jira_title_bg_normal : C4558R.C4559drawable.zm_msg_jira_title_bg_top;
                    int i3 = C4558R.C4559drawable.zm_msg_addon_action_btn_jira_bg;
                    break;
                case 2:
                    i = this.mMessageItem.onlyMessageShow ? C4558R.C4559drawable.zm_msg_github_title_bg_normal : C4558R.C4559drawable.zm_msg_github_title_bg_top;
                    int i4 = C4558R.C4559drawable.zm_msg_addon_action_btn_github_bg;
                    break;
                case 3:
                    i = this.mMessageItem.onlyMessageShow ? C4558R.C4559drawable.zm_msg_gitlab_title_bg_normal : C4558R.C4559drawable.zm_msg_gitlab_title_bg_top;
                    int i5 = C4558R.C4559drawable.zm_msg_addon_action_btn_gitlab_bg;
                    break;
            }
            this.mPanelTitle.setBackgroundResource(i);
            setTitle(mMAddonMessage.getType(), mMAddonMessage.getTitleIcon(), mMAddonMessage.getTitleImg(), mMAddonMessage.getTitle());
            setRichText(mMAddonMessage.getBody(), this.mTxtBody);
            setRichText(mMAddonMessage.getSummary(), this.mTxtSummary);
            setFooter(mMAddonMessage.getBody());
            setAction(mMAddonMessage.getAction());
            setStarredMessage(mMMessageItem);
        }
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

    public void setReactionLabels(MMMessageItem mMMessageItem) {
        if (!(mMMessageItem == null || this.mReactionLabels == null)) {
            if (mMMessageItem.hideStarView) {
                this.mReactionLabels.setVisibility(8);
                return;
            }
            this.mReactionLabels.setLabels(mMMessageItem, getOnClickReactionLabelListener());
        }
    }

    public void setAction(final List<AddonNode> list) {
        if (list == null || list.size() <= 0) {
            this.mActionBarLinear.setVisibility(8);
            return;
        }
        this.mActionBarLinear.setVisibility(0);
        if (list.get(0) instanceof ActionNode) {
            final ActionNode actionNode = (ActionNode) list.get(0);
            this.mActionBtn1.setText(actionNode == null ? "" : actionNode.getValue());
            this.mActionBtn1.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickActionListener onClickActionListener = MessageAddonView.this.getmOnClickActionListener();
                    if (onClickActionListener != null && actionNode != null) {
                        onClickActionListener.onClickAction(MessageAddonView.this.mMessageItem.fromJid, actionNode.getAction());
                    }
                }
            });
        }
        if (list.size() > 1 && (list.get(1) instanceof ActionNode)) {
            final ActionNode actionNode2 = (ActionNode) list.get(1);
            this.mActionBtn2.setText(actionNode2 == null ? "" : actionNode2.getValue());
            this.mActionBtn2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickActionListener onClickActionListener = MessageAddonView.this.getmOnClickActionListener();
                    if (onClickActionListener != null && actionNode2 != null) {
                        onClickActionListener.onClickAction(MessageAddonView.this.mMessageItem.fromJid, actionNode2.getAction());
                    }
                }
            });
        }
        if (list.size() == 1) {
            this.mActionBtnMore.setVisibility(8);
            this.mActionBtn1.setVisibility(0);
            this.mActionBtn2.setVisibility(8);
        } else if (list.size() == 2) {
            this.mActionBtnMore.setVisibility(8);
            this.mActionBtn1.setVisibility(0);
            this.mActionBtn2.setVisibility(0);
        } else {
            this.mActionBtnMore.setVisibility(0);
            this.mActionBtn1.setVisibility(0);
            this.mActionBtn2.setVisibility(8);
            this.mActionBtnMore.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickActionMoreListener onClickActionMoreListener = MessageAddonView.this.getmOnClickActionMoreListener();
                    if (onClickActionMoreListener != null) {
                        onClickActionMoreListener.onClickActionMore(MessageAddonView.this.mMessageItem.fromJid, list);
                    }
                }
            });
        }
    }

    public void setRichText(@Nullable List<AddonNode> list, @Nullable TextView textView) {
        if (list == null || textView == null) {
            if (textView != null) {
                textView.setText("");
            }
            return;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (AddonNode addonNode : list) {
            if (addonNode instanceof NodeMsgHref) {
                NodeMsgHref nodeMsgHref = (NodeMsgHref) addonNode;
                SpannableString spannableString = new SpannableString(addonNode.getValue());
                parseNodeStyle(addonNode, spannableString);
                spannableString.setSpan(new ForegroundColorClickableSpan((ForegroundColorSpan[]) spannableString.getSpans(0, spannableString.length(), ForegroundColorSpan.class), nodeMsgHref), 0, spannableString.length(), 33);
                spannableStringBuilder.append(spannableString);
            } else if (addonNode instanceof NodeBR) {
                spannableStringBuilder.append(FontStyleHelper.SPLITOR);
            } else if ((addonNode instanceof NodeLabel) || (addonNode instanceof NodeP)) {
                SpannableString parseNodeStyle = parseNodeStyle(addonNode);
                if (parseNodeStyle != null) {
                    spannableStringBuilder.append(parseNodeStyle);
                }
            }
        }
        textView.setText(spannableStringBuilder);
    }

    @Nullable
    private SpannableString parseNodeStyle(AddonNode addonNode) {
        return parseNodeStyle(addonNode, null);
    }

    @Nullable
    private SpannableString parseNodeStyle(@Nullable AddonNode addonNode, @Nullable SpannableString spannableString) {
        if (addonNode == null) {
            return null;
        }
        if (spannableString == null) {
            spannableString = new SpannableString(addonNode.getValue());
        }
        NamedNodeMap attrs = addonNode.getAttrs();
        if (attrs != null) {
            Node namedItem = attrs.getNamedItem("style");
            if (namedItem != null) {
                Map parseStyleMsg = parseStyleMsg(namedItem.getNodeValue());
                String str = (String) parseStyleMsg.get("color");
                String str2 = (String) parseStyleMsg.get("font-weight");
                if (!TextUtils.isEmpty(str)) {
                    try {
                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(str)), 0, spannableString.length(), 33);
                    } catch (Exception e) {
                        if ("orange".equalsIgnoreCase(str)) {
                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), 0, spannableString.length(), 33);
                        }
                        Log.e(MessageAddonView.class.getSimpleName(), e.getMessage(), e);
                    }
                }
                if (!TextUtils.isEmpty(str2)) {
                    StyleSpan styleSpan = new StyleSpan(0);
                    if (str2.equalsIgnoreCase("bold")) {
                        styleSpan = new StyleSpan(1);
                    } else if (str2.equalsIgnoreCase("ITALIC")) {
                        styleSpan = new StyleSpan(2);
                    } else if (str2.equalsIgnoreCase("BOLD_ITALIC")) {
                        styleSpan = new StyleSpan(3);
                    }
                    spannableString.setSpan(styleSpan, 0, spannableString.length(), 33);
                }
            }
        }
        return spannableString;
    }

    @NonNull
    private Map<String, String> parseStyleMsg(@Nullable String str) {
        HashMap hashMap = new HashMap();
        if (TextUtils.isEmpty(str)) {
            return hashMap;
        }
        String[] split = str.split(";");
        if (split.length > 0) {
            for (String split2 : split) {
                String[] split3 = split2.split(":");
                if (split3.length == 2) {
                    hashMap.put(split3[0], split3[1]);
                }
            }
        }
        return hashMap;
    }

    public void setFooter(@Nullable List<AddonNode> list) {
        if (this.mTxtFooter != null) {
            if (list == null || list.isEmpty()) {
                this.mTxtFooter.setText("");
                this.mTxtFooter.setVisibility(8);
                return;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            for (AddonNode addonNode : list) {
                if (addonNode instanceof NodeFooter) {
                    SpannableString parseNodeStyle = parseNodeStyle(addonNode);
                    if (parseNodeStyle != null) {
                        spannableStringBuilder.append(parseNodeStyle);
                    }
                } else if ((addonNode instanceof NodeBR) && spannableStringBuilder.length() > 0) {
                    spannableStringBuilder.append(FontStyleHelper.SPLITOR);
                }
            }
            if (spannableStringBuilder.length() > 0) {
                this.mTxtFooter.setVisibility(0);
                this.mTxtFooter.setText(spannableStringBuilder);
            } else {
                this.mTxtFooter.setText("");
                this.mTxtFooter.setVisibility(8);
            }
        }
    }

    public void setTitle(int i, int i2, @Nullable String str, @Nullable List<AddonNode> list) {
        ImageView imageView = this.mImgIcon;
        if (imageView != null) {
            if (i > 0) {
                imageView.setVisibility(0);
                this.mImgIcon.setImageResource(i2);
            } else {
                imageView.setVisibility(8);
                if (!TextUtils.isEmpty(str)) {
                    this.mImgIcon.setVisibility(0);
                    this.mImgIcon.setImageBitmap(null);
                    ImageLoader.getInstance().displayImage(this.mImgIcon, str);
                }
            }
        }
        ((LinearLayout) findViewById(C4558R.C4560id.zm_msg_addon_title_linear)).removeAllViews();
        if (list != null) {
            for (AddonNode addonNode : list) {
                if (addonNode instanceof NodeLabel) {
                    SpannableString parseNodeStyle = parseNodeStyle(addonNode);
                    if (i > 0) {
                        addTitleLabel(parseNodeStyle, C4558R.color.zm_text_on_dark);
                    } else {
                        addTitleLabel(parseNodeStyle, C4558R.color.zm_addon_title_label_bg);
                    }
                } else if (addonNode instanceof NodeMsgHref) {
                    SpannableString parseNodeStyle2 = parseNodeStyle(addonNode);
                    if (i > 0) {
                        addTitleHref(parseNodeStyle2, (NodeMsgHref) addonNode, C4558R.color.zm_text_on_dark);
                    } else {
                        addTitleHref(parseNodeStyle2, (NodeMsgHref) addonNode, C4558R.color.zm_addon_title_label_bg);
                    }
                }
            }
        }
    }

    @NonNull
    private TextView addTitleLabel(SpannableString spannableString, int i) {
        TextView textView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.rightMargin = UIUtil.dip2px(getContext(), 5.0f);
        textView.setLayoutParams(layoutParams);
        textView.setSingleLine();
        textView.setEllipsize(TruncateAt.END);
        textView.setTextColor(getResources().getColor(i));
        textView.setText(spannableString);
        ((LinearLayout) findViewById(C4558R.C4560id.zm_msg_addon_title_linear)).addView(textView);
        return textView;
    }

    private void addTitleHref(SpannableString spannableString, NodeMsgHref nodeMsgHref, int i) {
        TextView addTitleLabel = addTitleLabel(spannableString, i);
        addTitleLabel.setTag(nodeMsgHref);
        addTitleLabel.setOnClickListener(new OnClickListener() {
            public void onClick(@NonNull View view) {
                OnClickAddonListener onClickAddonListener = MessageAddonView.this.getOnClickAddonListener();
                if (onClickAddonListener != null) {
                    onClickAddonListener.onClickAddon((NodeMsgHref) view.getTag());
                }
            }
        });
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mTxtScreenName;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }
}
