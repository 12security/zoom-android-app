package p021us.zoom.androidlib.widget;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import androidx.annotation.NonNull;

/* renamed from: us.zoom.androidlib.widget.ZMSpanny */
public class ZMSpanny extends SpannableStringBuilder {
    private int flag = 33;

    public ZMSpanny() {
        super("");
    }

    public ZMSpanny(CharSequence charSequence) {
        super(charSequence);
    }

    public ZMSpanny(CharSequence charSequence, Object... objArr) {
        super(charSequence);
        for (Object span : objArr) {
            setSpan(span, 0, length());
        }
    }

    public ZMSpanny(CharSequence charSequence, Object obj) {
        super(charSequence);
        setSpan(obj, 0, charSequence.length());
    }

    public ZMSpanny append(CharSequence charSequence, Object... objArr) {
        append(charSequence);
        for (Object span : objArr) {
            setSpan(span, length() - charSequence.length(), length());
        }
        return this;
    }

    public ZMSpanny append(CharSequence charSequence, Object obj) {
        append(charSequence);
        setSpan(obj, length() - charSequence.length(), length());
        return this;
    }

    public ZMSpanny append(CharSequence charSequence, ImageSpan imageSpan) {
        StringBuilder sb = new StringBuilder();
        sb.append(".");
        sb.append(charSequence);
        String sb2 = sb.toString();
        append((CharSequence) sb2);
        setSpan(imageSpan, length() - sb2.length(), (length() - sb2.length()) + 1);
        return this;
    }

    @NonNull
    public ZMSpanny append(CharSequence charSequence) {
        super.append(charSequence);
        return this;
    }

    public void setFlag(int i) {
        this.flag = i;
    }

    private void setSpan(Object obj, int i, int i2) {
        setSpan(obj, i, i2, this.flag);
    }

    public ZMSpanny setSpans(CharSequence charSequence, CharacterStyle... characterStyleArr) {
        if (characterStyleArr == null || characterStyleArr.length == 0) {
            return this;
        }
        int i = 0;
        while (i != -1) {
            i = toString().indexOf(charSequence.toString(), i);
            if (i != -1) {
                for (CharacterStyle span : characterStyleArr) {
                    setSpan(span, i, charSequence.length() + i);
                }
                i += charSequence.length();
            }
        }
        return this;
    }

    public static SpannableString spanText(CharSequence charSequence, Object... objArr) {
        SpannableString spannableString = new SpannableString(charSequence);
        for (Object span : objArr) {
            spannableString.setSpan(span, 0, charSequence.length(), 33);
        }
        return spannableString;
    }

    public static SpannableString spanText(CharSequence charSequence, Object obj) {
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(obj, 0, charSequence.length(), 33);
        return spannableString;
    }
}
