package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.google.common.primitives.Ints;
import com.zipow.videobox.util.EmojiHelper;
import com.zipow.videobox.util.EmojiHelper.EmojiIndex;
import com.zipow.videobox.util.PreferenceUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.EmojiInputView */
public class EmojiInputView extends LinearLayout {
    private static final int EMOJI_CELL_HEIGHT_DP = 50;
    private EditText mEmojiInputEditText;
    private ZMViewPager mEmojiPager;
    private int mKeyboardHeight;
    private LinearLayout mPanelIndicator;

    /* renamed from: com.zipow.videobox.view.mm.EmojiInputView$EmojiAdapter */
    private static class EmojiAdapter extends PagerAdapter implements OnClickListener {
        private Context mContext;
        /* access modifiers changed from: private */
        public EmojiInputView mEmojiInputView;
        private List<EmojiIndex> mEmojis;
        @NonNull
        private Map<Integer, View> mPanelEmojis = new HashMap();

        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        public EmojiAdapter(Context context, List<EmojiIndex> list, EmojiInputView emojiInputView) {
            this.mContext = context;
            this.mEmojis = list;
            this.mEmojiInputView = emojiInputView;
        }

        public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
            viewGroup.removeView((View) this.mPanelEmojis.get(Integer.valueOf(i)));
        }

        @NonNull
        public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
            View view = (View) this.mPanelEmojis.get(Integer.valueOf(i));
            if (view == 0) {
                LinearLayout linearLayout = new LinearLayout(this.mContext);
                linearLayout.setGravity(17);
                linearLayout.setOrientation(1);
                int i2 = i * 20;
                int i3 = i2 + 20;
                LinearLayout linearLayout2 = null;
                while (i2 < i3) {
                    if (linearLayout2 == null || linearLayout2.getChildCount() == 7) {
                        linearLayout2 = new LinearLayout(this.mContext);
                        linearLayout2.setOrientation(0);
                        linearLayout2.setGravity(16);
                        linearLayout.addView(linearLayout2, new LayoutParams(-1, UIUtil.dip2px(this.mContext, 50.0f)));
                    }
                    ImageView imageView = new ImageView(this.mContext);
                    int dip2px = UIUtil.dip2px(this.mContext, 8.0f);
                    imageView.setPadding(dip2px, dip2px, dip2px, dip2px);
                    imageView.setScaleType(ScaleType.FIT_CENTER);
                    imageView.setBackgroundColor(0);
                    if (i2 < this.mEmojis.size()) {
                        EmojiIndex emojiIndex = (EmojiIndex) this.mEmojis.get(i2);
                        imageView.setImageResource(emojiIndex.getDrawResource());
                        imageView.setTag(emojiIndex);
                        imageView.setOnClickListener(this);
                    }
                    LayoutParams layoutParams = new LayoutParams(0, -1);
                    layoutParams.weight = 1.0f;
                    linearLayout2.addView(imageView, layoutParams);
                    i2++;
                }
                ImageView imageView2 = new ImageView(this.mContext);
                int dip2px2 = UIUtil.dip2px(this.mContext, 8.0f);
                imageView2.setPadding(dip2px2, dip2px2, dip2px2, dip2px2);
                imageView2.setScaleType(ScaleType.FIT_CENTER);
                imageView2.setBackgroundColor(0);
                imageView2.setImageResource(C4558R.C4559drawable.zm_mm_delete_btn);
                imageView2.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (EmojiAdapter.this.mEmojiInputView != null) {
                            EmojiAdapter.this.mEmojiInputView.onEmojiDelete();
                        }
                    }
                });
                LayoutParams layoutParams2 = new LayoutParams(0, -1);
                layoutParams2.weight = 1.0f;
                linearLayout2.addView(imageView2, layoutParams2);
                this.mPanelEmojis.put(Integer.valueOf(i), linearLayout);
                view = linearLayout;
            }
            viewGroup.addView(view);
            return view;
        }

        public int getCount() {
            List<EmojiIndex> list = this.mEmojis;
            if (list == null || list.size() == 0) {
                return 0;
            }
            return ((this.mEmojis.size() - 1) / 20) + 1;
        }

        public void onClick(@NonNull View view) {
            Object tag = view.getTag();
            if (tag instanceof EmojiIndex) {
                EmojiIndex emojiIndex = (EmojiIndex) tag;
                EmojiInputView emojiInputView = this.mEmojiInputView;
                if (emojiInputView != null) {
                    emojiInputView.onEmojiInput(emojiIndex);
                }
            }
        }
    }

    public EmojiInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public EmojiInputView(Context context) {
        super(context);
        init();
    }

    public void setKeyboardHeight(int i) {
        if (i != this.mKeyboardHeight) {
            PreferenceUtil.saveIntValue(PreferenceUtil.KEYBOARD_HEIGHT, i);
        }
        this.mKeyboardHeight = i;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int dip2px = UIUtil.dip2px(getContext(), 150.0f);
        if (this.mPanelIndicator.isShown()) {
            dip2px += this.mPanelIndicator.getLayoutParams().height;
        }
        if (getResources().getConfiguration().orientation == 1) {
            int i3 = this.mKeyboardHeight;
            if (dip2px <= i3) {
                dip2px = i3;
            }
        }
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(dip2px, Ints.MAX_POWER_OF_TWO));
    }

    public void setEmojiInputEditText(EditText editText) {
        this.mEmojiInputEditText = editText;
    }

    private void init() {
        setOrientation(1);
        this.mEmojiPager = new ZMViewPager(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, 0);
        layoutParams.weight = 1.0f;
        addView(this.mEmojiPager, layoutParams);
        this.mEmojiPager.setAdapter(new EmojiAdapter(getContext(), EmojiHelper.getInstance().getZMEmojis(), this));
        this.mPanelIndicator = new LinearLayout(getContext());
        this.mPanelIndicator.setGravity(17);
        addView(this.mPanelIndicator, new LayoutParams(-1, UIUtil.dip2px(getContext(), 10.0f)));
        this.mEmojiPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                EmojiInputView.this.refreshIndicator();
            }
        });
        refreshIndicator();
        this.mKeyboardHeight = PreferenceUtil.readIntValue(PreferenceUtil.KEYBOARD_HEIGHT, 0);
    }

    public void onEmojiDelete() {
        EditText editText = this.mEmojiInputEditText;
        if (editText != null) {
            editText.dispatchKeyEvent(new KeyEvent(0, 67));
        }
    }

    public void onEmojiInput(@Nullable EmojiIndex emojiIndex) {
        EditText editText = this.mEmojiInputEditText;
        if (editText != null && emojiIndex != null) {
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = this.mEmojiInputEditText.getSelectionEnd();
            Editable text = this.mEmojiInputEditText.getText();
            SpannableString spannableString = new SpannableString(emojiIndex.getShortCut());
            Drawable drawable = getResources().getDrawable(emojiIndex.getDrawResource());
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            spannableString.setSpan(new ImageSpan(drawable), 0, spannableString.length(), 33);
            text.replace(selectionStart, selectionEnd, spannableString);
        }
    }

    /* access modifiers changed from: private */
    public void refreshIndicator() {
        int currentItem = this.mEmojiPager.getCurrentItem();
        PagerAdapter adapter = this.mEmojiPager.getAdapter();
        if (adapter != null && currentItem < adapter.getCount()) {
            int count = adapter.getCount();
            this.mPanelIndicator.removeAllViews();
            if (count == 1) {
                this.mPanelIndicator.setVisibility(8);
                return;
            }
            this.mPanelIndicator.setVisibility(0);
            for (int i = 0; i < count; i++) {
                ImageView imageView = new ImageView(getContext());
                int i2 = C4558R.C4559drawable.zm_btn_switch_scene_selected_normal;
                if (i == currentItem) {
                    i2 = C4558R.C4559drawable.zm_btn_switch_scene_unselected_normal;
                }
                imageView.setImageResource(i2);
                int dip2px = UIUtil.dip2px(getContext(), 3.0f);
                imageView.setPadding(dip2px, 0, dip2px, 0);
                this.mPanelIndicator.addView(imageView);
            }
        }
    }
}
