package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ZMDomainUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.widget.ZMTextView */
public class ZMTextView extends TextView {
    public static final int SPAN_TYPE_AT_NAME_SPAN = 1;
    public static final int SPAN_TYPE_URL_SPAN = 0;
    private static final String TAG = "ZMTextView";
    @NonNull
    public static final List<String> mLinkKeywords = Arrays.asList(new String[]{ZMDomainUtil.ZM_URL_HTTP, "https://"});
    boolean dontConsumeNonUrlClicks = true;
    boolean linkHit;
    private boolean mAutoLink = false;
    private ForegroundColorSpan mForegroundColorSpan;
    private OnClickLinkListener mLinkListener;

    /* renamed from: us.zoom.androidlib.widget.ZMTextView$HighlightPos */
    static class HighlightPos {
        public int end;
        public int start;

        HighlightPos() {
        }
    }

    /* renamed from: us.zoom.androidlib.widget.ZMTextView$LocalLinkMovementMethod */
    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        private static final int LONG_TIME_DELAY = ViewConfiguration.getLongPressTimeout();
        private static final int TOUCH_SLOP = 20;
        static LocalLinkMovementMethod sInstance;
        /* access modifiers changed from: private */
        public CharSequence linkUrl;
        /* access modifiers changed from: private */
        @NonNull
        public Handler mHandler = new Handler();
        /* access modifiers changed from: private */
        public boolean mIsMoved = false;
        private long mLastClickTime;
        private float mLastX;
        private float mLastY;
        /* access modifiers changed from: private */
        @Nullable
        public Runnable mRunnable = new Runnable() {
            public void run() {
                if (LocalLinkMovementMethod.this.mRunnable != null) {
                    LocalLinkMovementMethod.this.mHandler.removeCallbacks(LocalLinkMovementMethod.this.mRunnable);
                }
                if (!LocalLinkMovementMethod.this.mIsMoved) {
                    if (!(LocalLinkMovementMethod.this.mTextView == null || LocalLinkMovementMethod.this.mTextView.get() == null)) {
                        try {
                            OnClickLinkListener onClickLinkListener = ((ZMTextView) LocalLinkMovementMethod.this.mTextView.get()).getOnClickLinkListener();
                            if (onClickLinkListener != null) {
                                if (TextUtils.isEmpty(LocalLinkMovementMethod.this.linkUrl) || TextUtils.equals(((TextView) LocalLinkMovementMethod.this.mTextView.get()).getText(), LocalLinkMovementMethod.this.linkUrl)) {
                                    onClickLinkListener.onLongClickWhole(((TextView) LocalLinkMovementMethod.this.mTextView.get()).getText().toString());
                                } else {
                                    onClickLinkListener.onLongClickLink(StringUtil.isEmptyOrNull(LocalLinkMovementMethod.this.url) ? LocalLinkMovementMethod.this.linkUrl.toString() : LocalLinkMovementMethod.this.url);
                                }
                            }
                        } catch (Exception unused) {
                        }
                    }
                    LocalLinkMovementMethod.this.linkUrl = null;
                    LocalLinkMovementMethod.this.url = null;
                    LocalLinkMovementMethod.this.mTextView = null;
                }
            }
        };
        /* access modifiers changed from: private */
        @Nullable
        public WeakReference<TextView> mTextView;
        /* access modifiers changed from: private */
        public String url;

        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null) {
                sInstance = new LocalLinkMovementMethod();
            }
            return sInstance;
        }

        public boolean onTouchEvent(TextView textView, @NonNull Spannable spannable, @NonNull MotionEvent motionEvent) {
            boolean z;
            try {
                int action = motionEvent.getAction();
                boolean z2 = textView instanceof ZMTextView ? ((ZMTextView) textView).getOnClickLinkListener() != null : false;
                if (z2) {
                    switch (action) {
                        case 0:
                            this.mLastClickTime = System.currentTimeMillis();
                            this.mLastX = motionEvent.getX();
                            this.mLastY = motionEvent.getY();
                            this.mTextView = new WeakReference<>(textView);
                            break;
                        case 1:
                        case 3:
                            if (this.mRunnable != null) {
                                this.mHandler.removeCallbacks(this.mRunnable);
                            }
                            this.mTextView = null;
                            this.mIsMoved = false;
                            break;
                        case 2:
                            if (!this.mIsMoved) {
                                if (Math.abs(this.mLastX - motionEvent.getX()) < 20.0f) {
                                    if (Math.abs(this.mLastY - motionEvent.getY()) < 20.0f) {
                                        z = false;
                                        this.mIsMoved = z;
                                        break;
                                    }
                                }
                                z = true;
                                this.mIsMoved = z;
                            }
                            break;
                    }
                } else if (action == 0) {
                    this.mLastClickTime = System.currentTimeMillis();
                }
                if (action != 1) {
                    if (action != 0) {
                        return Touch.onTouchEvent(textView, spannable, motionEvent);
                    }
                }
                int x = (((int) motionEvent.getX()) - textView.getTotalPaddingLeft()) + textView.getScrollX();
                int y = (((int) motionEvent.getY()) - textView.getTotalPaddingTop()) + textView.getScrollY();
                Layout layout = textView.getLayout();
                int offsetForHorizontal = layout.getOffsetForHorizontal(layout.getLineForVertical(y), (float) x);
                ClickableSpan[] clickableSpanArr = (ClickableSpan[]) spannable.getSpans(offsetForHorizontal, offsetForHorizontal, ClickableSpan.class);
                if (clickableSpanArr.length != 0) {
                    if (action != 1) {
                        Selection.setSelection(spannable, spannable.getSpanStart(clickableSpanArr[0]), spannable.getSpanEnd(clickableSpanArr[0]));
                        if (z2) {
                            if (!(clickableSpanArr[0] instanceof IZMSpan) || ((IZMSpan) clickableSpanArr[0]).getSpanType() != 1) {
                                this.linkUrl = spannable.subSequence(spannable.getSpanStart(clickableSpanArr[0]), spannable.getSpanEnd(clickableSpanArr[0]));
                            }
                            if (clickableSpanArr[0] instanceof URLSpan) {
                                this.url = ((URLSpan) clickableSpanArr[0]).getURL();
                            }
                            if (this.mRunnable != null) {
                                this.mHandler.postDelayed(this.mRunnable, (long) LONG_TIME_DELAY);
                            }
                        }
                    } else if (System.currentTimeMillis() - this.mLastClickTime < ((long) LONG_TIME_DELAY)) {
                        clickableSpanArr[0].onClick(textView);
                    }
                    if (textView instanceof ZMTextView) {
                        ((ZMTextView) textView).linkHit = true;
                    }
                    return true;
                }
                Selection.removeSelection(spannable);
                Touch.onTouchEvent(textView, spannable, motionEvent);
                return false;
            } catch (Exception unused) {
                return false;
            }
        }
    }

    /* renamed from: us.zoom.androidlib.widget.ZMTextView$OnClickLinkListener */
    public interface OnClickLinkListener {
        boolean onLongClickLink(String str);

        boolean onLongClickWhole(String str);
    }

    public boolean hasFocusable() {
        return false;
    }

    public ZMTextView(Context context) {
        super(context);
        initViews(context, null);
    }

    public ZMTextView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context, attributeSet);
    }

    public ZMTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context, attributeSet);
    }

    private void initViews(Context context, @Nullable AttributeSet attributeSet) {
        if (context != null) {
            if (attributeSet != null) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMTextView);
                this.mAutoLink = obtainStyledAttributes.getBoolean(C4409R.styleable.ZMTextView_showLink, false);
                obtainStyledAttributes.recycle();
            }
            this.mForegroundColorSpan = new ForegroundColorSpan(getResources().getColor(C4409R.color.zm_ui_kit_color_blue_0E71EB));
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.linkHit = false;
        return this.dontConsumeNonUrlClicks ? this.linkHit : super.onTouchEvent(motionEvent);
    }

    private int findLastIndex(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        int i = -1;
        for (String lastIndexOf : mLinkKeywords) {
            int lastIndexOf2 = str.lastIndexOf(lastIndexOf);
            if (lastIndexOf2 != -1) {
                if (i < 0) {
                    i = lastIndexOf2;
                } else if (lastIndexOf2 > i) {
                    i = lastIndexOf2;
                }
            }
        }
        return i;
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(highlightUrls(charSequence), bufferType);
    }

    public CharSequence highlightUrls(@Nullable CharSequence charSequence) {
        if (!this.mAutoLink || TextUtils.isEmpty(charSequence)) {
            return charSequence;
        }
        String lowerCase = charSequence.toString().toLowerCase();
        String[] split = lowerCase.split("[^\\x00-\\x7F]|[\n, \r]");
        if (split == null) {
            return charSequence;
        }
        ArrayList<HighlightPos> arrayList = new ArrayList<>();
        int i = 0;
        for (String str : split) {
            if (!TextUtils.isEmpty(str)) {
                int findLastIndex = findLastIndex(str);
                if (findLastIndex != -1) {
                    int indexOf = lowerCase.indexOf(str, i);
                    int length = str.length() + indexOf;
                    if (indexOf >= 0) {
                        HighlightPos highlightPos = new HighlightPos();
                        highlightPos.start = indexOf + findLastIndex;
                        highlightPos.end = length;
                        arrayList.add(highlightPos);
                        i = length;
                    }
                }
            }
        }
        String charSequence2 = charSequence.toString();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
        for (HighlightPos highlightPos2 : arrayList) {
            if (highlightPos2.start < highlightPos2.end) {
                spannableStringBuilder.setSpan(new URLSpan(toLowerLinkHeader(charSequence2.substring(highlightPos2.start, highlightPos2.end))), highlightPos2.start, highlightPos2.end, 33);
                ForegroundColorSpan foregroundColorSpan = this.mForegroundColorSpan;
                if (foregroundColorSpan != null) {
                    spannableStringBuilder.setSpan(foregroundColorSpan, highlightPos2.start, highlightPos2.end, 33);
                }
            }
        }
        return spannableStringBuilder;
    }

    public String toLowerLinkHeader(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        Iterator it = mLinkKeywords.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String str2 = (String) it.next();
            if (str.toLowerCase().startsWith(str2)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str.substring(str2.length()));
                str = sb.toString();
                break;
            }
        }
        return str;
    }

    public OnClickLinkListener getOnClickLinkListener() {
        return this.mLinkListener;
    }

    public void setOnClickLinkListener(OnClickLinkListener onClickLinkListener) {
        this.mLinkListener = onClickLinkListener;
    }
}
