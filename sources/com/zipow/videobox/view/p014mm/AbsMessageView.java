package com.zipow.videobox.view.p014mm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.zipow.videobox.tempbean.IMessageTemplateActionItem;
import com.zipow.videobox.view.p014mm.MMAddonMessage.AddonNode;
import com.zipow.videobox.view.p014mm.MMAddonMessage.NodeMsgHref;
import java.util.List;

/* renamed from: com.zipow.videobox.view.mm.AbsMessageView */
public abstract class AbsMessageView extends LinearLayout {
    private OnShowContextMenuListener mMessageOnShowContextMenuListener;
    private OnClickActionListener mOnClickActionListener;
    private OnClickActionMoreListener mOnClickActionMoreListener;
    private OnClickAddonListener mOnClickAddonListener;
    private OnClickAvatarListener mOnClickAvatarListener;
    private OnClickCancelListener mOnClickCancelListener;
    private OnClickGiphyBtnListener mOnClickGiphyBtnListener;
    private OnClickLinkPreviewListener mOnClickLinkPreviewListener;
    private OnClickMeetingNOListener mOnClickMeetingNOListener;
    private OnClickMessageListener mOnClickMessageListener;
    private OnClickReactionLabelListener mOnClickReactionLabelListener;
    private OnClickStatusImageListener mOnClickStatusImageListener;
    private OnClickTemplateActionMoreListener mOnClickTemplateActionMoreListener;
    private OnClickTemplateListener mOnClickTemplateListener;
    private OnLongClickAvatarListener mOnLongClickAvatarListener;

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickActionListener */
    public interface OnClickActionListener {
        void onClickAction(String str, String str2);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickActionMoreListener */
    public interface OnClickActionMoreListener {
        void onClickActionMore(String str, List<AddonNode> list);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickAddonListener */
    public interface OnClickAddonListener {
        void onClickAddon(NodeMsgHref nodeMsgHref);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickAvatarListener */
    public interface OnClickAvatarListener {
        void onClickAvatar(MMMessageItem mMMessageItem);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickCancelListener */
    public interface OnClickCancelListener {
        void onClickCancel(MMMessageItem mMMessageItem);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickGiphyBtnListener */
    public interface OnClickGiphyBtnListener {
        void onClickGiphyBtn(MMMessageItem mMMessageItem, View view);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickLinkPreviewListener */
    public interface OnClickLinkPreviewListener {
        void onClickLinkPreview(MMMessageItem mMMessageItem, LinkPreviewMetaInfo linkPreviewMetaInfo);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickMeetingNOListener */
    public interface OnClickMeetingNOListener {
        void onClickMeetingNO(String str);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickMessageListener */
    public interface OnClickMessageListener {
        void onClickMessage(MMMessageItem mMMessageItem);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickReactionLabelListener */
    public interface OnClickReactionLabelListener {
        void onClickAddReactionLabel(View view, MMMessageItem mMMessageItem);

        void onClickAddReplyLabel(View view, MMMessageItem mMMessageItem);

        void onClickMoreActionLabel(View view, MMMessageItem mMMessageItem);

        void onClickReactionLabel(View view, MMMessageItem mMMessageItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem, boolean z);

        boolean onLongClickAddReactionLabel(View view, MMMessageItem mMMessageItem);

        boolean onLongClickReactionLabel(View view, MMMessageItem mMMessageItem, MMCommentsEmojiCountItem mMCommentsEmojiCountItem);

        void onMoreReply(View view, MMMessageItem mMMessageItem);

        void onReachReactionLimit();

        void onShowFloatingText(View view, int i, boolean z);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickStatusImageListener */
    public interface OnClickStatusImageListener {
        void onClickStatusImage(MMMessageItem mMMessageItem);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickTemplateActionMoreListener */
    public interface OnClickTemplateActionMoreListener {
        void onClickTemplateActionMore(View view, String str, String str2, List<IMessageTemplateActionItem> list);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnClickTemplateListener */
    public interface OnClickTemplateListener {
        void onClickEditTemplate(String str, String str2, String str3);

        void onClickSelectTemplate(String str, String str2);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnLongClickAvatarListener */
    public interface OnLongClickAvatarListener {
        boolean onLongClickAvatar(MMMessageItem mMMessageItem);
    }

    /* renamed from: com.zipow.videobox.view.mm.AbsMessageView$OnShowContextMenuListener */
    public interface OnShowContextMenuListener {
        boolean onShowContextMenu(View view, MMMessageItem mMMessageItem);

        boolean onShowLinkContextMenu(View view, MMMessageItem mMMessageItem, String str);
    }

    public abstract MMMessageItem getMessageItem();

    public abstract Rect getMessageLocationOnScreen();

    public void reSizeTitleBarForReplyPage() {
    }

    public abstract void setMessageItem(MMMessageItem mMMessageItem);

    public abstract void setMessageItem(MMMessageItem mMMessageItem, boolean z);

    public OnClickLinkPreviewListener getOnClickLinkPreviewListener() {
        return this.mOnClickLinkPreviewListener;
    }

    public void setOnClickLinkPreviewListener(OnClickLinkPreviewListener onClickLinkPreviewListener) {
        this.mOnClickLinkPreviewListener = onClickLinkPreviewListener;
    }

    public OnClickTemplateListener getmOnClickTemplateListener() {
        return this.mOnClickTemplateListener;
    }

    public void setmOnClickTemplateListener(OnClickTemplateListener onClickTemplateListener) {
        this.mOnClickTemplateListener = onClickTemplateListener;
    }

    @SuppressLint({"NewApi"})
    public AbsMessageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public AbsMessageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AbsMessageView(Context context) {
        super(context);
    }

    public void setOnShowContextMenuListener(OnShowContextMenuListener onShowContextMenuListener) {
        this.mMessageOnShowContextMenuListener = onShowContextMenuListener;
    }

    public OnShowContextMenuListener getOnShowContextMenuListener() {
        return this.mMessageOnShowContextMenuListener;
    }

    public void setOnClickMessageListener(OnClickMessageListener onClickMessageListener) {
        this.mOnClickMessageListener = onClickMessageListener;
    }

    public OnClickMessageListener getOnClickMessageListener() {
        return this.mOnClickMessageListener;
    }

    public void setOnClickStatusImageListener(OnClickStatusImageListener onClickStatusImageListener) {
        this.mOnClickStatusImageListener = onClickStatusImageListener;
    }

    public OnClickStatusImageListener getOnClickStatusImageListener() {
        return this.mOnClickStatusImageListener;
    }

    public OnClickAvatarListener getOnClickAvatarListener() {
        return this.mOnClickAvatarListener;
    }

    public void setOnClickAvatarListener(OnClickAvatarListener onClickAvatarListener) {
        this.mOnClickAvatarListener = onClickAvatarListener;
    }

    public OnClickCancelListener getOnClickCancelListenter() {
        return this.mOnClickCancelListener;
    }

    public void setOnClickCancelListenter(OnClickCancelListener onClickCancelListener) {
        this.mOnClickCancelListener = onClickCancelListener;
    }

    public OnLongClickAvatarListener getOnLongClickAvatarListener() {
        return this.mOnLongClickAvatarListener;
    }

    public void setOnLongClickAvatarListener(OnLongClickAvatarListener onLongClickAvatarListener) {
        this.mOnLongClickAvatarListener = onLongClickAvatarListener;
    }

    public OnClickAddonListener getOnClickAddonListener() {
        return this.mOnClickAddonListener;
    }

    public void setOnClickAddonListener(OnClickAddonListener onClickAddonListener) {
        this.mOnClickAddonListener = onClickAddonListener;
    }

    public OnClickMeetingNOListener getOnClickMeetingNOListener() {
        return this.mOnClickMeetingNOListener;
    }

    public void setOnClickMeetingNOListener(OnClickMeetingNOListener onClickMeetingNOListener) {
        this.mOnClickMeetingNOListener = onClickMeetingNOListener;
    }

    public OnClickActionListener getmOnClickActionListener() {
        return this.mOnClickActionListener;
    }

    public void setmOnClickActionListener(OnClickActionListener onClickActionListener) {
        this.mOnClickActionListener = onClickActionListener;
    }

    public OnClickActionMoreListener getmOnClickActionMoreListener() {
        return this.mOnClickActionMoreListener;
    }

    public void setmOnClickActionMoreListener(OnClickActionMoreListener onClickActionMoreListener) {
        this.mOnClickActionMoreListener = onClickActionMoreListener;
    }

    public OnClickTemplateActionMoreListener getmOnClickTemplateActionMoreListener() {
        return this.mOnClickTemplateActionMoreListener;
    }

    public void setmOnClickTemplateActionMoreListener(OnClickTemplateActionMoreListener onClickTemplateActionMoreListener) {
        this.mOnClickTemplateActionMoreListener = onClickTemplateActionMoreListener;
    }

    public OnClickGiphyBtnListener getmOnClickGiphyBtnListener() {
        return this.mOnClickGiphyBtnListener;
    }

    public void setmOnClickGiphyBtnListener(OnClickGiphyBtnListener onClickGiphyBtnListener) {
        this.mOnClickGiphyBtnListener = onClickGiphyBtnListener;
    }

    public OnClickReactionLabelListener getOnClickReactionLabelListener() {
        return this.mOnClickReactionLabelListener;
    }

    public void setOnClickReactionLabelListener(OnClickReactionLabelListener onClickReactionLabelListener) {
        this.mOnClickReactionLabelListener = onClickReactionLabelListener;
    }
}
