package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.CaptionUtil;

/* renamed from: us.zoom.androidlib.widget.CaptionView */
public class CaptionView extends LinearLayout {
    private CaptionTextView mContent;
    private View mWindow;

    public CaptionView(Context context) {
        super(context);
        initView(context);
    }

    public CaptionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public CaptionView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    @SuppressLint({"NewApi"})
    public CaptionView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(getContext(), C4409R.layout.zm_caption_view, this);
        this.mContent = (CaptionTextView) findViewById(C4409R.C4411id.content);
        this.mWindow = findViewById(C4409R.C4411id.window);
    }

    private void refresh() {
        if (this.mWindow != null) {
            this.mWindow.setBackgroundColor(CaptionUtil.getCaptionStyle(getContext()).windowColor);
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(@NonNull View view, int i) {
        super.onVisibilityChanged(view, i);
        if (!isInEditMode()) {
            refresh();
        }
    }

    public void setText(String str) {
        CaptionTextView captionTextView = this.mContent;
        if (captionTextView != null) {
            captionTextView.setText(str);
        }
    }
}
