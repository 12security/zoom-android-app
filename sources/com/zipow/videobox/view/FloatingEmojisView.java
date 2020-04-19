package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pools.SynchronizedPool;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableSource;
import p015io.reactivex.android.schedulers.AndroidSchedulers;
import p015io.reactivex.disposables.CompositeDisposable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.functions.Function;
import p015io.reactivex.functions.Predicate;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMSecureRandom;

public class FloatingEmojisView extends FrameLayout {
    private static final int ANIMATION_DURATION = 4000;
    private static final int BATCH_INTERVAL_TIME = 1200;
    private static final float DURATION_DIVERSITY_RANGE = 0.25f;
    private static final int EMOJIS_NUM_PER_BATCH = 8;
    private static final int FLOATING_DURATION = 6000;
    private static final String TAG = "FloatingEmojisView";
    /* access modifiers changed from: private */
    @NonNull
    public HashSet<TranslateAnimation> mAnimationPool;
    private int mBatchIntervalTime;
    @NonNull
    private CompositeDisposable mCompositeDisposable;
    @NonNull
    private List<Drawable> mEmojiDrawables;
    /* access modifiers changed from: private */
    public SynchronizedPool<ImageView> mEmojiViewPool;
    private int mFloatingDuration;
    /* access modifiers changed from: private */
    public int mNumPerBatch;
    private int mSingleAnimatioDuration;

    /* access modifiers changed from: private */
    public void onError(Throwable th) {
    }

    public FloatingEmojisView(Context context) {
        this(context, null);
    }

    public FloatingEmojisView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FloatingEmojisView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCompositeDisposable = new CompositeDisposable();
        this.mEmojiDrawables = new ArrayList();
        this.mAnimationPool = new HashSet<>();
        init();
    }

    private void init() {
        this.mBatchIntervalTime = 1200;
        this.mNumPerBatch = 8;
        this.mFloatingDuration = 6000;
        this.mSingleAnimatioDuration = 4000;
    }

    public void addEmoji(Drawable drawable) {
        this.mEmojiDrawables.add(drawable);
    }

    public void addEmoji(@DrawableRes int i) {
        this.mEmojiDrawables.add(ContextCompat.getDrawable(getContext(), i));
    }

    public void clearEmojis() {
        this.mEmojiDrawables.clear();
    }

    public void stopAnimation() {
        this.mCompositeDisposable.clear();
        Iterator it = this.mAnimationPool.iterator();
        while (it.hasNext()) {
            ((TranslateAnimation) it.next()).cancel();
        }
        this.mAnimationPool.clear();
        removeAllViews();
    }

    public void startAnimation() {
        initEmojisPool();
        try {
            this.mCompositeDisposable.add(Observable.interval((long) this.mBatchIntervalTime, TimeUnit.MILLISECONDS).take((long) (this.mFloatingDuration / this.mBatchIntervalTime)).flatMap(new Function<Long, ObservableSource<?>>() {
                public ObservableSource<?> apply(Long l) throws Exception {
                    return Observable.range(0, FloatingEmojisView.this.mNumPerBatch);
                }
            }).map(new Function<Object, ImageView>() {
                public ImageView apply(Object obj) throws Exception {
                    return (ImageView) FloatingEmojisView.this.mEmojiViewPool.acquire();
                }
            }).filter(new Predicate<ImageView>() {
                public boolean test(ImageView imageView) throws Exception {
                    return imageView != null;
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ImageView>() {
                public void accept(ImageView imageView) throws Exception {
                    FloatingEmojisView.this.startDropAnimationForSingleEmoji(imageView);
                }
            }, new Consumer<Throwable>() {
                public void accept(Throwable th) throws Exception {
                    FloatingEmojisView.this.onError(th);
                }
            }));
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void startDropAnimationForSingleEmoji(final ImageView imageView) {
        if (imageView != null) {
            final TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, ZMSecureRandom.nextFloatAround(0.0f, 5.0f), 2, 0.0f, 0, (float) (UIUtil.getDisplayHeight(getContext()) + 150));
            translateAnimation.setDuration((long) ((int) (((float) this.mSingleAnimatioDuration) * ZMSecureRandom.nextFloatAround(1.0f, DURATION_DIVERSITY_RANGE))));
            translateAnimation.setStartOffset((long) ZMSecureRandom.nextInt(350));
            translateAnimation.setFillAfter(true);
            translateAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (FloatingEmojisView.this.mEmojiViewPool != null) {
                        FloatingEmojisView.this.mEmojiViewPool.release(imageView);
                    }
                    FloatingEmojisView.this.mAnimationPool.remove(translateAnimation);
                }
            });
            this.mAnimationPool.add(translateAnimation);
            imageView.startAnimation(translateAnimation);
        }
    }

    private void initEmojisPool() {
        int size = this.mEmojiDrawables.size();
        if (size != 0) {
            this.mAnimationPool.clear();
            clearEmojisPool();
            int i = (int) (((((float) this.mNumPerBatch) * 1.25f) * ((float) this.mSingleAnimatioDuration)) / (((float) this.mBatchIntervalTime) * 1.0f));
            this.mEmojiViewPool = new SynchronizedPool<>(i);
            for (int i2 = 0; i2 < i; i2++) {
                ImageView generateEmojiView = generateEmojiView((Drawable) this.mEmojiDrawables.get(i2 % size));
                if (generateEmojiView != null) {
                    addView(generateEmojiView, 0);
                    this.mEmojiViewPool.release(generateEmojiView);
                }
            }
            return;
        }
        throw new IllegalStateException("No emoji at all!");
    }

    private ImageView generateEmojiView(@Nullable Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        int dip2px = UIUtil.dip2px(getContext(), 30.0f);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth > dip2px || intrinsicHeight > dip2px) {
            float f = (float) dip2px;
            float f2 = (float) intrinsicWidth;
            float f3 = f / (f2 * 1.0f);
            float f4 = (float) intrinsicHeight;
            float f5 = f / (1.0f * f4);
            if (f3 <= f5) {
                f5 = f3;
            }
            intrinsicWidth = (int) (f2 * f5);
            intrinsicHeight = (int) (f5 * f4);
        }
        double nextDouble = (ZMSecureRandom.nextDouble() * 0.5d) + 1.0d;
        int i = (int) (((double) intrinsicWidth) * nextDouble);
        int i2 = (int) (((double) intrinsicHeight) * nextDouble);
        LayoutParams layoutParams = new LayoutParams(i, i2);
        layoutParams.leftMargin = ZMSecureRandom.nextIntInRange(i, UIUtil.getDisplayWidth(getContext()) - (i * 2));
        layoutParams.topMargin = -i2;
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawable);
        imageView.setLayoutParams(layoutParams);
        imageView.setElevation(100.0f);
        return imageView;
    }

    private void clearEmojisPool() {
        if (this.mEmojiViewPool != null) {
            while (true) {
                ImageView imageView = (ImageView) this.mEmojiViewPool.acquire();
                if (imageView != null) {
                    removeView(imageView);
                } else {
                    return;
                }
            }
        }
    }
}
