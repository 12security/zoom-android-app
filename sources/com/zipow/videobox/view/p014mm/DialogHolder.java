package com.zipow.videobox.view.p014mm;

import android.content.Context;
import com.zipow.videobox.view.p014mm.ReactionEmojiDialog.OnReactionEmojiListener;
import p021us.zoom.androidlib.C4409R;

/* renamed from: com.zipow.videobox.view.mm.DialogHolder */
/* compiled from: ReactionEmojiDialog */
class DialogHolder {
    private int anchorHeight;
    private int anchorOffsetY;
    private boolean cancelable;
    private Context context;
    private Object data;
    private ReactionEmojiDialog dialog;
    private OnReactionEmojiListener reactionEmojiListener;
    private int remainRange;
    private int theme = C4409R.style.ZMDialog_Material_Transparent;
    private int titleHeight;

    DialogHolder(Context context2) {
        this.context = context2;
        this.cancelable = true;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public void setCancelable(boolean z) {
        this.cancelable = z;
    }

    public boolean isCancelable() {
        return this.cancelable;
    }

    public int getTheme() {
        return this.theme;
    }

    public void setTheme(int i) {
        this.theme = i;
    }

    public void setDialog(ReactionEmojiDialog reactionEmojiDialog) {
        this.dialog = reactionEmojiDialog;
    }

    public ReactionEmojiDialog getDialog() {
        return this.dialog;
    }

    public int getAnchorOffsetY() {
        return this.anchorOffsetY;
    }

    public void setAnchorOffsetY(int i) {
        this.anchorOffsetY = i;
    }

    public int getAnchorHeight() {
        return this.anchorHeight;
    }

    public void setAnchorHeight(int i) {
        this.anchorHeight = i;
    }

    public int getTitleHeight() {
        return this.titleHeight;
    }

    public void setTitleHeight(int i) {
        this.titleHeight = i;
    }

    public int getRemainRange() {
        return this.remainRange;
    }

    public void setRemainRange(int i) {
        this.remainRange = i;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public OnReactionEmojiListener getReactionEmojiListener() {
        return this.reactionEmojiListener;
    }

    public void setAnchor(int i, int i2, int i3, int i4, OnReactionEmojiListener onReactionEmojiListener) {
        this.anchorOffsetY = i;
        this.anchorHeight = i2;
        this.titleHeight = i3;
        this.remainRange = i4;
        this.reactionEmojiListener = onReactionEmojiListener;
    }
}
