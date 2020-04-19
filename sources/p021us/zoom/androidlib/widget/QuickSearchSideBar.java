package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.QuickSearchSideBar */
public class QuickSearchSideBar extends LinearLayout implements OnTouchListener, OnClickListener {
    private static final String CATEGORY_CHARS_DEFAULT = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DISPLAY_CHARS_FULL_DEFAULT = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DISPLAY_CHARS_LARGE_DEFAULT = "#AB.IJK.RST.Z";
    private static final String DISPLAY_CHARS_MEDIUM_DEFAULT = "#A.IJ.RS.Z";
    private static final String DISPLAY_CHARS_SMALL_DEFAULT = "#A.I.R.Z";
    private String mCategoryChars = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String mDisplayCharsFull = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String mDisplayCharsLarge = DISPLAY_CHARS_LARGE_DEFAULT;
    private String mDisplayCharsMedium = DISPLAY_CHARS_MEDIUM_DEFAULT;
    private String mDisplayCharsSmall = DISPLAY_CHARS_SMALL_DEFAULT;
    /* access modifiers changed from: private */
    public char mLastChar = 0;
    /* access modifiers changed from: private */
    public IListener mListener = null;
    private float mMaxTextSize = 0.0f;

    /* renamed from: us.zoom.androidlib.widget.QuickSearchSideBar$IListener */
    public interface IListener {
        void onQuickSearchCharPressed(char c);

        void onQuickSearchCharReleased(char c);
    }

    public void onClick(View view) {
    }

    public QuickSearchSideBar(Context context) {
        super(context);
        initView(context);
    }

    public QuickSearchSideBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public QuickSearchSideBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public void setCategoryChars(String str, String str2, String str3, String str4, String str5) {
        if (str == null || str2 == null || str3 == null || str4 == null || str5 == null) {
            throw new NullPointerException("at least one of arguments is null");
        } else if (str.length() == str2.length()) {
            this.mCategoryChars = str;
            this.mDisplayCharsFull = str2;
            this.mDisplayCharsLarge = str3;
            this.mDisplayCharsMedium = str4;
            this.mDisplayCharsSmall = str5;
            prebuildCharViews(getContext());
        } else {
            throw new IllegalArgumentException("length of categoryChars and displayCharsFullSize do not match");
        }
    }

    public String getCategoryChars() {
        return this.mCategoryChars;
    }

    public String getDisplayCharsFullSize() {
        return this.mDisplayCharsFull;
    }

    private void initView(Context context) {
        setOrientation(1);
        setGravity(1);
        setBackgroundResource(C4409R.C4410drawable.zm_quick_search_sidebar);
        prebuildCharViews(context);
        if (!AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            setOnTouchListener(this);
            setOnClickListener(this);
        }
    }

    private void prebuildCharViews(Context context) {
        if (getChildCount() < this.mCategoryChars.length()) {
            LayoutParams layoutParams = new LayoutParams(-1, 0);
            layoutParams.weight = 1.0f;
            String str = this.mCategoryChars;
            TextView textView = null;
            int childCount = getChildCount();
            while (childCount < str.length()) {
                char charAt = str.charAt(childCount);
                C44801 r4 = new TextView(context) {
                    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
                        if (accessibilityEvent.getEventType() == 32768) {
                            QuickSearchSideBar.this.mListener.onQuickSearchCharReleased(getText().charAt(0));
                            QuickSearchSideBar.this.mLastChar = 0;
                        }
                        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
                    }
                };
                r4.setText(String.valueOf(charAt));
                r4.setTag(String.valueOf(charAt));
                r4.setTextColor(ContextCompat.getColor(getContext(), C4409R.color.zm_ui_kit_color_blue_0E71EB));
                r4.setGravity(17);
                r4.setTextSize(11.0f);
                addView(r4, layoutParams);
                childCount++;
                textView = r4;
            }
            if (textView != null) {
                this.mMaxTextSize = textView.getTextSize();
            }
        }
    }

    public void setQuickSearchSideBarListener(IListener iListener) {
        this.mListener = iListener;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i2);
        float updateTagsVisibility = (float) ((size / updateTagsVisibility(size)) - 4);
        float f = this.mMaxTextSize;
        if (updateTagsVisibility > f) {
            updateTagsVisibility = f;
        }
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            ((TextView) getChildAt(i3)).setTextSize(0, updateTagsVisibility);
        }
        super.onMeasure(i, i2);
    }

    private int updateTagsVisibility(int i) {
        float px2dip = UIUtil.px2dip(getContext(), i);
        String str = this.mDisplayCharsFull;
        if (!AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            if (px2dip < 100.0f) {
                str = this.mDisplayCharsSmall;
            }
            if (px2dip < 180.0f) {
                str = this.mDisplayCharsMedium;
            } else if (px2dip < 300.0f) {
                str = this.mDisplayCharsLarge;
            }
        }
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            TextView textView = (TextView) getChildAt(i2);
            if (i2 < str.length()) {
                String valueOf = String.valueOf(str.charAt(i2));
                textView.setTag(valueOf);
                textView.setText(valueOf);
                textView.setVisibility(0);
                textView.setContentDescription(getResources().getString(C4409R.string.zm_accessibility_quick_bar_section_22859, new Object[]{valueOf}));
            } else {
                textView.setVisibility(8);
            }
        }
        return str.length();
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (this.mListener == null) {
            return true;
        }
        int height = getHeight();
        int width = getWidth();
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (x < 0.0f || x > ((float) width) || y < 0.0f || y > ((float) height)) {
            char c = this.mLastChar;
            if (c != 0) {
                this.mListener.onQuickSearchCharReleased(c);
                this.mLastChar = 0;
                return true;
            }
        }
        int length = this.mCategoryChars.length();
        int i = ((int) y) / (height / length);
        if (i < 0) {
            i = 0;
        }
        if (i >= length) {
            i = length - 1;
        }
        char charAt = this.mCategoryChars.charAt(i);
        if (motionEvent.getAction() == 1) {
            this.mListener.onQuickSearchCharReleased(charAt);
            this.mLastChar = 0;
        } else if (motionEvent.getAction() == 0) {
            this.mListener.onQuickSearchCharPressed(charAt);
            this.mLastChar = charAt;
        } else if (motionEvent.getAction() == 2) {
            char c2 = this.mLastChar;
            if (!(c2 == 0 || charAt == c2)) {
                this.mListener.onQuickSearchCharPressed(charAt);
                this.mLastChar = charAt;
            }
        }
        return true;
    }
}
