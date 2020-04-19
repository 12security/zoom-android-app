package com.zipow.videobox.view.floatingtext;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import p021us.zoom.videomeetings.C4558R;

public class FloatingText {
    private FrameLayout floatingTextWrapper;
    private Builder mBuilder;
    private FloatingTextView mFloatingTextView;

    public static class Builder {
        private Activity activity;
        private String text;
        private int windowOffset;

        public Builder(Activity activity2) {
            this.activity = activity2;
        }

        public Activity getActivity() {
            return this.activity;
        }

        public Builder setText(String str) {
            this.text = str;
            return this;
        }

        public String getText() {
            return this.text;
        }

        public int getWindowOffset() {
            return this.windowOffset;
        }

        public Builder setWindowOffset(int i) {
            this.windowOffset = i;
            return this;
        }

        public FloatingText build() {
            if (this.activity == null) {
                throw new RuntimeException("activity is null!");
            } else if (!TextUtils.isEmpty(this.text)) {
                return new FloatingText(this);
            } else {
                throw new RuntimeException("text is empty!");
            }
        }
    }

    public FloatingText(Builder builder) {
        this.mBuilder = builder;
    }

    public FloatingTextView attachToWindow() {
        ViewGroup viewGroup = (ViewGroup) this.mBuilder.getActivity().findViewById(16908290);
        this.floatingTextWrapper = (FrameLayout) this.mBuilder.getActivity().findViewById(C4558R.C4560id.floatingViewWrapper);
        if (this.floatingTextWrapper == null) {
            this.floatingTextWrapper = new FrameLayout(this.mBuilder.getActivity());
            this.floatingTextWrapper.setId(C4558R.C4560id.floatingViewWrapper);
            viewGroup.addView(this.floatingTextWrapper);
        }
        this.mFloatingTextView = new FloatingTextView(this.mBuilder.getActivity());
        this.floatingTextWrapper.bringToFront();
        this.floatingTextWrapper.addView(this.mFloatingTextView, new LayoutParams(-2, -2));
        this.mFloatingTextView.setFloatingTextBuilder(this.mBuilder);
        return this.mFloatingTextView;
    }

    public void detachFromWidow() {
        if (this.mFloatingTextView != null) {
            Builder builder = this.mBuilder;
            if (builder != null) {
                ((ViewGroup) builder.getActivity().findViewById(16908290)).removeView(this.mFloatingTextView);
            }
        }
    }

    public void startFloating(View view) {
        this.mFloatingTextView.starAnimation(view);
    }
}
