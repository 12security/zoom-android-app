package com.zipow.videobox.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.videomeetings.C4558R;

public class FloatingEmojis {
    private Builder mBuilder;
    private FloatingEmojisView mFloatingEmojisView;
    private FrameLayout mFloatingEmojisWrapp;

    public static class Builder {
        private Activity activity;
        private List<Drawable> drawables = new ArrayList();

        public Builder(Activity activity2) {
            this.activity = activity2;
        }

        public Activity getActivity() {
            return this.activity;
        }

        public List<Drawable> getDrawables() {
            return this.drawables;
        }

        public Builder addEmoji(@DrawableRes int i) {
            this.drawables.add(ContextCompat.getDrawable(this.activity, i));
            return this;
        }

        public Builder addEmoji(Drawable drawable) {
            this.drawables.add(drawable);
            return this;
        }

        public FloatingEmojis build() {
            if (this.activity != null) {
                List<Drawable> list = this.drawables;
                if (list != null && !list.isEmpty()) {
                    return new FloatingEmojis(this);
                }
                throw new RuntimeException("no emojis!");
            }
            throw new RuntimeException("activity is null!");
        }
    }

    public FloatingEmojis(Builder builder) {
        this.mBuilder = builder;
    }

    public FloatingEmojisView attachToWindow() {
        ViewGroup viewGroup = (ViewGroup) this.mBuilder.getActivity().findViewById(16908290);
        this.mFloatingEmojisWrapp = (FrameLayout) this.mBuilder.getActivity().findViewById(C4558R.C4560id.floatingEmojisViewWrapper);
        if (this.mFloatingEmojisWrapp == null) {
            this.mFloatingEmojisWrapp = new FrameLayout(this.mBuilder.getActivity());
            this.mFloatingEmojisWrapp.setId(C4558R.C4560id.floatingEmojisViewWrapper);
            viewGroup.addView(this.mFloatingEmojisWrapp);
        }
        this.mFloatingEmojisView = new FloatingEmojisView(this.mBuilder.getActivity());
        this.mFloatingEmojisWrapp.bringToFront();
        this.mFloatingEmojisWrapp.addView(this.mFloatingEmojisView, new LayoutParams(-1, -1));
        Builder builder = this.mBuilder;
        if (!(builder == null || builder.getDrawables() == null)) {
            for (Drawable addEmoji : this.mBuilder.getDrawables()) {
                this.mFloatingEmojisView.addEmoji(addEmoji);
            }
        }
        return this.mFloatingEmojisView;
    }

    public void detachFromWidow() {
        FloatingEmojisView floatingEmojisView = this.mFloatingEmojisView;
        if (floatingEmojisView != null && this.mBuilder != null) {
            floatingEmojisView.stopAnimation();
            this.mFloatingEmojisView.clearEmojis();
            ViewGroup viewGroup = (ViewGroup) this.mBuilder.getActivity().findViewById(16908290);
            viewGroup.removeView(this.mFloatingEmojisView);
            viewGroup.removeView(this.mFloatingEmojisWrapp);
            this.mFloatingEmojisWrapp = null;
            this.mFloatingEmojisView = null;
        }
    }

    public void startEmojiRain() {
        this.mFloatingEmojisView.startAnimation();
    }
}
