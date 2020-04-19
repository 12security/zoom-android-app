package com.zipow.videobox.view.sip.sms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.LinkPreviewMetaInfo;

public abstract class AbsSmsView extends LinearLayout {
    private OnShowContextMenuListener mMessageOnShowContextMenuListener;
    private OnClickLinkPreviewListener mOnClickLinkPreviewListener;
    private OnClickMeetingNOListener mOnClickMeetingNOListener;
    private OnClickMessageListener mOnClickMessageListener;
    private OnClickStatusImageListener mOnClickStatusImageListener;

    public interface OnClickLinkPreviewListener {
        void onClickLinkPreview(PBXMessageItem pBXMessageItem, LinkPreviewMetaInfo linkPreviewMetaInfo);
    }

    public interface OnClickMeetingNOListener {
        void onClickMeetingNO(String str);
    }

    public interface OnClickMessageListener {
        void onClickMessage(PBXMessageItem pBXMessageItem);
    }

    public interface OnClickStatusImageListener {
        void onClickStatusImage(PBXMessageItem pBXMessageItem);
    }

    public interface OnShowContextMenuListener {
        boolean onShowContextMenu(View view, PBXMessageItem pBXMessageItem);

        boolean onShowLinkContextMenu(View view, PBXMessageItem pBXMessageItem, String str);
    }

    @Nullable
    public abstract PBXMessageItem getSmsItem();

    public abstract void setSmsItem(@NonNull PBXMessageItem pBXMessageItem);

    public OnClickLinkPreviewListener getOnClickLinkPreviewListener() {
        return this.mOnClickLinkPreviewListener;
    }

    public void setOnClickLinkPreviewListener(OnClickLinkPreviewListener onClickLinkPreviewListener) {
        this.mOnClickLinkPreviewListener = onClickLinkPreviewListener;
    }

    @SuppressLint({"NewApi"})
    public AbsSmsView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public AbsSmsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AbsSmsView(Context context) {
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

    public OnClickMeetingNOListener getOnClickMeetingNOListener() {
        return this.mOnClickMeetingNOListener;
    }

    public void setOnClickMeetingNOListener(OnClickMeetingNOListener onClickMeetingNOListener) {
        this.mOnClickMeetingNOListener = onClickMeetingNOListener;
    }
}
