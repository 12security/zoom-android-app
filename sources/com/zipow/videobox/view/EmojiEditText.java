package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper;
import com.zipow.videobox.view.p014mm.sticker.CommonEmojiHelper.OnEmojiPackageInstallListener;
import p021us.zoom.androidlib.widget.ZMMaterialEditText;

public class EmojiEditText extends ZMMaterialEditText implements OnEmojiPackageInstallListener {
    private void init() {
    }

    public void onEmojiPkgDownload(int i) {
    }

    public void onEmojiPkgDownloadFailed() {
    }

    public EmojiEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public EmojiEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public EmojiEditText(Context context) {
        super(context);
        init();
    }

    private void formatSelection() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getText());
        ImageSpan[] imageSpanArr = (ImageSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        if (imageSpanArr != null && imageSpanArr.length > 0) {
            for (ImageSpan imageSpan : imageSpanArr) {
                int spanStart = spannableStringBuilder.getSpanStart(imageSpan);
                int spanEnd = spannableStringBuilder.getSpanEnd(imageSpan);
                if (selectionStart > spanStart && selectionStart < spanEnd) {
                    selectionStart = spanStart;
                }
                if (selectionEnd > spanStart && selectionEnd < spanEnd) {
                    selectionEnd = spanEnd;
                }
            }
            setSelection(selectionStart, selectionEnd);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        CommonEmojiHelper.getInstance().addListener(this);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        CommonEmojiHelper.getInstance().removeListener(this);
        super.onDetachedFromWindow();
    }

    public boolean onTextContextMenuItem(int i) {
        int selectionEnd = getSelectionEnd();
        int length = getText().length();
        switch (i) {
            case 16908321:
                formatSelection();
                return super.onTextContextMenuItem(i);
            case 16908322:
                formatSelection();
                boolean onTextContextMenuItem = super.onTextContextMenuItem(i);
                setText(CommonEmojiHelper.getInstance().formatImgEmojiSize(getTextSize(), getText(), true));
                setSelection((selectionEnd + getText().length()) - length);
                return onTextContextMenuItem;
            default:
                return super.onTextContextMenuItem(i);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void onEmojiPkgInstalled() {
        int selectionEnd = getSelectionEnd();
        int length = getText().length();
        formatSelection();
        setText(CommonEmojiHelper.getInstance().formatImgEmojiSize(getTextSize(), getText(), true));
        int length2 = (selectionEnd + getText().length()) - length;
        if (length2 < 0) {
            length2 = 0;
        }
        setSelection(length2);
    }
}
