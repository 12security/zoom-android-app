package com.zipow.videobox.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZMLinkPatterns {
    public static final Pattern DOMAIN_NAME;
    private static final String GOOD_GTLD_CHAR = "a-zA-Z -퟿豈-﷏ﷰ-￯";
    public static final String GOOD_IRI_CHAR = "a-zA-Z0-9 -퟿豈-﷏ﷰ-￯";
    private static final String GTLD = "[a-zA-Z -퟿豈-﷏ﷰ-￯]{2,63}";
    private static final String HOST_NAME = "([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\-]{0,61}[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]){0,1}\\.)+[a-zA-Z -퟿豈-﷏ﷰ-￯]{2,63}";
    public static final Pattern IP_ADDRESS = Pattern.compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9]))");
    private static final String IRI = "[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\-]{0,61}[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]){0,1}";
    public static final Pattern WEB_URL;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("(([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]([a-zA-Z0-9 -퟿豈-﷏ﷰ-￯\\-]{0,61}[a-zA-Z0-9 -퟿豈-﷏ﷰ-￯]){0,1}\\.)+[a-zA-Z -퟿豈-﷏ﷰ-￯]{2,63}|");
        sb.append(IP_ADDRESS);
        sb.append(")");
        DOMAIN_NAME = Pattern.compile(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?(?:");
        sb2.append(DOMAIN_NAME);
        sb2.append(")(?:\\:\\d{1,5})?)(\\/(?:(?:[");
        sb2.append(GOOD_IRI_CHAR);
        sb2.append("\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)");
        WEB_URL = Pattern.compile(sb2.toString());
    }

    public static void hookWebPage(@Nullable TextView textView) {
        if (textView != null && !TextUtils.isEmpty(textView.getText())) {
            CharSequence text = textView.getText();
            if (!(text instanceof Spannable)) {
                SpannableString valueOf = SpannableString.valueOf(text);
                if (addLinks(valueOf)) {
                    addLinkMovementMethod(textView);
                    textView.setText(valueOf);
                }
            } else if (addLinks((Spannable) text)) {
                addLinkMovementMethod(textView);
            }
        }
    }

    public static final boolean addLinks(Spannable spannable) {
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (int length = uRLSpanArr.length - 1; length >= 0; length--) {
            spannable.removeSpan(uRLSpanArr[length]);
        }
        ArrayList arrayList = new ArrayList();
        Matcher matcher = WEB_URL.matcher(spannable);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            LinkSpec linkSpec = new LinkSpec();
            linkSpec.url = makeUrl(matcher.group(0), new String[]{ZMDomainUtil.ZM_URL_HTTP, "https://", "rtsp://"}, matcher);
            linkSpec.start = start;
            linkSpec.end = end;
            arrayList.add(linkSpec);
        }
        pruneOverlaps(arrayList);
        if (arrayList.size() == 0) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            LinkSpec linkSpec2 = (LinkSpec) it.next();
            applyLink(linkSpec2.url, linkSpec2.start, linkSpec2.end, spannable);
        }
        return true;
    }

    @NonNull
    private static final String makeUrl(@NonNull String str, String[] strArr, Matcher matcher) {
        boolean z;
        int i = 0;
        while (true) {
            z = true;
            if (i >= strArr.length) {
                z = false;
                break;
            }
            if (str.regionMatches(true, 0, strArr[i], 0, strArr[i].length())) {
                if (!str.regionMatches(false, 0, strArr[i], 0, strArr[i].length())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(strArr[i]);
                    sb.append(str.substring(strArr[i].length()));
                    str = sb.toString();
                }
            } else {
                i++;
            }
        }
        if (z) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(strArr[0]);
        sb2.append(str);
        return sb2.toString();
    }

    private static void addLinkMovementMethod(TextView textView) {
        if (!(textView.getMovementMethod() instanceof LinkMovementMethod) && textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private static final void applyLink(String str, int i, int i2, Spannable spannable) {
        spannable.setSpan(new URLSpan(str), i, i2, 33);
    }

    private static final void pruneOverlaps(@NonNull ArrayList<LinkSpec> arrayList) {
        Collections.sort(arrayList, new Comparator<LinkSpec>() {
            public final int compare(LinkSpec linkSpec, LinkSpec linkSpec2) {
                if (linkSpec.start < linkSpec2.start) {
                    return -1;
                }
                if (linkSpec.start > linkSpec2.start || linkSpec.end < linkSpec2.end) {
                    return 1;
                }
                if (linkSpec.end > linkSpec2.end) {
                    return -1;
                }
                return 0;
            }
        });
        int size = arrayList.size();
        int i = 0;
        while (i < size - 1) {
            LinkSpec linkSpec = (LinkSpec) arrayList.get(i);
            int i2 = i + 1;
            LinkSpec linkSpec2 = (LinkSpec) arrayList.get(i2);
            if (linkSpec.start <= linkSpec2.start && linkSpec.end > linkSpec2.start) {
                int i3 = linkSpec2.end <= linkSpec.end ? i2 : linkSpec.end - linkSpec.start > linkSpec2.end - linkSpec2.start ? i2 : linkSpec.end - linkSpec.start < linkSpec2.end - linkSpec2.start ? i : -1;
                if (i3 != -1) {
                    arrayList.remove(i3);
                    size--;
                }
            }
            i = i2;
        }
    }
}
