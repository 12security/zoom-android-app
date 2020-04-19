package p021us.zoom.androidlib.util;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.util.ZMHtmlUtil */
public class ZMHtmlUtil {

    /* renamed from: us.zoom.androidlib.util.ZMHtmlUtil$OnURLSpanClickListener */
    public interface OnURLSpanClickListener {
        void onClick(View view, String str, String str2);
    }

    /* renamed from: us.zoom.androidlib.util.ZMHtmlUtil$URLSpanToWebView */
    public static abstract class URLSpanToWebView extends ClickableSpan {
        private String title;
        private String url;

        URLSpanToWebView(String str, String str2) {
            this.url = str;
            this.title = str2;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }
    }

    @NonNull
    public static CharSequence fromHtml(final Context context, @NonNull String str, @NonNull final OnURLSpanClickListener onURLSpanClickListener) {
        Spanned fromHtml = Html.fromHtml(str);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(fromHtml);
        if (fromHtml instanceof Spannable) {
            Spannable spannable = (Spannable) fromHtml;
            URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, fromHtml.length(), URLSpan.class);
            spannableStringBuilder.clearSpans();
            for (URLSpan uRLSpan : uRLSpanArr) {
                int spanStart = spannable.getSpanStart(uRLSpan);
                int spanEnd = spannable.getSpanEnd(uRLSpan);
                if (spanStart == -1 || spanEnd == -1 || spanStart > spanEnd) {
                    return fromHtml;
                }
                spannableStringBuilder.setSpan(new URLSpanToWebView(uRLSpan.getURL(), spannableStringBuilder.subSequence(spanStart, spanEnd).toString()) {
                    public void onClick(@NonNull View view) {
                        OnURLSpanClickListener onURLSpanClickListener = onURLSpanClickListener;
                        if (onURLSpanClickListener != null) {
                            onURLSpanClickListener.onClick(view, getUrl(), getTitle());
                        }
                    }

                    public void updateDrawState(@NonNull TextPaint textPaint) {
                        Context context = context;
                        textPaint.setColor(context == null ? -16227631 : context.getResources().getColor(C4409R.color.zm_ui_kit_color_blue_0862D1));
                        textPaint.setUnderlineText(false);
                    }
                }, spanStart, spanEnd, 34);
            }
        }
        return spannableStringBuilder;
    }
}
