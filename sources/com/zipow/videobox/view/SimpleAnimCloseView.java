package com.zipow.videobox.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.StringRes;
import p021us.zoom.videomeetings.C4558R;

public class SimpleAnimCloseView extends LinearLayout {
    private int mCloseWidth;
    /* access modifiers changed from: private */
    public int mOpenWidth;
    private CharSequence mText;
    private TextView mTextView;

    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View view) {
            this.mTarget = view;
        }

        public void setTrueWidth(int i) {
            this.mTarget.getLayoutParams().width = i;
            this.mTarget.requestLayout();
        }

        public int getTrueWidth() {
            return this.mTarget.getLayoutParams().width;
        }
    }

    public SimpleAnimCloseView(Context context) {
        super(context);
        init();
    }

    public SimpleAnimCloseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public SimpleAnimCloseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        inflate(getContext(), C4558R.layout.zm_simple_anim_close_view, this);
        this.mTextView = (TextView) findViewById(C4558R.C4560id.btnClose);
        this.mTextView.setText(this.mText);
        this.mCloseWidth = getResources().getDimensionPixelSize(C4558R.dimen.zm_simple_anim_close_size);
        this.mOpenWidth = getResources().getDimensionPixelSize(C4558R.dimen.zm_simple_anim_close_open_width);
    }

    public void initClose() {
        ((LayoutParams) this.mTextView.getLayoutParams()).width = this.mCloseWidth;
        requestLayout();
    }

    public void initOpen() {
        ((LayoutParams) this.mTextView.getLayoutParams()).width = -2;
        requestLayout();
        post(new Runnable() {
            public void run() {
                SimpleAnimCloseView simpleAnimCloseView = SimpleAnimCloseView.this;
                simpleAnimCloseView.mOpenWidth = simpleAnimCloseView.getWidth();
            }
        });
    }

    private void close() {
        ObjectAnimator duration = ObjectAnimator.ofInt(new ViewWrapper(this), "trueWidth", new int[]{this.mOpenWidth, this.mCloseWidth}).setDuration(300);
        duration.setInterpolator(new AccelerateInterpolator());
        duration.start();
    }

    private void open() {
        ObjectAnimator duration = ObjectAnimator.ofInt(new ViewWrapper(this), "trueWidth", new int[]{this.mCloseWidth, this.mOpenWidth}).setDuration(300);
        duration.setInterpolator(new AccelerateInterpolator());
        duration.start();
    }

    private boolean isClosed() {
        return getWidth() == this.mCloseWidth;
    }

    private boolean isAnimating() {
        int width = getWidth();
        return width > this.mCloseWidth && width < this.mOpenWidth;
    }

    public void setText(CharSequence charSequence) {
        this.mText = charSequence;
        TextView textView = this.mTextView;
        if (textView != null) {
            textView.setText(this.mText);
        }
    }

    public void setText(@StringRes int i) {
        setText((CharSequence) getContext().getString(i));
    }

    public void startToClose() {
        if (!isAnimating() && !isClosed()) {
            close();
        }
    }
}
