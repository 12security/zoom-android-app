package com.zipow.videobox.markdown;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import p021us.zoom.videomeetings.C4558R;

public class RoundedSpanBgTextView extends AppCompatTextView {
    private int horizontalPadding = 0;
    @Nullable
    private Drawable mDrawable;
    @Nullable
    private Drawable mDrawableLeft;
    @Nullable
    private Drawable mDrawableMid;
    @Nullable
    private Drawable mDrawableRight;
    private int verticalPadding = 0;

    public RoundedSpanBgTextView(@NonNull Context context) {
        super(context);
        initViews(context);
    }

    public RoundedSpanBgTextView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context);
    }

    public RoundedSpanBgTextView(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    private void initViews(@NonNull Context context) {
        this.mDrawable = ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_monospace_bg);
        this.mDrawableLeft = ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_monospace_left_bg);
        this.mDrawableMid = ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_monospace_mid_bg);
        this.mDrawableRight = ContextCompat.getDrawable(context, C4558R.C4559drawable.zm_monospace_right_bg);
    }

    /* access modifiers changed from: protected */
    public void onDraw(@NonNull Canvas canvas) {
        CharSequence text = getText();
        if (text instanceof Spannable) {
            canvas.save();
            canvas.translate((float) getTotalPaddingLeft(), (float) getTotalPaddingTop());
            draw(canvas, (Spanned) text, getLayout());
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    /* JADX WARNING: type inference failed for: r2v9, types: [com.zipow.videobox.markdown.SingleLineRenderer] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void draw(android.graphics.Canvas r20, android.text.Spanned r21, @androidx.annotation.NonNull android.text.Layout r22) {
        /*
            r19 = this;
            r0 = r19
            r1 = r21
            r9 = r22
            int r2 = r21.length()
            java.lang.Class<com.zipow.videobox.markdown.BackgroundImageSpan> r3 = com.zipow.videobox.markdown.BackgroundImageSpan.class
            r4 = 0
            java.lang.Object[] r2 = r1.getSpans(r4, r2, r3)
            r10 = r2
            com.zipow.videobox.markdown.BackgroundImageSpan[] r10 = (com.zipow.videobox.markdown.BackgroundImageSpan[]) r10
            if (r10 == 0) goto L_0x0079
            int r2 = r10.length
            if (r2 <= 0) goto L_0x0079
            int r11 = r10.length
            r12 = 0
        L_0x001b:
            if (r12 >= r11) goto L_0x0079
            r2 = r10[r12]
            int r3 = r1.getSpanStart(r2)
            int r2 = r1.getSpanEnd(r2)
            int r5 = r9.getLineForOffset(r3)
            int r6 = r9.getLineForOffset(r2)
            float r3 = r9.getPrimaryHorizontal(r3)
            int r4 = r9.getParagraphDirection(r5)
            int r4 = r4 * -1
            int r7 = r0.horizontalPadding
            int r4 = r4 * r7
            float r4 = (float) r4
            float r3 = r3 + r4
            int r7 = (int) r3
            float r2 = r9.getPrimaryHorizontal(r2)
            int r3 = r9.getParagraphDirection(r6)
            int r14 = r0.horizontalPadding
            int r3 = r3 * r14
            float r3 = (float) r3
            float r2 = r2 + r3
            int r8 = (int) r2
            if (r5 != r6) goto L_0x005b
            com.zipow.videobox.markdown.SingleLineRenderer r2 = new com.zipow.videobox.markdown.SingleLineRenderer
            int r3 = r0.verticalPadding
            android.graphics.drawable.Drawable r4 = r0.mDrawable
            r2.<init>(r14, r3, r4)
            goto L_0x006f
        L_0x005b:
            com.zipow.videobox.markdown.MultiLineRenderer r2 = new com.zipow.videobox.markdown.MultiLineRenderer
            int r15 = r0.verticalPadding
            android.graphics.drawable.Drawable r3 = r0.mDrawableLeft
            android.graphics.drawable.Drawable r4 = r0.mDrawableMid
            android.graphics.drawable.Drawable r13 = r0.mDrawableRight
            r18 = r13
            r13 = r2
            r16 = r3
            r17 = r4
            r13.<init>(r14, r15, r16, r17, r18)
        L_0x006f:
            r3 = r20
            r4 = r22
            r2.draw(r3, r4, r5, r6, r7, r8)
            int r12 = r12 + 1
            goto L_0x001b
        L_0x0079:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.markdown.RoundedSpanBgTextView.draw(android.graphics.Canvas, android.text.Spanned, android.text.Layout):void");
    }
}
