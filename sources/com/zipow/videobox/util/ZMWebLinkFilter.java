package com.zipow.videobox.util;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ZMWebLinkFilter {
    @NonNull
    private static String[] mFilter = new String[0];

    public static void filter(TextView textView) {
        filter(textView, mFilter);
    }

    public static void filter(@Nullable TextView textView, @Nullable String[] strArr) {
        if (textView != null && strArr != null && !TextUtils.isEmpty(textView.getText()) && strArr.length > 0) {
            CharSequence text = textView.getText();
            if (text instanceof SpannableString) {
                filter((SpannableString) text, strArr);
            }
        }
    }

    private static void filter(@Nullable SpannableString spannableString, @Nullable String[] strArr) {
        if (!TextUtils.isEmpty(spannableString) && strArr != null && strArr.length > 0) {
            URLSpan[] uRLSpanArr = (URLSpan[]) spannableString.getSpans(0, spannableString.length(), URLSpan.class);
            if (uRLSpanArr != null && uRLSpanArr.length > 0) {
                for (URLSpan uRLSpan : uRLSpanArr) {
                    if (matchFilter(spannableString, uRLSpan, strArr)) {
                        spannableString.removeSpan(uRLSpan);
                    }
                }
            }
        }
    }

    private static boolean matchFilter(@Nullable SpannableString spannableString, @Nullable URLSpan uRLSpan, @Nullable String[] strArr) {
        if (TextUtils.isEmpty(spannableString) || uRLSpan == null || strArr == null || strArr.length <= 0) {
            return false;
        }
        int spanStart = spannableString.getSpanStart(uRLSpan);
        int spanEnd = spannableString.getSpanEnd(uRLSpan);
        if (spanEnd <= spanStart || spanStart - spanEnd > spannableString.length()) {
            return false;
        }
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                String charSequence = spannableString.subSequence(spanStart, spanEnd).toString();
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() >= str.length() && str.equalsIgnoreCase(charSequence.substring(0, str.length()))) {
                    return true;
                }
                if (spanStart < str.length()) {
                    continue;
                } else {
                    String charSequence2 = spannableString.subSequence(spanStart - str.length(), spanStart).toString();
                    if (!TextUtils.isEmpty(charSequence2) && str.equalsIgnoreCase(charSequence2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
