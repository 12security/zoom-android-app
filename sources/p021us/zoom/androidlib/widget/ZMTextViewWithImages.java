package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* renamed from: us.zoom.androidlib.widget.ZMTextViewWithImages */
public class ZMTextViewWithImages extends TextView {
    private static final Factory spannableFactory = Factory.getInstance();

    public ZMTextViewWithImages(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ZMTextViewWithImages(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMTextViewWithImages(Context context) {
        super(context);
    }

    public void setText(CharSequence charSequence, BufferType bufferType) {
        super.setText(getTextWithImages(getContext(), charSequence), BufferType.SPANNABLE);
    }

    private static boolean addImages(Context context, Spannable spannable) {
        boolean z;
        Matcher matcher = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9_]+?)\\Q/]\\E").matcher(spannable);
        boolean z2 = false;
        while (matcher.find()) {
            ImageSpan[] imageSpanArr = (ImageSpan[]) spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class);
            int length = imageSpanArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = true;
                    break;
                }
                ImageSpan imageSpan = imageSpanArr[i];
                if (spannable.getSpanStart(imageSpan) < matcher.start() || spannable.getSpanEnd(imageSpan) > matcher.end()) {
                    z = false;
                } else {
                    spannable.removeSpan(imageSpan);
                    i++;
                }
            }
            z = false;
            int identifier = context.getResources().getIdentifier(spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim(), "drawable", context.getPackageName());
            if (z) {
                spannable.setSpan(new ImageSpan(context, identifier), matcher.start(), matcher.end(), 33);
                z2 = true;
            }
        }
        return z2;
    }

    private static Spannable getTextWithImages(Context context, CharSequence charSequence) {
        Spannable newSpannable = spannableFactory.newSpannable(charSequence);
        addImages(context, newSpannable);
        return newSpannable;
    }
}
