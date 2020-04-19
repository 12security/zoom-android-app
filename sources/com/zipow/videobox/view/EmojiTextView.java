package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView.BufferType;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper.OnEmojiPackageInstallListener;
import p021us.zoom.androidlib.widget.ZMTextView;

public class EmojiTextView extends ZMTextView implements OnEmojiPackageInstallListener {
    private void init() {
    }

    public void onEmojiPkgDownload(int i) {
    }

    public void onEmojiPkgDownloadFailed() {
    }

    public EmojiTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public EmojiTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public EmojiTextView(Context context) {
        super(context);
        init();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        CommonEmojiHelper.getInstance().addListener(this);
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        CommonEmojiHelper.getInstance().removeListener(this);
        super.onDetachedFromWindow();
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(CommonEmojiHelper.getInstance().formatImgEmojiSize(getTextSize(), charSequence, false), bufferType);
    }

    public void onEmojiPkgInstalled() {
        super.setText(CommonEmojiHelper.getInstance().formatImgEmojiSize(getTextSize(), getText(), true));
    }
}
